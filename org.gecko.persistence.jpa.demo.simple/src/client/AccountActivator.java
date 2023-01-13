/*******************************************************************************
 * Copyright (c) 2010 Oracle.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution. 
 * The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at 
 *     http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 * Contributors:
 *     mkeith - Gemini JPA sample 
 ******************************************************************************/
package client;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jpa.EntityManagerFactoryBuilder;

import jakarta.persistence.EntityManagerFactory;

/**
 * Gemini JPA sample activator class
 * 
 * @author mkeith
 */
//@Requirement(namespace = "osgi.extender", name="osgi.jpa", filter = "(&(version>=1.1)(!(version>=3.1)))")
@Component
public class AccountActivator {

    @Reference(target = "(" + EntityManagerFactoryBuilder.JPA_UNIT_NAME + "=Accounts)")
    private EntityManagerFactory emf;
    
    @Activate
    public void start(BundleContext context) throws Exception {
        System.out.println("Gemini JPA Basic Sample started");
        new AccountClient().run(emf);
    }

}