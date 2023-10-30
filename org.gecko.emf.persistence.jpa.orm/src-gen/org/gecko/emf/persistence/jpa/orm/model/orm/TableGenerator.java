/*
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Table Generator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 
 *         @Target({TYPE, METHOD, FIELD}) @Retention(RUNTIME)
 *         public @interface TableGenerator {
 *           String name();
 *           String table() default "";
 *           String catalog() default "";
 *           String schema() default "";
 *           String pkColumnName() default "";
 *           String valueColumnName() default "";
 *           String pkColumnValue() default "";
 *           int initialValue() default 0;
 *           int allocationSize() default 50;
 *           UniqueConstraint[] uniqueConstraints() default {};
 *           Indexes[] indexes() default {};
 *         }
 * 
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getDescription <em>Description</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getUniqueConstraint <em>Unique Constraint</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getIndex <em>Index</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getAllocationSize <em>Allocation Size</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getCatalog <em>Catalog</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getInitialValue <em>Initial Value</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getName <em>Name</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getPkColumnName <em>Pk Column Name</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getPkColumnValue <em>Pk Column Value</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getSchema <em>Schema</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getTable <em>Table</em>}</li>
 *   <li>{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getValueColumnName <em>Value Column Name</em>}</li>
 * </ul>
 *
 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getTableGenerator()
 * @model extendedMetaData="name='table-generator' kind='elementOnly'"
 * @generated
 */
@ProviderType
public interface TableGenerator extends EObject {
	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getTableGenerator_Description()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Unique Constraint</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.emf.persistence.jpa.orm.model.orm.UniqueConstraint}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unique Constraint</em>' containment reference list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getTableGenerator_UniqueConstraint()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='unique-constraint' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<UniqueConstraint> getUniqueConstraint();

	/**
	 * Returns the value of the '<em><b>Index</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.emf.persistence.jpa.orm.model.orm.Index}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' containment reference list.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getTableGenerator_Index()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='index' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<Index> getIndex();

	/**
	 * Returns the value of the '<em><b>Allocation Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allocation Size</em>' attribute.
	 * @see #isSetAllocationSize()
	 * @see #unsetAllocationSize()
	 * @see #setAllocationSize(int)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getTableGenerator_AllocationSize()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int"
	 *        extendedMetaData="kind='attribute' name='allocation-size'"
	 * @generated
	 */
	int getAllocationSize();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getAllocationSize <em>Allocation Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Allocation Size</em>' attribute.
	 * @see #isSetAllocationSize()
	 * @see #unsetAllocationSize()
	 * @see #getAllocationSize()
	 * @generated
	 */
	void setAllocationSize(int value);

	/**
	 * Unsets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getAllocationSize <em>Allocation Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAllocationSize()
	 * @see #getAllocationSize()
	 * @see #setAllocationSize(int)
	 * @generated
	 */
	void unsetAllocationSize();

	/**
	 * Returns whether the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getAllocationSize <em>Allocation Size</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Allocation Size</em>' attribute is set.
	 * @see #unsetAllocationSize()
	 * @see #getAllocationSize()
	 * @see #setAllocationSize(int)
	 * @generated
	 */
	boolean isSetAllocationSize();

	/**
	 * Returns the value of the '<em><b>Catalog</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Catalog</em>' attribute.
	 * @see #setCatalog(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getTableGenerator_Catalog()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='catalog'"
	 * @generated
	 */
	String getCatalog();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getCatalog <em>Catalog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Catalog</em>' attribute.
	 * @see #getCatalog()
	 * @generated
	 */
	void setCatalog(String value);

	/**
	 * Returns the value of the '<em><b>Initial Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initial Value</em>' attribute.
	 * @see #isSetInitialValue()
	 * @see #unsetInitialValue()
	 * @see #setInitialValue(int)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getTableGenerator_InitialValue()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int"
	 *        extendedMetaData="kind='attribute' name='initial-value'"
	 * @generated
	 */
	int getInitialValue();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getInitialValue <em>Initial Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Initial Value</em>' attribute.
	 * @see #isSetInitialValue()
	 * @see #unsetInitialValue()
	 * @see #getInitialValue()
	 * @generated
	 */
	void setInitialValue(int value);

	/**
	 * Unsets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getInitialValue <em>Initial Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetInitialValue()
	 * @see #getInitialValue()
	 * @see #setInitialValue(int)
	 * @generated
	 */
	void unsetInitialValue();

	/**
	 * Returns whether the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getInitialValue <em>Initial Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Initial Value</em>' attribute is set.
	 * @see #unsetInitialValue()
	 * @see #getInitialValue()
	 * @see #setInitialValue(int)
	 * @generated
	 */
	boolean isSetInitialValue();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getTableGenerator_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Pk Column Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pk Column Name</em>' attribute.
	 * @see #setPkColumnName(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getTableGenerator_PkColumnName()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='pk-column-name'"
	 * @generated
	 */
	String getPkColumnName();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getPkColumnName <em>Pk Column Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pk Column Name</em>' attribute.
	 * @see #getPkColumnName()
	 * @generated
	 */
	void setPkColumnName(String value);

	/**
	 * Returns the value of the '<em><b>Pk Column Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pk Column Value</em>' attribute.
	 * @see #setPkColumnValue(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getTableGenerator_PkColumnValue()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='pk-column-value'"
	 * @generated
	 */
	String getPkColumnValue();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getPkColumnValue <em>Pk Column Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pk Column Value</em>' attribute.
	 * @see #getPkColumnValue()
	 * @generated
	 */
	void setPkColumnValue(String value);

	/**
	 * Returns the value of the '<em><b>Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Schema</em>' attribute.
	 * @see #setSchema(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getTableGenerator_Schema()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='schema'"
	 * @generated
	 */
	String getSchema();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getSchema <em>Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schema</em>' attribute.
	 * @see #getSchema()
	 * @generated
	 */
	void setSchema(String value);

	/**
	 * Returns the value of the '<em><b>Table</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Table</em>' attribute.
	 * @see #setTable(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getTableGenerator_Table()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='table'"
	 * @generated
	 */
	String getTable();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getTable <em>Table</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Table</em>' attribute.
	 * @see #getTable()
	 * @generated
	 */
	void setTable(String value);

	/**
	 * Returns the value of the '<em><b>Value Column Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value Column Name</em>' attribute.
	 * @see #setValueColumnName(String)
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#getTableGenerator_ValueColumnName()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='value-column-name'"
	 * @generated
	 */
	String getValueColumnName();

	/**
	 * Sets the value of the '{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator#getValueColumnName <em>Value Column Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value Column Name</em>' attribute.
	 * @see #getValueColumnName()
	 * @generated
	 */
	void setValueColumnName(String value);

} // TableGenerator
