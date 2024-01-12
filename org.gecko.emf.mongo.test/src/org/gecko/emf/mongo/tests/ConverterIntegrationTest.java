///**
// * Copyright (c) 2012 - 2017 Data In Motion and others.
// * All rights reserved. 
// * 
// * This program and the accompanying materials are made available under the terms of the 
// * Eclipse Public License v1.0 which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// * 
// * Contributors:
// *     Data In Motion - initial API and implementation
// */
//package org.gecko.emf.mongo.tests;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.junit.jupiter.api.Assertions.fail;
//
//import java.io.IOException;
//import java.util.Hashtable;
//
//import org.gecko.emf.mongo.ConverterService;
//import org.gecko.emf.mongo.converter.DefaultConverterService;
//import org.gecko.emf.mongo.tests.converter.NPEConverter;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.osgi.framework.BundleContext;
//import org.osgi.framework.BundleException;
//import org.osgi.framework.InvalidSyntaxException;
//import org.osgi.test.common.annotation.InjectBundleContext;
//import org.osgi.test.junit5.context.BundleContextExtension;
//import org.osgi.test.junit5.service.ServiceExtension;
//
//
///**
// * Integration tests for the complete EMF mongo setup
// * @author Mark Hoffmann
// * @since 26.07.2017
// */
//@ExtendWith(BundleContextExtension.class)
//@ExtendWith(ServiceExtension.class)
//public class ConverterIntegrationTest extends MongoEMFSetting {
//
//
//	@BeforeEach
//	public void doBefore(@InjectBundleContext BundleContext ctx) {
//		super.doBefore(ctx);
//	}
//	
//	@AfterEach
//	public void doAfter() {
//		super.doAfter();
//	}
//	
//	@Test
//	public void testDefaultConverterService() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//		ConverterService cs = createStaticTrackedChecker(ConverterService.class).trackedServiceNotNull().getTrackedService();
//		assertTrue(cs instanceof DefaultConverterService);
//	}
//	
//	@Test(expected=IllegalStateException.class)
//	public void testDefaultConverterServiceRegistration_Fail() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//		ConverterService cs = createStaticTrackedChecker(ConverterService.class).trackedServiceNotNull().getTrackedService();
//		assertTrue(cs instanceof DefaultConverterService);
//		cs.getConverter(BasicPackage.Literals.NPE);
//	}
//	
//	@Test
//	public void testDefaultConverterServiceRegistrationNew() throws BundleException, InvalidSyntaxException, IOException, InterruptedException {
//		ConverterService cs = createStaticTrackedChecker(ConverterService.class).trackedServiceNotNull().getTrackedService();
//		assertTrue(cs instanceof DefaultConverterService);
//		try {
//			cs.getConverter(BasicPackage.Literals.NPE);
//			fail("No expected to reach this line of code");
//		} catch (IllegalStateException e) {
//		}
//		
//		ServiceChecker<ValueConverter> valueSC = createTrackedChecker(ValueConverter.class).assertCreations(0, false);
//
//		ValueConverter converter = new NPEConverter();
//		registerServiceForCleanup(ValueConverter.class, converter, new Hashtable<>());
//		
//		ValueConverter c = valueSC.assertCreations(1, true).trackedServiceNotNull().getTrackedService();
//		assertEquals(converter, c);
//		
//		ValueConverter returnedConverter = cs.getConverter(BasicPackage.Literals.NPE);
//		assertEquals(converter, returnedConverter);
//		
//		unregisterService(converter);
//		
//		try {
//			cs.getConverter(BasicPackage.Literals.NPE);
//			fail("No expected to reach this line of code");
//		} catch (IllegalStateException e) {
//		}
//	}
//
//}
