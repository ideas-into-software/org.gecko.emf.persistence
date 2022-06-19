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
package org.gecko.emf.persistence.mongo.util;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.persistence.ConverterService;
import org.gecko.emf.persistence.input.InputContext;

import com.mongodb.client.FindIterable;

/**
 * 
 * @author mark
 * @since 19.06.2022
 */
public class MongoInputContext implements InputContext<FindIterable<EObject>> {
	
	private final FindIterable<EObject> iterable;
	private Map<Object, Object> options;
	
	/**
	 * Creates a new instance.
	 */
	public MongoInputContext(FindIterable<EObject> iterable, Map<Object, Object> options) {
		this.iterable = iterable;
		this.options = options;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.InputContext#getResult()
	 */
	@Override
	public FindIterable<EObject> getResult() {
		return iterable;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.InputContext#getOptions()
	 */
	@Override
	public Map<Object, Object> getOptions() {
		return options;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.InputContext#getLoadResource()
	 */
	@Override
	public Resource getLoadResource() {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.InputContext#getResourceCache()
	 */
	@Override
	public List<Resource> getResourceCache() {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.input.InputContext#getConverter()
	 */
	@Override
	public Optional<ConverterService> getConverter() {
		// TODO Auto-generated method stub
		return null;
	}

}
