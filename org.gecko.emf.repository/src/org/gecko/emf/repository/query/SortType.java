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

/**
 * Query sort type enum.
 *
 * @author Mark Hoffmann
 * @since 26.07.2017
 */
public enum SortType {
  
  DESCENDING(-1),
  ASCENDING(1);
  
  private int value;
  
  private SortType(int value) {
    this.value = value;
  }
  
  public int getValue() {
    return value;
  }
  

}
