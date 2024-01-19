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
package org.gecko.emf.repository.mongo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.gecko.emf.repository.mongo.query.MongoQueryBuilder;
import org.gecko.emf.repository.query.IQuery;
import org.gecko.emf.repository.query.IQueryBuilder;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author mark
 * @since 16.05.2018
 */
public class MongoQueryTests {

	@Test
	public void testAllQuery() {
		IQueryBuilder builder = new MongoQueryBuilder();
		IQuery query = builder.allQuery().build();
		assertNotNull(query);
		assertEquals("{}", query.getFilterString());
	}
	
	@Test
	public void testValueQuery() {
		IQueryBuilder builder = new MongoQueryBuilder();
		IQuery query = builder.column("test").simpleValue("value").build();
		assertNotNull(query);
		assertEquals("{\"test\": \"value\"}", query.getFilterString());
		
		builder = new MongoQueryBuilder();
		query = builder.simpleValue("value").build();
		assertNotNull(query);
		assertEquals("", query.getFilterString());
	}
	
	@Test
	public void testInQuery() {
		IQueryBuilder builder = new MongoQueryBuilder();
		IQuery query = builder.column("test").in("hello", "world").build();
		assertNotNull(query);
		assertEquals("{\"test\": {\"$in\": [\"hello\", \"world\"]}}", query.getFilterString());
		
		builder = new MongoQueryBuilder();
		query = builder.in("hallo", "welt").build();
		assertNotNull(query);
		assertEquals("", query.getFilterString());
		
		builder = new MongoQueryBuilder();
		query = builder.in(1, 2).column("counter").build();
		assertNotNull(query);
		assertEquals("{\"counter\": {\"$in\": [1, 2]}}", query.getFilterString());
	}
	
	@Test
	public void testRangeQuery() {
		IQueryBuilder builder = new MongoQueryBuilder();
		IQuery query = builder.rangeQuery().column("test").startValue(1).endValue(4).build();
		assertNotNull(query);
		assertEquals("{\"test\": {\"$gt\": 1, \"$lt\": 4}}", query.getFilterString());
		
		builder = new MongoQueryBuilder();
		query = builder.column("test").startValue(1).endValue(4).build();
		assertNull(query);
//		
		builder = new MongoQueryBuilder();
		query = builder.rangeQuery().column("test").startValue(1, true).endValue(4).build();
		assertNotNull(query);
		assertEquals("{\"test\": {\"$gte\": 1, \"$lt\": 4}}", query.getFilterString());
		builder = new MongoQueryBuilder();
		query = builder.rangeQuery().column("test").startValue(1, true).endValue(4, true).build();
		assertNotNull(query);
		assertEquals("{\"test\": {\"$gte\": 1, \"$lte\": 4}}", query.getFilterString());
		
		Calendar c = GregorianCalendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 15);
		c.set(Calendar.MONTH, 4);
		c.set(Calendar.YEAR, 2018);
		c.set(Calendar.HOUR_OF_DAY, 10);
		c.set(Calendar.MINUTE, 30);
		c.set(Calendar.SECOND, 50);
		c.set(Calendar.MILLISECOND, 0000);
		c.set(Calendar.ZONE_OFFSET, 0);
		
		Date today = c.getTime();
		c.add(Calendar.DAY_OF_YEAR, -1);
		Date yesterday = c.getTime();
		builder = new MongoQueryBuilder();
		query = builder.rangeQuery().column("test").startValue(yesterday).endValue(today).build();
		assertNotNull(query);
//		assertEquals("{ \"test\" : { \"$gt\" : { \"$date\" : \"2018-05-14T08:30:50.000Z\"} , \"$lt\" : { \"$date\" : \"2018-05-15T09:30:50.000Z\"}}}", query.getFilterString());
		
		builder = new MongoQueryBuilder();
		query = builder.rangeQuery().column("test").startValue("a").endValue("g").build();
		assertNotNull(query);
		assertEquals("{\"test\": {\"$gt\": \"a\", \"$lt\": \"g\"}}", query.getFilterString());
		
	}

}
