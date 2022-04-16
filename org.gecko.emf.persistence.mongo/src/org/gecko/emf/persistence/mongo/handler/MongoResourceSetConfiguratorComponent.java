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
package org.gecko.emf.persistence.mongo.handler;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.bson.Document;
import org.gecko.emf.osgi.EMFNamespaces;
import org.gecko.emf.osgi.ResourceSetConfigurator;
import org.gecko.emf.persistence.InputStreamFactory;
import org.gecko.emf.persistence.OutputStreamFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.gecko.persistence.mongo.InfoMongoDatabase;
import org.gecko.persistence.mongo.MongoConstants;

/**
 * This implementation of the ResourceSetConfigurator service will attach all
 * currently bound URI handlers to the ResourceSet. This service is intended to
 * be used with the IResourceSetFactory service.
 * 
 * @author bhunt
 * 
 */
@Component(name = "MongoResourceSetConfiguratorComponent", immediate = true)
public class MongoResourceSetConfiguratorComponent {

	private final Map<MongoDatabase, Map<String, Object>> mongoDatabases = new ConcurrentHashMap<MongoDatabase, Map<String, Object>>();
	private ServiceRegistration<ResourceSetConfigurator> configuratorRegistration;
	private MongoURIHandlerProvider uriHandlerProvider = new MongoURIHandlerProvider();
	private BundleContext ctx;
	private List<String> aliases = new LinkedList<String>();
	private Map<String, String> aliasIdentifierMap = new HashMap<String, String>();

	/**
	 * Called on component activation
	 * 
	 * @param context the component context
	 */
	@Activate
	public void activate(BundleContext context) {
		ctx = context;
		Dictionary<String, Object> properties = getDictionary();
		configuratorRegistration = ctx.registerService(ResourceSetConfigurator.class,
				new MongoResourceSetConfigurator(uriHandlerProvider), properties);

	}

	/**
	 * Called on component deactivation
	 */
	@Deactivate
	public void deactivate() {
		configuratorRegistration.unregister();
		configuratorRegistration = null;
	}

	/**
	 * Adds a {@link InfoMongoDatabase} to the provider map.
	 * 
	 * @param mongoDatabase the provider to be added
	 */
	@Reference(name = "MongoDatabase", policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.AT_LEAST_ONE)
	public void addMongoDatabase(InfoMongoDatabase mongoDatabase, Map<String, Object> map) {

		mongoDatabases.put(mongoDatabase, map);

		String alias = mongoDatabase.getAlias();
		if (alias == null) {
			throw new IllegalArgumentException("Database alias must not be null");
		}
		String databaseIdent = mongoDatabase.getDatabaseUniqueIdentifyer();

		uriHandlerProvider.addMongoDatabaseProvider(mongoDatabase);
		updateProperties(databaseIdent, alias, true);
	}

	/**
	 * Removes a {@link InfoMongoDatabase} from the map
	 * 
	 * @param mongoDatabase the provider to be removed
	 */
	public void removeMongoDatabase(InfoMongoDatabase mongoDatabase, Map<String, Object> map) {

		String alias = mongoDatabase.getAlias();
		String databaseIdent = mongoDatabase.getDatabaseUniqueIdentifyer();
		uriHandlerProvider.removeMongoDatabaseProvider(mongoDatabase);
		updateProperties(databaseIdent, alias, false);
	}

	/**
	 * Sets an {@link InputStreamFactory} to handle input streams
	 * 
	 * @param inputStreamFactory the factory to set
	 */
	@Reference(name = "InputStreamFactory", cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	public void setInputStreamFactory(InputStreamFactory<MongoCollection<Document>> inputStreamFactory) {
		uriHandlerProvider.setInputStreamFactory(inputStreamFactory);
	}

	/**
	 * Sets an {@link OutputStreamFactory} to handle output streams
	 * 
	 * @param outputStreamFactory the factory to set
	 */
	@Reference(name = "OutputStreamFactory", cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	public void setOutputStreamFactory(OutputStreamFactory<MongoCollection<Document>> outputStreamFactory) {
		uriHandlerProvider.setOutputStreamFactory(outputStreamFactory);
	}

	/**
	 * Updates the properties of the service, depending on changes on injected
	 * services
	 * 
	 * @param config the service properties from the injected service
	 * @param add    <code>true</code>, if the service was add, <code>false</code>
	 *               in case of an remove
	 */
	private void updateProperties(String uid, String alias, boolean add) {

		if (add) {
			aliases.add(alias);
			if (uid != null) {
				aliasIdentifierMap.put(alias, uid);
			}
		} else {
			aliases.remove(alias);
			aliasIdentifierMap.remove(alias);
		}

		updateRegistrationProperties();

	}

	/**
	 * Updates the service registration properties
	 */
	private void updateRegistrationProperties() {
		if (configuratorRegistration != null) {
			configuratorRegistration.setProperties(getDictionary());
		}
	}

	/**
	 * Creates a dictionary for the stored properties
	 * 
	 * @return a dictionary for the stored properties
	 */
	private Dictionary<String, Object> getDictionary() {
		Dictionary<String, Object> properties = new Hashtable<>();
		List<String> uidsList = new LinkedList<String>(aliases);
		String[] uidsArr = uidsList.toArray(new String[aliases.size()]);
		if (uidsArr.length > 0) {
			properties.put(MongoConstants.DB_PROP_DATABASE_ALIAS, uidsArr);
		}
		String[] ids = uidsList.stream().map(this::replaceWithIdentifier).collect(Collectors.toList())
				.toArray(new String[0]);
		String[] configNames = Arrays.copyOf(ids, ids.length + 1);
		configNames[ids.length] = "mongo";
		properties.put(EMFNamespaces.EMF_CONFIGURATOR_NAME, configNames);
		return properties;
	}

	/**
	 * Replaces the given uid with an identifier
	 * {@link MongoDatabaseProvider#PROP_DATABASE_IDENTIFIER} value if it exists
	 * 
	 * @param alias the alias
	 * @return the identifier, if it exists, otherwise the alias
	 */
	private String replaceWithIdentifier(String alias) {
		String ident = aliasIdentifierMap.get(alias);
		if (ident != null && !ident.isEmpty()) {
			return ident;
		} else {
			return alias;
		}
	}

}
