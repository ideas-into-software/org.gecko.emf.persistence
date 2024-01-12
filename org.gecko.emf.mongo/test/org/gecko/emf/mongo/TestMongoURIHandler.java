/**
 * Copyright (c) 2012 - 2019 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.mongo;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMLResource.URIHandler;
import org.gecko.emf.mongo.util.MongoResourceUriHandler;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author mark
 * @since 28.09.2019
 */
public class TestMongoURIHandler {

	private static String baseUri = "mongodb://localhost/dbname/collection/";
	
	private static String resourceID = "parentId";
	private static String containmentID = "//@codeBook.0/@st dyDscr.0/@citation.0/@titlStmt.0/@titl.0";
	
	private static String normalResourceUri = baseUri + resourceID;
	private static String objectIDnotMongoIDResourceUri = baseUri + "-1?id=1";
	private static String queryResourceUri = baseUri + "-1?someQueryFilter";
	
	private static String shortRelative = "#" + containmentID;
	
//	private static String normalRelative = resourceID + "#" + containmentID;
	
	@Test
	public void testURIHandler() {
		URIHandler normalUrihandler = new MongoResourceUriHandler();
		normalUrihandler.setBaseURI(URI.createURI(normalResourceUri));
		URIHandler objectIDnotMongoIDResourceUrihandler = new MongoResourceUriHandler();
		objectIDnotMongoIDResourceUrihandler.setBaseURI(URI.createURI(objectIDnotMongoIDResourceUri));
		URIHandler queryResourceUriHandler = new MongoResourceUriHandler();
		queryResourceUriHandler.setBaseURI(URI.createURI(queryResourceUri));
		
		System.out.println("Deresolving: " + normalResourceUri + shortRelative);
		URI result = normalUrihandler.deresolve(URI.createURI(normalResourceUri + shortRelative));
		
		System.out.println("Resolving URI: " + result.toString());
		System.out.println();
		
		URI normalResolve = normalUrihandler.resolve(result);
		URI objectIDnotMongoID = objectIDnotMongoIDResourceUrihandler.resolve(result);
		URI queryResourceUriResolve = queryResourceUriHandler.resolve(result);
		
		System.out.println("BaseURI: " + normalResourceUri + " result: " + normalResolve.toString());// Proxy can resolve
		System.out.println("BaseURI: " + objectIDnotMongoIDResourceUri + " result: " + objectIDnotMongoID.toString()); // Proxy can resolve
		System.out.println("BaseURI: " + queryResourceUri + " result: " + queryResourceUriResolve.toString());// Proxy can not resolve

		assertEquals(normalResourceUri + shortRelative, normalResolve.toString());
		assertEquals(normalResourceUri + shortRelative, objectIDnotMongoID.toString());
		assertEquals(normalResourceUri + shortRelative, queryResourceUriResolve.toString());

		
		
	}

}
