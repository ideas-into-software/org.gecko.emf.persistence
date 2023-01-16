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
package org.gecko.emf.persistence.jpa.streams;

import static org.gecko.emf.persistence.jpa.JPAPersistenceConstants.PERSISTENCE_FILTER;
import static org.gecko.emf.persistence.jpa.JPAPersistenceConstants.PERSISTENCE_FILTER_PROP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.gecko.emf.persistence.DefaultStreamFactory;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.PrimaryKeyFactory;
import org.gecko.emf.persistence.api.QueryEngine;
import org.gecko.emf.persistence.engine.InputStreamFactory;
import org.gecko.emf.persistence.engine.OutputStreamFactory;
import org.gecko.emf.persistence.jpa.mapper.JPAMapper;
import org.gecko.emf.persistence.mapping.InputContentHandler;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.util.promise.Promise;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;

/**
 * Jdbc stream factory
 * <TABLE> is of type {@link Promise<EntityManagerFactory>}
 * <QT> is of type {@link JPAQuery}
 * <RT> is of type {@link EntityManagerFactory}
 * @author Mark Hoffmann
 * @since 15.01.2023
 */
@Component(name="JPAStreamFactory", immediate=true, service= {InputStreamFactory.class, OutputStreamFactory.class}, property = PERSISTENCE_FILTER_PROP)
public class JPAStreamFactory extends DefaultStreamFactory<Promise<EntityManagerFactory>, Query, Query, EntityManager, JPAMapper> {

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
	public void setQueryEngine(QueryEngine<Query, EntityManager> queryEngine) {
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
	@Reference(name="JdbcInputHandler", policy=ReferencePolicy.STATIC, cardinality=ReferenceCardinality.AT_LEAST_ONE, unbind="removeInputHandler", target=PERSISTENCE_FILTER)
	public void addInputHandler(InputContentHandler<Query, JPAMapper> contentHandler) {
		super.addInputHandler(contentHandler);
	}

	/**
	 * Un-sets an {@link InputContentHandler} to be used
	 * @param contentHandler the content handler to be removed
	 */
	public void removeInputHandler(InputContentHandler<Query, JPAMapper> contentHandler) {
		super.removeInputHandler(contentHandler);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.DefaultStreamFactory#doCreateOutputStream(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
	 */
	protected OutputStream doCreateOutputStream(URI uri, Map<?, ?> options, Promise<EntityManagerFactory> connection,
			Map<Object, Object> response) {
		return new JPAOutputStream(converterService, connection, uri, idFactories, options, response);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.DefaultStreamFactory#doCreateInputStream(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
	 */
	protected InputStream doCreateInputStream(URI uri, Map<?, ?> options, Promise<EntityManagerFactory> connection, Map<Object, Object> response) throws IOException {
		return new JPAInputStream(converterService, queryEngine, connection, handlerList, uri, options, response);
	}

}
