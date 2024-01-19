/**
 * Copyright (c) 2012 - 2017 Data In Motion and others.
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

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.gecko.emf.mongo.Options;
import org.gecko.emf.osgi.configurator.ResourceSetConfigurator;
import org.osgi.service.component.annotations.Component;

/**
 * Implementation of the {@link ResourceSetConfigurator} to configure the ResourceSet. 
 * Sets the {@link Options#OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY} load option as default.
 * @author Juergen Albert
 * @since 08.08.2014
 */
@Component(name="DefaultOptionConfigurator", immediate=true, service=ResourceSetConfigurator.class)
public class DefaultOptionConfigurator implements ResourceSetConfigurator{

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.osgi.ResourceSetConfigurator#configureResourceSet(org.eclipse.emf.ecore.resource.ResourceSet)
	 */
	@Override
	public void configureResourceSet(ResourceSet resourceSet) {
		resourceSet.getLoadOptions().put(Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY, Boolean.TRUE);
	}
}
