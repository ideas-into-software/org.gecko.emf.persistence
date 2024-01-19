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
package org.gecko.emf.repository.mongo.api;

import org.gecko.emf.osgi.constants.EMFNamespaces;

/**
 * Constant interface
 * @author Juergen Albert
 * @since 27.07.2017
 */
public interface EMFMongoConfiguratorConstants {

	public static enum Type{
		SINGLETON,
		PROTOTYPE
	}
	
	public static final String MONGO_INSTANCE_PROP = "mongo.instances";
	public static final String MONGO_BASEURIS = "baseUris";
	public static final String MONGO_BASEURIS_ENV = "baseUris.env";
	public static final String MONGO_DATABASES = "databases";
	public static final String MONGO_AUTH_SOURCE_PROP = "authSource";
	public static final String MONGO_USER_PROP = "user";
	public static final String MONGO_PASSWORD_PROP = "password";
	public static final String MONGO_USER_ENV_PROP = "user.env";
	public static final String MONGO_AUTH_SOURCE_ENV_PROP = "authSource.env";
	public static final String MONGO_PASSWORD_ENV_PROP = "password.env";
	public static final String MONGO_REPOSITORY_TYPE = "repoType";
	
	public static final String REPO_RESOURCE_SET_FIELD_NAME = "ResourceSetFactory";
	public static final String REPO_RESOURCE_SET_FIELD_TARGET = "ResourceSetFactory.target";
	public static final String REPO_RESOURCE_SET_FILTER = "(&(" + EMFNamespaces.EMF_CONFIGURATOR_NAME + "=mongo)(" + EMFNamespaces.EMF_CONFIGURATOR_NAME + "=%s))";
	
	public static final String SINGLETON_REPOSITORY_CONFIGURATION_NAME = "MongoRepository";
	public static final String PROTOTYPE_REPOSITORY_CONFIGURATION_NAME = "PrototypeMongoRepository";
	public static final String EMF_MONGO_REPOSITORY_CONFIGURATOR_CONFIGURATION_NAME = "EMFMongoRepositoryConfigurator";
	
}
