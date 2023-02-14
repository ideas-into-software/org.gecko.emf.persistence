/**
 * Copyright (c) 2012 - 2022 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Byan Hunt -  initial API and implementation
 *     Ed Merks - initial API and implementation
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.mongo.streams;

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
import org.bson.conversions.Bson;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.gecko.emf.collection.CollectionFactory;
import org.gecko.emf.collection.EReferenceCollection;
import org.gecko.emf.persistence.context.ResultContextBuilder;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.mapping.InputContentHandler;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.Keywords;
import org.gecko.emf.persistence.api.Options;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.api.QueryEngine;
import org.gecko.emf.persistence.context.QueryContext;
import org.gecko.emf.persistence.context.ResultContext;
import org.gecko.emf.persistence.model.mongo.EMongoQuery;
import org.gecko.emf.persistence.mongo.codecs.EObjectCodecProvider;
import org.gecko.emf.persistence.mongo.util.MongoUtils;
import org.gecko.emf.persistence.streams.PersistenceInputStream;
import org.osgi.util.promise.Promise;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.internal.connection.tlschannel.impl.TlsChannelImpl.EofException;

/**
 * Input stream implementation that handles loading of {@link Resource}
 * @author bhunt
 * @author Mark Hoffmann
 */
public class MongoInputStream2 extends PersistenceInputStream<MongoCollection<EObject>, MongoCollection<Document>, EMongoQuery, FindIterable<EObject>, FindIterable<EObject>, EObjectMapper> implements URIConverter.Loadable {

	private Map<Object, Object> mergedOptions = new HashMap<>();
	
	public MongoInputStream2(ConverterService converterService, QueryEngine<EMongoQuery, FindIterable<EObject>>  queryEngine, Promise<MongoCollection<Document>> collection, List<InputContentHandler<FindIterable<EObject>, EObjectMapper>> contentHandler, URI uri, Map<?, ?> options, Map<Object, Object> response) throws PersistenceException {
		super(converterService, queryEngine, collection, contentHandler, uri, options, response);
	}

	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.streams.PersistenceInputStream#configureDriver(java.lang.Object)
	 */
	@Override
	protected Promise<MongoCollection<EObject>> configureDriver(Promise<MongoCollection<Document>> driver) {
		return driver.map(dr->{
			EObjectCodecProvider codecProvider = new EObjectCodecProvider(getResource(), getMergedOptions(), getResourcesCache());
			codecProvider.setConverterService(getConverterService());
			CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
			CodecRegistry defaultRegistry = dr.getCodecRegistry();

			CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
			// get collections and clear it
			MongoCollection<EObject> collection = dr.withCodecRegistry(codecRegistry).withDocumentClass(EObject.class);
			return collection;
		});
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.streams.PersistenceInputStream#executeQuery(org.gecko.emf.persistence.context.QueryContext)
	 */
	@Override
	protected FindIterable<EObject> executeQuery(QueryContext<MongoCollection<EObject>, EMongoQuery, EObjectMapper> queryCtx) {
		MongoCollection<EObject> collection = queryCtx.getDriver();
		EMongoQuery mongoQuery = queryCtx.getQuery();
		Bson filter = mongoQuery.getFilter();
		Document projection = mongoQuery.getProjection();
		boolean countResults = queryCtx.countResult();

		long elementCount = -1l;
		FindIterable<EObject> resultIterable;
		if (filter != null) {
			resultIterable = collection.find(filter);
			if (countResults) {
				elementCount = collection.countDocuments(filter);
			}
		} else {
			resultIterable = collection.find();
			if (countResults) {
				elementCount = collection.countDocuments();
			}
		}
		if (countResults) {
			queryCtx.getResponse().put(Options.OPTION_COUNT_RESPONSE, Long.valueOf(elementCount));
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

		return resultIterable;
	}


	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.streams.PersistenceInputStream#executeCount(org.gecko.emf.persistence.context.QueryContext)
	 */
	@Override
	protected long executeCount(QueryContext<MongoCollection<EObject>, EMongoQuery, EObjectMapper> context)
			throws PersistenceException {
		MongoCollection<EObject> collection = context.getDriver();
		EMongoQuery mongoQuery = context.getQuery();
		Bson filter = mongoQuery.getFilter();
		long elementCount = -1l;
		FindIterable<EObject> resultIterable;
		if (filter != null) {
			resultIterable = collection.find(filter);
			if (context.countResult()) {
				elementCount = collection.countDocuments(filter);
			}
		} else {
			resultIterable = collection.find();
			if (context.countResult()) {
				elementCount = collection.countDocuments();
			}
		}
		if (context.countResult()) {
			context.getResponse().put(Options.OPTION_COUNT_RESPONSE, Long.valueOf(elementCount));
		}
		return elementCount;
	}


	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.streams.PersistenceInputStream#createMapper(org.gecko.emf.persistence.context.ResultContext)
	 */
	@Override
	protected EObjectMapper createMapper(ResultContext<FindIterable<EObject>, EObjectMapper> inputContext) {
		return null;
	}


	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.streams.PersistenceInputStream#isProjectionOnly(java.lang.String)
	 */
	@Override
	protected boolean isProjectionOnly(String query) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Normalizes the load options
	 * @param options the original options
	 */
	private <K extends Object, V extends Object> void normalizeOptions(Map<K, V> options) {
		mergedOptions.putAll(options);
		EClass filterEClass = (EClass) options.getOrDefault(Options.OPTION_FILTER_ECLASS, null);
		EClass collectionEClass = Options.getTableEClass(options);
		if (collectionEClass != null && filterEClass == null) {
			mergedOptions.put(Options.OPTION_FILTER_ECLASS, collectionEClass);
		}
	}

}
