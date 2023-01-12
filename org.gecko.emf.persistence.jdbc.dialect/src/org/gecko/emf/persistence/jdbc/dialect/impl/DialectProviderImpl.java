/**
 * Copyright (c) 2012 - 2023 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.jdbc.dialect.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.gecko.emf.persistence.jdbc.dialect.Dialect;
import org.gecko.emf.persistence.jdbc.dialect.DialectProvider;
import org.osgi.service.component.annotations.Component;

/**
 * 
 * @author mark
 * @since 11.01.2023
 */
@Component(immediate = true)
public class DialectProviderImpl implements DialectProvider {
	
	private Map<String, Dialect> dialectsMap = new ConcurrentHashMap<>();
	
	/**
	 * Creates a new instance.
	 */
	public DialectProviderImpl() {
		Dialect d = new DefaultDialect();
		dialectsMap.put(d.getName(), d);
		d = new PostgreSLQDialect();
		dialectsMap.put(d.getName(), d);
		d = new DerbyDialect();
		dialectsMap.put(d.getName(), d);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.dialect.DialectProvider#getDialect(java.lang.String)
	 */
	@Override
	public Dialect getDialect(String dialectName) {
		return dialectsMap.get(dialectName);
	}

}
