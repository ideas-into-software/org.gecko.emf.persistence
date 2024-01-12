/**
 * Copyright (c) 2012 - 2016 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.mongo.codecs;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.gecko.emf.mongo.MongoUtils;
import org.gecko.emf.mongo.codecs.builder.DBObjectBuilder;

/**
 * {@link Codec} for {@link FeatureMap}'s
 * @author Mark Hoffmann
 * @since 07.05.2016
 */
public class FeatureMapEntryCodec implements Codec<Entry> {

	private final DBObjectBuilder builder;

	public FeatureMapEntryCodec(DBObjectBuilder builder) {
		this.builder = builder;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.bson.codecs.Encoder#encode(org.bson.BsonWriter, java.lang.Object, org.bson.codecs.EncoderContext)
	 */
	@Override
	public void encode(BsonWriter writer, Entry value, EncoderContext encoderContext) {
		EStructuralFeature feature = value.getEStructuralFeature();
		writer.writeString("key", EcoreUtil.getURI(feature).toString());

		if (feature instanceof EAttribute) {
			EDataType eDataType = ((EAttribute) feature).getEAttributeType();
			Object attributeValue = value.getValue();
			if (!MongoUtils.isNativeType(eDataType)) {
				attributeValue = builder.convertEMFToMongoValue(eDataType, value.getValue());
			}
			builder.writePrimitiveValue("value", attributeValue, writer);
		} else {
			builder.buildReferencedObject(writer, (EReference) feature, (EObject) value.getValue(), encoderContext);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.bson.codecs.Encoder#getEncoderClass()
	 */
	@Override
	public Class<Entry> getEncoderClass() {
		return FeatureMap.Entry.class;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.bson.codecs.Decoder#decode(org.bson.BsonReader, org.bson.codecs.DecoderContext)
	 */
	@Override
	public Entry decode(BsonReader reader, DecoderContext decoderContext) {
		// TODO Auto-generated method stub
		return null;
	}

}
