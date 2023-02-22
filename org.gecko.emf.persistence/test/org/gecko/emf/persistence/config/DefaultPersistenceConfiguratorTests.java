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

import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_DATABASES;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_INSTANCES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.gecko.emf.persistence.config.model.DatabaseModel;
import org.gecko.emf.persistence.config.model.InstanceModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationException;

/**
 * Tests the abstract {@link DefaultPersistenceConfigurator} with its functionality
 * @author Mark Hoffmann
 * @since 22.02.2023
 */
@ExtendWith(MockitoExtension.class)
public class DefaultPersistenceConfiguratorTests {

	private DefaultPersistenceConfigurator configurator;
	@Mock
	private Configuration configResourceFactory;
	@Mock
	private Configuration configDatabase;
	@Mock
	private Configuration configEngine;

	@BeforeEach
	public void before() {
		configurator = createConfiguratorMock();
	}

	/**
	 * @return 
	 * 
	 */
	private DefaultPersistenceConfigurator createConfiguratorMock() {
		return Mockito.mock(DefaultPersistenceConfigurator.class, 
				Mockito.withSettings().
				useConstructor().
				defaultAnswer(Mockito.CALLS_REAL_METHODS));
	}

	@Test
	public void testNoConfiguration() throws ConfigurationException, IOException {
		assertThrows(NullPointerException.class, ()->configurator.activate(null));
		verify(configurator, times(1)).doActivate(isNull());
		verify(configurator, times(1)).createInstanceModels(isNull());
		verify(configurator, never()).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, never()).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doSetupDatabaseConfiguration(any(DatabaseModel.class));

		verify(configDatabase, never()).delete();
		verify(configEngine, never()).delete();
		assertTrue(configurator.getDbConfigMap().isEmpty());
		assertTrue(configurator.getDbModelMap().isEmpty());
		assertTrue(configurator.getEngineConfigMap().isEmpty());

		reset(configurator);

		assertThrows(ConfigurationException.class, ()->configurator.activate(Collections.emptyMap()));
		verify(configurator, times(1)).doActivate(anyMap());
		verify(configurator, times(1)).createInstanceModels(anyMap());
		verify(configurator, never()).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, never()).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doSetupDatabaseConfiguration(any(DatabaseModel.class));

		verify(configDatabase, never()).delete();
		verify(configEngine, never()).delete();
		assertTrue(configurator.getDbConfigMap().isEmpty());
		assertTrue(configurator.getDbModelMap().isEmpty());
		assertTrue(configurator.getEngineConfigMap().isEmpty());

		reset(configurator);

		assertThrows(ConfigurationException.class, ()->configurator.activate(Collections.singletonMap("foo", "bar")));
		verify(configurator, times(1)).doActivate(anyMap());
		verify(configurator, times(1)).createInstanceModels(anyMap());
		verify(configurator, never()).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, never()).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doSetupDatabaseConfiguration(any(DatabaseModel.class));

		verify(configDatabase, never()).delete();
		verify(configEngine, never()).delete();
		assertTrue(configurator.getDbConfigMap().isEmpty());
		assertTrue(configurator.getDbModelMap().isEmpty());
		assertTrue(configurator.getEngineConfigMap().isEmpty());
	}

	@Test
	public void testInstancesOnlyActivate( ) throws ConfigurationException, IOException {
		configurator.activate(Collections.singletonMap(PROP_INSTANCES, "bar"));
		verify(configurator, times(1)).doActivate(anyMap());
		verify(configurator, times(1)).createInstanceModels(anyMap());
		verify(configurator, times(1)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(1)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, never()).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doSetupDatabaseConfiguration(any(DatabaseModel.class));

		verify(configDatabase, never()).delete();
		verify(configEngine, never()).delete();
		assertTrue(configurator.getDbConfigMap().isEmpty());
		assertTrue(configurator.getDbModelMap().isEmpty());
		assertTrue(configurator.getEngineConfigMap().isEmpty());

		configurator = createConfiguratorMock();

		configurator.activate(Collections.singletonMap(PROP_INSTANCES, "foo,bar,fizz"));
		verify(configurator, times(1)).doActivate(anyMap());
		verify(configurator, times(1)).createInstanceModels(anyMap());
		verify(configurator, times(3)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(3)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, never()).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doSetupDatabaseConfiguration(any(DatabaseModel.class));

		verify(configDatabase, never()).delete();
		verify(configEngine, never()).delete();
		assertTrue(configurator.getDbConfigMap().isEmpty());
		assertTrue(configurator.getDbModelMap().isEmpty());
		assertTrue(configurator.getEngineConfigMap().isEmpty());

		configurator = createConfiguratorMock();

		configurator.activate(Map.of(PROP_INSTANCES, "foo,bar,fizz", "fizz", "buzz"));
		verify(configurator, times(1)).doActivate(anyMap());
		verify(configurator, times(1)).createInstanceModels(anyMap());
		verify(configurator, times(3)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(3)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, never()).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doSetupDatabaseConfiguration(any(DatabaseModel.class));

		verify(configDatabase, never()).delete();
		verify(configEngine, never()).delete();
		assertTrue(configurator.getDbConfigMap().isEmpty());
		assertTrue(configurator.getDbModelMap().isEmpty());
		assertTrue(configurator.getEngineConfigMap().isEmpty());
	}
	
	@Test
	public void testInstancesOnlyDeactivate( ) throws ConfigurationException, IOException {
		Map<Object, Object> map = Collections.singletonMap(PROP_INSTANCES, "foo,bar,fizz");
		configurator.activate(map);
		verify(configurator, times(1)).doActivate(anyMap());
		verify(configurator, times(1)).createInstanceModels(anyMap());
		verify(configurator, times(3)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(3)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, never()).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doSetupDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doDeactivate(anyMap());
		verify(configurator, never()).teardownModel(any(InstanceModel.class));
		verify(configurator, never()).doTeardownConfiguration(any(InstanceModel.class));
		verify(configurator, never()).doTeardownDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doTeardownEngineConfiguration(any(DatabaseModel.class));
		
		verify(configDatabase, never()).delete();
		verify(configEngine, never()).delete();
		assertTrue(configurator.getDbConfigMap().isEmpty());
		assertTrue(configurator.getDbModelMap().isEmpty());
		assertTrue(configurator.getEngineConfigMap().isEmpty());
		
		configurator.deactivate(map);
		verify(configurator, times(1)).doActivate(anyMap());
		verify(configurator, times(1)).createInstanceModels(anyMap());
		verify(configurator, times(3)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(3)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, never()).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doSetupDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, times(1)).doDeactivate(anyMap());
		verify(configurator, times(3)).teardownModel(any(InstanceModel.class));
		verify(configurator, times(3)).doTeardownConfiguration(any(InstanceModel.class));
		verify(configurator, never()).doTeardownDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doTeardownEngineConfiguration(any(DatabaseModel.class));
		
		verify(configDatabase, never()).delete();
		verify(configEngine, never()).delete();
		assertTrue(configurator.getDbConfigMap().isEmpty());
		assertTrue(configurator.getDbModelMap().isEmpty());
		assertTrue(configurator.getEngineConfigMap().isEmpty());
	}
	
	@Test
	public void testInstancesOnlyModify( ) throws ConfigurationException, IOException {
		Map<Object, Object> map = Collections.singletonMap(PROP_INSTANCES, "foo,bar,fizz");
		configurator.activate(map);
		verify(configurator, times(1)).doActivate(anyMap());
		verify(configurator, times(1)).createInstanceModels(anyMap());
		verify(configurator, times(3)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(3)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, never()).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doSetupDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doDeactivate(anyMap());
		verify(configurator, never()).teardownModel(any(InstanceModel.class));
		verify(configurator, never()).doTeardownConfiguration(any(InstanceModel.class));
		verify(configurator, never()).doTeardownDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doTeardownEngineConfiguration(any(DatabaseModel.class));
		
		verify(configDatabase, never()).delete();
		verify(configEngine, never()).delete();
		assertTrue(configurator.getDbConfigMap().isEmpty());
		assertTrue(configurator.getDbModelMap().isEmpty());
		assertTrue(configurator.getEngineConfigMap().isEmpty());
		
		map = Collections.singletonMap(PROP_INSTANCES, "foo,bar");
		configurator.modified(map);
		
		verify(configurator, times(1)).doDeactivate(anyMap());
		verify(configurator, times(3)).teardownModel(any(InstanceModel.class));
		verify(configurator, times(3)).doTeardownConfiguration(any(InstanceModel.class));
		verify(configurator, never()).doTeardownDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doTeardownEngineConfiguration(any(DatabaseModel.class));
		
		verify(configurator, times(2)).doActivate(anyMap());
		verify(configurator, times(2)).createInstanceModels(anyMap());
		verify(configurator, times(5)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(5)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, never()).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doSetupDatabaseConfiguration(any(DatabaseModel.class));
		
		verify(configDatabase, never()).delete();
		verify(configEngine, never()).delete();
		assertTrue(configurator.getDbConfigMap().isEmpty());
		assertTrue(configurator.getDbModelMap().isEmpty());
		assertTrue(configurator.getEngineConfigMap().isEmpty());
		
		configurator.deactivate(map);
		
		verify(configurator, times(2)).doDeactivate(anyMap());
		verify(configurator, times(5)).teardownModel(any(InstanceModel.class));
		verify(configurator, times(5)).doTeardownConfiguration(any(InstanceModel.class));
		verify(configurator, never()).doTeardownDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doTeardownEngineConfiguration(any(DatabaseModel.class));
		
		verify(configurator, times(2)).doActivate(anyMap());
		verify(configurator, times(2)).createInstanceModels(anyMap());
		verify(configurator, times(5)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(5)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, never()).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doSetupDatabaseConfiguration(any(DatabaseModel.class));
		
		verify(configDatabase, never()).delete();
		verify(configEngine, never()).delete();
		assertTrue(configurator.getDbConfigMap().isEmpty());
		assertTrue(configurator.getDbModelMap().isEmpty());
		assertTrue(configurator.getEngineConfigMap().isEmpty());
	}

	@Test
	public void testInstanceWithDatabaseActivate( ) throws ConfigurationException, IOException {
		when(configurator.doSetupDatabaseConfiguration(any(DatabaseModel.class))).thenReturn(configDatabase);
		when(configurator.doSetupEngineConfiguration(any(DatabaseModel.class))).thenReturn(configEngine);

		configurator.activate(Map.of(PROP_INSTANCES, "foo,bar,fizz", "foo." + PROP_DATABASES, "test"));
		verify(configurator, times(1)).doActivate(anyMap());
		verify(configurator, times(1)).createInstanceModels(anyMap());
		verify(configurator, times(3)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(3)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, times(1)).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, times(1)).doSetupDatabaseConfiguration(any(DatabaseModel.class));

		verify(configDatabase, never()).delete();
		verify(configEngine, never()).delete();
		assertEquals(1, configurator.getDbConfigMap().size());
		assertEquals(1, configurator.getDbModelMap().size());
		assertEquals(1, configurator.getEngineConfigMap().size());
		assertTrue(configurator.getDbConfigMap().containsKey("foo.test"));
		assertTrue(configurator.getDbModelMap().containsKey("foo.test"));
		assertTrue(configurator.getEngineConfigMap().containsKey("foo.test"));

		configurator = createConfiguratorMock();

		when(configurator.doSetupDatabaseConfiguration(any(DatabaseModel.class))).thenReturn(configDatabase);
		when(configurator.doSetupEngineConfiguration(any(DatabaseModel.class))).thenReturn(configEngine);

		configurator.activate(Map.of(PROP_INSTANCES, "foo,bar,fizz", "foo." + PROP_DATABASES, "test,toast"));
		verify(configurator, times(1)).doActivate(anyMap());
		verify(configurator, times(1)).createInstanceModels(anyMap());
		verify(configurator, times(3)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(3)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, times(2)).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, times(2)).doSetupDatabaseConfiguration(any(DatabaseModel.class));

		verify(configDatabase, never()).delete();
		verify(configEngine, never()).delete();
		assertEquals(2, configurator.getDbConfigMap().size());
		assertEquals(2, configurator.getDbModelMap().size());
		assertEquals(2, configurator.getEngineConfigMap().size());
		assertTrue(configurator.getDbConfigMap().containsKey("foo.test"));
		assertTrue(configurator.getDbModelMap().containsKey("foo.test"));
		assertTrue(configurator.getEngineConfigMap().containsKey("foo.test"));
		assertTrue(configurator.getDbConfigMap().containsKey("foo.toast"));
		assertTrue(configurator.getDbModelMap().containsKey("foo.toast"));
		assertTrue(configurator.getEngineConfigMap().containsKey("foo.toast"));
	}

	@Test
	public void testInstanceWithDatabaseDeactivate( ) throws ConfigurationException, IOException {
		when(configurator.doSetupDatabaseConfiguration(any(DatabaseModel.class))).thenReturn(configDatabase);
		when(configurator.doSetupEngineConfiguration(any(DatabaseModel.class))).thenReturn(configEngine);

		Map<Object, Object> map = Map.of(PROP_INSTANCES, "foo,bar,fizz", "foo." + PROP_DATABASES, "test,toast");
		configurator.activate(map);
		
		verify(configurator, times(1)).doActivate(anyMap());
		verify(configurator, times(1)).createInstanceModels(anyMap());
		verify(configurator, times(3)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(3)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, times(2)).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, times(2)).doSetupDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doDeactivate(anyMap());
		verify(configurator, never()).teardownModel(any(InstanceModel.class));
		verify(configurator, never()).doTeardownConfiguration(any(InstanceModel.class));
		verify(configurator, never()).doTeardownDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doTeardownEngineConfiguration(any(DatabaseModel.class));

		verify(configDatabase, never()).delete();
		verify(configEngine, never()).delete();
		assertEquals(2, configurator.getDbConfigMap().size());
		assertEquals(2, configurator.getDbModelMap().size());
		assertEquals(2, configurator.getEngineConfigMap().size());
		
		assertTrue(configurator.getDbConfigMap().containsKey("foo.test"));
		assertTrue(configurator.getDbModelMap().containsKey("foo.test"));
		assertTrue(configurator.getEngineConfigMap().containsKey("foo.test"));
		assertTrue(configurator.getDbConfigMap().containsKey("foo.toast"));
		assertTrue(configurator.getDbModelMap().containsKey("foo.toast"));
		assertTrue(configurator.getEngineConfigMap().containsKey("foo.toast"));
		
		configurator.deactivate(map);
		
		verify(configurator, times(1)).doActivate(anyMap());
		verify(configurator, times(1)).createInstanceModels(anyMap());
		verify(configurator, times(3)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(3)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, times(2)).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, times(2)).doSetupDatabaseConfiguration(any(DatabaseModel.class));
		
		verify(configurator, times(1)).doDeactivate(anyMap());
		verify(configurator, times(3)).teardownModel(any(InstanceModel.class));
		verify(configurator, times(3)).doTeardownConfiguration(any(InstanceModel.class));
		verify(configurator, times(2)).doTeardownDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, times(2)).doTeardownEngineConfiguration(any(DatabaseModel.class));
		
		verify(configDatabase, times(2)).delete();
		verify(configEngine, times(2)).delete();
		assertTrue(configurator.getDbConfigMap().isEmpty());
		assertTrue(configurator.getDbModelMap().isEmpty());
		assertTrue(configurator.getEngineConfigMap().isEmpty());
	}
	
	@Test
	public void testInstanceWithDatabaseModify( ) throws ConfigurationException, IOException {
		when(configurator.doSetupDatabaseConfiguration(any(DatabaseModel.class))).thenReturn(configDatabase);
		when(configurator.doSetupEngineConfiguration(any(DatabaseModel.class))).thenReturn(configEngine);
		
		Map<Object, Object> map = Map.of(PROP_INSTANCES, "foo,bar,fizz", "foo." + PROP_DATABASES, "test,toast");
		configurator.activate(map);
		
		verify(configurator, times(1)).doActivate(anyMap());
		verify(configurator, times(1)).createInstanceModels(anyMap());
		verify(configurator, times(3)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(3)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, times(2)).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, times(2)).doSetupDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doDeactivate(anyMap());
		verify(configurator, never()).teardownModel(any(InstanceModel.class));
		verify(configurator, never()).doTeardownConfiguration(any(InstanceModel.class));
		verify(configurator, never()).doTeardownDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doTeardownEngineConfiguration(any(DatabaseModel.class));
		
		verify(configDatabase, never()).delete();
		verify(configEngine, never()).delete();
		assertEquals(2, configurator.getDbConfigMap().size());
		assertEquals(2, configurator.getDbModelMap().size());
		assertEquals(2, configurator.getEngineConfigMap().size());
		
		assertTrue(configurator.getDbConfigMap().containsKey("foo.test"));
		assertTrue(configurator.getDbModelMap().containsKey("foo.test"));
		assertTrue(configurator.getEngineConfigMap().containsKey("foo.test"));
		assertTrue(configurator.getDbConfigMap().containsKey("foo.toast"));
		assertTrue(configurator.getDbModelMap().containsKey("foo.toast"));
		assertTrue(configurator.getEngineConfigMap().containsKey("foo.toast"));
		
		// remove instance 'bar' and also remove database 'test' for instacne 'foo'
		map = Map.of(PROP_INSTANCES, "foo,fizz", "foo." + PROP_DATABASES, "toast");
		configurator.modified(map);
		
		verify(configurator, times(1)).doDeactivate(anyMap());
		verify(configurator, times(3)).teardownModel(any(InstanceModel.class));
		verify(configurator, times(3)).doTeardownConfiguration(any(InstanceModel.class));
		verify(configurator, times(2)).doTeardownDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, times(2)).doTeardownEngineConfiguration(any(DatabaseModel.class));
		
		verify(configurator, times(2)).doActivate(anyMap());
		verify(configurator, times(2)).createInstanceModels(anyMap());
		verify(configurator, times(5)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(5)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, times(3)).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, times(3)).doSetupDatabaseConfiguration(any(DatabaseModel.class));
		
		verify(configDatabase, times(2)).delete();
		verify(configEngine, times(2)).delete();
		assertEquals(1, configurator.getDbConfigMap().size());
		assertEquals(1, configurator.getDbModelMap().size());
		assertEquals(1, configurator.getEngineConfigMap().size());
		
		assertTrue(configurator.getDbConfigMap().containsKey("foo.toast"));
		assertTrue(configurator.getDbModelMap().containsKey("foo.toast"));
		assertTrue(configurator.getEngineConfigMap().containsKey("foo.toast"));
		
		configurator.deactivate(map);

		verify(configurator, times(2)).doDeactivate(anyMap());
		verify(configurator, times(5)).teardownModel(any(InstanceModel.class));
		verify(configurator, times(5)).doTeardownConfiguration(any(InstanceModel.class));
		verify(configurator, times(3)).doTeardownDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, times(3)).doTeardownEngineConfiguration(any(DatabaseModel.class));
		
		verify(configurator, times(2)).doActivate(anyMap());
		verify(configurator, times(2)).createInstanceModels(anyMap());
		verify(configurator, times(5)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(5)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, times(3)).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, times(3)).doSetupDatabaseConfiguration(any(DatabaseModel.class));
		
		verify(configDatabase, times(3)).delete();
		verify(configEngine, times(3)).delete();
		assertTrue(configurator.getDbConfigMap().isEmpty());
		assertTrue(configurator.getDbModelMap().isEmpty());
		assertTrue(configurator.getEngineConfigMap().isEmpty());
	}
	
	@Test
	public void testInstanceWithNullDatabaseConfigModify( ) throws ConfigurationException, IOException {
		when(configurator.doSetupDatabaseConfiguration(any(DatabaseModel.class))).thenAnswer(i->{
			DatabaseModel dbm = i.getArgument(0);
			return dbm.getName().endsWith("toast") ? configDatabase : null; 
		});
		when(configurator.doSetupEngineConfiguration(any(DatabaseModel.class))).thenReturn(configEngine);
		
		Map<Object, Object> map = Map.of(PROP_INSTANCES, "foo,bar,fizz", "foo." + PROP_DATABASES, "test,toast");
		configurator.activate(map);
		
		verify(configurator, times(1)).doActivate(anyMap());
		verify(configurator, times(1)).createInstanceModels(anyMap());
		verify(configurator, times(3)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(3)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, times(2)).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, times(2)).doSetupDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doDeactivate(anyMap());
		verify(configurator, never()).teardownModel(any(InstanceModel.class));
		verify(configurator, never()).doTeardownConfiguration(any(InstanceModel.class));
		verify(configurator, never()).doTeardownDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, never()).doTeardownEngineConfiguration(any(DatabaseModel.class));
		
		verify(configDatabase, never()).delete();
		verify(configEngine, never()).delete();
		assertEquals(1, configurator.getDbConfigMap().size());
		assertEquals(2, configurator.getDbModelMap().size());
		assertEquals(2, configurator.getEngineConfigMap().size());
		
		assertFalse(configurator.getDbConfigMap().containsKey("foo.test"));
		assertTrue(configurator.getDbModelMap().containsKey("foo.test"));
		assertTrue(configurator.getEngineConfigMap().containsKey("foo.test"));
		assertTrue(configurator.getDbConfigMap().containsKey("foo.toast"));
		assertTrue(configurator.getDbModelMap().containsKey("foo.toast"));
		assertTrue(configurator.getEngineConfigMap().containsKey("foo.toast"));
		
		// remove instance 'bar' and also remove database 'toast' for instance 'foo'
		map = Map.of(PROP_INSTANCES, "foo,fizz", "foo." + PROP_DATABASES, "test");
		configurator.modified(map);
		
		verify(configurator, times(1)).doDeactivate(anyMap());
		verify(configurator, times(3)).teardownModel(any(InstanceModel.class));
		verify(configurator, times(3)).doTeardownConfiguration(any(InstanceModel.class));
		verify(configurator, times(1)).doTeardownDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, times(2)).doTeardownEngineConfiguration(any(DatabaseModel.class));
		
		verify(configurator, times(2)).doActivate(anyMap());
		verify(configurator, times(2)).createInstanceModels(anyMap());
		verify(configurator, times(5)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(5)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, times(3)).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, times(3)).doSetupDatabaseConfiguration(any(DatabaseModel.class));
		
		verify(configDatabase, times(1)).delete();
		verify(configEngine, times(2)).delete();
		assertTrue(configurator.getDbConfigMap().isEmpty());
		assertEquals(1, configurator.getDbModelMap().size());
		assertEquals(1, configurator.getEngineConfigMap().size());
		
		assertTrue(configurator.getDbModelMap().containsKey("foo.test"));
		assertTrue(configurator.getEngineConfigMap().containsKey("foo.test"));
		
		configurator.deactivate(map);
		
		verify(configurator, times(2)).doDeactivate(anyMap());
		verify(configurator, times(5)).teardownModel(any(InstanceModel.class));
		verify(configurator, times(5)).doTeardownConfiguration(any(InstanceModel.class));
		verify(configurator, times(1)).doTeardownDatabaseConfiguration(any(DatabaseModel.class));
		verify(configurator, times(3)).doTeardownEngineConfiguration(any(DatabaseModel.class));
		
		verify(configurator, times(2)).doActivate(anyMap());
		verify(configurator, times(2)).createInstanceModels(anyMap());
		verify(configurator, times(5)).setupInstanceConfiguration(any(InstanceModel.class));
		verify(configurator, times(5)).doSetupInstanceModel(any(InstanceModel.class));
		verify(configurator, times(3)).doSetupEngineConfiguration(any(DatabaseModel.class));
		verify(configurator, times(3)).doSetupDatabaseConfiguration(any(DatabaseModel.class));
		
		verify(configDatabase, times(1)).delete();
		verify(configEngine, times(3)).delete();
		assertTrue(configurator.getDbConfigMap().isEmpty());
		assertTrue(configurator.getDbModelMap().isEmpty());
		assertTrue(configurator.getEngineConfigMap().isEmpty());
	}

}
