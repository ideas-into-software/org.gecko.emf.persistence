/**
 * Copyright (c) 2012 - 2022 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Byan Hunt -  initial API and implementation
 *     Data In Motion - initial API and implementation
 */
package org.gecko.persistence.mongo;

import org.bson.UuidRepresentation;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

import com.mongodb.AuthenticationMechanism;
import com.mongodb.ReadConcernLevel;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterType;

@ObjectClassDefinition()
public interface MongoClientConfig {

	@AttributeDefinition(required = true, name = "ident", description = "A Identifyer for the mongo client")
	 String ident();

	@AttributeDefinition(required = true, description = " e.g. `mongodb+srv://user:<password>@<host>:<port>/<database>?retryWrites=true&w=majority`. Takes the settings from the given Connectiondefault String")
	 String connectionString();

	@AttributeDefinition(required = false, name = ".credential.password")
	default	char[] credential_password()
	{
		return null;
	}

	@AttributeDefinition(required = false, name = ".credential.source")
	default String credential_source()
	{
		return null;
	}

	// Credential Section
	@AttributeDefinition(required = false, name = ".credential.type")
	default	AuthenticationMechanism credential_type()
	{
		return null;
	}

	@AttributeDefinition(required = false, name = ".credential.username")
	default String credential_username()
	{
		return null;
	}

	// Driver Section
	@AttributeDefinition(required = false, description = "Sets the name")
	default String driverName()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Sets the platform")
	default String driverPlatform()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Sets the version - Note: You must also set a driver name if setting a driver version.")
	default String driverVersion()
	{
		return null;
	}

	// ServerSettings

	@AttributeDefinition(required = false, description = "Sets the frequency that the cluster monitor attempts to reach each server. The Mongo-default value is 10 seconds. Time in millis, if <=0 value will be ignored")
	default	Integer heartbeatFrequency()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Sets the minimum heartbeat frequency. In the event that the driver has to frequently re-check a server's availability, it will wait at least this long since the previous check to avoid wasted effort.  The Mongo-default value is 500 milliseconds. Time in millis, if <=0 value will be ignored")

	default	Long minHeartbeatFrequency()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "The logical name of the application using this MongoClient. It may be null. The application name may be used by the client to identify the application to the server, for use in server logs, slow query logs, and profile collection. The UTF-8 encoding may not exceed 128 bytes.")
	default String applicationName() {
		return null;
	}

	@AttributeDefinition(required = false, description = "localThreshold the acceptable latency difference, in milliseconds, which must be >= 0. Mongo-default 15 ms")
	default Integer localThreshold()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Sets the mode for this cluster.")
	default ClusterConnectionMode clusterConnectionMode()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Sets the required cluster type for the cluster.")
	default	ClusterType requiredClusterType()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Sets the required replica set name for the cluster.")
	default String requiredReplicaSetName()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Sets the timeout to apply when selecting a server.  If the timeout expires before a server is found to handle a request, a com.mongodb.MongoTimeoutException will be thrown.  The default value is 30 seconds. A value of 0 means that it will timeout immediately if no server is available. A negative value means to wait indefinitely.")
	default Integer serverSelectionTimeout()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "The time period between runs of the maintenance job. In millis >0. Mongo-default 60 seconds")
	default Integer maintenanceFrequency()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "The period of time to wait before running the first maintenance job on the connection pool. In millis >=0. Mongo-default 0 millis")
	default Long maintenanceInitialDelay()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "The maximum idle time of a pooled connection.  A zero value indicates no limit to the idle time.  A pooled connection that has exceeded its idle time will be closed and replaced when necessary by a new connection. In millis >=0. Mongo-default 60 millis")
	default 	Long maxConnectionIdleTime()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "The maximum time a pooled connection can live for.  A zero value indicates no limit to the life time.  A pooled connection that has exceeded its life time will be closed and replaced when necessary by a new connection. In millis >=0. Mongo-default 60 millis")
	default Long maxConnectionLifeTime()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "The maximum number of connections allowed. Those connections will be kept in the pool when idle. Once the pool is exhausted, any operation requiring a connection will block waiting for an available connection. In millis >0. Mongo-default 100")
	default 	Integer maxSize()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "The maximum time that a thread may wait for a connection to become available. Default is 2 minutes. A value of 0 means that it will not wait.  A -1 value means it will wait indefinitely. In millis >0. Mongo-default 2 x 60 x 1000 millis")
	default Long maxWaitTime()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "The minimum number of connections. Those connections will be kept in the pool when idle, and the pool will ensure that it contains at least this minimum number. In millis >=0. Mongo-default 0")
	default Integer minSize()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Sets the socket connect timeout. Mongo-default 10.000")
	default Integer connectTimeout()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Sets the socket read timeout.")
	default Integer readTimeout()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Sets the receive buffer size.")
	default Integer receiveBufferSize()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Sets the receive buffer size")
	default Integer sendBufferSize()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Sets the Protocol for SSLContext for use when SSL is enabled.")
	default String sslProtocol()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Sets the Provicer for SSLContext for use when SSL is enabled.")
	default String sslProvider()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Define whether SSL should be enabled.")
	default String sslEnabled()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Define whether invalid host names should be allowed.  Defaults to false.  Take care before setting this to true, as it makes the application susceptible to man-in-the-middle attacks.", options = {
			@Option(value = "true"), @Option(value = "false") })
	default Boolean invalidHostNameAllowed()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Sets the read concern")
	default	ReadConcernLevel readConcern()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Sets whether writes should be retried if they fail due to a network error. Starting with the 3.11.0 release, the default value is true")
	default	Boolean retryWrites()
	{
		return null;
	}

	@AttributeDefinition(required = false, description = "Sets the UUID representation to use when encoding instances of {@link java.util.UUID} and when decoding BSON binary values with subtype of 3. See {@link #getUuidRepresentation()} for recommendations on settings this value.")
	default	UuidRepresentation uuidRepresentation()
	{
		return null;
	}

}