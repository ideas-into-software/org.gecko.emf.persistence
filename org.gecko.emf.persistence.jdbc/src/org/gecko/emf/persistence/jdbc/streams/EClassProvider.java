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
package org.gecko.emf.persistence.jdbc.streams;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * 
 * @author mark
 * @since 19.06.2022
 */
public interface EClassProvider {

	/**
	 * Finds the EClass for the given URI in the {@link ResourceSet}.
	 * This Method does not simply call {@link ResourceSet#getEObject(URI, boolean)}. 
	 * It looks directly in the PackageRegistry of the {@link ResourceSet} and only tries to load something,
	 * if nothing is found. 
	 * 
	 * @param resourceSet the resource set used to locate the EClass 
	 * @param eClassURI the URI of the EClass
	 * @return the EClass instance for the given URI
	 */
	EClass getEClassFromResourceSet(ResourceSet resourceSet, String eClassURI);

	/**
	 * Finds the EClass for the given URI
	 * 
	 * @param resourceSet the resource set used to locate the EClass if it was not
	 *          found in the cache
	 * @param eClassURI the URI of the EClass
	 * @return the EClass instance for the given URI
	 */
	EClass getEClass(ResourceSet resourceSet, String eClassURI);
	
	/**
	 * Returns the name of the type column or <code>null</code>
	 * @return the name of the type column or <code>null</code>
	 */
	String getTypeColumn();
	
	/**
	 * Returns the column name of the id field or <code>null</code>
	 * @return the column name of the id field or <code>null</code>
	 */
	String getIDColumn();
	
	/**
	 * Returns the EClass from  the initial configuration. Can be <code>null</code>
	 * @return the EClass from  the initial configuration or <code>null</code>
	 */
	EClass getConfiguredEClass();
	
}