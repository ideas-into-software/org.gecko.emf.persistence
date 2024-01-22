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
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.collection.EReferenceCollection;
import org.gecko.emf.mongo.Keywords;
import org.gecko.emf.mongo.Options;
import org.gecko.emf.osgi.annotation.require.RequireEMF;
import org.gecko.emf.osgi.constants.EMFNamespaces;
import org.gecko.emf.osgi.example.model.basic.Address;
import org.gecko.emf.osgi.example.model.basic.BasicFactory;
import org.gecko.emf.osgi.example.model.basic.BasicPackage;
import org.gecko.emf.osgi.example.model.basic.BusinessPerson;
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
public class StoreSuperTypeIntegrationTest extends MongoEMFSetting {

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
	public void testWriteSuperTypes() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
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
		saveOptions.put(Options.OPTION_STORE_SUPERTYPE, Boolean.TRUE);
		BusinessPerson person = BasicFactory.eINSTANCE.createBusinessPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann" );
		person.setId("maho");
		person.setCompanyIdCardNumber("666");
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
		
		assertEquals(1, personCollection.countDocuments());
		Document document = personCollection.find().first();
		assertNotNull(document);
		assertEquals("maho", document.get("_id"));
		assertTrue(document.containsKey(Keywords.SUPER_TYPES_KEY));
		personCollection.drop();
		
		saveOptions.put(Options.OPTION_KEY_SUPERTYPES, "mySupaType");
		resource.getContents().add(person);
		
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		assertEquals(1, personCollection.countDocuments());
		document = personCollection.find().first();
		assertNotNull(document);
		assertEquals("maho", document.get("_id"));
		assertFalse(document.containsKey(Keywords.SUPER_TYPES_KEY));
		assertTrue(document.containsKey("mySupaType"));
		
		personCollection.drop();

		resource.getContents().clear();
		testResourceSet(resourceSet, resource, 1, 0);
		resource.unload();
		
		saveOptions.put(Options.OPTION_STORE_SUPERTYPE, Boolean.FALSE);
		saveOptions.put(Options.OPTION_KEY_SUPERTYPES, "mySupaType");
		resource.getContents().add(person);
		
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		assertEquals(1, personCollection.countDocuments());
		document = personCollection.find().first();
		assertNotNull(document);
		assertEquals("maho", document.get("_id"));
		assertFalse(document.containsKey(Keywords.SUPER_TYPES_KEY));
		assertFalse(document.containsKey("mySupaType"));
		
		personCollection.drop();
		
		resource.getContents().clear();
		testResourceSet(resourceSet, resource, 1, 0);
		resource.unload();
		
		saveOptions = new HashMap<String, Object>();
		saveOptions.put(Options.OPTION_STORE_SUPERTYPE, Boolean.TRUE);

		Person p = BasicFactory.eINSTANCE.createPerson();
		p.setFirstName("Emil");
		p.setLastName("Tester" );
		p.setId("etester");
		person.setGender(GenderType.MALE);
		
		resource.getContents().clear();
		testResourceSet(resourceSet, resource, 1, 0);
		resource.unload();
		resource.getContents().add(p);
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		assertEquals(1, personCollection.countDocuments());
		document = personCollection.find().first();
		assertEquals("etester", document.get("_id"));
		assertFalse(document.containsKey(Keywords.SUPER_TYPES_KEY));
		
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
	public void testWriteCollectionName() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
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
		saveOptions.put(Options.OPTION_COLLECTION_NAME, BasicPackage.Literals.PERSON);
		BusinessPerson person = BasicFactory.eINSTANCE.createBusinessPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann" );
		person.setId("maho");
		person.setCompanyIdCardNumber("666");
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
		
		assertEquals(1, personCollection.countDocuments());
		Document document = personCollection.find().first();
		assertNotNull(document);
		assertEquals("maho", document.get("_id"));
		assertTrue(document.containsKey(Keywords.SUPER_TYPES_KEY));
		personCollection.drop();
		
		saveOptions.put(Options.OPTION_KEY_SUPERTYPES, "mySupaType");
		resource.getContents().add(person);
		
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		assertEquals(1, personCollection.countDocuments());
		document = personCollection.find().first();
		assertNotNull(document);
		assertEquals("maho", document.get("_id"));
		assertFalse(document.containsKey(Keywords.SUPER_TYPES_KEY));
		assertTrue(document.containsKey("mySupaType"));
		
		personCollection.drop();
		
		resource.getContents().clear();
		testResourceSet(resourceSet, resource, 1, 0);
		resource.unload();
		
		/*
		 * Option super type overwrites collection name
		 */
		saveOptions.put(Options.OPTION_COLLECTION_NAME, BasicPackage.Literals.PERSON);
		saveOptions.put(Options.OPTION_STORE_SUPERTYPE, Boolean.FALSE);
		saveOptions.put(Options.OPTION_KEY_SUPERTYPES, "mySupaType");
		resource.getContents().add(person);
		
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		assertEquals(1, personCollection.countDocuments());
		document = personCollection.find().first();
		assertNotNull(document);
		assertEquals("maho", document.get("_id"));
		assertFalse(document.containsKey(Keywords.SUPER_TYPES_KEY));
		assertFalse(document.containsKey("mySupaType"));
		
		personCollection.drop();
		
		resource.getContents().clear();
		testResourceSet(resourceSet, resource, 1, 0);
		resource.unload();
		
		saveOptions = new HashMap<String, Object>();
		saveOptions.put(Options.OPTION_COLLECTION_NAME, BasicPackage.Literals.PERSON);
		
		Person p = BasicFactory.eINSTANCE.createPerson();
		p.setFirstName("Emil");
		p.setLastName("Tester" );
		p.setId("etester");
		person.setGender(GenderType.MALE);
		
		resource.getContents().clear();
		testResourceSet(resourceSet, resource, 1, 0);
		resource.unload();
		resource.getContents().add(p);
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		assertEquals(1, personCollection.countDocuments());
		document = personCollection.find().first();
		assertEquals("etester", document.get("_id"));
		assertFalse(document.containsKey(Keywords.SUPER_TYPES_KEY));
		
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
	public void testUpdateCollectionName() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
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
		saveOptions.put(Options.OPTION_COLLECTION_NAME, BasicPackage.Literals.PERSON);
		BusinessPerson person = BasicFactory.eINSTANCE.createBusinessPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann" );
		person.setId("maho");
		person.setCompanyIdCardNumber("666");
		person.setGender(GenderType.MALE);
		person.getContact().add(EcoreUtil.copy(c1));
		person.getContact().add(EcoreUtil.copy(c2));
		resource.getContents().add(person);
		
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		assertEquals(1, personCollection.countDocuments());
		Document document = personCollection.find().first();
		assertNotNull(document);
		assertEquals("maho", document.get("_id"));
		assertEquals("Mark", document.get("firstName"));
		assertTrue(document.containsKey(Keywords.SUPER_TYPES_KEY));
		
		person.setFirstName("Juergen");
		resource.save(saveOptions);
		
		assertEquals(1, personCollection.countDocuments());
		document = personCollection.find().first();
		assertNotNull(document);
		assertEquals("maho", document.get("_id"));
		assertEquals("Juergen", document.get("firstName"));
		assertTrue(document.containsKey(Keywords.SUPER_TYPES_KEY));
		
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
	public void testReadSuperTypes() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
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
		saveOptions.put(Options.OPTION_STORE_SUPERTYPE, Boolean.TRUE);
		BusinessPerson person = BasicFactory.eINSTANCE.createBusinessPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann" );
		person.setId("maho");
		person.setCompanyIdCardNumber("666");
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
		
		Person p = BasicFactory.eINSTANCE.createPerson();
		p.setFirstName("Emil");
		p.setLastName("Tester" );
		p.setId("etester");
		p.setGender(GenderType.MALE);
		resource.getContents().add(p);
		
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		resource.getContents().clear();
		testResourceSet(resourceSet, resource, 1, 0);
		resource.unload();
		
		Address address = BasicFactory.eINSTANCE.createAddress();
		address.setId("adr");
		address.setStreet("My-Street");
		resource.getContents().add(address);
		
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		resource.getContents().clear();
		testResourceSet(resourceSet, resource, 1, 0);
		resource.unload();
		
		assertEquals(3, personCollection.countDocuments());
		
		resourceSet.getResources().clear();
		
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		testResourceSet(resourceSet, findResource, 1, 0);
		findResource.load(null);
		// 3 new result elements + the 2 existing
		testResourceSet(resourceSet, findResource, 4, 1);
		
		assertEquals(1, findResource.getContents().size());
		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
		EReferenceCollection collection = (EReferenceCollection) findResource.getContents().get(0);
		assertEquals(3, collection.getValues().size());
		
		assertTrue(collection.getValues().stream().filter(eo->eo instanceof Address).findFirst().isPresent());
		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("Person")).findFirst().isPresent());
		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("BusinessPerson")).findFirst().isPresent());
		
		resourceSet.getResources().clear();
		
		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		Map<String, Object> loadOptions = new HashMap<String, Object>();
		loadOptions.put(Options.OPTION_FILTER_ECLASS, BasicPackage.Literals.ADDRESS);
		
		testResourceSet(resourceSet, findResource, 1, 0);
		findResource.load(loadOptions);
		testResourceSet(resourceSet, findResource, 2, 1);
		
		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
		collection = (EReferenceCollection) findResource.getContents().get(0);
		assertEquals(1, collection.getValues().size());
		
		assertTrue(collection.getValues().stream().filter(eo->eo instanceof Address).findFirst().isPresent());
		
		resourceSet.getResources().clear();
		
		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		loadOptions.put(Options.OPTION_FILTER_ECLASS, BasicPackage.Literals.PERSON);
		
		testResourceSet(resourceSet, findResource, 1, 0);
		findResource.load(loadOptions);
		testResourceSet(resourceSet, findResource, 3, 1);
		
//		assertEquals(1, findResource.getContents().size());
		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
		collection = (EReferenceCollection) findResource.getContents().get(0);
		assertEquals(2, collection.getValues().size());
		
		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("Person")).findFirst().isPresent());
		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("BusinessPerson")).findFirst().isPresent());
		
	}
	/**
	 * Test creation of object and returning results as well as updating
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testReadCollectionName() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
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
		saveOptions.put(Options.OPTION_COLLECTION_NAME, BasicPackage.Literals.PERSON);
		BusinessPerson person = BasicFactory.eINSTANCE.createBusinessPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann" );
		person.setId("maho");
		person.setCompanyIdCardNumber("666");
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
		
		Person p = BasicFactory.eINSTANCE.createPerson();
		p.setFirstName("Emil");
		p.setLastName("Tester" );
		p.setId("etester");
		p.setGender(GenderType.MALE);
		resource.getContents().add(p);
		
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		resource.getContents().clear();
		testResourceSet(resourceSet, resource, 1, 0);
		resource.unload();
		
		Address address = BasicFactory.eINSTANCE.createAddress();
		address.setId("adr");
		address.setStreet("My-Street");
		resource.getContents().add(address);
		
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		resource.getContents().clear();
		testResourceSet(resourceSet, resource, 1, 0);
		resource.unload();
		
		assertEquals(3, personCollection.countDocuments());
		
		resourceSet.getResources().clear();
		
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		testResourceSet(resourceSet, findResource, 1, 0);
		findResource.load(null);
		// 3 new result elements + the 2 existing
		testResourceSet(resourceSet, findResource, 4, 1);
		
		assertEquals(1, findResource.getContents().size());
		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
		EReferenceCollection collection = (EReferenceCollection) findResource.getContents().get(0);
		assertEquals(3, collection.getValues().size());
		
		assertTrue(collection.getValues().stream().filter(eo->eo instanceof Address).findFirst().isPresent());
		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("Person")).findFirst().isPresent());
		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("BusinessPerson")).findFirst().isPresent());
		
		resourceSet.getResources().clear();
		
		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		Map<String, Object> loadOptions = new HashMap<String, Object>();
		loadOptions.put(Options.OPTION_COLLECTION_NAME, BasicPackage.Literals.ADDRESS);
		
		testResourceSet(resourceSet, findResource, 1, 0);
		findResource.load(loadOptions);
		testResourceSet(resourceSet, findResource, 2, 1);
		
		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
		collection = (EReferenceCollection) findResource.getContents().get(0);
		assertEquals(1, collection.getValues().size());
		
		assertTrue(collection.getValues().stream().filter(eo->eo instanceof Address).findFirst().isPresent());
		
		resourceSet.getResources().clear();
		
		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		loadOptions.put(Options.OPTION_COLLECTION_NAME, BasicPackage.Literals.PERSON);
		
		testResourceSet(resourceSet, findResource, 1, 0);
		findResource.load(loadOptions);
		testResourceSet(resourceSet, findResource, 3, 1);
		
//		assertEquals(1, findResource.getContents().size());
		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
		collection = (EReferenceCollection) findResource.getContents().get(0);
		assertEquals(2, collection.getValues().size());
		
		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("Person")).findFirst().isPresent());
		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("BusinessPerson")).findFirst().isPresent());
		
	}
	
	/**
	 * Test creation of object and returning results as well as updating
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testReadSuperTypesStrict() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
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
		saveOptions.put(Options.OPTION_STORE_SUPERTYPE, Boolean.TRUE);
		BusinessPerson person = BasicFactory.eINSTANCE.createBusinessPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann" );
		person.setId("maho");
		person.setCompanyIdCardNumber("666");
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
		
		Person p = BasicFactory.eINSTANCE.createPerson();
		p.setFirstName("Emil");
		p.setLastName("Tester" );
		p.setId("etester");
		p.setGender(GenderType.MALE);
		resource.getContents().add(p);
		
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		resource.getContents().clear();
		testResourceSet(resourceSet, resource, 1, 0);
		resource.unload();
		
		Address address = BasicFactory.eINSTANCE.createAddress();
		address.setId("adr");
		address.setStreet("My-Street");
		resource.getContents().add(address);
		
		testResourceSet(resourceSet, resource, 1, 1);
		resource.save(saveOptions);
		testResourceSet(resourceSet, resource, 1, 1);
		
		resource.getContents().clear();
		testResourceSet(resourceSet, resource, 1, 0);
		resource.unload();
		
		assertEquals(3, personCollection.countDocuments());
		
		resourceSet.getResources().clear();
		
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		testResourceSet(resourceSet, findResource, 1, 0);
		findResource.load(null);
		// 3 new result elements + the 2 existing
		testResourceSet(resourceSet, findResource, 4, 1);
		
		assertEquals(1, findResource.getContents().size());
		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
		EReferenceCollection collection = (EReferenceCollection) findResource.getContents().get(0);
		assertEquals(3, collection.getValues().size());
		
		assertTrue(collection.getValues().stream().filter(eo->eo instanceof Address).findFirst().isPresent());
		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("Person")).findFirst().isPresent());
		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("BusinessPerson")).findFirst().isPresent());
		
		resourceSet.getResources().clear();
		
		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		Map<String, Object> loadOptions = new HashMap<String, Object>();
		loadOptions.put(Options.OPTION_FILTER_ECLASS, BasicPackage.Literals.ADDRESS);
		
		testResourceSet(resourceSet, findResource, 1, 0);
		findResource.load(loadOptions);
		testResourceSet(resourceSet, findResource, 2, 1);
		
		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
		collection = (EReferenceCollection) findResource.getContents().get(0);
		assertEquals(1, collection.getValues().size());
		
		assertTrue(collection.getValues().stream().filter(eo->eo instanceof Address).findFirst().isPresent());
		
		resourceSet.getResources().clear();
		
		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		loadOptions.put(Options.OPTION_FILTER_ECLASS, BasicPackage.Literals.PERSON);
		loadOptions.put(Options.OPTION_FILTER_ECLASS_STRICT, Boolean.TRUE);
		
		testResourceSet(resourceSet, findResource, 1, 0);
		findResource.load(loadOptions);
		testResourceSet(resourceSet, findResource, 2, 1);
		
//		assertEquals(1, findResource.getContents().size());
		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
		collection = (EReferenceCollection) findResource.getContents().get(0);
		assertEquals(1, collection.getValues().size());
		
		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("Person")).findFirst().isPresent());
		
		resourceSet.getResources().clear();
		
		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		loadOptions.put(Options.OPTION_FILTER_ECLASS, BasicPackage.Literals.PERSON);
		loadOptions.put(Options.OPTION_FILTER_ECLASS_STRICT, Boolean.FALSE);
		
		testResourceSet(resourceSet, findResource, 1, 0);
		findResource.load(loadOptions);
		testResourceSet(resourceSet, findResource, 3, 1);
		
//		assertEquals(1, findResource.getContents().size());
		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
		collection = (EReferenceCollection) findResource.getContents().get(0);
		assertEquals(2, collection.getValues().size());
		
		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("Person")).findFirst().isPresent());
		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("BusinessPerson")).findFirst().isPresent());
		
		resourceSet.getResources().clear();
		
		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		loadOptions.put(Options.OPTION_FILTER_ECLASS, BasicPackage.Literals.BUSINESS_CONTACT);
		loadOptions.put(Options.OPTION_FILTER_ECLASS_STRICT, Boolean.TRUE);
		
		testResourceSet(resourceSet, findResource, 1, 0);
		findResource.load(loadOptions);
		testResourceSet(resourceSet, findResource, 1, 1);
		
		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
		collection = (EReferenceCollection) findResource.getContents().get(0);
		assertEquals(0, collection.getValues().size());
		
	}
	
	private void testResourceSet(ResourceSet rs, Resource r, int expectedResources, int expectedResourceContent) {
		assertNotNull(rs);
		assertNotNull(r);
		assertEquals(expectedResources, rs.getResources().size());
		assertTrue(rs.getResources().contains(r));
		assertEquals(expectedResourceContent, r.getContents().size());
	}

}
