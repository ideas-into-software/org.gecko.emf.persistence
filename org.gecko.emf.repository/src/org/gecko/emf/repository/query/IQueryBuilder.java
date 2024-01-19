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

import java.util.Collection;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Builder interface to create queries.
 * @author Mark Hoffmann
 * @since 26.07.2017
 */
public interface IQueryBuilder {
  
  public static final int QUERY_TYPE_ALL = 0;
  public static final int QUERY_TYPE_RANGE = 1;
  public static final int QUERY_TYPE_AND = 2;
  public static final int QUERY_TYPE_OR = 3;
  public static final int QUERY_TYPE_KEY_VALUE = 4;
  public static final int QUERY_TYPE_IN = 5;
  
  /**
   * Builds the {@link IQuery} from the given parameters
   * @return the {@link IQuery} from the given parameters
   */
  public IQuery build();
  
  /**
   * Limits the result set to the given values. For values <= 0 the limit means all entries
   * @param maxEntries the number of entries 
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder limit(long maxEntries);
  
  /**
   * Limits the result set to start with given skip'th element. For values <= 0 the skip means all entries from index 0
   * @param skipEntries the number of entries to skip
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder skip(long skipEntries);
  
  /**
   * Provides sorting for the given attribute
   * @param field the attribute to be sorted
   * @param sortType ASCENDING or DESCENDING
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder sort(EAttribute field, SortType sortType);
  
  /**
   * Create a range query
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder rangeQuery();
  
  /**
   * Create a get all query
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder allQuery();
  
  /**
   * Sets the name of the column as {@link EAttribute}
   * @param attribute the column name to set
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder column(EAttribute attribute);
  
  /**
   * Sets the name of the column
   * @param columnName the column name to set
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder column(String columnName);
  
  /**
   * Sets the start index
   * @param startValue the start index
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder startValue(Object startValue);

  /**
   * Sets the start index
   * @param startValue the start index
   * @param include should the value also be part of the range
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder startValue(Object startValue, boolean include);
  
  /**
   * Sets the end index
   * @param endValue the end index
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder endValue(Object endValue);
  /**
   * Sets the end index
   * @param endValue the end index
   * @param include should the value also be part of the range
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder endValue(Object endValue, boolean include);
  
  /**
   * Sets the value
   * @param value the value of this attribute
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder simpleValue(Object value);
  
  /**
   * Sets an AND between two queries
   * @param queries the {@link IQuery} instances
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder and(IQuery...queries);
  
  /**
   * Sets an OR between two queries
   * @param queries the {@link IQuery} instances
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder or(IQuery...queries);
  
  /**
   * Creates a in-query
   * @param valueList list with values
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder in(Collection<Object> valueList);
  
  /**
   * Creates a in-query
   * @param values array with values
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder in(Object... values);
  
  /**
   * Sets a reference path for the projection
   * @param queries the {@link IQuery} instances
   * @return the {@link IQueryBuilder} instance
   */
  public IQueryBuilder projectionPath(EStructuralFeature... referencePath);
  
}
