package org.gecko.emf.persistence.jpa.eclipselink;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.dynamic.DynamicClassLoader;
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

		String[] urls();
	}

	@Reference
	DataSource dataSource;

	Map<String, URL> ORMappingProviderMap = new ConcurrentHashMap<String, URL>();

	@Activate
	void activate(BundleContext bCtx, EntityManagerFactoryConfigurator.Config config) throws MalformedURLException {

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

				URL urlmp = ORMappingProviderMap.get(name);
				if (urlmp != null) {
					Enumeration<URL> enumeration = Collections.enumeration(List.of(urlmp));
					return enumeration;
				}
				// map name to the mappingprovider
				if ("META-INF/persistence.xml".equals(name)) {
					return bCtx.getBundle().getResources(name);
				}
				return super.getResources(name);

			}
		};

		PersistenceProvider persistenceProvider = new org.eclipse.persistence.jpa.PersistenceProvider();

		URL url = bCtx.getBundle().getEntry("META-INF/persistence.xml");

		System.out.println(url);
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

		DynamicPersistenceUnitInfo pui = new DynamicPersistenceUnitInfo("DynamicTest", url, map);

		for (String tmpsUrl : config.urls()) {
			String filename = "dynamicMappingFiles/" + UUID.randomUUID().toString() + ".xml";
			URL tmpUrl = URI.create(tmpsUrl).toURL();

			ORMappingProviderMap.put(filename, tmpUrl);
			pui.getMappingFileNames().add(filename);
		}

		EntityManagerFactory emf = persistenceProvider.createContainerEntityManagerFactory(pui, map);

		bCtx.registerService(EntityManagerFactory.class, emf, new Hashtable<String, Object>());



	}

}
