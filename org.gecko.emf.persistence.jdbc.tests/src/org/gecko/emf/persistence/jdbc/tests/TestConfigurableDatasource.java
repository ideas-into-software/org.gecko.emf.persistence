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
package org.gecko.emf.persistence.jdbc.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
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
public class TestConfigurableDatasource {
	
	@Mock
	private DataSourceFactory sdf;
	@Mock
	private Driver driver;
	@Mock
	private BundleContext ctx;
	private ServiceRegistration<DataSourceFactory> sdfRegistration;

	@BeforeEach
	public void before(@InjectBundleContext BundleContext ctx) {
		this.ctx = ctx;
		Dictionary<String, Object> properties = Dictionaries.dictionaryOf(JDBC_DATASOURCE_NAME, "dummy", OSGI_JDBC_DRIVER_CLASS, "dummy.Driver", OSGI_JDBC_DRIVER_NAME, "dummyDriver", "foo", "bar");
		sdfRegistration = ctx.registerService(DataSourceFactory.class, sdf, properties);
	}
	
	@AfterEach
	public void after() {
		if (sdfRegistration != null) {
			sdfRegistration.unregister();
		}
	}
	
	@Test
	@WithFactoryConfiguration(name="test", location = "?", factoryPid = "org.gecko.datasource", properties = {
			@Property(key = "datasource.name", value = "DummyTest"),
			@Property(key = "datasource.delegate.target", value = "(" + OSGI_JDBC_DRIVER_NAME + "=" + org.postgresql.util.DriverInfo.DRIVER_NAME + ")"),
			@Property(key = "datasource.dialect", value = "postgresql"),
			@Property(key = "datasource.serverName", value = "localhost"),
			@Property(key = "datasource.portNumber", value = "1234"),
			@Property(key = "datasource.user", value = "test"),
			@Property(key = "datasource.password", value = "1234")
	})
	public void testConfigurableDSFPostgresQL(@InjectService(timeout = 1000, filter = "(&(datasource.name=DummyTest)(datasource.type=configurable))") ServiceAware<DataSourceFactory> dataSourceFactoryAware) throws SQLException {
		assertFalse(dataSourceFactoryAware.isEmpty());
		DataSourceFactory factory = dataSourceFactoryAware.getService();
		Driver d = factory.createDriver(null);
		assertNotNull(d);
//		Properties properties = new Properties();
//		properties.put(JDBC_USER, "test");
//		properties.put(JDBC_PASSWORD, "1234");
//		Connection connection = d.connect("jdbc:postgresql://localhost/demo", properties);
//		boolean existTable = JdbcHelper.existTable("USERS", connection);
//		assertTrue(existTable);
//		try (Statement statement = connection.createStatement()) {
//			ResultSet result = statement.executeQuery("SELECT * FROM \"USERS\"");
//			while(result.next()) {
//				System.out.println("Name " + result.getString(2) + " " + result.getString(3));
//			}
//		};
	}
	
	@Test
	@WithFactoryConfiguration(name="test", location = "?", factoryPid = "org.gecko.datasource", properties = {
			@Property(key = "datasource.name", value = "DummyTest"),
			@Property(key = "datasource.delegate.target", value = "(" + OSGI_JDBC_DRIVER_NAME + "=derby)"),
			@Property(key = "datasource.dialect", value = "derby"),
			@Property(key = "datasource.serverName", value = "localhost"),
			@Property(key = "datasource.portNumber", value = "1234"),
			@Property(key = "datasource.user", value = "test"),
			@Property(key = "datasource.password", value = "1234")
	})
	public void testConfigurableDSFDerby(@InjectService(timeout = 1000, filter = "(&(datasource.name=DummyTest)(datasource.type=configurable))") ServiceAware<DataSourceFactory> dataSourceFactoryAware) throws SQLException {
		assertFalse(dataSourceFactoryAware.isEmpty());
		DataSourceFactory factory = dataSourceFactoryAware.getService();
		Driver d = factory.createDriver(null);
		assertNotNull(d);
//		Properties properties = new Properties();
//		properties.put(JDBC_USER, "test");
//		properties.put(JDBC_PASSWORD, "1234");
//		Connection connection = d.connect("jdbc:derby:TEST;create=true", properties);
//		boolean existTable = JdbcHelper.existTable("USERS", connection);
//		assertTrue(existTable);
//		try (Statement statement = connection.createStatement()) {
//			boolean createTable = statement.execute("CREATE TABLE \"USERS\" (\n"
//					+ "	\"ID\"	INTEGER NOT NULL,\n"
//					+ "	\"FIRST_NAME\"	VARCHAR(255),\n"
//					+ "	\"LAST_NAME\"	VARCHAR(255),\n"
//					+ "	\"BIRTH_DATE\"	DATE,\n"
//					+ "	PRIMARY KEY(\"ID\")\n"
//					+ ")");
//			assertTrue(createTable);
//			statement.addBatch("INSERT INTO \"USERS\" VALUES (1, 'Mark', 'Hoffmann', '1977-04-21')");
//			statement.addBatch("INSERT INTO \"USERS\" VALUES (2, 'Jürgen', 'Albert', '1984-03-30')");
//			int[] executeBatch = statement.executeBatch();
//			assertEquals(2, executeBatch.length);
//			ResultSet result = statement.executeQuery("SELECT * FROM \"USERS\"");
//			while(result.next()) {
//				System.out.println("Name " + result.getString(2) + " " + result.getString(3));
//			}
//		}
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
	public void testConfigurableDSF(@InjectService(filter = "(&(datasource.name=DummyTest)(datasource.type=configurable))") ServiceAware<DataSourceFactory> dataSourceFactoryAware) throws SQLException {
		assertFalse(dataSourceFactoryAware.isEmpty());
		AtomicReference<Properties> propRef = new AtomicReference<>();
		when(sdf.createDriver(any())).thenAnswer(ir->{
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
		
		verify(sdf, times(1)).createDriver(any());
	}
	
	@Test
	@WithFactoryConfiguration(name="test", location = "?", factoryPid = "org.gecko.datasource", properties = {
			@Property(key = "datasource.name", value = "DummyTest"),
			@Property(key = "datasource.delegate.target", value = "(" + OSGI_JDBC_DRIVER_CLASS + "=dummy.Driver)"),
			@Property(key = "datasource.serverName", value = "localhost"),
			@Property(key = "datasource.portNumber", value = "1234"),
			@Property(key = "datasource.cache", value = "true"),
			@Property(key = "pool", value = "12")
	})
	public void testConfigurableDSFCustomProperties(@InjectService(filter = "(&(datasource.name=DummyTest)(datasource.type=configurable))") ServiceAware<DataSourceFactory> dataSourceFactoryAware) throws SQLException {
		assertFalse(dataSourceFactoryAware.isEmpty());
		AtomicReference<Properties> propRef = new AtomicReference<>();
		when(sdf.createDriver(any())).thenAnswer(ir->{
			propRef.set((Properties)ir.getArgument(0));
			return driver;
		});
		DataSourceFactory factory = dataSourceFactoryAware.getService();
		Driver d = factory.createDriver(null);
		assertNotNull(d);
		Properties properties = propRef.get();
		assertNotNull(properties);
		assertEquals("localhost", properties.get(JDBC_SERVER_NAME));
		assertEquals("1234", properties.get(JDBC_PORT_NUMBER));
		assertEquals("dummy", properties.get(JDBC_DATASOURCE_NAME));
		
		// custom configuration property
		assertEquals("true", properties.get("cache"));
		assertFalse(properties.containsKey("pool"));
		
		verify(sdf, times(1)).createDriver(any());
	}
	
	@Test
	@WithFactoryConfiguration(name="test", location = "?", factoryPid = "org.gecko.datasource", properties = {
			@Property(key = "datasource.name", value = "DummyTest"),
			@Property(key = "datasource.delegate.target", value = "(" + OSGI_JDBC_DRIVER_CLASS + "=dummy.Driver)"),
			@Property(key = "datasource.serverName", value = "localhost"),
			@Property(key = "datasource.dataSourceName", value = "myName"),
			@Property(key = "datasource.portNumber", value = "1234"),
			@Property(key = "datasource.user", value = "test"),
			@Property(key = "datasource.password", value = "1234"),
			@Property(key = "datasource.cache", value = "true")
	})
	public void testConfigurableDSFPriority(@InjectService(filter = "(&(datasource.name=DummyTest)(datasource.type=configurable))") ServiceAware<DataSourceFactory> dataSourceFactoryAware) throws SQLException {
		assertFalse(dataSourceFactoryAware.isEmpty());
		AtomicReference<Properties> propRef = new AtomicReference<>();
		when(sdf.createDriver(any())).thenAnswer(ir->{
			propRef.set((Properties)ir.getArgument(0));
			return driver;
		});
		DataSourceFactory factory = dataSourceFactoryAware.getService();
		Driver d = factory.createDriver(null);
		assertNotNull(d);
		Properties properties = propRef.get();
		assertNotNull(properties);
		assertEquals("localhost", properties.get(JDBC_SERVER_NAME));
		assertEquals("1234", properties.get(JDBC_PORT_NUMBER));
		assertEquals("test", properties.get(JDBC_USER));
		assertEquals("1234", properties.get(JDBC_PASSWORD));
		assertEquals("true", properties.get("cache"));
		// expect dataSourceName from configuration instead from the deleaget properties
		assertEquals("myName", properties.get(JDBC_DATASOURCE_NAME));
		assertEquals("dummy.Driver", properties.get(OSGI_JDBC_DRIVER_CLASS));
		
		Properties userProps = new Properties();
		userProps.put(JDBC_DATASOURCE_NAME, "superDSName");
		userProps.put(OSGI_JDBC_DRIVER_CLASS, "my.Driver");
		d = factory.createDriver(userProps);
		assertNotNull(d);
		properties = propRef.get();
		assertNotNull(properties);
		assertEquals("localhost", properties.get(JDBC_SERVER_NAME));
		assertEquals("1234", properties.get(JDBC_PORT_NUMBER));
		assertEquals("test", properties.get(JDBC_USER));
		assertEquals("1234", properties.get(JDBC_PASSWORD));
		assertEquals("true", properties.get("cache"));
		// expect dataSourceName from configuration instead from the deleaget properties
		assertEquals("superDSName", properties.get(JDBC_DATASOURCE_NAME));
		assertEquals("my.Driver", properties.get(OSGI_JDBC_DRIVER_CLASS));
		
		verify(sdf, times(2)).createDriver(any());
	}
	
	@Test
	@WithFactoryConfiguration(name="test", location = "?", factoryPid = "org.gecko.datasource", properties = {
			@Property(key = "datasource.name", value = "DummyTest"),
			@Property(key = "datasource.delegate.target", value = "(" + OSGI_JDBC_DRIVER_CLASS + "=dummy.Driver)"),
			@Property(key = "datasource.user", value = "test"),
			@Property(key = "datasource.password", value = "1234")
	})
	public void testConfigurableDSFPriorityEnv(@InjectService(filter = "(&(datasource.name=DummyTest)(datasource.type=configurable))") ServiceAware<DataSourceFactory> dataSourceFactoryAware) throws SQLException {
		assertFalse(dataSourceFactoryAware.isEmpty());
		AtomicReference<Properties> propRef = new AtomicReference<>();
		when(sdf.createDriver(any())).thenAnswer(ir->{
			propRef.set((Properties)ir.getArgument(0));
			return driver;
		});
		DataSourceFactory factory = dataSourceFactoryAware.getService();
		Driver d = factory.createDriver(null);
		assertNotNull(d);
		Properties properties = propRef.get();
		assertNotNull(properties);
		assertEquals("test", properties.get(JDBC_USER));
		assertEquals("1234", properties.get(JDBC_PASSWORD));
		
		Properties userProps = new Properties();
		userProps.put(JDBC_USER, "admin");
		userProps.put(JDBC_PASSWORD, "424242");
		d = factory.createDriver(userProps);
		assertNotNull(d);
		properties = propRef.get();
		assertNotNull(properties);
		assertEquals("admin", properties.get(JDBC_USER));
		assertEquals("424242", properties.get(JDBC_PASSWORD));
		
		System.setProperty("datasource.user", "propRoot");
		System.setProperty("datasource.password", "propPwd");
		
		d = factory.createDriver(userProps);
		assertNotNull(d);
		properties = propRef.get();
		assertNotNull(properties);
		assertEquals("admin", properties.get(JDBC_USER));
		assertEquals("424242", properties.get(JDBC_PASSWORD));
		
		System.setProperty("datasource.DummyTest.user", "propRoot");
		System.setProperty("datasource.DummyTest.password", "propPwd");
		
		d = factory.createDriver(userProps);
		assertNotNull(d);
		properties = propRef.get();
		assertNotNull(properties);
		assertEquals("propRoot", properties.get(JDBC_USER));
		assertEquals("propPwd", properties.get(JDBC_PASSWORD));
		
		System.clearProperty("datasource.DummyTest.user");
		System.clearProperty("datasource.DummyTest.password");
		
		
		d = factory.createDriver(userProps);
		assertNotNull(d);
		properties = propRef.get();
		assertNotNull(properties);
		assertEquals("admin", properties.get(JDBC_USER));
		assertEquals("424242", properties.get(JDBC_PASSWORD));
		
		verify(sdf, times(5)).createDriver(any());
	}
}
