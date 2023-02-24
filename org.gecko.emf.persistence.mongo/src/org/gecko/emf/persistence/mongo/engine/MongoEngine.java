/**
 * Copyright (c) 2012 - 2023 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.mongo.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.gecko.emf.collection.CollectionFactory;
import org.gecko.emf.collection.ECollection;
import org.gecko.emf.collection.EReferenceCollection;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.Options;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.api.PrimaryKeyFactory;
import org.gecko.emf.persistence.api.QueryEngine;
import org.gecko.emf.persistence.context.QueryContext;
import org.gecko.emf.persistence.context.ResultContext;
import org.gecko.emf.persistence.engine.DefaultPersistenceEngine;
import org.gecko.emf.persistence.engine.EngineContext;
import org.gecko.emf.persistence.helper.ConcurrentHelper;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.mapping.InputContentHandler;
import org.gecko.emf.persistence.model.mongo.EMongoQuery;
import org.gecko.emf.persistence.mongo.codecs.EObjectCodecProvider;
import org.gecko.emf.persistence.mongo.util.MongoUtils;
import org.gecko.persistence.mongo.InfoMongoDatabase;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.util.promise.Promise;
import org.osgi.util.promise.PromiseFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.WriteModel;

/**
 * Mongo persistence engine implementation
 * @author Mark Hoffmann
 * @since 24.02.2023
 */
@Component(scope = ServiceScope.PROTOTYPE, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class MongoEngine extends DefaultPersistenceEngine<MongoCollection<EObject>, EObjectMapper, FindIterable<EObject>, EMongoQuery, QueryEngine<EMongoQuery, FindIterable<EObject>>> {

	private static final ReplaceOptions UPDATE_OPTIONS = new ReplaceOptions().upsert(true);

	private final PromiseFactory pf = new PromiseFactory(Executors.newCachedThreadPool(ConcurrentHelper.createThreadFactory("MongoPersistence-Connection")));
	private final Map<String, Object> mongoDatabaseProperties = new HashMap<>();
	private MongoDatabase mongoDatabase;
	@Reference(name = "resourceSet")
	private ResourceSet resourceSet;

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.engine.BasicPersistenceEngine#setConverterService(org.gecko.emf.persistence.api.ConverterService)
	 */
	@Override
	@Reference(name = "converterService")
	public void setConverterService(ConverterService converterService) {
		super.setConverterService(converterService);
	}
	
	public void unsetConverterService(ConverterService converterService) {
		super.setConverterService(null);
		dispose();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.engine.BasicPersistenceEngine#setQueryEngine(org.gecko.emf.persistence.api.QueryEngine)
	 */
	@Override
	@Reference(name = "queryEngine")
	public void setQueryEngine(QueryEngine<EMongoQuery, QueryEngine<EMongoQuery, FindIterable<EObject>>> queryEngine) {
		super.setQueryEngine(queryEngine);
	}
	
	public void unsetQueryEngine(QueryEngine<EMongoQuery, QueryEngine<EMongoQuery, FindIterable<EObject>>> queryEngine) {
		super.setQueryEngine(null);
		dispose();
	}
	
	/**
	 * Adds a {@link InfoMongoDatabase} to the provider map.
	 * @param mongoDatabase the provider to be added
	 */
	@Reference(name = "mongoDatabase", policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.AT_LEAST_ONE)
	public void setMongoDatabase(InfoMongoDatabase mongoDatabase, Map<String, Object> map) {
		mongoDatabaseProperties.putAll(map);
		this.mongoDatabase = mongoDatabase;
	}

	/**
	 * Removes a {@link InfoMongoDatabase} from the map
	 * @param mongoDatabase the provider to be removed
	 */
	public void unsetMongoDatabase(InfoMongoDatabase mongoDatabase, Map<String, Object> map) {
		mongoDatabaseProperties.clear();
		this.mongoDatabase = null;
	}

	@Reference(name="contentHandler", cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
	public void addContentHandler(InputContentHandler<FindIterable<EObject>, EObjectMapper> contentHandler) {
		super.addInputHandler(contentHandler);
	}

	public void removeContentHandler(InputContentHandler<FindIterable<EObject>, EObjectMapper> contentHandler) {
		super.removeInputHandler(contentHandler);
	}

	/**
	 * Sets the id factory 
	 * @param mongoIdFactory the id factory to be added
	 */
	@Reference(name="primaryKeyFactory", policy=ReferencePolicy.DYNAMIC, cardinality=ReferenceCardinality.MANDATORY)
	public void addPrimaryKeyFactory(PrimaryKeyFactory pkFactory) {
		super.addPrimaryKeyFactory(pkFactory);
	}

	/**
	 * Un-sets the id factory 
	 * @param mongoIdFactory the id factory to be removed
	 */
	public void removePrimaryKeyFactory(PrimaryKeyFactory pkFactory) {
		super.removePrimaryKeyFactory(pkFactory);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.engine.DefaultPersistenceEngine#createMapper(org.gecko.emf.persistence.context.ResultContext)
	 */
	@Override
	protected EObjectMapper createMapper(ResultContext<FindIterable<EObject>, EObjectMapper> inputContext) {
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.engine.DefaultPersistenceEngine#getDriver(org.gecko.emf.persistence.engine.EngineContext)
	 */
	@Override
	protected Promise<MongoCollection<EObject>> getDriver(EngineContext context) {
		return getMongoCollection(context).map(dr->{
			EObjectCodecProvider codecProvider = new EObjectCodecProvider(getResource(), getMergedOptions(), getResourcesCache());
			codecProvider.setConverterService(getConverterService());
			CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
			CodecRegistry defaultRegistry = dr.getCodecRegistry();

			CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
			// get collections and clear it
			MongoCollection<EObject> collection = dr.withCodecRegistry(codecRegistry).withDocumentClass(EObject.class);
			return collection;
		});
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.engine.DefaultPersistenceEngine#createNativePrimaryKey()
	 */
	@Override
	protected Object createNativePrimaryKey() {
		return new ObjectId();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.engine.DefaultPersistenceEngine#getIDUriSegment(org.eclipse.emf.common.util.URI)
	 */
	@Override
	protected String getIDUriSegment(URI uri) {
		try {
			return MongoUtils.getIDAsString(uri);
		} catch (IOException e) {
			throw new IllegalStateException("Cannot convert uri into Mongo id", e);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.engine.DefaultPersistenceEngine#executeRead(org.gecko.emf.persistence.context.QueryContext)
	 */
	@Override
	protected FindIterable<EObject> executeRead(
			QueryContext<MongoCollection<EObject>, EMongoQuery, EObjectMapper> context) {
		MongoCollection<EObject> collection = context.getDriver();
		EMongoQuery mongoQuery = context.getQuery();
		Bson filter = mongoQuery.getFilter();
		Document projection = mongoQuery.getProjection();
		boolean countResults = context.countResponse();

		long elementCount = -1l;
		FindIterable<EObject> resultIterable;
		if (filter != null) {
			resultIterable = collection.find(filter);
			if (countResults) {
				elementCount = collection.countDocuments(filter);
			}
		} else {
			resultIterable = collection.find();
			if (countResults) {
				elementCount = collection.countDocuments();
			}
		}
		if (countResults) {
			context.getResponse().put(Options.READ_COUNT_RESPONSE, Long.valueOf(elementCount));
		}


		if (projection != null) {
			resultIterable.projection(projection);
		}

		if (mongoQuery.getSkip() != null && mongoQuery.getSkip() > 0)
			resultIterable.skip(mongoQuery.getSkip());

		if (mongoQuery.getSort() != null)
			resultIterable = resultIterable.sort(mongoQuery.getSort());

		if (mongoQuery.getLimit() != null && mongoQuery.getLimit() > 0)
			resultIterable = resultIterable.limit(mongoQuery.getLimit());

		if (mongoQuery.getBatchSize() != null && mongoQuery.getBatchSize() > 0) {
			resultIterable.batchSize(mongoQuery.getBatchSize().intValue());
		}

		return resultIterable;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.engine.DefaultPersistenceEngine#executeCount(org.gecko.emf.persistence.context.QueryContext)
	 */
	@Override
	protected long executeCount(QueryContext<MongoCollection<EObject>, EMongoQuery, EObjectMapper> context)
			throws PersistenceException {
		MongoCollection<EObject> collection = context.getDriver();
		EMongoQuery mongoQuery = context.getQuery();
		Bson filter = mongoQuery.getFilter();
		long elementCount = -1l;
		if (filter != null) {
			elementCount = collection.countDocuments(filter);
		} else {
			elementCount = collection.countDocuments();
		}
		if (context.countResponse()) {
			context.getResponse().put(Options.READ_COUNT_RESPONSE, Long.valueOf(elementCount));
		}
		return elementCount;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.engine.DefaultPersistenceEngine#executeDelete(org.gecko.emf.persistence.context.QueryContext)
	 */
	@Override
	protected long executeDelete(QueryContext<MongoCollection<EObject>, EMongoQuery, EObjectMapper> inputContext)
			throws PersistenceException {
		// It is assumed that delete is called with the URI path /database/collection/id
		EngineContext context = inputContext.getEngineContext();
		MongoCollection<EObject> collection = inputContext.getDriver();
		try {
			collection.findOneAndDelete(new BasicDBObject(context.idField(), MongoUtils.getID(context.uri())));
			return 1;
		} catch (IOException e) {
			throw new PersistenceException("Error deleting file", e);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.engine.DefaultPersistenceEngine#saveMultipleObjects(org.gecko.emf.persistence.context.QueryContext)
	 */
	@Override
	protected void saveMultipleObjects(QueryContext<MongoCollection<EObject>, ?, EObjectMapper> inputContext)
			throws PersistenceException {
		MongoCollection<EObject> collection = inputContext.getDriver();
		Resource resource = inputContext.getResource();
		EngineContext context = inputContext.getEngineContext();
		resource.setURI(resource.getURI().trimSegments(1).appendSegment(""));
		List<EObject> contents = null;

		if (resource.getContents().get(0) instanceof ECollection) {
			contents = ((ECollection) resource.getContents().get(0)).getValues();
		} else {
			contents = resource.getContents();
		}

		List<WriteModel<EObject>> bulk = new ArrayList<>(contents.size()); 
		for (EObject eObject : contents) {
			EAttribute idAttribute = eObject.eClass().getEIDAttribute();

			if(idAttribute == null && context.useIdAttributeAsPrimaryKey()){
				throw new PersistenceException("EObjects have no ID Attribute to be used together with option " +  Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY);
			}

			if (idAttribute != null && context.useIdAttributeAsPrimaryKey()) {
				Object id = eObject.eGet(idAttribute);
				if(id == null){
					id = normalizeMongoId(null, context.uri());
					eObject.eSet(idAttribute, EcoreUtil.createFromString(idAttribute.getEAttributeType(), id.toString()));
				}
			}
			if(context.forceInsert()){
				bulk.add(new InsertOneModel<EObject>(eObject));
			} else {
				Bson updateFilter = createUpdateFilter(eObject, context);
				bulk.add(new ReplaceOneModel<EObject>(updateFilter, eObject, UPDATE_OPTIONS));
			}
		}
		collection.bulkWrite(bulk);

		if(context.clearResourceAfterInsert()){
			resource.getContents().clear();
		} else {
			URI baseURI = resource.getURI().trimSegments(1);
			InternalEObject[] eObjects = contents.toArray(new InternalEObject[contents.size()]);
			EReferenceCollection eCollection = CollectionFactory.eINSTANCE.createEReferenceCollection();
			InternalEList<EObject> values = (InternalEList<EObject>) eCollection.getValues();

			for (int i = 0; i < contents.size(); i++) {
				InternalEObject internalEObject = eObjects[i];
				internalEObject.eSetProxyURI(baseURI.appendSegment(EcoreUtil.getID(internalEObject)).appendFragment("/"));
				internalEObject.eAdapters().clear();
				values.addUnique(internalEObject);
			}
			resource.getContents().clear();
			resource.getContents().add(eCollection);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.engine.DefaultPersistenceEngine#saveSingleObject(org.gecko.emf.persistence.context.QueryContext)
	 */
	@Override
	protected void saveSingleObject(QueryContext<MongoCollection<EObject>, ?, EObjectMapper> inputContext)
			throws PersistenceException {
		Resource resource = inputContext.getResource();
		MongoCollection<EObject> collection = inputContext.getDriver();
		EngineContext context = inputContext.getEngineContext();
		EObject eObject = resource.getContents().get(0);
		if(context.forceInsert()){
			collection.insertOne(eObject);
		} else {
			Bson updateFilter = createUpdateFilter(eObject, context);
			FindOneAndReplaceOptions farOptions = new FindOneAndReplaceOptions().upsert(true).returnDocument(ReturnDocument.AFTER);
			EAttribute idAttribute = eObject.eClass().getEIDAttribute();
			// Minimize the load by just adding projection for minimum set of attributes
			if (idAttribute != null) {
				String eClassKey = Options.getEClassKey((Map<?, ?>) getMergedOptions());
				farOptions = farOptions.projection(Projections.include(idAttribute.getName(), eClassKey));
			}
			EObject replaced = collection.findOneAndReplace(updateFilter, eObject, farOptions);
			Resource replacedResource = replaced.eResource();
			String primaryKey = replacedResource.getURI().lastSegment();
			if (replacedResource.equals(resource)) {
				replacedResource.getContents().remove(replaced);
			} else {
				replacedResource.getContents().remove(replaced);
				replacedResource.getResourceSet().getResources().remove(replacedResource);
			}
			if (primaryKey != null) {
				resource.setURI(resource.getURI().trimSegments(1).appendSegment(primaryKey));
			}
		}
	}

	/**
	 * Returns the {@link MongoCollection}. Clients may extend this. 
	 * The default extracts the collection name from the URI and appends the value from the 
	 * {@link Options#OPTIONS_COLLECTION_PARTITION_EXTENSION}, if it set
	 * 
	 * @param context the {@link EngineContext}
	 * @return the {@link MongoCollection} {@link Promise}
	 */
	private Promise<MongoCollection<Document>> getMongoCollection(EngineContext context) {
		return pf.submit(()->{
		String collectionName = getTable(context);
		MongoCollection<Document> dbCollection = mongoDatabase.getCollection(collectionName);
		return dbCollection;
		});
	}


	/**
	 * Returns the normalized mongo id
	 * @param id a prepared id
	 * @param uri the uri
	 * @return the normalized mongo id
	 */
	private Object normalizeMongoId(Object id, URI uri) {
		if (id == null) {
			PrimaryKeyFactory mongoIdFactory = getPrimaryKeyFactoryMap().get(uri.trimSegments(uri.segmentCount() - 2).toString());
			if (mongoIdFactory != null) {
				id = mongoIdFactory.getNextId();
			} 
		}
		if (id == null) {
			return new ObjectId();
		}
		if (id instanceof ObjectId) {
			return id;
		}
		if(ObjectId.isValid(id.toString())){
			id = new ObjectId(id.toString());
		} else {
			id = id.toString();
		}
		return id;
	}


	/**
	 * Creates an update filter for the given EObject
	 * @param eObject the {@link EObject} to be updated
	 * @param context the engine context
	 * @return the {@link Bson} filter expression
	 * @throws PersistenceException
	 */
	private Bson createUpdateFilter(EObject eObject, EngineContext context) throws PersistenceException {
		Object id = null;
		URI uri = context.uri();
		String idKey = context.idField();
		try {
			if (!context.useIdAttributeAsPrimaryKey()) {
				String pkId;
				pkId = MongoUtils.getIDAsString(getResource().getURI());
				if (pkId != null && !pkId.isEmpty()) {
					id = normalizeMongoId(pkId, uri);
				} else {
					// TODO take care of useExtendedMetadata, PersistenceAnnotations 
					EAttribute idAttribute = eObject.eClass().getEIDAttribute();
					idKey = idAttribute == null ? idKey : idAttribute.getName();
					id = EcoreUtil.getID(eObject);
				}
			} else {
				id = EcoreUtil.getID(eObject);
				if(id == null){
					id = MongoUtils.getID(uri);
				} else {
					id = normalizeMongoId(id, uri);
				}
			}
		} catch (IOException e) {
			throw new PersistenceException("Cannot create update filter", e);
		}
		return Filters.eq(idKey, id);
	}

}
