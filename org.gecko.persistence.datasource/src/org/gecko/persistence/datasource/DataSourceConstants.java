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
package org.gecko.persistence.datasource;

/**
 * Constants
 * @author Mark Hoffmann
 * @since 14.01.2023
 */
public interface DataSourceConstants {

	public static final String DATASOURCE_PREFIX = "datasource.";
	public static final String PROP_DATASOURCE_TYPE = DATASOURCE_PREFIX + "type";
	public static final String PROP_DATASOURCE_DELEGATE = DATASOURCE_PREFIX + "delegate";
	public static final String PROP_DATASOURCE_NAME = DATASOURCE_PREFIX + "name";
	public static final String PROP_DATASOURCE_DRIVER = DATASOURCE_PREFIX + "driver";
	public static final String PROP_DIALECT_NAME = DATASOURCE_PREFIX + "dialect";

	public static final String FACTORY_PID = "org.gecko.datasource";
	
	public static final String DATASOURCE_CONFIGURABLE = "configurable";
	public static final String DATASOURCE_DRIVER_CLASS_PREFIX = PROP_DATASOURCE_DRIVER + "." + DATASOURCE_CONFIGURABLE + ".Driver";
	public static final String DATASOURCE_TYPE = PROP_DATASOURCE_TYPE + "=" + DATASOURCE_CONFIGURABLE;
	public static final String DATASOURCE_TYPE_FILTER = "(" + PROP_DATASOURCE_TYPE + "=" + DATASOURCE_CONFIGURABLE + ")";
	
	
}
