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
package org.gecko.emf.persistence.engine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.URI;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.config.model.InstanceConfiguration;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Factory that creates {@link PersistenceEngine} instance and releases them.
 * For each implementation usually just one factory is needed, that can then handle multiple configurations for the engines
 * @author Mark Hoffmann
 * @since 14.02.2023
 */
@Component(name = "DefaultPersistenceEngineFactory", configurationPolicy = ConfigurationPolicy.REQUIRE)
public class CSOEngineFactory implements PersistenceEngineFactory{
	
	private static final Logger LOGGER = Logger.getLogger(CSOEngineFactory.class.getName());
	private final List<ComponentServiceObjects<PersistenceEngine<?, ?, ?, ?, ?>>> engineCompontents = new LinkedList<>();
	private final Map<String, PersistenceEngine<?, ?, ?, ?, ?>> activeEngines = new HashMap<>();
	
	@interface EngineFactoryConfig {
		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.engine.PersistenceEngineFactory#createEngine(org.eclipse.emf.common.util.URI)
	 */
	public PersistenceEngine createEngine(URI uri) throws PersistenceException {
		return null;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.engine.PersistenceEngineFactory#disposeEngine(org.eclipse.emf.common.util.URI)
	 */
	public boolean disposeEngine(URI uri) {
		return false;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.engine.PersistenceEngineFactory#canHandle(org.eclipse.emf.common.util.URI)
	 */
	public boolean canHandle(URI uri) {
		return false;
	}
	
	@Reference(name="persistenceEngine", cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
	public void addPersistenceEngine(ComponentServiceObjects<PersistenceEngine<?, ?, ?, ?, ?>> engineCSO) {
		engineCompontents.add(engineCSO);
	}
	
	public void removePersistenceEngine(ComponentServiceObjects<PersistenceEngine<?, ?, ?, ?, ?>> engineCSO) {
		engineCompontents.remove(engineCSO);
	}

	
	/**
	 * Create the component service objects for creating {@link PersistenceEngine} instances. This may return an empty {@link Optional}, if no uri matches a {@link ComponentServiceObjects} 
	 * @param uri the URI to return the {@link ComponentServiceObjects} for a given URI
	 * @return {@link Optional} for {@link ComponentServiceObjects} for {@link PersistenceEngine} or an empty {@link Optional} 
	 */
	private Optional<ComponentServiceObjects<PersistenceEngine<?, ?, ?, ?, ?>>> createEngineFactory(URI uri) {
		synchronized (engineCompontents) {
			return engineCompontents.stream().filter(e->canHandle(uri)).findFirst();
		}
	}
	
	/**
	 * Creates an {@link PersistenceEngine} instance out of the {@link ComponentServiceObjects}. May return <code>null</code>, if the {@link ComponentServiceObjects} based service is already gone
	 * @param engineCSO the {@link ComponentServiceObjects} to create the instance
	 * @return the {@link PersistenceEngine} or <code>null</code>, if the service is already gone
	 */
	private PersistenceEngine<?, ?, ?, ?, ?> createEngineInstance(ComponentServiceObjects<PersistenceEngine<?, ?, ?, ?, ?>> engineCSO) {
		PersistenceEngine<?, ?, ?, ?, ?> engine = engineCSO.getService();
		if (engine == null) {
			LOGGER.log(Level.SEVERE, ()-> String.format("No persistence engine can be created. Seems that the engine factory has gone"));
		}
		return engine;
	}
}
