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

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.codecs.DecoderContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author bhunt
 * 
 */
public interface EObjectBuilder
{
	
	/**
	 * Decodes an {@link EObject} out of the {@link BsonReader}
	 * @param reader the {@link BsonReader} to read from
	 * @param context the {@link DecoderContext}
	 * @param resource the resource belonging to the object
	 * @return the created {@link EObject} instance
	 */
	public EObject decodeObject(BsonReader reader, DecoderContext context, Resource resource);
	
	/**
	 * Decodes {@link EStructuralFeature} from the {@link BsonReader}
	 * @param reader the {@link BsonReader} to read from
	 * @param decoderContext the decoder context
	 * @param parent the parent {@link EObject} to decode the {@link EStructuralFeature} for
	 */
	public void decodeFeatures(BsonReader reader, DecoderContext decoderContext, EObject parent);
	
	/**
	 * Decodes a {@link EReference} from the {@link BsonReader}
	 * @param reader the {@link BsonReader} to read from
	 * @param decoderContext the decoder context
	 * @param type the type of the reference
	 * @param parent the parent object to set the reference value
	 * @param reference the {@link EReference}
	 */
	public void decodeReference(BsonReader reader, DecoderContext decoderContext, BsonType type, EObject parent, EReference reference);
	
	/**
	 * Decodes a {@link EAttribute} from the {@link BsonReader}
	 * @param reader the {@link BsonReader} to read from
	 * @param decoderContext the decoder context
	 * @param type the type of the attribute
	 * @param parent the parent object to set the attributes values
	 * @param attribute the {@link EAttribute}
	 */
	public void decodeAttribute(BsonReader reader, DecoderContext decoderContext, BsonType type, EObject parent, EAttribute attribute);
}
