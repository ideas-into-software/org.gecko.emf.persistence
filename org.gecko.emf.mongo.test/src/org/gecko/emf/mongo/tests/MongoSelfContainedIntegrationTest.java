/**
 * Copyright (c) 2012 - 2017 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.mongo.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Collections;

import org.bson.Document;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource.URIHandler;
import org.gecko.collection.EReferenceCollection;
import org.gecko.emf.mongo.handlers.MongoResourceSetConfigurator;
import org.gecko.emf.osgi.ResourceSetFactory;
import org.gecko.emf.osgi.annotation.require.RequireEMF;
import org.gecko.emf.osgi.configurator.ResourceSetConfigurator;
import org.gecko.emf.osgi.constants.EMFNamespaces;
import org.gecko.emf.osgi.example.model.basic.BasicFactory;
import org.gecko.emf.osgi.example.model.basic.HLWidget;
import org.gecko.emf.osgi.example.model.basic.Person;
import org.gecko.emf.osgi.example.model.basic.Textwidget;
import org.gecko.emf.osgi.example.model.basic.Widget;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

import com.mongodb.client.MongoCollection;

/**
 * Integration tests for the complete EMF mongo setup
 * @author Mark Hoffmann
 * @since 26.07.2017
 */
@RequireEMF
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
@WithFactoryConfiguration(name = "mongoClient", location = "?", factoryPid = "MongoClientProvider", properties = {
		@Property(key = "client_id", value = "test"), @Property(key = "uri", value = "mongodb://localhost:27017") })
@WithFactoryConfiguration(name = "mongoDatabase", location = "?", factoryPid = "MongoDatabaseProvider", properties = {
		@Property(key = "alias", value = "TestDB"), @Property(key = "database", value = "test") })
public class MongoSelfContainedIntegrationTest extends MongoEMFSetting {
	@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME + "=mongo)")
	ServiceAware<ResourceSetConfigurator> configuratorAware;
	@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME + "=mongo)")
	ServiceAware<ResourceSetFactory> rsAware;

	@BeforeEach
	public void doBefore(@InjectBundleContext BundleContext ctx) {
		super.doBefore(ctx);
	}

	@AfterEach
	public void doAfter() {
		super.doAfter();
	}
	/**
	 * Test creation of object and returning results
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testSaveLoadNestedObjects() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {

		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);
		
		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();
		
		System.out.println("Dropping DB");
		MongoCollection<Document> bpCollection = client.getDatabase("test").getCollection("Widget");
		bpCollection.drop();
		
		assertEquals(0, bpCollection.countDocuments());
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Widget/"));
		
		Widget widget = BasicFactory.eINSTANCE.createWidget();
		widget.setId("test01");
		widget.setName("rootWidget");
		HLWidget hlw = BasicFactory.eINSTANCE.createHLWidget();
		hlw.setName("horizonalLayout");
		widget.setContent(hlw);
		
		
		Widget textWidget01 = BasicFactory.eINSTANCE.createWidget();
		textWidget01.setId("textWidget01");
		textWidget01.setName("widget-textWidget01");
		Widget textWidget02 = BasicFactory.eINSTANCE.createWidget();
		textWidget02.setId("textWidget02");
		textWidget02.setName("widget-textWidget02");
		hlw.getChildren().add(textWidget01);
		hlw.getChildren().add(textWidget02);
		
		Textwidget tw01 = BasicFactory.eINSTANCE.createTextwidget();
		tw01.setName("textWidget01-");
		textWidget01.setContent(tw01);
		
		Textwidget tw02 = BasicFactory.eINSTANCE.createTextwidget();
		tw02.setName("textWidget02");
		textWidget01.setContent(tw02);
		
		resource.getContents().add(widget);
		resource.save(null);
		
		resource.getContents().clear();
		resource.unload();
		/*
		 * Find person in the collection
		 */
		//		long start = System.currentTimeMillis();
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Widget/" + widget.getId()));
		findResource.load(null);
		
		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		
		
		// doing some object checks
		Widget p = (Widget) findResource.getContents().get(0);
		assertNotNull(p.getContent());
		assertTrue(p.getContent() instanceof HLWidget);
		HLWidget hl = (HLWidget) p.getContent();
		assertEquals(2, hl.getChildren().size());
		assertTrue(hl.getChildren().get(0) instanceof Widget);
		assertTrue(hl.getChildren().get(1) instanceof Widget);

		assertEquals(1, bpCollection.countDocuments());
		bpCollection.drop();
	}
	
	/**
	 * Test creation of object and returning results where Objects have a non containment proxies
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testSaveLoadProxyObjectsNonContainmentProxy() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		
		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);
		
		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();
		
		System.out.println("Dropping DB");
		MongoCollection<Document> bpCollection = client.getDatabase("test").getCollection("Person");
		MongoCollection<Document> addressCollection = client.getDatabase("test").getCollection("Address");
		bpCollection.drop();
		addressCollection.drop();
		
		assertEquals(0, bpCollection.countDocuments());
		Resource resourcePerson = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/testPerson"));
		
		Person p = BasicFactory.eINSTANCE.createPerson();
		p.setFirstName("firstName");
		p.setLastName("lastName");
		p.setId("testPerson");

		p.getRelatives().add(p);
		
		
		resourcePerson.getContents().add(p);
		resourcePerson.save(null);
		
		
		
		String typeFilter = "{ \"filter\" : { \"id\":\"testPerson\" }}";
		
		/*
		 * Find person in the collection
		 */
		//		long start = System.currentTimeMillis();
		Resource findResource = resourceSet.createResource(URI.createURI(resourcePerson.getURI().toString()+ "?" + URI.encodeQuery(typeFilter, true)));
		findResource.load(Collections.emptyMap());
		
		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		
		URIHandler handler = new org.eclipse.emf.ecore.xmi.impl.URIHandlerImpl();
		handler.setBaseURI(resourcePerson.getURI());
		EReferenceCollection coll =  (EReferenceCollection) findResource.getContents().get(0);
		
		assertNotNull(coll);
		assertEquals(1, coll.getValues().size());
		Person findPerson = (Person) coll.getValues().get(0);
		assertNotNull(findPerson);
		
		assertEquals(p.getFirstName(), findPerson.getFirstName());
		
		assertEquals(1, findPerson.getRelatives().size());
		
		Person proxy = findPerson.getRelatives().get(0);
		
		assertFalse(proxy.eIsProxy());
		
		bpCollection.drop();
	}

}
