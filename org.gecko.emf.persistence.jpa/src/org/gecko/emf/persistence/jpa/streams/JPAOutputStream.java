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
 *     Ed Merks - initial API and implementation
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.jpa.streams;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.PrimaryKeyFactory;
import org.gecko.emf.persistence.context.QueryContext;
import org.gecko.emf.persistence.jpa.mapper.JPAMapper;
import org.gecko.emf.persistence.streams.PersistenceOutputStream;
import org.osgi.util.promise.Promise;

import jakarta.persistence.EntityManagerFactory;

/**
 * JPA output stream, that can save a {@link Resource}
 * @author Mark Hoffmann
 * @since 16.01.2023
 */
public class JPAOutputStream extends PersistenceOutputStream<EntityManagerFactory, JPAMapper> {

	public JPAOutputStream(ConverterService converterService, Promise<EntityManagerFactory> driver, URI uri, Map<String, PrimaryKeyFactory> idProviders, Map<?, ?> options, Map<Object, Object> response) {
		super(converterService, driver, uri, idProviders, options, response);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.streams.PersistenceOutputStream#createNativeId()
	 */
	@Override
	protected Object createNativeId() {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.streams.PersistenceOutputStream#getIDUriSegment(org.eclipse.emf.common.util.URI)
	 */
	@Override
	protected String getIDUriSegment(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.streams.PersistenceOutputStream#saveMultipleObjects(org.gecko.emf.persistence.context.QueryContext)
	 */
	@Override
	protected void saveMultipleObjects(QueryContext<EntityManagerFactory, JPAMapper> inputContext) {
		// TODO Auto-generated method stub

	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.streams.PersistenceOutputStream#createMapper()
	 */
	@Override
	protected JPAMapper createMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.streams.PersistenceOutputStream#saveSingleObject(org.gecko.emf.persistence.context.QueryContext)
	 */
	@Override
	protected void saveSingleObject(QueryContext<EntityManagerFactory, JPAMapper> context) {
		// TODO Auto-generated method stub

	}


}
