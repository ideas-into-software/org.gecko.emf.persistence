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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.gecko.emf.mongo.Options;
import org.gecko.emf.mongo.handlers.MongoResourceSetConfigurator;
import org.gecko.emf.osgi.ResourceSetFactory;
import org.gecko.emf.osgi.annotation.require.RequireEMF;
import org.gecko.emf.osgi.configurator.ResourceSetConfigurator;
import org.gecko.emf.osgi.constants.EMFNamespaces;
import org.gecko.emf.osgi.example.model.basic.BasicFactory;
import org.gecko.emf.osgi.example.model.basic.BusinessPerson;
import org.gecko.emf.osgi.example.model.basic.EmployeeInfo;
import org.gecko.emf.osgi.example.model.basic.GenderType;
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

import com.mongodb.client.FindIterable;
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
public class MongoExtendedMetadataIntegrationTest extends MongoEMFSetting {
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
	public void testSaveNoExtendedMetadataAttribute() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		
		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		
		System.out.println("Dropping DB");
		MongoCollection<Document> bpCollection = client.getDatabase("test").getCollection("BusinessPerson");
		bpCollection.drop();
		
		assertEquals(0, bpCollection.countDocuments());
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/BusinessPerson/"));
		
		BusinessPerson person = BasicFactory.eINSTANCE.createBusinessPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann" );
		person.setGender(GenderType.MALE);
		assertNull(person.getId());
		person.setCompanyIdCardNumber("test1234");
		resource.getContents().add(person);
		resource.save(null);
		
		resource.getContents().clear();
		resource.unload();
		/*
		 * Find person in the collection
		 */
		//		long start = System.currentTimeMillis();
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/BusinessPerson/" + person.getId()));
		findResource.load(null);
		
		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		
		
		// doing some object checks
		BusinessPerson p = (BusinessPerson) findResource.getContents().get(0);
		assertEquals("Mark", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertNotNull(p.getId());
		assertEquals("test1234", p.getCompanyIdCardNumber());
		
		assertEquals(1, bpCollection.countDocuments());
		FindIterable<Document> docIterable = bpCollection.find();
		Document first = docIterable.first();
		Object cidField = first.get("companyIdCardNumber");
		assertNotNull(cidField);
		assertEquals("test1234", cidField);
		
		bpCollection.drop();
	}
	
	/**
	 * Test creation of object and returning results
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testSaveExtendedMetadataAttribute() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		
		System.out.println("Dropping DB");
		MongoCollection<Document> bpCollection = client.getDatabase("test").getCollection("BusinessPerson");
		bpCollection.drop();
		
		assertEquals(0, bpCollection.countDocuments());
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/BusinessPerson/"));
		
		BusinessPerson person = BasicFactory.eINSTANCE.createBusinessPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann" );
		person.setGender(GenderType.MALE);
		assertNull(person.getId());
		person.setCompanyIdCardNumber("test1234");
		resource.getContents().add(person);
		Map<String, Object> options = new HashMap<>();
		options.put(Options.OPTION_USE_EXTENDED_METADATA, Boolean.TRUE);
		resource.save(options);
		
		resource.getContents().clear();
		resource.unload();
		/*
		 * Find person in the collection
		 */
		//		long start = System.currentTimeMillis();
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/BusinessPerson/" + person.getId()));
		findResource.load(options);
		
		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		
		
		// doing some object checks
		BusinessPerson p = (BusinessPerson) findResource.getContents().get(0);
		assertEquals("Mark", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertNotNull(p.getId());
		assertEquals("test1234", p.getCompanyIdCardNumber());
		
		assertEquals(1, bpCollection.countDocuments());
		FindIterable<Document> docIterable = bpCollection.find();
		Document first = docIterable.first();
		Object cidField = first.get("compId");
		assertNotNull(cidField);
		assertEquals("test1234", cidField);
		
		bpCollection.drop();
	}
	
	/**
	 * Test creation of object and returning results
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSaveNoExtendedMetadataReferences() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		System.out.println("Dropping DB");
		MongoCollection<Document> bpCollection = client.getDatabase("test").getCollection("BusinessPerson");
		bpCollection.drop();
		
		assertEquals(0, bpCollection.countDocuments());
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/BusinessPerson/"));
		
		BusinessPerson person = BasicFactory.eINSTANCE.createBusinessPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann" );
		person.setGender(GenderType.MALE);
		assertNull(person.getId());
		person.setCompanyIdCardNumber("test1234");
		EmployeeInfo einfo01 = BasicFactory.eINSTANCE.createEmployeeInfo();
		einfo01.setPosition("CTO");
		EmployeeInfo einfo02 = BasicFactory.eINSTANCE.createEmployeeInfo();
		einfo02.setPosition("CEO");
		person.getEmployeeInfo().add(einfo01);
		person.getEmployeeInfo().add(einfo02);
		
		resource.getContents().add(person);
		resource.save(null);
		
		resource.getContents().clear();
		resource.unload();
		/*
		 * Find person in the collection
		 */
		//		long start = System.currentTimeMillis();
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/BusinessPerson/" + person.getId()));
		findResource.load(null);
		
		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		
		
		// doing some object checks
		BusinessPerson p = (BusinessPerson) findResource.getContents().get(0);
		assertEquals("Mark", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertNotNull(p.getId());
		assertEquals("test1234", p.getCompanyIdCardNumber());
		assertEquals(2, p.getEmployeeInfo().size());
		assertEquals("CTO", p.getEmployeeInfo().get(0).getPosition());
		assertEquals("CEO", p.getEmployeeInfo().get(1).getPosition());
		
		assertEquals(1, bpCollection.countDocuments());
		FindIterable<Document> docIterable = bpCollection.find();
		Document first = docIterable.first();
		Object eInfoField = first.get("employeeInfo");
		assertNotNull(eInfoField);
		assertTrue(eInfoField instanceof List);
		List<Document> docs = (List<Document>) eInfoField;
		assertEquals("CTO", docs.get(0).get("position"));
		assertEquals("CEO", docs.get(1).get("position"));
		Object eInfo2Field = first.get("employeeInfo2");
		assertNull(eInfo2Field);
		
		bpCollection.drop();
	}

	/**
	 * Test creation of object and returning results
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSaveExtendedMetadataReference() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		
		System.out.println("Dropping DB");
		MongoCollection<Document> bpCollection = client.getDatabase("test").getCollection("BusinessPerson");
		bpCollection.drop();
		
		assertEquals(0, bpCollection.countDocuments());
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/BusinessPerson/"));
		
		BusinessPerson person = BasicFactory.eINSTANCE.createBusinessPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann" );
		person.setGender(GenderType.MALE);
		assertNull(person.getId());
		person.setCompanyIdCardNumber("test1234");
		EmployeeInfo einfo01 = BasicFactory.eINSTANCE.createEmployeeInfo();
		einfo01.setPosition("CTO");
		EmployeeInfo einfo02 = BasicFactory.eINSTANCE.createEmployeeInfo();
		einfo02.setPosition("CEO");
		person.getEmployeeInfo().add(einfo01);
		person.getEmployeeInfo().add(einfo02);
		
		resource.getContents().add(person);
		Map<String, Object> options = new HashMap<>();
		options.put(Options.OPTION_USE_EXTENDED_METADATA, Boolean.TRUE);
		resource.save(options);
		
		resource.getContents().clear();
		resource.unload();
		/*
		 * Find person in the collection
		 */
		//		long start = System.currentTimeMillis();
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/BusinessPerson/" + person.getId()));
		findResource.load(options);
		
		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		
		
		// doing some object checks
		BusinessPerson p = (BusinessPerson) findResource.getContents().get(0);
		assertEquals("Mark", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertNotNull(p.getId());
		assertEquals("test1234", p.getCompanyIdCardNumber());
		assertEquals(2, p.getEmployeeInfo().size());
		assertEquals("CTO", p.getEmployeeInfo().get(0).getPosition());
		assertEquals("CEO", p.getEmployeeInfo().get(1).getPosition());
		
		assertEquals(1, bpCollection.countDocuments());
		FindIterable<Document> docIterable = bpCollection.find();
		Document first = docIterable.first();
		Object eInfoField = first.get("eInfo");
		assertNotNull(eInfoField);
		assertTrue(eInfoField instanceof List);
		List<Document> docs = (List<Document>) eInfoField;
		assertEquals("CTO", docs.get(0).get("position"));
		assertEquals("CEO", docs.get(1).get("position"));
		Object eInfo2Field = first.get("employeeInfo");
		assertNull(eInfo2Field);
		
		bpCollection.drop();
	}

}
