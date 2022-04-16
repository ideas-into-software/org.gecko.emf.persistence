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
package org.gecko.emf.persistence;

/**
 * A PrimaryKeyFactory provides the equivalent of an auto-increment primary key.
 * Calling getNextId() will return the next value to be used as the _id in the
 * database/collection. The current value is stored in an own database/collection to maintain
 * integrity across server restarts. 
 * 
 * There is an obvious performance penalty when using the PrimaryKeyFactory instead of letting the persistence
 * generate the _id for you.
 * @author Mark Hoffmann
 * @since 26.03.2022
 */
public interface PrimaryKeyFactory {
	
	/**
	 * The service property key for the MongoDB database reference filter.
	 */
	String PROP_DATABASE_FILTER = "PersistenceProvider.target";

	/**
	 * The service property key for configuring the database collection. Set
	 * the value of the collection property to the name of the database collection
	 * for which you want to use a sequential _id.
	 */
	String PROP_TABLE = "table";

	/**
	 * Returns the URI of the collection in the form <schema>://host[:port]/database/table
	 * or <schema>://host[:port]/database/collection
	 * @return the URI of the collection in the form <schema>://host[:port]/database/table
	 */
	String getTableURI();

	/**
	 * Returns the next id
	 * @return the next auto-increment id value to be used as the _id value
	 */
	Object getNextId();

}
