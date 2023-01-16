/**
 * Copyright (c) 2012 - 2022 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.api;

/**
 * Special exception for the EMF persistence
 * @author Mark Hoffmann
 * @since 15.11.2022
 */
public class PersistenceException extends Exception {

	/** serialVersionUID */
	private static final long serialVersionUID = 4945756658686122188L;
	
	public PersistenceException(String message) {
		super(message);
	}
	
	public PersistenceException(Throwable reason) {
		super(reason);
	}
	
	public PersistenceException(String message, Throwable reason) {
		super(message, reason);
	}

}
