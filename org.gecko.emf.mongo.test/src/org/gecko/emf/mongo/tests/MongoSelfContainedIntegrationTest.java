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
//import static org.junit.Assert.assertTrue;
//
//import java.io.IOException;
//import java.util.Collections;
//
//import org.eclipse.emf.ecore.resource.ResourceSet;
//import org.eclipse.emf.ecore.xmi.XMLResource.URIHandler;
//import org.gecko.collection.EReferenceCollection;
//import org.gecko.emf.mongo.handlers.MongoResourceSetConfigurator;
//import org.gecko.emf.mongo.handlers.MongoResourceSetConfiguratorComponent;
//import org.gecko.emf.osgi.EMFNamespaces;
//import org.gecko.emf.osgi.ResourceSetConfigurator;
//import org.gecko.emf.osgi.ResourceSetFactory;
//import org.gecko.emf.osgi.model.test.HLWidget;
//import org.gecko.emf.osgi.model.test.Person;
//import org.gecko.emf.osgi.model.test.Textwidget;
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
//import jdk.internal.org.jline.reader.Widget;
//
///**
// * Integration tests for the complete EMF mongo setup
// * @author Mark Hoffmann
// * @since 26.07.2017
// */
//@RunWith(MockitoJUnitRunner.class)
//public class MongoSelfContainedIntegrationTest extends MongoEMFSetting {
//
//	/**
//	 * Test creation of object and returning results
//	 * @throws IOException 
//	 * @throws BundleException 
//	 * @throws InvalidSyntaxException 
//	 * @throws InterruptedException 
//	 */
//	@Test
//	public void testSaveLoadNestedObjects() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//
//		defaultSetup();
//		
//		String dbAlias = "testDB";
//		String filter = "(&(" + EMFNamespaces.EMF_CONFIGURATOR_NAME + "=mongo)(objectClass=org.gecko.emf.osgi.ResourceSetConfigurator)(" + MongoResourceSetConfiguratorComponent.PROP_MONGO_ALIAS + "=" + dbAlias + "))";
//		ResourceSetConfigurator rsc = (ResourceSetConfigurator) createTrackedChecker(filter, true).trackedServiceNotNull().getTrackedService();
//		assertTrue(rsc instanceof MongoResourceSetConfigurator);
//		
//		filter = "(&(" + EMFNamespaces.EMF_CONFIGURATOR_NAME + "=mongo)(objectClass=org.gecko.emf.osgi.ResourceSetFactory))";
//		ResourceSetFactory rsf = (ResourceSetFactory) createTrackedChecker(filter, true).trackedServiceNotNull().getTrackedService();
//		ResourceSet resourceSet = rsf.createResourceSet();
//		
//		System.out.println("Dropping DB");
//		MongoCollection<Document> bpCollection = client.getDatabase("test").getCollection("Widget");
//		bpCollection.drop();
//		
//		assertEquals(0, bpCollection.countDocuments());
//		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Widget/"));
//		
//		Widget widget = TestFactory.eINSTANCE.createWidget();
//		widget.setId("test01");
//		widget.setName("rootWidget");
//		HLWidget hlw = TestFactory.eINSTANCE.createHLWidget();
//		hlw.setName("horizonalLayout");
//		widget.setContent(hlw);
//		
//		
//		Widget textWidget01 = TestFactory.eINSTANCE.createWidget();
//		textWidget01.setId("textWidget01");
//		textWidget01.setName("widget-textWidget01");
//		Widget textWidget02 = TestFactory.eINSTANCE.createWidget();
//		textWidget02.setId("textWidget02");
//		textWidget02.setName("widget-textWidget02");
//		hlw.getChildren().add(textWidget01);
//		hlw.getChildren().add(textWidget02);
//		
//		Textwidget tw01 = TestFactory.eINSTANCE.createTextwidget();
//		tw01.setName("textWidget01-");
//		textWidget01.setContent(tw01);
//		
//		Textwidget tw02 = TestFactory.eINSTANCE.createTextwidget();
//		tw02.setName("textWidget02");
//		textWidget01.setContent(tw02);
//		
//		resource.getContents().add(widget);
//		resource.save(null);
//		
//		resource.getContents().clear();
//		resource.unload();
//		/*
//		 * Find person in the collection
//		 */
//		//		long start = System.currentTimeMillis();
//		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Widget/" + widget.getId()));
//		findResource.load(null);
//		
//		// get the person
//		assertNotNull(findResource);
//		assertFalse(findResource.getContents().isEmpty());
//		assertEquals(1, findResource.getContents().size());
//		
//		
//		// doing some object checks
//		Widget p = (Widget) findResource.getContents().get(0);
//		assertNotNull(p.getContent());
//		assertTrue(p.getContent() instanceof HLWidget);
//		HLWidget hl = (HLWidget) p.getContent();
//		assertEquals(2, hl.getChildren().size());
//		assertTrue(hl.getChildren().get(0) instanceof Widget);
//		assertTrue(hl.getChildren().get(1) instanceof Widget);
//
//		assertEquals(1, bpCollection.countDocuments());
//		bpCollection.drop();
//	}
//	
//	/**
//	 * Test creation of object and returning results where Objects have a non containment proxies
//	 * @throws IOException 
//	 * @throws BundleException 
//	 * @throws InvalidSyntaxException 
//	 * @throws InterruptedException 
//	 */
//	@Test
//	public void testSaveLoadProxyObjectsNonContainmentProxy() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//		
//		defaultSetup();
//		
//		String dbAlias = "testDB";
//		String filter = "(&(" + EMFNamespaces.EMF_CONFIGURATOR_NAME + "=mongo)(objectClass=org.gecko.emf.osgi.ResourceSetConfigurator)(" + MongoResourceSetConfiguratorComponent.PROP_MONGO_ALIAS + "=" + dbAlias + "))";
//		ResourceSetConfigurator rsc = (ResourceSetConfigurator) createTrackedChecker(filter, true).trackedServiceNotNull().getTrackedService();
//		assertTrue(rsc instanceof MongoResourceSetConfigurator);
//		
//		filter = "(&(" + EMFNamespaces.EMF_CONFIGURATOR_NAME + "=mongo)(objectClass=org.gecko.emf.osgi.ResourceSetFactory))";
//		ResourceSetFactory rsf = (ResourceSetFactory) createTrackedChecker(filter, true).trackedServiceNotNull().getTrackedService();
//		ResourceSet resourceSet = rsf.createResourceSet();
//		
//		System.out.println("Dropping DB");
//		MongoCollection<Document> bpCollection = client.getDatabase("test").getCollection("Person");
//		MongoCollection<Document> addressCollection = client.getDatabase("test").getCollection("Address");
//		bpCollection.drop();
//		addressCollection.drop();
//		
//		assertEquals(0, bpCollection.countDocuments());
//		Resource resourcePerson = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/testPerson"));
//		
//		Person p = TestFactory.eINSTANCE.createPerson();
//		p.setFirstName("firstName");
//		p.setLastName("lastName");
//		p.setId("testPerson");
//
//		p.getRelatives().add(p);
//		
//		
//		resourcePerson.getContents().add(p);
//		resourcePerson.save(null);
//		
//		
//		
//		String typeFilter = "{ \"filter\" : { \"id\":\"testPerson\" }}";
//		
//		/*
//		 * Find person in the collection
//		 */
//		//		long start = System.currentTimeMillis();
//		Resource findResource = resourceSet.createResource(URI.createURI(resourcePerson.getURI().toString()+ "?" + URI.encodeQuery(typeFilter, true)));
//		findResource.load(Collections.emptyMap());
//		
//		// get the person
//		assertNotNull(findResource);
//		assertFalse(findResource.getContents().isEmpty());
//		assertEquals(1, findResource.getContents().size());
//		
//		URIHandler handler = new org.eclipse.emf.ecore.xmi.impl.URIHandlerImpl();
//		handler.setBaseURI(resourcePerson.getURI());
//		EReferenceCollection coll =  (EReferenceCollection) findResource.getContents().get(0);
//		
//		assertNotNull(coll);
//		assertEquals(1, coll.getValues().size());
//		Person findPerson = (Person) coll.getValues().get(0);
//		assertNotNull(findPerson);
//		
//		assertEquals(p.getFirstName(), findPerson.getFirstName());
//		
//		assertEquals(1, findPerson.getRelatives().size());
//		
//		Person proxy = findPerson.getRelatives().get(0);
//		
//		assertFalse(proxy.eIsProxy());
//		
//		bpCollection.drop();
//	}
//
//}
