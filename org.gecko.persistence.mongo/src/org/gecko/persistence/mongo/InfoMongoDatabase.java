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

import com.mongodb.client.MongoDatabase;

public interface InfoMongoDatabase extends MongoDatabase {

	/**
	 * An alternative name for the database, if set otherwise the database name
	 * @return
	 */
	String getAlias();

	/**
	 * The client unique identifier / the database alias
	 * @return the unique identifier that identifies client and database
	 */
	String getDatabaseUniqueIdentifyer();
}
