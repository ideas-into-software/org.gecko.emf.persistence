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
package org.gecko.emf.persistence.jpa.orm.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.ResultSet;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.gecko.emf.persistence.jpa.orm.ORMappingProvider;
import org.gecko.emf.persistence.jpa.orm.impl.ORMappingProviderImpl;
import org.gecko.emf.persistence.jpa.orm.model.orm.Entity;
import org.gecko.emf.persistence.jpa.orm.model.orm.EntityMappingsType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.BundleContext;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.common.annotation.config.WithFactoryConfigurations;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
@ExtendWith(MockitoExtension.class)
public class ORMappingProviderImplTest {

	@BeforeEach
	public void before(@InjectBundleContext BundleContext ctx) {
	}

	@Test
	void testResourceSetExists(@InjectService(filter = "(emf.model.name=orm)") ResourceSet resourceSet) {
		System.out.println(resourceSet);
	}

	@WithFactoryConfigurations({
			@WithFactoryConfiguration(name = "test", location = "?", factoryPid = ORMappingProviderImpl.PID, properties = {
					@Property(key = "url", value = "orm/entity_simple.xmi"), })

	})
	@Test
	void test(@InjectService() ORMappingProvider orMappingProvider) {

		assertThat(orMappingProvider).isNotNull();

		EntityMappingsType entityMappingsType = orMappingProvider.get();
		assertThat(entityMappingsType).isNotNull();
		assertThat(entityMappingsType.getEntity()).isNotNull().hasSize(1);
		Entity entity = entityMappingsType.getEntity().get(0);
		assertThat(entity).isNotNull();
		assertThat(entity.getClass_()).isEqualTo("org.gecko.emf.persistence.jpa.orm.tests.model.base.Simple");

	}

}
