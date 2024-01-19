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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.collection.ECollection;
import org.gecko.collection.EReferenceCollection;
import org.gecko.emf.mongo.Options;
import org.gecko.emf.mongo.handlers.MongoResourceSetConfigurator;
import org.gecko.emf.osgi.ResourceSetFactory;
import org.gecko.emf.osgi.annotation.require.RequireEMF;
import org.gecko.emf.osgi.configurator.ResourceSetConfigurator;
import org.gecko.emf.osgi.constants.EMFNamespaces;
import org.gecko.emf.osgi.example.model.basic.BasicFactory;
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
public class MongoIteratorIntegrationTest extends MongoEMFSetting {
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
	 * Test creation of many objects and returning results as iterator
	 * @throws IOException 
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testCreateAndFindObjectsIterator() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {

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
		resourceSet.getLoadOptions().put(Options.OPTION_QUERY_CURSOR, Boolean.TRUE);
		findResource.load(resourceSet.getLoadOptions());
		// get the persons
		System.out.println("Finding all persons with a size " + insertSize  + " took " + (System.currentTimeMillis() - start) + " ms");
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());
		assertTrue(findResource.getContents().get(0) instanceof ECollection);
		
		EReferenceCollection result = (EReferenceCollection) findResource.getContents().get(0);
		
		/*
		 * Iterating over the result and getting the real objects
		 */
		start = System.currentTimeMillis();
		List<Person> resultList = new ArrayList<Person>();
		// iterate over all elements
		result.getValues().forEach((eo)->{
			Person person = (Person) eo;
			resultList.add(person);
		});
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

}
