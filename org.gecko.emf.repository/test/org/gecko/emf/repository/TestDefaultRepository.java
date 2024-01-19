/**
 * Copyright (c) 2017 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.gecko.emf.osgi.example.model.basic.PersonObject;
import org.gecko.emf.osgi.example.model.basic.impl.BasicPackageImpl;
import org.gecko.emf.repository.exception.ConstraintValidationException;
import org.gecko.emf.repository.helper.RepositoryHelper;
import org.junit.jupiter.api.Test;

/**
 * @author jalbert
 *
 */
public class TestDefaultRepository {

	@Test
	public void testProxyGeneration() throws Exception {
		try (DefaultEMFRepository defaultEMFRepository = new DefaultEMFRepository() {

			@Override
			public ResourceSet createResourceSet() {
				ResourceSet set = new ResourceSetImpl();
				BasicFactory.eINSTANCE.createAddress();
				return set;
			}

			@Override
			public String getBaseUri() {
				return "file:/test";
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.gecko.emf.repository.DefaultEMFRepository#setIDs(org.eclipse.emf.ecore.
			 * EObject)
			 */
			@Override
			protected void setIDs(EObject rootObject) {
				RepositoryHelper.setIds(rootObject);
			}
		}) {

			Address proxy = defaultEMFRepository.createProxy(BasicPackage.Literals.ADDRESS, "123");

			assertTrue(proxy.eIsProxy());
			URI eProxyURI = ((InternalEObject) proxy).eProxyURI();
			assertEquals("file:/test/Address/123#123", eProxyURI.toString());
		}
	}

	@Test
	public void testUriHint_full() throws Exception {
		try (DefaultEMFRepository defaultEMFRepository = new DefaultEMFRepository() {

			@Override
			public ResourceSet createResourceSet() {
				ResourceSet set = new ResourceSetImpl();
				BasicFactory.eINSTANCE.createAddress();
				return set;
			}

			@Override
			public String getBaseUri() {
				return "file:/test";
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.gecko.emf.repository.DefaultEMFRepository#setIDs(org.eclipse.emf.ecore.
			 * EObject)
			 */
			@Override
			protected void setIDs(EObject rootObject) {
				RepositoryHelper.setIds(rootObject);
			}
		}) {

			Map<String, String> options = new HashMap<String, String>();

			String substitude = "AnotherTest";
			String prefix = "prefix_";
			String sufix = "_sufix";

			options.put(RepositoryConstants.URI_HINT, substitude);
			options.put(RepositoryConstants.URI_HINT_PREFIX, prefix);
			options.put(RepositoryConstants.URI_HINT_SUFIX, sufix);

			URI uri = defaultEMFRepository.createEClassUri("Test", options);

			assertEquals(2, uri.segmentCount());
			assertEquals(prefix + substitude + sufix, uri.segment(1));
		}
	}

	@Test
	public void testUriHint_fullWithEClass() throws Exception {
		try (DefaultEMFRepository defaultEMFRepository = new DefaultEMFRepository() {

			@Override
			public ResourceSet createResourceSet() {
				ResourceSet set = new ResourceSetImpl();
				BasicFactory.eINSTANCE.createAddress();
				return set;
			}

			@Override
			public String getBaseUri() {
				return "file:/test";
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.gecko.emf.repository.DefaultEMFRepository#setIDs(org.eclipse.emf.ecore.
			 * EObject)
			 */
			@Override
			protected void setIDs(EObject rootObject) {
				RepositoryHelper.setIds(rootObject);
			}
		}){

			Map<String, String> options = new HashMap<String, String>();

			String substitude = "AnotherTest";
			String prefix = "prefix_";
			String sufix = "_sufix";

			options.put(RepositoryConstants.URI_HINT, substitude);
			options.put(RepositoryConstants.URI_HINT_PREFIX, prefix);
			options.put(RepositoryConstants.URI_HINT_SUFIX, sufix);

			URI uri = defaultEMFRepository.createEClassUri(BasicPackage.Literals.ADDRESS, options);

			assertEquals(2, uri.segmentCount());
			assertEquals(prefix + substitude + sufix, uri.segment(1));
		}
	}

	@Test
	public void testUriHint_fullWithEClassWithoutSubstitude() throws Exception {
		try (DefaultEMFRepository defaultEMFRepository = new DefaultEMFRepository() {

			@Override
			public ResourceSet createResourceSet() {
				ResourceSet set = new ResourceSetImpl();
				BasicFactory.eINSTANCE.createAddress();
				return set;
			}

			@Override
			public String getBaseUri() {
				return "file:/test";
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.gecko.emf.repository.DefaultEMFRepository#setIDs(org.eclipse.emf.ecore.
			 * EObject)
			 */
			@Override
			protected void setIDs(EObject rootObject) {
				RepositoryHelper.setIds(rootObject);
			}
		}) {

			Map<String, String> options = new HashMap<String, String>();

			String prefix = "prefix_";
			String sufix = "_sufix";

			options.put(RepositoryConstants.URI_HINT_PREFIX, prefix);
			options.put(RepositoryConstants.URI_HINT_SUFIX, sufix);

			URI uri = defaultEMFRepository.createEClassUri(BasicPackage.Literals.ADDRESS, options);

			assertEquals(2, uri.segmentCount());
			assertEquals(prefix + BasicPackage.Literals.ADDRESS.getName() + sufix, uri.segment(1));
		}
	}

	@Test
	public void testUriHint_prefixOnly() throws Exception {
		try (DefaultEMFRepository defaultEMFRepository = new DefaultEMFRepository() {

			@Override
			public ResourceSet createResourceSet() {
				ResourceSet set = new ResourceSetImpl();
				BasicFactory.eINSTANCE.createAddress();
				return set;
			}

			@Override
			public String getBaseUri() {
				return "file:/test";
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.gecko.emf.repository.DefaultEMFRepository#setIDs(org.eclipse.emf.ecore.
			 * EObject)
			 */
			@Override
			protected void setIDs(EObject rootObject) {
				RepositoryHelper.setIds(rootObject);
			}
		}) {

			Map<String, String> options = new HashMap<String, String>();

			String prefix = "prefix_";

			options.put(RepositoryConstants.URI_HINT_PREFIX, prefix);

			URI uri = defaultEMFRepository.createEClassUri("Test", options);

			assertEquals(2, uri.segmentCount());
			assertEquals(prefix + "Test", uri.segment(1));
		}
	}

	@Test
	public void testUriHint_sufixOnly() throws Exception {
		try (DefaultEMFRepository defaultEMFRepository = new DefaultEMFRepository() {

			@Override
			public ResourceSet createResourceSet() {
				ResourceSet set = new ResourceSetImpl();
				BasicFactory.eINSTANCE.createAddress();
				return set;
			}

			@Override
			public String getBaseUri() {
				return "file:/test";
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.gecko.emf.repository.DefaultEMFRepository#setIDs(org.eclipse.emf.ecore.
			 * EObject)
			 */
			@Override
			protected void setIDs(EObject rootObject) {
				RepositoryHelper.setIds(rootObject);
			}
		}) {

			Map<String, String> options = new HashMap<String, String>();

			String sufix = "_sufix";

			options.put(RepositoryConstants.URI_HINT_SUFIX, sufix);

			URI uri = defaultEMFRepository.createEClassUri("Test", options);

			assertEquals(2, uri.segmentCount());
			assertEquals("Test" + sufix, uri.segment(1));
		}
	}

	@Test
	public void testNonContainmentCheckWithError() {
		ResourceSet set = new ResourceSetImpl();
		set.getResourceFactoryRegistry().getProtocolToFactoryMap().put("file", new XMIResourceFactoryImpl());
		Address address = BasicFactory.eINSTANCE.createAddress();

		Person p = BasicFactory.eINSTANCE.createPerson();
		p.setAddress(address);		

		Resource r = set.createResource(URI.createURI("file://test"));
		r.getContents().add(p);

		assertThrows(ConstraintValidationException.class, ()-> RepositoryHelper.checkForAttachedNonContainmentReferences(p));
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

		RepositoryHelper.checkForAttachedNonContainmentReferences(p);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testNonContainmentCheckWithErrorMany() {
		ResourceSet set = new ResourceSetImpl();
		set.getResourceFactoryRegistry().getProtocolToFactoryMap().put("file", new XMIResourceFactoryImpl());

		List<Person> many = new ArrayList<Person>();
		for (int i = 0; i < 20; i++) {
			Address address = BasicFactory.eINSTANCE.createAddress();

			Person p = BasicFactory.eINSTANCE.createPerson();
			p.setAddress(address);

			Resource r = set.createResource(URI.createURI("file://test"));
			r.getContents().add(p);
			many.add(p);
		}

		assertThrows(ConstraintValidationException.class, ()-> RepositoryHelper.checkForAttachedNonContainmentReferences((Collection) many));
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
		RepositoryHelper.checkForAttachedNonContainmentReferences((Collection) many);
	}
	
	@Test
	public void testNonContainmentCheckEClassNoError() {
		ResourceSet set = new ResourceSetImpl();
		set.getResourceFactoryRegistry().getProtocolToFactoryMap().put("file", new XMIResourceFactoryImpl());
		set.getPackageRegistry().put("test", BasicPackageImpl.init());
		
		PersonObject p = BasicFactory.eINSTANCE.createPersonObject();
		p.setType(BasicPackage.Literals.BUSINESS_PERSON);		

		Resource r = set.createResource(URI.createURI("file://test"));
		r.getContents().add(p);

		RepositoryHelper.checkForAttachedNonContainmentReferences(p);
	}
	
	@Test
	public void testNonContainmentCheckEClassWithError() {
		ResourceSet set = new ResourceSetImpl();
		set.getResourceFactoryRegistry().getProtocolToFactoryMap().put("file", new XMIResourceFactoryImpl());
		
		PersonObject p = BasicFactory.eINSTANCE.createPersonObject();
		p.setType(BasicPackage.Literals.BUSINESS_PERSON);		

		Resource r = set.createResource(URI.createURI("file://test"));
		r.getContents().add(p);

		assertThrows(ConstraintValidationException.class, ()-> RepositoryHelper.checkForAttachedNonContainmentReferences(p));
	}

}
