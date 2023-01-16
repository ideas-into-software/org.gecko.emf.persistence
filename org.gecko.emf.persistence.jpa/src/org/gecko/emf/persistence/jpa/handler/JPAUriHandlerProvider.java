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
package org.gecko.emf.persistence.jpa.handler;

import static org.gecko.emf.persistence.api.PersistenceConstants.PROPERTY_PERSISTENCE_NAME;
import static org.gecko.emf.persistence.jpa.JPAPersistenceConstants.RESOURCESET_CONFIG_PROP;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.eclipse.emf.ecore.resource.URIHandler;
import org.gecko.emf.osgi.UriHandlerProvider;
import org.gecko.emf.persistence.engine.InputStreamFactory;
import org.gecko.emf.persistence.engine.OutputStreamFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.promise.Promise;
import org.osgi.util.promise.PromiseFactory;

import jakarta.persistence.EntityManagerFactory;

/**
 * {@link UriHandlerProvider} that handles JPA schemas
 * @author Mark Hoffmann
 * @since 15.05.2023
 */
@Component(name = "org.gecko.persistence.epjpa", configurationPolicy = ConfigurationPolicy.REQUIRE, service = UriHandlerProvider.class, property = { RESOURCESET_CONFIG_PROP, "type=persistence"})
public class JPAUriHandlerProvider implements UriHandlerProvider {
	
	private volatile JPAURIHandlerImpl uriHandler;
	private volatile InputStreamFactory<Promise<EntityManagerFactory>> inputStreamFactory;
	private volatile OutputStreamFactory<Promise<EntityManagerFactory>> outputStreamFactory;
	private final Map<String,EMFHolder> entityManagerFactories = new ConcurrentHashMap<>();
	private final Map<String,Object> properties = new ConcurrentHashMap<>();
	private final PromiseFactory pf = new PromiseFactory(Executors.newCachedThreadPool(), Executors.newScheduledThreadPool(2));
	
	@interface JdbcUriHandlerConfig {
		static final String PREFIX_ = "persistence.jdbc.";
		String name();
		String dsType() default "Driver";
	}
	
	@Activate
	public void activate(JdbcUriHandlerConfig config, Map<String, Object> properties) {
		this.properties.put(PROPERTY_PERSISTENCE_NAME, config.name());
		this.properties.putAll(properties);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.osgi.UriHandlerProvider#getURIHandler()
	 */
	@Override
	public URIHandler getURIHandler() {
		if (uriHandler == null) {
			uriHandler = new JPAURIHandlerImpl(entityManagerFactories, properties, inputStreamFactory, outputStreamFactory, pf);
		}
		return uriHandler;
	}
	
	@Reference(name="persistence.epjpa.puName")
	public void setEntityManagerFactory(EntityManagerFactory emf, Map<String, Object> properties) {
		String name = (String) properties.getOrDefault(PROPERTY_PERSISTENCE_NAME, "default");
		entityManagerFactories.put(name, new EMFHolder(emf, properties));
	}
	public void unsetEntityManagerFactory(EntityManagerFactory dataSourceFactory, Map<String, Object> properties) {
		String name = (String) properties.getOrDefault(PROPERTY_PERSISTENCE_NAME, "default");
		entityManagerFactories.remove(name);
	}

	/**
	 * @param outputStreamFactory
	 */
	@Reference
	public void setOutputStreamFactory(OutputStreamFactory<Promise<EntityManagerFactory>> outputStreamFactory) {
		this.outputStreamFactory = outputStreamFactory;
	}

	/**
	 * @param inputStreamFactory
	 */
	@Reference
	public void setInputStreamFactory(InputStreamFactory<Promise<EntityManagerFactory>> inputStreamFactory) {
		this.inputStreamFactory = inputStreamFactory;
	}
	
}
