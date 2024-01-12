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
//import java.util.HashMap;
//import java.util.Map;
//
//import org.eclipse.emf.ecore.resource.ResourceSet;
//import org.eclipse.emf.ecore.util.EcoreUtil;
//import org.gecko.collection.EReferenceCollection;
//import org.gecko.emf.mongo.handlers.MongoResourceSetConfigurator;
//import org.gecko.emf.mongo.handlers.MongoResourceSetConfiguratorComponent;
//import org.gecko.emf.osgi.EMFNamespaces;
//import org.gecko.emf.osgi.ResourceSetConfigurator;
//import org.gecko.emf.osgi.ResourceSetFactory;
//import org.gecko.emf.osgi.model.test.BusinessPerson;
//import org.gecko.emf.osgi.model.test.Contact;
//import org.gecko.emf.osgi.model.test.ContactContextType;
//import org.gecko.emf.osgi.model.test.ContactType;
//import org.gecko.emf.osgi.model.test.GenderType;
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
//import com.sun.org.apache.xpath.internal.compiler.Keywords;
//
//import jdk.internal.loader.Resource;
//
///**
// * Integration tests for the complete EMF mongo setup
// * @author Mark Hoffmann
// * @since 26.07.2017
// */
//@RunWith(MockitoJUnitRunner.class)
//public class StoreSuperTypeIntegrationTest extends MongoEMFSetting {
//	
//	/**
//	 * Test creation of object and returning results as well as updating
//	 * @throws IOException 
//	 * @throws BundleException 
//	 * @throws InvalidSyntaxException 
//	 * @throws InterruptedException 
//	 */
//	@Test
//	public void testWriteSuperTypes() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
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
//		MongoCollection<Document> personCollection = client.getDatabase("test").getCollection("Person");
//		personCollection.drop();
//		
//		// create contacts
//		Contact c1 = TestFactory.eINSTANCE.createContact();
//		c1.setContext(ContactContextType.PRIVATE);
//		c1.setType(ContactType.SKYPE);
//		c1.setValue("charles-brown");
//		Contact c2 = TestFactory.eINSTANCE.createContact();
//		c2.setContext(ContactContextType.WORK);
//		c2.setType(ContactType.EMAIL);
//		c2.setValue("mark.hoffmann@tests.de");
//		
//		assertEquals(0, resourceSet.getResources().size());
//		assertEquals(0, personCollection.countDocuments());
//		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
//		testResourceSet(resourceSet, resource, 1, 0);
//		
//		Map<String, Object> saveOptions = new HashMap<String, Object>();
//		saveOptions.put(Options.OPTION_STORE_SUPERTYPE, Boolean.TRUE);
//		BusinessPerson person = TestFactory.eINSTANCE.createBusinessPerson();
//		person.setFirstName("Mark");
//		person.setLastName("Hoffmann" );
//		person.setId("maho");
//		person.setCompanyIdCardNumber("666");
//		person.setGender(GenderType.MALE);
//		person.getContact().add(EcoreUtil.copy(c1));
//		person.getContact().add(EcoreUtil.copy(c2));
//		resource.getContents().add(person);
//		
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		
//		assertEquals(1, personCollection.countDocuments());
//		Document document = personCollection.find().first();
//		assertNotNull(document);
//		assertEquals("maho", document.get("_id"));
//		assertTrue(document.containsKey(Keywords.SUPER_TYPES_KEY));
//		personCollection.drop();
//		
//		saveOptions.put(Options.OPTION_KEY_SUPERTYPES, "mySupaType");
//		resource.getContents().add(person);
//		
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		assertEquals(1, personCollection.countDocuments());
//		document = personCollection.find().first();
//		assertNotNull(document);
//		assertEquals("maho", document.get("_id"));
//		assertFalse(document.containsKey(Keywords.SUPER_TYPES_KEY));
//		assertTrue(document.containsKey("mySupaType"));
//		
//		personCollection.drop();
//
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		
//		saveOptions.put(Options.OPTION_STORE_SUPERTYPE, Boolean.FALSE);
//		saveOptions.put(Options.OPTION_KEY_SUPERTYPES, "mySupaType");
//		resource.getContents().add(person);
//		
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		assertEquals(1, personCollection.countDocuments());
//		document = personCollection.find().first();
//		assertNotNull(document);
//		assertEquals("maho", document.get("_id"));
//		assertFalse(document.containsKey(Keywords.SUPER_TYPES_KEY));
//		assertFalse(document.containsKey("mySupaType"));
//		
//		personCollection.drop();
//		
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		
//		saveOptions = new HashMap<String, Object>();
//		saveOptions.put(Options.OPTION_STORE_SUPERTYPE, Boolean.TRUE);
//
//		Person p = TestFactory.eINSTANCE.createPerson();
//		p.setFirstName("Emil");
//		p.setLastName("Tester" );
//		p.setId("etester");
//		person.setGender(GenderType.MALE);
//		
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		resource.getContents().add(p);
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		assertEquals(1, personCollection.countDocuments());
//		document = personCollection.find().first();
//		assertEquals("etester", document.get("_id"));
//		assertFalse(document.containsKey(Keywords.SUPER_TYPES_KEY));
//		
//		personCollection.drop();
//		
//	}
//	
//	/**
//	 * Test creation of object and returning results as well as updating
//	 * @throws IOException 
//	 * @throws BundleException 
//	 * @throws InvalidSyntaxException 
//	 * @throws InterruptedException 
//	 */
//	@Test
//	public void testWriteCollectionName() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
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
//		MongoCollection<Document> personCollection = client.getDatabase("test").getCollection("Person");
//		personCollection.drop();
//		
//		// create contacts
//		Contact c1 = TestFactory.eINSTANCE.createContact();
//		c1.setContext(ContactContextType.PRIVATE);
//		c1.setType(ContactType.SKYPE);
//		c1.setValue("charles-brown");
//		Contact c2 = TestFactory.eINSTANCE.createContact();
//		c2.setContext(ContactContextType.WORK);
//		c2.setType(ContactType.EMAIL);
//		c2.setValue("mark.hoffmann@tests.de");
//		
//		assertEquals(0, resourceSet.getResources().size());
//		assertEquals(0, personCollection.countDocuments());
//		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
//		testResourceSet(resourceSet, resource, 1, 0);
//		
//		Map<String, Object> saveOptions = new HashMap<String, Object>();
//		saveOptions.put(Options.OPTION_COLLECTION_NAME, BasicPackage.Literals.PERSON);
//		BusinessPerson person = TestFactory.eINSTANCE.createBusinessPerson();
//		person.setFirstName("Mark");
//		person.setLastName("Hoffmann" );
//		person.setId("maho");
//		person.setCompanyIdCardNumber("666");
//		person.setGender(GenderType.MALE);
//		person.getContact().add(EcoreUtil.copy(c1));
//		person.getContact().add(EcoreUtil.copy(c2));
//		resource.getContents().add(person);
//		
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		
//		assertEquals(1, personCollection.countDocuments());
//		Document document = personCollection.find().first();
//		assertNotNull(document);
//		assertEquals("maho", document.get("_id"));
//		assertTrue(document.containsKey(Keywords.SUPER_TYPES_KEY));
//		personCollection.drop();
//		
//		saveOptions.put(Options.OPTION_KEY_SUPERTYPES, "mySupaType");
//		resource.getContents().add(person);
//		
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		assertEquals(1, personCollection.countDocuments());
//		document = personCollection.find().first();
//		assertNotNull(document);
//		assertEquals("maho", document.get("_id"));
//		assertFalse(document.containsKey(Keywords.SUPER_TYPES_KEY));
//		assertTrue(document.containsKey("mySupaType"));
//		
//		personCollection.drop();
//		
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		
//		/*
//		 * Option super type overwrites collection name
//		 */
//		saveOptions.put(Options.OPTION_COLLECTION_NAME, BasicPackage.Literals.PERSON);
//		saveOptions.put(Options.OPTION_STORE_SUPERTYPE, Boolean.FALSE);
//		saveOptions.put(Options.OPTION_KEY_SUPERTYPES, "mySupaType");
//		resource.getContents().add(person);
//		
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		assertEquals(1, personCollection.countDocuments());
//		document = personCollection.find().first();
//		assertNotNull(document);
//		assertEquals("maho", document.get("_id"));
//		assertFalse(document.containsKey(Keywords.SUPER_TYPES_KEY));
//		assertFalse(document.containsKey("mySupaType"));
//		
//		personCollection.drop();
//		
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		
//		saveOptions = new HashMap<String, Object>();
//		saveOptions.put(Options.OPTION_COLLECTION_NAME, BasicPackage.Literals.PERSON);
//		
//		Person p = TestFactory.eINSTANCE.createPerson();
//		p.setFirstName("Emil");
//		p.setLastName("Tester" );
//		p.setId("etester");
//		person.setGender(GenderType.MALE);
//		
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		resource.getContents().add(p);
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		assertEquals(1, personCollection.countDocuments());
//		document = personCollection.find().first();
//		assertEquals("etester", document.get("_id"));
//		assertFalse(document.containsKey(Keywords.SUPER_TYPES_KEY));
//		
//		personCollection.drop();
//		
//	}
//	
//	/**
//	 * Test creation of object and returning results as well as updating
//	 * @throws IOException 
//	 * @throws BundleException 
//	 * @throws InvalidSyntaxException 
//	 * @throws InterruptedException 
//	 */
//	@Test
//	public void testUpdateCollectionName() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
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
//		MongoCollection<Document> personCollection = client.getDatabase("test").getCollection("Person");
//		personCollection.drop();
//		
//		// create contacts
//		Contact c1 = TestFactory.eINSTANCE.createContact();
//		c1.setContext(ContactContextType.PRIVATE);
//		c1.setType(ContactType.SKYPE);
//		c1.setValue("charles-brown");
//		Contact c2 = TestFactory.eINSTANCE.createContact();
//		c2.setContext(ContactContextType.WORK);
//		c2.setType(ContactType.EMAIL);
//		c2.setValue("mark.hoffmann@tests.de");
//		
//		assertEquals(0, resourceSet.getResources().size());
//		assertEquals(0, personCollection.countDocuments());
//		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
//		testResourceSet(resourceSet, resource, 1, 0);
//		
//		Map<String, Object> saveOptions = new HashMap<String, Object>();
//		saveOptions.put(Options.OPTION_COLLECTION_NAME, BasicPackage.Literals.PERSON);
//		BusinessPerson person = TestFactory.eINSTANCE.createBusinessPerson();
//		person.setFirstName("Mark");
//		person.setLastName("Hoffmann" );
//		person.setId("maho");
//		person.setCompanyIdCardNumber("666");
//		person.setGender(GenderType.MALE);
//		person.getContact().add(EcoreUtil.copy(c1));
//		person.getContact().add(EcoreUtil.copy(c2));
//		resource.getContents().add(person);
//		
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		assertEquals(1, personCollection.countDocuments());
//		Document document = personCollection.find().first();
//		assertNotNull(document);
//		assertEquals("maho", document.get("_id"));
//		assertEquals("Mark", document.get("firstName"));
//		assertTrue(document.containsKey(Keywords.SUPER_TYPES_KEY));
//		
//		person.setFirstName("Juergen");
//		resource.save(saveOptions);
//		
//		assertEquals(1, personCollection.countDocuments());
//		document = personCollection.find().first();
//		assertNotNull(document);
//		assertEquals("maho", document.get("_id"));
//		assertEquals("Juergen", document.get("firstName"));
//		assertTrue(document.containsKey(Keywords.SUPER_TYPES_KEY));
//		
//		personCollection.drop();
//		
//	}
//	
//	/**
//	 * Test creation of object and returning results as well as updating
//	 * @throws IOException 
//	 * @throws BundleException 
//	 * @throws InvalidSyntaxException 
//	 * @throws InterruptedException 
//	 */
//	@Test
//	public void testReadSuperTypes() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
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
//		MongoCollection<Document> personCollection = client.getDatabase("test").getCollection("Person");
//		personCollection.drop();
//		
//		// create contacts
//		Contact c1 = TestFactory.eINSTANCE.createContact();
//		c1.setContext(ContactContextType.PRIVATE);
//		c1.setType(ContactType.SKYPE);
//		c1.setValue("charles-brown");
//		Contact c2 = TestFactory.eINSTANCE.createContact();
//		c2.setContext(ContactContextType.WORK);
//		c2.setType(ContactType.EMAIL);
//		c2.setValue("mark.hoffmann@tests.de");
//		
//		assertEquals(0, resourceSet.getResources().size());
//		assertEquals(0, personCollection.countDocuments());
//		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
//		testResourceSet(resourceSet, resource, 1, 0);
//		
//		Map<String, Object> saveOptions = new HashMap<String, Object>();
//		saveOptions.put(Options.OPTION_STORE_SUPERTYPE, Boolean.TRUE);
//		BusinessPerson person = TestFactory.eINSTANCE.createBusinessPerson();
//		person.setFirstName("Mark");
//		person.setLastName("Hoffmann" );
//		person.setId("maho");
//		person.setCompanyIdCardNumber("666");
//		person.setGender(GenderType.MALE);
//		person.getContact().add(EcoreUtil.copy(c1));
//		person.getContact().add(EcoreUtil.copy(c2));
//		resource.getContents().add(person);
//		
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		
//		Person p = TestFactory.eINSTANCE.createPerson();
//		p.setFirstName("Emil");
//		p.setLastName("Tester" );
//		p.setId("etester");
//		p.setGender(GenderType.MALE);
//		resource.getContents().add(p);
//		
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		
//		Address address = TestFactory.eINSTANCE.createAddress();
//		address.setId("adr");
//		address.setStreet("My-Street");
//		resource.getContents().add(address);
//		
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		
//		assertEquals(3, personCollection.countDocuments());
//		
//		resourceSet.getResources().clear();
//		
//		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
//		testResourceSet(resourceSet, findResource, 1, 0);
//		findResource.load(null);
//		// 3 new result elements + the 2 existing
//		testResourceSet(resourceSet, findResource, 4, 1);
//		
//		assertEquals(1, findResource.getContents().size());
//		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
//		EReferenceCollection collection = (EReferenceCollection) findResource.getContents().get(0);
//		assertEquals(3, collection.getValues().size());
//		
//		assertTrue(collection.getValues().stream().filter(eo->eo instanceof Address).findFirst().isPresent());
//		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("Person")).findFirst().isPresent());
//		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("BusinessPerson")).findFirst().isPresent());
//		
//		resourceSet.getResources().clear();
//		
//		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
//		Map<String, Object> loadOptions = new HashMap<String, Object>();
//		loadOptions.put(Options.OPTION_FILTER_ECLASS, BasicPackage.Literals.ADDRESS);
//		
//		testResourceSet(resourceSet, findResource, 1, 0);
//		findResource.load(loadOptions);
//		testResourceSet(resourceSet, findResource, 2, 1);
//		
//		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
//		collection = (EReferenceCollection) findResource.getContents().get(0);
//		assertEquals(1, collection.getValues().size());
//		
//		assertTrue(collection.getValues().stream().filter(eo->eo instanceof Address).findFirst().isPresent());
//		
//		resourceSet.getResources().clear();
//		
//		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
//		loadOptions.put(Options.OPTION_FILTER_ECLASS, BasicPackage.Literals.PERSON);
//		
//		testResourceSet(resourceSet, findResource, 1, 0);
//		findResource.load(loadOptions);
//		testResourceSet(resourceSet, findResource, 3, 1);
//		
////		assertEquals(1, findResource.getContents().size());
//		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
//		collection = (EReferenceCollection) findResource.getContents().get(0);
//		assertEquals(2, collection.getValues().size());
//		
//		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("Person")).findFirst().isPresent());
//		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("BusinessPerson")).findFirst().isPresent());
//		
//	}
//	/**
//	 * Test creation of object and returning results as well as updating
//	 * @throws IOException 
//	 * @throws BundleException 
//	 * @throws InvalidSyntaxException 
//	 * @throws InterruptedException 
//	 */
//	@Test
//	public void testReadCollectionName() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
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
//		MongoCollection<Document> personCollection = client.getDatabase("test").getCollection("Person");
//		personCollection.drop();
//		
//		// create contacts
//		Contact c1 = TestFactory.eINSTANCE.createContact();
//		c1.setContext(ContactContextType.PRIVATE);
//		c1.setType(ContactType.SKYPE);
//		c1.setValue("charles-brown");
//		Contact c2 = TestFactory.eINSTANCE.createContact();
//		c2.setContext(ContactContextType.WORK);
//		c2.setType(ContactType.EMAIL);
//		c2.setValue("mark.hoffmann@tests.de");
//		
//		assertEquals(0, resourceSet.getResources().size());
//		assertEquals(0, personCollection.countDocuments());
//		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
//		testResourceSet(resourceSet, resource, 1, 0);
//		
//		Map<String, Object> saveOptions = new HashMap<String, Object>();
//		saveOptions.put(Options.OPTION_COLLECTION_NAME, BasicPackage.Literals.PERSON);
//		BusinessPerson person = TestFactory.eINSTANCE.createBusinessPerson();
//		person.setFirstName("Mark");
//		person.setLastName("Hoffmann" );
//		person.setId("maho");
//		person.setCompanyIdCardNumber("666");
//		person.setGender(GenderType.MALE);
//		person.getContact().add(EcoreUtil.copy(c1));
//		person.getContact().add(EcoreUtil.copy(c2));
//		resource.getContents().add(person);
//		
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		
//		Person p = TestFactory.eINSTANCE.createPerson();
//		p.setFirstName("Emil");
//		p.setLastName("Tester" );
//		p.setId("etester");
//		p.setGender(GenderType.MALE);
//		resource.getContents().add(p);
//		
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		
//		Address address = TestFactory.eINSTANCE.createAddress();
//		address.setId("adr");
//		address.setStreet("My-Street");
//		resource.getContents().add(address);
//		
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		
//		assertEquals(3, personCollection.countDocuments());
//		
//		resourceSet.getResources().clear();
//		
//		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
//		testResourceSet(resourceSet, findResource, 1, 0);
//		findResource.load(null);
//		// 3 new result elements + the 2 existing
//		testResourceSet(resourceSet, findResource, 4, 1);
//		
//		assertEquals(1, findResource.getContents().size());
//		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
//		EReferenceCollection collection = (EReferenceCollection) findResource.getContents().get(0);
//		assertEquals(3, collection.getValues().size());
//		
//		assertTrue(collection.getValues().stream().filter(eo->eo instanceof Address).findFirst().isPresent());
//		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("Person")).findFirst().isPresent());
//		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("BusinessPerson")).findFirst().isPresent());
//		
//		resourceSet.getResources().clear();
//		
//		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
//		Map<String, Object> loadOptions = new HashMap<String, Object>();
//		loadOptions.put(Options.OPTION_COLLECTION_NAME, BasicPackage.Literals.ADDRESS);
//		
//		testResourceSet(resourceSet, findResource, 1, 0);
//		findResource.load(loadOptions);
//		testResourceSet(resourceSet, findResource, 2, 1);
//		
//		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
//		collection = (EReferenceCollection) findResource.getContents().get(0);
//		assertEquals(1, collection.getValues().size());
//		
//		assertTrue(collection.getValues().stream().filter(eo->eo instanceof Address).findFirst().isPresent());
//		
//		resourceSet.getResources().clear();
//		
//		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
//		loadOptions.put(Options.OPTION_COLLECTION_NAME, BasicPackage.Literals.PERSON);
//		
//		testResourceSet(resourceSet, findResource, 1, 0);
//		findResource.load(loadOptions);
//		testResourceSet(resourceSet, findResource, 3, 1);
//		
////		assertEquals(1, findResource.getContents().size());
//		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
//		collection = (EReferenceCollection) findResource.getContents().get(0);
//		assertEquals(2, collection.getValues().size());
//		
//		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("Person")).findFirst().isPresent());
//		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("BusinessPerson")).findFirst().isPresent());
//		
//	}
//	
//	/**
//	 * Test creation of object and returning results as well as updating
//	 * @throws IOException 
//	 * @throws BundleException 
//	 * @throws InvalidSyntaxException 
//	 * @throws InterruptedException 
//	 */
//	@Test
//	public void testReadSuperTypesStrict() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
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
//		MongoCollection<Document> personCollection = client.getDatabase("test").getCollection("Person");
//		personCollection.drop();
//		
//		// create contacts
//		Contact c1 = TestFactory.eINSTANCE.createContact();
//		c1.setContext(ContactContextType.PRIVATE);
//		c1.setType(ContactType.SKYPE);
//		c1.setValue("charles-brown");
//		Contact c2 = TestFactory.eINSTANCE.createContact();
//		c2.setContext(ContactContextType.WORK);
//		c2.setType(ContactType.EMAIL);
//		c2.setValue("mark.hoffmann@tests.de");
//		
//		assertEquals(0, resourceSet.getResources().size());
//		assertEquals(0, personCollection.countDocuments());
//		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
//		testResourceSet(resourceSet, resource, 1, 0);
//		
//		Map<String, Object> saveOptions = new HashMap<String, Object>();
//		saveOptions.put(Options.OPTION_STORE_SUPERTYPE, Boolean.TRUE);
//		BusinessPerson person = TestFactory.eINSTANCE.createBusinessPerson();
//		person.setFirstName("Mark");
//		person.setLastName("Hoffmann" );
//		person.setId("maho");
//		person.setCompanyIdCardNumber("666");
//		person.setGender(GenderType.MALE);
//		person.getContact().add(EcoreUtil.copy(c1));
//		person.getContact().add(EcoreUtil.copy(c2));
//		resource.getContents().add(person);
//		
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		
//		Person p = TestFactory.eINSTANCE.createPerson();
//		p.setFirstName("Emil");
//		p.setLastName("Tester" );
//		p.setId("etester");
//		p.setGender(GenderType.MALE);
//		resource.getContents().add(p);
//		
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		
//		Address address = TestFactory.eINSTANCE.createAddress();
//		address.setId("adr");
//		address.setStreet("My-Street");
//		resource.getContents().add(address);
//		
//		testResourceSet(resourceSet, resource, 1, 1);
//		resource.save(saveOptions);
//		testResourceSet(resourceSet, resource, 1, 1);
//		
//		resource.getContents().clear();
//		testResourceSet(resourceSet, resource, 1, 0);
//		resource.unload();
//		
//		assertEquals(3, personCollection.countDocuments());
//		
//		resourceSet.getResources().clear();
//		
//		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
//		testResourceSet(resourceSet, findResource, 1, 0);
//		findResource.load(null);
//		// 3 new result elements + the 2 existing
//		testResourceSet(resourceSet, findResource, 4, 1);
//		
//		assertEquals(1, findResource.getContents().size());
//		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
//		EReferenceCollection collection = (EReferenceCollection) findResource.getContents().get(0);
//		assertEquals(3, collection.getValues().size());
//		
//		assertTrue(collection.getValues().stream().filter(eo->eo instanceof Address).findFirst().isPresent());
//		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("Person")).findFirst().isPresent());
//		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("BusinessPerson")).findFirst().isPresent());
//		
//		resourceSet.getResources().clear();
//		
//		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
//		Map<String, Object> loadOptions = new HashMap<String, Object>();
//		loadOptions.put(Options.OPTION_FILTER_ECLASS, BasicPackage.Literals.ADDRESS);
//		
//		testResourceSet(resourceSet, findResource, 1, 0);
//		findResource.load(loadOptions);
//		testResourceSet(resourceSet, findResource, 2, 1);
//		
//		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
//		collection = (EReferenceCollection) findResource.getContents().get(0);
//		assertEquals(1, collection.getValues().size());
//		
//		assertTrue(collection.getValues().stream().filter(eo->eo instanceof Address).findFirst().isPresent());
//		
//		resourceSet.getResources().clear();
//		
//		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
//		loadOptions.put(Options.OPTION_FILTER_ECLASS, BasicPackage.Literals.PERSON);
//		loadOptions.put(Options.OPTION_FILTER_ECLASS_STRICT, Boolean.TRUE);
//		
//		testResourceSet(resourceSet, findResource, 1, 0);
//		findResource.load(loadOptions);
//		testResourceSet(resourceSet, findResource, 2, 1);
//		
////		assertEquals(1, findResource.getContents().size());
//		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
//		collection = (EReferenceCollection) findResource.getContents().get(0);
//		assertEquals(1, collection.getValues().size());
//		
//		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("Person")).findFirst().isPresent());
//		
//		resourceSet.getResources().clear();
//		
//		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
//		loadOptions.put(Options.OPTION_FILTER_ECLASS, BasicPackage.Literals.PERSON);
//		loadOptions.put(Options.OPTION_FILTER_ECLASS_STRICT, Boolean.FALSE);
//		
//		testResourceSet(resourceSet, findResource, 1, 0);
//		findResource.load(loadOptions);
//		testResourceSet(resourceSet, findResource, 3, 1);
//		
////		assertEquals(1, findResource.getContents().size());
//		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
//		collection = (EReferenceCollection) findResource.getContents().get(0);
//		assertEquals(2, collection.getValues().size());
//		
//		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("Person")).findFirst().isPresent());
//		assertTrue(collection.getValues().stream().filter(eo->eo.eClass().getName().equals("BusinessPerson")).findFirst().isPresent());
//		
//		resourceSet.getResources().clear();
//		
//		findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
//		loadOptions.put(Options.OPTION_FILTER_ECLASS, BasicPackage.Literals.BUSINESS_CONTACT);
//		loadOptions.put(Options.OPTION_FILTER_ECLASS_STRICT, Boolean.TRUE);
//		
//		testResourceSet(resourceSet, findResource, 1, 0);
//		findResource.load(loadOptions);
//		testResourceSet(resourceSet, findResource, 1, 1);
//		
//		assertTrue(findResource.getContents().get(0) instanceof EReferenceCollection);
//		collection = (EReferenceCollection) findResource.getContents().get(0);
//		assertEquals(0, collection.getValues().size());
//		
//	}
//	
//	private void testResourceSet(ResourceSet rs, Resource r, int expectedResources, int expectedResourceContent) {
//		assertNotNull(rs);
//		assertNotNull(r);
//		assertEquals(expectedResources, rs.getResources().size());
//		assertTrue(rs.getResources().contains(r));
//		assertEquals(expectedResourceContent, r.getContents().size());
//	}
//
//}
