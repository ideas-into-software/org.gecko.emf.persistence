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
import org.gecko.emf.repository.query.IQuery;
import org.gecko.emf.repository.query.OperatorQuery;

/**
 * Mongo query using oerators AND, OR, NOT
 * @author Mark Hoffmann
 * @since 12.04.2015
 */
public class MongoOperatorQuery extends OperatorQuery {

	public MongoOperatorQuery(OperatorType type, List<EStructuralFeature[]> projectionPaths, IQuery... queries) {
		super(type, projectionPaths, queries);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.repository.query.AbstractQuery#getFilterString()
	 */
	@Override
	public String getFilterString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		if (getType() != null && getQueries() != null && getQueries().length > 0) {
			switch (getType()) {
			case AND:
				builder.append(" ");
				builder.append("$and: ");
				break;
			case NOT:
				builder.append(" ");
				builder.append("$not: ");
				break;
			case OR:
				builder.append(" ");
				builder.append("$or: ");
				break;
			default:
				throw new UnsupportedOperationException("The type is currently not supported: " + getType());
			}
			builder.append("[ ");
			for (int i = 0; i < getQueries().length; i++) {
				String subQuery = getQueries()[i].getFilterString();
				if (i > 0 && !subQuery.isEmpty()) {
					builder.append(", ");
				}
				builder.append(subQuery);
			}
			builder.append(" ] ");
		}
		builder.append("}");
		return builder.toString();
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
