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
 *     Bryan Hunt - initial API
 */
package org.gecko.emf.persistence.mongo.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.Document;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.gecko.emf.osgi.UriHandlerProvider;
import org.gecko.emf.persistence.engine.InputStreamFactory;
import org.gecko.emf.persistence.engine.OutputStreamFactory;
import org.gecko.persistence.mongo.InfoMongoDatabase;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * An {@link UriHandlerProvider} to handle the mongodb scheme in an URI
 * @author bhunt
 * @author Mark Hoffmann
 */
public class MongoURIHandlerProvider implements UriHandlerProvider {
	
	private volatile MongoURIHandlerImpl uriHandler;
	private volatile InputStreamFactory<MongoCollection<Document>> inputStreamFactory;
	private volatile OutputStreamFactory<MongoCollection<Document>> outputStreamFactory;
	private final Map<String,MongoDatabase> mongoDatabases = new ConcurrentHashMap<String,MongoDatabase>();


	@Override
	public synchronized URIHandler getURIHandler() {
		if (uriHandler == null) {
			uriHandler = new MongoURIHandlerImpl(mongoDatabases, inputStreamFactory, outputStreamFactory);
		}
		return uriHandler;
	}

	/**
	 * Adds a {@link MongoDatabase} to the mongoDatabase map.  
	 * @param mongoDatabase the MongoDatabase to be added
	 */
	public void addMongoDatabaseProvider(InfoMongoDatabase mongoDatabase) {
		mongoDatabases.put(mongoDatabase.getDatabaseUniqueIdentifyer(), mongoDatabase);
	}

	/**
	 * Removes a {@link MongoDatabase} from the map 
	 * @param mongoDatabase the MongoDatabase to be removed
	 */
	public void removeMongoDatabaseProvider(InfoMongoDatabase mongoDatabase) {
		mongoDatabases.remove(mongoDatabase.getDatabaseUniqueIdentifyer());
	}

	/**
	 * Sets an {@link InputStreamFactory} to handle input streams
	 * @param inputStreamFactory the factory to set
	 */
	public void setInputStreamFactory(InputStreamFactory<MongoCollection<Document>> inputStreamFactory) {
		this.inputStreamFactory = inputStreamFactory;
	}

	/**
	 * Sets an {@link OutputStreamFactory} to handle output streams
	 * @param outputStreamFactory the factory to set
	 */
	public void setOutputStreamFactory(OutputStreamFactory<MongoCollection<Document>> outputStreamFactory) {
		this.outputStreamFactory = outputStreamFactory;
	}

}
