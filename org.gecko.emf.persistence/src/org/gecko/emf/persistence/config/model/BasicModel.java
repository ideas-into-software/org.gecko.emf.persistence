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
package org.gecko.emf.persistence.config.model;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Basic configuration
 * @author Mark Hoffmann
 * @since 17.02.2023
 */
public class BasicModel {

	private String name;
	private String user;
	private String password;
	protected Map<String, Object> properties = new HashMap<>();
	private BasicModel parent;
	
	public BasicModel getParent() {
		return parent == null ? null : parent;
	}
	
	void setParent(BasicModel parent) {
		this.parent = parent;
	}
	
	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param user
	 */
	public void setUsername(String user) {
		this.user = user;
	}
	
	/**
	 * Returns the user.
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param pwd
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Returns the password.
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Returns the properties.
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return Collections.unmodifiableMap(properties);
	}
	
	void addProperties(Map<String, Object> sourceMap) {
		if (sourceMap == null) {
			return;
		}
		this.properties.putAll(sourceMap);
	}
	
	/**
	 * Creates an unmodifiable map of properties with key starting with the given prefix.
	 * The keys of the final map entries are then without the prefix
	 * @param prefix the prefix for the keys, to be taken into account
	 * @param sourceMap the source properties, to check
	 * @return an unmodifiable map with the entries or en empty map
	 */
	public static Map<String, Object> createProperties(String prefix, Map<Object, Object> sourceMap) {
		requireNonNull(prefix);
		requireNonNull(sourceMap);
		Map<String, Object> sourceMapped = sourceMap.entrySet().
				stream().
				filter(e->e.getKey().toString().startsWith(prefix)).
				collect(Collectors.toMap(k-> k.getKey().toString().replace(prefix, ""), Entry::getValue));
		return Collections.unmodifiableMap(sourceMapped);
	}


}
