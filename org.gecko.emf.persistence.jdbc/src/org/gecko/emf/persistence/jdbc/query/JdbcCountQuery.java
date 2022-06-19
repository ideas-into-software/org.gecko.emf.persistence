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
import java.sql.Statement;

/**
 * 
 * @author mark
 * @since 19.06.2022
 */
public class JdbcCountQuery implements JdbcQuery {

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.query.JdbcQuery#executeQuery(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public ResultSet executeQuery(Connection connection, String tableName, String column) throws SQLException {
		Statement countStatement = connection.createStatement();
		String query;
		if (column != null) {
			query = String.format(QUERY_COUNT, column, tableName);
		} else {
			query = String.format(QUERY_COUNT, "*", tableName);
		}
		return countStatement.executeQuery(query);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.query.JdbcQuery#executeQuery(java.sql.Connection, java.lang.String)
	 */
	@Override
	public ResultSet executeQuery(Connection connection, String tableName) throws SQLException {
		return executeQuery(connection, tableName, null);
	}
}
