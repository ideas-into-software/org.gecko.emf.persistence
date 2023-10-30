/*
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.gecko.emf.persistence.jpa.orm.model.orm.ForeignKey;
import org.gecko.emf.persistence.jpa.orm.model.orm.Index;
import org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage;
import org.gecko.emf.persistence.jpa.orm.model.orm.PrimaryKeyJoinColumn;
import org.gecko.emf.persistence.jpa.orm.model.orm.SecondaryTable;
import org.gecko.emf.persistence.jpa.orm.model.orm.UniqueConstraint;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Secondary Table</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.SecondaryTableImpl#getPrimaryKeyJoinColumn <em>Primary Key Join Column</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.SecondaryTableImpl#getPrimaryKeyForeignKey <em>Primary Key Foreign Key</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.SecondaryTableImpl#getUniqueConstraint <em>Unique Constraint</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.SecondaryTableImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.SecondaryTableImpl#getCatalog <em>Catalog</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.SecondaryTableImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.SecondaryTableImpl#getSchema <em>Schema</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SecondaryTableImpl extends MinimalEObjectImpl.Container implements SecondaryTable {
	/**
	 * The cached value of the '{@link #getPrimaryKeyJoinColumn() <em>Primary Key Join Column</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrimaryKeyJoinColumn()
	 * @generated
	 * @ordered
	 */
	protected EList<PrimaryKeyJoinColumn> primaryKeyJoinColumn;

	/**
	 * The cached value of the '{@link #getPrimaryKeyForeignKey() <em>Primary Key Foreign Key</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrimaryKeyForeignKey()
	 * @generated
	 * @ordered
	 */
	protected ForeignKey primaryKeyForeignKey;

	/**
	 * The cached value of the '{@link #getUniqueConstraint() <em>Unique Constraint</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUniqueConstraint()
	 * @generated
	 * @ordered
	 */
	protected EList<UniqueConstraint> uniqueConstraint;

	/**
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected EList<Index> index;

	/**
	 * The default value of the '{@link #getCatalog() <em>Catalog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCatalog()
	 * @generated
	 * @ordered
	 */
	protected static final String CATALOG_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCatalog() <em>Catalog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCatalog()
	 * @generated
	 * @ordered
	 */
	protected String catalog = CATALOG_EDEFAULT;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getSchema() <em>Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSchema()
	 * @generated
	 * @ordered
	 */
	protected static final String SCHEMA_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSchema() <em>Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSchema()
	 * @generated
	 * @ordered
	 */
	protected String schema = SCHEMA_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SecondaryTableImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OrmPackage.eINSTANCE.getSecondaryTable();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PrimaryKeyJoinColumn> getPrimaryKeyJoinColumn() {
		if (primaryKeyJoinColumn == null) {
			primaryKeyJoinColumn = new EObjectContainmentEList<PrimaryKeyJoinColumn>(PrimaryKeyJoinColumn.class, this, OrmPackage.SECONDARY_TABLE__PRIMARY_KEY_JOIN_COLUMN);
		}
		return primaryKeyJoinColumn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ForeignKey getPrimaryKeyForeignKey() {
		return primaryKeyForeignKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPrimaryKeyForeignKey(ForeignKey newPrimaryKeyForeignKey, NotificationChain msgs) {
		ForeignKey oldPrimaryKeyForeignKey = primaryKeyForeignKey;
		primaryKeyForeignKey = newPrimaryKeyForeignKey;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.SECONDARY_TABLE__PRIMARY_KEY_FOREIGN_KEY, oldPrimaryKeyForeignKey, newPrimaryKeyForeignKey);
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
	public void setPrimaryKeyForeignKey(ForeignKey newPrimaryKeyForeignKey) {
		if (newPrimaryKeyForeignKey != primaryKeyForeignKey) {
			NotificationChain msgs = null;
			if (primaryKeyForeignKey != null)
				msgs = ((InternalEObject)primaryKeyForeignKey).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.SECONDARY_TABLE__PRIMARY_KEY_FOREIGN_KEY, null, msgs);
			if (newPrimaryKeyForeignKey != null)
				msgs = ((InternalEObject)newPrimaryKeyForeignKey).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.SECONDARY_TABLE__PRIMARY_KEY_FOREIGN_KEY, null, msgs);
			msgs = basicSetPrimaryKeyForeignKey(newPrimaryKeyForeignKey, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.SECONDARY_TABLE__PRIMARY_KEY_FOREIGN_KEY, newPrimaryKeyForeignKey, newPrimaryKeyForeignKey));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<UniqueConstraint> getUniqueConstraint() {
		if (uniqueConstraint == null) {
			uniqueConstraint = new EObjectContainmentEList<UniqueConstraint>(UniqueConstraint.class, this, OrmPackage.SECONDARY_TABLE__UNIQUE_CONSTRAINT);
		}
		return uniqueConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Index> getIndex() {
		if (index == null) {
			index = new EObjectContainmentEList<Index>(Index.class, this, OrmPackage.SECONDARY_TABLE__INDEX);
		}
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCatalog() {
		return catalog;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCatalog(String newCatalog) {
		String oldCatalog = catalog;
		catalog = newCatalog;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.SECONDARY_TABLE__CATALOG, oldCatalog, catalog));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.SECONDARY_TABLE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getSchema() {
		return schema;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSchema(String newSchema) {
		String oldSchema = schema;
		schema = newSchema;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.SECONDARY_TABLE__SCHEMA, oldSchema, schema));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case OrmPackage.SECONDARY_TABLE__PRIMARY_KEY_JOIN_COLUMN:
				return ((InternalEList<?>)getPrimaryKeyJoinColumn()).basicRemove(otherEnd, msgs);
			case OrmPackage.SECONDARY_TABLE__PRIMARY_KEY_FOREIGN_KEY:
				return basicSetPrimaryKeyForeignKey(null, msgs);
			case OrmPackage.SECONDARY_TABLE__UNIQUE_CONSTRAINT:
				return ((InternalEList<?>)getUniqueConstraint()).basicRemove(otherEnd, msgs);
			case OrmPackage.SECONDARY_TABLE__INDEX:
				return ((InternalEList<?>)getIndex()).basicRemove(otherEnd, msgs);
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
			case OrmPackage.SECONDARY_TABLE__PRIMARY_KEY_JOIN_COLUMN:
				return getPrimaryKeyJoinColumn();
			case OrmPackage.SECONDARY_TABLE__PRIMARY_KEY_FOREIGN_KEY:
				return getPrimaryKeyForeignKey();
			case OrmPackage.SECONDARY_TABLE__UNIQUE_CONSTRAINT:
				return getUniqueConstraint();
			case OrmPackage.SECONDARY_TABLE__INDEX:
				return getIndex();
			case OrmPackage.SECONDARY_TABLE__CATALOG:
				return getCatalog();
			case OrmPackage.SECONDARY_TABLE__NAME:
				return getName();
			case OrmPackage.SECONDARY_TABLE__SCHEMA:
				return getSchema();
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
			case OrmPackage.SECONDARY_TABLE__PRIMARY_KEY_JOIN_COLUMN:
				getPrimaryKeyJoinColumn().clear();
				getPrimaryKeyJoinColumn().addAll((Collection<? extends PrimaryKeyJoinColumn>)newValue);
				return;
			case OrmPackage.SECONDARY_TABLE__PRIMARY_KEY_FOREIGN_KEY:
				setPrimaryKeyForeignKey((ForeignKey)newValue);
				return;
			case OrmPackage.SECONDARY_TABLE__UNIQUE_CONSTRAINT:
				getUniqueConstraint().clear();
				getUniqueConstraint().addAll((Collection<? extends UniqueConstraint>)newValue);
				return;
			case OrmPackage.SECONDARY_TABLE__INDEX:
				getIndex().clear();
				getIndex().addAll((Collection<? extends Index>)newValue);
				return;
			case OrmPackage.SECONDARY_TABLE__CATALOG:
				setCatalog((String)newValue);
				return;
			case OrmPackage.SECONDARY_TABLE__NAME:
				setName((String)newValue);
				return;
			case OrmPackage.SECONDARY_TABLE__SCHEMA:
				setSchema((String)newValue);
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
			case OrmPackage.SECONDARY_TABLE__PRIMARY_KEY_JOIN_COLUMN:
				getPrimaryKeyJoinColumn().clear();
				return;
			case OrmPackage.SECONDARY_TABLE__PRIMARY_KEY_FOREIGN_KEY:
				setPrimaryKeyForeignKey((ForeignKey)null);
				return;
			case OrmPackage.SECONDARY_TABLE__UNIQUE_CONSTRAINT:
				getUniqueConstraint().clear();
				return;
			case OrmPackage.SECONDARY_TABLE__INDEX:
				getIndex().clear();
				return;
			case OrmPackage.SECONDARY_TABLE__CATALOG:
				setCatalog(CATALOG_EDEFAULT);
				return;
			case OrmPackage.SECONDARY_TABLE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case OrmPackage.SECONDARY_TABLE__SCHEMA:
				setSchema(SCHEMA_EDEFAULT);
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
			case OrmPackage.SECONDARY_TABLE__PRIMARY_KEY_JOIN_COLUMN:
				return primaryKeyJoinColumn != null && !primaryKeyJoinColumn.isEmpty();
			case OrmPackage.SECONDARY_TABLE__PRIMARY_KEY_FOREIGN_KEY:
				return primaryKeyForeignKey != null;
			case OrmPackage.SECONDARY_TABLE__UNIQUE_CONSTRAINT:
				return uniqueConstraint != null && !uniqueConstraint.isEmpty();
			case OrmPackage.SECONDARY_TABLE__INDEX:
				return index != null && !index.isEmpty();
			case OrmPackage.SECONDARY_TABLE__CATALOG:
				return CATALOG_EDEFAULT == null ? catalog != null : !CATALOG_EDEFAULT.equals(catalog);
			case OrmPackage.SECONDARY_TABLE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case OrmPackage.SECONDARY_TABLE__SCHEMA:
				return SCHEMA_EDEFAULT == null ? schema != null : !SCHEMA_EDEFAULT.equals(schema);
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
		result.append(" (catalog: ");
		result.append(catalog);
		result.append(", name: ");
		result.append(name);
		result.append(", schema: ");
		result.append(schema);
		result.append(')');
		return result.toString();
	}

} //SecondaryTableImpl
