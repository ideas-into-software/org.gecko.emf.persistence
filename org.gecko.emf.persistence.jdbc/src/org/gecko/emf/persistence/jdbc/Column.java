/**
 * Copyright (c) 2012 - 2023 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.persistence.jdbc;

/**
 * 
 * @author mark
 * @since 06.01.2023
 */
public class Column {
	
	private String name;
	private String type;
	private boolean primaryKey = false;
	private boolean nullable = false;
	private boolean generateKeys = false;
	private boolean overwriteKeys = false;
	

}
