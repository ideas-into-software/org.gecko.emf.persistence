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

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

/**
 * Interface for a EMF persistence repository
 * @author Mark Hoffmann
 * @since 12.04.2015
 */
public interface EMFWriteRepository extends EMFRepository {

	/**
	 * Saves the given object
	 * @param object the object to save
	 * @param contentType the corresponding content type of the model
	 */
	public void save(EObject object, String contentType);

	/**
	 * Saves the given object
	 * @param object the object to save
	 * @param contentType the corresponding content type of the model
	 * @param options the save options
	 */
	public void save(EObject object, String contentType, Map<?, ?> options);

	/**
	 * Saves the given object
	 * @param object the object to save
	 */
	public void save(EObject object);

	/**
	 * Saves the given object
	 * @param object the object to save
	 * @param options the save options
	 */
	public void save(EObject object, Map<?, ?> options);

	/**
	 * Saves the given object at the given uri
	 * @param object the object to save
	 * @param uri the uri of the storage location
	 */
	public void save(EObject object, URI uri);

	/**
	 * Saves the given object at the given uri
	 * @param object the object to save
	 * @param uri the uri of the storage location
	 * @param options the save options
	 */
	public void save(EObject object, URI uri, Map<?, ?> options);

	/**
	 * Saves the given objects
	 * @param object the object to save
	 * @param options the save options
	 */
	public void save(Collection<EObject> object, Map<?, ?> options);

	/**
	 * Saves the given objects
	 * @param objects the object to save
	 * @param options the save options
	 */
	public void save(Map<?, ?> options, EObject... objects);

	/**
	 * Saves the given objects
	 * @param objects the object to save
	 * @param options the save options
	 */
	public void save(Collection<EObject> objects);
	/**
	 * Saves the given objects
	 * @param object the object to save
	 * @param options the save options
	 */
	public void save(EObject... objects);

}
