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

package org.gecko.mongo.osgi.components;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.gecko.mongo.osgi.MongoClientProvider;
import org.gecko.mongo.osgi.helper.MongoComponentHelper;
import org.osgi.annotation.bundle.Requirement;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

/**
 * Default component for a mongo client. This component was update to work with 
 * @author bhunt
 * @author Mark Hoffmann
 */
@Requirement(namespace="osgi.identity", name="org.mongodb.mongo-java-driver")
@Component(name="MongoClientProvider", configurationPolicy=ConfigurationPolicy.REQUIRE, immediate=true)
public class MongoClientProviderComponent extends AbstractComponent implements MongoClientProvider {
	
	private volatile String clientId;
	private volatile Collection<String> uris;
	private volatile MongoClient mongoClient;
	private volatile String credentials;

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.mongo.osgi.api.MongoClientProvider#getClientId()
	 */
	@Override
	public String getClientId() {
		return clientId;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.mongo.osgi.api.MongoClientProvider#getMongoClient()
	 */
	@Override
	public MongoClient getMongoClient() {
		return mongoClient;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.mongo.osgi.api.MongoClientProvider#getURIs()
	 */
	@Override
	public String[] getURIs() {
		return uris.toArray(new String[0]);
	}

	/**
	 * The OSGi DS activate callback
	 * @param properties the service properties
	 */
	@Activate
	public void activate(Map<String, Object> properties) {
		clientId = (String) properties.get(PROP_CLIENT_ID);
		handleIllegalConfiguration(MongoComponentHelper.validateProperty(clientId, "Mongo client id"));

		// The uriProperty is a single string containing one or more server URIs.
		// When more than one URI is specified, it denotes a replica set and the
		// URIs must be separated by a comma (CSV).
		String uriProperty = (String) properties.get(PROP_URI);
		uris = new ArrayList<String>();
		handleIllegalConfiguration(MongoComponentHelper.validateURI(uriProperty, uris));
		
		// The uriProperty is a single string containing one or more server URIs.
		// When more than one URI is specified, it denotes a replica set and the
		// URIs must be separated by a comma (CSV).
		credentials = (String) properties.get(PROP_CREDENTIALS);

		MongoClientOptions options = createMongoClientOptions(properties);
		String currentURI = null;

		try {
			if (mongoClient != null) {
				mongoClient.close();
			}
			if (uris.size() == 1) {
				currentURI = uris.iterator().next();
				ServerAddress serverAddress = createServerAddress(currentURI);
				mongoClient = createMongoClient(options, serverAddress);
			}
			else {
				ArrayList<ServerAddress> serverAddresses = new ArrayList<ServerAddress>(uris.size());

				for (String uri : uris) {
					currentURI = uri;
					serverAddresses.add(createServerAddress(currentURI));
				}

				mongoClient = createMongoClient(options, serverAddresses);
			}
		} catch (UnknownHostException e) {
			handleConfigurationException("The URI: '" + currentURI + "' has a bad hostname", e);
		} catch (URISyntaxException e) {
			handleConfigurationException("The URI: '" + currentURI + "' is not a proper URI", e);
		}
	}
	
	/**
	 * THe DS modify callback
	 * @param properties the new properties
	 */
	@Modified
	public void modify(Map<String, Object> properties) {
		activate(properties);
	}

	/**
	 * The DS deactivate callback 
	 */
	@Deactivate
	public void deactivate() {
		if (mongoClient != null) {
			mongoClient.close();
		}
	}

	/**
	 * Creates a mongo client with options and list of server address strings
	 * @param options the monfo client options
	 * @param serverAddresses {@link List} with server addresses 
	 * @return the mongo client instance
	 */
	protected MongoClient createMongoClient(MongoClientOptions options, List<ServerAddress> serverAddresses) {
		if (credentials != null) {
			MongoCredential credential = MongoComponentHelper.validateCredential(credentials);
			return new MongoClient(serverAddresses, credential, options);
		} else {
			return new MongoClient(serverAddresses, options);
		}
	}

	/**
	 * Creates a mongo client with options and list of server address strings
	 * @param options the monfo client options
	 * @param serverAddresse {@link ServerAddress} object 
	 * @return the mongo client instance
	 */
	protected MongoClient createMongoClient(MongoClientOptions options, ServerAddress serverAddress) {
		if (credentials != null) {
			MongoCredential credential = MongoComponentHelper.validateCredential(credentials);
			return new MongoClient(serverAddress, credential, options);
		} else {
			return new MongoClient(serverAddress, options);
		}
	}

	/**
	 * Creates the mongo client options
	 * @param properties the properties map
	 * @return the mongo client options instance
	 */
	private MongoClientOptions createMongoClientOptions(Map<String, Object> properties) {
		MongoClientOptions.Builder optionsBuilder = new MongoClientOptions.Builder();

		String description = (String) properties.get(PROP_DESCRIPTION);

		if (description != null)
			optionsBuilder.applicationName(description);

		Integer connectionsPerHost = (Integer) properties.get(PROP_CONNECTIONS_PER_HOST);

		if (connectionsPerHost != null)
			optionsBuilder.connectionsPerHost(connectionsPerHost);

		Integer maxWaitTime = (Integer) properties.get(PROP_MAX_WAIT_TIME);

		if (maxWaitTime != null)
			optionsBuilder.maxWaitTime(maxWaitTime);

		Integer connectTimeout = (Integer) properties.get(PROP_CONNECT_TIMEOUT);

		if (connectTimeout != null)
			optionsBuilder.connectTimeout(connectTimeout);

		Integer socketTimeout = (Integer) properties.get(PROP_SOCKET_TIMEOUT);

		if (socketTimeout != null)
			optionsBuilder.socketTimeout(socketTimeout);

		Integer w = (Integer) properties.get(PROP_W);

		if (w == null)
			w = Integer.valueOf(1);

		Integer wtimeout = (Integer) properties.get(PROP_WTIMEOUT);

		if (wtimeout == null)
			wtimeout = Integer.valueOf(0);

		Boolean journal = (Boolean) properties.get(PROP_JOURNAL);

		if (journal == null)
			journal = Boolean.FALSE;

		WriteConcern writeConcern = new WriteConcern(w, wtimeout);
		writeConcern.withJournal(journal);
		optionsBuilder.writeConcern(writeConcern);

		return optionsBuilder.build();
	}

	/**
	 * Creates a server address
	 * @param uriProperty the uri property string
	 * @return the server address instance
	 * @throws URISyntaxException
	 * @throws UnknownHostException
	 */
	private ServerAddress createServerAddress(String uriProperty) throws URISyntaxException, UnknownHostException {
		URI uri = new URI(uriProperty);
		int port = uri.getPort();
		ServerAddress serverAddress = port == -1 ? new ServerAddress(uri.getHost()) : new ServerAddress(uri.getHost(), uri.getPort());
		return serverAddress;
	}
}
