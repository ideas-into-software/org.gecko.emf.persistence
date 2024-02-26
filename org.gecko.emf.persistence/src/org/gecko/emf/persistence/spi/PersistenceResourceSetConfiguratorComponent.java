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

import org.gecko.emf.osgi.UriHandlerProvider;
import org.gecko.emf.osgi.configurator.ResourceSetConfigurator;
import org.gecko.emf.osgi.constants.EMFNamespaces;
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
	@Reference(name = "handlerProvider", policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE, target = "(type=persistence)")
	public void addProvider(UriHandlerProvider provider, Map<String, Object> map) {
		configurator.getPersistenceHandler().addProvider(provider);
		String configuratorName = getConfiguratorName(map);
		if (configuratorName != null) {
			providerName.add(configuratorName);
			updateRegistrationProperties();
		}
	}

	/**
	 * Removes a {@link UriHandlerProvider} from the map
	 * 
	 * @param provider the provider to be removed
	 */
	public void removeProvider(UriHandlerProvider provider, Map<String, Object> map) {
		String configuratorName = getConfiguratorName(map);
		if (configuratorName != null) {
			providerName.remove(configuratorName);
			updateRegistrationProperties();
		}
		configurator.getPersistenceHandler().removeProvider(provider);
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
	
	/**
	 * Returns the configurator name, depending on the service properties.
	 * @param properties the service properties
	 * @return the configurator name or <code>null</code>
	 */
	private String getConfiguratorName(Map<String, Object> properties) {
		String configuratorName = (String) properties.get(EMFNamespaces.EMF_CONFIGURATOR_NAME);
		String name = (String) properties.get("name");
		if (configuratorName != null) {
			if (name != null) {
				configuratorName += "." + name;
			}
		} else {
			configuratorName = name;
		}
		return configuratorName;
	}

}
