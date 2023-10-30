/*
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.gecko.emf.persistence.jpa.orm.model.orm.EmptyType;
import org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage;
import org.gecko.emf.persistence.jpa.orm.model.orm.PersistenceUnitDefaults;
import org.gecko.emf.persistence.jpa.orm.model.orm.PersistenceUnitMetadata;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Persistence Unit Metadata</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.PersistenceUnitMetadataImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.PersistenceUnitMetadataImpl#getXmlMappingMetadataComplete <em>Xml Mapping Metadata Complete</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.PersistenceUnitMetadataImpl#getPersistenceUnitDefaults <em>Persistence Unit Defaults</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PersistenceUnitMetadataImpl extends MinimalEObjectImpl.Container implements PersistenceUnitMetadata {
	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getXmlMappingMetadataComplete() <em>Xml Mapping Metadata Complete</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getXmlMappingMetadataComplete()
	 * @generated
	 * @ordered
	 */
	protected EmptyType xmlMappingMetadataComplete;

	/**
	 * The cached value of the '{@link #getPersistenceUnitDefaults() <em>Persistence Unit Defaults</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPersistenceUnitDefaults()
	 * @generated
	 * @ordered
	 */
	protected PersistenceUnitDefaults persistenceUnitDefaults;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PersistenceUnitMetadataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OrmPackage.eINSTANCE.getPersistenceUnitMetadata();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.PERSISTENCE_UNIT_METADATA__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EmptyType getXmlMappingMetadataComplete() {
		return xmlMappingMetadataComplete;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetXmlMappingMetadataComplete(EmptyType newXmlMappingMetadataComplete, NotificationChain msgs) {
		EmptyType oldXmlMappingMetadataComplete = xmlMappingMetadataComplete;
		xmlMappingMetadataComplete = newXmlMappingMetadataComplete;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.PERSISTENCE_UNIT_METADATA__XML_MAPPING_METADATA_COMPLETE, oldXmlMappingMetadataComplete, newXmlMappingMetadataComplete);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setXmlMappingMetadataComplete(EmptyType newXmlMappingMetadataComplete) {
		if (newXmlMappingMetadataComplete != xmlMappingMetadataComplete) {
			NotificationChain msgs = null;
			if (xmlMappingMetadataComplete != null)
				msgs = ((InternalEObject)xmlMappingMetadataComplete).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.PERSISTENCE_UNIT_METADATA__XML_MAPPING_METADATA_COMPLETE, null, msgs);
			if (newXmlMappingMetadataComplete != null)
				msgs = ((InternalEObject)newXmlMappingMetadataComplete).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.PERSISTENCE_UNIT_METADATA__XML_MAPPING_METADATA_COMPLETE, null, msgs);
			msgs = basicSetXmlMappingMetadataComplete(newXmlMappingMetadataComplete, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.PERSISTENCE_UNIT_METADATA__XML_MAPPING_METADATA_COMPLETE, newXmlMappingMetadataComplete, newXmlMappingMetadataComplete));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PersistenceUnitDefaults getPersistenceUnitDefaults() {
		return persistenceUnitDefaults;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPersistenceUnitDefaults(PersistenceUnitDefaults newPersistenceUnitDefaults, NotificationChain msgs) {
		PersistenceUnitDefaults oldPersistenceUnitDefaults = persistenceUnitDefaults;
		persistenceUnitDefaults = newPersistenceUnitDefaults;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.PERSISTENCE_UNIT_METADATA__PERSISTENCE_UNIT_DEFAULTS, oldPersistenceUnitDefaults, newPersistenceUnitDefaults);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPersistenceUnitDefaults(PersistenceUnitDefaults newPersistenceUnitDefaults) {
		if (newPersistenceUnitDefaults != persistenceUnitDefaults) {
			NotificationChain msgs = null;
			if (persistenceUnitDefaults != null)
				msgs = ((InternalEObject)persistenceUnitDefaults).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.PERSISTENCE_UNIT_METADATA__PERSISTENCE_UNIT_DEFAULTS, null, msgs);
			if (newPersistenceUnitDefaults != null)
				msgs = ((InternalEObject)newPersistenceUnitDefaults).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.PERSISTENCE_UNIT_METADATA__PERSISTENCE_UNIT_DEFAULTS, null, msgs);
			msgs = basicSetPersistenceUnitDefaults(newPersistenceUnitDefaults, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.PERSISTENCE_UNIT_METADATA__PERSISTENCE_UNIT_DEFAULTS, newPersistenceUnitDefaults, newPersistenceUnitDefaults));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case OrmPackage.PERSISTENCE_UNIT_METADATA__XML_MAPPING_METADATA_COMPLETE:
				return basicSetXmlMappingMetadataComplete(null, msgs);
			case OrmPackage.PERSISTENCE_UNIT_METADATA__PERSISTENCE_UNIT_DEFAULTS:
				return basicSetPersistenceUnitDefaults(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case OrmPackage.PERSISTENCE_UNIT_METADATA__DESCRIPTION:
				return getDescription();
			case OrmPackage.PERSISTENCE_UNIT_METADATA__XML_MAPPING_METADATA_COMPLETE:
				return getXmlMappingMetadataComplete();
			case OrmPackage.PERSISTENCE_UNIT_METADATA__PERSISTENCE_UNIT_DEFAULTS:
				return getPersistenceUnitDefaults();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case OrmPackage.PERSISTENCE_UNIT_METADATA__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case OrmPackage.PERSISTENCE_UNIT_METADATA__XML_MAPPING_METADATA_COMPLETE:
				setXmlMappingMetadataComplete((EmptyType)newValue);
				return;
			case OrmPackage.PERSISTENCE_UNIT_METADATA__PERSISTENCE_UNIT_DEFAULTS:
				setPersistenceUnitDefaults((PersistenceUnitDefaults)newValue);
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
			case OrmPackage.PERSISTENCE_UNIT_METADATA__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case OrmPackage.PERSISTENCE_UNIT_METADATA__XML_MAPPING_METADATA_COMPLETE:
				setXmlMappingMetadataComplete((EmptyType)null);
				return;
			case OrmPackage.PERSISTENCE_UNIT_METADATA__PERSISTENCE_UNIT_DEFAULTS:
				setPersistenceUnitDefaults((PersistenceUnitDefaults)null);
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
			case OrmPackage.PERSISTENCE_UNIT_METADATA__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case OrmPackage.PERSISTENCE_UNIT_METADATA__XML_MAPPING_METADATA_COMPLETE:
				return xmlMappingMetadataComplete != null;
			case OrmPackage.PERSISTENCE_UNIT_METADATA__PERSISTENCE_UNIT_DEFAULTS:
				return persistenceUnitDefaults != null;
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
		result.append(" (description: ");
		result.append(description);
		result.append(')');
		return result.toString();
	}

} //PersistenceUnitMetadataImpl
