/**
 * Copyright (c) 2012 - 2022 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Byan Hunt -  initial API and implementation
 *     Data In Motion - initial API and implementation
 */
package org.gecko.persistence.mongo;

public interface MongoConstants {

	public static final String PID_MONGO_CLIENT = "persistence.mongo.client";
	public static final String PID_MONGO_DATABASE = "persistence.mongo.database";

	public static final String CLIENT_PROP_CLIENT_IDENT = "ident";
	public static final String CLIENT_PROP_CLIENT_STATUS = "status";
	public static final String CLIENT_PROP_CONNECTION_STRING = "connectionString";

	
	public static final String DB_PROP_DATABASE_NAME = "name";
	public static final String DB_PROP_DATABASE_ALIAS = "alias";
	public static final String DB_PROP_DATABASE_MUST_EXIST = "must.exist";
	
	public static final String TARGET_MONGOCLIENT = "mongoclient.target";

	public static String TARGET_FILTER_CLIENT_BY_IDENT= "(" + MongoConstants.CLIENT_PROP_CLIENT_IDENT + "=%s)";
	public static String TARGET_DO_NOT_BIND = "(&(must.not.bind.by.default=*)(!(must.not.bind.by.default=*)))";
	public static String TARGET_MONGO_DATABASE_CONNECTED_UNBINDABLE = "("+MongoConstants.CLIENT_PROP_CLIENT_STATUS+"=true)";

	public static String TARGET_DEFAULT_MONGO_DATABASE_CONNECTED = "(&"+TARGET_DO_NOT_BIND+TARGET_MONGO_DATABASE_CONNECTED_UNBINDABLE+")";

	

	

	
	
}
