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
package org.gecko.emf.persistence.pushstreams;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.eclipse.emf.ecore.EObject;
import org.gecko.emf.persistence.input.InputContext;
import org.osgi.util.pushstream.PushEventConsumer;

/**
 * Default {@link AsyncPushEventSource} implementation that should be suitable for most common cases
 * @author Mark Hoffmann
 * @since 17.06.2022
 */
public abstract class AsyncPushEventSource<RESULT> implements PersistencePushEventSource<RESULT> {
	
	private final InputContext<RESULT> context;
	private final ExecutorService executor;

	public AsyncPushEventSource(InputContext<RESULT> context, ExecutorService executor) {
		this.context = context;
		this.executor = executor;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.osgi.util.pushstream.PushEventSource#open(org.osgi.util.pushstream.PushEventConsumer)
	 */
	@Override
	public AutoCloseable open(PushEventConsumer<? super EObject> pec) throws Exception {
		Runnable sourceRunnable = createRunnable(context, pec);
		Objects.requireNonNull(sourceRunnable, "createPushStreamSourceRunnable must not return null");
		Future<?> future = executor.submit(sourceRunnable);
		return ()->future.cancel(true);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.pushstreams.PersistencePushEventSource#getContext()
	 */
	final public InputContext<RESULT> getContext() {
		return context;
	}
	
	/**
	 * Returns the executor
	 * @return the executor
	 */
	final public ExecutorService getExecutor() {
		return executor;
	}
	
}
