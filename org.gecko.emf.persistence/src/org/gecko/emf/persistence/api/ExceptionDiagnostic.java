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

import org.eclipse.emf.ecore.resource.Resource.Diagnostic;

/**
 * 
 * @author mark
 * @since 14.02.2023
 */
public class ExceptionDiagnostic implements Diagnostic {
	
	/** NOT_AVAILABLE */
	public static final String NOT_AVAILABLE = "<not available>";
	private final Throwable throwable;
	private String location;

	/**
	 * Creates a new instance.
	 */
	public ExceptionDiagnostic(Throwable throwable) {
		this(throwable, null);
	}

	public ExceptionDiagnostic(Throwable throwable, String location) {
		this.throwable = throwable;
		this.location = location;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.Resource.Diagnostic#getMessage()
	 */
	@Override
	public String getMessage() {
		return throwable == null ? NOT_AVAILABLE : throwable.getMessage();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.Resource.Diagnostic#getLocation()
	 */
	@Override
	public String getLocation() {
		return location == null ? NOT_AVAILABLE : location;
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
