/**
 * Copyright (c) 2012 - 2024 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.mongo.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.gecko.emf.mongo.streams.MongoInputStream;
import org.gecko.emf.mongo.streams.MongoOutputStream;

/**
 * 
 * @author grune
 * @since Jan 23, 2024
 */
final class MongoPersistenceResourceImpl extends ResourceImpl {
	/**
	 * Creates a new instance.
	 * @param uri
	 */
	MongoPersistenceResourceImpl(URI uri) {
		super(uri);
	}

	@Override
	protected void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException {
			((MongoOutputStream) outputStream).saveResource(this);
	}

	@Override
	protected void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException {
			((MongoInputStream) inputStream).loadResource(this);
	}
}