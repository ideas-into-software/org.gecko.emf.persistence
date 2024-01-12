/*******************************************************************************
 * Copyright (c) 2013 Bryan Hunt.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 *    Data In Motion Consulting GmbH
 *******************************************************************************/
package org.gecko.mongo.osgi.components;

import java.util.Map;

import org.gecko.mongo.osgi.MongoClientProvider;
import org.gecko.mongo.osgi.MongoDatabaseProvider;
import org.gecko.mongo.osgi.helper.MongoComponentHelper;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.mongodb.client.MongoDatabase;

/**
 * Component for a mongo database provider.
 * @author bhunt
 * @author Mark Hoffmann
 */
@Component(name="MongoDatabaseProvider", immediate=true, configurationPolicy=ConfigurationPolicy.REQUIRE)
public class MongoDatabaseProviderComponent extends AbstractComponent implements MongoDatabaseProvider
{
	private volatile MongoClientProvider mongoClientProvider;
	private volatile String alias;
	private volatile String databaseName;
	private String uri;

	/**
	 * The OSGi DS activate callback
	 * @param properties the service properties
	 */
	@Activate
	public void activate(Map<String, Object> properties) {
		alias = (String) properties.get(PROP_ALIAS);
		handleIllegalConfiguration(MongoComponentHelper.validateProperty(alias, "database alias"));

		databaseName = (String) properties.get(PROP_DATABASE);
		handleIllegalConfiguration(MongoComponentHelper.validateProperty(databaseName, "database name"));

		uri = mongoClientProvider.getURIs()[0] + "/" + databaseName;
	}
	
	/**
	 * The OSGi DS modify callback
	 * @param properties the service properties
	 */
	@Modified
	public void modify(Map<String, Object> properties) {
		activate(properties);
	}
	
	/**
	 * The OSGi DS de-activate callback
	 */
	@Deactivate
	public void deactivate() {
		alias = null;
		databaseName = null;
		uri = null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.mongo.osgi.api.MongoDatabaseProvider#getURI()
	 */
	@Override
	public String getURI() {
		return uri;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.mongo.osgi.api.MongoDatabaseProvider#getDatabase()
	 */
	@Override
	public MongoDatabase getDatabase() {
		if (mongoClientProvider == null || databaseName == null) {
			return null;
		}
		MongoDatabase db = mongoClientProvider.getMongoClient().getDatabase(databaseName);
		return db;
	}

	/**
	 * Adds a mongo client provider
	 * @param mongoClientProvider
	 */
	@Reference(name="MongoClientProvider", policy=ReferencePolicy.STATIC, cardinality=ReferenceCardinality.MANDATORY)
	public void addMongoClientProvider(MongoClientProvider mongoClientProvider) {
		this.mongoClientProvider = mongoClientProvider;
	}
	
	/**
	 * Removes a mongo client provider
	 * @param mongoClientProvider
	 */
	public void removeMongoClientProvider(MongoClientProvider mongoClientProvider) {
		this.mongoClientProvider = null;
	}
	
}
