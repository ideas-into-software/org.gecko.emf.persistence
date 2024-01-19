/**
 * Copyright (c) 2014 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.repository.mongo.configurator;

import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.gecko.emf.osgi.configurator.ResourceFactoryConfigurator;
import org.osgi.service.component.annotations.Component;

/**
 * Resource factory configuration for MongoDB.
 * @author Mark Hoffmann
 * @since 12.04.2015
 */
@Component(name="MongoProtocolConfigurator", immediate=true, service=ResourceFactoryConfigurator.class)
public class MongoProtocolConfigurator implements ResourceFactoryConfigurator {

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.osgi.ResourceFactoryConfigurator#configureResourceFactory(org.eclipse.emf.ecore.resource.Resource.Factory.Registry)
	 */
	@Override
	public void configureResourceFactory(Registry registry) {
		registry.getProtocolToFactoryMap().put("mongodb", new XMIResourceFactoryImpl());
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.osgi.ResourceFactoryConfigurator#unconfigureResourceFactory(org.eclipse.emf.ecore.resource.Resource.Factory.Registry)
	 */
	@Override
	public void unconfigureResourceFactory(Registry registry) {
		registry.getProtocolToFactoryMap().remove("mongodb");
	}

}
