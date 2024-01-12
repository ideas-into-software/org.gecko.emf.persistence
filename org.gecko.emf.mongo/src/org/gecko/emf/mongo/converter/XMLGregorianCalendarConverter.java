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

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.ecore.EDataType;
import org.gecko.emf.mongo.ValueConverter;

/**
 * Value converter for {@link XMLGregorianCalendar}
 * @author Mark Hoffmann
 * @since 16.03.2020
 */
public class XMLGregorianCalendarConverter implements ValueConverter {
	
	private static final Logger logger = Logger.getLogger(XMLGregorianCalendarConverter.class.getName());

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.ValueConverter#convertMongoDBValueToEMFValue(org.eclipse.emf.ecore.EDataType, java.lang.Object)
	 */
	@Override
	public Object convertMongoDBValueToEMFValue(EDataType eDataType, Object databaseValue) {
		if (eDataType.getInstanceClass().equals(XMLGregorianCalendar.class)) {
			Date date;
			if (databaseValue instanceof Long) {
				date = new Date((long) databaseValue);
			} else if (databaseValue instanceof Date) {
				date = (Date) databaseValue;
			} else {
				logger.log(Level.WARNING, String.format("Cannot convert '%s' into XMLGregorianCalendar", databaseValue));
				return null;
			}
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(date);
			XMLGregorianCalendar c;
			try {
				c = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
				return c;
			} catch (DatatypeConfigurationException e) {
				logger.log(Level.SEVERE, "Cannot instanciate XMLGregorianCalendar", e);
			}
		}
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.ValueConverter#convertEMFValueToMongoDBValue(org.eclipse.emf.ecore.EDataType, java.lang.Object)
	 */
	@Override
	public Object convertEMFValueToMongoDBValue(EDataType eDataType, Object emfValue) {
		if (eDataType.getInstanceClass().equals(XMLGregorianCalendar.class)) {
			XMLGregorianCalendar c = (XMLGregorianCalendar) emfValue;
			return c.toGregorianCalendar().getTime();
		}
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.ValueConverter#isConverterForType(org.eclipse.emf.ecore.EDataType)
	 */
	@Override
	public boolean isConverterForType(EDataType eDataType) {
		if (eDataType.getInstanceClass().equals(XMLGregorianCalendar.class)) {
			return true;
		}
		return false;
	}

}
