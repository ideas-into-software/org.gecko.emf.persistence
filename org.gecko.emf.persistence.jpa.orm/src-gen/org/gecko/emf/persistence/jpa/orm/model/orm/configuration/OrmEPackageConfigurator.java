/*
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm.configuration;

import java.util.HashMap;
import java.util.Map;

import org.gecko.emf.osgi.EMFNamespaces;
import org.gecko.emf.osgi.EPackageConfigurator;

import org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>EPackageConfiguration</b> and <b>ResourceFactoryConfigurator</b> for the model.
 * The package will be registered into a OSGi base model registry.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * 
 * 
 *        This is the XML Schema for the persistence object/relational 
 *        mapping file.
 *        The file may be named "META-INF/orm.xml" in the persistence 
 *        archive or it may be named some other name which would be 
 *        used to locate the file as resource on the classpath.
 * 
 *        Object/relational mapping files must indicate the object/relational
 *        mapping file schema by using the persistence namespace:
 * 
 *        https://jakarta.ee/xml/ns/persistence/orm
 * 
 *        and indicate the version of the schema by
 *        using the version element as shown below:
 * 
 *       <entity-mappings xmlns="https://jakarta.ee/xml/ns/persistence/orm"
 *         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 *         xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence/orm
 *           https://jakarta.ee/xml/ns/persistence/orm/orm_3_1.xsd"
 *         version="3.1">
 *           ...
 *       </entity-mappings>
 * 
 * 
 *      
 * <!-- end-model-doc -->
 * @see EPackageConfigurator
 * @generated
 */
public class OrmEPackageConfigurator implements EPackageConfigurator {
	
	private OrmPackage ePackage;

	protected OrmEPackageConfigurator(OrmPackage ePackage){
		this.ePackage = ePackage;
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.gecko.emf.osgi.EPackageRegistryConfigurator#configureEPackage(org.eclipse.emf.ecore.EPackage.Registry)
	 * @generated
	 */
	@Override
	public void configureEPackage(org.eclipse.emf.ecore.EPackage.Registry registry) {
		registry.put(OrmPackage.eNS_URI, ePackage);
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.gecko.emf.osgi.EPackageRegistryConfigurator#unconfigureEPackage(org.eclipse.emf.ecore.EPackage.Registry)
	 * @generated
	 */
	@Override
	public void unconfigureEPackage(org.eclipse.emf.ecore.EPackage.Registry registry) {
		registry.remove(OrmPackage.eNS_URI);
	}
	
	/**
	 * A method providing the Prperties the services around this Model should be registered with.
	 * @generated
	 */
	public Map<String, Object> getServiceProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(EMFNamespaces.EMF_MODEL_NAME, OrmPackage.eNAME);
		properties.put(EMFNamespaces.EMF_MODEL_NSURI, OrmPackage.eNS_URI);
		properties.put(EMFNamespaces.EMF_MODEL_FILE_EXT, "orm");
		properties.put(EMFNamespaces.EMF_CONFIGURATOR_VERSION, "1.0");
		return properties;
	}
}