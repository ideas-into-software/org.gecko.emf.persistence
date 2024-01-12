/*******************************************************************************
 * Copyright (c) 2012 Bryan Hunt.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 *******************************************************************************/

package org.gecko.emf.mongo.handlers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.ecore.resource.URIHandler;
import org.gecko.emf.mongo.InputStreamFactory;
import org.gecko.emf.mongo.OutputStreamFactory;
import org.gecko.emf.osgi.UriHandlerProvider;
import org.gecko.mongo.osgi.MongoDatabaseProvider;

/**
 * An {@link UriHandlerProvider} to handle the mongodb scheme in an URI
 * @author bhunt
 * @author Mark Hoffmann
 */
//@Component(name="MongoURIHandlerProvider", immediate=true, service=UriHandlerProvider.class)
public class MongoURIHandlerProvider implements UriHandlerProvider {
	
	private volatile MongoURIHandlerImpl uriHandler;
	private volatile InputStreamFactory inputStreamFactory;
	private volatile OutputStreamFactory outputStreamFactory;
	private final Map<String, MongoDatabaseProvider> mongoDatabaseProviders = new ConcurrentHashMap<String, MongoDatabaseProvider>();
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.osgi.UriHandlerProvider#getURIHandler()
	 */
	@Override
	public synchronized URIHandler getURIHandler() {
		if (uriHandler == null) {
			uriHandler = new MongoURIHandlerImpl(mongoDatabaseProviders, inputStreamFactory, outputStreamFactory);
		}
		return uriHandler;
	}

	/**
	 * Adds a {@link MongoDatabaseProvider} to the provider map.  
	 * @param mongoDatabaseProvider the provider to be added
	 */
//	@Reference(name="MongoDatabaseProvider", policy=ReferencePolicy.DYNAMIC, cardinality=ReferenceCardinality.AT_LEAST_ONE, unbind="removeMongoDatabaseProvider")
	public void addMongoDatabaseProvider(MongoDatabaseProvider mongoDatabaseProvider) {
		mongoDatabaseProviders.put(mongoDatabaseProvider.getURI(), mongoDatabaseProvider);
	}

	/**
	 * Removes a {@link MongoDatabaseProvider} from the map 
	 * @param mongoDatabaseProvider the provider to be removed
	 */
	public void removeMongoDatabaseProvider(MongoDatabaseProvider mongoDatabaseProvider) {
		mongoDatabaseProviders.remove(mongoDatabaseProvider.getURI());
	}

	/**
	 * Sets an {@link InputStreamFactory} to handle input streams
	 * @param inputStreamFactory the factory to set
	 */
//	@Reference(name="InputStreamFactory", cardinality=ReferenceCardinality.MANDATORY, policy=ReferencePolicy.STATIC)
	public void setInputStreamFactory(InputStreamFactory inputStreamFactory) {
		this.inputStreamFactory = inputStreamFactory;
	}

	/**
	 * Sets an {@link OutputStreamFactory} to handle output streams
	 * @param outputStreamFactory the factory to set
	 */
//	@Reference(name="OutputStreamFactory", cardinality=ReferenceCardinality.MANDATORY, policy=ReferencePolicy.STATIC)
	public void setOutputStreamFactory(OutputStreamFactory outputStreamFactory) {
		this.outputStreamFactory = outputStreamFactory;
	}

}
