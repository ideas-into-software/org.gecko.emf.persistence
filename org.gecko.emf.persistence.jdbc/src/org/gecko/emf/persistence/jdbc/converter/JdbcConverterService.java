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
package org.gecko.emf.persistence.jdbc.converter;

import java.util.LinkedList;

import org.eclipse.emf.ecore.EDataType;
import org.gecko.emf.persistence.ConverterService;
import org.gecko.emf.persistence.ValueConverter;
import org.gecko.emf.persistence.jdbc.JdbcPersistenceConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * This class is thread safe
 * @author bhunt
 */
@Component(name="JdbcConverterService", service=ConverterService.class, immediate=true, property = JdbcPersistenceConstants.PERSISTENCE_FILTER_PROP)
public class JdbcConverterService implements ConverterService {
	
	private LinkedList<ValueConverter> converters;
	
	public JdbcConverterService() {
		this.converters = new LinkedList<ValueConverter>();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.converter.ConverterService#addConverter(org.gecko.emf.mongo.converter.ValueConverter)
	 */
	@Override
	@Reference(unbind="removeConverter", cardinality=ReferenceCardinality.MULTIPLE, policy=ReferencePolicy.DYNAMIC, target = JdbcPersistenceConstants.PERSISTENCE_FILTER)
	public void addConverter(ValueConverter converter)	{
		// The converter must be added at the beginning of the list so that the default converter is considered last
		synchronized (converters) {
			converters.addFirst(converter);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.converter.ConverterService#getConverter(org.eclipse.emf.ecore.EDataType)
	 */
	@Override
	public ValueConverter getConverter(EDataType eDataType) {
		synchronized (converters) {
			return converters.
					stream().
					filter((c)->c.isConverterForType(eDataType)).
					findFirst().
					orElseThrow(()->new IllegalStateException("The default converter was not found - this should never happen"));
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.mongo.converter.ConverterService#removeConverter(org.gecko.emf.mongo.converter.ValueConverter)
	 */
	@Override
	public void removeConverter(ValueConverter converter){
		synchronized (converters) {
			converters.remove(converter);
		}
	}

}
