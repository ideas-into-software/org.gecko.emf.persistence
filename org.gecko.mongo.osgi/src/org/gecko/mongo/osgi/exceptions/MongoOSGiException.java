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
 * A standard exception for handling errors in Mongo OSGi
 * @author Mark Hoffmann
 * @since 06.05.2016
 */
public class MongoOSGiException extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	public MongoOSGiException(String message) {
		super(message);
	}
	
	public MongoOSGiException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
