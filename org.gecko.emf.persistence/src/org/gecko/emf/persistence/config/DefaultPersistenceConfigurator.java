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
package org.gecko.emf.persistence.config;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gecko.emf.persistence.config.model.DatabaseModel;
import org.gecko.emf.persistence.config.model.InstanceModel;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.annotations.RequireConfigurationAdmin;

/**
 * Default persistence configuration
 * @author Mark Hoffmann
 * @since 21.02.2023
 */
@RequireConfigurationAdmin
public abstract class DefaultPersistenceConfigurator {

	private static final Logger logger = Logger.getLogger(DefaultPersistenceConfigurator.class.getName());
	private volatile Map<String, Configuration> engineConfigMap = new ConcurrentHashMap<>();
	private volatile Map<String, Configuration> dbConfigMap = new ConcurrentHashMap<>();
	private volatile Map<String, DatabaseModel> dbModelMap = new ConcurrentHashMap<>();
	private ConfigurationAdmin configAdmin;

	/**
	 * Sets the configAdmin.
	 * @param configAdmin the configAdmin to set
	 */
	public void setConfigAdmin(ConfigurationAdmin configAdmin) {
		this.configAdmin = configAdmin;
	}

	/**
	 * Returns the configAdmin.
	 * @return the configAdmin
	 */
	public ConfigurationAdmin getConfigAdmin() {
		return configAdmin;
	}

	/**
	 * Component activation method
	 * @param properties configuration properties
	 * @throws ConfigurationException
	 */
	protected void activate(Map<Object, Object> properties) throws ConfigurationException {
		Map<Object, Object> props = doActivate(properties);
		Set<InstanceModel> configs = createInstanceModel(props);
		if (configs == null) {
			throw new ConfigurationException("createInstanceConfigurations", "The returning Set, of the createInstanceConfigurations must not be null!");
		}
		configs.forEach(this::setupInstanceConfiguration);
	}
	
	/**
	 * User should extend this, to implement additional behavior for activation
	 * @param properties configuration properties
	 * @return the modified properties or the original one
	 */
	protected Map<Object, Object> doActivate(Map<Object, Object> properties) throws ConfigurationException {
		return properties;
	}

	/**
	 * Component modification method
	 * @param properties configuration properties
	 * @throws ConfigurationException
	 */
	protected void modified(Map<Object, Object> properties) throws ConfigurationException {
		deactivate(properties);
		activate(properties);
	}

	/**
	 * Component deactivation method
	 * @param properties configuration properties
	 * @throws ConfigurationException
	 */
	protected void deactivate(Map<Object, Object> properties) throws ConfigurationException {
		Map<Object, Object> props = doDeactivate(properties);
		Set<InstanceModel> configs = createInstanceModel(props);
		if (configs == null) {
			throw new ConfigurationException("createInstanceConfigurations", "The returning Set, of the createInstanceConfigurations must not be null!");
		}
		configs.forEach(this::teardownModel);
	}

	/**
	 * User should extend this, to implement additional behavior for de-activation
	 * @param properties configuration properties
	 * @return the modified properties or the original one
	 */
	protected Map<Object, Object> doDeactivate(Map<Object, Object> properties) {
		return properties;
	}

	/**
	 * Default factory method to create the {@link InstanceModel} objects
	 * @param properties the configuration properties
	 * @return {@link Set} of {@link InstanceModel} or emmpty set
	 * @throws ConfigurationException
	 */
	protected Set<InstanceModel> createInstanceModel(Map<Object, Object> properties) throws ConfigurationException {
		return InstanceModel.createInstanceModels(properties);
	}

	/**
	 * Default implementation to setup an instance
	 * @param instanceModel the instance configuration to be used
	 */
	protected void setupInstanceConfiguration(InstanceModel instanceModel) {
		requireNonNull(instanceModel);
		InstanceModel im = doSetupInstanceModel(instanceModel);
		requireNonNull(im);
		im.getDatabaseModels().forEach(this::createDatabaseConfig);
	}
	
	/**
	 * Implements the setup for an instance configuration model
	 * @param instanceModel the configuration model instance
	 */
	protected InstanceModel doSetupInstanceModel(InstanceModel instanceModel) {
		return instanceModel;
	}
	
	/**
	 * Implements the database related setup
	 * @param databaseModel the database configuration
	 */
	protected abstract Configuration doSetupDatabaseConfiguration(DatabaseModel databaseModel);
	
	/**
	 * Implements the persistence engine related setup
	 * @param databaseConfiguration the database configuration the engine belongs to
	 */
	protected abstract Configuration doSetupEngineConfiguration(DatabaseModel databaseConfiguration);
	
	/**
	 * Tears down an instance configuration model
	 * @param instanceModel the configuration model instance
	 */
	protected void teardownModel(InstanceModel instanceModel) {
		try {
			if (instanceModel == null) {
				logger.warning("For a null configuration is nothing to revert");
				return;
			}
			doTeardownConfiguration(instanceModel);
			instanceModel.getDatabaseModels().forEach(this::removeDatabaseConfig);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Cannot revert configurations for " + instanceModel.getName(), e);
		}
	}

	/**
	 * Addition teardown method for the instance configuration to be implemented by extender
	 * @param configuration the instance configuration
	 */
	protected void doTeardownConfiguration(InstanceModel configuration) {
	}

	/**
	 * Addition teardown method for the database configuration to be implemented by extender
	 * @param databaseConfiguration the database configuration
	 */
	protected void doTeardownDatabaseConfiguration(DatabaseModel databaseConfiguration) {
	}
	
	/**
	 * Addition teardown method for the persistence engine configuration to be implemented by extender
	 * @param databaseConfiguration the database configuration
	 */
	protected void doTeardownEngineConfiguration(DatabaseModel databaseConfiguration) {
	}

	/**
	 * Create the {@link Configuration} for the database configuration model
	 * @param databaseModel
	 */
	private void createDatabaseConfig(DatabaseModel databaseModel) {
		String dbName = databaseModel.getFqn();
		Configuration c = doSetupDatabaseConfiguration(databaseModel);
		if (nonNull(c)) {
			dbConfigMap.put(databaseModel.getFqn(), c);
		}
		c = doSetupEngineConfiguration(databaseModel);
		if (nonNull(c)) {
			engineConfigMap.put(databaseModel.getFqn(), c);
		}
		dbModelMap.put(dbName, databaseModel);
	}
	
	/**
	 * Create the {@link Configuration} for the database configuration model
	 * @param databaseModel
	 */
	private void removeDatabaseConfig(DatabaseModel databaseModel) {
		String dbName = databaseModel.getFqn();
		dbModelMap.remove(dbName);
		Configuration config = dbConfigMap.remove(dbName);
		if (config != null) {
			try {
				config.delete();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Cannot delete database configuration for " + dbName, e);
			}
			doTeardownDatabaseConfiguration(databaseModel);
		}
		config = engineConfigMap.remove(dbName);
		if (config != null) {
			try {
				config.delete();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Cannot delete persistence engine configuration for " + dbName, e);
			}
			doTeardownEngineConfiguration(databaseModel);
		}
	}


}
