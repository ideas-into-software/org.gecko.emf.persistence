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
package org.gecko.emf.persistence.config.model;

import static java.util.Objects.requireNonNull;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_AUTH_SOURCE;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_CONNECTION_URIS;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_INSTANCES;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_PASSWORD;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_PROPERTIES;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_REPOSITORY_TYPE;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_USER;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.RepositoryType;
import org.osgi.service.cm.ConfigurationException;

/**
 * 
 * @author Mark Hoffmann
 * @since 17.02.2023
 */
public class InstanceModel extends BasicModel {

	private RepositoryType repositoryType;
	private String connectionUris;
	private String authSource;
	private Set<DatabaseModel> databases = new HashSet<>();
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.config.model.BasicConfiguration#getFqn()
	 */
	@Override
	public String getFqn() {
		return super.getName();
	}

	/**
	 * @param valueOf
	 */
	public void setRepositoryType(RepositoryType type) {
		this.repositoryType = type;
	}

	/**
	 * Returns the repositoryType.
	 * @return the repositoryType
	 */
	public RepositoryType getRepositoryType() {
		return repositoryType;
	}

	/**
	 * @param connectionUris
	 */
	public void setConnectionUris(String connectionUris) {
		this.connectionUris = connectionUris;
	}

	/**
	 * Returns the connectionUris.
	 * @return the connectionUris
	 */
	public String getConnectionUris() {
		return connectionUris;
	}

	/**
	 * @param authSource
	 */
	public void setAuthenticationSource(String authSource) {
		this.authSource = authSource;
	}

	/**
	 * Returns the authSource.
	 * @return the authSource
	 */
	public String getAuthenticationSource() {
		return authSource;
	}
	
	void updateDatabaseModels(Set<DatabaseModel> configurations) {
		if (configurations == null) {
			return;
		}
		databases.addAll(configurations);
	}

	/**
	 * Returns the databaseConfig.
	 * @return the databaseConfig
	 */
	public Set<DatabaseModel> getDatabaseModels() {
		return databases;
	}
	/**
	 * Returns the instance array for a instance configuration
	 * @return the array of instance names
	 * @throws ConfigurationException thrown, if instance key wasn't found in the instance properties 
	 */
	public static String[] getInstances(Map<Object, Object> properties) throws ConfigurationException {
		String instances = (String) properties.get(PROP_INSTANCES);
		if (instances != null && !instances.isEmpty() && !instances.isBlank()) {
			if (instances.startsWith(",")) {
				instances = instances.substring(1);
			}
			return instances.split(",");
		} else {
			throw new ConfigurationException(PROP_INSTANCES, "No 'persistence.instance' property found in configuration properties");
		}
	}

	/**
	 * Returns all instance configurations that are configured in the given properties map
	 * @param properties the source properties
	 * @return a {@link Set} of {@link InstanceModel}
	 * @throws ConfigurationException
	 */
	public static Set<InstanceModel> createInstanceModels(Map<Object, Object> properties) throws ConfigurationException {
		requireNonNull(properties, "The properties map must no be null");
		String[] instances = getInstances(properties); 
		Set<InstanceModel> instanceConfigs = new HashSet<>();
		for (String instance : instances) {
			try {
				InstanceModel config = createInstanceModel(instance, properties);
				instanceConfigs.add(config);
			} catch (Exception e) {
				if (e instanceof ConfigurationException) {
					throw e;
				} else {
					throw new ConfigurationException(PROP_INSTANCES, "Error creating  instance configuration for instance: " + instance, e);
				}
			}
		}
		return instanceConfigs;
	}

	/**
	 * Creates a {@link InstanceModel} for the given instance name and properities
	 * @param instance the instance name
	 * @param instanceProperties the properties to get data from
	 * @return the {@link InstanceModel}
	 * @throws ConfigurationException
	 */
	public static InstanceModel createInstanceModel(String instance, Map<Object, Object> instanceProperties) throws ConfigurationException {
		requireNonNull(instance, "The instance name must not be null");
		requireNonNull(instanceProperties, "The properties map must no be null");
		if (instance.isBlank() || instance.isEmpty()) {
			throw new ConfigurationException(instance, "Instance value must not be empty or blank");
		}
		try {
			InstanceModel config = new InstanceModel();
			config.setName(instance);
			String instancePrefix = instance + ".";
			String authSource = (String) instanceProperties.get(instancePrefix + PROP_AUTH_SOURCE);
			String user = (String) instanceProperties.get(instancePrefix + PROP_USER);
			String pwd = (String) instanceProperties.get(instancePrefix + PROP_PASSWORD);
			String connectionUris = (String) instanceProperties.get(instancePrefix + PROP_CONNECTION_URIS);
			String repoTypeString = (String) instanceProperties.get(instancePrefix + PROP_REPOSITORY_TYPE);

			config.setConnectionUris(connectionUris);
			if (authSource != null) {
				config.setAuthenticationSource(authSource);
			}
			if (user != null) {
				config.setUsername(user);
			}
			if (pwd != null) {
				config.setPassword(pwd);
			}
			if (repoTypeString != null) {
				RepositoryType repoType = RepositoryType.valueOf(repoTypeString.toUpperCase());
				config.setRepositoryType(repoType);
			}
			Set<DatabaseModel> databaseConfigs = DatabaseModel.createDatabaseModels(instance, instanceProperties);
			config.updateDatabaseModels(databaseConfigs);
			
			String propertiesPrefix = instancePrefix + PROP_PROPERTIES + ".";
			config.addProperties(BasicModel.createProperties(propertiesPrefix, instanceProperties));
			return config;
		} catch (IllegalStateException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException("Error creating instance configuration for instance: " + instance, e);
		}
	}

}
