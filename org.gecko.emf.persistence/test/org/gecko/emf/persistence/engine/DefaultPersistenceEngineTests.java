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
import java.util.concurrent.Executors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.api.PrimaryKeyFactory;
import org.gecko.emf.persistence.api.QueryEngine;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.mapping.InputContentHandler;
import org.gecko.emf.persistence.resource.PersistenceResource;
import org.gecko.emf.persistence.resource.PersistenceResourceFactory;
import org.gecko.emf.persistence.resource.PersistenceResourceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.util.promise.PromiseFactory;

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
	private EObjectMapper mapper;
	private DefaultPersistenceEngine<String, EObjectMapper, String, String, String> engine;
	private final PromiseFactory pf = new PromiseFactory(Executors.newSingleThreadExecutor());

	@SuppressWarnings("unchecked")
	@BeforeEach
	public void before() throws PersistenceException {
		// to successfully add a primary key factory we need the table return URI
		when(primaryKeyFactory01.getTableURI()).thenReturn("foo");
		engine = createAbstractMock(DefaultPersistenceEngine.class);
		
		engine.setQueryEngine(queryEngine);
		engine.setConverterService(converterService);
		engine.addPrimaryKeyFactory(primaryKeyFactory01);
		engine.addInputHandler(contentHandler01);
		engine.addInputHandler(contentHandler02);
		engine.getProperties().put("foo", "bar");
	}
	
	private <T> T createAbstractMock(Class<T> mockClass) {
		return Mockito.mock(mockClass, 
				Mockito.withSettings().
				useConstructor().
				defaultAnswer(Mockito.CALLS_REAL_METHODS));
	}
	
	@Test
	public void testCreateEmptyResource() {
		when(engine.getDriver()).thenReturn(pf.resolved("TEST"));
		URI uri = URI.createURI("dummy://bar/world");
		PersistenceResource resource = new PersistenceResourceImpl(null, engine, uri);
		engine.setResource(resource);
		EClass eclass = EcoreFactory.eINSTANCE.createEClass();
		resource.getContents().add(eclass);
		assertThrows(NullPointerException.class, ()-> engine.create(Map.of("fizz", "buzz")));
		verify(engine, times(1)).setResource(any(PersistenceResource.class));
		verify(engine, times(1)).getDriver();
	}
	
}
