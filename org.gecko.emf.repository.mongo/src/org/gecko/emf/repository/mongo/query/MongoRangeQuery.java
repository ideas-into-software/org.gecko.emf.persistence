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
package org.gecko.emf.repository.mongo.query;

import java.util.Date;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.gecko.emf.repository.query.RangeQuery;

import com.mongodb.QueryBuilder;

/**
 * Range query for Mongo EMF.
 * @author Mark Hoffmann
 * @since 12.04.2015
 */
public class MongoRangeQuery extends RangeQuery {

	public MongoRangeQuery(String column, Object startValue, Object endValue, boolean includeStartValue,
			boolean includeEndValue, List<EStructuralFeature[]> projectionPaths) throws ClassCastException {
		super(column, startValue, endValue, includeStartValue, includeEndValue, projectionPaths);
		if (startValue != null && !(startValue instanceof Date || 
				startValue instanceof Number ||
				startValue instanceof String)) {
			throw new ClassCastException("Only Date, Number and String parameters for startValue allowed: " + startValue);
		}
		if (endValue != null && !(endValue instanceof Date || 
				endValue instanceof Number ||
				startValue instanceof String)) {
			throw new ClassCastException("Only Date, Number and String parameters for endValue allowed: " + endValue);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQuery#getFilterString()
	 */
	@Override
	public String getFilterString() {
		String query = "";
		if (getColumn() == null || getStartValue() == null && getEndValue() == null) {
			return query;
		}
		return createRangeValue();
	}

	/**
	 * Creates the range query
	 * @return the range query
	 */
	private String createRangeValue() {
		QueryBuilder queryBuilder = QueryBuilder.start(getColumn());
		if (getStartValue() != null) {
			Object startValue = getStartValue();
			if (isIncludeStartValue()) {
				queryBuilder.greaterThanEquals(startValue);
			} else {
				queryBuilder.greaterThan(startValue);
			}
		}

		if (getEndValue() != null) {
			Object endValue = getEndValue();
			if (isIncludeEndValue()) {
				queryBuilder.lessThanEquals(endValue);
			} else {
				queryBuilder.lessThan(endValue);
			}
		}

		return queryBuilder.get().toString();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQuery#getProjectionString()
	 */
	@Override
	public String getProjectionString() {
		return ProjectionHelper.createProjectionString(getProjectionPaths());
	}
}
