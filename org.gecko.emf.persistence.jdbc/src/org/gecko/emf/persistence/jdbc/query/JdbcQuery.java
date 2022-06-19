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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author mark
 * @since 16.04.2022
 */
public interface JdbcQuery {
	
	public static final String QUERY_COUNT = "SELECT COUNT(%s) FROM %s";
	public static final String QUERY_ALL = "SELECT %s FROM %s";
	public static final String QUERY_ID = "SELECT %s FROM %s WHERE %s=%s";
	

	/**
	 * Executes a Jdbc query
	 * @param connection the connection must not be null
	 * @param tableName the table name to query
	 * @param column the column to query against
	 * @return the Jdbc {@link ResultSet}
	 * @throws SQLException
	 */
	public ResultSet executeQuery(Connection connection, String tableName, String column) throws SQLException;
	
	/**
	 * Same as {@link JdbcQuery#executeQuery(Connection, String, String)} with last parameter <code>null</code>
	 * @param connection the connection must not be null
	 * @param tableName the table name to query
	 * @return the Jdbc {@link ResultSet}
	 * @throws SQLException
	 */
	public ResultSet executeQuery(Connection connection, String tableName) throws SQLException;
	
}
