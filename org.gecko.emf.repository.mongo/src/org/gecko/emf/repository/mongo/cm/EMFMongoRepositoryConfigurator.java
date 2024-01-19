/**
 * Copyright (c) 2012 - 2017 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.repository.mongo.cm;

import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.EMF_MONGO_REPOSITORY_CONFIGURATOR_CONFIGURATION_NAME;
import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.MONGO_AUTH_SOURCE_ENV_PROP;
import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.MONGO_AUTH_SOURCE_PROP;
import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.MONGO_BASEURIS;
import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.MONGO_BASEURIS_ENV;
import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.MONGO_DATABASES;
import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.MONGO_INSTANCE_PROP;
import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.MONGO_PASSWORD_ENV_PROP;
import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.MONGO_PASSWORD_PROP;
import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.MONGO_REPOSITORY_TYPE;
import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.MONGO_USER_ENV_PROP;
import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.MONGO_USER_PROP;
import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.PROTOTYPE_REPOSITORY_CONFIGURATION_NAME;
import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.REPO_RESOURCE_SET_FIELD_TARGET;
import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.REPO_RESOURCE_SET_FILTER;
import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.SINGLETON_REPOSITORY_CONFIGURATION_NAME;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gecko.emf.osgi.UriMapProvider;
import org.gecko.emf.repository.EMFRepository;
import org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.Type;
import org.gecko.emf.repository.mongo.cm.entities.MongoDatabaseConfig;
import org.gecko.emf.repository.mongo.cm.entities.MongoInstanceConfig;
import org.gecko.mongo.osgi.MongoClientProvider;
import org.gecko.mongo.osgi.MongoDatabaseProvider;
import org.gecko.mongo.osgi.configuration.ConfigurationProperties;
import org.osgi.framework.Constants;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Configuration component that brings up a mongo EMF repository, with complete set up of 
 * {@link MongoClient} and {@link MongoDatabase}
 * @author Mark Hoffmann
 * @since 27.07.2017
 */
@Component(name=EMF_MONGO_REPOSITORY_CONFIGURATOR_CONFIGURATION_NAME, immediate=true, configurationPolicy=ConfigurationPolicy.REQUIRE)
public class EMFMongoRepositoryConfigurator {

	private static final Logger logger = Logger.getLogger(EMFMongoRepositoryConfigurator.class.getName());
	private volatile Map<String, Configuration> uriMapConfigurationMap = new ConcurrentHashMap<>();
	private volatile Map<String, Configuration> clientConfigurationMap = new ConcurrentHashMap<>();
	private volatile Map<String, Configuration> databaseConfigurationMap = new ConcurrentHashMap<>();
	private volatile Map<String, MongoDatabaseConfig> mongoDbConfigMap = new ConcurrentHashMap<>();
	private volatile Map<String, Configuration> repositoryConfigurationMap = new ConcurrentHashMap<>();

	@Reference
	private volatile ConfigurationAdmin configAdmin;

	/**
	 * Called on component activation
	 * @param properties the configuration properties
	 * @throws ConfigurationException
	 */
	@Activate
	public void activate(Map<String, Object> properties) throws ConfigurationException {
		String instances = (String) properties.get(MONGO_INSTANCE_PROP);
		Set<MongoInstanceConfig> mongoConfigs = null;
		if(instances != null && !instances.isEmpty()){
			mongoConfigs = createMongoInstanceConfig(instances.split(","), properties);
		} else {
			throw new ConfigurationException(MONGO_INSTANCE_PROP, "No 'mongo.instance' property found in configuration properties for activation");
		}
		mongoConfigs.forEach(c->setupRepositoryConfiguration(c));
	}

	/**
	 * Called on configuration change
	 * @param properties the configuration properties
	 * @throws ConfigurationException 
	 */
	@Modified
	public void modified(Map<String, Object> properties) throws ConfigurationException {
		deactivate(properties);
		activate(properties);
	}

	/**
	 * Called on component de-activation
	 * @param properties the configuration properties
	 * @throws ConfigurationException
	 */
	@Deactivate
	public void deactivate(Map<String, Object> properties) throws ConfigurationException {
		String instances = (String) properties.get(MONGO_INSTANCE_PROP);
		Set<MongoInstanceConfig> mongoConfigs = null;
		if(instances != null && !instances.isEmpty()){
			mongoConfigs = createMongoInstanceConfig(instances.split(","), properties);
		} else {
			throw new ConfigurationException(MONGO_INSTANCE_PROP, "No 'mongo.instance' property found in configuration properties for deactivation");
		}
		mongoConfigs.forEach(c->tearDownConfigurations(c));
	}

	/**
	 * Normalizes the given baseUris String. The baseUris can consist of more than one URI, separated by a ','. The method takes care, that every URI does not end with an '/'
	 * @param baseUris the baseUris to normalize
	 * @return the normalized baseUris
	 */
	private String normalizeBaseUris(String baseUris) {
		StringBuilder response = new StringBuilder();
		for(String uri : baseUris.split("\\s*,\\s*")){
			response.append(",");
			response.append(uri.endsWith("/") ? uri.substring(0, uri.length() - 1) : uri );
		}
		return response.substring(1);
	}

	/**
	 * Registers all the given instances, by gathering the necessary informations from the configuration properties.
	 *
	 * This is a sample configuration: <br>
	 * <code>
	 * mongo.instances=test1,test2
	 * test1.baseUris=mongodb://localhost,mongodb://someotherhost:1234
	 * test1.databases=bla,test
	 * test1.bla.user=john
	 * test1.bla.password=test
	 * 
	 * test2.baseUris=mondodb://123.123.123.123
	 * test2.databases=somethingelse
	 * </code>
	 *
	 * @param instances
	 * 			The MonogDB instance names used in the configuration properties
	 * @param properties the map with all properties
	 * @return {@link Set} of configurations or an empty {@link Set}
	 */
	private Set<MongoInstanceConfig> createMongoInstanceConfig(String[] instances, Map<String, Object> properties) {
		Set<MongoInstanceConfig> instanceConfigs = new HashSet<>();
		if (instances == null || properties == null) {
			logger.warning("No instances or a null property map was given to create mongo instance configurations");
			return instanceConfigs;
		}
		for (int i = 0; i < instances.length; i++) {
			String instance = instances[i];
			try {
				MongoInstanceConfig config = new MongoInstanceConfig();
				config.setInstanceName(instance);
				if(properties.containsKey(instance + "." + MONGO_REPOSITORY_TYPE)) {
					config.setType(Type.valueOf(((String) properties.get(instance + "." + MONGO_REPOSITORY_TYPE)).toUpperCase()));
				}
				String authDB = (String) properties.get(instance + "." + MONGO_AUTH_SOURCE_PROP);
				String authDBEnv = (String) properties.get(instance + "." + MONGO_AUTH_SOURCE_ENV_PROP);
				String user = (String) properties.get(instance + "." + MONGO_USER_PROP);
				String userEnv = (String) properties.get(instance + "." + MONGO_USER_ENV_PROP);
				String pwd = (String) properties.get(instance + "." + MONGO_PASSWORD_PROP);
				String pwdEnv = (String) properties.get(instance + "." + MONGO_PASSWORD_ENV_PROP);
				String baseUris = (String) properties.get(instance + "." + MONGO_BASEURIS);
				String baseUrisEnv = (String) properties.get(instance + "." + MONGO_BASEURIS_ENV);
				String databases =(String) properties.get(instance + "." + MONGO_DATABASES);
				config.setMongoBaseUri(baseUris);
				config.setMongoBaseUriEnvironmentVariable(baseUrisEnv);
				if (authDB != null) {
					config.setAuthDatabase(authDB);
				}
				if(user != null){
					config.setUsername(user);
				}
				if(pwd != null){
					config.setPassword(pwd);
				}
				if (authDBEnv != null) {
					config.setAuthDatabaseEnvironmentVariable(authDBEnv);
				}
				if(userEnv != null){
					config.setUsernameEnvironmentVariable(userEnv);
				}
				if(pwdEnv != null){
					config.setPasswordEnvironmentVariable(pwdEnv);
				}
				for(String database : databases.split(",")){
					MongoDatabaseConfig dbConfig = new MongoDatabaseConfig();
					dbConfig.setName(database.trim());
					user = (String) properties.get(instance + "." + database + "." + MONGO_USER_PROP);
					userEnv = (String) properties.get(instance + "." + database + "." + MONGO_USER_ENV_PROP);
					pwd = (String) properties.get(instance + "." + database + "." + MONGO_PASSWORD_PROP);
					pwdEnv = (String) properties.get(instance + "." + database + "." + MONGO_PASSWORD_ENV_PROP);
					if(properties.containsKey(instance + "." + database + "." + MONGO_REPOSITORY_TYPE)) {
						dbConfig.setType(Type.valueOf(((String) properties.get(instance + "." + database + "." + MONGO_REPOSITORY_TYPE)).toUpperCase()));
					} else {
						dbConfig.setType(config.getType());
					}
					config.getDatabaseConfigs().add(dbConfig);
				}
				instanceConfigs.add(config);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error creating mongo instance configuration for instance: "+ instance, e);
			}
		}
		return instanceConfigs;
	}

	/**
	 * Sets up a repository configuration
	 * @param mongoConfig the mongo instance configuration to set up
	 */
	private void setupRepositoryConfiguration(MongoInstanceConfig mongoConfig) {
		String instance = mongoConfig.getInstanceName();
		String baseUris = getEnvironmentVariable(mongoConfig.getMongoBaseUriEnvironmentVariable(), mongoConfig.getMongoBaseUri());
		baseUris = normalizeBaseUris(baseUris);
		try {
			registerUriMapping(instance, baseUris);
			registerClientProvider(mongoConfig, baseUris);
			registerDatabaseProviders(mongoConfig, baseUris);
			registerEMFMongoRepositories(mongoConfig);
		} catch (Exception e) {
			tearDownConfigurations(mongoConfig);
		}
	}

	/**
	 * Looks in System.env and System.getProperty for the given variable
	 * @param envVariable the environmentVariable to look for
	 * @param default the default value to use if no value is found or the envVariable is null
	 * @return the desired value or the given default
	 */
	private String getEnvironmentVariable(String envVariable, String defaultValue) {
		if(envVariable == null || envVariable.isEmpty()) {
			return defaultValue;
		}

		String env = System.getenv(envVariable);
		if(env == null) {
			env = System.getProperty(envVariable);
		}

		if(env == null) {
			logger.warning("The variable " + envVariable + " was set, but no value was found in System.env and System.properties");
			return defaultValue;
		} else {
			logger.warning("Overwritting value with " + envVariable);
			return env;
		}
	}

	/**
	 * Registers the EMF repositories for the goven configuration
	 * @param mongoConfig
	 */
	private void registerEMFMongoRepositories(MongoInstanceConfig mongoConfig) {
		String instance = mongoConfig.getInstanceName();
		String rootUri = "mongodb://" + instance + "/";
		mongoConfig.getDatabaseConfigs().forEach((c)->{
			String baseUri = rootUri + c.getName();
			String id = instance + "." + c.getName();
			Dictionary<String, Object> properties = new Hashtable<>();
			properties.put(EMFRepository.PROP_ID, id);
			properties.put(EMFRepository.PROP_BASE_URI, baseUri);
			Configuration repoConfiguration = repositoryConfigurationMap.get(id);
			try {
				if (repoConfiguration == null) {
					String rsfTargetFilter = String.format(REPO_RESOURCE_SET_FILTER, id) ;
					properties.put(REPO_RESOURCE_SET_FIELD_TARGET, rsfTargetFilter);
					if(c.getType() == Type.SINGLETON) {
						repoConfiguration = configAdmin.createFactoryConfiguration(SINGLETON_REPOSITORY_CONFIGURATION_NAME, "?");
					} else {
						repoConfiguration = configAdmin.createFactoryConfiguration(PROTOTYPE_REPOSITORY_CONFIGURATION_NAME, "?");
					}
					repositoryConfigurationMap.put(id, repoConfiguration);
				}
				repoConfiguration.update(properties);
			} catch (Exception e) {
				throw new IllegalStateException("Error registering MongoRepository for mongo database instance: " + id, e);
			}
		});
	}

	/**
	 * Registers a new UriMapProvider for the given URI mapping
	 * @param instance the mongo instance name
	 * @param baseUris the baseUri's
	 * @throws IOException
	 */
	private void registerUriMapping(String instance, String baseUris) {
		Configuration uriMapConfiguration = uriMapConfigurationMap.get(instance);
		try {
			if (uriMapConfiguration == null) {
				uriMapConfiguration = configAdmin.createFactoryConfiguration("DefaultUriMapProvider", "?");
				uriMapConfigurationMap.put(instance, uriMapConfiguration);
			}
			Dictionary<String, Object> uriMapProperties = new Hashtable<>();
			uriMapProperties.put(UriMapProvider.URI_MAP_SOURE, "mongodb://" + instance + "/");
			uriMapProperties.put(UriMapProvider.URI_MAP_DESTINATION, baseUris.split("\\s*,\\s*")[0] + "/");
			uriMapConfiguration.update(uriMapProperties);
		} catch (Exception e) {
			throw new IllegalStateException("Error registering URI mapping for mongo instance: " + instance, e);
		} 
	}

	/**
	 * Registers a new {@link MongoClientProvider} for the given configuration
	 * @param mongoConfig the mongo instance configuration
	 * @param baseUris the baseUri's
	 */
	private void registerClientProvider(MongoInstanceConfig mongoConfig, String baseUris) {
		String instance = mongoConfig.getInstanceName();
		String instancePID = ConfigurationProperties.CLIENT_PID + "." + instance;
		StringBuilder credential = new StringBuilder(); 
		String user = getEnvironmentVariable(mongoConfig.getUsernameEnvironmentVariable(), mongoConfig.getUsername());
		String pwd = getEnvironmentVariable(mongoConfig.getPasswordEnvironmentVariable(), mongoConfig.getPassword());
		String authSource = getEnvironmentVariable(mongoConfig.getAuthDatabaseEnvironmentVariable(), mongoConfig.getAuthDatabase());
		if (user != null) {
			credential.append(user);
		}
		if (pwd != null) {
			credential.append(":");
			credential.append(pwd);
		}
		if (authSource != null) {
			credential.append("@");
			credential.append(authSource);
		}
		Map<String, Object> mongoClientProperties  = mongoConfig.getClientProperties();
		Dictionary<String, Object> clientProps = new Hashtable<>();
		clientProps.put(MongoClientProvider.PROP_URI, baseUris);
		clientProps.put(MongoClientProvider.PROP_CLIENT_ID, instance);
		mongoClientProperties.forEach((k,v)->clientProps.put(k, v));
		clientProps.put(Constants.SERVICE_PID, instancePID);
		if(credential.length() > 0){
			clientProps.put(MongoClientProvider.PROP_CREDENTIALS, credential.toString());
		}
		Configuration clientConfiguration = clientConfigurationMap.get(instance);
		try {
			if (clientConfiguration == null) {
				clientConfiguration = configAdmin.createFactoryConfiguration("MongoClientProvider", "?");
				clientConfigurationMap.put(instance, clientConfiguration);
			}
			clientConfiguration.update(clientProps);
		} catch (Exception e) {
			throw new IllegalStateException("Error registering MongoClientProvider for mongo instance: " + instance, e);
		}
	}

	/**
	 * Registers new {@link MongoDatabaseProvider}'s for the given configuration
	 * @param mongoConfig the mongo instance configuration
	 * @param baseUris the baseUri's
	 */
	private void registerDatabaseProviders(MongoInstanceConfig mongoConfig, String baseUris) {
		String instance = mongoConfig.getInstanceName();
		List<MongoDatabaseConfig> mdc = mongoConfig.getDatabaseConfigs();
		mdc.forEach((c)->{
			String database = c.getName();
			String dbName = instance + "." + database;

			String databaseProviderPID = ConfigurationProperties.DATABASE_PID + "." + instance + "." + database;
			Dictionary<String, Object> databaseProps = new Hashtable<>();
			databaseProps.put(MongoDatabaseProvider.PROP_ALIAS, database);
			databaseProps.put(MongoDatabaseProvider.PROP_DATABASE, database);
			databaseProps.put(MongoDatabaseProvider.PROP_DATABASE_IDENTIFIER , dbName);
			databaseProps.put(Constants.SERVICE_PID, databaseProviderPID);
			Configuration dbConfiguration = databaseConfigurationMap.get(dbName);
			try {
				if (dbConfiguration == null) {
					dbConfiguration = configAdmin.createFactoryConfiguration("MongoDatabaseProvider", "?");
					databaseConfigurationMap.put(dbName, dbConfiguration);
				}
				dbConfiguration.update(databaseProps);
				mongoDbConfigMap.put(dbName, c);
			} catch (Exception e) {
				throw new IllegalStateException("Error registering MongoDatabaseProvider for mongo database instance: " + dbName, e);
			}
		});
	}

	/**
	 * Reverts a configuration setup for the given configuration
	 * @param configuration the configuration to revert
	 */
	private void tearDownConfigurations(MongoInstanceConfig configuration) {
		try {
			if (configuration == null) {
				logger.warning("For a null configuration is nothing to revert");
				return;
			}
			String instance = configuration.getInstanceName();
			configuration.getDatabaseConfigs().forEach((c)->{
				String dbName = instance + "." + c.getName();
				mongoDbConfigMap.remove(dbName);
				Configuration repoc = repositoryConfigurationMap.remove(dbName);
				if (repoc != null) {
					try {
						repoc.delete();
					} catch (IOException e) {
						logger.log(Level.SEVERE, "Cannot delete repository configuration for " + dbName, e);
					}
				}
				Configuration dbc = databaseConfigurationMap.remove(dbName);
				if (dbc != null) {
					try {
						dbc.delete();
					} catch (IOException e) {
						logger.log(Level.SEVERE, "Cannot delete database configuration for " + dbName, e);
					}
				}
			});
			Configuration uriMapConfig = uriMapConfigurationMap.remove(instance);
			if (uriMapConfig != null) {
				uriMapConfig.delete();
			}
			Configuration clientConfig = clientConfigurationMap.remove(instance);
			if (clientConfig != null) {
				clientConfig.delete();
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Cannot revert configurations for " + configuration.getInstanceName(), e);
		}
	}

}
