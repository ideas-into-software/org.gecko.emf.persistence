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
package org.gecko.emf.persistence.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.gecko.emf.osgi.UriHandlerProvider;

/**
 * Delegate {@link URIHandler}, that can handle multiple uri handlers
 * @author Mark Hoffmann
 * @since 29.03.2022
 */
public class PersistenceUriHandler implements URIHandler {
	
	private List<UriHandlerProvider> delegates = new LinkedList<>();

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.URIHandler#canHandle(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public boolean canHandle(URI uri) {
		URIHandler handler = getDelegate(uri);
		return handler != null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.URIHandler#createInputStream(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
		URIHandler handler = getDelegate(uri);
		if (handler != null) {
			return handler.createInputStream(uri, options);
		}
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.URIHandler#createOutputStream(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException {
		URIHandler handler = getDelegate(uri);
		if (handler != null) {
			return handler.createOutputStream(uri, options);
		}
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.URIHandler#delete(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public void delete(URI uri, Map<?, ?> options) throws IOException {
		URIHandler handler = getDelegate(uri);
		if (handler != null) {
			handler.delete(uri, options);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.URIHandler#contentDescription(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public Map<String, ?> contentDescription(URI uri, Map<?, ?> options) throws IOException {
		URIHandler handler = getDelegate(uri);
		if (handler != null) {
			return handler.contentDescription(uri, options);
		}
		return Collections.emptyMap();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.URIHandler#exists(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public boolean exists(URI uri, Map<?, ?> options) {
		URIHandler handler = getDelegate(uri);
		if (handler != null) {
			return handler.exists(uri, options);
		}
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.URIHandler#getAttributes(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public Map<String, ?> getAttributes(URI uri, Map<?, ?> options) {
		URIHandler handler = getDelegate(uri);
		if (handler != null) {
			return handler.getAttributes(uri, options);
		}
		return Collections.emptyMap();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.URIHandler#setAttributes(org.eclipse.emf.common.util.URI, java.util.Map, java.util.Map)
	 */
	@Override
	public void setAttributes(URI uri, Map<String, ?> attributes, Map<?, ?> options) throws IOException {
		URIHandler handler = getDelegate(uri);
		if (handler != null) {
			handler.setAttributes(uri, attributes, options);
		}
	}
	
	public void addProvider(UriHandlerProvider provider) {
		if (provider != null) {
			delegates.add(provider);
		}
	}
	
	public void removeProvider(UriHandlerProvider provider) {
		if (provider != null) {
			delegates.remove(provider);
		}
	}
	
	private URIHandler getDelegate(URI uri) {
		synchronized (delegates) {
			return delegates.stream().map(UriHandlerProvider::getURIHandler).filter(d->d.canHandle(uri)).findFirst().orElse(null);
		}
	}

}
