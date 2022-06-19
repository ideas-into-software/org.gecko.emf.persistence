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
package org.gecko.emf.persistence.pushstreams;

import org.gecko.emf.persistence.PersistenceConstants;
import org.gecko.emf.persistence.input.InputContentHandler;

/**
 * Constants for the PushStream supports
 * @author mark
 * @since 17.06.2022
 */
public interface PushStreamConstants {
	
	/**
	 * If set to Boolean.TRUE, a query will return a {@link EPushStreamProvider} instead of a result
	 * Value type: Boolean
	 */
	public static final String OPTION_QUERY_PUSHSTREAM = "persistence.query.pushstream";
	
	/**
	 * Defines the {@link ExecutorService} to be used instead of the default one
	 * Value type: {@link ExecutorService}
	 */
	public static final String OPTION_QUERY_PUSHSTREAM_MULTITHREAD = "persistence.query.pushstream.multithread";
	
	/**
	 * Defines the {@link ExecutorService} to be used instead of the default one
	 * Value type: {@link ExecutorService}
	 */
	public static final String OPTION_QUERY_PUSHSTREAM_EXECUTOR = "persistence.query.pushstream.executor";
	
	/** 
	 * Defines the service property key, that defines {@link InputContentHandler} type
	 */
	public static final String PUSHSTREAM_CONTENT_HANDLER_VALUE_PROP = "pushstream";
	
	public static final String PUSHSTREAM_CONTENT_HANDLER_FILTER_PROP = PersistenceConstants.INPUT_CONTENT_HANDLER_PROP + "=" + PUSHSTREAM_CONTENT_HANDLER_VALUE_PROP;
	public static final String PUSHSTREAM_CONTENT_HANDLER_FILTER = "(" + PUSHSTREAM_CONTENT_HANDLER_FILTER_PROP + ")";

}
