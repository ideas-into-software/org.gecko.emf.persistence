/**
 * Copyright (c) 2012 - 2022 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Byan Hunt -  initial API and implementation
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.mongo.converter;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.emf.persistence.converter.ValueConverter;

/**
 * Default value converter for EMF 
 * @author bhunt
 */
public class DefaultConverter implements ValueConverter {
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.converter.ValueConverter#convertMongoDBValueToEMFValue(org.eclipse.emf.ecore.EDataType, java.lang.Object)
	 */
	@Override
	public Object convertDBValueToEMFValue(EDataType eDataType, Object databaseValue) {
		return EcoreUtil.createFromString(eDataType, (String) databaseValue);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.converter.ValueConverter#convertEMFValueToMongoDBValue(org.eclipse.emf.ecore.EDataType, java.lang.Object)
	 */
	@Override
	public Object convertEMFValueToDBValue(EDataType eDataType, Object emfValue) {
		return EcoreUtil.convertToString(eDataType, emfValue);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.converter.ValueConverter#isConverterForType(org.eclipse.emf.ecore.EDataType)
	 */
	@Override
	public boolean isConverterForType(EDataType eDataType) {
		int classifierId = eDataType.getClassifierID();
		return classifierId == EcorePackage.EBYTE_ARRAY ||
				classifierId == EcorePackage.EBYTE_OBJECT ||
				classifierId == EcorePackage.ECHAR ||
				classifierId == EcorePackage.ECHARACTER_OBJECT ||
				classifierId == EcorePackage.EJAVA_CLASS ||
				classifierId == EcorePackage.EJAVA_OBJECT;
	}
}
