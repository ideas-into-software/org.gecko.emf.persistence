/*******************************************************************************
 * Copyright (c) 2013 Bryan Hunt.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 *******************************************************************************/

package org.gecko.mongo.osgi.metatype;

import org.gecko.mongo.osgi.MongoClientProvider;
import org.gecko.mongo.osgi.configuration.ConfigurationProperties;
import org.gecko.mongo.osgi.helper.MongoComponentHelper;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.ObjectClassDefinition;

/**
 * The meta type provider for the mongo client provider
 * @author bhunt
 */
@Component(name="MongoClientMetaTypeProvider", immediate=true, service=MetaTypeProvider.class, property="metatype.factory.pid=MongoClientProvider")
public class MongoClientMetaTypeProvider implements MetaTypeProvider
{
	/* 
	 * (non-Javadoc)
	 * @see org.osgi.service.metatype.MetaTypeProvider#getLocales()
	 */
	@Override
	public String[] getLocales() {
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.osgi.service.metatype.MetaTypeProvider#getObjectClassDefinition(java.lang.String, java.lang.String)
	 */
	@Override
	public ObjectClassDefinition getObjectClassDefinition(String id, String locale) {
		AttributeDefinitionImpl clientId = new AttributeDefinitionImpl(MongoClientProvider.PROP_CLIENT_ID, "ID", AttributeDefinition.STRING) {
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.mongo.osgi.metatype.AttributeDefinitionImpl#validate(java.lang.String)
			 */
			@Override
			public String validate(String value) {
				return MongoComponentHelper.validateProperty(value, "mongo client id");
			}
		};

		clientId.setDescription("The unique identifier for the client.");

		AttributeDefinitionImpl uri = new AttributeDefinitionImpl(MongoClientProvider.PROP_URI, "URI", AttributeDefinition.STRING) {
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.mongo.osgi.metatype.AttributeDefinitionImpl#validate(java.lang.String)
			 */
			@Override
			public String validate(String value) {
				return MongoComponentHelper.validateURI(value);
			}
		};

		uri.setDescription("The URI of the MongoDB server of the form 'mongodb://host[:port]'.  Separate URIs with a comma (CSV) for a replica set.");

		AttributeDefinitionImpl description = new AttributeDefinitionImpl(MongoClientProvider.PROP_DESCRIPTION, "Description", AttributeDefinition.STRING);
		description.setDescription("The description for Mongo instances created with these options. This is used in various places like logging.");

		AttributeDefinitionImpl connectionsPerHost = new IntegerAttributeDefinitionImpl(MongoClientProvider.PROP_CONNECTIONS_PER_HOST, "Connections Per Host", 1);
		connectionsPerHost.setDefaultValue(new String[] { "100" });
		connectionsPerHost
				.setDescription("The maximum number of connections allowed per host for this Mongo instance. Those connections will be kept in a pool when idle. Once the pool is exhausted, any operation requiring a connection will block waiting for an available connection. Default is 100.");

		AttributeDefinitionImpl maxWaitTime = new IntegerAttributeDefinitionImpl(MongoClientProvider.PROP_MAX_WAIT_TIME, "Max Wait Time", 0);
		maxWaitTime.setDefaultValue(new String[] { "120000" });
		maxWaitTime.setDescription("The maximum wait time in ms that a thread may wait for a connection to become available. Default is 120,000.");

		AttributeDefinitionImpl connectTimeout = new IntegerAttributeDefinitionImpl(MongoClientProvider.PROP_CONNECT_TIMEOUT, "Connect Timeout", 0);
		connectTimeout.setDefaultValue(new String[] { "0" });
		connectTimeout
				.setDescription("The connection timeout in milliseconds. It is used solely when establishing a new connection Socket.connect(java.net.SocketAddress, int) Default is 0 and means no timeout.");

		AttributeDefinitionImpl socketTimeout = new IntegerAttributeDefinitionImpl(MongoClientProvider.PROP_SOCKET_TIMEOUT, "Socket Timeout", 0);
		socketTimeout.setDefaultValue(new String[] { "0" });
		socketTimeout.setDescription("The socket timeout in milliseconds It is used for I/O socket read and write operations Socket.setSoTimeout(int) Default is 0 and means no timeout.");

		AttributeDefinitionImpl autoConnectRetry = new AttributeDefinitionImpl(MongoClientProvider.PROP_AUTO_CONNECT_RETRY, "Auto Connect Retry", AttributeDefinition.BOOLEAN);
		autoConnectRetry.setDefaultValue(new String[] { "false" });
		autoConnectRetry
				.setDescription("If true, the driver will keep trying to connect to the same server in case that the socket cannot be established. There is maximum amount of time to keep retrying, which is 15s by default. This can be useful to avoid some exceptions being thrown when a server is down temporarily by blocking the operations. It also can be useful to smooth the transition to a new master (so that a new master is elected within the retry time). Note that when using this flag: - for a replica set, the driver will trying to connect to the old master for that time, instead of failing over to the new one right away - this does not prevent exception from being thrown in read/write operations on the socket, which must be handled by application Even if this flag is false, the driver already has mechanisms to automatically recreate broken connections and retry the read operations. Default is false.");

		AttributeDefinitionImpl maxAutoConnectRetryTime = new LongAttributeDefinitionImpl(MongoClientProvider.PROP_MAX_AUTO_CONNECT_RETRY_TIME, "Max Auto Connect Retry Time", 0);
		maxAutoConnectRetryTime.setDefaultValue(new String[] { "0" });
		maxAutoConnectRetryTime
				.setDescription("The maximum amount of time in MS to spend retrying to open connection to the same server. Default is 0, which means to use the default 15s if autoConnectRetry is on.");

		AttributeDefinitionImpl continueOnInsertError = new AttributeDefinitionImpl(MongoClientProvider.PROP_CONTINUE_ON_INSERT_ERROR, "Write Concern - continueOnInsertError", AttributeDefinition.BOOLEAN);
		continueOnInsertError.setDefaultValue(new String[] { "false" });
		continueOnInsertError.setDescription("If batch inserts should continue after the first error. Default is false.");

		AttributeDefinitionImpl w = new IntegerAttributeDefinitionImpl(MongoClientProvider.PROP_W, "Write Concern - w", 0);
		w.setDefaultValue(new String[] { "0" });
		w.setDescription("The 'w' value of the global WriteConcern. Default is 0.");

		AttributeDefinitionImpl wtimeout = new IntegerAttributeDefinitionImpl(MongoClientProvider.PROP_WTIMEOUT, "Write Concern - wtimeout", 0);
		wtimeout.setDefaultValue(new String[] { "0" });
		wtimeout.setDescription("The 'wtimeout' value of the global WriteConcern. Default is 0.");

		AttributeDefinitionImpl journal = new AttributeDefinitionImpl(MongoClientProvider.PROP_JOURNAL, "Write Concern - j", AttributeDefinition.BOOLEAN);
		journal.setDefaultValue(new String[] { "false" });
		journal.setDescription("The 'journal' value of the global WriteConcern. Default is false.");
		
		AttributeDefinitionImpl credentials = new AttributeDefinitionImpl(MongoClientProvider.PROP_CREDENTIALS, "Mongo Credentials", AttributeDefinition.STRING) {
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.mongo.osgi.metatype.AttributeDefinitionImpl#validate(java.lang.String)
			 */
			@Override
			public String validate(String value) {
				return MongoComponentHelper.doValidateCredentials(value);
			}
		};
		credentials.setDefaultValue(new String[0]);
		credentials.setDescription("The mongo client credentials");

		ObjectClassDefinitionImpl ocd = new ObjectClassDefinitionImpl(ConfigurationProperties.CLIENT_PID, "MongoDB Client", "MongoDB Client Configuration");
		ocd.addAttribute(clientId);
		ocd.addAttribute(uri);
		ocd.addAttribute(description);
		ocd.addAttribute(connectionsPerHost);
		ocd.addAttribute(maxWaitTime);
		ocd.addAttribute(connectTimeout);
		ocd.addAttribute(socketTimeout);
		ocd.addAttribute(autoConnectRetry);
		ocd.addAttribute(maxAutoConnectRetryTime);
		ocd.addAttribute(continueOnInsertError);
		ocd.addAttribute(w);
		ocd.addAttribute(wtimeout);
		ocd.addAttribute(journal);
		ocd.addAttribute(credentials);

		return ocd;
	}
}
