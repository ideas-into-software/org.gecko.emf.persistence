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
package org.gecko.emf.persistence.jdbc.streams;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.gecko.emf.persistence.api.ConverterService;
import org.gecko.emf.persistence.api.CountableOld;
import org.gecko.emf.persistence.api.Options;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.api.QueryEngine;
import org.gecko.emf.persistence.codec.EClassProvider;
import org.gecko.emf.persistence.context.ResultContextBuilder;
import org.gecko.emf.persistence.context.PersistenceInputContext;
import org.gecko.emf.persistence.helper.PersistenceHelper;
import org.gecko.emf.persistence.jdbc.JdbcPersistenceConstants;
import org.gecko.emf.persistence.jdbc.query.JdbcCountQuery;
import org.gecko.emf.persistence.jdbc.query.JdbcQuery;
import org.gecko.emf.persistence.mapping.InputContentHandler;
import org.gecko.emf.persistence.mapping.IteratorMapper;
import org.osgi.util.promise.Promise;

/**
 * Input stream implementation that handles loading of {@link Resource}
 * @author bhunt
 * @author Mark Hoffmann
 */
public class JdbcInputStream extends InputStream implements URIConverter.Loadable, CountableOld, EClassProvider {

	private final ConverterService converterService;
	private URI uri;
	private Map<Object, Object> mergedOptions = new HashMap<>();
	private QueryEngine<JdbcQuery, Statement> queryEngine;
	private Promise<Connection> connectionPromise;
	private List<InputContentHandler<ResultSet, IteratorMapper>> contentHandler;
	private Map<Object, Object> response;
	private String eClassUri;
	private String typeColumn;
	private boolean countOnly;
	private boolean countResults;
	private String idAttributeName;
	private boolean countIdAttributeFilter;
	private boolean countTypeFilter;
	private EClass eClass;
	private Map<String, EClass> eClassCache;
	private static final String QUERY_COUNT = "SELECT COUNT(%s) FROM %s";
	private static final String QUERY_ALL = "SELECT %s FROM %s";

	public JdbcInputStream(ConverterService converterService, QueryEngine<JdbcQuery, Statement>  queryEngine, Promise<Connection> connection, List<InputContentHandler<ResultSet, IteratorMapper>> contentHandler, URI uri, Map<?, ?> options, Map<Object, Object> response) throws PersistenceException {
		this.response = response;
		if (converterService == null)
			throw new NullPointerException("The converter service must not be null");
		this.converterService = converterService;
		if (connection == null)
			throw new NullPointerException("The database connection must not be null");

		this.contentHandler = contentHandler == null ? Collections.emptyList() : contentHandler;
		this.queryEngine = queryEngine;
		this.connectionPromise = connection;
		this.uri = uri;
		normalizeOptions(options);
		readOptions(mergedOptions);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.URIConverter.Loadable#loadResource(org.eclipse.emf.ecore.resource.Resource)
	 */
	@Override
	public void loadResource(Resource resource) throws IOException {

		boolean needCache = true;

		// determine the input content handler
		Optional<InputContentHandler<ResultSet, IteratorMapper>> handlerOptional = contentHandler.stream()
				.filter((ch)->ch.canHandle((Map<Object, Object>) mergedOptions))
				.findFirst();
		if (handlerOptional.isPresent()) {
			needCache = handlerOptional.get().enableResourceCache((Map<Object, Object>) mergedOptions);
		}

		// We need to set up the XMLResource.URIHandler so that proxy URIs are handled properly.EObjectCodecProvider codecProvider = new EObjectCodecProvider(resourceSet);
		final List<Resource> resourcesCache;
		if(needCache){
			resourcesCache = new ArrayList<>(resource.getContents().size());
		} else {
			resourcesCache = null;
		}

		try {
			Connection connection = connectionPromise.getValue();
			long results = -1l;

			if (eClass == null && eClassUri != null) {
				eClass = getEClass(resource.getResourceSet(), eClassUri);
			}
			String tableName = eClass != null ? PersistenceHelper.getElementNameLower(eClass) : getTable(uri, mergedOptions);
			if (eClass != null) {
				EAttribute idAttribute = eClass.getEIDAttribute();
				idAttributeName = idAttribute == null ? getTypeColumn() : PersistenceHelper.getElementEMDName(idAttribute);
			}
			// Execute a count query to get a number of all matches for that query
			if (countResults) {
				String countCol = idAttributeName == null ? getTypeColumn() : idAttributeName;
				results = executeCount(connection, tableName.toUpperCase(), countCol);
				// If returning counting result / mapping results as response value is active
				response.put(Options.OPTION_COUNT_RESPONSE, Long.valueOf(results));
			}

			// Step 1 - create query and execute it
			JdbcQuery jdbcQuery = queryEngine.buildQuery(uri, mergedOptions);
			ResultSet resultSet = jdbcQuery.executeQuery(connection, tableName, null);
			
			// Step 2 - build input context to handle the result
			PersistenceInputContext<ResultSet, IteratorMapper> inputContext = (PersistenceInputContext<ResultSet, IteratorMapper>)new ResultContextBuilder<ResultSet, IteratorMapper>()
					.result(resultSet)
					.resource(resource)
					.options(mergedOptions)
					.resourceCache(resourcesCache)
					.converterService(converterService)
					.build();

			// Step 3 - create default mapper to create EObjects out of the result
			final IteratorMapper inputMapper = new JdbcEObjectCodec(inputContext, this);
			inputMapper.initialize();


			// Step 4 - handle results depending on the configuration
			handlerOptional.ifPresentOrElse((ich)->{
				EObject result = ich.createContent(inputContext);
				if (result != null) {
					resource.getContents().add(result);
				}
			}, ()->defaultHandleResult(inputMapper, resource));
			
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		EObjectCodecProvider codecProvider = new EObjectCodecProvider(resource, mergedOptions, resourcesCache);
		//		codecProvider.setConverterService(converterService);
		//		CodecRegistry eobjectRegistry = CodecRegistries.fromProviders(codecProvider);
		//		CodecRegistry defaultRegistry = collection.getCodecRegistry();
		//
		//		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(eobjectRegistry, defaultRegistry);
		//		// get collections and clear it
		//		MongoCollection<EObject> curCollection = collection.withCodecRegistry(codecRegistry).withDocumentClass(EObject.class);
		//
		//
		//		// If the URI contains a query string, use it to locate a collection of objects from
		//		// MongoDB, otherwise simply get the object from MongoDB using the id.
		//
		//		EList<EObject> contents = resource.getContents();
		//		
		//		if (uri.query() != null && !isProjectionOnly(uri.query())) {
		//			if (queryEngine == null) {
		//				throw new IOException("The query engine was not found");
		//			}
		//
		//			Request queryRequest = queryEngine.buildQuery(uri, mergedOptions);
		//			ResultSet resultIterable = null;
		//
		//			boolean countResults = false;
		//			Object optionCountResult = mergedOptions.get(Options.OPTION_COUNT_RESULT);
		//			countResults = optionCountResult != null && Boolean.TRUE.equals(optionCountResult);
		//
		//			Bson filter = queryRequest.getFilter();
		//			Document projection = queryRequest.getProjection();
		//
		//			long elementCount = -1l;
		//			if (filter != null) {
		//				resultIterable = curCollection.find(filter);
		//				if (countResults) {
		//					elementCount = curCollection.countDocuments(filter);
		//				}
		//			} else {
		//				resultIterable = curCollection.find();
		//				if (countResults) {
		//					elementCount = curCollection.countDocuments();
		//				}
		//			}
		//			if (countResults) {
		//				response.put(Options.OPTION_COUNT_RESPONSE, Long.valueOf(elementCount));
		//			}
		//			
		//
		//			if (projection != null) {
		//				resultIterable.projection(projection);
		//			}
		//
		//			if (queryRequest.getSkip() != null && queryRequest.getSkip() > 0)
		//				resultIterable.skip(queryRequest.getSkip());
		//
		//			if (queryRequest.getSort() != null)
		//				resultIterable = resultIterable.sort(queryRequest.getSort());
		//
		//			if (queryRequest.getLimit() != null && queryRequest.getLimit() > 0)
		//				resultIterable = resultIterable.limit(queryRequest.getLimit());
		//
		//			if (queryRequest.getBatchSize() != null && queryRequest.getBatchSize() > 0) {
		//				resultIterable.batchSize(queryRequest.getBatchSize().intValue());
		//			}
		//
		//			final FindIterable<EObject> iterable = resultIterable;
		//			
		//			handlerOptional.ifPresent((ich)->{
		//				EObject result = ich.createContent(iterable, (Map<Object, Object>) mergedOptions, resourcesCache);
		//				if (result != null) {
		//					contents.add(result);
		//				}
		//			});
		//
		//			if (!handlerOptional.isPresent()) {
		//
		//				EReferenceCollection eCollection = CollectionFactory.eINSTANCE.createEReferenceCollection();
		//				InternalEList<EObject> values = (InternalEList<EObject>) eCollection.getValues();
		//				try(MongoCursor<EObject> mongoCursor = resultIterable.iterator()){
		//					while (mongoCursor.hasNext()){
		//						EObject dbObject = mongoCursor.next();
		//						if(Boolean.TRUE.equals(mergedOptions.get(Options.OPTION_LAZY_RESULT_LOADING))){
		//							((InternalEObject) dbObject).eSetProxyURI(EcoreUtil.getURI(dbObject).appendQuery(null));
		//							detachEObject(dbObject);
		//						}
		//						if (Boolean.TRUE.equals(mergedOptions.get(Options.OPTION_READ_DETACHED))) {
		//							detachEObject(dbObject);
		//						}
		//						values.addUnique(dbObject);
		//					}
		//				}
		//
		//				contents.add(eCollection);
		//			}
		//			if(!Boolean.TRUE.equals(mergedOptions.get(Options.OPTION_LAZY_RESULT_LOADING)) && needCache){
		//				resource.getResourceSet().getResources().addAll(resourcesCache);
		//			}
		//		} else {
		//			
		//			FindIterable<EObject> find = curCollection.find(new Document(Keywords.ID_KEY, MongoUtils.getID(uri)), EObject.class);
		//			
		//			if(uri.query() != null) {
		//				if (queryEngine == null) {
		//					throw new IOException("The query engine was not found");
		//				}
		//
		//				EMongoQuery mongoQuery = queryEngine.buildQuery(uri, mergedOptions);
		//				Document projectionOnly = mongoQuery.getProjectionOnly();
		//				if (projectionOnly != null) {
		//					find = find.projection(projectionOnly);
		//				}
		//				
		//			}
		//			EObject dbObject = find.first();
		//
		//			if (dbObject != null) {
		//				contents.add(dbObject);
		//			}
		//		}

	}

	/* 
	 * (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		// InputStream requires that we implement this function. It will never be called
		// since this implementation implements URIConverter.Loadable. The loadResource()
		// function will be called instead.
		return -1;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.Countable#count(org.eclipse.emf.common.util.URI, java.util.Map, java.util.Map)
	 */
	@Override
	public long count(URI uri, Map<?, ?> options, Map<Object, Object> response) {
		try {
			if (countIdAttributeFilter) {
				if (idAttributeName != null) {
	
				}
			}
			Connection connection = connectionPromise.getValue();
			String tableName = getTable(uri, mergedOptions);
			String idAttribute = getIdAttribute(uri, mergedOptions);
			// Execute a count query to get a number of all matches for that query
			return executeCount(connection, tableName, idAttribute != null ? idAttribute : getTypeColumn());
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return -1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.Countable#exists(org.eclipse.emf.common.util.URI, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean exists(URI uri, Map<?, ?> options, Map<Object, Object> response) throws PersistenceException {
		return count(uri, options, response) > 0;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.EClassProvider#getEClassFromResourceSet(org.eclipse.emf.ecore.resource.ResourceSet, java.lang.String)
	 */
	public EClass getEClassFromResourceSet(ResourceSet resourceSet, String eClassURI) {
		URI theUri = URI.createURI(eClassURI);
		String classifier = theUri.lastSegment();
		EPackage ePackage = resourceSet.getPackageRegistry().getEPackage(theUri.trimSegments(1).trimFragment().toString());
		if(ePackage != null) {
			EClassifier eClassifier = (EClassifier) ePackage.getEClassifier(classifier);
			if(eClassifier != null && eClassifier instanceof EClass) {
				return (EClass) eClassifier;
			}
		}
	
		return (EClass) resourceSet.getEObject(theUri, true);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.EClassProvider#getEClass(org.eclipse.emf.ecore.resource.ResourceSet, java.lang.String)
	 */
	public EClass getEClass(ResourceSet resourceSet, String eClassURI) {
		if (eClassCache != null) {
			synchronized (eClassCache) {
				EClass eClass = eClassCache.get(eClassURI);
	
				if (eClass == null) {
					eClass = getEClassFromResourceSet(resourceSet, eClassURI);
					eClassCache.put(eClassURI, eClass);
				}
				return eClass;
			}
		}
	
		return getEClassFromResourceSet(resourceSet, eClassURI);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.EClassProvider#getTypeColumn()
	 */
	public String getTypeColumn() {
		return typeColumn;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.EClassProvider#getIDColumn()
	 */
	@Override
	public String getIDColumn() {
		return idAttributeName;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.jdbc.streams.EClassProvider#getConfiguredEClass()
	 */
	@Override
	public EClass getConfiguredEClass() {
		return eClass;
	}

	

//	/**
//	 * @param query
//	 * @return
//	 */
//	private boolean isProjectionOnly(String query) {
//		//		Document d = Document.parse(URI.decode(query));
//		//		return d.containsKey("projectionOnly");
//		return false;
//	}
//
//	/**
//	 * Detaches the given {@link EObject}
//	 * @param eobject the eobject instance
//	 */
//	private void detachEObject(EObject eobject) {
//		if (eobject == null) {
//			return;
//		}
//		Resource resource = eobject.eResource();
//		if (resource != null) {
//			resource.getContents().clear();
//			if(resource.getResourceSet() != null){
//				resource.getResourceSet().getResources().remove(resource);
//			}
//		}
//	}

	/**
	 * Default way to handle mapping of the results
	 * @param inputMapper the {@link EObject} mapper
	 * @param resource the loading resource
	 */
	protected void defaultHandleResult(final IteratorMapper inputMapper, Resource resource) {
		try {
			EList<EObject> contents = resource.getContents();
			// counter for all mapped elements
			long mappedCount = 0l;
			while (inputMapper.hasNext()) {
				EObject next = inputMapper.next();
				contents.add(next);
			}
			// write count results
			if (!countResults) {
				// If returning counting result / mapping results as response value is active
				response.put(Options.OPTION_COUNT_RESPONSE, Long.valueOf(mappedCount));
			}
		} catch (PersistenceException e) {
			inputMapper.close();
			e.printStackTrace();
		}
	}

	/**
	 * Executes a count statement
	 * @param connection the databse connection
	 * @param tableName the table name to count
	 * @param idAttribute an optional id attribute, to make count more performant
	 * @return the count result;
	 * @throws SQLException
	 */
	protected long executeCount(Connection connection, String tableName, String column)
			throws SQLException {
		long result = -1l;
		JdbcCountQuery countQuery = new JdbcCountQuery();
		ResultSet countRS = countQuery.executeQuery(connection, tableName, column);
		if (countRS.next()) {
			result = countRS.getLong(1);
		}
		return result;
	}

	/**
	 * Executes a count statement
	 * @param connection the databse connection
	 * @param tableName the table name to count
	 * @param idAttribute an optional id attribute, to make count more performant
	 * @return the count result;
	 * @throws SQLException
	 */
	protected long executeCount(Connection connection, String tableName, EAttribute idAttribute)
			throws SQLException {
		return executeCount(connection, tableName, idAttribute.getName());
	}
	
	protected String getId(URI uri, Map<?, ?> options) {
		return uri.segmentCount() > 2 ? uri.segment(2) : null;
	}

	protected String getTable(URI uri, Map<?, ?> options) {
		return uri.segment(1);
	}

	protected String getIdAttribute(URI uri, Map<?, ?> options) {
		return uri.fragment();
	}

	/**
	 * @param mergedOptions2
	 */
	private void readOptions(Map<Object, Object> mergedOptions2) {
		eClass = (EClass) mergedOptions.getOrDefault(Options.OPTION_ECLASS_HINT, null);
		eClassUri = (String) mergedOptions.getOrDefault(Options.OPTION_ECLASS_URI_HINT, null);
		idAttributeName = (String) mergedOptions.getOrDefault(Options.OPTION_ECLASS_IDATTRIBUTE_HINT, null);
		typeColumn = (String) mergedOptions.getOrDefault(Options.OPTION_KEY_ECLASS_URI, JdbcPersistenceConstants.ECLASS_TYPE_COLUMN_NAME);
	
		countIdAttributeFilter = Boolean.TRUE.equals(mergedOptions.getOrDefault(CountableOld.OPTION_COUNT_ID_ATTRIBUTE, false));
		countTypeFilter = Boolean.TRUE.equals(mergedOptions.getOrDefault(CountableOld.OPTION_COUNT_URI_FILTER, false));
		countOnly = Boolean.TRUE.equals(mergedOptions.getOrDefault(Options.OPTION_COUNT_RESULT, false));
		countResults = Boolean.TRUE.equals(mergedOptions.getOrDefault(Options.OPTION_COUNT_RESULT, false));
	}

	/**
	 * Normalizes the load options
	 * @param options the original options
	 */
	private <K extends Object, V extends Object> void normalizeOptions(Map<K, V> options) {
		mergedOptions.putAll(options);
		EClass filterEClass = (EClass) options.getOrDefault(Options.OPTION_FILTER_ECLASS, null);
		EClass collectionEClass = Options.getTableEClass(options);
		if (collectionEClass != null && filterEClass == null) {
			mergedOptions.put(Options.OPTION_FILTER_ECLASS, collectionEClass);
		}
	}

}
