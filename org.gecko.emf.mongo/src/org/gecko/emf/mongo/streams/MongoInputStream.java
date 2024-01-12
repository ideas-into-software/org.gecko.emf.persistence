/*******************************************************************************
 * Copyright (c) 2011 Bryan Hunt & Ed Merks.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt & Ed Merks - initial API and implementation
 *    Data In Motion Consulting GmbH
 *******************************************************************************/
package org.gecko.emf.mongo.streams;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.gecko.collection.CollectionFactory;
import org.gecko.collection.EReferenceCollection;
import org.gecko.emf.mongo.ConverterService;
import org.gecko.emf.mongo.InputContentHandler;
import org.gecko.emf.mongo.Keywords;
import org.gecko.emf.mongo.MongoUtils;
import org.gecko.emf.mongo.Options;
import org.gecko.emf.mongo.QueryEngine;
import org.gecko.emf.mongo.codecs.EObjectCodecProvider;
import org.gecko.emf.mongo.model.EMongoQuery;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

/**
 * Input stream implementation that handles loading of {@link Resource}
 * @author bhunt
 * @author Mark Hoffmann
 */
public class MongoInputStream extends InputStream implements URIConverter.Loadable {

	private final ConverterService converterService;
	private URI uri;
	private Map<Object, Object> mergedOptions = new HashMap<>();
	private QueryEngine queryEngine;
	private MongoCollection<Document> collection;
	private List<InputContentHandler> contentHandler;
	private Map<Object, Object> response;

	public MongoInputStream(ConverterService converterService, QueryEngine queryEngine, MongoCollection<Document> collection, List<InputContentHandler> contentHandler, URI uri, Map<?, ?> options, Map<Object, Object> response) throws IOException {
		this.response = response;
		if (converterService == null)
			throw new NullPointerException("The converter service must not be null");
		this.converterService = converterService;
		if (collection == null)
			throw new NullPointerException("The database collection must not be null");

		this.contentHandler = contentHandler == null ? Collections.emptyList() : contentHandler;
		this.queryEngine = queryEngine;
		this.collection = collection;
		this.uri = uri;
		normalizeOptions(options);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.URIConverter.Loadable#loadResource(org.eclipse.emf.ecore.resource.Resource)
	 */
	@Override
	public void loadResource(Resource resource) throws IOException {

		boolean needCache = true;

		// determine the input content handler
		Optional<InputContentHandler> handlerOptional = contentHandler.stream()
				.filter((ch)->ch.canHandle((Map<Object, Object>) mergedOptions))
				.findFirst();
		if (handlerOptional.isPresent()) {
			needCache = handlerOptional.get().enableResourceCache((Map<Object, Object>) mergedOptions);
		}

		// We need to set up the XMLResource.URIHandler so that proxy URIs are handled properly.EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet);
		final List<Resource> resourcesCache;
		if(needCache){
			resourcesCache = new ArrayList<>(resource.getContents().size());
		} else {
			resourcesCache = null;
		}
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resource, mergedOptions, resourcesCache);
		codecProvider.setConverterService(converterService);
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it
		MongoCollection<EObject> curCollection = collection.withCodecRegistry(codecRegistry).withDocumentClass(EObject.class);


		// If the URI contains a query string, use it to locate a collection of objects from
		// MongoDB, otherwise simply get the object from MongoDB using the id.

		EList<EObject> contents = resource.getContents();
		
		if (uri.query() != null && !isProjectionOnly(uri.query())) {
			if (queryEngine == null) {
				throw new IOException("The query engine was not found");
			}

			EMongoQuery mongoQuery = queryEngine.buildMongoQuery(uri, mergedOptions);
			FindIterable<EObject> resultIterable = null;

//			NOW done in the NativeQueryEngine
//			if (options.containsKey(Options.OPTION_BATCH_SIZE)) {
//				Object size = options.get(Options.OPTION_BATCH_SIZE);
//				if (size != null && size instanceof Integer) {
//					mongoQuery.setBatchSize((Integer)size);
//				}
//			}
			
			boolean countResults = false;
			Object optionCountResult = mergedOptions.get(Options.OPTION_COUNT_RESULT);
			countResults = optionCountResult != null && Boolean.TRUE.equals(optionCountResult);

			Document filter = mongoQuery.getFilter();
			Document projection = mongoQuery.getProjection();

			long elementCount = -1l;
			if (filter != null) {
				resultIterable = curCollection.find(filter);
				if (countResults) {
					elementCount = curCollection.countDocuments(filter);
				}
			} else {
				resultIterable = curCollection.find();
				if (countResults) {
					elementCount = curCollection.countDocuments();
				}
			}
			if (countResults) {
				response.put(Options.OPTION_COUNT_RESPONSE, Long.valueOf(elementCount));
			}
			

			if (projection != null) {
				resultIterable.projection(projection);
			}

			if (mongoQuery.getSkip() != null && mongoQuery.getSkip() > 0)
				resultIterable.skip(mongoQuery.getSkip());

			if (mongoQuery.getSort() != null)
				resultIterable = resultIterable.sort(mongoQuery.getSort());

			if (mongoQuery.getLimit() != null && mongoQuery.getLimit() > 0)
				resultIterable = resultIterable.limit(mongoQuery.getLimit());

			if (mongoQuery.getBatchSize() != null && mongoQuery.getBatchSize() > 0) {
				resultIterable.batchSize(mongoQuery.getBatchSize().intValue());
			}

			final FindIterable<EObject> iterable = resultIterable;
			
			handlerOptional.ifPresent((ich)->{
				EObject result = ich.createContent(iterable, (Map<Object, Object>) mergedOptions, resourcesCache);
				if (result != null) {
					contents.add(result);
				}
			});

			if (!handlerOptional.isPresent()) {

				EReferenceCollection eCollection = CollectionFactory.eINSTANCE.createEReferenceCollection();
				InternalEList<EObject> values = (InternalEList<EObject>) eCollection.getValues();
				try(MongoCursor<EObject> mongoCursor = resultIterable.iterator()){
					while (mongoCursor.hasNext()){
						EObject dbObject = mongoCursor.next();
						if(Boolean.TRUE.equals(mergedOptions.get(Options.OPTION_LAZY_RESULT_LOADING))){
							((InternalEObject) dbObject).eSetProxyURI(EcoreUtil.getURI(dbObject).appendQuery(null));
							detachEObject(dbObject);
						}
						if (Boolean.TRUE.equals(mergedOptions.get(Options.OPTION_READ_DETACHED))) {
							detachEObject(dbObject);
						}
						values.addUnique(dbObject);
					}
				}

				contents.add(eCollection);
			}
			if(!Boolean.TRUE.equals(mergedOptions.get(Options.OPTION_LAZY_RESULT_LOADING)) && needCache){
				resource.getResourceSet().getResources().addAll(resourcesCache);
			}
		} else {
			
			FindIterable<EObject> find = curCollection.find(new Document(Keywords.ID_KEY, MongoUtils.getID(uri)), EObject.class);
			
			if(uri.query() != null) {
				if (queryEngine == null) {
					throw new IOException("The query engine was not found");
				}

				EMongoQuery mongoQuery = queryEngine.buildMongoQuery(uri, mergedOptions);
				Document projectionOnly = mongoQuery.getProjectionOnly();
				if (projectionOnly != null) {
					find = find.projection(projectionOnly);
				}
				
			}
			EObject dbObject = find.first();

			if (dbObject != null) {
				contents.add(dbObject);
			}
		}
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		// InputStream requires that we implement this function. It will never be called
		// since this implementation implements URIConverter.Loadable. The loadResource()
		// function will be called instead.
		return -1;
	}
	
	/**
	 * Normalizes the load options
	 * @param options the original options
	 */
	private <K extends Object, V extends Object> void normalizeOptions(Map<K, V> options) {
		mergedOptions.putAll(options);
		EClass filterEClass = (EClass) options.getOrDefault(Options.OPTION_FILTER_ECLASS, null);
		EClass collectionEClass = Options.getCollectionEClass(options);
		if (collectionEClass != null && filterEClass == null) {
			mergedOptions.put(Options.OPTION_FILTER_ECLASS, collectionEClass);
		}
	}

	/**
	 * @param query
	 * @return
	 */
	private boolean isProjectionOnly(String query) {
		Document d = Document.parse(URI.decode(query));
		return d.containsKey("projectionOnly");
	}

	/**
	 * Detaches the given {@link EObject}
	 * @param eobject the eobject instance
	 */
	private void detachEObject(EObject eobject) {
		if (eobject == null) {
			return;
		}
		Resource resource = eobject.eResource();
		if (resource != null) {
			resource.getContents().clear();
			if(resource.getResourceSet() != null){
				resource.getResourceSet().getResources().remove(resource);
			}
		}
	}

}
