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
package org.gecko.emf.repository.mongo.query;

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.gecko.emf.mongo.Keywords;

/**
 * A small Helper building the projection String use by all the mongo queries
 * @author Juergen Albert
 * @since 27.07.2017
 */
public class ProjectionHelper {

	/**
	 * Creates a string that is for usage of projection in mongo queries
	 * @param references the list of references to be projected
	 * @return the projection string
	 */
	public static String createProjectionString(List<EStructuralFeature[]> references){
		if(references == null || references.size() == 0){
			return "";
		}
		StringBuilder builder = new StringBuilder();
		builder.append("{\"");
		builder.append(Keywords.ECLASS_KEY);
		builder.append("\":1,\"");
		builder.append(Keywords.EXTRINSIC_ID_KEY);
		builder.append("\":1");
		for(EStructuralFeature[] path : references){
			builder.append(",\"");
			StringBuilder pathBuilder = new StringBuilder();
			for (EStructuralFeature EStructuralFeature : path) {
				if(pathBuilder.length() != 0){
					pathBuilder.append('.');
				}
				pathBuilder.append(EStructuralFeature.getName());
			}
			builder.append(pathBuilder.toString());
			builder.append("\":1");
		}
		builder.append('}');
		return builder.toString();
	}
	
}
