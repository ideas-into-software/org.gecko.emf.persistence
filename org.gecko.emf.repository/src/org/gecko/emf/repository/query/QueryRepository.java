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
package org.gecko.emf.repository.query;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * Interface for a repository that supports {@link IQuery}
 * @author Mark Hoffmann
 * @since 12.04.2015
 */
public interface QueryRepository {
  
  /**
   * Creates a query builder
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder createQueryBuilder();
  
  
  /**
   * Returns a list of all {@link EObject} of the given {@link EClass} that match the given {@link IQuery} or an empty {@link List}
   * @param eClass the {@link EClass} of the objects to return
   * @param query the {@link IQuery}
   * @return the list with objects or an empty {@link List}
   * @throws EMFRepositoryException thrown when an error during getting all objects occur
   */
  public <T extends EObject> List<T> getEObjectsByQuery(EClass eClass, IQuery query);


  /**
   * Returns a list of all {@link EObject} of the given {@link EClass} that match the given {@link IQuery} or an empty {@link List}
   * @param eClass the {@link EClass} of the objects to return
   * @param query the {@link IQuery}
   * @param options the Resource Load options
   * @return the list with objects or an empty {@link List}
   * @throws EMFRepositoryException thrown when an error during getting all objects occur
   */
  public <T extends EObject> List<T> getEObjectsByQuery(EClass eClass, IQuery query, Map<Object, Object> options);

  /**
   * Returns single {@link EObject} of the given {@link EClass} that match the given {@link IQuery} or null. 
   * If more then one Objects matches the query the first object will be returned.
   * @param eClass the {@link EClass} of the objects to return
   * @param query the {@link IQuery}
   * @param options the Resource Load options
   * @return The {@link EObject} desired or null
   * @throws EMFRepositoryException thrown when an error during getting all objects occur
   */
  public <T extends EObject> T getEObjectByQuery(EClass eClass, IQuery query, Map<Object, Object> options);
  
  /**
   * Counts the potential {@link EObject} of the given {@link EClass} that match the given {@link IQuery} or -1. 
   * @param eClass the {@link EClass} of the objects to return
   * @param query the {@link IQuery}
   * @param options the Resource Load options
   * @return The number of potential {@link EObject}
   * @throws EMFRepositoryException thrown when an error during getting all objects occur
   */
  public long count(EClass eClass, IQuery query, Map<Object, Object> options);
  
  /**
   * Counts the potential {@link EObject} of the given {@link EClass} that match the given {@link IQuery} or -1. 
   * @param eClass the {@link EClass} of the objects to return
   * @param query the {@link IQuery}
   * @return The number of potential {@link EObject}
   * @throws EMFRepositoryException thrown when an error during getting all objects occur
   */
  public long count(EClass eClass, IQuery query);

}
