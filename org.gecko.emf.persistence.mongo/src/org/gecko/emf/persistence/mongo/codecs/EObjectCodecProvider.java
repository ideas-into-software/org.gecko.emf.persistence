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
package org.gecko.emf.persistence.mongo.codecs;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLResource.URIHandler;
import org.gecko.emf.persistence.ConverterService;
import org.gecko.emf.persistence.mongo.codecs.builder.DBObjectBuilder;
import org.gecko.emf.persistence.mongo.codecs.builder.DBObjectBuilderImpl;
import org.gecko.emf.persistence.mongo.codecs.builder.EObjectBuilder;
import org.gecko.emf.persistence.mongo.codecs.builder.EObjectBuilderImpl;
import org.gecko.emf.persistence.mongo.util.MongoResourceUriHandler;

/**
 * Mongo code provider for {@link EObject}
 * @author Mark Hoffmann
 * @since 07.05.2016
 */
public class EObjectCodecProvider implements CodecProvider {

	private DBObjectBuilder dbBuilder = null;
	private EObjectBuilder eoBuilder = null;
	private ConverterService converterService;
	private ResourceSet resourceSet = null;
	private URIHandler uriHandler;
	private URI baseUri = null;
	private Map<?, ?> options;
	private Resource resource;
	private List<Resource> resourceCache;

	public EObjectCodecProvider(Resource resource, Map<?, ?> options) {
		this(resource, options, null);
	}
	public EObjectCodecProvider(Resource resource, Map<?, ?> options, List<Resource> resourceCache) {
		this.resource = resource;
		this.resourceCache = resourceCache;
		this.resourceSet = resource.getResourceSet();
		this.options = options;
		uriHandler = (XMLResource.URIHandler) options.get(XMLResource.OPTION_URI_HANDLER);
		
		if (uriHandler == null) {
			uriHandler = new MongoResourceUriHandler();
		}
		if (resource.getURI().hasQuery()) {
			baseUri = resource.getURI().trimSegments(1).appendSegment("-1");
		} else {
			baseUri = resource.getURI();
		}
		uriHandler.setBaseURI(baseUri);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.bson.codecs.configuration.CodecProvider#get(java.lang.Class, org.bson.codecs.configuration.CodecRegistry)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
		if (converterService == null) {
			throw new IllegalStateException("Converter service instance is missing to get the provider work");
		}
		if (dbBuilder == null) {
			dbBuilder = new DBObjectBuilderImpl(converterService, uriHandler, registry, options);
		}
		if (eoBuilder == null) {
			eoBuilder = new EObjectBuilderImpl(converterService, baseUri, registry, resourceSet, options, resourceCache);
		}
		if (EObject.class.isAssignableFrom(clazz)) {
			return (Codec<T>) new EObjectCollectibleCodec(dbBuilder, eoBuilder, resource, options);
		}
		if (FeatureMap.Entry.class.isAssignableFrom(clazz)) {
			return (Codec<T>) new FeatureMapEntryCodec(dbBuilder);
		}
		if (Entry.class.isAssignableFrom(clazz)) {
			return (Codec<T>) new MapEntryCodec(dbBuilder, eoBuilder, resource);
		}
		return null;
	}

	/**
	 * Sets the instance of the converter service usually using OSGi DS
	 * @param converterService the converter instance
	 */
	public void setConverterService(ConverterService converterService) {
		this.converterService = converterService;
	}
	
	/**
	 * Sets the instance of the resource set for resolving URI
	 * @param resourceSet the resource set to set
	 */
	public void setResourceSet(ResourceSet resourceSet) {
		this.resourceSet = resourceSet;
		
	}

}
