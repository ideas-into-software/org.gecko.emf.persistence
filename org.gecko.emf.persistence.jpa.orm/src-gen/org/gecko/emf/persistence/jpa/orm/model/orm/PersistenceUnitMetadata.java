/*
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Persistence Unit Metadata</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 
 *         Metadata that applies to the persistence unit and not just to 
 *         the mapping file in which it is contained. 
 * 
 *         If the xml-mapping-metadata-complete element is specified,
 *         the complete set of mapping metadata for the persistence unit 
 *         is contained in the XML mapping files for the persistence unit.
 * 
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.PersistenceUnitMetadata#getDescription <em>Description</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.PersistenceUnitMetadata#getXmlMappingMetadataComplete <em>Xml Mapping Metadata Complete</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.PersistenceUnitMetadata#getPersistenceUnitDefaults <em>Persistence Unit Defaults</em>}</li>
 * </ul>
 *
 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getPersistenceUnitMetadata()
 * @model extendedMetaData="name='persistence-unit-metadata' kind='elementOnly'"
 * @generated
 */
@ProviderType
public interface PersistenceUnitMetadata extends EObject {
	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getPersistenceUnitMetadata_Description()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.PersistenceUnitMetadata#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Xml Mapping Metadata Complete</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Xml Mapping Metadata Complete</em>' containment reference.
	 * @see #setXmlMappingMetadataComplete(EmptyType)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getPersistenceUnitMetadata_XmlMappingMetadataComplete()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='xml-mapping-metadata-complete' namespace='##targetNamespace'"
	 * @generated
	 */
	EmptyType getXmlMappingMetadataComplete();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.PersistenceUnitMetadata#getXmlMappingMetadataComplete <em>Xml Mapping Metadata Complete</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Xml Mapping Metadata Complete</em>' containment reference.
	 * @see #getXmlMappingMetadataComplete()
	 * @generated
	 */
	void setXmlMappingMetadataComplete(EmptyType value);

	/**
	 * Returns the value of the '<em><b>Persistence Unit Defaults</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Persistence Unit Defaults</em>' containment reference.
	 * @see #setPersistenceUnitDefaults(PersistenceUnitDefaults)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getPersistenceUnitMetadata_PersistenceUnitDefaults()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='persistence-unit-defaults' namespace='##targetNamespace'"
	 * @generated
	 */
	PersistenceUnitDefaults getPersistenceUnitDefaults();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.PersistenceUnitMetadata#getPersistenceUnitDefaults <em>Persistence Unit Defaults</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Persistence Unit Defaults</em>' containment reference.
	 * @see #getPersistenceUnitDefaults()
	 * @generated
	 */
	void setPersistenceUnitDefaults(PersistenceUnitDefaults value);

} // PersistenceUnitMetadata
