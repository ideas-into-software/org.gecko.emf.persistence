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
package org.gecko.emf.persistence.api;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.mapping.InputContentHandler;

/**
 * Implementation specific engine, that does persistence with EMF
 * @param <DRIVER> the driver for the engine e.g. JDBC java.sql.Connection, JPA jakarta.persistence.EntityManagerFactory, Mongo com.mongodb.client.MongoCollection
 * @param <MAPPER> the EObject to database object (and v.v.) mapper
 * @param <RESULT> the query result type e.g. JDBC java.sql.ResultSet, JPA jakarta.persistence.Query, Mongo com.mongodb.client.FindIterable
 * @param <QUERYTYPE> the query type 
 * @param <QUERYENGINE> a native query engine provided by a native Driver 
 * @author Mark Hoffmann
 * @since 10.02.2023
 */
public interface PersistenceEngine<DRIVER, MAPPER extends EObjectMapper, RESULT, QUERYTYPE, QUERYENGINE> {
	
	/**
	 * Returns the {@link QueryEngine} 
	 * @return the {@link QueryEngine}
	 */
	QueryEngine<QUERYTYPE, QUERYENGINE> getQueryEngine();
	
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

}
