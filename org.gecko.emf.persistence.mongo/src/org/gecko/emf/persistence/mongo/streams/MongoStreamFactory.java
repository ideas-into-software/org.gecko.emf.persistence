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
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.mongo.streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.gecko.emf.persistence.ConverterService;
import org.gecko.emf.persistence.DefaultStreamFactory;
import org.gecko.emf.persistence.Keywords;
import org.gecko.emf.persistence.Options;
import org.gecko.emf.persistence.OutputStreamFactory;
import org.gecko.emf.persistence.PrimaryKeyFactory;
import org.gecko.emf.persistence.QueryEngine;
import org.gecko.emf.persistence.input.InputContentHandler;
import org.gecko.emf.persistence.input.InputStreamFactory;
import org.gecko.emf.persistence.model.mongo.EMongoQuery;
import org.gecko.emf.persistence.mongo.util.MongoUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

/**
 * Mongo stream factory
 * <TABLE> is of type {@link MongoCollection}
 * <QT> is of type {@link EMongoQuery}
 * <RT> is of type {@link FindIterable}
 * @author Mark Hoffmann
 * @since 08.04.2022
 */
@Component(name="MongoStreamFactory", immediate=true, service= {InputStreamFactory.class, OutputStreamFactory.class})
public class MongoStreamFactory extends DefaultStreamFactory<MongoCollection<Document>, EMongoQuery, FindIterable<EObject>> {

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.OutputStreamFactory#createOutputStream(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
	 */
	@Override
	public OutputStream createOutputStream(URI uri, Map<?, ?> options, MongoCollection<Document> collection, Map<Object, Object> response) {
		return new MongoOutputStream(converterService, collection, uri, idFactories, options, response);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.InputStreamFactory#createInputStream(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
	 */
	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options, MongoCollection<Document> collection, Map<Object, Object> response) throws IOException {
		return new MongoInputStream(converterService, queryEngine, collection, handlerList, uri, options, response);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.InputStreamFactory#createDeleteRequest(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
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

			EMongoQuery mongoQuery = queryEngine.buildQuery(uri, mergedOptions);

			Bson filter = mongoQuery.getFilter();

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

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.InputStreamFactory#createExistRequest(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
	 */
	@Override
	public boolean createExistRequest(URI uri, Map<?, ?> options, MongoCollection<Document> table,
			Map<Object, Object> response) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.InputStreamFactory#createCountRequest(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
	 */
	@Override
	public long createCountRequest(URI uri, Map<?, ?> options, MongoCollection<Document> table,
			Map<Object, Object> response) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Sets the converter service
	 * @param converterService the converter service to set
	 */
	@Reference(name="ConverterService", policy=ReferencePolicy.STATIC, cardinality=ReferenceCardinality.MANDATORY)
	public void setConverterService(ConverterService converterService) {
		super.setConverterService(converterService);
	}

	/**
	 * Sets the query engine
	 * @param queryEngine the query engine to set
	 */
	@Reference(name="QueryEngine", policy=ReferencePolicy.STATIC, cardinality=ReferenceCardinality.MANDATORY)
	public void setQueryEngine(QueryEngine<EMongoQuery> queryEngine) {
		super.setQueryEngine(queryEngine);
	}

	/**
	 * Sets the id factory 
	 * @param mongoIdFactory the id factory to be added
	 */
	@Reference(name="PrimaryKeyFactory", policy=ReferencePolicy.DYNAMIC, cardinality=ReferenceCardinality.MANDATORY, unbind="removePrimaryKeyFactory")
	public void addPrimaryKeyFactory(PrimaryKeyFactory pkFactory) {
		super.addPrimaryKeyFactory(pkFactory);
	}

	/**
	 * Un-sets the id factory 
	 * @param mongoIdFactory the id factory to be removed
	 */
	public void removePrimaryKeyFactory(PrimaryKeyFactory pkFactory) {
		super.removePrimaryKeyFactory(pkFactory);
	}

	/**
	 * Sets an {@link InputContentHandler} to be used
	 * @param contentHandler the id factory to be added
	 */
	@Reference(name="InputHandler", policy=ReferencePolicy.DYNAMIC, cardinality=ReferenceCardinality.MULTIPLE, unbind="removeInputHandler")
	public void addInputHandler(InputContentHandler<FindIterable<EObject>> contentHandler) {
		super.addInputHandler(contentHandler);
	}

	/**
	 * Un-sets an {@link InputContentHandler} to be used
	 * @param contentHandler the content handler to be removed
	 */
	public void removeInputHandler(InputContentHandler<FindIterable<EObject>> contentHandler) {
		super.removeInputHandler(contentHandler);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.DefaultStreamFactory#doCreateOutputStream(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
	 */
	@Override
	protected OutputStream doCreateOutputStream(URI uri, Map<?, ?> options, MongoCollection<Document> collection,
			Map<Object, Object> response) throws IOException {
		return new MongoOutputStream(converterService, collection, uri, idFactories, options, response);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.DefaultStreamFactory#doCreateInputStream(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
	 */
	@Override
	protected InputStream doCreateInputStream(URI uri, Map<?, ?> options, MongoCollection<Document> collection,
			Map<Object, Object> response) throws IOException {
		return new MongoInputStream(converterService, queryEngine, collection, handlerList, uri, options, response);
	}

}
