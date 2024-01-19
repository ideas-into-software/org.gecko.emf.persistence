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

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.gecko.emf.repository.query.AbstractQuery;

import com.mongodb.QueryBuilder;

/**
 * <p>An in-query for Mongo EMF.</p>
 * @author Mark Hoffmann
 * @since 16.05.2017
 */
public class MongoInQuery extends AbstractQuery {

	private final String column;
	private final List<Object> valueList;

	public MongoInQuery(String column, List<Object> objectList, List<EStructuralFeature[]> projectionPaths) {
		super(projectionPaths);
		this.column = column;
		valueList = objectList == null ? Collections.emptyList() : objectList;
	}

	public String getColumn() {
		return column;
	}

	public List<Object> getValueList() {
		return valueList;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQuery#getFilterString()
	 */
	@Override
	public String getFilterString() {
		String query = "";
		if (getColumn() == null || getValueList().isEmpty()) {
			return query;
		}
		return createInValue();
	}

	/**
	 * Creates the range query using the MongoDB Query Builder
	 * @return the range query
	 */
	private String createInValue() {
		QueryBuilder queryBuilder = QueryBuilder.start(getColumn());
		if (!getValueList().isEmpty()) {
			queryBuilder.in(getValueList());
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
