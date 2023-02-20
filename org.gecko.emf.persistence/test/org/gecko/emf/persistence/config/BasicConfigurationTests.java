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
package org.gecko.emf.persistence.config;

import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_AUTH_SOURCE;
import static org.gecko.emf.persistence.config.PersistenceConfiguratorConstants.PROP_REPOSITORY_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Map;

import org.gecko.emf.persistence.config.model.BasicConfiguration;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author Mark Hoffmann
 * @since 20.02.2023
 */
public class BasicConfigurationTests {
	
	@Test
	public void testGetPropertiesNPE() {
		assertThrows(NullPointerException.class, ()->BasicConfiguration.createProperties(null, null));
		assertThrows(NullPointerException.class, ()->BasicConfiguration.createProperties("test", null));
		assertThrows(NullPointerException.class, ()->BasicConfiguration.createProperties(null, Collections.emptyMap()));
	}
	
	@Test
	public void testGetPropertiesEmpty() {
		Map<String, Object> props = BasicConfiguration.createProperties("foo.prop.", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE"));
		assertNotNull(props);
		assertTrue(props.isEmpty());
		
		props = BasicConfiguration.createProperties("foo.prop.", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE", "demo", "test"));
		assertNotNull(props);
		assertTrue(props.isEmpty());
	}
	
	@Test
	public void testGetProperties() {
		Map<String, Object> props = BasicConfiguration.createProperties("foo.prop.", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE", "foo.prop.Demo", "Test"));
		assertNotNull(props);
		assertEquals(1, props.size());
		assertEquals("Test", props.get("Demo"));
		
		props = BasicConfiguration.createProperties("foo.prop.", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE", "foo.prop.Demo", "Test", "foo.prop.foo", "Bar"));
		assertNotNull(props);
		assertEquals(2, props.size());
		assertEquals("Bar", props.get("foo"));
	}
	
	@Test
	public void testGetPropertiesEmptyPrefix() {
		Map<String, Object> props = BasicConfiguration.createProperties("", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE", "prop.Demo", "Test"));
		assertNotNull(props);
		assertEquals(3, props.size());
		assertEquals("Test", props.get("prop.Demo"));
		assertEquals("authDB", props.get("foo." + PROP_AUTH_SOURCE));
		
	}
	
	@Test
	public void testGetPropertiesNoAdd() {
		Map<String, Object> props = BasicConfiguration.createProperties("foo.prop.", Map.of("foo." + PROP_AUTH_SOURCE, "authDB", 
				"foo." + PROP_REPOSITORY_TYPE, "PROTOTYPE", "foo.prop.Demo", "Test"));
		assertNotNull(props);
		assertEquals(1, props.size());
		assertEquals("Test", props.get("Demo"));
		
		assertThrows(UnsupportedOperationException.class, ()->props.put("A", "B"));
	}

}
