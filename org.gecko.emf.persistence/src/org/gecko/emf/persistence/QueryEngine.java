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

import java.util.Map;

import org.eclipse.emf.common.util.URI;

/**
 * Interface to define an engine that creates a query object of type <QT> out of the provided 
 * resource URI and/or the corresponding options. Depending on the implementation these options may be
 * resource load or save options.
 * @param <QT> the Query type
 * @author Mark Hoffmann
 * @since 08.04.2022
 */
public interface QueryEngine<QT> {
	
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
	 * @return the MongoQuery representation of the query
	 */
	QT buildQuery(URI uri, Map<?, ?> options);
	
}
