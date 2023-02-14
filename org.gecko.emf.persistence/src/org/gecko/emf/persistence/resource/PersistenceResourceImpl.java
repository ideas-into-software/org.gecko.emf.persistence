/**
 * Copyright (c) 2012 - 2022 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.gecko.emf.persistence.helper.ConcurrentHelper;
import org.osgi.util.promise.Promise;
import org.osgi.util.promise.PromiseFactory;

/**
 * Special resource that is able to handle additional persistence methods
 * @author Mark Hoffmann
 * @since 30.05.2022
 */
public class PersistenceResourceImpl extends ResourceImpl implements PersistenceResource, AsyncPersistenceResource {

	private final ThreadFactory threadFactory = ConcurrentHelper.createThreadFactory("EMFAsync-Resource");
	private final PromiseFactory pf = new PromiseFactory(Executors.newSingleThreadExecutor(threadFactory), Executors.newScheduledThreadPool(2, threadFactory));
	/**
	 * The storage for the default count options.
	 */
	protected Map<Object, Object> defaultCountOptions;
	protected Map<Object, Object> defaultExistOptions;

	/**
	 * Return the default delete options
	 * @return the delete option map
	 */
	public Map<Object, Object> getDefaultDeleteOptions() {
		if (defaultDeleteOptions == null) {
			defaultDeleteOptions = new HashMap<Object, Object>();
		}
		return defaultDeleteOptions;
	}

	/**
	 * Return the default count options
	 * @return the count option map
	 */
	public Map<Object, Object> getDefaultCountOptions() {
		if (defaultCountOptions == null) {
			defaultCountOptions = new HashMap<Object, Object>();
		}
		return defaultCountOptions;
	}
	
	/**
	 * Return the default exist options
	 * @return the exist option map
	 */
	public Map<Object, Object> getDefaultExistOptions() {
		if (defaultExistOptions == null) {
			defaultExistOptions = new HashMap<Object, Object>();
		}
		return defaultExistOptions;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.PersistenceResource#count()
	 */
	@Override
	public long count() throws IOException {
		return count(null);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.PersistenceResource#count(java.util.Map)
	 */
	@Override
	public long count(Map<?, ?> options) throws IOException  {
		org.eclipse.emf.ecore.resource.URIHandler uriHandler = getURIConverter().getURIHandler(getURI());
		if (uriHandler instanceof PersistenceURIHandler) {
			PersistenceURIHandler persistenceUriHandler = (PersistenceURIHandler) uriHandler;
			try {
				return persistenceUriHandler.count(getURI(), mergeMaps(options, defaultCountOptions));
			} finally {
				unload();
				ResourceSet resourceSet = getResourceSet();
				if (resourceSet != null)
				{
					resourceSet.getResources().remove(this);
				}

			}
		}
		throw new IOException("No PersistenceURIHandler found, that can handle the resources' URI");
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.PersistenceResource#exist()
	 */
	@Override
	public boolean exist() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.PersistenceResource#exist(java.util.Map)
	 */
	@Override
	public boolean exist(Map<?, ?> options) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceImpl#doLoad(java.io.InputStream, java.util.Map)
	 */
	@Override
	protected void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException {
		// TODO Auto-generated method stub
		super.doLoad(inputStream, options);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceImpl#doSave(java.io.OutputStream, java.util.Map)
	 */
	@Override
	protected void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException {
		// TODO Auto-generated method stub
		super.doSave(outputStream, options);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceImpl#delete(java.util.Map)
	 */
	@Override
	public void delete(Map<?, ?> options) throws IOException {
		// TODO Auto-generated method stub
		super.delete(options);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceImpl#isLoaded()
	 */
	@Override
	public boolean isLoaded() {
		// TODO Auto-generated method stub
		return super.isLoaded();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#countAsync()
	 */
	@Override
	public Promise<Long> countAsync() {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#countAsync(java.util.Map)
	 */
	@Override
	public Promise<Long> countAsync(Map<?, ?> options) {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#existAsync()
	 */
	@Override
	public Promise<Boolean> existAsync() {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#existAsync(java.util.Map)
	 */
	@Override
	public Promise<Boolean> existAsync(Map<?, ?> options) {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#loadAsync(java.util.Map)
	 */
	@Override
	public Promise<Map<String, ?>> loadAsync(Map<?, ?> options) {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#loadAsync(java.io.InputStream, java.util.Map)
	 */
	@Override
	public Promise<Map<String, ?>> loadAsync(InputStream stream, Map<?, ?> options) {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#saveAsync(java.util.Map)
	 */
	@Override
	public Promise<Map<String, ?>> saveAsync(Map<?, ?> options) {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#saveAsync(java.io.OutputStream, java.util.Map)
	 */
	@Override
	public Promise<Map<String, ?>> saveAsync(OutputStream stream, Map<?, ?> options) {
		return pf.submit(()->{
			save(stream, options);
			return Collections.emptyMap();
		});
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#deleteAsync(java.util.Map)
	 */
	@Override
	public Promise<Map<String, ?>> deleteAsync(Map<?, ?> options) {
		return pf.submit(()->{
			delete(options);
			return Collections.emptyMap();
		});
	}

}
