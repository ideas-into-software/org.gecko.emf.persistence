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
public class Index {
	
	private String name;
	private String tableName;
	private String[] columns;
	private boolean unique = false;
	private boolean composite = false;

	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	public String create() {
		return null;
	}
	public String show() {
		return null;
	}
	public String drop() {
		return null;
	}
	/**
	 * Returns the tableName.
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * Sets the tableName.
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * Returns the columns.
	 * @return the columns
	 */
	public String[] getColumns() {
		return columns;
	}
	/**
	 * Sets the columns.
	 * @param columns the columns to set
	 */
	public void setColumns(String[] columns) {
		this.columns = columns;
	}
	/**
	 * Returns the unique.
	 * @return the unique
	 */
	public boolean isUnique() {
		return unique;
	}
	/**
	 * Sets the unique.
	 * @param unique the unique to set
	 */
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	/**
	 * Returns the composite.
	 * @return the composite
	 */
	public boolean isComposite() {
		return composite;
	}
	/**
	 * Sets the composite.
	 * @param composite the composite to set
	 */
	public void setComposite(boolean composite) {
		this.composite = composite;
	}
}
