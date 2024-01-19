/**
 * Copyright (c) 2016 Data In Motion and others.
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

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Query interface with default queries.
 * @author Mark Hoffmann
 * @since 26.07.2017
 */
public interface IQuery {

	/**
	 * Returns the query string that is responsible for sorting
	 * @return the query string that is responsible for sorting
	 */
	public String getSortString();

	/**
	 * Returns the query string that is responsible for filtering
	 * @return the query string that is responsible for filtering
	 */
	public String getFilterString();

	/**
	 * Returns the query string that is responsible for limiting the result size
	 * @return the query string that is responsible for limiting the result size
	 */
	public String getLimitString();

	/**
	 * Returns the query string that is responsible for projection
	 * @return the query string that is responsible for projection
	 */
	public String getProjectionString();

	/**
	 * Sets the entry count limit
	 * @param limit
	 *            the entry count limit to set
	 */
	public void setLimit(long limit);

	/**
	 * Returns the entry limit count. Default value is -1 and means no limit
	 * @return the entry limit count.
	 */
	public long getLimit();

	/**
	 * Sets the entry skip count to start from
	 * @param skip
	 *            the entry skip count to start from
	 */
	public void setSkip(long skip);

	/**
	 * Returns the entry skip/offset count. Default value is -1 and means no
	 * skipping
	 * @return the entry skip/offset count.
	 */
	public long getSkip();

	/**
	 * Sets the sorting parameters
	 * @param field
	 *            the field to be sorted
	 * @param sortType
	 *            the sort type ascending or descending
	 */
	public void setSort(EAttribute field, SortType sortType);

	/**
	 * Returns the sort field or <code>null</code>
	 * @return the sort field or <code>null</code>
	 */
	public String getSortField();

	/**
	 * Returns the sort direction
	 * @return the sort direction
	 */
	public SortType getSortDirection();

	/**
	 * Returns the {@link EStructuralFeature} paths for the projection
	 * @return the {@link EStructuralFeature} paths for the projection
	 */
	public List<EStructuralFeature[]> getProjectionPaths();
}
