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
package org.gecko.emf.persistence.jdbc;

import org.gecko.emf.osgi.EMFNamespaces;

/**
 * 
 * @author mark
 * @since 16.04.2022
 */
public interface JdbcPersistenceConstants {
	
	public static final String PERSISTENCE_FILTER = "(persistence=jdbc)";
	public static final String PERSISTENCE_FILTER_PROP = "persistence=jdbc";
	
	public static final String RESOURCESET_CONFIG_NAME = "emf.persistence.jdbc";
	public static final String RESOURCESET_CONFIG_PROP = EMFNamespaces.EMF_CONFIGURATOR_NAME + "=" + RESOURCESET_CONFIG_NAME;

	public static final String ECLASS_TYPE_COLUMN_NAME = "ETYPE";
}
