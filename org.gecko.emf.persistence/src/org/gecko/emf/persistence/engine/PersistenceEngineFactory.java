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

import org.eclipse.emf.common.util.URI;
import org.gecko.emf.persistence.api.PersistenceException;

/**
 * Factory that creates {@link PersistenceEngine} instance and releases them.
 * For each implementation usually just one factory is needed, that can then handle multiple configurations for the engines
 * @author Mark Hoffmann
 * @since 14.02.2023
 */
public interface PersistenceEngineFactory {
	
	/**
	 * Creates the engine instance for the given URI
	 * @param uri the {@link URI} to create the engine for
	 * @param options the Resource options
	 * @return the {@link PersistenceEngine} instance
	 */
	PersistenceEngine createEngine(URI uri) throws PersistenceException;
	
	/**
	 * Releases the {@link PersistenceEngine} for the given {@link URI} and return <code>true</code>, if disposal was successfull. Otherwise <code>false</code> will returned.
	 * @param uri the {@link URI} to release the engine for
	 * @return <code>true</code>, if disposal was successful, otherwise <code>false</code>
	 */
	boolean disposeEngine(URI uri);
	
	/**
	 * Returns <code>true</code>, if this factory can handle the given {@link URI}.
	 * @param uri the {@link URI} to check, if it can be handled
	 * @return <code>true</code>, if this factory can handle the {@link URI}
	 */
	boolean canHandle(URI uri);

}
