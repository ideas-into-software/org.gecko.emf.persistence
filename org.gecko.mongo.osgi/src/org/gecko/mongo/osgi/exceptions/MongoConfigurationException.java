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
package org.gecko.mongo.osgi.exceptions;

/**
 * An exception for handling configuration errors in Mongo OSGi
 * @author Mark Hoffmann
 * @since 06.05.2016
 */
public class MongoConfigurationException extends MongoOSGiException {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	public MongoConfigurationException(String message) {
		super(message);
	}
	
	public MongoConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
