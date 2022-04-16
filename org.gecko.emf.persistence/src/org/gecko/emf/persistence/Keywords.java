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

/**
 * 
 * @author mark
 * @since 26.03.2022
 */
public interface Keywords {
	
	/**
	 * ID field identifier. Not intended to be used by clients.
	 */
	public static final String ID_KEY = "_id";
	/**
	 * eClass field identifier. Not intended to be used by clients.
	 */
	public static final String ECLASS_KEY = "_type";
	/**
	 * eProxyURI field identifier. Not intended to be used by clients.
	 */
	public static final String PROXY_KEY = "_eProxyURI";
	/**
	 * super-type array field identifier. Not intended to be used by clients.
	 */
	public static final String SUPER_TYPES_KEY = "_superTypes";
	/**
	 * Extrensic ID field identifier. Not intended to be used by clients.
	 */
	public static final String EXTRINSIC_ID_KEY = "_eId";
	/**
	 * Timestamp field identifier. Not intended to be used by clients.
	 */
	public static final String TIME_STAMP_KEY = "_timeStamp";

	public static final String CAPABILITY_NAMESPACE = "org.geckoprojects.emf.persistence";
	
	public static final String CAPABILITY_EXTENSION_NAMESPACE = "org.geckoprojects.emf.persistence.extension";

	public static final String CAPABILITY_VERSION = "1.0";

}
