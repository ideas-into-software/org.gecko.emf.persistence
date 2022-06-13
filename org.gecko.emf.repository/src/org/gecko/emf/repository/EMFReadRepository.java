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

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Interface for a EMF persistence repository
 * @author Mark Hoffmann
 * @since 12.04.2015
 */
public interface EMFReadRepository extends EMFRepository {

	/**
	 * Gets a resource for the given {@link URI} and loads it on demand, if the parameter is set to <code>true</code>
	 * @param uri the URI to create a resource from
	 * @param loadOnDemand set to <code>true</code> if the resource should be loaded 
	 * @return the {@link Resource} or <code>null</code> 
	 */
	public Resource getResource(URI createURI, boolean b);


	/**
	 * Returns the {@link EObject} for a given {@link URI} or <code>null</code>
	 * @param uri the object uri
	 * @return the {@link EObject} instance or <code>null</code>
	 */
	public  <T extends EObject> T getEObject(URI uri);

	/**
	 * Returns the {@link EObject} for a given {@link URI} or <code>null</code>
	 * @param uri the object uri
	 * @param options the load options
	 * @return the {@link EObject} instance or <code>null</code>
	 */
	public  <T extends EObject> T getEObject(URI uri, Map<?, ?> options);

	/**
	 * Returns the {@link EObject} of the given {@link EClass} name and id or <code>null</code>
	 * @param eClassName the {@link String} name of the {@link EObject}
	 * @param id the primary key of the object
	 * @return the {@link EObject} of the given {@link EClass} name and id or <code>null</code>
	 */
	public <T extends EObject> T getEObject(String eClassName, Object id);

	/**
	 * Returns the {@link EObject} of the given {@link EClass} name and id or <code>null</code>
	 * @param eClassName the {@link String} name of the {@link EObject}
	 * @param id the primary key of the object
	 * @param options the load options
	 * @return the {@link EObject} of the given {@link EClass} name and id or <code>null</code>
	 */
	public <T extends EObject> T getEObject(String eClassName, Object id, Map<?, ?> options);

	/**
	 * Returns the {@link EObject} of the given {@link EClass} and id or <code>null</code>
	 * @param eClass the {@link EClass} of the {@link EObject}
	 * @param id the primary key of the object
	 * @return the {@link EObject} of the given {@link EClass} name and id or <code>null</code>
	 */
	public <T extends EObject> T getEObject(EClass eClass, Object id);

	/**
	 * Returns the {@link EObject} of the given {@link EClass} and id or <code>null</code>
	 * @param eClass the {@link EClass} of the {@link EObject}
	 * @param id the primary key of the object
	 * @param options the load options
	 * @return the {@link EObject} of the given {@link EClass} name and id or <code>null</code>
	 */
	public <T extends EObject> T getEObject(EClass eClass, Object id, Map<?, ?> options);

	/**
	 * Returns a list of all {@link EObject} of the given {@link EClass} or an empty {@link List}
	 * @param eClass the {@link EClass} of the objects to return
	 * @return the list with objects or an empty {@link List}
	 */
	public <T extends EObject> List<T> getAllEObjects(EClass eClass);

	/**
	 * Returns a list of all {@link EObject} of the given {@link EClass} or an empty {@link List}
	 * @param eClass the {@link EClass} of the objects to return
	 * @param options the load options
	 * @return the list with objects or an empty {@link List}
	 * @throws EMFRepositoryException thrown when an error during getting all objects occur
	 */
	public <T extends EObject> List<T> getAllEObjects(EClass eClass, Map<?, ?> options);
	
	/**
	 * Counts all objects of the {@link EClass} type
	 * @param eClass the {@link EClass} of the objects to be counted
	 * @return the number of elements or -1 on invalid queries
	 */
	public long count(EClass eClass); 
	
	/**
	 * Counts all objects of the {@link EClass} type
	 * @param eClass the {@link EClass} of the objects to be counted
	 * @param options the load options
	 * @return the number of elements or -1 on invalid queries
	 */
	public long count(EClass eClass, Map<String, Object> options); 

}
