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

import org.bson.BsonDocument;
import org.bson.Document;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EMongo Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.model.mongo.EMongoQuery#getFilter <em>Filter</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.model.mongo.EMongoQuery#getProjection <em>Projection</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.model.mongo.EMongoQuery#getProjectionOnly <em>Projection Only</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.model.mongo.EMongoQuery#getSort <em>Sort</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.model.mongo.EMongoQuery#getSkip <em>Skip</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.model.mongo.EMongoQuery#getLimit <em>Limit</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.model.mongo.EMongoQuery#getBatchSize <em>Batch Size</em>}</li>
 * </ul>
 *
 * @see org.gecko.emf.persistence.model.mongo.MongoPackage#getEMongoQuery()
 * @model kind="class"
 * @generated
 */
public class EMongoQuery extends MinimalEObjectImpl.Container implements EObject {
	/**
	 * The default value of the '{@link #getFilter() <em>Filter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFilter()
	 * @generated
	 * @ordered
	 */
	protected static final BsonDocument FILTER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFilter() <em>Filter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFilter()
	 * @generated
	 * @ordered
	 */
	protected BsonDocument filter = FILTER_EDEFAULT;

	/**
	 * The default value of the '{@link #getProjection() <em>Projection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProjection()
	 * @generated
	 * @ordered
	 */
	protected static final Document PROJECTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProjection() <em>Projection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProjection()
	 * @generated
	 * @ordered
	 */
	protected Document projection = PROJECTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getProjectionOnly() <em>Projection Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProjectionOnly()
	 * @generated
	 * @ordered
	 */
	protected static final Document PROJECTION_ONLY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProjectionOnly() <em>Projection Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProjectionOnly()
	 * @generated
	 * @ordered
	 */
	protected Document projectionOnly = PROJECTION_ONLY_EDEFAULT;

	/**
	 * The default value of the '{@link #getSort() <em>Sort</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSort()
	 * @generated
	 * @ordered
	 */
	protected static final Document SORT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSort() <em>Sort</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSort()
	 * @generated
	 * @ordered
	 */
	protected Document sort = SORT_EDEFAULT;

	/**
	 * The default value of the '{@link #getSkip() <em>Skip</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSkip()
	 * @generated
	 * @ordered
	 */
	protected static final Integer SKIP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSkip() <em>Skip</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSkip()
	 * @generated
	 * @ordered
	 */
	protected Integer skip = SKIP_EDEFAULT;

	/**
	 * The default value of the '{@link #getLimit() <em>Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLimit()
	 * @generated
	 * @ordered
	 */
	protected static final Integer LIMIT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLimit() <em>Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLimit()
	 * @generated
	 * @ordered
	 */
	protected Integer limit = LIMIT_EDEFAULT;

	/**
	 * The default value of the '{@link #getBatchSize() <em>Batch Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBatchSize()
	 * @generated
	 * @ordered
	 */
	protected static final Integer BATCH_SIZE_EDEFAULT = Integer.valueOf(1000);

	/**
	 * The cached value of the '{@link #getBatchSize() <em>Batch Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBatchSize()
	 * @generated
	 * @ordered
	 */
	protected Integer batchSize = BATCH_SIZE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EMongoQuery() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MongoPackage.Literals.EMONGO_QUERY;
	}

	/**
	 * Returns the value of the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Filter</em>' attribute.
	 * @see #setFilter(BsonDocument)
	 * @see org.gecko.emf.persistence.model.mongo.MongoPackage#getEMongoQuery_Filter()
	 * @model dataType="org.gecko.emf.persistence.model.mongo.EBsonDocument"
	 * @generated
	 */
	public BsonDocument getFilter() {
		return filter;
	}

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.model.mongo.EMongoQuery#getFilter <em>Filter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param newFilter the new value of the '<em>Filter</em>' attribute.
	 * @see #getFilter()
	 * @generated
	 */
	public void setFilter(BsonDocument newFilter) {
		BsonDocument oldFilter = filter;
		filter = newFilter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MongoPackage.EMONGO_QUERY__FILTER, oldFilter, filter));
	}

	/**
	 * Returns the value of the '<em><b>Projection</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Projection</em>' attribute.
	 * @see #setProjection(Document)
	 * @see org.gecko.emf.persistence.model.mongo.MongoPackage#getEMongoQuery_Projection()
	 * @model dataType="org.gecko.emf.persistence.model.mongo.EDocument"
	 * @generated
	 */
	public Document getProjection() {
		return projection;
	}

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.model.mongo.EMongoQuery#getProjection <em>Projection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param newProjection the new value of the '<em>Projection</em>' attribute.
	 * @see #getProjection()
	 * @generated
	 */
	public void setProjection(Document newProjection) {
		Document oldProjection = projection;
		projection = newProjection;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MongoPackage.EMONGO_QUERY__PROJECTION, oldProjection, projection));
	}

	/**
	 * Returns the value of the '<em><b>Projection Only</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Projection Only</em>' attribute.
	 * @see #setProjectionOnly(Document)
	 * @see org.gecko.emf.persistence.model.mongo.MongoPackage#getEMongoQuery_ProjectionOnly()
	 * @model dataType="org.gecko.emf.persistence.model.mongo.EDocument"
	 * @generated
	 */
	public Document getProjectionOnly() {
		return projectionOnly;
	}

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.model.mongo.EMongoQuery#getProjectionOnly <em>Projection Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param newProjectionOnly the new value of the '<em>Projection Only</em>' attribute.
	 * @see #getProjectionOnly()
	 * @generated
	 */
	public void setProjectionOnly(Document newProjectionOnly) {
		Document oldProjectionOnly = projectionOnly;
		projectionOnly = newProjectionOnly;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MongoPackage.EMONGO_QUERY__PROJECTION_ONLY, oldProjectionOnly, projectionOnly));
	}

	/**
	 * Returns the value of the '<em><b>Sort</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sort</em>' attribute.
	 * @see #setSort(Document)
	 * @see org.gecko.emf.persistence.model.mongo.MongoPackage#getEMongoQuery_Sort()
	 * @model dataType="org.gecko.emf.persistence.model.mongo.EDocument"
	 * @generated
	 */
	public Document getSort() {
		return sort;
	}

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.model.mongo.EMongoQuery#getSort <em>Sort</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param newSort the new value of the '<em>Sort</em>' attribute.
	 * @see #getSort()
	 * @generated
	 */
	public void setSort(Document newSort) {
		Document oldSort = sort;
		sort = newSort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MongoPackage.EMONGO_QUERY__SORT, oldSort, sort));
	}

	/**
	 * Returns the value of the '<em><b>Skip</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Skip</em>' attribute.
	 * @see #setSkip(Integer)
	 * @see org.gecko.emf.persistence.model.mongo.MongoPackage#getEMongoQuery_Skip()
	 * @model
	 * @generated
	 */
	public Integer getSkip() {
		return skip;
	}

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.model.mongo.EMongoQuery#getSkip <em>Skip</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param newSkip the new value of the '<em>Skip</em>' attribute.
	 * @see #getSkip()
	 * @generated
	 */
	public void setSkip(Integer newSkip) {
		Integer oldSkip = skip;
		skip = newSkip;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MongoPackage.EMONGO_QUERY__SKIP, oldSkip, skip));
	}

	/**
	 * Returns the value of the '<em><b>Limit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Limit</em>' attribute.
	 * @see #setLimit(Integer)
	 * @see org.gecko.emf.persistence.model.mongo.MongoPackage#getEMongoQuery_Limit()
	 * @model
	 * @generated
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.model.mongo.EMongoQuery#getLimit <em>Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param newLimit the new value of the '<em>Limit</em>' attribute.
	 * @see #getLimit()
	 * @generated
	 */
	public void setLimit(Integer newLimit) {
		Integer oldLimit = limit;
		limit = newLimit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MongoPackage.EMONGO_QUERY__LIMIT, oldLimit, limit));
	}

	/**
	 * Returns the value of the '<em><b>Batch Size</b></em>' attribute.
	 * The default value is <code>"1000"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Batch Size</em>' attribute.
	 * @see #setBatchSize(Integer)
	 * @see org.gecko.emf.persistence.model.mongo.MongoPackage#getEMongoQuery_BatchSize()
	 * @model default="1000"
	 * @generated
	 */
	public Integer getBatchSize() {
		return batchSize;
	}

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.model.mongo.EMongoQuery#getBatchSize <em>Batch Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param newBatchSize the new value of the '<em>Batch Size</em>' attribute.
	 * @see #getBatchSize()
	 * @generated
	 */
	public void setBatchSize(Integer newBatchSize) {
		Integer oldBatchSize = batchSize;
		batchSize = newBatchSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MongoPackage.EMONGO_QUERY__BATCH_SIZE, oldBatchSize, batchSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MongoPackage.EMONGO_QUERY__FILTER:
				return getFilter();
			case MongoPackage.EMONGO_QUERY__PROJECTION:
				return getProjection();
			case MongoPackage.EMONGO_QUERY__PROJECTION_ONLY:
				return getProjectionOnly();
			case MongoPackage.EMONGO_QUERY__SORT:
				return getSort();
			case MongoPackage.EMONGO_QUERY__SKIP:
				return getSkip();
			case MongoPackage.EMONGO_QUERY__LIMIT:
				return getLimit();
			case MongoPackage.EMONGO_QUERY__BATCH_SIZE:
				return getBatchSize();
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
			case MongoPackage.EMONGO_QUERY__FILTER:
				setFilter((BsonDocument)newValue);
				return;
			case MongoPackage.EMONGO_QUERY__PROJECTION:
				setProjection((Document)newValue);
				return;
			case MongoPackage.EMONGO_QUERY__PROJECTION_ONLY:
				setProjectionOnly((Document)newValue);
				return;
			case MongoPackage.EMONGO_QUERY__SORT:
				setSort((Document)newValue);
				return;
			case MongoPackage.EMONGO_QUERY__SKIP:
				setSkip((Integer)newValue);
				return;
			case MongoPackage.EMONGO_QUERY__LIMIT:
				setLimit((Integer)newValue);
				return;
			case MongoPackage.EMONGO_QUERY__BATCH_SIZE:
				setBatchSize((Integer)newValue);
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
			case MongoPackage.EMONGO_QUERY__FILTER:
				setFilter(FILTER_EDEFAULT);
				return;
			case MongoPackage.EMONGO_QUERY__PROJECTION:
				setProjection(PROJECTION_EDEFAULT);
				return;
			case MongoPackage.EMONGO_QUERY__PROJECTION_ONLY:
				setProjectionOnly(PROJECTION_ONLY_EDEFAULT);
				return;
			case MongoPackage.EMONGO_QUERY__SORT:
				setSort(SORT_EDEFAULT);
				return;
			case MongoPackage.EMONGO_QUERY__SKIP:
				setSkip(SKIP_EDEFAULT);
				return;
			case MongoPackage.EMONGO_QUERY__LIMIT:
				setLimit(LIMIT_EDEFAULT);
				return;
			case MongoPackage.EMONGO_QUERY__BATCH_SIZE:
				setBatchSize(BATCH_SIZE_EDEFAULT);
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
			case MongoPackage.EMONGO_QUERY__FILTER:
				return FILTER_EDEFAULT == null ? filter != null : !FILTER_EDEFAULT.equals(filter);
			case MongoPackage.EMONGO_QUERY__PROJECTION:
				return PROJECTION_EDEFAULT == null ? projection != null : !PROJECTION_EDEFAULT.equals(projection);
			case MongoPackage.EMONGO_QUERY__PROJECTION_ONLY:
				return PROJECTION_ONLY_EDEFAULT == null ? projectionOnly != null : !PROJECTION_ONLY_EDEFAULT.equals(projectionOnly);
			case MongoPackage.EMONGO_QUERY__SORT:
				return SORT_EDEFAULT == null ? sort != null : !SORT_EDEFAULT.equals(sort);
			case MongoPackage.EMONGO_QUERY__SKIP:
				return SKIP_EDEFAULT == null ? skip != null : !SKIP_EDEFAULT.equals(skip);
			case MongoPackage.EMONGO_QUERY__LIMIT:
				return LIMIT_EDEFAULT == null ? limit != null : !LIMIT_EDEFAULT.equals(limit);
			case MongoPackage.EMONGO_QUERY__BATCH_SIZE:
				return BATCH_SIZE_EDEFAULT == null ? batchSize != null : !BATCH_SIZE_EDEFAULT.equals(batchSize);
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
		result.append(" (filter: ");
		result.append(filter);
		result.append(", projection: ");
		result.append(projection);
		result.append(", projectionOnly: ");
		result.append(projectionOnly);
		result.append(", sort: ");
		result.append(sort);
		result.append(", skip: ");
		result.append(skip);
		result.append(", limit: ");
		result.append(limit);
		result.append(", batchSize: ");
		result.append(batchSize);
		result.append(')');
		return result.toString();
	}

} // EMongoQuery
