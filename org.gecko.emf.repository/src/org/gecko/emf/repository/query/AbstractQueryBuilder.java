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
package org.gecko.emf.repository.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Abstract implementation of the query builder.
 * @author Mark Hoffmann
 * @since 26.07.2017
 */
public abstract class AbstractQueryBuilder implements IQueryBuilder {

	private String columnName = null;
	private int queryType = -1;
	private Object startValue = null;
	private Object endValue = null;
	private Object objectValue = null;
	private long limit = -1;
	private long skip = -1;
	private IQuery[] queries = null;
	private EAttribute sortField;
	private SortType sortType;
	private boolean includeStart;
	private boolean includeEnd;
	private List<Object> inValueList = new LinkedList<>();
	private List<EStructuralFeature[]> projectionFilter = new LinkedList<>();

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#sort(org.eclipse.emf.ecore.EAttribute, org.gecko.emf.repository.query.SortType)
	 */
	@Override
	public IQueryBuilder sort(EAttribute field, SortType sortType) {
		if (field != null && sortType != null) {
			this.sortField = field;
			this.sortType = sortType;
		}
		return this;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#limit(long)
	 */
	@Override
	public IQueryBuilder limit(long maxEntries) {
		if (maxEntries > 0) {
			this.limit = maxEntries;
		} else {
			this.limit = -1;
		}
		return this;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#skip(long)
	 */
	@Override
	public IQueryBuilder skip(long skipEntries) {
		if (skipEntries > 0) {
			this.skip = skipEntries;
		} else {
			this.skip = -1;
		}
		return this;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#allQuery()
	 */
	@Override
	public IQueryBuilder allQuery() {
		queryType = QUERY_TYPE_ALL;
		return this;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#rangeQuery()
	 */
	@Override
	public IQueryBuilder rangeQuery() {
		queryType =QUERY_TYPE_RANGE;
		return this;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#startValue(java.lang.Object)
	 */
	@Override
	public IQueryBuilder startValue(Object startValue) {
		return startValue(startValue, false);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#startValue(java.lang.Object, boolean)
	 */
	@Override
	public IQueryBuilder startValue(Object startValue, boolean include) {
		this.startValue = startValue;
		this.includeStart = include;
		return this;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#endValue(java.lang.Object)
	 */
	@Override
	public IQueryBuilder endValue(Object endValue) {
		return endValue(endValue, false);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#endValue(java.lang.Object, boolean)
	 */
	@Override
	public IQueryBuilder endValue(Object endValue, boolean include) {
		this.endValue = endValue;
		this.includeEnd = include;
		return this;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#simpleValue(java.lang.Object)
	 */
	@Override
	public IQueryBuilder simpleValue(Object value) {
		if (value != null) {
			queryType = QUERY_TYPE_KEY_VALUE;
			this.objectValue = value;
		}
		return this;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#column(org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	public IQueryBuilder column(EAttribute attribute) {
		if (attribute != null) {
			return column(attribute.getName());
		}
		return this;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#column(java.lang.String)
	 */
	@Override
	public IQueryBuilder column(String columnName) {
		if (columnName != null) {
			this.columnName = columnName;
		}
		return this;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#and(org.gecko.emf.repository.query.IQuery[])
	 */
	@Override
	public IQueryBuilder and(IQuery...queries) {
		queryType = QUERY_TYPE_AND;
		this.queries = queries;
		return this;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#or(org.gecko.emf.repository.query.IQuery[])
	 */
	@Override
	public IQueryBuilder or(IQuery...queries) {
		queryType = QUERY_TYPE_OR;
		this.queries = queries;
		return this;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#in(java.util.Collection)
	 */
	@Override
	public IQueryBuilder in(Collection<Object> valueList) {
		queryType = QUERY_TYPE_IN;
		if (valueList != null && !valueList.isEmpty()) {
			inValueList.addAll(valueList);
		}
		return this;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#in(java.lang.String[])
	 */
	@Override
	public IQueryBuilder in(Object... values) {
		queryType = QUERY_TYPE_IN;
		if (values != null && values.length > 0) {
			inValueList.addAll(Arrays.asList(values));
		}
		return this;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQueryBuilder#build()
	 */
	@Override
	public IQuery build() {
		IQuery query = null;
		switch (queryType) {
			case QUERY_TYPE_ALL:
				query = createAllQuery();
				break;
			case QUERY_TYPE_RANGE:
				query = createRangeQuery();
				break;
			case QUERY_TYPE_AND:
				if (queries != null) {
					query = createAndQuery(queries);
				} else {
					throw new IllegalStateException("There is no inner operator query object for the AND query");
				}
				break;
			case QUERY_TYPE_OR:
				if (queries != null) {
					query = createOrQuery(queries);
				} else {
					throw new IllegalStateException("There is no inner operator query object for the OR query");
				}
				break;
			case QUERY_TYPE_KEY_VALUE:
				query = createValueQuery();
				break;
			case QUERY_TYPE_IN:
		        query = createInQuery();
		        break;
		}
		if (query != null && limit > 0) {
			query.setLimit(limit);
		}
		if (query != null && skip > 0) {
			query.setSkip(skip);
		}
		if (query != null && sortField != null && sortType != null) {
			query.setSort(sortField, sortType);
		}
		return query;
	}

	/**
	 * Returns the query type
	 * @return the query type
	 */
	protected int getQueryType() {
		return queryType;
	}

	/**
	 * Returns the start index 
	 * @return the start index 
	 */
	protected Object getStartValue() {
		return startValue;
	}

	/**
	 * Returns the end index 
	 * @return the end index 
	 */
	protected Object getEndValue() {
		return endValue;
	}

	/**
	 * Returns the name of the column name 
	 * @return the name of the column name
	 */
	protected String getColumnName() {
		return columnName;
	}

	/**
	 * Returns the inValueList
	 * @return the in value list
	 */
	protected List<Object> getInValueList() {
		return inValueList;
	}

	/**
	 * Returns the name of the object value 
	 * @return the name of the object value
	 */
	protected Object getObjectValue() {
		return objectValue;
	}

	/**
	 * Returns the {@link InnerOperatorQuery} or <code>null</code>
	 * @return the {@link InnerOperatorQuery} or <code>null</code>
	 */
	protected IQuery[] getOperatorQueries() {
		return queries;
	}

	/**
	 * @return the includeStart
	 */
	public boolean isIncludeStart() {
		return includeStart;
	}

	/**
	 * @return the includeEnd
	 */
	public boolean isIncludeEnd() {
		return includeEnd;
	}

	/* (non-Javadoc)
	 * @see de.dim.persistence.emf.api.query.IQueryBuilder#projectionPath(org.eclipse.emf.ecore.EStructuralFeature[])
	 */
	@Override
	public IQueryBuilder projectionPath(EStructuralFeature... referencePath) {
		projectionFilter.add(referencePath);
		return this;
	}

	/**
	 * @return the projection filter
	 */
	protected List<EStructuralFeature[]> getProjectionFilter(){
		return projectionFilter;
	}

	/**
	 * Creates a range query
	 * @return a range query
	 */
	protected abstract IQuery createRangeQuery();

	/**
	 * Creates an all query
	 * @return an all query
	 */
	protected abstract IQuery createAllQuery();

	/**
	 * Creates an AND Query from two sub queries
	 * @param queries the {@link IQuery} instances
	 * @return an AND Query from two sub queries
	 */
	protected abstract IQuery createAndQuery(IQuery...queries);

	/**
	 * Creates an OR Query from two sub queries
	 * @param queries the {@link IQuery} instances
	 * @return an OR Query from two sub queries
	 */
	protected abstract IQuery createOrQuery(IQuery...queries);

	/**
	 * Creates a query that matches a value
	 * @return a query that matches a value
	 */
	protected abstract IQuery createValueQuery();

	/**
	 * Creates a query that matches a list of values
	 */
	protected abstract IQuery createInQuery();

}
