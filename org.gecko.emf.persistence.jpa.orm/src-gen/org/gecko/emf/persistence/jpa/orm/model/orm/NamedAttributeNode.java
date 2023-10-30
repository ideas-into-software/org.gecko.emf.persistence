/*
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Named Attribute Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 
 *         @Target({}) @Retention(RUNTIME)
 *         public @interface NamedAttributeNode {
 *           String value();
 *           String subgraph() default "";
 *           String keySubgraph() default "";
 *         }
 *  
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedAttributeNode#getKeySubgraph <em>Key Subgraph</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedAttributeNode#getName <em>Name</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedAttributeNode#getSubgraph <em>Subgraph</em>}</li>
 * </ul>
 *
 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getNamedAttributeNode()
 * @model extendedMetaData="name='named-attribute-node' kind='empty'"
 * @generated
 */
@ProviderType
public interface NamedAttributeNode extends EObject {
	/**
	 * Returns the value of the '<em><b>Key Subgraph</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key Subgraph</em>' attribute.
	 * @see #setKeySubgraph(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getNamedAttributeNode_KeySubgraph()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='key-subgraph'"
	 * @generated
	 */
	String getKeySubgraph();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedAttributeNode#getKeySubgraph <em>Key Subgraph</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key Subgraph</em>' attribute.
	 * @see #getKeySubgraph()
	 * @generated
	 */
	void setKeySubgraph(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getNamedAttributeNode_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedAttributeNode#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Subgraph</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Subgraph</em>' attribute.
	 * @see #setSubgraph(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getNamedAttributeNode_Subgraph()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='subgraph'"
	 * @generated
	 */
	String getSubgraph();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedAttributeNode#getSubgraph <em>Subgraph</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Subgraph</em>' attribute.
	 * @see #getSubgraph()
	 * @generated
	 */
	void setSubgraph(String value);

} // NamedAttributeNode
