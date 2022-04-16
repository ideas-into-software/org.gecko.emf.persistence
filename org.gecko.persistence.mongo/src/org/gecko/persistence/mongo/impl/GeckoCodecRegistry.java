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
package org.gecko.persistence.mongo.impl;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.MongoClientSettings;

public class GeckoCodecRegistry implements CodecRegistry {


	CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry());
	@Override
	public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
		return codecRegistry.get(clazz, registry);

	}

	@Override
	public <T> Codec<T> get(Class<T> clazz) {
		return codecRegistry.get(clazz);
	}

}
