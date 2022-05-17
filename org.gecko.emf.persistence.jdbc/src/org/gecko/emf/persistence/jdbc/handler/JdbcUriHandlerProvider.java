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
package org.gecko.emf.persistence.jdbc.handler;

import static org.gecko.emf.persistence.jdbc.JdbcPersistenceConstants.RESOURCESET_CONFIG_PROP;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.eclipse.emf.ecore.resource.URIHandler;
import org.gecko.emf.osgi.UriHandlerProvider;
import org.gecko.emf.persistence.InputStreamFactory;
import org.gecko.emf.persistence.OutputStreamFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.util.promise.Promise;
import org.osgi.util.promise.PromiseFactory;

/**
 * 
 * @author mark
 * @since 16.04.2022
 * @TODO Make this configurable an create a java.sql.Connection out of the DataSourceFactory
 * Change Generic type of the In- and Outputstrems from DataSourceFactory to Connection
 */
@Component(service = UriHandlerProvider.class, property = RESOURCESET_CONFIG_PROP)
public class JdbcUriHandlerProvider implements UriHandlerProvider {
	
	private volatile JdbcURIHandlerImpl uriHandler;
	private volatile InputStreamFactory<Promise<Connection>> inputStreamFactory;
	private volatile OutputStreamFactory<Promise<Connection>> outputStreamFactory;
	private final Map<String,DataSourceFactory> connections = new ConcurrentHashMap<>();
	private final PromiseFactory pf = new PromiseFactory(Executors.newCachedThreadPool(), Executors.newScheduledThreadPool(2));
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.osgi.UriHandlerProvider#getURIHandler()
	 */
	@Override
	public URIHandler getURIHandler() {
		if (uriHandler == null) {
			uriHandler = new JdbcURIHandlerImpl(connections, inputStreamFactory, outputStreamFactory, pf);
		}
		return uriHandler;
	}

	/**
	 * @param connection
	 */
	public void addDataSourceFactory(DataSourceFactory connection, String name) {
			connections.put(name, connection);
		
	}
	public void removeDataSourceFactory(DataSourceFactory connection, String name) {
			connections.remove(name);
	}

	/**
	 * @param outputStreamFactory
	 */
	@Reference
	public void setOutputStreamFactory(OutputStreamFactory<Promise<Connection>> outputStreamFactory) {
		this.outputStreamFactory = outputStreamFactory;
	}

	/**
	 * @param inputStreamFactory
	 */
	public void setInputStreamFactory(InputStreamFactory<Promise<Connection>> inputStreamFactory) {
		this.inputStreamFactory = inputStreamFactory;
	}
	
}
