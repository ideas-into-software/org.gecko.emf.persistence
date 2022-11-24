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
package org.gecko.emf.persistence.codec;

import static java.util.function.Predicate.not;
import static org.gecko.emf.persistence.helper.PersistenceHelper.getElementNameLower;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.emf.persistence.ConverterService;
import org.gecko.emf.persistence.PersistenceException;
import org.gecko.emf.persistence.converter.ValueConverter;
import org.gecko.emf.persistence.helper.PersistenceHelper;
import org.gecko.emf.persistence.input.InputContext;

/**
 * Maps the result into an {@link EObject}
 * @author Mark Hoffmann
 * @since 19.06.2022
 */
public abstract class BasicEObjectCodec<RESULT, T extends InputContext<RESULT>> {
	
	protected final Map<String, EStructuralFeature> mappableFeatures = new HashMap<String, EStructuralFeature>();
	private final EClassProvider eClassProvider;
	private final T context;
	private final RESULT result;
	private final AtomicLong counter = new AtomicLong();
	private List<String> columns;

	/**
	 * Creates a new instance.
	 * @param context the input context
	 * @param resultEClass the resulting {@link EClass}, can be <code>null</code>
	 * @param typeColumn the type definition column name, can be <code>null</code>
	 */
	protected BasicEObjectCodec(T context, EClassProvider eClassProvider) {
		this.context = context;
		this.result = context.getResult();
		this.eClassProvider = eClassProvider;
		doInitialize();
	}
	
	protected abstract void doInitialize();
	
	/**
	 * Returns the columns.
	 * @return the columns
	 */
	public List<String> getColumns() {
		if (columns == null) {
			columns = new LinkedList<>();
		}
		return columns;
	}
	
	/**
	 * Returns the eClassProvider.
	 * @return the eClassProvider
	 */
	public EClassProvider getEClassProvider() {
		return eClassProvider;
	}
	
	/**
	 * Returns the result.
	 * @return the result
	 */
	public RESULT getResult() {
		return result;
	}
	
	/**
	 * Returns the context.
	 * @return the context
	 */
	public T getContext() {
		return context;
	}
	
	/**
	 * Read one data entry from the table
	 * @return the mapped {@link EObject}
	 * @throws PersistenceException
	 */
	public EObject readEntry() throws PersistenceException {
		// TODO It may be interesting to enable parsing per entry, so every data entry can of a different type
		// We have no type information yet, so we try to extract it from the first entry
		assert(eClassProvider != null);
		EClass resultEClass = eClassProvider.getConfiguredEClass();
		String typeColumn = eClassProvider.getTypeColumn();
		if (resultEClass == null) {
			if (typeColumn == null) {
				throw new PersistenceException("Cannot determine EClass without type information");
			}
			String typeUri = readTypeColumn(typeColumn);
			resultEClass = eClassProvider.getEClass(context.getLoadResource().getResourceSet(), typeUri);
			// Continue until we find a EClass type in the column
			if (resultEClass == null) {
				throw new PersistenceException("Cannot determine EClass for type: " + typeUri);
			} else {
			}
		}
		mappableFeatures.putAll(buildMappableFeaturesMap(resultEClass, columns));
		EObject eObject = EcoreUtil.create(resultEClass);
		mappableFeatures.forEach((k,v)->setValue(v, eObject));
		counter.incrementAndGet();
		return eObject;
	}
	
	/**
	 * Returns the object from the RESULT for the given column name.   
	 * @param columnName the name of the column to get the result from.
	 * @return the value or <code>null</code>
	 * @throws PersistenceException
	 */
	protected abstract Object getObject(String columnName) throws PersistenceException;
	
	/**
	 * Read the EClass type from a special 'type-column', if available 
	 * @param typeColumn name of the column with type information
	 * @return
	 * @throws PersistenceException
	 */
	protected String readTypeColumn(String typeColumn) throws PersistenceException {
		assert(typeColumn != null);
		return doReadTypeColumn(typeColumn);
	}
	
	/**
	 * Read the EClass type from a special 'type-column', if available 
	 * @param typeColumn name of the column with type information
	 * @return
	 * @throws PersistenceException
	 */
	protected abstract String doReadTypeColumn(String typeColumn) throws PersistenceException;
	
	/**
	 * Reflectively sets a value for a given structural feature. The RESULT values are
	 * fetched from the {@link BasicEObjectCodec#getObject(String)}
	 * @param resultSet the result set to get the data from
	 * @param feature the EStructuralFeature instance to be set 
	 * @param eObject the instance to the set value for the feature
	 * @return the pure value from the feature
	 */
	protected Object setValue(EStructuralFeature feature, EObject eObject) {
		try {
			String featureName = getElementNameLower(feature);
			Object value = getObject(featureName);
			if (Objects.isNull(value)) {
				return value;
			}
			if (feature instanceof EAttribute) {
				EAttribute attribute = (EAttribute) feature;
				value = convertToEMFValue(attribute.getEAttributeType(), value);
				if (value != null) {
					eObject.eSet(feature, value);
				}
				return value;
			} else {
				System.out.println("ERefernce Mapping not supported yet");
			}
		} catch (PersistenceException e) {
			System.out.println("Error setting value for " + feature.getName());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Converts the DB value into an EMF value using the converter service
	 * 
	 * @param eDataType the value type
	 * @param dbValue the value
	 * @return the converted value
	 */
	protected Object convertToEMFValue(EDataType eDataType, Object dbValue) {
		assert(eDataType != null);
		if (dbValue == null) {
			return dbValue;
		}
		Object convertedValue = dbValue;

		if (!isNativeType(eDataType)) {
			if (context.getConverter().isEmpty()) {
				return convertedValue;
			}
			ConverterService converterService = context.getConverter().get();
			// Types not native to MongoDB are stored as strings and must be converted to the proper object type by EMF
			ValueConverter valueConverter = converterService.getConverter(eDataType);
			if (valueConverter != null) {
				convertedValue = valueConverter.convertDBValueToEMFValue(eDataType, dbValue);
			} else {
				System.out.println("No ValueConverter found for data type " + eDataType.getName() + " and value " + dbValue);
			}
		} else {
			if (dbValue.getClass().isPrimitive()) {
				convertedValue = forceBox(dbValue);
			}
			convertedValue =  eDataType.getEPackage().getEFactoryInstance().createFromString(eDataType, convertedValue.toString());
		}

		return convertedValue;
	}
	
	public static <T> T forceBox(T t) { // compiler will add the conversion at the call site
	    return t; 
	}
	
//	public static void convert(Map container, String key, Class toType) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
//	    Method method=toType.getMethod("valueOf", String.class);
//	    if(container.containsKey(key)){
//	    	Character.va
//	        container.put(key, method.invoke(toType, container.get(key)));
//	    }
//	}
	
	/**
	 * Builds a {@link Map} of all {@link EStructuralFeature} of the given that match the column list.
	 * @param eClass The {@link EClass}, to get the {@link EStructuralFeature} from
	 * @param columns the list of column names
	 * @return a {@link Map} with the 
	 */
	protected Map<? extends String, ? extends EStructuralFeature> initializeMappableFeatures(EClass eClass) {
		Map<String, EStructuralFeature> allFeatures = eClass.getEAllStructuralFeatures()
				.stream()
				.filter(not(EStructuralFeature::isTransient))
				.collect(Collectors.toMap(PersistenceHelper::getElementNameLower, Function.identity()));
		return allFeatures;
	}

	/**
	 * Builds a {@link Map} of all {@link EStructuralFeature} of the given that match the column list.
	 * @param eClass The {@link EClass}, to get the {@link EStructuralFeature} from
	 * @param columns the list of column names
	 * @return a {@link Map} with the 
	 */
	protected Map<? extends String, ? extends EStructuralFeature> buildMappableFeaturesMap(EClass eClass, List<String> columns) {
		return initializeMappableFeatures(eClass).entrySet().stream().filter(e->columns.contains(e.getKey().toUpperCase())).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
	
	/**
	 * This function determines whether or not the given EDataType can be represented natively by MongoDB.
	 * 
	 * @param dataType the EMF data type to check
	 * @return true if the data type can be represented natively by MongoDB; false otherwise
	 */
	public static boolean isNativeType(EDataType dataType) {
		if (dataType instanceof EEnum) {
			return true;
		}
		String instanceClassName = dataType.getInstanceClassName();
		//@formatter:off
		return
			instanceClassName == "java.lang.String"  ||
			instanceClassName == "int"               ||
			instanceClassName == "boolean"           ||
			instanceClassName == "float"             ||
			instanceClassName == "long"              ||
			instanceClassName == "double"            ||
			instanceClassName == "java.util.Date"    ||
			instanceClassName == "java.util.Calendar"||
			instanceClassName == "short"             ||
			instanceClassName == "char"             ||
			instanceClassName == "byte[]"            ||
			instanceClassName == "byte"              ||
			instanceClassName == "java.lang.Integer" ||
			instanceClassName == "java.lang.Character" ||
			instanceClassName == "java.lang.Boolean" ||
			instanceClassName == "java.lang.Long"    ||
			instanceClassName == "java.lang.Float"   ||
			instanceClassName == "java.lang.Double"  ||
			instanceClassName == "java.lang.Short"   ||
			instanceClassName == "java.lang.Byte";
		//@formatter:on
	}
	
	/**
	 * This function determines whether or not the given EDataType can be represented natively by MongoDB.
	 * 
	 * @param dataType the EMF data type to check
	 * @return true if the data type can be represented natively by MongoDB; false otherwise
	 */
	public static Class<?> toBoxedType(EDataType dataType) {
		if (dataType == null || dataType.getInstanceClass() == null) {
			return null;
		}
		if (dataType.getInstanceClass().isPrimitive()) {
			return primitiveWrapperMap.get(dataType.getInstanceClass());
		} else {
			return dataType.getInstanceClass();
		}
	}
	
	private static final Map<Class<?>, Class<?>> primitiveWrapperMap =
            Map.of(boolean.class, Boolean.class,
                    byte.class, Byte.class,
                    char.class, Character.class,
                    double.class, Double.class,
                    float.class, Float.class,
                    int.class, Integer.class,
                    long.class, Long.class,
                    short.class, Short.class);

}
