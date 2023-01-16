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

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.persistence.context.ResultContext;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.mapping.UncachedInputContentHandler;
import org.gecko.emf.pushstream.CustomPushStreamProvider;
import org.gecko.emf.pushstream.PushStreamFactory;
import org.osgi.util.pushstream.PushEventSource;
import org.osgi.util.pushstream.PushStreamProvider;

/**
 * Implementation of InputContentHandler for PushStreams {@link PushEventSource} implementation.
 * It wraps the {@link PushEventSource} into an {@link EObject} of type {@link CustomPushStreamProvider}
 * and returns it. Its might be the loading result, that is placed into the {@link Resource} content.
 * @author Mark Hoffmann
 * @since 17.06.2022
 */
public abstract class PushStreamInputContentHandler<RESULT, MAPPER extends EObjectMapper> extends UncachedInputContentHandler<RESULT, MAPPER> {

	private final ExecutorService DEFAULT_EXECUTOR = Executors.newCachedThreadPool((r)->new Thread(r, "PushEventSource"));
	private ExecutorService executor;

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.InputContentHandler#canHandle(java.util.Map)
	 */
	@Override
	public boolean canHandle(Map<Object, Object> options) {
		boolean canHandle = Boolean.TRUE.equals(options.get(PushStreamConstants.OPTION_QUERY_PUSHSTREAM));
		return canHandle;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.InputContentHandler#createContent(org.gecko.emf.persistence.InputContext)
	 */
	@Override
	public EObject createContent(ResultContext<RESULT, MAPPER> context) {
		CustomPushStreamProvider psp = PushStreamFactory.eINSTANCE.createCustomPushStreamProvider();
		psp.setProvider(new PushStreamProvider());
		ExecutorService es = getExecutor(context.getOptions());
		PushEventSource<EObject> mpes = getEventSource(context, es);
		psp.setEventSource(mpes);
		return psp;
	}
	
	/**
	 * Returns the executor. It initializes either the default one or a executor service given
	 * by the load options.
	 * @param options the load options
	 * @return the executor service instance
	 */
	protected ExecutorService getExecutor(Map<Object, Object> options) {
		if (executor == null) {
			if (options != null && options.containsKey(PushStreamConstants.OPTION_QUERY_PUSHSTREAM_EXECUTOR)) {
				Object o = options.get(PushStreamConstants.OPTION_QUERY_PUSHSTREAM_EXECUTOR);
				if (o != null && o instanceof ExecutorService) {
					executor = (ExecutorService) o;
				}
			} else {
				executor = DEFAULT_EXECUTOR;
			}
		}
		return executor;
	}

	/**
	 * Returns the {@link PushEventSource} depending on the properties
	 * @param context the input context for the query including the result and options
	 * @param executor the optional {@link ExecutorService}
	 * @return the {@link PushEventSource} instance
	 */
	protected PushEventSource<EObject> getEventSource(ResultContext<RESULT, MAPPER> context, ExecutorService executor) {
		Map<Object, Object> options = context.getOptions();
		boolean multiThreaded = Boolean.TRUE.equals(options.get(PushStreamConstants.OPTION_QUERY_PUSHSTREAM_MULTITHREAD));
		return multiThreaded ? doGetMultithreadedEventSource(context, executor) : doGetSimpleEventSource(context);
	}
	
	/**
	 * Gets called if the {@link PushStreamConstants#OPTION_QUERY_PUSHSTREAM_MULTITHREAD} is <code>true</code>
	 * @param context the input context with result and options
	 * @param executor there might be an executor provided by the options, otherwise a default ExecutorService is provided
	 * @return the {@link PushEventSource} instance, must not be <code>null</code>
	 */
	protected PushEventSource<EObject> doGetMultithreadedEventSource(ResultContext<RESULT, MAPPER> context, ExecutorService executor) {
		return doGetSimpleEventSource(context);
	}

	/**
	 * Gets called if the {@link PushStreamConstants#OPTION_QUERY_PUSHSTREAM_MULTITHREAD} is <code>false</code> or unset
	 * @param context the input context with result and options
	 * @return the {@link PushEventSource} instance, must not be <code>null</code>
	 */
	abstract protected PushEventSource<EObject> doGetSimpleEventSource(ResultContext<RESULT, MAPPER> context);
	
}
