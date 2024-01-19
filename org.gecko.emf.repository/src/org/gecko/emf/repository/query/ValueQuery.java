/**
 * Copyright (c) 2014 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.repository.query;

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Abstract key value query.
 * @author Mark Hoffmann
 * @since 26.07.2017
 */
public abstract class ValueQuery extends AbstractQuery {
  
  private final Object value;
  private final String key;

  public ValueQuery(String key, Object value, List<EStructuralFeature[]> projectionPaths) {
    super(projectionPaths);
	this.key = key;
    this.value = value;
  }

  public Object getValue() {
    return value;
  }

  public String getKey() {
    return key;
  }

}
