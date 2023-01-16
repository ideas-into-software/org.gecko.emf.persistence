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
package org.gecko.emf.persistence.mapping;

import org.gecko.emf.persistence.api.PersistenceException;

/**
 * Input mapper interface
 * @author Mark Hoffmann
 * @since 16.01.2023
 */
public interface EObjectMapper {
	
	/**
	 * Initializes the mapper
	 * @throws PersistenceException
	 */
	public void initialize() throws PersistenceException;
	
	/**
	 * Closes the mapper
	 */
	void close();

}
