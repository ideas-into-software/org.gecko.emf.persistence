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

import java.util.List;
import java.util.Map;

import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.PrimaryKeyFactory;
import org.gecko.emf.persistence.api.QueryEngine;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.mapping.InputContentHandler;

/**
 * Just a wrapper to hold important information
 * @author Mark Hoffmann
 * @since 08.04.2022
 */
public interface PersistenceContext<TABLE, QT, RT, ENGINE, MAPPER extends EObjectMapper> {
	
	ConverterService getConverterSevice();
	
	List<InputContentHandler<RT, MAPPER>> getInputContentHandler();
	
	QueryEngine<QT, ENGINE> getQueryEngine();
	
	Map<String, PrimaryKeyFactory> getKeyFactories();

}
