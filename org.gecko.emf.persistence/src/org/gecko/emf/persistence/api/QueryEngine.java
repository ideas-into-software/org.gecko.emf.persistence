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
package org.gecko.emf.persistence.api;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Optional;

import javax.management.Query;

import org.eclipse.emf.common.util.URI;

/**
 * Interface to define an engine that creates a query object of type <QT> out of the provided 
 * resource URI and/or the corresponding options. Depending on the implementation these options may be
 * resource load or save options.
 * @param <QT> the Query type
 * @author Mark Hoffmann
 * @since 08.04.2022
 */
public interface QueryEngine<QT, ENGINE> {
	
	/**
	 * Sets an optional native query engine
	 * @param nativeEngine the native query engine to be set
	 */
	void setNativeEngine(ENGINE nativeEngine);
	
	/**
	 * Returns the native query engine instance or <code>null</code>
	 * @return the native query engine instance or <code>null</code>
	 */
	Optional<ENGINE> getNativeEngine();
	
	/**
	 * This function builds a query object to be used as a query to th persistence from the EMF query
	 * 
	 * @param uri the incoming URI containing the query
	 * @return the MongoQuery representation of the query
	 */
	QT buildQuery(URI uri);
	
	/**
	 * This function builds a query object to be used as a query to the persistence from the EMF query
	 * 
	 * @param uri the incoming URI containing the query
	 * @param options the options
	 * @return the representation instance of the query (JPA {@link Query}, JDBC {@link ResultSet})
	 */
	QT buildQuery(URI uri, Map<?, ?> options);
	
	/**
	 * This function builds a query object to be used as a query to th persistence from the EMF query
	 * 
	 * @param uri the incoming URI containing the query
	 * @param nativeEngine the underlying native engine to create a query. This parameter can be <code>null</code>
	 * @return the MongoQuery representation of the query
	 */
	QT buildQuery(URI uri, ENGINE nativeEngine);
	
	/**
	 * This function builds a query object to be used as a query to the persistence from the EMF query
	 * 
	 * @param uri the incoming URI containing the query
	 * @param options the options
	 * @param nativeEngine the underlying native engine to create a query. This parameter can be <code>null</code>
	 * @return the MongoQuery representation of the query
	 */
	QT buildQuery(URI uri, Map<?, ?> options, ENGINE nativeEngine);
	
}
