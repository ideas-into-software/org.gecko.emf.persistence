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
 * Abstract range query implementation.
 * @author Mark Hoffmann
 * @since 26.07.2017
 */
public abstract class RangeQuery extends AbstractQuery {
  
  private final String column;
  private final Object startValue;
  private final boolean includeStartValue;
  private final Object endValue;
  private final boolean includeEndValue;
  
  public RangeQuery(String column, Object startValue, Object endValue, boolean includeStartValue, boolean includeEndValue, List<EStructuralFeature[]> projectionPaths) {
    super(projectionPaths);
	this.column = column;
    this.startValue = startValue;
    this.endValue = endValue;
	this.includeStartValue = includeStartValue;
	this.includeEndValue = includeEndValue;
  }

  public Object getStartValue() {
    return startValue;
  }

  public Object getEndValue() {
    return endValue;
  }

  public String getColumn() {
    return column;
  }

  public boolean isIncludeStartValue() {
	return includeStartValue;
  }

  public boolean isIncludeEndValue() {
	return includeEndValue;
  }
}
