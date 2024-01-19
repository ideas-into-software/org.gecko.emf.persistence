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
package org.gecko.emf.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.gecko.emf.osgi.annotation.require.RequireEMF;

/**
 * Interface for a EMF persistence repository
 * @author Mark Hoffmann
 * @since 12.04.2015
 */
@RequireEMF
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
	 * Gets a resource for the given {@link URI} and loads it on demand, if the parameter is set to <code>true</code>
	 * @param uri the URI to create a resource from
	 * @param loadOnDemand set to <code>true</code> if the resource should be loaded 
	 * @return the {@link Resource} or <code>null</code> 
	 */
	public Resource getResource(URI createURI, boolean b);

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
	 * Returns the {@link EObject} tha is a proxy of the given EClass and with the given id
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

	/**
	 * Detaches an object from the resource
	 * @param object the object to detach
	 * @return the detached {@link EObject}
	 */
	public EObject detach(EObject object);

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
