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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.gecko.emf.osgi.ResourceSetConfigurator;

/**
 * {@link ResourceSetConfigurator} just handling the Jdbc stuff
 * @author Mark Hoffmann
 * @since 17.05.2022
 */
public class JdbcResourceSetConfigurator implements ResourceSetConfigurator {
	
	private final JdbcUriHandlerProvider provider;

	/**
	 * Creates a new instance.
	 */
	public JdbcResourceSetConfigurator(JdbcUriHandlerProvider provider) {
		this.provider = provider;
	}


	@Override
	public void configureResourceSet(ResourceSet resourceSet) {
		URIConverter uriConverter = resourceSet.getURIConverter();
		EList<URIHandler> uriHandlers = uriConverter.getURIHandlers();
		uriHandlers.add(0, provider.getURIHandler());
	}

}
