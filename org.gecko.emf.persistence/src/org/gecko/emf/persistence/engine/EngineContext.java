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
package org.gecko.emf.persistence.engine;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.gecko.emf.persistence.api.Countable;
import org.gecko.emf.persistence.api.Keywords;
import org.gecko.emf.persistence.api.Options;

/**
 * Interface for a context object for a {@link PersistenceEngine}. It contains all supported features in a typed way
 * @author Mark Hoffmann
 * @since 15.02.2023
 */
public interface EngineContext {
	
	public static enum ActionType {
		COUNT,
		EXIST,
		CREATE,
		UPDATE,
		READ,
		DELETE
	}
	
	ActionType action();
	
	/**
	 * Returns the underlying resource
	 * @return the underlying resource
	 */
	Resource resource();
	
	/**
	 * Returns the resource {@link URI}. Shortcut for {@link Resource#getURI()}
	 * @return the resource {@link URI}
	 */
	URI uri();
	
	/**
	 * Returns response map, to put information back to the resource
	 * @return response map
	 */
	Map<Object, Object> response();
	
	/**
	 * Returns the effective options
	 * @return the effective options
	 */
	Map<Object, Object> effectiveOptions();
	
	/**
	 * Returns the name of the primary key {@link EAttribute}
	 * @return the name of the primary key {@link EAttribute}
	 */
	String idField();
	
	/**
	 * Returns the identifier for the type information in the database
	 * @return the identifier for the type information in the database
	 */
	String typeField();
	
	/**
	 * Returns the identifier for the super type information in the database
	 * @return the identifier for the super type information in the database
	 */
	String superTypeField();
	
	/**
	 * Returns the timestamp field name or <code>null</code>, if no timestamp field should be used
	 * @return the timestamp field name or <code>null</code>, if no timestamp field should be used
	 */
	String timestampField();
	
	/**
	 * Returns the proxy field name
	 * @return the proxy field name
	 */
	String proxyField();
	
	/**
	 * Returns the EClass URI
	 * @return the EClass URI
	 */
	String eClassURI();
	
	/**
	 * Returns the {@link EClass} instance
	 * @return the {@link EClass} instance
	 */
	EClass eClass();
	
	/**
	 * @see Options#CAP_PROXY_URI_AS_STRING
	 * @return <code>true</code> to use the proxy as uri string
	 */
	boolean useProxyAsString();
	
	/**
	 * @see Options#CAP_USE_EXTENDED_METADATA
	 * @return <code>true</code>, if to use extended metadata annotation
	 */
	boolean useExtendedMetadata();
	
	/**
	 * @see Options#CAP_USE_PERSISTENCE_ANNOTATIONS
	 * @return <code>true</code>, if to use persistence annotations
	 */
	boolean usePersistenceAnnotations();
	
	/**
	 * @see Countable#CAP_COUNT_ID_FIELD
	 * return <code>true</code>, if the id field should be used for counting
	 */
	boolean countIdField();
	
	/**
	 * @see Countable#CAP_COUNT_ECLASS_TYPE_FILTER
	 * @return <code>true</code> when to create an additional type filter
	 */
	boolean countTypeFilter();
	
	/**
	 * Returns the count field name
	 * @return the count field name
	 */
	String countField();
	
	/**
	 * Returns <code>true</code>, if the matched result should counted. The result will be returned in the response map
	 * @return <code>true</code>, if the matched result should counted
	 */
	boolean countResponse();
	
	/**
	 * Returns <code>true</code>, if the ID {@link EAttribute} in a EClass should be used as primary key
	 * @return <code>true</code>, if the ID {@link EAttribute} in a EClass should be used as primary key
	 */
	boolean useIdAttributeAsPrimaryKey();
	
	/**
	 * Instead of updating an object this option force to insert, if set to <code>true</code>
	 * @return of updating an object this option force to insert, if set to <code>true</code>
	 */
	boolean forceInsert();
	
	/**
	 * Returns <code>true</code>, if the resource should be cleared after the insert is done
	 * @return <code>true</code>, if the resource should be cleared after the insert is done
	 */
	boolean clearResourceAfterInsert();
	
	static EngineContext createContext(PersistenceEngine<?, ?, ?, ?, ?> engine, ActionType action) {
		requireNonNull(engine);
		requireNonNull(action);
		final Map<Object, Object> effectiveOptions = new HashMap<>();
		effectiveOptions.putAll(engine.getMergedOptions());

		// type information for EClasses
		EClass eClass = (EClass) effectiveOptions.getOrDefault(Options.CAP_ECLASS, null);
		String eClassUri = (String) effectiveOptions.getOrDefault(Options.CAP_ECLASS_URI, null);
		// field names for type informations
		String typeField = (String) effectiveOptions.getOrDefault(Options.CAP_FIELD_ECLASS_TYPE, Keywords.ECLASS_TYPE_KEY);
		String superTypeField = (String) effectiveOptions.getOrDefault(Options.CAP_FIELD_ECLASS_SUPERTYPES, Keywords.ECLASS_SUPER_TYPES_KEY);
		// primary key field and handling
		String idField = (String) effectiveOptions.getOrDefault(Options.CAP_ID_FIELD_NAME, Keywords.ID_KEY);
		Boolean useIdAttributeAsPrimaryKey = Boolean.TRUE.equals(effectiveOptions.get(Options.OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY));
		// Store modification timestamps
		String timestampField = (String) effectiveOptions.getOrDefault(Options.CAP_TIMESTAMP_FIELD_NAME, Keywords.TIMESTAMP_KEY);
		Boolean useTimestamp =  Boolean.TRUE.equals(effectiveOptions.get(Options.CAP_USE_TIMESTAMP));
		// field names and settings for proxy handling
		String proxyField = (String) effectiveOptions.getOrDefault(Options.CAP_FIELD_PROXY_URI, Keywords.PROXY_KEY);
		Boolean useProxyAsString =  Boolean.TRUE.equals(effectiveOptions.get(Options.CAP_PROXY_URI_AS_STRING));
		// db, table, field alias handling
		Boolean useExtendedMetadata =  Boolean.TRUE.equals(effectiveOptions.get(Options.CAP_USE_EXTENDED_METADATA));
		Boolean usePersistenceAnnotations =  Boolean.TRUE.equals(effectiveOptions.getOrDefault(Options.CAP_USE_PERSISTENCE_ANNOTATIONS, Boolean.TRUE));
		String customAnnotationSource = (String) effectiveOptions.getOrDefault(Options.CAP_ANNOTATION_SOURCE, null);
		String customAnnotationNameKey = (String) effectiveOptions.getOrDefault(Options.CAP_ANNOTATION_NAME_KEY, null);

		Boolean forceInsert = Boolean.TRUE.equals(effectiveOptions.get(Options.SAVE_FORCE_INSERT));
		Boolean clearResourceAfterInsert = !effectiveOptions.containsKey(Options.SAVE_CLEAR_RESOURCE_AFTER_BATCH_INSERT) || Boolean.TRUE.equals(effectiveOptions.get(Options.SAVE_CLEAR_RESOURCE_AFTER_BATCH_INSERT));

		Boolean countTypeFilter = Boolean.TRUE.equals(effectiveOptions.getOrDefault(Countable.CAP_COUNT_ECLASS_TYPE_FILTER, false));
		Boolean countIdField = Boolean.TRUE.equals(effectiveOptions.getOrDefault(Countable.CAP_COUNT_ID_FIELD, false));
		String countField = (String) effectiveOptions.get(Countable.CAP_COUNT_FIELD);
		
		Boolean countResults = Boolean.TRUE.equals(effectiveOptions.getOrDefault(Options.READ_COUNT_RESPONSE, false));

		return new EngineContext() {
			
			@Override
			public ActionType action() {
				return action;
			}

			@Override
			public URI uri() {
				return engine.getResource() != null ? engine.getResource().getURI() : null;
			}

			@Override
			public String typeField() {
				return typeField;
			}
			
			@Override
			public String superTypeField() {
				return superTypeField;
			}

			@Override
			public String timestampField() {
				return useTimestamp ? timestampField : null;
			}
			
			@Override
			public String proxyField() {
				return proxyField;
			}
			
			/* 
			 * (non-Javadoc)
			 * @see org.gecko.emf.persistence.engine.EngineContext#useProxyAsString()
			 */
			@Override
			public boolean useProxyAsString() {
				return useProxyAsString;
			}
			
			@Override
			public boolean useExtendedMetadata() {
				return useExtendedMetadata;
			}

			@Override
			public boolean usePersistenceAnnotations() {
				return useExtendedMetadata() ? false : usePersistenceAnnotations;
			}
			
			@Override
			public Map<Object, Object> response() {
				return engine.getResponse();
			}

			@Override
			public Resource resource() {
				return engine.getResource();
			}

			@Override
			public String idField() {
				return idField;
			}

			@Override
			public Map<Object, Object> effectiveOptions() {
				return effectiveOptions;
			}

			@Override
			public String eClassURI() {
				return eClassUri;
			}

			@Override
			public EClass eClass() {
				return eClass;
			}

			@Override
			public boolean countTypeFilter() {
				return countTypeFilter;
			}

			@Override
			public boolean countIdField() {
				return countIdField;
			}
			
			@Override
			public String countField() {
				return countField;
			}
			
			@Override
			public boolean countResponse() {
				return countResults;
			}


			@Override
			public boolean useIdAttributeAsPrimaryKey() {
				return useIdAttributeAsPrimaryKey;
			}

			@Override
			public boolean forceInsert() {
				return forceInsert;
			}

			@Override
			public boolean clearResourceAfterInsert() {
				return clearResourceAfterInsert;
			}
		};
	}
	
}
