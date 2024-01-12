/**
 * Copyright (c) 2012 - 2017 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.mongo;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.mongodb.client.FindIterable;

/**
 * Implementation of InputContentHandler that does not need a resource cache for the result
 * @author Mark Hoffmann
 * @since 23.11.2017
 */
public abstract class UncachedInputContentHandler implements InputContentHandler {
	
	/* (non-Javadoc)
	 * @see org.gecko.emf.mongo.InputContentHandler#enableResourceCache(java.util.Map)
	 */
	@Override
	public boolean enableResourceCache(Map<Object, Object> options) {
		return false;
	}
	
	public abstract EObject doCreateContent(FindIterable<EObject> iterable, Map<Object, Object> options);

	/* (non-Javadoc)
	 * @see org.gecko.emf.mongo.InputContentHandler#createContent(com.mongodb.client.FindIterable, java.util.Map)
	 */
	@Override
	public EObject createContent(FindIterable<EObject> iterable, Map<Object, Object> options) {
		return createContent(iterable, options, null);
	}
	
	/* (non-Javadoc)
	 * @see org.gecko.emf.mongo.InputContentHandler#createContent(com.mongodb.client.FindIterable, java.util.Map, java.util.List)
	 */
	@Override
	public EObject createContent(FindIterable<EObject> iterable, Map<Object, Object> options,
			List<Resource> resourceCache) {
		return doCreateContent(iterable, options);
	}

}
