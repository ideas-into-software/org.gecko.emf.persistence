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
package org.gecko.persistence.datasource.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.osgi.service.jdbc.DataSourceFactory.JDBC_DATASOURCE_NAME;
import static org.osgi.service.jdbc.DataSourceFactory.JDBC_PASSWORD;
import static org.osgi.service.jdbc.DataSourceFactory.JDBC_PORT_NUMBER;
import static org.osgi.service.jdbc.DataSourceFactory.JDBC_SERVER_NAME;
import static org.osgi.service.jdbc.DataSourceFactory.JDBC_USER;
import static org.osgi.service.jdbc.DataSourceFactory.OSGI_JDBC_DRIVER_CLASS;
import static org.osgi.service.jdbc.DataSourceFactory.OSGI_JDBC_DRIVER_NAME;

import java.sql.Driver;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.config.InjectConfiguration;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.common.dictionary.Dictionaries;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * 
 * @author mark
 * @since 11.01.2023
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
@ExtendWith(MockitoExtension.class)
public class TestChangeDelegateDataSourceFactory {
	
	@Mock
	private DataSourceFactory sdf01;
	@Mock
	private DataSourceFactory sdf02;
	@Mock
	private Driver driver;
	@Mock
	private BundleContext ctx;
	private ServiceRegistration<DataSourceFactory> sdfRegistration01;
	private ServiceRegistration<DataSourceFactory> sdfRegistration02;

	@BeforeEach
	public void before(@InjectBundleContext BundleContext ctx) {
		this.ctx = ctx;
		Dictionary<String, Object> properties = Dictionaries.dictionaryOf(JDBC_DATASOURCE_NAME, "dummy", OSGI_JDBC_DRIVER_CLASS, "dummy.Driver", OSGI_JDBC_DRIVER_NAME, "dummyDriver", "foo", "bar");
		sdfRegistration01 = ctx.registerService(DataSourceFactory.class, sdf01, properties);
		properties = Dictionaries.dictionaryOf(JDBC_DATASOURCE_NAME, "test", OSGI_JDBC_DRIVER_CLASS, "test.Driver", OSGI_JDBC_DRIVER_NAME, "testDriver", "bar", "foo");
		sdfRegistration01 = ctx.registerService(DataSourceFactory.class, sdf01, properties);
	}
	
	@AfterEach
	public void after() {
		if (sdfRegistration01 != null) {
			sdfRegistration01.unregister();
		}
		if (sdfRegistration02 != null) {
			sdfRegistration02.unregister();
		}
	}
	
	/**
	 * Starting with a unmatching delegate filter
	 * @param dataSourceFactoryAware
	 * @param configuration
	 * @throws SQLException
	 */
	@Test
	@WithFactoryConfiguration(name="test", location = "?", factoryPid = "org.gecko.datasource", properties = {
			@Property(key = "datasource.name", value = "DummyTest"),
			@Property(key = "datasource.delegate.target", value = "(" + OSGI_JDBC_DRIVER_CLASS + "=foo.Driver)"),
			@Property(key = "datasource.serverName", value = "localhost"),
			@Property(key = "datasource.portNumber", value = "1234"),
			@Property(key = "datasource.user", value = "test"),
			@Property(key = "datasource.password", value = "1234")
	})
	public void testConfigurableDSFWrongDelegate01(@InjectService(cardinality = 0, filter = "(&(datasource.name=DummyTest)(datasource.type=configurable))") ServiceAware<DataSourceFactory> dataSourceFactoryAware,
			@InjectConfiguration(value="org.gecko.datasource~test") Configuration configuration) throws SQLException {
		assertTrue(dataSourceFactoryAware.isEmpty());
		
		AtomicReference<Properties> propRef = new AtomicReference<>();
		when(sdf01.createDriver(any())).thenAnswer(ir->{
			propRef.set((Properties)ir.getArgument(0));
			return driver;
		});
		Dictionary<String, Object> newConfig = new Hashtable<>(Dictionaries.asMap(configuration.getProperties()));
		newConfig.put("datasource.delegate.target", "(" + OSGI_JDBC_DRIVER_CLASS + "=dummy.Driver)");
		
		assertDoesNotThrow(()->configuration.updateIfDifferent(newConfig));
		
		assertDoesNotThrow(()->dataSourceFactoryAware.waitForService(500l));
		assertFalse(dataSourceFactoryAware.isEmpty());
		
		DataSourceFactory factory = dataSourceFactoryAware.getService();
		Driver d = factory.createDriver(null);
		assertNotNull(d);
		Properties properties = propRef.get();
		assertNotNull(properties);
		// configuration properties
		assertEquals("localhost", properties.get(JDBC_SERVER_NAME));
		assertEquals("1234", properties.get(JDBC_PORT_NUMBER));
		assertEquals("test", properties.get(JDBC_USER));
		assertEquals("1234", properties.get(JDBC_PASSWORD));
		assertEquals("dummy", properties.get(JDBC_DATASOURCE_NAME));
		assertFalse(properties.containsKey("cache"));
		// delegate service properties
		assertEquals("bar", properties.get("foo"));
		assertEquals("dummy.Driver", properties.get(OSGI_JDBC_DRIVER_CLASS));
		
		verify(sdf01, times(1)).createDriver(any());
	}
	
	/**
	 * Starting with a matching delegate filter and then put an unmatching filter in
	 * @param dataSourceFactoryAware
	 * @param configuration
	 * @throws SQLException
	 * @throws InterruptedException 
	 */
	@Test
	@WithFactoryConfiguration(name="test", location = "?", factoryPid = "org.gecko.datasource", properties = {
			@Property(key = "datasource.name", value = "DummyTest"),
			@Property(key = "datasource.delegate.target", value = "(" + OSGI_JDBC_DRIVER_CLASS + "=dummy.Driver)"),
			@Property(key = "datasource.serverName", value = "localhost"),
			@Property(key = "datasource.portNumber", value = "1234"),
			@Property(key = "datasource.user", value = "test"),
			@Property(key = "datasource.password", value = "1234")
	})
	public void testConfigurableDSFWrongDelegate02(@InjectService(cardinality = 0, filter = "(&(datasource.name=DummyTest)(datasource.type=configurable))") ServiceAware<DataSourceFactory> dataSourceFactoryAware,
			@InjectConfiguration(value="org.gecko.datasource~test") Configuration configuration) throws SQLException, InterruptedException {
		assertFalse(dataSourceFactoryAware.isEmpty());
		AtomicReference<Properties> propRef = new AtomicReference<>();
		when(sdf01.createDriver(any())).thenAnswer(ir->{
			propRef.set((Properties)ir.getArgument(0));
			return driver;
		});
		DataSourceFactory factory = dataSourceFactoryAware.getService();
		Driver d = factory.createDriver(null);
		assertNotNull(d);
		Properties properties = propRef.get();
		assertNotNull(properties);
		// configuration properties
		assertEquals("localhost", properties.get(JDBC_SERVER_NAME));
		assertEquals("1234", properties.get(JDBC_PORT_NUMBER));
		assertEquals("test", properties.get(JDBC_USER));
		assertEquals("1234", properties.get(JDBC_PASSWORD));
		assertEquals("dummy", properties.get(JDBC_DATASOURCE_NAME));
		assertFalse(properties.containsKey("cache"));
		// delegate service properties
		assertEquals("bar", properties.get("foo"));
		assertEquals("dummy.Driver", properties.get(OSGI_JDBC_DRIVER_CLASS));
		
		verify(sdf01, times(1)).createDriver(any());
		
		Dictionary<String, Object> newConfig = new Hashtable<>(Dictionaries.asMap(configuration.getProperties()));
		newConfig.put("datasource.delegate.target", "(" + OSGI_JDBC_DRIVER_CLASS + "=foo.Driver)");
		
		assertDoesNotThrow(()->configuration.updateIfDifferent(newConfig));
		Thread.sleep(1000l);
		try {
			assertNull(dataSourceFactoryAware.waitForService(500l));
		} catch (InterruptedException e) {
			fail("unexpected interruption");
		}
		assertTrue(dataSourceFactoryAware.isEmpty());
		
	}
	
	@Test
	@WithFactoryConfiguration(name="test", location = "?", factoryPid = "org.gecko.datasource", properties = {
			@Property(key = "datasource.name", value = "DummyTest"),
			@Property(key = "datasource.delegate.target", value = "(" + OSGI_JDBC_DRIVER_CLASS + "=dummy.Driver)"),
			@Property(key = "datasource.serverName", value = "localhost"),
			@Property(key = "datasource.portNumber", value = "1234"),
			@Property(key = "datasource.user", value = "test"),
			@Property(key = "datasource.password", value = "1234")
	})
	public void testConfigurableDSFChangeDelegate(@InjectService(filter = "(&(datasource.name=DummyTest)(datasource.type=configurable))") ServiceAware<DataSourceFactory> dataSourceFactoryAware,
			@InjectConfiguration(value="org.gecko.datasource~test") Configuration configuration) throws SQLException, InterruptedException {
		assertFalse(dataSourceFactoryAware.isEmpty());
		AtomicReference<Properties> propRef = new AtomicReference<>();
		when(sdf01.createDriver(any())).thenAnswer(ir->{
			propRef.set((Properties)ir.getArgument(0));
			return driver;
		});
		DataSourceFactory factory = dataSourceFactoryAware.getService();
		Driver d = factory.createDriver(null);
		assertNotNull(d);
		Properties properties = propRef.get();
		assertNotNull(properties);
		// configuration properties
		assertEquals("localhost", properties.get(JDBC_SERVER_NAME));
		assertEquals("1234", properties.get(JDBC_PORT_NUMBER));
		assertEquals("test", properties.get(JDBC_USER));
		assertEquals("1234", properties.get(JDBC_PASSWORD));
		assertEquals("dummy", properties.get(JDBC_DATASOURCE_NAME));
		assertFalse(properties.containsKey("cache"));
		// delegate service properties
		assertEquals("bar", properties.get("foo"));
		assertEquals("dummy.Driver", properties.get(OSGI_JDBC_DRIVER_CLASS));
		
		verify(sdf01, times(1)).createDriver(any());
		
		Dictionary<String, Object> newConfig = new Hashtable<>(Dictionaries.asMap(configuration.getProperties()));
		newConfig.put("datasource.delegate.target", "(" + OSGI_JDBC_DRIVER_CLASS + "=test.Driver)");
		
		assertDoesNotThrow(()->configuration.updateIfDifferent(newConfig));
		
		assertDoesNotThrow(()->dataSourceFactoryAware.waitForService(500l));
		
		assertFalse(dataSourceFactoryAware.isEmpty());
		Thread.sleep(500l);
		assertEquals("datasource.driver.configurable.DriverDummyTest", dataSourceFactoryAware.getServiceReference().getProperties().get(DataSourceFactory.OSGI_JDBC_DRIVER_CLASS));
		DataSourceFactory factoryNew = dataSourceFactoryAware.getService();
		
		// Instances must be the same
		assertNotEquals(factory, factoryNew);
		d = factoryNew.createDriver(null);
		assertNotNull(d);
		properties = propRef.get();
		assertNotNull(properties);
		// configuration properties
		assertEquals("localhost", properties.get(JDBC_SERVER_NAME));
		assertEquals("1234", properties.get(JDBC_PORT_NUMBER));
		assertEquals("test", properties.get(JDBC_USER));
		assertEquals("1234", properties.get(JDBC_PASSWORD));
		assertEquals("test", properties.get(JDBC_DATASOURCE_NAME));
		assertFalse(properties.containsKey("cache"));
		// delegate service properties
		assertEquals("foo", properties.get("bar"));
		assertEquals("test.Driver", properties.get(OSGI_JDBC_DRIVER_CLASS));
		
		verify(sdf01, times(2)).createDriver(any());
	}
}
