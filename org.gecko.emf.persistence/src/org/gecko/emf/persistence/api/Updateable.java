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
package org.gecko.emf.persistence.api;

import java.util.Map;

/**
 * Interface to create and update data
 * @author Mark Hoffmann
 * @since 14.02.2023
 */
public interface Updateable {
	
	/**
	 * Executes a update operation
	 * @param properties additional update properties
	 * @throws PersistenceException thrown on lower level errors 
	 */
	void update(Map<Object, Object> properties) throws PersistenceException;
	
	/**
	 * Executes a create operation
	 * @param properties additional create properties
	 * @throws PersistenceException thrown on lower level errors 
	 */
	void create(Map<Object, Object> properties) throws PersistenceException;
	
}
