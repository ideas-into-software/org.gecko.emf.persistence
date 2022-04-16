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

import com.mongodb.event.ConnectionCheckOutFailedEvent;
import com.mongodb.event.ConnectionCheckOutStartedEvent;
import com.mongodb.event.ConnectionCheckedInEvent;
import com.mongodb.event.ConnectionCheckedOutEvent;
import com.mongodb.event.ConnectionClosedEvent;
import com.mongodb.event.ConnectionCreatedEvent;
import com.mongodb.event.ConnectionPoolClearedEvent;
import com.mongodb.event.ConnectionPoolClosedEvent;
import com.mongodb.event.ConnectionPoolCreatedEvent;
import com.mongodb.event.ConnectionPoolListener;
import com.mongodb.event.ConnectionReadyEvent;

public class ConnectionPoolListenerDelegate implements ConnectionPoolListener {

	ReadWriteLock lock = new ReentrantReadWriteLock();
	List<ConnectionPoolListener> delegate = new ArrayList<>();

	public void connectionPoolCreated(ConnectionPoolCreatedEvent event) {
		lock.readLock().lock();
		try {
			delegate.forEach(c -> connectionPoolCreated(event));
			;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void connectionPoolCleared(ConnectionPoolClearedEvent event) {
		lock.readLock().lock();
		try {
			delegate.forEach(c -> connectionPoolCleared(event));
			;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void connectionPoolClosed(ConnectionPoolClosedEvent event) {
		lock.readLock().lock();
		try {
			delegate.forEach(c -> connectionPoolClosed(event));
			;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void connectionCheckOutStarted(ConnectionCheckOutStartedEvent event) {
		lock.readLock().lock();
		try {
			delegate.forEach(c -> connectionCheckOutStarted(event));
			;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void connectionCheckedOut(ConnectionCheckedOutEvent event) {
		lock.readLock().lock();
		try {
			delegate.forEach(c -> connectionCheckedOut(event));
			;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void connectionCheckOutFailed(ConnectionCheckOutFailedEvent event) {
		lock.readLock().lock();
		try {
			delegate.forEach(c -> connectionCheckOutFailed(event));
			;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void connectionCheckedIn(ConnectionCheckedInEvent event) {
		lock.readLock().lock();
		try {
			delegate.forEach(c -> connectionCheckedIn(event));
			;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void connectionCreated(ConnectionCreatedEvent event) {
		lock.readLock().lock();
		try {
			delegate.forEach(c -> connectionCreated(event));
			;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void connectionReady(ConnectionReadyEvent event) {
		lock.readLock().lock();
		try {
			delegate.forEach(c -> connectionReady(event));
			;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void connectionClosed(ConnectionClosedEvent event) {
		lock.readLock().lock();
		try {
			delegate.forEach(c -> connectionClosed(event));
		} finally {
			lock.readLock().unlock();
		}
	}

	public void bindListener(ConnectionPoolListener listener) {

		lock.writeLock().lock();
		try {
			delegate.add(listener);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void unbindListener(ConnectionPoolListener listener) {
		lock.writeLock().lock();
		try {

			delegate.remove(listener);
		} finally {
			lock.writeLock().unlock();
		}
	}

}
