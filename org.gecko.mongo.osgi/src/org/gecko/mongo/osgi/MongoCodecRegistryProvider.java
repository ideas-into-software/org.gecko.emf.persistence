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
package org.gecko.mongo.osgi;

import org.bson.codecs.configuration.CodecRegistry;

/**
 * Interface to register a mongo codec registry
 * @author Mark Hoffmann
 * @since 28.06.2016
 */
public interface MongoCodecRegistryProvider {
	
	/**
	 * Returns the codec registry id
	 * @return the codec registry id
	 */
	public String getIdentifier();
	
	/**
	 * Returns the code registry
	 * @return the code registry
	 */
	public CodecRegistry getCodecRegistry(); 

}
