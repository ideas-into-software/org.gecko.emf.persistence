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
package org.gecko.emf.repository.mongo.cm.entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.Type;

/**
 * This Class holds all Data necessary for a eMongo configuration 
 * @author Juergen Albert
 * @version 0.0.1
 */
public class MongoInstanceConfig extends ModelObject {

	private Type type = Type.SINGLETON;
	private String instanceName = null;
	private String mongoBaseUri = null;
	private String authDatabase = null;
	private String username = null;
	private String password = null;
	private String usernameEnvironmentVariable = null;
	private String passwordEnvironmentVariable = null;
	private String authDatabaseEnvironmentVariable = null;
	private String mongoBaseUriEnvironmentVariable = null;
	private Map<String, Object> clientProperties = new HashMap<String, Object>();
	private List<MongoDatabaseConfig> databaseConfigs = new LinkedList<MongoDatabaseConfig>();

	/**
	 * @return the instanceName
	 */
	public String getInstanceName() {
		return instanceName;
	}

	/**
	 * @param instanceName the instanceName to set
	 */
	public void setInstanceName(String instanceName) {
		firePropertyChange("instanceName", this.instanceName, this.instanceName = instanceName);
	}

	/**
	 * @return the mongoBaseUri
	 */
	public String getMongoBaseUri() {
		return mongoBaseUri;
	}

	/**
	 * @param mongoBaseUri the mongoBaseUri to set
	 */
	public void setMongoBaseUri(String mongoBaseUri) {
		firePropertyChange("mongoBaseUri", this.mongoBaseUri, this.mongoBaseUri = mongoBaseUri);
	}

	/**
	 * @return the databaseConfigs
	 */
	public List<MongoDatabaseConfig> getDatabaseConfigs() {
		return databaseConfigs;
	}

	public Map<String, Object> getClientProperties() {
		return clientProperties;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		firePropertyChange("type", this.type, this.type = type);
	}
	
	/**
	 * Returns the mongoBaseUriEnvironmentVariable.
	 * @return the mongoBaseUriEnvironmentVariable
	 */
	public String getMongoBaseUriEnvironmentVariable() {
		return mongoBaseUriEnvironmentVariable;
	}
	
	/**
	 * Sets the mongoBaseUriEnvironmentVariable.
	 * @param mongoBaseUriEnvironmentVariable the mongoBaseUriEnvironmentVariable to set
	 */
	public void setMongoBaseUriEnvironmentVariable(String mongoBaseUriEnvironmentVariable) {
		firePropertyChange("mongoBaseUriEnvironmentVariable", this.mongoBaseUriEnvironmentVariable, this.mongoBaseUriEnvironmentVariable = mongoBaseUriEnvironmentVariable);
	}

	/**
	 * Returns the authDatabase.
	 * @return the authDatabase
	 */
	public String getAuthDatabase() {
		return authDatabase;
	}

	/**
	 * Sets the authDatabase.
	 * @param authDatabase the authDatabase to set
	 */
	public void setAuthDatabase(String authDatabase) {
		firePropertyChange("authDB", this.authDatabase, this.authDatabase = authDatabase);
	}

	/**
	 * Returns the username.
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		firePropertyChange("username", this.username, this.username = username);
	}

	/**
	 * Returns the password.
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		firePropertyChange("password", this.password, this.password = password);
	}

	/**
	 * Returns the usernameEnvironementVariable.
	 * @return the usernameEnvironementVariable
	 */
	public String getUsernameEnvironmentVariable() {
		return usernameEnvironmentVariable;
	}

	/**
	 * Sets the usernameEnvironementVariable.
	 * @param usernameEnvironmentVariable the usernameEnvironementVariable to set
	 */
	public void setUsernameEnvironmentVariable(String usernameEnvironmentVariable) {
		firePropertyChange("usernameEnvironmentVariable", this.usernameEnvironmentVariable, this.usernameEnvironmentVariable = usernameEnvironmentVariable);
	}

	/**
	 * Returns the passwordEnvironementVariable.
	 * @return the passwordEnvironementVariable
	 */
	public String getPasswordEnvironmentVariable() {
		return passwordEnvironmentVariable;
	}

	/**
	 * Sets the passwordEnvironementVariable.
	 * @param passwordEnvironmentVariable the passwordEnvironementVariable to set
	 */
	public void setPasswordEnvironmentVariable(String passwordEnvironmentVariable) {
		firePropertyChange("passwordEnvironmentVariable", this.passwordEnvironmentVariable, this.passwordEnvironmentVariable = passwordEnvironmentVariable);
	}

	/**
	 * Returns the authDatabaseEnvironmentVariable.
	 * @return the authDatabaseEnvironmentVariable
	 */
	public String getAuthDatabaseEnvironmentVariable() {
		return authDatabaseEnvironmentVariable;
	}

	/**
	 * Sets the authDatabaseEnvironmentVariable.
	 * @param authDatabaseEnvironmentVariable the authDatabaseEnvironmentVariable to set
	 */
	public void setAuthDatabaseEnvironmentVariable(String authDatabaseEnvironmentVariable) {
		this.authDatabaseEnvironmentVariable = authDatabaseEnvironmentVariable;
	}
	
}
