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
package org.gecko.emf.persistence.spi;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.gecko.emf.osgi.EMFNamespaces;
import org.gecko.emf.osgi.ResourceSetConfigurator;
import org.gecko.emf.osgi.UriHandlerProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * This implementation of a component will register a ResourceSetConfigurator service.
 * This component will attach and detach all {@link UriHandlerProvider} to the configurator,
 * that are registered as whiteboard. 
 * @author Mark Hoffmann
 * @since 29.03.2022
 */
@Component(name = "PersistenceResourceSetConfiguratorComponent", immediate = true)
public class PersistenceResourceSetConfiguratorComponent {

	private final PersistenceResourceSetConfigurator configurator = new PersistenceResourceSetConfigurator();
	private final Set<String> providerName = new HashSet<>();
	private ServiceRegistration<ResourceSetConfigurator> configuratorRegistration;
	private BundleContext ctx;

	/**
	 * Called on component activation
	 * 
	 * @param context the component context
	 */
	@Activate
	public void activate(BundleContext context) {
		ctx = context;
		Dictionary<String, Object> properties = getDictionary();
		configuratorRegistration = ctx.registerService(ResourceSetConfigurator.class, configurator, properties);

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
	 * Adds a {@link UriHandlerProvider} to the provider map.
	 * 
	 * @param provider the provider to be added
	 */
	@Reference(name = "handlerProvider", policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
	public void addProvider(UriHandlerProvider provider, Map<String, Object> map) {
		configurator.getPeristenceHandler().addProvider(provider);
		String name = (String) map.get(EMFNamespaces.EMF_CONFIGURATOR_NAME);
		if (name != null) {
			providerName.add(name);
			updateRegistrationProperties();
		}
	}

	/**
	 * Removes a {@link UriHandlerProvider} from the map
	 * 
	 * @param provider the provider to be removed
	 */
	public void removeProvider(UriHandlerProvider provider, Map<String, Object> map) {
		String name = (String) map.get(EMFNamespaces.EMF_CONFIGURATOR_NAME);
		if (name != null) {
			providerName.remove(name);
			updateRegistrationProperties();
		}
		configurator.getPeristenceHandler().removeProvider(provider);
	}

	/**
	 * Updates the service registration properties
	 */
	private void updateRegistrationProperties() {
		if (configuratorRegistration != null) {
			configuratorRegistration.setProperties(getDictionary());
		}
	}

	/**
	 * Creates a dictionary for the stored properties
	 * 
	 * @return a dictionary for the stored properties
	 */
	private Dictionary<String, Object> getDictionary() {
		Dictionary<String, Object> properties = new Hashtable<>();
		String[] ids = providerName.toArray(new String[providerName.size()]);
		properties.put(EMFNamespaces.EMF_CONFIGURATOR_NAME, ids);
		return properties;
	}

}
