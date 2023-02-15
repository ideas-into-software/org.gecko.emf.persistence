/**
 * Copyright (c) 2012 - 2023 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.helper;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;

/**
 * EMF helper class
 * @author Mark Hoffmann
 * @since 15.02.2023
 */
public class EMFHelper {


	/**
	 * Returns the response map
	 * @param options the options to get the response from
	 * @return the response map or a new onev
	 */
	@SuppressWarnings("unchecked")
	public static Map<Object, Object> getResponse(Map<?, ?> options) {
		Map<?, ?> response = options == null ? null : (Map<?, ?>)options.get(URIConverter.OPTION_RESPONSE);
		return response == null ? new HashMap<>() : (Map<Object, Object>) response;
	}
	
	public static EClass getEClassFromResourceSet(ResourceSet resourceSet, String eClassURI) {
		URI theUri = URI.createURI(eClassURI);
		String classifier = theUri.lastSegment();
		EPackage ePackage = resourceSet.getPackageRegistry().getEPackage(theUri.trimSegments(1).trimFragment().toString());
		if(ePackage != null) {
			EClassifier eClassifier = (EClassifier) ePackage.getEClassifier(classifier);
			if(eClassifier != null && eClassifier instanceof EClass) {
				return (EClass) eClassifier;
			}
		}

		return (EClass) resourceSet.getEObject(theUri, true);
	}
	
	public static EClass getEClass(ResourceSet resourceSet, String eClassURI, Map<String, EClass> eClassCache) {
		if (eClassCache != null) {
			synchronized (eClassCache) {
				EClass eClass = eClassCache.get(eClassURI);

				if (eClass == null) {
					eClass = getEClassFromResourceSet(resourceSet, eClassURI);
					eClassCache.put(eClassURI, eClass);
				}
				return eClass;
			}
		}
		return getEClassFromResourceSet(resourceSet, eClassURI);
	}

}
