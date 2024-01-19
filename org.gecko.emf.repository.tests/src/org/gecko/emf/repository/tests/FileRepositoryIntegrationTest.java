package org.gecko.emf.repository.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.emf.osgi.ResourceSetFactory;
import org.gecko.emf.osgi.constants.EMFNamespaces;
import org.gecko.emf.osgi.example.model.basic.Address;
import org.gecko.emf.osgi.example.model.basic.BasicFactory;
import org.gecko.emf.osgi.example.model.basic.Person;
import org.gecko.emf.repository.EMFRepository;
import org.gecko.emf.repository.file.annotations.RequireFileEMFRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;
import org.osgi.util.tracker.ServiceTracker;

@RequireFileEMFRepository
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(MockitoExtension.class)
public class FileRepositoryIntegrationTest {

	@InjectBundleContext
	BundleContext context;
	@InjectService
	ConfigurationAdmin ca;
	private FolderHelper folderHelper;

	@BeforeEach
	public void before() {
		folderHelper = new FolderHelper("EMFFileRepository/" + UUID.randomUUID().toString());
		folderHelper.initialize();
	}

	@AfterEach
	public void after() {
		folderHelper.dispose();
	}

	@Test
	public void testEMFRepositorySaveLoad(@InjectService(filter = "(" + EMFNamespaces.EMF_MODEL_NAME + "=basic)") ServiceAware<ResourceSetFactory> rsfAware) throws IOException, InterruptedException, InvalidSyntaxException {
		assertNotNull(ca);
		assertFalse(rsfAware.isEmpty());
		
		ResourceSetFactory rsf = rsfAware.getService();
		assertNotNull(rsf);

		ServiceReference<EMFRepository> repoRef = context.getServiceReference(EMFRepository.class);
		assertNull(repoRef);

		Configuration config = ca.createFactoryConfiguration("EMFFileRepository", "?");
		assertNotNull(config);
		Dictionary<String, Object> properties = new Hashtable<>();

		String repoId = "test_repo";
		String baseFolder = folderHelper.getFolderPath();
		properties.put(EMFRepository.PROP_ID, repoId);
		properties.put(EMFRepository.PROP_BASE_URI, baseFolder);
		properties.put(EMFRepository.PROP_CONTENT_TYPE, "ecore");
		config.update(properties);

		EMFRepository repository = getService(5000l, "(" + EMFRepository.PROP_ID + "=" + repoId + ")");
		assertNotNull(repository);

		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setId("test");
		person.setFirstName("Emil");
		person.setLastName("Tester");
		URI uri = URI.createFileURI("/" + folderHelper.getFolderPath() + "/testperson.test");
		File testPersonFile = new File(folderHelper.getFolderPathFile(), "testperson.test");
		assertFalse(testPersonFile.exists());
		repository.save(person, uri);
		assertTrue(testPersonFile.exists());

		Resource r = person.eResource();
		assertNotNull(r);
		ResourceSet rs = r.getResourceSet();
		assertNotNull(rs);
		assertEquals(1, rs.getResources().size());

		repository.detach(person);
		assertNull(person.eResource());
		assertEquals(0, rs.getResources().size());

		Person personResult = repository.getEObject(uri);
		assertNotNull(personResult);
		assertNotEquals(person, personResult);
		assertNotEquals(r, personResult.eResource());

		assertTrue(EcoreUtil.equals(person, personResult));

		config.delete();

		Thread.sleep(1000l);

		repoRef = context.getServiceReference(EMFRepository.class);
		assertNull(repoRef);
	}

	@Test
	public void testEMFRepositorySaveLoadWithReference(@InjectService(filter = "(" + EMFNamespaces.EMF_MODEL_NAME + "=basic)") ServiceAware<ResourceSetFactory> rsfAware) throws IOException, InterruptedException, InvalidSyntaxException {
		assertNotNull(ca);
		assertFalse(rsfAware.isEmpty());
		
		ResourceSetFactory rsf = rsfAware.getService();
		assertNotNull(rsf);
		
		ServiceReference<EMFRepository> repoRef = context.getServiceReference(EMFRepository.class);
		assertNull(repoRef);

		Configuration config = ca.createFactoryConfiguration("EMFFileRepository", "?");
		assertNotNull(config);
		Dictionary<String, Object> properties = new Hashtable<>();

		String repoId = "test_repo";
		String baseFolder = folderHelper.getFolderPath();
		properties.put(EMFRepository.PROP_ID, repoId);
		properties.put(EMFRepository.PROP_BASE_URI, "file:///" + baseFolder);
		properties.put(EMFRepository.PROP_CONTENT_TYPE, "ecore");
		config.update(properties);

		EMFRepository repository = getService(5000l, "(" + EMFRepository.PROP_ID + "=" + repoId + ")");
		assertNotNull(repository);

		Address a = BasicFactory.eINSTANCE.createAddress();
		a.setId("address");

		repository.save(a);

		Person person = BasicFactory.eINSTANCE.createPerson();
		person.setId("test");
		person.setFirstName("Emil");
		person.setLastName("Tester");
		person.setAddress(a);
		URI uri = URI.createFileURI("/" + folderHelper.getFolderPath() + "/Person/test");
		File testPersonFile = new File(folderHelper.getFolderPathFile(), "/Person/test");
		repository.save(person);
		assertTrue(testPersonFile.exists());

		Resource r = person.eResource();
		assertNotNull(r);
		ResourceSet rs = r.getResourceSet();
		assertNotNull(rs);
		assertEquals(2, rs.getResources().size());

		repository.detach(person);
		assertNull(person.eResource());
		assertEquals(1, rs.getResources().size());

		Person personResult = repository.getEObject(uri);
		assertNotNull(personResult);
		assertNotEquals(person, personResult);
		assertNotEquals(r, personResult.eResource());

		assertTrue(EcoreUtil.equals(person, personResult));

		config.delete();

		Thread.sleep(1000l);

		repoRef = context.getServiceReference(EMFRepository.class);
		assertNull(repoRef);
	}

	// TODO Update to used OSGi-Test. We need a manual package registration here
//	@Test
//	public void testEMFRepositorySaveLoadUnregisteredPackage(@InjectService(filter = "emf.model.name=basic") ServiceAware<ResourceSetFactory> rsfAware) throws IOException, InterruptedException, InvalidSyntaxException {
//		assertNotNull(ca);
//		assertFalse(rsfAware.isEmpty());
//		
//		ResourceSetFactory rsf = rsfAware.getService();
//		assertNotNull(rsf);
//
//		final ServiceReference<EMFRepository> repoRef = context.getServiceReference(EMFRepository.class);
//		assertNull(repoRef);
//		
//
//		Configuration config = ca.createFactoryConfiguration("EMFFileRepository", "?");
//		assertNotNull(config);
//		Dictionary<String, Object> properties = new Hashtable<>();
//
//		String repoId = "test_repo";
//		String baseFolder = folderHelper.getFolderPath();
//		properties.put(EMFRepository.PROP_ID, repoId);
//		properties.put(EMFRepository.PROP_BASE_URI, baseFolder);
//		properties.put(EMFRepository.PROP_CONTENT_TYPE, "ecore");
//		config.update(properties);
//
//		EMFRepository repository = getService(5000l, "(" + EMFRepository.PROP_ID + "=" + repoId + ")");
//		assertNotNull(repository);
//
//		Person person = BasicFactory.eINSTANCE.createPerson();
//		person.setId("test");
//		person.setFirstName("Emil");
//		person.setLastName("Tester");
//		URI uri = URI.createFileURI("/" + folderHelper.getFolderPath() + "/testperson.test");
//		File testPersonFile = new File(folderHelper.getFolderPathFile(), "testperson.test");
//		assertFalse(testPersonFile.exists());
//		repository.save(person, uri);
//		assertTrue(testPersonFile.exists());
//
//		// unregister the EPackage for test
//		testPackageRegistration.unregister();
//		testPackageRegistration = null;
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
//		assertThrows(IllegalStateException.class, ()->{
//			try {
//				repository.getEObject(uri);
//				fail("Not expected to have a return object here");
//			} finally {
//				config.delete();
//				Thread.sleep(1000l);
//				repoRef = context.getServiceReference(EMFRepository.class);
//				assertNull(repoRef);
//			}});
//	}

	@Test
	public void testEMFRepositoryNoContent(@InjectService(filter = "(" + EMFNamespaces.EMF_MODEL_NAME + "=basic)") ServiceAware<ResourceSetFactory> rsfAware) throws IOException, InterruptedException, InvalidSyntaxException {
		assertNotNull(ca);
		assertFalse(rsfAware.isEmpty());
		ResourceSetFactory rsf = rsfAware.getService();
		assertNotNull(rsf);

		ServiceReference<EMFRepository> repoRef = context.getServiceReference(EMFRepository.class);
		assertNull(repoRef);

		Configuration config = ca.createFactoryConfiguration("EMFFileRepository", "?");
		assertNotNull(config);
		Dictionary<String, Object> properties = new Hashtable<>();

		String repoId = "test_repo";
		String baseFolder = folderHelper.getFolderPath();
		properties.put(EMFRepository.PROP_ID, repoId);
		properties.put(EMFRepository.PROP_BASE_URI, baseFolder);
		properties.put(EMFRepository.PROP_CONTENT_TYPE, "ecore");
		config.update(properties);

		EMFRepository repository = getService(5000l, "(" + EMFRepository.PROP_ID + "=" + repoId + ")");
		assertNotNull(repository);

		URI uri = URI.createFileURI("/" + folderHelper.getFolderPath() + "/testperson.test");
		File testPersonFile = new File(folderHelper.getFolderPathFile(), "testperson.test");
		assertFalse(testPersonFile.exists());
		Person personResult = repository.getEObject(uri);
		assertNull(personResult);
		assertFalse(testPersonFile.exists());

		config.delete();

		Thread.sleep(1000l);

		repoRef = context.getServiceReference(EMFRepository.class);
		assertNull(repoRef);
	}

	<T> ServiceReference<T> getServiceReference(long timeout, String filter) throws InterruptedException, InvalidSyntaxException {
		Filter f = FrameworkUtil.createFilter(filter);
		ServiceTracker<T, T> tracker = new ServiceTracker<>(context, f, null);
		tracker.open();
		tracker.waitForService(timeout);
		return tracker.getServiceReference();
	}

	<T> T getService(long timeout, String filter) throws InterruptedException, InvalidSyntaxException {
		Filter f = FrameworkUtil.createFilter(filter);
		ServiceTracker<T, T> tracker = new ServiceTracker<>(context, f, null);
		tracker.open();
		return tracker.waitForService(timeout);
	}

}