/**
 * Copyright (c) 2012 - 2023 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.engine;

import java.util.Map;

import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.resource.PersistenceResource;

/**
 * Implementation specific engine, that does persistence with EMF
 * @param <DRIVER> the driver for the engine e.g. JDBC java.sql.Connection, JPA jakarta.persistence.EntityManagerFactory, Mongo com.mongodb.client.MongoCollection
 * @param <MAPPER> the EObject to database object (and v.v.) mapper
 * @param <RESULTTYPE> the query result type e.g. JDBC java.sql.ResultSet, JPA jakarta.persistence.Query, Mongo com.mongodb.client.FindIterable
 * @param <QUERYTYPE> the query type 
 * @param <QUERYENGINE> a native query engine provided by a native Driver 
 * @author Mark Hoffmann
 * @since 10.02.2023
 */
public interface PersistenceEngine<DRIVER, MAPPER extends EObjectMapper, RESULTTYPE, QUERYTYPE, QUERYENGINE> {
	
	/**
	 * Sets the resource
	 * @param resource the {@link PersistenceResource} to be used for configuration
	 */
	void setResource(PersistenceResource resource);
	
	/**
	 * Returns the {@link PersistenceResource}
	 * @return the {@link PersistenceResource}
	 */
	PersistenceResource getResource();
	
	/**
	 * Returns the mergedOptions.
	 * @return the mergedOptions
	 */
	public Map<Object, Object> getMergedOptions();
	
	/**
	 * returns the response optins map
	 * @return the response optins map
	 */
	public Map<Object, Object> getResponse();
	
	/**
	 * Releases a resources
	 */
	void dispose();

}
