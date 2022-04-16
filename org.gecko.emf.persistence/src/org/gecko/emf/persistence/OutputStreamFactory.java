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
package org.gecko.emf.persistence;

import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.osgi.annotation.versioning.ProviderType;

/**
 * This interface provides the API for for the OutputStream factory. If you wish to use a custom OutputStream,
 * you must create a factory class for your stream that implements this interface.
 * This is later used in EMF to create an {@link OutputStream} when calling {@link Resource#load(java.util.Map)}
 * @param <TABLE> the table or collection type
 * @author Mark Hoffmann
 * @since 26.03.2022
 */
@ProviderType
public interface OutputStreamFactory<TABLE> {
	
	/**
	 * Constructs the OutputStream
	 * 
	 * @param uri the URI of the resource
	 * @param options the EMF load options
	 * @param table the persistence table / collection specified in the URI
	 * @param response the EMF response
	 * @return the stream for saving the EMF object to MongoDB
	 */
	OutputStream createOutputStream(URI uri, Map<?, ?> options, TABLE table, Map<Object, Object> response);
}
