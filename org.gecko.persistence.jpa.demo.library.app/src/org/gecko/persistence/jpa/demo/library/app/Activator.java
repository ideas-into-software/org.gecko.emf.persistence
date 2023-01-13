/**
 * Copyright (c) 2012 - 2023 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.persistence.jpa.demo.library.app;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jpa.EntityManagerFactoryBuilder;

import jakarta.persistence.EntityManagerFactory;

/**
 * 
 * @author mark
 * @since 13.01.2023
 */
@Component
public class Activator {
	@Reference(target = "(" + EntityManagerFactoryBuilder.JPA_UNIT_NAME + "=Library)")
    private EntityManagerFactory emf;
    
    @Activate
    public void start(BundleContext context) throws Exception {
        System.out.println("Gemini JPA Library Sample started");
        new LibraryClient().run(emf);
    }
}
