/**
 * Copyright (c) 2012 - 2023 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.api.PrimaryKeyFactory;
import org.gecko.emf.persistence.api.QueryEngine;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.mapping.InputContentHandler;
import org.gecko.emf.persistence.resource.PersistenceResource;
import org.gecko.emf.persistence.resource.PersistenceResourceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests the abstract {@link DefaultPersistenceEngine} with its functionality
 * @author Mark Hoffmann
 * @since 22.02.2023
 */
@ExtendWith(MockitoExtension.class)
public class DefaultPersistenceEngineIntegrationTests {
	
	@Mock
	private InputContentHandler<String, EObjectMapper> contentHandler01;
	@Mock
	private InputContentHandler<String, EObjectMapper> contentHandler02;
	@Mock
	private PrimaryKeyFactory primaryKeyFactory01;
	@Mock
	private ConverterService converterService;
	@Mock
	private QueryEngine<String, String> queryEngine;
	@Mock
	private PersistenceEngineFactory  pef;
	@Mock
	private EObjectMapper mapper;
	private DefaultPersistenceEngine<String, EObjectMapper, String, String, String> engine;
	private PersistenceResourceFactory resourceFactory;

	@SuppressWarnings("unchecked")
	@BeforeEach
	public void before() throws PersistenceException {
		// to select our PersistenceEngineFactory, we must return true here
		when(pef.canHandle(any(URI.class))).thenReturn(Boolean.TRUE);
		// to successfully add a primary key factory we need the table return URI
		when(primaryKeyFactory01.getTableURI()).thenReturn("foo");
		resourceFactory = createAbstractMock(PersistenceResourceFactory.class);
		resourceFactory.getProperties().put("foo", "bar");
		resourceFactory.getProperties().put("fizz", "buzz");
		engine = createAbstractMock(DefaultPersistenceEngine.class);
		// PersistenceEngineFactory must return the engine
		when(pef.createEngine(any(URI.class))).thenReturn(engine);
		
		resourceFactory.addEngine(pef);
		
		engine.setQueryEngine(queryEngine);
		engine.setConverterService(converterService);
		engine.addPrimaryKeyFactory(primaryKeyFactory01);
		engine.addInputHandler(contentHandler01);
		engine.addInputHandler(contentHandler02);
	}
	
	private <T> T createAbstractMock(Class<T> mockClass) {
		return Mockito.mock(mockClass, 
				Mockito.withSettings().
				useConstructor().
				defaultAnswer(Mockito.CALLS_REAL_METHODS));
	}
	
	@Test
	public void testCreateEmptyResource() {
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		verify(engine, times(1)).setResource(any(PersistenceResource.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSampleSave() throws IOException, PersistenceException {
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		verify(engine, times(1)).setResource(any(PersistenceResource.class));
		try (PersistenceResource pr = (PersistenceResource) resource) {

			ArgumentCaptor<Map<Object, Object>> capturer = ArgumentCaptor.forClass(Map.class);
			assertThrows(NullPointerException.class, ()-> pr.save(Map.of("emil", "test")));

			verify(engine, times(1)).create(capturer.capture());
			Map<Object, Object> saveMap = capturer.getValue();
			assertEquals(4, saveMap.size());
			assertTrue(saveMap.containsKey(URIConverter.OPTION_RESPONSE));
			assertTrue(saveMap.containsKey("emil"));
			assertTrue(saveMap.containsKey("foo"));
			assertTrue(saveMap.containsKey("fizz"));
		} catch (Exception e) {
			fail("Unexpected exception on sample save close", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSampleLoad() throws IOException, PersistenceException {
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		verify(engine, times(1)).setResource(any(PersistenceResource.class));
		try (PersistenceResource pr = (PersistenceResource) resource) {
			
			ArgumentCaptor<Map<Object, Object>> capturer = ArgumentCaptor.forClass(Map.class);
			assertThrows(NullPointerException.class, ()-> pr.load(Map.of("emil", "test")));
			
			verify(engine, times(1)).read(capturer.capture());
			Map<Object, Object> loadMap = capturer.getValue();
			assertEquals(4, loadMap.size());
			assertTrue(loadMap.containsKey(URIConverter.OPTION_RESPONSE));
			assertTrue(loadMap.containsKey("emil"));
			assertTrue(loadMap.containsKey("foo"));
			assertTrue(loadMap.containsKey("fizz"));
		} catch (Exception e) {
			fail("Unexpected exception on sample load close", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSampleCount() throws IOException, PersistenceException {
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		verify(engine, times(1)).setResource(any(PersistenceResource.class));
		try (PersistenceResource pr = (PersistenceResource) resource) {
			
			ArgumentCaptor<Map<Object, Object>> capturer = ArgumentCaptor.forClass(Map.class);
			assertThrows(IOException.class, ()-> pr.count(Map.of("emil", "test")));
			
			verify(engine, times(1)).count(capturer.capture());
			Map<Object, Object> countMap = capturer.getValue();
			assertEquals(4, countMap.size());
			assertTrue(countMap.containsKey(URIConverter.OPTION_RESPONSE));
			assertTrue(countMap.containsKey("emil"));
			assertTrue(countMap.containsKey("foo"));
			assertTrue(countMap.containsKey("fizz"));
		} catch (Exception e) {
			fail("Unexpected exception on sample count close", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSampleExist() throws IOException, PersistenceException {
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		verify(engine, times(1)).setResource(any(PersistenceResource.class));
		try (PersistenceResource pr = (PersistenceResource) resource) {
			
			ArgumentCaptor<Map<Object, Object>> capturer = ArgumentCaptor.forClass(Map.class);
			assertThrows(IOException.class, ()-> pr.exist(Map.of("emil", "test")));
			
			verify(engine, times(1)).exist(capturer.capture());
			Map<Object, Object> existMap = capturer.getValue();
			assertEquals(4, existMap.size());
			assertTrue(existMap.containsKey(URIConverter.OPTION_RESPONSE));
			assertTrue(existMap.containsKey("emil"));
			assertTrue(existMap.containsKey("foo"));
			assertTrue(existMap.containsKey("fizz"));
		} catch (Exception e) {
			fail("Unexpected exception on sample count close", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSampleDelete() throws IOException, PersistenceException {
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		verify(engine, times(1)).setResource(any(PersistenceResource.class));
		try (PersistenceResource pr = (PersistenceResource) resource) {
			
			ArgumentCaptor<Map<Object, Object>> capturer = ArgumentCaptor.forClass(Map.class);
			assertThrows(NullPointerException.class, ()-> pr.delete(Map.of("emil", "test")));
			
			verify(engine, times(1)).delete(capturer.capture());
			Map<Object, Object> deleteMap = capturer.getValue();
			assertEquals(4, deleteMap.size());
			assertTrue(deleteMap.containsKey(URIConverter.OPTION_RESPONSE));
			assertTrue(deleteMap.containsKey("emil"));
			assertTrue(deleteMap.containsKey("foo"));
			assertTrue(deleteMap.containsKey("fizz"));
		} catch (Exception e) {
			fail("Unexpected exception on sample count close", e);
		}
	}


}
