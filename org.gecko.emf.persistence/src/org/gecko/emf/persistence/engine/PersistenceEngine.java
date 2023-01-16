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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.QueryEngine;
import org.gecko.emf.persistence.context.PersistenceContext;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.mapping.InputContentHandler;

/**
 * Interface for a persistence engine that is used for CRUD operations
 * @param <DRIVER> the driver for the engine e.g. JDBC java.sql.Connection, JPA jakarta.persistence.EntityManagerFactory, Mongo com.mongodb.client.MongoCollection
 * @param <QUERY> the query type 
 * @param <RESULT> the query result type e.g. JDBC java.sql.ResultSet, JPA jakarta.persistence.Query, Mongo com.mongodb.client.FindIterable
 * @param <MAPPER> the EObject to database object (and v.v.) mapper
 * @param <ENGINE> a native query engine provided by a native Driver 
 * @author Mark Hoffmann
 * @since 16.01.2023
 */
/**
 * @author mark
 * @since 16.01.2023
 */
public interface PersistenceEngine<DRIVER, QUERY, RESULT, MAPPER extends EObjectMapper, ENGINE> extends InputStreamFactory<DRIVER>, OutputStreamFactory<DRIVER>{
	
	/**
	 * Returns the {@link QueryEngine} 
	 * @return the {@link QueryEngine}
	 */
	QueryEngine<QUERY, ENGINE> getQueryEngine();
	
	/**
	 * Returns the {@link ConverterService}
	 * @return the {@link ConverterService}
	 */
	ConverterService getConverterService();
	
	/**
	 * Returns all {@link InputContentHandler} or an empty {@link List}
	 * @return all {@link InputContentHandler} or an empty {@link List}
	 */
	List<InputContentHandler<RESULT, MAPPER>> getInputContentHandler();
	
	/**
	 * Returns the persistence driver of the implementation
	 * @return the persistence driver
	 */
	DRIVER getDriver();
	
	/**
	 * Returns the DB-Object to {@link EObject} and back mapper 
	 * @return the mapper
	 */
	MAPPER getMapper();
	
	/**
	 * Create the persistence context object
	 * @return the persistence context object
	 */
	public PersistenceContext<DRIVER, QUERY, RESULT, ENGINE, MAPPER> createContext() ;
	
}
