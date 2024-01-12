/*******************************************************************************
 * Copyright (c) 2012 Bryan Hunt & Ed Merks.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt & Ed Merks - initial API and implementation
 *******************************************************************************/

package org.gecko.emf.mongo.codecs.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bson.BsonBinary;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.gecko.emf.mongo.ConverterService;
import org.gecko.emf.mongo.Keywords;
import org.gecko.emf.mongo.MongoUtils;
import org.gecko.emf.mongo.Options;
import org.gecko.emf.mongo.ValueConverter;

/**
 * This class builds a DBObject from an EMFObject. This builder is designed to be extensible,
 * but you must be aware of the assumptions made by each of the functions in the builder.
 * 
 * This class is thread safe.
 * 
 * @author bhunt
 */
public class DBObjectBuilderImpl implements DBObjectBuilder {

	private final Logger logger = Logger.getLogger(DBObjectBuilderImpl.class.getName());
	private final ConverterService converterService;
	private final XMLResource.URIHandler uriHandler;
	private final boolean serializeDefaultAttributeValues;
	private final CodecRegistry codecRegistry;
	private final Map<?, ?> options;
	private final Map<Object, Object> response;
	private boolean serializeAllEClassUris;
	private boolean useExtendedMetadata = false;
	private boolean serializeSuperTypes = false;
	private List<EReference> sourceReference = new LinkedList<>();
	private final String eClassKey;
	private final String superTypeKey;
	private final String eProxyUriKey;

	/**
	 * @param converterService the service to use when converting attribute values
	 * @param uriHandler the handler for creating proxy URIs
	 * @param serializeDefaultAttributeValues true causes default attribute values to be written to the DBObject;
	 *          false causes default attribute values to be skipped
	 * @param resourceSet 
	 * @param options the resource save options
	 */
	public DBObjectBuilderImpl(ConverterService converterService, XMLResource.URIHandler uriHandler, CodecRegistry codecRegistry, Map<?, ?> options) {
		this.converterService = converterService;
		this.uriHandler = uriHandler;
		this.codecRegistry = codecRegistry;
		this.options = options;
		this.response = getResponseOptions(options);
		this.serializeDefaultAttributeValues = getSerializeDefaultOption(options);
		this.serializeAllEClassUris = getSerializeAllEClassUrisOption(options);
		this.serializeSuperTypes  = getSerializeSuperTypes(options);
		this.useExtendedMetadata = Options.isUseExtendedMetadata(options);
		this.eClassKey = Options.getEClassKey(options);
		this.superTypeKey = Options.getSuperType(options);
		this.eProxyUriKey = Options.getProxyUriKey(options);
	}
	
	/**
	 * Returns <code>true</code>, if the super types of the element to store should be saved in an field
	 * '_superType'
	 * @param options the option map to check against
	 * @return <code>true</code>, if the suüper type field should be created
	 */
	private boolean getSerializeSuperTypes(Map<?, ?> options) {
		Boolean serializeSuperTypes = (Boolean) options.get(Options.OPTION_STORE_SUPERTYPE);
		return Boolean.TRUE.equals(serializeSuperTypes);
	}

	/**
	 * Returns <code>true</code>, if the every eClass URIi should be written for every containment Object.
	 * If 
	 * @param options the option map to check against
	 * @return <code>true</code>, if the default values should be serialized too
	 */
	private boolean getSerializeAllEClassUrisOption(Map<?, ?> options) {
		Boolean serializeOption = (Boolean) options.get(Options.OPTION_SERIALIZE_ALL_ECLASS_URIS);
		return Boolean.TRUE.equals(serializeOption);
	}

	/**
	 * Returns <code>true</code>, if the default values should be serialized too
	 * @param options the option map to check against
	 * @return <code>true</code>, if the default values should be serialized too
	 */
	private boolean getSerializeDefaultOption(Map<?, ?> options) {
		Boolean serializeOption = (Boolean) options.get(Options.OPTION_SERIALIZE_DEFAULT_ATTRIBUTE_VALUES);
		return Boolean.TRUE.equals(serializeOption);
	}

	/**
	 * Returns the response options map from the save options map
	 * @param options the save options map
	 * @return the response options map from the save options map
	 */
	@SuppressWarnings("unchecked")
	private Map<Object, Object> getResponseOptions(Map<?, ?> options) {
		return (Map<Object, Object>)options.get(URIConverter.OPTION_RESPONSE);
	}

	/* (non-Javadoc)
	 * @see de.dim.spark.test.mongo.builder.DBObjectBuilder#buildDBObject(org.bson.BsonWriter, org.eclipse.emf.ecore.EObject, org.bson.codecs.EncoderContext)
	 */
	@Override
	public void buildDBObject(BsonWriter writer, EObject eObject, EncoderContext context) 	{
		// Build a MongoDB object from the EMF object.
		EClass eClass = eObject.eClass();

		// We will write the _id only for the rootObject 
		if(eObject.eContainer() == null){
			writeMongoIdAttribute(writer, eObject);
		}

		if(serializeAllEClassUris 
				|| sourceReference.size() == 0 // if the source Refs List is empty, this must be the root object
				|| !getLastReference(sourceReference).getEReferenceType().equals(eObject.eClass())){ // we only write the URI if it is something else then the named Type
			
			// We have to add the URI of the class to the object so that we can
			// reconstruct the EMF object when we read it back out of MongoDB.
			writer.writeString(eClassKey, EcoreUtil.getURI(eClass).toString());
		}
		
		if(serializeSuperTypes) { // we only write the URI if it is something else then the named Type
			EList<EClass> superTypes = eClass.getEAllSuperTypes();
			if (!superTypes.isEmpty()) {
				writer.writeStartArray(superTypeKey);
				superTypes.forEach(c->writer.writeString(EcoreUtil.getURI(c).toString()));
				writer.writeEndArray();
			}
		}
		
		
		// Save the XML extrinsic id if necessary
		buildExtrinsicID(writer, eObject);

		// All attributes are mapped as key / value pairs with the key being the attribute name.

		for (EAttribute attribute : eClass.getEAllAttributes()) {
			if (!attribute.isTransient() && 
					(eObject.eIsSet(attribute) || 
							(!attribute.isUnsettable() && serializeDefaultAttributeValues)))
				buildAttribute(writer, eObject, attribute, context);
		}

		// All references are mapped as key / value pairs with the key being the reference name.

		for (EReference reference : eClass.getEAllReferences()) {
			if (!reference.isTransient() && 
					eObject.eIsSet(reference) && 
					!(reference.getEOpposite() != null && 
					reference.isContainer()))
				buildReference(writer, eObject, reference, context);
		}
		
		// We will write the _timeStamp only for the rootObject 
		if(eObject.eContainer() == null){
			long timeStamp = System.currentTimeMillis();
			writePrimitiveValue(Keywords.TIME_STAMP_KEY, timeStamp, writer);
			if(response != null){
				response.put(URIConverter.RESPONSE_TIME_STAMP_PROPERTY, timeStamp);
			}
		}

	}
	
	/**
	 * Extracts the last added sourceReference or null
	 * @param sourceReferences the list of sourceReferences
	 * @return null or the last {@link EReference} used
	 */
	private EReference getLastReference(List<EReference> sourceReferences) {
		if(sourceReferences.size() == 0){
			return null;
		}
		
		return sourceReferences.get(sourceReferences.size() -1);
	}

	/**
	 * Write the objects id to the writer
	 * @param writer the {@link BsonWriter} to use
	 * @param eObject the {@link EObject} to write 
	 * @throws IOException if the uri is not correct
	 */
	private void writeMongoIdAttribute(BsonWriter writer, EObject eObject){
		Boolean useIdAttributeAsPrimaryKey = (Boolean) options.get(Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY);
		Object id = null;
		if (useIdAttributeAsPrimaryKey == null || useIdAttributeAsPrimaryKey) {
			EAttribute idAttribute = eObject.eClass().getEIDAttribute();
			if (idAttribute != null) {
				id = eObject.eGet(idAttribute);
			}
		} else {
			return;
		}
		if(id instanceof ObjectId){
			writer.writeObjectId(Keywords.ID_KEY, (ObjectId) id);
		} else if(id instanceof String && ObjectId.isValid((String) id)){
			ObjectId objectId = new ObjectId((String) id);
			writer.writeObjectId(Keywords.ID_KEY, objectId);
		} else {
			writePrimitiveValue(Keywords.ID_KEY, id, writer);
		}
	}

	/**
	 * Serializes the attribute from the EMF object into the DBObject
	 * Feature maps are delegated to buildFeatureMap() and non-native arrays to
	 * buildAttributeArray(). The converter service is used for value conversion.
	 * Attribute values are mapped in the DBObject using the attribute
	 * name as the key.
	 * 
	 * @param writer the {@link BsonWriter} to write into
	 * @param eObject the EMF object to serialize
	 * @param attribute the attribute to serialize from the EMF object
	 * @param context the encoder context
	 */
	protected void buildAttribute(BsonWriter writer, EObject eObject, EAttribute attribute, EncoderContext context) {
		Object value = eObject.eGet(attribute);

		if (FeatureMapUtil.isFeatureMap(attribute))
			buildFeatureMap(writer, attribute, value, context);
		else if (attribute.isMany())
			buildAttributeArray(writer, attribute, value);
		else
			buildAttributeValue(writer, attribute, value);
	}

	/**
	 * Serializes an attribute as a java.util.ArrayList. Each value is
	 * converted using the converter service.
	 * 
	 * @param writer the {@link BsonWriter} to write into
	 * @param attribute the attribute to serialize from the EMF object
	 * @param values the attribute values to serialize
	 */
	protected void buildAttributeArray(BsonWriter writer, EAttribute attribute, Object values) {
		EDataType eDataType = attribute.getEAttributeType();
		List<?> eValues = (List<?>) values;
		if (!MongoUtils.isNativeType(eDataType)) {
			List<Object> converted = new ArrayList<>(eValues.size());
			for (Object rawValue : eValues) {
				converted.add(convertEMFToMongoValue(attribute.getEAttributeType(), rawValue));
			}
			eValues = converted;
		}
		String name = MongoUtils.getNameByEStructuralFeature(attribute, useExtendedMetadata);
		writer.writeStartArray(name);
		for (Object object : eValues) {
			writePrimitiveValueNoName(object, writer);
		}
		writer.writeEndArray();
	}

	/**
	 * Converts the attribute value if needed
	 * 
	 * @param writer the {@link BsonWriter} to write into
	 * @param attribute the attribute to serialize from the EMF object
	 * @param value the value of the attribute from the EMF object
	 */
	protected void buildAttributeValue(BsonWriter writer, EAttribute attribute, Object value) {
		EDataType eDataType = attribute.getEAttributeType();
		if (!MongoUtils.isNativeType(eDataType)) {
			value = convertEMFToMongoValue(eDataType, value);
		}
		String name = MongoUtils.getNameByEStructuralFeature(attribute, useExtendedMetadata);
		writePrimitiveValue(name, value, writer);
	}

	/**
	 * Sets the extrensic ID if it exists and the resource is of type XMLResource. The
	 * extrensic ID is mapped to the key EXTRINSIC_ID_KEY.
	 * 
	 * @param writer the {@link BsonWriter} to write into
	 * @param eObject the EMF object to serialize
	 */
	protected void buildExtrinsicID(BsonWriter writer, EObject eObject) {
		Resource resource = eObject.eResource();

		if (resource instanceof XMLResource) {
			String id = ((XMLResource) resource).getID(eObject);

			if (id != null) {
				writer.writeString(Keywords.EXTRINSIC_ID_KEY, id);
			}
		}
	}

	/**
	 * Serializes a feature map from the attribute value. Feature maps
	 * of references are delegated to buildReferencedObject to build
	 * the referenced object.
	 * 
	 * @param writer the {@link BsonWriter} to write into
	 * @param attribute the emf attribute being serialized
	 * @param value the feature map
	 */
	protected void buildFeatureMap(BsonWriter writer, EAttribute attribute, Object value, EncoderContext context) {
		FeatureMap.Internal featureMap = (FeatureMap.Internal) value;
		Iterator<FeatureMap.Entry> iterator = featureMap.basicIterator();
		String name = MongoUtils.getNameByEStructuralFeature(attribute, useExtendedMetadata);
		writer.writeStartArray(name);
		while (iterator.hasNext()) {
			FeatureMap.Entry entry = iterator.next();
			context.encodeWithChildContext(codecRegistry.get(FeatureMap.Entry.class), writer, entry);
		}
		writer.writeEndArray();
	}

	/**
	 * Serializes a reference value from the EMF object. References with cardinality greater
	 * than one are stored as a java.util.ArrayList. Reference values are mapped in the
	 * DBObject using the reference name as the key. Building of the referenced object is
	 * delegated to buildReferencedObject().
	 * 
	 * @param writer the {@link BsonWriter} to write into
	 * @param eObject the EMF object to serialize
	 * @param reference
	 */
	protected void buildReference(BsonWriter writer, EObject eObject, EReference reference, EncoderContext context) {
		Object value = eObject.eGet(reference, false);
		String name = MongoUtils.getNameByEStructuralFeature(reference, useExtendedMetadata);
		if (reference.isMany()) {
			// One to many reference

			@SuppressWarnings("unchecked")
			List<EObject> targetObjects = ((InternalEList<EObject>) value).basicList();

			writer.writeStartArray(name);
			for (EObject targetObject : targetObjects) {
				buildReferencedObject(writer, reference, targetObject, context);
			}
			writer.writeEndArray();
		} else if (value != null) {
			// One to one reference

			EObject targetObject = (EObject) value;
			writer.writeName(name);
			buildReferencedObject(writer, reference, targetObject, context);
		}
	}

	/* (non-Javadoc)
	 * @see de.dim.spark.test.mongo.builder.DBObjectBuilder#buildReferencedObject(org.bson.BsonWriter, org.eclipse.emf.ecore.EReference, org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public void buildReferencedObject(BsonWriter writer, EReference eReference, EObject targetObject, EncoderContext context) {
		InternalEObject internalEObject = (InternalEObject) targetObject;
		URI eProxyURI = internalEObject.eProxyURI();
		if (eProxyURI != null) {
			writer.writeStartDocument();
			writer.writeString(eProxyUriKey, uriHandler.deresolve(eProxyURI).toString());
			if(serializeAllEClassUris || !eReference.getEType().equals(targetObject.eClass())){
				writer.writeString(eClassKey, EcoreUtil.getURI(targetObject.eClass()).toString());
			}
			writer.writeEndDocument();
			return;
		} else if (!eReference.isContainment() || 
				(eReference.isResolveProxies() && 
						internalEObject.eDirectResource() != null)) {
			//			// Cross-document containment, or non-containment reference - build a proxy
			writer.writeStartDocument();
			writer.writeString(eProxyUriKey, uriHandler.deresolve(EcoreUtil.getURI(targetObject)).toString());
			if(serializeAllEClassUris || !eReference.getEType().equals(targetObject.eClass())){
				writer.writeString(eClassKey, EcoreUtil.getURI(targetObject.eClass()).toString());
			}
			writer.writeEndDocument();
			return;
		} else {
			//We have to tempor
			if(!serializeAllEClassUris){
				sourceReference.add(eReference);
			}
			context.encodeWithChildContext(codecRegistry.get(EObject.class), writer, targetObject);
			sourceReference.remove(eReference);
			return;
		}
	}

	/**
	 * Converts the EMF value into a MongoDB value using the converter service
	 * 
	 * @param eDataType the value type
	 * @param emfValue the value
	 * @return the converted value
	 */
	public Object convertEMFToMongoValue(EDataType eDataType, Object emfValue) {
		Object convertedValue = emfValue;
		if (converterService == null) {
			logger.warning("No ConverterService was found for data type " + eDataType.getName() + ". Returning original value");
			return convertedValue;
		}
		// Types not native to MongoDB are stored as strings and must be converted to the proper object type by EMF
		ValueConverter valueConverter = converterService.getConverter(eDataType);
		if (valueConverter != null) {
			convertedValue = valueConverter.convertEMFValueToMongoDBValue(eDataType, emfValue);
		} else {
			logger.warning("No ValueConverter found for data type " + eDataType.getName() + " and value " + emfValue);
		}
		return convertedValue;
	}


	/**
	 * Writes a primitive value to the {@link BsonWriter}. If the value is <code>null</code>, nothing will be written
	 * @param value the value
	 * @param writer the writer to write into
	 */
	public void writePrimitiveValueNoName(Object value, BsonWriter writer) {
		if (value == null) {
			return;
		}
		assert writer != null;
		
		if(!(value instanceof byte[]) && value.getClass().isArray()) {
			Object[] array = (Object[]) value;
			writer.writeStartArray();
			for (Object object : array) {
				writePrimitiveValueNoName(object, writer);
			}
			writer.writeEndArray();
		}
		else if (value instanceof String) {
			writer.writeString(value.toString());
		} else if (value instanceof Long) {
			Long longValue = (Long) value;
			writer.writeInt64(longValue.longValue());
		} else if (value instanceof Integer) {
			Integer intValue = (Integer) value;
			writer.writeInt32(intValue.intValue());
		} else if (value instanceof Date) {
			Date dateValue = (Date) value;
			writer.writeDateTime(dateValue.getTime());
		} else if (value instanceof Calendar) {
			Calendar calendarValue = (Calendar) value;
			writer.writeDateTime(calendarValue.getTime().getTime());
		} else if (value instanceof Double ) {
			Double doubleValue = (Double) value;
			writer.writeDouble(doubleValue.doubleValue());
		} else if (value instanceof Float ) {
			Float floatValue = (Float) value;
			writer.writeDouble(floatValue.doubleValue());
		} else if (value instanceof Boolean) {
			Boolean booleanValue = (Boolean) value;
			writer.writeBoolean(booleanValue.booleanValue());
		} else if (value instanceof Byte) {
			byte byteValue = (byte) value;
			writer.writeBinaryData(new BsonBinary(new byte[]{byteValue}));
		} else if (value instanceof byte[]) {
			byte[] byteValue = (byte[]) value;
			writer.writeBinaryData(new BsonBinary(byteValue));
		} else if (value instanceof Enum<?>) {
			Enum<?> enumValue = (Enum<?>) value;
			writer.writeString(enumValue.name());
		} else {
			writer.writeUndefined();
		}
	}

	/**
	 * Writes a primitive value to the {@link BsonWriter}. If the value is <code>null</code>, nothing will be written
	 * @param name the name of the field
	 * @param value the value
	 * @param writer the writer to write into
	 */
	public void writePrimitiveValue(String name, Object value, BsonWriter writer) {
		if (value == null) {
			return;
		}
		assert name != null;
		assert writer != null;
		
		if(!(value instanceof byte[]) && value.getClass().isArray()) {
			Object[] array = (Object[]) value;
			writer.writeStartArray();
			for (Object object : array) {
				writePrimitiveValueNoName(object, writer);
			}
			writer.writeEndArray();
		}
		else if (value instanceof String) {
			writer.writeString(name, value.toString());
		} else if (value instanceof Character) {
			writer.writeString(name, value.toString());
		} else if (value instanceof Long) {
			Long longValue = (Long) value;
			writer.writeInt64(name, longValue.longValue());
		} else if (value instanceof Integer) {
			Integer intValue = (Integer) value;
			writer.writeInt32(name, intValue.intValue());
		} else if (value instanceof Short) {
			Short shortValue = (Short) value;
			writer.writeInt32(name, shortValue.intValue());
		} else if (value instanceof Date) {
			Date dateValue = (Date) value;
			writer.writeDateTime(name, dateValue.getTime());
		} else if (value instanceof Double) {
			Double doubleValue = (Double) value;
			writer.writeDouble(name, doubleValue.doubleValue());
		} else if (value instanceof Float ) {
			Float floatValue = (Float) value;
			writer.writeDouble(name, floatValue.doubleValue());
		} else if (value instanceof Boolean) {
			Boolean booleanValue = (Boolean) value;
			writer.writeBoolean(name, booleanValue.booleanValue());
		} else if (value instanceof Byte) {
			byte byteValue = (byte) value;
			writer.writeBinaryData(name, new BsonBinary(new byte[]{byteValue}));
		} else if (value instanceof byte[]) {
			byte[] byteValue = (byte[]) value;
			writer.writeBinaryData(name, new BsonBinary(byteValue));
		} else if (value instanceof Enum<?>) {
			Enum<?> enumValue = (Enum<?>) value;
			String writeValue = enumValue.name();
			if( enumValue instanceof Enumerator) {
				writeValue = ((Enumerator) enumValue).getName();
				if (Boolean.TRUE.equals(options.get(Options.OPTION_USE_ENUM_LITERAL))) {
					writeValue = ((Enumerator)enumValue).getLiteral();
				}
			}
			writer.writeString(name, writeValue);
		} else {
			writer.writeUndefined(name);
		}
	}
}
