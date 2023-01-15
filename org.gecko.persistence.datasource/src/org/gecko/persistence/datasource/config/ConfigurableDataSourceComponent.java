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

import static org.gecko.persistence.datasource.DataSourceConstants.DATASOURCE_CONFIGURABLE;
import static org.gecko.persistence.datasource.DataSourceConstants.DATASOURCE_DRIVER_CLASS_PREFIX;
import static org.gecko.persistence.datasource.DataSourceConstants.DATASOURCE_PREFIX;
import static org.gecko.persistence.datasource.DataSourceConstants.DATASOURCE_TYPE;
import static org.gecko.persistence.datasource.DataSourceConstants.FACTORY_PID;
import static org.gecko.persistence.datasource.DataSourceConstants.PROP_DATASOURCE_DELEGATE;
import static org.gecko.persistence.datasource.DataSourceConstants.PROP_DATASOURCE_NAME;
import static org.gecko.persistence.datasource.DataSourceConstants.PROP_DATASOURCE_TYPE;
import static org.gecko.persistence.datasource.DataSourceConstants.PROP_DIALECT_NAME;
import static org.osgi.service.jdbc.DataSourceFactory.OSGI_JDBC_DRIVER_CLASS;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.gecko.persistence.datasource.dialect.Dialect;
import org.gecko.persistence.datasource.dialect.DialectProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jdbc.DataSourceFactory;

/**
 * Component to configure the {@link ConfigurableDatasourceFactory} and register it as OSGi service
 * @author Mark Hoffmann
 * @since 14.01.2023
 */
@Component(configurationPid = FACTORY_PID, configurationPolicy = ConfigurationPolicy.REQUIRE, property = {DATASOURCE_TYPE})
public class ConfigurableDataSourceComponent {
	
	private final Map<String, Object> configurationProperties = new HashMap<>();
	private Map<String, Object> delegateProperties = new HashMap<>();
	private DataSourceFactory  delegate = null;
	private ServiceRegistration<DataSourceFactory> cdsfRegistration;
	private ConfigurableDatasourceFactory dataSourceFactory;
	private BundleContext ctx;
	@Reference
	private DialectProvider dialectProvider;
	private String configurationName;
	private String dialectName;
	
	@Activate
	public void activate(Map<String, Object> configMap, BundleContext ctx) throws ConfigurationException {
		System.out.println("activated");
		this.ctx = ctx;
		updateDataSourceFactoryProperties(configMap);
		registerDataSourceFactoryService();
	}
	
	@Modified
	public void modified(Map<String, Object> configMap) throws ConfigurationException {
		System.out.println("modifies");
		updateDataSourceFactoryProperties(configMap);
		updateDataSourceFactoryService();
	}
	
	@Deactivate
	public void deactivate() {
		System.out.println("deactivate");
		shutdownDataSourceFactory();
	}
	
	@Reference(name=PROP_DATASOURCE_DELEGATE)
	public void setDelegate(DataSourceFactory delegate, Map<String, Object> properties) {
		System.out.println("set delegate");
		this.delegate = delegate;
		this.delegateProperties = properties;
		try {
			updateDataSourceFactoryProperties(null);
			registerDataSourceFactoryService();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void unsetDelegate(DataSourceFactory delegate) {
		System.out.println("unset delegate");
		shutdownDataSourceFactory();
	}

	private void updateDataSourceFactoryProperties(Map<String, Object> configurationMap) throws ConfigurationException {
		if (Objects.isNull(configurationMap)) {
			return;
		}
		synchronized (this.configurationProperties) {
			configurationProperties.clear();
			configurationProperties.putAll(configurationMap);
			configurationName = (String) this.configurationProperties.get(PROP_DATASOURCE_NAME);
			dialectName = (String) this.configurationProperties.get(PROP_DIALECT_NAME);
			if (dialectName == null) {
				dialectName = "default";
				this.configurationProperties.put(PROP_DIALECT_NAME, dialectName);
			}
		}
		if (configurationName == null || configurationName.isBlank() || configurationName.isEmpty()) {
			throw new ConfigurationException(PROP_DATASOURCE_NAME, "Configuration name must be set");
		}
	}

	/**
	 * Creates a new data source factory, if not already exists.
	 * Its mandatory, that configuration name and dialect name exist, otherwise the component is not already acitvated.
	 */
	private boolean createDataSourceFactory() {
		if (dataSourceFactory == null && 
				Objects.nonNull(configurationName) 
				&& Objects.nonNull(dialectName)) {
			dataSourceFactory = new ConfigurableDatasourceFactory();
			dataSourceFactory.setConfigurationName(configurationName);
			Dialect dialect = dialectProvider.getDialect(dialectName);
			dataSourceFactory.setDialect(dialect);
			return true;
		}
		return false;
	}
	
	private boolean updateDataSourceFactory() {
		boolean changed = false;
		if (dataSourceFactory != null) {
			if (Objects.nonNull(configurationName) 
					&& !configurationName.equals(dataSourceFactory.getConfigurationName())) {
				dataSourceFactory.setConfigurationName(configurationName);
				changed = true;
			}
			Properties properties = createProperties();
			if (!dataSourceFactory.getProperties().equals(properties)) {
				dataSourceFactory.setProperties(properties);
				changed &= true;
			}
			if (Objects.nonNull(dialectName) && !dialectName.equals(dataSourceFactory.getDialect().getName())) {
				Dialect dialect = dialectProvider.getDialect(dialectName);
				dataSourceFactory.setDialect(dialect);
				changed &= true;
			}
		}
		return changed;
	}

	/**
	 * Shuts the {@link DataSourceFactory} down
	 */
	private void shutdownDataSourceFactory() {
		if (cdsfRegistration != null) {
			cdsfRegistration.unregister();
			cdsfRegistration = null;
			dataSourceFactory = null;
			synchronized (configurationProperties) {
				configurationProperties.clear();
			}
		}
	}

	/**
	 * Registers the DataSourceFactory as service
	 */
	private void registerDataSourceFactoryService() {
		if (createDataSourceFactory()) {
			Properties properties = createProperties();
			dataSourceFactory.setDelegate(delegate);
			dataSourceFactory.setProperties(properties);
			cdsfRegistration = ctx.registerService(DataSourceFactory.class, dataSourceFactory, getServiceProperties());
		}
	}

	/**
	 * Updates the service properties with current values
	 */
	private void updateDataSourceFactoryService() {
		if (cdsfRegistration != null && updateDataSourceFactory()) {
			Dictionary<String,Object> properties = getServiceProperties();
			cdsfRegistration.setProperties(properties);
		}
		
	}

	/**
	 * Updates the given Properties given by the caller and additional configuration properties to 
	 * one properties instance
	 * @return a new {@link Properties} instance
	 */
	private synchronized Properties createProperties() {
		Map<String, Object> delegateMap = new HashMap<>(delegateProperties);
		Map<String, Object> configurationMap = new HashMap<>(configurationProperties);

		Properties properties = new Properties();
		// first put the delegate properties
		delegateMap.entrySet().stream().forEach(e->properties.setProperty((String) e.getKey().replace(DATASOURCE_PREFIX, ""), e.getValue().toString()));
		// second put the configuration properties
		configurationMap.entrySet().
			stream().
			filter(e->e.getKey().startsWith(DATASOURCE_PREFIX)).
			forEach(e->properties.setProperty((String) e.getKey().replace(DATASOURCE_PREFIX, ""), e.getValue().toString()));
		return properties;
	}
	
	private Dictionary<String, Object> getServiceProperties() {
		Dictionary<String, Object> serviceProps = new Hashtable<>();
		String configurationName = dataSourceFactory.getConfigurationName(); 
		serviceProps.put(PROP_DATASOURCE_NAME, configurationName);
		serviceProps.put(PROP_DATASOURCE_TYPE, DATASOURCE_CONFIGURABLE);
		serviceProps.put(OSGI_JDBC_DRIVER_CLASS, DATASOURCE_DRIVER_CLASS_PREFIX + configurationName);
		return serviceProps;
	}
}
