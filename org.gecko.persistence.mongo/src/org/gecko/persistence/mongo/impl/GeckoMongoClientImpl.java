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

import org.gecko.persistence.mongo.InfoMongoClient;
import org.gecko.persistence.mongo.MongoClientConfig;

import com.mongodb.client.MongoClient;

public class GeckoMongoClientImpl extends AbstractMongoClient implements MongoClient, InfoMongoClient {
	private MongoClient delegate;
	private MongoClientConfig mongoConfig;

	private GeckoMongoClientImpl() {

	}

	public GeckoMongoClientImpl(MongoClient mongoClient, MongoClientConfig mongoConfig) {
		this();
		this.delegate = mongoClient;
		this.mongoConfig = mongoConfig;

	}

	@Override
	MongoClient delegate() {

		return delegate;
	}

	@Override
	public String getClientUniqueIdentifyer() {
		return mongoConfig.ident();
	}

}
