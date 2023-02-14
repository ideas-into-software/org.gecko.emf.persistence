/**
 * Copyright (c) 2012 - 2022 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Byan Hunt -  initial API and implementation
 *     Ed Merks - initial API and implementation
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.streams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gecko.emf.collection.ECollection;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.Options;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.api.PrimaryKeyFactory;
import org.gecko.emf.persistence.context.QueryContext;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.osgi.util.promise.Promise;

/**
 * Mongo output stream, that can save a {@link Resource}
 * @author Mark Hoffmann
 * @param <K>
 */
public abstract class PersistenceOutputStream<DRIVER_RAW, DRIVER, MAPPER extends EObjectMapper> extends ByteArrayOutputStream implements URIConverter.Saveable {

	private Promise<DRIVER> driver;
	private final Map<Object, Object> mergedOptions = new HashMap<>();
	private final ConverterService converterService;
	private Resource resource;
	private URI uri;
	private Map<String, PrimaryKeyFactory> idFactories;
	private final boolean useIdAttributeAsPrimaryKey;
	private final boolean forceInsert;
	private final boolean clearResourceAfterInsert;

	public PersistenceOutputStream(ConverterService converterService, Promise<DRIVER_RAW> driver, URI uri, Map<String, PrimaryKeyFactory> idProviders, Map<?, ?> options, Map<Object, Object> response) {
		if (converterService == null) {
			throw new NullPointerException("The converter service must not be null");
		}
		this.converterService = converterService;
		if (driver == null) {
			throw new NullPointerException("The driver must not be null");
		}
		this.driver = configureDriver(driver);
		this.uri = uri;
		this.idFactories = idProviders;
		normalizeOptions(options);
		Boolean useIdAttributeAsPrimaryKey = (Boolean) options.get(Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY);
		this.useIdAttributeAsPrimaryKey = (useIdAttributeAsPrimaryKey == null || useIdAttributeAsPrimaryKey);
		this.forceInsert = Boolean.TRUE.equals(options.get(Options.OPTION_FORCE_INSERT));
		this.clearResourceAfterInsert = !options.containsKey(Options.OPTION_CLEAR_RESOURCE_AFTER_BATCH_INSERT) || Boolean.TRUE.equals(options.get(Options.OPTION_CLEAR_RESOURCE_AFTER_BATCH_INSERT));
	}

	/* 
	 * (non-Javadoc)
	 * @see java.io.ByteArrayOutputStream#close()
	 */
	@Override
	public void close() throws IOException {
		super.close();

		try {
			DRIVER d = driver.getValue();

			MAPPER mapper = createMapper();
			QueryContext<DRIVER, ?, MAPPER> inputContext = QueryContext.<DRIVER, Object, MAPPER>createContextBuilder().
					converterService(converterService).
					mapper(mapper).
					driver(d).
					resource(resource).
					build();
			if (resource.getContents().size() > 1 || resource.getContents().get(0) instanceof ECollection) {
				saveMultipleObjects(inputContext);
			} else {
				EObject eObject = resource.getContents().get(0);
				EAttribute idAttribute = eObject.eClass().getEIDAttribute();
				String uriId = getIDUriSegment(uri);
				if(idAttribute == null && useIdAttributeAsPrimaryKey){
					throw new IllegalStateException("EObject has no ID Attribute to be used together with option " +  Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY);
				} 
				if(useIdAttributeAsPrimaryKey){
					Object objectId = eObject.eGet(idAttribute);
					if(objectId != null){
						if(uriId == null || uriId.isEmpty()){
							resource.setURI(resource.getURI().trimSegments(1).appendSegment(objectId.toString()));
							uri = resource.getURI();
						}
					} else {
						if(uriId != null && !uriId.isEmpty()){
							eObject.eSet(idAttribute, EcoreUtil.createFromString(idAttribute.getEAttributeType(), uriId));
						} else {
							PrimaryKeyFactory mongoIdFactory = idFactories.get(uri.trimSegments(uri.segmentCount() - 2).toString());
							Object newId = null;
							if (mongoIdFactory != null) {
								newId = mongoIdFactory.getNextId();
							} else {
								//								newId = new ObjectId();
								newId = createNativeId();
							}
							uri = uri.trimSegments(1).appendSegment(newId.toString());
							resource.setURI(uri);
							eObject.eSet(idAttribute, EcoreUtil.createFromString(idAttribute.getEAttributeType(), newId.toString()));
						}
					}
				}
				saveSingleObject(inputContext);
			}
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param driver
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Promise<DRIVER> configureDriver(Promise<DRIVER_RAW> driver) {
		return driver.map(dr->(DRIVER)dr);
	}

	/**
	 * @return
	 */
	protected abstract Object createNativeId();

	/**
	 * @param uri2
	 * @return
	 */
	protected abstract String getIDUriSegment(URI uri);

	/**
	 * @return
	 */
	protected abstract MAPPER createMapper();

	/**
	 * Saves many objects using bulk/batch operations
	 * @param inputContext the input context
	 * @throws PersistenceException thrown on errors during save
	 */
	protected abstract void saveMultipleObjects(QueryContext<DRIVER, ?, MAPPER> inputContext) throws PersistenceException;

	/**
	 * Saves a single object into a driver
	 * @param context the input context
	 * @throws PersistenceException thrown on errors during saving
	 */
	protected abstract void saveSingleObject(QueryContext<DRIVER, ?, MAPPER> context) throws PersistenceException;

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.URIConverter.Saveable#saveResource(org.eclipse.emf.ecore.resource.Resource)
	 */
	@Override
	public void saveResource(Resource resource) {
		this.resource = resource;
	}

	/**
	 * Returns the clearResourceAfterInsert.
	 * @return the clearResourceAfterInsert
	 */
	public boolean isClearResourceAfterInsert() {
		return clearResourceAfterInsert;
	}

	/**
	 * Returns the forceInsert.
	 * @return the forceInsert
	 */
	public boolean isForceInsert() {
		return forceInsert;
	}

	/**
	 * Returns the useIdAttributeAsPrimaryKey.
	 * @return the useIdAttributeAsPrimaryKey
	 */
	public boolean isUseIdAttributeAsPrimaryKey() {
		return useIdAttributeAsPrimaryKey;
	}

	/**
	 * Returns the converterService.
	 * @return the converterService
	 */
	public ConverterService getConverterService() {
		return converterService;
	}

	/**
	 * Returns the mergedOptions.
	 * @return the mergedOptions
	 */
	public Map<Object, Object> getMergedOptions() {
		return mergedOptions;
	}

	/**
	 * Returns the resource.
	 * @return the resource
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * Returns the uri.
	 * @return the uri
	 */
	public URI getUri() {
		return uri;
	}

	/**
	 * Returns the idFactories.
	 * @return the idFactories
	 */
	public Map<String, PrimaryKeyFactory> getIdFactories() {
		return idFactories;
	}

	/**
	 * Normalizes the save options
	 * @param options the original options
	 */
	private <K extends Object, V extends Object> void normalizeOptions(Map<K, V> options) {
		mergedOptions.putAll(options);
		Boolean storeSuperType = (Boolean) options.getOrDefault(Options.OPTION_STORE_SUPERTYPE, null);
		String collectionName = Options.getTableName(options);
		if (collectionName != null && storeSuperType == null) {
			mergedOptions.put(Options.OPTION_STORE_SUPERTYPE, Boolean.TRUE);
		}
	}

}
