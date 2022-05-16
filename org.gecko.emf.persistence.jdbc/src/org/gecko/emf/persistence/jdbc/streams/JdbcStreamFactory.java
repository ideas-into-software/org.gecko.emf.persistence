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
package org.gecko.emf.persistence.jdbc.streams;

import static org.gecko.emf.persistence.jdbc.JdbcPersistenceConstants.PERSISTENCE_FILTER;
import static org.gecko.emf.persistence.jdbc.JdbcPersistenceConstants.PERSISTENCE_FILTER_PROP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.gecko.emf.persistence.ConverterService;
import org.gecko.emf.persistence.DefaultStreamFactory;
import org.gecko.emf.persistence.InputContentHandler;
import org.gecko.emf.persistence.InputStreamFactory;
import org.gecko.emf.persistence.OutputStreamFactory;
import org.gecko.emf.persistence.PrimaryKeyFactory;
import org.gecko.emf.persistence.QueryEngine;
import org.gecko.emf.persistence.jdbc.query.JdbcQuery;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.jdbc.DataSourceFactory;

/**
 * Mongo stream factory
 * <TABLE> is of type {@link MongoCollection}
 * <QT> is of type {@link EMongoQuery}
 * <RT> is of type {@link FindIterable}
 * @author Mark Hoffmann
 * @since 08.04.2022
 */
@Component(name="JdbcStreamFactory", immediate=true, service= {InputStreamFactory.class, OutputStreamFactory.class}, property = PERSISTENCE_FILTER_PROP)
public class JdbcStreamFactory extends DefaultStreamFactory<Connection, JdbcQuery, ResultSet> {

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.OutputStreamFactory#createOutputStream(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
	 */
	@Override
	public OutputStream createOutputStream(URI uri, Map<?, ?> options, Connection connection, Map<Object, Object> response) {
		return new JdbcOutputStream(converterService, connection, uri, idFactories, options, response);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.InputStreamFactory#createInputStream(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
	 */
	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options, Connection connection, Map<Object, Object> response) throws IOException {
		return new JdbcInputStream(converterService, queryEngine, connection, handlerList, uri, options, response);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.InputStreamFactory#createDeleteRequest(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
	 */
	@Override
	public void createDeleteRequest(URI uri, Map<?, ?> options, Connection connection,
			Map<Object, Object> response) throws IOException {
		normalizeOptions(options);
//		boolean countResults = false;
//		Object optionCountResult = mergedOptions.get(Options.OPTION_COUNT_RESULT);
//		long elementCount = -1l;
//		countResults = optionCountResult != null && Boolean.TRUE.equals(optionCountResult);
//		DeleteResult deleteResult = null;
//
//		if (uri.query() != null) {
//			if (queryEngine == null) {
//				throw new IOException("The query engine was not found");
//			}
//
//			EMongoQuery mongoQuery = queryEngine.buildQuery(uri, mergedOptions);
//
//			Bson filter = mongoQuery.getFilter();
//
//			if (filter != null) {
//				deleteResult = collection.deleteMany(filter);
//				if (countResults) {
//					elementCount = deleteResult.getDeletedCount();
//				}
//			} else {
//				deleteResult = collection.deleteOne(new BasicDBObject(Keywords.ID_KEY, MongoUtils.getID(uri)));
//				if (countResults) {
//					elementCount = deleteResult.getDeletedCount();
//				}
//			}
//			if (countResults) {
//				response.put(Options.OPTION_COUNT_RESPONSE, Long.valueOf(elementCount));
//			}
//
//		} else {
//			deleteResult = collection.deleteOne(new BasicDBObject(Keywords.ID_KEY, MongoUtils.getID(uri)));
//			if (countResults) {
//				elementCount = deleteResult.getDeletedCount();
//				response.put(Options.OPTION_COUNT_RESPONSE, Long.valueOf(elementCount));
//			}
//		}
	}

	/**
	 * Sets the converter service
	 * @param converterService the converter service to set
	 */
	@Reference(name="JdbcConverterService", policy=ReferencePolicy.STATIC, cardinality=ReferenceCardinality.MANDATORY, target=PERSISTENCE_FILTER)
	public void setConverterService(ConverterService converterService) {
		super.setConverterService(converterService);
	}

	/**
	 * Sets the query engine
	 * @param queryEngine the query engine to set
	 */
	@Reference(name="JdbcQueryEngine", policy=ReferencePolicy.STATIC, cardinality=ReferenceCardinality.MANDATORY, target=PERSISTENCE_FILTER)
	public void setQueryEngine(QueryEngine<JdbcQuery> queryEngine) {
		super.setQueryEngine(queryEngine);
	}

	/**
	 * Sets the id factory 
	 * @param mongoIdFactory the id factory to be added
	 */
	@Reference(name="PrimaryKeyFactory", policy=ReferencePolicy.DYNAMIC, cardinality=ReferenceCardinality.MANDATORY, unbind="removePrimaryKeyFactory", target=PERSISTENCE_FILTER)
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
	@Reference(name="JdbcInputHandler", policy=ReferencePolicy.DYNAMIC, cardinality=ReferenceCardinality.MULTIPLE, unbind="removeInputHandler", target=PERSISTENCE_FILTER)
	public void addInputHandler(InputContentHandler<ResultSet> contentHandler) {
		super.addInputHandler(contentHandler);
	}

	/**
	 * Un-sets an {@link InputContentHandler} to be used
	 * @param contentHandler the content handler to be removed
	 */
	public void removeInputHandler(InputContentHandler<ResultSet> contentHandler) {
		super.removeInputHandler(contentHandler);
	}

}
