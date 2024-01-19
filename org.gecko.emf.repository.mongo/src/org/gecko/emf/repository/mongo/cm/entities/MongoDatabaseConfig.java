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

import org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.Type;

/**
 * This Class holds all data necessary for a EMF mongo database configuration 
 * @author Juergen Albert
 * @since 27.07.2017
 */
public class MongoDatabaseConfig extends ModelObject {

	Type type = Type.SINGLETON;
	String name = null;
	String userName = null;
	String password = null;
	String userNameEnvironementVariable = null;
	String passwordEnvironementVariable = null;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		firePropertyChange("type", this.type, this.type = type);
	}
}
