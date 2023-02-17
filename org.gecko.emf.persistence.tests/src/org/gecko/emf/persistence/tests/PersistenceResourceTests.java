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
package org.gecko.emf.persistence.tests;

import static org.gecko.emf.persistence.helper.EMFHelper.getResponse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.gecko.emf.persistence.api.Countable;
import org.gecko.emf.persistence.api.Deletable;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.api.Readable;
import org.gecko.emf.persistence.api.Updateable;
import org.gecko.emf.persistence.engine.PersistenceEngine;
import org.gecko.emf.persistence.engine.PersistenceEngineFactory;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.resource.PersistenceResource;
import org.gecko.emf.persistence.resource.PersistenceResourceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.osgi.framework.BundleContext;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * See documentation here: 
 * 	https://github.com/osgi/osgi-test
 * 	https://github.com/osgi/osgi-test/wiki
 * Examples: https://github.com/osgi/osgi-test/tree/main/examples
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(MockitoExtension.class)
public class PersistenceResourceTests {

	private PersistenceResourceFactory resourceFactory;
	private PersistenceEngine<String, EObjectMapper, String, String, String> engine01;
	private PersistenceEngine<String, EObjectMapper, String, String, String> engine02;
	@Mock
	private PersistenceEngineFactory  ef01;
	@Mock
	private PersistenceEngineFactory  ef02;

	@BeforeEach
	public void before(@InjectBundleContext BundleContext ctx) throws PersistenceException {
		resourceFactory = Mockito.mock(PersistenceResourceFactory.class, 
				Mockito.withSettings().
				useConstructor().
				defaultAnswer(Mockito.CALLS_REAL_METHODS));
		engine01 = Mockito.mock(PersistenceEngine.class, Mockito.withSettings());
		engine02 = Mockito.mock(PersistenceEngine.class, Mockito.withSettings().extraInterfaces(Readable.class, Updateable.class, Countable.class, Deletable.class));
		assertNotNull(resourceFactory);
		assertNotNull(engine01);
		assertNotNull(engine02);
	}


	@Test
	public void testAllMethodsBasic() throws PersistenceException {
		when(ef01.createEngine(any(URI.class))).thenReturn(engine01);
		when(ef01.canHandle(any(URI.class))).thenReturn(true);
		when(ef02.canHandle(any(URI.class))).thenReturn(false);
		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		try (PersistenceResource pr01 = (PersistenceResource) resource) {
			// No ComponentServiceObjects are registered
			verify(ef01, times(1)).createEngine(any(URI.class));
			verify(ef01, times(1)).canHandle(any(URI.class));
			verify(ef02, never()).createEngine(any(URI.class));
			verify(ef02, never()).canHandle(any(URI.class));

			assertEquals(engine01, pr01.getEngine());

			assertThrows(UnsupportedOperationException.class, ()->pr01.save(null));
			assertThrows(UnsupportedOperationException.class, ()->pr01.load(null));
			assertThrows(UnsupportedOperationException.class, ()->pr01.count(null));
			assertThrows(UnsupportedOperationException.class, ()->pr01.exist(null));
			assertThrows(UnsupportedOperationException.class, ()->pr01.delete(null));

			assertThrows(UnsupportedOperationException.class, ()->pr01.save(Collections.emptyMap()));
			assertThrows(UnsupportedOperationException.class, ()->pr01.load(Collections.emptyMap()));
			assertThrows(UnsupportedOperationException.class, ()->pr01.count(Collections.emptyMap()));
			assertThrows(UnsupportedOperationException.class, ()->pr01.exist(Collections.emptyMap()));
			assertThrows(UnsupportedOperationException.class, ()->pr01.delete(Collections.emptyMap()));

		} catch (Exception e) {
			fail("Unexpected exception on close");
		}

		reset(ef01, ef02);
		
		when(ef02.createEngine(any(URI.class))).thenReturn(engine02);
		when(ef01.canHandle(any(URI.class))).thenReturn(false);
		when(ef02.canHandle(any(URI.class))).thenReturn(true);
		uri = URI.createURI("dummy://bar/world");
		resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		try (PersistenceResource pr02 = (PersistenceResource) resource) {
			// No ComponentServiceObjects are registered
			verify(ef01, never()).createEngine(any(URI.class));
			verify(ef01, times(1)).canHandle(any(URI.class));
			verify(ef02, times(1)).createEngine(any(URI.class));
			verify(ef02, times(1)).canHandle(any(URI.class));

			assertEquals(engine02, pr02.getEngine());

			assertDoesNotThrow(()->pr02.load(null));
			assertDoesNotThrow(()->pr02.save(null));
			assertDoesNotThrow(()->pr02.count(null));
			assertDoesNotThrow(()->pr02.exist(null));
			assertDoesNotThrow(()->pr02.delete(null));


			assertDoesNotThrow(()->pr02.load(Collections.emptyMap()));
			assertDoesNotThrow(()->pr02.save(Collections.emptyMap()));
			assertDoesNotThrow(()->pr02.count(Collections.emptyMap()));
			assertDoesNotThrow(()->pr02.exist(Collections.emptyMap()));
			assertDoesNotThrow(()->pr02.delete(Collections.emptyMap()));
		} catch (Exception e) {
			fail("Unexpected exception on close");
		}

	}
	
	/**
	 * Tests, if the loaded state is correctly
	 * @throws PersistenceException
	 */
	@Test
	public void testLoadState() throws PersistenceException {
		when(ef02.createEngine(any(URI.class))).thenReturn(engine02);
		when(ef02.canHandle(any(URI.class))).thenReturn(true);
		resourceFactory.addEngine(ef02);
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		try (PersistenceResource pr = (PersistenceResource) resource) {
			
			assertEquals(engine02, pr.getEngine());
			
			Readable re02 = (Readable) engine02;
			doAnswer(defaultResoureActionAnswer(null, 1111)).when(re02).read(anyMap());
			Updateable ue02 = (Updateable) engine02;
			doAnswer(defaultResoureActionAnswer(null, 2222)).when(ue02).create(anyMap());
			Deletable de02 = (Deletable) engine02;
			when(de02.delete(anyMap())).then(defaultResoureActionAnswer(true, 3333));
			
			pr.save(null);
			assertEquals(2222, pr.getTimeStamp());
			assertFalse(pr.isLoaded());
			assertFalse(pr.isModified());
			
			pr.load(null);
			assertTrue(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(1111, pr.getTimeStamp());

			// reset mocks
			reset(re02);
			doAnswer(defaultResoureActionAnswer(null, 1112)).when(re02).read(anyMap());
			doAnswer(defaultResoureActionAnswer(null, 2222)).when(ue02).update(anyMap());
			when(de02.delete(anyMap())).then(defaultResoureActionAnswer(true, 3333));
			
			// load again
			pr.load(null);
			assertTrue(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(1111, pr.getTimeStamp());
			
			// after unload the timestamp will be reseted
			pr.unload();
			assertEquals(-1, pr.getTimeStamp());
			
			// after load the load timestamp will be correct now
			pr.load(null);
			assertTrue(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(1112, pr.getTimeStamp());

			pr.delete(null);
			assertFalse(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(-1, pr.getTimeStamp());
			pr.unload();
			
			pr.delete(null);
			assertFalse(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(3333, pr.getTimeStamp());
			
			// You can save after load
			pr.load(null);
			assertTrue(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(1112, pr.getTimeStamp());
			
			pr.save(null);
			assertTrue(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(2222, pr.getTimeStamp());
			
			// A loaded resource will be unloaded after delete
			pr.delete(null);
			assertFalse(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(-1, pr.getTimeStamp());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception on close", e); 
		}
		
	}
	
	/**
	 * Tests, if the count and exist state is correctly. It should work, depending on, if a resource was loaded or not
	 * @throws PersistenceException
	 */
	@Test
	public void testCountableState() throws PersistenceException {
		when(ef02.createEngine(any(URI.class))).thenReturn(engine02);
		when(ef02.canHandle(any(URI.class))).thenReturn(true);
		resourceFactory.addEngine(ef02);
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		try (PersistenceResource pr = (PersistenceResource) resource) {
			
			assertEquals(engine02, pr.getEngine());
			
			Readable re02 = (Readable) engine02;
			doAnswer((i)-> {
				Object result = createDefaultAnswer(null, 1111, i);
				pr.getContents().add(EcoreFactory.eINSTANCE.createEClass());
				pr.getContents().add(EcoreFactory.eINSTANCE.createEAttribute());
				return result;
			}).when(re02).read(anyMap());
			Countable ce02 = (Countable) engine02;
			when(ce02.count(anyMap())).then(defaultResoureActionAnswer(42l, 5555));
			
			assertEquals(42, pr.count(null));
			assertEquals(5555, pr.getTimeStamp()); 
			
			assertFalse(pr.isLoaded());
			assertFalse(pr.isModified());
			
			pr.load(null);
			
			assertTrue(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(1111, pr.getTimeStamp());
			
			assertEquals(2, pr.count(null));
			assertEquals(1111, pr.getTimeStamp());
			assertTrue(pr.isLoaded());
			assertFalse(pr.isModified());
			
			pr.unload();
			when(ce02.exist(anyMap())).then(defaultResoureActionAnswer(true, 4444));
			
			assertTrue(pr.exist(null));
			assertEquals(4444, pr.getTimeStamp()); 
			reset(ce02);
			when(ce02.exist(anyMap())).then(defaultResoureActionAnswer(false, 4445));
			doAnswer((i)-> {
				Object result = createDefaultAnswer(null, 1112, i);
				pr.getContents().add(EcoreFactory.eINSTANCE.createEClass());
				pr.getContents().add(EcoreFactory.eINSTANCE.createEAttribute());
				return result;
			}).when(re02).read(anyMap());
			
			assertFalse(pr.exist(null));
			assertEquals(4445, pr.getTimeStamp()); 
			assertFalse(pr.isLoaded());
			assertFalse(pr.isModified());
			
			pr.load(null);
			
			assertTrue(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(1112, pr.getTimeStamp());
			
			assertTrue(pr.exist(null));
			assertTrue(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(1112, pr.getTimeStamp()); 
			
			pr.unload();
			
			assertFalse(pr.exist(null));
			assertEquals(4445, pr.getTimeStamp()); 
			assertFalse(pr.isLoaded());
			assertFalse(pr.isModified());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception on close", e); 
		}
		
	}
	
	/**
	 * Tests, if the save state is correctly. It should work, no matter, if resource is in laoded state or not
	 * @throws PersistenceException
	 */
	@Test
	public void testSaveState() throws PersistenceException {
		when(ef02.createEngine(any(URI.class))).thenReturn(engine02);
		when(ef02.canHandle(any(URI.class))).thenReturn(true);
		resourceFactory.addEngine(ef02);
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		try (PersistenceResource pr = (PersistenceResource) resource) {
			
			assertEquals(engine02, pr.getEngine());
			
			Readable re02 = (Readable) engine02;
			doAnswer(defaultResoureActionAnswer(null, 1111)).when(re02).read(anyMap());
			Updateable ue02 = (Updateable) engine02;
			doAnswer(defaultResoureActionAnswer(null, 2222)).when(ue02).create(anyMap());
			doAnswer(defaultResoureActionAnswer(null, 22222)).when(ue02).update(anyMap());
			
			pr.save(null);
			assertEquals(2222, pr.getTimeStamp());
			assertFalse(pr.isLoaded());
			assertFalse(pr.isModified());
			
			pr.save(null);
			assertEquals(22222, pr.getTimeStamp());
			assertFalse(pr.isLoaded());
			assertFalse(pr.isModified());

			pr.load(null);
			assertTrue(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(1111, pr.getTimeStamp());
			
			reset(ue02);
			doAnswer(defaultResoureActionAnswer(null, 22223)).when(ue02).update(anyMap());
			
			pr.save(null);
			assertEquals(22223, pr.getTimeStamp());
			assertTrue(pr.isLoaded());
			assertFalse(pr.isModified());

			reset(ue02);
			doAnswer(defaultResoureActionAnswer(null, 22224)).when(ue02).update(anyMap());
			pr.unload();
			
			pr.save(null);
			assertEquals(22224, pr.getTimeStamp());
			assertFalse(pr.isLoaded());
			assertFalse(pr.isModified());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception on close", e); 
		}
		
	}
	
	/**
	 * Tests the internal handle response method, tha is different for load and save operatons
	 * @throws PersistenceException
	 */
	@Test
	public void testHandleResponse() throws PersistenceException {
		when(ef02.createEngine(any(URI.class))).thenReturn(engine02);
		when(ef02.canHandle(any(URI.class))).thenReturn(true);
		resourceFactory.addEngine(ef02);
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		try (PersistenceResource pr = (PersistenceResource) resource) {
			
			assertEquals(engine02, pr.getEngine());
			
			Readable re02 = (Readable) engine02;
			doAnswer(defaultResoureActionAnswer(null, 1111)).when(re02).read(anyMap());
			Updateable ue02 = (Updateable) engine02;
			doAnswer(defaultResoureActionAnswer(null, 2222)).when(ue02).create(anyMap());
			doAnswer(defaultResoureActionAnswer(null, 22222)).when(ue02).update(anyMap());
			Deletable de02 = (Deletable) engine02;
			when(de02.delete(anyMap())).then(defaultResoureActionAnswer(true, 3333));
			Countable ce02 = (Countable) engine02;
			when(ce02.exist(anyMap())).then(defaultResoureActionAnswer(true, 4444));
			when(ce02.count(anyMap())).then(defaultResoureActionAnswer(1l, 5555));

			pr.save(null);
			assertEquals(2222, pr.getTimeStamp());
			assertFalse(pr.isLoaded());
			assertFalse(pr.isModified());
			pr.save(null);
			assertFalse(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(22222, pr.getTimeStamp());
			pr.load(null);
			assertTrue(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(1111, pr.getTimeStamp());
			pr.unload();
			pr.delete(null);
			assertFalse(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(3333, pr.getTimeStamp());
			pr.count(null);
			assertFalse(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(5555, pr.getTimeStamp());
			pr.exist(null);
			assertFalse(pr.isLoaded());
			assertFalse(pr.isModified());
			assertEquals(4444, pr.getTimeStamp());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception on close", e); 
		}
		
	}

	/**
	 * Test, if the response options was set correctly
	 * @throws PersistenceException
	 */
	@Test
	public void testResponse() throws PersistenceException {
		when(ef02.createEngine(any(URI.class))).thenReturn(engine02);
		when(ef01.canHandle(any(URI.class))).thenReturn(false);
		when(ef02.canHandle(any(URI.class))).thenReturn(true);
		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		try (PersistenceResource pr = (PersistenceResource) resource) {
			
			assertEquals(engine02, pr.getEngine());
			
			assertDoesNotThrow(()->pr.save(null));
			
			Updateable ue02 = (Updateable) engine02;
			ArgumentCaptor<Map> ac = ArgumentCaptor.forClass(Map.class);
			verify(ue02, times(1)).create(ac.capture());
			assertEquals(1, ac.getValue().size());
			assertTrue(ac.getValue().containsKey(URIConverter.OPTION_RESPONSE));
			assertInstanceOf(Map.class, ac.getValue());
			
			assertDoesNotThrow(()->pr.load(null));
			Readable re02 = (Readable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(re02, times(1)).read(ac.capture());
			assertEquals(1, ac.getValue().size());
			assertTrue(ac.getValue().containsKey(URIConverter.OPTION_RESPONSE));
			assertInstanceOf(Map.class, ac.getValue());
			pr.unload();
			
			assertDoesNotThrow(()->pr.count(null));
			Countable ce02 = (Countable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(1)).count(ac.capture());
			assertEquals(1, ac.getValue().size());
			assertTrue(ac.getValue().containsKey(URIConverter.OPTION_RESPONSE));
			assertInstanceOf(Map.class, ac.getValue());
			
			assertDoesNotThrow(()->pr.exist(null));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(1)).exist(ac.capture());
			assertEquals(1, ac.getValue().size());
			assertTrue(ac.getValue().containsKey(URIConverter.OPTION_RESPONSE));
			assertInstanceOf(Map.class, ac.getValue());
			
			assertDoesNotThrow(()->pr.delete(null));
			Deletable de02 = (Deletable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(de02, times(1)).delete(ac.capture());
			assertEquals(1, ac.getValue().size());
			assertTrue(ac.getValue().containsKey(URIConverter.OPTION_RESPONSE));
			assertInstanceOf(Map.class, ac.getValue());
			
			
			assertDoesNotThrow(()->pr.save(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ue02, times(1)).create(anyMap());
			verify(ue02, times(1)).update(ac.capture());
			assertEquals(1, ac.getValue().size());
			assertTrue(ac.getValue().containsKey(URIConverter.OPTION_RESPONSE));
			assertInstanceOf(Map.class, ac.getValue());
			
			assertDoesNotThrow(()->pr.load(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(re02, times(2)).read(ac.capture());
			assertEquals(1, ac.getValue().size());
			assertTrue(ac.getValue().containsKey(URIConverter.OPTION_RESPONSE));
			assertInstanceOf(Map.class, ac.getValue());
			pr.unload();
			
			assertDoesNotThrow(()->pr.count(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(2)).count(ac.capture());
			assertEquals(1, ac.getValue().size());
			assertTrue(ac.getValue().containsKey(URIConverter.OPTION_RESPONSE));
			assertInstanceOf(Map.class, ac.getValue());
			
			assertDoesNotThrow(()->pr.exist(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(2)).exist(ac.capture());
			assertEquals(1, ac.getValue().size());
			assertTrue(ac.getValue().containsKey(URIConverter.OPTION_RESPONSE));
			assertInstanceOf(Map.class, ac.getValue());
			
			assertDoesNotThrow(()->pr.delete(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(de02, times(2)).delete(ac.capture());
			assertEquals(1, ac.getValue().size());
			assertTrue(ac.getValue().containsKey(URIConverter.OPTION_RESPONSE));
			assertInstanceOf(Map.class, ac.getValue());
		} catch (Exception e) {
			fail("Unexpected exception on close");
		}
		
	}
	
	/**
	 * Tests the default save options
	 * @throws PersistenceException
	 */
	@Test
	public void testSaveOptions() throws PersistenceException {
		when(ef02.createEngine(any(URI.class))).thenReturn(engine02);
		when(ef01.canHandle(any(URI.class))).thenReturn(false);
		when(ef02.canHandle(any(URI.class))).thenReturn(true);
		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		try (PersistenceResource pr = (PersistenceResource) resource) {
			
			assertEquals(engine02, pr.getEngine());
			Map<Object,Object> defaultSaveOptions = pr.getDefaultSaveOptions();
			assertNotNull(defaultSaveOptions);
			defaultSaveOptions.put("foo", "bar");
			
			assertDoesNotThrow(()->pr.save(null));
			Updateable ue02 = (Updateable) engine02;
			ArgumentCaptor<Map> ac = ArgumentCaptor.forClass(Map.class);
			verify(ue02, times(1)).create(ac.capture());
			verify(ue02, never()).update(anyMap());
			assertFooBarFizzBuzz(ac, true, false);
			
			assertDoesNotThrow(()->pr.load(null));
			Readable re02 = (Readable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(re02, times(1)).read(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			pr.unload();
			
			assertDoesNotThrow(()->pr.count(null));
			Countable ce02 = (Countable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(1)).count(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.exist(null));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(1)).exist(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.delete(null));
			Deletable de02 = (Deletable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(de02, times(1)).delete(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.save(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ue02, times(1)).create(anyMap());
			verify(ue02, times(1)).update(ac.capture());
			assertFooBarFizzBuzz(ac, true, false);
			
			assertDoesNotThrow(()->pr.save(Collections.singletonMap("fizz", "buzz")));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ue02, times(1)).create(anyMap());
			verify(ue02, times(2)).update(ac.capture());
			assertFooBarFizzBuzz(ac, true, true);

			assertDoesNotThrow(()->pr.load(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(re02, times(2)).read(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			pr.unload();
			
			assertDoesNotThrow(()->pr.count(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(2)).count(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.exist(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(2)).exist(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.delete(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(de02, times(2)).delete(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
		} catch (Exception e) {
			fail("Unexpected exception on close");
		}
		
	}
	
	@Test
	public void testLoadOptions() throws PersistenceException {
		when(ef02.createEngine(any(URI.class))).thenReturn(engine02);
		when(ef01.canHandle(any(URI.class))).thenReturn(false);
		when(ef02.canHandle(any(URI.class))).thenReturn(true);
		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		assertInstanceOf(PersistenceResource.class, resource);
		try (PersistenceResource pr = (PersistenceResource) resource) {
			
			assertEquals(engine02, pr.getEngine());
			Map<Object,Object> defaultLoadOptions = pr.getDefaultLoadOptions();
			assertNotNull(defaultLoadOptions);
			defaultLoadOptions.put("foo", "bar");
			
			assertDoesNotThrow(()->pr.save(null));
			
			Updateable ue02 = (Updateable) engine02;
			ArgumentCaptor<Map> ac = ArgumentCaptor.forClass(Map.class);
			verify(ue02, times(1)).create(ac.capture());
			verify(ue02, never()).update(anyMap());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.load(null));
			Readable re02 = (Readable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(re02, times(1)).read(ac.capture());
			assertFooBarFizzBuzz(ac, true, false);
			pr.unload();
			
			assertDoesNotThrow(()->pr.count(null));
			Countable ce02 = (Countable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(1)).count(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.exist(null));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(1)).exist(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.delete(null));
			Deletable de02 = (Deletable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(de02, times(1)).delete(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.save(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ue02, times(1)).create(anyMap());
			verify(ue02, times(1)).update(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.load(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(re02, times(2)).read(ac.capture());
			assertFooBarFizzBuzz(ac, true, false);
			
			pr.unload();
			assertDoesNotThrow(()->pr.load(Collections.singletonMap("fizz", "buzz")));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(re02, times(3)).read(ac.capture());
			assertFooBarFizzBuzz(ac, true, true);
			pr.unload();
			
			assertDoesNotThrow(()->pr.count(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(2)).count(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.exist(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(2)).exist(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.delete(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(de02, times(2)).delete(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
		} catch (Exception e) {
			fail("Unexpected exception on close");
		}
		
	}
	
	@Test
	public void testCountOptions() throws PersistenceException {
		when(ef02.createEngine(any(URI.class))).thenReturn(engine02);
		when(ef01.canHandle(any(URI.class))).thenReturn(false);
		when(ef02.canHandle(any(URI.class))).thenReturn(true);
		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		assertInstanceOf(PersistenceResource.class, resource);
		try (PersistenceResource pr = (PersistenceResource) resource) {
			
			assertEquals(engine02, pr.getEngine());
			Map<Object,Object> defaultCountOptions = pr.getDefaultCountOptions();
			assertNotNull(defaultCountOptions);
			defaultCountOptions.put("foo", "bar");
			
			assertDoesNotThrow(()->pr.save(null));
			
			Updateable ue02 = (Updateable) engine02;
			ArgumentCaptor<Map> ac = ArgumentCaptor.forClass(Map.class);
			verify(ue02, times(1)).create(ac.capture());
			verify(ue02, never()).update(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.load(null));
			Readable re02 = (Readable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(re02, times(1)).read(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			pr.unload();
			
			assertDoesNotThrow(()->pr.count(null));
			Countable ce02 = (Countable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(1)).count(ac.capture());
			assertFooBarFizzBuzz(ac, true, false);
			
			assertDoesNotThrow(()->pr.exist(null));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(1)).exist(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.delete(null));
			Deletable de02 = (Deletable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(de02, times(1)).delete(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.save(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ue02, times(1)).create(anyMap());
			verify(ue02, times(1)).update(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.load(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(re02, times(2)).read(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			pr.unload();
			
			assertDoesNotThrow(()->pr.count(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(2)).count(ac.capture());
			assertFooBarFizzBuzz(ac, true, false);
			
			assertDoesNotThrow(()->pr.count(Collections.singletonMap("fizz", "buzz")));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(3)).count(ac.capture());
			assertFooBarFizzBuzz(ac, true, true);
			
			assertDoesNotThrow(()->pr.exist(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(2)).exist(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.delete(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(de02, times(2)).delete(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
		} catch (Exception e) {
			fail("Unexpected exception on close");
		}
		
	}
	
	@Test
	public void testExistOptions() throws PersistenceException {
		when(ef02.createEngine(any(URI.class))).thenReturn(engine02);
		when(ef01.canHandle(any(URI.class))).thenReturn(false);
		when(ef02.canHandle(any(URI.class))).thenReturn(true);
		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		assertInstanceOf(PersistenceResource.class, resource);
		try (PersistenceResource pr = (PersistenceResource) resource) {
			
			assertEquals(engine02, pr.getEngine());
			Map<Object,Object> defaultExistOptions = pr.getDefaultExistOptions();
			assertNotNull(defaultExistOptions);
			defaultExistOptions.put("foo", "bar");
			
			assertDoesNotThrow(()->pr.save(null));
			
			Updateable ue02 = (Updateable) engine02;
			ArgumentCaptor<Map> ac = ArgumentCaptor.forClass(Map.class);
			verify(ue02, times(1)).create(ac.capture());
			verify(ue02, never()).update(anyMap());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.load(null));
			Readable re02 = (Readable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(re02, times(1)).read(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			pr.unload();
			
			assertDoesNotThrow(()->pr.count(null));
			Countable ce02 = (Countable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(1)).count(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.exist(null));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(1)).exist(ac.capture());
			assertFooBarFizzBuzz(ac, true, false);
			
			assertDoesNotThrow(()->pr.delete(null));
			Deletable de02 = (Deletable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(de02, times(1)).delete(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.save(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ue02, times(1)).create(anyMap());
			verify(ue02, times(1)).update(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.load(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(re02, times(2)).read(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			pr.unload();
			
			assertDoesNotThrow(()->pr.count(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(2)).count(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.exist(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(2)).exist(ac.capture());
			assertFooBarFizzBuzz(ac, true, false);
			
			assertDoesNotThrow(()->pr.exist(Collections.singletonMap("fizz", "buzz")));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(3)).exist(ac.capture());
			assertFooBarFizzBuzz(ac, true, true);
			
			assertDoesNotThrow(()->pr.delete(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(de02, times(2)).delete(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
		} catch (Exception e) {
			fail("Unexpected exception on close");
		}
		
	}
	
	@Test
	public void testDeleteOptions() throws PersistenceException {
		when(ef02.createEngine(any(URI.class))).thenReturn(engine02);
		when(ef01.canHandle(any(URI.class))).thenReturn(false);
		when(ef02.canHandle(any(URI.class))).thenReturn(true);
		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		assertInstanceOf(PersistenceResource.class, resource);
		try (PersistenceResource pr = (PersistenceResource) resource) {
			
			assertEquals(engine02, pr.getEngine());
			Map<Object,Object> defaultDeleteOptions = pr.getDefaultDeleteOptions();
			assertNotNull(defaultDeleteOptions);
			defaultDeleteOptions.put("foo", "bar");
			
			assertDoesNotThrow(()->pr.save(null));
			
			Updateable ue02 = (Updateable) engine02;
			ArgumentCaptor<Map> ac = ArgumentCaptor.forClass(Map.class);
			verify(ue02, times(1)).create(ac.capture());
			verify(ue02, never()).update(anyMap());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.load(null));
			Readable re02 = (Readable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(re02, times(1)).read(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			pr.unload();
			
			assertDoesNotThrow(()->pr.count(null));
			Countable ce02 = (Countable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(1)).count(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.exist(null));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(1)).exist(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.delete(null));
			Deletable de02 = (Deletable) engine02;
			ac = ArgumentCaptor.forClass(Map.class);
			verify(de02, times(1)).delete(ac.capture());
			assertFooBarFizzBuzz(ac, true, false);
			
			assertDoesNotThrow(()->pr.save(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ue02, times(1)).create(anyMap());
			verify(ue02, times(1)).update(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.load(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(re02, times(2)).read(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			pr.unload();
			
			assertDoesNotThrow(()->pr.count(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(2)).count(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.exist(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(ce02, times(2)).exist(ac.capture());
			assertFooBarFizzBuzz(ac, false, false);
			
			assertDoesNotThrow(()->pr.delete(Collections.emptyMap()));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(de02, times(2)).delete(ac.capture());
			assertFooBarFizzBuzz(ac, true, false);
			
			assertDoesNotThrow(()->pr.delete(Collections.singletonMap("fizz", "buzz")));
			ac = ArgumentCaptor.forClass(Map.class);
			verify(de02, times(3)).delete(ac.capture());
			assertFooBarFizzBuzz(ac, true, true);
		} catch (Exception e) {
			fail("Unexpected exception on close");
		}
		
	}


	/**
	 * Returns a default answer for timestamp reponses
	 * @return a default answer for timestamp reponses
	 */
	private Answer defaultResoureActionAnswer(Object returnValue, long timestamp) {
		return i->{
			return createDefaultAnswer(returnValue, timestamp, i);
		};
	}


	/**
	 * Creates a default answer data
	 * @param returnValue
	 * @param timestamp
	 * @param i
	 * @return
	 */
	private Object createDefaultAnswer(Object returnValue, long timestamp, InvocationOnMock i) {
		Map<?, ?> map = i.getArgument(0);
		Map<Object,Object> response = getResponse(map);
		assertNotNull(response);
		response.put(URIConverter.RESPONSE_TIME_STAMP_PROPERTY, timestamp);
		return returnValue;
	}


	/**
	 * @param ac
	 */
	private void assertFooBarFizzBuzz(ArgumentCaptor<Map> ac, boolean foobar, boolean fizzbuzz) {
		if (!foobar && !fizzbuzz) {
			assertEquals(1, ac.getValue().size());
			assertFalse(ac.getValue().containsKey("foo"));
			assertFalse(ac.getValue().containsValue("bar"));
			assertFalse(ac.getValue().containsKey("fizz"));
			assertFalse(ac.getValue().containsValue("buzz"));
		} else if (foobar && fizzbuzz) {
			assertEquals(3, ac.getValue().size());
			assertTrue(ac.getValue().containsKey("foo"));
			assertTrue(ac.getValue().containsValue("bar"));
			assertTrue(ac.getValue().containsKey("fizz"));
			assertTrue(ac.getValue().containsValue("buzz"));
		} else {
			assertEquals(2, ac.getValue().size());
			assertTrue(ac.getValue().containsKey("foo"));
			assertTrue(ac.getValue().containsValue("bar"));
			assertFalse(ac.getValue().containsKey("fizz"));
			assertFalse(ac.getValue().containsValue("buzz"));
		}
	}

}
