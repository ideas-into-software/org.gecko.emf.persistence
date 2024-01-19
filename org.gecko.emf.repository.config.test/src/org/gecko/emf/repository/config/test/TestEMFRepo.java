/**
 * Copyright (c) 2012 - 2024 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.repository.config.test;

import org.gecko.emf.repository.EMFRepository;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * 
 * @author mark
 * @since 19.01.2024
 */
@Component
public class TestEMFRepo {
	
	@Reference
	private EMFRepository repo;

	@Activate
	private void activate() {
//		System.out.println("REPO_ ");
		System.out.println("REPO_ " + repo.getId());
	}
}
