/*******************************************************************************
 * Copyright (c) 2013 Bryan Hunt.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 *******************************************************************************/
package org.gecko.emf.mongo.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bson.Document;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.emf.mongo.Keywords;
import org.gecko.emf.mongo.Options;
import org.gecko.emf.mongo.QueryEngine;
import org.gecko.emf.mongo.model.EMongoQuery;
import org.gecko.emf.mongo.model.ModelFactory;
import org.gecko.emf.mongo.util.ProjectionHelper;
import org.osgi.service.component.annotations.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

/**
 * Implementation of a EMF Mongo query engine
 * @author Mark Hoffmann
 * @since 03.07.2016
 */
@Component(name="NativeQueryEngine", immediate=true, service=QueryEngine.class)
public class NativeQueryEngine implements QueryEngine {
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.QueryEngine#buildMongoQuery(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public EMongoQuery buildMongoQuery(URI uri) {
		return buildMongoQuery(uri, null);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.QueryEngine#buildMongoQuery(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public EMongoQuery buildMongoQuery(URI uri, Map<?, ?> options) {
		Document query = (Document) Document.parse(URI.decode(uri.query()));
		
		EMongoQuery mongoQuery = ModelFactory.eINSTANCE.createEMongoQuery();
		checkBatchSize(mongoQuery, options);
		buildFilter(mongoQuery, query, options);
		buildProjection(mongoQuery, query, options);
		buildProjectionOnly(mongoQuery, query, options);
		mongoQuery.setSort((Document) query.get("sort"));
		mongoQuery.setLimit((Integer) query.get("limit"));
		mongoQuery.setSkip((Integer) query.get("skip"));
		return mongoQuery;
	}
	
	/**
	 * @param mongoQuery
	 * @param query
	 * @param options
	 */
	private void buildProjectionOnly(EMongoQuery query, Document rawQuery, Map<?, ?> options) {
		Document projection = (Document) rawQuery.get("projectionOnly");
		projection = createEMFProjection(projection, options);
		query.setProjectionOnly(projection);
	}

	/**
	 * Builds the projection for the query
	 * @param query the {@link EMongoQuery} object
	 * @param rawQuery the parsed query {@link Document}
	 * @param options the options map, can be <code>null</code>
	 */
	private void buildProjection(EMongoQuery query, Document rawQuery, Map<?, ?> options) {
		Document projection = (Document) rawQuery.get("projection");
		projection = createEMFProjection(projection, options);

		if (projection == null && Boolean.TRUE.equals(options.get(Options.OPTION_LAZY_RESULT_LOADING))) {
			projection = createLazyLoadingProjection(options);
		}
		query.setProjection(projection);
	}

	/**
	 * Builds and extends a filter query, if the EClass filter is active 
	 * @param query the {@link EMongoQuery}
	 * @param rawQuery the parsed query {@link Document}
	 * @param options the options map, can be <code>null</code>
	 */
	protected void buildFilter(EMongoQuery query, Document rawQuery, Map<?, ?> options) {
		Document filterQuery = (Document) rawQuery.get("filter");
		EClass eClass = Options.getFilterEClass(options);
		if (eClass !=null) {
			filterQuery = createEClassTypeFilter(filterQuery, eClass, options);
		}
		query.setFilter(filterQuery);
	}
	
	/**
	 * Checks the options for the batch size and sets it to the MongoDB query
	 * @param query the query object
	 * @param options the options {@link Map}, can be <code>null</code>
	 */
	protected void checkBatchSize(EMongoQuery query, Map<?, ?> options) {
		if (options == null) {
			return;
		}
		if (options.containsKey(Options.OPTION_BATCH_SIZE)) {
			Object size = options.get(Options.OPTION_BATCH_SIZE);
			if (size != null && size instanceof Integer) {
				query.setBatchSize((Integer)size);
			}
		}
	}

	/**
	 * Creates a EClass type filter for the _eClass and _superType fields. If there is an existing
	 * filter, the type filter is prepended and the existing filter is ANDed to the type filter.
	 * @param existingFilter an existing filter, can be <code>null</code>
	 * @param eClass the EClass type to filter against. Can be <code>null</code>
	 * @param options the options map. Can be <code>null</code>
	 * @return the filter {@link Document}
	 */
	@SuppressWarnings("unchecked")
	private Document createEClassTypeFilter(Document existingFilter, EClass eClass, Map<?, ?> options) {
		if (eClass == null) {
			return existingFilter;
		}
		boolean strict = options != null && Boolean.TRUE.equals(options.get(Options.OPTION_FILTER_ECLASS_STRICT));
		String eClassKey = Options.getEClassKey(options);
		String superTypeKey = Options.getSuperType(options);
		DBObject typeFilter = QueryBuilder.
				start(eClassKey).
				is(EcoreUtil.getURI(eClass).toString()).get();
		if (!strict) {
			DBObject superTypeFilter = QueryBuilder.
					start(superTypeKey).
					is(EcoreUtil.getURI(eClass).toString()).get();
			typeFilter = QueryBuilder.
					start().
					or(typeFilter, superTypeFilter).get();
		}
		if (existingFilter != null) {
			DBObject combinedFilter = QueryBuilder.start().
					and(typeFilter, new BasicDBObject(existingFilter)).get();
			return new Document(combinedFilter.toMap());
		} else {
			return new Document(typeFilter.toMap());
		}
	}
	
	/**
	 * Creates a projection request document, that matches the EMFisms. That allows us to 
	 * de-serialize the projected result into an EMF instance.
	 * @return the {@link Document} representing
	 */
	private Document createEMFProjection(Document projection, Map<?, ?> options) {
		if (projection == null) {
			return null;
		}
		// Add type projection to each segment hierarchy
		String eClassKey = Options.getEClassKey(options);
		Set<String> typeProjections = ProjectionHelper.evaluateKeys(projection.keySet(), eClassKey);
		projection.put(Keywords.ID_KEY, 1);
		projection.put(eClassKey, 1);
		// Add the additional type projections as well
		typeProjections.forEach(tp->projection.put(tp, 1));
		projection.put(Keywords.EXTRINSIC_ID_KEY, 1);
		return projection;
	}
	
	/**
	 * Creates a projection request document for lazy loading. It just projects against the 
	 * @return the {@link Document} representing the projection definition
	 */
	private Document createLazyLoadingProjection(Map<?, ?> options) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Keywords.ID_KEY, 1);
		String eClassKey = Options.getEClassKey(options);
		map.put(eClassKey, 1);
		map.put(Keywords.EXTRINSIC_ID_KEY, 1);
		Document proxyProjection = new Document(map);
		return proxyProjection;
	}
}
