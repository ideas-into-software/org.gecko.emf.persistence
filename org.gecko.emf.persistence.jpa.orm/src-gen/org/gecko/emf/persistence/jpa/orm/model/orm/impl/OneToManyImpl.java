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
import org.gecko.emf.persistence.jpa.orm.model.orm.AttributeOverride;
import org.gecko.emf.persistence.jpa.orm.model.orm.CascadeType;
import org.gecko.emf.persistence.jpa.orm.model.orm.Convert;
import org.gecko.emf.persistence.jpa.orm.model.orm.EnumType;
import org.gecko.emf.persistence.jpa.orm.model.orm.FetchType;
import org.gecko.emf.persistence.jpa.orm.model.orm.ForeignKey;
import org.gecko.emf.persistence.jpa.orm.model.orm.JoinColumn;
import org.gecko.emf.persistence.jpa.orm.model.orm.JoinTable;
import org.gecko.emf.persistence.jpa.orm.model.orm.MapKey;
import org.gecko.emf.persistence.jpa.orm.model.orm.MapKeyClass;
import org.gecko.emf.persistence.jpa.orm.model.orm.MapKeyColumn;
import org.gecko.emf.persistence.jpa.orm.model.orm.MapKeyJoinColumn;
import org.gecko.emf.persistence.jpa.orm.model.orm.OneToMany;
import org.gecko.emf.persistence.jpa.orm.model.orm.OrderColumn;
import org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage;
import org.gecko.emf.persistence.jpa.orm.model.orm.TemporalType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>One To Many</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getOrderBy <em>Order By</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getOrderColumn <em>Order Column</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getMapKey <em>Map Key</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getMapKeyClass <em>Map Key Class</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getMapKeyTemporal <em>Map Key Temporal</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getMapKeyEnumerated <em>Map Key Enumerated</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getMapKeyAttributeOverride <em>Map Key Attribute Override</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getMapKeyConvert <em>Map Key Convert</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getMapKeyColumn <em>Map Key Column</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getMapKeyJoinColumn <em>Map Key Join Column</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getMapKeyForeignKey <em>Map Key Foreign Key</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getJoinTable <em>Join Table</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getJoinColumn <em>Join Column</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getForeignKey <em>Foreign Key</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getCascade <em>Cascade</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getAccess <em>Access</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getFetch <em>Fetch</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getMappedBy <em>Mapped By</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#isOrphanRemoval <em>Orphan Removal</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.OneToManyImpl#getTargetEntity <em>Target Entity</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OneToManyImpl extends MinimalEObjectImpl.Container implements OneToMany {
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
	 * The cached value of the '{@link #getJoinTable() <em>Join Table</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJoinTable()
	 * @generated
	 * @ordered
	 */
	protected JoinTable joinTable;

	/**
	 * The cached value of the '{@link #getJoinColumn() <em>Join Column</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJoinColumn()
	 * @generated
	 * @ordered
	 */
	protected EList<JoinColumn> joinColumn;

	/**
	 * The cached value of the '{@link #getForeignKey() <em>Foreign Key</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getForeignKey()
	 * @generated
	 * @ordered
	 */
	protected ForeignKey foreignKey;

	/**
	 * The cached value of the '{@link #getCascade() <em>Cascade</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCascade()
	 * @generated
	 * @ordered
	 */
	protected CascadeType cascade;

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
	 * The default value of the '{@link #getMappedBy() <em>Mapped By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMappedBy()
	 * @generated
	 * @ordered
	 */
	protected static final String MAPPED_BY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMappedBy() <em>Mapped By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMappedBy()
	 * @generated
	 * @ordered
	 */
	protected String mappedBy = MAPPED_BY_EDEFAULT;

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
	 * The default value of the '{@link #isOrphanRemoval() <em>Orphan Removal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOrphanRemoval()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ORPHAN_REMOVAL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isOrphanRemoval() <em>Orphan Removal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOrphanRemoval()
	 * @generated
	 * @ordered
	 */
	protected boolean orphanRemoval = ORPHAN_REMOVAL_EDEFAULT;

	/**
	 * This is true if the Orphan Removal attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean orphanRemovalESet;

	/**
	 * The default value of the '{@link #getTargetEntity() <em>Target Entity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetEntity()
	 * @generated
	 * @ordered
	 */
	protected static final String TARGET_ENTITY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTargetEntity() <em>Target Entity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetEntity()
	 * @generated
	 * @ordered
	 */
	protected String targetEntity = TARGET_ENTITY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OneToManyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OrmPackage.eINSTANCE.getOneToMany();
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
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__ORDER_BY, oldOrderBy, orderBy));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__ORDER_COLUMN, oldOrderColumn, newOrderColumn);
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
				msgs = ((InternalEObject)orderColumn).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ONE_TO_MANY__ORDER_COLUMN, null, msgs);
			if (newOrderColumn != null)
				msgs = ((InternalEObject)newOrderColumn).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ONE_TO_MANY__ORDER_COLUMN, null, msgs);
			msgs = basicSetOrderColumn(newOrderColumn, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__ORDER_COLUMN, newOrderColumn, newOrderColumn));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__MAP_KEY, oldMapKey, newMapKey);
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
				msgs = ((InternalEObject)mapKey).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ONE_TO_MANY__MAP_KEY, null, msgs);
			if (newMapKey != null)
				msgs = ((InternalEObject)newMapKey).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ONE_TO_MANY__MAP_KEY, null, msgs);
			msgs = basicSetMapKey(newMapKey, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__MAP_KEY, newMapKey, newMapKey));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__MAP_KEY_CLASS, oldMapKeyClass, newMapKeyClass);
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
				msgs = ((InternalEObject)mapKeyClass).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ONE_TO_MANY__MAP_KEY_CLASS, null, msgs);
			if (newMapKeyClass != null)
				msgs = ((InternalEObject)newMapKeyClass).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ONE_TO_MANY__MAP_KEY_CLASS, null, msgs);
			msgs = basicSetMapKeyClass(newMapKeyClass, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__MAP_KEY_CLASS, newMapKeyClass, newMapKeyClass));
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
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__MAP_KEY_TEMPORAL, oldMapKeyTemporal, mapKeyTemporal));
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
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__MAP_KEY_ENUMERATED, oldMapKeyEnumerated, mapKeyEnumerated));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<AttributeOverride> getMapKeyAttributeOverride() {
		if (mapKeyAttributeOverride == null) {
			mapKeyAttributeOverride = new EObjectContainmentEList<AttributeOverride>(AttributeOverride.class, this, OrmPackage.ONE_TO_MANY__MAP_KEY_ATTRIBUTE_OVERRIDE);
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
			mapKeyConvert = new EObjectContainmentEList<Convert>(Convert.class, this, OrmPackage.ONE_TO_MANY__MAP_KEY_CONVERT);
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__MAP_KEY_COLUMN, oldMapKeyColumn, newMapKeyColumn);
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
				msgs = ((InternalEObject)mapKeyColumn).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ONE_TO_MANY__MAP_KEY_COLUMN, null, msgs);
			if (newMapKeyColumn != null)
				msgs = ((InternalEObject)newMapKeyColumn).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ONE_TO_MANY__MAP_KEY_COLUMN, null, msgs);
			msgs = basicSetMapKeyColumn(newMapKeyColumn, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__MAP_KEY_COLUMN, newMapKeyColumn, newMapKeyColumn));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<MapKeyJoinColumn> getMapKeyJoinColumn() {
		if (mapKeyJoinColumn == null) {
			mapKeyJoinColumn = new EObjectContainmentEList<MapKeyJoinColumn>(MapKeyJoinColumn.class, this, OrmPackage.ONE_TO_MANY__MAP_KEY_JOIN_COLUMN);
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__MAP_KEY_FOREIGN_KEY, oldMapKeyForeignKey, newMapKeyForeignKey);
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
				msgs = ((InternalEObject)mapKeyForeignKey).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ONE_TO_MANY__MAP_KEY_FOREIGN_KEY, null, msgs);
			if (newMapKeyForeignKey != null)
				msgs = ((InternalEObject)newMapKeyForeignKey).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ONE_TO_MANY__MAP_KEY_FOREIGN_KEY, null, msgs);
			msgs = basicSetMapKeyForeignKey(newMapKeyForeignKey, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__MAP_KEY_FOREIGN_KEY, newMapKeyForeignKey, newMapKeyForeignKey));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public JoinTable getJoinTable() {
		return joinTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetJoinTable(JoinTable newJoinTable, NotificationChain msgs) {
		JoinTable oldJoinTable = joinTable;
		joinTable = newJoinTable;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__JOIN_TABLE, oldJoinTable, newJoinTable);
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
	public void setJoinTable(JoinTable newJoinTable) {
		if (newJoinTable != joinTable) {
			NotificationChain msgs = null;
			if (joinTable != null)
				msgs = ((InternalEObject)joinTable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ONE_TO_MANY__JOIN_TABLE, null, msgs);
			if (newJoinTable != null)
				msgs = ((InternalEObject)newJoinTable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ONE_TO_MANY__JOIN_TABLE, null, msgs);
			msgs = basicSetJoinTable(newJoinTable, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__JOIN_TABLE, newJoinTable, newJoinTable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<JoinColumn> getJoinColumn() {
		if (joinColumn == null) {
			joinColumn = new EObjectContainmentEList<JoinColumn>(JoinColumn.class, this, OrmPackage.ONE_TO_MANY__JOIN_COLUMN);
		}
		return joinColumn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ForeignKey getForeignKey() {
		return foreignKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetForeignKey(ForeignKey newForeignKey, NotificationChain msgs) {
		ForeignKey oldForeignKey = foreignKey;
		foreignKey = newForeignKey;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__FOREIGN_KEY, oldForeignKey, newForeignKey);
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
	public void setForeignKey(ForeignKey newForeignKey) {
		if (newForeignKey != foreignKey) {
			NotificationChain msgs = null;
			if (foreignKey != null)
				msgs = ((InternalEObject)foreignKey).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ONE_TO_MANY__FOREIGN_KEY, null, msgs);
			if (newForeignKey != null)
				msgs = ((InternalEObject)newForeignKey).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ONE_TO_MANY__FOREIGN_KEY, null, msgs);
			msgs = basicSetForeignKey(newForeignKey, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__FOREIGN_KEY, newForeignKey, newForeignKey));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CascadeType getCascade() {
		return cascade;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCascade(CascadeType newCascade, NotificationChain msgs) {
		CascadeType oldCascade = cascade;
		cascade = newCascade;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__CASCADE, oldCascade, newCascade);
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
	public void setCascade(CascadeType newCascade) {
		if (newCascade != cascade) {
			NotificationChain msgs = null;
			if (cascade != null)
				msgs = ((InternalEObject)cascade).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ONE_TO_MANY__CASCADE, null, msgs);
			if (newCascade != null)
				msgs = ((InternalEObject)newCascade).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.ONE_TO_MANY__CASCADE, null, msgs);
			msgs = basicSetCascade(newCascade, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__CASCADE, newCascade, newCascade));
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
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__ACCESS, oldAccess, access, !oldAccessESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, OrmPackage.ONE_TO_MANY__ACCESS, oldAccess, ACCESS_EDEFAULT, oldAccessESet));
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
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__FETCH, oldFetch, fetch, !oldFetchESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, OrmPackage.ONE_TO_MANY__FETCH, oldFetch, FETCH_EDEFAULT, oldFetchESet));
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
	public String getMappedBy() {
		return mappedBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMappedBy(String newMappedBy) {
		String oldMappedBy = mappedBy;
		mappedBy = newMappedBy;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__MAPPED_BY, oldMappedBy, mappedBy));
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
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isOrphanRemoval() {
		return orphanRemoval;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOrphanRemoval(boolean newOrphanRemoval) {
		boolean oldOrphanRemoval = orphanRemoval;
		orphanRemoval = newOrphanRemoval;
		boolean oldOrphanRemovalESet = orphanRemovalESet;
		orphanRemovalESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__ORPHAN_REMOVAL, oldOrphanRemoval, orphanRemoval, !oldOrphanRemovalESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetOrphanRemoval() {
		boolean oldOrphanRemoval = orphanRemoval;
		boolean oldOrphanRemovalESet = orphanRemovalESet;
		orphanRemoval = ORPHAN_REMOVAL_EDEFAULT;
		orphanRemovalESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, OrmPackage.ONE_TO_MANY__ORPHAN_REMOVAL, oldOrphanRemoval, ORPHAN_REMOVAL_EDEFAULT, oldOrphanRemovalESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetOrphanRemoval() {
		return orphanRemovalESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getTargetEntity() {
		return targetEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTargetEntity(String newTargetEntity) {
		String oldTargetEntity = targetEntity;
		targetEntity = newTargetEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.ONE_TO_MANY__TARGET_ENTITY, oldTargetEntity, targetEntity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case OrmPackage.ONE_TO_MANY__ORDER_COLUMN:
				return basicSetOrderColumn(null, msgs);
			case OrmPackage.ONE_TO_MANY__MAP_KEY:
				return basicSetMapKey(null, msgs);
			case OrmPackage.ONE_TO_MANY__MAP_KEY_CLASS:
				return basicSetMapKeyClass(null, msgs);
			case OrmPackage.ONE_TO_MANY__MAP_KEY_ATTRIBUTE_OVERRIDE:
				return ((InternalEList<?>)getMapKeyAttributeOverride()).basicRemove(otherEnd, msgs);
			case OrmPackage.ONE_TO_MANY__MAP_KEY_CONVERT:
				return ((InternalEList<?>)getMapKeyConvert()).basicRemove(otherEnd, msgs);
			case OrmPackage.ONE_TO_MANY__MAP_KEY_COLUMN:
				return basicSetMapKeyColumn(null, msgs);
			case OrmPackage.ONE_TO_MANY__MAP_KEY_JOIN_COLUMN:
				return ((InternalEList<?>)getMapKeyJoinColumn()).basicRemove(otherEnd, msgs);
			case OrmPackage.ONE_TO_MANY__MAP_KEY_FOREIGN_KEY:
				return basicSetMapKeyForeignKey(null, msgs);
			case OrmPackage.ONE_TO_MANY__JOIN_TABLE:
				return basicSetJoinTable(null, msgs);
			case OrmPackage.ONE_TO_MANY__JOIN_COLUMN:
				return ((InternalEList<?>)getJoinColumn()).basicRemove(otherEnd, msgs);
			case OrmPackage.ONE_TO_MANY__FOREIGN_KEY:
				return basicSetForeignKey(null, msgs);
			case OrmPackage.ONE_TO_MANY__CASCADE:
				return basicSetCascade(null, msgs);
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
			case OrmPackage.ONE_TO_MANY__ORDER_BY:
				return getOrderBy();
			case OrmPackage.ONE_TO_MANY__ORDER_COLUMN:
				return getOrderColumn();
			case OrmPackage.ONE_TO_MANY__MAP_KEY:
				return getMapKey();
			case OrmPackage.ONE_TO_MANY__MAP_KEY_CLASS:
				return getMapKeyClass();
			case OrmPackage.ONE_TO_MANY__MAP_KEY_TEMPORAL:
				return getMapKeyTemporal();
			case OrmPackage.ONE_TO_MANY__MAP_KEY_ENUMERATED:
				return getMapKeyEnumerated();
			case OrmPackage.ONE_TO_MANY__MAP_KEY_ATTRIBUTE_OVERRIDE:
				return getMapKeyAttributeOverride();
			case OrmPackage.ONE_TO_MANY__MAP_KEY_CONVERT:
				return getMapKeyConvert();
			case OrmPackage.ONE_TO_MANY__MAP_KEY_COLUMN:
				return getMapKeyColumn();
			case OrmPackage.ONE_TO_MANY__MAP_KEY_JOIN_COLUMN:
				return getMapKeyJoinColumn();
			case OrmPackage.ONE_TO_MANY__MAP_KEY_FOREIGN_KEY:
				return getMapKeyForeignKey();
			case OrmPackage.ONE_TO_MANY__JOIN_TABLE:
				return getJoinTable();
			case OrmPackage.ONE_TO_MANY__JOIN_COLUMN:
				return getJoinColumn();
			case OrmPackage.ONE_TO_MANY__FOREIGN_KEY:
				return getForeignKey();
			case OrmPackage.ONE_TO_MANY__CASCADE:
				return getCascade();
			case OrmPackage.ONE_TO_MANY__ACCESS:
				return getAccess();
			case OrmPackage.ONE_TO_MANY__FETCH:
				return getFetch();
			case OrmPackage.ONE_TO_MANY__MAPPED_BY:
				return getMappedBy();
			case OrmPackage.ONE_TO_MANY__NAME:
				return getName();
			case OrmPackage.ONE_TO_MANY__ORPHAN_REMOVAL:
				return isOrphanRemoval();
			case OrmPackage.ONE_TO_MANY__TARGET_ENTITY:
				return getTargetEntity();
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
			case OrmPackage.ONE_TO_MANY__ORDER_BY:
				setOrderBy((String)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__ORDER_COLUMN:
				setOrderColumn((OrderColumn)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY:
				setMapKey((MapKey)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_CLASS:
				setMapKeyClass((MapKeyClass)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_TEMPORAL:
				setMapKeyTemporal((TemporalType)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_ENUMERATED:
				setMapKeyEnumerated((EnumType)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_ATTRIBUTE_OVERRIDE:
				getMapKeyAttributeOverride().clear();
				getMapKeyAttributeOverride().addAll((Collection<? extends AttributeOverride>)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_CONVERT:
				getMapKeyConvert().clear();
				getMapKeyConvert().addAll((Collection<? extends Convert>)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_COLUMN:
				setMapKeyColumn((MapKeyColumn)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_JOIN_COLUMN:
				getMapKeyJoinColumn().clear();
				getMapKeyJoinColumn().addAll((Collection<? extends MapKeyJoinColumn>)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_FOREIGN_KEY:
				setMapKeyForeignKey((ForeignKey)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__JOIN_TABLE:
				setJoinTable((JoinTable)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__JOIN_COLUMN:
				getJoinColumn().clear();
				getJoinColumn().addAll((Collection<? extends JoinColumn>)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__FOREIGN_KEY:
				setForeignKey((ForeignKey)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__CASCADE:
				setCascade((CascadeType)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__ACCESS:
				setAccess((AccessType)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__FETCH:
				setFetch((FetchType)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__MAPPED_BY:
				setMappedBy((String)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__NAME:
				setName((String)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__ORPHAN_REMOVAL:
				setOrphanRemoval((Boolean)newValue);
				return;
			case OrmPackage.ONE_TO_MANY__TARGET_ENTITY:
				setTargetEntity((String)newValue);
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
			case OrmPackage.ONE_TO_MANY__ORDER_BY:
				setOrderBy(ORDER_BY_EDEFAULT);
				return;
			case OrmPackage.ONE_TO_MANY__ORDER_COLUMN:
				setOrderColumn((OrderColumn)null);
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY:
				setMapKey((MapKey)null);
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_CLASS:
				setMapKeyClass((MapKeyClass)null);
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_TEMPORAL:
				setMapKeyTemporal(MAP_KEY_TEMPORAL_EDEFAULT);
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_ENUMERATED:
				setMapKeyEnumerated(MAP_KEY_ENUMERATED_EDEFAULT);
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_ATTRIBUTE_OVERRIDE:
				getMapKeyAttributeOverride().clear();
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_CONVERT:
				getMapKeyConvert().clear();
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_COLUMN:
				setMapKeyColumn((MapKeyColumn)null);
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_JOIN_COLUMN:
				getMapKeyJoinColumn().clear();
				return;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_FOREIGN_KEY:
				setMapKeyForeignKey((ForeignKey)null);
				return;
			case OrmPackage.ONE_TO_MANY__JOIN_TABLE:
				setJoinTable((JoinTable)null);
				return;
			case OrmPackage.ONE_TO_MANY__JOIN_COLUMN:
				getJoinColumn().clear();
				return;
			case OrmPackage.ONE_TO_MANY__FOREIGN_KEY:
				setForeignKey((ForeignKey)null);
				return;
			case OrmPackage.ONE_TO_MANY__CASCADE:
				setCascade((CascadeType)null);
				return;
			case OrmPackage.ONE_TO_MANY__ACCESS:
				unsetAccess();
				return;
			case OrmPackage.ONE_TO_MANY__FETCH:
				unsetFetch();
				return;
			case OrmPackage.ONE_TO_MANY__MAPPED_BY:
				setMappedBy(MAPPED_BY_EDEFAULT);
				return;
			case OrmPackage.ONE_TO_MANY__NAME:
				setName(NAME_EDEFAULT);
				return;
			case OrmPackage.ONE_TO_MANY__ORPHAN_REMOVAL:
				unsetOrphanRemoval();
				return;
			case OrmPackage.ONE_TO_MANY__TARGET_ENTITY:
				setTargetEntity(TARGET_ENTITY_EDEFAULT);
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
			case OrmPackage.ONE_TO_MANY__ORDER_BY:
				return ORDER_BY_EDEFAULT == null ? orderBy != null : !ORDER_BY_EDEFAULT.equals(orderBy);
			case OrmPackage.ONE_TO_MANY__ORDER_COLUMN:
				return orderColumn != null;
			case OrmPackage.ONE_TO_MANY__MAP_KEY:
				return mapKey != null;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_CLASS:
				return mapKeyClass != null;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_TEMPORAL:
				return MAP_KEY_TEMPORAL_EDEFAULT == null ? mapKeyTemporal != null : !MAP_KEY_TEMPORAL_EDEFAULT.equals(mapKeyTemporal);
			case OrmPackage.ONE_TO_MANY__MAP_KEY_ENUMERATED:
				return MAP_KEY_ENUMERATED_EDEFAULT == null ? mapKeyEnumerated != null : !MAP_KEY_ENUMERATED_EDEFAULT.equals(mapKeyEnumerated);
			case OrmPackage.ONE_TO_MANY__MAP_KEY_ATTRIBUTE_OVERRIDE:
				return mapKeyAttributeOverride != null && !mapKeyAttributeOverride.isEmpty();
			case OrmPackage.ONE_TO_MANY__MAP_KEY_CONVERT:
				return mapKeyConvert != null && !mapKeyConvert.isEmpty();
			case OrmPackage.ONE_TO_MANY__MAP_KEY_COLUMN:
				return mapKeyColumn != null;
			case OrmPackage.ONE_TO_MANY__MAP_KEY_JOIN_COLUMN:
				return mapKeyJoinColumn != null && !mapKeyJoinColumn.isEmpty();
			case OrmPackage.ONE_TO_MANY__MAP_KEY_FOREIGN_KEY:
				return mapKeyForeignKey != null;
			case OrmPackage.ONE_TO_MANY__JOIN_TABLE:
				return joinTable != null;
			case OrmPackage.ONE_TO_MANY__JOIN_COLUMN:
				return joinColumn != null && !joinColumn.isEmpty();
			case OrmPackage.ONE_TO_MANY__FOREIGN_KEY:
				return foreignKey != null;
			case OrmPackage.ONE_TO_MANY__CASCADE:
				return cascade != null;
			case OrmPackage.ONE_TO_MANY__ACCESS:
				return isSetAccess();
			case OrmPackage.ONE_TO_MANY__FETCH:
				return isSetFetch();
			case OrmPackage.ONE_TO_MANY__MAPPED_BY:
				return MAPPED_BY_EDEFAULT == null ? mappedBy != null : !MAPPED_BY_EDEFAULT.equals(mappedBy);
			case OrmPackage.ONE_TO_MANY__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case OrmPackage.ONE_TO_MANY__ORPHAN_REMOVAL:
				return isSetOrphanRemoval();
			case OrmPackage.ONE_TO_MANY__TARGET_ENTITY:
				return TARGET_ENTITY_EDEFAULT == null ? targetEntity != null : !TARGET_ENTITY_EDEFAULT.equals(targetEntity);
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
		result.append(", access: ");
		if (accessESet) result.append(access); else result.append("<unset>");
		result.append(", fetch: ");
		if (fetchESet) result.append(fetch); else result.append("<unset>");
		result.append(", mappedBy: ");
		result.append(mappedBy);
		result.append(", name: ");
		result.append(name);
		result.append(", orphanRemoval: ");
		if (orphanRemovalESet) result.append(orphanRemoval); else result.append("<unset>");
		result.append(", targetEntity: ");
		result.append(targetEntity);
		result.append(')');
		return result.toString();
	}

} //OneToManyImpl
