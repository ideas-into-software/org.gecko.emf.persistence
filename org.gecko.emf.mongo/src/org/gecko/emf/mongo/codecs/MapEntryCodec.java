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

import java.util.Map;
import java.util.Map.Entry;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.mongo.codecs.builder.DBObjectBuilder;
import org.gecko.emf.mongo.codecs.builder.EObjectBuilder;

/**
 * {@link Codec} for {@link Map.Entry}'s
 * @author Mark Hoffmann
 * @since 07.05.2016
 */
@SuppressWarnings("rawtypes")
public class MapEntryCodec implements Codec<Entry> {

	private final DBObjectBuilder builder;
	private final EObjectBuilder eoBuilder;
	private final Resource resource;

	public MapEntryCodec(DBObjectBuilder dbBuilder, EObjectBuilder eoBuilder, Resource resource) {
		this.builder = dbBuilder;
		this.eoBuilder = eoBuilder;
		this.resource = resource;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.bson.codecs.Encoder#encode(org.bson.BsonWriter, java.lang.Object, org.bson.codecs.EncoderContext)
	 */
	public void encode(BsonWriter writer, Entry value, EncoderContext encoderContext) {
		Object objectKey = value.getKey();
		Object objectValue = value.getValue();
		if (objectKey.getClass().isPrimitive() || objectKey instanceof String) {
			builder.writePrimitiveValue("key", objectKey, writer);
		}
		if (objectValue.getClass().isPrimitive() || objectValue instanceof String) {
			builder.writePrimitiveValue("value", objectValue, writer);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.bson.codecs.Encoder#getEncoderClass()
	 */
	@Override
	public Class<Entry> getEncoderClass() {
		return Entry.class;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.bson.codecs.Decoder#decode(org.bson.BsonReader, org.bson.codecs.DecoderContext)
	 */
	@Override
	public Entry decode(BsonReader reader, DecoderContext decoderContext) {
		try{
			reader.readStartDocument();
			EObject result = eoBuilder.decodeObject(reader, decoderContext, resource);
			reader.readEndDocument();
			return (Entry) result;
		} catch (Throwable e) {
			throw new IllegalStateException("Error while decoding EObject. Cause is: " + e.getMessage(), e);
		}
	}

}
