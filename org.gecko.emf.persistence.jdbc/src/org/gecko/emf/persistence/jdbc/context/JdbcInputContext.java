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
package org.gecko.emf.persistence.jdbc.context;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.persistence.ConverterService;
import org.gecko.emf.persistence.input.InputContext;
import org.gecko.emf.persistence.jdbc.streams.JdbcInputMapper;

/**
 * 
 * @author mark
 * @since 19.06.2022
 */
public class JdbcInputContext implements InputContext<ResultSet> {
	
	private final InputContext<ResultSet> parent;
	private JdbcInputMapper mapper;
	
	/**
	 * Creates a new instance.
	 */
	JdbcInputContext(InputContext<ResultSet> parent) {
		this.parent = parent;
	}
	
	public JdbcInputMapper getMapper() {
		return mapper;
	}
	
	public void setMapper(JdbcInputMapper mapper) {
		Objects.requireNonNull(mapper);
		this.mapper = mapper;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.input.InputContext#getResult()
	 */
	@Override
	public ResultSet getResult() {
		return parent.getResult();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.input.InputContext#getOptions()
	 */
	@Override
	public Map<Object, Object> getOptions() {
		return parent.getOptions();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.input.InputContext#getLoadResource()
	 */
	@Override
	public Resource getLoadResource() {
		return parent.getLoadResource();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.input.InputContext#getResourceCache()
	 */
	@Override
	public List<Resource> getResourceCache() {
		return parent.getResourceCache();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.input.InputContext#getConverter()
	 */
	@Override
	public Optional<ConverterService> getConverter() {
		return parent.getConverter();
	}

}
