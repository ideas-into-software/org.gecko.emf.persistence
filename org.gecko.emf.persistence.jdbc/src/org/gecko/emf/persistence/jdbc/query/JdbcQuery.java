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
package org.gecko.emf.persistence.jdbc.query;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author mark
 * @since 16.04.2022
 */
public class JdbcQuery {
	
	private Set<String> projection = new HashSet<>();
	private Set<String> tables = new HashSet<>();

	public String getSelect() {
		return "SELECT * FROM Person";
	}
	
	/**
	 * Returns the projection.
	 * @return the projection
	 */
	public Set<String> getProjection() {
		return projection;
	}
}
