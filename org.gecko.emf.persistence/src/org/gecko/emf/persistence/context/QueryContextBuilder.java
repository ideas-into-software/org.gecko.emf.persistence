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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.mapping.EObjectMapper;

/**
 * Builder for the {@link ResultContext}
 * @author Mark Hoffmann
 * @since 19.06.2022
 */
public class QueryContextBuilder<DRIVER, MAPPER extends EObjectMapper> {

	private EClass eClass;
	private MAPPER mapper;
	private DRIVER driver;
	private Map<Object, Object> options;
	private Resource resource;
	private List<Resource> resourceCache;
	private ConverterService converter;
	private String idColumn;
	private String column;
	private String table;

	public QueryContextBuilder<DRIVER, MAPPER> eClass(EClass eClass) {
		Objects.requireNonNull(eClass);
		this.eClass = eClass;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, MAPPER> idColumn(String idColumn) {
		Objects.requireNonNull(idColumn);
		this.idColumn = idColumn;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, MAPPER> column(String column) {
		Objects.requireNonNull(column);
		this.column = column;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, MAPPER> table(String table) {
		Objects.requireNonNull(table);
		this.table = table;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, MAPPER> driver(DRIVER driver) {
		Objects.requireNonNull(driver);
		this.driver = driver;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, MAPPER> mapper(MAPPER mapper) {
		Objects.requireNonNull(mapper);
		this.mapper = mapper;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, MAPPER> options(Map<Object, Object> options) {
		this.options = options == null ? Collections.emptyMap() : options;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, MAPPER> resource(Resource resource) {
		Objects.requireNonNull(resource);
		this.resource = resource;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, MAPPER> resourceCache(List<Resource> resourceCache) {
		this.resourceCache = resourceCache == null ? Collections.emptyList() : resourceCache;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, MAPPER> converterService(ConverterService converter) {
		this.converter = converter;
		return this;
	}
	
	protected void verifyBuild() {
		Objects.requireNonNull(getResource());
		Objects.requireNonNull(getEClass());
	}
	
	public QueryContext<DRIVER, MAPPER> build() {
		verifyBuild();
		return new QueryContext<>() {
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.context.QueryContext#getLoadResource()
			 */
			@Override
			public Resource getLoadResource() {
				return getResource();
			}
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.context.QueryContext#getOptions()
			 */
			@Override
			public Map<Object, Object> getOptions() {
				return options == null ? Collections.emptyMap() : options;
			}
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.context.QueryContext#getResourceCache()
			 */
			@Override
			public List<Resource> getResourceCache() {
				return resourceCache == null ? Collections.emptyList() : resourceCache;
			}
			
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.context.QueryContext#getEClass()
			 */
			@Override
			public EClass getEClass() {
				return eClass;
			}
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.context.QueryContext#getConverter()
			 */
			@Override
			public Optional<ConverterService> getConverter() {
				return converter == null ? Optional.empty() : Optional.of(converter);
			}
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.context.QueryContext#getMapper()
			 */
			@Override
			public MAPPER getMapper() {
				return mapper;
			}
			/**
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.context.QueryContext#getDriver()
			 */
			@Override
			public DRIVER getDriver() {
				return driver;
			}
			@Override
			public String getIdColumn() {
				return idColumn;
			}
			@Override
			public String getColumn() {
				return column;
			}
			@Override
			public String getTable() {
				return table;
			}
		};
	}
	
	/**
	 * Returns the driver.
	 * @return the driver
	 */
	public DRIVER getDriver() {
		return driver;
	}

	/**
	 * Returns the {@link EClass}.
	 * @return the {@link EClass}
	 */
	public EClass getEClass() {
		return eClass;
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
	
	/**
	 * Returns the idColumn.
	 * @return the idColumn
	 */
	public String getIdColumn() {
		return idColumn;
	}
	
	/**
	 * Returns the column.
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}
	
	/**
	 * Returns the table.
	 * @return the table
	 */
	public String getTable() {
		return table;
	}

}
