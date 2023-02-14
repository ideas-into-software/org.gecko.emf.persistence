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
package org.gecko.emf.persistence;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.Countable;
import org.gecko.emf.persistence.api.Deletable;
import org.gecko.emf.persistence.api.Options;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.api.PrimaryKeyFactory;
import org.gecko.emf.persistence.api.QueryEngine;
import org.gecko.emf.persistence.context.PersistenceContext;
import org.gecko.emf.persistence.engine.InputStreamFactory;
import org.gecko.emf.persistence.engine.OutputStreamFactory;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.mapping.InputContentHandler;

/**
 * This is a base component class for input and output streams
 * @param <DRIVER_RAW> Driver, Table or Collection type, whatever is the base to do something on the database
 * @param <DRIVER> Driver, Table or Collection type, whatever is the base to do something on the database
 * @param <QT> the query object type of you implementation
 * @param <RT> the result type {@link ResultSet} for jdbc or a FindIterable for MongoDB
 * @param <ENGINE> the native query engine
 * @param <MAPPER> an mapper for result types to {@link EObject} and {@link EObject} to input type
 * @author Mark Hoffmann
 * @since 08.04.2022
 */
public abstract class DefaultStreamFactory<DRIVER, DRIVER_RAW, QT, RT, ENGINE, MAPPER extends EObjectMapper> implements InputStreamFactory<DRIVER_RAW>, OutputStreamFactory<DRIVER_RAW> {
	
	/** queryEngine for query type and native query engine*/
	protected QueryEngine<QT, ENGINE> queryEngine;
	protected ConverterService converterService;
	protected volatile Map<String, PrimaryKeyFactory> idFactories = new ConcurrentHashMap<>();
	/** handlerList mapper for the result type to EObject using an optional mapper*/
	protected volatile List<InputContentHandler<RT, MAPPER>> handlerList = new CopyOnWriteArrayList<>();
	protected Map<Object, Object> mergedOptions = new HashMap<>();
	
	/**
	 * Sets the converter service
	 * @param converterService the converter service to set
	 */
	public void setConverterService(ConverterService converterService) {
		this.converterService = converterService;
	}
	
	/**
	 * Returns the converterService.
	 * @return the converterService
	 */
	public ConverterService getConverterService() {
		return converterService;
	}

	/**
	 * Sets the query engine
	 * @param queryEngine the query engine to set
	 */
	public void setQueryEngine(QueryEngine<QT, ENGINE> queryEngine) {
		this.queryEngine = queryEngine;
	}
	
	/**
	 * Returns the queryEngine.
	 * @return the queryEngine
	 */
	public QueryEngine<QT, ENGINE> getQueryEngine() {
		return queryEngine;
	}
	
	public PersistenceContext<DRIVER, QT, RT, ENGINE, MAPPER> createContext() {
		return new PersistenceContext<DRIVER, QT, RT, ENGINE, MAPPER>() {

			@Override
			public ConverterService getConverterSevice() {
				return converterService;
			}

			@Override
			public QueryEngine<QT, ENGINE> getQueryEngine() {
				return queryEngine;
			}

			@Override
			public List<InputContentHandler<RT, MAPPER>> getInputContentHandler() {
				return handlerList;
			}
			
			@Override
			public Map<String, PrimaryKeyFactory> getKeyFactories() {
				return idFactories;
			}
		};
	}
	
	/**
	 * Sets the id factory 
	 * @param pkFactory the id factory to be added
	 */
	protected void addPrimaryKeyFactory(PrimaryKeyFactory pkFactory) {
		idFactories.put(pkFactory.getTableURI(), pkFactory);
	}

	/**
	 * Un-sets the id factory 
	 * @param pkFactory the id factory to be removed
	 */
	protected void removePrimaryKeyFactory(PrimaryKeyFactory pkFactory) {
		PrimaryKeyFactory target = idFactories.get(pkFactory.getTableURI());
		if (pkFactory == target)
			idFactories.remove(pkFactory.getTableURI());
	}
	
	/**
	 * Sets an {@link InputContentHandler} to be used
	 * @param contentHandler the id factory to be added
	 */
	protected void addInputHandler(InputContentHandler<RT, MAPPER> contentHandler) {
		handlerList.add(contentHandler);
	}
	
	/**
	 * Un-sets an {@link InputContentHandler} to be used
	 * @param contentHandler the content handler to be removed
	 */
	protected void removeInputHandler(InputContentHandler<RT, MAPPER> contentHandler) {
		handlerList.remove(contentHandler);
	}
	
	/**
	 * Normalizes the load options
	 * @param options the original options
	 */
	protected <K extends Object, V extends Object> void normalizeOptions(Map<K, V> options) {
		mergedOptions.putAll(options);
		EClass filterEClass = (EClass) options.getOrDefault(Options.OPTION_FILTER_ECLASS, null);
		EClass collectionEClass = Options.getTableEClass(options);
		if (collectionEClass != null && filterEClass == null) {
			mergedOptions.put(Options.OPTION_FILTER_ECLASS, collectionEClass);
		}
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.InputStreamFactory#createInputStream(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
	 */
	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options, DRIVER_RAW table, Map<Object, Object> response)
			throws PersistenceException {
		return doCreateInputStream(uri, options, table, response);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.OutputStreamFactory#createOutputStream(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
	 */
	@Override
	public OutputStream createOutputStream(URI uri, Map<?, ?> options, DRIVER_RAW table, Map<Object, Object> response) throws PersistenceException  {
		return doCreateOutputStream(uri, options, table, response);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.InputStreamFactory#createCountRequest(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
	 */
	@Override
	public long createCountRequest(URI uri, Map<?, ?> options, DRIVER_RAW table, Map<Object, Object> response)
			throws PersistenceException {
		InputStream is = doCreateInputStream(uri, options, table, response);
		if (is instanceof Countable) {
			return ((Countable)is).count(uri, options, response);
		}
		throw new PersistenceException("InputStream does not implement Countable, to produce a result");
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.InputStreamFactory#createDeleteRequest(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
	 */
	@Override
	public void createDeleteRequest(URI uri, Map<?, ?> options, DRIVER_RAW table, Map<Object, Object> response)
			throws PersistenceException {
		InputStream is = doCreateInputStream(uri, options, table, response);
		if (is instanceof Deletable) {
			((Deletable)is).delete(uri, options, response);
		}
		throw new PersistenceException("InputStream does not implement Deletable, to produce a result");
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.InputStreamFactory#createExistRequest(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object, java.util.Map)
	 */
	@Override
	public boolean createExistRequest(URI uri, Map<?, ?> options, DRIVER_RAW table, Map<Object, Object> response)
			throws PersistenceException {
		InputStream is = doCreateInputStream(uri, options, table, response);
		if (is instanceof Countable) {
			return ((Countable)is).exists(uri, options, response);
		}
		throw new PersistenceException("InputStream does not implement Countable, to produce a result");
	}
	
	protected abstract OutputStream doCreateOutputStream(URI uri, Map<?, ?> options, DRIVER_RAW table, Map<Object, Object> response) throws PersistenceException ;

	protected abstract InputStream doCreateInputStream(URI uri, Map<?, ?> options, DRIVER_RAW table, Map<Object, Object> response) throws PersistenceException ;

}
