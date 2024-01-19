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
//package org.gecko.emf.repository.mongo.tests;
//
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.Dictionary;
//import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
//import org.bson.Document;
//import org.eclipse.emf.common.util.BasicEList;
//import org.eclipse.emf.common.util.URI;
//import org.eclipse.emf.ecore.EObject;
//import org.eclipse.emf.ecore.resource.Resource;
//import org.eclipse.emf.ecore.resource.ResourceSet;
//import org.eclipse.emf.ecore.util.EcoreUtil;
//import org.gecko.core.tests.ServiceChecker;
//import org.gecko.emf.mongo.Keywords;
//import org.gecko.emf.mongo.Options;
//import org.gecko.emf.osgi.model.test.Address;
//import org.gecko.emf.osgi.model.test.BusinessPerson;
//import org.gecko.emf.osgi.model.test.Family;
//import org.gecko.emf.osgi.model.test.Person;
//import org.gecko.emf.osgi.model.test.TestFactory;
//import org.gecko.emf.osgi.model.test.TestPackage;
//import org.gecko.emf.repository.EMFRepository;
//import org.gecko.emf.repository.exception.ConstraintValidationException;
//import org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants;
//import org.gecko.emf.repository.query.IQueryBuilder;
//import org.gecko.emf.repository.query.QueryRepository;
//import org.gecko.mongo.osgi.MongoClientProvider;
//import org.gecko.mongo.osgi.MongoDatabaseProvider;
//import org.gecko.mongo.osgi.configuration.ConfigurationProperties;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.osgi.framework.BundleException;
//import org.osgi.framework.InvalidSyntaxException;
//
//import com.mongodb.client.MongoCollection;
//
///**
// * Integration tests for the complete EMF mongo repository setup
// * @author Mark Hoffmann
// * @since 26.07.2017
// */
//@RunWith(MockitoJUnitRunner.class)
//public class MongoRepositoryIntegrationTest extends EMFMongoIT {
//
//	@Test
//	public void testEMFMongoRepository() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//
//		// add service properties
//		String clientId = "testClient1";
//		String clientUri = "mongodb://" + mongoHost + ":27017";
//		Dictionary<String, Object> p = new Hashtable<String, Object>();
//		p.put(MongoClientProvider.PROP_CLIENT_ID, clientId);
//		p.put(MongoClientProvider.PROP_URI, clientUri);
//
//		defaultCheck();
//
//		ServiceChecker<MongoClientProvider> clientChecker = createTrackedChecker(MongoClientProvider.class).verbose(true).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.CLIENT_PID, "?", p);
//		clientChecker.assertCreations(1, true);
//
//		// add service properties
//		String dbAlias = "testDB";
//		String db = "test";
//		Dictionary<String, Object> dbp = new Hashtable<String, Object>();
//		dbp.put(MongoDatabaseProvider.PROP_ALIAS, dbAlias);
//		dbp.put(MongoDatabaseProvider.PROP_DATABASE, db);
//
//		ServiceChecker<MongoDatabaseProvider> dbChecker = createTrackedChecker(MongoDatabaseProvider.class).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.DATABASE_PID, "?", dbp);
//		dbChecker.assertCreations(1, true);
//
//		Dictionary<String, Object> properties = new Hashtable<>();
//
//		String repoId = "test_repo";
//		properties.put(EMFRepository.PROP_ID, repoId);
//		properties.put(EMFRepository.PROP_BASE_URI, clientUri + "/" + db);
//
//		ServiceChecker<?> repoChecker = createTrackedChecker("(" + EMFRepository.PROP_ID + "=" + repoId + ")", false).assertCreations(0, false);
//		createConfigForCleanup(EMFMongoConfiguratorConstants.SINGLETON_REPOSITORY_CONFIGURATION_NAME, "?", properties);
//		repoChecker.assertCreations(1, true);
//
//		MongoCollection<?> personCollection = getCollection(db, "Person");
//		MongoCollection<?> addressCollection = getCollection(db, "Address");
//		addressCollection.drop();
//		personCollection.drop();
//
//		EMFRepository repository = (EMFRepository) repoChecker.trackedServiceNotNull().getTrackedService();
//
//		Person person = TestFactory.eINSTANCE.createPerson();
//		person.setId("test");
//		person.setFirstName("Emil");
//		person.setLastName("Tester");
//		URI uri = repository.createUri(person);
//		assertEquals(clientUri + "/" + db + "/Person/test#test", uri.toString());
//		assertEquals(0, personCollection.countDocuments());
//
//		repository.save(person);
//
//		assertEquals(1, personCollection.countDocuments());
//
//		Resource r = person.eResource();
//		assertNotNull(r);
//		ResourceSet rs = r.getResourceSet();
//		assertNotNull(rs);
//		assertEquals(1, rs.getResources().size());
//
//		repository.detach(person);
//		assertNull(person.eResource());
//		assertEquals(0, rs.getResources().size());
//
//		Person personResult = repository.getEObject(TestPackage.Literals.PERSON, "test");
//		assertNotNull(personResult);
//		assertNotEquals(person, personResult);
//		assertNotEquals(r, personResult.eResource());
//
//		assertTrue(EcoreUtil.equals(person, personResult));
//
//	}
//
//	@Test
//	public void testMongoEMFMongoRepositoryGetByQuery() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//
//		// add service properties
//		String clientId = "testClient2";
//		String clientUri = "mongodb://" + mongoHost + ":27017";
//		Dictionary<String, Object> p = new Hashtable<String, Object>();
//		p.put(MongoClientProvider.PROP_CLIENT_ID, clientId);
//		p.put(MongoClientProvider.PROP_URI, clientUri);
//
//		defaultCheck();
//
//		ServiceChecker<MongoClientProvider> clientChecker = createTrackedChecker(MongoClientProvider.class).verbose(true).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.CLIENT_PID, "?", p);
//		clientChecker.assertCreations(1, true);
//
//		// add service properties
//		String dbAlias = "testDB";
//		String db = "test";
//		Dictionary<String, Object> dbp = new Hashtable<String, Object>();
//		dbp.put(MongoDatabaseProvider.PROP_ALIAS, dbAlias);
//		dbp.put(MongoDatabaseProvider.PROP_DATABASE, db);
//
//		ServiceChecker<MongoDatabaseProvider> dbChecker = createTrackedChecker(MongoDatabaseProvider.class).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.DATABASE_PID, "?", dbp);
//		dbChecker.assertCreations(1, true);
//
//		Dictionary<String, Object> properties = new Hashtable<>();
//
//		String repoId = "test_repo";
//		properties.put(EMFRepository.PROP_ID, repoId);
//		properties.put(EMFRepository.PROP_BASE_URI, clientUri + "/" + db);
//
//		ServiceChecker<?> repoChecker = createTrackedChecker("(" + EMFRepository.PROP_ID + "=" + repoId + ")", false).assertCreations(0, false);
//		createConfigForCleanup(EMFMongoConfiguratorConstants.SINGLETON_REPOSITORY_CONFIGURATION_NAME, "?", properties);
//		repoChecker.assertCreations(1, true);
//
//		MongoCollection<?> personCollection = getCollection(db, "Person");
//		MongoCollection<?> addressCollection = getCollection(db, "Address");
//		addressCollection.drop();
//		personCollection.drop();
//
//		EMFRepository repository = (EMFRepository) repoChecker.trackedServiceNotNull().getTrackedService();
//		QueryRepository queryRepo = (QueryRepository) repository.getAdapter(QueryRepository.class);
//		Person person = TestFactory.eINSTANCE.createPerson();
//		person.setId("test");
//		person.setFirstName("Emil");
//		person.setLastName("Tester");
//		URI uri = repository.createUri(person);
//		assertEquals(clientUri + "/" + db + "/Person/test#test", uri.toString());
//		assertEquals(0, personCollection.countDocuments());
//
//		repository.save(person);
//
//		assertEquals(1, personCollection.countDocuments());
//
//		person = TestFactory.eINSTANCE.createPerson();
//		person.setId("test2");
//		person.setFirstName("Emil2");
//		person.setLastName("Tester2");
//		uri = repository.createUri(person);
//		assertEquals(clientUri + "/" + db + "/Person/test2#test2", uri.toString());
//
//		repository.save(person);
//
//		assertEquals(2, personCollection.countDocuments());
//
//		ResourceSet resourceSet = repository.getResourceSet();
//
//		resourceSet.getResources().clear();
//
//		assertTrue(resourceSet.getResources().isEmpty());
//
//		IQueryBuilder allQuery = queryRepo.createQueryBuilder().allQuery();
//
//		List<EObject> result = queryRepo.getEObjectsByQuery(TestPackage.Literals.PERSON, allQuery.build());
//
//		//only the result of our 2 saved Persons must have a related resource
//		assertEquals(2, result.size());
//		assertEquals(result.size(), resourceSet.getResources().size());
//
//		resourceSet.getResources().clear();
//
//		assertTrue(resourceSet.getResources().isEmpty());
//
//		result = queryRepo.getEObjectsByQuery(TestPackage.Literals.PERSON, allQuery.build(), Collections.singletonMap(Options.OPTION_LAZY_RESULT_LOADING, true));
//
//		person = (Person) result.get(0);
//
//		assertFalse(person.eIsProxy());
//		assertEquals(2, resourceSet.getResources().size());
//
//	}
//
//	@Test
//	public void testEMFMongoRepositorySaveMultiple1() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//
//		// add service properties
//		String clientId = "testClient3";
//		String clientUri = "mongodb://" + mongoHost + ":27017";
//		Dictionary<String, Object> p = new Hashtable<String, Object>();
//		p.put(MongoClientProvider.PROP_CLIENT_ID, clientId);
//		p.put(MongoClientProvider.PROP_URI, clientUri);
//
//		defaultCheck();
//
//		ServiceChecker<MongoClientProvider> clientChecker = createTrackedChecker(MongoClientProvider.class).verbose(true).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.CLIENT_PID, "?", p);
//		clientChecker.assertCreations(1, true);
//
//		// add service properties
//		String dbAlias = "testDB";
//		String db = "test";
//		Dictionary<String, Object> dbp = new Hashtable<String, Object>();
//		dbp.put(MongoDatabaseProvider.PROP_ALIAS, dbAlias);
//		dbp.put(MongoDatabaseProvider.PROP_DATABASE, db);
//
//		ServiceChecker<MongoDatabaseProvider> dbChecker = createTrackedChecker(MongoDatabaseProvider.class).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.DATABASE_PID, "?", dbp);
//		dbChecker.assertCreations(1, true);
//
//		Dictionary<String, Object> properties = new Hashtable<>();
//
//		String repoId = "test_repo";
//		properties.put(EMFRepository.PROP_ID, repoId);
//		properties.put(EMFRepository.PROP_BASE_URI, clientUri + "/" + db);
//
//		ServiceChecker<?> repoChecker = createTrackedChecker("(" + EMFRepository.PROP_ID + "=" + repoId + ")", false).assertCreations(0, false);
//		createConfigForCleanup(EMFMongoConfiguratorConstants.SINGLETON_REPOSITORY_CONFIGURATION_NAME, "?", properties);
//		repoChecker.assertCreations(1, true);
//
//
//		MongoCollection<?> personCollection = getCollection(db, "Person");
//		MongoCollection<?> addressCollection = getCollection(db, "Address");
//		addressCollection.drop();
//		personCollection.drop();
//
//		CountDownLatch latch = new CountDownLatch(1);
//		latch.await(1, TimeUnit.SECONDS);
//
//		EMFRepository repository = (EMFRepository) repoChecker.trackedServiceNotNull().getTrackedService();
//
//		Address address = repository.createProxy(TestPackage.eINSTANCE.getAddress(), "testAddress");
//
//		Person person = TestFactory.eINSTANCE.createPerson();
//		person.setId("test");
//		person.setFirstName("Emil");
//		person.setLastName("Tester");
//		person.setAddress(address);
//		URI uri = repository.createUri(person);
//		assertEquals(clientUri + "/" + db + "/Person/test#test", uri.toString());
//		assertEquals(0, personCollection.countDocuments());
//
//		repository.save(person);
//
//		assertEquals(1, personCollection.countDocuments());
//
//		Resource r = person.eResource();
//		assertNotNull(r);
//		ResourceSet rs = r.getResourceSet();
//		assertNotNull(rs);
//		assertEquals(1, rs.getResources().size());
//
//		repository.detach(person);
//		assertNull(person.eResource());
//		assertEquals(0, rs.getResources().size());
//
//		Person personResult = repository.getEObject(TestPackage.Literals.PERSON, "test");
//		assertNotNull(personResult);
//		assertNotEquals(person, personResult);
//		assertNotEquals(r, personResult.eResource());
//
//		assertTrue(EcoreUtil.equals(person, personResult));
//		assertNotNull(personResult.getAddress());
//		assertTrue(personResult.getAddress().eIsProxy());
//
//		repository.detach(personResult);
//
//		repository.save(person);
//
//		repository.detach(person);
//
//		personResult = repository.getEObject(TestPackage.Literals.PERSON, "test");
//		assertNotNull(personResult);
//		assertNotEquals(person, personResult);
//		assertNotEquals(r, personResult.eResource());
//
//		assertTrue(EcoreUtil.equals(person, personResult));
//		assertNotNull(personResult.getAddress());
//		assertTrue(personResult.getAddress().eIsProxy());
//
//		assertNotEquals(address, personResult.getAddress());
//	}
//
//	@Test
//	public void testEMFMongoRepository_CollectionHint() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//
//		// add service properties
//		String clientId = "testClient4";
//		String clientUri = "mongodb://" + mongoHost + ":27017";
//		Dictionary<String, Object> p = new Hashtable<String, Object>();
//		p.put(MongoClientProvider.PROP_CLIENT_ID, clientId);
//		p.put(MongoClientProvider.PROP_URI, clientUri);
//
//		defaultCheck();
//
//		ServiceChecker<MongoClientProvider> clientChecker = createTrackedChecker(MongoClientProvider.class).verbose(true).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.CLIENT_PID, "?", p);
//		clientChecker.assertCreations(1, true);
//
//		// add service properties
//		String dbAlias = "testDB";
//		String db = "test";
//		Dictionary<String, Object> dbp = new Hashtable<String, Object>();
//		dbp.put(MongoDatabaseProvider.PROP_ALIAS, dbAlias);
//		dbp.put(MongoDatabaseProvider.PROP_DATABASE, db);
//
//		ServiceChecker<MongoDatabaseProvider> dbChecker = createTrackedChecker(MongoDatabaseProvider.class).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.DATABASE_PID, "?", dbp);
//		dbChecker.assertCreations(1, true);
//
//		Dictionary<String, Object> properties = new Hashtable<>();
//
//		String repoId = "test_repo";
//		properties.put(EMFRepository.PROP_ID, repoId);
//		properties.put(EMFRepository.PROP_BASE_URI, clientUri + "/" + db);
//
//		ServiceChecker<?> repoChecker = createTrackedChecker("(" + EMFRepository.PROP_ID + "=" + repoId + ")", false).assertCreations(0, false);
//		createConfigForCleanup(EMFMongoConfiguratorConstants.SINGLETON_REPOSITORY_CONFIGURATION_NAME, "?", properties);
//		repoChecker.assertCreations(1, true);
//
//
//		MongoCollection<?> personCollection = getCollection(db, "Person");
//		MongoCollection<?> addressCollection = getCollection(db, "Address");
//		addressCollection.drop();
//		personCollection.drop();
//
//		CountDownLatch latch = new CountDownLatch(1);
//		latch.await(1, TimeUnit.SECONDS);
//
//		EMFRepository repository = (EMFRepository) repoChecker.trackedServiceNotNull().getTrackedService();
//
//		BusinessPerson bPerson = TestFactory.eINSTANCE.createBusinessPerson();
//		bPerson.setId("bp");
//		bPerson.setFirstName("Lord");
//		bPerson.setLastName("Business");
//
//		URI uri = repository.createUri(bPerson);
//		assertEquals(clientUri + "/" + db + "/BusinessPerson/bp#bp", uri.toString());
//		Map<String, Object> saveProperties = new HashMap<String, Object>();
//		saveProperties.put(Options.OPTION_COLLECTION_NAME, TestPackage.eINSTANCE.getPerson().getName());
//		assertNull(saveProperties.get(Options.OPTION_STORE_SUPERTYPE));
//		uri = repository.createUri(bPerson, saveProperties);
//		assertEquals(clientUri + "/" + db + "/Person/bp#bp", uri.toString());
//		assertEquals(Boolean.TRUE, saveProperties.get(Options.OPTION_STORE_SUPERTYPE));
//		assertEquals(TestPackage.Literals.BUSINESS_PERSON, saveProperties.get(Options.OPTION_FILTER_ECLASS));
//
//		assertEquals(0, personCollection.countDocuments());
//		repository.save(bPerson, saveProperties);
//		assertEquals(1, personCollection.countDocuments());
//
//		Person person = TestFactory.eINSTANCE.createPerson();
//		person.setId("test");
//		person.setFirstName("Emil");
//		person.setLastName("Tester");
//		uri = repository.createUri(person);
//		assertEquals(clientUri + "/" + db + "/Person/test#test", uri.toString());
//
//		assertEquals(1, personCollection.countDocuments());
//		repository.save(person);
//		assertEquals(2, personCollection.countDocuments());
//
//		Address address = TestFactory.eINSTANCE.createAddress();
//		address.setId("adr");
//		address.setStreet("DateSquare");
//		address.setCity("You Nork");
//		uri = repository.createUri(address);
//		assertEquals(clientUri + "/" + db + "/Address/adr#adr", uri.toString());
//		saveProperties = new HashMap<String, Object>();
//		saveProperties.put(Options.OPTION_COLLECTION_NAME, TestPackage.eINSTANCE.getPerson().getName());
//		assertNull(saveProperties.get(Options.OPTION_STORE_SUPERTYPE));
//		uri = repository.createUri(address, saveProperties);
//		assertEquals(clientUri + "/" + db + "/Person/adr#adr", uri.toString());
//		assertEquals(Boolean.TRUE, saveProperties.get(Options.OPTION_STORE_SUPERTYPE));
//		assertEquals(TestPackage.Literals.ADDRESS, saveProperties.get(Options.OPTION_FILTER_ECLASS));
//
//		assertEquals(2, personCollection.countDocuments());
//		repository.save(address, saveProperties);
//		assertEquals(3, personCollection.countDocuments());
//
//		saveProperties = new HashMap<String, Object>();
//		saveProperties.put(Options.OPTION_COLLECTION_NAME, TestPackage.eINSTANCE.getPerson().getName());
//		assertNull(saveProperties.get(Options.OPTION_STORE_SUPERTYPE));
//
//
//		Resource ra = address.eResource();
//		assertNotNull(ra);
//		Resource rbp = bPerson.eResource();
//		assertNotNull(rbp);
//		Resource rp = person.eResource();
//		assertNotNull(rp);
//		ResourceSet rs = rbp.getResourceSet();
//		assertNotNull(rs);
//		assertEquals(3, rs.getResources().size());
//
//		repository.detach(address);
//		assertNull(address.eResource());
//		assertEquals(2, rs.getResources().size());
//		repository.detach(person);
//		assertNull(person.eResource());
//		assertEquals(1, rs.getResources().size());
//		repository.detach(bPerson);
//		assertNull(bPerson.eResource());
//		assertEquals(0, rs.getResources().size());
//
//		Person personResult = repository.getEObject(TestPackage.Literals.PERSON, "test");
//		assertNotNull(personResult);
//		assertNotEquals(person, personResult);
//		assertNotEquals(rp, personResult.eResource());
//
//		BusinessPerson bPersonResult = repository.getEObject(TestPackage.Literals.BUSINESS_PERSON, "bp");
//		assertNull(bPersonResult);
//		Map<String, Object> loadProperties = new HashMap<String, Object>();
//		loadProperties.put(Options.OPTION_COLLECTION_NAME, TestPackage.eINSTANCE.getPerson().getName());
//		bPersonResult = repository.getEObject(TestPackage.Literals.BUSINESS_PERSON, "bp", loadProperties);
//		assertNotNull(bPersonResult);
//		assertNotEquals(bPerson, bPersonResult);
//		assertNotEquals(rbp, bPersonResult.eResource());
//
//		List<EObject> addresses = repository.getAllEObjects(TestPackage.Literals.ADDRESS, null);
//		assertTrue(addresses.isEmpty());
//
//		addresses = repository.getAllEObjects(TestPackage.Literals.ADDRESS, loadProperties);
//		assertEquals(1, addresses.size());
//
//		List<Person> persons = repository.getAllEObjects(TestPackage.Literals.PERSON, loadProperties);
//		assertEquals(2, persons.size());
//
//		List<EObject> objects = repository.getAllEObjects(TestPackage.Literals.PERSON);
//		assertEquals(3, objects.size());
//	}
//
//	@Test
//	public void testEMFMongoRepository_UpdateCollectionHint() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//
//		// add service properties
//		String clientId = "testClient5";
//		String clientUri = "mongodb://" + mongoHost + ":27017";
//		Dictionary<String, Object> p = new Hashtable<String, Object>();
//		p.put(MongoClientProvider.PROP_CLIENT_ID, clientId);
//		p.put(MongoClientProvider.PROP_URI, clientUri);
//
//		defaultCheck();
//
//		ServiceChecker<MongoClientProvider> clientChecker = createTrackedChecker(MongoClientProvider.class).verbose(true).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.CLIENT_PID, "?", p);
//		clientChecker.assertCreations(1, true);
//
//		// add service properties
//		String dbAlias = "testDB";
//		String db = "test";
//		Dictionary<String, Object> dbp = new Hashtable<String, Object>();
//		dbp.put(MongoDatabaseProvider.PROP_ALIAS, dbAlias);
//		dbp.put(MongoDatabaseProvider.PROP_DATABASE, db);
//
//		ServiceChecker<MongoDatabaseProvider> dbChecker = createTrackedChecker(MongoDatabaseProvider.class).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.DATABASE_PID, "?", dbp);
//		dbChecker.assertCreations(1, true);
//
//		Dictionary<String, Object> properties = new Hashtable<>();
//
//		String repoId = "test_repo";
//		properties.put(EMFRepository.PROP_ID, repoId);
//		properties.put(EMFRepository.PROP_BASE_URI, clientUri + "/" + db);
//
//		ServiceChecker<?> repoChecker = createTrackedChecker("(" + EMFRepository.PROP_ID + "=" + repoId + ")", false).assertCreations(0, false);
//		createConfigForCleanup(EMFMongoConfiguratorConstants.SINGLETON_REPOSITORY_CONFIGURATION_NAME, "?", properties);
//		repoChecker.assertCreations(1, true);
//
//
//		MongoCollection<?> personCollection = getCollection(db, "Person");
//		MongoCollection<?> addressCollection = getCollection(db, "Address");
//		addressCollection.drop();
//		personCollection.drop();
//
//		CountDownLatch latch = new CountDownLatch(1);
//		latch.await(1, TimeUnit.SECONDS);
//
//		EMFRepository repository = (EMFRepository) repoChecker.trackedServiceNotNull().getTrackedService();
//
//		BusinessPerson bPerson = TestFactory.eINSTANCE.createBusinessPerson();
//		bPerson.setId("bp");
//		bPerson.setFirstName("Lord");
//		bPerson.setLastName("Business");
//
//		URI uri = repository.createUri(bPerson);
//		assertEquals(clientUri + "/" + db + "/BusinessPerson/bp#bp", uri.toString());
//		Map<String, Object> saveProperties = new HashMap<String, Object>();
//		saveProperties.put(Options.OPTION_COLLECTION_NAME, TestPackage.eINSTANCE.getPerson().getName());
//		assertNull(saveProperties.get(Options.OPTION_STORE_SUPERTYPE));
//		assertEquals(0, personCollection.countDocuments());
//		repository.save(bPerson, saveProperties);
//		assertEquals(Boolean.TRUE, saveProperties.get(Options.OPTION_STORE_SUPERTYPE));
//		assertEquals(TestPackage.Literals.BUSINESS_PERSON, saveProperties.get(Options.OPTION_FILTER_ECLASS));
//		assertEquals(1, personCollection.countDocuments());
//
//		Document document = (Document) personCollection.find().first();
//		assertEquals("Lord", document.get("firstName"));
//		assertTrue(document.containsKey(Keywords.SUPER_TYPES_KEY));
//
//		bPerson.setFirstName("Mr.");
//		saveProperties = new HashMap<String, Object>();
//		saveProperties.put(Options.OPTION_COLLECTION_NAME, TestPackage.eINSTANCE.getPerson().getName());
//		assertNull(saveProperties.get(Options.OPTION_STORE_SUPERTYPE));
//		repository.save(bPerson, saveProperties);
//
//		assertEquals(1, personCollection.countDocuments());
//
//		document = (Document) personCollection.find().first();
//		assertEquals("Mr.", document.get("firstName"));
//		assertTrue(document.containsKey(Keywords.SUPER_TYPES_KEY));
//	}
//
//	@Test(expected=ClassCastException.class)
//	public void testEMFMongoRepository_ErrorCollectionHint() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//
//		// add service properties
//		String clientId = "testClient6";
//		String clientUri = "mongodb://" + mongoHost + ":27017";
//		Dictionary<String, Object> p = new Hashtable<String, Object>();
//		p.put(MongoClientProvider.PROP_CLIENT_ID, clientId);
//		p.put(MongoClientProvider.PROP_URI, clientUri);
//
//		defaultCheck();
//
//		ServiceChecker<MongoClientProvider> clientChecker = createTrackedChecker(MongoClientProvider.class).verbose(true).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.CLIENT_PID, "?", p);
//		clientChecker.assertCreations(1, true);
//
//		// add service properties
//		String dbAlias = "testDB";
//		String db = "test";
//		Dictionary<String, Object> dbp = new Hashtable<String, Object>();
//		dbp.put(MongoDatabaseProvider.PROP_ALIAS, dbAlias);
//		dbp.put(MongoDatabaseProvider.PROP_DATABASE, db);
//
//		ServiceChecker<MongoDatabaseProvider> dbChecker = createTrackedChecker(MongoDatabaseProvider.class).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.DATABASE_PID, "?", dbp);
//		dbChecker.assertCreations(1, true);
//
//		Dictionary<String, Object> properties = new Hashtable<>();
//
//		String repoId = "test_repo";
//		properties.put(EMFRepository.PROP_ID, repoId);
//		properties.put(EMFRepository.PROP_BASE_URI, clientUri + "/" + db);
//
//		ServiceChecker<?> repoChecker = createTrackedChecker("(" + EMFRepository.PROP_ID + "=" + repoId + ")", false).assertCreations(0, false);
//		createConfigForCleanup(EMFMongoConfiguratorConstants.SINGLETON_REPOSITORY_CONFIGURATION_NAME, "?", properties);
//		repoChecker.assertCreations(1, true);
//
//		MongoCollection<?> personCollection = getCollection(db, "Person");
//		MongoCollection<?> addressCollection = getCollection(db, "Address");
//		addressCollection.drop();
//		personCollection.drop();
//
//		CountDownLatch latch = new CountDownLatch(1);
//		latch.await(1, TimeUnit.SECONDS);
//
//		EMFRepository repository = (EMFRepository) repoChecker.trackedServiceNotNull().getTrackedService();
//
//		BusinessPerson bPerson = TestFactory.eINSTANCE.createBusinessPerson();
//		bPerson.setId("bp");
//		bPerson.setFirstName("Lord");
//		bPerson.setLastName("Business");
//
//		URI uri = repository.createUri(bPerson);
//		assertEquals(clientUri + "/" + db + "/BusinessPerson/bp#bp", uri.toString());
//		Map<String, Object> saveProperties = new HashMap<String, Object>();
//		saveProperties.put(Options.OPTION_COLLECTION_NAME, TestPackage.eINSTANCE.getPerson().getName());
//		assertNull(saveProperties.get(Options.OPTION_STORE_SUPERTYPE));
//		uri = repository.createUri(bPerson, saveProperties);
//		assertEquals(clientUri + "/" + db + "/Person/bp#bp", uri.toString());
//		assertEquals(Boolean.TRUE, saveProperties.get(Options.OPTION_STORE_SUPERTYPE));
//		assertEquals(TestPackage.Literals.BUSINESS_PERSON, saveProperties.get(Options.OPTION_FILTER_ECLASS));
//
//		assertEquals(0, personCollection.countDocuments());
//		repository.save(bPerson, saveProperties);
//		assertEquals(1, personCollection.countDocuments());
//
//		Person person = TestFactory.eINSTANCE.createPerson();
//		person.setId("test");
//		person.setFirstName("Emil");
//		person.setLastName("Tester");
//		uri = repository.createUri(person);
//		assertEquals(clientUri + "/" + db + "/Person/test#test", uri.toString());
//
//		assertEquals(1, personCollection.countDocuments());
//		repository.save(person);
//		assertEquals(2, personCollection.countDocuments());
//
//		Address address = TestFactory.eINSTANCE.createAddress();
//		address.setId("adr");
//		address.setStreet("DateSquare");
//		address.setCity("You Nork");
//		uri = repository.createUri(address);
//		assertEquals(clientUri + "/" + db + "/Address/adr#adr", uri.toString());
//		saveProperties = new HashMap<String, Object>();
//		saveProperties.put(Options.OPTION_COLLECTION_NAME, TestPackage.eINSTANCE.getPerson().getName());
//		assertNull(saveProperties.get(Options.OPTION_STORE_SUPERTYPE));
//		uri = repository.createUri(address, saveProperties);
//		assertEquals(clientUri + "/" + db + "/Person/adr#adr", uri.toString());
//		assertEquals(Boolean.TRUE, saveProperties.get(Options.OPTION_STORE_SUPERTYPE));
//		assertEquals(TestPackage.Literals.ADDRESS, saveProperties.get(Options.OPTION_FILTER_ECLASS));
//
//		assertEquals(2, personCollection.countDocuments());
//		repository.save(address, saveProperties);
//		assertEquals(3, personCollection.countDocuments());
//
//		Resource ra = address.eResource();
//		assertNotNull(ra);
//		Resource rbp = bPerson.eResource();
//		assertNotNull(rbp);
//		Resource rp = person.eResource();
//		assertNotNull(rp);
//		ResourceSet rs = rbp.getResourceSet();
//		assertNotNull(rs);
//		assertEquals(3, rs.getResources().size());
//
//		repository.detach(address);
//		assertNull(address.eResource());
//		assertEquals(2, rs.getResources().size());
//		repository.detach(person);
//		assertNull(person.eResource());
//		assertEquals(1, rs.getResources().size());
//		repository.detach(bPerson);
//		assertNull(bPerson.eResource());
//		assertEquals(0, rs.getResources().size());
//
//		List<Person> objects = repository.getAllEObjects(TestPackage.Literals.PERSON);
//		@SuppressWarnings("unused")
//		Person result = objects.get(2);
//		fail("Should not reach this line");
//	}
//
//	@Test(expected = ConstraintValidationException.class)
//	public void testEMFMongoRepositorySaveMultiple1WithNonContainmentList() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//
//		// add service properties
//		String clientId = "testClient7";
//		String clientUri = "mongodb://" + mongoHost + ":27017";
//		Dictionary<String, Object> p = new Hashtable<String, Object>();
//		p.put(MongoClientProvider.PROP_CLIENT_ID, clientId);
//		p.put(MongoClientProvider.PROP_URI, clientUri);
//
//		defaultCheck();
//
//		ServiceChecker<MongoClientProvider> clientChecker = createTrackedChecker(MongoClientProvider.class).verbose(true).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.CLIENT_PID, "?", p);
//		clientChecker.assertCreations(1, true);
//
//		// add service properties
//		String dbAlias = "testDB";
//		String db = "test";
//		Dictionary<String, Object> dbp = new Hashtable<String, Object>();
//		dbp.put(MongoDatabaseProvider.PROP_ALIAS, dbAlias);
//		dbp.put(MongoDatabaseProvider.PROP_DATABASE, db);
//
//		ServiceChecker<MongoDatabaseProvider> dbChecker = createTrackedChecker(MongoDatabaseProvider.class).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.DATABASE_PID, "?", dbp);
//		dbChecker.assertCreations(1, true);
//
//		Dictionary<String, Object> properties = new Hashtable<>();
//
//		String repoId = "test_repo";
//		properties.put(EMFRepository.PROP_ID, repoId);
//		properties.put(EMFRepository.PROP_BASE_URI, clientUri + "/" + db);
//
//		ServiceChecker<?> repoChecker = createTrackedChecker("(" + EMFRepository.PROP_ID + "=" + repoId + ")", false).assertCreations(0, false);
//		createConfigForCleanup(EMFMongoConfiguratorConstants.SINGLETON_REPOSITORY_CONFIGURATION_NAME, "?", properties);
//		repoChecker.assertCreations(1, true);
//
//		MongoCollection<?> personCollection = getCollection(db, "Person");
//		MongoCollection<?> addressCollection = getCollection(db, "Address");
//		addressCollection.drop();
//		personCollection.drop();
//
//		CountDownLatch latch = new CountDownLatch(1);
//		latch.await(1, TimeUnit.SECONDS);
//
//		EMFRepository repository = (EMFRepository) repoChecker.trackedServiceNotNull().getTrackedService();
//
//		Person father = TestFactory.eINSTANCE.createPerson();
//		father.setFirstName("Father");
//		father.setLastName("Tester");
//		repository.save(father);
//		Person mother = TestFactory.eINSTANCE.createPerson();
//		mother.setFirstName("Mother");
//		mother.setLastName("Tester");
//		repository.save(mother);
//		Person child1 = TestFactory.eINSTANCE.createPerson();
//		child1.setFirstName("Child1");
//		child1.setLastName("Tester");
//		repository.save(child1);
//		Person child2 = TestFactory.eINSTANCE.createPerson();
//		child2.setFirstName("Child2");
//		child2.setLastName("Tester");
//		repository.save(child2);
//
//		Family fam = TestFactory.eINSTANCE.createFamily();
//		fam.setFather(father);
//		fam.setMother(mother);
//		fam.getChildren().add(child1);
//		fam.getChildren().add(child2);
//
//		repository.save(fam);
//
//		//Detach everything and save the Family again. The goal is to have the children as proxies and 
//		// to check if they stay proxies after saving to ensure that the check is not resolving them by accident.
//
//		child1.eResource().unload();
//		child2.eResource().unload();
//		father.eResource().unload();
//		mother.eResource().unload();
//
//		EObject fatherProxy = (EObject) fam.eGet(TestPackage.eINSTANCE.getFamily_Father(), false);
//
//		assertTrue(fatherProxy.eIsProxy());
//
//		BasicEList<Person> basicList = (BasicEList<Person>) fam.getChildren();
//		assertTrue(basicList.basicGet(0).eIsProxy());
//
//		repository.save(fam);
//
//		fatherProxy = (EObject) fam.eGet(TestPackage.eINSTANCE.getFamily_Father(), false);
//
//		basicList = (BasicEList<Person>) fam.getChildren();
//		assertTrue(basicList.basicGet(0).eIsProxy());
//
//		//Now we Except it to fail
//		Person childToDetache = fam.getChildren().get(0);
//		repository.detach(childToDetache);
//
//		personCollection.drop();
//
//		repository.save(fam);
//
//	}
//
//	@Test
//	public void testEMFMongoRepositorySaveMultiple2() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//
//		// add service properties
//		String clientId = "testClient8";
//		String clientUri = "mongodb://" + mongoHost + ":27017";
//		Dictionary<String, Object> p = new Hashtable<String, Object>();
//		p.put(MongoClientProvider.PROP_CLIENT_ID, clientId);
//		p.put(MongoClientProvider.PROP_URI, clientUri);
//
//		defaultCheck();
//
//		ServiceChecker<MongoClientProvider> clientChecker = createTrackedChecker(MongoClientProvider.class).verbose(true).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.CLIENT_PID, "?", p);
//		clientChecker.assertCreations(1, true);
//
//		// add service properties
//		String dbAlias = "testDB";
//		String db = "test";
//		Dictionary<String, Object> dbp = new Hashtable<String, Object>();
//		dbp.put(MongoDatabaseProvider.PROP_ALIAS, dbAlias);
//		dbp.put(MongoDatabaseProvider.PROP_DATABASE, db);
//
//		ServiceChecker<MongoDatabaseProvider> dbChecker = createTrackedChecker(MongoDatabaseProvider.class).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.DATABASE_PID, "?", dbp);
//		dbChecker.assertCreations(1, true);
//
//		Dictionary<String, Object> properties = new Hashtable<>();
//
//		String repoId = "test_repo";
//		properties.put(EMFRepository.PROP_ID, repoId);
//		properties.put(EMFRepository.PROP_BASE_URI, clientUri + "/" + db);
//
//		ServiceChecker<?> repoChecker = createTrackedChecker("(" + EMFRepository.PROP_ID + "=" + repoId + ")", false).assertCreations(0, false);
//		createConfigForCleanup(EMFMongoConfiguratorConstants.SINGLETON_REPOSITORY_CONFIGURATION_NAME, "?", properties);
//		repoChecker.assertCreations(1, true);
//
//		MongoCollection<?> personCollection = getCollection(db, "Person");
//		MongoCollection<?> addressCollection = getCollection(db, "Address");
//		addressCollection.drop();
//		personCollection.drop();
//
//		CountDownLatch latch = new CountDownLatch(1);
//		latch.await(1, TimeUnit.SECONDS);
//
//		EMFRepository repository = (EMFRepository) repoChecker.trackedServiceNotNull().getTrackedService();
//
//		Address address = repository.createProxy(TestPackage.eINSTANCE.getAddress(), "testAddress");
//
//		Person person1 = TestFactory.eINSTANCE.createPerson();
//		person1.setId("test1");
//		person1.setFirstName("Emil");
//		person1.setLastName("Tester");
//		person1.setAddress(address);
//		URI uri = repository.createUri(person1);
//		assertEquals(clientUri + "/" + db + "/Person/test1#test1", uri.toString());
//		Person person2 = TestFactory.eINSTANCE.createPerson();
//		person2.setId("test2");
//		person2.setFirstName("Emil");
//		person2.setLastName("Tester");
//		person2.setAddress(address);
//		uri = repository.createUri(person2);
//		assertEquals(clientUri + "/" + db + "/Person/test2#test2", uri.toString());
//		assertEquals(0, personCollection.countDocuments());
//
//		repository.save(person1, person2);
//	}
//
//	@Test
//	public void testEMFMongoRepositorySaveMultiple_UseIdAsPK() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//
//		// add service properties
//		String clientId = "testClient9";
//		String clientUri = "mongodb://" + mongoHost + ":27017";
//		Dictionary<String, Object> p = new Hashtable<String, Object>();
//		p.put(MongoClientProvider.PROP_CLIENT_ID, clientId);
//		p.put(MongoClientProvider.PROP_URI, clientUri);
//
//		defaultCheck();
//
//		ServiceChecker<MongoClientProvider> clientChecker = createTrackedChecker(MongoClientProvider.class).verbose(true).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.CLIENT_PID, "?", p);
//		clientChecker.assertCreations(1, true);
//
//		// add service properties
//		String dbAlias = "testDB";
//		String db = "test";
//		Dictionary<String, Object> dbp = new Hashtable<String, Object>();
//		dbp.put(MongoDatabaseProvider.PROP_ALIAS, dbAlias);
//		dbp.put(MongoDatabaseProvider.PROP_DATABASE, db);
//
//		ServiceChecker<MongoDatabaseProvider> dbChecker = createTrackedChecker(MongoDatabaseProvider.class).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.DATABASE_PID, "?", dbp);
//		dbChecker.assertCreations(1, true);
//
//		Dictionary<String, Object> properties = new Hashtable<>();
//
//		String repoId = "test_repo";
//		properties.put(EMFRepository.PROP_ID, repoId);
//		properties.put(EMFRepository.PROP_BASE_URI, clientUri + "/" + db);
//
//		ServiceChecker<?> repoChecker = createTrackedChecker("(" + EMFRepository.PROP_ID + "=" + repoId + ")", false).assertCreations(0, false);
//		createConfigForCleanup(EMFMongoConfiguratorConstants.SINGLETON_REPOSITORY_CONFIGURATION_NAME, "?", properties);
//		repoChecker.assertCreations(1, true);
//
//		MongoCollection<?> personCollection = getCollection(db, "Person");
//		MongoCollection<?> addressCollection = getCollection(db, "Address");
//		addressCollection.drop();
//		personCollection.drop();
//
//		CountDownLatch latch = new CountDownLatch(1);
//		latch.await(1, TimeUnit.SECONDS);
//
//		EMFRepository repository = (EMFRepository) repoChecker.trackedServiceNotNull().getTrackedService();
//
//		Address address = repository.createProxy(TestPackage.eINSTANCE.getAddress(), "testAddress");
//
//		Person person1 = TestFactory.eINSTANCE.createPerson();
//		person1.setId("test1");
//		person1.setFirstName("Emil");
//		person1.setLastName("Tester");
//		person1.setAddress(address);
//
//		Map<String, Object> idProps = new HashMap<String, Object>();
//		idProps.put(Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY, Boolean.FALSE);
//
//		URI uri = repository.createUri(person1, idProps);
//		assertEquals(clientUri + "/" + db + "/Person/#test1", uri.toString());
//		Person person2 = TestFactory.eINSTANCE.createPerson();
//		person2.setId("test2");
//		person2.setFirstName("Emil");
//		person2.setLastName("Tester");
//		person2.setAddress(address);
//		uri = repository.createUri(person2, idProps);
//		assertEquals(clientUri + "/" + db + "/Person/#test2", uri.toString());
//		assertEquals(0, personCollection.countDocuments());
//
//		repository.save(idProps, person1, person2);
//
//	}
//
//	@Test (expected = ConstraintValidationException.class)
//	public void testEMFMongoRepositorySaveMultiple1WithConstraintError() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//
//		// add service properties
//		String clientId = "testClient10";
//		String clientUri = "mongodb://" + mongoHost + ":27017";
//		Dictionary<String, Object> p = new Hashtable<String, Object>();
//		p.put(MongoClientProvider.PROP_CLIENT_ID, clientId);
//		p.put(MongoClientProvider.PROP_URI, clientUri);
//
//		defaultCheck();
//
//		ServiceChecker<MongoClientProvider> clientChecker = createTrackedChecker(MongoClientProvider.class).verbose(true).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.CLIENT_PID, "?", p);
//		clientChecker.assertCreations(1, true);
//
//		// add service properties
//		String dbAlias = "testDB";
//		String db = "test";
//		Dictionary<String, Object> dbp = new Hashtable<String, Object>();
//		dbp.put(MongoDatabaseProvider.PROP_ALIAS, dbAlias);
//		dbp.put(MongoDatabaseProvider.PROP_DATABASE, db);
//
//		ServiceChecker<MongoDatabaseProvider> dbChecker = createTrackedChecker(MongoDatabaseProvider.class).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.DATABASE_PID, "?", dbp);
//		dbChecker.assertCreations(1, true);
//
//		Dictionary<String, Object> properties = new Hashtable<>();
//
//		String repoId = "test_repo";
//		properties.put(EMFRepository.PROP_ID, repoId);
//		properties.put(EMFRepository.PROP_BASE_URI, clientUri + "/" + db);
//
//		ServiceChecker<?> repoChecker = createTrackedChecker("(" + EMFRepository.PROP_ID + "=" + repoId + ")", false).assertCreations(0, false);
//		createConfigForCleanup(EMFMongoConfiguratorConstants.SINGLETON_REPOSITORY_CONFIGURATION_NAME, "?", properties);
//		repoChecker.assertCreations(1, true);
//
//		MongoCollection<?> personCollection = getCollection(db, "Person");
//		MongoCollection<?> addressCollection = getCollection(db, "Address");
//		addressCollection.drop();
//		personCollection.drop();
//
//		CountDownLatch latch = new CountDownLatch(1);
//		latch.await(1, TimeUnit.SECONDS);
//
//		EMFRepository repository = (EMFRepository) repoChecker.trackedServiceNotNull().getTrackedService();
//
//		Address address = TestFactory.eINSTANCE.createAddress();
//		address.setId("testAddress");
//
//		Person person = TestFactory.eINSTANCE.createPerson();
//		person.setId("test");
//		person.setFirstName("Emil");
//		person.setLastName("Tester");
//		person.setAddress(address);
//		URI uri = repository.createUri(person);
//		assertEquals(clientUri + "/" + db + "/Person/test#test", uri.toString());
//		assertEquals(0, personCollection.countDocuments());
//
//		repository.save(person);
//		fail("Should not reach this block");
//	}
//
//	@Test(expected = ConstraintValidationException.class)
//	public void testEMFMongoRepositorySaveMultiple2WithConstraintError() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//
//		// add service properties
//		String clientId = "testClient11";
//		String clientUri = "mongodb://" + mongoHost + ":27017";
//		Dictionary<String, Object> p = new Hashtable<String, Object>();
//		p.put(MongoClientProvider.PROP_CLIENT_ID, clientId);
//		p.put(MongoClientProvider.PROP_URI, clientUri);
//
//		defaultCheck();
//
//		ServiceChecker<MongoClientProvider> clientChecker = createTrackedChecker(MongoClientProvider.class).verbose(true).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.CLIENT_PID, "?", p);
//		clientChecker.assertCreations(1, true);
//
//		// add service properties
//		String dbAlias = "testDB";
//		String db = "test";
//		Dictionary<String, Object> dbp = new Hashtable<String, Object>();
//		dbp.put(MongoDatabaseProvider.PROP_ALIAS, dbAlias);
//		dbp.put(MongoDatabaseProvider.PROP_DATABASE, db);
//
//		ServiceChecker<MongoDatabaseProvider> dbChecker = createTrackedChecker(MongoDatabaseProvider.class).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.DATABASE_PID, "?", dbp);
//		dbChecker.assertCreations(1, true);
//
//		Dictionary<String, Object> properties = new Hashtable<>();
//
//		String repoId = "test_repo";
//		properties.put(EMFRepository.PROP_ID, repoId);
//		properties.put(EMFRepository.PROP_BASE_URI, clientUri + "/" + db);
//
//		ServiceChecker<?> repoChecker = createTrackedChecker("(" + EMFRepository.PROP_ID + "=" + repoId + ")", false).assertCreations(0, false);
//		createConfigForCleanup(EMFMongoConfiguratorConstants.SINGLETON_REPOSITORY_CONFIGURATION_NAME, "?", properties);
//		repoChecker.assertCreations(1, true);
//
//		MongoCollection<?> personCollection = getCollection(db, "Person");
//		MongoCollection<?> addressCollection = getCollection(db, "Address");
//		addressCollection.drop();
//		personCollection.drop();
//
//		CountDownLatch latch = new CountDownLatch(1);
//		latch.await(1, TimeUnit.SECONDS);
//
//		EMFRepository repository = (EMFRepository) repoChecker.trackedServiceNotNull().getTrackedService();
//
//		Address address = TestFactory.eINSTANCE.createAddress();
//		address.setId("testAddress");
//
//		Person person1 = TestFactory.eINSTANCE.createPerson();
//		person1.setId("test1");
//		person1.setFirstName("Emil");
//		person1.setLastName("Tester");
//		person1.setAddress(address);
//		URI uri = repository.createUri(person1);
//		assertEquals(clientUri + "/" + db + "/Person/test1#test1", uri.toString());
//		Person person2 = TestFactory.eINSTANCE.createPerson();
//		person2.setId("test2");
//		person2.setFirstName("Emil");
//		person2.setLastName("Tester");
//		person2.setAddress(address);
//		uri = repository.createUri(person2);
//		assertEquals(clientUri + "/" + db + "/Person/test2#test2", uri.toString());
//		assertEquals(0, personCollection.countDocuments());
//
//		repository.save(person1, person2);
//		fail("Should not reach this block");
//	}
//
//	@Test(expected = ConstraintValidationException.class)
//	public void testEMFMongoRepositorySaveMultiple_UseIdAsPKWithConstraintError() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//
//		// add service properties
//		String clientId = "testClient12";
//		String clientUri = "mongodb://" + mongoHost + ":27017";
//		Dictionary<String, Object> p = new Hashtable<String, Object>();
//		p.put(MongoClientProvider.PROP_CLIENT_ID, clientId);
//		p.put(MongoClientProvider.PROP_URI, clientUri);
//
//		defaultCheck();
//
//		ServiceChecker<MongoClientProvider> clientChecker = createTrackedChecker(MongoClientProvider.class).verbose(true).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.CLIENT_PID, "?", p);
//		clientChecker.assertCreations(1, true);
//
//		// add service properties
//		String dbAlias = "testDB";
//		String db = "test";
//		Dictionary<String, Object> dbp = new Hashtable<String, Object>();
//		dbp.put(MongoDatabaseProvider.PROP_ALIAS, dbAlias);
//		dbp.put(MongoDatabaseProvider.PROP_DATABASE, db);
//
//		ServiceChecker<MongoDatabaseProvider> dbChecker = createTrackedChecker(MongoDatabaseProvider.class).assertCreations(0, false);
//		createConfigForCleanup(ConfigurationProperties.DATABASE_PID, "?", dbp);
//		dbChecker.assertCreations(1, true);
//
//		Dictionary<String, Object> properties = new Hashtable<>();
//
//		String repoId = "test_repo";
//		properties.put(EMFRepository.PROP_ID, repoId);
//		properties.put(EMFRepository.PROP_BASE_URI, clientUri + "/" + db);
//
//		ServiceChecker<?> repoChecker = createTrackedChecker("(" + EMFRepository.PROP_ID + "=" + repoId + ")", false).assertCreations(0, false);
//		createConfigForCleanup(EMFMongoConfiguratorConstants.SINGLETON_REPOSITORY_CONFIGURATION_NAME, "?", properties);
//		repoChecker.assertCreations(1, true);
//
//		MongoCollection<?> personCollection = getCollection(db, "Person");
//		MongoCollection<?> addressCollection = getCollection(db, "Address");
//		addressCollection.drop();
//		personCollection.drop();
//
//		CountDownLatch latch = new CountDownLatch(1);
//		latch.await(1, TimeUnit.SECONDS);
//
//		EMFRepository repository = (EMFRepository) repoChecker.trackedServiceNotNull().getTrackedService();
//
//		Address address = TestFactory.eINSTANCE.createAddress();
//		address.setId("testAddress");
//
//		Person person1 = TestFactory.eINSTANCE.createPerson();
//		person1.setId("test1");
//		person1.setFirstName("Emil");
//		person1.setLastName("Tester");
//		person1.setAddress(address);
//
//		Map<String, Object> idProps = new HashMap<String, Object>();
//		idProps.put(Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY, Boolean.FALSE);
//
//		URI uri = repository.createUri(person1, idProps);
//		assertEquals(clientUri + "/" + db + "/Person/#test1", uri.toString());
//		Person person2 = TestFactory.eINSTANCE.createPerson();
//		person2.setId("test2");
//		person2.setFirstName("Emil");
//		person2.setLastName("Tester");
//		person2.setAddress(address);
//		uri = repository.createUri(person2, idProps);
//		assertEquals(clientUri + "/" + db + "/Person/#test2", uri.toString());
//		assertEquals(0, personCollection.countDocuments());
//
//		repository.save(idProps, person1, person2);
//		fail("Should not reach this block");
//
//	}
//}
