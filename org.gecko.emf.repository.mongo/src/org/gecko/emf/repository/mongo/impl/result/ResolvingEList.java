/**
 * Copyright (c) 2012 - 2018 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.repository.mongo.impl.result;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * A List that only requires a ResourceSet to resolve its content. This List is required to enable a cleanup of our query resources 
 * @author Juergen Albert
 * @since 16 Aug 2018
 */
public class ResolvingEList<E> extends EcoreEList<E> {

	/** serialVersionUID */
	private static final long serialVersionUID = -7394308950476605774L;
	private ResourceSet resourceSet;

	/**
	 * Creates a new instance.
	 * 
	 * @param dataClass
	 * @param owner
	 */
	public ResolvingEList(Class<?> dataClass, InternalEObject owner, ResourceSet set) {
		super(dataClass, owner);
		this.resourceSet = set;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreEList#resolveProxy(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected EObject resolveProxy(EObject eObject) {
		// TODO Auto-generated method stub
		return eObject.eIsProxy() ? EcoreUtil.resolve(eObject, resourceSet) : eObject;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreEList#hasProxies()
	 */
	@Override
	protected boolean hasProxies() {
		return true;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.common.util.AbstractEList#useEquals()
	 */
	@Override
	protected boolean useEquals() {
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.common.util.AbstractEList#isUnique()
	 */
	@Override
	protected boolean isUnique() {
		return true;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#hasInverse()
	 */
	@Override
	protected boolean hasInverse() {
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreEList#isEObject()
	 */
	@Override
	protected boolean isEObject() {
		return true;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#canContainNull()
	 */
	@Override
	protected boolean canContainNull() {
		return false;
	}

}
