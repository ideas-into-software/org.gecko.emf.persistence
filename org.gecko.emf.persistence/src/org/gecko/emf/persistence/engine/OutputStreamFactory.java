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
package org.gecko.emf.persistence.engine;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.persistence.api.PersistenceException;
import org.osgi.annotation.versioning.ProviderType;

/**
 * This interface provides the API for for the OutputStream factory. If you wish to use a custom OutputStream,
 * you must create a factory class for your stream that implements this interface.
 * This is later used in EMF to create an {@link OutputStream} when calling {@link Resource#load(java.util.Map)}
 * @param <DRIVER> the table or collection type
 * @author Mark Hoffmann
 * @since 26.03.2022
 * @deprecated use {@link PersistenceEngine} instead
 */
@ProviderType
public interface OutputStreamFactory<DRIVER> {
	
	/**
	 * Constructs the OutputStream
	 * 
	 * @param uri the URI of the resource
	 * @param options the EMF load options
	 * @param table the persistence table / collection specified in the URI
	 * @param response the EMF response
	 * @return the stream for saving the EMF object to a persistence layer
	 */
	OutputStream createOutputStream(URI uri, Map<?, ?> options, DRIVER table, Map<Object, Object> response) throws PersistenceException ;
} 
