/**
 */
package org.gecko.emf.mongo.model;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EMongo Cursor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.mongo.model.EMongoCursor#getCursor <em>Cursor</em>}</li>
 *   <li>{@link org.gecko.emf.mongo.model.EMongoCursor#getCollection <em>Collection</em>}</li>
 * </ul>
 *
 * @see org.gecko.emf.mongo.model.ModelPackage#getEMongoCursor()
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
		return ModelPackage.Literals.EMONGO_CURSOR;
	}

	/**
	 * Returns the value of the '<em><b>Cursor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cursor</em>' attribute.
	 * @see #setCursor(MongoCursor)
	 * @see org.gecko.emf.mongo.model.ModelPackage#getEMongoCursor_Cursor()
	 * @model unique="false" dataType="org.gecko.emf.mongo.model.ECursor"
	 * @generated
	 */
	public MongoCursor<EObject> getCursor() {
		return cursor;
	}

	/**
	 * Sets the value of the '{@link org.gecko.emf.mongo.model.EMongoCursor#getCursor <em>Cursor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cursor</em>' attribute.
	 * @see #getCursor()
	 * @generated
	 */
	public void setCursor(MongoCursor<EObject> newCursor) {
		MongoCursor<EObject> oldCursor = cursor;
		cursor = newCursor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.EMONGO_CURSOR__CURSOR, oldCursor, cursor));
	}

	/**
	 * Returns the value of the '<em><b>Collection</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Collection</em>' attribute.
	 * @see #setCollection(MongoCollection)
	 * @see org.gecko.emf.mongo.model.ModelPackage#getEMongoCursor_Collection()
	 * @model unique="false" dataType="org.gecko.emf.mongo.model.EMongoCollection"
	 * @generated
	 */
	public MongoCollection<EObject> getCollection() {
		return collection;
	}

	/**
	 * Sets the value of the '{@link org.gecko.emf.mongo.model.EMongoCursor#getCollection <em>Collection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Collection</em>' attribute.
	 * @see #getCollection()
	 * @generated
	 */
	public void setCollection(MongoCollection<EObject> newCollection) {
		MongoCollection<EObject> oldCollection = collection;
		collection = newCollection;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.EMONGO_CURSOR__COLLECTION, oldCollection, collection));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.EMONGO_CURSOR__CURSOR:
				return getCursor();
			case ModelPackage.EMONGO_CURSOR__COLLECTION:
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
			case ModelPackage.EMONGO_CURSOR__CURSOR:
				setCursor((MongoCursor<EObject>)newValue);
				return;
			case ModelPackage.EMONGO_CURSOR__COLLECTION:
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
			case ModelPackage.EMONGO_CURSOR__CURSOR:
				setCursor((MongoCursor<EObject>)null);
				return;
			case ModelPackage.EMONGO_CURSOR__COLLECTION:
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
			case ModelPackage.EMONGO_CURSOR__CURSOR:
				return cursor != null;
			case ModelPackage.EMONGO_CURSOR__COLLECTION:
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
