/*
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.gecko.emf.persistence.jpa.orm.model.orm.Basic;
import org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection;
import org.gecko.emf.persistence.jpa.orm.model.orm.EmbeddableAttributes;
import org.gecko.emf.persistence.jpa.orm.model.orm.Embedded;
import org.gecko.emf.persistence.jpa.orm.model.orm.ManyToMany;
import org.gecko.emf.persistence.jpa.orm.model.orm.ManyToOne;
import org.gecko.emf.persistence.jpa.orm.model.orm.OneToMany;
import org.gecko.emf.persistence.jpa.orm.model.orm.OneToOne;
import org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage;
import org.gecko.emf.persistence.jpa.orm.model.orm.Transient;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Embeddable Attributes</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.EmbeddableAttributesImpl#getBasic <em>Basic</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.EmbeddableAttributesImpl#getManyToOne <em>Many To One</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.EmbeddableAttributesImpl#getOneToMany <em>One To Many</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.EmbeddableAttributesImpl#getOneToOne <em>One To One</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.EmbeddableAttributesImpl#getManyToMany <em>Many To Many</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.EmbeddableAttributesImpl#getElementCollection <em>Element Collection</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.EmbeddableAttributesImpl#getEmbedded <em>Embedded</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.impl.EmbeddableAttributesImpl#getTransient <em>Transient</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EmbeddableAttributesImpl extends MinimalEObjectImpl.Container implements EmbeddableAttributes {
	/**
	 * The cached value of the '{@link #getBasic() <em>Basic</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBasic()
	 * @generated
	 * @ordered
	 */
	protected EList<Basic> basic;

	/**
	 * The cached value of the '{@link #getManyToOne() <em>Many To One</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getManyToOne()
	 * @generated
	 * @ordered
	 */
	protected EList<ManyToOne> manyToOne;

	/**
	 * The cached value of the '{@link #getOneToMany() <em>One To Many</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOneToMany()
	 * @generated
	 * @ordered
	 */
	protected EList<OneToMany> oneToMany;

	/**
	 * The cached value of the '{@link #getOneToOne() <em>One To One</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOneToOne()
	 * @generated
	 * @ordered
	 */
	protected EList<OneToOne> oneToOne;

	/**
	 * The cached value of the '{@link #getManyToMany() <em>Many To Many</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getManyToMany()
	 * @generated
	 * @ordered
	 */
	protected EList<ManyToMany> manyToMany;

	/**
	 * The cached value of the '{@link #getElementCollection() <em>Element Collection</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElementCollection()
	 * @generated
	 * @ordered
	 */
	protected EList<ElementCollection> elementCollection;

	/**
	 * The cached value of the '{@link #getEmbedded() <em>Embedded</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmbedded()
	 * @generated
	 * @ordered
	 */
	protected EList<Embedded> embedded;

	/**
	 * The cached value of the '{@link #getTransient() <em>Transient</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransient()
	 * @generated
	 * @ordered
	 */
	protected EList<Transient> transient_;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EmbeddableAttributesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OrmPackage.eINSTANCE.getEmbeddableAttributes();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Basic> getBasic() {
		if (basic == null) {
			basic = new EObjectContainmentEList<Basic>(Basic.class, this, OrmPackage.EMBEDDABLE_ATTRIBUTES__BASIC);
		}
		return basic;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ManyToOne> getManyToOne() {
		if (manyToOne == null) {
			manyToOne = new EObjectContainmentEList<ManyToOne>(ManyToOne.class, this, OrmPackage.EMBEDDABLE_ATTRIBUTES__MANY_TO_ONE);
		}
		return manyToOne;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<OneToMany> getOneToMany() {
		if (oneToMany == null) {
			oneToMany = new EObjectContainmentEList<OneToMany>(OneToMany.class, this, OrmPackage.EMBEDDABLE_ATTRIBUTES__ONE_TO_MANY);
		}
		return oneToMany;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<OneToOne> getOneToOne() {
		if (oneToOne == null) {
			oneToOne = new EObjectContainmentEList<OneToOne>(OneToOne.class, this, OrmPackage.EMBEDDABLE_ATTRIBUTES__ONE_TO_ONE);
		}
		return oneToOne;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ManyToMany> getManyToMany() {
		if (manyToMany == null) {
			manyToMany = new EObjectContainmentEList<ManyToMany>(ManyToMany.class, this, OrmPackage.EMBEDDABLE_ATTRIBUTES__MANY_TO_MANY);
		}
		return manyToMany;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ElementCollection> getElementCollection() {
		if (elementCollection == null) {
			elementCollection = new EObjectContainmentEList<ElementCollection>(ElementCollection.class, this, OrmPackage.EMBEDDABLE_ATTRIBUTES__ELEMENT_COLLECTION);
		}
		return elementCollection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Embedded> getEmbedded() {
		if (embedded == null) {
			embedded = new EObjectContainmentEList<Embedded>(Embedded.class, this, OrmPackage.EMBEDDABLE_ATTRIBUTES__EMBEDDED);
		}
		return embedded;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Transient> getTransient() {
		if (transient_ == null) {
			transient_ = new EObjectContainmentEList<Transient>(Transient.class, this, OrmPackage.EMBEDDABLE_ATTRIBUTES__TRANSIENT);
		}
		return transient_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__BASIC:
				return ((InternalEList<?>)getBasic()).basicRemove(otherEnd, msgs);
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__MANY_TO_ONE:
				return ((InternalEList<?>)getManyToOne()).basicRemove(otherEnd, msgs);
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__ONE_TO_MANY:
				return ((InternalEList<?>)getOneToMany()).basicRemove(otherEnd, msgs);
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__ONE_TO_ONE:
				return ((InternalEList<?>)getOneToOne()).basicRemove(otherEnd, msgs);
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__MANY_TO_MANY:
				return ((InternalEList<?>)getManyToMany()).basicRemove(otherEnd, msgs);
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__ELEMENT_COLLECTION:
				return ((InternalEList<?>)getElementCollection()).basicRemove(otherEnd, msgs);
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__EMBEDDED:
				return ((InternalEList<?>)getEmbedded()).basicRemove(otherEnd, msgs);
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__TRANSIENT:
				return ((InternalEList<?>)getTransient()).basicRemove(otherEnd, msgs);
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
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__BASIC:
				return getBasic();
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__MANY_TO_ONE:
				return getManyToOne();
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__ONE_TO_MANY:
				return getOneToMany();
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__ONE_TO_ONE:
				return getOneToOne();
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__MANY_TO_MANY:
				return getManyToMany();
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__ELEMENT_COLLECTION:
				return getElementCollection();
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__EMBEDDED:
				return getEmbedded();
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__TRANSIENT:
				return getTransient();
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
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__BASIC:
				getBasic().clear();
				getBasic().addAll((Collection<? extends Basic>)newValue);
				return;
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__MANY_TO_ONE:
				getManyToOne().clear();
				getManyToOne().addAll((Collection<? extends ManyToOne>)newValue);
				return;
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__ONE_TO_MANY:
				getOneToMany().clear();
				getOneToMany().addAll((Collection<? extends OneToMany>)newValue);
				return;
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__ONE_TO_ONE:
				getOneToOne().clear();
				getOneToOne().addAll((Collection<? extends OneToOne>)newValue);
				return;
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__MANY_TO_MANY:
				getManyToMany().clear();
				getManyToMany().addAll((Collection<? extends ManyToMany>)newValue);
				return;
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__ELEMENT_COLLECTION:
				getElementCollection().clear();
				getElementCollection().addAll((Collection<? extends ElementCollection>)newValue);
				return;
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__EMBEDDED:
				getEmbedded().clear();
				getEmbedded().addAll((Collection<? extends Embedded>)newValue);
				return;
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__TRANSIENT:
				getTransient().clear();
				getTransient().addAll((Collection<? extends Transient>)newValue);
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
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__BASIC:
				getBasic().clear();
				return;
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__MANY_TO_ONE:
				getManyToOne().clear();
				return;
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__ONE_TO_MANY:
				getOneToMany().clear();
				return;
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__ONE_TO_ONE:
				getOneToOne().clear();
				return;
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__MANY_TO_MANY:
				getManyToMany().clear();
				return;
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__ELEMENT_COLLECTION:
				getElementCollection().clear();
				return;
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__EMBEDDED:
				getEmbedded().clear();
				return;
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__TRANSIENT:
				getTransient().clear();
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
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__BASIC:
				return basic != null && !basic.isEmpty();
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__MANY_TO_ONE:
				return manyToOne != null && !manyToOne.isEmpty();
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__ONE_TO_MANY:
				return oneToMany != null && !oneToMany.isEmpty();
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__ONE_TO_ONE:
				return oneToOne != null && !oneToOne.isEmpty();
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__MANY_TO_MANY:
				return manyToMany != null && !manyToMany.isEmpty();
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__ELEMENT_COLLECTION:
				return elementCollection != null && !elementCollection.isEmpty();
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__EMBEDDED:
				return embedded != null && !embedded.isEmpty();
			case OrmPackage.EMBEDDABLE_ATTRIBUTES__TRANSIENT:
				return transient_ != null && !transient_.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //EmbeddableAttributesImpl
