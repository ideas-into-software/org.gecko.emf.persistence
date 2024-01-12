/**
 * Copyright (c) 2012 - 2018 Data In Motion and others.
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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.gecko.emf.mongo.util.ProjectionHelper;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author ChristophDockhorn
 * @since 20.11.2018
 */

public class TestProjectionHelper {

	@Test
	public void test() {
		assertTrue("java.lang.Short".equals("java.lang.Short"));
		assertTrue("java.lang.Short" == "java.lang.Short");
	}
	
	@Test
	public void testThreeSegments() {
		
		Set<String> data = new HashSet<String>(10);
		data.add("abc.def.ghi");
		data.add("abc.def.jkl");
		data.add("abc.def.mno");
		
		Set<String> featurePathKeys = ProjectionHelper.evaluateKeys(data, "_type");
		
		assertTrue(featurePathKeys.contains("abc._type"));
		assertTrue(featurePathKeys.contains("abc.def._type"));
		assertEquals(2, featurePathKeys.size());
		
	}
	
	// same as before, but with different second segments
	@Test
	public void testSimple() {
		
		Set<String> data = new HashSet<String>(10);
		data.add("abc");
		data.add("efd.jkl");
		data.add("fde");
		
		Set<String> featurePathKeys = ProjectionHelper.evaluateKeys(data, "_type");
		
		assertEquals(1, featurePathKeys.size());
		assertTrue(featurePathKeys.contains("efd._type"));
	}
	
	
	// same as before, but with different second segments
	@Test
	public void testThreeSegments2() {
		
		Set<String> data = new HashSet<String>(10);
		data.add("abc.def.ghi");
		data.add("abc.efd.jkl");
		data.add("abc.fde.mno");
		
		Set<String> featurePathKeys = ProjectionHelper.evaluateKeys(data, "_type");
		
		assertTrue(featurePathKeys.contains("abc._type"));
		assertTrue(featurePathKeys.contains("abc.def._type"));
		assertTrue(featurePathKeys.contains("abc.efd._type"));
		assertTrue(featurePathKeys.contains("abc.fde._type"));
		assertEquals(4, featurePathKeys.size());
		
	}
	
	@Test
	public void testThreeSegments3() {
		
		Set<String> data = new HashSet<String>(10);
		data.add("abc.def.ghi");
		data.add("abc.efd.jkl");
		data.add("abc.fde.mno");
		
		Set<String> featurePathKeys = ProjectionHelper.evaluateKeys(data, "_type");
		
		assertTrue(featurePathKeys.contains("abc._type"));
		assertTrue(featurePathKeys.contains("abc.def._type"));
		assertTrue(featurePathKeys.contains("abc.efd._type"));
		assertTrue(featurePathKeys.contains("abc.fde._type"));
		assertEquals(4, featurePathKeys.size());
		
	}
	
	@Test
	public void testFourSegments() {
		
		Set<String> data = new HashSet<String>(10);
		data.add("abc.def.ghi.plo");
		data.add("abc.def.jkl.kre");
		data.add("abc.def.mno.xyz");
		
		Set<String> featurePathKeys = ProjectionHelper.evaluateKeys(data, "_type");
		
		assertEquals(5, featurePathKeys.size());
		assertTrue(featurePathKeys.contains("abc._type"));
		assertTrue(featurePathKeys.contains("abc.def._type"));
		assertTrue(featurePathKeys.contains("abc.def.ghi._type"));
		assertTrue(featurePathKeys.contains("abc.def.jkl._type"));
		assertTrue(featurePathKeys.contains("abc.def.mno._type"));
				
	}
	
	@Test
	public void testFiveSegments() {
		
		Set<String> data = new HashSet<String>(10);
		data.add("abc.def.ghi.plo.123");
		data.add("abc.def.jkl.kre.456");
		data.add("abc.def.mno.xyz.789");
		
		Set<String> featurePathKeys = ProjectionHelper.evaluateKeys(data, "_type");
		
		assertEquals(8, featurePathKeys.size());
		assertTrue(featurePathKeys.contains("abc._type"));
		assertTrue(featurePathKeys.contains("abc.def._type"));
		assertTrue(featurePathKeys.contains("abc.def.jkl._type"));
		assertTrue(featurePathKeys.contains("abc.def.jkl.kre._type"));
		assertTrue(featurePathKeys.contains("abc.def.ghi._type"));
		assertTrue(featurePathKeys.contains("abc.def.ghi.plo._type"));
		assertTrue(featurePathKeys.contains("abc.def.mno._type"));
		assertTrue(featurePathKeys.contains("abc.def.mno.xyz._type"));
		
	}
	
	@Test
	public void testFiveSegments2() {
		
		Set<String> data = new HashSet<String>(10);
		data.add("abc.def.ghi.plo.123");
		data.add("abc.def.jkl.kre.456");
		data.add("abc.def.mno.xyz.789");
		
		Set<String> featurePathKeys = ProjectionHelper.evaluateKeys(data, "_type");
		
		assertEquals(8, featurePathKeys.size());
		assertTrue(featurePathKeys.contains("abc._type"));
		assertTrue(featurePathKeys.contains("abc.def._type"));
		assertTrue(featurePathKeys.contains("abc.def.jkl._type"));
		assertTrue(featurePathKeys.contains("abc.def.ghi._type"));
		assertTrue(featurePathKeys.contains("abc.def.jkl.kre._type"));
		assertTrue(featurePathKeys.contains("abc.def.mno.xyz._type"));
		
	}
	
	@Test
	public void testLongList() {
		
		Set<String> data = new HashSet<String>(10);
		data.add("abc.def.ghi.plo");
		data.add("abc.def.jkl.kre");
		data.add("abc.def.mno.xyz");
		data.add("abc.def.pqr.123");
		data.add("abc.def.stu.456");
		data.add("abc.def.vwx.789");
		data.add("abc.def.yzz.012");
		
		
		Set<String> featurePathKeys = ProjectionHelper.evaluateKeys(data, "_type");
		
		assertEquals(9, featurePathKeys.size());
		assertTrue(featurePathKeys.contains("abc._type"));
		assertTrue(featurePathKeys.contains("abc.def._type"));
		assertTrue(featurePathKeys.contains("abc.def.ghi._type"));
		assertTrue(featurePathKeys.contains("abc.def.jkl._type"));
		assertTrue(featurePathKeys.contains("abc.def.mno._type"));
		assertTrue(featurePathKeys.contains("abc.def.vwx._type"));
		
	}
	
	
	@Test
	public void testLongList2() {
		
		Set<String> data = new HashSet<String>(10);
		data.add("abc.def.ghi.plo.12");
		data.add("abc.def.jkl.kre.34");
		data.add("abc.def.mno.xyz.45");
		data.add("abc.def.pqr.123.56");
		data.add("abc.def.stu.456.67");
		data.add("abc.def.vwx.789.78");
		data.add("abc.def.yzz.012.89");
		
		
		Set<String> featurePathKeys = ProjectionHelper.evaluateKeys(data, "_type");
		
		assertEquals(16, featurePathKeys.size());
		assertTrue(featurePathKeys.contains("abc._type"));
		assertTrue(featurePathKeys.contains("abc.def._type"));
		assertTrue(featurePathKeys.contains("abc.def.ghi._type"));
		assertTrue(featurePathKeys.contains("abc.def.jkl._type"));
		assertTrue(featurePathKeys.contains("abc.def.mno._type"));
		assertTrue(featurePathKeys.contains("abc.def.vwx._type"));
		assertTrue(featurePathKeys.contains("abc.def.vwx.789._type"));
		assertTrue(featurePathKeys.contains("abc.def.pqr.123._type"));
		
	}
	
	@Test
	public void testLongList3() {
		
		Set<String> data = new HashSet<String>(10);
		data.add("abc.def.ghi.plo.12");
		data.add("abc.def.jkl.kre.34");
		data.add("abc.def.mno.xyz.45");
		data.add("abc.def.pqr.123.56");
		data.add("abc.def.stu.456.67");
		data.add("abc.def.vwx.789.78");
		data.add("abc.def.yzz.012.89");
		
		
		Set<String> featurePathKeys = ProjectionHelper.evaluateKeys(data, "_type");
		
		assertEquals(16, featurePathKeys.size());
		assertTrue(featurePathKeys.contains("abc._type"));
		assertTrue(featurePathKeys.contains("abc.def._type"));
		assertTrue(featurePathKeys.contains("abc.def.ghi._type"));
		assertTrue(featurePathKeys.contains("abc.def.jkl._type"));
		assertTrue(featurePathKeys.contains("abc.def.mno._type"));
		assertTrue(featurePathKeys.contains("abc.def.vwx._type"));
		assertTrue(featurePathKeys.contains("abc.def.vwx.789._type"));
		assertTrue(featurePathKeys.contains("abc.def.pqr.123._type"));
		
	}

}
