/*
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Converter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 
 *         @Target({TYPE, METHOD, FIELD}) @Retention(RUNTIME)
 *         public @interface Converter {
 *           boolean autoApply() default false;
 *         }
 * 
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.Converter#getDescription <em>Description</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.Converter#isAutoApply <em>Auto Apply</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.Converter#getClass_ <em>Class</em>}</li>
 * </ul>
 *
 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getConverter()
 * @model extendedMetaData="name='converter' kind='elementOnly'"
 * @generated
 */
@ProviderType
public interface Converter extends EObject {
	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getConverter_Description()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Converter#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Auto Apply</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Auto Apply</em>' attribute.
	 * @see #isSetAutoApply()
	 * @see #unsetAutoApply()
	 * @see #setAutoApply(boolean)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getConverter_AutoApply()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='auto-apply'"
	 * @generated
	 */
	boolean isAutoApply();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Converter#isAutoApply <em>Auto Apply</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Auto Apply</em>' attribute.
	 * @see #isSetAutoApply()
	 * @see #unsetAutoApply()
	 * @see #isAutoApply()
	 * @generated
	 */
	void setAutoApply(boolean value);

	/**
	 * Unsets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Converter#isAutoApply <em>Auto Apply</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAutoApply()
	 * @see #isAutoApply()
	 * @see #setAutoApply(boolean)
	 * @generated
	 */
	void unsetAutoApply();

	/**
	 * Returns whether the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Converter#isAutoApply <em>Auto Apply</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Auto Apply</em>' attribute is set.
	 * @see #unsetAutoApply()
	 * @see #isAutoApply()
	 * @see #setAutoApply(boolean)
	 * @generated
	 */
	boolean isSetAutoApply();

	/**
	 * Returns the value of the '<em><b>Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Class</em>' attribute.
	 * @see #setClass(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getConverter_Class()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='class'"
	 * @generated
	 */
	String getClass_();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Converter#getClass_ <em>Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Class</em>' attribute.
	 * @see #getClass_()
	 * @generated
	 */
	void setClass(String value);

} // Converter
