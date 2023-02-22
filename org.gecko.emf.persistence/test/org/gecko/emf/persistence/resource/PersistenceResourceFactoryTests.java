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
package org.gecko.emf.persistence.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.engine.PersistenceEngine;
import org.gecko.emf.persistence.engine.PersistenceEngineFactory;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

/**
 * See documentation here: 
 * 	https://github.com/osgi/osgi-test
 * 	https://github.com/osgi/osgi-test/wiki
 * Examples: https://github.com/osgi/osgi-test/tree/main/examples
 */
@ExtendWith(MockitoExtension.class)
public class PersistenceResourceFactoryTests {

	private PersistenceResourceFactory resourceFactory;
	@Mock
	private PersistenceEngine<String, EObjectMapper, String, String, String> engine01;
	@Mock
	private PersistenceEngine<String, EObjectMapper, String, String, String> engine02;
	@Mock
	private PersistenceEngineFactory  ef01;
	@Mock
	private PersistenceEngineFactory  ef02;

	@BeforeEach
	public void before() throws PersistenceException {
		resourceFactory = Mockito.mock(PersistenceResourceFactory.class, 
				Mockito.withSettings().
				useConstructor().
				defaultAnswer(Mockito.CALLS_REAL_METHODS));
		lenient().when(ef01.createEngine(any(URI.class))).thenReturn(engine01);
		lenient().when(ef02.createEngine(any(URI.class))).thenReturn(engine02);
		assertNotNull(resourceFactory);
		assertNotNull(engine01);
		assertNotNull(engine02);
		assertNotNull(ef01);
		assertNotNull(ef02);
	}

	@Test
	public void testCreateResourceNullUri() {
		Resource resource = resourceFactory.createResource(null);
		assertNull(resource);
		verify(ef01, never()).canHandle(any(URI.class));
		verify(ef02, never()).canHandle(any(URI.class));
	}

	@Test
	public void testCreateResourceNoEngine() throws PersistenceException {
		when(ef01.canHandle(any(URI.class))).then(handleConfigs());
		when(ef02.canHandle(any(URI.class))).then(handleConfigs());
		URI uri = URI.createURI("dummy://hello/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(ResourceImpl.class, resource);
		assertFalse(resource instanceof PersistenceResource);
		// No ComponentServiceObjects are registered
		verify(ef01, never()).canHandle(any(URI.class));
		verify(ef01, never()).createEngine(any(URI.class));

		verify(ef02, never()).canHandle(any(URI.class));
		verify(ef02, never()).createEngine(any(URI.class));

		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(ResourceImpl.class, resource);
		assertFalse(resource instanceof PersistenceResource);
		// Two ComponentServiceObjects are registered
		verify(ef01, never()).createEngine(any(URI.class));
		verify(ef01, times(1)).canHandle(any(URI.class));

		verify(ef02, never()).createEngine(any(URI.class));
		verify(ef02, times(1)).canHandle(any(URI.class));
	}

	@Test
	public void testCreateResource01() throws PersistenceException {
		when(ef01.canHandle(any(URI.class))).then(handleConfigs());
		when(ef02.canHandle(any(URI.class))).then(handleConfigs());
		URI uri = URI.createURI("dummy://bar/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(ResourceImpl.class, resource);
		assertFalse(resource instanceof PersistenceResource);
		// No ComponentServiceObjects are registered
		verify(ef01, never()).createEngine(any(URI.class));
		verify(ef01, never()).canHandle(any(URI.class));

		verify(ef02, never()).createEngine(any(URI.class));
		verify(ef02, never()).canHandle(any(URI.class));

		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		assertEquals(engine02, ((PersistenceResource)resource).getEngine());
		// Two ComponentServiceObjects are registered
		verify(ef01, never()).createEngine(any(URI.class));
		verify(ef01, times(1)).canHandle(any(URI.class));

		verify(ef02, times(1)).createEngine(any(URI.class));
		verify(ef02, times(1)).canHandle(any(URI.class));
	}

	@Test
	public void testCreateResource02() throws PersistenceException {
		when(ef01.canHandle(any(URI.class))).then(handleConfigs());
		URI uri = URI.createURI("dummy://foo/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(ResourceImpl.class, resource);
		assertFalse(resource instanceof PersistenceResource);
		// No ComponentServiceObjects are registered
		verify(ef01, never()).canHandle(any(URI.class));
		verify(ef01, never()).createEngine(any(URI.class));

		verify(ef02, never()).canHandle(any(URI.class));
		verify(ef02, never()).createEngine(any(URI.class));

		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		
		try (PersistenceResourceImpl impl = (PersistenceResourceImpl) resource) {

			assertTrue(impl.getDefaultCountOptions().isEmpty());
			assertTrue(impl.getDefaultDeleteOptions().isEmpty());
			assertTrue(impl.getDefaultSaveOptions().isEmpty());
			assertTrue(impl.getDefaultLoadOptions().isEmpty());
			assertTrue(impl.getDefaultDeleteOptions().isEmpty());
		} catch (Exception e) {
			fail("Unexpected exception during testing default options", e);
		}

		assertEquals(engine01, ((PersistenceResource)resource).getEngine());
		// Two ComponentServiceObjects are registered, but first hit matched
		verify(ef01, times(1)).createEngine(any(URI.class));
		// One for the creation and seoond call for PersistenceResource#close
		verify(ef01, times(2)).canHandle(any(URI.class));

		verify(ef02, never()).createEngine(any(URI.class));
		verify(ef02, never()).canHandle(any(URI.class));
	}

	@Test
	public void testCreateResourceDefaultProperties() throws PersistenceException {
		when(ef01.canHandle(any(URI.class))).then(handleConfigs());
		URI uri = URI.createURI("dummy://foo/world");
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(ResourceImpl.class, resource);
		assertFalse(resource instanceof PersistenceResource);
		// No ComponentServiceObjects are registered
		verify(ef01, never()).canHandle(any(URI.class));
		verify(ef01, never()).createEngine(any(URI.class));

		verify(ef02, never()).canHandle(any(URI.class));
		verify(ef02, never()).createEngine(any(URI.class));

		resourceFactory.getProperties().put("foo", "bar");
		resourceFactory.getProperties().put("fizz", "buzz");
		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		assertEquals(engine01, ((PersistenceResource)resource).getEngine());
		// Two ComponentServiceObjects are registered, but first hit matched
		verify(ef01, times(1)).createEngine(any(URI.class));
		verify(ef01, times(1)).canHandle(any(URI.class));

		verify(ef02, never()).createEngine(any(URI.class));
		verify(ef02, never()).canHandle(any(URI.class));

		try (PersistenceResourceImpl impl = (PersistenceResourceImpl) resource) {

			assertEquals(2, impl.getDefaultCountOptions().size());
			assertEquals(2, impl.getDefaultDeleteOptions().size());
			assertEquals(2, impl.getDefaultSaveOptions().size());
			assertEquals(2, impl.getDefaultLoadOptions().size());
			assertEquals(2, impl.getDefaultDeleteOptions().size());
		} catch (Exception e) {
			fail("Unexpected exception during testing default options", e);
		}
	}

	@Test
	public void testCreateResourceException01() throws PersistenceException {
		when(ef01.createEngine(any(URI.class))).thenReturn(engine01);
		when(ef01.canHandle(any(URI.class))).then(handleConfigs());
		URI uri = URI.createURI("dummy://foo/world");
		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		assertEquals(engine01, ((PersistenceResource)resource).getEngine());
		// Two ComponentServiceObjects are registered, but first hit matched
		verify(ef01, times(1)).canHandle(any(URI.class));
		verify(ef01, times(1)).createEngine(any(URI.class));

		verify(ef02, never()).canHandle(any(URI.class));
		verify(ef02, never()).createEngine(any(URI.class));
	}

	@Test
	public void testCreateResourceException02() throws PersistenceException {
		when(ef01.createEngine(any(URI.class))).thenThrow(PersistenceException.class);
		when(ef01.canHandle(any(URI.class))).then(handleConfigs());
		URI uri = URI.createURI("dummy://foo/world");
		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(ResourceImpl.class, resource);
		assertFalse(resource instanceof PersistenceResource);
		// Two ComponentServiceObjects are registered, but first hit matched
		verify(ef01, times(1)).canHandle(any(URI.class));
		verify(ef01, times(1)).createEngine(any(URI.class));

		verify(ef02, never()).canHandle(any(URI.class));
		verify(ef02, never()).createEngine(any(URI.class));
	}

	@Test
	public void testCloseResource01() throws Exception {
		when(ef01.canHandle(any(URI.class))).then(handleConfigs());
		lenient().when(ef02.canHandle(any(URI.class))).then(handleConfigs());
		URI uri = URI.createURI("dummy://foo/world");
		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		assertEquals(engine01, ((PersistenceResource)resource).getEngine());
		// Two ComponentServiceObjects are registered, but first hit matched
		verify(ef01, times(1)).canHandle(any(URI.class));
		verify(ef01, times(1)).createEngine(any(URI.class));
		verify(ef01, never()).disposeEngine(any(URI.class));

		verify(ef02, never()).canHandle(any(URI.class));
		verify(ef02, never()).createEngine(any(URI.class));
		verify(ef02, never()).disposeEngine(any(URI.class));
		verify(resourceFactory, never()).accept(any(PersistenceResource.class));

		PersistenceResource pr = (PersistenceResource) resource;
		pr.close();
		verify(ef01, times(2)).canHandle(any(URI.class));
		verify(ef01, times(1)).createEngine(any(URI.class));
		verify(ef01, times(1)).disposeEngine(any(URI.class));

		verify(ef02, never()).canHandle(any(URI.class));
		verify(ef02, never()).createEngine(any(URI.class));
		verify(ef02, never()).disposeEngine(any(URI.class));
		verify(resourceFactory, times(1)).accept(any(PersistenceResource.class));
	}

	@Test
	public void testCloseResource02() throws Exception {
		when(ef01.canHandle(any(URI.class))).then(handleConfigs());
		when(ef02.canHandle(any(URI.class))).then(handleConfigs());
		URI uri = URI.createURI("dummy://bar/world");
		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		assertEquals(engine02, ((PersistenceResource)resource).getEngine());
		// Two ComponentServiceObjects are registered, but first hit matched
		verify(ef01, times(1)).canHandle(any(URI.class));
		verify(ef01, never()).createEngine(any(URI.class));
		verify(ef01, never()).disposeEngine(any(URI.class));

		verify(ef02, times(1)).canHandle(any(URI.class));
		verify(ef02, times(1)).createEngine(any(URI.class));
		verify(ef02, never()).disposeEngine(any(URI.class));
		verify(resourceFactory, never()).accept(any(PersistenceResource.class));

		PersistenceResource pr = (PersistenceResource) resource;
		pr.close();
		verify(ef01, times(2)).canHandle(any(URI.class));
		verify(ef01, never()).disposeEngine(any(URI.class));
		verify(ef01, never()).createEngine(any(URI.class));

		verify(ef02, times(2)).canHandle(any(URI.class));
		verify(ef02, times(1)).createEngine(any(URI.class));
		verify(ef02, times(1)).disposeEngine(any(URI.class));
		verify(resourceFactory, times(1)).accept(any(PersistenceResource.class));
	}

	@Test
	public void testCloseResource03() throws PersistenceException {
		when(ef01.canHandle(any(URI.class))).then(handleConfigs());
		when(ef02.canHandle(any(URI.class))).then(handleConfigs());
		URI uri = URI.createURI("dummy://dummy/world");
		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(ResourceImpl.class, resource);
		assertFalse(resource instanceof PersistenceResource);
		// Two ComponentServiceObjects are registered, but first hit matched
		verify(ef01, times(1)).canHandle(any(URI.class));
		verify(ef01, never()).createEngine(any(URI.class));
		verify(ef01, never()).disposeEngine(any(URI.class));

		verify(ef02, times(1)).canHandle(any(URI.class));
		verify(ef02, never()).createEngine(any(URI.class));
		verify(ef02, never()).disposeEngine(any(URI.class));
		verify(resourceFactory, never()).accept(any(PersistenceResource.class));
	}

	@Test
	public void testCloseResourceException01() throws PersistenceException {
		when(ef01.canHandle(any(URI.class))).then(handleConfigs());
		when(ef02.canHandle(any(URI.class))).then(handleConfigs());
		URI uri = URI.createURI("dummy://bar/world");
		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		assertEquals(engine02, ((PersistenceResource)resource).getEngine());
		// Two ComponentServiceObjects are registered, but first hit matched
		verify(ef01, times(1)).canHandle(any(URI.class));
		verify(ef01, never()).createEngine(any(URI.class));
		verify(ef01, never()).disposeEngine(any(URI.class));

		verify(ef02, times(1)).canHandle(any(URI.class));
		verify(ef02, times(1)).createEngine(any(URI.class));
		verify(ef02, never()).disposeEngine(any(URI.class));
		verify(resourceFactory, never()).accept(any(PersistenceResource.class));

		when(ef02.canHandle(any(URI.class))).thenThrow(NullPointerException.class);
		PersistenceResource pr = (PersistenceResource) resource;
		assertThrows(NullPointerException.class, ()->pr.close());
		verify(ef01, times(2)).canHandle(any(URI.class));
		verify(ef01, never()).createEngine(any(URI.class));
		verify(ef01, never()).disposeEngine(any(URI.class));

		verify(ef02, times(2)).canHandle(any(URI.class));
		verify(ef02, times(1)).createEngine(any(URI.class));
		verify(ef02, never()).disposeEngine(any(URI.class));

		verify(resourceFactory, times(1)).accept(any(PersistenceResource.class));
	}

	@Test
	public void testCloseResourceException02() throws PersistenceException {
		when(ef01.canHandle(any(URI.class))).then(handleConfigs());
		when(ef02.canHandle(any(URI.class))).then(handleConfigs());
		URI uri = URI.createURI("dummy://bar/world");
		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		Resource resource = resourceFactory.createResource(uri);
		assertNotNull(resource);
		assertInstanceOf(PersistenceResource.class, resource);
		assertEquals(engine02, ((PersistenceResource)resource).getEngine());
		// Two ComponentServiceObjects are registered, but first hit matched
		verify(ef01, times(1)).canHandle(any(URI.class));
		verify(ef01, never()).createEngine(any(URI.class));
		verify(ef01, never()).disposeEngine(any(URI.class));

		verify(ef02, times(1)).canHandle(any(URI.class));
		verify(ef02, times(1)).createEngine(any(URI.class));
		verify(ef02, never()).disposeEngine(any(URI.class));
		verify(resourceFactory, never()).accept(any(PersistenceResource.class));

		when(ef02.disposeEngine(any(URI.class))).thenThrow(NullPointerException.class);
		PersistenceResource pr = (PersistenceResource) resource;
		assertThrows(NullPointerException.class, ()->pr.close());

		verify(ef01, times(2)).canHandle(any(URI.class));
		verify(ef01, never()).createEngine(any(URI.class));
		verify(ef01, never()).disposeEngine(any(URI.class));

		verify(ef02, times(2)).canHandle(any(URI.class));
		verify(ef02, times(1)).createEngine(any(URI.class));
		verify(ef02, times(1)).disposeEngine(any(URI.class));
		verify(resourceFactory, times(1)).accept(any(PersistenceResource.class));
	}

	@Test
	public void testCanHandleException01() throws PersistenceException {
		when(ef01.canHandle(any(URI.class))).then(handleConfigs());
		when(ef02.canHandle(any(URI.class))).thenThrow(NullPointerException.class);
		URI uri = URI.createURI("dummy://bar/world");
		resourceFactory.addEngine(ef01);
		resourceFactory.addEngine(ef02);
		assertThrows(NullPointerException.class, ()->resourceFactory.createResource(uri));
		// Two ComponentServiceObjects are registered, but first hit matched
		verify(ef01, times(1)).canHandle(any(URI.class));
		verify(ef01, never()).createEngine(any(URI.class));

		verify(ef02, times(1)).canHandle(any(URI.class));
		verify(ef02, never()).createEngine(any(URI.class));

	}

	private Answer<Boolean> handleConfigs() {
		return new Answer<Boolean>() {

			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				PersistenceEngineFactory factory = (PersistenceEngineFactory) invocation.getMock();
				URI uri = invocation.getArgument(0);
				if (ef01.equals(factory) && uri.host().contains("foo")) {
					return true;
				}
				if (ef02.equals(factory) && uri.host().contains("bar")) {
					return true;
				}
				return false;
			}
		};
	}
}
