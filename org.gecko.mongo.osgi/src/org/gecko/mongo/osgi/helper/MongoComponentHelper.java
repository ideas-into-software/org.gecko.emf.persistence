/**
 * Copyright (c) 2012 - 2016 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.mongo.osgi.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.gecko.mongo.osgi.exceptions.MongoConfigurationException;

import com.mongodb.MongoClientException;
import com.mongodb.MongoCredential;

/**
 * Small helper class with static methods for e.g. property checks
 * @author Mark Hoffmann
 * @since 06.05.2016
 */
public class MongoComponentHelper {
	
	/**
	 * Validates the given URI string and puts valid URI's into the uris collection
	 * @param value the URI {@link String} with CSV URI's in it
	 * @return a the collection with valid URI's or an empty collections
	 * @throws the MongoConfigurationException on errors during configuration
	 */
	public static Collection<String> doValidateURI(String value) throws MongoConfigurationException {
		if (value == null || value.isEmpty()) {
			throw new MongoConfigurationException("The MongoDB URI was not found in the configuration properties");
		}

		// The regex \s matches whitepsace. The extra \ is needed because of how it's treated in java
		// strings. The split is done on any number of whitespace chars followed by a comma followed by
		// any number of whitespace chars. What is left is the URI(s).
		List<String> uris = new LinkedList<String>();
		for (String targetURI : value.split("\\s*,\\s*")) {
			String uri = targetURI.trim();

			if (!uri.startsWith("mongodb://") || 
					uri.endsWith("/") || 
					uri.split("/").length != 3) {
				throw new MongoConfigurationException("The uri: '" + uri + "' does not have the form 'mongodb://host[:port]'");
			}
			if (uris != null) {
				uris.add(uri);
			}
		}
		return uris;
	}
	
	/**
	 * Validates the given URI string and puts valid URI's into the uris collection
	 * @param value the URI {@link String} with CSV URI's in it
	 * @param uris the collection with valid URI's as out parameter
	 * @return the error message or <code>null</code>, on no errors
	 */
	public static String validateURI(String value, Collection<String> uris) {
		try {
			Collection<String> validated = doValidateURI(value);
			if (uris != null) {
				uris.addAll(validated);
			}
			return null;
		} catch (MongoConfigurationException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * Simple validates the given uri string
	 * @param value the uri string, with options CSV
	 * @return the error messaage or <code>null</code>, on no errors
	 */
	public static String validateURI(String value) {
		return validateURI(value, null);
	}
	
	/**
	 * Validates a property for <code>null</code> and emptiness
	 * @param property the property to check
	 * @param propertyDesc the property description to be returned in the error message
	 * @return the error message or <code>null</code>
	 */
	public static String validateProperty(String property, String propertyDesc) {
		if (property == null || property.isEmpty())
			return "The property '" + propertyDesc + "' was not found in the configuration properties";

		return null;
	}
	
	/**
	 * Validates the credentials string. This String can contain CSV of credentials like 
	 * 'user:password@dbname,user2:password2@db2'
	 * @param credentialString string with credentials
	 * @return list of  {@link MongoCredential} or an empty list
	 * @throws MongoClientException
	 */
	public static List<MongoCredential> validateCredentials(String credentialString) throws MongoClientException {
		List<MongoCredential> credentials = new ArrayList<MongoCredential>();
		if (credentialString == null || 
				credentialString.isEmpty() || 
				!credentialString.contains(":") || 
				!credentialString.contains("@")) {
			throw new MongoClientException("The credential string is not of valid format");
		}
		String[] credentialStringArray = credentialString.split(",");
		for (String credential : credentialStringArray) {
			if (!credential.contains(":") || !credential.contains("@")) {
				credentials.clear();
				throw new MongoClientException("The credential string is not of valid format");
			}
			int dbIdx = credential.lastIndexOf("@");
			String database = credential.substring(dbIdx + 1);
			String[] userPassword = credential.substring(0, dbIdx).split(":");
			if (userPassword.length != 2) {
				credentials.clear();
				throw new MongoClientException("The credential string is not of valid format with only user set (note that no @ is allowed in DB name)");
			}
			String username = userPassword[0];
			String password = userPassword[1];
			MongoCredential mongoCredential = MongoCredential.createCredential(username, database, password.toCharArray());
			credentials.add(mongoCredential);
		}
		return credentials;
	}
	
	/**
	 * Validates the credentials string. This String can contain CSV of credentials like 
	 * 'user:password@dbname,user2:password2@db2'. For multiple credentials this method returns just the first one
	 * @param credentialString the string with credentials
	 * @return the {@link MongoCredential} instance or <code>null</code>
	 * @throws MongoClientException
	 */
	public static MongoCredential validateCredential(String credentialString) throws MongoClientException {
		List<MongoCredential> credentials = validateCredentials(credentialString);
		return credentials.isEmpty() ? null : credentials.get(0);
	}
	
	/**
	 * Validates the credentials string. This String can contain CSV of credentials like 
	 * 'user:password@dbname,user2:password2@db2'
	 * @param credentialString
	 * @return the error message or <code>null</code>
	 * @throws MongoClientException
	 */
	public static String doValidateCredentials(String credentialString) {
		try {
			validateCredentials(credentialString);
			return null;
		} catch (MongoClientException e) {
			return e.getMessage();
		}
	}

}
