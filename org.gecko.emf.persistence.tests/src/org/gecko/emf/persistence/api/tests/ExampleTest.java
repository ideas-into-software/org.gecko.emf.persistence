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
package org.gecko.emf.persistence.api.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.BundleContext;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * See documentation here: 
 * 	https://github.com/osgi/osgi-test
 * 	https://github.com/osgi/osgi-test/wiki
 * Examples: https://github.com/osgi/osgi-test/tree/main/examples
 * 
 * TODO Test
 * - Test PersistenceResource
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
@ExtendWith(MockitoExtension.class)
public class ExampleTest {

	@BeforeEach
	public void before(@InjectBundleContext BundleContext ctx) {

	}

	@Test
	public void testFileToString() {
		try {
			String ioString = FileUtils.readFileToString(new File("data/pom.xml"), "UTF-8");
			String javaString = Files.readString(Path.of("data/pom.xml"), StandardCharsets.UTF_8);
			assertEquals(ioString, javaString);
		} catch (IOException e) {
			fail("Error ", e);
		}
	}
	@Test
	public void testFileToStringnotEqual() {
		try {
			String ioString = FileUtils.readFileToString(new File("data/pom2.xml"), "UTF-8");
			String javaString = Files.readString(Path.of("data/pom.xml"), StandardCharsets.UTF_8);
			assertNotEquals(ioString, javaString);
		} catch (IOException e) {
			fail("Error ", e);
		}
	}
	@Test
	public void testWriteByteArrayToFile() {
		try {
			byte[] data = "Hello World!".getBytes();
			File newFile = Path.of("data/newpom.xml").toFile();
			Path newFileJava = Path.of("data/newpomj.xml");
			FileUtils.writeByteArrayToFile(new File(newFile.getParentFile(), FilenameUtils.getName(newFile.getName())),
					data);
			Files.write(newFileJava, data);
			
			String ioString = Files.readString(newFile.toPath(), StandardCharsets.UTF_8);
			String javaString = Files.readString(newFileJava, StandardCharsets.UTF_8);
			System.out.println(javaString + " - " + ioString);
			assertEquals(ioString, javaString);
		} catch (IOException e) {
			fail("Error ", e);
		}
	}

}
