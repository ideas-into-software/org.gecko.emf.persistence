/**
 * Copyright (c) 2012 - 2022 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.context;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.engine.EngineContext;
import org.gecko.emf.persistence.mapping.EObjectMapper;

/**
 * Context object that hold information that are needed to create and execute a query from a given EObject or statement
 * @author mark
 * @since 17.06.2022
 */
public interface QueryContext<DRIVER, QUERY, MAPPER extends EObjectMapper> {
	
	public static <DRIVER, QUERY, MAPPER extends EObjectMapper> QueryContextBuilder<DRIVER, QUERY, MAPPER> createContextBuilder(EngineContext context) {
		return new QueryContextBuilder<DRIVER, QUERY, MAPPER>(context);
	}
	
	/**
	 * Returns the {@link EClass}
	 * @return the {@link EClass}
	 */
	EClass getEClass();
	
	/**
	 * Returns the driver
	 * @return the driver
	 */
	DRIVER getDriver();
	
	/**
	 * Returns the query object
	 * @return the query object
	 */
	QUERY getQuery();
	
	/**
	 * Returns the input mapper, if there is one
	 * @return the input mapper, if there is one
	 */
	public MAPPER getMapper();
	
	/**
	 * Returns the idColumn.
	 * @return the idColumn
	 */
	public String getIdColumn();
	
	/**
	 * Returns the column.
	 * @return the column
	 */
	public String getColumn();
	
	/**
	 * Returns the table.
	 * @return the table
	 */
	public String getTable();
	
	/**
	 * Return the load options
	 * @return the load options or an empty {@link Map}
	 */
	Map<Object, Object> getOptions();
	
	/**
	 * Return the loading resource
	 * @return the loading resource
	 */
	Resource getResource();
	
	/**
	 * Returns the resource cache if there is any
	 * @return the resource cache or <code>null</code>, if not available
	 */
	List<Resource> getResourceCache();
	
	/**
	 * Returns the converter service
	 * @return the converter service
	 */
	Optional<ConverterService> getConverter();
	
	/**
	 * Returns the response map 
	 * @return the response map
	 */
	Map<Object, Object> getResponse();
	
	/**
	 * Return the engine context
	 * @return the engine context
	 */
	EngineContext getEngineContext();
	
	/**
	 * Returns <code>true</code>, if the result should be mapped for only projected fields, otherwise <code>false</code>
	 * @return <code>true</code>, if the result should be mapped for only projected fields
	 */
	boolean projectOnly();
	
	/**
	 * Returns <code>true</code>, if the resulting mapped elements should be counted as well.
	 * @return <code>true</code>, if the resulting elements should be counted as well.
	 */
	boolean countResponse();

}
