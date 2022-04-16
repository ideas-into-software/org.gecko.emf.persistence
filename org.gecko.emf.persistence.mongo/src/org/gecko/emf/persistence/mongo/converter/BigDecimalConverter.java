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
package org.gecko.emf.persistence.mongo.converter;

import java.math.BigDecimal;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.emf.persistence.ValueConverter;

/**
 * Converter for BigInteger.
 * @author Juergen Albert
 */
public class BigDecimalConverter implements ValueConverter {

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.converter.ValueConverter#isConverterForType(org.eclipse.emf.ecore.EDataType)
	 */
	@Override
	public boolean isConverterForType(EDataType eDataType) {
		if (eDataType.getInstanceClass().equals(BigDecimal.class)) {
			return true;
		}
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.converter.ValueConverter#convertMongoDBValueToEMFValue(org.eclipse.emf.ecore.EDataType, java.lang.Object)
	 */
	@Override
	public Object convertMongoDBValueToEMFValue(EDataType eDataType,
			Object databaseValue) {
		if (databaseValue instanceof Double) {
			return EcoreUtil.createFromString(eDataType, ((Double)databaseValue).toString());
		}
		if (databaseValue instanceof String) {
			return EcoreUtil.createFromString(eDataType, (String) databaseValue);
		}
		return databaseValue.toString();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.converter.ValueConverter#convertEMFValueToMongoDBValue(org.eclipse.emf.ecore.EDataType, java.lang.Object)
	 */
	@Override
	public Object convertEMFValueToMongoDBValue(EDataType eDataType,
			Object emfValue) {
		return EcoreUtil.convertToString(eDataType, emfValue);
	}

}
