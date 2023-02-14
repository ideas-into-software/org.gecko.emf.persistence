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

import org.gecko.emf.persistence.context.PersistenceContext;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.osgi.util.promise.Promise;

/**
 * Initialization to setup the configuration and persistence implementation.
 * This is e.g. for caching table, column to EStructuralFeature mappings, setting up DB structures.
 * @author Mark Hoffmann
 * @since 10.02.2023
 */
public interface PersistenceInitialization<TABLE, QT, RT, ENGINE, MAPPER extends EObjectMapper> {
	
	/**
	 * Creates the persistence context
	 * @return the persistence context
	 */
	Promise<PersistenceContext<TABLE, QT, RT, ENGINE, MAPPER>> createContext();
	
	/**
	 * Initializes the mapping structure between persistence and EMF 
	 * @param context the persistence context
	 * @return resolved when initialization finished
	 */
	Promise<PersistenceContext<TABLE, QT, RT, ENGINE, MAPPER>> initializeStructuralMapping(PersistenceContext<TABLE, QT, RT, ENGINE, MAPPER> context);
	
	/**
	 * Initializes the underlying persistence layer
	 * @param context the persistence context
	 * @return resolves when this initialization is done
	 */
	Promise<PersistenceContext<TABLE, QT, RT, ENGINE, MAPPER>> initializePersistence(PersistenceContext<TABLE, QT, RT, ENGINE, MAPPER> context);

}
