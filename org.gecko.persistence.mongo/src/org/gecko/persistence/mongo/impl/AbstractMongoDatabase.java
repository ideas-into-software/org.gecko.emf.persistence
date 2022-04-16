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
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.ClientSession;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.CreateViewOptions;

public abstract class AbstractMongoDatabase implements com.mongodb.client.MongoDatabase {

	abstract MongoDatabase delegate();

	public String getName() {
		return delegate().getName();
	}

	public CodecRegistry getCodecRegistry() {
		return delegate().getCodecRegistry();
	}

	public ReadPreference getReadPreference() {
		return delegate().getReadPreference();
	}

	public WriteConcern getWriteConcern() {
		return delegate().getWriteConcern();
	}

	public ReadConcern getReadConcern() {
		return delegate().getReadConcern();
	}

	public MongoDatabase withCodecRegistry(CodecRegistry codecRegistry) {
		return delegate().withCodecRegistry(codecRegistry);
	}

	public MongoDatabase withReadPreference(ReadPreference readPreference) {
		return delegate().withReadPreference(readPreference);
	}

	public MongoDatabase withWriteConcern(WriteConcern writeConcern) {
		return delegate().withWriteConcern(writeConcern);
	}

	public MongoDatabase withReadConcern(ReadConcern readConcern) {
		return delegate().withReadConcern(readConcern);
	}

	public MongoCollection<Document> getCollection(String collectionName) {
		return delegate().getCollection(collectionName);
	}

	public <TDocument> MongoCollection<TDocument> getCollection(String collectionName, Class<TDocument> documentClass) {
		return delegate().getCollection(collectionName, documentClass);
	}

	public Document runCommand(Bson command) {
		return delegate().runCommand(command);
	}

	public Document runCommand(Bson command, ReadPreference readPreference) {
		return delegate().runCommand(command, readPreference);
	}

	public <TResult> TResult runCommand(Bson command, Class<TResult> resultClass) {
		return delegate().runCommand(command, resultClass);
	}

	public <TResult> TResult runCommand(Bson command, ReadPreference readPreference, Class<TResult> resultClass) {
		return delegate().runCommand(command, readPreference, resultClass);
	}

	public Document runCommand(ClientSession clientSession, Bson command) {
		return delegate().runCommand(clientSession, command);
	}

	public Document runCommand(ClientSession clientSession, Bson command, ReadPreference readPreference) {
		return delegate().runCommand(clientSession, command, readPreference);
	}

	public <TResult> TResult runCommand(ClientSession clientSession, Bson command, Class<TResult> resultClass) {
		return delegate().runCommand(clientSession, command, resultClass);
	}

	public <TResult> TResult runCommand(ClientSession clientSession, Bson command, ReadPreference readPreference,
			Class<TResult> resultClass) {
		return delegate().runCommand(clientSession, command, readPreference, resultClass);
	}

	public void drop() {
		delegate().drop();
	}

	public void drop(ClientSession clientSession) {
		delegate().drop(clientSession);
	}

	public MongoIterable<String> listCollectionNames() {
		return delegate().listCollectionNames();
	}

	public ListCollectionsIterable<Document> listCollections() {
		return delegate().listCollections();
	}

	public <TResult> ListCollectionsIterable<TResult> listCollections(Class<TResult> resultClass) {
		return delegate().listCollections(resultClass);
	}

	public MongoIterable<String> listCollectionNames(ClientSession clientSession) {
		return delegate().listCollectionNames(clientSession);
	}

	public ListCollectionsIterable<Document> listCollections(ClientSession clientSession) {
		return delegate().listCollections(clientSession);
	}

	public <TResult> ListCollectionsIterable<TResult> listCollections(ClientSession clientSession,
			Class<TResult> resultClass) {
		return delegate().listCollections(clientSession, resultClass);
	}

	public void createCollection(String collectionName) {
		delegate().createCollection(collectionName);
	}

	public void createCollection(String collectionName, CreateCollectionOptions createCollectionOptions) {
		delegate().createCollection(collectionName, createCollectionOptions);
	}

	public void createCollection(ClientSession clientSession, String collectionName) {
		delegate().createCollection(clientSession, collectionName);
	}

	public void createCollection(ClientSession clientSession, String collectionName,
			CreateCollectionOptions createCollectionOptions) {
		delegate().createCollection(clientSession, collectionName, createCollectionOptions);
	}

	public void createView(String viewName, String viewOn, List<? extends Bson> pipeline) {
		delegate().createView(viewName, viewOn, pipeline);
	}

	public void createView(String viewName, String viewOn, List<? extends Bson> pipeline,
			CreateViewOptions createViewOptions) {
		delegate().createView(viewName, viewOn, pipeline, createViewOptions);
	}

	public void createView(ClientSession clientSession, String viewName, String viewOn, List<? extends Bson> pipeline) {
		delegate().createView(clientSession, viewName, viewOn, pipeline);
	}

	public void createView(ClientSession clientSession, String viewName, String viewOn, List<? extends Bson> pipeline,
			CreateViewOptions createViewOptions) {
		delegate().createView(clientSession, viewName, viewOn, pipeline, createViewOptions);
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

	public AggregateIterable<Document> aggregate(List<? extends Bson> pipeline) {
		return delegate().aggregate(pipeline);
	}

	public <TResult> AggregateIterable<TResult> aggregate(List<? extends Bson> pipeline, Class<TResult> resultClass) {
		return delegate().aggregate(pipeline, resultClass);
	}

	public AggregateIterable<Document> aggregate(ClientSession clientSession, List<? extends Bson> pipeline) {
		return delegate().aggregate(clientSession, pipeline);
	}

	public <TResult> AggregateIterable<TResult> aggregate(ClientSession clientSession, List<? extends Bson> pipeline,
			Class<TResult> resultClass) {
		return delegate().aggregate(clientSession, pipeline, resultClass);
	}
}
