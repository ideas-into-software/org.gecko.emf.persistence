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
package org.gecko.emf.persistence.jdbc.dialect;

import java.util.Properties;

import org.osgi.annotation.versioning.ProviderType;

/**
 * 
 * @author mark
 * @since 11.01.2023
 */
@ProviderType
public interface Dialect {
	
	String getName();
	
	/**
	 * Returns <code>true</code>, if the driver creation expects <code>null</code> value for properties.
	 * Otherwise properties are allowed to be non <code>null</code>
	 * @return <code>true</code>, if no properties are allowed for driver creation
	 */
	boolean driverPropertiesNull();

	/**
	 * @param properties the datasource properties
	 * @param driver <code>true</code>, if the filter should run for driver creation
	 * @return the filtered properties or maybe <code>null</code>
	 */
	Properties filterProperties(Properties properties, boolean driver);

}
