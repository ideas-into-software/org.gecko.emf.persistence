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
package org.gecko.emf.repository.mongo.impl;

import static org.gecko.emf.repository.mongo.api.EMFMongoConfiguratorConstants.REPO_RESOURCE_SET_FIELD_NAME;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.types.ObjectId;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.collection.EReferenceCollection;
import org.gecko.emf.mongo.Options;
import org.gecko.emf.osgi.ResourceSetFactory;
import org.gecko.emf.repository.DefaultEMFRepository;
import org.gecko.emf.repository.helper.RepositoryHelper;
import org.gecko.emf.repository.mongo.query.MongoQueryBuilder;
import org.gecko.emf.repository.query.IQuery;
import org.gecko.emf.repository.query.IQueryBuilder;
import org.gecko.emf.repository.query.QueryRepository;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * EMF repository to store emf objects into a mongo database.
 * @author Mark Hoffmann
 * @since 12.04.2015
 */
public abstract class AbstractEMFMongoRepository extends DefaultEMFRepository implements QueryRepository {

	private final Logger logger = Logger.getLogger(AbstractEMFMongoRepository.class.getName());

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.DefaultEMFRepository#activate(java.util.Map)
	 */
	@Override
	@Activate
	public void activate(Map<String, ?> properties) {
		super.activate(properties);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.DefaultEMFRepository#deactivate()
	 */
	@Override
	@Deactivate
	public void deactivate() {
		super.deactivate();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.DefaultEMFRepository#setResourceSetFactory(org.gecko.emf.osgi.ResourceSetFactory)
	 */
	@Override
	@Reference(name=REPO_RESOURCE_SET_FIELD_NAME, cardinality=ReferenceCardinality.MANDATORY, policy=ReferencePolicy.STATIC)
	public void setResourceSetFactory(ResourceSetFactory resourceSetFactory) {
		super.setResourceSetFactory(resourceSetFactory);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.DefaultEMFRepository#createResource(org.eclipse.emf.ecore.EObject, java.lang.String)
	 */
	@Override
	public Resource createResource(EObject object, String contentType) {
		return createResource(object, contentType, Collections.emptyMap());
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.DefaultEMFRepository#createResource(org.eclipse.emf.ecore.EObject, java.lang.String, java.util.Map)
	 */
	@Override
	public synchronized Resource createResource(EObject object, String contentType, Map<?, ?> options) {
		if (object == null) {
			return null;
		}
		Resource resultResource = object.eResource();
		if (resultResource != null && 
				resultResource.getResourceSet() != null && 
				"mongodb".equals(resultResource.getURI().scheme())) {
			getCollectionName(object.eClass(), options);
			resultResource = object.eResource();
		} else {
			URI uri = createUri(object, options);
			resultResource = getResourceSet().getResource(uri, false);
			if (resultResource == null) {
				resultResource = getResourceSet().createResource(uri, contentType);
			}
		}
		return resultResource;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.DefaultEMFRepository#getEObject(org.eclipse.emf.ecore.EClass, java.lang.Object, java.util.Map)
	 */
	@Override
	public <T extends EObject> T getEObject(EClass eClass, Object id, Map<?, ?> options) {
		if (eClass == null) {
			logger.log(Level.SEVERE, "Error getting EObject without EClass parameter");
			return null;
		}
		String eclassAlias = getCollectionName(eClass, options);
		return getEObject(eclassAlias, id, options);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.DefaultEMFRepository#getAllEObjects(org.eclipse.emf.ecore.EClass, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends EObject> List<T> getAllEObjects(EClass eClass, Map<?, ?> options) {
		IQuery query = createQueryBuilder().allQuery().build();
		return getEObjectsByQuery(eClass, query, (Map<Object, Object>) options);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.DefaultEMFRepository#getAllEObjects(org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public List<EObject> getAllEObjects(EClass eClass) {
		return getAllEObjects(eClass, Collections.emptyMap());
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.DefaultEMFRepository#createUri(org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	@Override
	public URI createUri(EObject object, Map<?, ?> options) {
		if (object == null) {
			return null;
		}
		String id = EcoreUtil.getID(object);
		URI uri = createMongoURI(object.eClass(), options);
		
		Object useId = options.get(Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY);
		if (useId == null || Boolean.TRUE.equals(useId)) {
			if (id == null) {
				throw new IllegalStateException("The given EObject of EClass " + object.eClass().getName() + " doesn't contain a id attribute but should have one");
			}
		}
		/*
		 * Depending on this option we add the id as segment or not. If the option is set to false,
		 * the _id will be generated from the MongoDB 
		 */
		if (useId == null || Boolean.TRUE.equals(useId)) {
			uri = uri.appendSegment(id);
		} else {
			uri = uri.appendSegment("");
		}
		if (id != null) {
			uri = uri.appendFragment(id);
		}
		return uri;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.QueryRepository#createQueryBuilder()
	 */
	@Override
	public IQueryBuilder createQueryBuilder() {
		return new MongoQueryBuilder();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.QueryRepository#getEObjectsByQuery(org.eclipse.emf.ecore.EClass, org.gecko.emf.repository.query.IQuery)
	 */
	@Override
	public <T extends EObject> List<T> getEObjectsByQuery(EClass eClass, IQuery query) {
		return getEObjectsByQuery(eClass, query, defaultResourceSetLoadOptions);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.QueryRepository#getEObjectsByQuery(org.eclipse.emf.ecore.EClass, org.gecko.emf.repository.query.IQuery, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends EObject> List<T> getEObjectsByQuery(EClass eClass, IQuery query,
			Map<Object, Object> options) {
		Resource resource = executeQuery(eClass, query, options);
		try {
			if (resource.getContents().size() > 0) {
				EReferenceCollection result = (EReferenceCollection) resource.getContents().get(0);
				List<EObject> resultList = result.getValues();
				List<T> newResult = new ArrayList<T>(resultList.size());
				resultList.stream().map(o -> (T) o).forEach(newResult::add);
				return newResult;
			} else {
				return Collections.emptyList();
			}
		}
		finally {
			resource.getContents().clear();
			resource.getResourceSet().getResources().remove(resource);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.QueryRepository#getEObjectByQuery(org.eclipse.emf.ecore.EClass, org.gecko.emf.repository.query.IQuery, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends EObject> T getEObjectByQuery(EClass eClass, IQuery query, Map<Object, Object> options) {
		Resource resource = executeQuery(eClass, query, options);
		if(resource.getContents().isEmpty()){
			return null;
		} else {
			T result = null;
			if(resource.getContents().get(0) instanceof EReferenceCollection){
				EReferenceCollection referenceCollection = (EReferenceCollection) resource.getContents().get(0);
				if(referenceCollection.getValues().size() > 0){
					result = (T) referenceCollection.getValues().get(0);
				}
				resource.getContents().clear();
				resource.getResourceSet().getResources().remove(resource);
			} else {
				result =  (T) resource.getContents().get(0);
			}
			return result;
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.EMFRepository#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class<?> adapter) {
		if (adapter.isAssignableFrom(QueryRepository.class)) {
			return this;
		}
		if (adapter.isAssignableFrom(IQueryBuilder.class)) {
			return createQueryBuilder();
		}
		return null;
	}

	/**
	 * Executes the fiven query and returns the result as a resource
	 * @param eClass the {@link EClass} to look for
	 * @param query the query
	 * @param options the options {@link Map}
	 * @return a {@link Resource} containing the desired result (or is empty)
	 */
	private Resource executeQuery(EClass eClass, IQuery query,
			Map<Object, Object> options) {
		if (eClass == null || query == null) {
			throw new IllegalStateException("Cannot get EObjects by query because of missing parameters: EClass: " + eClass + ", IQuery: " + query);
		}
		String queryString = MongoQueryBuilder.createMongoQuery(query);
		if (queryString != null && queryString.length() < 3) {
			queryString = "{ filter: " + queryString + " }";
		}
		queryString = URI.encodeQuery(queryString, true);
		URI uri = createMongoURI(eClass, options);
		uri = URI.createURI(uri.toString() + "/?" + queryString);
		org.eclipse.emf.ecore.resource.Resource resource = createResource(uri);
		try {
			resource.load(options);
		} catch (IOException e) {
			throw new IllegalStateException("Could not load EObjects by query with message " + e.getMessage(), e);
		}
		return resource;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.DefaultEMFRepository#setIDs(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected void setIDs(EObject rootObject) {
		RepositoryHelper.setIds(rootObject, () -> new ObjectId().toString(), () -> new ObjectId().toString());
	}
	
	/**
	 * REturn the final collection name
	 * @param eclass the eclass type
	 * @param options the load or save options
	 * @return the name or <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	private String getCollectionName(EClass eClass, Map<?, ?> options) {
		String collectionName = Options.getCollectionName(options);
		if (collectionName != null) {
			@SuppressWarnings("rawtypes")
			Map options2 = new HashMap();
			options2.put(Options.OPTION_FILTER_ECLASS, eClass);
			options2.put(Options.OPTION_STORE_SUPERTYPE, Boolean.TRUE);
			options.putAll(options2);
			return collectionName;
		} else {
			return eClass != null ? RepositoryHelper.getUriHintNameForEClass(eClass) : null;
		}
	}
	
	/**
	 * Creates a URI for the given {@link EClass} and properties
	 * @param eclass the {@link EClass} of the object to be stored
	 * @param options the save options
	 * @return the URI or <code>null</code>
	 */
	private URI createMongoURI(EClass eclass, Map<?, ?> options) {
		String collectionName = getCollectionName(eclass, options);
		if (collectionName != null) {
			return createEClassUri(collectionName, options);
		}
		return null;
	}
}
