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
 *     Bryan Hunt
 */
package org.gecko.emf.persistence.mongo.codecs.builder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.bson.BsonBinary;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.Document;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLResource.URIHandler;
import org.eclipse.emf.ecore.xmi.impl.URIHandlerImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.Keywords;
import org.gecko.emf.persistence.api.Options;
import org.gecko.emf.persistence.converter.ValueConverter;
import org.gecko.emf.persistence.mongo.util.MongoUtils;

/**
 * This class builds EMF EObjects from a MongoDB DBObject. This builder is designed to be extensible,
 * but you must be aware of the assumptions made by each of the functions in the builder.
 * 
 * This class is thread safe.
 * 
 * @author bhunt
 */
public class EObjectBuilderImpl implements EObjectBuilder {

	private final Logger logger = Logger.getLogger("o.e.e.m.eObjectBuilder");
	private ConverterService converterService;
	private Map<String, EClass> eClassCache;
	private final CodecRegistry codecRegistry;
	private final ResourceSet resourceSet;
	private final Map<?, ?> options;
	private Map<Object, Object> response;
	private URI baseUri;
	private final static XMIResourceFactoryImpl factory = new XMIResourceFactoryImpl();
	private final List<Resource> resourceCache;
	private final URIHandler uriHandler;
	private final String eClassKey;
	private final String eProxyUriKey;
	private List<EReference> sourceReferences = new LinkedList<>();
	private boolean useExtendedMetadata = false;

	/**
	 * Constructs an object builder without an EClass cache.
	 * 
	 * @param converterService the service to use when converting attribute values
	 * @param uriHandler the handler for creating proxy URIs
	 * @param baseUri the basic {@link URI} to use 
	 * @param includeAttributesForProxyReferences true if you want attribute values to be set on proxy references; false otherwise
	 * @param options the resource load options
	 */
	public EObjectBuilderImpl(ConverterService converterService, URI baseUri, CodecRegistry codecRegistry, ResourceSet resourceSet, Map<?, ?> options, List<Resource> resourceCache) {
		this(converterService, baseUri, new ConcurrentHashMap<>(), codecRegistry, resourceSet, options, resourceCache);

	}

	/**
	 * Constructs an object builder with an optional EClass cache.
	 * 
	 * @param converterService the service to use when converting attribute values
	 * @param baseUri the basic {@link URI} to use
	 * @param includeAttributesForProxyReferences true if you want attribute values to be set on proxy references; false otherwise
	 * @param eClassCache the cache to use to EClass lookups when building the EObject instance - may be null
	 * @param options the Resource load options
	 */
	public EObjectBuilderImpl(ConverterService converterService, URI baseUri, Map<String, EClass> eClassCache, CodecRegistry codecRegistry, ResourceSet resourceSet, Map<?, ?> options, List<Resource> resourceCache) {
		this.baseUri = baseUri;
		this.options = options;
		this.converterService = converterService;
		this.eClassCache = eClassCache;
		this.codecRegistry = codecRegistry;
		this.resourceSet = resourceSet;
		this.resourceCache = resourceCache;
		this.response = getResponseOptions(this.options);
		this.uriHandler = getURIHandler(this.options);
		this.useExtendedMetadata = Options.isUseExtendedMetadata(this.options);
		this.eClassKey = Options.getEClassKey(options);
		this.eProxyUriKey = Options.getProxyUriKey(options);
	}

	/**
	 * looks for the {@link XMLResource#OPTION_URI_HANDLER} or creates a new instance
	 * @param options the save options {@link Map}
	 * @return the UriHandler desired
	 */
	private URIHandler getURIHandler(Map<?, ?> options) {
		URIHandler handler = (URIHandler) options.get(XMLResource.OPTION_URI_HANDLER);
		if(handler == null){
			handler = new URIHandlerImpl();
			handler.setBaseURI(baseUri);
		}
		return handler;
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

	/**
	 * Sets the extrensic ID if it exists and the resource is of type XMLResource. The
	 * extrensic ID is expected to be mapped to the key EXTRINSIC_ID_KEY.
	 * 
	 * @param reader the object read from MongoDB
	 * @param resource the resource that will contain the EMF Object
	 * @param eObject the EMF object being built
	 */
	protected void buildExtrinsicID(BsonReader reader, Resource resource, EObject eObject) {
		String id = reader.readString(Keywords.EXTRINSIC_ID_KEY);

		if (id != null && resource != null && resource instanceof XMLResource) {
			((XMLResource) resource).setID(eObject, id);
		}
	}


	/**
	 * Converts the MongoDB value into an EMF value using the converter service
	 * 
	 * @param eDataType the value type
	 * @param dbValue the value
	 * @return the converted value
	 */
	protected Object convertMongoToEMFValue(EDataType eDataType, Object dbValue) {
		Object convertedValue = dbValue;

		if (!MongoUtils.isNativeType(eDataType)) {
			if (converterService == null) {
				logger.warning("No ConverterService was found for data type " + eDataType.getName() + ". Returning original value");
				return convertedValue;
			}
			// Types not native to MongoDB are stored as strings and must be converted to the proper object type by EMF
			ValueConverter valueConverter = converterService.getConverter(eDataType);
			if (valueConverter != null) {
				convertedValue = valueConverter.convertDBValueToEMFValue(eDataType, dbValue);
			} else {
				logger.warning("No ValueConverter found for data type " + eDataType.getName() + " and value " + dbValue);
			}
		}

		return convertedValue;
	}

	/**
	 * This function creates an empty EObject ether according to the given eClass URI or with reference type 
	 * of the given reference.
	 * 
	 * @param reader the {@link BsonReader} to read from
	 * @param resourceSet the resourceSet that will be used to locate the EClass if it is not cached
	 * @param reference the reference this object is for
	 * @param eClassUri if a eClass attribute has been read before 
	 * @return the newly created object of type as specified by the data read from MongoDB
	 */
	protected EObject createEObject(BsonReader reader, ResourceSet resourceSet, EReference reference, String eClassUri) {
		EClass eClass = eClassUri != null ? getEClass(resourceSet, eClassUri) : reference.getEReferenceType();
		return EcoreUtil.create(eClass);
	}

	/**
	 * Finds the EClass for the given URI
	 * 
	 * @param resourceSet the resource set used to locate the EClass if it was not
	 *          found in the cache
	 * @param eClassURI the URI of the EClass
	 * @return the EClass instance for the given URI
	 */
	protected EClass getEClass(ResourceSet resourceSet, String eClassURI) {
		if (eClassCache != null) {
			synchronized (eClassCache) {
				EClass eClass = eClassCache.get(eClassURI);

				if (eClass == null) {
					eClass = getEClassFromResourceSet(resourceSet, eClassURI);
					eClassCache.put(eClassURI, eClass);
				}
				return eClass;
			}
		}

		return getEClassFromResourceSet(resourceSet, eClassURI);
	}
	
	/**
	 * Finds the EClass for the given URI in the {@link ResourceSet}.
	 * This Method does not simply call {@link ResourceSet#getEObject(URI, boolean)}. 
	 * It looks directly in the PackageRegistry of the {@link ResourceSet} and only tries to load something,
	 * if nothing is found. 
	 * 
	 * @param resourceSet the resource set used to locate the EClass 
	 * @param eClassURI the URI of the EClass
	 * @return the EClass instance for the given URI
	 */
	private EClass getEClassFromResourceSet(ResourceSet resourceSet, String eClassURI) {
		URI theUri = URI.createURI(eClassURI);
		EPackage ePackage = resourceSet.getPackageRegistry().getEPackage(theUri.trimFragment().toString());
		if(ePackage != null) {
			EClassifier eClassifier = (EClassifier) ePackage.eResource().getEObject(theUri.fragment());
			if(eClassifier != null && eClassifier instanceof EClass) {
				return (EClass) eClassifier;
			}
		}
		
		return (EClass) resourceSet.getEObject(theUri, true);
	}
	
	


	@Override
	public EObject decodeObject(BsonReader reader, DecoderContext context, Resource resource) {

		Resource loadResource = resource;
		reader.readBsonType();
		String proxyString = null;
		if(!BsonType.END_OF_DOCUMENT.equals(reader.getCurrentBsonType()) && sourceReferences.size() == 0){
			String oid = null;
			String name = reader.readName();
			if(Keywords.ID_KEY.equals(name)){
				Object oidValue = getPrimitiveValue(reader, reader.getCurrentBsonType(), (EDataType) null, null);
				if (oidValue == null) {
					throw new IllegalStateException("Getting '_id' value from BSON failed and returned null");
				}
				oid = oidValue.toString(); 
				reader.readBsonType(); //set the reader to the state requried by the next step.
				URI uri = baseUri.trimSegments(1).appendSegment(oid).trimQuery();
				if(!resource.getURI().equals(uri)){
					loadResource = factory.createResource(uri);
					if(resourceCache != null){
						resourceCache.add(loadResource);
					} else {
						resourceSet.getResources().add(loadResource);
					}
					//TODO we have to set the resource somehow to loaded
					//				try {
					//					loadResource.load(new ByteArrayInputStream(new byte[0]), null);
					//				} catch (IOException e) {
					//					// TODO Check if nothing happens here and loaded is set
					//					e.printStackTrace();
					//				}
				}
			}
		}

		String eClassUri = null;
		if(!BsonType.END_OF_DOCUMENT.equals(reader.getCurrentBsonType())){
			String name = reader.readName();
			if(eProxyUriKey.equals(name)) {
				proxyString = reader.readString();
				BsonType nextType = reader.readBsonType();
				if(!BsonType.END_OF_DOCUMENT.equals(nextType)){
					name = reader.readName();
				}
			}
			if(eClassKey.equals(name)){
				eClassUri = reader.readString();
				//set the reader to the next required state to match the expectation of the next step.
				BsonType nextType = reader.readBsonType();
				if(!BsonType.END_OF_DOCUMENT.equals(nextType)){
					reader.readName();
				}
			} 
		}
		EObject result = createEObject(reader, resourceSet, getLastReference(sourceReferences), eClassUri);
		if(sourceReferences.size() == 0){
			loadResource.getContents().add(result);
		}
		if(proxyString != null) {
			URI proxyUri = URI.createURI(proxyString);
			//We need to resolve the URI, because it might be a relative URI. 
			//Relative proxy URIs can not be resolved via EcoreUtil.resolve 
			proxyUri = uriHandler.resolve(proxyUri);
			Optional<String> queryString = getProxyQuery(getLastReference(sourceReferences));
			proxyUri = queryString.map(proxyUri::appendQuery).orElse(proxyUri);
			((InternalEObject) result).eSetProxyURI(proxyUri);

		}
		decodeFeatures(reader, context, result);
		return result;
	}

	/**
	 * Extracts the last added sourceReference or null
	 * @param sourceReference the list of sourceReferences
	 * @return null or the last {@link EReference} used
	 */
	private EReference getLastReference(List<EReference> sourceReference) {
		if(sourceReference.size() == 0){
			return null;
		}

		return sourceReference.get(sourceReference.size() -1);
	}

	/* (non-Javadoc)
	 * @see de.dim.spark.test.mongo.builder.EObjectBuilder#decodeFeatures(org.bson.BsonReader, org.bson.codecs.DecoderContext, org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public void decodeFeatures(BsonReader reader, DecoderContext decoderContext, EObject parent) {
		BsonType currentType = reader.getCurrentBsonType();
		EClass eclass = parent.eClass();
		boolean firstCycle = true;
		while (!BsonType.END_OF_DOCUMENT.equals(currentType)) {
			String currentFeatureName = null;
			if(firstCycle){
				currentFeatureName = reader.getCurrentName();
				firstCycle = false;
			} else {
				currentFeatureName = reader.readName();
			}
			if(currentFeatureName.equals(Keywords.TIMESTAMP_KEY)){
				if(response != null){
					response.put(URIConverter.ATTRIBUTE_TIME_STAMP, reader.readInt64());
				} else {
					reader.skipValue();
				}
				currentType = reader.readBsonType();
				continue;
			}
			currentType = reader.getCurrentBsonType();
			EStructuralFeature currentFeature = MongoUtils.getEStructuralFeatureByName(eclass, currentFeatureName, useExtendedMetadata);

			if (currentFeature != null) {
				if (currentFeature instanceof EAttribute) {
					decodeAttribute(reader, decoderContext, currentType, parent, (EAttribute) currentFeature);
				} else if (currentFeature instanceof EReference) {
					decodeReference(reader, decoderContext, currentType, parent, (EReference)currentFeature);
				}
			} else {
				reader.skipValue();
			}
			currentType = reader.readBsonType();
		}
	}
	
	/* (non-Javadoc)
	 * @see de.dim.spark.test.mongo.builder.EObjectBuilder#decodeReference(org.bson.BsonReader, org.bson.codecs.DecoderContext, org.bson.BsonType, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EReference)
	 */
	@Override
	public void decodeReference(BsonReader reader, DecoderContext decoderContext, BsonType type, EObject parent, EReference reference) {
		Object result = null;
		sourceReferences.add(reference);
		try{
			if (reference.isMany() && BsonType.ARRAY.equals(type)) {
				List<EObject> children = new LinkedList<>();
				reader.readStartArray();
				while (!BsonType.END_OF_DOCUMENT.equals(reader.readBsonType())) {
					EObject child = (EObject) codecRegistry.get(reference.getEReferenceType().getInstanceClass()).decode(reader, decoderContext);
					if (child != null) {
						children.add(child);
					}
				} 
				result = children;
				reader.readEndArray();
			} else {
				result = codecRegistry.get(reference.getEReferenceType().getInstanceClass()).decode(reader, decoderContext);
			}
			if (result != null) {
				parent.eSet(reference, result);
			}
		} finally {
			sourceReferences.remove(sourceReferences.size() - 1);
		}
	}

	

	/**
	 * @param reference the reference to look for
	 * @return an {@link Optional} with the query {@link String}
	 */
	@SuppressWarnings("unchecked")
	private Optional<String> getProxyQuery(EReference reference) {
		return Optional.ofNullable(options.get(Options.READ_QUERY_FOR_PROXIES))
				.map(o -> (Map<EReference, String>) o)
				.map(m -> m.get(reference))
				.map(Document::parse)
				.map(d -> {
					EAttribute eidAttribute = ((EClass) reference.getEType()).getEIDAttribute();
					Document projection = (Document) d.remove("projection");
					if(eidAttribute != null) {
						projection.put(eidAttribute.getName(), 1);
					}
					d.put("projectionOnly", projection);
					return d.toJson();
				})
				.map(URI::decode);
	}

	/* (non-Javadoc)
	 * @see de.dim.spark.test.mongo.builder.EObjectBuilder#decodeAttribute(org.bson.BsonReader, org.bson.codecs.DecoderContext, org.bson.BsonType, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	public void decodeAttribute(BsonReader reader, DecoderContext decoderContext, BsonType type, EObject parent, EAttribute attribute) {
		if (FeatureMapUtil.isFeatureMap(attribute)) {
			//      buildFeatureMap(collection, resource, eObject, attribute, (List<DBObject>) value);
		} else if (attribute.isMany()) {
			decodeAttributeArray(reader, parent, attribute, type);
		} else {
			decodeAttributeValue(reader, parent, attribute, type);
		}
	}

	/**
	 * Decodes a attribute value
	 * @param reader the {@link BsonReader} to read from
	 * @param parent the parent model object
	 * @param attribute the {@link EAttribute}
	 * @param type the source data type
	 */
	private void decodeAttributeValue(BsonReader reader, EObject parent, EAttribute attribute, BsonType type) {
		Object value = getPrimitiveValue(reader, type, attribute.getEAttributeType(), null);
		if (value != null) {
			Object convertedValue = convertMongoToEMFValue(attribute.getEAttributeType(), value);
			parent.eSet(attribute, convertedValue);
		} else {
			throw new IllegalStateException(String.format("[%s] Getting value for attribute '%s' returned null", parent, attribute.getName()));
		}
	}

	/**
	 * Decodes multi value attribute values
	 * @param reader the {@link BsonReader} to read from
	 * @param parent the parent model object to be set
	 * @param attribute the {@link EAttribute}
	 * @param type the source data type
	 */
	private void decodeAttributeArray(BsonReader reader, EObject parent, EAttribute attribute, BsonType type) {
		assert reader != null;
		assert parent != null;
		assert attribute != null;
		assert type != null;     

		List<Object> convertedValues = new LinkedList<Object>();
		EDataType dataType = attribute.getEAttributeType();
		if (BsonType.ARRAY.equals(type)) {
			reader.readStartArray();
			while (!BsonType.END_OF_DOCUMENT.equals(reader.readBsonType())) {
				type = reader.getCurrentBsonType();
				Object object = getPrimitiveValue(reader, type, dataType, null);
				if (object != null) {
					object = convertMongoToEMFValue(dataType, object);
					convertedValues.add(object);
				} else {
					throw new IllegalStateException(String.format("[%s] Getting value for multi-value attribute '%s' returned null", parent, attribute.getName()));
				}
			}
			reader.readEndArray();
		} 
		parent.eSet(attribute, convertedValues);
	}
	
	/**
	 * Returns a primitive value from the {@link BsonReader}
	 * @param reader the {@link BsonReader} to read from
	 * @param type the data type
	 * @param eDataType the target EMF data type
	 * @param if the eDataType is null, this instanceClassName will be used
	 * @return the extracted {@link Object} from the reader
	 */
	private Object getPrimitiveValue(BsonReader reader, BsonType type, EDataType eDataType, String instanceClassName) {
		
		if (!isDatatypeCompatible(type, eDataType, instanceClassName)) {
			logger.warning("Error detected invalid Mongo - Ecore data type combination: Mongo: " + type + ", Ecore: " + eDataType  + 
					" InstanceClassName " + instanceClassName +". Maybe use the ValueConverter for that.");
		}
		
		instanceClassName = eDataType != null ? eDataType.getInstanceClassName() : instanceClassName != null ? instanceClassName : "";
		assert reader != null;
		assert type != null;
		
		if(!(instanceClassName.endsWith("byte[]")) && BsonType.ARRAY.equals(type)) {		
			List<Object> list = new ArrayList<>();
			reader.readStartArray();
			while (!BsonType.END_OF_DOCUMENT.equals(reader.readBsonType())) {
				type = reader.getCurrentBsonType();	
				Object obj = null;
				if(!BsonType.ARRAY.equals(type)) {
					String objInstanceClassName = getInstanceClassName(type);				
					obj = getPrimitiveValue(reader, type, null, objInstanceClassName);
				}
				else {
					obj = getPrimitiveValue(reader, type, eDataType, null);
				}			    
				if(obj != null) {
					list.add(obj);
				}
			}
			reader.readEndArray();
			Object[] array = list.toArray();
			return array;
		}
		
		switch(type) {
		case OBJECT_ID:
			ObjectId oid = reader.readObjectId();
			if (instanceClassName == "java.lang.String") {
				return oid.toString();
			} else if (instanceClassName == "java.lang.Object") {
				return oid;
			} else if (instanceClassName == "byte[]") {
				return oid.toByteArray();
			} else {
				return oid;
			}
		case BOOLEAN:
			return Boolean.valueOf(reader.readBoolean());
		case INT32:
			int intValue = reader.readInt32();
			if (instanceClassName == "short" || instanceClassName == "java.lang.Short") {
				return Integer.valueOf(intValue).shortValue();
			} else if (instanceClassName == "long" || instanceClassName == "java.lang.Long") {
				return Integer.valueOf(intValue).longValue();
			} else if (instanceClassName == "int" || instanceClassName == "java.lang.Integer") {
				return Integer.valueOf(intValue);
			} else if (instanceClassName == "byte" || instanceClassName == "java.lang.Byte") {
				return Integer.valueOf(intValue).byteValue();
			} else if (instanceClassName == "java.util.Date") {
				return new Date(Integer.valueOf(intValue).longValue());
			} else {
				return intValue;
			}
		case INT64:
			long longValue = reader.readInt64();
			if (instanceClassName == "short" || instanceClassName == "java.lang.Short") {
				return Long.valueOf(longValue).shortValue();
			} else if (instanceClassName == "long" || instanceClassName == "java.lang.Long") {
				return Long.valueOf(longValue);
			} else if (instanceClassName == "int" || instanceClassName == "java.lang.Integer") {
				return Long.valueOf(longValue).intValue();
			} else if (instanceClassName == "byte" || instanceClassName == "java.lang.Byte") {
				return Long.valueOf(longValue).byteValue();
			} else if (instanceClassName == "java.util.Date") {
				return new Date(Long.valueOf(longValue));
			} else {
				return longValue;
			}
		case DOUBLE:
			if((eDataType != null && eDataType.getInstanceClass().equals(Float.class)) || instanceClassName.equals("float")){
				return Float.valueOf((float) reader.readDouble());
			} else {
				return Double.valueOf(reader.readDouble());
			}
		case BINARY:
			BsonBinary bsonBin = reader.readBinaryData();
			if (instanceClassName == "byte[]" || instanceClassName == "java.lang.Object") {
				return bsonBin.getData();
			} else if (instanceClassName == "byte" || instanceClassName == "java.lang.Byte") {
				return bsonBin.getData()[0];
			} 
		case DATE_TIME:
			long dateValue = reader.readDateTime();
			if (instanceClassName == "java.util.Date") {
				return new Date(dateValue);
			} else if (instanceClassName == "java.util.Calendar") {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(dateValue);
				return c;
			} else if (instanceClassName == "long" || instanceClassName == "java.lang.Long") {
				return dateValue;
			} else {
				return dateValue;
			}
		case STRING:
			String value = reader.readString();
			if (eDataType != null && eDataType instanceof EEnum) {
				EEnumLiteral literal = null;
				if (Boolean.TRUE.equals(options.get(Options.SAVE_USE_ENUM_LITERAL))) {
					literal = ((EEnum) eDataType).getEEnumLiteralByLiteral(value);
				} 
				if (literal == null) {
					literal = ((EEnum) eDataType).getEEnumLiteral(value);
					// Fallback
					if (literal == null) {
						literal = ((EEnum) eDataType).getEEnumLiteralByLiteral(value);
					}
				}
				return literal == null ? null : literal.getInstance();
			} else if(instanceClassName == "char" || instanceClassName == Character.class.getName()) {
				if(value == null || value.length() == 0) {
					return null;
				} else if ( value.length() > 1) {
					throw new RuntimeException("The value [" + value + "] was expected to be a character, but it is too long");
				} else {
					return value.charAt(0);
				}
			} else {
				return value;
			}
		case TIMESTAMP:
			long tsValue = reader.readTimestamp().asDateTime().getValue();
			if (instanceClassName == "java.util.Date") {
				return new Date(tsValue);
			} else if (instanceClassName == "java.util.Calendar") {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(tsValue);
				return c;
			} else if (instanceClassName == "long" || instanceClassName == "java.lang.Long") {
				return tsValue;
			} else {
				return tsValue;
			}
		case NULL:
			reader.readNull();
			return null;
		default:
			reader.readUndefined();
			return null;
		}
	}
	

	/**
	 * Returns <code>true</code>, if the {@link BsonType} is compatible to
	 * the {@link EDataType}
	 * @param type the BSON data type
	 * @param eDataType the target EMF data type
	 * @param instanceClassName, used when eDataType is <code>null<code>
	 * @return the <code>true</code>, if both data types are compatible
	 */
	private boolean isDatatypeCompatible(BsonType type, EDataType eDataType, String instanceClassName) {
		if (eDataType == null && instanceClassName == null) {
			return true;
		}
		if(eDataType != null) {
			instanceClassName = eDataType.getInstanceClassName();
		}
		assert type != null;
		switch(type) {
		case OBJECT_ID:
			return instanceClassName == "byte[]" || 
					instanceClassName == "java.lang.String" || 
					instanceClassName == "java.lang.Object";
		case BOOLEAN:
			return instanceClassName == "boolean" || 
					instanceClassName == "java.lang.Boolean";
		case INT32:
		case INT64:
			return instanceClassName == "int" || 
					instanceClassName == "java.lang.Integer" || 
					instanceClassName == "short" || 
					instanceClassName == "java.lang.Short" ||
					instanceClassName == "long" || 
					instanceClassName == "java.util.Date" || 
					instanceClassName == "java.util.Calendar" || 
					instanceClassName == "java.lang.Long";
		case DOUBLE:
			return instanceClassName == "float" || 
					instanceClassName == "double" || 
					instanceClassName == "java.lang.Float" || 
					instanceClassName == "java.lang.Double";
		case BINARY:
			return instanceClassName == "byte[]" || 
					instanceClassName == "byte" || 
					instanceClassName == "java.lang.Object" || 
					instanceClassName == "java.lang.Byte";
		case DATE_TIME:
		case TIMESTAMP:
			return instanceClassName == "java.util.Date" || 
					instanceClassName == "long" || 
					instanceClassName == "java.lang.Long" ||
					instanceClassName == "java.util.Calendar";
		case STRING:
			return  (eDataType != null && eDataType instanceof EEnum) ||
					instanceClassName == "java.lang.String" || 
					instanceClassName =="java.math.BigInteger" || 
					instanceClassName == "java.math.BigDecimal";
		case ARRAY:
			return instanceClassName.endsWith("[]");
		case NULL:
		default:
			return false;
		}
	}
	
	private String getInstanceClassName(BsonType type) {
		switch (type) {
		case DOUBLE:
			return "java.lang.Double";
		case INT32: case INT64:
			return "java.lang.Integer";
		case STRING:
			return "java.lang.String";
		default:
			return null;
		}
	}
}
