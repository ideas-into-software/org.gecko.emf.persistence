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
package org.gecko.persistence.derby;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource40;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.derby.jdbc.EmbeddedDriver;
import org.apache.derby.jdbc.EmbeddedXADataSource;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jdbc.DataSourceFactory;

/**
 * 
 * @author mark
 * @since 16.04.2022
 */
@Component(immediate = true, configurationPid = "org.gecko.datasource", configurationPolicy = ConfigurationPolicy.REQUIRE)
public class JDBCProvider implements DataSourceFactory {
	
	/* 
	 * (non-Javadoc)
	 * @see org.osgi.service.jdbc.DataSourceFactory#createDataSource(java.util.Properties)
	 */
	@Override
	public DataSource createDataSource(Properties props) throws SQLException {
		return new EmbeddedDataSource();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.osgi.service.jdbc.DataSourceFactory#createConnectionPoolDataSource(java.util.Properties)
	 */
	@Override
	public ConnectionPoolDataSource createConnectionPoolDataSource(Properties props) throws SQLException {
		return new EmbeddedConnectionPoolDataSource();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.osgi.service.jdbc.DataSourceFactory#createXADataSource(java.util.Properties)
	 */
	@Override
	public XADataSource createXADataSource(Properties props) throws SQLException {
		return new EmbeddedXADataSource();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.osgi.service.jdbc.DataSourceFactory#createDriver(java.util.Properties)
	 */
	@Override
	public Driver createDriver(Properties props) throws SQLException {
		return new EmbeddedDriver();
	}

}
