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
package org.gecko.mongo.osgi.tests;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.gecko.mongo.osgi.MongoClientProvider;
import org.gecko.mongo.osgi.MongoDatabaseProvider;
import org.gecko.mongo.osgi.configuration.ConfigurationProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

import com.mongodb.client.MongoDatabase;

/**
 * Tests the database provider
 * @author Mark Hoffmann
 * @since 25.07.2017
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(MockitoExtension.class)
public class MongoDatabaseProviderTest {

	private String mongoHost = System.getProperty("mongo.host", "localhost");
	
	@Test
	public void testNoMongoClientProvider(@InjectService(cardinality = 0) ServiceAware<MongoDatabaseProvider> dbProviderAware, @InjectService ConfigurationAdmin ca) throws IOException, InterruptedException {

		// add service properties
		String dbAlias = "testDB";
		String db = "test";
		Dictionary<String, Object> p = new Hashtable<String, Object>();
		p.put(MongoDatabaseProvider.PROP_ALIAS, dbAlias);
		p.put(MongoDatabaseProvider.PROP_DATABASE, db);
		
		assertTrue(dbProviderAware.isEmpty());
		
		Configuration dbConfig = ca.createFactoryConfiguration(ConfigurationProperties.DATABASE_PID, "?");
		dbConfig.update(p);
		
		Thread.sleep(2000l);
		assertTrue(dbProviderAware.isEmpty());

		// re-check configuration must not available, because of the missing client
		Configuration clientConfig = ca.getConfiguration(ConfigurationProperties.CLIENT_PID, "?");
		assertNull(clientConfig.getProperties());

	}

	@Test
	public void testCreateMongoDatabaseProvider(@InjectService(cardinality = 0) ServiceAware<MongoClientProvider> clientProviderAware, @InjectService(cardinality = 0) ServiceAware<MongoDatabaseProvider> dbProviderAware, @InjectService ConfigurationAdmin ca) throws InvalidSyntaxException, BundleException, IOException, InterruptedException {

		// add service properties
		String clientId = "testClient";
		String clientUri = "mongodb://" + mongoHost + ":27017";
		Dictionary<String, Object> p = new Hashtable<String, Object>();
		p.put(MongoClientProvider.PROP_CLIENT_ID, clientId);
		p.put(MongoClientProvider.PROP_URI, clientUri);
		
		assertTrue(clientProviderAware.isEmpty());
		assertTrue(dbProviderAware.isEmpty());
		
		Configuration clientConfig = ca.createFactoryConfiguration(ConfigurationProperties.CLIENT_PID, "?");
		clientConfig.update(p);
		
		clientProviderAware.waitForService(2000l);

		// add service properties
		String dbAlias = "testDB";
		String db = "test";
		Dictionary<String, Object> dbp = new Hashtable<String, Object>();
		dbp.put(MongoDatabaseProvider.PROP_ALIAS, dbAlias);
		dbp.put(MongoDatabaseProvider.PROP_DATABASE, db);
		Configuration dbConfig = ca.createFactoryConfiguration(ConfigurationProperties.DATABASE_PID, "?");
		dbConfig.update(dbp);
		
		MongoDatabaseProvider databaseProvider = dbProviderAware.waitForService(2000l);
		MongoDatabase database = databaseProvider.getDatabase();
		assertEquals(db, database.getName());
		
		dbConfig.delete();
		clientConfig.delete();
		
		Thread.sleep(1000l);
		assertTrue(dbProviderAware.isEmpty());
		assertTrue(clientProviderAware.isEmpty());
	}

}
