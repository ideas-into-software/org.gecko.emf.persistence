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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.gecko.emf.collection.CollectionFactory;
import org.gecko.emf.collection.EReferenceCollection;
import org.gecko.emf.persistence.api.Countable;
import org.gecko.emf.persistence.api.Deletable;
import org.gecko.emf.persistence.api.DeletableOlf;
import org.gecko.emf.persistence.api.Options;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.api.Updateable;
import org.gecko.emf.persistence.context.PersistenceContext;
import org.gecko.emf.persistence.context.QueryContext;
import org.gecko.emf.persistence.context.QueryContextBuilder;
import org.gecko.emf.persistence.context.ResultContext;
import org.gecko.emf.persistence.context.ResultContextBuilder;
import org.gecko.emf.persistence.helper.PersistenceHelper;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.mapping.InputContentHandler;
import org.gecko.emf.persistence.mapping.IteratorMapper;
import org.gecko.emf.persistence.resource.PersistenceResource;

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
	 * @see org.gecko.emf.persistence.engine.PersistenceEngine#getContext()
	 */
	@Override
	public PersistenceContext<DRIVER, QUERYTYPE, RESULTTYPE, QUERYENGINE, MAPPER> getContext() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @param inputContext
	 * @param persistenceInputStream
	 * @return
	 */
	protected abstract MAPPER createMapper(ResultContext<RESULTTYPE, MAPPER> inputContext);

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.api.Updateable#create(java.util.Map)
	 */
	@Override
	public void create(Map<String, Object> properties) throws PersistenceException {
		Resource resource = getResource();
		boolean needCache = true;
		Map<Object, Object> mergedOptions = getMergedOptions();
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
			QUERYTYPE query = queryEngine.buildQuery(uri, mergedOptions);
			qcb.query(query);
			if (countResults) {
				String countCol = idAttributeName == null ? getTypeColumn() : idAttributeName;
				qcb.column(countCol);
				QueryContext<DRIVER, QUERYTYPE, MAPPER> queryCtx = qcb.build();
				results = executeCount(queryCtx);
				// If returning counting result / mapping results as response value is active
				response.put(Options.OPTION_COUNT_RESPONSE, Long.valueOf(results));
			}
			// Step 1 - create query and execute it
			QueryContext<DRIVER, QUERYTYPE, MAPPER> queryCtx = qcb.build();


			RESULTTYPE result = executeQuery(queryCtx);

			// Step 2 - build input context to handle the result
			ResultContext<RESULTTYPE, MAPPER> resultContext = new ResultContextBuilder<RESULTTYPE, MAPPER>()
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
			handlerOptional.ifPresentOrElse((ch)->{
				EObject eResult = ch.createContent(resultContext);
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

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.api.Readable#read(java.util.Map)
	 */
	@Override
	public void read(Map<String, Object> properties) throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.api.Updateable#update(java.util.Map)
	 */
	@Override
	public void update(Map<String, Object> properties) throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.api.Deletable#delete(java.util.Map)
	 */
	@Override
	public boolean delete(Map<String, Object> properties) throws PersistenceException {
		// TODO Auto-generated method stub
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.api.Countable#count(java.util.Map)
	 */
	@Override
	public long count(Map<String, Object> properties) throws PersistenceException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.api.Countable#exists(java.util.Map)
	 */
	@Override
	public boolean exist(Map<String, Object> properties) throws PersistenceException {
		// TODO Auto-generated method stub
		return false;
	}
	
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
	 */
	protected void defaultHandleIteratorResult(final IteratorMapper mapper, Resource resource) {
		EReferenceCollection eCollection = CollectionFactory.eINSTANCE.createEReferenceCollection();
		InternalEList<EObject> values = (InternalEList<EObject>) eCollection.getValues();
		EList<EObject> contents = resource.getContents();
		Map<String, Object> mergedOptions = getMergedOptions();
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
			if (!countResults) {
				// If returning counting result / mapping results as response value is active
				response.put(Options.OPTION_COUNT_RESPONSE, Long.valueOf(mappedCount));
			}
		} catch (PersistenceException e) {
			mapper.close();
			e.printStackTrace();
		}
		contents.add(eCollection);
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
