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
package org.gecko.emf.persistence.api;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.gecko.emf.persistence.engine.PersistenceEngine;

/**
 * An interface that is optionally implemented by the input streams returned from 
 * {@link URIConverter#createInputStream(URI)} and {@link URIConverter#createInputStream(URI, Map)}.
 * It is responsible for existence checks or counting elements
 * @author Mark Hoffmann
 * @since 30.05.2022
 * @deprecated use {@link Countable} instead
 */
public interface CountableOld {
	
	/**
	 * This option can be set to tell the count to create a filter over the uri-type column. Only instances of of the given EClass URI are counted then.
	 * This may be used in combination with {@link Options#CAP_ECLASS_URI}. This option must be set as well, to tell the counter implementation,
	 * which class to filter against.
	 * 
	 * value type: Boolean, default is <code>false</code>
	 */
	public static final String OPTION_COUNT_URI_FILTER = "COUNT_URI_FILTER";
	
	/**
	 * This option can be set to tell the count to create a filter over the uri-type column. Only instances of of the given EClass URI are counted then.
	 * This may be used in combination with {@link Options#CAP_ID_FIELD_NAME}. This option must be set as well, to tell the counter implementation,
	 * which id-column to filter against.
	 * 
	 * value type: Boolean, default is <code>false</code>
	 */
	public static final String OPTION_COUNT_ID_ATTRIBUTE = "COUNT_ID_ATTRIBUTE";
	
	/**
	 * Counts the elements that match the given {@link URI} and options
	 * @param uri the uri to count elements for
	 * @param options the count options
	 * @param response the response options map
	 * @return the number of elements or -1
	 * @throws PersistenceException
	 */
	long count(URI uri, Map<?, ?> options, Map<Object, Object> response) throws PersistenceException;
	
	/**
	 * Checks if an element exists for the given {@link URI} and options
	 * @param uri the uri to check for existence
	 * @param options the exist options
	 * @param response the response options map
	 * @return <code>true</code> if at least an element exists, otherwise <code>false</code>
	 * @throws PersistenceException
	 */
	boolean exists(URI uri, Map<?, ?> options, Map<Object, Object> response) throws PersistenceException;

}
