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

import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.converter.DefaultConverterService;
import org.gecko.emf.persistence.converter.ValueConverter;
import org.gecko.emf.persistence.jdbc.JdbcPersistenceConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Converter service for Jdbc. We just take the default converter
 * @author bhunt
 */
@Component(name="JdbcConverterService", service=ConverterService.class, immediate=true, property = JdbcPersistenceConstants.PERSISTENCE_FILTER_PROP)
public class JdbcConverterService extends DefaultConverterService {
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.ConverterService#addConverter(org.gecko.emf.persistence.converter.ValueConverter)
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
	 * @see org.gecko.emf.persistence.ConverterService#removeConverter(org.gecko.emf.persistence.converter.ValueConverter)
	 */
	@Override
	public void removeConverter(ValueConverter converter){
		synchronized (converters) {
			converters.remove(converter);
		}
	}

}
