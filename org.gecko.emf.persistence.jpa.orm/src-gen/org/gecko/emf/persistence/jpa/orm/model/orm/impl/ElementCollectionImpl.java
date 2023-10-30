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

import org.gecko.emf.persistence.jpa.orm.model.orm.AccessType;
import org.gecko.emf.persistence.jpa.orm.model.orm.AssociationOverride;
import org.gecko.emf.persistence.jpa.orm.model.orm.AttributeOverride;
import org.gecko.emf.persistence.jpa.orm.model.orm.CollectionTable;
import org.gecko.emf.persistence.jpa.orm.model.orm.Column;
import org.gecko.emf.persistence.jpa.orm.model.orm.Convert;
import org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection;
import org.gecko.emf.persistence.jpa.orm.model.orm.EnumType;
import org.gecko.emf.persistence.jpa.orm.model.orm.FetchType;
import org.gecko.emf.persistence.jpa.orm.model.orm.ForeignKey;
import org.gecko.emf.persistence.jpa.orm.model.orm.Lob;
import org.gecko.emf.persistence.jpa.orm.model.orm.MapKey;
import org.gecko.emf.persistence.jpa.orm.model.orm.MapKeyClass;
import org.gecko.emf.persistence.jpa.orm.model.orm.MapKeyColumn;
import org.gecko.emf.persistence.jpa.orm.model.orm.MapKeyJoinColumn;
import org.gecko.emf.persistence.jpa.orm.model.orm.OrderColumn;
import org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage;
import org.gecko.emf.persistence.jpa.orm.model.orm.TemporalType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Element Collection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getOrderBy <em>Order By</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getOrderColumn <em>Order Column</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getMapKey <em>Map Key</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getMapKeyClass <em>Map Key Class</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getMapKeyTemporal <em>Map Key Temporal</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getMapKeyEnumerated <em>Map Key Enumerated</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getMapKeyAttributeOverride <em>Map Key Attribute Override</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getMapKeyConvert <em>Map Key Convert</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getMapKeyColumn <em>Map Key Column</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getMapKeyJoinColumn <em>Map Key Join Column</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getMapKeyForeignKey <em>Map Key Foreign Key</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getColumn <em>Column</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getTemporal <em>Temporal</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getEnumerated <em>Enumerated</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getLob <em>Lob</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getAttributeOverride <em>Attribute Override</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getAssociationOverride <em>Association Override</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getConvert <em>Convert</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getCollectionTable <em>Collection Table</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getAccess <em>Access</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getFetch <em>Fetch</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.ElementCollectionImpl#getTargetClass <em>Target Class</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ElementCollectionImpl extends MinimalEObjectImpl.Container implements ElementCollection {
	/**
	 * The default value of the '{@link #getOrderBy() <em>Order By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOrderBy()
	 * @generated
	 * @ordered
	 */
	protected static final String ORDER_BY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOrderBy() <em>Order By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOrderBy()
	 * @generated
	 * @ordered
	 */
	protected String orderBy = ORDER_BY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getOrderColumn() <em>Order Column</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOrderColumn()
	 * @generated
	 * @ordered
	 */
	protected OrderColumn orderColumn;

	/**
	 * The cached value of the '{@link #getMapKey() <em>Map Key</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKey()
	 * @generated
	 * @ordered
	 */
	protected MapKey mapKey;

	/**
	 * The cached value of the '{@link #getMapKeyClass() <em>Map Key Class</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKeyClass()
	 * @generated
	 * @ordered
	 */
	protected MapKeyClass mapKeyClass;

	/**
	 * The default value of the '{@link #getMapKeyTemporal() <em>Map Key Temporal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKeyTemporal()
	 * @generated
	 * @ordered
	 */
	protected static final TemporalType MAP_KEY_TEMPORAL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMapKeyTemporal() <em>Map Key Temporal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKeyTemporal()
	 * @generated
	 * @ordered
	 */
	protected TemporalType mapKeyTemporal = MAP_KEY_TEMPORAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getMapKeyEnumerated() <em>Map Key Enumerated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKeyEnumerated()
	 * @generated
	 * @ordered
	 */
	protected static final EnumType MAP_KEY_ENUMERATED_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMapKeyEnumerated() <em>Map Key Enumerated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKeyEnumerated()
	 * @generated
	 * @ordered
	 */
	protected EnumType mapKeyEnumerated = MAP_KEY_ENUMERATED_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMapKeyAttributeOverride() <em>Map Key Attribute Override</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKeyAttributeOverride()
	 * @generated
	 * @ordered
	 */
	protected EList<AttributeOverride> mapKeyAttributeOverride;

	/**
	 * The cached value of the '{@link #getMapKeyConvert() <em>Map Key Convert</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKeyConvert()
	 * @generated
	 * @ordered
	 */
	protected EList<Convert> mapKeyConvert;

	/**
	 * The cached value of the '{@link #getMapKeyColumn() <em>Map Key Column</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKeyColumn()
	 * @generated
	 * @ordered
	 */
	protected MapKeyColumn mapKeyColumn;

	/**
	 * The cached value of the '{@link #getMapKeyJoinColumn() <em>Map Key Join Column</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKeyJoinColumn()
	 * @generated
	 * @ordered
	 */
	protected EList<MapKeyJoinColumn> mapKeyJoinColumn;

	/**
	 * The cached value of the '{@link #getMapKeyForeignKey() <em>Map Key Foreign Key</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKeyForeignKey()
	 * @generated
	 * @ordered
	 */
	protected ForeignKey mapKeyForeignKey;

	/**
	 * The cached value of the '{@link #getColumn() <em>Column</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumn()
	 * @generated
	 * @ordered
	 */
	protected Column column;

	/**
	 * The default value of the '{@link #getTemporal() <em>Temporal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemporal()
	 * @generated
	 * @ordered
	 */
	protected static final TemporalType TEMPORAL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTemporal() <em>Temporal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemporal()
	 * @generated
	 * @ordered
	 */
	protected TemporalType temporal = TEMPORAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getEnumerated() <em>Enumerated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnumerated()
	 * @generated
	 * @ordered
	 */
	protected static final EnumType ENUMERATED_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEnumerated() <em>Enumerated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnumerated()
	 * @generated
	 * @ordered
	 */
	protected EnumType enumerated = ENUMERATED_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLob() <em>Lob</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLob()
	 * @generated
	 * @ordered
	 */
	protected Lob lob;

	/**
	 * The cached value of the '{@link #getAttributeOverride() <em>Attribute Override</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributeOverride()
	 * @generated
	 * @ordered
	 */
	protected EList<AttributeOverride> attributeOverride;

	/**
	 * The cached value of the '{@link #getAssociationOverride() <em>Association Override</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssociationOverride()
	 * @generated
	 * @ordered
	 */
	protected EList<AssociationOverride> associationOverride;

	/**
	 * The cached value of the '{@link #getConvert() <em>Convert</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConvert()
	 * @generated
	 * @ordered
	 */
	protected EList<Convert> convert;

	/**
	 * The cached value of the '{@link #getCollectionTable() <em>Collection Table</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCollectionTable()
	 * @generated
	 * @ordered
	 */
	protected CollectionTable collectionTable;

	/**
	 * The default value of the '{@link #getAccess() <em>Access</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccess()
	 * @generated
	 * @ordered
	 */
	protected static final AccessType ACCESS_EDEFAULT = AccessType.PROPERTY;

	/**
	 * The cached value of the '{@link #getAccess() <em>Access</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccess()
	 * @generated
	 * @ordered
	 */
	protected AccessType access = ACCESS_EDEFAULT;

	/**
	 * This is true if the Access attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean accessESet;

	/**
	 * The default value of the '{@link #getFetch() <em>Fetch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFetch()
	 * @generated
	 * @ordered
	 */
	protected static final FetchType FETCH_EDEFAULT = FetchType.LAZY;

	/**
	 * The cached value of the '{@link #getFetch() <em>Fetch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFetch()
	 * @generated
	 * @ordered
	 */
	protected FetchType fetch = FETCH_EDEFAULT;

	/**
	 * This is true if the Fetch attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean fetchESet;

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
	 * The default value of the '{@link #getTargetClass() <em>Target Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetClass()
	 * @generated
	 * @ordered
	 */
	protected static final String TARGET_CLASS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTargetClass() <em>Target Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetClass()
	 * @generated
	 * @ordered
	 */
	protected String targetClass = TARGET_CLASS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ElementCollectionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OrmPackage.eINSTANCE.getElementCollection();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOrderBy(String newOrderBy) {
		String oldOrderBy = orderBy;
		orderBy = newOrderBy;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__ORDER_BY, oldOrderBy, orderBy));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OrderColumn getOrderColumn() {
		return orderColumn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOrderColumn(OrderColumn newOrderColumn, NotificationChain msgs) {
		OrderColumn oldOrderColumn = orderColumn;
		orderColumn = newOrderColumn;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__ORDER_COLUMN, oldOrderColumn, newOrderColumn);
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
	public void setOrderColumn(OrderColumn newOrderColumn) {
		if (newOrderColumn != orderColumn) {
			NotificationChain msgs = null;
			if (orderColumn != null)
				msgs = ((InternalEObject)orderColumn).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ELEMENT_COLLECTION__ORDER_COLUMN, null, msgs);
			if (newOrderColumn != null)
				msgs = ((InternalEObject)newOrderColumn).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ELEMENT_COLLECTION__ORDER_COLUMN, null, msgs);
			msgs = basicSetOrderColumn(newOrderColumn, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__ORDER_COLUMN, newOrderColumn, newOrderColumn));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MapKey getMapKey() {
		return mapKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMapKey(MapKey newMapKey, NotificationChain msgs) {
		MapKey oldMapKey = mapKey;
		mapKey = newMapKey;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__MAP_KEY, oldMapKey, newMapKey);
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
	public void setMapKey(MapKey newMapKey) {
		if (newMapKey != mapKey) {
			NotificationChain msgs = null;
			if (mapKey != null)
				msgs = ((InternalEObject)mapKey).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ELEMENT_COLLECTION__MAP_KEY, null, msgs);
			if (newMapKey != null)
				msgs = ((InternalEObject)newMapKey).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ELEMENT_COLLECTION__MAP_KEY, null, msgs);
			msgs = basicSetMapKey(newMapKey, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__MAP_KEY, newMapKey, newMapKey));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MapKeyClass getMapKeyClass() {
		return mapKeyClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMapKeyClass(MapKeyClass newMapKeyClass, NotificationChain msgs) {
		MapKeyClass oldMapKeyClass = mapKeyClass;
		mapKeyClass = newMapKeyClass;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__MAP_KEY_CLASS, oldMapKeyClass, newMapKeyClass);
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
	public void setMapKeyClass(MapKeyClass newMapKeyClass) {
		if (newMapKeyClass != mapKeyClass) {
			NotificationChain msgs = null;
			if (mapKeyClass != null)
				msgs = ((InternalEObject)mapKeyClass).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ELEMENT_COLLECTION__MAP_KEY_CLASS, null, msgs);
			if (newMapKeyClass != null)
				msgs = ((InternalEObject)newMapKeyClass).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ELEMENT_COLLECTION__MAP_KEY_CLASS, null, msgs);
			msgs = basicSetMapKeyClass(newMapKeyClass, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__MAP_KEY_CLASS, newMapKeyClass, newMapKeyClass));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TemporalType getMapKeyTemporal() {
		return mapKeyTemporal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMapKeyTemporal(TemporalType newMapKeyTemporal) {
		TemporalType oldMapKeyTemporal = mapKeyTemporal;
		mapKeyTemporal = newMapKeyTemporal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__MAP_KEY_TEMPORAL, oldMapKeyTemporal, mapKeyTemporal));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EnumType getMapKeyEnumerated() {
		return mapKeyEnumerated;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMapKeyEnumerated(EnumType newMapKeyEnumerated) {
		EnumType oldMapKeyEnumerated = mapKeyEnumerated;
		mapKeyEnumerated = newMapKeyEnumerated;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__MAP_KEY_ENUMERATED, oldMapKeyEnumerated, mapKeyEnumerated));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<AttributeOverride> getMapKeyAttributeOverride() {
		if (mapKeyAttributeOverride == null) {
			mapKeyAttributeOverride = new EObjectContainmentEList<AttributeOverride>(AttributeOverride.class, this, OrmPackage.ELEMENT_COLLECTION__MAP_KEY_ATTRIBUTE_OVERRIDE);
		}
		return mapKeyAttributeOverride;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Convert> getMapKeyConvert() {
		if (mapKeyConvert == null) {
			mapKeyConvert = new EObjectContainmentEList<Convert>(Convert.class, this, OrmPackage.ELEMENT_COLLECTION__MAP_KEY_CONVERT);
		}
		return mapKeyConvert;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MapKeyColumn getMapKeyColumn() {
		return mapKeyColumn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMapKeyColumn(MapKeyColumn newMapKeyColumn, NotificationChain msgs) {
		MapKeyColumn oldMapKeyColumn = mapKeyColumn;
		mapKeyColumn = newMapKeyColumn;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__MAP_KEY_COLUMN, oldMapKeyColumn, newMapKeyColumn);
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
	public void setMapKeyColumn(MapKeyColumn newMapKeyColumn) {
		if (newMapKeyColumn != mapKeyColumn) {
			NotificationChain msgs = null;
			if (mapKeyColumn != null)
				msgs = ((InternalEObject)mapKeyColumn).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ELEMENT_COLLECTION__MAP_KEY_COLUMN, null, msgs);
			if (newMapKeyColumn != null)
				msgs = ((InternalEObject)newMapKeyColumn).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ELEMENT_COLLECTION__MAP_KEY_COLUMN, null, msgs);
			msgs = basicSetMapKeyColumn(newMapKeyColumn, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__MAP_KEY_COLUMN, newMapKeyColumn, newMapKeyColumn));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<MapKeyJoinColumn> getMapKeyJoinColumn() {
		if (mapKeyJoinColumn == null) {
			mapKeyJoinColumn = new EObjectContainmentEList<MapKeyJoinColumn>(MapKeyJoinColumn.class, this, OrmPackage.ELEMENT_COLLECTION__MAP_KEY_JOIN_COLUMN);
		}
		return mapKeyJoinColumn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ForeignKey getMapKeyForeignKey() {
		return mapKeyForeignKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMapKeyForeignKey(ForeignKey newMapKeyForeignKey, NotificationChain msgs) {
		ForeignKey oldMapKeyForeignKey = mapKeyForeignKey;
		mapKeyForeignKey = newMapKeyForeignKey;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__MAP_KEY_FOREIGN_KEY, oldMapKeyForeignKey, newMapKeyForeignKey);
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
	public void setMapKeyForeignKey(ForeignKey newMapKeyForeignKey) {
		if (newMapKeyForeignKey != mapKeyForeignKey) {
			NotificationChain msgs = null;
			if (mapKeyForeignKey != null)
				msgs = ((InternalEObject)mapKeyForeignKey).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ELEMENT_COLLECTION__MAP_KEY_FOREIGN_KEY, null, msgs);
			if (newMapKeyForeignKey != null)
				msgs = ((InternalEObject)newMapKeyForeignKey).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ELEMENT_COLLECTION__MAP_KEY_FOREIGN_KEY, null, msgs);
			msgs = basicSetMapKeyForeignKey(newMapKeyForeignKey, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__MAP_KEY_FOREIGN_KEY, newMapKeyForeignKey, newMapKeyForeignKey));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Column getColumn() {
		return column;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetColumn(Column newColumn, NotificationChain msgs) {
		Column oldColumn = column;
		column = newColumn;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__COLUMN, oldColumn, newColumn);
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
	public void setColumn(Column newColumn) {
		if (newColumn != column) {
			NotificationChain msgs = null;
			if (column != null)
				msgs = ((InternalEObject)column).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ELEMENT_COLLECTION__COLUMN, null, msgs);
			if (newColumn != null)
				msgs = ((InternalEObject)newColumn).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ELEMENT_COLLECTION__COLUMN, null, msgs);
			msgs = basicSetColumn(newColumn, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__COLUMN, newColumn, newColumn));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TemporalType getTemporal() {
		return temporal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTemporal(TemporalType newTemporal) {
		TemporalType oldTemporal = temporal;
		temporal = newTemporal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__TEMPORAL, oldTemporal, temporal));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EnumType getEnumerated() {
		return enumerated;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEnumerated(EnumType newEnumerated) {
		EnumType oldEnumerated = enumerated;
		enumerated = newEnumerated;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__ENUMERATED, oldEnumerated, enumerated));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Lob getLob() {
		return lob;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLob(Lob newLob, NotificationChain msgs) {
		Lob oldLob = lob;
		lob = newLob;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__LOB, oldLob, newLob);
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
	public void setLob(Lob newLob) {
		if (newLob != lob) {
			NotificationChain msgs = null;
			if (lob != null)
				msgs = ((InternalEObject)lob).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ELEMENT_COLLECTION__LOB, null, msgs);
			if (newLob != null)
				msgs = ((InternalEObject)newLob).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ELEMENT_COLLECTION__LOB, null, msgs);
			msgs = basicSetLob(newLob, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__LOB, newLob, newLob));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<AttributeOverride> getAttributeOverride() {
		if (attributeOverride == null) {
			attributeOverride = new EObjectContainmentEList<AttributeOverride>(AttributeOverride.class, this, OrmPackage.ELEMENT_COLLECTION__ATTRIBUTE_OVERRIDE);
		}
		return attributeOverride;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<AssociationOverride> getAssociationOverride() {
		if (associationOverride == null) {
			associationOverride = new EObjectContainmentEList<AssociationOverride>(AssociationOverride.class, this, OrmPackage.ELEMENT_COLLECTION__ASSOCIATION_OVERRIDE);
		}
		return associationOverride;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Convert> getConvert() {
		if (convert == null) {
			convert = new EObjectContainmentEList<Convert>(Convert.class, this, OrmPackage.ELEMENT_COLLECTION__CONVERT);
		}
		return convert;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CollectionTable getCollectionTable() {
		return collectionTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCollectionTable(CollectionTable newCollectionTable, NotificationChain msgs) {
		CollectionTable oldCollectionTable = collectionTable;
		collectionTable = newCollectionTable;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__COLLECTION_TABLE, oldCollectionTable, newCollectionTable);
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
	public void setCollectionTable(CollectionTable newCollectionTable) {
		if (newCollectionTable != collectionTable) {
			NotificationChain msgs = null;
			if (collectionTable != null)
				msgs = ((InternalEObject)collectionTable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ELEMENT_COLLECTION__COLLECTION_TABLE, null, msgs);
			if (newCollectionTable != null)
				msgs = ((InternalEObject)newCollectionTable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ELEMENT_COLLECTION__COLLECTION_TABLE, null, msgs);
			msgs = basicSetCollectionTable(newCollectionTable, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__COLLECTION_TABLE, newCollectionTable, newCollectionTable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AccessType getAccess() {
		return access;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAccess(AccessType newAccess) {
		AccessType oldAccess = access;
		access = newAccess == null ? ACCESS_EDEFAULT : newAccess;
		boolean oldAccessESet = accessESet;
		accessESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__ACCESS, oldAccess, access, !oldAccessESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetAccess() {
		AccessType oldAccess = access;
		boolean oldAccessESet = accessESet;
		access = ACCESS_EDEFAULT;
		accessESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, OrmPackage.ELEMENT_COLLECTION__ACCESS, oldAccess, ACCESS_EDEFAULT, oldAccessESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetAccess() {
		return accessESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FetchType getFetch() {
		return fetch;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFetch(FetchType newFetch) {
		FetchType oldFetch = fetch;
		fetch = newFetch == null ? FETCH_EDEFAULT : newFetch;
		boolean oldFetchESet = fetchESet;
		fetchESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__FETCH, oldFetch, fetch, !oldFetchESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetFetch() {
		FetchType oldFetch = fetch;
		boolean oldFetchESet = fetchESet;
		fetch = FETCH_EDEFAULT;
		fetchESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, OrmPackage.ELEMENT_COLLECTION__FETCH, oldFetch, FETCH_EDEFAULT, oldFetchESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetFetch() {
		return fetchESet;
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
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getTargetClass() {
		return targetClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTargetClass(String newTargetClass) {
		String oldTargetClass = targetClass;
		targetClass = newTargetClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ELEMENT_COLLECTION__TARGET_CLASS, oldTargetClass, targetClass));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case OrmPackage.ELEMENT_COLLECTION__ORDER_COLUMN:
				return basicSetOrderColumn(null, msgs);
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY:
				return basicSetMapKey(null, msgs);
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_CLASS:
				return basicSetMapKeyClass(null, msgs);
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_ATTRIBUTE_OVERRIDE:
				return ((InternalEList<?>)getMapKeyAttributeOverride()).basicRemove(otherEnd, msgs);
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_CONVERT:
				return ((InternalEList<?>)getMapKeyConvert()).basicRemove(otherEnd, msgs);
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_COLUMN:
				return basicSetMapKeyColumn(null, msgs);
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_JOIN_COLUMN:
				return ((InternalEList<?>)getMapKeyJoinColumn()).basicRemove(otherEnd, msgs);
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_FOREIGN_KEY:
				return basicSetMapKeyForeignKey(null, msgs);
			case OrmPackage.ELEMENT_COLLECTION__COLUMN:
				return basicSetColumn(null, msgs);
			case OrmPackage.ELEMENT_COLLECTION__LOB:
				return basicSetLob(null, msgs);
			case OrmPackage.ELEMENT_COLLECTION__ATTRIBUTE_OVERRIDE:
				return ((InternalEList<?>)getAttributeOverride()).basicRemove(otherEnd, msgs);
			case OrmPackage.ELEMENT_COLLECTION__ASSOCIATION_OVERRIDE:
				return ((InternalEList<?>)getAssociationOverride()).basicRemove(otherEnd, msgs);
			case OrmPackage.ELEMENT_COLLECTION__CONVERT:
				return ((InternalEList<?>)getConvert()).basicRemove(otherEnd, msgs);
			case OrmPackage.ELEMENT_COLLECTION__COLLECTION_TABLE:
				return basicSetCollectionTable(null, msgs);
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
			case OrmPackage.ELEMENT_COLLECTION__ORDER_BY:
				return getOrderBy();
			case OrmPackage.ELEMENT_COLLECTION__ORDER_COLUMN:
				return getOrderColumn();
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY:
				return getMapKey();
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_CLASS:
				return getMapKeyClass();
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_TEMPORAL:
				return getMapKeyTemporal();
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_ENUMERATED:
				return getMapKeyEnumerated();
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_ATTRIBUTE_OVERRIDE:
				return getMapKeyAttributeOverride();
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_CONVERT:
				return getMapKeyConvert();
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_COLUMN:
				return getMapKeyColumn();
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_JOIN_COLUMN:
				return getMapKeyJoinColumn();
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_FOREIGN_KEY:
				return getMapKeyForeignKey();
			case OrmPackage.ELEMENT_COLLECTION__COLUMN:
				return getColumn();
			case OrmPackage.ELEMENT_COLLECTION__TEMPORAL:
				return getTemporal();
			case OrmPackage.ELEMENT_COLLECTION__ENUMERATED:
				return getEnumerated();
			case OrmPackage.ELEMENT_COLLECTION__LOB:
				return getLob();
			case OrmPackage.ELEMENT_COLLECTION__ATTRIBUTE_OVERRIDE:
				return getAttributeOverride();
			case OrmPackage.ELEMENT_COLLECTION__ASSOCIATION_OVERRIDE:
				return getAssociationOverride();
			case OrmPackage.ELEMENT_COLLECTION__CONVERT:
				return getConvert();
			case OrmPackage.ELEMENT_COLLECTION__COLLECTION_TABLE:
				return getCollectionTable();
			case OrmPackage.ELEMENT_COLLECTION__ACCESS:
				return getAccess();
			case OrmPackage.ELEMENT_COLLECTION__FETCH:
				return getFetch();
			case OrmPackage.ELEMENT_COLLECTION__NAME:
				return getName();
			case OrmPackage.ELEMENT_COLLECTION__TARGET_CLASS:
				return getTargetClass();
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
			case OrmPackage.ELEMENT_COLLECTION__ORDER_BY:
				setOrderBy((String)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__ORDER_COLUMN:
				setOrderColumn((OrderColumn)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY:
				setMapKey((MapKey)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_CLASS:
				setMapKeyClass((MapKeyClass)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_TEMPORAL:
				setMapKeyTemporal((TemporalType)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_ENUMERATED:
				setMapKeyEnumerated((EnumType)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_ATTRIBUTE_OVERRIDE:
				getMapKeyAttributeOverride().clear();
				getMapKeyAttributeOverride().addAll((Collection<? extends AttributeOverride>)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_CONVERT:
				getMapKeyConvert().clear();
				getMapKeyConvert().addAll((Collection<? extends Convert>)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_COLUMN:
				setMapKeyColumn((MapKeyColumn)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_JOIN_COLUMN:
				getMapKeyJoinColumn().clear();
				getMapKeyJoinColumn().addAll((Collection<? extends MapKeyJoinColumn>)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_FOREIGN_KEY:
				setMapKeyForeignKey((ForeignKey)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__COLUMN:
				setColumn((Column)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__TEMPORAL:
				setTemporal((TemporalType)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__ENUMERATED:
				setEnumerated((EnumType)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__LOB:
				setLob((Lob)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__ATTRIBUTE_OVERRIDE:
				getAttributeOverride().clear();
				getAttributeOverride().addAll((Collection<? extends AttributeOverride>)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__ASSOCIATION_OVERRIDE:
				getAssociationOverride().clear();
				getAssociationOverride().addAll((Collection<? extends AssociationOverride>)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__CONVERT:
				getConvert().clear();
				getConvert().addAll((Collection<? extends Convert>)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__COLLECTION_TABLE:
				setCollectionTable((CollectionTable)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__ACCESS:
				setAccess((AccessType)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__FETCH:
				setFetch((FetchType)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__NAME:
				setName((String)newValue);
				return;
			case OrmPackage.ELEMENT_COLLECTION__TARGET_CLASS:
				setTargetClass((String)newValue);
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
			case OrmPackage.ELEMENT_COLLECTION__ORDER_BY:
				setOrderBy(ORDER_BY_EDEFAULT);
				return;
			case OrmPackage.ELEMENT_COLLECTION__ORDER_COLUMN:
				setOrderColumn((OrderColumn)null);
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY:
				setMapKey((MapKey)null);
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_CLASS:
				setMapKeyClass((MapKeyClass)null);
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_TEMPORAL:
				setMapKeyTemporal(MAP_KEY_TEMPORAL_EDEFAULT);
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_ENUMERATED:
				setMapKeyEnumerated(MAP_KEY_ENUMERATED_EDEFAULT);
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_ATTRIBUTE_OVERRIDE:
				getMapKeyAttributeOverride().clear();
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_CONVERT:
				getMapKeyConvert().clear();
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_COLUMN:
				setMapKeyColumn((MapKeyColumn)null);
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_JOIN_COLUMN:
				getMapKeyJoinColumn().clear();
				return;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_FOREIGN_KEY:
				setMapKeyForeignKey((ForeignKey)null);
				return;
			case OrmPackage.ELEMENT_COLLECTION__COLUMN:
				setColumn((Column)null);
				return;
			case OrmPackage.ELEMENT_COLLECTION__TEMPORAL:
				setTemporal(TEMPORAL_EDEFAULT);
				return;
			case OrmPackage.ELEMENT_COLLECTION__ENUMERATED:
				setEnumerated(ENUMERATED_EDEFAULT);
				return;
			case OrmPackage.ELEMENT_COLLECTION__LOB:
				setLob((Lob)null);
				return;
			case OrmPackage.ELEMENT_COLLECTION__ATTRIBUTE_OVERRIDE:
				getAttributeOverride().clear();
				return;
			case OrmPackage.ELEMENT_COLLECTION__ASSOCIATION_OVERRIDE:
				getAssociationOverride().clear();
				return;
			case OrmPackage.ELEMENT_COLLECTION__CONVERT:
				getConvert().clear();
				return;
			case OrmPackage.ELEMENT_COLLECTION__COLLECTION_TABLE:
				setCollectionTable((CollectionTable)null);
				return;
			case OrmPackage.ELEMENT_COLLECTION__ACCESS:
				unsetAccess();
				return;
			case OrmPackage.ELEMENT_COLLECTION__FETCH:
				unsetFetch();
				return;
			case OrmPackage.ELEMENT_COLLECTION__NAME:
				setName(NAME_EDEFAULT);
				return;
			case OrmPackage.ELEMENT_COLLECTION__TARGET_CLASS:
				setTargetClass(TARGET_CLASS_EDEFAULT);
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
			case OrmPackage.ELEMENT_COLLECTION__ORDER_BY:
				return ORDER_BY_EDEFAULT == null ? orderBy != null : !ORDER_BY_EDEFAULT.equals(orderBy);
			case OrmPackage.ELEMENT_COLLECTION__ORDER_COLUMN:
				return orderColumn != null;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY:
				return mapKey != null;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_CLASS:
				return mapKeyClass != null;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_TEMPORAL:
				return MAP_KEY_TEMPORAL_EDEFAULT == null ? mapKeyTemporal != null : !MAP_KEY_TEMPORAL_EDEFAULT.equals(mapKeyTemporal);
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_ENUMERATED:
				return MAP_KEY_ENUMERATED_EDEFAULT == null ? mapKeyEnumerated != null : !MAP_KEY_ENUMERATED_EDEFAULT.equals(mapKeyEnumerated);
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_ATTRIBUTE_OVERRIDE:
				return mapKeyAttributeOverride != null && !mapKeyAttributeOverride.isEmpty();
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_CONVERT:
				return mapKeyConvert != null && !mapKeyConvert.isEmpty();
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_COLUMN:
				return mapKeyColumn != null;
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_JOIN_COLUMN:
				return mapKeyJoinColumn != null && !mapKeyJoinColumn.isEmpty();
			case OrmPackage.ELEMENT_COLLECTION__MAP_KEY_FOREIGN_KEY:
				return mapKeyForeignKey != null;
			case OrmPackage.ELEMENT_COLLECTION__COLUMN:
				return column != null;
			case OrmPackage.ELEMENT_COLLECTION__TEMPORAL:
				return TEMPORAL_EDEFAULT == null ? temporal != null : !TEMPORAL_EDEFAULT.equals(temporal);
			case OrmPackage.ELEMENT_COLLECTION__ENUMERATED:
				return ENUMERATED_EDEFAULT == null ? enumerated != null : !ENUMERATED_EDEFAULT.equals(enumerated);
			case OrmPackage.ELEMENT_COLLECTION__LOB:
				return lob != null;
			case OrmPackage.ELEMENT_COLLECTION__ATTRIBUTE_OVERRIDE:
				return attributeOverride != null && !attributeOverride.isEmpty();
			case OrmPackage.ELEMENT_COLLECTION__ASSOCIATION_OVERRIDE:
				return associationOverride != null && !associationOverride.isEmpty();
			case OrmPackage.ELEMENT_COLLECTION__CONVERT:
				return convert != null && !convert.isEmpty();
			case OrmPackage.ELEMENT_COLLECTION__COLLECTION_TABLE:
				return collectionTable != null;
			case OrmPackage.ELEMENT_COLLECTION__ACCESS:
				return isSetAccess();
			case OrmPackage.ELEMENT_COLLECTION__FETCH:
				return isSetFetch();
			case OrmPackage.ELEMENT_COLLECTION__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case OrmPackage.ELEMENT_COLLECTION__TARGET_CLASS:
				return TARGET_CLASS_EDEFAULT == null ? targetClass != null : !TARGET_CLASS_EDEFAULT.equals(targetClass);
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
		result.append(" (orderBy: ");
		result.append(orderBy);
		result.append(", mapKeyTemporal: ");
		result.append(mapKeyTemporal);
		result.append(", mapKeyEnumerated: ");
		result.append(mapKeyEnumerated);
		result.append(", temporal: ");
		result.append(temporal);
		result.append(", enumerated: ");
		result.append(enumerated);
		result.append(", access: ");
		if (accessESet) result.append(access); else result.append("<unset>");
		result.append(", fetch: ");
		if (fetchESet) result.append(fetch); else result.append("<unset>");
		result.append(", name: ");
		result.append(name);
		result.append(", targetClass: ");
		result.append(targetClass);
		result.append(')');
		return result.toString();
	}

} //ElementCollectionImpl
