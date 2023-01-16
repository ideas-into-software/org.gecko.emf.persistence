/**
 * Copyright (c) 2012 - 2022 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 * 	   Bryan Hunt - initial API
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.jpa.query;

import static org.gecko.emf.persistence.jpa.JPAPersistenceConstants.PERSISTENCE_FILTER_PROP;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.persistence.dynamic.DynamicEntity;
import org.gecko.emf.persistence.api.QueryEngine;
import org.gecko.emf.utilities.Request;
import org.osgi.service.component.annotations.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * Implementation of a EMF JPA query engine
 * @author Mark Hoffmann
 * @since 03.07.2016
 */
@Component(name="JPAQueryEngine", immediate=true, service=QueryEngine.class, property = PERSISTENCE_FILTER_PROP)
public class JPAQueryEngine implements QueryEngine<Query, EntityManager> {
	
	public static final String QUERY_COUNT = "SELECT COUNT(c) FROM %s c";
	public static final String QUERY_ALL = "SELECT a FROM %s a";
	public static final String QUERY_ID = "SELECT id FROM %s id WHERE id.%s=%s";
	private EntityManager entityManager;
	
	
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.QueryEngine#buildQuery(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public Query buildQuery(URI uri) {
		return buildQuery(uri, Collections.emptyMap());
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.QueryEngine#buildQuery(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public Query buildQuery(URI uri, Map<?, ?> options) {
		Request request = (Request) options.get("request");
		return createQuery(request, options, this.entityManager);
	}
	
	/**
	 * @param request
	 * @param options
	 * @return
	 */
	private Query createQuery(Request request, Map<?, ?> options, EntityManager entityManager) {
		return executeAllQuery(entityManager, QUERY_COUNT, QUERY_ALL);
	}
	
	public Query executeCountQuery(EntityManager entityManager, String entityName, String feature) {
		Objects.requireNonNull(entityManager);
		Objects.requireNonNull(entityName);
		return entityManager.createQuery(String.format(QUERY_COUNT, entityName), DynamicEntity.class);
	}
	
	public Query executeAllQuery(EntityManager entityManager, String entityName, String feature) {
		Objects.requireNonNull(entityManager);
		Objects.requireNonNull(entityName);
		return entityManager.createQuery(String.format(QUERY_ALL, entityName), DynamicEntity.class);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.QueryEngine#buildQuery(org.eclipse.emf.common.util.URI, java.lang.Object)
	 */
	@Override
	public Query buildQuery(URI uri, EntityManager nativeEngine) {
		// TODO Auto-generated method stub
		return executeAllQuery(nativeEngine, QUERY_COUNT, QUERY_ALL);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.QueryEngine#buildQuery(org.eclipse.emf.common.util.URI, java.util.Map, java.lang.Object)
	 */
	@Override
	public Query buildQuery(URI uri, Map<?, ?> options, EntityManager nativeEngine) {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.QueryEngine#setNativeEngine(java.lang.Object)
	 */
	@Override
	public void setNativeEngine(EntityManager nativeEngine) {
		this.entityManager = nativeEngine;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.QueryEngine#getNativeEngine()
	 */
	@Override
	public Optional<EntityManager> getNativeEngine() {
		return Optional.ofNullable(entityManager);
	}

//	/**
//	 * @param mongoQuery
//	 * @param query
//	 * @param options
//	 */
//	private void buildProjectionOnly(EMongoQuery query, Document rawQuery, Map<?, ?> options) {
//		Document projection = (Document) rawQuery.get("projectionOnly");
//		projection = createEMFProjection(projection, options);
//		query.setProjectionOnly(projection);
//	}
//
//	/**
//	 * Builds the projection for the query
//	 * @param query the {@link EMongoQuery} object
//	 * @param rawQuery the parsed query {@link Document}
//	 * @param options the options map, can be <code>null</code>
//	 */
//	private void buildProjection(EMongoQuery query, Document rawQuery, Map<?, ?> options) {
//		Document projection = (Document) rawQuery.get("projection");
//		projection = createEMFProjection(projection, options);
//
//		if (projection == null && Boolean.TRUE.equals(options.get(Options.OPTION_LAZY_RESULT_LOADING))) {
//			projection = createLazyLoadingProjection(options);
//		}
//		query.setProjection(projection);
//	}

//	/**
//	 * Builds and extends a filter query, if the EClass filter is active 
//	 * @param query the {@link EMongoQuery}
//	 * @param rawQuery the parsed query {@link Document}
//	 * @param options the options map, can be <code>null</code>
//	 */
//	protected void buildFilter(Request query, Document rawQuery, Map<?, ?> options) {
//		EClass eClass = Options.getFilterEClass(options);
//		if (eClass !=null) {
//			filterQuery = createEClassTypeFilter(filterQuery, eClass, options);
//		}
//		query.setFilter(filterQuery);
//	}
	
//	/**
//	 * Checks the options for the batch size and sets it to the MongoDB query
//	 * @param query the query object
//	 * @param options the options {@link Map}, can be <code>null</code>
//	 */
//	protected void checkBatchSize(EMongoQuery query, Map<?, ?> options) {
//		if (options == null) {
//			return;
//		}
//		if (options.containsKey(Options.OPTION_BATCH_SIZE)) {
//			Object size = options.get(Options.OPTION_BATCH_SIZE);
//			if (size != null && size instanceof Integer) {
//				query.setBatchSize((Integer)size);
//			}
//		}
//	}

//	/**
//	 * Creates a EClass type filter for the _eClass and _superType fields. If there is an existing
//	 * filter, the type filter is prepended and the existing filter is ANDed to the type filter.
//	 * @param existingFilter an existing filter, can be <code>null</code>
//	 * @param eClass the EClass type to filter against. Can be <code>null</code>
//	 * @param options the options map. Can be <code>null</code>
//	 * @return the filter {@link Document}
//	 */
//	private BsonDocument createEClassTypeFilter(BsonDocument existingFilter, EClass eClass, Map<?, ?> options) {
//		if (eClass == null) {
//			return existingFilter;
//		}
//		boolean strict = options != null && Boolean.TRUE.equals(options.get(Options.OPTION_FILTER_ECLASS_STRICT));
//		String eClassKey = Options.getEClassKey(options);
//		String superTypeKey = Options.getSuperType(options);
//
//		
//		Bson filter = Filters.eq(eClassKey, EcoreUtil.getURI(eClass).toString());
//		if (!strict) {
//			Bson superTFilter = Filters.eq(superTypeKey, EcoreUtil.getURI(eClass).toString());
//			filter = Filters.or(filter, superTFilter);
//		}
//
//		if (existingFilter != null) {
//			filter = Filters.and(filter, existingFilter);
//		}
//		return filter.toBsonDocument();
//		
//	
//	}
	
//	/**
//	 * Creates a projection request document, that matches the EMFisms. That allows us to 
//	 * de-serialize the projected result into an EMF instance.
//	 * @return the {@link Document} representing
//	 */
//	private JdbcQuery createEMFProjection(JdbcQuery projection, Map<?, ?> options) {
//		if (projection == null) {
//			return null;
//		}
//		// Add type projection to each segment hierarchy
//		String eClassKey = Options.getEClassKey(options);
//		Set<String> typeProjections = ProjectionHelper.evaluateKeys(projection.keySet(), eClassKey);
//		projection.getProjection().add(Keywords.ID_KEY);
//		projection.put(eClassKey, 1);
//		// Add the additional type projections as well
//		typeProjections.forEach(tp->projection.put(tp, 1));
//		projection.put(Keywords.EXTRINSIC_ID_KEY, 1);
//		return projection;
//	}
	
}
