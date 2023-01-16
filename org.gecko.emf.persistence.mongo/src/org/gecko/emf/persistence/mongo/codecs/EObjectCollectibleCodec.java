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
package org.gecko.emf.persistence.mongo.codecs;

import java.util.Map;

import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonObjectId;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.emf.persistence.api.Options;
import org.gecko.emf.persistence.mongo.codecs.builder.DBObjectBuilder;
import org.gecko.emf.persistence.mongo.codecs.builder.EObjectBuilder;

/**
 * {@link CollectibleCodec} for {@link EObject}'s
 * @author Mark Hoffmann
 * @since 07.05.2016
 */
public class EObjectCollectibleCodec implements CollectibleCodec<EObject> {

	private final DBObjectBuilder dbBuilder;
	private final EObjectBuilder eoBuilder;
	private final Resource resource;
	private final Map<?, ?> options;
	private final String eClassKey;

	public EObjectCollectibleCodec(DBObjectBuilder dbBuilder, EObjectBuilder eoBuilder, Resource resource, Map<?, ?> options) {
		this.dbBuilder = dbBuilder;
		this.eoBuilder = eoBuilder;
		this.resource = resource;
		this.options = options;
		eClassKey = Options.getEClassKey(this.options);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.bson.codecs.Encoder#encode(org.bson.BsonWriter, java.lang.Object, org.bson.codecs.EncoderContext)
	 */
	@Override
	public void encode(BsonWriter writer, EObject value, EncoderContext encoderContext) {
		try{
			writer.writeStartDocument();
			dbBuilder.buildDBObject(writer, value, encoderContext);
			writer.writeEndDocument();
		} catch (Throwable e) {
			if (e instanceof IllegalStateException) {
				throw e;
			}
			throw new IllegalStateException("Error while encoding EObject. Cause is: " + e.getMessage(), e);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.bson.codecs.Encoder#getEncoderClass()
	 */
	@Override
	public Class<EObject> getEncoderClass() {
		return EObject.class;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.bson.codecs.Decoder#decode(org.bson.BsonReader, org.bson.codecs.DecoderContext)
	 */
	@Override
	public EObject decode(BsonReader reader, DecoderContext decoderContext) {
		try {			
			reader.readStartDocument();						
			EObject result = eoBuilder.decodeObject(reader, decoderContext, resource);
			reader.readEndDocument();
		return result;
		} catch (Throwable e) {
			throw new IllegalStateException("Error while decoding EObject. Cause is: " + e.getMessage(), e);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.bson.codecs.CollectibleCodec#generateIdIfAbsentFromDocument(java.lang.Object)
	 */
	@Override
	public EObject generateIdIfAbsentFromDocument(EObject document) {
		return document;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.bson.codecs.CollectibleCodec#documentHasId(java.lang.Object)
	 */
	@Override
	public boolean documentHasId(EObject document) {
		return Boolean.TRUE;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.bson.codecs.CollectibleCodec#getDocumentId(java.lang.Object)
	 */
	@Override
	public BsonValue getDocumentId(EObject document) {
		if (!documentHasId(document)) {
			throw new IllegalArgumentException("Cannot get document id because it has none");
		}
		EAttribute idAttribute = document.eClass().getEIDAttribute();
		if (idAttribute == null) {
			throw new IllegalArgumentException("EObject of " + document.eClass().getName() + " doesnt have an id field");
		}
		Object idObject = document.eGet(idAttribute);
		if (idObject == null) {
			throw new IllegalArgumentException("EObject of " + document.eClass().getName() + " has not id value set");
		}
		if (idObject instanceof Long) {
			Long idLong = (Long) idObject;
			return new BsonInt64(idLong.longValue());
		}
		if (idObject instanceof Integer) {
			Integer idInteger = (Integer) idObject;
			return new BsonInt32(idInteger.intValue());
		}
		byte[] idArray = null;
		if (idObject instanceof String) {
			idArray = idObject.toString().getBytes();
			if (idArray.length > 12) {
				return new BsonString(idObject.toString());
			}
		}
		if (idObject instanceof byte[]) {
			idArray = (byte[]) idObject;
		}
		if (idArray != null && idArray.length == 12) {
			return new BsonObjectId(new ObjectId(idArray));
		}
		return new BsonObjectId(new ObjectId());
	}


	/**
	 * This function creates an empty EObject by extracting the EClass type from the given DBObject
	 * using the ECLASS_KEY. This function also maintains a static cache of EClass URI to EClass for
	 * improved performance.
	 * 
	 * @param reader the {@link BsonReader} to read from
	 * @param resourceSet the resourceSet that will be used to locate the EClass if it is not cached
	 * @return the newly created object of type as specified by the data read from MongoDB
	 */
	protected EObject createEObject(BsonReader reader, ResourceSet resourceSet, boolean readWithKey) {
		String eClassURI = readWithKey ? reader.readString(eClassKey) : reader.readString();
		EClass eClass = getEClass(resourceSet, eClassURI);
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
		return (EClass) resourceSet.getEObject(URI.createURI(eClassURI), true);
	}

}
