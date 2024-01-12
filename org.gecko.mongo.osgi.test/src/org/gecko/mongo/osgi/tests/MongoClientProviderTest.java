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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.gecko.mongo.osgi.MongoClientProvider;
import org.gecko.mongo.osgi.configuration.ConfigurationProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * Tests the mongo client provider
 * @author Mark Hoffmann
 * @since 25.07.2017
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(MockitoExtension.class)
public class MongoClientProviderTest {

	private String mongoHost = System.getProperty("mongo.host", "localhost");

	@Test
	public void testCreateMongoClientProvider(@InjectService(cardinality = 0) ServiceAware<MongoClientProvider> clientProviderAware, @InjectService ConfigurationAdmin ca) throws IOException, InterruptedException  {

		
		// add service properties
		String clientId = "testClient";
		String clientUri = "mongodb://" + mongoHost + ":27017";
		Dictionary<String, Object> p = new Hashtable<String, Object>();
		p.put(MongoClientProvider.PROP_CLIENT_ID, clientId);
		p.put(MongoClientProvider.PROP_URI, clientUri);

		assertTrue(clientProviderAware.isEmpty());

		Configuration clientConfig = ca.createFactoryConfiguration(ConfigurationProperties.CLIENT_PID, "?");
		clientConfig.update(p);

		MongoClientProvider clientProvider = clientProviderAware.waitForService(2000l);
		assertEquals(clientId, clientProvider.getClientId());
		assertEquals(1, clientProvider.getURIs().length);
		assertEquals(clientUri, clientProvider.getURIs()[0]);

		clientConfig.delete();
		Thread.sleep(1000l);
		assertTrue(clientProviderAware.isEmpty());
	}

	@Test
	public void testModifyMongoClientProvider(@InjectService(cardinality = 0) ServiceAware<MongoClientProvider> clientProviderAware, @InjectService ConfigurationAdmin ca) throws IOException, InterruptedException {

		// add service properties
		String clientId = "testClient";
		String clientUri = "mongodb://" + mongoHost + ":27017";
		Dictionary<String, Object> p = new Hashtable<String, Object>();
		p.put(MongoClientProvider.PROP_CLIENT_ID, clientId);
		p.put(MongoClientProvider.PROP_URI, clientUri);

		assertTrue(clientProviderAware.isEmpty());

		Configuration clientConfig = ca.createFactoryConfiguration(ConfigurationProperties.CLIENT_PID, "?");
		clientConfig.update(p);

		MongoClientProvider clientProvider = clientProviderAware.waitForService(2000l);
		assertEquals(clientId, clientProvider.getClientId());
		assertEquals(1, clientProvider.getURIs().length);
		assertEquals(clientUri, clientProvider.getURIs()[0]);

		// remove configuration
		p = new Hashtable<String, Object>();
		p.put(MongoClientProvider.PROP_CLIENT_ID, clientId + "2");
		p.put(MongoClientProvider.PROP_URI, clientUri);
		
		clientConfig.update(p);
		Thread.sleep(1000l);
		clientProvider = clientProviderAware.getService();
		assertNotNull(clientProvider);
		assertEquals(clientId + "2", clientProvider.getClientId());
		assertEquals(1, clientProvider.getURIs().length);
		assertEquals(clientUri, clientProvider.getURIs()[0]);

		clientConfig.delete();
		Thread.sleep(1000l);
		assertTrue(clientProviderAware.isEmpty());
	}

}
