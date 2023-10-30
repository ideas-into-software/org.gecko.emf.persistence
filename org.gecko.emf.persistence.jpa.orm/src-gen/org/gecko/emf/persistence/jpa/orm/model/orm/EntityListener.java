/*
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entity Listener</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 
 *         Defines an entity listener to be invoked at lifecycle events
 *         for the entities that list this listener.
 * 
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getDescription <em>Description</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getPrePersist <em>Pre Persist</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getPostPersist <em>Post Persist</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getPreRemove <em>Pre Remove</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getPostRemove <em>Post Remove</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getPreUpdate <em>Pre Update</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getPostUpdate <em>Post Update</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getPostLoad <em>Post Load</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getClass_ <em>Class</em>}</li>
 * </ul>
 *
 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getEntityListener()
 * @model extendedMetaData="name='entity-listener' kind='elementOnly'"
 * @generated
 */
@ProviderType
public interface EntityListener extends EObject {
	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getEntityListener_Description()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Pre Persist</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pre Persist</em>' containment reference.
	 * @see #setPrePersist(PrePersist)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getEntityListener_PrePersist()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='pre-persist' namespace='##targetNamespace'"
	 * @generated
	 */
	PrePersist getPrePersist();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getPrePersist <em>Pre Persist</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pre Persist</em>' containment reference.
	 * @see #getPrePersist()
	 * @generated
	 */
	void setPrePersist(PrePersist value);

	/**
	 * Returns the value of the '<em><b>Post Persist</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Post Persist</em>' containment reference.
	 * @see #setPostPersist(PostPersist)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getEntityListener_PostPersist()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='post-persist' namespace='##targetNamespace'"
	 * @generated
	 */
	PostPersist getPostPersist();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getPostPersist <em>Post Persist</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Post Persist</em>' containment reference.
	 * @see #getPostPersist()
	 * @generated
	 */
	void setPostPersist(PostPersist value);

	/**
	 * Returns the value of the '<em><b>Pre Remove</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pre Remove</em>' containment reference.
	 * @see #setPreRemove(PreRemove)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getEntityListener_PreRemove()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='pre-remove' namespace='##targetNamespace'"
	 * @generated
	 */
	PreRemove getPreRemove();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getPreRemove <em>Pre Remove</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pre Remove</em>' containment reference.
	 * @see #getPreRemove()
	 * @generated
	 */
	void setPreRemove(PreRemove value);

	/**
	 * Returns the value of the '<em><b>Post Remove</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Post Remove</em>' containment reference.
	 * @see #setPostRemove(PostRemove)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getEntityListener_PostRemove()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='post-remove' namespace='##targetNamespace'"
	 * @generated
	 */
	PostRemove getPostRemove();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getPostRemove <em>Post Remove</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Post Remove</em>' containment reference.
	 * @see #getPostRemove()
	 * @generated
	 */
	void setPostRemove(PostRemove value);

	/**
	 * Returns the value of the '<em><b>Pre Update</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pre Update</em>' containment reference.
	 * @see #setPreUpdate(PreUpdate)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getEntityListener_PreUpdate()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='pre-update' namespace='##targetNamespace'"
	 * @generated
	 */
	PreUpdate getPreUpdate();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getPreUpdate <em>Pre Update</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pre Update</em>' containment reference.
	 * @see #getPreUpdate()
	 * @generated
	 */
	void setPreUpdate(PreUpdate value);

	/**
	 * Returns the value of the '<em><b>Post Update</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Post Update</em>' containment reference.
	 * @see #setPostUpdate(PostUpdate)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getEntityListener_PostUpdate()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='post-update' namespace='##targetNamespace'"
	 * @generated
	 */
	PostUpdate getPostUpdate();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getPostUpdate <em>Post Update</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Post Update</em>' containment reference.
	 * @see #getPostUpdate()
	 * @generated
	 */
	void setPostUpdate(PostUpdate value);

	/**
	 * Returns the value of the '<em><b>Post Load</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Post Load</em>' containment reference.
	 * @see #setPostLoad(PostLoad)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getEntityListener_PostLoad()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='post-load' namespace='##targetNamespace'"
	 * @generated
	 */
	PostLoad getPostLoad();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getPostLoad <em>Post Load</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Post Load</em>' containment reference.
	 * @see #getPostLoad()
	 * @generated
	 */
	void setPostLoad(PostLoad value);

	/**
	 * Returns the value of the '<em><b>Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Class</em>' attribute.
	 * @see #setClass(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getEntityListener_Class()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='class'"
	 * @generated
	 */
	String getClass_();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener#getClass_ <em>Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Class</em>' attribute.
	 * @see #getClass_()
	 * @generated
	 */
	void setClass(String value);

} // EntityListener
