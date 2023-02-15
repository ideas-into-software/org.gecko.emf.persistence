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

/**
 * An interface that is optionally implemented by the input streams returned from 
 * {@link URIConverter#createInputStream(URI)} and {@link URIConverter#createInputStream(URI, Map)}.
 * It is responsible for existence checks or counting elements
 * @author Mark Hoffmann
 * @since 30.05.2022
 */
public interface Countable {
	
	/**
	 * This option can be set to tell the count to create a filter over the uri-type column. Only instances of of the given EClass URI are counted then.
	 * This may be used in combination with {@link Options#OPTION_ECLASS_URI_HINT}. This option must be set as well, to tell the counter implementation,
	 * which class to filter against.
	 * 
	 * value type: Boolean, default is <code>false</code>
	 */
	public static final String OPTION_COUNT_URI_FILTER = "COUNT_URI_FILTER";
	
	/**
	 * This option can be set to tell the count to create a filter over the uri-type column. Only instances of of the given EClass URI are counted then.
	 * This may be used in combination with {@link Options#OPTION_ECLASS_IDATTRIBUTE_HINT}. This option must be set as well, to tell the counter implementation,
	 * which id-column to filter against.
	 * 
	 * value type: Boolean, default is <code>false</code>
	 */
	public static final String OPTION_COUNT_ID_ATTRIBUTE = "COUNT_ID_ATTRIBUTE";
	
	/**
	 * Counts the elements that match the given {@link URI} and options
	 * @param properties additional count properties
	 * @throws PersistenceException thrown on lower level errors 
	 */
	long count(Map<Object, Object> properties) throws PersistenceException;
	
	/**
	 * Checks if an element exists 
	 * @param properties additional exist properties
	 * @return <code>true</code> if at least an element exists, otherwise <code>false</code>
	 * @throws PersistenceException thrown on lower level errors 
	 */
	boolean exist(Map<Object, Object> properties) throws PersistenceException;
	
}
