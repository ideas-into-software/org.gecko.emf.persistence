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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.Countable;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.api.QueryEngine;
import org.gecko.emf.persistence.codec.EClassProvider;
import org.gecko.emf.persistence.context.QueryContext;
import org.gecko.emf.persistence.context.ResultContext;
import org.gecko.emf.persistence.jpa.mapper.JPAMapper;
import org.gecko.emf.persistence.mapping.InputContentHandler;
import org.gecko.emf.persistence.streams.PersistenceInputStream;
import org.osgi.util.promise.Promise;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;

/**
 * Input stream implementation that handles loading of {@link Resource}
 * @author Mark Hoffmann
 */
public class JPAInputStream extends PersistenceInputStream<EntityManagerFactory, EntityManagerFactory, Query, Query, EntityManager, JPAMapper> implements URIConverter.Loadable, Countable, EClassProvider {


	public JPAInputStream(ConverterService converterService, QueryEngine<Query, EntityManager>  queryEngine, Promise<EntityManagerFactory> connection, List<InputContentHandler<Query, JPAMapper>> contentHandler, URI uri, Map<?, ?> options, Map<Object, Object> response) throws PersistenceException {
		super(converterService, queryEngine, connection, contentHandler, uri, options, response);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.input.PersistenceInputStream#createMapper(org.gecko.emf.persistence.context.ResultContext, org.gecko.emf.persistence.input.PersistenceInputStream)
	 */
	@Override
	protected JPAMapper createMapper(ResultContext<Query, JPAMapper> inputContext) {
		// TODO Auto-generated method stub
		return new JPAMapper() {
			
			@Override
			public void initialize() throws PersistenceException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void close() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Stream<EObject> getStream() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Collection<EObject> getCollection() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.input.PersistenceInputStream#executeQuery(org.gecko.emf.persistence.context.QueryContext)
	 */
	@Override
	protected Query executeQuery(QueryContext<EntityManagerFactory, Query, JPAMapper> queryCtx) {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.input.PersistenceInputStream#executeCount(org.gecko.emf.persistence.context.QueryContext)
	 */
	@Override
	protected long executeCount(QueryContext<EntityManagerFactory, Query, JPAMapper> context) throws PersistenceException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.streams.PersistenceInputStream#isProjectionOnly(java.lang.String)
	 */
	@Override
	protected boolean isProjectionOnly(String query) {
		return false;
	}

}
