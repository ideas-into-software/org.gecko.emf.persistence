/*
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Collection Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 
 *         @Target({METHOD, FIELD}) @Retention(RUNTIME)
 *         public @interface CollectionTable {
 *           String name() default "";
 *           String catalog() default "";
 *           String schema() default "";
 *           JoinColumn[] joinColumns() default {};
 *           UniqueConstraint[] uniqueConstraints() default {};
 *           Index[] indexes() default {};
 *         }
 * 
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.CollectionTable#getJoinColumn <em>Join Column</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.CollectionTable#getForeignKey <em>Foreign Key</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.CollectionTable#getUniqueConstraint <em>Unique Constraint</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.CollectionTable#getIndex <em>Index</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.CollectionTable#getCatalog <em>Catalog</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.CollectionTable#getName <em>Name</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.CollectionTable#getSchema <em>Schema</em>}</li>
 * </ul>
 *
 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getCollectionTable()
 * @model extendedMetaData="name='collection-table' kind='elementOnly'"
 * @generated
 */
@ProviderType
public interface CollectionTable extends EObject {
	/**
	 * Returns the value of the '<em><b>Join Column</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.emf.persistence.jpa.orm.model.orm.JoinColumn}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Join Column</em>' containment reference list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getCollectionTable_JoinColumn()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='join-column' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<JoinColumn> getJoinColumn();

	/**
	 * Returns the value of the '<em><b>Foreign Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Foreign Key</em>' containment reference.
	 * @see #setForeignKey(ForeignKey)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getCollectionTable_ForeignKey()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='foreign-key' namespace='##targetNamespace'"
	 * @generated
	 */
	ForeignKey getForeignKey();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.CollectionTable#getForeignKey <em>Foreign Key</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Foreign Key</em>' containment reference.
	 * @see #getForeignKey()
	 * @generated
	 */
	void setForeignKey(ForeignKey value);

	/**
	 * Returns the value of the '<em><b>Unique Constraint</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.emf.persistence.jpa.orm.model.orm.UniqueConstraint}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unique Constraint</em>' containment reference list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getCollectionTable_UniqueConstraint()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='unique-constraint' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<UniqueConstraint> getUniqueConstraint();

	/**
	 * Returns the value of the '<em><b>Index</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.emf.persistence.jpa.orm.model.orm.Index}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' containment reference list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getCollectionTable_Index()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='index' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<Index> getIndex();

	/**
	 * Returns the value of the '<em><b>Catalog</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Catalog</em>' attribute.
	 * @see #setCatalog(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getCollectionTable_Catalog()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='catalog'"
	 * @generated
	 */
	String getCatalog();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.CollectionTable#getCatalog <em>Catalog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Catalog</em>' attribute.
	 * @see #getCatalog()
	 * @generated
	 */
	void setCatalog(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getCollectionTable_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.CollectionTable#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Schema</em>' attribute.
	 * @see #setSchema(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getCollectionTable_Schema()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='schema'"
	 * @generated
	 */
	String getSchema();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.CollectionTable#getSchema <em>Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schema</em>' attribute.
	 * @see #getSchema()
	 * @generated
	 */
	void setSchema(String value);

} // CollectionTable
