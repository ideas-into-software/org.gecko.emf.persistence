/*******************************************************************************
 * Copyright (c) 2012 Bryan Hunt.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 *******************************************************************************/

package org.gecko.emf.mongo.streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bson.Document;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.gecko.emf.mongo.ConverterService;
import org.gecko.emf.mongo.InputContentHandler;
import org.gecko.emf.mongo.InputStreamFactory;
import org.gecko.emf.mongo.Keywords;
import org.gecko.emf.mongo.MongoUtils;
import org.gecko.emf.mongo.Options;
import org.gecko.emf.mongo.OutputStreamFactory;
import org.gecko.emf.mongo.QueryEngine;
import org.gecko.emf.mongo.model.EMongoQuery;
import org.gecko.mongo.osgi.MongoIdFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

/**
 * @author bhunt
 * 
 */
@Component(name="DefaultStreamFactory", immediate=true, service= {InputStreamFactory.class, OutputStreamFactory.class})
public class DefaultStreamFactory implements InputStreamFactory, OutputStreamFactory {
	
	private QueryEngine queryEngine;
	private ConverterService converterService;
	private volatile Map<String, MongoIdFactory> idFactories = new ConcurrentHashMap<>();
	private volatile List<InputContentHandler> handlerList = new CopyOnWriteArrayList<>();
	private Map<Object, Object> mergedOptions = new HashMap<>();
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.OutputStreamFactory#createOutputStream(org.eclipse.emf.common.util.URI, java.util.Map, com.mongodb.client.MongoCollection, java.util.Map)
	 */
	@Override
	public OutputStream createOutputStream(URI uri, Map<?, ?> options, MongoCollection<Document> collection, Map<Object, Object> response) {
		return new MongoOutputStream(converterService, collection, uri, idFactories, options, response);
	}

	/* (non-Javadoc)
	 * @see org.gecko.emf.mongo.InputStreamFactory#createInputStream(org.eclipse.emf.common.util.URI, java.util.Map, com.mongodb.client.MongoCollection, java.util.Map)
	 */
	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options, MongoCollection<Document> collection, Map<Object, Object> response) throws IOException {
		return new MongoInputStream(converterService, queryEngine, collection, handlerList, uri, options, response);
	}

	/* 
		 * (non-Javadoc)
		 * @see org.gecko.emf.mongo.InputStreamFactory#createDeleteRequest(org.eclipse.emf.common.util.URI, java.util.Map, com.mongodb.client.MongoCollection, java.util.Map)
		 */
		@Override
		public void createDeleteRequest(URI uri, Map<?, ?> options, MongoCollection<Document> collection,
				Map<Object, Object> response) throws IOException {
			normalizeOptions(options);
			boolean countResults = false;
			Object optionCountResult = mergedOptions.get(Options.OPTION_COUNT_RESULT);
			long elementCount = -1l;
			countResults = optionCountResult != null && Boolean.TRUE.equals(optionCountResult);
			DeleteResult deleteResult = null;
			
			if (uri.query() != null) {
				if (queryEngine == null) {
					throw new IOException("The query engine was not found");
				}
	
				EMongoQuery mongoQuery = queryEngine.buildMongoQuery(uri, mergedOptions);
	
				Document filter = mongoQuery.getFilter();
	
				if (filter != null) {
					deleteResult = collection.deleteMany(filter);
					if (countResults) {
						elementCount = deleteResult.getDeletedCount();
					}
				} else {
					deleteResult = collection.deleteOne(new BasicDBObject(Keywords.ID_KEY, MongoUtils.getID(uri)));
					if (countResults) {
						elementCount = deleteResult.getDeletedCount();
					}
				}
				if (countResults) {
					response.put(Options.OPTION_COUNT_RESPONSE, Long.valueOf(elementCount));
				}
				
			} else {
				deleteResult = collection.deleteOne(new BasicDBObject(Keywords.ID_KEY, MongoUtils.getID(uri)));
				if (countResults) {
					elementCount = deleteResult.getDeletedCount();
					response.put(Options.OPTION_COUNT_RESPONSE, Long.valueOf(elementCount));
				}
			}
		}

	/**
	 * Sets the converter service
	 * @param converterService the converter service to set
	 */
	@Reference(name="ConverterService")
	public void setConverterService(ConverterService converterService) {
		this.converterService = converterService;
	}

	/**
	 * Sets the query engine
	 * @param queryEngine the query engine to set
	 */
	@Reference(name="QueryEngine")
	public void setQueryEngine(QueryEngine queryEngine) {
		this.queryEngine = queryEngine;
	}
	
	/**
	 * Sets the id factory 
	 * @param mongoIdFactory the id factory to be added
	 */
	@Reference(name="MongoIdFactory", unbind="removeMongoIdFactory")
	public void addMongoIdFactory(MongoIdFactory mongoIdFactory) {
		idFactories.put(mongoIdFactory.getCollectionURI(), mongoIdFactory);
	}

	/**
	 * Un-sets the id factory 
	 * @param mongoIdFactory the id factory to be removed
	 */
	public void removeMongoIdFactory(MongoIdFactory mongoIdFactory) {
		MongoIdFactory target = idFactories.get(mongoIdFactory.getCollectionURI());
		if (mongoIdFactory == target)
			idFactories.remove(mongoIdFactory.getCollectionURI());
	}
	
	/**
	 * Sets an {@link InputContentHandler} to be used
	 * @param contentHandler the id factory to be added
	 */
	@Reference(name="InputHandler", policy=ReferencePolicy.DYNAMIC, cardinality=ReferenceCardinality.MULTIPLE, unbind="removeInputHandler")
	public void addInputHandler(InputContentHandler contentHandler) {
		handlerList.add(contentHandler);
	}
	
	/**
	 * Un-sets an {@link InputContentHandler} to be used
	 * @param contentHandler the content handler to be removed
	 */
	public void removeInputHandler(InputContentHandler contentHandler) {
		handlerList.remove(contentHandler);
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

}
