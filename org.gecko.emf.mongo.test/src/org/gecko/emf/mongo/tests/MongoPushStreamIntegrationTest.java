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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.emf.mongo.InputContentHandler;
import org.gecko.emf.mongo.Options;
import org.gecko.emf.mongo.annotations.RequireMongoEMFPushStreamExtension;
import org.gecko.emf.mongo.handlers.MongoResourceSetConfigurator;
import org.gecko.emf.mongo.pushstream.PushStreamInputContentHandler;
import org.gecko.emf.mongo.pushstream.constants.MongoPushStreamConstants;
import org.gecko.emf.osgi.ResourceSetFactory;
import org.gecko.emf.osgi.configurator.ResourceSetConfigurator;
import org.gecko.emf.osgi.constants.EMFNamespaces;
import org.gecko.emf.osgi.example.model.basic.BasicFactory;
import org.gecko.emf.osgi.example.model.basic.Contact;
import org.gecko.emf.osgi.example.model.basic.ContactContextType;
import org.gecko.emf.osgi.example.model.basic.ContactType;
import org.gecko.emf.osgi.example.model.basic.GenderType;
import org.gecko.emf.osgi.example.model.basic.Person;
import org.gecko.emf.pushstream.EPushStreamProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.component.ComponentConstants;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;
import org.osgi.util.promise.Promise;
import org.osgi.util.pushstream.PushStream;

/**
 * Integration tests for the complete EMF mongo setup
 * 
 * @author Mark Hoffmann
 * @since 26.07.2017
 */
@RequireMongoEMFPushStreamExtension()
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
@WithFactoryConfiguration(name = "mongoClient", location = "?", factoryPid = "MongoClientProvider", properties = {
		@Property(key = "client_id", value = "test"), @Property(key = "uri", value = "mongodb://localhost:27017") })
@WithFactoryConfiguration(name = "mongoDatabase", location = "?", factoryPid = "MongoDatabaseProvider", properties = {
		@Property(key = "alias", value = "TestDB"), @Property(key = "database", value = "test") })
public class MongoPushStreamIntegrationTest extends MongoEMFSetting {
	
	@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME + "=mongo)")
	ServiceAware<ResourceSetConfigurator> configuratorAware;
	@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME + "=mongo)")
	ServiceAware<ResourceSetFactory> rsAware;
	@InjectService(cardinality = 0, filter = "("+ComponentConstants.COMPONENT_NAME+"=PushStreamInputContentHandler)")
	ServiceAware<InputContentHandler> inputContentHandlerAware;

	@BeforeEach
	public void doBefore(@InjectBundleContext BundleContext ctx) {
		super.doBefore(ctx);
	}

	@AfterEach
	public void doAfter() {
		super.doAfter();
	}

	/**
	 * Test creation of many objects and returning results as {@link PushStream}
	 * multithreaded
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	@Test
	public void testCreateAndFindObjectsPushStream_Multithreaded() throws BundleException, InvalidSyntaxException,
			IOException, InterruptedException, InvocationTargetException {

		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		InputContentHandler ps = (InputContentHandler) inputContentHandlerAware.waitForService(2000l);
		assertFalse(inputContentHandlerAware.isEmpty());
		assertTrue(ps instanceof PushStreamInputContentHandler);

		System.out.println("Dropping DB");
		collection = client.getDatabase("test").getCollection("Person");
		collection.drop();

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		Contact c2 = BasicFactory.eINSTANCE.createContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@tests.de");

		assertEquals(0, collection.countDocuments());
		/*
		 * Inserting many persons and with containment contacts
		 */
		int insertSize = 10000;
		int insertBatchSize = 500;

		long start = System.currentTimeMillis();
		List<Person> personsList = new ArrayList<>(insertBatchSize);

		System.out.println("Batch inserting: ");
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/"));
		Map<?, ?> options = Collections.singletonMap(Options.OPTION_FORCE_INSERT, Boolean.TRUE);
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
				if (personsList.size() > 1) {
					assertTrue(resource.getContents().size() == 0);
				} else {
					resource.getContents().clear();
				}
				personsList.clear();
			}
		}
		System.out.println("Insert of " + insertSize + " persons with batchSize=" + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, collection.countDocuments());

		/*
		 * Find person in the collection
		 */
		start = System.currentTimeMillis();
		Resource findResource = resourceSet
				.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		resourceSet.getLoadOptions().put(MongoPushStreamConstants.OPTION_QUERY_PUSHSTREAM, Boolean.TRUE);
		resourceSet.getLoadOptions().put(MongoPushStreamConstants.OPTION_QUERY_PUSHSTREAM_MULTITHREAD, Boolean.TRUE);
		findResource.load(resourceSet.getLoadOptions());
		// get the persons
		System.out.println("Finding all persons with a size " + insertSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		assertTrue(findResource.getContents().get(0) instanceof EPushStreamProvider);

		EPushStreamProvider psp = (EPushStreamProvider) findResource.getContents().get(0);
		PushStream<EObject> ps1 = psp.createPushStreamUnbuffered();
		PushStream<EObject> ps2 = psp.createPushStreamUnbuffered();
		assertNotEquals(ps1, ps2);

		/*
		 * Iterating over the result and getting the real objects
		 */
		start = System.currentTimeMillis();
		List<Person> resultList = new ArrayList<Person>();
		assertEquals(0, resultList.size());
		// iterate over all elements
		Promise<Void> foreachPromise = ps1.forEach((eo) -> {
			resultList.add((Person) eo);
			assertTrue(Thread.currentThread().getName().startsWith("MongoPushEvSrc"));
		});

		foreachPromise.getValue();
		System.out.println("Iterating over all persons and mapping with a batch size " + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, resultList.size());
		Promise<Long> countPromise = ps2.count();
		long count = countPromise.getValue();
		assertEquals(insertSize, count);

		// doing some object checks
		Person p = resultList.get(50);
		assertEquals("Mark50", p.getFirstName());
		assertEquals("Hoffmann50", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		p = resultList.get(25);
		assertEquals("Mark25", p.getFirstName());
		assertEquals("Hoffmann25", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		p = resultList.get(89);
		assertEquals("Mark89", p.getFirstName());
		assertEquals("Hoffmann89", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		collection.drop();
	}

	/**
	 * Test creation of many objects and executing a query that must return 0
	 * results
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	@Test
	public void testCreateAndFindObjectsPushStreamEmptyResultSet() throws BundleException, InvalidSyntaxException,
			IOException, InterruptedException, InvocationTargetException {

		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		assertFalse(inputContentHandlerAware.isEmpty());

		System.out.println("Dropping DB");
		collection = client.getDatabase("test").getCollection("Person");
		collection.drop();

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		Contact c2 = BasicFactory.eINSTANCE.createContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@tests.de");

		assertEquals(0, collection.countDocuments());
		/*
		 * Inserting many persons and with containment contacts
		 */
		int insertSize = 10;
		int insertBatchSize = 500;

		long start = System.currentTimeMillis();
		List<Person> personsList = new ArrayList<>(insertBatchSize);

		System.out.println("Batch inserting: ");
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/"));
		Map<?, ?> options = Collections.singletonMap(Options.OPTION_FORCE_INSERT, Boolean.TRUE);
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
				if (personsList.size() > 1) {
					assertTrue(resource.getContents().size() == 0);
				} else {
					resource.getContents().clear();
				}
				personsList.clear();
			}
		}
		System.out.println("Insert of " + insertSize + " persons with batchSize=" + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, collection.countDocuments());

		/*
		 * Find person in the collection
		 */
		start = System.currentTimeMillis();
		Resource findResource = resourceSet
				.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{ filter : { bla : 1}}"));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		resourceSet.getLoadOptions().put(MongoPushStreamConstants.OPTION_QUERY_PUSHSTREAM, Boolean.TRUE);
		resourceSet.getLoadOptions().put(MongoPushStreamConstants.OPTION_QUERY_PUSHSTREAM_MULTITHREAD, Boolean.TRUE);
		findResource.load(resourceSet.getLoadOptions());
		// get the persons
		System.out.println("Finding all persons with a size " + insertSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		assertTrue(findResource.getContents().get(0) instanceof EPushStreamProvider);

		EPushStreamProvider psp = (EPushStreamProvider) findResource.getContents().get(0);
		PushStream<EObject> ps1 = psp.createPushStreamUnbuffered();
		ps1.onError(t -> assertNull(t));
		Promise<List<EObject>> collect = ps1.collect(Collectors.toList());
		collect = collect.timeout(500);
		List<EObject> value = collect.getValue();

		assertTrue(value.isEmpty());

		collection.drop();
	}

	/**
	 * Test creation of many objects and returning results as {@link PushStream}
	 * multithreaded
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	@Test
	public void testCreateAndFindObjectsPushStream_Detached_MT() throws BundleException, InvalidSyntaxException,
			IOException, InterruptedException, InvocationTargetException {

		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		assertFalse(inputContentHandlerAware.isEmpty());

		System.out.println("Dropping DB");
		collection = client.getDatabase("test").getCollection("Person");
		collection.drop();

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		Contact c2 = BasicFactory.eINSTANCE.createContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@tests.de");

		assertEquals(0, collection.countDocuments());
		/*
		 * Inserting many persons and with containment contacts
		 */
		int insertSize = 10000;
		int insertBatchSize = 500;

		long start = System.currentTimeMillis();
		List<Person> personsList = new ArrayList<>(insertBatchSize);

		System.out.println("Batch inserting: ");
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/"));
		Map<?, ?> options = Collections.singletonMap(Options.OPTION_FORCE_INSERT, Boolean.TRUE);
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
				if (personsList.size() > 1) {
					assertTrue(resource.getContents().size() == 0);
				} else {
					resource.getContents().clear();
				}
				personsList.clear();
			}
		}
		System.out.println("Insert of " + insertSize + " persons with batchSize=" + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, collection.countDocuments());

		/*
		 * Find person in the collection
		 */
		start = System.currentTimeMillis();
		Resource findResource = resourceSet
				.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		resourceSet.getLoadOptions().put(MongoPushStreamConstants.OPTION_QUERY_PUSHSTREAM, Boolean.TRUE);
		resourceSet.getLoadOptions().put(MongoPushStreamConstants.OPTION_QUERY_PUSHSTREAM_MULTITHREAD, Boolean.TRUE);
		resourceSet.getLoadOptions().put(Options.OPTION_READ_DETACHED, Boolean.TRUE);
		findResource.load(resourceSet.getLoadOptions());
		// get the persons
		System.out.println("Finding all persons with a size " + insertSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		assertTrue(findResource.getContents().get(0) instanceof EPushStreamProvider);

		EPushStreamProvider psp = (EPushStreamProvider) findResource.getContents().get(0);
		PushStream<EObject> ps1 = psp.createPushStreamUnbuffered();
		PushStream<EObject> ps2 = psp.createPushStreamUnbuffered();
		assertNotEquals(ps1, ps2);

		/*
		 * Iterating over the result and getting the real objects
		 */
		start = System.currentTimeMillis();
		List<Person> resultList = new ArrayList<Person>();
		assertEquals(0, resultList.size());
		// iterate over all elements
		Promise<Void> foreachPromise = ps1.forEach((eo) -> {
			resultList.add((Person) eo);
			assertTrue(Thread.currentThread().getName().startsWith("MongoPushEvSrc"));
		});

		foreachPromise.getValue();
		System.out.println("Iterating over all persons and mapping with a batch size " + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, resultList.size());
		Promise<Long> countPromise = ps2.count();
		long count = countPromise.getValue();
		assertEquals(insertSize, count);

		// doing some object checks
		Person p = resultList.get(50);
		assertNull(p.eResource());
		assertEquals("Mark50", p.getFirstName());
		assertEquals("Hoffmann50", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		p = resultList.get(25);
		assertNull(p.eResource());
		assertEquals("Mark25", p.getFirstName());
		assertEquals("Hoffmann25", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		p = resultList.get(89);
		assertNull(p.eResource());
		assertEquals("Mark89", p.getFirstName());
		assertEquals("Hoffmann89", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		collection.drop();
	}

	/**
	 * Test creation of many objects and returning results as {@link PushStream}
	 * singlethreaded
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	@Test
	public void testCreateAndFindObjectsPushStream_SingleThreaded() throws BundleException, InvalidSyntaxException,
			IOException, InterruptedException, InvocationTargetException {

		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		assertFalse(inputContentHandlerAware.isEmpty());

		System.out.println("Dropping DB");
		collection = client.getDatabase("test").getCollection("Person");
		collection.drop();

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		Contact c2 = BasicFactory.eINSTANCE.createContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@tests.de");

		assertEquals(0, collection.countDocuments());
		/*
		 * Inserting many persons and with containment contacts
		 */
		int insertSize = 10000;
		int insertBatchSize = 500;

		long start = System.currentTimeMillis();
		List<Person> personsList = new ArrayList<>(insertBatchSize);

		System.out.println("Batch inserting: ");
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/"));
		Map<?, ?> options = Collections.singletonMap(Options.OPTION_FORCE_INSERT, Boolean.TRUE);
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
				if (personsList.size() > 1) {
					assertTrue(resource.getContents().size() == 0);
				} else {
					resource.getContents().clear();
				}
				personsList.clear();
			}
		}
		System.out.println("Insert of " + insertSize + " persons with batchSize=" + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, collection.countDocuments());

		/*
		 * Find person in the collection
		 */
		start = System.currentTimeMillis();
		Resource findResource = resourceSet
				.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		resourceSet.getLoadOptions().put(MongoPushStreamConstants.OPTION_QUERY_PUSHSTREAM, Boolean.TRUE);
		findResource.load(resourceSet.getLoadOptions());
		// get the persons
		System.out.println("Finding all persons with a size " + insertSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		assertTrue(findResource.getContents().get(0) instanceof EPushStreamProvider);

		EPushStreamProvider psp = (EPushStreamProvider) findResource.getContents().get(0);
		PushStream<EObject> ps1 = psp.createPushStreamUnbuffered();
		PushStream<EObject> ps2 = psp.createPushStreamUnbuffered();
		assertNotEquals(ps1, ps2);

		/*
		 * Iterating over the result and getting the real objects
		 */
		start = System.currentTimeMillis();
		List<Person> resultList = new ArrayList<Person>();
		assertEquals(0, resultList.size());
		// iterate over all elements
		ps1.forEach((eo) -> {
			resultList.add((Person) eo);
			assertEquals("main", Thread.currentThread().getName());
		});

		System.out.println("Iterating over all persons and mapping with a batch size " + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, resultList.size());
		Promise<Long> countPromise = ps2.count();
		long count = countPromise.getValue();
		assertEquals(insertSize, count);

		// doing some object checks
		Person p = resultList.get(50);
		assertNotNull(p.eResource());
		assertEquals("Mark50", p.getFirstName());
		assertEquals("Hoffmann50", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		p = resultList.get(25);
		assertNotNull(p.eResource());
		assertEquals("Mark25", p.getFirstName());
		assertEquals("Hoffmann25", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		p = resultList.get(89);
		assertNotNull(p.eResource());
		assertEquals("Mark89", p.getFirstName());
		assertEquals("Hoffmann89", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		collection.drop();
	}

	/**
	 * Test creation of many objects and returning results as {@link PushStream}
	 * multithreaded
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	@Test
	public void testCreateAndFindObjectsPushStream_Detached_ST() throws BundleException, InvalidSyntaxException,
			IOException, InterruptedException, InvocationTargetException {

		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		assertFalse(inputContentHandlerAware.isEmpty());

		System.out.println("Dropping DB");
		collection = client.getDatabase("test").getCollection("Person");
		collection.drop();

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		Contact c2 = BasicFactory.eINSTANCE.createContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@tests.de");

		assertEquals(0, collection.countDocuments());
		/*
		 * Inserting many persons and with containment contacts
		 */
		int insertSize = 10000;
		int insertBatchSize = 500;

		long start = System.currentTimeMillis();
		List<Person> personsList = new ArrayList<>(insertBatchSize);

		System.out.println("Batch inserting: ");
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/"));
		Map<?, ?> options = Collections.singletonMap(Options.OPTION_FORCE_INSERT, Boolean.TRUE);
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
				if (personsList.size() > 1) {
					assertTrue(resource.getContents().size() == 0);
				} else {
					resource.getContents().clear();
				}
				personsList.clear();
			}
		}
		System.out.println("Insert of " + insertSize + " persons with batchSize=" + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, collection.countDocuments());

		/*
		 * Find person in the collection
		 */
		start = System.currentTimeMillis();
		Resource findResource = resourceSet
				.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/?{}"));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		resourceSet.getLoadOptions().put(MongoPushStreamConstants.OPTION_QUERY_PUSHSTREAM, Boolean.TRUE);
		resourceSet.getLoadOptions().put(Options.OPTION_READ_DETACHED, Boolean.TRUE);
		findResource.load(resourceSet.getLoadOptions());
		// get the persons
		System.out.println("Finding all persons with a size " + insertSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		assertTrue(findResource.getContents().get(0) instanceof EPushStreamProvider);

		EPushStreamProvider psp = (EPushStreamProvider) findResource.getContents().get(0);
		PushStream<EObject> ps1 = psp.createPushStreamUnbuffered();
		PushStream<EObject> ps2 = psp.createPushStreamUnbuffered();
		assertNotEquals(ps1, ps2);

		/*
		 * Iterating over the result and getting the real objects
		 */
		start = System.currentTimeMillis();
		List<Person> resultList = new ArrayList<Person>();
		assertEquals(0, resultList.size());
		// iterate over all elements
		ps1.forEach((eo) -> {
			resultList.add((Person) eo);
			assertEquals("main", Thread.currentThread().getName());
		});

		System.out.println("Iterating over all persons and mapping with a batch size " + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, resultList.size());
		Promise<Long> countPromise = ps2.count();
		long count = countPromise.getValue();
		assertEquals(insertSize, count);

		// doing some object checks
		Person p = resultList.get(50);
		assertNull(p.eResource());
		assertEquals("Mark50", p.getFirstName());
		assertEquals("Hoffmann50", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		p = resultList.get(25);
		assertNull(p.eResource());
		assertEquals("Mark25", p.getFirstName());
		assertEquals("Hoffmann25", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		p = resultList.get(89);
		assertNull(p.eResource());
		assertEquals("Mark89", p.getFirstName());
		assertEquals("Hoffmann89", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		collection.drop();
	}

}
