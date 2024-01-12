/**
 * Copyright (c) 2012 - 2020 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.mongo.converter;

import java.util.logging.Logger;

import org.eclipse.emf.ecore.EDataType;
import org.gecko.emf.mongo.ValueConverter;

/**
 * 
 * @author ilenia
 * @since May 15, 2020
 */
public class ArrayConverter implements ValueConverter {

	private static final Logger logger = Logger.getLogger(ArrayConverter.class.getName());


	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.ValueConverter#convertMongoDBValueToEMFValue(org.eclipse.emf.ecore.EDataType, java.lang.Object)
	 */
	@Override
	public Object convertMongoDBValueToEMFValue(EDataType eDataType, Object databaseValue) {
		if(databaseValue instanceof Object[]) {
			Object[] objArray = (Object[]) databaseValue;
			String[] splitName = eDataType.getInstanceClassName().split("(?<=\\[)]"); //to get the array dimension
			int arrayDim = splitName.length;
			Object finalArray = createArray(objArray, arrayDim, eDataType.getInstanceClassName());
			return finalArray;
		}		
		logger.severe("Database object is not of type Object[]. Not supported by this converter!");
		return null;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.ValueConverter#convertEMFValueToMongoDBValue(org.eclipse.emf.ecore.EDataType, java.lang.Object)
	 */
	@Override
	public Object convertEMFValueToMongoDBValue(EDataType eDataType, Object emfValue) {
		
		if(emfValue instanceof Object[]) {
			return emfValue;
		}
		return null;		
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.ValueConverter#isConverterForType(org.eclipse.emf.ecore.EDataType)
	 */
	@Override
	public boolean isConverterForType(EDataType eDataType) {
		if (eDataType.getInstanceClassName().endsWith("[]")) {
			return true;
		}
		return false;
	}
	
	/**
	 * @param objArray
	 * @param arrayDim
	 * @return
	 */
	private Object createArray(Object[] objArray, int arrayDim, String className) {
		Object[] array = null;
		array = doCreateArray(objArray, arrayDim, className);
		return array;
	}
	
	/**
	 * From the Object[] it creates the array of the right type. 
	 * If the elements of the array are arrays, then the createArray method is called
	 * recursively till a primitive type is found
	 * 
	 * @param objArray
	 * @param arrayDim
	 * @param className
	 * @return
	 */
	private Object[] doCreateArray(Object[] objArray, int arrayDim, String className) {
		Object[] array = getStartingArray(arrayDim, objArray.length, className);
		for(int i = 0; i < objArray.length; i++) {				
			if(objArray[i] instanceof Object[]) {
				array[i] = createArray((Object[]) objArray[i], arrayDim-1, className);
			}
			else {
				array[i] = objArray[i];
			}
		}
		return array;
	}

	/**
	 * Depending on the type and the array dimension creates the right instance for the array
	 * 
	 * @param arrayDim
	 * @param firstDimSize
	 * @param type
	 * @return
	 */
	private Object[] getStartingArray(int arrayDim, int firstDimSize, String type) {
		switch(arrayDim) {
		case 1: 
			if(type.startsWith("java.lang.Double")) {
				return new Double[firstDimSize];
			}
			else if(type.startsWith("java.lang.Integer")) {
				return new Integer[firstDimSize];
			}
			else if(type.startsWith("java.lang.Long")) {
				return new Long[firstDimSize];
			}
			else if(type.startsWith("java.lang.Float")) {
				return new Float[firstDimSize];
			}
			else if(type.startsWith("java.lang.String")) {
				return new String[firstDimSize];
			}
			break;
		case 2: 
			if(type.startsWith("java.lang.Double")) {
				return new Double[firstDimSize][];
			}
			else if(type.startsWith("java.lang.Integer")) {
				return new Integer[firstDimSize][];
			}
			else if(type.startsWith("java.lang.Long")) {
				return new Long[firstDimSize][];
			}
			else if(type.startsWith("java.lang.Float")) {
				return new Float[firstDimSize][];
			}
			else if(type.startsWith("java.lang.String")){
				return new String[firstDimSize][];
			}
			break;
		case 3: 
			if(type.startsWith("java.lang.Double")) {
				return new Double[firstDimSize][][];
			}
			else if(type.startsWith("java.lang.Integer")) {
				return new Integer[firstDimSize][][];
			}
			else if(type.startsWith("java.lang.Long")) {
				return new Long[firstDimSize][][];
			}
			else if(type.startsWith("java.lang.Float")) {
				return new Float[firstDimSize][][];
			}
			else if(type.startsWith("java.lang.String")) {
				return new String[firstDimSize][][];
			}
			break;
		case 4: 
			if("java.lang.Double".equals(type)) {
				return new Double[firstDimSize][][][];
			}
			else if("java.lang.Integer".equals(type)) {
				return new Integer[firstDimSize][][][];
			}
			else if("java.lang.Long".equals(type)) {
				return new Long[firstDimSize][][][];
			}
			else if("java.lang.Float".equals(type)) {
				return new Float[firstDimSize][][][];
			}
			else if("java.lang.String".equals(type)) {
				return new String[firstDimSize][][][];
			}
			break;
		}
		logger.warning(String.format("Array Type %s or Dimension %s not yet supported!", type, arrayDim));
		return null;
	}
}
