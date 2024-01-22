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

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.bson.Document;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.gecko.emf.mongo.Options;
import org.gecko.emf.osgi.annotation.require.RequireEMF;
import org.gecko.emf.osgi.constants.EMFNamespaces;
import org.gecko.emf.osgi.example.model.basic.Address;
import org.gecko.emf.osgi.example.model.basic.BasicFactory;
import org.gecko.emf.osgi.example.model.basic.BasicPackage;
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
public class MongoProxyQueryAppenderTest extends MongoEMFSetting {

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
	 * Test creation of object and returning results
	 * 
	 * @throws IOException
	 * @throws BundleException
	 * @throws InvalidSyntaxException
	 * @throws InterruptedException
	 */
	@Test
	public void testSaveLoadProxyObjects()
			throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
		ResourceSet resourceSet = rsAware.getService();

		System.out.println("Dropping DB");
		MongoCollection<Document> bpCollection = client.getDatabase("test").getCollection("Person");
		MongoCollection<Document> addressCollection = client.getDatabase("test").getCollection("Address");
		bpCollection.drop();
		addressCollection.drop();

		assertEquals(0, bpCollection.countDocuments());
		Resource resourceAddress = resourceSet
				.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Address/testAddress"));
		Resource resourcePerson = resourceSet
				.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Person/testPerson"));

		Address address = BasicFactory.eINSTANCE.createAddress();
		address.setId("testAddress");
		address.setCity("city");
		address.setStreet("street");
		address.setZip("0123");

		Person p = BasicFactory.eINSTANCE.createPerson();
		p.setFirstName("firstName");
		p.setLastName("lastName");
		p.setId("testPerson");
		p.setAddress(address);

		resourceAddress.getContents().add(address);
		resourceAddress.save(null);

		resourcePerson.getContents().add(p);
		resourcePerson.save(null);

		/*
		 * Find person in the collection
		 */
		// long start = System.currentTimeMillis();
		Resource findResource = resourceSet.createResource(URI.createURI(resourcePerson.getURI().toString()));

		Map<EReference, String> proxyQuery = Collections.singletonMap(BasicPackage.Literals.PERSON__ADDRESS,
				"{\"projection\":{\"city\" : 1, \"street\" : 1}}");

		findResource.load(Collections.singletonMap(Options.OPTION_QUERY_FOR_PROXIES, proxyQuery));

		// get the person
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());

		Person findPerson = (Person) findResource.getContents().get(0);

		assertEquals(p.getFirstName(), findPerson.getFirstName());

		assertNotNull(findPerson.getAddress());

		Address findAddress = findPerson.getAddress();
		assertFalse(findAddress.eIsProxy());
		assertEquals(address.getCity(), findAddress.getCity());
		assertEquals(address.getStreet(), findAddress.getStreet());
		assertNull(findAddress.getZip());

		bpCollection.drop();
		addressCollection.drop();
	}

}
