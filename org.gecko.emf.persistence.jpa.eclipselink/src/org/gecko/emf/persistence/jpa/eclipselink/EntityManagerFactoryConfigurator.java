package org.gecko.emf.persistence.jpa.eclipselink;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.dynamic.DynamicClassLoader;
import org.eclipse.persistence.dynamic.DynamicType;
import org.eclipse.persistence.jpa.dynamic.JPADynamicHelper;
import org.eclipse.persistence.jpa.dynamic.JPADynamicTypeBuilder;
import org.eclipse.persistence.tools.schemaframework.SchemaManager;
import org.gecko.emf.persistence.jpa.orm.ORMappingProvider;
import org.gecko.emf.persistence.jpa.orm.model.orm.Entity;
import org.gecko.emf.persistence.jpa.orm.model.orm.EntityMappingsType;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.spi.PersistenceProvider;

@Designate(factory = true, ocd = EntityManagerFactoryConfigurator.Config.class)
@Component(configurationPid = { EntityManagerFactoryConfigurator.PID })
public class EntityManagerFactoryConfigurator {

	public static final String PID = "org.gecko.emf.persistence.jpa.eclipselink.EntityManagerFactoryConfigurator";

	@ObjectClassDefinition
	@interface Config {

		String test();
	}

	@Reference
	DataSource dataSource;

	@Reference
	private EPackage ePackage;

	@Reference
	Optional<ORMappingProvider> oRMappingProvider;

	private List<DynamicType> dynamicTypes = new ArrayList<DynamicType>();

	@Activate
	void activate(BundleContext bCtx, EntityManagerFactoryConfigurator.Config config) {

		// WHAT OTHERS DO
		//https://github.com/jeddict/jeddict/blob/3765ebb6682d28f444e9a13daf7d2ed7ea53c515/relation-mapper/src/main/java/io/github/jeddict/relation/mapper/persistence/internal/jpa/metadata/JPAMMetadataProject.java#L24
		Optional<EntityMappingsType> oEntityMappingsType = ORMappingUtil.entityMappingsType(oRMappingProvider);
		DynamicClassLoader dcl = new DynamicClassLoader(DynamicClassLoader.class.getClassLoader()) {

			@Override
			public URL getResource(String name) {
				return super.getResource(name);
			}

			@Override
			public InputStream getResourceAsStream(String name) {
				return super.getResourceAsStream(name);
			}

			@Override
			public Enumeration<URL> getResources(String name) throws IOException {
				return bCtx.getBundle().getResources(name);
			}
		};

		String packege_Prefix = ePackage.getName() + ".";
		EList<EClassifier> eClassifiers = ePackage.getEClassifiers();
		for (EClassifier eClassifier : eClassifiers) {
			System.out.println(eClassifier);

			if (eClassifier instanceof EClass) {

				EClass eClass = (EClass) eClassifier;
				String fqClassName = packege_Prefix + eClass.getName();

				Optional<Entity> oEntity = ORMappingUtil.entity(oEntityMappingsType, fqClassName);

				Class<?> dynamicClass = dcl.createDynamicClass(fqClassName);

				JPADynamicTypeBuilder dynamicTypeBuilder = new JPADynamicTypeBuilder(dynamicClass, null,
						eClass.getName());

				for (EAttribute eAttribute : eClass.getEAttributes()) {
					String colName = eAttribute.getName().toUpperCase();
					System.out.println(eAttribute);
					dynamicTypeBuilder.addDirectMapping(eAttribute.getName(), Util.convType(eAttribute), colName);

					if (eAttribute.isID()) {
						dynamicTypeBuilder.setPrimaryKeyFields(colName);
						dynamicTypeBuilder.configureSequencing("SEQ_" + eClass.getName(), colName);
					}
				}
				dynamicTypes.add(dynamicTypeBuilder.getType());

			}
		}

//		DatabaseMapping dbm = new EDirectToFieldMapping();
//
//		simpleEObjectTypeBuilder.addMapping(dbm);

		PersistenceProvider persistenceProvider = new org.eclipse.persistence.jpa.PersistenceProvider();

		HashMap<String, Object> map = new HashMap<>();
		map.put(PersistenceUnitProperties.CLASSLOADER, dcl);
		map.put(PersistenceUnitProperties.WEAVING, "static");
		map.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, dataSource);
		map.put("eclipselink.target-database", "Derby");
		map.put("eclipselink.logging.level", "FINE");
		map.put("eclipselink.logging.timestamp", "false");
		map.put("eclipselink.logging.thread", "false");
		map.put("eclipselink.logging.exceptions", "true");
		map.put("eclipselink.orm.throw.exceptions", "true");
		map.put("eclipselink.jdbc.read-connections.min", "1");
		map.put("eclipselink.jdbc.write-connections.min", "1");
		map.put("eclipselink.ddl-generation", "drop-and-create-tables");

		EntityManagerFactory emf = persistenceProvider.createEntityManagerFactory("DynamicTest", map);

		DynamicType[] types = dynamicTypes.stream().toArray(DynamicType[]::new);

		JPADynamicHelper helper = new JPADynamicHelper(emf);
		helper.addTypes(true, true, types);

		SchemaManager schemaManager = new SchemaManager(helper.getSession());
		schemaManager.outputCreateDDLToWriter(new PrintWriter(System.out));
		schemaManager.outputCreateDDLToWriter(new PrintWriter(System.out));
		schemaManager.outputDropDDLToWriter(new PrintWriter(System.out));
		schemaManager.replaceDefaultTables();
		schemaManager.setCreateSQLFiles(true);

		bCtx.registerService(EntityManagerFactory.class, emf, new Hashtable<String, Object>());

	}

}
