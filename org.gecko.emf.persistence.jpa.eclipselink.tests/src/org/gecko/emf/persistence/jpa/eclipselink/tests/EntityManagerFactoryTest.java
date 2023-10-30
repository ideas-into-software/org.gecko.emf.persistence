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
package org.gecko.emf.persistence.jpa.eclipselink.tests;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.sql.DataSource;

import org.apache.derby.jdbc.BasicEmbeddedDataSource40;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.persistence.dynamic.DynamicEntity;
import org.eclipse.persistence.jpa.JpaHelper;
import org.gecko.emf.persistence.jpa.eclipselink.EntityManagerFactoryConfigurator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.BundleContext;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.config.WithConfiguration;
import org.osgi.test.common.annotation.config.WithConfigurations;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
@ExtendWith(MockitoExtension.class)
public class EntityManagerFactoryTest {

	@BeforeEach
	void beforeEach(@InjectBundleContext BundleContext ctx) {

		BasicEmbeddedDataSource40 ds = new BasicEmbeddedDataSource40();
		ds.setDatabaseName("dbDynamicTest");
		ds.setCreateDatabase("create");

		Dictionary<String, Object> dict = new Hashtable<String, Object>();
		dict.put("testSelector", "1");
		ctx.registerService(DataSource.class, ds, dict);

	}	

	private static void registerEPackage(BundleContext bc, String url) {
		ResourceSet resourceSet = new ResourceSetImpl();

		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
				new EcoreResourceFactoryImpl());

		URI fileURI = org.eclipse.emf.common.util.URI.createURI(url);

		Resource resource = resourceSet.getResource(fileURI, true);
		EObject eObject = resource.getContents().get(0);
		if (eObject instanceof EPackage) {
			EPackage p = (EPackage) eObject;

			Dictionary<String, Object> dict = new Hashtable<String, Object>();
			dict.put("emf.model.name", p.getName());
			bc.registerService(EPackage.class, p, dict);

		}
	}

	@WithConfigurations(value = {
			@WithConfiguration(location = "?", pid = EntityManagerFactoryConfigurator.PID, properties = {
					@org.osgi.test.common.annotation.Property(key = "dataSource.target", value = "(testSelector=1)"),
					@org.osgi.test.common.annotation.Property(key = "ePackage.target", value = "(emf.model.name=simple)"),
					@org.osgi.test.common.annotation.Property(key = "oRMappingProvider.target", value = "(a=b)")
			})
	})

	@Test
	public void testModelBasicWithoutMapper(@InjectBundleContext BundleContext bc,
			@InjectService(cardinality = 0) ServiceAware<EntityManagerFactory> entityManagerFactoryAware)
			throws SQLException, InterruptedException {

		registerEPackage(bc, "ecore/simple.ecore");
		EntityManagerFactory entityManagerFactory = entityManagerFactoryAware.waitForService(100000);

		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();

		org.eclipse.persistence.descriptors.ClassDescriptor descriptor = JpaHelper
				.getServerSession(entityManagerFactory).getDescriptorForAlias("ClassOne");

		DynamicEntity dynamicEntitySimpleEObject = (DynamicEntity) descriptor.getInstantiationPolicy()
				.buildNewInstance();

		dynamicEntitySimpleEObject.set("attributeOne", "foo");

		em.persist(dynamicEntitySimpleEObject);
		em.flush();
		em.getTransaction().commit();
		em.clear();

	}

}
