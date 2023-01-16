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

import org.eclipse.emf.ecore.EObject;
import org.gecko.emf.persistence.context.ResultContext;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.osgi.util.pushstream.PushEventConsumer;
import org.osgi.util.pushstream.PushEventSource;

/**
 * Special {@link PushEventSource} for the support in the EMF persistence
 * @param <RESULT> the result object type
 * @author mark
 * @since 17.06.2022
 */
public interface PersistencePushEventSource<RESULT, MAPPER extends EObjectMapper> extends PushEventSource<EObject> {
	
	/**
	 * Creates a runnable to execute 
	 * @param context the context including result and options
	 * @param pushEventConsumer the needed {@link PushEventConsumer} instance
	 * @return the runnable, must not be <code>null</code>
	 */
	public PushEventSourceRunnable<RESULT, MAPPER> createRunnable(ResultContext<RESULT, MAPPER> context, PushEventConsumer<? super EObject> pushEventConsumer);

	/**
	 * Returns the context instance
	 * @return the context instance
	 */
	public ResultContext<RESULT, MAPPER> getContext();
}
