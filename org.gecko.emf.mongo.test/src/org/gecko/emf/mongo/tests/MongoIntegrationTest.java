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
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.collection.CollectionPackage;
import org.gecko.collection.ECollection;
import org.gecko.collection.EReferenceCollection;
import org.gecko.emf.mongo.Options;
import org.gecko.emf.mongo.codecs.EObjectCodecProvider;
import org.gecko.emf.mongo.converter.DefaultConverterService;
import org.gecko.emf.mongo.handlers.MongoResourceSetConfigurator;
import org.gecko.emf.osgi.ResourceSetFactory;
import org.gecko.emf.osgi.annotation.require.RequireEMF;
import org.gecko.emf.osgi.configurator.ResourceSetConfigurator;
import org.gecko.emf.osgi.constants.EMFNamespaces;
import org.gecko.emf.osgi.example.model.basic.Address;
import org.gecko.emf.osgi.example.model.basic.BasicFactory;
import org.gecko.emf.osgi.example.model.basic.Contact;
import org.gecko.emf.osgi.example.model.basic.ContactContextType;
import org.gecko.emf.osgi.example.model.basic.ContactType;
import org.gecko.emf.osgi.example.model.basic.GenderType;
import org.gecko.emf.osgi.example.model.basic.Person;
import org.gecko.mongo.osgi.MongoClientProvider;
import org.gecko.mongo.osgi.MongoDatabaseProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

/**
 * Integration tests for the complete EMF mongo setup
 * 
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
public class MongoIntegrationTest extends MongoEMFSetting {

	private static final String FIND_PERSON = "mongodb://localhost:27017/test/Person/?{}";

//	@InjectService
//	public ConfigurationAdmin configAdmin;

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

	@Test
	public void testEMFMongo() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {

		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		assertTrue(rsf instanceof ResourceSetFactory);
	}

	@Test
	public void testEMFMongoMany(
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetConfigurator> configuratorAware,
			@InjectService(cardinality = 0, filter = "(&(" + EMFNamespaces.EMF_CONFIGURATOR_NAME + "=mongo)("
					+ EMFNamespaces.EMF_CONFIGURATOR_NAME + "=testDB))") ServiceAware<ResourceSetFactory> rsAware,
			@InjectService(cardinality = 0, filter = "(&(" + EMFNamespaces.EMF_CONFIGURATOR_NAME + "=mongo)("
					+ EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=test.myDB))") ServiceAware<ResourceSetFactory> rsMyDBAware)
			throws BundleException, InvalidSyntaxException, IOException, InterruptedException {

		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		String db2 = "myDB";
		String dbAlias2 = "myDB";
		// add service properties
		Dictionary<String, Object> dbp = new Hashtable<String, Object>();
		dbp.put(MongoDatabaseProvider.PROP_ALIAS, dbAlias2);
		dbp.put(MongoDatabaseProvider.PROP_DATABASE, db2);
		dbp.put(MongoDatabaseProvider.PROP_DATABASE_IDENTIFIER, "test." + db2);
//		Configuration databaseConfig = createConfigForCleanup(ConfigurationProperties.DATABASE_PID, "?", dbp);
//
//		ServiceChecker<?> dbchecker = getServiceCheckerForConfiguration(databaseConfig).assertCreations(1, true)
//				.assertRemovals(0, false).trackedServiceNotNull();
//
//		checker.assertCreations(1, true).assertRemovals(0, false).trackedServiceNotNull();
//
//		Collection<ServiceReference<MongoDatabaseProvider>> serviceReferences = getBundleContext()
//				.getServiceReferences(MongoDatabaseProvider.class, null);
//		assertEquals(2, serviceReferences.size());
//
//		deleteConfigurationAndRemoveFromCleanup(databaseConfig);
//
//		dbchecker.assertRemovals(1, true);
//		checker.assertCreations(1, false).assertRemovals(1, true);
	}

	/**
	 * Test creation of object and returning results
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 */
	@Test
	public void testCreateId()
			throws BundleException, InvalidSyntaxException, IOException, InterruptedException {

		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		System.out.println("Dropping DB");
		MongoCollection<Document> personCollection = client.getDatabase("test").getCollection("Person");
		personCollection.drop();

		assertEquals(0, personCollection.countDocuments());
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://localhost:27017/test/Person/"));

		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann");
		person.setGender(GenderType.MALE);
		assertNull(person.getId());
		resource.getContents().add(person);
		resource.save(null);

		resource.getContents().clear();
		resource.unload();
		/*
		 * Find person in the collection
		 */
		// long start = System.currentTimeMillis();
		Resource findResource = resourceSet
				.createResource(URI.createURI("mongodb://localhost:27017/test/Person/" + person.getId()));
		findResource.load(null);

		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());

		// doing some object checks
		Person p = (Person) findResource.getContents().get(0);
		assertEquals("Mark", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertNotNull(p.getId());

		assertEquals(1, personCollection.countDocuments());
		FindIterable<Document> docIterable = personCollection.find();
		Document first = docIterable.first();
		Object idField = first.get("_id");
		assertTrue(idField instanceof ObjectId);

		personCollection.drop();
	}

	/**
	 * Test creation of object and returning results
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 */
	@Test
	public void testBigIntegerConverter()
			throws BundleException, InvalidSyntaxException, IOException, InterruptedException {

		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		System.out.println("Dropping DB");
		MongoCollection<Document> personCollection = client.getDatabase("test").getCollection("Person");
		personCollection.drop();

		assertEquals(0, personCollection.countDocuments());
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://localhost:27017/test/Person/"));

		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann");
		person.setGender(GenderType.MALE);
		person.setBigInt(new BigInteger("12"));
		assertNull(person.getId());
		resource.getContents().add(person);
		resource.save(null);

		resource.getContents().clear();
		resource.unload();
		/*
		 * Find person in the collection
		 */
		// long start = System.currentTimeMillis();
		Resource findResource = resourceSet
				.createResource(URI.createURI("mongodb://localhost:27017/test/Person/" + person.getId()));
		findResource.load(null);

		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());

		// doing some object checks
		Person p = (Person) findResource.getContents().get(0);
		assertEquals("Mark", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertNotNull(p.getId());
		assertNotNull(p.getBigInt());
		assertEquals(12, p.getBigInt().intValue());

		personCollection.drop();
	}

	/**
	 * Test creation of object and returning results
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 */
	@Test
	public void testByteArrayConverter()
			throws BundleException, InvalidSyntaxException, IOException, InterruptedException {

		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		System.out.println("Dropping DB");
		MongoCollection<Document> personCollection = client.getDatabase("test").getCollection("Person");
		personCollection.drop();

		assertEquals(0, personCollection.countDocuments());
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://localhost:27017/test/Person/"));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0);
		baos.write(8);
		baos.write(1);
		baos.write(5);
		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann");
		person.setGender(GenderType.MALE);
		person.setImage(baos.toByteArray());
		assertNull(person.getId());
		resource.getContents().add(person);
		resource.save(null);

		resource.getContents().clear();
		resource.unload();
		/*
		 * Find person in the collection
		 */
		// long start = System.currentTimeMillis();
		Resource findResource = resourceSet
				.createResource(URI.createURI("mongodb://localhost:27017/test/Person/" + person.getId()));
		findResource.load(null);

		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());

		// doing some object checks
		Person p = (Person) findResource.getContents().get(0);
		assertEquals("Mark", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertNotNull(p.getId());
		assertNotNull(p.getImage());
		byte[] image = p.getImage();
		assertEquals(4, image.length);
		ByteArrayInputStream bais = new ByteArrayInputStream(image);
		assertEquals(0, bais.read());
		assertEquals(8, bais.read());
		assertEquals(1, bais.read());
		assertEquals(5, bais.read());

		personCollection.drop();
	}

	/**
	 * Test creation of object and returning results
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 */
	@Test
	public void testBigDecimalConverter()
			throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		System.out.println("Dropping DB");
		MongoCollection<Document> personCollection = client.getDatabase("test").getCollection("Person");
		personCollection.drop();

		assertEquals(0, personCollection.countDocuments());
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://localhost:27017/test/Person/"));

		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann");
		person.setGender(GenderType.MALE);
		person.getBigDec().add(new BigDecimal("12.3"));
		person.getBigDec().add(new BigDecimal("45.6"));
		assertNull(person.getId());
		resource.getContents().add(person);
		resource.save(null);

		resource.getContents().clear();
		resource.unload();
		/*
		 * Find person in the collection
		 */
		// long start = System.currentTimeMillis();
		Resource findResource = resourceSet
				.createResource(URI.createURI("mongodb://localhost:27017/test/Person/" + person.getId()));
		findResource.load(null);

		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());

		// doing some object checks
		Person p = (Person) findResource.getContents().get(0);
		assertEquals("Mark", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertNotNull(p.getId());
		assertEquals(2, p.getBigDec().size());
		assertTrue(p.getBigDec().get(0) instanceof BigDecimal);
		assertTrue(p.getBigDec().get(1) instanceof BigDecimal);
		assertEquals("12.3", p.getBigDec().get(0).toString());
		assertEquals("45.6", p.getBigDec().get(1).toString());

		personCollection.drop();
	}

	/**
	 * Test creation of object and returning results
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 */
	@Test
	public void testCreateContainmentSingle()
			throws BundleException, InvalidSyntaxException, IOException, InterruptedException {

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
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://localhost:27017/test/Person/"));

		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann");
		person.setGender(GenderType.MALE);
		person.getContact().add(EcoreUtil.copy(c1));
		person.getContact().add(EcoreUtil.copy(c2));
		resource.getContents().add(person);
		resource.save(null);

		resource.getContents().clear();
		resource.unload();
		/*
		 * Find person in the collection
		 */
		// long start = System.currentTimeMillis();
		Resource findResource = resourceSet
				.createResource(URI.createURI("mongodb://localhost:27017/test/Person/" + person.getId()));
		findResource.load(null);

		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());

		// doing some object checks
		Person p = (Person) findResource.getContents().get(0);
		assertEquals("Mark", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		personCollection.drop();
	}

	@Test
	public void testBla(@InjectService(cardinality = 0) ServiceAware<MongoClientProvider> saClient,
			@InjectService(cardinality = 0) ServiceAware<MongoDatabaseProvider> saDatabase,
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetConfigurator> configuratorAware,
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetFactory> rsAware)
			throws InterruptedException {
		System.out.println(saClient.getServices().size());
		System.out.println(saDatabase.getServices().size());
		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		System.out.println(configuratorAware.getServices().size() + " " + rsc);
		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		System.out.println(rsAware.getServices().size() + " " + rsf);
		fail();
	}

	/**
	 * Test creation of object and returning results as well as updating
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 */
	@Test
	public void testCreateAndUpdateContainmentSingle()
			throws BundleException, InvalidSyntaxException, IOException, InterruptedException {

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
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://localhost:27017/test/Person/"));

		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann");
		person.setGender(GenderType.MALE);
		person.getContact().add(EcoreUtil.copy(c1));
		person.getContact().add(EcoreUtil.copy(c2));
		resource.getContents().add(person);
		resource.save(null);

		resource.getContents().clear();
		resource.unload();
		/*
		 * Find person in the collection
		 */
		// long start = System.currentTimeMillis();
		Resource findResource = resourceSet
				.createResource(URI.createURI("mongodb://localhost:27017/test/Person/" + person.getId()));
		findResource.load(null);

		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());

		// doing some object checks
		Person p = (Person) findResource.getContents().get(0);
		assertEquals("Mark", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		findResource.unload();

		person.setFirstName("Mark2");

		resource.getContents().add(person);
		resource.save(null);

		resource.getContents().clear();
		resource.unload();
		/*
		 * Find person in the collection
		 */
		// start = System.currentTimeMillis();
		findResource = resourceSet
				.createResource(URI.createURI("mongodb://localhost:27017/test/Person/" + person.getId()));
		findResource.load(null);

		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());

		// doing some object checks
		p = (Person) findResource.getContents().get(0);
		assertEquals("Mark2", p.getFirstName());
		assertEquals("Hoffmann", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		personCollection.drop();
	}

	/**
	 * Test creation of object and removing
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 */
	@Test
	public void testCreateAndRemoveSingle(
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetConfigurator> configuratorAware,
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetFactory> rsAware)
			throws BundleException, InvalidSyntaxException, IOException, InterruptedException {

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
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://localhost:27017/test/Person/"));

		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setFirstName("Mark");
		person.setLastName("Hoffmann");
		person.setGender(GenderType.MALE);
		person.getContact().add(EcoreUtil.copy(c1));
		person.getContact().add(EcoreUtil.copy(c2));
		resource.getContents().add(person);
		resource.save(null);

		resource.getContents().clear();
		resource.unload();
		/*
		 * Find person in the collection
		 */
		// long start = System.currentTimeMillis();
		Resource findResource = resourceSet
				.createResource(URI.createURI("mongodb://localhost:27017/test/Person/" + person.getId()));
		findResource.load(null);
		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());

		Resource removeResource = resourceSet
				.createResource(URI.createURI("mongodb://localhost:27017/test/Person/" + person.getId()));
		removeResource.delete(null);

		findResource.unload();
		findResource.load(null);
		// no person anymore
		assertNotNull(findResource);
		assertTrue(findResource.getContents().isEmpty());

		personCollection.drop();
	}

	/**
	 * Test creation of object and removing
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 */
	@Test
	public void testCreateAndRemoveMany(
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetConfigurator> configuratorAware,
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetFactory> rsAware)
			throws BundleException, InvalidSyntaxException, IOException, InterruptedException {

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
		Resource resource01 = resourceSet.createResource(URI.createURI("mongodb://localhost:27017/test/Person/"));

		Person person01 = BasicFactory.eINSTANCE.createPerson();
		person01.setFirstName("Mark");
		person01.setLastName("Hoffmann");
		person01.setGender(GenderType.MALE);
		person01.getContact().add(EcoreUtil.copy(c1));
		person01.getContact().add(EcoreUtil.copy(c2));
		resource01.getContents().add(person01);
		resource01.save(null);

		resource01.getContents().clear();
		resource01.unload();

		Resource resource02 = resourceSet.createResource(URI.createURI("mongodb://localhost:27017/test/Person/"));

		Person person02 = BasicFactory.eINSTANCE.createPerson();
		person02.setFirstName("Jürgen");
		person02.setLastName("Albert");
		resource02.getContents().add(person02);
		resource02.save(null);

		resource02.getContents().clear();
		resource02.unload();

		Resource resource03 = resourceSet.createResource(URI.createURI("mongodb://localhost:27017/test/Person/"));

		Person person03 = BasicFactory.eINSTANCE.createPerson();
		person03.setFirstName("Emil");
		person03.setLastName("Tester");
		resource03.getContents().add(person03);
		resource03.save(null);

		resource03.getContents().clear();
		resource03.unload();
		assertEquals(3, personCollection.countDocuments());
		/*
		 * Find person in the collection
		 */
		// long start = System.currentTimeMillis();
		Resource findResource = resourceSet.createResource(URI.createURI(FIND_PERSON));
		findResource.load(null);
		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		EReferenceCollection rc = (EReferenceCollection) findResource.getContents().get(0);
		assertEquals(3, rc.getValues().size());

		String query = "{\"filter\":{$or:[{\"lastName\":\"Albert\"},{\"firstName\":\"Mark\"}]}}";
		Resource removeResource = resourceSet
				.createResource(URI.createURI("mongodb://localhost:27017/test/Person/?" + query));
		removeResource.delete(null);

		findResource.unload();
		findResource.load(null);
		// no person anymore
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		rc = (EReferenceCollection) findResource.getContents().get(0);
		assertEquals(1, rc.getValues().size());

		Person p = (Person) rc.getValues().get(0);
		assertEquals("Emil", p.getFirstName());
		assertEquals("Tester", p.getLastName());
		personCollection.drop();
	}

	/**
	 * Test creation of many objects and returning results
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 */
	@Test
	public void testCreateAndFindObjects_ContainmentMany(
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetConfigurator> configuratorAware,
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetFactory> rsAware)
			throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
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
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://localhost:27017/test/Person/"));
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
		assertEquals(insertSize, personCollection.countDocuments());

		/*
		 * Find person in the collection
		 */
		start = System.currentTimeMillis();
		Resource findResource = resourceSet.createResource(URI.createURI(FIND_PERSON));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource.load(resourceSet.getLoadOptions());
		// get the persons
		System.out.println("Finding all persons with a size " + insertSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
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
		System.out.println("Iterating over all persons and mapping with a batch size " + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
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
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 */
	@Test
	public void testCreateAndFindObjects_ContainmentManyDetached(
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetConfigurator> configuratorAware,
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetFactory> rsAware)
			throws BundleException, InvalidSyntaxException, IOException, InterruptedException {

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
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://localhost:27017/test/Person/"));
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
		assertEquals(insertSize, personCollection.countDocuments());

		/*
		 * Find person in the collection
		 */
		start = System.currentTimeMillis();
		Resource findResource = resourceSet.createResource(URI.createURI(FIND_PERSON));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		resourceSet.getLoadOptions().put(Options.OPTION_READ_DETACHED, Boolean.TRUE);
		findResource.load(resourceSet.getLoadOptions());
		// get the persons
		System.out.println("Finding all persons with a size " + insertSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
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
		System.out.println("Iterating over all persons and mapping with a batch size " + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, resultList.size());

		// doing some object checks
		Person p = resultList.get(500);
		assertNull(p.eResource());
		assertEquals("Mark500", p.getFirstName());
		assertEquals("Hoffmann500", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		p = resultList.get(2500);
		assertNull(p.eResource());
		assertEquals("Mark2500", p.getFirstName());
		assertEquals("Hoffmann2500", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		p = resultList.get(8999);
		assertNull(p.eResource());
		assertEquals("Mark8999", p.getFirstName());
		assertEquals("Hoffmann8999", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		personCollection.drop();
	}

	/**
	 * Test creation of objects and returning results. It uses partitioning of a
	 * certain collection. Partitioning is handles using the
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 */
	@Test
	public void testCreateAndFindObjects_CollectionPartitioning(
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetConfigurator> configuratorAware,
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetFactory> rsAware)
			throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		System.out.println("Dropping DB");
		MongoCollection<Document> personCollection = client.getDatabase("test").getCollection("Person");
		personCollection.drop();
		String collectionExtension = "test";
		MongoCollection<Document> personExtCollection = client.getDatabase("test")
				.getCollection("Person" + "_" + collectionExtension);
		personExtCollection.drop();

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
		assertEquals(0, personExtCollection.countDocuments());
		/*
		 * Inserting many persons and with containment contacts
		 */
		int insertSize = 10000;
		int insertBatchSize = 500;

		long start = System.currentTimeMillis();
		List<Person> personsList = new ArrayList<>(insertBatchSize);

		System.out.println("Batch inserting: ");
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://localhost:27017/test/Person/"));
		Map<String, Object> options = new HashMap<>();
		options.put(Options.OPTION_FORCE_INSERT, Boolean.TRUE);
		int extensionCollectionCount = 0;
		boolean useCollectionExtension = false;
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
				if (useCollectionExtension) {
					options.put(Options.OPTIONS_COLLECTION_PARTITION_EXTENSION, collectionExtension);
					extensionCollectionCount += personsList.size();
				} else {
					options.remove(Options.OPTIONS_COLLECTION_PARTITION_EXTENSION);
				}
				resource.getContents().addAll(personsList);
				resource.save(options);
				if (personsList.size() > 1) {
					assertTrue(resource.getContents().size() == 0);
				} else {
					resource.getContents().clear();
				}
				personsList.clear();
				useCollectionExtension = !useCollectionExtension;
			}
		}
		System.out.println("Insert of " + insertSize + " persons with batchSize=" + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize - extensionCollectionCount, personCollection.countDocuments());
		assertEquals(extensionCollectionCount, personExtCollection.countDocuments());

		/*
		 * Find person in the collection
		 */
		start = System.currentTimeMillis();
		Resource findResource01 = resourceSet.createResource(URI.createURI(FIND_PERSON));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource01.load(null);
		Resource findResource02 = resourceSet.createResource(URI.createURI(FIND_PERSON));
		Map<String, Object> loadOptions = new HashMap<>();
		loadOptions.put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		loadOptions.put(Options.OPTIONS_COLLECTION_PARTITION_EXTENSION, collectionExtension);
		findResource02.load(loadOptions);
		// get the persons
		System.out.println("Finding all persons with a size " + insertSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertNotNull(findResource01);
		assertFalse(findResource01.getContents().isEmpty());
		assertEquals(1, findResource01.getContents().size());
		assertNotNull(findResource02);
		assertFalse(findResource02.getContents().isEmpty());
		assertEquals(1, findResource02.getContents().size());

		ECollection resultCollection = (ECollection) findResource01.getContents().get(0);
		ECollection resultExtCollection = (ECollection) findResource02.getContents().get(0);

		/*
		 * Iterating over the result and getting the real objects
		 */
		start = System.currentTimeMillis();
		List<Person> resultList = new ArrayList<Person>();
		assertEquals(0, resultList.size());
		// iterate over all elements
		System.out.println("Result " + resultCollection.getValues().size());
		for (EObject object : resultCollection.getValues()) {
			Person person = (Person) object;
			resultList.add(person);
		}
		System.out.println("Result Ext " + resultExtCollection.getValues().size());
		for (EObject object : resultExtCollection.getValues()) {
			Person person = (Person) object;
			resultList.add(person);
		}
		System.out.println("Iterating over all persons and mapping with a batch size " + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, resultList.size());

		// doing some object checks
		Person p = resultList.get(500);
		assertEquals("Mark1498", p.getFirstName());
		assertEquals("Hoffmann1498", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		p = resultList.get(2500);
		assertEquals("Mark5494", p.getFirstName());
		assertEquals("Hoffmann5494", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		p = resultList.get(8999);
		assertEquals("Mark8001", p.getFirstName());
		assertEquals("Hoffmann8001", p.getLastName());
		assertEquals(GenderType.MALE, p.getGender());
		assertEquals(2, p.getContact().size());
		assertEquals("charles-brown", p.getContact().get(0).getValue());
		assertEquals("mark.hoffmann@tests.de", p.getContact().get(1).getValue());

		personCollection.drop();
		personExtCollection.drop();
	}

	/**
	 * Test creation of objects and returning results.
	 * 
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 */
	@Test
	public void testCreateAndFindAndUpdateAndFindObjects_ContainmentMany(
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetConfigurator> configuratorAware,
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetFactory> rsAware)
			throws BundleException, InvalidSyntaxException, IOException, InterruptedException {

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
		Resource resource = resourceSet.createResource(URI.createURI("mongodb://localhost:27017/test/Person/"));
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
				personsList.clear();
				resource.getContents().clear();
			}
		}
		System.out.println("Insert of " + insertSize + " persons with batchSize=" + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, personCollection.countDocuments());

		/*
		 * Find person in the collection
		 */
		start = System.currentTimeMillis();
		CollectionPackage.eINSTANCE.eClass();
		Resource findResource = resourceSet.createResource(URI.createURI(FIND_PERSON));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource.load(null);
		// get the persons
		System.out.println("Finding all persons with a size " + insertSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
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
		System.out.println("Iterating over all persons and mapping with a batch size " + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, resultList.size());

		// doing some object checks
		Person p = resultList.get(500);
		assertEquals("Mark500", p.getFirstName());
		assertEquals("Hoffmann500", p.getLastName());
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

		resourceSet = rsf.createResourceSet();

		start = System.currentTimeMillis();

		System.out.println("Batch inserting: ");
		resource = resourceSet.createResource(URI.createURI("mongodb://localhost:27017/test/Person/"));
		int i = 0;
		personsList.clear();
		for (Person person : resultList) {
			Resource eResource = person.eResource();
			eResource.getContents().clear();
			eResource.unload();
			person.setFirstName(person.getFirstName() + "2");
			personsList.add(person);
			// using insert many
			if (i % (insertBatchSize - 1) == 0 || i == (insertSize - 1)) {
				resource.getContents().addAll(personsList);
				resource.save(null);
				personsList.clear();
				resource.getContents().clear();
			}
			i++;
		}
		System.out.println("Insert of " + insertSize + " persons with batchSize=" + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, personCollection.countDocuments());

		/*
		 * Find person in the collection
		 */
		start = System.currentTimeMillis();
		findResource = resourceSet.createResource(URI.createURI(FIND_PERSON));
		resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, Integer.valueOf(insertBatchSize));
		findResource.load(null);
		// get the persons
		System.out.println("Finding all persons with a size " + insertSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
		assertNotNull(findResource);
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
		System.out.println("Iterating over all persons and mapping with a batch size " + insertBatchSize + " took "
				+ (System.currentTimeMillis() - start) + " ms");
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

	/**
	 * This test is made to check the correct re-implementation of the retrieval of
	 * non-contained reference. In case we had in the DB a contained reference, but
	 * our model has changed in the meanwhile and the same reference is now
	 * non-contained, we would need to still be able to load the object and re-save
	 * it correctly accordingly to the new model. This was previously not possible
	 * because if the model has the non contained reference it would expect a proxy
	 * to be found there, and if this was not the case, it would not load the
	 * object.
	 * 
	 * @throws InvalidSyntaxException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testRetrivalContainedRefInNonContainedModel(
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetConfigurator> configuratorAware,
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetFactory> rsAware)
			throws IOException, InvalidSyntaxException, InterruptedException {

		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();
		Resource findResource = resourceSet.createResource(URI.createURI(FIND_PERSON));

		// get collections and clear it
		MongoCollection<Document> personDocCollection = client.getDatabase("test").getCollection("Person");
		personDocCollection.drop();

		Document doc = new Document();
		doc.put("_id", "test");
		doc.put("_eClass", "http://gecko.org/example/model/basic#//Person");
		doc.put("firstName", "Tester");

		Document addDoc = new Document();
		addDoc.put("_eClass", "http://gecko.org/example/model/basic#//Address");
		addDoc.put("id", "test_address");
		addDoc.put("city", "Jena");
		addDoc.put("street", "Otto-Devrient-Str");
		addDoc.put("zip", "07743");
		doc.put("address", addDoc);

		assertEquals(0, personDocCollection.countDocuments());
		personDocCollection.insertOne(doc);
		assertEquals(1, personDocCollection.countDocuments());

		findResource.load(null);
		// get the persons
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());

		ECollection resultCollection = (ECollection) findResource.getContents().get(0);
		assertFalse(resultCollection.getValues().isEmpty());
		assertEquals(1, resultCollection.getValues().size());

		EObject object = resultCollection.getValues().get(0);
		assertTrue(object instanceof Person);

		Person person = (Person) object;
		assertEquals("Tester", person.getFirstName());
		assertNotNull(person.getAddress());

		Address add = person.getAddress();
		assertEquals("Jena", add.getCity());
		assertEquals("07743", add.getZip());
		assertEquals("test_address", add.getId());
		assertEquals("Otto-Devrient-Str", add.getStreet());

		personDocCollection.drop();
	}

	/**
	 * This test is made to check the correct re-implementation of the retrieval of
	 * non-contained reference. In case we had in the DB a contained reference, but
	 * our model has changed in the meanwhile and the same reference is now
	 * non-contained, we would need to still be able to load the object and re-save
	 * it correctly accordingly to the new model. This was previously not possible
	 * because if the model has the non contained reference it would expect a proxy
	 * to be found there, and if this was not the case, it would not load the
	 * object.
	 * 
	 * @throws InvalidSyntaxException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void test2RetrivalContainedRefInNonContainedModel(
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetConfigurator> configuratorAware,
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetFactory> rsAware)
			throws IOException, InvalidSyntaxException, InterruptedException {

		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();
		Resource findResource = resourceSet.createResource(URI.createURI(FIND_PERSON));

		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(findResource, options, null);

		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);

		// get collections and clear it
		MongoCollection<Document> personDocCollection = client.getDatabase("test").getCollection("Person");
		MongoCollection<Person> personCollection = client.getDatabase("test").getCollection("Person", Person.class)
				.withCodecRegistry(codecRegistry);
		personDocCollection.drop();
		personCollection.drop();

		Document doc = new Document();
		doc.put("_id", "test");
		doc.put("_eClass", "http://gecko.org/example/model/basic#//Person");
		doc.put("id", "test");
		doc.put("firstName", "Tester");

		Person relative = BasicFactory.eINSTANCE.createPerson();
		relative.setId("test_relative");
		relative.setFirstName("Relative");
		personCollection.insertOne(relative);

		Document addDoc = new Document();
		addDoc.put("_eClass", "http://gecko.org/example/model/basic#//Address");
		addDoc.put("id", "test_address");
		addDoc.put("city", "Jena");
		addDoc.put("street", "Otto-Devrient-Str");
		addDoc.put("zip", "07743");
		doc.put("address", addDoc);

		Document relDoc = new Document();
		relDoc.put("_eProxyURI", "test_relative#test_relative");
		List<Document> relatives = new ArrayList<>();
		relatives.add(relDoc);
		doc.put("relatives", relatives);

		assertEquals(1, personDocCollection.countDocuments());
		personDocCollection.insertOne(doc);
		assertEquals(2, personDocCollection.countDocuments());

		findResource.load(null);
		// get the persons
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());

		ECollection resultCollection = (ECollection) findResource.getContents().get(0);
		assertFalse(resultCollection.getValues().isEmpty());
		assertEquals(2, resultCollection.getValues().size());

		Person person1 = null;
		Person person2 = null;
		for (EObject obj : resultCollection.getValues()) {
			assertTrue(obj instanceof Person);
			if ("test".equals(((Person) obj).getId())) {
				person1 = (Person) obj;
			}
			if ("test_relative".equals(((Person) obj).getId())) {
				person2 = (Person) obj;
			}
		}
		assertNotNull(person1);
		assertNotNull(person2);

		assertEquals("Tester", person1.getFirstName());
		assertNotNull(person1.getAddress());

		Address add = person1.getAddress();
		assertEquals("Jena", add.getCity());
		assertEquals("07743", add.getZip());
		assertEquals("test_address", add.getId());
		assertEquals("Otto-Devrient-Str", add.getStreet());

		assertFalse(person1.getRelatives().isEmpty());
		assertEquals(1, person1.getRelatives().size());
		Person rel = person1.getRelatives().get(0);

		assertEquals(person2.getId(), rel.getId());
		personDocCollection.drop();
	}

	/**
	 * This test is made to check the correct re-implementation of the retrieval of
	 * non-contained reference. In case we had in the DB a contained reference, but
	 * our model has changed in the meanwhile and the same reference is now
	 * non-contained, we would need to still be able to load the object and re-save
	 * it correctly accordingly to the new model. This was previously not possible
	 * because if the model has the non contained reference it would expect a proxy
	 * to be found there, and if this was not the case, it would not load the
	 * object.
	 * 
	 * Address without _eClass attribute
	 * 
	 * @throws InvalidSyntaxException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void test3RetrivalContainedRefInNonContainedModel(
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetConfigurator> configuratorAware,
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetFactory> rsAware)
			throws IOException, InvalidSyntaxException, InterruptedException {

		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();
		Resource findResource = resourceSet.createResource(URI.createURI(FIND_PERSON));

		// get collections and clear it
		MongoCollection<Document> personDocCollection = client.getDatabase("test").getCollection("Person");
		personDocCollection.drop();

		Document doc = new Document();
		doc.put("_id", "test");
		doc.put("_eClass", "http://gecko.org/example/model/basic#//Person");
		doc.put("firstName", "Tester");

		Document addDoc = new Document();
		addDoc.put("id", "test_address");
		addDoc.put("city", "Jena");
		addDoc.put("street", "Otto-Devrient-Str");
		addDoc.put("zip", "07743");
		doc.put("address", addDoc);

		assertEquals(0, personDocCollection.countDocuments());
		personDocCollection.insertOne(doc);
		assertEquals(1, personDocCollection.countDocuments());

		findResource.load(Collections.emptyMap());
		// get the persons
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());

		ECollection resultCollection = (ECollection) findResource.getContents().get(0);
		assertFalse(resultCollection.getValues().isEmpty());
		assertEquals(1, resultCollection.getValues().size());

		EObject object = resultCollection.getValues().get(0);
		assertTrue(object instanceof Person);

		Person person = (Person) object;
		assertEquals("Tester", person.getFirstName());
		assertNotNull(person.getAddress());

		Address add = person.getAddress();
		assertEquals("Jena", add.getCity());
		assertEquals("07743", add.getZip());
		assertEquals("test_address", add.getId());
		assertEquals("Otto-Devrient-Str", add.getStreet());

		personDocCollection.drop();
	}

	/**
	 * This test is made to check the correct re-implementation of the retrieval of
	 * non-contained reference. In case we had in the DB a contained reference, but
	 * our model has changed in the meanwhile and the same reference is now
	 * non-contained, we would need to still be able to load the object and re-save
	 * it correctly accordingly to the new model. This was previously not possible
	 * because if the model has the non contained reference it would expect a proxy
	 * to be found there, and if this was not the case, it would not load the
	 * object.
	 * 
	 * Address with _eProxy attribute but also some fields
	 * 
	 * @throws InvalidSyntaxException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void test4RetrivalContainedRefInNonContainedModel(
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetConfigurator> configuratorAware,
			@InjectService(cardinality = 0, filter = "(" + EMFNamespaces.EMF_CONFIGURATOR_NAME
					+ "=mongo)") ServiceAware<ResourceSetFactory> rsAware)
			throws IOException, InvalidSyntaxException, InterruptedException {

		ResourceSetConfigurator rsc = (ResourceSetConfigurator) configuratorAware.waitForService(2000l);
		assertFalse(configuratorAware.isEmpty());
		assertTrue(rsc instanceof MongoResourceSetConfigurator);

		ResourceSetFactory rsf = (ResourceSetFactory) rsAware.waitForService(2000l);
		assertFalse(rsAware.isEmpty());
		ResourceSet resourceSet = rsf.createResourceSet();

		// get collections and clear it
		MongoCollection<Document> personDocCollection = client.getDatabase("test").getCollection("Person");
		personDocCollection.drop();

		Document doc = new Document();
		doc.put("_id", "test");
		doc.put("_eClass", "http://gecko.org/example/model/basic#//Person");
		doc.put("firstName", "Tester");

		Document addDoc = new Document();
		addDoc.put("_eProxyURI", "#test_address");
		addDoc.put("_eClass", "http://gecko.org/example/model/basic#//Address");
		addDoc.put("id", "test_address");
		addDoc.put("city", "Jena");
		addDoc.put("street", "Otto-Devrient-Str");
		addDoc.put("zip", "07743");
		doc.put("address", addDoc);

		assertEquals(0, personDocCollection.countDocuments());
		personDocCollection.insertOne(doc);
		assertEquals(1, personDocCollection.countDocuments());
		Resource findResource = resourceSet.createResource(URI.createURI(FIND_PERSON));

		findResource.load(null);
		// get the persons
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());

		ECollection resultCollection = (ECollection) findResource.getContents().get(0);
		assertFalse(resultCollection.getValues().isEmpty());
		assertEquals(1, resultCollection.getValues().size());

		EObject object = resultCollection.getValues().get(0);
		assertTrue(object instanceof Person);

		Person person = (Person) object;
		assertEquals("Tester", person.getFirstName());
		assertNotNull(person.getAddress());
		assertTrue(person.getAddress().eIsProxy());
		Address add = person.getAddress();
		assertEquals("Jena", add.getCity());
		assertEquals("07743", add.getZip());
		assertEquals("test_address", add.getId());
		assertEquals("Otto-Devrient-Str", add.getStreet());

		personDocCollection.drop();
	}

}
