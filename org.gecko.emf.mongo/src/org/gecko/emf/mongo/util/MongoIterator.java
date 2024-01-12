/*******************************************************************************
 * Copyright (c) 2012 Bryan Hunt.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 *    Data In Motion Consulting GmbH
 *******************************************************************************/
package org.gecko.emf.mongo.util;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;

import com.mongodb.client.MongoCursor;

/**
 * Java iterator implementation that can be used with a {@link MongoCursor}
 * @author bhunt
 * @author Mark Hoffmann
 */
public class MongoIterator implements Iterator<EObject>
{
	private MongoCursor<EObject> cursor;

	public MongoIterator(MongoCursor<EObject> cursor) {
		super();
		this.cursor = cursor;
	}

	/* 
	 * (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return cursor.hasNext();
	}

	/* 
	 * (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public EObject next() {
		return cursor.next();
	}

	/* 
	 * (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		cursor.remove();
	}
}
