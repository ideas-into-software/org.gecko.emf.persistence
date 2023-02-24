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
package org.gecko.emf.persistence.engine;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.gecko.emf.collection.CollectionFactory;
import org.gecko.emf.collection.ECollection;
import org.gecko.emf.collection.EReferenceCollection;
import org.gecko.emf.persistence.api.Countable;
import org.gecko.emf.persistence.api.Deletable;
import org.gecko.emf.persistence.api.Options;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.api.PrimaryKeyFactory;
import org.gecko.emf.persistence.api.QueryEngine;
import org.gecko.emf.persistence.api.Updateable;
import org.gecko.emf.persistence.context.QueryContext;
import org.gecko.emf.persistence.context.QueryContextBuilder;
import org.gecko.emf.persistence.context.ResultContext;
import org.gecko.emf.persistence.context.ResultContextBuilder;
import org.gecko.emf.persistence.engine.EngineContext.ActionType;
import org.gecko.emf.persistence.helper.EMFHelper;
import org.gecko.emf.persistence.helper.PersistenceHelper;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.mapping.InputContentHandler;
import org.gecko.emf.persistence.mapping.IteratorMapper;

/**
 * Persistence engine implementation that should handle a lot of common stuff
 * @author Mark Hoffmann
 * @since 14.02.2023
 */
public abstract class DefaultPersistenceEngine<DRIVER, MAPPER extends EObjectMapper, RESULTTYPE, QUERYTYPE, QUERYENGINE> extends BasicPersistenceEngine<DRIVER, MAPPER, RESULTTYPE, QUERYTYPE, QUERYENGINE> implements Updateable, org.gecko.emf.persistence.api.Readable, Countable, Deletable {

	private List<Resource> resourcesCache;

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.engine.PersistenceEngine#dispose()
	 */
	@Override
	public void dispose() {
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.api.Updateable#create(java.util.Map)
	 */
	@Override
	public void create(Map<Object, Object> properties) throws PersistenceException {
		EngineContext context = createEngineContext(properties, ActionType.CREATE); 
		Resource resource = context.resource();
		URI uri = context.uri();

		QueryContext<DRIVER, ?, MAPPER> inputContext = createQueryContextBuilder(context).build();
		if (resource.getContents().size() > 1 || resource.getContents().get(0) instanceof ECollection) {
			saveMultipleObjects(inputContext);
		} else {
			EObject eObject = resource.getContents().get(0);
			EAttribute idAttribute = eObject.eClass().getEIDAttribute();
			String uriId = getIDUriSegment(uri);
			if(idAttribute == null && context.useIdAttributeAsPrimaryKey()){
				throw new IllegalStateException("EObject has no ID Attribute to be used together with option " +  Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY);
			} 
			if(context.useIdAttributeAsPrimaryKey()){
				Object objectId = eObject.eGet(idAttribute);
				if(objectId != null){
					if(uriId == null || uriId.isEmpty()){
						resource.setURI(resource.getURI().trimSegments(1).appendSegment(objectId.toString()));
						uri = resource.getURI();
					}
				} else {
					if(uriId != null && !uriId.isEmpty()){
						eObject.eSet(idAttribute, EcoreUtil.createFromString(idAttribute.getEAttributeType(), uriId));
					} else {
						PrimaryKeyFactory mongoIdFactory = getPrimaryKeyFactoryMap().get(uri.trimSegments(uri.segmentCount() - 2).toString());
						Object newId = null;
						if (mongoIdFactory != null) {
							newId = mongoIdFactory.getNextId();
						} else {
							//								newId = new ObjectId();
							newId = createNativePrimaryKey();
						}
						uri = uri.trimSegments(1).appendSegment(newId.toString());
						resource.setURI(uri);
						eObject.eSet(idAttribute, EcoreUtil.createFromString(idAttribute.getEAttributeType(), newId.toString()));
					}
				}
			}
			saveSingleObject(inputContext);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.api.Readable#read(java.util.Map)
	 */
	@Override
	public void read(Map<Object, Object> properties) throws PersistenceException {
		EngineContext context = createEngineContext(properties, ActionType.READ);
		Map<Object,Object> mergedOptions = context.effectiveOptions();
		Resource resource = context.resource();
		URI uri = context.uri();
		Map<Object, Object> response = context.response();

		boolean needCache = true;
		// determine the input content handler
		Optional<InputContentHandler<RESULTTYPE, MAPPER>> handlerOptional = getContentHandler().stream()
				.filter((ch)->ch.canHandle((Map<Object, Object>) mergedOptions))
				.findFirst();
		if (handlerOptional.isPresent()) {
			needCache = handlerOptional.get().enableResourceCache((Map<Object, Object>) mergedOptions);
		}

		// We need to set up the XMLResource.URIHandler so that proxy URIs are handled properly.EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet);
		if(needCache){
			resourcesCache = new ArrayList<>(resource.getContents().size());
		} else {
			resourcesCache = null;
		}

		QueryContextBuilder<DRIVER, QUERYTYPE, MAPPER> qcb = createQueryContextBuilder(context);
		long results = -1l;
		EClass eClass = context.eClass();
		if (eClass == null && context.eClassURI() != null) {
			eClass = EMFHelper.getEClass(resource.getResourceSet(), context.eClassURI(), Collections.emptyMap());
		}
		String tableName = eClass != null ? PersistenceHelper.getElementNameLower(eClass) : getTable(context);
		String idAttributeName = context.idField();
		qcb.table(tableName);
		if (eClass != null) {
			qcb.eClass(eClass);
			EAttribute idAttribute = eClass.getEIDAttribute();
			idAttributeName = idAttribute == null ? context.typeField() : PersistenceHelper.getElementEMDName(idAttribute);
			qcb.idColumn(idAttributeName);
		}
		// Execute a count query to get a number of all matches for that query
		QueryEngine<QUERYTYPE,QUERYENGINE> queryEngine = getQueryEngine();
		QUERYTYPE query = queryEngine.buildQuery(uri, mergedOptions);
		qcb.query(query);
		if (context.countResponse()) {
			String countCol = idAttributeName == null ? context.typeField() : idAttributeName;
			qcb.column(countCol);
			QueryContext<DRIVER, QUERYTYPE, MAPPER> queryCtx = qcb.build();
			results = executeCount(queryCtx);
			// If returning counting result / mapping results as response value is active
			response.put(Options.READ_COUNT_RESPONSE, Long.valueOf(results));
		}
		// Step 1 - create query and execute it
		QueryContext<DRIVER, QUERYTYPE, MAPPER> queryCtx = qcb.build();


		RESULTTYPE result = executeRead(queryCtx);

		// Step 2 - build input context to handle the result
		ResultContext<RESULTTYPE, MAPPER> resultContext = new ResultContextBuilder<RESULTTYPE, MAPPER>()
				.result(result)
				.resource(resource)
				.options(mergedOptions)
				.resourceCache(resourcesCache)
				.converterService(getConverterService())
				.build();

		// Step 3 - create default mapper to create EObjects out of the result
		final MAPPER inputMapper = createMapper(resultContext);
		inputMapper.initialize();


		// Step 4 - handle results depending on the configuration
		handlerOptional.ifPresentOrElse((ch)->{
			EObject eResult = ch.createContent(resultContext);
			if (eResult != null) {
				resource.getContents().add(eResult);
			}
		}, ()->defaultHandleResult(inputMapper, resource, context));
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.api.Updateable#update(java.util.Map)
	 */
	@Override
	public void update(Map<Object, Object> properties) throws PersistenceException {
		create(properties);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.api.Deletable#delete(java.util.Map)
	 */
	@Override
	public boolean delete(Map<Object, Object> properties) throws PersistenceException {
		EngineContext context = createEngineContext(properties, ActionType.DELETE);
		URI uri = context.uri();
		Map<Object, Object> mergedOptions = context.effectiveOptions();
		QueryEngine<QUERYTYPE, QUERYENGINE> queryEngine = getQueryEngine();
		try {
			QueryContextBuilder<DRIVER, QUERYTYPE, MAPPER> qcb = QueryContext.createContextBuilder(context);
			DRIVER driver = getDriver(context).getValue();
			qcb.driver(driver);
			long results = -1l;
			EClass eClass = context.eClass();
			if (eClass == null && context.eClassURI() != null) {
				eClass = EMFHelper.getEClass(context.resource().getResourceSet(), context.eClassURI(), Collections.emptyMap());
			}
			String tableName = eClass != null ? PersistenceHelper.getElementNameLower(eClass) : getTable(context);
			String idAttributeName = context.idField();
			qcb.table(tableName);
			if (eClass != null) {
				qcb.eClass(eClass);
				EAttribute idAttribute = eClass.getEIDAttribute();
				idAttributeName = idAttribute == null ? context.typeField() : PersistenceHelper.getElementEMDName(idAttribute);
				qcb.idColumn(idAttributeName);
			}
			// Execute a count query to get a number of all matches for that query
			QUERYTYPE query = queryEngine.buildQuery(uri, mergedOptions);
			qcb.query(query);
			if (context.countResponse()) {
				String countCol = idAttributeName == null ? context.typeField() : idAttributeName;
				qcb.column(countCol);
				QueryContext<DRIVER, QUERYTYPE, MAPPER> queryCtx = qcb.build();
				results = executeDelete(queryCtx);
				// If returning counting result / mapping results as response value is active
				context.response().put(Options.READ_COUNT_RESPONSE, Long.valueOf(results));
			}
			return true;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return false;
	}

	/**
	 * @param properties
	 * @return
	 * @throws PersistenceException
	 */
	@Override
	public long count(Map<Object, Object> properties) throws PersistenceException {
		EngineContext context = createEngineContext(properties, ActionType.COUNT);
		try {
			QueryContextBuilder<DRIVER, QUERYTYPE, MAPPER> qcb = QueryContext.createContextBuilder(context);
			DRIVER driver = getDriver(context).getValue();
			qcb.driver(driver);
			String tableName = getTable(context);
			qcb.table(tableName);
			if (context.countIdField()) {
				if (context.idField() != null) {
					qcb.idColumn(context.idField());
				}
			}
			// Execute a count query to get a number of all matches for that query
			return executeCount(qcb.build());
		} catch (InvocationTargetException e) {
			throw new PersistenceException("Error counting data because of invocation targte exception:", e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new PersistenceException("Error counting data while interrupted:", e);
		} catch (Exception e) {
			if (e instanceof PersistenceException) {
				throw e;
			}
			throw new PersistenceException("Error counting data: ", e);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.api.Countable#exists(java.util.Map)
	 */
	@Override
	public boolean exist(Map<Object, Object> properties) throws PersistenceException {
		return count(properties) > 0;
	}

	/**
	 * Creates a mapper with the result context
	 * @param inputContext the input context
	 * @return the mapper instance
	 */
	protected abstract MAPPER createMapper(ResultContext<RESULTTYPE, MAPPER> inputContext);

	/**
	 * Method to create a native primary key
	 * @return the navive primary key object
	 */
	protected abstract Object createNativePrimaryKey();

	protected abstract String getIDUriSegment(URI uri);

	/**
	 * Executes the read request
	 * @param context the context to create a read request
	 * @return result object that contains the response data
	 */
	protected abstract RESULTTYPE executeRead(QueryContext<DRIVER, QUERYTYPE, MAPPER> context);

	/**
	 * Executes a count statement
	 * @param context the context with all information to execute the count
	 * @return number of elements
	 * @throws PersistenceException
	 */
	protected abstract long executeCount(QueryContext<DRIVER, QUERYTYPE, MAPPER> context) throws PersistenceException;

	/**
	 * Execute a delete statement
	 * @param context the context with all information to execute delete
	 * @return number of deleted elements
	 * @throws PersistenceException
	 */
	protected abstract long executeDelete(QueryContext<DRIVER, QUERYTYPE, MAPPER> context) throws PersistenceException;

	/**
	 * Saves many objects using bulk/batch operations
	 * @param inputContext the input context
	 * @throws PersistenceException thrown on errors during save
	 */
	protected abstract void saveMultipleObjects(QueryContext<DRIVER, ?, MAPPER> inputContext) throws PersistenceException;

	/**
	 * Saves a single object into a driver
	 * @param context the input context
	 * @throws PersistenceException thrown on errors during saving
	 */
	protected abstract void saveSingleObject(QueryContext<DRIVER, ?, MAPPER> context) throws PersistenceException;

	/**
	 * Default way to handle mapping of the results
	 * @param mapper the {@link EObject} mapper
	 * @param resource the loading resource
	 */
	protected void defaultHandleResult(final MAPPER mapper, Resource resource, EngineContext context) {
		if (mapper instanceof IteratorMapper) {
			defaultHandleIteratorResult((IteratorMapper) mapper, resource, context);
		}
	}

	/**
	 * Default way to handle mapping of the results
	 * @param mapper the {@link EObject} mapper
	 * @param resource the loading resource
	 */
	protected void defaultHandleIteratorResult(final IteratorMapper mapper, Resource resource, EngineContext context) {
		EReferenceCollection eCollection = CollectionFactory.eINSTANCE.createEReferenceCollection();
		InternalEList<EObject> values = (InternalEList<EObject>) eCollection.getValues();
		EList<EObject> contents = resource.getContents();
		Map<Object, Object> mergedOptions = getMergedOptions();
		Map<Object, Object> response = getResponse();
		// counter for all mapped elements
		long mappedCount = 0l;
		try {
			while (mapper.hasNext()){
				EObject dbObject = mapper.next();
				if(Boolean.TRUE.equals(mergedOptions.get(Options.READ_LAZY_RESULT_LOADING))){
					((InternalEObject) dbObject).eSetProxyURI(EcoreUtil.getURI(dbObject).appendQuery(null));
					detachEObject(dbObject);
				}
				if (Boolean.TRUE.equals(mergedOptions.get(Options.READ_READ_DETACHED))) {
					detachEObject(dbObject);
				}
				values.addUnique(dbObject);
				mappedCount++;
			}
			// write count results
			if (!context.countResponse()) {
				// If returning counting result / mapping results as response value is active
				response.put(Options.READ_COUNT_RESPONSE, Long.valueOf(mappedCount));
			}
		} catch (PersistenceException e) {
			mapper.close();
			e.printStackTrace();
		}
		contents.add(eCollection);
	}

	protected String getId(EngineContext context) {
		URI uri = context.uri();
		return uri.segmentCount() > 2 ? uri.segment(2) : uri.fragment();
	}

	/**
	 * Returns the {@link MongoCollection}. Clients may extend this. 
	 * The default extracts the collection name from the URI and appends the value from the 
	 * {@link Options#OPTIONS_COLLECTION_PARTITION_EXTENSION}, if it set.
	 * TODO put that into the get table part
	 * @param context the engine context
	 * @return
	 */
	protected String getTable(EngineContext context) {
		URI uri = context.uri();
		return uri.segmentCount() > 1 ? uri.segment(1) :  null;
	}

	protected List<Resource> getResourcesCache() {
		return resourcesCache;
	}

	/**
	 * Detaches the given {@link EObject}
	 * @param eobject the eobject instance
	 */
	private void detachEObject(EObject eobject) {
		if (eobject == null) {
			return;
		}
		Resource resource = eobject.eResource();
		if (resource != null) {
			resource.getContents().clear();
			if(resource.getResourceSet() != null){
				resource.getResourceSet().getResources().remove(resource);
			}
		}
	}

}
