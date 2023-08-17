/**
 * Copyright (c) 2012 - 2023 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.repository.tests;

import java.util.List;

import org.gecko.emf.repository.query.AbstractQueryBuilder;
import org.gecko.emf.repository.query.IQuery;

/**
 * 
 * @author ilenia
 * @since Aug 17, 2023
 */
public class SimpleQueryBuilder extends AbstractQueryBuilder {
	
	public List<Object> getInValueList() {
		return super.getInValueList();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQueryBuilder#createRangeQuery()
	 */
	@Override
	protected IQuery createRangeQuery() {
		// TODO Auto-generated method stub
		
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQueryBuilder#createAllQuery()
	 */
	@Override
	protected IQuery createAllQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQueryBuilder#createAndQuery(org.gecko.emf.repository.query.IQuery[])
	 */
	@Override
	protected IQuery createAndQuery(IQuery... queries) {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQueryBuilder#createOrQuery(org.gecko.emf.repository.query.IQuery[])
	 */
	@Override
	protected IQuery createOrQuery(IQuery... queries) {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQueryBuilder#createValueQuery()
	 */
	@Override
	protected IQuery createValueQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQueryBuilder#createInQuery()
	 */
	@Override
	protected IQuery createInQuery() {
		// TODO Auto-generated method stub
		return null;
	}

}
