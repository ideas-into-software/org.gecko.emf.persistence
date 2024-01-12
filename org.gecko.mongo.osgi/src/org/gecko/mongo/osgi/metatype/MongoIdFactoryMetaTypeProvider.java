/*******************************************************************************
 * Copyright (c) 2013 Bryan Hunt.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bryan Hunt - initial API and implementation
 *******************************************************************************/
package org.gecko.mongo.osgi.metatype;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.gecko.mongo.osgi.configuration.ConfigurationProperties;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.ObjectClassDefinition;

/**
 * Meta type provider for the mongo id factory
 * @author bhunt
 */
@Component(name="MongoIdFactoryMetaTypeProvider", immediate=true, service=MetaTypeProvider.class, property="metatype.factory.pid=MongoIdFactory")
public class MongoIdFactoryMetaTypeProvider implements MetaTypeProvider
{
	Set<String> databases = new CopyOnWriteArraySet<String>();

	/* 
	 * (non-Javadoc)
	 * @see org.osgi.service.metatype.MetaTypeProvider#getLocales()
	 */
	@Override
	public String[] getLocales() {
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.osgi.service.metatype.MetaTypeProvider#getObjectClassDefinition(java.lang.String, java.lang.String)
	 */
	@Override
	public ObjectClassDefinition getObjectClassDefinition(String arg0, String arg1)	{
		ObjectClassDefinitionImpl ocd = new ObjectClassDefinitionImpl(ConfigurationProperties.ID_FACTORY_PID, "MongoDB ID", "MongoDB ID Provider Configuration");
		return ocd;
	}

}
