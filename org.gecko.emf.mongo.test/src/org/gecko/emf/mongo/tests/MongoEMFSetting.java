/**
 * Copyright (c) 2012 - 2019 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.mongo.tests;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.gecko.mongo.osgi.MongoClientProvider;
import org.gecko.mongo.osgi.MongoDatabaseProvider;
import org.gecko.mongo.osgi.configuration.ConfigurationProperties;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;

/**
 * 
 * @author mark
 * @since 21.11.2019
 */
public abstract class MongoEMFSetting {

	static protected String mongoHost = System.getProperty("mongo.host", "localhost");

	protected MongoClient client;
	protected MongoCollection<?> collection;
//	private ServiceRegistration<?> BasicPackageRegistration = null;
	
	public void doBefore(BundleContext ctx) {
		MongoClientOptions options = MongoClientOptions.builder().build();
		client = new MongoClient(mongoHost, options);
//		BasicPackageRegistration = ctx.registerService(new String[] {EPackageConfigurator.class.getName(), ResourceFactoryConfigurator.class.getName()}, new ManualPackageConfigurator(), null);
	}

	public void doAfter() {
		if (collection != null) {
			collection.drop();
		}
		if (client != null) {
			client.close();
		}
//		if (BasicPackageRegistration != null) {
//			BasicPackageRegistration.unregister();
//			BasicPackageRegistration = null;
//		}
	}
	
	protected void defaultSetup(ConfigurationAdmin ca) throws IOException, InvalidSyntaxException {
		// has to be a new configuration
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		// add service properties
		String clientId = "testClient";
		String clientUri = "mongodb://" + mongoHost + ":27017";
		props = new Hashtable<String, Object>();
		props.put(MongoClientProvider.PROP_CLIENT_ID, clientId);
		props.put(MongoClientProvider.PROP_URI, clientUri);
		Configuration clientConfig = ca.createFactoryConfiguration(ConfigurationProperties.CLIENT_PID, "?");
		clientConfig.update(props);
		
		// add service properties
		String dbAlias = "testDB";
		String db = "test";
		Dictionary<String, Object> dbp = new Hashtable<String, Object>();
		dbp.put(MongoDatabaseProvider.PROP_ALIAS, dbAlias);
		dbp.put(MongoDatabaseProvider.PROP_DATABASE, db);
		Configuration databaseConfig = ca.createFactoryConfiguration(ConfigurationProperties.DATABASE_PID, "?");
		databaseConfig.update(dbp);
		
//		createStaticTrackedChecker(MongoIdFactory.class).assertCreations(1, true).trackedServiceNotNull();
//		createStaticTrackedChecker(QueryEngine.class).assertCreations(1, true).trackedServiceNotNull();
//		createStaticTrackedChecker(ConverterService.class).assertCreations(1, true).trackedServiceNotNull();
//		createStaticTrackedChecker(InputStreamFactory.class).assertCreations(1, true).trackedServiceNotNull();
//		createStaticTrackedChecker(OutputStreamFactory.class).assertCreations(1, true).trackedServiceNotNull();
	}

}
