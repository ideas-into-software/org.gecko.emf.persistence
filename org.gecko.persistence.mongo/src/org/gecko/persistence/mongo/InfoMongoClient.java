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

import com.mongodb.client.MongoClient;

public interface InfoMongoClient extends MongoClient {

	/**
	 *  A long term unique identifyer of the Connection that will not change. E,g, if you use the atlas-cloud it can be the unique host from the uri (without port and protocol)
	 * 
	 * @return ClientUniqueIdentifyer
	 */
	String getClientUniqueIdentifyer();
}
