///**
// * Copyright (c) 2012 - 2017 Data In Motion and others.
// * All rights reserved. 
// * 
// * This program and the accompanying materials are made available under the terms of the 
// * Eclipse Public License v1.0 which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// * 
// * Contributors:
// *     Data In Motion - initial API and implementation
// */
//package org.gecko.emf.mongo.tests;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.Map;
//
//import org.eclipse.emf.ecore.EReference;
//import org.eclipse.emf.ecore.resource.ResourceSet;
//import org.gecko.emf.mongo.handlers.MongoResourceSetConfigurator;
//import org.gecko.emf.mongo.handlers.MongoResourceSetConfiguratorComponent;
//import org.gecko.emf.osgi.EMFNamespaces;
//import org.gecko.emf.osgi.ResourceSetConfigurator;
//import org.gecko.emf.osgi.ResourceSetFactory;
//import org.gecko.emf.osgi.model.test.Person;
//import org.gecko.emf.osgi.model.test.BasicPackage;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestFactory;
//import org.junit.runner.RunWith;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.osgi.framework.BundleException;
//import org.osgi.framework.InvalidSyntaxException;
//
//import com.mongodb.client.MongoCollection;
//
//import jdk.internal.loader.Resource;
//
///**
// * Integration tests for the complete EMF mongo setup
// * @author Mark Hoffmann
// * @since 26.07.2017
// */
//@RunWith(MockitoJUnitRunner.class)
//public class MongoProxyQueryAppenderTest extends MongoEMFSetting {
//
//	/**
//	 * Test creation of object and returning results
//	 * @throws IOException 
//	 * @throws BundleException 
//	 * @throws InvalidSyntaxException 
//	 * @throws InterruptedException 
//	 */
//	@Test
//	public void testSaveLoadProxyObjects() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//		
//		defaultSetup();
//		
//		String dbAlias = "testDB";
//		String filter = "(&(" + EMFNamespaces.EMF_CONFIGURATOR_NAME + "=mongo)(objectClass=org.gecko.emf.osgi.ResourceSetConfigurator)(" + MongoResourceSetConfiguratorComponent.PROP_MONGO_ALIAS + "=" + dbAlias + "))";
//		ResourceSetConfigurator rsc = (ResourceSetConfigurator) createTrackedChecker(filter, true).trackedServiceNotNull().getTrackedService();
//		assertTrue(rsc instanceof MongoResourceSetConfigurator);
//		
//		filter = "(&(" + EMFNamespaces.EMF_CONFIGURATOR_NAME + "=mongo)(" + EMFNamespaces.EMF_CONFIGURATOR_NAME + "=" + dbAlias + ")(objectClass=org.gecko.emf.osgi.ResourceSetFactory))";
//		ResourceSetFactory rsf = (ResourceSetFactory) createTrackedChecker(filter, true).assertCreations(1, true).trackedServiceNotNull().getTrackedService();
//		ResourceSet resourceSet = rsf.createResourceSet();
//		
//		System.out.println("Dropping DB");
//		MongoCollection<Document> bpCollection = client.getDatabase("test").getCollection("Person");
//		MongoCollection<Document> addressCollection = client.getDatabase("test").getCollection("Address");
//		bpCollection.drop();
//		addressCollection.drop();
//		
//		assertEquals(0, bpCollection.countDocuments());
//		Resource resourceAddress = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Address/testAddress"));
//		Resource resourcePerson = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/testPerson"));
//		
//		Address address = TestFactory.eINSTANCE.createAddress();
//		address.setId("testAddress");
//		address.setCity("city");
//		address.setStreet("street");
//		address.setZip("0123");
//		
//		Person p = TestFactory.eINSTANCE.createPerson();
//		p.setFirstName("firstName");
//		p.setLastName("lastName");
//		p.setId("testPerson");
//		p.setAddress(address);
//		
//		resourceAddress.getContents().add(address);
//		resourceAddress.save(null);
//		
//		resourcePerson.getContents().add(p);
//		resourcePerson.save(null);
//		
//		
//		
//		/*
//		 * Find person in the collection
//		 */
//		//		long start = System.currentTimeMillis();
//		Resource findResource = resourceSet.createResource(URI.createURI(resourcePerson.getURI().toString()));
//		
//		Map<EReference, String> proxyQuery = Collections.singletonMap(BasicPackage.Literals.PERSON__ADDRESS, "{\"projection\":{\"city\" : 1, \"street\" : 1}}"); 
//		
//		findResource.load(Collections.singletonMap(Options.OPTION_QUERY_FOR_PROXIES, proxyQuery));
//		
//		// get the person
//		assertNotNull(findResource);
//		assertFalse(findResource.getContents().isEmpty());
//		assertEquals(1, findResource.getContents().size());
//		
//		
//		
//		Person findPerson = (Person) findResource.getContents().get(0);
//		
//		assertEquals(p.getFirstName(), findPerson.getFirstName());
//		
//		assertNotNull(findPerson.getAddress());
//		
//		Address findAddress = findPerson.getAddress();
//		assertFalse(findAddress.eIsProxy());
//		assertEquals(address.getCity(), findAddress.getCity());
//		assertEquals(address.getStreet(), findAddress.getStreet());
//		assertNull(findAddress.getZip());
//		
//		bpCollection.drop();
//		addressCollection.drop();
//	}
//
//}
