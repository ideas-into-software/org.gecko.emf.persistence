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

import static java.util.Objects.isNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.engine.PersistenceEngine;
import org.gecko.emf.persistence.engine.PersistenceEngineFactory;
import org.gecko.emf.persistence.resource.PersistenceResource.ActionType;

/**
 * Persistence {@link ResourceFactoryImpl} that is registered to the Gecko EMF Framework
 * @author Mark Hoffmann
 * @since 10.02.2023
 */
public abstract class PersistenceResourceFactory extends ResourceFactoryImpl implements Consumer<PersistenceResource> {

	private final static Logger LOGGER = Logger.getLogger(PersistenceResourceFactory.class.getName());
	private final List<PersistenceEngineFactory> engineFactories = new LinkedList<>();
	private final Map<Object, Object> properties = new HashMap<>();
	
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
		Optional<PersistenceEngineFactory> engineFactoryOpt = getEngineFactory(resource.getURI());
		if (engineFactoryOpt.isEmpty()) {
			LOGGER.log(Level.FINE, ()-> String.format("No persistence engine found to be disposed for resource '%s'", resource.getURI().toString()));
		} else {
			engineFactoryOpt.get().disposeEngine(resource.getURI());
		}
		resource.getContents().clear();
		ResourceSet rs = resource.getResourceSet();
		if (rs != null) {
			rs.getResources().remove(resource);
		}
	}

	public void addEngine(PersistenceEngineFactory engineFactory) {
		engineFactories.add(engineFactory);
	}

	public void removeEngine(PersistenceEngineFactory engineFactory) {
		engineFactories.remove(engineFactory);
	}
	
	protected PersistenceResource doCreateResource(URI uri) {
		Optional<PersistenceEngineFactory> engineFactoryOpt = getEngineFactory(uri);
		if (engineFactoryOpt.isEmpty()) {
			LOGGER.log(Level.SEVERE, ()-> String.format("No persistence engine factory was found for URI '%s'", uri.toString()));
			return null;
		}
		try {
			PersistenceEngine<?, ?, ?, ?, ?> engine = engineFactoryOpt.get().createEngine(uri);
			if (isNull(engine)) {
				LOGGER.log(Level.SEVERE, ()-> String.format("No persistence engine was created for URI '%s'. This looks like an error", uri.toString()));
				return null;
			}
			PersistenceResourceImpl resource = new PersistenceResourceImpl(this, engine, uri);
			resource.updateDefaultOptions(getProperties(), ActionType.ALL);
			engine.setResource(resource);
			return resource;
		} catch (PersistenceException e) {
			LOGGER.log(Level.SEVERE, e, ()-> String.format("Error creating the Persistence Engine for URI '%s'.", uri.toString()));
			return null;
		}
	}
	
	/**
	 * Returns the properties.
	 * @return the properties
	 */
	protected Map<Object, Object> getProperties() {
		return properties;
	}

	/**
	 * Create the component service objects for creating {@link PersistenceEngine} instances. This may return an empty {@link Optional}, if no uri matches a {@link PersistenceEngineFactory} 
	 * @param uri the URI to return the {@link PersistenceEngineFactory} for a given URI
	 * @return {@link Optional} for {@link PersistenceEngineFactory} for {@link PersistenceEngine} or an empty {@link Optional} 
	 */
	private Optional<PersistenceEngineFactory> getEngineFactory(URI uri) {
		synchronized (engineFactories) {
			return engineFactories.stream().filter(f->f.canHandle(uri)).findFirst();
		}
	}

}
