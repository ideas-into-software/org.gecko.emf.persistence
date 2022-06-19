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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.gecko.emf.utilities.Request;

/**
 * An JDBC all query
 * @author mark
 * @since 19.06.2022
 */
public class JdbcAllQuery implements JdbcQuery {

	private final Request request;

	/**
	 * Creates a new instance.
	 */
	public JdbcAllQuery(Request request) {
		this.request = request;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.query.JdbcQuery#executeQuery(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public ResultSet executeQuery(Connection connection, String tableName, String column) throws SQLException {
		return executeQuery(connection, tableName);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.query.JdbcQuery#executeQuery(java.sql.Connection, java.lang.String)
	 */
	@Override
	public ResultSet executeQuery(Connection connection, String tableName) throws SQLException {
		String query;
		List<String> projection = request == null ? Collections.emptyList() : request.getProjection();
		if (projection.isEmpty()) {
			query = String.format(QUERY_ALL, "*", tableName);
		} else {
			//  TODO Escape SQL here Apache Commons Text
			String columns = projection.stream().collect(Collectors.joining(","));
			query = String.format(QUERY_ALL, columns, tableName);
		}
		Statement allStatement = connection.createStatement();
		return allStatement.executeQuery(query);
	}
}
