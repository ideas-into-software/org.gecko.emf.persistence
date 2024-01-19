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
 * Abstract operator query.
 * @author Mark Hoffmann
 * @since 26.07.2017
 */
public abstract class OperatorQuery extends AbstractQuery {
  
  private final OperatorType type;
  private final IQuery[] queries;

  public enum OperatorType {
    AND,
    OR,
    NOT
  }
  
  public OperatorQuery(OperatorType type, List<EStructuralFeature[]> projectionPaths, IQuery... queries) {
	super(projectionPaths);
    this.type = type;
    this.queries = queries;
  }

  public OperatorType getType() {
    return type;
  }

  public IQuery[] getQueries() {
    return queries;
  }

}
