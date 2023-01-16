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

import java.util.Collections;
import java.util.Map;

import org.osgi.service.jdbc.DataSourceFactory;

/**
 * Simple holder for {@link DataSourceFactory} and properties
 * @author Mark Hoffmann
 * @since 15.01.2023
 */
public class DataSourceFactoryHolder {
	
	final DataSourceFactory dataSourceFactory;
	final Map<String, Object> properties;

	/**
	 * Creates a new instance.
	 */
	public DataSourceFactoryHolder(DataSourceFactory factory, Map<String, Object> properties) {
		this.dataSourceFactory = factory;
		this.properties = properties != null ? properties : Collections.emptyMap();
	}

	/**
	 * Returns the dataSourcefactory.
	 * @return the dataSourcefactory
	 */
	public DataSourceFactory getDataSourceFactory() {
		return dataSourceFactory;
	}

	/**
	 * Returns the properties or an empty map.
	 * @return the properties or an empty map.
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}
}