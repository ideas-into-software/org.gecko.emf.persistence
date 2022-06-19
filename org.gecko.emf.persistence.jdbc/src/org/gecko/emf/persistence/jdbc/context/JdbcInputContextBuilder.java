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
import java.util.Objects;

import org.gecko.emf.persistence.input.InputContextBuilder;
import org.gecko.emf.persistence.jdbc.streams.JdbcInputMapper;

/**
 * 
 * @author mark
 * @since 19.06.2022
 */
public class JdbcInputContextBuilder extends InputContextBuilder<ResultSet> {
	
	private JdbcInputMapper mapper;

	public JdbcInputContextBuilder jdbcMapper(JdbcInputMapper mapper) {
		this.mapper = mapper;
		Objects.requireNonNull(mapper);
		return this;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.input.InputContextBuilder#verifyBuild()
	 */
	@Override
	protected void verifyBuild() {
		super.verifyBuild();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.input.InputContextBuilder#build()
	 */
	@Override
	public JdbcInputContext build() {
		verifyBuild();
		JdbcInputContext ctx = new JdbcInputContext(super.build());
		if (mapper != null) {
			ctx.setMapper(mapper);
		}
		return ctx;
	}

}
