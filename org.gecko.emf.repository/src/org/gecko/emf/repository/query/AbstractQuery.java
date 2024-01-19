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

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * An abstract query.
 * @author Mark Hoffmann
 * @since 26.07.2017
 */
public abstract class AbstractQuery implements IQuery {

	private long limit = -1;
	private long skip = -1;
	private EAttribute sortField = null;
	private SortType sortType = null;
	private List<EStructuralFeature[]> projectionPaths;

	public AbstractQuery(List<EStructuralFeature[]> projectionPaths) {
		this.projectionPaths = projectionPaths;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQuery#setLimit(long)
	 */
	@Override
	public void setLimit(long limit) {
		if (limit > 0) {
			this.limit = limit;
		} else {
			this.limit = -1;
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQuery#getLimit()
	 */
	@Override
	public long getLimit() {
		return limit;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQuery#setSkip(long)
	 */
	@Override
	public void setSkip(long skip) {
		if (skip > 0) {
			this.skip = skip;
		} else {
			this.skip = -1;
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQuery#getSkip()
	 */
	@Override
	public long getSkip() {
		return skip;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQuery#setSort(org.eclipse.emf.ecore.EAttribute, org.gecko.emf.repository.query.SortType)
	 */
	@Override
	public void setSort(EAttribute field, SortType sortType) {
		this.sortField = field;
		if (field == null) {
			sortType = null;
		} else {
			if (sortType == null) {
				throw new IllegalArgumentException("SortType is missing as parameter");
			} else {
				this.sortType = sortType;
			}
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQuery#getSortField()
	 */
	@Override
	public String getSortField() {
		return sortField != null ? sortField.getName() : null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQuery#getSortDirection()
	 */
	public SortType getSortDirection() {
		return sortType;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQuery#getFilterString()
	 */
	@Override
	public String getFilterString() {
		return "";
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQuery#getLimitString()
	 */
	@Override
	public String getLimitString() {
		return "";
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQuery#getSortString()
	 */
	@Override
	public String getSortString() {
		return "";
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQuery#getProjectionString()
	 */
	@Override
	public String getProjectionString() {
		return "";
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.IQuery#getProjectionPaths()
	 */
	@Override
	public List<EStructuralFeature[]> getProjectionPaths() {
		return projectionPaths;
	}

}
