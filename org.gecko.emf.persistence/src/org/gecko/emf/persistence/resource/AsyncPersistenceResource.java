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
package org.gecko.emf.persistence.resource;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.osgi.util.promise.Promise;

/**
 * Resource extension to handle asynchronous handling
 * @author Mark Hoffmann
 * @since 10.02.2023
 */
public interface AsyncPersistenceResource {
	
	/**
	 * Counts the elements of the resource URI response asynchronous and resolves with the number
	 * @return resolves with the number of elements of the resource URI response asynchronous
	 */
	Promise<Long> countAsync();
	
	/**
	 * Counts the elements of the resource URI response asynchronous and resolves with the number
	 * @param options the count properties
	 * @return resolves with the number of elements of the resource URI response asynchronous
	 */
	Promise<Long> countAsync(Map<?, ?> options);
	
	/**
	 * Verifies, if the resource exists an resolves with <code>true</code>
	 * @return resolved with <code>true</code>, if the resource exists and is not empty. Otherwise it resolves with <code>false</code> 
	 */
	Promise<Boolean> existAsync();
	
	/**
	 * Verifies, if the resource exists an resolves with <code>true</code>
	 * @param options the exist properties
	 * @return resolved with <code>true</code>, if the resource exists and is not empty. Otherwise it resolves with <code>false</code> 
	 */
	Promise<Boolean> existAsync(Map<?, ?> options);
	
	/**
	 * Asynchronous call of the load method {@link Resource#load(Map)}
	 * @param options the load options
	 * @return resolves, if load was successful
	 */
	Promise<Map<String, ?>> loadAsync(Map<?, ?> options);
	
	/**
	 * Asynchronous call of the load method {@link Resource#load(InputStream, Map)}
	 * @param stream the {@link InputStream} to load
	 * @param options the load options
	 * @return resolves, if load was successful
	 */
	Promise<Map<String, ?>> loadAsync(InputStream stream, Map<?, ?> options);
	
	/**
	 * Asynchronous call of the save method {@link Resource#save(Map)}
	 * @param options the save options
	 * @return resolves, if save was successful
	 */
	Promise<Map<String, ?>> saveAsync(Map<?, ?> options);
	
	/**
	 * Asynchronous call of the save method {@link Resource#save(OutputStream, Map)}
	 * @param stream the {@link OutputStream} to save into
	 * @param options the save options
	 * @return resolves, if save was successful
	 */
	Promise<Map<String, ?>> saveAsync(OutputStream stream, Map<?, ?> options);
	
	/**
	 * Asynchronous call of the delete method {@link Resource#delete(Map)}
	 * @param options the delete options
	 * @return resolves, if delete was successful
	 */
	Promise<Map<String, ?>> deleteAsync(Map<?, ?> options);
	
}
