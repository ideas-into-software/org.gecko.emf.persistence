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
package org.gecko.emf.persistence.mapping;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.persistence.context.ResultContext;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Interface to handle 3rd party persistence input. The order can be handles using the service.rank or 
 * other service properties
 * @author Mark Hoffmann
 * @since 26.03.2022
 */
@ProviderType
public interface InputContentHandler<RESULT, MAPPER extends EObjectMapper> {
	/**
	 * Returns <code>true</code>, if a resource cache is needed
	 * @param options the load options map
	 * @return <code>true</code>, if a resource cache is needed
	 */
	public boolean enableResourceCache(Map<Object, Object> options);
	
	/**
	 * Returns <code>true</code>, if the handler can be used to create content for the {@link Resource}
	 * @param options the load soptions map
	 * @return <code>true</code>, if this handler can create content
	 */
	public boolean canHandle(Map<Object, Object> options);
	
	/**
	 * Creates the content from the given result
	 * @param context the input context for the query including the result
	 * @return the content as {@link EObject} or <code>null</code>
	 */
	public EObject createContent(ResultContext<RESULT, MAPPER> context);
	
}
