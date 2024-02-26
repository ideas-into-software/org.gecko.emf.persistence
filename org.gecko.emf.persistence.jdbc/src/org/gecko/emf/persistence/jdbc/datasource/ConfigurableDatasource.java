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
package org.gecko.emf.persistence.jdbc.datasource;

import static org.gecko.emf.persistence.jdbc.JdbcPersistenceConstants.DATASOURCE_PREFIX;
import static org.gecko.emf.persistence.jdbc.JdbcPersistenceConstants.PROP_DATASOURCE_NAME;
import static org.gecko.emf.persistence.jdbc.JdbcPersistenceConstants.PROP_DIALECT_NAME;

import java.sql.Driver;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.gecko.emf.persistence.jdbc.dialect.Dialect;
import org.gecko.emf.persistence.jdbc.dialect.DialectProvider;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
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
@Component(immediate = true, configurationPid = "org.gecko.datasource", configurationPolicy = ConfigurationPolicy.REQUIRE, property = {"datasource.type=configurable"})
public class ConfigurableDatasource implements DataSourceFactory {

	private static final Set<String> SUPPORTED_PROPERTIES = new HashSet<>();
	private Map<String, Object> configurationMap;
	private AtomicReference<Map<String, Object>> delegatePropertiesRef = new AtomicReference<>();
	private AtomicReference<DataSourceFactory>  delegateRef = new AtomicReference<>();
	private Dialect dialect;
	private String configurationName;
	@Reference
	private DialectProvider dialectProvider;
	
	static {
		SUPPORTED_PROPERTIES.add(JDBC_DATABASE_NAME);
		SUPPORTED_PROPERTIES.add(JDBC_DATASOURCE_NAME);
		SUPPORTED_PROPERTIES.add(JDBC_DESCRIPTION);
		SUPPORTED_PROPERTIES.add(JDBC_INITIAL_POOL_SIZE);
		SUPPORTED_PROPERTIES.add(JDBC_MAX_IDLE_TIME);
		SUPPORTED_PROPERTIES.add(JDBC_MAX_POOL_SIZE);
		SUPPORTED_PROPERTIES.add(JDBC_MAX_STATEMENTS);
		SUPPORTED_PROPERTIES.add(JDBC_MIN_POOL_SIZE);
		SUPPORTED_PROPERTIES.add(JDBC_NETWORK_PROTOCOL);
		SUPPORTED_PROPERTIES.add(JDBC_PASSWORD);
		SUPPORTED_PROPERTIES.add(JDBC_PORT_NUMBER);
		SUPPORTED_PROPERTIES.add(JDBC_PROPERTY_CYCLE);
		SUPPORTED_PROPERTIES.add(JDBC_ROLE_NAME);
		SUPPORTED_PROPERTIES.add(JDBC_SERVER_NAME);
		SUPPORTED_PROPERTIES.add(JDBC_URL);
		SUPPORTED_PROPERTIES.add(JDBC_USER);
	}

	@Activate
	public void activate(Map<String, Object> configMap, BundleContext ctx) throws ConfigurationException {
		this.configurationMap = configMap;
		configurationName = (String) this.configurationMap.get(PROP_DATASOURCE_NAME);
		if (configurationName == null || configurationName.isBlank() || configurationName.isEmpty()) {
			throw new ConfigurationException(PROP_DATASOURCE_NAME, "Configuration name must be set");
		}
		String dialectName = (String) this.configurationMap.getOrDefault(PROP_DIALECT_NAME, "default");
		if (dialectName == null || dialectName.isBlank() || dialectName.isEmpty()) {
			throw new ConfigurationException(PROP_DIALECT_NAME, "Configuration dialect must be set");
		}
		dialect = dialectProvider.getDialect(dialectName);
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
			if (dialect != null ) {
				properties = dialect.filterProperties(properties, true);
			}
			return delegate.createDriver(properties);
		} else {
			throw new SQLException("No delegate DataSourceFactory available to create Driver for configurable datasource factory");
		}
	}

	@Reference(name="datasource.delegate")
	public void setDelegate(DataSourceFactory delegate, Map<String, Object> properties) {
		if (this.delegateRef.compareAndSet(null, delegate)) {
			this.delegatePropertiesRef.set(properties);
		}
	}

	public void unsetDelegate(DataSourceFactory delegate) {
		if (this.delegateRef.compareAndSet(delegate, null)) {
			this.delegatePropertiesRef.set(null);
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
		Map<String, Object> map = delegatePropertiesRef.get();
		Map<String, Object> delegateProperties = map != null ? new HashMap<>(map) : new HashMap<>();
		Map<String, Object> configurationProperties = new HashMap<>(configurationMap);

		Properties properties = new Properties();
		// first put the delegate properties
		delegateProperties.entrySet().stream().forEach(e->properties.setProperty((String) e.getKey().replace(DATASOURCE_PREFIX, ""), e.getValue().toString()));
		// second put the configuration properties
		configurationProperties.entrySet().
			stream().
			filter(e->e.getKey().startsWith(DATASOURCE_PREFIX)).
			forEach(e->properties.setProperty((String) e.getKey().replace(DATASOURCE_PREFIX, ""), e.getValue().toString()));
		// put the user properties in
		if (userProperties != null) {
			properties.putAll(userProperties);
		}
		
		String propPrefix = DATASOURCE_PREFIX + configurationName + ".";
		setSystemEnvProperty(propPrefix, JDBC_USER, properties);
		setSystemEnvProperty(propPrefix, JDBC_PASSWORD, properties);

		
		return properties;
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
