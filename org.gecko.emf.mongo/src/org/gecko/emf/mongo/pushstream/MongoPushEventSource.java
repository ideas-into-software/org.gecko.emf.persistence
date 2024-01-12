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
package org.gecko.emf.mongo.pushstream;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.eclipse.emf.ecore.EObject;
import org.osgi.util.pushstream.PushEventConsumer;
import org.osgi.util.pushstream.PushEventSource;

import com.mongodb.client.FindIterable;

/**
 * {@link PushEventSource} implementation for the mongo iterator
 * @author Mark Hoffmann
 * @since 23.11.2017
 */
public class MongoPushEventSource implements PushEventSource<EObject> {
	
	private final ExecutorService DEFAULT_EXECUTOR = Executors.newCachedThreadPool(new ThreadFactory() {

		private int counter = 0;

		/* (non-Javadoc)
		 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
		 */
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, "MongoPushEvSrc-" + (counter++));
			return t;
		}

	});
	
	private final FindIterable<EObject> iterable;
	private final ExecutorService executor;
	private Map<Object, Object> options;

	public MongoPushEventSource(FindIterable<EObject> iterable, Map<Object, Object> options) {
		this.iterable = iterable;
		this.options = options;
		this.executor = DEFAULT_EXECUTOR;
	}
	
	public MongoPushEventSource(FindIterable<EObject> iterable, ExecutorService executor, Map<Object, Object> options) {
		this.iterable = iterable;
		this.executor = executor == null ? DEFAULT_EXECUTOR : executor;
		this.options = options;
	}

	/* (non-Javadoc)
	 * @see org.osgi.util.pushstream.PushEventSource#open(org.osgi.util.pushstream.PushEventConsumer)
	 */
	@Override
	public AutoCloseable open(PushEventConsumer<? super EObject> pec) throws Exception {
		Future<?> future = executor.submit(new MongoPushStreamRunnable(iterable.iterator(), pec, options));
		return ()->future.cancel(true);
	}
	
}
