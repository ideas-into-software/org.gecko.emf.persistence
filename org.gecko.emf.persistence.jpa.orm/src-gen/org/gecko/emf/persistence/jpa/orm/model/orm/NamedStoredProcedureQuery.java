/*
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Named Stored Procedure Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 
 *         @Target({TYPE}) @Retention(RUNTIME)
 *         public @interface NamedStoredProcedureQuery {
 *           String name();
 *           String procedureName();
 *           StoredProcedureParameter[] parameters() default {};
 *           Class[] resultClasses() default {};
 *           String[] resultSetMappings() default{};
 *           QueryHint[] hints() default {};
 *         }
 * 
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedStoredProcedureQuery#getDescription <em>Description</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedStoredProcedureQuery#getParameter <em>Parameter</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedStoredProcedureQuery#getResultClass <em>Result Class</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedStoredProcedureQuery#getResultSetMapping <em>Result Set Mapping</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedStoredProcedureQuery#getHint <em>Hint</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedStoredProcedureQuery#getName <em>Name</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedStoredProcedureQuery#getProcedureName <em>Procedure Name</em>}</li>
 * </ul>
 *
 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getNamedStoredProcedureQuery()
 * @model extendedMetaData="name='named-stored-procedure-query' kind='elementOnly'"
 * @generated
 */
@ProviderType
public interface NamedStoredProcedureQuery extends EObject {
	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getNamedStoredProcedureQuery_Description()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedStoredProcedureQuery#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Parameter</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.emf.persistence.jpa.orm.model.orm.StoredProcedureParameter}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter</em>' containment reference list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getNamedStoredProcedureQuery_Parameter()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='parameter' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<StoredProcedureParameter> getParameter();

	/**
	 * Returns the value of the '<em><b>Result Class</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Result Class</em>' attribute list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getNamedStoredProcedureQuery_ResultClass()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='result-class' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<String> getResultClass();

	/**
	 * Returns the value of the '<em><b>Result Set Mapping</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Result Set Mapping</em>' attribute list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getNamedStoredProcedureQuery_ResultSetMapping()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='result-set-mapping' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<String> getResultSetMapping();

	/**
	 * Returns the value of the '<em><b>Hint</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.emf.persistence.jpa.orm.model.orm.QueryHint}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hint</em>' containment reference list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getNamedStoredProcedureQuery_Hint()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='hint' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<QueryHint> getHint();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getNamedStoredProcedureQuery_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedStoredProcedureQuery#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Procedure Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Procedure Name</em>' attribute.
	 * @see #setProcedureName(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getNamedStoredProcedureQuery_ProcedureName()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='procedure-name'"
	 * @generated
	 */
	String getProcedureName();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedStoredProcedureQuery#getProcedureName <em>Procedure Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Procedure Name</em>' attribute.
	 * @see #getProcedureName()
	 * @generated
	 */
	void setProcedureName(String value);

} // NamedStoredProcedureQuery
