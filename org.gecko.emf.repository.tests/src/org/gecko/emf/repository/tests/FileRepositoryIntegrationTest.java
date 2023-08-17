/**
 * Copyright (c) 2012 - 2022 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.repository.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.emf.osgi.ResourceSetFactory;
import org.gecko.emf.osgi.example.model.basic.Address;
import org.gecko.emf.osgi.example.model.basic.BasicFactory;
import org.gecko.emf.osgi.example.model.basic.BasicPackage;
import org.gecko.emf.osgi.example.model.basic.Person;
import org.gecko.emf.repository.EMFReadRepository;
import org.gecko.emf.repository.EMFRepository;
import org.gecko.emf.repository.EMFWriteRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.osgi.service.cm.Configuration;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.config.InjectConfiguration;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.common.dictionary.Dictionaries;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
@WithFactoryConfiguration(name="1", factoryPid = "EMFFileRepository", location = "?")
public class FileRepositoryIntegrationTest {

	@Test
	public void testEMFRepositorySaveLoad(
			@InjectService(filter = "(" + EMFRepository.PROP_ID + "=test_repo)", cardinality = 0) ServiceAware<EMFWriteRepository> writeRepositoryAware, 
			@InjectService(filter = "(" + EMFRepository.PROP_ID + "=test_repo)", cardinality = 0) ServiceAware<EMFReadRepository> readRepositoryAware, 
			@InjectConfiguration("EMFFileRepository~1") Configuration config,
			@TempDir Path tempDir) throws InterruptedException, IOException {
		
		assertTrue(writeRepositoryAware.isEmpty());
		assertTrue(readRepositoryAware.isEmpty());
		
		Dictionary<String, Object> properties = new Hashtable<>();
		
		String repoId = "test_repo";
		String baseFolder = tempDir.toString();
		properties.put(EMFRepository.PROP_ID, repoId);
		properties.put(EMFRepository.PROP_BASE_URI, baseFolder);
		properties.put(EMFRepository.PROP_CONTENT_TYPE, "ecore");
		config.update(properties);
		
		EMFWriteRepository writeRepository = writeRepositoryAware.waitForService(5000l);
		EMFReadRepository readRepository = readRepositoryAware.waitForService(5000l);
		assertNotNull(writeRepository);
		assertNotNull(readRepository);
		
		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setId("test");
		person.setFirstName("Emil");
		person.setLastName("Tester");
		URI uri = URI.createFileURI("/" + tempDir.toString() + "/Person/" + person.getId());
		File testPersonFile = Path.of(tempDir.toString(), "/Person/" + person.getId()).toFile();
		assertFalse(testPersonFile.exists());
		writeRepository.save(person, uri);
		assertTrue(testPersonFile.exists());
		
		Resource r = person.eResource();
		assertNotNull(r);
		ResourceSet rs = r.getResourceSet();
		assertNotNull(rs);
		assertEquals(1, rs.getResources().size());
		
		writeRepository.getHelper().detach(person);
		assertNull(person.eResource());
		assertEquals(0, rs.getResources().size());
		
		Person personResult = readRepository.getEObject(uri);
		assertNotNull(personResult);
		assertNotEquals(person, personResult);
		assertNotEquals(r, personResult.eResource());
		
		assertTrue(EcoreUtil.equals(person, personResult));
		
		config.delete();
		
		Thread.sleep(500);
		
		assertTrue(writeRepositoryAware.isEmpty());
		assertTrue(readRepositoryAware.isEmpty());
		
	}

	@Test
	public void testEMFRepositorySaveLoadWithReference(
			@InjectService(filter = "(" + EMFRepository.PROP_ID + "=test_repo)", cardinality = 0) ServiceAware<EMFWriteRepository> writeRepositoryAware, 
			@InjectService(filter = "(" + EMFRepository.PROP_ID + "=test_repo)", cardinality = 0) ServiceAware<EMFReadRepository> readRepositoryAware, 
			@InjectConfiguration("EMFFileRepository~1") Configuration config,
			@TempDir Path tempDir) throws InterruptedException, IOException {
		
		assertTrue(writeRepositoryAware.isEmpty());
		assertTrue(readRepositoryAware.isEmpty());
		
		Dictionary<String, Object> properties = new Hashtable<>();
		
		String repoId = "test_repo";
		properties.put(EMFRepository.PROP_ID, repoId);
		properties.put(EMFRepository.PROP_BASE_URI, "file:///" + tempDir.toString());
		properties.put(EMFRepository.PROP_CONTENT_TYPE, "ecore");
		config.update(properties);
		
		EMFWriteRepository writeRepository = writeRepositoryAware.waitForService(5000l);
		EMFReadRepository readRepository = readRepositoryAware.waitForService(5000l);
		assertNotNull(writeRepository);
		assertNotNull(readRepository);
		
		Address a = BasicFactory.eINSTANCE.createAddress();
		a.setId("address");
		
		writeRepository.save(a);
		
		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setId("test");
		person.setFirstName("Emil");
		person.setLastName("Tester");
		person.setAddress(a);
		URI uri = URI.createFileURI("/" + tempDir.toString() + "/Person/" + person.getId());
		File testPersonFile = Path.of(tempDir.toString(), "/Person/" + person.getId()).toFile();
		assertFalse(testPersonFile.exists());
		writeRepository.save(person);
		assertTrue(testPersonFile.exists());
		
		Resource r = person.eResource();
		assertNotNull(r);
		ResourceSet rs = r.getResourceSet();
		assertNotNull(rs);
		assertEquals(2, rs.getResources().size());
		
		writeRepository.getHelper().detach(person);
		assertNull(person.eResource());
		assertEquals(1, rs.getResources().size());
		
		Person personResult = readRepository.getEObject(uri);
		assertNotNull(personResult);
		assertNotEquals(person, personResult);
		assertNotEquals(r, personResult.eResource());
		
		assertTrue(EcoreUtil.equals(person, personResult));
		config.delete();
		
		Thread.sleep(200);
		assertTrue(writeRepositoryAware.isEmpty());
		assertTrue(readRepositoryAware.isEmpty());
	}

	@Test
	@Disabled
	public void testEMFRepositorySaveLoadUnregisteredPackage(
			@InjectService(filter = "(" + EMFRepository.PROP_ID + "=test_repo)", cardinality = 0) ServiceAware<EMFWriteRepository> writeRepositoryAware, 
			@InjectService(filter = "(" + EMFRepository.PROP_ID + "=test_repo)", cardinality = 0) ServiceAware<EMFReadRepository> readRepositoryAware, 
			@InjectConfiguration("EMFFileRepository~1") Configuration config,
			@InjectService ResourceSetFactory rsf,
			@TempDir Path tempDir) throws InterruptedException, IOException {

		assertTrue(writeRepositoryAware.isEmpty());
		assertTrue(readRepositoryAware.isEmpty());

		assertNotNull(config);
		
		String repoId = "test_repo";
		String baseFolder = tempDir.toString();
		Dictionary<String, String> dictionary = Dictionaries.dictionaryOf(EMFRepository.PROP_ID, repoId, EMFRepository.PROP_BASE_URI, baseFolder, EMFRepository.PROP_CONTENT_TYPE, "ecore");
		config.update(dictionary);

		EMFWriteRepository writeRepository = writeRepositoryAware.waitForService(5000l);
		EMFReadRepository readRepository = readRepositoryAware.waitForService(5000l);
		assertNotNull(writeRepository);
		assertNotNull(readRepository);

		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setId("test");
		person.setFirstName("Emil");
		person.setLastName("Tester");
		URI uri = URI.createFileURI("/" + baseFolder + "/testperson.test");
		File testPersonFile = new File(baseFolder, "testperson.test");
		assertFalse(testPersonFile.exists());
		writeRepository.save(person, uri);
		assertTrue(testPersonFile.exists());

//		// unregister the EPackage for test
//		testPackageRegistration.unregister();
//		testPackageRegistration = null;

		Resource r = person.eResource();
		assertNotNull(r);
		ResourceSet rs = r.getResourceSet();
		assertNotNull(rs);
		assertEquals(1, rs.getResources().size());

		writeRepository.getHelper().detach(person);
		assertNull(person.eResource());
		assertEquals(0, rs.getResources().size());

		org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, ()->{
			try {
				readRepository.getEObject(uri);
				fail("Not expected to have a return object here");
			} finally {
				config.delete();
				assertTrue(writeRepositoryAware.isEmpty());
				assertTrue(readRepositoryAware.isEmpty());
			}
		});
	}
	
	@Test
	public void testEMFRepositoryNoContent(
			@InjectService(filter = "(" + EMFRepository.PROP_ID + "=test_repo)", cardinality = 0) ServiceAware<EMFWriteRepository> writeRepositoryAware, 
			@InjectService(filter = "(" + EMFRepository.PROP_ID + "=test_repo)", cardinality = 0) ServiceAware<EMFReadRepository> readRepositoryAware, 
			@InjectConfiguration("EMFFileRepository~1") Configuration config,
			@TempDir Path tempDir) throws InterruptedException, IOException {
		
		assertTrue(writeRepositoryAware.isEmpty());
		assertTrue(readRepositoryAware.isEmpty());

		Dictionary<String, Object> properties = new Hashtable<>();
		String repoId = "test_repo";
		properties.put(EMFRepository.PROP_ID, repoId);
		properties.put(EMFRepository.PROP_BASE_URI, tempDir.toString());
		properties.put(EMFRepository.PROP_CONTENT_TYPE, "ecore");
		config.update(properties);

		EMFWriteRepository writeRepository = writeRepositoryAware.waitForService(5000l);
		EMFReadRepository readRepository = readRepositoryAware.waitForService(5000l);
		assertNotNull(writeRepository);
		assertNotNull(readRepository);

		URI uri = URI.createFileURI("/" + tempDir.toString() + "/Person/test");
		File testPersonFile = Path.of(tempDir.toString(), "/Person/test").toFile();
		assertFalse(testPersonFile.exists());
		Person personResult = readRepository.getEObject(uri);
		assertNull(personResult);
		assertFalse(testPersonFile.exists());

		config.delete();

		Thread.sleep(200);
		assertTrue(writeRepositoryAware.isEmpty());
		assertTrue(readRepositoryAware.isEmpty());
	}
	
	@Test
	public void testEMFRepositorySaveExistsURI(
			@InjectService(filter = "(" + EMFRepository.PROP_ID + "=test_repo)", cardinality = 0) ServiceAware<EMFWriteRepository> writeRepositoryAware, 
			@InjectService(filter = "(" + EMFRepository.PROP_ID + "=test_repo)", cardinality = 0) ServiceAware<EMFReadRepository> readRepositoryAware, 
			@InjectConfiguration("EMFFileRepository~1") Configuration config,
			@TempDir Path tempDir) throws InterruptedException, IOException {
		
		assertTrue(writeRepositoryAware.isEmpty());
		assertTrue(readRepositoryAware.isEmpty());
		
		Dictionary<String, Object> properties = new Hashtable<>();
		
		String repoId = "test_repo";
		String baseFolder = "file:/"+tempDir.toString(); //the "file:/" is needed otherwise when looking for the URI it does not construct it properly
		properties.put(EMFRepository.PROP_ID, repoId);
		properties.put(EMFRepository.PROP_BASE_URI, baseFolder);
		properties.put(EMFRepository.PROP_CONTENT_TYPE, "ecore");
		config.update(properties);
		
		EMFWriteRepository writeRepository = writeRepositoryAware.waitForService(5000l);
		EMFReadRepository readRepository = readRepositoryAware.waitForService(5000l);
		assertNotNull(writeRepository);
		assertNotNull(readRepository);
		
		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setId("test");
		person.setFirstName("Emil");
		person.setLastName("Tester");
		URI uri = URI.createFileURI("/" + tempDir.toString() + "/Person/" + person.getId());
		assertFalse(readRepository.exists(uri));		
		writeRepository.save(person, uri);
		assertTrue(readRepository.exists(uri));
		
		config.delete();
		
		Thread.sleep(200);
		assertTrue(writeRepositoryAware.isEmpty());
		assertTrue(readRepositoryAware.isEmpty());
	}
	
	@Test
	public void testEMFRepositorySaveExistsEClass(
			@InjectService(filter = "(" + EMFRepository.PROP_ID + "=test_repo)", cardinality = 0) ServiceAware<EMFWriteRepository> writeRepositoryAware, 
			@InjectService(filter = "(" + EMFRepository.PROP_ID + "=test_repo)", cardinality = 0) ServiceAware<EMFReadRepository> readRepositoryAware, 
			@InjectConfiguration("EMFFileRepository~1") Configuration config,
			@TempDir Path tempDir) throws InterruptedException, IOException {
		
		assertTrue(writeRepositoryAware.isEmpty());
		assertTrue(readRepositoryAware.isEmpty());
		
		Dictionary<String, Object> properties = new Hashtable<>();
		
		String repoId = "test_repo";
		
		String baseFolder = "file:/"+tempDir.toString(); //the "file:/" is needed otherwise when looking for the URI it does not construct it properly
		properties.put(EMFRepository.PROP_ID, repoId);
		properties.put(EMFRepository.PROP_BASE_URI, baseFolder);
		properties.put(EMFRepository.PROP_CONTENT_TYPE, "ecore");
		config.update(properties);
		
		EMFWriteRepository writeRepository = writeRepositoryAware.waitForService(5000l);
		EMFReadRepository readRepository = readRepositoryAware.waitForService(5000l);
		assertNotNull(writeRepository);
		assertNotNull(readRepository);
		
		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setId("test");
		person.setFirstName("Emil");
		person.setLastName("Tester");
		URI uri = URI.createFileURI("/" + tempDir.toString() + "/Person/" + person.getId());
		assertFalse(readRepository.exists(BasicPackage.eINSTANCE.getPerson(), "test"));		
		writeRepository.save(person, uri);
		assertTrue(readRepository.exists(BasicPackage.eINSTANCE.getPerson(), "test"));		
		
		config.delete();
		
		Thread.sleep(200);
		assertTrue(writeRepositoryAware.isEmpty());
		assertTrue(readRepositoryAware.isEmpty());
	}
	
	@Test
	public void testEMFRepositorySaveExistsWithReferenceURI(
			@InjectService(filter = "(" + EMFRepository.PROP_ID + "=test_repo)", cardinality = 0) ServiceAware<EMFWriteRepository> writeRepositoryAware, 
			@InjectService(filter = "(" + EMFRepository.PROP_ID + "=test_repo)", cardinality = 0) ServiceAware<EMFReadRepository> readRepositoryAware, 
			@InjectConfiguration("EMFFileRepository~1") Configuration config,
			@TempDir Path tempDir) throws InterruptedException, IOException {
		
		assertTrue(writeRepositoryAware.isEmpty());
		assertTrue(readRepositoryAware.isEmpty());
		
		Dictionary<String, Object> properties = new Hashtable<>();
		
		String repoId = "test_repo";
		properties.put(EMFRepository.PROP_ID, repoId);
		String baseFolder = "file:/"+tempDir.toString(); //the "file:/" is needed otherwise when looking for the URI it does not construct it properly
		properties.put(EMFRepository.PROP_BASE_URI, baseFolder);
		properties.put(EMFRepository.PROP_CONTENT_TYPE, "ecore");
		config.update(properties);
		
		EMFWriteRepository writeRepository = writeRepositoryAware.waitForService(5000l);
		EMFReadRepository readRepository = readRepositoryAware.waitForService(5000l);
		assertNotNull(writeRepository);
		assertNotNull(readRepository);
		
		Address a = BasicFactory.eINSTANCE.createAddress();
		a.setId("address");
		
		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setId("test");
		person.setFirstName("Emil");
		person.setLastName("Tester");
		person.setAddress(a);
		URI uriPerson = URI.createFileURI("/" + tempDir.toString() + "/Person/" + person.getId());
		URI uriAddress = URI.createFileURI("/" + tempDir.toString() + "/Address/" + a.getId());

		assertFalse(readRepository.exists(uriPerson));
		assertFalse(readRepository.exists(uriAddress));
		
		writeRepository.save(a);
		writeRepository.save(person);
		
		assertTrue(readRepository.exists(uriPerson));
		assertTrue(readRepository.exists(uriAddress));
		
		config.delete();
		
		Thread.sleep(200);
		assertTrue(writeRepositoryAware.isEmpty());
		assertTrue(readRepositoryAware.isEmpty());
	}
	
	@Test
	public void testEMFRepositorySaveExistsWithReferenceEClass(
			@InjectService(filter = "(" + EMFRepository.PROP_ID + "=test_repo)", cardinality = 0) ServiceAware<EMFWriteRepository> writeRepositoryAware, 
			@InjectService(filter = "(" + EMFRepository.PROP_ID + "=test_repo)", cardinality = 0) ServiceAware<EMFReadRepository> readRepositoryAware, 
			@InjectConfiguration("EMFFileRepository~1") Configuration config,
			@TempDir Path tempDir) throws InterruptedException, IOException {
		
		assertTrue(writeRepositoryAware.isEmpty());
		assertTrue(readRepositoryAware.isEmpty());
		
		Dictionary<String, Object> properties = new Hashtable<>();
		
		String repoId = "test_repo";
		properties.put(EMFRepository.PROP_ID, repoId);
		String baseFolder = "file:/"+tempDir.toString(); //the "file:/" is needed otherwise when looking for the URI it does not construct it properly
		properties.put(EMFRepository.PROP_BASE_URI, baseFolder);
		properties.put(EMFRepository.PROP_CONTENT_TYPE, "ecore");
		config.update(properties);
		
		EMFWriteRepository writeRepository = writeRepositoryAware.waitForService(5000l);
		EMFReadRepository readRepository = readRepositoryAware.waitForService(5000l);
		assertNotNull(writeRepository);
		assertNotNull(readRepository);
		
		Address a = BasicFactory.eINSTANCE.createAddress();
		a.setId("address");
		
		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setId("test");
		person.setFirstName("Emil");
		person.setLastName("Tester");
		person.setAddress(a);
		
		assertFalse(readRepository.exists(BasicPackage.eINSTANCE.getPerson(), person.getId()));
		assertFalse(readRepository.exists(BasicPackage.eINSTANCE.getAddress(), a.getId()));
		
		writeRepository.save(a);
		writeRepository.save(person);
		
		assertTrue(readRepository.exists(BasicPackage.eINSTANCE.getPerson(), person.getId()));
		assertTrue(readRepository.exists(BasicPackage.eINSTANCE.getAddress(), a.getId()));
		
		config.delete();
		
		Thread.sleep(200);
		assertTrue(writeRepositoryAware.isEmpty());
		assertTrue(readRepositoryAware.isEmpty());
	}

}