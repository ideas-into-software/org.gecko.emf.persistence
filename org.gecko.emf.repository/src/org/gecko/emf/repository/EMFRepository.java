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
package org.gecko.emf.repository;

import org.eclipse.emf.ecore.EObject;

/**
 * The base interface for a EMF persistence repositories
 * @author Mark Hoffmann
 * @since 12.04.2015
 */
public interface EMFRepository extends AutoCloseable {

	public static final String PROP_ID = "repo_id";
	public static final String PROP_BASE_URI = "base_uri";
	public static final String PROP_CONTENT_TYPE = "contentType";
	public static final String PROP_DEFAULT_LOAD_OPTIONS = "loadOptions";

	/**
	 * Returns the id of the resource provider
	 * @return the id of the resource provider
	 */
	public String getId();
	
	/**
	 * Returns the {@link EMFRepositoryHelper} with additional helper methods
	 * @return the {@link EMFRepositoryHelper}
	 */
	public EMFRepositoryHelper getHelper();

	/**
	 * Reloads an object
	 * @param object the {@link EObject} to reload
	 */
	public void reload(EObject object);

	/**
	 * Deletes an object
	 * @param object the {@link EObject} to reload
	 */
	public void delete(EObject object);

	/**
	 * Returns <code>true</code>, if the repository was already disposed
	 * @returns <code>true</code>, if the repository was already disposed
	 */
	public boolean isDisposed();

	/**
	 * Cleans up all resources
	 */
	public void dispose();
	
	/**
	 * An adapter method to adapt the repository for e.g. query builders
	 * @param adapter the type to adapt to
	 * @return the instance of the adapted type or <code>null</code>
	 */
	public Object getAdapter(Class<?> adapter);

}
