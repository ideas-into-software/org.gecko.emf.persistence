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
package org.gecko.emf.persistence.mongo.streams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.gecko.emf.collection.CollectionFactory;
import org.gecko.emf.collection.ECollection;
import org.gecko.emf.collection.EReferenceCollection;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.Options;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.api.PrimaryKeyFactory;
import org.gecko.emf.persistence.context.QueryContext;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.gecko.emf.persistence.mongo.util.MongoUtils;
import org.gecko.emf.persistence.streams.PersistenceOutputStream;
import org.osgi.util.promise.Promise;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.WriteModel;

/**
 * Mongo output stream, that can save a {@link Resource}
 * @author bhunt
 * @author Mark Hoffmann
 * @param <K>
 */
public class MongoOutputStream2 extends PersistenceOutputStream<MongoCollection<Document>, MongoCollection<EObject>, EObjectMapper> implements URIConverter.Saveable {

	private static final ReplaceOptions UPDATE_OPTIONS = new ReplaceOptions().upsert(true);

	public MongoOutputStream2(ConverterService converterService, Promise<MongoCollection<Document>> collection, URI uri, Map<String, PrimaryKeyFactory> idProviders, Map<?, ?> options, Map<Object, Object> response) {
		super(converterService, collection, uri, idProviders, options, response);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.streams.PersistenceOutputStream#createNativeId()
	 */
	@Override
	protected Object createNativeId() {
		return new ObjectId();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.streams.PersistenceOutputStream#getIDUriSegment(org.eclipse.emf.common.util.URI)
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
	 * @see org.gecko.emf.persistence.streams.PersistenceOutputStream#createMapper()
	 */
	@Override
	protected EObjectMapper createMapper() {
		return null;
	}

	/**
	 * Returns the normalized mongo id
	 * @param id a prepared id
	 * @return the normalized mongo id
	 */
	private Object normalizeMongoId(Object id) {
		URI uri = getUri();
		if (id == null) {
			PrimaryKeyFactory mongoIdFactory = getIdFactories().get(uri.trimSegments(uri.segmentCount() - 2).toString());
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


	private Bson createUpdateFilter(EObject eObject) throws PersistenceException {
		URI uri = getUri();
		String idKey = "_id";
		Object id = null;
		try {
			if (!isUseIdAttributeAsPrimaryKey()) {
				String pkId;
				pkId = MongoUtils.getIDAsString(getResource().getURI());
				if (pkId != null && !pkId.isEmpty()) {
					id = normalizeMongoId(pkId);
				} else {
					EAttribute idAttribute = eObject.eClass().getEIDAttribute();
					idKey = idAttribute == null ? "_id" : idAttribute.getName();
					id = EcoreUtil.getID(eObject);
				}
			} else {
				id = EcoreUtil.getID(eObject);
				if(id == null){
					id = MongoUtils.getID(uri);
				} else {
					id = normalizeMongoId(id);
				}
			}
		} catch (IOException e) {
			throw new PersistenceException("Cannot create update filter", e);
		}

		return Filters.eq(idKey, id);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.streams.PersistenceOutputStream#saveMultipleObjects(org.gecko.emf.persistence.context.QueryContext)
	 */
	@Override
	protected void saveMultipleObjects(QueryContext<MongoCollection<EObject>, ?, EObjectMapper> inputContext)
			throws PersistenceException {
		MongoCollection<EObject> collection = inputContext.getDriver();
		Resource resource = inputContext.getResource();
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

			if(idAttribute == null && isUseIdAttributeAsPrimaryKey()){
				throw new IllegalStateException("EObjects have no ID Attribute to be used together with option " +  Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY);
			}

			if (idAttribute != null && isUseIdAttributeAsPrimaryKey()) {
				Object id = eObject.eGet(idAttribute);
				if(id == null){
					id = normalizeMongoId(null);
					eObject.eSet(idAttribute, EcoreUtil.createFromString(idAttribute.getEAttributeType(), id.toString()));
				}
			}
			if(isForceInsert()){
				bulk.add(new InsertOneModel<EObject>(eObject));
			} else {
				Bson updateFilter = createUpdateFilter(eObject);
				bulk.add(new ReplaceOneModel<EObject>(updateFilter, eObject, UPDATE_OPTIONS));
			}
		}
		collection.bulkWrite(bulk);

		if(isClearResourceAfterInsert()){
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
	 * @see org.gecko.emf.persistence.streams.PersistenceOutputStream#saveSingleObject(org.gecko.emf.persistence.context.QueryContext)
	 */
	@Override
	protected void saveSingleObject(QueryContext<MongoCollection<EObject>, ?, EObjectMapper> context)
			throws PersistenceException {
		Resource resource = context.getResource();
		MongoCollection<EObject> collection = context.getDriver();
		EObject eObject = resource.getContents().get(0);
		if(isForceInsert()){
			collection.insertOne(eObject);
		} else {
			Bson updateFilter = createUpdateFilter(eObject);
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

}
