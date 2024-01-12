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

import org.eclipse.emf.ecore.EObject;
import org.gecko.emf.mongo.InputContentHandler;
import org.gecko.emf.mongo.Keywords;
import org.gecko.emf.mongo.UncachedInputContentHandler;
import org.gecko.emf.mongo.pushstream.constants.MongoPushStreamConstants;
import org.gecko.emf.pushstream.CustomPushStreamProvider;
import org.gecko.emf.pushstream.PushStreamFactory;
import org.osgi.annotation.bundle.Capability;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.util.pushstream.PushEventSource;
import org.osgi.util.pushstream.PushStreamProvider;

import com.mongodb.client.FindIterable;

/**
 * Implementation of InputContentHandler for PushStreams {@link PushEventSource} implementation for the mongo iterator
 * @author Mark Hoffmann
 * @since 23.11.2017
 */
@Component(name="PushStreamInputContentHandler", service=InputContentHandler.class, property = {Constants.SERVICE_RANKING + "=10"})
@Capability(
		namespace = Keywords.CAPABILITY_EXTENSION_NAMESPACE,
		name = "pushstream",
		version = "2.0",
		attribute = "type=mongo"
		)
public class PushStreamInputContentHandler extends UncachedInputContentHandler {
	private ExecutorService executor;

	/* (non-Javadoc)
	 * @see org.gecko.emf.mongo.InputContentHandler#canHandle(java.util.Map)
	 */
	@Override
	public boolean canHandle(Map<Object, Object> options) {
		boolean canHandle = Boolean.TRUE.equals(options.get(MongoPushStreamConstants.OPTION_QUERY_PUSHSTREAM));
		return canHandle;
	}

	/* (non-Javadoc)
	 * @see org.gecko.emf.mongo.UncachedInputContentHandler#doCreateContent(com.mongodb.client.FindIterable, java.util.Map)
	 */
	@Override
	public EObject doCreateContent(FindIterable<EObject> iterable, Map<Object, Object> options) {
		CustomPushStreamProvider psp = PushStreamFactory.eINSTANCE.createCustomPushStreamProvider();
		psp.setProvider(new PushStreamProvider());
		ExecutorService es = getExecutor(options);
		PushEventSource<EObject> mpes = getEventSource(options, iterable, es);
		psp.setEventSource(mpes);
		return psp;
	}
	
	/**
	 * Returns the {@link PushEventSource} depending on the properties
	 * @param options the load property
	 * @param iterable the {@link FindIterable}
	 * @param executor the optional {@link ExecutorService}
	 * @return the {@link PushEventSource} instance
	 */
	private PushEventSource<EObject> getEventSource(Map<Object, Object> options, FindIterable<EObject> iterable, ExecutorService executor) {
		boolean multiThreaded = Boolean.TRUE.equals(options.get(MongoPushStreamConstants.OPTION_QUERY_PUSHSTREAM_MULTITHREAD));
		return multiThreaded ? new MongoPushEventSource(iterable, executor, options) : new MongoSimplePushEventSource(iterable, options);
	}

	/**
	 * Returns the executor. It initializes either the default one or a executor service given
	 * by the load options.
	 * @param options the load options
	 * @return the executor service instance
	 */
	private ExecutorService getExecutor(Map<Object, Object> options) {
		if (executor == null) {
			if (options != null && options.containsKey(MongoPushStreamConstants.OPTION_QUERY_PUSHSTREAM_EXECUTOR)) {
				Object o = options.get(MongoPushStreamConstants.OPTION_QUERY_PUSHSTREAM_EXECUTOR);
				if (o != null && o instanceof ExecutorService) {
					executor = (ExecutorService) o;
				}
			}
		}
		return executor;
	}

}
