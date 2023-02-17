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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.Options;
import org.gecko.emf.persistence.mapping.EObjectMapper;

/**
 * Builder for the {@link ResultContext}
 * @author Mark Hoffmann
 * @since 19.06.2022
 */
public class QueryContextBuilder<DRIVER, QUERY, MAPPER extends EObjectMapper> {

	private EClass eClass;
	private MAPPER mapper;
	private QUERY query;
	private DRIVER driver;
	private Map<Object, Object> options;
	private Map<Object, Object> response;
	private Map<Object, Object> mergedOptions;
	private Resource resource;
	private List<Resource> resourceCache;
	private ConverterService converter;
	private String idColumn;
	private String column;
	private String table;
	private boolean countOnly = false;
	private boolean projectOnly = false;

	public QueryContextBuilder<DRIVER, QUERY, MAPPER> eClass(EClass eClass) {
		Objects.requireNonNull(eClass);
		this.eClass = eClass;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, QUERY, MAPPER> query(QUERY query) {
		Objects.requireNonNull(query);
		this.query = query;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, QUERY, MAPPER> idColumn(String idColumn) {
		Objects.requireNonNull(idColumn);
		this.idColumn = idColumn;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, QUERY, MAPPER> column(String column) {
		Objects.requireNonNull(column);
		this.column = column;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, QUERY, MAPPER> table(String table) {
		Objects.requireNonNull(table);
		this.table = table;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, QUERY, MAPPER> driver(DRIVER driver) {
		Objects.requireNonNull(driver);
		this.driver = driver;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, QUERY, MAPPER> mapper(MAPPER mapper) {
		Objects.requireNonNull(mapper);
		this.mapper = mapper;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, QUERY, MAPPER> options(Map<Object, Object> options) {
		this.options = options == null ? Collections.emptyMap() : options;
		normalizeOptions(this.options);
		return this;
	}
	
	public QueryContextBuilder<DRIVER, QUERY, MAPPER> response(Map<Object, Object> response) {
		this.response = response;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, QUERY, MAPPER> resource(Resource resource) {
		Objects.requireNonNull(resource);
		this.resource = resource;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, QUERY, MAPPER> resourceCache(List<Resource> resourceCache) {
		this.resourceCache = resourceCache == null ? Collections.emptyList() : resourceCache;
		return this;
	}
	
	public QueryContextBuilder<DRIVER, QUERY, MAPPER> converterService(ConverterService converter) {
		this.converter = converter;
		return this;
	}
	
	protected void verifyBuild() {
		Objects.requireNonNull(getResource());
		Objects.requireNonNull(getEClass());
	}
	
	public QueryContext<DRIVER, QUERY, MAPPER> build() {
		verifyBuild();
		return new QueryContext<>() {
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.context.QueryContext#getLoadResource()
			 */
			@Override
			public Resource getResource() {
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
			 * @see org.gecko.emf.persistence.context.QueryContext#getQuery()
			 */
			@Override
			public QUERY getQuery() {
				return query;
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
			@Override
			public boolean countOnly() {
				
				return countOnly;
			}
			@Override
			public boolean projectOnly() {
				return projectOnly;
			}
			@Override
			public boolean countResult() {
				if (countOnly()) {
					return false;
				}
				Object optionCountResult = mergedOptions.get(Options.READ_COUNT_RESULT);
				return optionCountResult != null && Boolean.TRUE.equals(optionCountResult);
			}
			@Override
			public Map<Object, Object> getResponse() {
				return response == null ? new HashMap<>() : response;
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
	
	/**
	 * Normalizes the load options
	 * @param options the original options
	 */
	private <K extends Object, V extends Object> void normalizeOptions(Map<K, V> options) {
		mergedOptions.putAll(options);
		EClass filterEClass = (EClass) options.getOrDefault(Options.READ_FILTER_ECLASS, null);
		EClass collectionEClass = Options.getTableEClass(options);
		if (collectionEClass != null && filterEClass == null) {
			mergedOptions.put(Options.READ_FILTER_ECLASS, collectionEClass);
		}
	}

}
