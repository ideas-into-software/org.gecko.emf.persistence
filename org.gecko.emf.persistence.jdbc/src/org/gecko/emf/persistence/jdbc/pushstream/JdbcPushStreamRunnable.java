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

import org.eclipse.emf.ecore.EObject;
import org.gecko.emf.persistence.context.ResultContext;
import org.gecko.emf.persistence.mapping.IteratorMapper;
import org.gecko.emf.persistence.pushstreams.PushEventSourceRunnable;
import org.osgi.util.pushstream.PushEventConsumer;

/**
 * 
 * @author mark
 * @since 17.06.2022
 */
public class JdbcPushStreamRunnable extends PushEventSourceRunnable<ResultSet, IteratorMapper> {

	private final IteratorMapper mapper;
	
	/**
	 * Creates a new instance.
	 */
	public JdbcPushStreamRunnable(ResultContext<ResultSet, IteratorMapper> context, PushEventConsumer<? super EObject> consumer) {
		super(context, consumer);
		mapper = context.getMapper();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.pushstreams.PushStreamRunnable#doGetNext()
	 */
	@Override
	protected EObject doGetNext() throws Exception {
		return mapper.next();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.pushstreams.PushStreamRunnable#doHasNext()
	 */
	@Override
	protected boolean doHasNext() throws Exception {
		return mapper.hasNext();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.pushstreams.PushStreamRunnable#doClose()
	 */
	@Override
	protected void doClose() throws Exception {
		mapper.close();
	}

}
