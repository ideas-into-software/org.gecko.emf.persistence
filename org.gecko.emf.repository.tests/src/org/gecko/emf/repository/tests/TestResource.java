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
package org.gecko.emf.repository.tests;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.file.Path;
import java.util.UUID;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.gecko.emf.osgi.example.model.basic.Address;
import org.gecko.emf.osgi.example.model.basic.BasicFactory;
import org.gecko.emf.osgi.example.model.basic.BasicPackage;
import org.gecko.emf.persistence.helper.PersistenceHelper;
import org.gecko.emf.repository.DefaultEMFRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.platform.commons.annotation.Testable;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * Tests to reproduce the bug reported in #17
 * 
 * @author ilenia
 * @since Aug 9, 2019
 */
@Testable
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class TestResource {

	@Test
	public void testReproduceBug17(@TempDir Path tempDir) throws Exception {
		try (DefaultEMFRepository defaultEMFRepository = new DefaultEMFRepository() {

			@Override
			public ResourceSet createResourceSet() {
				ResourceSet set = new ResourceSetImpl();
				BasicFactory.eINSTANCE.createAddress();
				set.getResourceFactoryRegistry().getProtocolToFactoryMap().put("file", new XMIResourceFactoryImpl());
				return set;
			}

			@Override
			public String getBaseUri() {
				return "file:" + tempDir.toString();
			}

			@Override
			protected void setIDs(EObject rootObject) {
				PersistenceHelper.setIds(rootObject);
			}
		}) {

			Address address = BasicFactory.eINSTANCE.createAddress();
			address.setId(UUID.randomUUID().toString());

			Address add = defaultEMFRepository.getEObject(BasicPackage.Literals.ADDRESS, address.getId());
			assertNull(add);
			defaultEMFRepository.save(address);
			
			defaultEMFRepository.getHelper().detach(address);

			Address retrievedAdd = defaultEMFRepository.getEObject(BasicPackage.Literals.ADDRESS, address.getId());
			assertNotNull(retrievedAdd);
		}
	}


}
