/**
 * Copyright (c) 2012 - 2020 Data In Motion and others.
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
import java.util.UUID;

import org.bson.Document;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.gecko.emf.osgi.annotation.require.RequireEMF;
import org.gecko.emf.osgi.constants.EMFNamespaces;
import org.gecko.emf.osgi.example.model.basic.BasicFactory;
import org.gecko.emf.osgi.example.model.basic.Geometry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
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
 * Tests the correct implementation of the ArrayConverter and the changes made
 * to the DBObjectBuilder and EObjectBuilder to handle multi-dimensional arrays
 * 
 * @author ilenia
 * @since May 14, 2020
 */
@RequireEMF
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
@WithFactoryConfiguration(name = "mongoClient", location = "?", factoryPid = "MongoClientProvider", properties = {
		@Property(key = "client_id", value = "test"), @Property(key = "uri", value = "mongodb://localhost:27017") })
@WithFactoryConfiguration(name = "mongoDatabase", location = "?", factoryPid = "MongoDatabaseProvider", properties = {
		@Property(key = "alias", value = "TestDB"), @Property(key = "database", value = "test") })
public class CustomArrayDataTypeTest extends MongoEMFSetting {
	
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

	@Test
	public void testSimpleArray() throws IOException, InvalidSyntaxException, InterruptedException {

		ResourceSet resourceSet = rsAware.waitForService(2000l);

		System.out.println("Dropping DB");
		MongoCollection<Document> geoCollection = client.getDatabase("test").getCollection("Geometry");
		geoCollection.drop();

		assertEquals(0, geoCollection.countDocuments());
		Resource resource = resourceSet
				.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Geometry/"));

//		Create the Geometry object
		Geometry geometry = BasicFactory.eINSTANCE.createGeometry();
		Double[] coord1 = new Double[] { 11.23, 58.98 };
		Double[] coord2 = new Double[] { 11.45, 57.44 };

		geometry.getCoordinates().add(coord1);
		geometry.getCoordinates().add(coord2);
		String geoId = UUID.randomUUID().toString();
		geometry.setId(geoId);

//		save the Geometry object
		resource.getContents().add(geometry);
		resource.save(null);

		resource.getContents().clear();
		resource.unload();

		// load the Geometry object from the db
		Resource findResource = resourceSet
				.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Geometry/" + geometry.getId()));
		findResource.load(null);
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());

		assertTrue(findResource.getContents().get(0) instanceof Geometry);
		Geometry retrievedGeometry = (Geometry) findResource.getContents().get(0);
		assertEquals(geoId, retrievedGeometry.getId());
		assertNotNull(retrievedGeometry.getCoordinates());
		assertFalse(retrievedGeometry.getCoordinates().isEmpty());
		assertEquals(2, retrievedGeometry.getCoordinates().size());
		assertTrue(retrievedGeometry.getCoordinates().get(0) instanceof Double[]);
		Double[] retrievedCoord1 = retrievedGeometry.getCoordinates().get(0);
		assertEquals(2, retrievedCoord1.length);
		assertEquals(11.23, retrievedCoord1[0], 0.001);
		assertEquals(58.98, retrievedCoord1[1], 0.001);

		assertTrue(retrievedGeometry.getCoordinates().get(1) instanceof Double[]);
		Double[] retrievedCoord2 = retrievedGeometry.getCoordinates().get(1);
		assertEquals(2, retrievedCoord2.length);
		assertEquals(11.45, retrievedCoord2[0], 0.001);
		assertEquals(57.44, retrievedCoord2[1], 0.001);

		geoCollection.drop();
	}

	@Test
	public void testMultiDimensionalArray() throws IOException, InvalidSyntaxException, InterruptedException {
		ResourceSet resourceSet = rsAware.waitForService(2000l);
		
		System.out.println("Dropping DB");
		MongoCollection<Document> geoCollection = client.getDatabase("test").getCollection("Geometry");
		geoCollection.drop();

		assertEquals(0, geoCollection.countDocuments());
		Resource resource = resourceSet
				.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Geometry/"));

//		Create the Geometry object
		Geometry geometry = BasicFactory.eINSTANCE.createGeometry();
		Double[][] multiDimensionalArray = new Double[2][];
		Double[] coord1 = new Double[] { 11.23, 58.98 };
		Double[] coord2 = new Double[] { 11.45, 57.44 };
		multiDimensionalArray[0] = coord1;
		multiDimensionalArray[1] = coord2;
		geometry.getMultiDimensionalCoordinates().add(multiDimensionalArray);
		String geoId = UUID.randomUUID().toString();
		geometry.setId(geoId);

//		save the Geometry object
		resource.getContents().add(geometry);
		resource.save(null);

		resource.getContents().clear();
		resource.unload();

		// load the Geometry object from the db
		Resource findResource = resourceSet
				.createResource(URI.createURI("mongodb://" + mongoHost + ":27017/test/Geometry/" + geometry.getId()));
		findResource.load(null);
		assertNotNull(findResource);
		assertFalse(findResource.getContents().isEmpty());
		assertEquals(1, findResource.getContents().size());

		assertTrue(findResource.getContents().get(0) instanceof Geometry);
		Geometry retrievedGeometry = (Geometry) findResource.getContents().get(0);
		assertEquals(geoId, retrievedGeometry.getId());
		assertNotNull(retrievedGeometry.getMultiDimensionalCoordinates());
		assertFalse(retrievedGeometry.getMultiDimensionalCoordinates().isEmpty());
		assertEquals(1, retrievedGeometry.getMultiDimensionalCoordinates().size());
		assertTrue(retrievedGeometry.getMultiDimensionalCoordinates().get(0) instanceof Double[][]);
		Double[][] retrievedMultiDCoord = retrievedGeometry.getMultiDimensionalCoordinates().get(0);
		assertEquals(2, retrievedMultiDCoord.length);

		assertTrue(retrievedMultiDCoord[0] instanceof Double[]);
		Double[] retrievedCoord1 = retrievedMultiDCoord[0];
		assertEquals(2, retrievedCoord1.length);
		assertEquals(11.23, retrievedCoord1[0], 0.001);
		assertEquals(58.98, retrievedCoord1[1], 0.001);

		assertTrue(retrievedMultiDCoord[1] instanceof Double[]);
		Double[] retrievedCoord2 = retrievedMultiDCoord[1];
		assertEquals(2, retrievedCoord2.length);
		assertEquals(11.45, retrievedCoord2[0], 0.001);
		assertEquals(57.44, retrievedCoord2[1], 0.001);

		geoCollection.drop();

	}

}
