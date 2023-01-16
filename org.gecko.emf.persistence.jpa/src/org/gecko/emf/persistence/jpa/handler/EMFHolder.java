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
package org.gecko.emf.persistence.jpa.handler;

import java.util.Collections;
import java.util.Map;

import jakarta.persistence.EntityManagerFactory;

/**
 * Holder fpr the {@link EntityManagerFactory} and its properties
 * @author Mark Hoffmann
 * @since 15.01.2023
 */
public class EMFHolder {
	
	final EntityManagerFactory emf;
	final Map<String, Object> properties;

	/**
	 * Creates a new instance.
	 */
	public EMFHolder(EntityManagerFactory factory, Map<String, Object> properties) {
		this.emf = factory;
		this.properties = properties != null ? properties : Collections.emptyMap();
	}

	/**
	 * Returns the {@link EntityManagerFactory}.
	 * @return the {@link EntityManagerFactory}
	 */
	public EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}

	/**
	 * Returns the properties or an empty map.
	 * @return the properties or an empty map.
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

}
