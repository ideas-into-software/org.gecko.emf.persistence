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
package org.gecko.emf.mongo.tests.codecs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.gecko.emf.mongo.Keywords;
import org.gecko.emf.mongo.Options;
import org.gecko.emf.mongo.codecs.EObjectCodecProvider;
import org.gecko.emf.mongo.converter.DefaultConverterService;
import org.gecko.emf.osgi.example.model.basic.Address;
import org.gecko.emf.osgi.example.model.basic.BasicFactory;
import org.gecko.emf.osgi.example.model.basic.BasicPackage;
import org.gecko.emf.osgi.example.model.basic.BusinessContact;
import org.gecko.emf.osgi.example.model.basic.Contact;
import org.gecko.emf.osgi.example.model.basic.ContactContextType;
import org.gecko.emf.osgi.example.model.basic.ContactType;
import org.gecko.emf.osgi.example.model.basic.Family;
import org.gecko.emf.osgi.example.model.basic.GenderType;
import org.gecko.emf.osgi.example.model.basic.Person;
import org.gecko.emf.osgi.example.model.basic.Tag;
import org.gecko.emf.osgi.example.model.basic.util.BasicResourceFactoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

import com.mongodb.MongoClient;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;

@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class MongoCodecTest {

	private ResourceSetImpl resourceSet;
	private MongoClient client;
	private MongoCollection<?> collection;
	private String mongoHost = System.getProperty("mongo.host", "localhost");
	private String baseUri = null;

	@BeforeEach
	public void setup() {
		// setup EMF
		BasicPackage.eINSTANCE.eClass();
		resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(BasicPackage.eNS_URI, BasicPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("test", new BasicResourceFactoryImpl());
		resourceSet.getResourceFactoryRegistry().getProtocolToFactoryMap().put("mongodb", new BasicResourceFactoryImpl());
		// create codec provider

		baseUri = "mongodb://" + mongoHost + "/person";
		client = new MongoClient(mongoHost);
	}

	@AfterEach
	public void teardown() {
		if (collection != null) {
			collection.drop();
		}
		if (client != null) {
			client.close();
		}
		resourceSet = null;
	}

	/**
	 * Test creation of objects and returning results using modelled Maps
	 * @throws InterruptedException 
	 */
//	@Test
	public void testCreate_ChangeStream() throws InterruptedException {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI("mongodb://localhost/person/Person/")), options, null);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it


		MongoCollection<Person> personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
		personCollection.drop();
		CountDownLatch latch = new CountDownLatch(2);
			ChangeStreamIterable<Person> streamIterable = personCollection.watch();
			streamIterable.forEach(new Consumer<ChangeStreamDocument<Person>>() {

				@Override
				public void accept(ChangeStreamDocument<Person> p) {
					System.out.println("change " + p.getUpdateDescription().getUpdatedFields());
					latch.countDown();
				}
			});

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		Contact c2 = BasicFactory.eINSTANCE.createContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@test.de");
		Contact c3 = BasicFactory.eINSTANCE.createContact();
		c3.setContext(ContactContextType.WORK);
		c3.setType(ContactType.WEBADDRESS);
		c3.setValue("www.test.de");

		// create person
		Person p1 = BasicFactory.eINSTANCE.createPerson();
		p1.setFirstName("Mark");
		p1.setLastName("Hoffmann");
		p1.setGender(GenderType.MALE);
		// add contacts as containment
		p1.getContact().add(c1);
		p1.getContact().add(c2);
		p1.getContact().add(c3);
		p1.getProperties().put("Test", "Me");
		// insert person
		assertEquals(0, personCollection.countDocuments());
		personCollection.insertOne(p1);
		assertEquals(1, personCollection.countDocuments());
		Thread.sleep(2000l);
		Person p2 = EcoreUtil.copy(p1);
		p2.setFirstName("Mark2");
		personCollection.replaceOne(Filters.eq("firstName", "Mark"), p2);
		// get the person

		personCollection.drop();
		
		assertTrue(latch.await(5, TimeUnit.SECONDS));
	}

	/**
	 * Test creation of objects and returning results using modelled Maps
	 */
	@Test
	public void testCreateAndFindObjects_Maps() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI("mongodb://localhost/person/Person/")), options, null);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it

		MongoCollection<Person> personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
		personCollection.drop();

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		Contact c2 = BasicFactory.eINSTANCE.createContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@test.de");
		Contact c3 = BasicFactory.eINSTANCE.createContact();
		c3.setContext(ContactContextType.WORK);
		c3.setType(ContactType.WEBADDRESS);
		c3.setValue("www.test.de");

		// create person
		Person p1 = BasicFactory.eINSTANCE.createPerson();
		p1.setFirstName("Mark");
		p1.setLastName("Hoffmann");
		p1.setGender(GenderType.MALE);
		// add contacts as containment
		p1.getContact().add(c1);
		p1.getContact().add(c2);
		p1.getContact().add(c3);
		p1.getProperties().put("Test", "Me");
		// insert person
		assertEquals(0, personCollection.countDocuments());
		personCollection.insertOne(p1);
		assertEquals(1, personCollection.countDocuments());

		// get the person
		FindIterable<Person> personIterable = personCollection.find();
		Person person = personIterable.first();
		assertNotNull(person);
		assertNotEquals(p1, person);
		assertEquals(p1.getFirstName(), person.getFirstName());
		assertEquals(p1.getLastName(), person.getLastName());
		assertEquals(p1.getGender(), person.getGender());
		assertEquals(3, person.getContact().size());
		assertEquals(1, person.getProperties().size());
		assertTrue(person.getProperties().containsKey("Test"));
		assertEquals("Me", person.getProperties().get("Test"));

		Contact contact1 = person.getContact().get(0);
		assertFalse(contact1.eIsProxy());
		assertNotEquals(c1, contact1);
		assertEquals(c1.getContext(), contact1.getContext());
		assertEquals(c1.getType(), contact1.getType());
		assertEquals(c1.getValue(), contact1.getValue());
		Contact contact2 = person.getContact().get(1);
		assertFalse(contact2.eIsProxy());
		assertNotEquals(c2, contact2);
		assertEquals(c2.getContext(), contact2.getContext());
		assertEquals(c2.getType(), contact2.getType());
		assertEquals(c2.getValue(), contact2.getValue());

		personCollection.drop();
	}

	/**
	 * Test creation of objects and returning results using modelled Maps
	 */
	@Test
	public void testCreateAndFindObjects_BasicValues() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI("mongodb://localhost/person/Person/")), options, null);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it

		MongoCollection<EObject> personCollection = client.getDatabase("person").getCollection("Person", EObject.class).withCodecRegistry(codecRegistry);
		personCollection.drop();

		EClass eClass = EcoreFactory.eINSTANCE.createEClass();

		eClass.setName("ExtendedPerson");

		EAttribute shortAttribute = EcoreFactory.eINSTANCE.createEAttribute();
		shortAttribute.setName("age");
		shortAttribute.setEType(EcorePackage.Literals.ESHORT);

		EAttribute charAttribute = EcoreFactory.eINSTANCE.createEAttribute();
		charAttribute.setName("letter");
		charAttribute.setEType(EcorePackage.Literals.ECHAR);

		eClass.getEAttributes().add(shortAttribute);
		eClass.getEAttributes().add(charAttribute);

		// create contacts
		EPackage dynamicEPackage = EcoreFactory.eINSTANCE.createEPackage();
		dynamicEPackage.setName("dynamicPackage");
		dynamicEPackage.setNsPrefix("dp");
		dynamicEPackage.setNsURI("http://test.de/dynamic/1.0");
		dynamicEPackage.getEClassifiers().add(eClass);

		//		ResourceSetImpl resourceSetImpl = new ResourceSetImpl();

		//		resourceSetImpl.getResourceFactoryRegistry().getExtensionToFactoryMap().put(dynamicEPackage.getName(), new XMIResourceFactoryImpl());
		Resource packageResource = new XMIResourceFactoryImpl().createResource(URI.createURI(dynamicEPackage.getNsURI()));
		packageResource.getContents().add(dynamicEPackage);
		resourceSet.getResources().add(packageResource);
		resourceSet.getPackageRegistry().put(dynamicEPackage.getNsURI(), dynamicEPackage);
		EObject eObject = EcoreUtil.create(eClass);

		eObject.eSet(charAttribute, 'L');
		eObject.eSet(shortAttribute, (short) 10);

		// insert person
		assertEquals(0, personCollection.countDocuments());
		personCollection.insertOne(eObject);
		assertEquals(1, personCollection.countDocuments());

		// get the person
		FindIterable<EObject> personIterable = personCollection.find();
		EObject person = personIterable.first();
		assertNotNull(person);
		assertNotEquals(eObject, person);
		assertEquals(eObject.eGet(shortAttribute), person.eGet(shortAttribute));
		assertEquals(eObject.eGet(charAttribute), person.eGet(charAttribute));

		personCollection.drop();
	}


	/**
	 * Test creation of objects
	 */
	@Test
	public void testCreateObjects_Containment() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI(baseUri + "/Person/")), options);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it
		MongoCollection<Person> personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
		personCollection.drop();

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		Contact c2 = BasicFactory.eINSTANCE.createContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@test.de");

		// create person
		Person p1 = BasicFactory.eINSTANCE.createPerson();
		p1.setFirstName("Mark");
		p1.setLastName("Hoffmann");
		p1.setGender(GenderType.MALE);
		// add contacts as containment
		p1.getContact().add(c1);
		p1.getContact().add(c2);
		// insert person
		assertEquals(0, personCollection.countDocuments());
		personCollection.insertOne(p1);
		assertEquals(1, personCollection.countDocuments());

		//		personCollection.drop();
	}

	/**
	 * Test creation of objects
	 */
	@Test
	public void testCreateObjects_NonContainment() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI(baseUri + "/Person/")), options);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it
		MongoCollection<Person> personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
		MongoCollection<Address> addressCollection = client.getDatabase("person").getCollection("Address", Address.class).withCodecRegistry(codecRegistry);
		personCollection.drop();
		addressCollection.drop();

		Address address = BasicFactory.eINSTANCE.createAddress();
		address.setCity("Berlin");
		address.setId("b1");
		address.setStreet("Friedrichstraße 1");
		address.setZip("12345");
		assertEquals(0, addressCollection.countDocuments());
		addressCollection.insertOne(address);
		assertEquals(1, addressCollection.countDocuments());
		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		Contact c2 = BasicFactory.eINSTANCE.createContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@test.de");

		// create person
		Person p1 = BasicFactory.eINSTANCE.createPerson();
		p1.setFirstName("Mark");
		p1.setLastName("Hoffmann");
		p1.setGender(GenderType.MALE);
		// add contacts as containment
		p1.getContact().add(c1);
		p1.getContact().add(c2);
		p1.setAddress(address);
		// insert person
		assertEquals(0, personCollection.countDocuments());
		personCollection.insertOne(p1);
		assertEquals(1, personCollection.countDocuments());

		personCollection.drop();
		addressCollection.drop();
	}

	/**
	 * Test creation of objects
	 */
	@Test
	public void testCreateAndFindObjects_NonContainment() {

		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI(baseUri + "/Person/")), options, null);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it
		MongoCollection<Person> personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
		
		MongoCollection<Address> addressCollection = client.getDatabase("person").getCollection("Address", Address.class).withCodecRegistry(codecRegistry);
		personCollection.drop();
		addressCollection.drop();

		EcoreUtil.create(BasicPackage.Literals.ADDRESS);

		Address address = BasicFactory.eINSTANCE.createAddress();
		address.setCity("Berlin");
		address.setId("b1");
		address.setStreet("Friedrichstraße 1");
		address.setZip("12345");
		assertEquals(0, addressCollection.countDocuments());
		addressCollection.insertOne(address);
		assertEquals(1, addressCollection.countDocuments());
		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		Contact c2 = BasicFactory.eINSTANCE.createContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@test.de");

		// create person
		Person p1 = BasicFactory.eINSTANCE.createPerson();
		p1.setFirstName("Mark");
		p1.setLastName("Hoffmann");
		p1.setGender(GenderType.MALE);
		// add contacts as containment
		p1.getContact().add(c1);
		p1.getContact().add(c2);
		p1.setAddress(address);
		// insert person
		assertEquals(0, personCollection.countDocuments());
		personCollection.insertOne(p1);
		assertEquals(1, personCollection.countDocuments());

		Iterable<Address> addressIterable = addressCollection.find();
		Iterator<Address> aIterator = addressIterable.iterator();
		assertNotNull(aIterator);
		assertTrue(aIterator.hasNext());
		Address a = aIterator.next();
		assertNotNull(a);
		assertEquals(address.getCity(), a.getCity());
		assertEquals(address.getStreet(), a.getStreet());
		assertEquals(address.getZip(), a.getZip());

		FindIterable<Person> personIterable = personCollection.find();
		Person person = personIterable.first();
		assertNotNull(person);
		assertNotEquals(p1, person);
		assertEquals(p1.getFirstName(), person.getFirstName());
		assertEquals(p1.getLastName(), person.getLastName());
		assertEquals(p1.getGender(), person.getGender());
		assertEquals(2, person.getContact().size());

		Contact contact1 = person.getContact().get(0);
		assertFalse(contact1.eIsProxy());
		assertNotEquals(c1, contact1);
		assertEquals(c1.getContext(), contact1.getContext());
		assertEquals(c1.getType(), contact1.getType());
		assertEquals(c1.getValue(), contact1.getValue());
		Contact contact2 = person.getContact().get(1);
		assertFalse(contact2.eIsProxy());
		assertNotEquals(c2, contact2);
		assertEquals(c2.getContext(), contact2.getContext());
		assertEquals(c2.getType(), contact2.getType());
		assertEquals(c2.getValue(), contact2.getValue());

		assertNotNull(person.getAddress());
		assertTrue(person.getAddress().eIsProxy());
		URI proxyUri = ((InternalEObject)person.getAddress()).eProxyURI();
		assertEquals(baseUri + "/Person/#b1", proxyUri.toString());

		personCollection.drop();
		addressCollection.drop();
	}

	
	
	
	/**
	 * Test creation of objects and returning results
	 */
	@Test
	public void testCreateAndFindObjects_Containment() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI(baseUri + "/Person/")), options, null);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it
		MongoCollection<Person> personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
		personCollection.drop();

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		Contact c2 = BasicFactory.eINSTANCE.createContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@test.de");

		// create person
		Person p1 = BasicFactory.eINSTANCE.createPerson();
		p1.setFirstName("Mark");
		p1.setLastName("Hoffmann");
		p1.setGender(GenderType.MALE);
		// add contacts as containment
		p1.getContact().add(c1);
		p1.getContact().add(c2);
		// insert person
		assertEquals(0, personCollection.countDocuments());
		personCollection.insertOne(p1);
		assertEquals(1, personCollection.countDocuments());

		// get the person
		FindIterable<Person> personIterable = personCollection.find();
		Person person = personIterable.first();
		assertNotNull(person);
		assertNotEquals(p1, person);
		assertEquals(p1.getFirstName(), person.getFirstName());
		assertEquals(p1.getLastName(), person.getLastName());
		assertEquals(p1.getGender(), person.getGender());
		assertEquals(2, person.getContact().size());

		Contact contact1 = person.getContact().get(0);
		assertFalse(contact1.eIsProxy());
		assertNotEquals(c1, contact1);
		assertEquals(c1.getContext(), contact1.getContext());
		assertEquals(c1.getType(), contact1.getType());
		assertEquals(c1.getValue(), contact1.getValue());
		Contact contact2 = person.getContact().get(1);
		assertFalse(contact2.eIsProxy());
		assertNotEquals(c2, contact2);
		assertEquals(c2.getContext(), contact2.getContext());
		assertEquals(c2.getType(), contact2.getType());
		assertEquals(c2.getValue(), contact2.getValue());

		personCollection.drop();
	}

	/**
	 * Test creation of objects and returning results
	 */
	@Test
	public void testCreateAndFindWithoutOptionsMap() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI(baseUri + "/Person/")), options, null);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it
		MongoCollection<Person> personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
		personCollection.drop();

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		Contact c2 = BasicFactory.eINSTANCE.createContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@test.de");

		// create person
		Person p1 = BasicFactory.eINSTANCE.createPerson();
		p1.setFirstName("Mark");
		p1.setLastName("Hoffmann");
		p1.setGender(GenderType.MALE);
		// add contacts as containment
		p1.getContact().add(c1);
		p1.getContact().add(c2);
		// insert person
		assertEquals(0, personCollection.countDocuments());
		personCollection.insertOne(p1);
		assertEquals(1, personCollection.countDocuments());

		// get the person
		FindIterable<Person> personIterable = personCollection.find();
		Person person = personIterable.first();
		assertNotNull(person);
		assertNotEquals(p1, person);
		assertEquals(p1.getFirstName(), person.getFirstName());
		assertEquals(p1.getLastName(), person.getLastName());
		assertEquals(p1.getGender(), person.getGender());
		assertEquals(2, person.getContact().size());

		Contact contact1 = person.getContact().get(0);
		assertFalse(contact1.eIsProxy());
		assertNotEquals(c1, contact1);
		assertEquals(c1.getContext(), contact1.getContext());
		assertEquals(c1.getType(), contact1.getType());
		assertEquals(c1.getValue(), contact1.getValue());
		Contact contact2 = person.getContact().get(1);
		assertFalse(contact2.eIsProxy());
		assertNotEquals(c2, contact2);
		assertEquals(c2.getContext(), contact2.getContext());
		assertEquals(c2.getType(), contact2.getType());
		assertEquals(c2.getValue(), contact2.getValue());

		personCollection.drop();
	}


	/**
	 * Test creation of objects and returning results
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testTimestampInContainement() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI(baseUri + "/Person/")), options, null);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it
		MongoCollection<Person> personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
		collection = personCollection;
		MongoCollection<Document> personCollectionDocument = client.getDatabase("person").getCollection("Person");
		personCollection.drop();

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		Contact c2 = BasicFactory.eINSTANCE.createContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@test.de");

		// create person
		Person p1 = BasicFactory.eINSTANCE.createPerson();
		p1.setFirstName("Mark");
		p1.setLastName("Hoffmann");
		p1.setGender(GenderType.MALE);
		// add contacts as containment
		p1.getContact().add(c1);
		p1.getContact().add(c2);
		// insert person
		assertEquals(0, personCollection.countDocuments());
		personCollection.insertOne(p1);
		assertEquals(1, personCollection.countDocuments());

		// get the person
		FindIterable<Document> personIterable = personCollectionDocument.find();
		Document person = personIterable.first();
		assertTrue(person.containsKey(Keywords.TIME_STAMP_KEY));
		List<Document> contacts = person.get("contact", List.class);
		assertTrue(contacts.size() > 0);
		Document contact = contacts.get(0);
		assertFalse(contact.containsKey(Keywords.TIME_STAMP_KEY));

		personCollection.drop();
	}


	/**
	 * Test creation of objects and returning results with checking that only eClassUris are written for Objects inhering from the referenced type
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testEClassUrisSubstitutionContainmentsFalse() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		options.put(Options.OPTION_KEY_ECLASS_URI, "_type");
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI(baseUri + "/Person/")), options, null);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it
		MongoCollection<Person> personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
		collection = personCollection;
		MongoCollection<Document> personCollectionDocument = client.getDatabase("person").getCollection("Person");
		personCollection.drop();

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		BusinessContact c2 = BasicFactory.eINSTANCE.createBusinessContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@test.de");
		c2.setCompanyName("TEST");

		// create person
		Person p1 = BasicFactory.eINSTANCE.createPerson();
		p1.setFirstName("Mark");
		p1.setLastName("Hoffmann");
		p1.setGender(GenderType.MALE);
		// add contacts as containment
		p1.getContact().add(c1);
		p1.getContact().add(c2);
		p1.getContact().add(BasicFactory.eINSTANCE.createContact());
		p1.getContact().add(BasicFactory.eINSTANCE.createBusinessContact());
		// insert person
		assertEquals(0, personCollection.countDocuments());
		personCollection.insertOne(p1);
		assertEquals(1, personCollection.countDocuments());

		// get the person
		FindIterable<Document> personIterable = personCollectionDocument.find();
		Document person = personIterable.first();
		assertTrue(person.containsKey(Keywords.TIME_STAMP_KEY));
		List<Document> contacts = person.get("contact", List.class);
		assertTrue(contacts.size() > 0);
		Document contactWith = contacts.get(0);
		assertFalse(contactWith.containsKey("_type"));
		assertFalse(contactWith.containsKey(Keywords.ECLASS_KEY));
		Document contactWithout = contacts.get(1);
		assertTrue(contactWithout.containsKey("_type"));
		assertFalse(contactWithout.containsKey(Keywords.ECLASS_KEY));

		FindIterable<Person> ePersonIterable = personCollection.find();
		Person ePerson = ePersonIterable.first();
		assertNotNull(ePerson);
		assertNotEquals(p1, ePerson);
		assertEquals(p1.getFirstName(), ePerson.getFirstName());
		assertEquals(p1.getLastName(), ePerson.getLastName());
		assertEquals(p1.getGender(), ePerson.getGender());
		assertEquals(4, ePerson.getContact().size());

		Contact contact1 = ePerson.getContact().get(0);
		assertFalse(contact1.eIsProxy());
		assertNotEquals(c1, contact1);
		assertEquals(c1.getContext(), contact1.getContext());
		assertEquals(c1.getType(), contact1.getType());
		assertEquals(c1.getValue(), contact1.getValue());
		Contact contact2 = ePerson.getContact().get(1);
		assertFalse(contact2.eIsProxy());
		assertTrue(contact2.eClass().equals(BasicPackage.Literals.BUSINESS_CONTACT));
		BusinessContact bc = (BusinessContact) contact2;
		assertNotEquals(c2, contact2);
		assertEquals(c2.getContext(), contact2.getContext());
		assertEquals(c2.getType(), contact2.getType());
		assertEquals(c2.getValue(), contact2.getValue());
		assertEquals(c2.getCompanyName(), bc.getCompanyName());

		Contact contact3 = ePerson.getContact().get(2);
		assertNotNull(contact3);
		assertNull(contact3.getValue());

		Contact contact4 = ePerson.getContact().get(3);
		assertFalse(contact4.eIsProxy());
		assertTrue(contact4.eClass().equals(BasicPackage.Literals.BUSINESS_CONTACT));
		BusinessContact bc2 = (BusinessContact) contact4;
		assertNull(bc2.getValue());
		assertNull(bc2.getCompanyName());

		personCollection.drop();
	}

	/**
	 * Test creation of objects and returning results with checking that only eClassUris are written for Objects inhering from the referenced type
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testEClassUrisInContainmentsFalse() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI(baseUri + "/Person/")), options, null);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it
		MongoCollection<Person> personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
		collection = personCollection;
		MongoCollection<Document> personCollectionDocument = client.getDatabase("person").getCollection("Person");
		personCollection.drop();

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		BusinessContact c2 = BasicFactory.eINSTANCE.createBusinessContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@test.de");
		c2.setCompanyName("TEST");

		// create person
		Person p1 = BasicFactory.eINSTANCE.createPerson();
		p1.setFirstName("Mark");
		p1.setLastName("Hoffmann");
		p1.setGender(GenderType.MALE);
		// add contacts as containment
		p1.getContact().add(c1);
		p1.getContact().add(c2);
		p1.getContact().add(BasicFactory.eINSTANCE.createContact());
		p1.getContact().add(BasicFactory.eINSTANCE.createBusinessContact());
		// insert person
		assertEquals(0, personCollection.countDocuments());
		personCollection.insertOne(p1);
		assertEquals(1, personCollection.countDocuments());

		// get the person
		FindIterable<Document> personIterable = personCollectionDocument.find();
		Document person = personIterable.first();
		assertTrue(person.containsKey(Keywords.TIME_STAMP_KEY));
		List<Document> contacts = person.get("contact", List.class);
		assertTrue(contacts.size() > 0);
		Document contactWith = contacts.get(0);
		assertFalse(contactWith.containsKey(Keywords.ECLASS_KEY));
		Document contactWithout = contacts.get(1);
		assertTrue(contactWithout.containsKey(Keywords.ECLASS_KEY));

		FindIterable<Person> ePersonIterable = personCollection.find();
		Person ePerson = ePersonIterable.first();
		assertNotNull(ePerson);
		assertNotEquals(p1, ePerson);
		assertEquals(p1.getFirstName(), ePerson.getFirstName());
		assertEquals(p1.getLastName(), ePerson.getLastName());
		assertEquals(p1.getGender(), ePerson.getGender());
		assertEquals(4, ePerson.getContact().size());

		Contact contact1 = ePerson.getContact().get(0);
		assertFalse(contact1.eIsProxy());
		assertNotEquals(c1, contact1);
		assertEquals(c1.getContext(), contact1.getContext());
		assertEquals(c1.getType(), contact1.getType());
		assertEquals(c1.getValue(), contact1.getValue());
		Contact contact2 = ePerson.getContact().get(1);
		assertFalse(contact2.eIsProxy());
		assertTrue(contact2.eClass().equals(BasicPackage.Literals.BUSINESS_CONTACT));
		BusinessContact bc = (BusinessContact) contact2;
		assertNotEquals(c2, contact2);
		assertEquals(c2.getContext(), contact2.getContext());
		assertEquals(c2.getType(), contact2.getType());
		assertEquals(c2.getValue(), contact2.getValue());
		assertEquals(c2.getCompanyName(), bc.getCompanyName());

		Contact contact3 = ePerson.getContact().get(2);
		assertNotNull(contact3);
		assertNull(contact3.getValue());

		Contact contact4 = ePerson.getContact().get(3);
		assertFalse(contact4.eIsProxy());
		assertTrue(contact4.eClass().equals(BasicPackage.Literals.BUSINESS_CONTACT));
		BusinessContact bc2 = (BusinessContact) contact4;
		assertNull(bc2.getValue());
		assertNull(bc2.getCompanyName());

		personCollection.drop();
	}

	/**
	 * Test creation of objects and returning results with checking that only eClassUris are written for Objects inhering from the referenced type
	 */
	@SuppressWarnings("unused")
	@Test
	public void testEClassUrisInContainmentsFalseFailing() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI(baseUri + "/Person/")), options, null);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it
		MongoCollection<Person> personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
		collection = personCollection;
		MongoCollection<Document> personCollectionDocument = client.getDatabase("person").getCollection("Person");
		personCollection.drop();

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		BusinessContact c2 = BasicFactory.eINSTANCE.createBusinessContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@test.de");
		c2.setCompanyName("TEST");

		// create person
		Person p1 = BasicFactory.eINSTANCE.createPerson();
		p1.setFirstName("Mark");
		p1.setLastName("Hoffmann");
		p1.setGender(GenderType.MALE);
		// add contacts as containment
		p1.getContact().add(BasicFactory.eINSTANCE.createContact());
		// insert person

		Tag tag1 = BasicFactory.eINSTANCE.createTag();
		tag1.setName("test");
		tag1.setValue("test2");
		Tag tag2 = BasicFactory.eINSTANCE.createTag();
		tag1.getTags().add(tag2);

		p1.getTags().add(tag1);

		assertEquals(0, personCollection.countDocuments());
		personCollection.insertOne(p1);
		assertEquals(1, personCollection.countDocuments());

		client.close();
		setup();

		codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI(baseUri + "/Person/")), options, null);
		codecProvider.setConverterService(new DefaultConverterService());
		eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		defaultRegistry = MongoClient.getDefaultCodecRegistry();
		codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it
		personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
		collection = personCollection;
		personCollectionDocument = client.getDatabase("person").getCollection("Person");

		FindIterable<Person> ePersonIterable = personCollection.find();
		Person ePerson = ePersonIterable.first();
		assertNotNull(ePerson);
		assertNotEquals(p1, ePerson);
		assertEquals(p1.getFirstName(), ePerson.getFirstName());
		assertEquals(p1.getLastName(), ePerson.getLastName());
		assertEquals(p1.getGender(), ePerson.getGender());
		personCollection.drop();
	}

	/**
	 * Test creation of objects and returning results
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testEClassUrisInContainmentsTrue() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		options.put(Options.OPTION_SERIALIZE_ALL_ECLASS_URIS, Boolean.TRUE);
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI(baseUri + "/Person/")), options, null);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it
		MongoCollection<Person> personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
		collection = personCollection;
		MongoCollection<Document> personCollectionDocument = client.getDatabase("person").getCollection("Person");
		personCollection.drop();

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		BusinessContact c2 = BasicFactory.eINSTANCE.createBusinessContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@test.de");
		c2.setCompanyName("TEST");

		// create person
		Person p1 = BasicFactory.eINSTANCE.createPerson();
		p1.setFirstName("Mark");
		p1.setLastName("Hoffmann");
		p1.setGender(GenderType.MALE);
		// add contacts as containment
		p1.getContact().add(c1);
		p1.getContact().add(c2);
		p1.getContact().add(BasicFactory.eINSTANCE.createContact());
		p1.getContact().add(BasicFactory.eINSTANCE.createBusinessContact());
		// insert person
		assertEquals(0, personCollection.countDocuments());
		personCollection.insertOne(p1);
		assertEquals(1, personCollection.countDocuments());

		// get the person
		FindIterable<Document> personIterable = personCollectionDocument.find();
		Document person = personIterable.first();
		assertTrue(person.containsKey(Keywords.TIME_STAMP_KEY));
		List<Document> contacts = person.get("contact", List.class);
		assertTrue(contacts.size() > 0);
		Document contactWith = contacts.get(0);
		assertTrue(contactWith.containsKey(Keywords.ECLASS_KEY));
		Document contactWithout = contacts.get(1);
		assertTrue(contactWithout.containsKey(Keywords.ECLASS_KEY));

		FindIterable<Person> ePersonIterable = personCollection.find();
		Person ePerson = ePersonIterable.first();
		assertNotNull(ePerson);
		assertNotEquals(p1, ePerson);
		assertEquals(p1.getFirstName(), ePerson.getFirstName());
		assertEquals(p1.getLastName(), ePerson.getLastName());
		assertEquals(p1.getGender(), ePerson.getGender());
		assertEquals(4, ePerson.getContact().size());

		Contact contact1 = ePerson.getContact().get(0);
		assertFalse(contact1.eIsProxy());
		assertNotEquals(c1, contact1);
		assertEquals(c1.getContext(), contact1.getContext());
		assertEquals(c1.getType(), contact1.getType());
		assertEquals(c1.getValue(), contact1.getValue());
		Contact contact2 = ePerson.getContact().get(1);
		assertFalse(contact2.eIsProxy());
		assertTrue(contact2.eClass().equals(BasicPackage.Literals.BUSINESS_CONTACT));
		BusinessContact bc = (BusinessContact) contact2;
		assertNotEquals(c2, contact2);
		assertEquals(c2.getContext(), contact2.getContext());
		assertEquals(c2.getType(), contact2.getType());
		assertEquals(c2.getValue(), contact2.getValue());
		assertEquals(c2.getCompanyName(), bc.getCompanyName());

		Contact contact3 = ePerson.getContact().get(2);
		assertNotNull(contact3);
		assertNull(contact3.getValue());

		Contact contact4 = ePerson.getContact().get(3);
		assertFalse(contact4.eIsProxy());
		assertTrue(contact4.eClass().equals(BasicPackage.Literals.BUSINESS_CONTACT));
		BusinessContact bc2 = (BusinessContact) contact4;
		assertNull(bc2.getValue());
		assertNull(bc2.getCompanyName());

		personCollection.drop();
	}

	/**
	 * Test creation of objects and returning results
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testEClassUrisInContainmentsFalseProxy() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI(baseUri + "/Person/")), options, null);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it
		MongoCollection<Person> personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
		MongoCollection<Family> familyCollection = client.getDatabase("person").getCollection("Family", Family.class).withCodecRegistry(codecRegistry);
		collection = personCollection;
		MongoCollection<Document> personCollectionDocument = client.getDatabase("person").getCollection("Person");
		MongoCollection<Document> familyCollectionDocument = client.getDatabase("person").getCollection("Family");
		familyCollectionDocument.drop();
		personCollection.drop();

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		BusinessContact c2 = BasicFactory.eINSTANCE.createBusinessContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@test.de");
		c2.setCompanyName("TEST");

		// create person
		Person p1 = BasicFactory.eINSTANCE.createPerson();
		p1.setFirstName("Mark");
		p1.setLastName("Hoffmann");
		p1.setGender(GenderType.MALE);
		// add contacts as containment
		p1.getContact().add(c1);
		p1.getContact().add(c2);
		p1.getContact().add(BasicFactory.eINSTANCE.createContact());
		p1.getContact().add(BasicFactory.eINSTANCE.createBusinessContact());
		// insert person
		assertEquals(0, personCollection.countDocuments());
		personCollection.insertOne(p1);
		assertEquals(1, personCollection.countDocuments());

		Family family = BasicFactory.eINSTANCE.createFamily();
		family.setFather(p1);
		family.setMother(BasicFactory.eINSTANCE.createBusinessPerson());

		familyCollection.insertOne(family);

		// get the person
		FindIterable<Document> personIterable = personCollectionDocument.find();
		Document person = personIterable.first();
		assertTrue(person.containsKey(Keywords.TIME_STAMP_KEY));
		List<Document> contacts = person.get("contact", List.class);
		assertTrue(contacts.size() > 0);
		Document contactWith = contacts.get(0);
		assertFalse(contactWith.containsKey(Keywords.ECLASS_KEY));
		Document contactWithout = contacts.get(1);
		assertTrue(contactWithout.containsKey(Keywords.ECLASS_KEY));

		FindIterable<Document> familyIterator = familyCollectionDocument.find();
		Document familyDocument = familyIterator.first();
		Document fatherDoc = familyDocument.get("father", Document.class);
		assertNotNull(fatherDoc);
		assertFalse(fatherDoc.containsKey(Keywords.ECLASS_KEY));

		Document motherDoc = familyDocument.get("mother", Document.class);
		assertNotNull(motherDoc);
		assertTrue(motherDoc.containsKey(Keywords.ECLASS_KEY));



		FindIterable<Person> ePersonIterable = personCollection.find();
		Person ePerson = ePersonIterable.first();
		assertNotNull(ePerson);
		assertNotEquals(p1, ePerson);
		assertEquals(p1.getFirstName(), ePerson.getFirstName());
		assertEquals(p1.getLastName(), ePerson.getLastName());
		assertEquals(p1.getGender(), ePerson.getGender());
		assertEquals(4, ePerson.getContact().size());

		Contact contact1 = ePerson.getContact().get(0);
		assertFalse(contact1.eIsProxy());
		assertNotEquals(c1, contact1);
		assertEquals(c1.getContext(), contact1.getContext());
		assertEquals(c1.getType(), contact1.getType());
		assertEquals(c1.getValue(), contact1.getValue());
		Contact contact2 = ePerson.getContact().get(1);
		assertFalse(contact2.eIsProxy());
		assertTrue(contact2.eClass().equals(BasicPackage.Literals.BUSINESS_CONTACT));
		BusinessContact bc = (BusinessContact) contact2;
		assertNotEquals(c2, contact2);
		assertEquals(c2.getContext(), contact2.getContext());
		assertEquals(c2.getType(), contact2.getType());
		assertEquals(c2.getValue(), contact2.getValue());
		assertEquals(c2.getCompanyName(), bc.getCompanyName());

		Contact contact3 = ePerson.getContact().get(2);
		assertNotNull(contact3);
		assertNull(contact3.getValue());

		Contact contact4 = ePerson.getContact().get(3);
		assertFalse(contact4.eIsProxy());
		assertTrue(contact4.eClass().equals(BasicPackage.Literals.BUSINESS_CONTACT));
		BusinessContact bc2 = (BusinessContact) contact4;
		assertNull(bc2.getValue());
		assertNull(bc2.getCompanyName());

		personCollection.drop();
		familyCollectionDocument.drop();
	}

	/**
	 * Test creation of objects and returning results
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testEClassUrisInContainmentsTrueProxy() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		options.put(Options.OPTION_SERIALIZE_ALL_ECLASS_URIS, Boolean.TRUE);
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI(baseUri + "/Person/")), options, null);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it
		MongoCollection<Person> personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
		MongoCollection<Family> familyCollection = client.getDatabase("person").getCollection("Family", Family.class).withCodecRegistry(codecRegistry);
		collection = personCollection;
		MongoCollection<Document> personCollectionDocument = client.getDatabase("person").getCollection("Person");
		MongoCollection<Document> familyCollectionDocument = client.getDatabase("person").getCollection("Family");
		familyCollectionDocument.drop();
		personCollection.drop();

		// create contacts
		Contact c1 = BasicFactory.eINSTANCE.createContact();
		c1.setContext(ContactContextType.PRIVATE);
		c1.setType(ContactType.SKYPE);
		c1.setValue("charles-brown");
		BusinessContact c2 = BasicFactory.eINSTANCE.createBusinessContact();
		c2.setContext(ContactContextType.WORK);
		c2.setType(ContactType.EMAIL);
		c2.setValue("mark.hoffmann@test.de");
		c2.setCompanyName("TEST");

		// create person
		Person p1 = BasicFactory.eINSTANCE.createPerson();
		p1.setFirstName("Mark");
		p1.setLastName("Hoffmann");
		p1.setGender(GenderType.MALE);
		// add contacts as containment
		p1.getContact().add(c1);
		p1.getContact().add(c2);
		p1.getContact().add(BasicFactory.eINSTANCE.createContact());
		p1.getContact().add(BasicFactory.eINSTANCE.createBusinessContact());
		// insert person
		assertEquals(0, personCollection.countDocuments());
		personCollection.insertOne(p1);
		assertEquals(1, personCollection.countDocuments());

		Family family = BasicFactory.eINSTANCE.createFamily();
		family.setFather(p1);
		family.setMother(BasicFactory.eINSTANCE.createBusinessPerson());

		familyCollection.insertOne(family);

		// get the person
		FindIterable<Document> personIterable = personCollectionDocument.find();
		Document person = personIterable.first();
		assertTrue(person.containsKey(Keywords.TIME_STAMP_KEY));
		List<Document> contacts = person.get("contact", List.class);
		assertTrue(contacts.size() > 0);
		Document contactWith = contacts.get(0);
		assertTrue(contactWith.containsKey(Keywords.ECLASS_KEY));
		Document contactWithout = contacts.get(1);
		assertTrue(contactWithout.containsKey(Keywords.ECLASS_KEY));

		FindIterable<Document> familyIterator = familyCollectionDocument.find();
		Document familyDocument = familyIterator.first();
		Document fatherDoc = familyDocument.get("father", Document.class);
		assertNotNull(fatherDoc);
		assertTrue(fatherDoc.containsKey(Keywords.ECLASS_KEY));

		Document motherDoc = familyDocument.get("mother", Document.class);
		assertNotNull(motherDoc);
		assertTrue(motherDoc.containsKey(Keywords.ECLASS_KEY));



		FindIterable<Person> ePersonIterable = personCollection.find();
		Person ePerson = ePersonIterable.first();
		assertNotNull(ePerson);
		assertNotEquals(p1, ePerson);
		assertEquals(p1.getFirstName(), ePerson.getFirstName());
		assertEquals(p1.getLastName(), ePerson.getLastName());
		assertEquals(p1.getGender(), ePerson.getGender());
		assertEquals(4, ePerson.getContact().size());

		Contact contact1 = ePerson.getContact().get(0);
		assertFalse(contact1.eIsProxy());
		assertNotEquals(c1, contact1);
		assertEquals(c1.getContext(), contact1.getContext());
		assertEquals(c1.getType(), contact1.getType());
		assertEquals(c1.getValue(), contact1.getValue());
		Contact contact2 = ePerson.getContact().get(1);
		assertFalse(contact2.eIsProxy());
		assertTrue(contact2.eClass().equals(BasicPackage.Literals.BUSINESS_CONTACT));
		BusinessContact bc = (BusinessContact) contact2;
		assertNotEquals(c2, contact2);
		assertEquals(c2.getContext(), contact2.getContext());
		assertEquals(c2.getType(), contact2.getType());
		assertEquals(c2.getValue(), contact2.getValue());
		assertEquals(c2.getCompanyName(), bc.getCompanyName());

		Contact contact3 = ePerson.getContact().get(2);
		assertNotNull(contact3);
		assertNull(contact3.getValue());

		Contact contact4 = ePerson.getContact().get(3);
		assertFalse(contact4.eIsProxy());
		assertTrue(contact4.eClass().equals(BasicPackage.Literals.BUSINESS_CONTACT));
		BusinessContact bc2 = (BusinessContact) contact4;
		assertNull(bc2.getValue());
		assertNull(bc2.getCompanyName());

		personCollection.drop();
		familyCollectionDocument.drop();
	}

	/**
	 * Test creation of objects and returning results
	 */
	@Test
	public void testCreateAndFindObjects_ContainmentMany() {
		int insertSize = 10000;

		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		List<Resource> resourcesCache = new ArrayList<Resource>(insertSize);
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI(baseUri + "/Person/")), options, resourcesCache);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it
		MongoCollection<Person> personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
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
		int insertBatchSize = 500;

		long start = System.currentTimeMillis();
		List<Person> personsList = new ArrayList<>(insertBatchSize);
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
				personCollection.insertMany(personsList);
				personsList.clear();
			}
		}
		System.out.println("Insert of " + insertSize  + " persons with batchSize=" + insertBatchSize + " took " + (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, personCollection.countDocuments());

		/*
		 * Find person in the collection
		 */
		start = System.currentTimeMillis();
		// get the persons
		FindIterable<Person> personIterable = personCollection.find();
		System.out.println("Finding all persons with a size " + insertSize  + " took " + (System.currentTimeMillis() - start) + " ms");
		assertNotNull(personIterable);

		/*
		 * Iterating over the result and getting the real objects
		 */
		start = System.currentTimeMillis();
		List<Person> resultList = new ArrayList<Person>();
		assertEquals(0, resultList.size());
		// iterate over all elements
		personIterable.batchSize(insertBatchSize);
		for (Person person : personIterable) {
			resultList.add(person);
		}
		resourceSet.getResources().addAll(resourcesCache);
		System.out.println("Iterating over all persons and mapping with a batch size " + insertBatchSize  + " took " + (System.currentTimeMillis() - start) + " ms");

		for(Person p : resultList){
			assertNotNull(p.eResource());
			assertNotNull(p.eResource().getResourceSet());
			assertEquals(resourceSet, p.eResource().getResourceSet());
		}

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

		personCollection.drop();
	}

	/**
	 * Test creation of objects and returning results
	 * size:	1000000
	 * batch:	5000
	 * performance: 
	 * 		write	30 entries/ms
	 * 		read	60 entries/ms
	 * Insert of 1000000 persons with batchSize=5000 took 32366 ms
	 * Finding all persons with a size 1000000 took 2 ms
	 * Iterating over all persons and mapping with a batch size 5000 took 16782 ms
	 * 
	 * size:	1000000
	 * batch:	10000
	 * performance: 
	 * 		write	32 entries/ms
	 * 		read	64 entries/ms
	 * Insert of 1000000 persons with batchSize=10000 took 30612 ms
	 * Finding all persons with a size 1000000 took 1 ms
	 * Iterating over all persons and mapping with a batch size 10000 took 15498 ms
	 * 
	 * size:	1000000
	 * batch:	25000
	 * performance: 
	 * 		write	31 entries/ms
	 * 		read	64 entries/ms
	 * Insert of 1000000 persons with batchSize=25000 took 31480 ms
	 * Finding all persons with a size 1000000 took 2 ms
	 * Iterating over all persons and mapping with a batch size 25000 took 15592 ms
	 */
	//	@Test
	public void testCreateAndFindObjects_ContainmentManyMore() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(URIConverter.OPTION_RESPONSE, new HashMap<Object, Object>());
		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet.createResource(URI.createURI(baseUri + "/Person/")), options);
		codecProvider.setConverterService(new DefaultConverterService());
		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		CodecRegistry defaultRegistry = MongoClient.getDefaultCodecRegistry();
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		// get collections and clear it
		MongoCollection<Person> personCollection = client.getDatabase("person").getCollection("Person", Person.class).withCodecRegistry(codecRegistry);
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
		int insertSize = 1000000;
		int insertBatchSize = 25000;

		long start = System.currentTimeMillis();
		List<Person> personsList = new ArrayList<>(insertBatchSize);
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
				personCollection.insertMany(personsList);
				personsList.clear();
			}
		}
		System.out.println("Insert of " + insertSize  + " persons with batchSize=" + insertBatchSize + " took " + (System.currentTimeMillis() - start) + " ms");
		assertEquals(insertSize, personCollection.countDocuments());

		/*
		 * Find person in the collection
		 */
		start = System.currentTimeMillis();
		// get the persons
		FindIterable<Person> personIterable = personCollection.find();
		System.out.println("Finding all persons with a size " + insertSize  + " took " + (System.currentTimeMillis() - start) + " ms");
		assertNotNull(personIterable);

		/*
		 * Iterating over the result and getting the real objects
		 */
		start = System.currentTimeMillis();
		List<Person> resultList = new ArrayList<Person>();
		assertEquals(0, resultList.size());
		// iterate over all elements
		personIterable.batchSize(insertBatchSize);
		for (Person person : personIterable) {
			resultList.add(person);
		}
		System.out.println("Iterating over all persons and mapping with a batch size " + insertBatchSize  + " took " + (System.currentTimeMillis() - start) + " ms");
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

		personCollection.drop();
	}

	public static void main(String[] args) {
		MongoCodecTest test = new MongoCodecTest();
		test.setup();
		test.testCreateAndFindObjects_ContainmentMany();
		test.teardown();
	}

}
