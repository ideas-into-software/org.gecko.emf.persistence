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
package org.gecko.persistence.mongo.impl.delegate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.mongodb.event.ClusterClosedEvent;
import com.mongodb.event.ClusterDescriptionChangedEvent;
import com.mongodb.event.ClusterListener;
import com.mongodb.event.ClusterOpeningEvent;

public class ClusterListenerDelegate implements ClusterListener {

	ReadWriteLock lock = new ReentrantReadWriteLock();
	List<ClusterListener> delegte = new ArrayList<>();

	public void bindListener(ClusterListener listener) {

		lock.writeLock().lock();
		try {
			delegte.add(listener);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void unbindListener(ClusterListener listener) {
		lock.writeLock().lock();
		try {
			delegte.remove(listener);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void clusterOpening(ClusterOpeningEvent event) {
		lock.readLock().lock();
		try {
			delegte.forEach(l -> l.clusterOpening(event));
		} finally {
			lock.readLock().unlock();
		}
	}

	public void clusterClosed(ClusterClosedEvent event) {
		lock.readLock().lock();
		try {
			delegte.forEach(l -> l.clusterClosed(event));
		} finally {
			lock.readLock().unlock();
		}
	}

	public void clusterDescriptionChanged(ClusterDescriptionChangedEvent event) {
		lock.readLock().lock();
		try {

			delegte.forEach(l -> l.clusterDescriptionChanged(event));
		} finally {
			lock.readLock().unlock();
		}
	}
}
