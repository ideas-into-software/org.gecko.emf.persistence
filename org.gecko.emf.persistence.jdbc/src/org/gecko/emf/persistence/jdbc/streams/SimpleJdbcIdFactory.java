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
package org.gecko.emf.persistence.jdbc.streams;

import java.util.concurrent.atomic.AtomicLong;

import org.gecko.emf.persistence.api.PrimaryKeyFactory;
import org.gecko.emf.persistence.jdbc.JdbcPersistenceConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * Returns the {@link ObjectId} as id. This is the default implementation of mongo
 * @author Mark Hoffmann
 * @since 06.05.2016
 */
@Component(name="JdbcIdFactory", immediate=true, service=PrimaryKeyFactory.class, scope = ServiceScope.SINGLETON, property = JdbcPersistenceConstants.PERSISTENCE_FILTER_PROP)
public class SimpleJdbcIdFactory implements PrimaryKeyFactory{
	
	private final AtomicLong id = new AtomicLong();

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
		return id.addAndGet(1);
	}

}
