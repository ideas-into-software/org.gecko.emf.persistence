/*******************************************************************************
 * Copyright (c) 2012 Bryan Hunt.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 *******************************************************************************/

package org.gecko.emf.mongo.codecs.builder;

import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * Object builder using the codec frameowrk of the mongo driver
 * @author Mark Hoffmann
 */
public interface DBObjectBuilder {
	/**
	 * Build a Mongo DB object from the supplied EMF object using streamings.
	 * 
	 * @param writer the {@link BsonWriter} instance to write into
	 * @param eObject the EMF object to serialize
	 */
	void buildDBObject(BsonWriter writer, EObject eObject, EncoderContext context);
	
  /**
   * Writes a primitive value to the {@link BsonWriter}. If the value is <code>null</code>, nothing will be written
   * @param name the name of the field
   * @param value the value
   * @param writer the writer to write into
   */
	public void writePrimitiveValue(String name, Object value, BsonWriter writer);
	
  /**
   * Writes a primitive value to the {@link BsonWriter}. If the value is <code>null</code>, nothing will be written
   * @param value the value
   * @param writer the writer to write into
   */
	public void writePrimitiveValueNoName(Object value, BsonWriter writer);
	
	 /**
   * Converts the EMF value into a MongoDB value using the converter service
   * 
   * @param eDataType the value type
   * @param emfValue the value
   * @return the converted value
   */
  public Object convertEMFToMongoValue(EDataType eDataType, Object emfValue);
  
  /**
   * Serializes a reference as an embedded object or a proxy as appropriate
   * 
   * @param writer the {@link BsonWriter} to write into
   * @param eReference the reference to serialize
   * @param targetObject to referenced object
   * @param context the encoder context
   */
  public void buildReferencedObject(BsonWriter writer, EReference eReference, EObject targetObject, EncoderContext context);
}
