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
package org.gecko.emf.persistence.config;

/**
 * Constant interface
 * @author Juergen Albert
 * @since 27.07.2017
 */
public interface PersistenceConfiguratorConstants {

	public static enum RepositoryType{
		SINGLETON,
		PROTOTYPE
	}
	
	public static enum AuthenticationSource {
		NONE,
		DRIVER,
		DATABASE,
		CONNECTION
	}
	
	/* Comma separated list of INSTANCE / Peristence config names */ 
	public static final String PROP_INSTANCES = "instances";
	/* Property name to define property elements */ 
	public static final String PROP_PROPERTIES = "prop";
	/* Comma separated list of connection URI's */ 
	public static final String PROP_CONNECTION_URIS = "connectionUris";
	/* Comma separated list of database names*/ 
	public static final String PROP_DATABASES = "databases";
	/* Indicator for authentication source, DEFAULT is DRIVER*/ 
	public static final String PROP_AUTH_SOURCE= "authSource";
	/* Username property*/ 
	public static final String PROP_USER = "user";
	/* Password property*/ 
	public static final String PROP_PASSWORD = "password";
	public static final String PROP_REPOSITORY_TYPE = "repoType";
	
	public static final String REPO_RESOURCE_SET_FIELD_NAME = "ResourceSetFactory";
	public static final String REPO_RESOURCE_SET_FIELD_TARGET = "ResourceSetFactory.target";
	public static final String REPO_RESOURCE_SET_FILTER = "(&(emf.configurator.name=mongo)(emf.configurator.name=%s))";
	
	public static final String SINGLETON_REPOSITORY_CONFIGURATION_NAME = "MongoRepository";
	public static final String PROTOTYPE_REPOSITORY_CONFIGURATION_NAME = "PrototypeMongoRepository";
	public static final String EMF_PERSISTENCE_CONFIGURATION_NAME = "EMFPersistenceConfigurator";
	
}
