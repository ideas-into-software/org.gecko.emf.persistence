package org.gecko.emf.persistence.converter;

import java.util.LinkedList;

import org.eclipse.emf.ecore.EDataType;
import org.gecko.emf.persistence.api.ConverterService;

/**
 * This class is thread safe
 * @author bhunt
 */
public abstract class DefaultConverterService implements ConverterService {
	
	protected LinkedList<ValueConverter> converters;
	
	public DefaultConverterService() {
		this.converters = new LinkedList<ValueConverter>();
		converters.add(new ArrayConverter());
		converters.add(new DefaultConverter());
		converters.add(new XMLGregorianCalendarConverter());
		converters.add(new BigDecimalConverter());
		converters.add(new BigIntegerConverter());
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.ConverterService#getConverter(org.eclipse.emf.ecore.EDataType)
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

}
