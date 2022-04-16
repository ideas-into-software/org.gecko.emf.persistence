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
package org.gecko.emf.persistence;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Implementation of InputContentHandler that does not need a resource cache for the result
 * @param <RESULT> the result provider / iterator
 * @author Mark Hoffmann
 * @since 26.03.2022
 */
public abstract class UncachedInputContentHandler<RESULT> implements InputContentHandler<RESULT> {
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.api.InputContentHandler#enableResourceCache(java.util.Map)
	 */
	@Override
	public boolean enableResourceCache(Map<Object, Object> options) {
		return false;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.api.InputContentHandler#createContent(java.lang.Object, java.util.Map)
	 */
	@Override
	public EObject createContent(RESULT iterable, Map<Object, Object> options) {
		return createContent(iterable, options, null);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.api.InputContentHandler#createContent(java.lang.Object, java.util.Map, java.util.List)
	 */
	@Override
	public EObject createContent(RESULT iterable, Map<Object, Object> options,
			List<Resource> resourceCache) {
		return doCreateContent(iterable, options);
	}

	/**
	 * Method to be implemented for creating content
	 * @param iterable the result iterable
	 * @param options the the load options
	 * @return the content {@link EObject}
	 */
	public abstract EObject doCreateContent(RESULT iterable, Map<Object, Object> options);
}
