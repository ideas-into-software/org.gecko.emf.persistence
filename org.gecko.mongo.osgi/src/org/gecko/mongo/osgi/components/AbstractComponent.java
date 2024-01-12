/*******************************************************************************
 * Copyright (c) 2012 Bryan Hunt.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 *******************************************************************************/

package org.gecko.mongo.osgi.components;

import java.util.concurrent.atomic.AtomicReference;

import org.gecko.mongo.osgi.exceptions.MongoConfigurationException;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;
import org.osgi.service.log.Logger;
import org.osgi.service.log.LoggerFactory;

/**
 * @author bhunt
 */
public abstract class AbstractComponent {
	
	private AtomicReference<LoggerFactory> logServiceReference = new AtomicReference<LoggerFactory>();
	private Logger logger = null;

	@Reference
	public void bindLogService(LogService logService) {
		logServiceReference.set(logService);
		logger = logService.getLogger("gecko.mongo.osgi");
	}

	public void unbindLogService(LogService logService) {
		logServiceReference.compareAndSet(logService, null);
		logger = null;
	}

	/**
	 * Handles logging for illegal configurations
	 * @param message the log message
	 */
	protected void handleIllegalConfiguration(String message) {
		if (message == null) {
			return;
		}
		if (logger != null) {
			logger.error(message);
		}
		throw new IllegalStateException(message);
	}

	/**
	 * Handles configuration exceptions, logging them and wrapping them into context exception
	 * @param message the error message
	 * @param e the exception
	 */
	protected void handleConfigurationException(String message, Exception e) {
		if (logger != null) {
			logger.error(message, e);
		}
		throw new MongoConfigurationException(message, e);
	}
}