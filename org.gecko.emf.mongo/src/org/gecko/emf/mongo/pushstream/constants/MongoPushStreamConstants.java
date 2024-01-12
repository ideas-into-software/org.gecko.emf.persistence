/**
 * Copyright (c) 2012 - 2017 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.mongo.pushstream.constants;

import java.util.concurrent.ExecutorService;

import org.gecko.emf.pushstream.EPushStreamProvider;

/**
 * Constants interface for the Mongo push stream support
 * @author Mark Hoffmann
 * @since 23.11.2017
 */
public interface MongoPushStreamConstants {
	
	/**
	 * If set to Boolean.TRUE, a query will return a {@link EPushStreamProvider} instead of a result
	 * Value type: Boolean
	 */
	public static final String OPTION_QUERY_PUSHSTREAM = "mongo.query.pushstream";
	
	/**
	 * Defines the {@link ExecutorService} to be used instead of the default one
	 * Value type: {@link ExecutorService}
	 */
	public static final String OPTION_QUERY_PUSHSTREAM_MULTITHREAD = "mongo.query.pushstream.multithread";
	
	/**
	 * Defines the {@link ExecutorService} to be used instead of the default one
	 * Value type: {@link ExecutorService}
	 */
	public static final String OPTION_QUERY_PUSHSTREAM_EXECUTOR = "mongo.query.pushstream.executor";

}
