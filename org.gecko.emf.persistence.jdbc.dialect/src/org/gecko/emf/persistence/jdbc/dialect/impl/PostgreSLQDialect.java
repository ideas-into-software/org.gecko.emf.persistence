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
package org.gecko.emf.persistence.jdbc.dialect.impl;

import java.util.Properties;

import org.gecko.emf.persistence.jdbc.dialect.Dialect;

/**
 * 
 * @author mark
 * @since 11.01.2023
 */
public class PostgreSLQDialect implements Dialect {
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.dialect.Dialect#driverPropertiesNull()
	 */
	@Override
	public boolean driverPropertiesNull() {
		return true;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.dialect.Dialect#getName()
	 */
	@Override
	public String getName() {
		return "postgresql";
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.dialect.Dialect#filterProperties(java.util.Properties, boolean)
	 */
	@Override
	public Properties filterProperties(Properties properties, boolean driver) {
		return driver && driverPropertiesNull() ? null : properties;
	}

}
