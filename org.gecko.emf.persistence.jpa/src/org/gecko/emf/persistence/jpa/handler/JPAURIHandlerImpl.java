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
 *     Bryan Hunt - initial API and implementation
 *     Ed Merks - initial API and implementation
 */
package org.gecko.emf.persistence.jpa.handler;

import static org.gecko.emf.persistence.jpa.JPAPersistenceConstants.SCHEMA;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.gecko.emf.persistence.api.PersistenceConstants;
import org.gecko.emf.persistence.emf.PersistenceURIHandlerImpl;
import org.gecko.emf.persistence.engine.InputStreamFactory;
import org.gecko.emf.persistence.engine.OutputStreamFactory;
import org.gecko.persistence.datasource.DataSourceFactoryHolder;
import org.osgi.util.promise.Promise;
import org.osgi.util.promise.PromiseFactory;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

/**
 * This EMF URI handler interfaces belongs to JPA. This URI handler can handle URIs with the "epjpa"
 * scheme. The URI path must have exactly 3 segments and be of the form /database/table/{id}
 * where id is optional the first time the EMF object is saved. When building queries, do not
 * specify an id, but make sure path has 3 segments by placing a "/" after the collection.
 * 
 * Note that if the id is not specified when the object is first saved, DB will assign the id
 * and the URI of the EMF Resource will be modified to include the id in the URI. Examples of valid
 * URIs:
 * 
 * epjpa://puName/database/table/
 * epjpa://puName/database/table/4d0a3e259095b5b334a59df0
 * 
 * where puName is the perisistence unit name, which is equinvalent to the persistene.name.
 * 
 * This class is intended to be used with the IResourceSetFactory service. If you are not using the
 * factory service, you will have to supply instances of IMongoLocator, IIntputStreamFActory, and
 * IOutputStreamFactory.
 * 
 * @author Mark Hoffmann
 * 
 */
public class JPAURIHandlerImpl extends PersistenceURIHandlerImpl<Promise<EntityManagerFactory>> {
	
	private final PromiseFactory pf;
	private Map<String, EMFHolder> entityManagerFactoryHolders;
	/**
	 * Creates a new instance.
	 * @param inputStreamFactory an instance of the input stream factory service
	 * @param outputStreamFactory an instance of the output stream factory service
	 * @param promiseFactory the promise factory
	 */
	public JPAURIHandlerImpl(Map<String, EMFHolder> emfs, Map<String, Object> properties, InputStreamFactory<Promise<EntityManagerFactory>> inputStreamFactory, OutputStreamFactory<Promise<EntityManagerFactory>> outputStreamFactory, PromiseFactory promiseFactory) {
		super(properties, inputStreamFactory, outputStreamFactory);
		this.pf = promiseFactory;
		this.entityManagerFactoryHolders = emfs;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.PersistenceURIHandlerImpl#getSchema()
	 */
	@Override
	public String getSchema() {
		return SCHEMA;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.PersistenceURIHandlerImpl#canHandle(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public boolean canHandle(URI uri) {
		String name = uri.host();
		return super.canHandle(uri) && entityManagerFactoryHolders.containsKey(name);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.PersistenceURIHandlerImpl#getConnection(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	public Promise<EntityManagerFactory> getConnection(URI uri, Map<?, ?> options) {
		String name = uri.host();
		EMFHolder holder = entityManagerFactoryHolders.get(name);
		if (holder != null) {
			return pf.submit(holder::getEntityManagerFactory);
		} else {
			return pf.failed(new PersistenceException(String.format("Cannot find EntityManagerFactory for name '%s'", name)));
		}
	}

	protected String getTable(URI uri, Map<?, ?> options) {
		return uri.segment(1);
	}

	protected String getDatabase(DataSourceFactoryHolder holder, URI uri, Map<?, ?> options) {
		if (holder != null) {
			String database = (String) holder.getProperties().get(PersistenceConstants.PROPERTY_DATABASE_NAME);
			if (database != null) {
				return database;
			}
		}
		if (options != null) {
			String database = (String) options.get(PersistenceConstants.PROPERTY_DATABASE_NAME);
			if (database != null) {
				return database;
			}
		}
		return uri.segment(0);
	}
	
}
