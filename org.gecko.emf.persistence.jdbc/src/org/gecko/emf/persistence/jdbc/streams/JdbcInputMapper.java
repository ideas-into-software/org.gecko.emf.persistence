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
package org.gecko.emf.persistence.jdbc.streams;

import java.sql.SQLException;

import org.eclipse.emf.ecore.EObject;

/**
 * 
 * @author mark
 * @since 19.06.2022
 */
public interface JdbcInputMapper {
	
	public void initialize() throws SQLException;
	
	boolean hasNext() throws SQLException;
	
	EObject next() throws SQLException;
	
	void close();

}
