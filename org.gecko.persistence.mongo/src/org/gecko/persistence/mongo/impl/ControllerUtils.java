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
package org.gecko.persistence.mongo.impl;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.List;

import javax.net.ssl.SSLContext;

import org.gecko.persistence.mongo.MongoClientConfig;

import com.mongodb.ConnectionString;
import com.mongodb.MongoCredential;
import com.mongodb.MongoDriverInformation;
import com.mongodb.event.ClusterListener;
import com.mongodb.event.ConnectionPoolListener;
import com.mongodb.event.ServerListener;
import com.mongodb.event.ServerMonitorListener;
import com.mongodb.selector.ServerSelector;

public class ControllerUtils {
	
	static void applyClusterSettings(MongoClientConfig mongoConfig, List<ClusterListener> clusterListeners,
			ServerSelector serverSelector, com.mongodb.connection.ClusterSettings.Builder cs) {
		if (clusterListeners != null) {
			clusterListeners.forEach(c -> cs.addClusterListener(c));
		}

		cs.applyConnectionString(new ConnectionString(mongoConfig.connectionString()));

		if (mongoConfig.localThreshold() != null) {
			cs.localThreshold(0, MILLISECONDS);
		}

		if (mongoConfig.clusterConnectionMode() != null) {
			cs.mode(mongoConfig.clusterConnectionMode());
		}

		if (mongoConfig.requiredClusterType() != null) {
			cs.requiredClusterType(mongoConfig.requiredClusterType());
		}

		if (mongoConfig.requiredReplicaSetName() != null) {
			cs.requiredReplicaSetName(mongoConfig.requiredReplicaSetName());
		}

		if (mongoConfig.serverSelectionTimeout() != null) {
			cs.serverSelectionTimeout(mongoConfig.serverSelectionTimeout(), MILLISECONDS);
		}

		if (serverSelector != null) {
			cs.serverSelector(serverSelector);
		}

	}

	static void applyConnectionPoolSettings(MongoClientConfig mongoConfig,
			List<ConnectionPoolListener> connectionPoolListeners,
			com.mongodb.connection.ConnectionPoolSettings.Builder cps) {
		cps.applyConnectionString(new ConnectionString(mongoConfig.connectionString()));

		if (connectionPoolListeners != null) {
			connectionPoolListeners.forEach(c -> cps.addConnectionPoolListener(c));
		}

		if (mongoConfig.maintenanceFrequency() != null) {
			cps.maintenanceFrequency(mongoConfig.maintenanceFrequency(), MILLISECONDS);
		}

		if (mongoConfig.maintenanceInitialDelay() != null) {
			cps.maintenanceInitialDelay(mongoConfig.maintenanceInitialDelay(), MILLISECONDS);
		}

		if (mongoConfig.maxConnectionIdleTime() != null) {
			cps.maxConnectionIdleTime(mongoConfig.maxConnectionIdleTime(), MILLISECONDS);
		}

		if (mongoConfig.maxConnectionLifeTime() != null) {

			cps.maxConnectionLifeTime(mongoConfig.maxConnectionLifeTime(), MILLISECONDS);
		}

		if (mongoConfig.maxSize() != null) {
			cps.maxSize(mongoConfig.maxSize());

		}
		if (mongoConfig.maxWaitTime() != null) {
			cps.maxWaitTime(mongoConfig.maxWaitTime(), MILLISECONDS);
		}
		if (mongoConfig.minSize() != null) {
			cps.minSize(mongoConfig.minSize());
		}
	}

	static void applyServerSettings(MongoClientConfig mongoConfig, List<ServerListener> serverListeners,
			List<ServerMonitorListener> serverMonitorListeners, com.mongodb.connection.ServerSettings.Builder ss) {

		ss.applyConnectionString(new ConnectionString(mongoConfig.connectionString()));

		if (serverListeners != null) {
			serverListeners.forEach(sl -> ss.addServerListener(sl));
		}
		if (serverMonitorListeners != null) {
			serverMonitorListeners.forEach(sml -> ss.addServerMonitorListener(sml));
		}
		if (mongoConfig.heartbeatFrequency() != null) {
			ss.heartbeatFrequency(mongoConfig.heartbeatFrequency(), MILLISECONDS);
		}
		if (mongoConfig.minHeartbeatFrequency() != null) {
			ss.minHeartbeatFrequency(mongoConfig.minHeartbeatFrequency(), MILLISECONDS);
		}
	}

	static void applySSL(MongoClientConfig mongoConfig, com.mongodb.connection.SslSettings.Builder ssl) {
		if (mongoConfig.sslEnabled() != null) {

			ssl.enabled(Boolean.getBoolean(mongoConfig.sslEnabled()));
		}

		if (mongoConfig.invalidHostNameAllowed() != null) {

			ssl.invalidHostNameAllowed(mongoConfig.invalidHostNameAllowed());
		}

		if (mongoConfig.sslProtocol() != null) {

			try {

				if (mongoConfig.sslProvider() == null) {
					ssl.context(SSLContext.getInstance(mongoConfig.sslProtocol()));
				} else {
					ssl.context(SSLContext.getInstance(mongoConfig.sslProtocol(), mongoConfig.sslProvider()));

				}
			} catch (Exception e) {
				throw new RuntimeException("Could not create ssl context", e);
			}
		}
	}

	static void applySocketSettings(MongoClientConfig mongoConfig, com.mongodb.connection.SocketSettings.Builder ss) {
		ss.applyConnectionString(new ConnectionString(mongoConfig.connectionString()));

		if (mongoConfig.connectTimeout() != null) {
			ss.connectTimeout(mongoConfig.connectTimeout(), MILLISECONDS);
		}

		if (mongoConfig.readTimeout() != null) {
			ss.readTimeout(mongoConfig.readTimeout(), MILLISECONDS);
		}

		if (mongoConfig.receiveBufferSize() != null) {
			ss.receiveBufferSize(mongoConfig.receiveBufferSize());
		}

		if (mongoConfig.sendBufferSize() != null) {
			ss.sendBufferSize(mongoConfig.sendBufferSize());
		}
	}

	static MongoDriverInformation toDriverInformation(MongoClientConfig config) {

		if (config.driverName() == null && config.driverPlatform() == null && config.driverVersion() == null) {
			return null;
		}

		MongoDriverInformation.Builder driverBuilder = MongoDriverInformation.builder();

		if (config.driverName() != null) {
			driverBuilder.driverName(config.driverName());
		}

		if (config.driverPlatform() != null) {
			driverBuilder.driverPlatform(config.driverPlatform());
		}

		if (config.driverVersion() != null) {
			driverBuilder.driverVersion(config.driverVersion());
		}

		return driverBuilder.build();
	}

	static MongoCredential toCredential(MongoClientConfig config) {

		String username = config.credential_username() == null ? "" : config.credential_username();
		String source = config.credential_source() == null ? "" : config.credential_source();
		char[] pw = config.credential_password() == null ? new char[] {} : config.credential_password();
		if (config.credential_type() == null&&config.credential_username() == null&&config.credential_password() == null) {
			
		}
		if (config.credential_type() == null) {
			return null;
		}
		switch (config.credential_type()) {
		case GSSAPI:
			return MongoCredential.createGSSAPICredential(username);
		case MONGODB_X509:
			return MongoCredential.createMongoX509Credential(username);
		case SCRAM_SHA_1:
			return MongoCredential.createScramSha1Credential(username, source, pw);
		case SCRAM_SHA_256:
			return MongoCredential.createScramSha256Credential(username, source, pw);
		case PLAIN:
			return MongoCredential.createPlainCredential(username, source, pw);
		default:
			return MongoCredential.createCredential(username, source, pw);
		}
	}

}
