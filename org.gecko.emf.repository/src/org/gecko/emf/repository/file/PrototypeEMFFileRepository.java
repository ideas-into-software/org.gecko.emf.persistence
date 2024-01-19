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
package org.gecko.emf.repository.file;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.gecko.emf.osgi.ResourceSetFactory;
import org.gecko.emf.repository.DefaultEMFRepository;
import org.gecko.emf.repository.EMFRepository;
import org.gecko.emf.repository.helper.RepositoryHelper;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * EMF persistence repository using file persistence as prototype service
 * @author Juergen Albert
 */
@Component(
		name="PrototypeEMFFileRepository", 
		service=EMFRepository.class, 
		scope = ServiceScope.PROTOTYPE,
		configurationPolicy=ConfigurationPolicy.REQUIRE, 
		property= {"persistence=file"})

public class PrototypeEMFFileRepository extends DefaultEMFRepository {
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.DefaultEMFRepository#activate(java.util.Map)
	 */
	@Override
	@Activate
	public void activate(Map<String, ?> properties) {
		super.activate(properties);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.DefaultEMFRepository#deactivate()
	 */
	@Override
	@Deactivate
	public void deactivate() {
		super.deactivate();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.DefaultEMFRepository#setResourceSetFactory(org.gecko.emf.osgi.ResourceSetFactory)
	 */
	@Override
	@Reference(name="ResourceSetFactory", cardinality=ReferenceCardinality.MANDATORY, policy=ReferencePolicy.STATIC, unbind="unsetResourceSetFactory")
	public void setResourceSetFactory(ResourceSetFactory resourceSetFactory) {
		super.setResourceSetFactory(resourceSetFactory);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.DefaultEMFRepository#setIDs(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected void setIDs(EObject rootObject) {
		RepositoryHelper.setIds(rootObject);	
	}
}
