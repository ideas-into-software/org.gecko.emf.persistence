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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.collection.CollectionPackage;
import org.gecko.collection.ECollection;
import org.gecko.collection.EReferenceCollection;
import org.gecko.emf.mongo.Options;
import org.gecko.emf.osgi.annotation.require.RequireEMF;
import org.gecko.emf.osgi.constants.EMFNamespaces;
import org.gecko.emf.osgi.example.model.basic.Address;
import org.gecko.emf.osgi.example.model.basic.BasicFactory;
import org.gecko.emf.osgi.example.model.basic.BasicPackage;
import org.gecko.emf.osgi.example.model.basic.Contact;
import org.gecko.emf.osgi.example.model.basic.ContactContextType;
import org.gecko.emf.osgi.example.model.basic.ContactType;
import org.gecko.emf.osgi.example.model.basic.GenderType;
import org.gecko.emf.osgi.example.model.basic.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceObjects;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Indexes;

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
public class MongoUseIdAsPKIntegrationTest extends MongoEMFSetting {

	@InjectService(cardinality = 0, filter = "(&(" + EMFNamespaces.EMF_CONFIGURATOR_NAME + "=mongo)("+EMFNamespaces.EMF_MODEL_NAME+"=collection))")
	ServiceAware<ResourceSet> rsAware;

	@BeforeEach
	public void doBefore(@InjectBundleContext BundleContext ctx) {
		super.doBefore(ctx);
	}

	@AfterEach
	public void doAfter() {
		super.doAfter();
	}
	/**
	 * Test creation of object and returning results as well as updating
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testCreateAndUpdateContainmentSingle() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		ResourceSet resourceSet = rsAware.getService();
		
		System.out.println("Dropping DB");
		MongoCollection<Document> personCollection = client.getDatabase("test").getCollection("Person");
		personCollection.drop();
		
		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		Contact c2 = BasicFactory.eINSTANCE.createContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@tests.de");
		
		assertEquals(0, resourceSet.getResources().size());
		assertEquals(0, personCollection.countDocuments());
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
		testResourceSet(resourceSet, resource, 1, 0);
		
		Map<String, Object> saveOptions = new HashMap<String, Object>();
		saveOptions.put(Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY, Boolean.FALSE);
		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann" );
		person.setId("maho");
		person.setGender(GenderType.MALE);
		person.getContact().add(EcoreUtil.copy(c1));
		person.getContact().add(EcoreUtil.copy(c2));
		resource.getContents().add(person);
		
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		resource.getContents().clear();
		testResourceSet(resourceSet, resource, 1, 0);
		resource.unload();
		/*
		 * Find person in the collection
		 */
		//		long start = System.currentTimeMillis();
		testResourceSet(resourceSet, resource, 1, 0);
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{\"id\":\"maho\"}"));
		testResourceSet(resourceSet, findResource, 2, 0);
		findResource.load(null);
		testResourceSet(resourceSet, findResource, 3, 1);
		
		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		
		
		// doing some object checks
		EReferenceCollection erc = (EReferenceCollection) findResource.getContents().get(0);
		assertEquals(1, erc.getValues().size());
		Person p = (Person) erc.getValues().get(0);
		testResourceSet(resourceSet, p.eResource(), 3, 1);
		
		assertEquals("Mark", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals("maho", p.getId());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());
		testResourceSet(resourceSet, findResource, 3, 1);
		findResource.unload();
		testResourceSet(resourceSet, findResource, 3, 0);
		testResourceSet(resourceSet, p.eResource(), 3, 1);
		
		person.setFirstName("Mark2");
		
		resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
		testResourceSet(resourceSet, resource, 4, 0);
		resource.getContents().add(person);
		testResourceSet(resourceSet, resource, 4, 1);
		resource.save(saveOptions);
		testResourceSet(resourceSet, resource, 4, 1);
//		
		resource.getContents().clear();
		testResourceSet(resourceSet, resource, 4, 0);
		resource.unload();
		testResourceSet(resourceSet, resource, 4, 0);
//		/*
//		 * Find person in the collection
//		 */
//		//		start = System.currentTimeMillis();
		testResourceSet(resourceSet, resource, 4, 0);
		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{\"id\":\"maho\"}"));
		testResourceSet(resourceSet, findResource, 5, 0);
		findResource.load(null);
		testResourceSet(resourceSet, findResource, 6, 1);
		
		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		
		erc = (EReferenceCollection) findResource.getContents().get(0);
		assertEquals(1, erc.getValues().size());
		p = (Person) erc.getValues().get(0);
		testResourceSet(resourceSet, p.eResource(), 6, 1);
		
		// doing some object checks
		assertEquals("Mark2", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());
		
		p.setFirstName("Mark3");
		testResourceSet(resourceSet, findResource, 6, 1);
		p.eResource().save(saveOptions);
		testResourceSet(resourceSet, findResource, 6, 1);
		
		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{\"id\":\"maho\"}"));
		testResourceSet(resourceSet, findResource, 7, 0);
		findResource.load(null);
		testResourceSet(resourceSet, findResource, 8, 1);
		
		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		
		erc = (EReferenceCollection) findResource.getContents().get(0);
		assertEquals(1, erc.getValues().size());
		p = (Person) erc.getValues().get(0);
		testResourceSet(resourceSet, p.eResource(), 8, 1);
		// doing some object checks
		assertEquals("Mark3", p.getFirstName());
		
		personCollection.drop();
		
	}
	
	/**
	 * Test creation of object and returning results as well as updating
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testCreateAndFindWithReferences() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		
		ResourceSet resourceSet = rsAware.getService();
 		
		System.out.println("Dropping DB");
		MongoCollection<Document> personCollection = client.getDatabase("test").getCollection("Person");
		personCollection.drop();
		
		assertEquals(0, personCollection.countDocuments());
		assertEquals(0, resourceSet.getResources().size());
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
		testResourceSet(resourceSet, resource, 1, 0);
		
		Map<String, Object> saveOptions = new HashMap<String, Object>();
		saveOptions.put(Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY, Boolean.FALSE);
		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann" );
		person.setId("maho");
		person.setGender(GenderType.MALE);
		resource.getContents().add(person);
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		assertEquals(3, resource.getURI().segmentCount());
//		assertEquals("maho", resource.getURI().fragment());
		assertNotEquals("maho", resource.getURI().lastSegment());
		String id = resource.getURI().lastSegment();
		resource.getContents().clear();
		resource.unload();
		testResourceSet(resourceSet, resource, 1, 0);
		/*
		 * Find person in the collection
		 */
		//		long start = System.currentTimeMillis();
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/" + id + "#maho"));
		testResourceSet(resourceSet, findResource, 2, 0);
		findResource.load(null);
		testResourceSet(resourceSet, findResource, 2, 1);
		
		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		Person p = (Person) findResource.getContents().get(0);
		assertEquals("Mark", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals("maho", p.getId());
		assertEquals(GenderType.MALE, p.getGender());
		
		assertEquals(3, findResource.getURI().segmentCount());
		assertEquals("maho", findResource.getURI().fragment());
		assertEquals(id, findResource.getURI().lastSegment());
		
		Address a = BasicFactory.eINSTANCE.createAddress();
		a.setId("gera-kks93");
		a.setStreet("K-K-S");
		a.setCity("Gera");
		
		MongoCollection<Document> addressCollection = client.getDatabase("test").getCollection("Address");
		addressCollection.drop();
		
		assertEquals(0, addressCollection.countDocuments());
		Resource adrResource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Address/"));
		testResourceSet(resourceSet, adrResource, 3, 0);
		adrResource.getContents().add(a);
		testResourceSet(resourceSet, adrResource, 3, 1);
		adrResource.save(saveOptions);
		testResourceSet(resourceSet, adrResource, 3, 1);
		assertEquals(1, addressCollection.countDocuments());
		assertEquals(3, adrResource.getURI().segmentCount());
//		assertEquals("maho", resource.getURI().fragment());
		assertNotEquals("gera-kks93", adrResource.getURI().lastSegment());
//		String adrId = resource.getURI().lastSegment();
		
		p.setAddress(a);
		
		testResourceSet(resourceSet, adrResource, 3, 1);
		testResourceSet(resourceSet, findResource, 3, 1);
		findResource.save(saveOptions);
		testResourceSet(resourceSet, adrResource, 3, 1);
		testResourceSet(resourceSet, findResource, 3, 1);
		findResource.getContents().clear();
		findResource.unload();
		testResourceSet(resourceSet, findResource, 3, 0);
		
		findResource.load(saveOptions);
		testResourceSet(resourceSet, findResource, 3, 1);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		p = (Person) findResource.getContents().get(0);
		assertEquals("Mark", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals("maho", p.getId());
		assertEquals(GenderType.MALE, p.getGender());
		assertNotNull(p.getAddress());
		assertFalse(p.getAddress().eIsProxy());
		
		personCollection.drop();
		
	}
	
	/**
	 * Test creation of object and returning results as well as updating
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testCreateAndFindSimple() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		
		ResourceSet resourceSet = rsAware.getService();

		
		System.out.println("Dropping DB");
		MongoCollection<Document> personCollection = client.getDatabase("test").getCollection("Person");
		personCollection.drop();
		
		assertEquals(0, resourceSet.getResources().size());
		assertEquals(0, personCollection.countDocuments());
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
		assertEquals(1, resourceSet.getResources().size());
		Map<String, Object> saveLoadOptions = new HashMap<String, Object>();
		saveLoadOptions.put(Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY, Boolean.FALSE);
		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann" );
		person.setId("maho");
		person.setGender(GenderType.MALE);
		
		testResourceSet(resourceSet, resource, 1, 0);
		resource.getContents().add(person);
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveLoadOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		assertEquals(1, personCollection.countDocuments());
		assertEquals(3, resource.getURI().segmentCount());
		assertNotEquals("maho", resource.getURI().lastSegment());
		String id = resource.getURI().lastSegment();
		resource.getContents().clear();
		resource.unload();
		testResourceSet(resourceSet, resource, 1, 0);
		/*
		 * Find person in the collection with no options
		 */
		testResourceSet(resourceSet, resource, 1, 0);
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/" + id + "#maho"));
		testResourceSet(resourceSet, findResource, 2, 0);
		findResource.load(null);
		testResourceSet(resourceSet, findResource, 2, 1);
		
		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		Person p = (Person) findResource.getContents().get(0);
		assertEquals("Mark", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals("maho", p.getId());
		assertEquals(GenderType.MALE, p.getGender());
		
		assertEquals(3, findResource.getURI().segmentCount());
		assertEquals("maho", findResource.getURI().fragment());
		assertEquals(id, findResource.getURI().lastSegment());
		
		findResource.unload();
		testResourceSet(resourceSet, findResource, 2, 0);
		
		// Load again but with load options
		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/" + id + "#maho"));
		testResourceSet(resourceSet, findResource, 3, 0);
		findResource.load(saveLoadOptions);
		testResourceSet(resourceSet, findResource, 3, 1);
		
		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		p = (Person) findResource.getContents().get(0);
		assertEquals("Mark", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals("maho", p.getId());
		assertEquals(GenderType.MALE, p.getGender());
		
		assertEquals(3, findResource.getURI().segmentCount());
		assertEquals("maho", findResource.getURI().fragment());
		assertEquals(id, findResource.getURI().lastSegment());
		
		
		testResourceSet(resourceSet, findResource, 3, 1);
		findResource.unload();
		testResourceSet(resourceSet, findResource, 3, 0);
		
		// Fins again with no load options
		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/#maho"));
		testResourceSet(resourceSet, findResource, 4, 0);
		findResource.load(null);
		testResourceSet(resourceSet, findResource, 4, 0);
		
		// get the person
		assertNotNull(findResource);
		assertTrue(findResource.getContents().isEmpty());
		
		findResource.unload();
		testResourceSet(resourceSet, findResource, 4, 0);
		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/#maho"));
		testResourceSet(resourceSet, findResource, 5, 0);
		findResource.load(saveLoadOptions);
		testResourceSet(resourceSet, findResource, 5, 0);
		assertNotNull(findResource);
		assertTrue(findResource.getContents().isEmpty());
		
		Person findPerson = EcoreUtil.copy(p);
		findPerson.setFirstName("Santa");
		testResourceSet(resourceSet, findResource, 5, 0);
		findResource.getContents().add(findPerson);
		testResourceSet(resourceSet, findResource, 5, 1);
		
		assertEquals(3, findResource.getURI().segmentCount());
		assertEquals("maho", findResource.getURI().fragment());
		assertTrue(findResource.getURI().lastSegment().isEmpty());
		assertEquals(1, personCollection.countDocuments());
		
		findResource.save(saveLoadOptions);
		testResourceSet(resourceSet, findResource, 5, 1);
		
		assertEquals(3, findResource.getURI().segmentCount());
		assertEquals("maho", findResource.getURI().fragment());
		assertEquals(id, findResource.getURI().lastSegment());
		assertEquals(1, personCollection.countDocuments());
		
		testResourceSet(resourceSet, findResource, 5, 1);
		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/" + id + "#maho"));
		testResourceSet(resourceSet, findResource, 6, 0);
		findResource.load(saveLoadOptions);
		testResourceSet(resourceSet, findResource, 6, 1);
		
		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		p = (Person) findResource.getContents().get(0);
		assertEquals("Santa", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals("maho", p.getId());
		assertEquals(GenderType.MALE, p.getGender());
		
		assertEquals(3, findResource.getURI().segmentCount());
		assertEquals("maho", findResource.getURI().fragment());
		assertEquals(id, findResource.getURI().lastSegment());
		
		// get the person again these use cases are not covered by the implementation
		
		personCollection.drop();
	}
	
	/**
	 * Test creation of object and returning results as well as updating
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testCreateAndUpdateSimple() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		
		ResourceSet resourceSet = rsAware.getService();
		
		System.out.println("Dropping DB");
		MongoCollection<Document> personCollection = client.getDatabase("test").getCollection("Person");
		personCollection.drop();
		
		assertEquals(0, resourceSet.getResources().size());
		assertEquals(0, personCollection.countDocuments());
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
		assertEquals(1, resourceSet.getResources().size());
		Map<String, Object> saveLoadOptions = new HashMap<String, Object>();
		saveLoadOptions.put(Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY, Boolean.FALSE);
		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann" );
		person.setId("maho");
		person.setGender(GenderType.MALE);
		
		testResourceSet(resourceSet, resource, 1, 0);
		resource.getContents().add(person);
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveLoadOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		assertEquals(1, personCollection.countDocuments());
		assertEquals(3, resource.getURI().segmentCount());
		assertNotEquals("maho", resource.getURI().lastSegment());
		String id = resource.getURI().lastSegment();
		resource.getContents().clear();
		resource.unload();
		testResourceSet(resourceSet, resource, 1, 0);
		/*
		 * Find person in the collection with no options
		 */
		testResourceSet(resourceSet, resource, 1, 0);
		Resource updateResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/" + id + "#maho"));
		testResourceSet(resourceSet, updateResource, 2, 0);
		Person p = EcoreUtil.copy(person);
		p.setFirstName("Juergen");
		
		updateResource.getContents().add(p);
		testResourceSet(resourceSet, updateResource, 2, 1);
		updateResource.save(saveLoadOptions);
		testResourceSet(resourceSet, updateResource, 2, 1);
		
		// Load again but with load options
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/" + id + "#maho"));
		testResourceSet(resourceSet, findResource, 3, 0);
		findResource.load(saveLoadOptions);
		testResourceSet(resourceSet, findResource, 3, 1);
		
		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		p = (Person) findResource.getContents().get(0);
		assertEquals("Juergen", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals("maho", p.getId());
		assertEquals(GenderType.MALE, p.getGender());
		
		assertEquals(3, updateResource.getURI().segmentCount());
		assertEquals("maho", updateResource.getURI().fragment());
		assertEquals(id, updateResource.getURI().lastSegment());
		
		// get the person again these use cases are not covered by the implementation
		
		personCollection.drop();
	}

	/**
	 * Test creation of objects and returning results. This creates an index to speed every thing up.
	 * THIS TEST CREATES AN INDEX IN THE MONGO DB
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testCreateAndFindAndUpdateAndFindObjects(@InjectBundleContext BundleContext bct) throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		rsAware.waitForService(2000l);
		ServiceObjects<ResourceSet> serviceObjects = bct.getServiceObjects(rsAware.getServiceReference());
		ResourceSet resourceSet = serviceObjects.getService();;

		Map<String, Object> options = new HashMap<String, Object>();
		options.put(Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY, Boolean.FALSE);
		
		System.out.println("Dropping DB");
		MongoCollection<Document> personCollection = client.getDatabase("test").getCollection("Person");
		personCollection.drop();
		
		EAttribute idAttr = BasicPackage.Literals.PERSON.getEIDAttribute();
		assertNotNull(idAttr);
		personCollection.createIndex(Indexes.ascending(idAttr.getName()));
		
		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		Contact c2 = BasicFactory.eINSTANCE.createContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@tests.de");
		
		assertEquals(0, personCollection.countDocuments());
		/*
		 * Inserting many persons and with containment contacts
		 */
		int insertSize = 10000;
		int insertBatchSize = 500;
		
		long start = System.currentTimeMillis();
		List<Person> personsList = new ArrayList<>(insertBatchSize);
		
		System.out.println("Batch inserting: ");
		assertEquals(0, resourceSet.getResources().size());
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
		testResourceSet(resourceSet, resource, 1, 0);
		for (int i = 0; i < insertSize; i++) {
			Person person = BasicFactory.eINSTANCE.createPerson();
			person.setFirstName("Mark" + i);
			person.setLastName("Hoffmann" + i);
			person.setId("test" + i);
			person.setGender(GenderType.MALE);
			person.getContact().add(EcoreUtil.copy(c1));
			person.getContact().add(EcoreUtil.copy(c2));
			personsList.add(person);
			// using insert many
			if (i % (insertBatchSize - 1) == 0 || i == (insertSize - 1)) {
				resource.getContents().addAll(personsList);
				testResourceSet(resourceSet, resource, 1, personsList.size());
				resource.save(options);
				personsList.clear();
				resource.getContents().clear();
			}
		}
		testResourceSet(resourceSet, resource, 1, 0);
		System.out.println("Insert of " + insertSize  + " persons with batchSize=" + insertBatchSize + " took " + (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, personCollection.countDocuments());
		
		/*
		 * Find person in the collection
		 */
		start = System.currentTimeMillis();
		CollectionPackage.eINSTANCE.eClass();
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		testResourceSet(resourceSet, findResource, 2, 0);
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource.load(null);
		testResourceSet(resourceSet, findResource, 2 + insertSize, 1);
		// get the persons
		System.out.println("Finding all persons with a size " + insertSize  + " took " + (System.currentTimeMillis() - start) + " ms");
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		
		ECollection resultCollection = (ECollection) findResource.getContents().get(0);
		
		/*
		 * Iterating over the result and getting the real objects
		 */
		start = System.currentTimeMillis();
		List<Person> resultList = new ArrayList<Person>();
		assertEquals(0, resultList.size());
		// iterate over all elements
		for (EObject object : resultCollection.getValues()) {
			Person person = (Person) object;
			resultList.add(person);
		}
		System.out.println("Iterating over all persons and mapping with a batch size " + insertBatchSize  + " took " + (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, resultList.size());
		
		// doing some object checks
		Person p = resultList.get(500);
		assertEquals("Mark500", p.getFirstName());
		assertEquals("Hoffmann500", p.getLastName());
		assertEquals("test500", p.getId());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());
		
		p = resultList.get(2500);
		assertEquals("Mark2500", p.getFirstName());
		assertEquals("Hoffmann2500", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());
		
		p = resultList.get(8999);
		assertEquals("Mark8999", p.getFirstName());
		assertEquals("Hoffmann8999", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());
		
		resourceSet = serviceObjects.getService();
		
		start = System.currentTimeMillis();
		
		System.out.println("Batch inserting: ");
		resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
		testResourceSet(resourceSet, resource, 1, 0);
		int i = 0;
		personsList.clear();
		long unload = 0;
		for (Person person : resultList) {
			long unloadingOne = System.currentTimeMillis();
			Resource eResource = person.eResource();
			eResource.getContents().clear();
			eResource.unload();
			unload += System.currentTimeMillis() - unloadingOne;
			person.setFirstName(person.getFirstName() + "2");
			personsList.add(person);
			// using insert many
			if (i % (insertBatchSize - 1) == 0 || i == (insertSize - 1)) {
				resource.getContents().addAll(personsList);
				testResourceSet(resourceSet, resource, 1, personsList.size());
				resource.save(options);
				personsList.clear();
				resource.getContents().clear();
			}
			i++;
		}
		
		System.out.println("Unloading of " + i  + " EObjects took in average " + (unload / i) + "ms per object");
		
		testResourceSet(resourceSet, resource, 1, 0);
		System.out.println("Insert of " + insertSize  + " persons with batchSize=" + insertBatchSize + " took " + (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, personCollection.countDocuments());
		
		/*
		 * Find person in the collection
		 */
		start = System.currentTimeMillis();
		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		testResourceSet(resourceSet, findResource, 2, 0);
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource.load(null);
		// get the persons
		System.out.println("Finding all persons with a size " + insertSize  + " took " + (System.currentTimeMillis() - start) + " ms");
		assertNotNull(findResource);
		testResourceSet(resourceSet, findResource, 2 + insertSize, 1);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		
		resultCollection = (ECollection) findResource.getContents().get(0);
		
		/*
		 * Iterating over the result and getting the real objects
		 */
		start = System.currentTimeMillis();
		resultList = new ArrayList<Person>();
		assertEquals(0, resultList.size());
		// iterate over all elements
		for (EObject object : resultCollection.getValues()) {
			Person person = (Person) object;
			resultList.add(person);
		}
		System.out.println("Iterating over all persons and mapping with a batch size " + insertBatchSize  + " took " + (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, resultList.size());
		
		// doing some object checks
		p = resultList.get(500);
		assertEquals("Mark5002", p.getFirstName());
		assertEquals("Hoffmann500", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());
		
		p = resultList.get(2500);
		assertEquals("Mark25002", p.getFirstName());
		assertEquals("Hoffmann2500", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());
		
		p = resultList.get(8999);
		assertEquals("Mark89992", p.getFirstName());
		assertEquals("Hoffmann8999", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());
		
		personCollection.drop();
	}
	
	private void testResourceSet(ResourceSet rs, Resource r, int expectedResources, int expectedResourceContent) {
		assertNotNull(rs);
		assertNotNull(r);
		assertEquals(expectedResources, rs.getResources().size());
		assertTrue(rs.getResources().contains(r));
		assertEquals(expectedResourceContent, r.getContents().size());
	}
	
}
