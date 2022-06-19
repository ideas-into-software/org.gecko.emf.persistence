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
package org.gecko.emf.persistence.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.osgi.annotation.versioning.ProviderType;

/**
 * This interface provides the API for for the InputStream factory. If you wish to use a custom InputStream,
 * you must create a factory class for your stream that implements this interface.
 * This is for persisting EObject into the store. For that EMF will use this factory to create InputStream, when 
 * calling {@link Resource#save(Map)}.
 * This Factory is also used for {@link Resource#delete(Map)} calls
 * @param <TABLE> the table or collection type
 * @author Mark Hoffmann
 * @since 26.03.2022
 */
@ProviderType
public interface InputStreamFactory<TABLE> {
	
	/**
	 * Constructs the InputStream
	 * 
	 * @param uri the URI of the persistence resource
	 * @param options the EMF load options
	 * @param table the persistence table / collection specified in the URI
	 * @param response the EMF response
	 * @return the stream for loading an EMF object from a persistence store
	 * @throws IOException if there is a problem constructing the EMF object
	 */
	InputStream createInputStream(URI uri, Map<?, ?> options, TABLE table, Map<Object, Object> response) throws IOException;

	/**
	 * Constructs a remove request
	 * @param uri the URI of the resource
	 * @param options the EMF remove options
	 * @param table the persistence table / collection specified in the URI
	 * @param response the EMF response
	 * @throws IOException if there is a problem constructing the remove 
	 */
	void createDeleteRequest(URI uri, Map<?, ?> options, TABLE table, Map<Object, Object> response) throws IOException;
	
	/**
	 * Creates an exist request for the given uri
	 * @param uri the uri to check for existence
	 * @param options the load options
	 * @param table the table to query against
	 * @param response the response option map
	 * @return <code>true</code>, if an object for the uri exists, otherwise <code>false</code>
	 * @throws IOException
	 */
	boolean createExistRequest(URI uri, Map<?, ?> options, TABLE table, Map<Object, Object> response) throws IOException;
	
	/**
	 * Creates an count request for the given uri
	 * @param uri the uri to check for existence
	 * @param options the load options
	 * @param table the table to query against
	 * @param response the response option map
	 * @return the number of elements, or -1 on errors
	 * @throws IOException
	 */
	long createCountRequest(URI uri, Map<?, ?> options, TABLE table, Map<Object, Object> response) throws IOException;

}
