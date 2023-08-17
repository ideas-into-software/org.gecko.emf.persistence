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
package org.gecko.emf.repository.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.gecko.emf.osgi.example.model.basic.Address;
import org.gecko.emf.osgi.example.model.basic.BasicFactory;
import org.gecko.emf.osgi.example.model.basic.BasicPackage;
import org.gecko.emf.osgi.example.model.basic.Person;
import org.gecko.emf.persistence.ConstraintValidationException;
import org.gecko.emf.persistence.PersistenceConstants;
import org.gecko.emf.persistence.helper.PersistenceHelper;
import org.gecko.emf.repository.DefaultEMFRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.platform.commons.annotation.Testable;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * @author jalbert
 *
 */
@Testable
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class TestDefaultRepository {
	
	private class SimpleDefaultRepository extends DefaultEMFRepository {
		private Path tempdir;
		protected SimpleDefaultRepository(Path path) {
			this.tempdir = path;
		}
		
		public ResourceSet createResourceSet() {
			ResourceSet set = new ResourceSetImpl();
			BasicFactory.eINSTANCE.createAddress();
			return set;
		}

		public String getBaseUri() {
			return "file:/" + tempdir.toString();
		}

		protected void setIDs(EObject rootObject) {
			PersistenceHelper.setIds(rootObject);
		}
	}
	
	@Test
	public void testProxyGeneration(@TempDir Path tempDir) throws Exception {
		try (DefaultEMFRepository repository = new SimpleDefaultRepository(tempDir)) {

			Address proxy = repository.createProxy(BasicPackage.Literals.ADDRESS, "123");

			assertTrue(proxy.eIsProxy());
			URI eProxyURI = ((InternalEObject) proxy).eProxyURI();
			assertEquals("file:/" + tempDir.toString() + "/Address/123#123", eProxyURI.toString());
		}
	}

	@Test
	public void testUriHint_full(@TempDir Path tempDir) throws Exception {
		try (DefaultEMFRepository repository = new SimpleDefaultRepository(tempDir)) {

			Map<String, String> options = new HashMap<String, String>();

			String substitude = "AnotherTest";
			String prefix = "prefix_";
			String sufix = "_suffix";

			options.put(PersistenceConstants.URI_HINT, substitude);
			options.put(PersistenceConstants.URI_HINT_PREFIX, prefix);
			options.put(PersistenceConstants.URI_HINT_SUFFIX, sufix);

			URI uri = repository.createEClassUri("Test", options);

			assertEquals(2, uri.segmentCount());
			assertEquals(prefix + substitude + sufix, uri.segment(1));
		}
	}

	@Test
	public void testUriHint_fullWithEClass(@TempDir Path tempDir) throws Exception {
		try (DefaultEMFRepository repository = new SimpleDefaultRepository(tempDir)) {

			Map<String, String> options = new HashMap<String, String>();

			String substitude = "AnotherTest";
			String prefix = "prefix_";
			String suffix = "_suffix";

			options.put(PersistenceConstants.URI_HINT, substitude);
			options.put(PersistenceConstants.URI_HINT_PREFIX, prefix);
			options.put(PersistenceConstants.URI_HINT_SUFFIX, suffix);

			URI uri = repository.createEClassUri(BasicPackage.Literals.ADDRESS, options);

			assertEquals(2, uri.segmentCount());
			assertEquals(prefix + substitude + suffix, uri.segment(1));
		}
	}

	@Test
	public void testUriHint_fullWithEClassWithoutSubstitute(@TempDir Path tempDir) throws Exception {
		try (DefaultEMFRepository repository = new SimpleDefaultRepository(tempDir)) {

			Map<String, String> options = new HashMap<String, String>();

			String prefix = "prefix_";
			String suffix = "_suffix";

			options.put(PersistenceConstants.URI_HINT_PREFIX, prefix);
			options.put(PersistenceConstants.URI_HINT_SUFFIX, suffix);

			URI uri = repository.createEClassUri(BasicPackage.Literals.ADDRESS, options);

			assertEquals(2, uri.segmentCount());
			assertEquals(prefix + BasicPackage.Literals.ADDRESS.getName() + suffix, uri.segment(1));
		}
	}

	@Test
	public void testUriHint_prefixOnly(@TempDir Path tempDir) throws Exception {
		try (DefaultEMFRepository repository = new SimpleDefaultRepository(tempDir)) {

			Map<String, String> options = new HashMap<String, String>();

			String prefix = "prefix_";

			options.put(PersistenceConstants.URI_HINT_PREFIX, prefix);

			URI uri = repository.createEClassUri("Test", options);

			assertEquals(2, uri.segmentCount());
			assertEquals(prefix + "Test", uri.segment(1));
		}
	}

	@Test
	public void testUriHint_suffixOnly(@TempDir Path tempDir) throws Exception {
		try (DefaultEMFRepository repository = new SimpleDefaultRepository(tempDir)) {

			Map<String, String> options = new HashMap<String, String>();

			String suffix = "_suffix";

			options.put(PersistenceConstants.URI_HINT_SUFFIX, suffix);

			URI uri = repository.createEClassUri("Test", options);

			assertEquals(2, uri.segmentCount());
			assertEquals("Test" + suffix, uri.segment(1));
		}
	}

	@Test
	public void testNonContainmentCheckWithError() {
		ResourceSet set = new ResourceSetImpl();
		set.getResourceFactoryRegistry().getProtocolToFactoryMap().put("file", new XMIResourceFactoryImpl());
		Address address = BasicFactory.eINSTANCE.createAddress();

		Person p = BasicFactory.eINSTANCE.createPerson();
		p.setAddress(address);

		Assertions.assertThrows(ConstraintValidationException.class, ()->{
			Resource r = set.createResource(URI.createURI("file://test"));
			r.getContents().add(p);
			
			PersistenceHelper.checkForAttachedNonContainmentReferences(p);
			
		});
	}

	@Test
	public void testNonContainmentCheckNoError() {
		ResourceSet set = new ResourceSetImpl();
		set.getResourceFactoryRegistry().getProtocolToFactoryMap().put("file", new XMIResourceFactoryImpl());
		Address address = BasicFactory.eINSTANCE.createAddress();

		Person p = BasicFactory.eINSTANCE.createPerson();
		p.setAddress(address);

		Resource r = set.createResource(URI.createURI("file://test"));
		r.getContents().add(p);

		Resource rAddress = set.createResource(URI.createURI("file://testAddress"));
		rAddress.getContents().add(address);

		PersistenceHelper.checkForAttachedNonContainmentReferences(p);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testNonContainmentCheckWithErrorMany() {
		ResourceSet set = new ResourceSetImpl();
		set.getResourceFactoryRegistry().getProtocolToFactoryMap().put("file", new XMIResourceFactoryImpl());

		Assertions.assertThrows(ConstraintValidationException.class, ()->{
			List<Person> many = new ArrayList<Person>();
			for (int i = 0; i < 20; i++) {
				Address address = BasicFactory.eINSTANCE.createAddress();

				Person p = BasicFactory.eINSTANCE.createPerson();
				p.setAddress(address);

				Resource r = set.createResource(URI.createURI("file://test"));
				r.getContents().add(p);
				many.add(p);
			}

			PersistenceHelper.checkForAttachedNonContainmentReferences((Collection) many);
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testNonContainmentCheckNoErrorMany() {
		ResourceSet set = new ResourceSetImpl();
		set.getResourceFactoryRegistry().getProtocolToFactoryMap().put("file", new XMIResourceFactoryImpl());

		List<Person> many = new ArrayList<Person>();
		for (int i = 0; i < 20; i++) {

			Address address = BasicFactory.eINSTANCE.createAddress();

			Person p = BasicFactory.eINSTANCE.createPerson();
			p.setAddress(address);

			Resource r = set.createResource(URI.createURI("file://test"));
			r.getContents().add(p);

			Resource rAddress = set.createResource(URI.createURI("file://testAddress"));
			rAddress.getContents().add(address);

			many.add(p);
		}
		PersistenceHelper.checkForAttachedNonContainmentReferences((Collection) many);
	}

}
