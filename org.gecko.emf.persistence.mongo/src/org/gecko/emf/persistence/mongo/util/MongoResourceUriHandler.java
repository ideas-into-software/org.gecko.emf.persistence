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
package org.gecko.emf.persistence.mongo.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.impl.URIHandlerImpl;

/**
 * 
 * @author Juergen Albert
 * @since 16 Jan 2020
 */
public class MongoResourceUriHandler extends URIHandlerImpl{

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.xmi.impl.URIHandlerImpl#deresolve(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public URI deresolve(URI uri) {
		URI result = super.deresolve(uri);
		if(result.segmentCount() == 0 && uri.segmentCount() > 0) {
			result = result.appendSegment(uri.lastSegment());
		}
		return result;
	}
	
}
