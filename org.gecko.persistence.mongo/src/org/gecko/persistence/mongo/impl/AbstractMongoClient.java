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

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.ClientSessionOptions;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.ClientSession;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.connection.ClusterDescription;

public abstract class AbstractMongoClient implements MongoClient {

	abstract MongoClient delegate();

	public MongoDatabase getDatabase(String databaseName) {
		return delegate().getDatabase(databaseName);
	}

	public ClientSession startSession() {
		return delegate().startSession();
	}

	public ClientSession startSession(ClientSessionOptions options) {
		return delegate().startSession(options);
	}

	public void close() {
		delegate().close();
	}

	public MongoIterable<String> listDatabaseNames() {
		return delegate().listDatabaseNames();
	}

	public MongoIterable<String> listDatabaseNames(ClientSession clientSession) {
		return delegate().listDatabaseNames(clientSession);
	}

	public ListDatabasesIterable<Document> listDatabases() {
		return delegate().listDatabases();
	}

	public ListDatabasesIterable<Document> listDatabases(ClientSession clientSession) {
		return delegate().listDatabases(clientSession);
	}

	public <TResult> ListDatabasesIterable<TResult> listDatabases(Class<TResult> resultClass) {
		return delegate().listDatabases(resultClass);
	}

	public <TResult> ListDatabasesIterable<TResult> listDatabases(ClientSession clientSession,
			Class<TResult> resultClass) {
		return delegate().listDatabases(clientSession, resultClass);
	}

	public ChangeStreamIterable<Document> watch() {
		return delegate().watch();
	}

	public <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> resultClass) {
		return delegate().watch(resultClass);
	}

	public ChangeStreamIterable<Document> watch(List<? extends Bson> pipeline) {
		return delegate().watch(pipeline);
	}

	public <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> pipeline, Class<TResult> resultClass) {
		return delegate().watch(pipeline, resultClass);
	}

	public ChangeStreamIterable<Document> watch(ClientSession clientSession) {
		return delegate().watch(clientSession);
	}

	public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, Class<TResult> resultClass) {
		return delegate().watch(clientSession, resultClass);
	}

	public ChangeStreamIterable<Document> watch(ClientSession clientSession, List<? extends Bson> pipeline) {
		return delegate().watch(clientSession, pipeline);
	}

	public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, List<? extends Bson> pipeline,
			Class<TResult> resultClass) {
		return delegate().watch(clientSession, pipeline, resultClass);
	}

	public ClusterDescription getClusterDescription() {
		return delegate().getClusterDescription();
	}
}
