/**
 * Copyright (c) 2012 - 2022 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.helper;

import java.util.HashSet;
import java.util.Set;

/**
 * Helper class to support multi-hierarchy projection
 * @author Mark Hoffmann
 * @since 30.11.2018
 */
public class ProjectionHelper {
	
	/**
	 * Evaluates all projection data and adds a projection for all segments
	 * to the given type string to the projection
	 * @param data the projection data
	 * @param typeString the string for the type qualifier
	 * @return the set with additional projection or an empty set
	 */
	public static Set<String> evaluateKeys(Set<String> data, String typeString) {
		final Set<String> result = new HashSet<String>();
		data.forEach(s->createType(s, result, typeString));
		return result;
	}
	
	private static Set<String> createType(String key, Set<String> result, String typeString) {
		int idx = key.lastIndexOf(".");
		if (idx < 0) {
			return result;
		}
		String shortened = key.substring(0, idx);
		result.add(shortened + "." + typeString);
		createType(shortened, result, typeString);
		return result;
	}

}
