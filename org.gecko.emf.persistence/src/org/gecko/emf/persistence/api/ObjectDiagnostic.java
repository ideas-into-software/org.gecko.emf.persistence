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
package org.gecko.emf.persistence.api;

import static org.gecko.emf.persistence.api.ExceptionDiagnostic.NOT_AVAILABLE;

import org.eclipse.emf.ecore.resource.Resource.Diagnostic;

/**
 * 
 * @author mark
 * @since 14.02.2023
 */
public class ObjectDiagnostic implements Diagnostic {
	
	private final String message;
	private final String location;

	/**
	 * Creates a new instance.
	 */
	public ObjectDiagnostic(String message, String location) {
		this.message = message;
		this.location = location;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.Resource.Diagnostic#getMessage()
	 */
	@Override
	public String getMessage() {
		return message != null ? message : NOT_AVAILABLE;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.Resource.Diagnostic#getLocation()
	 */
	@Override
	public String getLocation() {
		return location != null ? location : NOT_AVAILABLE;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.Resource.Diagnostic#getLine()
	 */
	@Override
	public int getLine() {
		return -1;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.Resource.Diagnostic#getColumn()
	 */
	@Override
	public int getColumn() {
		return -1;
	}

}
