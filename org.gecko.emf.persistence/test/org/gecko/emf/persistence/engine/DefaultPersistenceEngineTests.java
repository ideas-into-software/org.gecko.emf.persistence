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

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests the abstract {@link DefaultPersistenceEngine} with its functionality
 * @author Mark Hoffmann
 * @since 22.02.2023
 */
@ExtendWith(MockitoExtension.class)
public class DefaultPersistenceEngineTests {
	
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


}
