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

import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_AUTH_SOURCE;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_CONNECTION_URIS;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_DATABASES;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_INSTANCES;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_PASSWORD;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_PROPERTIES;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_REPOSITORY_TYPE;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.RepositoryType;
import org.gecko.emf.persistence.config.model.DatabaseModel;
import org.gecko.emf.persistence.config.model.InstanceModel;
import org.junit.jupiter.api.Test;
import org.osgi.service.cm.ConfigurationException;

/**
 * 
 * @author Mark Hoffmann
 * @since 20.02.2023
 */
public class InstanceModelTests {
	
	@Test
	public void testGetInstancesFail() throws ConfigurationException {
		assertThrows(NullPointerException.class, ()->InstanceModel.getInstances(null));
		
		assertThrows(ConfigurationException.class, ()->InstanceModel.getInstances(Collections.emptyMap()));
		assertThrows(ConfigurationException.class, ()->InstanceModel.getInstances(Collections.singletonMap("foo", "bar")));
		
	}
	
	@Test
	public void testGetInstances() throws ConfigurationException {
		
		String[] instances = InstanceModel.getInstances(Collections.singletonMap(PROP_INSTANCES, "bar"));
		assertNotNull(instances);
		assertEquals(1, instances.length);
		assertEquals("bar", instances[0]);
		
		instances = InstanceModel.getInstances(Collections.singletonMap(PROP_INSTANCES, "foo,bar"));
		assertNotNull(instances);
		assertEquals(2, instances.length);
		assertEquals("foo", instances[0]);
		assertEquals("bar", instances[1]);
		
		instances = InstanceModel.getInstances(Collections.singletonMap(PROP_INSTANCES, ",foo,bar"));
		assertNotNull(instances);
		assertEquals(2, instances.length);
		assertEquals("foo", instances[0]);
		assertEquals("bar", instances[1]);
		
		instances = InstanceModel.getInstances(Collections.singletonMap(PROP_INSTANCES, "foo,bar,"));
		assertNotNull(instances);
		assertEquals(2, instances.length);
		assertEquals("foo", instances[0]);
		assertEquals("bar", instances[1]);
		
		instances = InstanceModel.getInstances(Collections.singletonMap(PROP_INSTANCES, ",foo,bar,"));
		assertNotNull(instances);
		assertEquals(2, instances.length);
		assertEquals("foo", instances[0]);
		assertEquals("bar", instances[1]);
	}
	
	@Test
	public void testGetInstanceModel() throws ConfigurationException {
		
		assertThrows(NullPointerException.class, ()->InstanceModel.createInstanceModel(null, null));
		assertThrows(NullPointerException.class, ()->InstanceModel.createInstanceModel("test", null));
		assertThrows(NullPointerException.class, ()->InstanceModel.createInstanceModel(null, Collections.emptyMap()));
		
		assertThrows(ConfigurationException.class, ()->InstanceModel.createInstanceModel("", Collections.emptyMap()));
		assertThrows(ConfigurationException.class, ()->InstanceModel.createInstanceModel(" ", Collections.emptyMap()));
		
		InstanceModel config = InstanceModel.createInstanceModel("foo", Collections.singletonMap("foo." + PROP_DATABASES, "foo"));
		assertEquals("foo", config.getName());
		assertNull(config.getAuthenticationSource());
		assertNull(config.getConnectionUris());
		assertNull(config.getPassword());
		assertNull(config.getUser());
		assertNull(config.getRepositoryType());
		assertTrue(config.getProperties().isEmpty());
		
		config = InstanceModel.createInstanceModel("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_CONNECTION_URIS, "derby:test,jpa:test2", 
				"foo." + PROP_USER, "emil", 
				"foo." + PROP_PASSWORD, "1234", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals("derby:test,jpa:test2", config.getConnectionUris());
		assertEquals("1234", config.getPassword());
		assertEquals("emil", config.getUser());
		assertEquals(RepositoryType.PROTOTYPE, config.getRepositoryType());
		assertTrue(config.getProperties().isEmpty());
	}
	
	@Test
	public void testGetInstanceModelProperties() throws ConfigurationException {
		
		InstanceModel config = InstanceModel.createInstanceModel("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(RepositoryType.PROTOTYPE, config.getRepositoryType());
		assertTrue(config.getProperties().isEmpty());
		
		config = InstanceModel.createInstanceModel("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE", 
				"foo.test", "test"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(RepositoryType.PROTOTYPE, config.getRepositoryType());
		assertTrue(config.getProperties().isEmpty());
		
		config = InstanceModel.createInstanceModel("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE", 
				"foo." + PROP_PROPERTIES + ".myTest", "test"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(RepositoryType.PROTOTYPE, config.getRepositoryType());
		assertEquals(1, config.getProperties().size());
		assertEquals("test", config.getProperties().get("myTest"));
		
	}
	
	@Test
	public void testGetInstanceModelDatabases() throws ConfigurationException {
		
		InstanceModel config = InstanceModel.createInstanceModel("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(RepositoryType.PROTOTYPE, config.getRepositoryType());
		assertTrue(config.getProperties().isEmpty());
		assertTrue(config.getDatabaseModels().isEmpty());
		
		config = InstanceModel.createInstanceModel("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE",
				"foo.test", "test",
				"foo." + PROP_DATABASES, "foo"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(RepositoryType.PROTOTYPE, config.getRepositoryType());
		assertEquals(1, config.getDatabaseModels().size());
		assertEquals("foo.foo", config.getDatabaseModels().iterator().next().getName());
		assertTrue(config.getProperties().isEmpty());
		
		config = InstanceModel.createInstanceModel("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_PROPERTIES + ".myTest", "test",
				"foo." + PROP_DATABASES, "foo,bar"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(2, config.getDatabaseModels().size());
		Iterator<DatabaseModel> iterator = config.getDatabaseModels().iterator();
		List<String> names = List.of("foo.foo", "foo.bar");
		assertTrue(names.contains(iterator.next().getName()));
		assertTrue(names.contains(iterator.next().getName()));
		assertEquals(1, config.getProperties().size());
		assertEquals("test", config.getProperties().get("myTest"));
		
	}
	
	@Test
	public void testDatabaseModelParent() throws ConfigurationException {
		
		InstanceModel config = InstanceModel.createInstanceModel("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_PROPERTIES + ".myTest", "test",
				"foo." + PROP_DATABASES, "foo,bar"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(2, config.getDatabaseModels().size());
		Iterator<DatabaseModel> iterator = config.getDatabaseModels().iterator();
		DatabaseModel dbm01 = iterator.next();
		DatabaseModel dbm02 = iterator.next();
		List<String> instances = List.of("foo.bar", "foo.foo");
		assertTrue(instances.contains(dbm01.getName()));
		assertEquals(config, dbm01.getParent());
		assertTrue(instances.contains(dbm02.getName()));
		assertEquals(config, dbm02.getParent());
		assertEquals(1, config.getProperties().size());
		assertEquals("test", config.getProperties().get("myTest"));
		
	}
	
	@Test
	public void testGetInstanceModelRepoType() throws ConfigurationException {
		
		InstanceModel config = InstanceModel.createInstanceModel("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(RepositoryType.PROTOTYPE, config.getRepositoryType());
		
		config = InstanceModel.createInstanceModel("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "protoType"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(RepositoryType.PROTOTYPE, config.getRepositoryType());
		
		config = InstanceModel.createInstanceModel("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "singleton"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(RepositoryType.SINGLETON, config.getRepositoryType());
		
		Map<Object, Object> properties = new HashMap<>();
		properties.put("foo." + PROP_AUTH_SOURCE, "authDB");
		properties.put("foo." + PROP_REPOSITORY_TYPE, null);
		config = InstanceModel.createInstanceModel("foo", properties);
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertNull(config.getRepositoryType());
		
		assertThrows(IllegalStateException.class, ()->InstanceModel.createInstanceModel("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "other")));
		assertThrows(IllegalStateException.class, ()->InstanceModel.createInstanceModel("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "")));
	}

}
