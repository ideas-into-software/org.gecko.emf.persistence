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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.gecko.emf.persistence.api.PersistenceEngine;
import org.gecko.emf.persistence.api.QueryEngine;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.BundleContext;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;

/**
 * See documentation here: 
 * 	https://github.com/osgi/osgi-test
 * 	https://github.com/osgi/osgi-test/wiki
 * Examples: https://github.com/osgi/osgi-test/tree/main/examples
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
@ExtendWith(MockitoExtension.class)
public class SimpleTest {
	
	@Mock
	private QueryEngine<String, String> queryEngine;
	@Mock
	private EObjectMapper mapper;
	@Mock
	private PersistenceEngine<String, EObjectMapper, String, String, String> engine;

	@BeforeEach
	public void before(@InjectBundleContext BundleContext ctx) {
		when(engine.getMapper()).thenReturn(mapper);
		when(engine.getQueryEngine()).thenReturn(queryEngine);
	}

	@Test
	public void testSimple() {
		assertNotNull(queryEngine);
		assertNotNull(mapper);
		assertNotNull(engine);
		assertEquals(mapper, engine.getMapper());
		assertEquals(queryEngine, engine.getQueryEngine());
		verify(engine, times(1)).getMapper();
		verify(engine, never()).getDriver();
		verify(engine, times(1)).getQueryEngine();
	}
}
