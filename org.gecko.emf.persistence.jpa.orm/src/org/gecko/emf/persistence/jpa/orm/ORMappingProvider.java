package org.gecko.emf.persistence.jpa.orm;

import java.util.function.Supplier;

import org.gecko.emf.persistence.jpa.orm.model.orm.EntityMappingsType;

public interface ORMappingProvider extends Supplier<EntityMappingsType> {

}
