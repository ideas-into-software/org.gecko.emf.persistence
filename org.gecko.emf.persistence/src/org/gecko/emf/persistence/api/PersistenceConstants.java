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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.gecko.emf.persistence.mapping.InputContentHandler;

/**
 * Constants for the EMF Repository
 * @author Juergen Albert
 * @since 15 Feb 2018
 */
public class PersistenceConstants {

	/** The namepace of the repository Capabilities */
	public static final String CAPABILITY_NAMESPACE = "org.gecko.emf.repository";
	
	/** The {@link ExtendedMetaData} name space  */
	public static final String EXTENDED_METADATA_NAMESPACE = "emf.persistence";
	
	/** The {@link ExtendedMetaData} name space key */
	public static final String EXTENDED_METADATA_NAMESPACE_KEY = "namespace";
	
	/** The {@link ExtendedMetaData} name key  */
	public static final String EXTENDED_METADATA_NAME_KEY = "name";

	/** The annotation source for a replacement of the {@link EClass#getName}, that is used as part of the {@link URI}. */
	public static final String URI_HINT = "uri.hint";
	
	/** The {@link EAnnotation} name key for the URI_HINT source */
	public static final String URI_HINT_NAME_KEY = "name";

	/** This option will be used as a prefix to any given {@link EClass} name in the {@link URI} or its substitute */
	public static final String URI_HINT_PREFIX = "uri.hint.prefix";
	
	/** This option will be used as a sufix to any given {@link EClass} name in the {@link URI} or its substitute */
	public static final String URI_HINT_SUFFIX = "uri.hint.suffix";
	
	/** General property for a database name */
	public static final String PROPERTY_DATABASE_NAME = "databaseName";
	
	/** General property for a persistence name */
	public static final String PROPERTY_PERSISTENCE_NAME = "persistence.name";
	
	/** 
	 * If this option is set to <code>true</code> a full EMF Object validation is performed before an object is saved. 
	 * Note that a copy of each object will be made and validated.  
	 * */
	public static final String FULL_CONSTRAINT_VALIDATION = "full.contraint.validation";
	
	/** 
	 * Defines the service property key, that defines {@link InputContentHandler} type
	 */
	public static final String INPUT_CONTENT_HANDLER_PROP = "pushstream";
	
	public static final String ECLASS_TYPE_COLUMN_NAME = "ETYPE";
	
	public static final String PERSISTENCE_TYPE_PROPERTIES = "type=persistence";
	public static final String PERSISTENCE_TYPE_FILTER = "(" + PERSISTENCE_TYPE_PROPERTIES + ")";
	
	
	
}
