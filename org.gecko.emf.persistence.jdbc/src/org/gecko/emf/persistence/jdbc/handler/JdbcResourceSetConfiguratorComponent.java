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

import java.sql.Connection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import org.gecko.emf.osgi.EMFNamespaces;
import org.gecko.emf.osgi.ResourceSetConfigurator;
import org.gecko.emf.persistence.InputStreamFactory;
import org.gecko.emf.persistence.OutputStreamFactory;
import org.gecko.emf.persistence.jdbc.JdbcPersistenceConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.util.promise.Promise;

/**
 * This implementation of the ResourceSetConfigurator service will attach all
 * currently bound URI handlers to the ResourceSet. This service is intended to
 * be used with the IResourceSetFactory service.
 * 
 * @author bhunt
 * 
 */
@Component(name = "org.gecko.persistence.jdbc", configurationPolicy = ConfigurationPolicy.REQUIRE)
public class JdbcResourceSetConfiguratorComponent {

	private ServiceRegistration<ResourceSetConfigurator> configuratorRegistration;
	private JdbcUriHandlerProvider uriHandlerProvider = new JdbcUriHandlerProvider();
	private BundleContext ctx;
	private String alias;

	/**
	 * Called on component activation
	 * 
	 * @param context the component context
	 */
	@Activate
	public void activate(BundleContext context) {
		ctx = context;
		Dictionary<String, Object> properties = getDictionary();
		configuratorRegistration = ctx.registerService(ResourceSetConfigurator.class,
				new JdbcResourceSetConfigurator(uriHandlerProvider), properties);

	}

	/**
	 * Called on component deactivation
	 */
	@Deactivate
	public void deactivate() {
		configuratorRegistration.unregister();
		configuratorRegistration = null;
	}

	/**
	 * Sets an {@link InputStreamFactory} to handle input streams
	 * 
	 * @param inputStreamFactory the factory to set
	 */
	@Reference(name = "InputStreamFactory", target = JdbcPersistenceConstants.PERSISTENCE_FILTER)
	public void setInputStreamFactory(InputStreamFactory<Promise<Connection>> inputStreamFactory) {
		uriHandlerProvider.setInputStreamFactory(inputStreamFactory);
	}

	/**
	 * Sets an {@link OutputStreamFactory} to handle output streams
	 * 
	 * @param outputStreamFactory the factory to set
	 */
	@Reference(name = "OutputStreamFactory", target = JdbcPersistenceConstants.PERSISTENCE_FILTER)
	public void setOutputStreamFactory(OutputStreamFactory<Promise<Connection>> outputStreamFactory) {
		uriHandlerProvider.setOutputStreamFactory(outputStreamFactory);
	}
	
	@Reference(name="dataSource")
	public void setDataSourceFactory(DataSourceFactory dataSourceFactory, Map<String, Object> properties) {
		String name = (String) properties.getOrDefault("name", "default");
		uriHandlerProvider.addDataSourceFactory(dataSourceFactory, name);
	}
	public void unsetDataSourceFactory(DataSourceFactory dataSourceFactory, Map<String, Object> properties) {
		String name = (String) properties.getOrDefault("name", "default");
		uriHandlerProvider.removeDataSourceFactory(dataSourceFactory, name);
		alias = name;
	}

	/**
	 * Creates a dictionary for the stored properties
	 * 
	 * @return a dictionary for the stored properties
	 */
	private Dictionary<String, Object> getDictionary() {
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put(EMFNamespaces.EMF_CONFIGURATOR_NAME, alias);
		return properties;
	}

}
