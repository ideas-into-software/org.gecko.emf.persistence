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

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.gecko.mongo.osgi.MongoClientProvider;
import org.gecko.mongo.osgi.MongoDatabaseProvider;
import org.gecko.mongo.osgi.configuration.ConfigurationProperties;
import org.gecko.mongo.osgi.helper.MongoComponentHelper;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.ObjectClassDefinition;

/**
 * Meta data provider for the mongo database provider
 * @author bhunt
 */
@Component(name="MongoDatabaseMetaTypeProvider", immediate=true, service=MetaTypeProvider.class, property="metatype.factory.pid=MongoDatabaseProvider")
public class MongoDatabaseMetaTypeProvider implements MetaTypeProvider
{
	private Set<String> mongoClientProviders = new CopyOnWriteArraySet<String>();

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
		AttributeDefinitionImpl clientId = new AttributeDefinitionImpl(MongoDatabaseProvider.PROP_CLIENT_FILTER, "Client", AttributeDefinition.STRING);
		clientId.setDescription("The MongoDB database client ID");

		String[] clients = new String[mongoClientProviders.size()];
		String[] targetFilters = new String[mongoClientProviders.size()];

		mongoClientProviders.toArray(clients);

		for (int i = 0; i < clients.length; i++)
			targetFilters[i] = "(" + MongoClientProvider.PROP_CLIENT_ID + "=" + clients[i] + ")";

		clientId.setOptionLabels(clients);
		clientId.setOptionValues(targetFilters);

		if (!mongoClientProviders.isEmpty())
			clientId.setDefaultValue(new String[] { mongoClientProviders.iterator().next() });

		AttributeDefinitionImpl alias = new AttributeDefinitionImpl(MongoDatabaseProvider.PROP_ALIAS, "Alias", AttributeDefinition.STRING) {
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.mongo.osgi.metatype.AttributeDefinitionImpl#validate(java.lang.String)
			 */
			@Override
			public String validate(String value) {
				return MongoComponentHelper.validateProperty(value, "database alias");
			}
		};

		alias.setDescription("The alias of the MongoDB database.");

		AttributeDefinitionImpl database = new AttributeDefinitionImpl(MongoDatabaseProvider.PROP_DATABASE, "Database", AttributeDefinition.STRING) {
			@Override
			public String validate(String value) {
				return MongoComponentHelper.validateProperty(value, "database name");
			}
		};
		alias.setDescription("The identifier of the MongoDB database.");
		
		AttributeDefinitionImpl identifier = new AttributeDefinitionImpl(MongoDatabaseProvider.PROP_DATABASE_IDENTIFIER, "Identitifier", AttributeDefinition.STRING) {
			@Override
			public String validate(String value) {
				return null;
			}
		};

		identifier.setDescription("The name MongoDB database.");

		ObjectClassDefinitionImpl ocd = new ObjectClassDefinitionImpl(ConfigurationProperties.DATABASE_PID, "MongoDB Database", "MongoDB Database Configuration");
		ocd.addAttribute(clientId);
		ocd.addAttribute(alias);
		ocd.addAttribute(database);
		ocd.addAttribute(identifier);

		return ocd;
	}

	/**
	 * Adds a mongo client provider as service reference
	 * @param serviceReference the service reference of a mongo client provider
	 */
	@Reference(name="MongoClientProvider", policy=ReferencePolicy.DYNAMIC, cardinality=ReferenceCardinality.MULTIPLE)
	public void addMongoClientProvider(ServiceReference<MongoClientProvider> serviceReference) {
		mongoClientProviders.add((String) serviceReference.getProperty(MongoClientProvider.PROP_CLIENT_ID));
	}

	/**
	 * Removes a mongo client provider 
	 * @param serviceReference the service reference of the client provider to be removed
	 */
	public void removeMongoClientProvider(ServiceReference<MongoClientProvider> serviceReference) {
		mongoClientProviders.remove((String) serviceReference.getProperty(MongoClientProvider.PROP_CLIENT_ID));
	}
}
