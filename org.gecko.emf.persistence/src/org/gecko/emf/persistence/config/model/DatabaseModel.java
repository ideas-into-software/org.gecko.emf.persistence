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
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_DATABASES;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_PASSWORD;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_PROPERTIES;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_USER;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.osgi.service.cm.ConfigurationException;

/**
 * 
 * @author mark
 * @since 17.02.2023
 */
public class DatabaseModel extends BasicModel {

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.config.model.BasicConfiguration#getParent()
	 */
	@Override
	public InstanceModel getParent() {
		return (InstanceModel)super.getParent();
	}
	
	/**
	 * Returns the databases array for a databases config of an instance
	 * @return the array of databases
	 * @throws ConfigurationException thrown, if databases key wasn't found in the instance properties 
	 */
	public static String[] getDatabases(String instance, Map<Object, Object> instanceProperties) throws ConfigurationException {
		requireNonNull(instance, "The instance name must not be null");
		requireNonNull(instanceProperties, "The properties map must no be null");
		String instancePrefix = instance.isEmpty() ? instance :  instance.isBlank() ? "" : instance  + ".";
		String databases = (String) instanceProperties.get(instancePrefix + PROP_DATABASES);
		if (databases == null) {
			return new String[0];
		}
		if (databases != null && !databases.isEmpty() && !databases.isBlank()) {
			if (databases.startsWith(",")) {
				databases = databases.substring(1);
			}
			return databases.split(",");
		} else {
			throw new ConfigurationException(instancePrefix + PROP_DATABASES, "No 'persistence.databases' property found in configuration properties");
		}
	}
	
	/**
	 * Returns all instance configurations that are configured in the given properties map
	 * @param properties the source properties
	 * @return a {@link Set} of {@link InstanceModel}
	 * @throws ConfigurationException
	 */
	public static Set<DatabaseModel> createDatabaseModels(String instance, Map<Object, Object> properties) throws ConfigurationException {
		requireNonNull(instance, "The instance name must not be null");
		requireNonNull(properties, "The properties map must no be null");
		String[] databases = getDatabases(instance, properties); 
		Set<DatabaseModel> databaseConfigs = new HashSet<>();
		for (String database : databases) {
			try {
				DatabaseModel config = createDatabaseModel(instance, database, properties);
				databaseConfigs.add(config);
			} catch (Exception e) {
				if (e instanceof ConfigurationException) {
					throw e;
				} else {
					throw new ConfigurationException(PROP_DATABASES, "Error creating database configuration for instance: " + instance + " and database: " + database, e);
				}
			}
		}
		return databaseConfigs;
	}

	/**
	 * Creates a {@link InstanceModel} for the given instance name and properities
	 * @param instance the instance name
	 * @param instanceProperties the properties to get data from
	 * @return the {@link InstanceModel}
	 * @throws ConfigurationException
	 */
	public static DatabaseModel createDatabaseModel(String instance, String database, Map<Object, Object> instanceProperties) throws ConfigurationException {
		requireNonNull(instance, "The instance name must not be null");
		requireNonNull(instanceProperties, "The properties map must no be null");
		if(database == null) {
			return null;
		}
		if (instance.isBlank() || instance.isEmpty()) {
			throw new ConfigurationException(instance, "Instance value must not be empty or blank");
		}
		if (database.isBlank() || database.isEmpty()) {
			throw new ConfigurationException(database, "Database value must not be empty or blank");
		}
		try {
			DatabaseModel config = new DatabaseModel();
			String instancePrefix = instance + ".";
			String databasePrefix = instancePrefix + database + ".";
			config.setName(instancePrefix + database);
			String user = (String) instanceProperties.get(databasePrefix + PROP_USER);
			String pwd = (String) instanceProperties.get(databasePrefix + PROP_PASSWORD);

			if (user != null) {
				config.setUsername(user);
			}
			if (pwd != null) {
				config.setPassword(pwd);
			}
			String propertiesPrefix = databasePrefix + PROP_PROPERTIES + ".";
			config.addProperties(BasicModel.createProperties(propertiesPrefix, instanceProperties));
			return config;
		} catch (IllegalStateException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException("Error creating instance configuration for instance: " + instance, e);
		}
	}

}
