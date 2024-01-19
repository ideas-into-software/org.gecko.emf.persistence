/**
 * Copyright (c) 2012 - 2020 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.repository.mongo.tests;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Abstract basic test for a default setting
 * @author mark
 * @since 07.03.2020
 */
public abstract class EMFMongoIT{
	
	protected MongoClient client;
	protected List<MongoCollection<?>> collections = new LinkedList<>();
	protected String mongoHost = System.getProperty("mongo.host", "localhost");
	
	public void doBefore() {
		MongoClientOptions options = MongoClientOptions.builder().build();
		client = new MongoClient(mongoHost, options);
	}

	public void doAfter() {
		collections.forEach(MongoCollection::drop);
		if (client != null) {
			client.close();
		}
	}
	
	protected MongoCollection<?> getCollection(String database, String collection) {
		MongoDatabase db = client.getDatabase(database);
		assertNotNull(db);
		MongoCollection<?> c = db.getCollection(collection); 
		assertNotNull(c);
		collections.add(c);
		return c;
	}
	
//	prOotected void defaultCheck() throws IOException, InvalidSyntaxException {
//		createStaticTrackedChecker(MongoIdFactory.class).assertCreations(1, true).trackedServiceNotNull();
//		createStaticTrackedChecker(QueryEngine.class).assertCreations(1, true).trackedServiceNotNull();
//		createStaticTrackedChecker(ConverterService.class).assertCreations(1, true).trackedServiceNotNull();
//		createStaticTrackedChecker(InputStreamFactory.class).assertCreations(1, true).trackedServiceNotNull();
//		createStaticTrackedChecker(OutputStreamFactory.class).assertCreations(1, true).trackedServiceNotNull();
//	}

}
