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

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.gecko.emf.repository.query.AbstractQuery;

/**
 * Mongo get-all query.
 * @author Mark Hoffmann
 * @since 27.07.2017
 */
public class MongoAllQuery extends AbstractQuery {

	public MongoAllQuery(List<EStructuralFeature[]> projectionPaths) {
		super(projectionPaths);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQuery#getFilterString()
	 */
	@Override
	public String getFilterString() {
		return "{}";
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
