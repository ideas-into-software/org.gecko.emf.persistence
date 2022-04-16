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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.ecore.resource.URIHandler;
import org.gecko.emf.osgi.UriHandlerProvider;
import org.gecko.emf.persistence.InputStreamFactory;
import org.gecko.emf.persistence.OutputStreamFactory;
import static org.gecko.emf.persistence.jdbc.JdbcPersistenceConstants.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.jdbc.DataSourceFactory;

/**
 * 
 * @author mark
 * @since 16.04.2022
 */
@Component(service = UriHandlerProvider.class, property = RESOURCESET_CONFIG_PROP)
public class JdbcUriHandlerProvider implements UriHandlerProvider {
	
	private volatile JdbcURIHandlerImpl uriHandler;
	private volatile InputStreamFactory<DataSourceFactory> inputStreamFactory;
	private volatile OutputStreamFactory<DataSourceFactory> outputStreamFactory;
	private final Map<String,DataSourceFactory> connections = new ConcurrentHashMap<>();

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.osgi.UriHandlerProvider#getURIHandler()
	 */
	@Override
	public URIHandler getURIHandler() {
		if (uriHandler == null) {
			uriHandler = new JdbcURIHandlerImpl(connections, inputStreamFactory, outputStreamFactory);
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
	public void setOutputStreamFactory(OutputStreamFactory<DataSourceFactory> outputStreamFactory) {
		this.outputStreamFactory = outputStreamFactory;
	}

	/**
	 * @param inputStreamFactory
	 */
	public void setInputStreamFactory(InputStreamFactory<DataSourceFactory> inputStreamFactory) {
		this.inputStreamFactory = inputStreamFactory;
	}

}
