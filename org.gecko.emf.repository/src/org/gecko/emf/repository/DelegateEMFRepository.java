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
public class DelegateEMFRepository implements EMFRepositoryHelper, EMFWriteRepository, EMFReadRepository {
	
	private EMFRepository delegate;
	private EMFReadRepository readDelegate;
	private EMFWriteRepository writeDelegate;
	
	/**
	 * Sets the delegate.
	 * @param delegate the delegate to set
	 */
	public void setDelegateRepository(EMFRepository delegate) {
		this.delegate = delegate;
		this.readDelegate = delegate instanceof EMFReadRepository ? (EMFReadRepository)delegate : null;
		this.writeDelegate = delegate instanceof EMFWriteRepository ? (EMFWriteRepository)delegate : null;
	}
	
	/**
	 * Returns the delegate.
	 * @return the delegate
	 */
	public EMFRepository getDelegateRepository() {
		return delegate;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFRepository#getId()
	 */
	public String getId() {
		return delegate.getId();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFRepository#getHelper()
	 */
	@Override
	public EMFRepositoryHelper getHelper() {
		return delegate.getHelper();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFRepositoryHelper#getResourceSet()
	 */
	public ResourceSet getResourceSet() {
		return delegate.getHelper().getResourceSet();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFRepositoryHelper#createResourceSet()
	 */
	public ResourceSet createResourceSet() {
		return delegate.getHelper().createResourceSet();
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	public void close() throws Exception {
		delegate.close();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFReadRepository#getResource(org.eclipse.emf.common.util.URI, boolean)
	 */
	public Resource getResource(URI createURI, boolean b) {
		if (readDelegate == null) {
			throw new UnsupportedOperationException();
		}
		return readDelegate.getResource(createURI, b);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFRepositoryHelper#cleanResource(org.eclipse.emf.ecore.resource.Resource)
	 */
	public void cleanResource(Resource allTlcResource) {
		delegate.getHelper().cleanResource(allTlcResource);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFRepositoryHelper#createResource(org.eclipse.emf.ecore.EObject, java.lang.String)
	 */
	public Resource createResource(EObject object, String contentType) {
		return delegate.getHelper().createResource(object, contentType);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFRepositoryHelper#createResource(org.eclipse.emf.ecore.EObject, java.lang.String, java.util.Map)
	 */
	public Resource createResource(EObject object, String contentType, Map<?, ?> options) {
		return delegate.getHelper().createResource(object, contentType, options);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFRepositoryHelper#createResource(org.eclipse.emf.ecore.EObject)
	 */
	public Resource createResource(EObject object) {
		return delegate.getHelper().createResource(object);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFRepositoryHelper#getBaseUri()
	 */
	public String getBaseUri() {
		return delegate.getHelper().getBaseUri();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFRepositoryHelper#createProxy(org.eclipse.emf.ecore.EClass, java.lang.String)
	 */
	public <T extends EObject> T createProxy(EClass eClass, String id) {
		return delegate.getHelper().createProxy(eClass, id);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFRepositoryHelper#proxiefyEObject(org.eclipse.emf.ecore.EObject)
	 */
	public void proxiefyEObject(EObject eObject) {
		delegate.getHelper().proxiefyEObject(eObject);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFReadRepository#getEObject(org.eclipse.emf.common.util.URI)
	 */
	public <T extends EObject> T getEObject(URI uri) {
		if (readDelegate == null) {
			throw new UnsupportedOperationException();
		}
		return readDelegate.getEObject(uri);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFReadRepository#getEObject(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	public <T extends EObject> T getEObject(URI uri, Map<?, ?> options) {
		if (readDelegate == null) {
			throw new UnsupportedOperationException();
		}
		return readDelegate.getEObject(uri, options);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFReadRepository#getEObject(java.lang.String, java.lang.Object)
	 */
	public <T extends EObject> T getEObject(String eClassName, Object id) {
		if (readDelegate == null) {
			throw new UnsupportedOperationException();
		}
		return readDelegate.getEObject(eClassName, id);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFReadRepository#getEObject(java.lang.String, java.lang.Object, java.util.Map)
	 */
	public <T extends EObject> T getEObject(String eClassName, Object id, Map<?, ?> options) {
		if (readDelegate == null) {
			throw new UnsupportedOperationException();
		}
		return readDelegate.getEObject(eClassName, id, options);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFRepositoryHelper#createUri(org.eclipse.emf.ecore.EObject)
	 */
	public URI createUri(EObject object) {
		return delegate.getHelper().createUri(object);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFRepositoryHelper#createUri(org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	public URI createUri(EObject object, Map<?, ?> options) {
		return delegate.getHelper().createUri(object, options);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.repository.EMFRepository#reload(org.eclipse.emf.ecore.EObject)
	 */
	public void reload(EObject object) {
		delegate.reload(object);
	}

	/**
	 * @param object
	 * @see org.gecko.emf.repository.repository.EMFRepository#delete(org.eclipse.emf.ecore.EObject)
	 */
	public void delete(EObject object) {
		delegate.delete(object);
	}

	/**
	 * @param object
	 * @param contentType
	 * @see org.gecko.emf.repository.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject, java.lang.String)
	 */
	public void save(EObject object, String contentType) {
		if (writeDelegate == null) {
			throw new UnsupportedOperationException();
		}
		writeDelegate.save(object, contentType);
	}

	/**
	 * @param object
	 * @param contentType
	 * @param options
	 * @see org.gecko.emf.repository.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject, java.lang.String, java.util.Map)
	 */
	public void save(EObject object, String contentType, Map<?, ?> options) {
		if (writeDelegate == null) {
			throw new UnsupportedOperationException();
		}
		writeDelegate.save(object, contentType, options);
	}

	/**
	 * @param object
	 * @see org.gecko.emf.repository.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject)
	 */
	public void save(EObject object) {
		if (writeDelegate == null) {
			throw new UnsupportedOperationException();
		}
		writeDelegate.save(object);
	}

	/**
	 * @param object
	 * @param options
	 * @see org.gecko.emf.repository.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	public void save(EObject object, Map<?, ?> options) {
		if (writeDelegate == null) {
			throw new UnsupportedOperationException();
		}
		writeDelegate.save(object, options);
	}

	/**
	 * @param object
	 * @param uri
	 * @see org.gecko.emf.repository.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject, org.eclipse.emf.common.util.URI)
	 */
	public void save(EObject object, URI uri) {
		if (writeDelegate == null) {
			throw new UnsupportedOperationException();
		}
		writeDelegate.save(object, uri);
	}

	/**
	 * @param object
	 * @param uri
	 * @param options
	 * @see org.gecko.emf.repository.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject, org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	public void save(EObject object, URI uri, Map<?, ?> options) {
		if (writeDelegate == null) {
			throw new UnsupportedOperationException();
		}
		writeDelegate.save(object, uri, options);
	}

	/**
	 * @param object
	 * @param options
	 * @see org.gecko.emf.repository.repository.EMFRepository#save(java.util.Collection, java.util.Map)
	 */
	public void save(Collection<EObject> object, Map<?, ?> options) {
		if (writeDelegate == null) {
			throw new UnsupportedOperationException();
		}
		writeDelegate.save(object, options);
	}

	/**
	 * @param options
	 * @param objects
	 * @see org.gecko.emf.repository.repository.EMFRepository#save(java.util.Map, org.eclipse.emf.ecore.EObject[])
	 */
	public void save(Map<?, ?> options, EObject... objects) {
		if (writeDelegate == null) {
			throw new UnsupportedOperationException();
		}
		writeDelegate.save(options, objects);
	}

	/**
	 * @param objects
	 * @see org.gecko.emf.repository.repository.EMFRepository#save(java.util.Collection)
	 */
	public void save(Collection<EObject> objects) {
		if (writeDelegate == null) {
			throw new UnsupportedOperationException();
		}
		writeDelegate.save(objects);
	}

	/**
	 * @param objects
	 * @see org.gecko.emf.repository.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject[])
	 */
	public void save(EObject... objects) {
		if (writeDelegate == null) {
			throw new UnsupportedOperationException();
		}
		writeDelegate.save(objects);
	}

	/**
	 * @param object
	 * @return
	 * @see org.gecko.emf.repository.repository.EMFRepository#detach(org.eclipse.emf.ecore.EObject)
	 */
	public EObject detach(EObject object) {
		return delegate.getHelper().detach(object);
	}

	/**
	 * @param <T>
	 * @param eClass
	 * @param id
	 * @return
	 * @see org.gecko.emf.repository.repository.EMFRepository#getEObject(org.eclipse.emf.ecore.EClass, java.lang.Object)
	 */
	public <T extends EObject> T getEObject(EClass eClass, Object id) {
		if (readDelegate == null) {
			throw new UnsupportedOperationException();
		}
		return readDelegate.getEObject(eClass, id);
	}

	/**
	 * @param <T>
	 * @param eClass
	 * @param id
	 * @param options
	 * @return
	 * @see org.gecko.emf.repository.repository.EMFRepository#getEObject(org.eclipse.emf.ecore.EClass, java.lang.Object, java.util.Map)
	 */
	public <T extends EObject> T getEObject(EClass eClass, Object id, Map<?, ?> options) {
		if (readDelegate == null) {
			throw new UnsupportedOperationException();
		}
		return readDelegate.getEObject(eClass, id, options);
	}

	/**
	 * @param <T>
	 * @param eClass
	 * @return
	 * @see org.gecko.emf.repository.repository.EMFRepository#getAllEObjects(org.eclipse.emf.ecore.EClass)
	 */
	public <T extends EObject> List<T> getAllEObjects(EClass eClass) {
		if (readDelegate == null) {
			throw new UnsupportedOperationException();
		}
		return readDelegate.getAllEObjects(eClass);
	}

	/**
	 * @param <T>
	 * @param eClass
	 * @param options
	 * @return
	 * @see org.gecko.emf.repository.repository.EMFRepository#getAllEObjects(org.eclipse.emf.ecore.EClass, java.util.Map)
	 */
	public <T extends EObject> List<T> getAllEObjects(EClass eClass, Map<?, ?> options) {
		if (readDelegate == null) {
			throw new UnsupportedOperationException();
		}
		return readDelegate.getAllEObjects(eClass, options);
	}

	/**
	 * @return
	 * @see org.gecko.emf.repository.repository.EMFRepository#isDisposed()
	 */
	public boolean isDisposed() {
		return delegate.isDisposed();
	}

	/**
	 * 
	 * @see org.gecko.emf.repository.repository.EMFRepository#dispose()
	 */
	public void dispose() {
		delegate.dispose();
	}

	/**
	 * @param adapter
	 * @return
	 * @see org.gecko.emf.repository.repository.EMFRepository#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class<?> adapter) {
		return delegate.getAdapter(adapter);
	}

	/**
	 * @param object
	 * @return
	 * @see org.gecko.emf.repository.repository.EMFRepository#attach(org.eclipse.emf.ecore.EObject)
	 */
	public Resource attach(EObject object) {
		return delegate.getHelper().attach(object);
	}

	/**
	 * @param object
	 * @param options
	 * @return
	 * @see org.gecko.emf.repository.repository.EMFRepository#attach(org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	public Resource attach(EObject object, Map<?, ?> options) {
		return delegate.getHelper().attach(object, options);
	}

}
