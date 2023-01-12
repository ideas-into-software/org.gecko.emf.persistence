/**
 * Copyright (c) 2012 - 2022 Data In Motion and others.
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.osgi.service.jdbc.DataSourceFactory.OSGI_JDBC_DRIVER_NAME;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.gecko.emf.persistence.helper.PersistenceHelper;
import org.gecko.emf.persistence.helper.PersistenceHelper.EMFPersistenceContext;
import org.gecko.emf.persistence.jdbc.JdbcHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.common.annotation.config.WithFactoryConfigurations;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

import de.jena.mdo.model.dbtree.DBTree;
import de.jena.mdo.model.dbtree.DbtreeFactory;
import de.jena.mdo.model.dbtree.DbtreePackage;

//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;

/**
 * See documentation here: 
 * 	https://github.com/osgi/osgi-test
 * 	https://github.com/osgi/osgi-test/wiki
 * Examples: https://github.com/osgi/osgi-test/tree/main/examples
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
//@ExtendWith(MockitoExtension.class)
public class TreeExampleDerbyTest {
	
	private static final String DB_TEMPLATE = "jdbc:derby:%s;create=true";
	private static final String TREE_BASE_URI = "jdbc://DerbyTest/derbytest";
//	@Mock
//	TestInterface test;
	
	@BeforeEach
	public void before(@InjectBundleContext BundleContext ctx) {
		
	}
	
	@Test
	@WithFactoryConfigurations ({
		@WithFactoryConfiguration(name="test", location = "?", factoryPid = "org.gecko.datasource", properties = {
				@Property(key = "datasource.name", value = "DerbyTest"),
				@Property(key = "datasource.delegate.target", value = "(" + OSGI_JDBC_DRIVER_NAME + "=derby)"),
				@Property(key = "datasource.dialect", value = "derby"),
				@Property(key = "datasource.databaseName", value = "TEST"),
				@Property(key = "datasource.serverName", value = "localhost"),
				@Property(key = "datasource.portNumber", value = "1234"),
				@Property(key = "datasource.user", value = "test"),
				@Property(key = "datasource.password", value = "1234")
		}),
		@WithFactoryConfiguration(name="Derby-Test", location = "?", factoryPid = "org.gecko.persistence.jdbc", properties = {
				@Property(key = "persistence.jdbc.name", value = "derbytest"),
				@Property(key = "persistence.jdbc.dsType", value = "XADataSource"),
				@Property(key = "persistence.jdbc.ds.target", value = "(datasource.name=DerbyTest)")
		})
	})
	public void testSaveTree(@InjectService(filter = "(&(emf.configurator.name=emf.persistence.jdbc.derbytest)(emf.model.name=dbtree))") ResourceSet resourceSet,
			@InjectService(filter = "(name=DerbyTest)") DataSourceFactory dsFactory) {
		assertNotNull(resourceSet);
		assertNotNull(dsFactory);
		String dbUrl = String.format(DB_TEMPLATE, "TEST");
		try {
			Connection con = dsFactory.createDriver(null).connect(dbUrl, null);
			assertFalse(JdbcHelper.existTable("TREE", con));
			EMFPersistenceContext context = PersistenceHelper.createPersistenceContext(TREE_BASE_URI, DbtreePackage.Literals.DB_TREE, null);
			
			Resource saveTreeResource = resourceSet.createResource(context.getUri());
			
			final Map<String, Object> saveOptions = context.getOptions();
			saveOptions.put("type", "derby");
			DBTree tree = DbtreeFactory.eINSTANCE.createDBTree();
			tree.setAlkNumber("123");
			tree.setArea("Paradies");
			tree.setGenre("Pinie");
			saveTreeResource.getContents().add(tree);
			saveTreeResource.save(saveOptions);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	@WithFactoryConfigurations ({
		@WithFactoryConfiguration(name="test", location = "?", factoryPid = "org.gecko.datasource", properties = {
				@Property(key = "datasource.name", value = "DerbyTest"),
				@Property(key = "datasource.delegate.target", value = "(" + OSGI_JDBC_DRIVER_NAME + "=derby)"),
				@Property(key = "datasource.dialect", value = "derby"),
				@Property(key = "datasource.databaseName", value = "TEST"),
				@Property(key = "datasource.serverName", value = "localhost"),
				@Property(key = "datasource.portNumber", value = "1234"),
				@Property(key = "datasource.user", value = "test"),
				@Property(key = "datasource.password", value = "1234")
		}),
		@WithFactoryConfiguration(name="Derby-Test", location = "?", factoryPid = "org.gecko.persistence.jdbc", properties = {
				@Property(key = "name", value = "derbytest"),
				@Property(key = "dataSource.target", value = "(datasource.name=DerbyTest)")
		})
	})
	public void testLoadTree(@InjectService(filter = "(&(emf.configurator.name=emf.persistence.jdbc.derbytest)(emf.model.name=dbtree))") ResourceSet resourceSet) {
		assertNotNull(resourceSet);
		EMFPersistenceContext context = PersistenceHelper.createPersistenceContext(TREE_BASE_URI, DbtreePackage.Literals.DB_TREE, null);
		
		Resource loadTreeResource = resourceSet.createResource(context.getUri());
		
		final Map<String, Object> loadOptions = context.getOptions();
		loadOptions.put("type", "derby");
		
		assertDoesNotThrow(()->loadTreeResource.load(loadOptions));
		assertTrue(loadTreeResource.getContents().isEmpty());
	}

}
