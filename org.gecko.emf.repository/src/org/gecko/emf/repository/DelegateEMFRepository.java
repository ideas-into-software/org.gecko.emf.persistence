/**
 * Copyright (c) 2012 - 2019 Data In Motion and others.
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

/**
 * A simple delegate repository
 * @author Mark Hoffmann
 * @since 02.12.2019
 */
public class DelegateEMFRepository implements EMFRepository {
	
	private EMFRepository delegate;
	
	/**
	 * Sets the delegate.
	 * @param delegate the delegate to set
	 */
	public void setDelegateRepository(EMFRepository delegate) {
		this.delegate = delegate;
	}
	
	/**
	 * Returns the delegate.
	 * @return the delegate
	 */
	public EMFRepository getDelegateRepository() {
		return delegate;
	}

	/**
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#getId()
	 */
	public String getId() {
		return delegate.getId();
	}

	/**
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#getResourceSet()
	 */
	public ResourceSet getResourceSet() {
		return delegate.getResourceSet();
	}

	/**
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#createResourceSet()
	 */
	public ResourceSet createResourceSet() {
		return delegate.createResourceSet();
	}

	/**
	 * @throws Exception
	 * @see java.lang.AutoCloseable#close()
	 */
	public void close() throws Exception {
		delegate.close();
	}

	/**
	 * @param createURI
	 * @param b
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#getResource(org.eclipse.emf.common.util.URI, boolean)
	 */
	public Resource getResource(URI createURI, boolean b) {
		return delegate.getResource(createURI, b);
	}

	/**
	 * @param allTlcResource
	 * @see org.gecko.emf.repository.EMFRepository#cleanResource(org.eclipse.emf.ecore.resource.Resource)
	 */
	public void cleanResource(Resource allTlcResource) {
		delegate.cleanResource(allTlcResource);
	}

	/**
	 * @param object
	 * @param contentType
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#createResource(org.eclipse.emf.ecore.EObject, java.lang.String)
	 */
	public Resource createResource(EObject object, String contentType) {
		return delegate.createResource(object, contentType);
	}

	/**
	 * @param object
	 * @param contentType
	 * @param options
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#createResource(org.eclipse.emf.ecore.EObject, java.lang.String, java.util.Map)
	 */
	public Resource createResource(EObject object, String contentType, Map<?, ?> options) {
		return delegate.createResource(object, contentType, options);
	}

	/**
	 * @param object
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#createResource(org.eclipse.emf.ecore.EObject)
	 */
	public Resource createResource(EObject object) {
		return delegate.createResource(object);
	}

	/**
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#getBaseUri()
	 */
	public String getBaseUri() {
		return delegate.getBaseUri();
	}

	/**
	 * @param <T>
	 * @param eClass
	 * @param id
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#createProxy(org.eclipse.emf.ecore.EClass, java.lang.String)
	 */
	public <T extends EObject> T createProxy(EClass eClass, String id) {
		return delegate.createProxy(eClass, id);
	}

	/**
	 * @param eObject
	 * @see org.gecko.emf.repository.EMFRepository#proxiefyEObject(org.eclipse.emf.ecore.EObject)
	 */
	public void proxiefyEObject(EObject eObject) {
		delegate.proxiefyEObject(eObject);
	}

	/**
	 * @param <T>
	 * @param uri
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#getEObject(org.eclipse.emf.common.util.URI)
	 */
	public <T extends EObject> T getEObject(URI uri) {
		return delegate.getEObject(uri);
	}

	/**
	 * @param <T>
	 * @param uri
	 * @param options
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#getEObject(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	public <T extends EObject> T getEObject(URI uri, Map<?, ?> options) {
		return delegate.getEObject(uri, options);
	}

	/**
	 * @param <T>
	 * @param eClassName
	 * @param id
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#getEObject(java.lang.String, java.lang.Object)
	 */
	public <T extends EObject> T getEObject(String eClassName, Object id) {
		return delegate.getEObject(eClassName, id);
	}

	/**
	 * @param <T>
	 * @param eClassName
	 * @param id
	 * @param options
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#getEObject(java.lang.String, java.lang.Object, java.util.Map)
	 */
	public <T extends EObject> T getEObject(String eClassName, Object id, Map<?, ?> options) {
		return delegate.getEObject(eClassName, id, options);
	}

	/**
	 * @param object
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#createUri(org.eclipse.emf.ecore.EObject)
	 */
	public URI createUri(EObject object) {
		return delegate.createUri(object);
	}

	/**
	 * @param object
	 * @param options
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#createUri(org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	public URI createUri(EObject object, Map<?, ?> options) {
		return delegate.createUri(object, options);
	}

	/**
	 * @param object
	 * @see org.gecko.emf.repository.EMFRepository#reload(org.eclipse.emf.ecore.EObject)
	 */
	public void reload(EObject object) {
		delegate.reload(object);
	}

	/**
	 * @param object
	 * @see org.gecko.emf.repository.EMFRepository#delete(org.eclipse.emf.ecore.EObject)
	 */
	public void delete(EObject object) {
		delegate.delete(object);
	}

	/**
	 * @param object
	 * @param contentType
	 * @see org.gecko.emf.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject, java.lang.String)
	 */
	public void save(EObject object, String contentType) {
		delegate.save(object, contentType);
	}

	/**
	 * @param object
	 * @param contentType
	 * @param options
	 * @see org.gecko.emf.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject, java.lang.String, java.util.Map)
	 */
	public void save(EObject object, String contentType, Map<?, ?> options) {
		delegate.save(object, contentType, options);
	}

	/**
	 * @param object
	 * @see org.gecko.emf.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject)
	 */
	public void save(EObject object) {
		delegate.save(object);
	}

	/**
	 * @param object
	 * @param options
	 * @see org.gecko.emf.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	public void save(EObject object, Map<?, ?> options) {
		delegate.save(object, options);
	}

	/**
	 * @param object
	 * @param uri
	 * @see org.gecko.emf.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject, org.eclipse.emf.common.util.URI)
	 */
	public void save(EObject object, URI uri) {
		delegate.save(object, uri);
	}

	/**
	 * @param object
	 * @param uri
	 * @param options
	 * @see org.gecko.emf.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject, org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	public void save(EObject object, URI uri, Map<?, ?> options) {
		delegate.save(object, uri, options);
	}

	/**
	 * @param object
	 * @param options
	 * @see org.gecko.emf.repository.EMFRepository#save(java.util.Collection, java.util.Map)
	 */
	public void save(Collection<EObject> object, Map<?, ?> options) {
		delegate.save(object, options);
	}

	/**
	 * @param options
	 * @param objects
	 * @see org.gecko.emf.repository.EMFRepository#save(java.util.Map, org.eclipse.emf.ecore.EObject[])
	 */
	public void save(Map<?, ?> options, EObject... objects) {
		delegate.save(options, objects);
	}

	/**
	 * @param objects
	 * @see org.gecko.emf.repository.EMFRepository#save(java.util.Collection)
	 */
	public void save(Collection<EObject> objects) {
		delegate.save(objects);
	}

	/**
	 * @param objects
	 * @see org.gecko.emf.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject[])
	 */
	public void save(EObject... objects) {
		delegate.save(objects);
	}

	/**
	 * @param object
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#detach(org.eclipse.emf.ecore.EObject)
	 */
	public EObject detach(EObject object) {
		return delegate.detach(object);
	}

	/**
	 * @param <T>
	 * @param eClass
	 * @param id
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#getEObject(org.eclipse.emf.ecore.EClass, java.lang.Object)
	 */
	public <T extends EObject> T getEObject(EClass eClass, Object id) {
		return delegate.getEObject(eClass, id);
	}

	/**
	 * @param <T>
	 * @param eClass
	 * @param id
	 * @param options
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#getEObject(org.eclipse.emf.ecore.EClass, java.lang.Object, java.util.Map)
	 */
	public <T extends EObject> T getEObject(EClass eClass, Object id, Map<?, ?> options) {
		return delegate.getEObject(eClass, id, options);
	}

	/**
	 * @param <T>
	 * @param eClass
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#getAllEObjects(org.eclipse.emf.ecore.EClass)
	 */
	public <T extends EObject> List<T> getAllEObjects(EClass eClass) {
		return delegate.getAllEObjects(eClass);
	}

	/**
	 * @param <T>
	 * @param eClass
	 * @param options
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#getAllEObjects(org.eclipse.emf.ecore.EClass, java.util.Map)
	 */
	public <T extends EObject> List<T> getAllEObjects(EClass eClass, Map<?, ?> options) {
		return delegate.getAllEObjects(eClass, options);
	}

	/**
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#isDisposed()
	 */
	public boolean isDisposed() {
		return delegate.isDisposed();
	}

	/**
	 * 
	 * @see org.gecko.emf.repository.EMFRepository#dispose()
	 */
	public void dispose() {
		delegate.dispose();
	}

	/**
	 * @param adapter
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class<?> adapter) {
		return delegate.getAdapter(adapter);
	}

	/**
	 * @param object
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#attach(org.eclipse.emf.ecore.EObject)
	 */
	public Resource attach(EObject object) {
		return delegate.attach(object);
	}

	/**
	 * @param object
	 * @param options
	 * @return
	 * @see org.gecko.emf.repository.EMFRepository#attach(org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	public Resource attach(EObject object, Map<?, ?> options) {
		return delegate.attach(object, options);
	}

}
