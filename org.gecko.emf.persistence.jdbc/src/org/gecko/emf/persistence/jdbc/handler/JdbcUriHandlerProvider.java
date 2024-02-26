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
package org.gecko.emf.persistence.jdbc.handler;

import static org.gecko.emf.persistence.PersistenceConstants.PROPERTY_PERSISTENCE_NAME;
import static org.gecko.emf.persistence.jdbc.JdbcPersistenceConstants.RESOURCESET_CONFIG_PROP;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.eclipse.emf.ecore.resource.URIHandler;
import org.gecko.emf.osgi.UriHandlerProvider;
import org.gecko.emf.osgi.annotation.require.RequireEMF;
import org.gecko.emf.persistence.OutputStreamFactory;
import org.gecko.emf.persistence.input.InputStreamFactory;
import org.gecko.emf.persistence.jdbc.handler.JdbcURIHandlerImpl.DataSourceFactoryHolder;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.util.promise.Promise;
import org.osgi.util.promise.PromiseFactory;

/**
 * 
 * @author mark
 * @since 16.04.2022
 * Change Generic type of the In- and Outputstreams from DataSourceFactory to Connection
 */
@Component(name = "org.gecko.persistence.jdbc", configurationPolicy = ConfigurationPolicy.REQUIRE, service = UriHandlerProvider.class, property = { RESOURCESET_CONFIG_PROP, "type=persistence"})
@RequireEMF
@Designate(ocd = org.gecko.emf.persistence.jdbc.handler.JdbcUriHandlerProvider.JdbcUriHandlerConfig.class, factory = true)
public class JdbcUriHandlerProvider implements UriHandlerProvider {
	
	private volatile JdbcURIHandlerImpl uriHandler;
	private volatile InputStreamFactory<Promise<Connection>> inputStreamFactory;
	private volatile OutputStreamFactory<Promise<Connection>> outputStreamFactory;
	private final Map<String,DataSourceFactoryHolder> connections = new ConcurrentHashMap<>();
	private final Map<String,Object> properties = new ConcurrentHashMap<>();
	private final PromiseFactory pf = new PromiseFactory(Executors.newCachedThreadPool(), Executors.newScheduledThreadPool(2));
	
	
	@ObjectClassDefinition
	public @interface JdbcUriHandlerConfig {
		public static final String PREFIX_ = "persistence.";
		String name() default "test";
		String dsType() default "Driver";
	}
	
	@Activate
	public void activate(JdbcUriHandlerConfig config, Map<String, Object> properties) {
		System.out.println(config.name());
		System.out.println(config.dsType());
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
			uriHandler = new JdbcURIHandlerImpl(connections, properties, inputStreamFactory, outputStreamFactory, pf);
		}
		return uriHandler;
	}
	
	@Reference(name="persistence.jdbc.ds", cardinality = ReferenceCardinality.AT_LEAST_ONE, policy = ReferencePolicy.DYNAMIC, policyOption = ReferencePolicyOption.GREEDY)
	public void setDataSourceFactory(DataSourceFactory dataSourceFactory, Map<String, Object> properties) {
		String name = (String) properties.getOrDefault(PROPERTY_PERSISTENCE_NAME, "default");
		connections.put(name, new DataSourceFactoryHolder(dataSourceFactory, properties));
	}
	public void unsetDataSourceFactory(DataSourceFactory dataSourceFactory, Map<String, Object> properties) {
		String name = (String) properties.getOrDefault(PROPERTY_PERSISTENCE_NAME, "default");
		connections.remove(name);
	}

	/**
	 * @param outputStreamFactory
	 */
	@Reference
	public void setOutputStreamFactory(OutputStreamFactory<Promise<Connection>> outputStreamFactory) {
		this.outputStreamFactory = outputStreamFactory;
	}

	/**
	 * @param inputStreamFactory
	 */
	@Reference
	public void setInputStreamFactory(InputStreamFactory<Promise<Connection>> inputStreamFactory) {
		this.inputStreamFactory = inputStreamFactory;
	}
	
}
