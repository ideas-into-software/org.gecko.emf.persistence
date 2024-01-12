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

package org.gecko.mongo.osgi;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;

/**
 * This OSGi service provides access to a configured MongoDB driver. The MongoDB driver
 * is initialized using the configured service properties.
 * 
 * @author bhunt
 * 
 */
public interface MongoClientProvider
{
	// --- MongoOptions properties ---------------------------------------------------------

	/**
	 * The service property key for: The description for Mongo instances created with these options.
	 * This is used in various places like logging.
	 */
	String PROP_DESCRIPTION = "description";

	/**
	 * The service property key for: The maximum number of connections allowed per host for this Mongo
	 * instance. Those connections will be kept in a pool when idle. Once the pool is exhausted, any
	 * operation requiring a connection will block waiting for an available connection. Default is
	 * 100.
	 */
	String PROP_CONNECTIONS_PER_HOST = "connectionsPerHost";

	/**
	 * The service property key for: The maximum wait time in ms that a thread may wait for a
	 * connection to become available. Default is 120,000.
	 */
	String PROP_MAX_WAIT_TIME = "maxWaitTime";

	/**
	 * The service property key for: The connection timeout in milliseconds. It is used solely when
	 * establishing a new connection Socket.connect(java.net.SocketAddress, int) Default is 0 and
	 * means no timeout.
	 */
	String PROP_CONNECT_TIMEOUT = "connectTimeout";

	/**
	 * The service property key for: The socket timeout in milliseconds It is used for I/O socket read
	 * and write operations Socket.setSoTimeout(int) Default is 0 and means no timeout.
	 */
	String PROP_SOCKET_TIMEOUT = "socketTimeout";

	/**
	 * The service property key for: This flag controls the socket keep alive feature that keeps a
	 * connection alive through firewalls Socket.setKeepAlive(boolean) Default is false.
	 * @deprecated Due to {@link MongoClientOptions#isSocketKeepAlive()} This will not be set anymore
	 */
	String PROP_SOCKET_KEEP_ALIVE = "socketKeepAlive";

	/**
	 * The service property key for: If true, the driver will keep trying to connect to the same
	 * server in case that the socket cannot be established. There is maximum amount of time to keep
	 * retrying, which is 15s by default. This can be useful to avoid some exceptions being thrown
	 * when a server is down temporarily by blocking the operations. It also can be useful to smooth
	 * the transition to a new master (so that a new master is elected within the retry time). Note
	 * that when using this flag: - for a replica set, the driver will trying to connect to the old
	 * master for that time, instead of failing over to the new one right away - this does not prevent
	 * exception from being thrown in read/write operations on the socket, which must be handled by
	 * application Even if this flag is false, the driver already has mechanisms to automatically
	 * recreate broken connections and retry the read operations. Default is false.
	 */
	String PROP_AUTO_CONNECT_RETRY = "autoConnectRetry";

	/**
	 * The service property key for: The maximum amount of time in MS to spend retrying to open
	 * connection to the same server. Default is 0, which means to use the default 15s if
	 * autoConnectRetry is on.
	 */
	String PROP_MAX_AUTO_CONNECT_RETRY_TIME = "maxAutoConnectRetryTime";

	/**
	 * The service property key for: If batch inserts should continue after the first error. Default
	 * is false.
	 */
	String PROP_CONTINUE_ON_INSERT_ERROR = "continueOnInsertError";

	/**
	 * The service property key for: The 'w' value of the global WriteConcern. Default is 0.
	 */
	String PROP_W = "w";

	/**
	 * The service property key for: The 'wtimeout' value of the global WriteConcern. Default is 0.
	 */
	String PROP_WTIMEOUT = "wtimeout";

	/**
	 * The service property key for: The 'journal' value of the global WriteConcern. Default is false.
	 */
	String PROP_JOURNAL = "journal";

	// --- Provider service specific properties ---------------------------------------------

	/**
	 * The service property key for the client URI. The value may be a single URI for one MongoDB
	 * server, or a CSV of URIs for a MongoDB replica set. The value must have the form:
	 * mongodb://host[:port] [,mongodb://host[:port]]
	 */
	String PROP_URI = "uri";

	/**
	 * The service property key for the client id. The value must be unique to an OSGi application
	 * instance.
	 */
	String PROP_CLIENT_ID = "client_id";

	/**
	 * The service property key for the MongoDB user when configuring the client provider. Used for
	 * authentication (optional). Multiple values can be set using CSV. The credential string has the following format
	 * '<user>:<password>@<database>'
	 */
	String PROP_CREDENTIALS = ".credentials";

	// --------------------------------------------------------------------------------------

	/**
	 * 
	 * @return the MongoDB client driver configured by the service properties
	 */
	MongoClient getMongoClient();

	/**
	 * Returns the list of all URI's configured for this client
	 * @return list of URIs configured on the client. A single URI will be returned for a single
	 *         MongoDB. Multiple URIs will be returned for a replica set.
	 */
	String[] getURIs();

	/**
	 * Returns the id of this client
	 * @return the unique client id configured by the service properties
	 */
	String getClientId();
}
