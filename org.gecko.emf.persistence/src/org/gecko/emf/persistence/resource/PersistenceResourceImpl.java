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

import static org.gecko.emf.persistence.helper.EMFHelper.getEffectiveOptions;
import static org.gecko.emf.persistence.helper.EMFHelper.getResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.gecko.emf.persistence.api.Countable;
import org.gecko.emf.persistence.api.Deletable;
import org.gecko.emf.persistence.api.ExceptionDiagnostic;
import org.gecko.emf.persistence.api.PersistenceException;
import org.gecko.emf.persistence.api.Readable;
import org.gecko.emf.persistence.api.Updateable;
import org.gecko.emf.persistence.engine.PersistenceEngine;
import org.gecko.emf.persistence.helper.ConcurrentHelper;
import org.osgi.util.promise.Promise;
import org.osgi.util.promise.PromiseFactory;

/**
 * Special resource that is able to handle additional persistence methods
 * @author Mark Hoffmann
 * @since 30.05.2022
 */
@SuppressWarnings("unchecked")
public class PersistenceResourceImpl extends ResourceImpl implements PersistenceResource, AsyncPersistenceResource {

	private final ThreadFactory threadFactory = ConcurrentHelper.createThreadFactory("EMFAsync-Resource");
	private final PromiseFactory pf = new PromiseFactory(Executors.newSingleThreadExecutor(threadFactory), Executors.newScheduledThreadPool(2, threadFactory));
	private final Consumer<PersistenceResource> disposeHandler;
	private final PersistenceEngine<?, ?, ?, ?, ?> engine;
	/**
	 * The storage for the default count options.
	 */
	protected Map<Object, Object> defaultCountOptions;
	protected Map<Object, Object> defaultExistOptions;

	/**
	 * Creates a new instance.
	 */
	public PersistenceResourceImpl(Consumer<PersistenceResource> disposeHandler, PersistenceEngine<?, ?, ?, ?, ?> engine, URI uri){
		super(uri);
		this.disposeHandler = disposeHandler;
		this.engine = engine;
	}

	/**
	 * Return the default delete options
	 * @return the delete option map
	 */
	protected Map<Object, Object> getDefaultDeleteOptions() {
		if (defaultDeleteOptions == null) {
			defaultDeleteOptions = new HashMap<Object, Object>();
		}
		return defaultDeleteOptions;
	}

	/**
	 * Return the default count options
	 * @return the count option map
	 */
	protected Map<Object, Object> getDefaultCountOptions() {
		if (defaultCountOptions == null) {
			defaultCountOptions = new HashMap<Object, Object>();
		}
		return defaultCountOptions;
	}

	/**
	 * Return the default exist options
	 * @return the exist option map
	 */
	protected Map<Object, Object> getDefaultExistOptions() {
		if (defaultExistOptions == null) {
			defaultExistOptions = new HashMap<Object, Object>();
		}
		return defaultExistOptions;
	}

	/**
	 * Return the default load options
	 * @return the load option map
	 */
	protected Map<Object, Object> getDefaultLoadOptions() {
		if (defaultLoadOptions == null) {
			defaultLoadOptions = new HashMap<Object, Object>();
		}
		return defaultLoadOptions;
	}

	/**
	 * Return the default save options
	 * @return the save option map
	 */
	protected Map<Object, Object> getDefaultSaveOptions() {
		if (defaultSaveOptions == null) {
			defaultSaveOptions = new HashMap<Object, Object>();
		}
		return defaultSaveOptions;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.PersistenceResource#updateDefaultOptions(java.util.Map, org.gecko.emf.persistence.resource.PersistenceResource.ActionType[])
	 */
	@Override
	public void updateDefaultOptions(Map<Object, Object> options, ActionType... types) {
		if (options == null) {
			return;
		}
//		Map<Object, Object> filtered = filterSupportedOptions(options);
		Objects.requireNonNull(types, "At least one action type must be provided");
		for (ActionType type : types) {
			switch (type) {
			case LOAD:
				getDefaultLoadOptions().putAll(options);
				break;
			case SAVE:
				getDefaultSaveOptions().putAll(options);
				break;
			case COUNT:
				getDefaultCountOptions().putAll(options);
				break;
			case EXIST:
				getDefaultExistOptions().putAll(options);
				break;
			case DELETE:
				getDefaultDeleteOptions().putAll(options);
				break;
			case ALL:
				getDefaultLoadOptions().putAll(options);
				getDefaultSaveOptions().putAll(options);
				getDefaultCountOptions().putAll(options);
				getDefaultExistOptions().putAll(options);
				getDefaultDeleteOptions().putAll(options);
				break;
			default:
				break;
			}
		}
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
		if (engine instanceof Countable) {
			Countable countable = (Countable) engine;
			Map<Object,Object> effectiveOptions = getEffectiveOptions(options, getDefaultCountOptions());
			try {
				isLoading = true;
				if (isLoaded()) {
					return getContents().size();
				}
				long elements = countable.count(effectiveOptions);
				return elements;
			} catch (PersistenceException e) {
				handleErrors(e);
				throw new IOException(e);
			} finally {
				if (!isLoaded()) {
					unload();
					handleResponse(effectiveOptions, true);
					ResourceSet resourceSet = getResourceSet();
					if (resourceSet != null)
					{
						resourceSet.getResources().remove(this);
					}
				}
				isLoading = false;
				setModified(false);
			}
		} else {
			throw new UnsupportedOperationException("The persistence engine does not implement Countable. Count is not available for this engine.");
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.PersistenceResource#exist()
	 */
	@Override
	public boolean exist() throws IOException {
		return exist(Collections.emptyMap());
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.PersistenceResource#exist(java.util.Map)
	 */
	@Override
	public boolean exist(Map<?, ?> options) throws IOException {
		if (engine instanceof Countable) {
			Countable countable = (Countable) engine; 
			Map<Object,Object> effectiveOptions = getEffectiveOptions(options, getDefaultExistOptions());
			try {
				isLoading = true;
				if (isLoaded()) {
					return !getContents().isEmpty();
				}
				boolean exist = countable.exist(effectiveOptions);
				return exist;
			} catch (PersistenceException e) {
				handleErrors(e);
				throw new IOException(e);
			} finally {
				if (!isLoaded()) {
					unload();
					handleResponse(effectiveOptions, true);
					ResourceSet resourceSet = getResourceSet();
					if (resourceSet != null)
					{
						resourceSet.getResources().remove(this);
					}
				}
				setModified(false);
			}
		} else {
			throw new UnsupportedOperationException("The persistence engine does not implement Countable. Exist is not available for this engine.");
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceImpl#load(java.util.Map)
	 */
	@Override
	public void load(Map<?, ?> options) throws IOException {
		doLoad(null, getEffectiveOptions(options, getDefaultLoadOptions()));
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceImpl#doLoad(java.io.InputStream, java.util.Map)
	 */
	@Override
	protected void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException {
		if (engine instanceof Readable) {
			Readable readable = (Readable) engine;
			if (!isLoaded) {
				Notification notification = setLoaded(true);
				isLoading = true;

				if (errors != null) {
					errors.clear();
				}
				if (warnings != null){
					warnings.clear();
				}
				try {
					readable.read((Map<Object, Object>) options);
					handleResponse(options, true);
				} catch (PersistenceException e) {
					handleErrors(e);
				} finally {
					isLoading = false;

					if (notification != null) {
						eNotify(notification);
					}
					setModified(false);
				}
			}
		} else {
			throw new UnsupportedOperationException("The persistence engine does not implement Readable. A load / read is not available for this engine.");
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceImpl#save(java.util.Map)
	 */
	@Override
	public void save(Map<?, ?> options) throws IOException {
		save(null, getEffectiveOptions(options, getDefaultSaveOptions()));
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceImpl#doSave(java.io.OutputStream, java.util.Map)
	 */
	@Override
	protected void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException {
		if (engine instanceof Updateable) {
			Updateable updatable = (Updateable) engine;
			try {
				if (getTimeStamp() == 0) {
					updatable.create((Map<Object, Object>) options);
				} else {
					updatable.update((Map<Object, Object>) options);
				}
				handleResponse(options, false);
			} catch (PersistenceException e) {
				handleErrors(e);
			}
		} else {
			throw new UnsupportedOperationException("The persistence engine does not implement Updatable. A save via create or update is not available for this engine.");
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceImpl#delete(java.util.Map)
	 */
	@Override
	public void delete(Map<?, ?> options) throws IOException {
		if (engine instanceof Deletable) {
			Deletable deletable = (Deletable) engine;
			try {
				isLoading = true;
				Map<Object,Object> effectiveOptions = getEffectiveOptions(options, getDefaultDeleteOptions());
				deletable.delete(effectiveOptions); 
				handleResponse(effectiveOptions, true);
			} catch (PersistenceException e) {
				handleErrors(e);
			} finally {
				unload();
				ResourceSet resourceSet = getResourceSet();
				if (resourceSet != null)
				{
					resourceSet.getResources().remove(this);
				}
				isLoading = false;
				setModified(false);
			}
		} else {
			throw new UnsupportedOperationException("The persistence engine does not implement Deletable. A delete is not available for this engine.");
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceImpl#isLoaded()
	 */
	@Override
	public boolean isLoaded() {
		return super.isLoaded();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#countAsync()
	 */
	@Override
	public Promise<Long> countAsync() {
		return countAsync(Collections.emptyMap());
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#countAsync(java.util.Map)
	 */
	@Override
	public Promise<Long> countAsync(Map<?, ?> options) {
		return pf.submit(()->{
			return count(options);
		});
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#existAsync()
	 */
	@Override
	public Promise<Boolean> existAsync() {
		return existAsync(Collections.emptyMap());
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#existAsync(java.util.Map)
	 */
	@Override
	public Promise<Boolean> existAsync(Map<?, ?> options) {
		return pf.submit(()->{
			return exist(options);
		});
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#loadAsync(java.util.Map)
	 */
	@Override
	public Promise<Map<?, ?>> loadAsync(Map<?, ?> options) {
		return loadAsync(null, options);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#loadAsync(java.io.InputStream, java.util.Map)
	 */
	@Override
	public Promise<Map<?, ?>> loadAsync(InputStream stream, Map<?, ?> options) {
		return pf.submit(()->{
			load(stream, options);
			return getResponse(options);
		});
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#saveAsync(java.util.Map)
	 */
	@Override
	public Promise<Map<?, ?>> saveAsync(Map<?, ?> options) {
		return saveAsync(null, options);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#saveAsync(java.io.OutputStream, java.util.Map)
	 */
	@Override
	public Promise<Map<?, ?>> saveAsync(OutputStream stream, Map<?, ?> options) {
		return pf.submit(()->{
			save(stream, options);
			return getResponse(options);
		});
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.AsyncPersistenceResource#deleteAsync(java.util.Map)
	 */
	@Override
	public Promise<Map<?, ?>> deleteAsync(Map<?, ?> options) {
		return pf.submit(()->{
			delete(options);
			return getResponse(options);
		});
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.resource.PersistenceResource#getEngine()
	 */
	@Override
	public PersistenceEngine<?, ?, ?, ?, ?> getEngine() {
		return engine;
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		if (disposeHandler != null) {
			disposeHandler.accept(this);
		}
	}

	/**
	 * Handles the persistence errors
	 * @param t the {@link Throwable}
	 */
	private void handleErrors(Throwable t) {
		getErrors().add(new ExceptionDiagnostic(t, getURI().toString()));
	}

	/**
	 * Handles the response data from the request
	 * @param options the options map, to get the response from
	 * @param isRead <code>true</code> for a read operation, <code>false</code>, for a write operation
	 */
	private void handleResponse(Map<?, ?> options, boolean isRead) {
		Map<Object, Object> response = getResponse(options);
		List<Diagnostic> warnings = (List<Diagnostic>) response.get("PERSISTENCE_WARNINGS");
		if (warnings != null) {
			getWarnings().addAll(warnings);
		}
		if (isRead) {
			handleLoadResponse(response, options);
		} else {
			handleSaveResponse(response, options);
		}
	}

}
