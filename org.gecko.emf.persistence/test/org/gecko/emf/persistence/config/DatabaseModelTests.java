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
import java.util.Map;

import org.gecko.emf.persistence.config.model.DatabaseModel;
import org.junit.jupiter.api.Test;
import org.osgi.service.cm.ConfigurationException;

/**
 * 
 * @author Mark Hoffmann
 * @since 20.02.2023
 */
public class DatabaseModelTests {

	@Test
	public void testGetDatabasesFail() throws ConfigurationException {
		assertThrows(NullPointerException.class, ()->DatabaseModel.getDatabases(null, null));
		assertThrows(NullPointerException.class, ()->DatabaseModel.getDatabases(null, Collections.emptyMap()));
		assertThrows(NullPointerException.class, ()->DatabaseModel.getDatabases("test", null));
		
		String[] dbs = DatabaseModel.getDatabases(" ", Collections.emptyMap());
		assertEquals(0 , dbs.length);
		dbs = DatabaseModel.getDatabases("", Collections.emptyMap());
		assertEquals(0, dbs.length);
	}
	
	@Test
	public void testGetDatabases() throws ConfigurationException {
		String[] dbs = DatabaseModel.getDatabases("", Collections.singletonMap(PROP_DATABASES, "bar"));
		assertNotNull(dbs);
		assertEquals(1, dbs.length);
		assertEquals("bar", dbs[0]);
		
		dbs = DatabaseModel.getDatabases("", Collections.singletonMap(PROP_DATABASES, "foo,bar"));
		assertNotNull(dbs);
		assertEquals(2, dbs.length);
		assertEquals("foo", dbs[0]);
		assertEquals("bar", dbs[1]);
		
		dbs = DatabaseModel.getDatabases("", Collections.singletonMap(PROP_DATABASES, ",foo,bar"));
		assertNotNull(dbs);
		assertEquals(2, dbs.length);
		assertEquals("foo", dbs[0]);
		assertEquals("bar", dbs[1]);
		
		dbs = DatabaseModel.getDatabases("", Collections.singletonMap(PROP_DATABASES, ",foo,bar,"));
		assertNotNull(dbs);
		assertEquals(2, dbs.length);
		assertEquals("foo", dbs[0]);
		assertEquals("bar", dbs[1]);
		
		dbs = DatabaseModel.getDatabases("", Collections.singletonMap(PROP_DATABASES, "foo,bar,"));
		assertNotNull(dbs);
		assertEquals(2, dbs.length);
		assertEquals("foo", dbs[0]);
		assertEquals("bar", dbs[1]);
		
		dbs = DatabaseModel.getDatabases("foo", Collections.singletonMap("foo." + PROP_DATABASES, "foo,bar"));
		assertNotNull(dbs);
		assertEquals(2, dbs.length);
		assertEquals("foo", dbs[0]);
		assertEquals("bar", dbs[1]);
		
		dbs = DatabaseModel.getDatabases("foo", Collections.singletonMap("foo." + PROP_DATABASES, ",foo,bar"));
		assertNotNull(dbs);
		assertEquals(2, dbs.length);
		assertEquals("foo", dbs[0]);
		assertEquals("bar", dbs[1]);
		
		dbs = DatabaseModel.getDatabases("foo", Collections.singletonMap("foo." + PROP_DATABASES, ",foo,bar,"));
		assertNotNull(dbs);
		assertEquals(2, dbs.length);
		assertEquals("foo", dbs[0]);
		assertEquals("bar", dbs[1]);
		
		dbs = DatabaseModel.getDatabases("foo", Collections.singletonMap("foo." + PROP_DATABASES, "foo,bar,"));
		assertNotNull(dbs);
		assertEquals(2, dbs.length);
		assertEquals("foo", dbs[0]);
		assertEquals("bar", dbs[1]);
	}
	
	@Test
	public void testGetDatabasesModelParent() throws ConfigurationException {
		DatabaseModel config = DatabaseModel.createDatabaseModel("foo", "bar", Collections.emptyMap());
		assertEquals("foo.bar", config.getName());
		assertNull(config.getPassword());
		assertNull(config.getUser());
		assertTrue(config.getProperties().isEmpty());
		
		config = DatabaseModel.createDatabaseModel("foo", null, Collections.emptyMap());
		assertNull(config);
		
		config = DatabaseModel.createDatabaseModel("foo", "bar", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_CONNECTION_URIS, "derby:test,jpa:test2", 
				"foo." + PROP_USER, "emil", 
				"foo." + PROP_PASSWORD, "1234", 
				"foo.bar." + PROP_USER, "foobar", 
				"foo.bar." + PROP_PASSWORD, "4321", 
				"foo." + PROP_DATABASES,	"testDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE"));
		assertEquals("foo.bar", config.getName());
		assertEquals("4321", config.getPassword());
		assertEquals("foobar", config.getUser());
		assertTrue(config.getProperties().isEmpty());
		assertNull(config.getParent());
	}
	
	@Test
	public void testGetDatabaseModel() throws ConfigurationException {
		
		assertThrows(NullPointerException.class, ()->DatabaseModel.createDatabaseModel(null, null, null));
		assertThrows(NullPointerException.class, ()->DatabaseModel.createDatabaseModel("test", null, null));
		assertThrows(NullPointerException.class, ()->DatabaseModel.createDatabaseModel("test", "test2", null));
		assertThrows(NullPointerException.class, ()->DatabaseModel.createDatabaseModel(null, "test2", null));
		assertThrows(NullPointerException.class, ()->DatabaseModel.createDatabaseModel(null, null, Collections.emptyMap()));
		
		assertThrows(ConfigurationException.class, ()->DatabaseModel.createDatabaseModel("", "", Collections.emptyMap()));
		assertThrows(ConfigurationException.class, ()->DatabaseModel.createDatabaseModel(" ", " ", Collections.emptyMap()));
		
		DatabaseModel config = DatabaseModel.createDatabaseModel("foo", "bar", Collections.emptyMap());
		assertEquals("foo.bar", config.getName());
		assertNull(config.getPassword());
		assertNull(config.getUser());
		assertTrue(config.getProperties().isEmpty());
		
		config = DatabaseModel.createDatabaseModel("foo", null, Collections.emptyMap());
		assertNull(config);
		
		config = DatabaseModel.createDatabaseModel("foo", "bar", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_CONNECTION_URIS, "derby:test,jpa:test2", 
				"foo." + PROP_USER, "emil", 
				"foo." + PROP_PASSWORD, "1234", 
				"foo.bar." + PROP_USER, "foobar", 
				"foo.bar." + PROP_PASSWORD, "4321", 
				"foo." + PROP_DATABASES,	"testDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE"));
		assertEquals("foo.bar", config.getName());
		assertEquals("4321", config.getPassword());
		assertEquals("foobar", config.getUser());
		assertTrue(config.getProperties().isEmpty());
	}
	
	@Test
	public void testGetDatabaseModelProperties() throws ConfigurationException {
		
		DatabaseModel config = DatabaseModel.createDatabaseModel("foo", "bar", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo.bar." + PROP_USER, "DBUSER",
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE"));
		assertEquals("foo.bar", config.getName());
		assertEquals("DBUSER", config.getUser());
		assertTrue(config.getProperties().isEmpty());
		
		config = DatabaseModel.createDatabaseModel("foo", "bar", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_USER, "DBUSER",
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE", "foo.test", "test"));
		assertEquals("foo.bar", config.getName());
		assertTrue(config.getProperties().isEmpty());
		
		config = DatabaseModel.createDatabaseModel("foo", "bar", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_USER, "DBUSER",
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE", "foo.bar.test", "test"));
		assertEquals("foo.bar", config.getName());
		assertTrue(config.getProperties().isEmpty());
		
		config = DatabaseModel.createDatabaseModel("foo", "bar", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_USER, "DBUSER",
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE", "foo." + PROP_PROPERTIES + ".myTest", "test"));
		assertEquals("foo.bar", config.getName());
		assertTrue(config.getProperties().isEmpty());
		
		config = DatabaseModel.createDatabaseModel("foo", "bar", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE", "foo.bar." + PROP_PROPERTIES + ".myTest", "test"));
		assertEquals("foo.bar", config.getName());
		assertEquals(1, config.getProperties().size());
		assertEquals("test", config.getProperties().get("myTest"));
		
	}
	
}
