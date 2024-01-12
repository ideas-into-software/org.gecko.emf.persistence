/**
 * Copyright (c) 2012 - 2016 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.mongo.osgi.components;

import org.bson.types.ObjectId;
import org.gecko.mongo.osgi.MongoIdFactory;
import org.gecko.mongo.osgi.exceptions.MongoOSGiException;
import org.osgi.service.component.annotations.Component;

/**
 * Returns the {@link ObjectId} as id. This is the default implementation of mongo
 * @author Mark Hoffmann
 * @since 06.05.2016
 */
@Component(name="MongoIdFactory", immediate=true, service=MongoIdFactory.class)
public class MongoIdFactoryComponent extends AbstractComponent implements MongoIdFactory {

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.mongo.osgi.api.MongoIdFactory#getCollectionURI()
	 */
	@Override
	public String getCollectionURI() {
		return "*";
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.mongo.osgi.api.MongoIdFactory#getNextId()
	 */
	@Override
	public Object getNextId() throws MongoOSGiException {
		return new ObjectId();
	}
	
}
