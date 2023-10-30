/*
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Convert</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 
 *         @Target({TYPE, METHOD, FIELD}) @Retention(RUNTIME)
 *         public @interface Convert {
 *           Class converter() default void.class;
 *           String attributeName() default "";
 *           boolean disableConversion() default false;
 *         }
 * 
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.Convert#getDescription <em>Description</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.Convert#getAttributeName <em>Attribute Name</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.Convert#getConverter <em>Converter</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.Convert#isDisableConversion <em>Disable Conversion</em>}</li>
 * </ul>
 *
 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getConvert()
 * @model extendedMetaData="name='convert' kind='elementOnly'"
 * @generated
 */
@ProviderType
public interface Convert extends EObject {
	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getConvert_Description()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Convert#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Attribute Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute Name</em>' attribute.
	 * @see #setAttributeName(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getConvert_AttributeName()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='attribute-name'"
	 * @generated
	 */
	String getAttributeName();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Convert#getAttributeName <em>Attribute Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attribute Name</em>' attribute.
	 * @see #getAttributeName()
	 * @generated
	 */
	void setAttributeName(String value);

	/**
	 * Returns the value of the '<em><b>Converter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Converter</em>' attribute.
	 * @see #setConverter(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getConvert_Converter()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='converter'"
	 * @generated
	 */
	String getConverter();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Convert#getConverter <em>Converter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Converter</em>' attribute.
	 * @see #getConverter()
	 * @generated
	 */
	void setConverter(String value);

	/**
	 * Returns the value of the '<em><b>Disable Conversion</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Disable Conversion</em>' attribute.
	 * @see #isSetDisableConversion()
	 * @see #unsetDisableConversion()
	 * @see #setDisableConversion(boolean)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getConvert_DisableConversion()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='disable-conversion'"
	 * @generated
	 */
	boolean isDisableConversion();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Convert#isDisableConversion <em>Disable Conversion</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Disable Conversion</em>' attribute.
	 * @see #isSetDisableConversion()
	 * @see #unsetDisableConversion()
	 * @see #isDisableConversion()
	 * @generated
	 */
	void setDisableConversion(boolean value);

	/**
	 * Unsets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Convert#isDisableConversion <em>Disable Conversion</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDisableConversion()
	 * @see #isDisableConversion()
	 * @see #setDisableConversion(boolean)
	 * @generated
	 */
	void unsetDisableConversion();

	/**
	 * Returns whether the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Convert#isDisableConversion <em>Disable Conversion</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Disable Conversion</em>' attribute is set.
	 * @see #unsetDisableConversion()
	 * @see #isDisableConversion()
	 * @see #setDisableConversion(boolean)
	 * @generated
	 */
	boolean isSetDisableConversion();

} // Convert
