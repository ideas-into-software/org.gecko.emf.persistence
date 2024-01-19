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
package org.gecko.emf.repository.file.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.gecko.emf.repository.RepositoryConstants;
import org.osgi.annotation.bundle.Requirement;
import org.osgi.service.cm.annotations.RequireConfigurationAdmin;

/**
 * 
 * @author jalbert
 * @since 15 Feb 2018
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({
		ElementType.TYPE, ElementType.PACKAGE
})
@RequireConfigurationAdmin
@Requirement(namespace = RepositoryConstants.CAPABILITY_NAMESPACE, filter="(" + RepositoryConstants.CAPABILITY_NAMESPACE + "=file)")
public @interface RequireFileEMFRepository {

}
