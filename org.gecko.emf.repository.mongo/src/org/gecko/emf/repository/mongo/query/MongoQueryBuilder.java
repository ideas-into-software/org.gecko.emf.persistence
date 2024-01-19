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

import org.gecko.emf.repository.query.AbstractQueryBuilder;
import org.gecko.emf.repository.query.IQuery;
import org.gecko.emf.repository.query.IQueryBuilder;
import org.gecko.emf.repository.query.OperatorQuery.OperatorType;

/**
 * Implementation of the {@link IQueryBuilder} for mongo queries.
 * @author Mark Hoffmann
 * @since 12.04.2015
 */
public class MongoQueryBuilder extends AbstractQueryBuilder {


	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQueryBuilder#createRangeQuery()
	 */
	@Override
	protected IQuery createRangeQuery() {
		return new MongoRangeQuery(getColumnName(), getStartValue(), getEndValue(), isIncludeStart(), isIncludeEnd(), getProjectionFilter());
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQueryBuilder#createAllQuery()
	 */
	@Override
	protected IQuery createAllQuery() {
		return new MongoAllQuery(getProjectionFilter());
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQueryBuilder#createValueQuery()
	 */
	@Override
	protected IQuery createValueQuery() {
		return new MongoValueQuery(getColumnName(), getObjectValue(), getProjectionFilter());
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQueryBuilder#createAndQuery(org.gecko.emf.repository.query.IQuery[])
	 */
	@Override
	protected IQuery createAndQuery(IQuery... queries) {
		return new MongoOperatorQuery(OperatorType.AND, getProjectionFilter(), queries);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQueryBuilder#createOrQuery(org.gecko.emf.repository.query.IQuery[])
	 */
	@Override
	protected IQuery createOrQuery(IQuery... queries) {
		return new MongoOperatorQuery(OperatorType.OR, getProjectionFilter(), queries);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQueryBuilder#createInQuery()
	 */
	@Override
	protected IQuery createInQuery() {
		return new MongoInQuery(getColumnName(), getInValueList(), getProjectionFilter());
	}

	/**
	 * Creates a mongo like query form a given query object
	 * @param query the query object
	 * @return the query string from the query object
	 */
	public static String createMongoQuery(IQuery query) {
		if (query == null) {
			return "{filter: {}}";
		}
		StringBuilder queryString = new StringBuilder();
		queryString.append("{filter: ");
		String filterString = query.getFilterString();
		if (filterString != null && filterString.length() > 0) {
			queryString.append(filterString);
		} else {
			queryString.append("{}");
		}
		if (query.getSkip() > 0) {
			queryString.append(", skip: " + query.getSkip());
		}
		if (query.getLimit() > 0) {
			queryString.append(", limit: " + query.getLimit());
		}
		if (query.getSortField() != null) {
			queryString.append(", sort: {");
			queryString.append(query.getSortField() + ": " + query.getSortDirection().getValue());
			queryString.append("}");
		}
		String projectionString = query.getProjectionString();
		if(projectionString != null && !projectionString.isEmpty()){
			queryString.append(", projection: " + projectionString);
		}
		queryString.append("}");
		return queryString.toString(); 
	}

}
