/**
 * Copyright (c) 2012 - 2022 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 * 	Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.model.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EMongo Cursor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.model.mongo.EMongoCursor#getCursor <em>Cursor</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.model.mongo.EMongoCursor#getCollection <em>Collection</em>}</li>
 * </ul>
 *
 * @see org.gecko.emf.persistence.model.mongo.MongoPackage#getEMongoCursor()
 * @model kind="class"
 * @generated
 */
public class EMongoCursor extends MinimalEObjectImpl.Container implements EObject {
	/**
	 * The cached value of the '{@link #getCursor() <em>Cursor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCursor()
	 * @generated
	 * @ordered
	 */
	protected MongoCursor<EObject> cursor;

	/**
	 * The cached value of the '{@link #getCollection() <em>Collection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCollection()
	 * @generated
	 * @ordered
	 */
	protected MongoCollection<EObject> collection;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EMongoCursor() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MongoPackage.Literals.EMONGO_CURSOR;
	}

	/**
	 * Returns the value of the '<em><b>Cursor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cursor</em>' attribute.
	 * @see #setCursor(MongoCursor)
	 * @see org.gecko.emf.persistence.model.mongo.MongoPackage#getEMongoCursor_Cursor()
	 * @model dataType="org.gecko.emf.persistence.model.mongo.ECursor"
	 * @generated
	 */
	public MongoCursor<EObject> getCursor() {
		return cursor;
	}

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.model.mongo.EMongoCursor#getCursor <em>Cursor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param newCursor the new value of the '<em>Cursor</em>' attribute.
	 * @see #getCursor()
	 * @generated
	 */
	public void setCursor(MongoCursor<EObject> newCursor) {
		MongoCursor<EObject> oldCursor = cursor;
		cursor = newCursor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MongoPackage.EMONGO_CURSOR__CURSOR, oldCursor, cursor));
	}

	/**
	 * Returns the value of the '<em><b>Collection</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Collection</em>' attribute.
	 * @see #setCollection(MongoCollection)
	 * @see org.gecko.emf.persistence.model.mongo.MongoPackage#getEMongoCursor_Collection()
	 * @model dataType="org.gecko.emf.persistence.model.mongo.EMongoCollection"
	 * @generated
	 */
	public MongoCollection<EObject> getCollection() {
		return collection;
	}

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.model.mongo.EMongoCursor#getCollection <em>Collection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param newCollection the new value of the '<em>Collection</em>' attribute.
	 * @see #getCollection()
	 * @generated
	 */
	public void setCollection(MongoCollection<EObject> newCollection) {
		MongoCollection<EObject> oldCollection = collection;
		collection = newCollection;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MongoPackage.EMONGO_CURSOR__COLLECTION, oldCollection, collection));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MongoPackage.EMONGO_CURSOR__CURSOR:
				return getCursor();
			case MongoPackage.EMONGO_CURSOR__COLLECTION:
				return getCollection();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MongoPackage.EMONGO_CURSOR__CURSOR:
				setCursor((MongoCursor<EObject>)newValue);
				return;
			case MongoPackage.EMONGO_CURSOR__COLLECTION:
				setCollection((MongoCollection<EObject>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case MongoPackage.EMONGO_CURSOR__CURSOR:
				setCursor((MongoCursor<EObject>)null);
				return;
			case MongoPackage.EMONGO_CURSOR__COLLECTION:
				setCollection((MongoCollection<EObject>)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case MongoPackage.EMONGO_CURSOR__CURSOR:
				return cursor != null;
			case MongoPackage.EMONGO_CURSOR__COLLECTION:
				return collection != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (cursor: ");
		result.append(cursor);
		result.append(", collection: ");
		result.append(collection);
		result.append(')');
		return result.toString();
	}

} // EMongoCursor
