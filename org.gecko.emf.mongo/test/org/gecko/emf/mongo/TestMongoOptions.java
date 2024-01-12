/**
 * Copyright (c) 2012 - 2019 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.mongo;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.gecko.emf.osgi.example.model.basic.BasicPackage;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author mark
 * @since 28.09.2019
 */
public class TestMongoOptions {

	@Test
	public void testOptionCollectionName() {
		assertNull(Options.getCollectionName(null));
		
		Map<String, Object> properties = new HashMap<String, Object>();
		assertNull(Options.getCollectionName(properties));
		
		properties.put("test", "me");
		assertNull(Options.getCollectionName(properties));
		
		properties.put(Options.OPTION_COLLECTION_NAME, BasicPackage.Literals.PERSON);
		assertEquals(BasicPackage.Literals.PERSON.getName(), Options.getCollectionName(properties));
		
		properties.put(Options.OPTION_COLLECTION_NAME, "uhu");
		assertEquals("uhu", Options.getCollectionName(properties));
		
		properties.put(Options.OPTION_COLLECTION_NAME, Integer.valueOf(123));
		assertEquals("123", Options.getCollectionName(properties));
		
	}

}
