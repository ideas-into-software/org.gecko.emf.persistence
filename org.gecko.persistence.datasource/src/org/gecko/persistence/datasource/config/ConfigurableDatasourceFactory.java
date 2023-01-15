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
package org.gecko.persistence.datasource.config;

import static org.gecko.persistence.datasource.DataSourceConstants.DATASOURCE_PREFIX;

import java.sql.Driver;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.gecko.persistence.datasource.dialect.Dialect;
import org.osgi.service.jdbc.DataSourceFactory;

/**
 * A configurable {@link DataSourceFactory}, that uses the configurator to provide default settings.
 * Beside the mandatory properties, vendor specific properties can also be provided by prefixing
 * the property key with 'datasource.' 
 * The properties for the delegate {@link DataSourceFactory} are combined from, 
 * <li>Method parameter {@link Properties}</li>
 * <li>Delegate-{@link DataSourceFactory} service properties</li>
 * <li>Component-Configuration {@link Properties}</li>
 * 
 * Where the method properties have the highest priority and overload everything. 
 * After that configuration properties come and overwrite delegate service properties with the same key  
 * @author Mark Hoffmann
 * @since 11.01.2023
 */
public class ConfigurableDatasourceFactory implements DataSourceFactory {

	private final AtomicReference<DataSourceFactory>  delegateRef = new AtomicReference<>();
	private final AtomicReference<Properties> delegatePropertiesRef = new AtomicReference<>();
	private final AtomicReference<Dialect> dialectRef = new AtomicReference<>();
	private final AtomicReference<String> configurationNameRef = new AtomicReference<>();;

	/**
	 * Creates a new instance.
	 */
	ConfigurableDatasourceFactory() {
	}
	
	ConfigurableDatasourceFactory(String configurationName, DataSourceFactory delegate, Properties delegateProperties, Dialect dialect) {
		setConfigurationName(configurationName);
		setDelegate(delegate);
		setProperties(delegateProperties);
		setDialect(dialect);
	}
	
	/**
	 * Returns the configuration name.
	 * @return the configuration name 
	 */
	String getConfigurationName() {
		return configurationNameRef.get();
	}
	
	void setConfigurationName(String configurationName) {
		Objects.requireNonNull(configurationName);
		String currentName = configurationNameRef.get();
		configurationNameRef.compareAndSet(currentName, configurationName);
	}
	
	Dialect getDialect() {
		return dialectRef.get();
	}
	
	/**
	 * Sets the dialect.
	 * @param dialect the dialect to set
	 */
	void setDialect(Dialect dialect) {
		Dialect currentDialect = dialectRef.get();
		String currentName = currentDialect == null ? null : currentDialect.getName();
		String dialectName = dialect == null ? null : dialect.getName();
		if ((currentName != null && !currentName.equals(dialectName)) || 
				(dialectName != null && !dialectName.equals(currentName))) {
			dialectRef.set(dialect);
		}
	}
	
	/**
	 * Returns the delegate properties.
	 * @return the delegate properties
	 */
	public Properties getProperties() {
		return delegatePropertiesRef.get();
	}
	
	/**
	 * Sets the data source properties for {@link DataSourceFactory}
	 * @param properties the delegate properties
	 */
	void setProperties(Properties properties) {
		delegatePropertiesRef.set((properties == null ? new Properties() : properties));
	}
	
	/**
	 * Sets the delegate {@link DataSourceFactory}
	 * @param delegate the delegate {@link DataSourceFactory}
	 */
	void setDelegate(DataSourceFactory delegate) {
		DataSourceFactory currentDelegate = delegateRef.get();
		delegateRef.compareAndSet(currentDelegate, delegate);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.osgi.service.jdbc.DataSourceFactory#createDataSource(java.util.Properties)
	 */
	@Override
	public DataSource createDataSource(Properties props) throws SQLException {
		Properties properties = updateProperties(props);
		DataSourceFactory delegate = delegateRef.get();
		if (delegate != null) {
			return delegate.createDataSource(properties);
		} else {
			throw new SQLException("No delegate DataSourceFactory available to create DataSource for configurable datasource factory");
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.osgi.service.jdbc.DataSourceFactory#createConnectionPoolDataSource(java.util.Properties)
	 */
	@Override
	public ConnectionPoolDataSource createConnectionPoolDataSource(Properties props) throws SQLException {
		Properties properties = updateProperties(props);
		DataSourceFactory delegate = delegateRef.get();
		if (delegate != null) {
			return delegate.createConnectionPoolDataSource(properties);
		} else {
			throw new SQLException("No delegate DataSourceFactory available to create ConnectionPoolDataSource for configurable datasource factory");
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.osgi.service.jdbc.DataSourceFactory#createXADataSource(java.util.Properties)
	 */
	@Override
	public XADataSource createXADataSource(Properties props) throws SQLException {
		Properties properties = updateProperties(props);
		DataSourceFactory delegate = delegateRef.get();
		if (delegate != null) {
			return delegate.createXADataSource(properties);
		} else {
			throw new SQLException("No delegate DataSourceFactory available to create XADataSource for configurable datasource factory");
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.osgi.service.jdbc.DataSourceFactory#createDriver(java.util.Properties)
	 */
	@Override
	public Driver createDriver(Properties props) throws SQLException {
		Properties properties = updateProperties(props);
		DataSourceFactory delegate = delegateRef.get();
		if (delegate != null) {
			Dialect dialect = dialectRef.get();
			if (dialect != null ) {
				properties = dialect.filterProperties(properties, true);
			}
			return delegate.createDriver(properties);
		} else {
			throw new SQLException("No delegate DataSourceFactory available to create Driver for configurable datasource factory");
		}
	}

	/**
	 * Updates the given Properties given by the caller and additional configuration properties to 
	 * one properties instance
	 * @param userProperties the properties of the caller
	 * @param configProperties
	 * @return a new {@link Properties} instance
	 */
	private Properties updateProperties(Properties userProperties) {
		// put the user properties in
		Properties delegateProperties = delegatePropertiesRef.get();
		if (userProperties != null) {
			delegateProperties.putAll(userProperties);
		}

		String propPrefix = DATASOURCE_PREFIX + configurationNameRef.get() + ".";
		setSystemEnvProperty(propPrefix, JDBC_USER, delegateProperties);
		setSystemEnvProperty(propPrefix, JDBC_PASSWORD, delegateProperties);


		return delegateProperties;
	}

	private void setSystemEnvProperty(String propEnvPrefix, String key, Properties properties) {
		String propEnvKey = propEnvPrefix + key;
		String value = System.getProperty(propEnvKey);
		if (value != null) {
			if (System.getenv(propEnvKey) != null) {
				value = System.getenv(propEnvKey);
			}
			properties.put(key, value);
		}
	}

}
