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
package org.gecko.emf.persistence.resource;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * Resource extension for the persistence context
 * @author Mark Hoffmann
 * @since 30.05.2022
 */
public interface PersistenceResource extends Resource {
	
	/**
	 * Counts the elements of this resource
	 * @return the number of elements 
	 * @throws IOException
	 */
	long count() throws IOException;
	
	/**
	 * Counts the elements of this resource
	 * @param options the count options map
	 * @return the number of elements 
	 * @throws IOException
	 */
	long count(Map<?, ?> options) throws IOException;
	
	/**
	 * Checks weather the resource content exists 
	 * @return <code>true</code>, if the resource content exists
	 * @throws IOException
	 */
	boolean exist() throws IOException;
	
	/**
	 * Checks weather the resource content exists 
	 * @param options the exist options map
	 * @return <code>true</code>, if the resource content exists
	 * @throws IOException
	 */
	boolean exist(Map<?, ?> options) throws IOException;
	
}
