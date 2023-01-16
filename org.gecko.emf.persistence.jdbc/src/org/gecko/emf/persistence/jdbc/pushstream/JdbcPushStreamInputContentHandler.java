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
package org.gecko.emf.persistence.jdbc.pushstream;

import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;

import org.eclipse.emf.ecore.EObject;
import org.gecko.emf.persistence.context.ResultContext;
import org.gecko.emf.persistence.jdbc.JdbcPersistenceConstants;
import org.gecko.emf.persistence.jdbc.streams.JdbcInputStream;
import org.gecko.emf.persistence.mapping.InputContentHandler;
import org.gecko.emf.persistence.mapping.IteratorMapper;
import org.gecko.emf.persistence.pushstreams.AsyncPushEventSource;
import org.gecko.emf.persistence.pushstreams.PushEventSourceRunnable;
import org.gecko.emf.persistence.pushstreams.PushStreamConstants;
import org.gecko.emf.persistence.pushstreams.PushStreamInputContentHandler;
import org.gecko.emf.persistence.pushstreams.SimplePushEventSource;
import org.osgi.service.component.annotations.Component;
import org.osgi.util.pushstream.PushEventConsumer;
import org.osgi.util.pushstream.PushEventSource;

/**
 * Jdbc based input content handler for PushStreams. This handler is injected into
 * {@link JdbcInputStream} and used there. Content reading is then delegated to this
 * implementation.
 * The general handling is generalized, so we just need to define a simple "un-threaded" and a multi-threading capable 
 * {@link PushEventSource} instance
 * @author mark
 * @since 17.06.2022
 */
@Component(name="JdbcPushStreamInputContentHandler", service=InputContentHandler.class, property = {JdbcPersistenceConstants.PERSISTENCE_FILTER_PROP, PushStreamConstants.PUSHSTREAM_CONTENT_HANDLER_FILTER_PROP})
public class JdbcPushStreamInputContentHandler extends PushStreamInputContentHandler<ResultSet, IteratorMapper> {

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.pushstreams.PushStreamInputContentHandler#doGetSimpleEventSource(org.gecko.emf.persistence.InputContext)
	 */
	@Override
	protected PushEventSource<EObject> doGetSimpleEventSource(ResultContext<ResultSet, IteratorMapper> context) {
		return new SimplePushEventSource<ResultSet, IteratorMapper>(context) {
			@Override
			public PushEventSourceRunnable<ResultSet, IteratorMapper> createRunnable(ResultContext<ResultSet, IteratorMapper> context,
					PushEventConsumer<? super EObject> pec) {
				return new JdbcPushStreamRunnable(context, pec);
			}
		};
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.pushstreams.PushStreamInputContentHandler#doGetMultithreadedEventSource(org.gecko.emf.persistence.InputContext, java.util.concurrent.ExecutorService)
	 */
	@Override
	protected PushEventSource<EObject> doGetMultithreadedEventSource(ResultContext<ResultSet, IteratorMapper> context,
			ExecutorService executor) {
		return new AsyncPushEventSource<ResultSet, IteratorMapper>(context, executor) {
			@Override
			public PushEventSourceRunnable<ResultSet, IteratorMapper> createRunnable(ResultContext<ResultSet, IteratorMapper> context,
					PushEventConsumer<? super EObject> pec) {
				return new JdbcPushStreamRunnable(context, pec);
			}
		};
	}

}
