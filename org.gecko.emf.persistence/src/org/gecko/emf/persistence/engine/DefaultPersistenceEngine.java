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

import static org.gecko.emf.persistence.api.PersistenceConstants.ECLASS_TYPE_COLUMN_NAME;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import org.gecko.emf.persistence.api.ConverterService;
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
import org.gecko.emf.persistence.helper.EMFHelper;
import org.gecko.emf.persistence.helper.PersistenceHelper;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.mapping.InputContentHandler;
import org.gecko.emf.persistence.mapping.IteratorMapper;
import org.osgi.util.promise.Promise;

/**
 * 
 * @author mark
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
		// TODO Auto-generated method stub
		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.api.Updateable#create(java.util.Map)
	 */
	@Override
	public void create(Map<Object, Object> properties) throws PersistenceException {
		EngineContext context = createContext(properties); 
		Resource resource = context.resource();
		URI uri = context.uri();
		
		try {
			DRIVER d = getDriver().getValue();

//			MAPPER mapper = createMapper(resultcontext);
			QueryContext<DRIVER, ?, MAPPER> inputContext = QueryContext.<DRIVER, Object, MAPPER>createContextBuilder().
					converterService(getConverter()).
//					mapper(mapper).
					driver(d).
					resource(resource).
					build();
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
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.api.Readable#read(java.util.Map)
	 */
	@Override
	public void read(Map<Object, Object> properties) throws PersistenceException {
		EngineContext context = createContext(properties);
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

		try {
			QueryContextBuilder<DRIVER, QUERYTYPE, MAPPER> qcb = new QueryContextBuilder<>();
			DRIVER driver = getDriver().getValue();
			qcb.driver(driver);
			long results = -1l;
			EClass eClass = context.eClass();
			if (eClass == null && context.eClassURI() != null) {
				eClass = EMFHelper.getEClass(resource.getResourceSet(), context.eClassURI(), Collections.emptyMap());
			}
			String tableName = eClass != null ? PersistenceHelper.getElementNameLower(eClass) : getTable(uri, mergedOptions);
			String idAttributeName = context.idAttributeName();
			qcb.table(tableName);
			if (eClass != null) {
				qcb.eClass(eClass);
				EAttribute idAttribute = eClass.getEIDAttribute();
				idAttributeName = idAttribute == null ? context.typeColumn() : PersistenceHelper.getElementEMDName(idAttribute);
				qcb.idColumn(idAttributeName);
			}
			// Execute a count query to get a number of all matches for that query
			QueryEngine<QUERYTYPE,QUERYENGINE> queryEngine = getContext().getQueryEngine();
			QUERYTYPE query = queryEngine.buildQuery(uri, mergedOptions);
			qcb.query(query);
			if (context.countResults()) {
				String countCol = idAttributeName == null ? context.typeColumn() : idAttributeName;
				qcb.column(countCol);
				QueryContext<DRIVER, QUERYTYPE, MAPPER> queryCtx = qcb.build();
				results = executeCount(queryCtx);
				// If returning counting result / mapping results as response value is active
				response.put(Options.OPTION_COUNT_RESPONSE, Long.valueOf(results));
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
					.converterService(getConverter())
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

		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		EngineContext context = createContext(properties);
		URI uri = context.uri();
		Map<Object, Object> mergedOptions = context.effectiveOptions();
		QueryEngine<QUERYTYPE, QUERYENGINE> queryEngine = getQueryEngine();
		try {
			QueryContextBuilder<DRIVER, QUERYTYPE, MAPPER> qcb = new QueryContextBuilder<>();
			DRIVER driver = getDriver().getValue();
			qcb.driver(driver);
			long results = -1l;
			EClass eClass = context.eClass();
			if (eClass == null && context.eClassURI() != null) {
				eClass = EMFHelper.getEClass(context.resource().getResourceSet(), context.eClassURI(), Collections.emptyMap());
			}
			String tableName = eClass != null ? PersistenceHelper.getElementNameLower(eClass) : getTable(uri, mergedOptions);
			String idAttributeName = context.idAttributeName();
			qcb.table(tableName);
			if (eClass != null) {
				qcb.eClass(eClass);
				EAttribute idAttribute = eClass.getEIDAttribute();
				idAttributeName = idAttribute == null ? context.typeColumn() : PersistenceHelper.getElementEMDName(idAttribute);
				qcb.idColumn(idAttributeName);
			}
			// Execute a count query to get a number of all matches for that query
			QUERYTYPE query = queryEngine.buildQuery(uri, mergedOptions);
			qcb.query(query);
			if (context.countResults()) {
				String countCol = idAttributeName == null ? context.typeColumn() : idAttributeName;
				qcb.column(countCol);
				QueryContext<DRIVER, QUERYTYPE, MAPPER> queryCtx = qcb.build();
				results = executeDelete(queryCtx);
				// If returning counting result / mapping results as response value is active
				context.response().put(Options.OPTION_COUNT_RESPONSE, Long.valueOf(results));
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
		EngineContext context = createContext(properties);
		Map<Object, Object> mergedOptions = getMergedOptions();
		Resource resource = getResource();
		URI uri = resource.getURI();
		try {
			if (context.countIdAttributeFilter()) {
				if (context.idAttributeName() != null) {

				}
			}
			QueryContextBuilder<DRIVER, QUERYTYPE, MAPPER> qcb = new QueryContextBuilder<>();
			DRIVER driver = getDriver().getValue();
			qcb.driver(driver);
			String tableName = getTable(uri, mergedOptions);
			qcb.table(tableName);
			String idAttribute = getIdAttribute(uri, mergedOptions);
			qcb.idColumn(idAttribute != null ? idAttribute : context.typeColumn());
			// Execute a count query to get a number of all matches for that query
			return executeCount(qcb.build());
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return -1;
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
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
	 * Returns the driver promise
	 * @return the driver promise
	 */
	protected abstract Promise<DRIVER> getDriver();

	/**
	 * Returns the converter service
	 * @return the converter service
	 */
	protected abstract ConverterService getConverter();
	
	/**
	 * Returns the map with {@link PrimaryKeyFactory} for certain URI's
	 * @return the map with {@link PrimaryKeyFactory} for certain URI's
	 */
	protected abstract Map<String, PrimaryKeyFactory> getPrimaryKeyFactoryMap();
	
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
	 */
	protected abstract long executeDelete(QueryContext<DRIVER, QUERYTYPE, MAPPER> context);

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
				if(Boolean.TRUE.equals(mergedOptions.get(Options.OPTION_LAZY_RESULT_LOADING))){
					((InternalEObject) dbObject).eSetProxyURI(EcoreUtil.getURI(dbObject).appendQuery(null));
					detachEObject(dbObject);
				}
				if (Boolean.TRUE.equals(mergedOptions.get(Options.OPTION_READ_DETACHED))) {
					detachEObject(dbObject);
				}
				values.addUnique(dbObject);
				mappedCount++;
			}
			// write count results
			if (!context.countResults()) {
				// If returning counting result / mapping results as response value is active
				response.put(Options.OPTION_COUNT_RESPONSE, Long.valueOf(mappedCount));
			}
		} catch (PersistenceException e) {
			mapper.close();
			e.printStackTrace();
		}
		contents.add(eCollection);
	}

	protected String getId(URI uri, Map<?, ?> options) {
		return uri.segmentCount() > 2 ? uri.segment(2) : null;
	}

	protected String getTable(URI uri, Map<?, ?> options) {
		return uri.segment(1);
	}

	protected String getIdAttribute(URI uri, Map<?, ?> options) {
		return uri.fragment();
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
	
	EngineContext createContext(Map<Object, Object> options) {
		Map<Object, Object> effectiveOptions = new HashMap<>();
		if (options != null) {
			effectiveOptions.putAll(options);
		}
		effectiveOptions.putAll(getMergedOptions());
		
		EClass eClass = (EClass) effectiveOptions.getOrDefault(Options.OPTION_ECLASS_HINT, null);
		String eClassUri = (String) effectiveOptions.getOrDefault(Options.OPTION_ECLASS_URI_HINT, null);
		String idAttributeName = (String) effectiveOptions.getOrDefault(Options.OPTION_ECLASS_IDATTRIBUTE_HINT, null);
		String typeColumn = (String) effectiveOptions.getOrDefault(Options.OPTION_KEY_ECLASS_URI, ECLASS_TYPE_COLUMN_NAME);
		
		Boolean useIdAttributeAsPrimaryKey = Boolean.TRUE.equals(options.get(Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY));
		Boolean forceInsert = Boolean.TRUE.equals(options.get(Options.OPTION_FORCE_INSERT));
		Boolean clearResourceAfterInsert = !options.containsKey(Options.OPTION_CLEAR_RESOURCE_AFTER_BATCH_INSERT) || Boolean.TRUE.equals(options.get(Options.OPTION_CLEAR_RESOURCE_AFTER_BATCH_INSERT));

		Boolean countIdAttributeFilter = Boolean.TRUE.equals(effectiveOptions.getOrDefault(Countable.OPTION_COUNT_ID_ATTRIBUTE, false));
		Boolean countTypeFilter = Boolean.TRUE.equals(effectiveOptions.getOrDefault(Countable.OPTION_COUNT_URI_FILTER, false));
		Boolean countOnly = Boolean.TRUE.equals(effectiveOptions.getOrDefault(Options.OPTION_COUNT_RESULT, false));
		Boolean countResults = Boolean.TRUE.equals(effectiveOptions.getOrDefault(Options.OPTION_COUNT_RESULT, false));
		String eClassUriHint = (String) effectiveOptions.getOrDefault(Options.OPTION_ECLASS_URI_HINT, null);

		return new EngineContext() {
			
			@Override
			public URI uri() {
				return getResource() != null ? getResource().getURI() : null;
			}
			
			@Override
			public String typeColumn() {
				return typeColumn;
			}
			
			@Override
			public Map<Object, Object> response() {
				return getResponse();
			}
			
			@Override
			public Resource resource() {
				return getResource();
			}
			
			@Override
			public String idAttributeName() {
				return idAttributeName;
			}
			
			@Override
			public Map<Object, Object> effectiveOptions() {
				return getMergedOptions();
			}
			
			@Override
			public String eClassURIHint() {
				return eClassUriHint;
			}
			
			@Override
			public String eClassURI() {
				return eClassUri;
			}
			
			@Override
			public EClass eClass() {
				return eClass;
			}
			
			@Override
			public boolean countTypeFilter() {
				return countTypeFilter;
			}
			
			@Override
			public boolean countResults() {
				return countResults;
			}
			
			@Override
			public boolean countOnly() {
				return countOnly;
			}
			
			@Override
			public boolean countIdAttributeFilter() {
				return countIdAttributeFilter;
			}

			@Override
			public boolean useIdAttributeAsPrimaryKey() {
				return useIdAttributeAsPrimaryKey;
			}

			@Override
			public boolean forceInsert() {
				return forceInsert;
			}

			@Override
			public boolean clearResourceAfterInsert() {
				return clearResourceAfterInsert;
			}
		};
	}
}
