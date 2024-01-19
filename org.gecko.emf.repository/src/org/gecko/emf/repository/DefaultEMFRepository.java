/**
 * Copyright (c) 2017 Data In Motion and others.
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.emf.osgi.ResourceSetFactory;
import org.gecko.emf.repository.exception.ConstraintValidationException;
import org.gecko.emf.repository.helper.RepositoryHelper;

/**
 * Default implementation if {@link EMFRepository}.
 * @author Mark Hoffmann
 * @since 12.04.2015
 */
public abstract class DefaultEMFRepository implements EMFRepository {

	/** STRING */
	private static final String EMPTY_STRING = "";
	private final Logger logger = Logger.getLogger(DefaultEMFRepository.class.getName());
	private ResourceSetFactory resourceSetFactory;
	private ResourceSet resourceSet = null;
	private Boolean disposed = Boolean.FALSE;
	private String id;
	private String baseUri;
	protected Map<Object, Object> defaultResourceSetLoadOptions;

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#getBaseUri()
	 */
	@Override
	public String getBaseUri() {
		return baseUri;
	}

	/* (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#createProxy(org.eclipse.emf.ecore.EClass, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends EObject> T createProxy(EClass eClass, String id) {
		EObject eObject = EcoreUtil.create(eClass);
		EcoreUtil.setID(eObject, id);
		proxiefyEObject(eObject);
		return (T) eObject;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#proxiefyEObject(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public void proxiefyEObject(EObject eObject) {
		if(eObject.eIsProxy()) {
			return;
		}
		String id = EcoreUtil.getID(eObject);
		if(id == null) {
			throw new RuntimeException("the given eObject has no id set. Thus no valid proxy uri can be created");
		}
		URI uri = eObject.eResource() != null ? eObject.eResource().getURI() : createUri(eObject);
		((InternalEObject) eObject).eSetProxyURI(uri.appendFragment(id));
		
		if(eObject.eResource() != null) {
			detach(eObject);
		}
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject, java.lang.String)
	 */
	@Override
	public void save(EObject object, String contentType) {
		save(object, contentType, Collections.emptyMap());
	}
	
	@Override
	public void save(Collection<EObject> objects) {
		save(objects, Collections.emptyMap());
	}
	
	/* (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject[])
	 */
	@Override
	public void save(EObject... objects) {
		save(Collections.emptyMap(), objects);
	}
	
	/* (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#save(java.util.Map, org.eclipse.emf.ecore.EObject[])
	 */
	@Override
	public void save(Map<?, ?> options, EObject... objects) {
		save(Arrays.asList(objects), options);
	}
	
	/* (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#save(java.util.Collection, java.util.Map)
	 */
	@Override
	public void save(Collection<EObject> objects, Map<?, ?> options) {
		final Map<Object, Object> saveOptions = mergeOptions(options);
		
		
		List<Resource> toSave = new ArrayList<>(objects.size());
		objects.stream()
		.map(eo->this.attach(eo, saveOptions))
		.filter(r -> !toSave.contains(r))
		.forEach(toSave::add);
		
		RepositoryHelper.checkForAttachedNonContainmentReferences(objects);
		if(options != null) {
			Object validation = options.get(RepositoryConstants.FULL_CONSTRAINT_VALIDATION);
			if( Boolean.TRUE == validation || "true".equals(validation)) {
				BasicDiagnostic diagnostic = new BasicDiagnostic();
				objects.stream()
				.map(EcoreUtil::copy)
				.forEach(eo -> Diagnostician.INSTANCE.validate(eo, diagnostic));
				if(diagnostic.getSeverity() == Diagnostic.ERROR) {
					throw new ConstraintValidationException("Validation Error", diagnostic);
				}
			}
		}
		for (Resource resource : toSave) {
			try {
				resource.save(saveOptions);
			} catch (IOException e) {
				throw new IllegalStateException("Error saving object " + resource, e); 
			}
		}
	}

	private void save(EObject object, URI uri, String contentType, Map<?, ?> options) {
		Resource resource = attach(object, uri, contentType, options);
		RepositoryHelper.checkForAttachedNonContainmentReferences(object);
		
		if(options != null) {
			Object validation = options.get(RepositoryConstants.FULL_CONSTRAINT_VALIDATION);
			if( Boolean.TRUE == validation || "true".equals(validation)) {
				EObject copy = EcoreUtil.copy(object);
				Diagnostic diag = Diagnostician.INSTANCE.validate(copy);
				if(diag.getSeverity() == Diagnostic.ERROR) {
					throw new ConstraintValidationException("Validation Error on " + object.toString(), diag);
				}
			}
		}
		try {
			resource.save(options);
		} catch (IOException e) {
			throw new IllegalStateException("Error saving object " + object, e); 
		}
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject, java.lang.String, java.util.Map)
	 */
	@Override
	public void save(EObject object, String contentType, Map<?, ?> options) {
		save(object, null, contentType, options);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject, org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public void save(EObject object, URI uri, Map<?, ?> options) {
		save(object, uri, null, options);
	}
	
	@Override
	public Resource attach(EObject object) {
		return attach(object, Collections.emptyMap());
	}
	
	@Override
	public Resource attach(EObject object, Map<?, ?> options) {
		return attach(object, null, null, options);
	}
	
	private Resource attach(EObject object, URI uri, String contentType, Map<?, ?> options) {
		if (object == null) {
			throw new IllegalStateException("Error saving object that is null");
		}
		EObject root = EcoreUtil.getRootContainer(object);
		if (root == null) {
			root = object;
		}
		setIDs(root);
		org.eclipse.emf.ecore.resource.Resource storageResource = createResource(root, uri, contentType, options);
		if (storageResource == null) {
			throw new IllegalStateException("Cannot create a storage resource for controller " + root);
		}
		/* 
		 * if the resource from the object is not the same like the storage resource, we have to add the object to the
		 * storage resource 
		 */
		if (!storageResource.equals(root.eResource())) {
			storageResource.getContents().clear();
			storageResource.getContents().add(root);
		}
		return storageResource;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	@Override
	public void save(EObject object, Map<?, ?> options) {
		save(object, null, null, options);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public void save(EObject object) {
		save(object, Collections.emptyMap());
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#save(org.eclipse.emf.ecore.EObject, org.eclipse.emf.common.util.URI)
	 */
	@Override
	public void save(EObject object, URI uri) {
		save(object, uri, Collections.emptyMap());
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#getEObject(org.eclipse.emf.ecore.EClass, java.lang.Object, java.util.Map)
	 */
	@Override
	public <T extends EObject> T getEObject(EClass eClass, Object id, Map<?, ?> options) {
		if (eClass == null) {
			logger.log(Level.SEVERE, "Error getting EObject without class parameter");
			return null;
		}
		return getEObject(RepositoryHelper.getUriHintNameForEClass(eClass), id, options);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#getEObject(org.eclipse.emf.ecore.EClass, java.lang.Object)
	 */
	@Override
	public <T extends EObject> T getEObject(EClass eClass, Object id) {
		return getEObject(eClass, id, Collections.emptyMap());
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#getAllEObjects(org.eclipse.emf.ecore.EClass, java.util.Map)
	 */
	@Override
	public <T extends EObject> List<T> getAllEObjects(EClass eClass, Map<?, ?> options) {
		return Collections.emptyList();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#getAllEObjects(org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public <T extends EObject> List<T> getAllEObjects(EClass eClass) {
		return Collections.emptyList();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#getEObject(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends EObject> T getEObject(URI uri, Map<?, ?> options) {
		try {
			if(uri.fragment() != null && !uri.fragment().isEmpty()){
				EObject eObject = getResourceSet().getEObject(uri, true);
				if(eObject != null){
					return (T) eObject;
				} else {
					logger.fine("No content found for URI " + uri.toString());
					return null;
				}
			}
			Resource resource = getResource(uri, true);
			if (resource == null) {
				resource = createResource(uri);
			}
			if (resource == null) {
				throw new IllegalStateException("Cannot create resource for URI " + uri.toString());
			}
			resource.load(options);
			if (resource.getContents().isEmpty()) {
				logger.fine("No content found for URI " + uri.toString());
				return null;
			}
			return (T) resource.getContents().get(0);
		} catch (Exception e) {
			if(e.getCause() instanceof FileNotFoundException){
				logger.log(Level.FINE, "No content found for URI " + uri.toString(), e);
				return null;
			}
			throw new IllegalStateException("No content found for URI " + uri.toString(), e); 
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#getEObject(org.eclipse.emf.common.util.URI)
	 */
	public <T extends EObject> T getEObject(URI uri) {
		return getEObject(uri, Collections.emptyMap());
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#reload(org.eclipse.emf.ecore.EObject)
	 */
	public void reload(EObject object) {
		if (object == null || object.eResource() == null) {
			return;
		}
		Resource resource = object.eResource();
		if (resource.isLoaded()) {
			resource.unload();
			try {
				resource.load(null);
			} catch (IOException e) {
				throw new IllegalStateException("Error during object reload " + object, e);
			}
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#delete(org.eclipse.emf.ecore.EObject)
	 */
	public void delete(EObject object) {
		if (object == null) {
			return;
		}
		Resource resource = null;
		// if object has no resource try to load it
		if (object.eResource() == null) {
			URI uri = createUri(object);
			resource = getResource(uri, true);
		} else {
			resource = object.eResource();
		}
		// if there is no resource for the uri forget the delete
		if (resource == null) {
			return;
		}

		try {
			if (resource.isLoaded()) {
				resource.delete(null);
			} else {
				resource.load(null);
				resource.delete(null);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Error during object reload " + object, e);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#detach(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public EObject detach(EObject object) {
		if (object == null || object.eResource() == null) {
			return null;
		}
		Resource resource = object.eResource();
		cleanResource(resource);
		ResourceSet resourceSet = resource.getResourceSet();
		if (resourceSet != null) {
			resourceSet.getResources().remove(resource);
		}
		return object;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#getResourceSet()
	 */
	@Override
	public ResourceSet getResourceSet() {
		if (resourceSet == null) {
			resourceSet = createResourceSet();
		}
		return resourceSet;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#createResourceSet()
	 */
	public ResourceSet createResourceSet() {
		ResourceSet resourceSet = getResourceSetFactory().createResourceSet();
		resourceSet.getLoadOptions().putAll(defaultResourceSetLoadOptions);
		return resourceSet;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#getResource(org.eclipse.emf.common.util.URI, boolean)
	 */
	public Resource getResource(URI uri, boolean loadOnDemand) {
		return getResourceSet().getResource(uri, loadOnDemand);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#createResource(org.eclipse.emf.ecore.EObject, java.lang.String)
	 */
	public Resource createResource(EObject object, String contentType) {
		return createResource(object, contentType, null);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#createResource(org.eclipse.emf.ecore.EObject, java.lang.String, java.util.Map)
	 */
	public Resource createResource(EObject object, String contentType, Map<?, ?> options) {
		return createResource(object, null, contentType, options); 
	}

	private Resource createResource(EObject object, URI uri, String contentType, Map<?, ?> options) {
		if (object == null) {
			return null;
		}
		if (options == null) {
			options = Collections.emptyMap();
		}
		if (object.eResource() != null && object.eResource().getResourceSet() != null && getResourceSet().getResources().contains(object.eResource())) {
			return object.eResource();
		}
		URI theUri = uri;
		if(uri == null) {
			theUri = createUri(object, options); 
		}
		theUri = theUri.trimFragment();
		Resource r = getResourceSet().getResource(theUri, false);
		if (r == null) {
			r = getResourceSet().createResource(theUri, contentType);
		}
		return r;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#createResource(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public synchronized Resource createResource(EObject object) {
		return createResource(object, null, Collections.emptyMap());
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#cleanResource(org.eclipse.emf.ecore.resource.Resource)
	 */
	public void cleanResource(Resource resource) {
		if (resource == null) {
			return;
		}
		resource.getContents().clear();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#createUri(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public URI createUri(EObject object) {
		return createUri(object, Collections.emptyMap()); 
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#createUri(org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	@Override
	public URI createUri(EObject object, Map<?, ?> options) {
		if (object == null) {
			return null;
		}
		String id = EcoreUtil.getID(object);
		if (id == null) {
			throw new IllegalStateException("The given EObject of EClass " + object.eClass().getName() + " doesn't contain a id attribute but should have one");
		}
		URI uri = createEClassUri(object.eClass(), mergeOptions(options));
		uri = uri.appendSegment(id);
		return uri;
	}

	/**
	 * Creates an uri for a given {@link EClass}
	 * @param eClass the {@link EClass} to create the URI from
	 * @return the URI or <code>null</code>
	 */
	public URI createEClassUri(EClass eClass) {
		return createEClassUri(eClass, null);
	}
	/**
	 * Creates an uri for a given {@link EClass}
	 * @param eClass the {@link EClass} to create the URI from
	 * @param options the options to consider
	 * @return the URI or <code>null</code>
	 */
	public URI createEClassUri(EClass eClass, Map<?, ?> options) {
		
		if (eClass == null) {
			return null;
		}
		
		return createEClassUri(RepositoryHelper.getUriHintNameForEClass(eClass), options);
	}
	
	/**
	 * Creates an uri for a given {@link EClass}
	 * @param eClass the {@link EClass} name to create the URI from
	 * @param options the options to consider
	 * @return the URI or <code>null</code>
	 */
	public URI createEClassUri(String eClass, Map<?, ?> options) {
		
		if (eClass == null) {
			return null;
		}
		
		String prefix = null;
		String sufix = null;
		String theEClassNameToUse = null;
		if(options != null) {
			prefix= (String) options.get(RepositoryConstants.URI_HINT_PREFIX);
			sufix = (String) options.get(RepositoryConstants.URI_HINT_SUFIX);
			theEClassNameToUse = (String) options.get(RepositoryConstants.URI_HINT);
		}
		if(theEClassNameToUse == null ) {
			theEClassNameToUse = eClass;
		}
		return createEClassUri(theEClassNameToUse, prefix, sufix);
	}

	/**
	 * Creates an uri for a given {@link EClass}
	 * @param eClass the {@link EClass} to create the URI from
	 * @return the URI or <code>null</code>
	 */
	public URI createEClassUri(String eClass, String prefix, String sufix) {
		if (eClass == null) {
			return null;
		}
		
		String thePrefix = prefix;
		if(thePrefix == null) {
			thePrefix = EMPTY_STRING;
		}
		
		String theSufix = sufix;
		if(theSufix == null) {
			theSufix = EMPTY_STRING;
		}
		
		String uri = String.format("%s/%s%s%s", getBaseUri(), thePrefix, eClass, theSufix);
		return URI.createURI(uri);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#dispose()
	 */
	@Override
	public void dispose() {
		if (!disposed && resourceSet != null) {
			disposed = Boolean.TRUE;
			for (Resource resource : resourceSet.getResources()) {
				resource.getContents().clear();
			}
			resourceSet.getResources().clear();
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		dispose();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#isDisposed()
	 */
	@Override
	public synchronized boolean isDisposed() {
		return disposed;
	}

	/**
	 * Creates a resource for the given {@link URI}
	 * @param uri the URI to create a resource from
	 * @return the {@link Resource} or <code>null</code> 
	 */
	public Resource createResource(URI uri) {
		return getResourceSet().createResource(uri);
	}

	/**
	 * Creates a resource for the given {@link URI}
	 * @param uri the URI to create a resource from
	 * @param contentType the content type of the object
	 * @return the {@link Resource} or <code>null</code> 
	 */
	public Resource createResource(URI uri, String contentType) {
		return getResourceSet().createResource(uri, contentType);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#getEObject(java.lang.String, java.lang.Object, java.util.Map)
	 */
	public <T extends EObject> T getEObject(String eClassName, Object id, Map<?, ?> options) {
		if (eClassName == null || id == null) {
			logger.log(Level.SEVERE, "Error getting EObject without class name or id parameters");
			return null;
		}
		URI uri = createEClassUri(eClassName, options).appendSegment(id.toString()).appendFragment(id.toString());
		return getEObject(uri, options);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#getEObject(java.lang.String, java.lang.Object)
	 */
	public <T extends EObject> T getEObject(String eClassName, Object id) {
		return getEObject(eClassName, id, Collections.emptyMap());
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class<?> adapter) {
		return null;
	}

	/**
	 * Called on service activation
	 * @param properties the service properties
	 */
	@SuppressWarnings("unchecked")
	public void activate(Map<String, ?> properties) {
		id = (String) properties.get(PROP_ID);
		if (id == null) {
			throw new IllegalStateException(
					"Error because of missing property 'repo_id' in service properties");
		}
		baseUri = (String) properties.get(PROP_BASE_URI);
		if (baseUri == null) {
			throw new IllegalStateException(
					"Error because of missing property 'base_uri' in service properties");
		}
		if(baseUri.endsWith("/")){
			baseUri = baseUri.substring(0, baseUri.length() -1);
		}
		defaultResourceSetLoadOptions = (Map<Object, Object>) properties.get(PROP_DEFAULT_LOAD_OPTIONS);
		if(defaultResourceSetLoadOptions == null){
			defaultResourceSetLoadOptions = new HashMap<Object, Object>();
		}
	}

	/**
	 * Called on service de-activation
	 */
	public void deactivate() {
		dispose();
	}

	/**
	 * Returns the injected resource set factory
	 * @return the injected resource set factory
	 */
	public ResourceSetFactory getResourceSetFactory() {
		return resourceSetFactory;
	}

	/**
	 * Un-sets the resource set factory using OSGi DS
	 * @param resourceSetFactory the resource set factory to set
	 */
	public void unsetResourceSetFactory(ResourceSetFactory resourceSetFactory) {
		dispose();
		this.resourceSetFactory = null;
	}

	/**
	 * Sets the resource set factory using OSGi DS
	 * @param resourceSetFactory the resource set factory to set
	 */
	public void setResourceSetFactory(ResourceSetFactory resourceSetFactory) {
		this.resourceSetFactory = resourceSetFactory;
	}

	/**
	 * Merges the given options Map with the default load options from the {@link ResourceSet}.
	 * If options are given a new Map will be created and the default options will be added before the
	 * given options.
	 * @param options the given options. Can be null
	 * @return a {@link Map} with options
	 */
	private Map<Object, Object> mergeOptions(Map<?, ?> options) {
		ResourceSet resourceSet = getResourceSet();
		if (options == null) {
			return resourceSet.getLoadOptions();
		} else {
			Map<Object, Object> saveOptions = null;
			saveOptions = new HashMap<>();
			saveOptions.putAll(resourceSet.getLoadOptions());
			saveOptions.putAll(options);
			return saveOptions;
		}
	}
	
	/**
	 * Sets the IDs for the given Object Tree. This can be repository specific, because different Types of IDs might be better suited.
	 * @param rootObject
	 */
	abstract protected void setIDs(EObject rootObject);
}
