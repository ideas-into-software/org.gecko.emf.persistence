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

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.persistence.api.Countable;
import org.gecko.emf.persistence.api.Options;

/**
 * Interface for a context object for a {@link PersistenceEngine}. It contains all supported features in a typed way
 * @author Mark Hoffmann
 * @since 15.02.2023
 */
public interface EngineContext {
	
	/**
	 * Returns the underlying resource
	 * @return the underlying resource
	 */
	Resource resource();
	
	/**
	 * Returns the resource {@link URI}. Shortcut for {@link Resource#getURI()}
	 * @return the resource {@link URI}
	 */
	URI uri();
	
	/**
	 * Returns response map, to put information back to the resource
	 * @return response map
	 */
	Map<Object, Object> response();
	
	/**
	 * Returns the effective options
	 * @return the effective options
	 */
	Map<Object, Object> effectiveOptions();
	
	/**
	 * Returns the name of the primary key {@link EAttribute}
	 * @return the name of the primary key {@link EAttribute}
	 */
	String idAttributeName();
	
	/**
	 * Returns the identifier for the type information in the database
	 * @return the identifier for the type information in the database
	 */
	String typeColumn();

	/**
	 * Returns the EClass URI as additional hint parameter. Might be <code>null</code>
	 * @return the EClass URI as additional hint parameter
	 */
	String eClassURIHint();
	
	/**
	 * Returns the EClass URI
	 * @return the EClass URI
	 */
	String eClassURI();
	
	/**
	 * Returns the {@link EClass} instance
	 * @return the {@link EClass} instance
	 */
	EClass eClass();
	
	/**
	 * Count filter over the uri-type column. Only instances of of the given EClass URI are counted then.
	 * This may be used in combination with {@link Options#OPTION_ECLASS_IDATTRIBUTE_HINT}. This option must be set as well, to tell the counter implementation,
	 * which id-column to filter against.
	 */
	boolean countIdAttributeFilter();
	
	boolean countTypeFilter();
	
	/**
	 * Returns <code>true</code>, if the matched result should counted. The result will be returned in the response map
	 * @return <code>true</code>, if the matched result should counted
	 */
	boolean countResults();
	
	/**
	 * Returns <code>true</code>, if the request is to count only, instead of return data. The result will be returned in the response map. Eventually the content list might be empty then.
	 * Alternatively, the count call can be used {@link Countable#count(Map)}
	 * @return <code>true</code>, if the request is to count only, instead of return data
	 */
	boolean countOnly();
	
	/**
	 * Returns <code>true</code>, if the ID {@link EAttribute} in a EClass should be used as primary key
	 * @return <code>true</code>, if the ID {@link EAttribute} in a EClass should be used as primary key
	 */
	boolean useIdAttributeAsPrimaryKey();
	
	/**
	 * Instead of updating an object this option force to insert, if set to <code>true</code>
	 * @return of updating an object this option force to insert, if set to <code>true</code>
	 */
	boolean forceInsert();
	
	/**
	 * Returns <code>true</code>, if the resource should be cleared after the insert is done
	 * @return <code>true</code>, if the resource should be cleared after the insert is done
	 */
	boolean clearResourceAfterInsert();
	
}
