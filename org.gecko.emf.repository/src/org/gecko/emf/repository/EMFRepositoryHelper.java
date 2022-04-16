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

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Interface for a EMF persistence helper
 * @author Mark Hoffmann
 * @since 12.04.2015
 */
public interface EMFRepositoryHelper {

	/**
	 * Gets the repository owned {@link ResourceSet}
	 * @return the repository owned {@link ResourceSet}
	 */
	public ResourceSet getResourceSet();

	/**
	 * Creates a new  {@link ResourceSet} out side the repository
	 * @return a new {@link ResourceSet}
	 */
	public ResourceSet createResourceSet();

	/**
	 * In EMF data are cached. Especially for query is this can be annoying, so this helper method unload the resource 
	 * @param resource the resource to be cleaned
	 */
	public void cleanResource(Resource allTlcResource);

	/**
	 * Creates a resource for a given {@link EObject}. If the object already has a resource,
	 * this will returned. Otherwise a new resource will be created and the object will be attached to the resource
	 * @param object the {@link EObject} to create the resource from
	 * @param contentType the content type of the object (find it in the package interface)
	 * @return the resource
	 */
	public Resource createResource(EObject object, String contentType);
	
	/**
	 * Creates a resource for a given {@link EObject}. If the object already has a resource,
	 * this will returned. Otherwise a new resource will be created and the object will be attached to the resource
	 * @param object the {@link EObject} to create the resource from
	 * @param contentType the content type of the object (find it in the package interface)
	 * @param options the options map
	 * @return the resource
	 */
	public Resource createResource(EObject object, String contentType, Map<?, ?> options);

	/**
	 * Creates a resource for a given {@link EObject}. If the object already has a resource,
	 * this will returned. Otherwise a new resource will be created and the object will be attached to the resource
	 * @param object the {@link EObject} to create the resource from
	 * @return the resource
	 */
	public Resource createResource(EObject object);

	/**
	 * Returns the base uri
	 * @return the base uri
	 */
	public String getBaseUri();

	/**
	 * Returns the {@link EObject} that is a proxy of the given EClass and with the given id
	 * @param eClass the EClass to create the proxy for
	 * @param id the id of the Object
	 * @return the {@link EObject} as a proxy
	 */
	public  <T extends EObject> T createProxy(EClass eClass, String id);

	/**
	 * Changes the given EObject to become a proxy. If the Object already has a eResource, 
	 * it will be detached and the eResource disposed. Note that no non containment references will be touched
	 * @param eObject the {@link EObject} to turn into a proxy
	 */
	public void proxiefyEObject(EObject eObject);

	/**
	 * Creates the {@link URI} from a given {@link EObject}
	 * @param object the {@link EObject} to create the {@link URI} from
	 * @return the {@link URI} or <code>null</code> on errors
	 */
	public URI createUri(EObject object);

	/**
	 * Creates the {@link URI} from a given {@link EObject}
	 * @param object the {@link EObject} to create the {@link URI} from
	 * @param options the options to use
	 * @return the {@link URI} or <code>null</code> on errors
	 */
	public URI createUri(EObject object, Map<?, ?> options);

	/**
	 * Detaches an object from the resource
	 * @param object the object to detach
	 * @return the detached {@link EObject}
	 */
	public EObject detach(EObject object);
	
	/**
	 * An adapter method to adapt the repository for e.g. query builders
	 * @param adapter the type to adapt to
	 * @return the instance of the adapted type or <code>null</code>
	 */
	public Object getAdapter(Class<?> adapter);

	/**
	 * Attaches the given {@link EObject} to a Resource with a proper {@link URI} without saving the Object 
	 * @param object the {@link EObject} to attache
	 * @return The {@link Resource} the EObject is contained in
	 */
	public Resource attach(EObject object);
	
	/**
	 * Attaches the given {@link EObject} to a Resource with a proper {@link URI} without saving the Object 
	 * @param object the {@link EObject} to attach
	 * @param options the options map
	 * @return The {@link Resource} the EObject is contained in
	 */
	public Resource attach(EObject object, Map<?, ?> options);

}
