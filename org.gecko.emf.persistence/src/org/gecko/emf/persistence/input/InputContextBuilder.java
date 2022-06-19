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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.persistence.ConverterService;

/**
 * 
 * @author mark
 * @since 19.06.2022
 */
public class InputContextBuilder<RESULT> {

	private RESULT result;
	private Map<Object, Object> options;
	private Resource resource;
	private List<Resource> resourceCache;
	private ConverterService converter;

	public InputContextBuilder<RESULT> result(RESULT result) {
		Objects.requireNonNull(result);
		this.result = result;
		return this;
	}
	
	public InputContextBuilder<RESULT> options(Map<Object, Object> options) {
		this.options = options == null ? Collections.emptyMap() : options;
		return this;
	}
	
	public InputContextBuilder<RESULT> resource(Resource resource) {
		Objects.requireNonNull(resource);
		this.resource = resource;
		return this;
	}
	
	public InputContextBuilder<RESULT> resourceCache(List<Resource> resourceCache) {
		this.resourceCache = resourceCache == null ? Collections.emptyList() : resourceCache;
		return this;
	}
	
	public InputContextBuilder<RESULT> converterService(ConverterService converter) {
		this.converter = converter;
		return this;
	}
	
	protected void verifyBuild() {
		Objects.requireNonNull(getResource());
		Objects.requireNonNull(getResult());
	}
	
	public InputContext<RESULT> build() {
		verifyBuild();
		return new InputContext<>() {
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.input.InputContext#getLoadResource()
			 */
			@Override
			public Resource getLoadResource() {
				return getResource();
			}
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.input.InputContext#getOptions()
			 */
			@Override
			public Map<Object, Object> getOptions() {
				return options == null ? Collections.emptyMap() : options;
			}
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.input.InputContext#getResourceCache()
			 */
			@Override
			public List<Resource> getResourceCache() {
				return resourceCache == null ? Collections.emptyList() : resourceCache;
			}
			
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.input.InputContext#getResult()
			 */
			@Override
			public RESULT getResult() {
				return result;
			}
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.input.InputContext#getConverter()
			 */
			@Override
			public Optional<ConverterService> getConverter() {
				return converter == null ? Optional.empty() : Optional.of(converter);
			}
		};
	}

	/**
	 * Returns the result.
	 * @return the result
	 */
	public RESULT getResult() {
		return result;
	}

	/**
	 * Returns the options.
	 * @return the options
	 */
	public Map<Object, Object> getOptions() {
		return options;
	}

	/**
	 * Returns the resource.
	 * @return the resource
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * Returns the resourceCache.
	 * @return the resourceCache
	 */
	public List<Resource> getResourceCache() {
		return resourceCache;
	}

}
