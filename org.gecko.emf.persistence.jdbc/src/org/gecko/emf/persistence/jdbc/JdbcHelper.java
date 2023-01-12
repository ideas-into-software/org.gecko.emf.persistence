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
package org.gecko.emf.persistence.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * 
 * @author mark
 * @since 06.01.2023
 */
public class JdbcHelper {
	
	private static final String CREATE_DATABASE = "CREATE DATABASE %s";

	public static boolean existDatabase(String databaseName, Connection connection) throws SQLException {
		Objects.requireNonNull(databaseName);
		Objects.requireNonNull(connection);
		String catalog = connection.getCatalog();
		String schema = connection.getSchema();
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		try (ResultSet tables = databaseMetaData.getTables(catalog, schema, databaseName, null)) {
			while (tables.next()) {
				String existingDatabaseName = tables.getString(3);
				if(existingDatabaseName.equalsIgnoreCase(databaseName)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean createDatabaseIfExists(String databaseName, Connection connection) throws SQLException {
		Objects.requireNonNull(databaseName);
		Objects.requireNonNull(connection);
		
		if (!existDatabase(databaseName, connection)) {
			try (Statement statement = connection.createStatement()) {
				int executeUpdate = statement.executeUpdate(String.format(CREATE_DATABASE, databaseName));
				return executeUpdate == 1;
			}
		}
		return false;
	}
	
	public static boolean existTable(String tableName, Connection connection) throws SQLException {
		Objects.requireNonNull(tableName);
		Objects.requireNonNull(connection);
		String catalog = connection.getCatalog();
		String schema = connection.getSchema();
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		try (ResultSet tables = databaseMetaData.getTables(catalog, schema, tableName, null)) {
			while (tables.next()) {
				String existingTable = tables.getString(3);
				if(existingTable.equalsIgnoreCase(tableName)){
					return true;
				}
			}
		}
		return false;
	}
	
//	public static boolean createTable(String tableName, EClass eClass, Connection connection) throws SQLException {
//		Objects.requireNonNull(tableName);
//		Objects.requireNonNull(eClass);
//		Objects.requireNonNull(connection);
//		if (!existTable(tableName, connection) ) {
//			
//			StringBuilder sb = new StringBuilder();
//			sb.append("CREATE TABLE ");
//			sb.append(tableName);
//			sb.append("(");
//			PersistenceHelper.
//		}
//		ResultSet resultSet = connection.getMetaData().getTables(null, null, tableName, null);
//		try {
//			while (resultSet.next()) {
//				String existingTableName = resultSet.getString(1);
//				if(existingTableName.equalsIgnoreCase(tableName)){
//					return true;
//				}
//			}
//		} finally {
//			resultSet.close();
//		}
//		return false;
//	}
	
	
	
}
