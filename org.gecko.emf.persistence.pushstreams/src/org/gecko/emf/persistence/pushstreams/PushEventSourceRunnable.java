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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.persistence.api.Options;
import org.gecko.emf.persistence.context.ResultContext;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.osgi.util.pushstream.PushEvent;
import org.osgi.util.pushstream.PushEventConsumer;

/**
 * {@link Runnable} implementation for a typed result iterator to be used multi-threaded or not
 * @author Mark Hoffmann
 * @since 17.06.2022
 */
public abstract class PushEventSourceRunnable<RESULT, MAPPER extends EObjectMapper> implements Runnable {

	protected static final Logger LOGGER = Logger.getLogger(PushEventSourceRunnable.class.getName());
	private final PushEventConsumer<? super EObject> consumer;
	private final ResultContext<RESULT, MAPPER> context;
	private boolean closed = false;

	public PushEventSourceRunnable(ResultContext<RESULT, MAPPER> context, PushEventConsumer<? super EObject> consumer) {
		this.context = context;
		this.consumer = consumer;
	}
	
	public final ResultContext<RESULT, MAPPER> getContext() {
		return context;
	}

	/**
	 * Returns the consumer.
	 * @return the consumer
	 */
	public PushEventConsumer<? super EObject> getConsumer() {
		return consumer;
	}

	/**
	 * Returns the closed.
	 * @return the closed
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * Sets the closed.
	 * @param closed the closed to set
	 */
	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		setClosed(false);
		Map<Object, Object> options = context.getOptions();
		try {
			try {
				while (doHasNext() && !isClosed() && !Thread.currentThread().isInterrupted()){
					EObject eo = doGetNext();
					if (Boolean.TRUE.equals(options.get(Options.READ_READ_DETACHED))) {
						detachEObject(eo);
					}
					long r = getConsumer().accept(PushEvent.data(eo));
					if ( r < 0 ) {
						LOGGER.warning("Detected a back-pressure smaller than 0, ignoring entry: " + eo);
						break;
					}
					if ( r > 0) {
						Thread.sleep(r);
					}
				}
			} catch (Exception e) {
				LOGGER.log(Level.INFO, "Got exception during iterating over cursor", e);
				getConsumer().accept(PushEvent.error(e));
				setClosed(true);
			} finally {
				close(getConsumer());
			}
		} catch (Exception e) {
			LOGGER.log(Level.INFO, "Got exception running PushStream cursor in a thread", e);
			close(getConsumer());
		}
	}
	
	/**
	 * Detaches the given {@link EObject}
	 * @param eobject the {@link EObject} instance
	 */
	protected void detachEObject(EObject eobject) {
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
	protected void close(PushEventConsumer<? super EObject> consumer) {
		if (consumer == null) {
			return;
		}
		if (!isClosed()) {
			setClosed(true);
			try {
				consumer.accept(PushEvent.close());
				doClose();
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Error closing PushStream cursor", e);
			}
		}
	}
	
	/**
	 * Returns the next mapped {@link EObject}
	 * @return the next {@link EObject}
	 */
	abstract protected EObject doGetNext() throws Exception;
	
	/**
	 * Returns <code>true</code>, if there is another object, to map
	 * @return <code>true</code>, if there is another object, to map, otherwise <code>false</code>
	 */
	abstract protected boolean doHasNext() throws Exception;
	
	/**
	 * Close operation called after the push event was closed.
	 * This can be used to clean up underlying resources.
	 */
	abstract protected void doClose() throws Exception;

}
