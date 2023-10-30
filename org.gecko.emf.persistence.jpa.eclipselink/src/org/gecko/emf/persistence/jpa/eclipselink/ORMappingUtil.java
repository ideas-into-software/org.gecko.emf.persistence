package org.gecko.emf.persistence.jpa.eclipselink;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EClass;
import org.gecko.emf.persistence.jpa.orm.ORMappingProvider;
import org.gecko.emf.persistence.jpa.orm.model.orm.Entity;
import org.gecko.emf.persistence.jpa.orm.model.orm.EntityMappingsType;
import org.gecko.emf.persistence.jpa.orm.model.orm.Table;

public class ORMappingUtil {

	public static Optional<EntityMappingsType> entityMappingsType(Optional<ORMappingProvider> oRMappingProvider) {
		return oRMappingProvider.map(Supplier::get);
	}

	public static Optional<Entity> entity(Optional<EntityMappingsType> oEntityMappingsType, String fqClassName) {
		return oEntityMappingsType.map(EntityMappingsType::getEntity)
				.map(l -> l.parallelStream().filter(e -> fqClassName.equals(e.getClass_())).findAny())
				.orElse(Optional.empty());
	}

	public static String[] tables(Optional<Entity> oEntity, EClass c) {
		List<String> list = new ArrayList<>();
		if (oEntity.isPresent()) {
			Entity entity = oEntity.get();
			Table table = entity.getTable();

			list.add(table(table, c));
		} else {

		}
		return new String[] { c.getName() };
	}

	public static String table(Table table, EClass c) {
		if (table == null) {
			return c.getName();
		} else {
			StringBuilder sb = new StringBuilder();

			if (table.getCatalog() != null) {
				sb.append(table.getCatalog()).append(".");
			}
			if (table.getSchema() != null) {
				sb.append(table.getSchema()).append(".");
			}
			return	sb.append(table.getName()).toString();

		}

	}

}
