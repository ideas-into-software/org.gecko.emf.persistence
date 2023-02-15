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
package org.gecko.emf.persistence.engine;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.Options;
import org.gecko.emf.persistence.api.PrimaryKeyFactory;
import org.gecko.emf.persistence.api.QueryEngine;
import org.gecko.emf.persistence.context.PersistenceContext;
import org.gecko.emf.persistence.helper.EMFHelper;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.mapping.InputContentHandler;
import org.gecko.emf.persistence.resource.PersistenceResource;

/**
 * This is a base component class for input and output streams
 * @param <DRIVER> Driver, Table or Collection type, whatever is the base to do something on the database
 * @param <QT> the query object type of you implementation
 * @param <RT> the result type {@link ResultSet} for jdbc or a FindIterable for MongoDB
 * @param <ENGINE> the native query engine
 * @param <MAPPER> an mapper for result types to {@link EObject} and {@link EObject} to input type
 * @author Mark Hoffmann
 * @since 08.04.2022
 */
public abstract class BasicPersistenceEngine<DRIVER, MAPPER extends EObjectMapper, RESULTTYPE, QUERYTYPE, QUERYENGINE> implements PersistenceEngine<DRIVER, MAPPER, RESULTTYPE, QUERYTYPE, QUERYENGINE> {
	
	/** queryEngine for query type and native query engine*/
	private QueryEngine<QUERYTYPE, QUERYENGINE> queryEngine;
	private ConverterService converterService;
	private volatile Map<String, PrimaryKeyFactory> idFactories = new ConcurrentHashMap<>();
	/** handlerList mapper for the result type to EObject using an optional mapper*/
	private volatile List<InputContentHandler<RESULTTYPE, MAPPER>> handlerList = new CopyOnWriteArrayList<>();
	private final Map<Object, Object> mergedOptions = new HashMap<>();
	private PersistenceResource resource;
	private Map<Object, Object> properties;
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.engine.PersistenceEngine#configure(org.gecko.emf.persistence.resource.PersistenceResource, java.util.Map)
	 */
	@Override
	public void configure(PersistenceResource resource, Map<Object, Object> properties) {
		this.resource = resource;
		this.properties = properties;
		normalizeOptions(properties);
	}
	
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
	public void setQueryEngine(QueryEngine<QUERYTYPE, QUERYENGINE> queryEngine) {
		this.queryEngine = queryEngine;
	}
	
	/**
	 * Returns the queryEngine.
	 * @return the queryEngine
	 */
	public QueryEngine<QUERYTYPE, QUERYENGINE> getQueryEngine() {
		return queryEngine;
	}
	
	/**
	 * Returns the resource.
	 * @return the resource
	 */
	public PersistenceResource getResource() {
		return resource;
	}
	
	/**
	 * Returns the properties.
	 * @return the properties
	 */
	public Map<Object, Object> getProperties() {
		return properties;
	}
	
	/**
	 * Returns the mergedOptions.
	 * @return the mergedOptions
	 */
	public Map<Object, Object> getMergedOptions() {
		return mergedOptions;
	}
	
	public Map<Object, Object> getResponse() {
		return EMFHelper.getResponse(getMergedOptions());
	}
	
	protected List<InputContentHandler<RESULTTYPE, MAPPER>> getContentHandler() {
		return Collections.unmodifiableList(handlerList);
	}
	
	public PersistenceContext<DRIVER, QUERYTYPE, RESULTTYPE, QUERYENGINE, MAPPER> createContext() {
		return new PersistenceContext<DRIVER, QUERYTYPE, RESULTTYPE, QUERYENGINE, MAPPER>() {

			@Override
			public ConverterService getConverterSevice() {
				return converterService;
			}

			@Override
			public QueryEngine<QUERYTYPE, QUERYENGINE> getQueryEngine() {
				return queryEngine;
			}

			@Override
			public List<InputContentHandler<RESULTTYPE, MAPPER>> getInputContentHandler() {
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
	protected void addInputHandler(InputContentHandler<RESULTTYPE, MAPPER> contentHandler) {
		handlerList.add(contentHandler);
	}
	
	/**
	 * Un-sets an {@link InputContentHandler} to be used
	 * @param contentHandler the content handler to be removed
	 */
	protected void removeInputHandler(InputContentHandler<RESULTTYPE, MAPPER> contentHandler) {
		handlerList.remove(contentHandler);
	}
	
	/**
	 * Normalizes the load options
	 * @param options the original options
	 */
	protected void normalizeOptions(Map<Object, Object> options) {
		mergedOptions.putAll(options);
		EClass collectionEClass = Options.getTableEClass(options);
		if (collectionEClass != null && !options.containsKey(Options.OPTION_FILTER_ECLASS)) {
			mergedOptions.put(Options.OPTION_FILTER_ECLASS, collectionEClass);
		}
	}
	

}
