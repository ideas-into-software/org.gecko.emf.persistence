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
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.codec.BasicEObjectCodec;
import org.gecko.emf.persistence.codec.EClassProvider;
import org.gecko.emf.persistence.context.PersistenceInputContext;
import org.gecko.emf.persistence.mapping.IteratorMapper;

/**
 * Maps the Jdbc result into an {@link EObject}
 * @author Mark Hoffmann
 * @since 19.06.2022
 */
public class JdbcEObjectCodec extends BasicEObjectCodec<ResultSet, IteratorMapper, PersistenceInputContext<ResultSet, IteratorMapper>> implements IteratorMapper {
	
	private static final Logger LOGGER = Logger.getLogger(JdbcEObjectCodec.class.getName());
	
	/**
	 * Creates a new instance.
	 * @param context the input context
	 * @param resultEClass the resulting {@link EClass}, can be <code>null</code>
	 * @param typeColumn the type definition column name, can be <code>null</code>
	 */
	public JdbcEObjectCodec(PersistenceInputContext<ResultSet, IteratorMapper> context, EClassProvider eClassProvider) {
		super(context, eClassProvider);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.mapping.EObjectMapper#initialize()
	 */
	@Override
	public void initialize() throws PersistenceException {
		EClass resultEClass = getEClassProvider().getConfiguredEClass();
		ResultSetMetaData metaData;
		try {
			metaData = getResult().getMetaData();
			for (int c = 1; c <= metaData.getColumnCount();c++) {
				String columnName = metaData.getColumnName(c);
				getColumns().add(columnName);
			}
			if (resultEClass != null) {
				mappableFeatures.putAll(buildMappableFeaturesMap(resultEClass, getColumns()));
			}
		} catch (SQLException e) {
			throw new PersistenceException("Error initializing JDBC EObject mapper", e);
		}

	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.mapping.IteratorMapper#hasNext()
	 */
	@Override
	public boolean hasNext() throws PersistenceException {
		try {
			return getResult().next();
		} catch (SQLException e) {
			throw new PersistenceException("Error calling next result in EObject mapper", e);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.mapping.IteratorMapper#next()
	 */
	@Override
	public EObject next() throws PersistenceException {
		return readEntry();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.mapping.EObjectMapper#close()
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
