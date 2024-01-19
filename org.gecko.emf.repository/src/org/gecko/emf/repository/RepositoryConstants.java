/**
 * Copyright (c) 2012 - 2018 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.repository;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;

/**
 * Constants for the EMF Repository
 * @author Juergen Albert
 * @since 15 Feb 2018
 */
public class RepositoryConstants {

	/** The namepace of the repository Capabilities */
	public static final String CAPABILITY_NAMESPACE = "org.gecko.emf.repository";

	/** usually the {@link EClass#getName} is used as part of the {@link URI}. This option will works as a replacement*/
	public static final String URI_HINT = "uri.hint";

	/** This option will be used as a prefix to any given {@link EClass} name in the {@link URI} or its substitute */
	public static final String URI_HINT_PREFIX = "uri.hint.prefix";
	
	/** This option will be used as a sufix to any given {@link EClass} name in the {@link URI} or its substitute */
	public static final String URI_HINT_SUFIX = "uri.hint.sufix";
	
	/** 
	 * If this option is set to <code>true</code> a full EMF Object validation is performed before an object is saved. 
	 * Note that a copy of each object will be made and validated.  
	 * */
	public static final String FULL_CONSTRAINT_VALIDATION = "full.contraint.validation";
	
}
