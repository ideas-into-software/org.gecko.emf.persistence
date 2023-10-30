package org.gecko.emf.persistence.jpa.orm.impl;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.gecko.emf.persistence.jpa.orm.ORMappingProvider;
import org.gecko.emf.persistence.jpa.orm.impl.ORMappingProviderImpl.Config;
import org.gecko.emf.persistence.jpa.orm.model.orm.EntityMappingsType;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Designate(factory = true, ocd = Config.class)
@Component(service = ORMappingProvider.class,configurationPid = {ORMappingProviderImpl.PID})
public class ORMappingProviderImpl implements ORMappingProvider {

	public static final String PID = "org.gecko.emf.persistence.jpa.orm.impl.ORMappingProviderImpl";

	@Reference(target = "(emf.model.name=orm)")
	private ResourceSet resourceSet;

	@ObjectClassDefinition()
	@interface Config {

		@AttributeDefinition
		String url();

	}

	private EntityMappingsType entityMappingsType;

	@Activate
	void activate(Config config) {
		Resource resource = resourceSet.getResource(URI.createURI(config.url()), true);

		entityMappingsType = (EntityMappingsType) resource.getContents().get(0);
	}

	@Override
	public EntityMappingsType get() {
		return entityMappingsType;
	}

}
