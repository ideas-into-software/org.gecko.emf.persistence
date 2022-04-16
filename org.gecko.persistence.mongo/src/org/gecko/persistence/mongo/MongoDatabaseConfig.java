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
package org.gecko.persistence.mongo;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition()
public @interface MongoDatabaseConfig {
	
	public static String PREFIX = "persistence.mongo"; 

	@AttributeDefinition(required = true, description = "the name of the database the mongo-client used to call the Database")
	String name();

	@AttributeDefinition(required = false, description = "additional alias for the database,this overrides of the `name` in the databaseUniqueIdentifyer")
	String alias();

	@AttributeDefinition(required = false, description = "verifyes, that the database exists on the for the client.")
	boolean must_exist() default false;

}
