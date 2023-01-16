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
package org.gecko.emf.persistence.mongo.streams;

import org.bson.types.ObjectId;
import org.gecko.emf.persistence.api.PrimaryKeyFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * Returns the {@link ObjectId} as id. This is the default implementation of mongo
 * @author Mark Hoffmann
 * @since 06.05.2016
 */
@Component(name="MongoIdFactory", immediate=true, service=PrimaryKeyFactory.class, scope = ServiceScope.SINGLETON)

public class SimpleMongoIdFactory implements PrimaryKeyFactory{

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.PrimaryKeyFactory#getTableURI()
	 */
	@Override
	public String getTableURI() {
		return "*";
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.PrimaryKeyFactory#getNextId()
	 */
	@Override
	public Object getNextId() {
		return new ObjectId();
	}

}
