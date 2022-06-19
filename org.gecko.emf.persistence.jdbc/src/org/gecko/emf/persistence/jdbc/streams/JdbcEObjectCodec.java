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
package org.gecko.emf.persistence.jdbc.streams;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.gecko.emf.persistence.converter.ValueConverter;
import org.gecko.emf.persistence.jdbc.context.JdbcInputContext;

/**
 * Maps the Jdbc result into an {@link EObject}
 * @author mark
 * @since 19.06.2022
 */
public class JdbcEObjectCodec implements JdbcInputMapper {
	
	private final Map<String, EStructuralFeature> mappableFeatures = new HashMap<String, EStructuralFeature>();
	private final AtomicLong counter = new AtomicLong();
	private final JdbcInputContext context;
	private final EClassProvider eClassProvider;
	private final ResultSet resultSet;
	private List<String> columns;

	/**
	 * Creates a new instance.
	 * @param context the input context
	 * @param resultEClass the resulting {@link EClass}, can be <code>null</code>
	 * @param typeColumn the type definition column name, can be <code>null</code>
	 */
	JdbcEObjectCodec(JdbcInputContext context, EClassProvider eClassProvider) {
		this.context = context;
		this.context.setMapper(this);
		this.resultSet = context.getResult();
		this.eClassProvider = eClassProvider;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.JdbcInputMapper#initialize()
	 */
	@Override
	public void initialize() throws SQLException {
		EClass resultEClass = eClassProvider.getConfiguredEClass();
		ResultSetMetaData metaData = resultSet.getMetaData();
		columns = new ArrayList<String>(metaData.getColumnCount());
		for (int c = 1; c <= metaData.getColumnCount();c++) {
			String columnName = metaData.getColumnName(c);
			columns.add(columnName);
		}
		// 
		if (resultEClass != null) {
			mappableFeatures.putAll(buildMappableFeaturesMap(resultEClass, columns));
		}

	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.JdbcInputMapper#hasNext()
	 */
	@Override
	public boolean hasNext() throws SQLException {
		return resultSet.next();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.JdbcInputMapper#next()
	 */
	@Override
	public EObject next() throws SQLException {
		// TODO It may be interesting to enable parsing per entry, so every data entry can of a different type
		// We have no type information yet, so we try to extract it from the first entry
		EClass resultEClass = eClassProvider.getConfiguredEClass();
		String typeColumn = eClassProvider.getTypeColumn();
		if (resultEClass == null) {
			String typeUri = resultSet.getString(typeColumn);
			resultEClass = eClassProvider.getEClass(context.getLoadResource().getResourceSet(), typeUri);
			// Continue until we find a EClass type in the column
			if (resultEClass == null) {
				;
			} else {
				mappableFeatures.putAll(buildMappableFeaturesMap(resultEClass, columns));
				// Rewind the ResultSet and go to the first row, to apply mapping for all entries of the current EClass
				//						resultSet.first();
			}
		}
		EObject eObject = EcoreUtil.create(resultEClass);
		mappableFeatures.forEach((k,v)->setValue(resultSet, v, eObject));
		counter.incrementAndGet();
		return eObject;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.JdbcInputMapper#close()
	 */
	@Override
	public void close() {
		try {
			resultSet.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Reflectively sets a value for a given structural feature
	 * @param resultSet the result set to get the data from
	 * @param feature the EStructuralFeature instance to be set 
	 * @param eObject the instance to the set value for the feature
	 * @return the pure value from the feature
	 */
	private Object setValue(ResultSet resultSet, EStructuralFeature feature, EObject eObject) {
		try {
			Object value = resultSet.getObject(feature.getName());
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
		} catch (SQLException e) {
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
		}

		return convertedValue;
	}

	/**
	 * Builds a {@link Map} of all {@link EStructuralFeature} of the given that match the column list.
	 * @param eClass The {@link EClass}, to get the {@link EStructuralFeature} from
	 * @param columns the list of column names
	 * @return a {@link Map} with the 
	 */
	private Map<? extends String, ? extends EStructuralFeature> buildMappableFeaturesMap(EClass eClass, List<String> columns) {
		Map<String, EStructuralFeature> allFeatures = eClass.getEAllStructuralFeatures().stream().collect(Collectors.toMap(EStructuralFeature::getName, Function.identity()));
		return allFeatures.entrySet().stream().filter(e->columns.contains(e.getKey().toUpperCase())).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
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

}
