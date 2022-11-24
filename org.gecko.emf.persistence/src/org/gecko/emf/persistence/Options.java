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
package org.gecko.emf.persistence;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * 
 * @author mark
 * @since 26.03.2022
 */
public interface Options {
	
	/**
	 * This option can be set to tell the serializer and de-serializer to use the enum literal instead of the default enum name.
	 * This property only work with generated enum that also implement {@link Enumerator}. Otherwise this property will be ignored.
	 * The {@link DBObjectBuilderImpl} and {@link EObjectBuilderImpl} are using this option
	 * 
	 * value type: Boolean, default is <code>false</code>
	 */
	String OPTION_USE_ENUM_LITERAL = "STORE_ENUM_LITERAL";
	
	/**
	 * This option can be set to tell the persistence client, if to count the resulting elements of a query.
	 * This option has a performance impact, because it is an additional query. The result will be provided as {@link Long} value in the
	 * {@link Resource} as response option with Key {@link #OPTION_COUNT_RESPONSE}.
	 * 
	 * To get the response options you have to set {@link URIConverter#OPTION_RESPONSE} with a new map
	 * to the load options
	 * 
	 * value type: Boolean, default is <code>false</code>
	 */
	String OPTION_COUNT_RESULT = "COUNT_RESULT";
	
	/**
	 * This option can be set to tell the persistence client, if to count the resulting elements of a query.
	 * This option only executes a count and does no further query and mapping. The result will be provided as {@link Long} value in the
	 * {@link Resource} as response option with Key {@link #OPTION_COUNT_RESPONSE}.
	 * 
	 * To get the response options you have to set {@link URIConverter#OPTION_RESPONSE} with a new map
	 * to the load options
	 * 
	 * value type: Boolean, default is <code>false</code>
	 */
	String OPTION_COUNT_ONLY = "COUNT_ONLY";
	
	/**
	 * This option is a response options. It will be set if {@link Options#OPTION_COUNT_RESULT} is set to <code>true</code>.
	 * In this case this property key is, corresponding to its {@link Long} value, that contains the result count
	 * of elements for a filter query. As long as you have set a {@link URIConverter#OPTION_RESPONSE} with a map 
	 * to the load options, this value is than available in this map
	 * 
	 * value type: Boolean, default is <code>false</code>
	 */
	String OPTION_COUNT_RESPONSE = "COUNT_RESPONSE";
	
	/**
	 * When you load a resource with a query, only information that are necessary to build a proxy 
	 * will be loaded from the database. Thus the results will be lazy while iterating over the returned 
	 * {@link ECollections}.
	 * 
	 * This only works with query without projection.
	 * 
	 * value type: Boolean
	 */
	String OPTION_LAZY_RESULT_LOADING = "LAZY_RESULT_LOADING";
	
	/**
	 * When you load an object with cross-document references, they will be proxies. When you access
	 * the reference, EMF will resolve the proxy and you can then access the attributes. This can
	 * cause
	 * performance problems for example when expanding a tree where you only need a name attribute to
	 * display the children and then only resolve the next child to be expanded. Setting this option
	 * to
	 * Boolean.TRUE will cause the proxy instance to have its attribute values populated so that you
	 * can display the child names in the tree without resolving the proxy.
	 * 
	 * Value type: Boolean
	 */
	String OPTION_PROXY_ATTRIBUTES = BinaryResourceImpl.OPTION_STYLE_PROXY_ATTRIBUTES;

	/**
	 * EMF's default serialization is designed to conserve space by not serializing attributes that
	 * are set to their default value. This is a problem when attempting to query objects by an
	 * attributes default value. By setting this option to Boolean.TRUE, all attribute values will be stored to
	 * MongoDB.
	 * 
	 * Value type: Boolean, default is <code>false</code>
	 */
	String OPTION_SERIALIZE_DEFAULT_ATTRIBUTE_VALUES = "SERIALIZE_DEFAULT_ATTRIBUTE_VALUES";

	/**
	 * To avoid writing unnecessary URIs in the DB, mongo emf writes eClassUris only for the root class and for 
	 * EReferences, where the actual value does not equal but inherit from the stated reference type. 
	 * default value. By setting this option to Boolean.TRUE, all eClass URIs will be written regardless. 
	 * 
	 * Value type: Boolean. default is <code>false</code>
	 */
	String OPTION_SERIALIZE_ALL_ECLASS_URIS = "SERIALIZE_ALL_ECLASS_URIS";

	/**
	 * If it is set to Boolean.TRUE and the ID was not specified in the URI, the value of the ID
	 * attribute will be used as the MongoDB _id if it exists.
	 * 
	 * Value type: Boolean, default is <code>true</code>
	 */
	String OPTION_USE_ID_ATTRIBUTE_AS_PRIMARY_KEY = "USE_ID_ATTRIBUTE_AS_PRIMARY_KEY";

	/**
	 * If set to Boolean.TRUE, a query will return a Cursor/Iterator instead of a result
	 * 
	 * Value type: Boolean, default is <code>false</code>
	 */
	String OPTION_QUERY_CURSOR = "QUERY_CURSOR";
	
	/**
	 * This option may be used when you wish to set a non standard batch size for writing data or reading data from a MongoDB
	 * 
	 * <code>
	 * resourceSet.getLoadOptions().put(Options.OPTION_BATCH_SIZE, 400);
	 * </code>
	 * 
	 * Value type: {@link Integer}
	 */
	String OPTION_BATCH_SIZE = "BATCH_SIZE";

	/**
	 * This option may be used when you wish to force an insert even if there is an ID set. Default is <code>false</code>, which would urge 
	 * mongoDB to perform an update.
	 * 
	 * <code>
	 * resourceSet.getLoadOptions().put(Options.OPTION_FORCE_INSERT, Boolean.TRUE);
	 * </code>
	 * 
	 * Value type: {@link Boolean}, default is <code>false</code>
	 */
	String OPTION_FORCE_INSERT = "FORCE_INSERT";
	
	/**
	 * This option may be used when you wish to customizer serialization and/or de-serialization, using an
	 * alternative {@link EStructuralFeature} name. This can be achieved by annotating the metamodel 
	 * using the ExtendedMetadate with detail entry "name"  
	 * 
	 * <code>
	 * resourceSet.getLoadOptions().put(Options.OPTION_USE_EXTENDED_METADATA, Boolean.TRUE);
	 * </code>
	 * 
	 * Value type: {@link Boolean}, default is <code>false</code>
	 */
	String OPTION_USE_EXTENDED_METADATA = "USE_EXTENDED_METADATA";
	
	/**
	 * This option may be used when you wish to customize serialization and/or de-serialization, using an
	 * alternative name for the EClass Uri type column. The default is {@link Keywords#ECLASS_KEY}
	 * 
	 * <code>
	 * resourceSet.getLoadOptions().put(Options.OPTION_ECLASS_URI_HINT, "_type");
	 * </code>
	 * 
	 * Value type: {@link String}
	 */
	String OPTION_KEY_ECLASS_URI = "KEY_ECLASS_URI";
	
	/**
	 * This option may be used when you wish to provide the EClass type information as URI string via load or save option
	 * You will need that when e.g. loading data from a table that does not contain {@link EClass} type
	 * information.
	 * 
	 * <code>
	 * resourceSet.getLoadOptions().put(Options.OPTION_KEY_ECLASS_URI, EcoreUtil.getURI(myEClass).toString());
	 * </code>
	 * 
	 * Value type: {@link String}
	 */
	String OPTION_ECLASS_URI_HINT = "ECLASS_URI_HINT";
	
	/**
	 * This option may be used when you wish to provide the EClass type information via load or save option
	 * You will need that when e.g. loading data from a table that does not contain {@link EClass} type
	 * information.
	 * 
	 * <code>
	 * resourceSet.getLoadOptions().put(Options.OPTION_KEY_ECLASS, myEClass);
	 * </code>
	 * 
	 * Value type: {@link EClass}
	 */
	String OPTION_ECLASS_HINT = "ECLASS_HINT";
	
	/**
	 * This option may be used when you wish to provide the EClass'es id-attribute information via load or save option
	 * You will need that when e.g. loading data from a table that does not contain {@link EClass} type
	 * information.
	 * 
	 * <code>
	 * resourceSet.getLoadOptions().put(Options.OPTION_KEY_ECLASS_URI, EcoreUtil.getURI(myEClass).toString());
	 * </code>
	 * 
	 * Value type: {@link String}
	 */
	String OPTION_ECLASS_IDATTRIBUTE_HINT = "ECLASS_IDATTRIBUTE_HINT";
	
	/**
	 * This option may be used when you wish to customize serialization and/or de-serialization, using an
	 * alternative field name for the EClass super types. The default is {@link Keywords#SUPER_TYPES_KEY}
	 * 
	 * <code>
	 * resourceSet.getLoadOptions().put(Options.OPTION_KEY_SUPERTYPES, "_mySuperType");
	 * resourceSet.getSaveOptions().put(Options.OPTION_KEY_SUPERTYPES, "_mySuperType");
	 * </code>
	 * 
	 * Value type: {@link String}
	 */
	String OPTION_KEY_SUPERTYPES = "KEY_SUPERTYPES";
	
	/**
	 * This option may be used when you wish to customize serialization and/or de-serialization, using an
	 * alternative name for the EProxyUri. The default is {@link Keywords#PROXY_KEY}
	 * 
	 * <code>
	 * resourceSet.getLoadOptions().put(Options.OPTION_KEY_PROXY_URI, "_proxy");
	 * </code>
	 * 
	 * Value type: {@link String}
	 */
	String OPTION_KEY_PROXY_URI = "KEY_PROXY_URI";

	/**
	 * If set to <code>true</code> the resource containing the batch will be cleared after saving and will not be populated with proxies
	 * 
	 * <code>
	 * resourceSet.getLoadOptions().put(Options.OPTION_CLEAR_RESOURCE_AFTER_BATCH_INSERT, Boolean.TRUE);
	 * </code>
	 * 
	 * Value type: {@link Boolean}, default is <code>false</code>
	 */
	String OPTION_CLEAR_RESOURCE_AFTER_BATCH_INSERT = "CLEAR_RESOURCE_AFTER_BATCH_INSERT";
	
	/**
	 * If set to <code>true</code> the resource will be detached from the read {@link EObject}
	 * 
	 * <code>
	 * resourceSet.getLoadOptions().put(Options.OPTION_READ_DETACHED, Boolean.TRUE);
	 * </code>
	 * 
	 * Value type: {@link Boolean}, default is <code>false</code>
	 */
	String OPTION_READ_DETACHED = "READ_DETACHED";
	
	/**
	 * If set to <code>true</code> the proxies will be stored into one field instead of a complex type
	 * 
	 * <code>
	 * resourceSet.getLoadOptions().put(Options.OPTION_PROXY_URI_AS_STRING, Boolean.TRUE);
	 * </code>
	 * 
	 * Value type: {@link Boolean}, default is <code>false</code>
	 */
	String OPTION_PROXY_URI_AS_STRING = "PROXY_URI_AS_STRING";

	/**
	 * If set a {@link Map} with {@link EReference}es as key and a String representing a query that can be handled by the {@link NativeQueryEngine}  
	 * 
	 * <code>
	 * resourceSet.getLoadOptions().put(Options.OPTION_PROJECT_FOR_PROXIES, Collections.sinlgetonMap(eReference, queryString));
	 * </code>
	 */
	String OPTION_QUERY_FOR_PROXIES = "QUERY_FOR_PROXIES";
	
	/**
	 * If set to <code>true</code>, all super types for the given object are stored as array of {@link String}. 
	 * This array contains the ECLass URI's of all super types. The field is created with name '_superTypes', right after the _eClass
	 * Default value is <code>false</code>
	 * <code>
	 * resourceSet.getSaveOptions().put(Options.OPTION_STORE_SUPERTYPE, Boolean.TRUE));
	 * </code>
	 */
	String OPTION_STORE_SUPERTYPE = "STORE_SUPERTYPE";
	
	/**
	 * If set to an {@link EClass}, an additional filter will be created, that filters against
	 * all _eClass fields or look in the _superType array for the EClass URI. 
	 * 
	 * <code>
	 * resourceSet.getLoadOptions().put(Options.OPTION_FILTER_ECLASS, BasicPackage.Literals.MYTEST));
	 * </code>
	 */
	String OPTION_FILTER_ECLASS = "FILTER_ECLASS";
	
	/**
	 * If set to <code>true</code> and the OPTION_FILTER_ECLASS, is set an additional filter will be created, 
	 * that filters against all _eClass fields only to match only this EClass type. 
	 * Default value is <code>false</code>
	 * 
	 * <code>
	 * resourceSet.getLoadOptions().put(Options.OPTION_FILTER_ECLASS, BasicPackage.Literals.MYTEST));
	 * resourceSet.getLoadOptions().put(Options.OPTION_FILTER_ECLASS_STRICT, Boolean.TRUE));
	 * </code>
	 */
	String OPTION_FILTER_ECLASS_STRICT = "FILTER_ECLASS_STRICT";
	
	/**
	 * If set to an {@link EClass} or a {@link String} EMF persistence uses the give value as database/collection
	 * 
	 * <code>
	 * resourceSet.getSaveOptions().put(Options.OPTION_TABLE_NAME, BasicPackage.Literals.MYTEST));
	 * resourceSet.getSaveOptions().put(Options.OPTION_TABLE_NAME, "mycollection"));
	 * resourceSet.getLoadOptions().put(Options.OPTION_TABLE_NAME, BasicPackage.Literals.MYTEST));
	 * resourceSet.getLoadOptions().put(Options.OPTION_TABLE_NAME, "mycollection"));
	 * </code>
	 */
	String OPTION_TABLE_NAME = "DATABASE_NAME";
	
	/**
	 * Returns <code>true</code>, if the {@link Options#OPTION_USE_EXTENDED_METADATA}
	 * was set to <code>true</code>, otherwise <code>false</code>
	 * @param options the options
	 * @return <code>true</code>, if the option was set
	 */
	public static boolean isUseExtendedMetadata(Map<?, ?> options) {
		if (options == null) {
			return false;
		}
		Object result = options.get(OPTION_USE_EXTENDED_METADATA);
		return Boolean.TRUE.equals(result);
	}
	
	/**
	 * Returns the proxy key, if the {@link Options#OPTION_KEY_PROXY_URI}
	 * was set to a value, otherwise the default {@link Keywords#PROXY_KEY}
	 * @param options the options
	 * @return the key {@link String}, if the option was set otherwise the default value 
	 */
	public static String getProxyUriKey(Map<?, ?> options) {
		if (options == null) {
			return Keywords.PROXY_KEY;
		}
		Object result = options.get(OPTION_KEY_PROXY_URI);
		return result == null ? Keywords.PROXY_KEY : result.toString();
	}
	
	/**
	 * Returns the EClass key, if the {@link Options#OPTION_KEY_ECLASS_URI}
	 * was set to a value, otherwise the default {@link Keywords#ECLASS_KEY}
	 * @param options the options
	 * @return the key {@link String}, if the option was set otherwise the default value 
	 */
	public static String getEClassKey(Map<?, ?> options) {
		if (options == null) {
			return Keywords.ECLASS_KEY;
		}
		Object result = options.get(OPTION_KEY_ECLASS_URI);
		return result == null ? Keywords.ECLASS_KEY : result.toString();
	}
	
	/**
	 * Returns the field key, if the {@link Options#OPTION_KEY_SUPERTYPES}
	 * was set to a value, otherwise the default {@link Keywords#SUPER_TYPES_KEY}
	 * @param options the options
	 * @return the key {@link String}, if the option was set otherwise the default value 
	 */
	public static String getSuperType(Map<?, ?> options) {
		if (options == null) {
			return Keywords.SUPER_TYPES_KEY;
		}
		Object result = options.get(OPTION_KEY_SUPERTYPES);
		return result == null ? Keywords.SUPER_TYPES_KEY : result.toString();
	}
	
	/**
	 * Returns the EClass for the property OPTION_FILTER_ECLASS or <code>null</code>.
	 * It throws an exception, if the value is not of the expected type
	 * @param options the options map
	 * @return the {@link EClass} or <code>null</code>
	 */
	public static EClass getFilterEClass(Map<?, ?> options) {
		if (options == null) {
			return null;
		}
		Object result = options.getOrDefault(OPTION_FILTER_ECLASS, null);
		if (result == null) {
			return null;
		}
		if (result instanceof EClass) {
			return (EClass) result;
		} else {
			throw new IllegalStateException("The property OPTION_FILTER_ECLASS is expected to have a value of type EClass but was: " + result.getClass().getName());
		}
	}
	
	/**
	 * Returns the table/collection {@link EClass} for the given options
	 * @param options the options to check
	 * @return the table/collection {@link EClass} or <code>null</code>
	 */
	public static EClass getTableEClass(Map<?, ?> options) {
		if (options == null) {
			return null;
		}
		Object alias = getTableObject(options);
		if (alias instanceof EClass) {
			return (EClass)alias;
		}
		return null;
	}
	
	/**
	 * Returns the table {@link EClass} for the given options
	 * @param options the options to check
	 * @return the table {@link EClass} or <code>null</code>
	 */
	public static Object getTableObject(Map<?, ?> options) {
		if (options == null) {
			return null;
		}
		Object alias = options.getOrDefault(Options.OPTION_TABLE_NAME, null);
		return alias;
	}
	
	/**
	 * Returns the table/collection name for the given options
	 * @param options the options to check
	 * @return the table name or <code>null</code>
	 */
	public static String getTableName(Map<?, ?> options) {
		if (options == null) {
			return null;
		}
		Object alias = getTableObject(options);
		if (alias instanceof EClass) {
			return ((EClass)alias).getName();
		}
		return alias == null ? null : alias.toString();
	}

}
