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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Hashtable;

import org.gecko.emf.mongo.ConverterService;
import org.gecko.emf.mongo.ValueConverter;
import org.gecko.emf.mongo.converter.DefaultConverterService;
import org.gecko.emf.mongo.tests.converter.NPEConverter;
import org.gecko.emf.osgi.annotation.require.RequireEMF;
import org.gecko.emf.osgi.example.model.basic.BasicPackage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * Integration tests for the complete EMF mongo setup
 * 
 * @author Mark Hoffmann
 * @since 26.07.2017
 */
@RequireEMF
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
public class ConverterIntegrationTest extends MongoEMFSetting {

	@BeforeEach
	public void doBefore(@InjectBundleContext BundleContext ctx) {
		super.doBefore(ctx);
	}

	@AfterEach
	public void doAfter() {
		super.doAfter();
	}

	@Test
	public void testDefaultConverterService2(@InjectService ConverterService cs) {
		assertTrue(cs instanceof DefaultConverterService);
	}

	@Test
	public void testDefaultConverterServiceRegistration_Fail(@InjectService ConverterService cs) {
		assertTrue(cs instanceof DefaultConverterService);
		try {
			cs.getConverter(BasicPackage.Literals.NPE);
			fail("IllegalStateException expected");
		} catch (IllegalStateException e) {
			assertEquals("The default converter was not found - this should never happen", e.getMessage());
		}
	}

	@Test
	public void testDefaultConverterServiceRegistrationNew(@InjectService ConverterService cs,
			@InjectBundleContext BundleContext bc) {
		assertTrue(cs instanceof DefaultConverterService);
		assertMissingService(cs);

		ValueConverter converter = new NPEConverter();
		ServiceRegistration<ValueConverter> rs = bc.registerService(ValueConverter.class, converter, new Hashtable<>());
		try {
			ValueConverter registeredConverter = cs.getConverter(BasicPackage.Literals.NPE);
			assertEquals(converter, registeredConverter);
		} finally {
			rs.unregister();
		}

		assertMissingService(cs);
	}

	private void assertMissingService(ConverterService cs) {
		try {
			cs.getConverter(BasicPackage.Literals.NPE);
			fail("No expected to reach this line of code");
		} catch (IllegalStateException e) {
		}
	}

}
