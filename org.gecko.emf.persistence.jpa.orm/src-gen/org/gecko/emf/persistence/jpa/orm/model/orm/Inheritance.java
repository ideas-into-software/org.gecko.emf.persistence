/*
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inheritance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 
 *         @Target({TYPE}) @Retention(RUNTIME)
 *         public @interface Inheritance {
 *           InheritanceType strategy() default SINGLE_TABLE;
 *         }
 * 
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.Inheritance#getStrategy <em>Strategy</em>}</li>
 * </ul>
 *
 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getInheritance()
 * @model extendedMetaData="name='inheritance' kind='empty'"
 * @generated
 */
@ProviderType
public interface Inheritance extends EObject {
	/**
	 * Returns the value of the '<em><b>Strategy</b></em>' attribute.
	 * The literals are from the enumeration {@link org.gecko.emf.persistence.jpa.orm.model.orm.InheritanceType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Strategy</em>' attribute.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.InheritanceType
	 * @see #isSetStrategy()
	 * @see #unsetStrategy()
	 * @see #setStrategy(InheritanceType)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getInheritance_Strategy()
	 * @model unsettable="true"
	 *        extendedMetaData="kind='attribute' name='strategy'"
	 * @generated
	 */
	InheritanceType getStrategy();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Inheritance#getStrategy <em>Strategy</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Strategy</em>' attribute.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.InheritanceType
	 * @see #isSetStrategy()
	 * @see #unsetStrategy()
	 * @see #getStrategy()
	 * @generated
	 */
	void setStrategy(InheritanceType value);

	/**
	 * Unsets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Inheritance#getStrategy <em>Strategy</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStrategy()
	 * @see #getStrategy()
	 * @see #setStrategy(InheritanceType)
	 * @generated
	 */
	void unsetStrategy();

	/**
	 * Returns whether the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Inheritance#getStrategy <em>Strategy</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Strategy</em>' attribute is set.
	 * @see #unsetStrategy()
	 * @see #getStrategy()
	 * @see #setStrategy(InheritanceType)
	 * @generated
	 */
	boolean isSetStrategy();

} // Inheritance
