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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.gecko.emf.persistence.PersistenceException;
import org.gecko.emf.persistence.codec.BasicEObjectCodec;
import org.gecko.emf.persistence.codec.EClassProvider;
import org.gecko.emf.persistence.jdbc.context.JdbcInputContext;

/**
 * Maps the Jdbc result into an {@link EObject}
 * @author Mark Hoffmann
 * @since 19.06.2022
 */
public class JdbcEObjectCodec extends BasicEObjectCodec<ResultSet, JdbcInputContext> implements JdbcInputMapper {
	
	private static final Logger LOGGER = Logger.getLogger(JdbcEObjectCodec.class.getName());
	
	/**
	 * Creates a new instance.
	 * @param context the input context
	 * @param resultEClass the resulting {@link EClass}, can be <code>null</code>
	 * @param typeColumn the type definition column name, can be <code>null</code>
	 */
	public JdbcEObjectCodec(JdbcInputContext context, EClassProvider eClassProvider) {
		super(context, eClassProvider);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.JdbcInputMapper#initialize()
	 */
	@Override
	public void initialize() throws SQLException {
		EClass resultEClass = getEClassProvider().getConfiguredEClass();
		ResultSetMetaData metaData = getResult().getMetaData();
		for (int c = 1; c <= metaData.getColumnCount();c++) {
			String columnName = metaData.getColumnName(c);
			getColumns().add(columnName);
		}
		if (resultEClass != null) {
			mappableFeatures.putAll(buildMappableFeaturesMap(resultEClass, getColumns()));
		}

	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.JdbcInputMapper#hasNext()
	 */
	@Override
	public boolean hasNext() throws SQLException {
		return getResult().next();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.JdbcInputMapper#next()
	 */
	@Override
	public EObject next() throws SQLException {
		try {
			return readEntry();
		} catch (PersistenceException e) {
			if (e.getCause() instanceof SQLException) {
				throw (SQLException)e.getCause();
			}
			throw new SQLException("Cannot get read table entry", e);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.JdbcInputMapper#close()
	 */
	@Override
	public void close() {
		try {
			getResult().close();
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, e, ()->"Error closing the JDBC result set");
		}
	}


	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.codec.BasicEObjectCodec#doInitialize()
	 */
	@Override
	protected void doInitialize() {
		getContext().setMapper(this);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.codec.BasicEObjectCodec#getObject(java.lang.String)
	 */
	@Override
	protected Object getObject(String columnName) throws PersistenceException {
		try {
			return getResult().getObject(columnName);
		} catch (SQLException e) {
			throw new PersistenceException("Cannot get object for column: " + columnName, e);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.codec.BasicEObjectCodec#doReadTypeColumn(java.lang.String)
	 */
	@Override
	protected String doReadTypeColumn(String typeColumn) throws PersistenceException {
		try {
			return getResult().getString(typeColumn);
		} catch (SQLException e) {
			throw new PersistenceException("Cannot get type column string value for column: " + typeColumn, e);
		}
	}

}
