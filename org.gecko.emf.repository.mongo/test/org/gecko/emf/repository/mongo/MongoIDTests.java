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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bson.types.ObjectId;
import org.eclipse.emf.ecore.EObject;
import org.gecko.emf.osgi.example.model.basic.BasicFactory;
import org.gecko.emf.osgi.example.model.basic.Person;
import org.gecko.emf.repository.helper.RepositoryHelper;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author mark
 * @since 16.05.2018
 */
public class MongoIDTests {

	@Test
	public void testObjectIds() {
		Person p = BasicFactory.eINSTANCE.createPerson();
		p.setFirstName("Test");
		
		p.getContact().add(BasicFactory.eINSTANCE.createContact());
		p.getContact().add(BasicFactory.eINSTANCE.createContact());
		
		setIDs(p);
		
		assertNotNull(p.getId());
		assertTrue(ObjectId.isValid(p.getId()));
		
	}
	
	protected void setIDs(EObject rootObject) {
		RepositoryHelper.setIds(rootObject, () -> new ObjectId().toString(), () -> new ObjectId().toString());	
	}
	
	
}
