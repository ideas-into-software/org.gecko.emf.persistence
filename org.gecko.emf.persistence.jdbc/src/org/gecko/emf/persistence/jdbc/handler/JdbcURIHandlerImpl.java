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
 *     Bryan Hunt - initial API and implementation
 *     Ed Merks - initial API and implementation
 */
package org.gecko.emf.persistence.jdbc.handler;

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import org.eclipse.emf.common.util.URI;
import org.gecko.emf.persistence.InputStreamFactory;
import org.gecko.emf.persistence.OutputStreamFactory;
import org.gecko.emf.persistence.PersistenceURIHandlerImpl;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.util.promise.Promise;
import org.osgi.util.promise.PromiseFactory;

/**
 * This EMF URI handler interfaces to JDBC. This URI handler can handle URIs with the "jdbc"
 * scheme. The URI path must have exactly 3 segments and be of the form /database/table/{id}
 * where id is optional the first time the EMF object is saved. When building queries, do not
 * specify an id, but make sure path has 3 segments by placing a "/" after the collection.
 * 
 * Note that if the id is not specified when the object is first saved, DB will assign the id
 * and the URI of the EMF Resource will be modified to include the id in the URI. Examples of valid
 * URIs:
 * 
 * jdbc://dataSourceId/database/table/
 * jdbc://dataSourceId/database/table/4d0a3e259095b5b334a59df0
 * 
 * This class is intended to be used with the IResourceSetFactory service. If you are not using the
 * factory service, you will have to supply instances of IMongoLocator, IIntputStreamFActory, and
 * IOutputStreamFactory.
 * 
 * @author bhunt
 * 
 */
public class JdbcURIHandlerImpl extends PersistenceURIHandlerImpl<Promise<Connection>> {

	private static final String SCHEMA_DB = "jdbc";
	private static final String DB_TEMPLATE = "jdbc:%s:%s;create=true";
	private final PromiseFactory pf;
	private Map<String, DataSourceFactory> connections;
	/**
	 * Creates a new instance.
	 * @param databaseLocator an instance of the mongo locator service
	 * @param inputStreamFactory an instance of the input stream factory service
	 * @param outputStreamFactory an instance of the output stream factory service
	 * @param promiseFactory the promise factory
	 */
	public JdbcURIHandlerImpl(Map<String, DataSourceFactory> databases, InputStreamFactory<Promise<Connection>> inputStreamFactory, OutputStreamFactory<Promise<Connection>> outputStreamFactory, PromiseFactory promiseFactory) {
		super(inputStreamFactory, outputStreamFactory);
		this.pf = promiseFactory;
		this.connections = databases;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.PersistenceURIHandlerImpl#getSchema()
	 */
	@Override
	public String getSchema() {
		return SCHEMA_DB;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.PersistenceURIHandlerImpl#getConnection(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	public Promise<Connection> getConnection(URI uri, Map<?, ?> options) {
		String name = uri.host();
		DataSourceFactory dataSourceFactory = connections.get(name);
		String database = getDatabase(uri, options);
		String type = (String) options.get("type");
		type = type.toLowerCase();
		Properties prop = new Properties();
		prop.putAll(options);
		String dbUrl = String.format(DB_TEMPLATE, type, database);
		Promise<Connection> connectionP = pf.submit(()->dataSourceFactory.createDriver(null).connect(dbUrl, null));
		return connectionP;
	}

	protected String getTable(URI uri, Map<?, ?> options) {
		return uri.segment(1);
	}

	protected String getDatabase(URI uri, Map<?, ?> options) {
		return uri.segment(0);
	}
	
}
