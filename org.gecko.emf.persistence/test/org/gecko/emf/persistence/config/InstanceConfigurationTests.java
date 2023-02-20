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
import java.util.Map;

import org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.RepositoryType;
import org.gecko.emf.persistence.config.model.DatabaseConfiguration;
import org.gecko.emf.persistence.config.model.InstanceConfiguration;
import org.junit.jupiter.api.Test;
import org.osgi.service.cm.ConfigurationException;

/**
 * 
 * @author Mark Hoffmann
 * @since 20.02.2023
 */
public class InstanceConfigurationTests {
	
	@Test
	public void testGetInstancesFail() throws ConfigurationException {
		assertThrows(NullPointerException.class, ()->InstanceConfiguration.getInstances(null));
		
		assertThrows(ConfigurationException.class, ()->InstanceConfiguration.getInstances(Collections.emptyMap()));
		assertThrows(ConfigurationException.class, ()->InstanceConfiguration.getInstances(Collections.singletonMap("foo", "bar")));
		
	}
	
	@Test
	public void testGetInstances() throws ConfigurationException {
		
		String[] instances = InstanceConfiguration.getInstances(Collections.singletonMap(PROP_INSTANCES, "bar"));
		assertNotNull(instances);
		assertEquals(1, instances.length);
		assertEquals("bar", instances[0]);
		
		instances = InstanceConfiguration.getInstances(Collections.singletonMap(PROP_INSTANCES, "foo,bar"));
		assertNotNull(instances);
		assertEquals(2, instances.length);
		assertEquals("foo", instances[0]);
		assertEquals("bar", instances[1]);
		
		instances = InstanceConfiguration.getInstances(Collections.singletonMap(PROP_INSTANCES, ",foo,bar"));
		assertNotNull(instances);
		assertEquals(2, instances.length);
		assertEquals("foo", instances[0]);
		assertEquals("bar", instances[1]);
		
		instances = InstanceConfiguration.getInstances(Collections.singletonMap(PROP_INSTANCES, "foo,bar,"));
		assertNotNull(instances);
		assertEquals(2, instances.length);
		assertEquals("foo", instances[0]);
		assertEquals("bar", instances[1]);
		
		instances = InstanceConfiguration.getInstances(Collections.singletonMap(PROP_INSTANCES, ",foo,bar,"));
		assertNotNull(instances);
		assertEquals(2, instances.length);
		assertEquals("foo", instances[0]);
		assertEquals("bar", instances[1]);
	}
	
	@Test
	public void testGetInstanceConfiguration() throws ConfigurationException {
		
		assertThrows(NullPointerException.class, ()->InstanceConfiguration.createInstanceConfiguration(null, null));
		assertThrows(NullPointerException.class, ()->InstanceConfiguration.createInstanceConfiguration("test", null));
		assertThrows(NullPointerException.class, ()->InstanceConfiguration.createInstanceConfiguration(null, Collections.emptyMap()));
		
		assertThrows(ConfigurationException.class, ()->InstanceConfiguration.createInstanceConfiguration("", Collections.emptyMap()));
		assertThrows(ConfigurationException.class, ()->InstanceConfiguration.createInstanceConfiguration(" ", Collections.emptyMap()));
		
		InstanceConfiguration config = InstanceConfiguration.createInstanceConfiguration("foo", Collections.singletonMap("foo." + PROP_DATABASES, "foo"));
		assertEquals("foo", config.getName());
		assertNull(config.getAuthenticationSource());
		assertNull(config.getConnectionUris());
		assertNull(config.getPassword());
		assertNull(config.getUser());
		assertNull(config.getRepositoryType());
		assertTrue(config.getProperties().isEmpty());
		
		config = InstanceConfiguration.createInstanceConfiguration("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
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
	public void testGetInstanceConfigurationProperties() throws ConfigurationException {
		
		InstanceConfiguration config = InstanceConfiguration.createInstanceConfiguration("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(RepositoryType.PROTOTYPE, config.getRepositoryType());
		assertTrue(config.getProperties().isEmpty());
		
		config = InstanceConfiguration.createInstanceConfiguration("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE", 
				"foo.test", "test"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(RepositoryType.PROTOTYPE, config.getRepositoryType());
		assertTrue(config.getProperties().isEmpty());
		
		config = InstanceConfiguration.createInstanceConfiguration("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE", 
				"foo." + PROP_PROPERTIES + ".myTest", "test"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(RepositoryType.PROTOTYPE, config.getRepositoryType());
		assertEquals(1, config.getProperties().size());
		assertEquals("test", config.getProperties().get("myTest"));
		
	}
	
	@Test
	public void testGetInstanceConfigurationDatabases() throws ConfigurationException {
		
		InstanceConfiguration config = InstanceConfiguration.createInstanceConfiguration("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(RepositoryType.PROTOTYPE, config.getRepositoryType());
		assertTrue(config.getProperties().isEmpty());
		assertTrue(config.getDatabaseConfigurations().isEmpty());
		
		config = InstanceConfiguration.createInstanceConfiguration("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE",
				"foo.test", "test",
				"foo." + PROP_DATABASES, "foo"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(RepositoryType.PROTOTYPE, config.getRepositoryType());
		assertEquals(1, config.getDatabaseConfigurations().size());
		assertEquals("foo.foo", config.getDatabaseConfigurations().iterator().next().getName());
		assertTrue(config.getProperties().isEmpty());
		
		config = InstanceConfiguration.createInstanceConfiguration("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_PROPERTIES + ".myTest", "test",
				"foo." + PROP_DATABASES, "foo,bar"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(2, config.getDatabaseConfigurations().size());
		Iterator<DatabaseConfiguration> iterator = config.getDatabaseConfigurations().iterator();
		assertEquals("foo.bar", iterator.next().getName());
		assertEquals("foo.foo", iterator.next().getName());
		assertEquals(1, config.getProperties().size());
		assertEquals("test", config.getProperties().get("myTest"));
		
	}
	
	@Test
	public void testGetInstanceConfigurationRepoType() throws ConfigurationException {
		
		InstanceConfiguration config = InstanceConfiguration.createInstanceConfiguration("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(RepositoryType.PROTOTYPE, config.getRepositoryType());
		
		config = InstanceConfiguration.createInstanceConfiguration("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "protoType"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(RepositoryType.PROTOTYPE, config.getRepositoryType());
		
		config = InstanceConfiguration.createInstanceConfiguration("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "singleton"));
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertEquals(RepositoryType.SINGLETON, config.getRepositoryType());
		
		Map<Object, Object> properties = new HashMap<>();
		properties.put("foo." + PROP_AUTH_SOURCE, "authDB");
		properties.put("foo." + PROP_REPOSITORY_TYPE, null);
		config = InstanceConfiguration.createInstanceConfiguration("foo", properties);
		assertEquals("foo", config.getName());
		assertEquals("authDB", config.getAuthenticationSource());
		assertNull(config.getRepositoryType());
		
		assertThrows(IllegalStateException.class, ()->InstanceConfiguration.createInstanceConfiguration("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "other")));
		assertThrows(IllegalStateException.class, ()->InstanceConfiguration.createInstanceConfiguration("foo", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "")));
	}

}
