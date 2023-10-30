/*
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sql Result Set Mapping</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 
 *         @Target({TYPE}) @Retention(RUNTIME)
 *         public @interface SqlResultSetMapping {
 *           String name();
 *           EntityResult[] entities() default {};
 *           ConstructorResult[] classes() default{};
 *           ColumnResult[] columns() default {};
 *         }
 * 
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.SqlResultSetMapping#getDescription <em>Description</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.SqlResultSetMapping#getEntityResult <em>Entity Result</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.SqlResultSetMapping#getConstructorResult <em>Constructor Result</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.SqlResultSetMapping#getColumnResult <em>Column Result</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.SqlResultSetMapping#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getSqlResultSetMapping()
 * @model extendedMetaData="name='sql-result-set-mapping' kind='elementOnly'"
 * @generated
 */
@ProviderType
public interface SqlResultSetMapping extends EObject {
	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getSqlResultSetMapping_Description()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.SqlResultSetMapping#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Entity Result</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityResult}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity Result</em>' containment reference list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getSqlResultSetMapping_EntityResult()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='entity-result' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<EntityResult> getEntityResult();

	/**
	 * Returns the value of the '<em><b>Constructor Result</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.emf.persistence.jpa.orm.model.orm.ConstructorResult}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constructor Result</em>' containment reference list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getSqlResultSetMapping_ConstructorResult()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='constructor-result' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<ConstructorResult> getConstructorResult();

	/**
	 * Returns the value of the '<em><b>Column Result</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.emf.persistence.jpa.orm.model.orm.ColumnResult}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Column Result</em>' containment reference list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getSqlResultSetMapping_ColumnResult()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='column-result' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<ColumnResult> getColumnResult();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getSqlResultSetMapping_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.SqlResultSetMapping#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // SqlResultSetMapping
