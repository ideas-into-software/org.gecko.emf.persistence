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
package org.gecko.emf.persistence.emf;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIHandler;

/**
 * Persistence URI handler with additional methods
 * @author Mark Hoffmann
 * @since 30.05.2022
 */
public interface PersistenceURIHandler extends URIHandler {
	
	/**
	 * Counts the number of matching elements for a given {@link URI}
	 * @param uri the uri
	 * @param options the load/count options
	 * @return the number of elements or -1 on errors
	 * @throws IOException the exception thrown on errors
	 */
	public long count(URI uri, Map<?, ?> options) throws IOException;

}
