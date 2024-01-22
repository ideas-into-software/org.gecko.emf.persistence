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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.collection.ECollection;
import org.gecko.emf.mongo.Options;
import org.gecko.emf.mongo.handlers.MongoResourceSetConfigurator;
import org.gecko.emf.osgi.ResourceSetFactory;
import org.gecko.emf.osgi.annotation.require.RequireEMF;
import org.gecko.emf.osgi.configurator.ResourceSetConfigurator;
import org.gecko.emf.osgi.constants.EMFNamespaces;
import org.gecko.emf.osgi.example.model.basic.BasicFactory;
import org.gecko.emf.osgi.example.model.basic.BasicPackage;
import org.gecko.emf.osgi.example.model.basic.BusinessContact;
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
public class MongoQueryIntegrationTest extends MongoEMFSetting {

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
	 * Test creation of many objects and returning results
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testCreateAndFindObjects_ContainmentMany() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		
		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();
		
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

		assertEquals(0, personCollection.countDocuments());
		/*
		 * Inserting many persons and with containment contacts
		 */
		int insertSize = 10000;
		int insertBatchSize = 500;

		long start = System.currentTimeMillis();
		List<Person> personsList = new ArrayList<>(insertBatchSize);

		System.out.println("Batch inserting: ");
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
		Map<?,?> options = Collections.singletonMap(Options.OPTION_FORCE_INSERT, Boolean.TRUE);
		for (int i = 0; i < insertSize; i++) {
			Person person = BasicFactory.eINSTANCE.createPerson();
			person.setFirstName("Mark" + i);
			person.setLastName("Hoffmann" + i);
			person.setGender(GenderType.MALE);
			person.getContact().add(EcoreUtil.copy(c1));
			person.getContact().add(EcoreUtil.copy(c2));
			personsList.add(person);
			// using insert many
			if (i % (insertBatchSize - 1) == 0 || i == (insertSize - 1)) {
				resource.getContents().addAll(personsList);
				resource.save(options);
				if(personsList.size() > 1){
					assertTrue(resource.getContents().size() == 0);
				} else {
					resource.getContents().clear();
				}
				personsList.clear();
			}
		}
		System.out.println("Insert of " + insertSize  + " persons with batchSize=" + insertBatchSize + " took " + (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, personCollection.countDocuments());

		/*
		 * Find person in the collection
		 */
		start = System.currentTimeMillis();
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource.load(resourceSet.getLoadOptions());
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
		assertNotNull(p.eResource());
		assertEquals("Mark500", p.getFirstName());
		assertEquals("Hoffmann500", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		p = resultList.get(2500);
		assertNotNull(p.eResource());
		assertEquals("Mark2500", p.getFirstName());
		assertEquals("Hoffmann2500", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		p = resultList.get(8999);
		assertNotNull(p.eResource());
		assertEquals("Mark8999", p.getFirstName());
		assertEquals("Hoffmann8999", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		personCollection.drop();

	}
	
	/**
	 * Test creation of many objects and returning results
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testQueryWithSpecialCharacters() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();
		
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
		
		assertEquals(0, personCollection.countDocuments());
		/*
		 * Inserting many persons and with containment contacts
		 */
		int insertSize = 10000;
		int insertBatchSize = 500;
		
		long start = System.currentTimeMillis();
		List<Person> personsList = new ArrayList<>(insertBatchSize);
		
		System.out.println("Batch inserting: ");
		
		
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
		Map<?,?> options = Collections.singletonMap(Options.OPTION_FORCE_INSERT, Boolean.TRUE);
		for (int i = 0; i < insertSize; i++) {
			Person person = BasicFactory.eINSTANCE.createPerson();
			person.setFirstName("Mark" + i);
			person.setLastName("Hoffmann" + i);
			person.setGender(GenderType.MALE);
			person.getContact().add(EcoreUtil.copy(c1));
			person.getContact().add(EcoreUtil.copy(c2));
			personsList.add(person);
			// using insert many
			if (i % (insertBatchSize - 1) == 0 || i == (insertSize - 1)) {
				resource.getContents().addAll(personsList);
				resource.save(options);
				if(personsList.size() > 1){
					assertTrue(resource.getContents().size() == 0);
				} else {
					resource.getContents().clear();
				}
				personsList.clear();
			}
		}
		System.out.println("Insert of " + insertSize  + " persons with batchSize=" + insertBatchSize + " took " + (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, personCollection.countDocuments());
		
		/*
		 * Find person in the collection
		 */
		start = System.currentTimeMillis();
		
		String typeFilter = "{ \"filter\" : { \"_eClass\":\"" + EcoreUtil.getURI(BasicPackage.Literals.PERSON).toString() + "\" }}";
		
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/?" + URI.encodeQuery(typeFilter, true)));
		
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource.load(resourceSet.getLoadOptions());
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
		assertNotNull(p.eResource());
		assertEquals("Mark500", p.getFirstName());
		assertEquals("Hoffmann500", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());
		
		p = resultList.get(2500);
		assertNotNull(p.eResource());
		assertEquals("Mark2500", p.getFirstName());
		assertEquals("Hoffmann2500", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());
		
		p = resultList.get(8999);
		assertNotNull(p.eResource());
		assertEquals("Mark8999", p.getFirstName());
		assertEquals("Hoffmann8999", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());
		
		personCollection.drop();
		
	}
	
	/**
	 * Test creation of many objects and returning results
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testSkipLimitMany() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		
		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		
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
		
		assertEquals(0, personCollection.countDocuments());
		/*
		 * Inserting many persons and with containment contacts
		 */
		int insertSize = 100;
		int insertBatchSize = 500;
		
		long start = System.currentTimeMillis();
		List<Person> personsList = new ArrayList<>(insertBatchSize);
		
		System.out.println("Batch inserting: ");
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
		Map<?,?> options = Collections.singletonMap(Options.OPTION_FORCE_INSERT, Boolean.TRUE);
		for (int i = 0; i < insertSize; i++) {
			Person person = BasicFactory.eINSTANCE.createPerson();
			person.setFirstName("Mark" + i);
			person.setLastName("Hoffmann" + i);
			person.setGender(GenderType.MALE);
			person.getContact().add(EcoreUtil.copy(c1));
			person.getContact().add(EcoreUtil.copy(c2));
			personsList.add(person);
			// using insert many
			if (i % (insertBatchSize - 1) == 0 || i == (insertSize - 1)) {
				resource.getContents().addAll(personsList);
				resource.save(options);
				if(personsList.size() > 1){
					assertTrue(resource.getContents().size() == 0);
				} else {
					resource.getContents().clear();
				}
				personsList.clear();
			}
		}
		System.out.println("Insert of " + insertSize  + " persons with batchSize=" + insertBatchSize + " took " + (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, personCollection.countDocuments());
		
		/*
		 * Test 1
		 * Find person in the collection with skip and limit
		 */
		String query = "{\"filter\":{},\"skip\": 10,\"limit\":20}";
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?"+query));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource.load(resourceSet.getLoadOptions());
		// get the persons
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		
		ECollection resultCollection = (ECollection) findResource.getContents().get(0);
		findResource.unload();
		
		assertEquals(20, resultCollection.getValues().size());
		
		List<Person> personList = resultCollection.getValues().stream().map(eo->(Person)eo).collect(Collectors.toList());
		assertEquals(20, personList.size());
		Person p1Test1 = personList.get(0);
		assertEquals("Mark10", p1Test1.getFirstName());
		assertEquals("Hoffmann10", p1Test1.getLastName());
		Person p2Test1 = personList.get(19);
		assertEquals("Mark29", p2Test1.getFirstName());
		assertEquals("Hoffmann29", p2Test1.getLastName());
		
		
		
		/*
		 * Test 2
		 * Find person in the collection with skip and limit
		 */
		String query2 = "{\"filter\":{},\"skip\": 90,\"limit\":20}";
		Resource findResource2 = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?"+query2));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource2.load(resourceSet.getLoadOptions());
		// get the persons
		assertNotNull(findResource2);
		assertFalse(findResource2.getContents().isEmpty());
		assertEquals(1, findResource2.getContents().size());
		
		ECollection resultCollection2 = (ECollection) findResource2.getContents().get(0);
		findResource2.unload();
		
		assertEquals(10, resultCollection2.getValues().size());
		
		List<Person> personList2 = resultCollection2.getValues().stream().map(eo->(Person)eo).collect(Collectors.toList());
		assertEquals(10, personList2.size());
		assertFalse(personList2.size()==20);
		Person p1Test2 = personList2.get(0);
		assertEquals("Mark90", p1Test2.getFirstName());
		assertEquals("Hoffmann90", p1Test2.getLastName());
		Person p2Test2 = personList2.get(9);
		assertEquals("Mark99", p2Test2.getFirstName());
		assertEquals("Hoffmann99", p2Test2.getLastName());
		
		
		
		/*
		 * Test 3
		 * Find person in the collection with skip and limit
		 */
		String query3 = "{\"filter\":{},\"skip\": -2,\"limit\": 20}";
		Resource findResource3 = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?"+query3));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource3.load(resourceSet.getLoadOptions());
		// get the persons
		assertNotNull(findResource3);
		assertFalse(findResource3.getContents().isEmpty());
		assertEquals(1, findResource3.getContents().size());
		
		ECollection resultCollection3 = (ECollection) findResource3.getContents().get(0);
		findResource3.unload();
		
		assertEquals(20, resultCollection3.getValues().size());
		
		List<Person> personList3 = resultCollection3.getValues().stream().map(eo->(Person)eo).collect(Collectors.toList());
		assertEquals(20, personList3.size());
		Person p1Test3 = personList3.get(0);
		assertEquals("Mark0", p1Test3.getFirstName());
		assertEquals("Hoffmann0", p1Test3.getLastName());
		Person p2Test3 = personList3.get(19);
		assertEquals("Mark19", p2Test3.getFirstName());
		assertEquals("Hoffmann19", p2Test3.getLastName());
		
		
		/* Test 4
		 * Find person in the collection with skip and limit
		 */
		String query4 = "{\"filter\":{},\"skip\": 20}";
		Resource findResource4 = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?"+query4));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource4.load(resourceSet.getLoadOptions());
		// get the persons
		assertNotNull(findResource4);
		assertFalse(findResource4.getContents().isEmpty());
		assertEquals(1, findResource4.getContents().size());
		
		ECollection resultCollection4 = (ECollection) findResource4.getContents().get(0);
		findResource4.unload();
		
		assertEquals(80, resultCollection4.getValues().size());
		
		List<Person> personList4 = resultCollection4.getValues().stream().map(eo->(Person)eo).collect(Collectors.toList());
		assertEquals(80, personList4.size());
		Person p1Test4 = personList4.get(0);
		assertEquals("Mark20", p1Test4.getFirstName());
		assertEquals("Hoffmann20", p1Test4.getLastName());
		Person p2Test4 = personList4.get(79);
		assertEquals("Mark99", p2Test4.getFirstName());
		assertEquals("Hoffmann99", p2Test4.getLastName());
		
		
		/* Test 5
		 * Find person in the collection with skip and limit
		 */
		String query5 = "{\"filter\":{},\"limit\": 25}";
		Resource findResource5 = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?"+query5));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource5.load(resourceSet.getLoadOptions());
		// get the persons
		assertNotNull(findResource5);
		assertFalse(findResource5.getContents().isEmpty());
		assertEquals(1, findResource5.getContents().size());
		
		ECollection resultCollection5 = (ECollection) findResource5.getContents().get(0);
		findResource5.unload();
		
		assertEquals(25, resultCollection5.getValues().size());
		
		List<Person> personList5 = resultCollection5.getValues().stream().map(eo->(Person)eo).collect(Collectors.toList());
		assertEquals(25, personList5.size());
		Person p1Test5 = personList5.get(0);
		assertEquals("Mark0", p1Test5.getFirstName());
		assertEquals("Hoffmann0", p1Test5.getLastName());
		Person p2Test5 = personList5.get(24);
		assertEquals("Mark24", p2Test5.getFirstName());
		assertEquals("Hoffmann24", p2Test5.getLastName());
		
		
		/*
		 * Test 6
		 * Find person in the collection with skip and limit
		 */
		String query6 = "{\"filter\":{},\"skip\": -1,\"limit\": -1}";
		Resource findResource6 = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?"+query6));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource6.load(resourceSet.getLoadOptions());
		// get the persons
		assertNotNull(findResource6);
		assertFalse(findResource6.getContents().isEmpty());
		assertEquals(1, findResource6.getContents().size());
		
		ECollection resultCollection6 = (ECollection) findResource6.getContents().get(0);
		findResource6.unload();
		
		// we ignore negative limits
		assertEquals(100, resultCollection6.getValues().size());
		
		List<Person> personList6 = resultCollection6.getValues().stream().map(eo->(Person)eo).collect(Collectors.toList());
		assertEquals(100, personList6.size());
		Person p1Test6 = personList6.get(0);
		assertEquals("Mark0", p1Test6.getFirstName());
		assertEquals("Hoffmann0", p1Test6.getLastName());
		
		
		/*
		 * Test 7
		 * Find person in the collection with skip and limit
		 */
		String query7 = "{\"filter\":{},\"skip\": 30,\"limit\": -3}";
		Resource findResource7 = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?"+query7));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource7.load(resourceSet.getLoadOptions());
		// get the persons
		assertNotNull(findResource7);
		assertFalse(findResource7.getContents().isEmpty());
		assertEquals(1, findResource7.getContents().size());
		
		ECollection resultCollection7 = (ECollection) findResource7.getContents().get(0);
		findResource7.unload();
		// because of skip we have just 70 results
		assertEquals(70, resultCollection7.getValues().size());
		
		List<Person> personList7 = resultCollection7.getValues().stream().map(eo->(Person)eo).collect(Collectors.toList());
		assertEquals(70, personList7.size());
		Person p1Test7 = personList7.get(0);
		assertEquals("Mark30", p1Test7.getFirstName());
		assertEquals("Hoffmann30", p1Test7.getLastName());
		
		
		
		/* Test 8
		 * Find person in the collection with skip and limit
		 */
		String query8 = "{\"filter\":{},\"skip\": 100,\"limit\": 25}";
		Resource findResource8 = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?"+query8));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource8.load(resourceSet.getLoadOptions());
		// get the persons
		assertNotNull(findResource8);
		assertFalse(findResource8.getContents().isEmpty());
		assertEquals(1, findResource8.getContents().size());
		
		ECollection resultCollection8 = (ECollection) findResource8.getContents().get(0);
		findResource8.unload();
		
		assertEquals(0, resultCollection8.getValues().size());
		
		List<Person> personList8 = resultCollection8.getValues().stream().map(eo->(Person)eo).collect(Collectors.toList());
		assertEquals(0, personList8.size());
		
		
		
		personCollection.drop();
		
	}
	
	/**
	 * Test creation of many objects and returning results
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testCountResult() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		
		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		
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
		
		assertEquals(0, personCollection.countDocuments());
		/*
		 * Inserting many persons and with containment contacts
		 */
		int insertSize = 100;
		int insertBatchSize = 500;
		
		long start = System.currentTimeMillis();
		List<Person> personsList = new ArrayList<>(insertBatchSize);
		
		System.out.println("Batch inserting: ");
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
		Map<?,?> options = Collections.singletonMap(Options.OPTION_FORCE_INSERT, Boolean.TRUE);
		for (int i = 0; i < insertSize; i++) {
			Person person = BasicFactory.eINSTANCE.createPerson();
			person.setFirstName("Mark" + i);
			person.setLastName("Hoffmann" + i);
			person.setGender(GenderType.MALE);
			person.getContact().add(EcoreUtil.copy(c1));
			person.getContact().add(EcoreUtil.copy(c2));
			personsList.add(person);
			// using insert many
			if (i % (insertBatchSize - 1) == 0 || i == (insertSize - 1)) {
				resource.getContents().addAll(personsList);
				resource.save(options);
				if(personsList.size() > 1){
					assertTrue(resource.getContents().size() == 0);
				} else {
					resource.getContents().clear();
				}
				personsList.clear();
			}
		}
		System.out.println("Insert of " + insertSize  + " persons with batchSize=" + insertBatchSize + " took " + (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, personCollection.countDocuments());
		
		/*
		 * Test 1
		 * Find person in the collection with skip and limit and a count option
		 */
		String query = "{\"filter\":{},\"skip\": 10,\"limit\":20}";
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?"+query));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		Map<Object, Object> loadOptions = new HashMap<Object, Object>(resourceSet.getLoadOptions());
		loadOptions.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		loadOptions.put(Options.OPTION_COUNT_RESULT, Boolean.TRUE);
		findResource.load(loadOptions);
		// get the persons
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		
		ECollection resultCollection = (ECollection) findResource.getContents().get(0);
		findResource.unload();
		
		assertEquals(20, resultCollection.getValues().size());
		
		List<Person> personList = resultCollection.getValues().stream().map(eo->(Person)eo).collect(Collectors.toList());
		assertEquals(20, personList.size());
		Person p1Test1 = personList.get(0);
		assertEquals("Mark10", p1Test1.getFirstName());
		assertEquals("Hoffmann10", p1Test1.getLastName());
		Person p2Test1 = personList.get(19);
		assertEquals("Mark29", p2Test1.getFirstName());
		assertEquals("Hoffmann29", p2Test1.getLastName());
		
		Map<Object, Object> responseOption = (Map<Object, Object>) loadOptions.get(URIConverter.OPTION_RESPONSE);
		assertNotNull(responseOption);
		Object size = responseOption.get(Options.OPTION_COUNT_RESPONSE);
		assertNotNull(size);
		assertEquals(insertSize, ((Long)size).intValue());
		
		
		
		/*
		 * Test 2
		 * Find person in the collection with skip and limit and no count option set
		 */
		String query2 = "{\"filter\":{},\"skip\": 90,\"limit\":20}";
		Resource findResource2 = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?"+query2));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		loadOptions = new HashMap<Object, Object>(resourceSet.getLoadOptions());
		loadOptions.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		findResource2.load(loadOptions);
		// get the persons
		assertNotNull(findResource2);
		assertFalse(findResource2.getContents().isEmpty());
		assertEquals(1, findResource2.getContents().size());
		
		ECollection resultCollection2 = (ECollection) findResource2.getContents().get(0);
		findResource2.unload();
		
		assertEquals(10, resultCollection2.getValues().size());
		
		List<Person> personList2 = resultCollection2.getValues().stream().map(eo->(Person)eo).collect(Collectors.toList());
		assertEquals(10, personList2.size());
		assertFalse(personList2.size()==20);
		Person p1Test2 = personList2.get(0);
		assertEquals("Mark90", p1Test2.getFirstName());
		assertEquals("Hoffmann90", p1Test2.getLastName());
		Person p2Test2 = personList2.get(9);
		assertEquals("Mark99", p2Test2.getFirstName());
		assertEquals("Hoffmann99", p2Test2.getLastName());
		
		responseOption = (Map<Object, Object>) loadOptions.get(URIConverter.OPTION_RESPONSE);
		assertNotNull(responseOption);
		size = responseOption.get(Options.OPTION_COUNT_RESPONSE);
		assertNull(size);
		
		
		/*
		 * Test 3
		 * Find person in the collection with skip and limit with a wrong count option
		 */
		String query3 = "{\"filter\":{},\"skip\": -2,\"limit\": 20}";
		Resource findResource3 = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?"+query3));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		loadOptions = new HashMap<Object, Object>(resourceSet.getLoadOptions());
		loadOptions.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		loadOptions.put(Options.OPTION_COUNT_RESULT, "test");
		findResource3.load(loadOptions);
		// get the persons
		assertNotNull(findResource3);
		assertFalse(findResource3.getContents().isEmpty());
		assertEquals(1, findResource3.getContents().size());
		
		ECollection resultCollection3 = (ECollection) findResource3.getContents().get(0);
		findResource3.unload();
		
		assertEquals(20, resultCollection3.getValues().size());
		
		List<Person> personList3 = resultCollection3.getValues().stream().map(eo->(Person)eo).collect(Collectors.toList());
		assertEquals(20, personList3.size());
		Person p1Test3 = personList3.get(0);
		assertEquals("Mark0", p1Test3.getFirstName());
		assertEquals("Hoffmann0", p1Test3.getLastName());
		Person p2Test3 = personList3.get(19);
		assertEquals("Mark19", p2Test3.getFirstName());
		assertEquals("Hoffmann19", p2Test3.getLastName());
		
		responseOption = (Map<Object, Object>) loadOptions.get(URIConverter.OPTION_RESPONSE);
		assertNotNull(responseOption);
		size = responseOption.get(Options.OPTION_COUNT_RESPONSE);
		assertNull(size);
		
		
		/* Test 4
		 * Find person in the collection with skip and limit with false as count option
		 */
		String query4 = "{\"filter\":{},\"skip\": 20}";
		Resource findResource4 = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?"+query4));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		loadOptions = new HashMap<Object, Object>(resourceSet.getLoadOptions());
		loadOptions.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		loadOptions.put(Options.OPTION_COUNT_RESULT, false);
		findResource4.load(loadOptions);
		// get the persons
		assertNotNull(findResource4);
		assertFalse(findResource4.getContents().isEmpty());
		assertEquals(1, findResource4.getContents().size());
		
		ECollection resultCollection4 = (ECollection) findResource4.getContents().get(0);
		findResource4.unload();
		
		assertEquals(80, resultCollection4.getValues().size());
		
		List<Person> personList4 = resultCollection4.getValues().stream().map(eo->(Person)eo).collect(Collectors.toList());
		assertEquals(80, personList4.size());
		Person p1Test4 = personList4.get(0);
		assertEquals("Mark20", p1Test4.getFirstName());
		assertEquals("Hoffmann20", p1Test4.getLastName());
		Person p2Test4 = personList4.get(79);
		assertEquals("Mark99", p2Test4.getFirstName());
		assertEquals("Hoffmann99", p2Test4.getLastName());
		
		responseOption = (Map<Object, Object>) loadOptions.get(URIConverter.OPTION_RESPONSE);
		assertNotNull(responseOption);
		size = responseOption.get(Options.OPTION_COUNT_RESPONSE);
		assertNull(size);
		
		
		/* Test 5
		 * Find person in the collection with skip and limit no response map was set
		 */
		String query5 = "{\"filter\":{},\"limit\": 25}";
		Resource findResource5 = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?"+query5));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		loadOptions = new HashMap<Object, Object>(resourceSet.getLoadOptions());
		loadOptions.put(Options.OPTION_COUNT_RESULT, true);
		findResource5.load(loadOptions);
		// get the persons
		assertNotNull(findResource5);
		assertFalse(findResource5.getContents().isEmpty());
		assertEquals(1, findResource5.getContents().size());
		
		ECollection resultCollection5 = (ECollection) findResource5.getContents().get(0);
		findResource5.unload();
		
		assertEquals(25, resultCollection5.getValues().size());
		
		List<Person> personList5 = resultCollection5.getValues().stream().map(eo->(Person)eo).collect(Collectors.toList());
		assertEquals(25, personList5.size());
		Person p1Test5 = personList5.get(0);
		assertEquals("Mark0", p1Test5.getFirstName());
		assertEquals("Hoffmann0", p1Test5.getLastName());
		Person p2Test5 = personList5.get(24);
		assertEquals("Mark24", p2Test5.getFirstName());
		assertEquals("Hoffmann24", p2Test5.getLastName());
		
		responseOption = (Map<Object, Object>) loadOptions.get(URIConverter.OPTION_RESPONSE);
		assertNull(responseOption);
		
		
		
		
		personCollection.drop();
		
	}

	/**
	 * Test creation of many objects and returning results
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testProjectionMany() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		
		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		
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
		
		assertEquals(0, personCollection.countDocuments());
		/*
		 * Inserting many persons and with containment contacts
		 */
		int insertSize = 100;
		int insertBatchSize = 500;
		
		long start = System.currentTimeMillis();
		List<Person> personsList = new ArrayList<>(insertBatchSize);
		
		System.out.println("Batch inserting: ");
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
		Map<?,?> options = Collections.singletonMap(Options.OPTION_FORCE_INSERT, Boolean.TRUE);
		for (int i = 0; i < insertSize; i++) {
			Person person = BasicFactory.eINSTANCE.createPerson();
			person.setId(String.valueOf(i));
			person.setFirstName("Mark" + i);
			person.setLastName("Hoffmann" + i);
			person.setGender(GenderType.MALE);
			person.getContact().add(EcoreUtil.copy(c1));
			person.getContact().add(EcoreUtil.copy(c2));
			personsList.add(person);
			// using insert many
			if (i % (insertBatchSize - 1) == 0 || i == (insertSize - 1)) {
				resource.getContents().addAll(personsList);
				resource.save(options);
				if(personsList.size() > 1){
					assertTrue(resource.getContents().size() == 0);
				} else {
					resource.getContents().clear();
				}
				personsList.clear();
			}
		}
		System.out.println("Insert of " + insertSize  + " persons with batchSize=" + insertBatchSize + " took " + (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, personCollection.countDocuments());
		
		/*
		 * Find person in the collection with skip and limit
		 */
		String query = "{\"filter\":{},\"projection\": {\"firstName\":1,\"lastName\":1}}";
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?"+query));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource.load(resourceSet.getLoadOptions());
		// get the persons
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		
		ECollection resultCollection = (ECollection) findResource.getContents().get(0);
		findResource.unload();
		
		assertEquals(100, resultCollection.getValues().size());
		
		List<Person> personList = resultCollection.getValues().stream().map(eo->(Person)eo).collect(Collectors.toList());
		assertEquals(100, personList.size());
		
		Person p = personList.get(10);
		assertEquals("Mark10", p.getFirstName());
		assertEquals("Hoffmann10", p.getLastName());
		assertFalse(p.eIsSet(BasicPackage.Literals.PERSON__GENDER));
		assertFalse(p.eIsSet(BasicPackage.Literals.PERSON__ID));
		
		
		personCollection.drop();
		
	}
	
	
	/*
	 * Test contact data.
	 */
	@Test
	public void testProjectionContact() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		
		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		
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
		
		assertEquals(0, personCollection.countDocuments());
		/*
		 * Inserting many persons and with containment contacts
		 */
		int insertSize = 100;
		int insertBatchSize = 500;
		
		long start = System.currentTimeMillis();
		List<Person> personsList = new ArrayList<>(insertBatchSize);
		
		System.out.println("Batch inserting: ");
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://"+ mongoHost + ":27017/test/Person/"));
		Map<?,?> options = Collections.singletonMap(Options.OPTION_FORCE_INSERT, Boolean.TRUE);
		for (int i = 0; i < insertSize; i++) {
			Person person = BasicFactory.eINSTANCE.createPerson();
			person.setId(String.valueOf(i));
			person.setFirstName("Mark" + i);
			person.setLastName("Hoffmann" + i);
			person.setGender(GenderType.MALE);
//			person.getContact().add(EcoreUtil.copy(c1));
			BusinessContact bc = BasicFactory.eINSTANCE.createBusinessContact();
			bc.setCompanyName("data in motion");
			person.getContact().add(bc);
//			person.getContact().add(EcoreUtil.copy(c2));
			personsList.add(person);
			// using insert many
			if (i % (insertBatchSize - 1) == 0 || i == (insertSize - 1)) {
				resource.getContents().addAll(personsList);
				resource.save(options);
				if(personsList.size() > 1){
					assertTrue(resource.getContents().size() == 0);
				} else {
					resource.getContents().clear();
				}
				personsList.clear();
			}
		}
		System.out.println("Insert of " + insertSize  + " persons with batchSize=" + insertBatchSize + " took " + (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, personCollection.countDocuments());
		
		
		/*
		 * Find person in the collection with skip and limit, test contact data.
		 */
		String query = "{\"filter\":{},\"projection\": {\"firstName\":1,\"lastName\":1,\"contact.value\":1,\"contact.context\":1,\"contact.type\":1}}";
		Resource findResource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?"+query));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource.load(resourceSet.getLoadOptions());
		// get the persons
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		
		ECollection resultCollection = (ECollection) findResource.getContents().get(0);
		findResource.unload();
		
		assertEquals(100, resultCollection.getValues().size());
		
		List<Person> personList = resultCollection.getValues().stream().map(eo->(Person)eo).collect(Collectors.toList());
		assertEquals(100, personList.size());
		
		Person p = personList.get(10);
		assertEquals("Mark10", p.getFirstName());
		assertEquals("Hoffmann10", p.getLastName());
		assertFalse(p.eIsSet(BasicPackage.Literals.PERSON__GENDER));
		assertFalse(p.eIsSet(BasicPackage.Literals.PERSON__ID));
		List<Contact> contactList = p.getContact().stream().map(contact->(Contact)contact).collect(Collectors.toList());
		assertFalse(contactList.isEmpty());
		assertEquals(1, contactList.size());
		Contact contact1 = contactList.get(0);
		assertTrue(contact1 instanceof BusinessContact);
		
		/*Contact contact2 = contactList.get(1);
		assertEquals("SKYPE", contact1.getType().name());
		assertEquals("EMAIL", contact2.getType().name());
		assertEquals("PRIVATE", contact1.getContext().name());
		assertEquals("WORK", contact2.getContext().name());
		assertEquals("charles-brown", contact1.getValue());
		assertEquals("mark.hoffmann@tests.de", contact2.getValue());*/
		System.out.println("Output: " + contact1.eContents().toString());
		
		personCollection.drop();
		
	}
}
