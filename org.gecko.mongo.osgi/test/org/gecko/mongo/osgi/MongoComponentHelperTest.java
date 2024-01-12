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
package org.gecko.mongo.osgi;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.gecko.mongo.osgi.helper.MongoComponentHelper;
import org.junit.jupiter.api.Test;

import com.mongodb.MongoClientException;
import com.mongodb.MongoCredential;

public class MongoComponentHelperTest {

	@Test
	public void testValidateCredentials_Success01() {
		String credential = "user1:pwd1@db";
		List<MongoCredential> credentialList = MongoComponentHelper.validateCredentials(credential);
		assertNotNull(credentialList);
		assertEquals(1, credentialList.size());
		MongoCredential c = credentialList.get(0);
		assertEquals("user1", c.getUserName());
		assertEquals("pwd1", new String(c.getPassword()));
		assertEquals("db", c.getSource());
		
		c = MongoComponentHelper.validateCredential(credential);
		assertEquals("user1", c.getUserName());
		assertEquals("pwd1", new String(c.getPassword()));
		assertEquals("db", c.getSource());
		
		credential = "user1:pwd@1@db,user2:pwd2@db2";
		credentialList = MongoComponentHelper.validateCredentials(credential);
		assertNotNull(credentialList);
		assertEquals(2, credentialList.size());
		MongoCredential c1 = credentialList.get(0);
		assertEquals("user1", c1.getUserName());
		assertEquals("pwd@1", new String(c1.getPassword()));
		assertEquals("db", c1.getSource());
		MongoCredential c2 = credentialList.get(1);
		assertEquals("user2", c2.getUserName());
		assertEquals("pwd2", new String(c2.getPassword()));
		assertEquals("db2", c2.getSource());
		
		c = MongoComponentHelper.validateCredential(credential);
		assertEquals("user1", c.getUserName());
		assertEquals("pwd@1", new String(c.getPassword()));
		assertEquals("db", c.getSource());
		
		credential = "user1:@mypwd@db,user2:pwd2@db2";
		credentialList = MongoComponentHelper.validateCredentials(credential);
		assertNotNull(credentialList);
		assertEquals(2, credentialList.size());
		c1 = credentialList.get(0);
		assertEquals("user1", c1.getUserName());
		assertEquals("@mypwd", new String(c1.getPassword()));
		assertEquals("db", c1.getSource());
		c2 = credentialList.get(1);
		assertEquals("user2", c2.getUserName());
		assertEquals("pwd2", new String(c2.getPassword()));
		assertEquals("db2", c2.getSource());
		
		credential = "sdg:@sTs_2016!@sdgtlc";
		credentialList = MongoComponentHelper.validateCredentials(credential);
		assertNotNull(credentialList);
		assertEquals(1, credentialList.size());
		c1 = credentialList.get(0);
		assertEquals("sdg", c1.getUserName());
		assertEquals("@sTs_2016!", new String(c1.getPassword()));
		assertEquals("sdgtlc", c1.getSource());
	}
	
	@Test
	public void testValidateCredentials_Success02() {
		String credential = "user1:pwd1@db";
		List<MongoCredential> credentialList = MongoComponentHelper.validateCredentials(credential);
		assertNotNull(credentialList);
		assertEquals(1, credentialList.size());
		MongoCredential c = credentialList.get(0);
		assertEquals("user1", c.getUserName());
		assertEquals("pwd1", new String(c.getPassword()));
		assertEquals("db", c.getSource());
		
		credential = "user1:pwd@1@db,user2:pwd2@db2";
		credentialList = MongoComponentHelper.validateCredentials(credential);
		assertNotNull(credentialList);
		assertEquals(2, credentialList.size());
		MongoCredential c1 = credentialList.get(0);
		assertEquals("user1", c1.getUserName());
		assertEquals("pwd@1", new String(c1.getPassword()));
		assertEquals("db", c1.getSource());
		MongoCredential c2 = credentialList.get(1);
		assertEquals("user2", c2.getUserName());
		assertEquals("pwd2", new String(c2.getPassword()));
		assertEquals("db2", c2.getSource());
	}
	
	@Test
//	@Test(expected = MongoClientException.class)
	public void testValidateCredentials_Fail01() {
		String credential = "user1:pwd1db";
		assertThrows(MongoClientException.class, ()->MongoComponentHelper.validateCredentials(credential));
	}
	
	@Test
//	@Test(expected = MongoClientException.class)
	public void testValidateCredentials_Fail02() {
		String credential = "user1pwd1@db";
		assertThrows(MongoClientException.class, ()->MongoComponentHelper.validateCredentials(credential));
	}
	
	@Test
//	@Test(expected = MongoClientException.class)
	public void testValidateCredentials_Fail03() {
		String credential = "user1:pwd1@db;user2:pwd2@db2";
		assertThrows(MongoClientException.class, ()->MongoComponentHelper.validateCredentials(credential));
	}
	
	@Test
//	@Test(expected=MongoClientException.class)
	public void testValidateCredentials_Fail04() {
		String credential = "user1@db:pwd";
		assertThrows(MongoClientException.class, ()->MongoComponentHelper.validateCredentials(credential));
	}
	
	@Test
//	@Test(expected=MongoClientException.class)
	public void testValidateCredentials_Fail05() {
		String credential = "user1:pwd1@db1,user2@db2:pwd2";
		assertThrows(MongoClientException.class, ()->MongoComponentHelper.validateCredentials(credential));
	}

}
