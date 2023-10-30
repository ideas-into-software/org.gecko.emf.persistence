/*
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element Collection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 
 *         @Target({METHOD, FIELD}) @Retention(RUNTIME)
 *         public @interface ElementCollection {
 *           Class targetClass() default void.class;
 *           FetchType fetch() default LAZY;
 *         }
 * 
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getOrderBy <em>Order By</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getOrderColumn <em>Order Column</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getMapKey <em>Map Key</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getMapKeyClass <em>Map Key Class</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getMapKeyTemporal <em>Map Key Temporal</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getMapKeyEnumerated <em>Map Key Enumerated</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getMapKeyAttributeOverride <em>Map Key Attribute Override</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getMapKeyConvert <em>Map Key Convert</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getMapKeyColumn <em>Map Key Column</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getMapKeyJoinColumn <em>Map Key Join Column</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getMapKeyForeignKey <em>Map Key Foreign Key</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getColumn <em>Column</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getTemporal <em>Temporal</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getEnumerated <em>Enumerated</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getLob <em>Lob</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getAttributeOverride <em>Attribute Override</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getAssociationOverride <em>Association Override</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getConvert <em>Convert</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getCollectionTable <em>Collection Table</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getAccess <em>Access</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getFetch <em>Fetch</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getName <em>Name</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getTargetClass <em>Target Class</em>}</li>
 * </ul>
 *
 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection()
 * @model extendedMetaData="name='element-collection' kind='elementOnly'"
 * @generated
 */
@ProviderType
public interface ElementCollection extends EObject {
	/**
	 * Returns the value of the '<em><b>Order By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Order By</em>' attribute.
	 * @see #setOrderBy(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_OrderBy()
	 * @model dataType="org.gecko.emf.persistence.jpa.orm.model.orm.OrderBy"
	 *        extendedMetaData="kind='element' name='order-by' namespace='##targetNamespace'"
	 * @generated
	 */
	String getOrderBy();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getOrderBy <em>Order By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Order By</em>' attribute.
	 * @see #getOrderBy()
	 * @generated
	 */
	void setOrderBy(String value);

	/**
	 * Returns the value of the '<em><b>Order Column</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Order Column</em>' containment reference.
	 * @see #setOrderColumn(OrderColumn)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_OrderColumn()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='order-column' namespace='##targetNamespace'"
	 * @generated
	 */
	OrderColumn getOrderColumn();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getOrderColumn <em>Order Column</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Order Column</em>' containment reference.
	 * @see #getOrderColumn()
	 * @generated
	 */
	void setOrderColumn(OrderColumn value);

	/**
	 * Returns the value of the '<em><b>Map Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key</em>' containment reference.
	 * @see #setMapKey(MapKey)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_MapKey()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='map-key' namespace='##targetNamespace'"
	 * @generated
	 */
	MapKey getMapKey();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getMapKey <em>Map Key</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Map Key</em>' containment reference.
	 * @see #getMapKey()
	 * @generated
	 */
	void setMapKey(MapKey value);

	/**
	 * Returns the value of the '<em><b>Map Key Class</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Class</em>' containment reference.
	 * @see #setMapKeyClass(MapKeyClass)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_MapKeyClass()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='map-key-class' namespace='##targetNamespace'"
	 * @generated
	 */
	MapKeyClass getMapKeyClass();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getMapKeyClass <em>Map Key Class</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Map Key Class</em>' containment reference.
	 * @see #getMapKeyClass()
	 * @generated
	 */
	void setMapKeyClass(MapKeyClass value);

	/**
	 * Returns the value of the '<em><b>Map Key Temporal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Temporal</em>' attribute.
	 * @see #setMapKeyTemporal(TemporalType)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_MapKeyTemporal()
	 * @model dataType="org.gecko.emf.persistence.jpa.orm.model.orm.Temporal"
	 *        extendedMetaData="kind='element' name='map-key-temporal' namespace='##targetNamespace'"
	 * @generated
	 */
	TemporalType getMapKeyTemporal();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getMapKeyTemporal <em>Map Key Temporal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Map Key Temporal</em>' attribute.
	 * @see #getMapKeyTemporal()
	 * @generated
	 */
	void setMapKeyTemporal(TemporalType value);

	/**
	 * Returns the value of the '<em><b>Map Key Enumerated</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Enumerated</em>' attribute.
	 * @see #setMapKeyEnumerated(EnumType)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_MapKeyEnumerated()
	 * @model dataType="org.gecko.emf.persistence.jpa.orm.model.orm.Enumerated"
	 *        extendedMetaData="kind='element' name='map-key-enumerated' namespace='##targetNamespace'"
	 * @generated
	 */
	EnumType getMapKeyEnumerated();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getMapKeyEnumerated <em>Map Key Enumerated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Map Key Enumerated</em>' attribute.
	 * @see #getMapKeyEnumerated()
	 * @generated
	 */
	void setMapKeyEnumerated(EnumType value);

	/**
	 * Returns the value of the '<em><b>Map Key Attribute Override</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.emf.persistence.jpa.orm.model.orm.AttributeOverride}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Attribute Override</em>' containment reference list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_MapKeyAttributeOverride()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='map-key-attribute-override' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<AttributeOverride> getMapKeyAttributeOverride();

	/**
	 * Returns the value of the '<em><b>Map Key Convert</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.emf.persistence.jpa.orm.model.orm.Convert}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Convert</em>' containment reference list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_MapKeyConvert()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='map-key-convert' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<Convert> getMapKeyConvert();

	/**
	 * Returns the value of the '<em><b>Map Key Column</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Column</em>' containment reference.
	 * @see #setMapKeyColumn(MapKeyColumn)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_MapKeyColumn()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='map-key-column' namespace='##targetNamespace'"
	 * @generated
	 */
	MapKeyColumn getMapKeyColumn();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getMapKeyColumn <em>Map Key Column</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Map Key Column</em>' containment reference.
	 * @see #getMapKeyColumn()
	 * @generated
	 */
	void setMapKeyColumn(MapKeyColumn value);

	/**
	 * Returns the value of the '<em><b>Map Key Join Column</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.emf.persistence.jpa.orm.model.orm.MapKeyJoinColumn}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Join Column</em>' containment reference list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_MapKeyJoinColumn()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='map-key-join-column' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<MapKeyJoinColumn> getMapKeyJoinColumn();

	/**
	 * Returns the value of the '<em><b>Map Key Foreign Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Foreign Key</em>' containment reference.
	 * @see #setMapKeyForeignKey(ForeignKey)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_MapKeyForeignKey()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='map-key-foreign-key' namespace='##targetNamespace'"
	 * @generated
	 */
	ForeignKey getMapKeyForeignKey();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getMapKeyForeignKey <em>Map Key Foreign Key</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Map Key Foreign Key</em>' containment reference.
	 * @see #getMapKeyForeignKey()
	 * @generated
	 */
	void setMapKeyForeignKey(ForeignKey value);

	/**
	 * Returns the value of the '<em><b>Column</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Column</em>' containment reference.
	 * @see #setColumn(Column)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_Column()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='column' namespace='##targetNamespace'"
	 * @generated
	 */
	Column getColumn();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getColumn <em>Column</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Column</em>' containment reference.
	 * @see #getColumn()
	 * @generated
	 */
	void setColumn(Column value);

	/**
	 * Returns the value of the '<em><b>Temporal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Temporal</em>' attribute.
	 * @see #setTemporal(TemporalType)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_Temporal()
	 * @model dataType="org.gecko.emf.persistence.jpa.orm.model.orm.Temporal"
	 *        extendedMetaData="kind='element' name='temporal' namespace='##targetNamespace'"
	 * @generated
	 */
	TemporalType getTemporal();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getTemporal <em>Temporal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Temporal</em>' attribute.
	 * @see #getTemporal()
	 * @generated
	 */
	void setTemporal(TemporalType value);

	/**
	 * Returns the value of the '<em><b>Enumerated</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Enumerated</em>' attribute.
	 * @see #setEnumerated(EnumType)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_Enumerated()
	 * @model dataType="org.gecko.emf.persistence.jpa.orm.model.orm.Enumerated"
	 *        extendedMetaData="kind='element' name='enumerated' namespace='##targetNamespace'"
	 * @generated
	 */
	EnumType getEnumerated();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getEnumerated <em>Enumerated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Enumerated</em>' attribute.
	 * @see #getEnumerated()
	 * @generated
	 */
	void setEnumerated(EnumType value);

	/**
	 * Returns the value of the '<em><b>Lob</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lob</em>' containment reference.
	 * @see #setLob(Lob)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_Lob()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='lob' namespace='##targetNamespace'"
	 * @generated
	 */
	Lob getLob();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getLob <em>Lob</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lob</em>' containment reference.
	 * @see #getLob()
	 * @generated
	 */
	void setLob(Lob value);

	/**
	 * Returns the value of the '<em><b>Attribute Override</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.emf.persistence.jpa.orm.model.orm.AttributeOverride}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute Override</em>' containment reference list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_AttributeOverride()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='attribute-override' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<AttributeOverride> getAttributeOverride();

	/**
	 * Returns the value of the '<em><b>Association Override</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.emf.persistence.jpa.orm.model.orm.AssociationOverride}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Association Override</em>' containment reference list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_AssociationOverride()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='association-override' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<AssociationOverride> getAssociationOverride();

	/**
	 * Returns the value of the '<em><b>Convert</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.emf.persistence.jpa.orm.model.orm.Convert}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Convert</em>' containment reference list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_Convert()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='convert' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<Convert> getConvert();

	/**
	 * Returns the value of the '<em><b>Collection Table</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Collection Table</em>' containment reference.
	 * @see #setCollectionTable(CollectionTable)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_CollectionTable()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='collection-table' namespace='##targetNamespace'"
	 * @generated
	 */
	CollectionTable getCollectionTable();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getCollectionTable <em>Collection Table</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Collection Table</em>' containment reference.
	 * @see #getCollectionTable()
	 * @generated
	 */
	void setCollectionTable(CollectionTable value);

	/**
	 * Returns the value of the '<em><b>Access</b></em>' attribute.
	 * The literals are from the enumeration {@link org.gecko.emf.persistence.jpa.orm.model.orm.AccessType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Access</em>' attribute.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.AccessType
	 * @see #isSetAccess()
	 * @see #unsetAccess()
	 * @see #setAccess(AccessType)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_Access()
	 * @model unsettable="true"
	 *        extendedMetaData="kind='attribute' name='access'"
	 * @generated
	 */
	AccessType getAccess();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getAccess <em>Access</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Access</em>' attribute.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.AccessType
	 * @see #isSetAccess()
	 * @see #unsetAccess()
	 * @see #getAccess()
	 * @generated
	 */
	void setAccess(AccessType value);

	/**
	 * Unsets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getAccess <em>Access</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAccess()
	 * @see #getAccess()
	 * @see #setAccess(AccessType)
	 * @generated
	 */
	void unsetAccess();

	/**
	 * Returns whether the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getAccess <em>Access</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Access</em>' attribute is set.
	 * @see #unsetAccess()
	 * @see #getAccess()
	 * @see #setAccess(AccessType)
	 * @generated
	 */
	boolean isSetAccess();

	/**
	 * Returns the value of the '<em><b>Fetch</b></em>' attribute.
	 * The literals are from the enumeration {@link org.gecko.emf.persistence.jpa.orm.model.orm.FetchType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fetch</em>' attribute.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.FetchType
	 * @see #isSetFetch()
	 * @see #unsetFetch()
	 * @see #setFetch(FetchType)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_Fetch()
	 * @model unsettable="true"
	 *        extendedMetaData="kind='attribute' name='fetch'"
	 * @generated
	 */
	FetchType getFetch();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getFetch <em>Fetch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fetch</em>' attribute.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.FetchType
	 * @see #isSetFetch()
	 * @see #unsetFetch()
	 * @see #getFetch()
	 * @generated
	 */
	void setFetch(FetchType value);

	/**
	 * Unsets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getFetch <em>Fetch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetFetch()
	 * @see #getFetch()
	 * @see #setFetch(FetchType)
	 * @generated
	 */
	void unsetFetch();

	/**
	 * Returns whether the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getFetch <em>Fetch</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Fetch</em>' attribute is set.
	 * @see #unsetFetch()
	 * @see #getFetch()
	 * @see #setFetch(FetchType)
	 * @generated
	 */
	boolean isSetFetch();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Target Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target Class</em>' attribute.
	 * @see #setTargetClass(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getElementCollection_TargetClass()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='target-class'"
	 * @generated
	 */
	String getTargetClass();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection#getTargetClass <em>Target Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target Class</em>' attribute.
	 * @see #getTargetClass()
	 * @generated
	 */
	void setTargetClass(String value);

} // ElementCollection
