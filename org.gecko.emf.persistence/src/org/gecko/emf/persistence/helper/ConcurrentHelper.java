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
package org.gecko.emf.persistence.helper;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.osgi.util.promise.PromiseFactory;
import org.osgi.util.promise.Promises;

/**
 * Helper class for convenient use of {@link Promises} and {@link Executors}
 * @author Mark Hoffmann
 * @since 10.02.2023
 */
public class ConcurrentHelper {
	
	static class PersistenceThreadFactory implements ThreadFactory {
		
		private static final String NAME_FORMAT = "[%02d] %s";
		private volatile int count = 0;
		private final String name;
		
		PersistenceThreadFactory(String name) {
			this.name = name;
		}

		/* 
		 * (non-Javadoc)
		 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
		 */
		@Override
		public Thread newThread(Runnable r) {
			count++;
			Thread t = new Thread(r, String.format(NAME_FORMAT, count, name));
			return t;
		}

	}
	
	private static final String DEFAULT_THREAD_FACTORY_NAME = "default-<no-name>"; 
	
	/**
	 * Creates a {@link ThreadFactory} using the given name
	 * @param name the name of the factory
	 * @returnthe {@link ThreadFactory}
	 */
	public static ThreadFactory createThreadFactory(String name) {
		return new PersistenceThreadFactory(name == null ? DEFAULT_THREAD_FACTORY_NAME : name);
	}
	
}
