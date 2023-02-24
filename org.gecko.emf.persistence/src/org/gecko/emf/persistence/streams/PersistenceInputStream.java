/**
 * Copyright (c) 2012 - 2022 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Byan Hunt -  initial API and implementation
 *     Ed Merks - initial API and implementation
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.streams;

import static org.gecko.emf.persistence.api.PersistenceConstants.ECLASS_TYPE_COLUMN_NAME;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
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
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.gecko.emf.collection.CollectionFactory;
import org.gecko.emf.collection.EReferenceCollection;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.CountableOld;
import org.gecko.emf.persistence.api.Options;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.api.QueryEngine;
import org.gecko.emf.persistence.codec.EClassProvider;
import org.gecko.emf.persistence.context.QueryContext;
import org.gecko.emf.persistence.context.QueryContextBuilder;
import org.gecko.emf.persistence.context.ResultContext;
import org.gecko.emf.persistence.context.ResultContextBuilder;
import org.gecko.emf.persistence.engine.PersistenceEngine;
import org.gecko.emf.persistence.helper.PersistenceHelper;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.mapping.InputContentHandler;
import org.gecko.emf.persistence.mapping.IteratorMapper;
import org.osgi.util.promise.Promise;

/**
 * Abstract input stream implementation that handles loading of {@link Resource}
 * @author Mark Hoffmann
 * @since 16.01.2023
 * @deprecated use {@link PersistenceEngine} instead
 */
public abstract class PersistenceInputStream<DRIVER, DRIVER_RAW, QT, RT, ENGINE, MAPPER extends EObjectMapper> extends InputStream implements URIConverter.Loadable, CountableOld, EClassProvider {

	private final ConverterService converterService;
	private URI uri;
	private Map<Object, Object> mergedOptions = new HashMap<>();
	private QueryEngine<QT, ENGINE> queryEngine;
	private Promise<DRIVER> driverPromise;
	private List<InputContentHandler<RT, MAPPER>> contentHandler;
	private Map<Object, Object> response;
	private String eClassUri;
	private String typeColumn;
	private boolean countOnly;
	private boolean countResults;
	private String idAttributeName;
	private boolean countIdAttributeFilter;
	private boolean countTypeFilter;
	private EClass eClass;
	private Map<String, EClass> eClassCache;
	private Resource resource;
	private List<Resource> resourcesCache;

	public PersistenceInputStream(ConverterService converterService, QueryEngine<QT, ENGINE>  queryEngine, Promise<DRIVER_RAW> driverRaw, List<InputContentHandler<RT, MAPPER>> contentHandler, URI uri, Map<?, ?> options, Map<Object, Object> response) throws PersistenceException {
		this.response = response;
		if (converterService == null)
			throw new NullPointerException("The converter service must not be null");
		this.converterService = converterService;
		if (driverRaw == null)
			throw new NullPointerException("The database connection must not be null");

		this.contentHandler = contentHandler == null ? Collections.emptyList() : contentHandler;
		this.queryEngine = queryEngine;
		this.driverPromise = configureDriver(driverRaw);
		this.uri = uri;
		normalizeOptions(options);
		readOptions(mergedOptions);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.URIConverter.Loadable#loadResource(org.eclipse.emf.ecore.resource.Resource)
	 */
	@Override
	public void loadResource(Resource resource) throws IOException {

		this.resource = resource;
		boolean needCache = true;

		// determine the input content handler
		Optional<InputContentHandler<RT, MAPPER>> handlerOptional = contentHandler.stream()
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
			QueryContextBuilder<DRIVER, QT, MAPPER> qcb = QueryContext.createContextBuilder(null);
			DRIVER driver = driverPromise.getValue();
			qcb.driver(driver);
			long results = -1l;

			if (eClass == null && eClassUri != null) {
				eClass = getEClass(resource.getResourceSet(), eClassUri);
			}
			String tableName = eClass != null ? PersistenceHelper.getElementNameLower(eClass) : getTable(uri, mergedOptions);
			qcb.table(tableName);
			if (eClass != null) {
				qcb.eClass(eClass);
				EAttribute idAttribute = eClass.getEIDAttribute();
				idAttributeName = idAttribute == null ? getTypeColumn() : PersistenceHelper.getElementEMDName(idAttribute);
				qcb.idColumn(idAttributeName);
			}
			// Execute a count query to get a number of all matches for that query
			QT query = queryEngine.buildQuery(uri, mergedOptions);
			qcb.query(query);
			if (countResults) {
				String countCol = idAttributeName == null ? getTypeColumn() : idAttributeName;
				qcb.column(countCol);
				QueryContext<DRIVER, QT, MAPPER> queryCtx = qcb.build();
				results = executeCount(queryCtx);
				// If returning counting result / mapping results as response value is active
				response.put(Options.READ_COUNT_RESPONSE, Long.valueOf(results));
			}
			// Step 1 - create query and execute it
			QueryContext<DRIVER, QT, MAPPER> queryCtx = qcb.build();


			RT result = executeQuery(queryCtx);

			// Step 2 - build input context to handle the result
			ResultContext<RT, MAPPER> resultContext = new ResultContextBuilder<RT, MAPPER>()
					.result(result)
					.resource(resource)
					.options(mergedOptions)
					.resourceCache(resourcesCache)
					.converterService(converterService)
					.build();

			// Step 3 - create default mapper to create EObjects out of the result
			final MAPPER inputMapper = createMapper(resultContext);
			inputMapper.initialize();


			// Step 4 - handle results depending on the configuration
			handlerOptional.ifPresentOrElse((ich)->{
				EObject eResult = ich.createContent(resultContext);
				if (eResult != null) {
					resource.getContents().add(eResult);
				}
			}, ()->defaultHandleResult(inputMapper, resource));

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

	/**
	 * @param driver
	 * @return
	 */
	protected Promise<DRIVER> configureDriver(Promise<DRIVER_RAW> driverRawPromise) {
		return driverRawPromise.map(dr->(DRIVER)dr);
	}

	/* 
	 * (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		// InputStream requires that we implement this function. It will never be called
		// since this implementation implements URIConverter.Loadable. The loadResource()
		// function will be called instead.
		return -1;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.Countable#count(org.eclipse.emf.common.util.URI, java.util.Map, java.util.Map)
	 */
	@Override
	public long count(URI uri, Map<?, ?> options, Map<Object, Object> response) {
		try {
			if (countIdAttributeFilter) {
				if (idAttributeName != null) {

				}
			}
			QueryContextBuilder<DRIVER, QT, MAPPER> qcb = QueryContext.createContextBuilder(null);
			DRIVER driver = driverPromise.getValue();
			qcb.driver(driver);
			String tableName = getTable(uri, mergedOptions);
			qcb.table(tableName);
			String idAttribute = getIdAttribute(uri, mergedOptions);
			qcb.idColumn(idAttribute != null ? idAttribute : getTypeColumn());
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
	 * @see org.gecko.emf.persistence.Countable#exists(org.eclipse.emf.common.util.URI, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean exists(URI uri, Map<?, ?> options, Map<Object, Object> response) throws PersistenceException {
		return count(uri, options, response) > 0;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.EClassProvider#getEClassFromResourceSet(org.eclipse.emf.ecore.resource.ResourceSet, java.lang.String)
	 */
	public EClass getEClassFromResourceSet(ResourceSet resourceSet, String eClassURI) {
		URI theUri = URI.createURI(eClassURI);
		String classifier = theUri.lastSegment();
		EPackage ePackage = resourceSet.getPackageRegistry().getEPackage(theUri.trimSegments(1).trimFragment().toString());
		if(ePackage != null) {
			EClassifier eClassifier = (EClassifier) ePackage.getEClassifier(classifier);
			if(eClassifier != null && eClassifier instanceof EClass) {
				return (EClass) eClassifier;
			}
		}

		return (EClass) resourceSet.getEObject(theUri, true);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.EClassProvider#getEClass(org.eclipse.emf.ecore.resource.ResourceSet, java.lang.String)
	 */
	public EClass getEClass(ResourceSet resourceSet, String eClassURI) {
		if (eClassCache != null) {
			synchronized (eClassCache) {
				EClass eClass = eClassCache.get(eClassURI);

				if (eClass == null) {
					eClass = getEClassFromResourceSet(resourceSet, eClassURI);
					eClassCache.put(eClassURI, eClass);
				}
				return eClass;
			}
		}

		return getEClassFromResourceSet(resourceSet, eClassURI);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.EClassProvider#getTypeColumn()
	 */
	public String getTypeColumn() {
		return typeColumn;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.EClassProvider#getIDColumn()
	 */
	@Override
	public String getIDColumn() {
		return idAttributeName;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.EClassProvider#getConfiguredEClass()
	 */
	@Override
	public EClass getConfiguredEClass() {
		return eClass;
	}

	/**
	 * Returns the converterService.
	 * @return the converterService
	 */
	public ConverterService getConverterService() {
		return converterService;
	}

	/**
	 * Returns the resource.
	 * @return the resource
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * Returns the resourcesCache.
	 * @return the resourcesCache
	 */
	public List<Resource> getResourcesCache() {
		return resourcesCache;
	}

	/**
	 * Returns the mergedOptions.
	 * @return the mergedOptions
	 */
	public Map<Object, Object> getMergedOptions() {
		return mergedOptions;
	}

	/**
	 * Returns the countResults.
	 * @return the countResults
	 * @deprecated Use {@link QueryContext#countResponse()} instead
	 */
	public boolean isCountResults() {
		return countResults;
	}
	/**
	 * @param inputContext
	 * @param persistenceInputStream
	 * @return
	 */
	protected abstract MAPPER createMapper(ResultContext<RT, MAPPER> inputContext);

	/**
	 * @param queryCtx
	 * @return
	 */
	protected abstract RT executeQuery(QueryContext<DRIVER, QT, MAPPER> queryCtx);

	/**
	 * Default way to handle mapping of the results
	 * @param mapper the {@link EObject} mapper
	 * @param resource the loading resource
	 */
	protected void defaultHandleResult(final MAPPER mapper, Resource resource) {
		if (mapper instanceof IteratorMapper) {
			defaultHandleIteratorResult((IteratorMapper) mapper, resource);
		}
	}

	/**
	 * Default way to handle mapping of the results
	 * @param mapper the {@link EObject} mapper
	 * @param resource the loading resource
	 * @deprecated use {@link PersistenceInputStream#defaultHandleIteratorResult(IteratorMapper, Resource)}
	 */
	protected void defaultHandleIteratorResultOld(final IteratorMapper mapper, Resource resource) {
		try {
			EList<EObject> contents = resource.getContents();
			// counter for all mapped elements
			long mappedCount = 0l;
			while (mapper.hasNext()) {
				EObject next = mapper.next();
				contents.add(next);
				mappedCount++;
			}
			// write count results
			if (!countResults) {
				// If returning counting result / mapping results as response value is active
				response.put(Options.READ_COUNT_RESPONSE, Long.valueOf(mappedCount));
			}
		} catch (PersistenceException e) {
			mapper.close();
			e.printStackTrace();
		}
	}

	/**
	 * Default way to handle mapping of the results
	 * @param mapper the {@link EObject} mapper
	 * @param resource the loading resource
	 */
	protected void defaultHandleIteratorResult(final IteratorMapper mapper, Resource resource) {
		EReferenceCollection eCollection = CollectionFactory.eINSTANCE.createEReferenceCollection();
		InternalEList<EObject> values = (InternalEList<EObject>) eCollection.getValues();
		EList<EObject> contents = resource.getContents();
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
			if (!countResults) {
				// If returning counting result / mapping results as response value is active
				response.put(Options.READ_COUNT_RESPONSE, Long.valueOf(mappedCount));
			}
		} catch (PersistenceException e) {
			mapper.close();
			e.printStackTrace();
		}
		contents.add(eCollection);
	}

	/**
	 * Executes a count statement
	 * @param emf the databse connection
	 * @param tableName the table name to count
	 * @param idAttribute an optional id attribute, to make count more performant
	 * @return the count result;
	 * @throws SQLException
	 */
	protected abstract long executeCount(QueryContext<DRIVER, QT, MAPPER> context) throws PersistenceException;

	protected abstract boolean isProjectionOnly(String query);

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
	 * Read some defaults
	 * @param mergedOptions
	 */
	private void readOptions(Map<Object, Object> mergedOptions) {
		eClass = (EClass) mergedOptions.getOrDefault(Options.CAP_ECLASS, null);
		eClassUri = (String) mergedOptions.getOrDefault(Options.CAP_ECLASS_URI, null);
		idAttributeName = (String) mergedOptions.getOrDefault(Options.CAP_ID_FIELD_NAME, null);
		typeColumn = (String) mergedOptions.getOrDefault(Options.CAP_FIELD_ECLASS_TYPE, ECLASS_TYPE_COLUMN_NAME);

		countIdAttributeFilter = Boolean.TRUE.equals(mergedOptions.getOrDefault(CountableOld.OPTION_COUNT_ID_ATTRIBUTE, false));
		countTypeFilter = Boolean.TRUE.equals(mergedOptions.getOrDefault(CountableOld.OPTION_COUNT_URI_FILTER, false));
		countOnly = false;//Boolean.TRUE.equals(mergedOptions.getOrDefault(Options.READ_COUNT_RESULT, false));
		countResults = false;//Boolean.TRUE.equals(mergedOptions.getOrDefault(Options.READ_COUNT_RESULT, false));
	}

	/**
	 * Normalizes the load options
	 * @param options the original options
	 */
	private <K extends Object, V extends Object> void normalizeOptions(Map<K, V> options) {
		mergedOptions.putAll(options);
		EClass filterEClass = (EClass) options.getOrDefault(Options.READ_FILTER_ECLASS, null);
		EClass collectionEClass = Options.getTableEClass(options);
		if (collectionEClass != null && filterEClass == null) {
			mergedOptions.put(Options.READ_FILTER_ECLASS, collectionEClass);
		}
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
