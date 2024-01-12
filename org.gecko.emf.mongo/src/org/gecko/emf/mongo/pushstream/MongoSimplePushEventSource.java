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

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.mongo.Options;
import org.osgi.util.pushstream.PushEvent;
import org.osgi.util.pushstream.PushEventConsumer;
import org.osgi.util.pushstream.PushEventSource;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;

/**
 * {@link PushEventSource} implementation for the mongo iterator singe threaded
 * @author Mark Hoffmann
 * @since 23.11.2017
 */
public class MongoSimplePushEventSource implements PushEventSource<EObject> {
	
	private final Logger logger = Logger.getLogger(MongoSimplePushEventSource.class.getName());
	private final FindIterable<EObject> iterable;
	private final Map<Object, Object> options;
	private AtomicReference<MongoCursor<EObject>> currentCursor = new AtomicReference<>();
	private volatile boolean closed = false;

	public MongoSimplePushEventSource(FindIterable<EObject> iterable, Map<Object, Object> options) {
		this.iterable = iterable;
		this.options = options == null ? Collections.emptyMap() : options;
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.util.pushstream.PushEventSource#open(org.osgi.util.pushstream.PushEventConsumer)
	 */
	@Override
	public AutoCloseable open(PushEventConsumer<? super EObject> pec) throws Exception {
		closed = false;
		try {
			try (MongoCursor<EObject> cursor = iterable.iterator()){
				currentCursor.set(cursor);
				while (cursor.hasNext() && !closed){
					EObject eo = cursor.next();
					if (Boolean.TRUE.equals(options.get(Options.OPTION_READ_DETACHED))) {
						detachEObject(eo);
					}
					long r = pec.accept(PushEvent.data(eo));
					if ( r < 0 ) {
						logger.warning("Detected a back-pressure smaller than 0, ignoring entry: " + eo);
						break;
					}
					if ( r > 0) {
						Thread.sleep(r);
					}
				}
			} catch (Exception e) {
				logger.log(Level.INFO, "Got exception during iterating over cursor", e);
				pec.accept(PushEvent.error(e));
				closed = true;
			} finally {
				close(pec);
			}
		} catch (Exception e) {
			logger.log(Level.INFO, "Got exception running mongo cursor in a thread", e);
			close(pec);
		}
		return () -> {
			close(pec);
		};
	}
	
	/**
	 * Detaches the given {@link EObject}
	 * @param eobject the eobject instance
	 */
	private void detachEObject(EObject eobject) {
		if (eobject == null) {
			return;
		}
		Resource resource = eobject.eResource();
		if (resource != null) {
			resource.getContents().clear();
			if(resource.getResourceSet() != null){
				resource.getResourceSet().getResources().remove(resource);
			}
		}
	}

	/**
	 * Closes the event source and the underlying cursor, if it exist
	 * @param consumer the consumer to be closed
	 */
	private void close(PushEventConsumer<? super EObject> consumer) {
		if (consumer == null) {
			return;
		}
		if (!closed) {
			closed = true;
			try {
				consumer.accept(PushEvent.close());
				MongoCursor<EObject> c = currentCursor.getAndSet(null);
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error closing mongo cursor", e);
			}
		}
	}
	
}
