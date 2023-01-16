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
package org.gecko.emf.persistence.context;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.mapping.EObjectMapper;

/**
 * Builder for the {@link ResultContext}
 * @author Mark Hoffmann
 * @since 19.06.2022
 */
public class ResultContextBuilder<RESULT, MAPPER extends EObjectMapper> {

	private RESULT result;
	private MAPPER mapper;
	private Map<Object, Object> options;
	private Resource resource;
	private List<Resource> resourceCache;
	private ConverterService converter;

	public ResultContextBuilder<RESULT, MAPPER> result(RESULT result) {
		Objects.requireNonNull(result);
		this.result = result;
		return this;
	}
	
	public ResultContextBuilder<RESULT, MAPPER> mapper(MAPPER mapper) {
		Objects.requireNonNull(mapper);
		this.mapper = mapper;
		return this;
	}
	
	public ResultContextBuilder<RESULT, MAPPER> options(Map<Object, Object> options) {
		this.options = options == null ? Collections.emptyMap() : options;
		return this;
	}
	
	public ResultContextBuilder<RESULT, MAPPER> resource(Resource resource) {
		Objects.requireNonNull(resource);
		this.resource = resource;
		return this;
	}
	
	public ResultContextBuilder<RESULT, MAPPER> resourceCache(List<Resource> resourceCache) {
		this.resourceCache = resourceCache == null ? Collections.emptyList() : resourceCache;
		return this;
	}
	
	public ResultContextBuilder<RESULT, MAPPER> converterService(ConverterService converter) {
		this.converter = converter;
		return this;
	}
	
	protected void verifyBuild() {
		Objects.requireNonNull(getResource());
		Objects.requireNonNull(getResult());
	}
	
	public ResultContext<RESULT, MAPPER> build() {
		verifyBuild();
		return new ResultContext<>() {
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.context.ResultContext#getLoadResource()
			 */
			@Override
			public Resource getLoadResource() {
				return getResource();
			}
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.context.ResultContext#getOptions()
			 */
			@Override
			public Map<Object, Object> getOptions() {
				return options == null ? Collections.emptyMap() : options;
			}
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.context.ResultContext#getResourceCache()
			 */
			@Override
			public List<Resource> getResourceCache() {
				return resourceCache == null ? Collections.emptyList() : resourceCache;
			}
			
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.context.ResultContext#getResult()
			 */
			@Override
			public RESULT getResult() {
				return result;
			}
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.context.ResultContext#getConverter()
			 */
			@Override
			public Optional<ConverterService> getConverter() {
				return converter == null ? Optional.empty() : Optional.of(converter);
			}
			@Override
			public MAPPER getMapper() {
				return mapper;
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
