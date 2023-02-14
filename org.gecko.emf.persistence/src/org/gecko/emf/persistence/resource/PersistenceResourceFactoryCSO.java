/**
 * Copyright (c) 2012 - 2023 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.resource;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.gecko.emf.persistence.engine.PersistenceEngine;
import org.osgi.service.component.ComponentServiceObjects;

/**
 * Persistence {@link ResourceFactoryImpl} that is registered to the Gecko EMF Framework
 * @author Mark Hoffmann
 * @since 10.02.2023
 * @deprecated use {@link PersistenceResourceFactory} instead
 */
public abstract class PersistenceResourceFactoryCSO extends ResourceFactoryImpl implements Consumer<PersistenceResource> {

	private final static Logger LOGGER = Logger.getLogger(PersistenceResourceFactoryCSO.class.getName());
	private final List<ComponentServiceObjects<PersistenceEngine>> engineCompontents = new LinkedList<>();
	private final Map<PersistenceResource, ComponentServiceObjects<PersistenceEngine>> resourceEngineMap = new ConcurrentHashMap<>();
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl#createResource(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public Resource createResource(URI uri) {
		if (uri == null) {
			LOGGER.log(Level.SEVERE, ()-> "Cannot create resource with null URI");
			return null;
		}
		PersistenceResource resource = doCreateResource(uri);
		if (resource != null) {
			return resource;
		}
		LOGGER.log(Level.SEVERE, ()-> String.format("Cannot create persistence resource for URI '%s'. Returning default ResourceImpl.", uri.toString()));
		return super.createResource(uri);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.util.function.Consumer#accept(java.lang.Object)
	 */
	@Override
	public void accept(PersistenceResource resource) {
		ComponentServiceObjects<PersistenceEngine> cso = resourceEngineMap.remove(resource);
		if (cso == null) {
			LOGGER.log(Level.FINE, ()-> String.format("No persistence engine found to be disposed for resource '%s'", resource.getURI().toString()));
		} else {
			cso.ungetService(resource.getEngine());
		}
		resource.getContents().clear();
		ResourceSet rs = resource.getResourceSet();
		if (rs != null) {
			rs.getResources().remove(resource);
		}
	}
	
	public void addEngine(ComponentServiceObjects<PersistenceEngine> engine) {
		engineCompontents.add(engine);
	}
	
	public void removeEngine(ComponentServiceObjects<PersistenceEngine> engine) {
		engineCompontents.remove(engine);
	}
	
	/**
	 * Checks, if the {@link ComponentServiceObjects} of the engine can handle the given URI and returns <code>true</code>, if so, otherwise <code>false</code>
	 * @param engine the {@link ComponentServiceObjects} for the {@link PersistenceEngine}
	 * @param uri the resource URI to check
	 * @return <code>true</code>, if this URI can be handled by this {@link ComponentServiceObjects}
	 */
	public abstract boolean canHandle(ComponentServiceObjects<PersistenceEngine> engine, URI uri);

	protected PersistenceResource doCreateResource(URI uri) {
		Optional<ComponentServiceObjects<PersistenceEngine>> engineFactoryOpt = createEngineFactory(uri);
		if (engineFactoryOpt.isEmpty()) {
		}
		Optional<PersistenceEngine> engineOpt = engineFactoryOpt.map(this::createEngineInstance);
		if (engineOpt.isEmpty()) {
			LOGGER.log(Level.SEVERE, ()-> String.format("No persistence engine can be created. Seems that the engine service has gone for URI '%s'", uri.toString()));
			return null;
		}
		PersistenceResourceImpl resource = new PersistenceResourceImpl(this, engineOpt.get(), uri);
		resourceEngineMap.put(resource, engineFactoryOpt.get());
		return resource;
	}
	
	/**
	 * Create the component service objects for creating {@link PersistenceEngine} instances. This may return an empty {@link Optional}, if no uri matches a {@link ComponentServiceObjects} 
	 * @param uri the URI to return the {@link ComponentServiceObjects} for a given URI
	 * @return {@link Optional} for {@link ComponentServiceObjects} for {@link PersistenceEngine} or an empty {@link Optional} 
	 */
	private Optional<ComponentServiceObjects<PersistenceEngine>> createEngineFactory(URI uri) {
		synchronized (engineCompontents) {
			return engineCompontents.stream().filter(e->canHandle(e, uri)).findFirst();
		}
	}
	
	/**
	 * Creates an {@link PersistenceEngine} instance out of the {@link ComponentServiceObjects}. May return <code>null</code>, if the {@link ComponentServiceObjects} based service is already gone
	 * @param engineCSO the {@link ComponentServiceObjects} to create the instance
	 * @return the {@link PersistenceEngine} or <code>null</code>, if the service is already gone
	 */
	private PersistenceEngine createEngineInstance(ComponentServiceObjects<PersistenceEngine> engineCSO) {
		PersistenceEngine engine = engineCSO.getService();
		if (engine == null) {
			LOGGER.log(Level.SEVERE, ()-> String.format("No persistence engine can be created. Seems that the engine factory has gone"));
		}
		return engine;
	}
}
