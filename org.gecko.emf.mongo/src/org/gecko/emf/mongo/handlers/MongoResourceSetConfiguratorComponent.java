package org.gecko.emf.mongo.handlers;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.gecko.emf.mongo.InputStreamFactory;
import org.gecko.emf.mongo.OutputStreamFactory;
import org.gecko.emf.osgi.configurator.ResourceSetConfigurator;
import org.gecko.emf.osgi.constants.EMFNamespaces;
import org.gecko.mongo.osgi.MongoDatabaseProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * This implementation of the ResourceSetConfigurator service will attach
 * all currently bound URI handlers to the ResourceSet. This service is
 * intended to be used with the IResourceSetFactory service.
 * 
 * @author bhunt
 * 
 */
@Component(name="MongoResourceSetConfiguratorComponent", immediate=true)
public class MongoResourceSetConfiguratorComponent {

	public static final String PROP_MONGO_ALIAS = "database.alias";
	private final Map<String, MongoDatabaseProvider> mongoDatabaseProviders = new ConcurrentHashMap<String, MongoDatabaseProvider>();
	private ServiceRegistration<ResourceSetConfigurator> configuratorRegistration;
	private MongoURIHandlerProvider uriHandlerProvider = new MongoURIHandlerProvider();
	private BundleContext ctx;
	private List<String> aliases = new LinkedList<String>();
	private Map<String, String> aliasIdentifierMap = new HashMap<String, String>();

	/**
	 * Called on component activation
	 * @param context the component context
	 */
	@Activate
	public void activate(ComponentContext context) {
		ctx = context.getBundleContext();
		Dictionary<String, Object> properties = getDictionary();
		configuratorRegistration = ctx.registerService(ResourceSetConfigurator.class, new MongoResourceSetConfigurator(uriHandlerProvider), properties);
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
	 * Adds a {@link MongoDatabaseProvider} to the provider map.  
	 * @param mongoDatabaseProvider the provider to be added
	 */
	@Reference(name="MongoDatabaseProvider", policy=ReferencePolicy.DYNAMIC, cardinality=ReferenceCardinality.AT_LEAST_ONE, unbind="removeMongoDatabaseProvider")
	public void addMongoDatabaseProvider(MongoDatabaseProvider mongoDatabaseProvider, Map<String, Object> properties) {
		String uri = mongoDatabaseProvider.getURI();
		mongoDatabaseProviders.put(uri, mongoDatabaseProvider);
		uriHandlerProvider.addMongoDatabaseProvider(mongoDatabaseProvider);
		updateProperties(MongoDatabaseProvider.PROP_ALIAS, properties, true);
	}

	/**
	 * Removes a {@link MongoDatabaseProvider} from the map 
	 * @param mongoDatabaseProvider the provider to be removed
	 */
	public void removeMongoDatabaseProvider(MongoDatabaseProvider mongoDatabaseProvider, Map<String, Object> properties) {
		String uri = mongoDatabaseProvider.getURI();
		mongoDatabaseProviders.remove(uri);
		uriHandlerProvider.removeMongoDatabaseProvider(mongoDatabaseProvider);
		updateProperties(MongoDatabaseProvider.PROP_ALIAS, properties, false);
	}

	/**
	 * Sets an {@link InputStreamFactory} to handle input streams
	 * @param inputStreamFactory the factory to set
	 */
	@Reference(name="InputStreamFactory")
	public void setInputStreamFactory(InputStreamFactory inputStreamFactory) {
		uriHandlerProvider.setInputStreamFactory(inputStreamFactory);
	}

	/**
	 * Sets an {@link OutputStreamFactory} to handle output streams
	 * @param outputStreamFactory the factory to set
	 */
	@Reference(name="OutputStreamFactory")
	public void setOutputStreamFactory(OutputStreamFactory outputStreamFactory) {
		uriHandlerProvider.setOutputStreamFactory(outputStreamFactory);
	}

	/**
	 * Updates the properties of the service, depending on changes on injected services
	 * @param type the type of the property to publish 
	 * @param serviceProperties the service properties from the injected service
	 * @param add <code>true</code>, if the service was add, <code>false</code> in case of an remove
	 */
	private void updateProperties(String type, Map<String, Object> serviceProperties, boolean add) {
		Object name = serviceProperties.get(type);
		Object identifier = serviceProperties.get(MongoDatabaseProvider.PROP_DATABASE_IDENTIFIER);
		if (name != null && name instanceof String) {
			switch (type) {
			case MongoDatabaseProvider.PROP_ALIAS:
				if (add) {
					aliases.add(name.toString());
					if (identifier != null && identifier instanceof String) {
						aliasIdentifierMap.put(name.toString(), identifier.toString());
					}
				} else {
					aliases.remove(name.toString());
					aliasIdentifierMap.remove(name.toString());
				}
				break;
			default:
				break;
			}
			updateRegistrationProperties();
		}
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
	 * @return a dictionary for the stored properties
	 */
	private Dictionary<String, Object> getDictionary() {
		Dictionary<String, Object> properties = new Hashtable<>();
		List<String> aliasList = new LinkedList<String>(aliases);
		String[] aliasNames = aliasList.toArray(new String[0]);
		if (aliasNames.length > 0) {
			properties.put(PROP_MONGO_ALIAS, aliasNames);
		}
		String[] ids = aliasList.stream()
				.map(this::replaceWithIdentifier)
				.collect(Collectors.toList())
				.toArray(new String[0]);
		String[] configNames = Arrays.copyOf(ids, ids.length + 1);
		configNames[ids.length] = "mongo";
 		properties.put(EMFNamespaces.EMF_CONFIGURATOR_NAME, configNames);
		return properties;
	}
	
	/**
	 * Replaces the given alias with an identifier {@link MongoDatabaseProvider#PROP_DATABASE_IDENTIFIER} value
	 * if it exists
	 * @param alias the alias
	 * @return the identifier, if it exists, otherwise the alias
	 */
	private String replaceWithIdentifier(String alias) {
		String id = aliasIdentifierMap.get(alias);
		if (id != null && !id.isEmpty()) {
			return id;
		} else {
			return alias;
		}
	}
	
}
