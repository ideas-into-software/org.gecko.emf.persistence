/**
 * Copyright (c) 2012 - 2024 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.mongo.resource;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.gecko.emf.osgi.constants.EMFNamespaces;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * 
 * @author grune
 * @since Jan 23, 2024
 * 
 * enabled = false da es eine weiter Implementierung im codec mongo_generator branch gibt
 */
@Component(enabled = false , service = Resource.Factory.class, property = { EMFNamespaces.EMF_CONFIGURATOR_NAME + "=myMongo",
		EMFNamespaces.EMF_MODEL_PROTOCOL + "=mongodb" })
public class MongoPersistenceResourceFactoryImpl extends ResourceFactoryImpl {

	@Activate
	public void activate() {
	}

	@Deactivate
	public void deactivate() {
	}

	@Override
	public Resource createResource(URI uri) {
		return new MongoPersistenceResourceImpl(uri);
	}

}
