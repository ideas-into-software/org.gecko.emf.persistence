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
package org.gecko.emf.persistence;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;

/**
 * Special resource that is able to handle additional persistence methods
 * @author Mark Hoffmann
 * @since 30.05.2022
 */
public class PersistenceResourceImpl extends XMLResourceImpl implements PersistenceResource {
	
	  /**
	   * The storage for the default count options.
	   */
	  protected Map<Object, Object> defaultCountOptions;

	public Map<Object, Object> getDefaultDeleteOptions() {
		if (defaultDeleteOptions == null) {
			defaultDeleteOptions = new HashMap<Object, Object>();
		}
		return defaultDeleteOptions;
	}
	
	public Map<Object, Object> getDefaultCountOptions() {
		if (defaultCountOptions == null) {
			defaultCountOptions = new HashMap<Object, Object>();
		}
		return defaultCountOptions;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.PersistenceResource#count()
	 */
	@Override
	public long count() throws IOException {
		return count(null);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.PersistenceResource#count(java.util.Map)
	 */
	@Override
	public long count(Map<?, ?> options) throws IOException  {
		org.eclipse.emf.ecore.resource.URIHandler uriHandler = getURIConverter().getURIHandler(getURI());
		if (uriHandler instanceof PersistenceURIHandler) {
			PersistenceURIHandler persistenceUriHandler = (PersistenceURIHandler) uriHandler;
			try {
				return persistenceUriHandler.count(getURI(), mergeMaps(options, defaultCountOptions));
			} finally {
				unload();
				ResourceSet resourceSet = getResourceSet();
				if (resourceSet != null)
				{
					resourceSet.getResources().remove(this);
				}

			}
		}
		throw new IOException("No PersistenceURIHandler found, that can handle the resources' URI");
	}

}
