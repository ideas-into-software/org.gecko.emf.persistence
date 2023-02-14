///**
// * Copyright (c) 2012 - 2022 Data In Motion and others.
// * All rights reserved. 
// * 
// * This program and the accompanying materials are made available under the terms of the 
// * Eclipse Public License v2.0 which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v20.html
// * 
// * Contributors:
// *     Data In Motion - initial API and implementation
// */
//package org.gecko.emf.persistence.resource;
//
//import java.util.Map;
//
//import org.eclipse.emf.common.util.EList;
//import org.eclipse.emf.ecore.resource.ResourceSet;
//import org.eclipse.emf.ecore.resource.URIConverter;
//import org.eclipse.emf.ecore.resource.URIHandler;
//import org.gecko.emf.osgi.ResourceSetConfigurator;
//import org.osgi.framework.BundleContext;
//import org.osgi.service.component.annotations.Activate;
//import org.osgi.service.component.annotations.Component;
//import org.osgi.service.component.annotations.ConfigurationPolicy;
//
///**
// * {@link ResourceSetConfigurator} just handling the persistence {@link URIHandler}
// * @author Mark Hoffmann
// * @since 18.04.2018
// */
//@Component(name = "emf.persistence.resource", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
//public class PersistenceResourceConfigurator implements ResourceSetConfigurator {
//
//	@Activate
//	public void activate(Map<String, Object> properties, BundleContext ctx) {
//	}
//	
//	/* 
//	 * (non-Javadoc)
//	 * @see org.gecko.emf.osgi.ResourceSetConfigurator#configureResourceSet(org.eclipse.emf.ecore.resource.ResourceSet)
//	 */
//	@Override
//	public void configureResourceSet(ResourceSet resourceSet) {
//		resourceSet.getResourceFactoryRegistry().getProtocolToFactoryMap()
//		URIConverter uriConverter = resourceSet.getURIConverter();
//		EList<URIHandler> uriHandlers = uriConverter.getURIHandlers();
//		uriHandlers.add(0, persistenceHandler);
//	}
//	
//	/**
//	 * Returns the peristenceHandler.
//	 * @return the peristenceHandler
//	 */
//	public DelegatingPersistenceURIHandler getPersistenceHandler() {
//		return persistenceHandler;
//	}
//
//}
