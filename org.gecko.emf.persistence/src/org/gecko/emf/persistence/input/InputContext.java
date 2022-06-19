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
package org.gecko.emf.persistence.input;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.persistence.ConverterService;

/**
 * Context object that hold information that are needed to mpa data from the RESULT into {@link EObject}'s
 * @author mark
 * @since 17.06.2022
 */
public interface InputContext<RESULT> {
	
	public static <RESULT> InputContextBuilder<RESULT> createContextBuilder() {
		return new InputContextBuilder<RESULT>();
	}
	
	/**
	 * Returns the result object
	 * @return the result object
	 */
	RESULT getResult();
	
	/**
	 * Return the load options
	 * @return the load options or an empty {@link Map}
	 */
	Map<Object, Object> getOptions();
	
	/**
	 * Return the loading resource
	 * @return the loading resource
	 */
	Resource getLoadResource();
	
	/**
	 * Returns the resource cache if there is any
	 * @return the resource cache or <code>null</code>, if not available
	 */
	List<Resource> getResourceCache();
	
	/**
	 * Returns the converter service
	 * @return the converter service
	 */
	Optional<ConverterService> getConverter();

}
