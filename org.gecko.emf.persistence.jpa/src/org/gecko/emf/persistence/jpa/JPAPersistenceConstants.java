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
package org.gecko.emf.persistence.jpa;

/**
 * Constants for the persistence JPA engine
 * @author Mark Hoffmann
 * @since 14.01.2023
 */
public interface JPAPersistenceConstants {
	
	public static final String PREFIX = "persistence.epjpa";
	public static final String SCHMEA = "epjpa";
	
	public static final String PERSISTENCE_FILTER = "(persistence=epjpa)";
	public static final String PERSISTENCE_FILTER_PROP = "persistence=epjpa";

}
