/**
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.gecko.emf.persistence.jpa.orm.model.orm.AccessType;
import org.gecko.emf.persistence.jpa.orm.model.orm.AssociationOverride;
import org.gecko.emf.persistence.jpa.orm.model.orm.AttributeOverride;
import org.gecko.emf.persistence.jpa.orm.model.orm.Attributes;
import org.gecko.emf.persistence.jpa.orm.model.orm.Basic;
import org.gecko.emf.persistence.jpa.orm.model.orm.CascadeType;
import org.gecko.emf.persistence.jpa.orm.model.orm.CollectionTable;
import org.gecko.emf.persistence.jpa.orm.model.orm.Column;
import org.gecko.emf.persistence.jpa.orm.model.orm.ColumnResult;
import org.gecko.emf.persistence.jpa.orm.model.orm.ConstraintMode;
import org.gecko.emf.persistence.jpa.orm.model.orm.ConstructorResult;
import org.gecko.emf.persistence.jpa.orm.model.orm.Convert;
import org.gecko.emf.persistence.jpa.orm.model.orm.Converter;
import org.gecko.emf.persistence.jpa.orm.model.orm.DiscriminatorColumn;
import org.gecko.emf.persistence.jpa.orm.model.orm.DiscriminatorType;
import org.gecko.emf.persistence.jpa.orm.model.orm.DocumentRoot;
import org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection;
import org.gecko.emf.persistence.jpa.orm.model.orm.Embeddable;
import org.gecko.emf.persistence.jpa.orm.model.orm.EmbeddableAttributes;
import org.gecko.emf.persistence.jpa.orm.model.orm.Embedded;
import org.gecko.emf.persistence.jpa.orm.model.orm.EmbeddedId;
import org.gecko.emf.persistence.jpa.orm.model.orm.EmptyType;
import org.gecko.emf.persistence.jpa.orm.model.orm.Entity;
import org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener;
import org.gecko.emf.persistence.jpa.orm.model.orm.EntityListeners;
import org.gecko.emf.persistence.jpa.orm.model.orm.EntityMappingsType;
import org.gecko.emf.persistence.jpa.orm.model.orm.EntityResult;
import org.gecko.emf.persistence.jpa.orm.model.orm.EnumType;
import org.gecko.emf.persistence.jpa.orm.model.orm.FetchType;
import org.gecko.emf.persistence.jpa.orm.model.orm.FieldResult;
import org.gecko.emf.persistence.jpa.orm.model.orm.ForeignKey;
import org.gecko.emf.persistence.jpa.orm.model.orm.GeneratedValue;
import org.gecko.emf.persistence.jpa.orm.model.orm.GenerationType;
import org.gecko.emf.persistence.jpa.orm.model.orm.Id;
import org.gecko.emf.persistence.jpa.orm.model.orm.IdClass;
import org.gecko.emf.persistence.jpa.orm.model.orm.Index;
import org.gecko.emf.persistence.jpa.orm.model.orm.Inheritance;
import org.gecko.emf.persistence.jpa.orm.model.orm.InheritanceType;
import org.gecko.emf.persistence.jpa.orm.model.orm.JoinColumn;
import org.gecko.emf.persistence.jpa.orm.model.orm.JoinTable;
import org.gecko.emf.persistence.jpa.orm.model.orm.Lob;
import org.gecko.emf.persistence.jpa.orm.model.orm.LockModeType;
import org.gecko.emf.persistence.jpa.orm.model.orm.ManyToMany;
import org.gecko.emf.persistence.jpa.orm.model.orm.ManyToOne;
import org.gecko.emf.persistence.jpa.orm.model.orm.MapKey;
import org.gecko.emf.persistence.jpa.orm.model.orm.MapKeyClass;
import org.gecko.emf.persistence.jpa.orm.model.orm.MapKeyColumn;
import org.gecko.emf.persistence.jpa.orm.model.orm.MapKeyJoinColumn;
import org.gecko.emf.persistence.jpa.orm.model.orm.MappedSuperclass;
import org.gecko.emf.persistence.jpa.orm.model.orm.NamedAttributeNode;
import org.gecko.emf.persistence.jpa.orm.model.orm.NamedEntityGraph;
import org.gecko.emf.persistence.jpa.orm.model.orm.NamedNativeQuery;
import org.gecko.emf.persistence.jpa.orm.model.orm.NamedQuery;
import org.gecko.emf.persistence.jpa.orm.model.orm.NamedStoredProcedureQuery;
import org.gecko.emf.persistence.jpa.orm.model.orm.NamedSubgraph;
import org.gecko.emf.persistence.jpa.orm.model.orm.OneToMany;
import org.gecko.emf.persistence.jpa.orm.model.orm.OneToOne;
import org.gecko.emf.persistence.jpa.orm.model.orm.OrderColumn;
import org.gecko.emf.persistence.jpa.orm.model.orm.OrmFactory;
import org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage;
import org.gecko.emf.persistence.jpa.orm.model.orm.ParameterMode;
import org.gecko.emf.persistence.jpa.orm.model.orm.PersistenceUnitDefaults;
import org.gecko.emf.persistence.jpa.orm.model.orm.PersistenceUnitMetadata;
import org.gecko.emf.persistence.jpa.orm.model.orm.PostLoad;
import org.gecko.emf.persistence.jpa.orm.model.orm.PostPersist;
import org.gecko.emf.persistence.jpa.orm.model.orm.PostRemove;
import org.gecko.emf.persistence.jpa.orm.model.orm.PostUpdate;
import org.gecko.emf.persistence.jpa.orm.model.orm.PrePersist;
import org.gecko.emf.persistence.jpa.orm.model.orm.PreRemove;
import org.gecko.emf.persistence.jpa.orm.model.orm.PreUpdate;
import org.gecko.emf.persistence.jpa.orm.model.orm.PrimaryKeyJoinColumn;
import org.gecko.emf.persistence.jpa.orm.model.orm.QueryHint;
import org.gecko.emf.persistence.jpa.orm.model.orm.SecondaryTable;
import org.gecko.emf.persistence.jpa.orm.model.orm.SequenceGenerator;
import org.gecko.emf.persistence.jpa.orm.model.orm.SqlResultSetMapping;
import org.gecko.emf.persistence.jpa.orm.model.orm.StoredProcedureParameter;
import org.gecko.emf.persistence.jpa.orm.model.orm.Table;
import org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator;
import org.gecko.emf.persistence.jpa.orm.model.orm.TemporalType;
import org.gecko.emf.persistence.jpa.orm.model.orm.Transient;
import org.gecko.emf.persistence.jpa.orm.model.orm.UniqueConstraint;
import org.gecko.emf.persistence.jpa.orm.model.orm.Version;

import org.gecko.emf.persistence.jpa.orm.model.orm.util.OrmValidator;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class OrmPackageImpl extends EPackageImpl implements OrmPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass associationOverrideEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass attributeOverrideEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass attributesEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass basicEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cascadeTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass collectionTableEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass columnEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass columnResultEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass constructorResultEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass convertEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass converterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass discriminatorColumnEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass documentRootEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass elementCollectionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass embeddableEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass embeddableAttributesEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass embeddedEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass embeddedIdEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass emptyTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityListenerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityListenersEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityMappingsTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityResultEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fieldResultEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass foreignKeyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass generatedValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass idEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass idClassEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indexEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass inheritanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass joinColumnEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass joinTableEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lobEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass manyToManyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass manyToOneEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mapKeyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mapKeyClassEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mapKeyColumnEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mapKeyJoinColumnEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mappedSuperclassEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedAttributeNodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedEntityGraphEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedNativeQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedStoredProcedureQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedSubgraphEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass oneToManyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass oneToOneEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass orderColumnEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass persistenceUnitDefaultsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass persistenceUnitMetadataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass postLoadEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass postPersistEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass postRemoveEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass postUpdateEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass prePersistEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass preRemoveEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass preUpdateEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass primaryKeyJoinColumnEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass queryHintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass secondaryTableEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sequenceGeneratorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sqlResultSetMappingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass storedProcedureParameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tableEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tableGeneratorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass transientEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass uniqueConstraintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass versionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum accessTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum constraintModeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum discriminatorTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum enumTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum fetchTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum generationTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum inheritanceTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum lockModeTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum parameterModeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum temporalTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType accessTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType constraintModeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType discriminatorTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType discriminatorValueEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType enumeratedEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType enumTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType fetchTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType generationTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType inheritanceTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType lockModeTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType orderByEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType parameterModeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType temporalEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType temporalTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType versionTypeEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private OrmPackageImpl() {
		super(eNS_URI, OrmFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link OrmPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static OrmPackage init() {
		if (isInited) return (OrmPackage)EPackage.Registry.INSTANCE.getEPackage(OrmPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredOrmPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		OrmPackageImpl theOrmPackage = registeredOrmPackage instanceof OrmPackageImpl ? (OrmPackageImpl)registeredOrmPackage : new OrmPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theOrmPackage.createPackageContents();

		// Initialize created meta-data
		theOrmPackage.initializePackageContents();

		// Register package validator
		EValidator.Registry.INSTANCE.put
			(theOrmPackage,
			 new EValidator.Descriptor() {
				 @Override
				 public EValidator getEValidator() {
					 return OrmValidator.INSTANCE;
				 }
			 });

		// Mark meta-data to indicate it can't be changed
		theOrmPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(OrmPackage.eNS_URI, theOrmPackage);
		return theOrmPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAssociationOverride() {
		return associationOverrideEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAssociationOverride_Description() {
		return (EAttribute)associationOverrideEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAssociationOverride_JoinColumn() {
		return (EReference)associationOverrideEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAssociationOverride_ForeignKey() {
		return (EReference)associationOverrideEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAssociationOverride_JoinTable() {
		return (EReference)associationOverrideEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAssociationOverride_Name() {
		return (EAttribute)associationOverrideEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAttributeOverride() {
		return attributeOverrideEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAttributeOverride_Description() {
		return (EAttribute)attributeOverrideEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAttributeOverride_Column() {
		return (EReference)attributeOverrideEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAttributeOverride_Name() {
		return (EAttribute)attributeOverrideEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAttributes() {
		return attributesEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAttributes_Description() {
		return (EAttribute)attributesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAttributes_Id() {
		return (EReference)attributesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAttributes_EmbeddedId() {
		return (EReference)attributesEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAttributes_Basic() {
		return (EReference)attributesEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAttributes_Version() {
		return (EReference)attributesEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAttributes_ManyToOne() {
		return (EReference)attributesEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAttributes_OneToMany() {
		return (EReference)attributesEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAttributes_OneToOne() {
		return (EReference)attributesEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAttributes_ManyToMany() {
		return (EReference)attributesEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAttributes_ElementCollection() {
		return (EReference)attributesEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAttributes_Embedded() {
		return (EReference)attributesEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAttributes_Transient() {
		return (EReference)attributesEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBasic() {
		return basicEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBasic_Column() {
		return (EReference)basicEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBasic_Lob() {
		return (EReference)basicEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBasic_Temporal() {
		return (EAttribute)basicEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBasic_Enumerated() {
		return (EAttribute)basicEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBasic_Convert() {
		return (EReference)basicEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBasic_Access() {
		return (EAttribute)basicEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBasic_Fetch() {
		return (EAttribute)basicEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBasic_Name() {
		return (EAttribute)basicEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBasic_Optional() {
		return (EAttribute)basicEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCascadeType() {
		return cascadeTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCascadeType_CascadeAll() {
		return (EReference)cascadeTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCascadeType_CascadePersist() {
		return (EReference)cascadeTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCascadeType_CascadeMerge() {
		return (EReference)cascadeTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCascadeType_CascadeRemove() {
		return (EReference)cascadeTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCascadeType_CascadeRefresh() {
		return (EReference)cascadeTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCascadeType_CascadeDetach() {
		return (EReference)cascadeTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCollectionTable() {
		return collectionTableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCollectionTable_JoinColumn() {
		return (EReference)collectionTableEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCollectionTable_ForeignKey() {
		return (EReference)collectionTableEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCollectionTable_UniqueConstraint() {
		return (EReference)collectionTableEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCollectionTable_Index() {
		return (EReference)collectionTableEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCollectionTable_Catalog() {
		return (EAttribute)collectionTableEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCollectionTable_Name() {
		return (EAttribute)collectionTableEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCollectionTable_Schema() {
		return (EAttribute)collectionTableEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getColumn() {
		return columnEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getColumn_ColumnDefinition() {
		return (EAttribute)columnEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getColumn_Insertable() {
		return (EAttribute)columnEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getColumn_Length() {
		return (EAttribute)columnEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getColumn_Name() {
		return (EAttribute)columnEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getColumn_Nullable() {
		return (EAttribute)columnEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getColumn_Precision() {
		return (EAttribute)columnEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getColumn_Scale() {
		return (EAttribute)columnEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getColumn_Table() {
		return (EAttribute)columnEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getColumn_Unique() {
		return (EAttribute)columnEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getColumn_Updatable() {
		return (EAttribute)columnEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getColumnResult() {
		return columnResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getColumnResult_Class() {
		return (EAttribute)columnResultEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getColumnResult_Name() {
		return (EAttribute)columnResultEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getConstructorResult() {
		return constructorResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getConstructorResult_Column() {
		return (EReference)constructorResultEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getConstructorResult_TargetClass() {
		return (EAttribute)constructorResultEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getConvert() {
		return convertEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getConvert_Description() {
		return (EAttribute)convertEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getConvert_AttributeName() {
		return (EAttribute)convertEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getConvert_Converter() {
		return (EAttribute)convertEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getConvert_DisableConversion() {
		return (EAttribute)convertEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getConverter() {
		return converterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getConverter_Description() {
		return (EAttribute)converterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getConverter_AutoApply() {
		return (EAttribute)converterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getConverter_Class() {
		return (EAttribute)converterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDiscriminatorColumn() {
		return discriminatorColumnEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDiscriminatorColumn_ColumnDefinition() {
		return (EAttribute)discriminatorColumnEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDiscriminatorColumn_DiscriminatorType() {
		return (EAttribute)discriminatorColumnEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDiscriminatorColumn_Length() {
		return (EAttribute)discriminatorColumnEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDiscriminatorColumn_Name() {
		return (EAttribute)discriminatorColumnEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDocumentRoot() {
		return documentRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDocumentRoot_Mixed() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDocumentRoot_XMLNSPrefixMap() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDocumentRoot_XSISchemaLocation() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDocumentRoot_EntityMappings() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getElementCollection() {
		return elementCollectionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getElementCollection_OrderBy() {
		return (EAttribute)elementCollectionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getElementCollection_OrderColumn() {
		return (EReference)elementCollectionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getElementCollection_MapKey() {
		return (EReference)elementCollectionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getElementCollection_MapKeyClass() {
		return (EReference)elementCollectionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getElementCollection_MapKeyTemporal() {
		return (EAttribute)elementCollectionEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getElementCollection_MapKeyEnumerated() {
		return (EAttribute)elementCollectionEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getElementCollection_MapKeyAttributeOverride() {
		return (EReference)elementCollectionEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getElementCollection_MapKeyConvert() {
		return (EReference)elementCollectionEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getElementCollection_MapKeyColumn() {
		return (EReference)elementCollectionEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getElementCollection_MapKeyJoinColumn() {
		return (EReference)elementCollectionEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getElementCollection_MapKeyForeignKey() {
		return (EReference)elementCollectionEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getElementCollection_Column() {
		return (EReference)elementCollectionEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getElementCollection_Temporal() {
		return (EAttribute)elementCollectionEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getElementCollection_Enumerated() {
		return (EAttribute)elementCollectionEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getElementCollection_Lob() {
		return (EReference)elementCollectionEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getElementCollection_AttributeOverride() {
		return (EReference)elementCollectionEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getElementCollection_AssociationOverride() {
		return (EReference)elementCollectionEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getElementCollection_Convert() {
		return (EReference)elementCollectionEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getElementCollection_CollectionTable() {
		return (EReference)elementCollectionEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getElementCollection_Access() {
		return (EAttribute)elementCollectionEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getElementCollection_Fetch() {
		return (EAttribute)elementCollectionEClass.getEStructuralFeatures().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getElementCollection_Name() {
		return (EAttribute)elementCollectionEClass.getEStructuralFeatures().get(21);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getElementCollection_TargetClass() {
		return (EAttribute)elementCollectionEClass.getEStructuralFeatures().get(22);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEmbeddable() {
		return embeddableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEmbeddable_Description() {
		return (EAttribute)embeddableEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEmbeddable_Attributes() {
		return (EReference)embeddableEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEmbeddable_Access() {
		return (EAttribute)embeddableEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEmbeddable_Class() {
		return (EAttribute)embeddableEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEmbeddable_MetadataComplete() {
		return (EAttribute)embeddableEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEmbeddableAttributes() {
		return embeddableAttributesEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEmbeddableAttributes_Basic() {
		return (EReference)embeddableAttributesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEmbeddableAttributes_ManyToOne() {
		return (EReference)embeddableAttributesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEmbeddableAttributes_OneToMany() {
		return (EReference)embeddableAttributesEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEmbeddableAttributes_OneToOne() {
		return (EReference)embeddableAttributesEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEmbeddableAttributes_ManyToMany() {
		return (EReference)embeddableAttributesEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEmbeddableAttributes_ElementCollection() {
		return (EReference)embeddableAttributesEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEmbeddableAttributes_Embedded() {
		return (EReference)embeddableAttributesEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEmbeddableAttributes_Transient() {
		return (EReference)embeddableAttributesEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEmbedded() {
		return embeddedEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEmbedded_AttributeOverride() {
		return (EReference)embeddedEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEmbedded_AssociationOverride() {
		return (EReference)embeddedEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEmbedded_Convert() {
		return (EReference)embeddedEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEmbedded_Access() {
		return (EAttribute)embeddedEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEmbedded_Name() {
		return (EAttribute)embeddedEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEmbeddedId() {
		return embeddedIdEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEmbeddedId_AttributeOverride() {
		return (EReference)embeddedIdEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEmbeddedId_Access() {
		return (EAttribute)embeddedIdEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEmbeddedId_Name() {
		return (EAttribute)embeddedIdEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEmptyType() {
		return emptyTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEntity() {
		return entityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntity_Description() {
		return (EAttribute)entityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_Table() {
		return (EReference)entityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_SecondaryTable() {
		return (EReference)entityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_PrimaryKeyJoinColumn() {
		return (EReference)entityEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_PrimaryKeyForeignKey() {
		return (EReference)entityEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_IdClass() {
		return (EReference)entityEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_Inheritance() {
		return (EReference)entityEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntity_DiscriminatorValue() {
		return (EAttribute)entityEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_DiscriminatorColumn() {
		return (EReference)entityEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_SequenceGenerator() {
		return (EReference)entityEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_TableGenerator() {
		return (EReference)entityEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_NamedQuery() {
		return (EReference)entityEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_NamedNativeQuery() {
		return (EReference)entityEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_NamedStoredProcedureQuery() {
		return (EReference)entityEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_SqlResultSetMapping() {
		return (EReference)entityEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_ExcludeDefaultListeners() {
		return (EReference)entityEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_ExcludeSuperclassListeners() {
		return (EReference)entityEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_EntityListeners() {
		return (EReference)entityEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_PrePersist() {
		return (EReference)entityEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_PostPersist() {
		return (EReference)entityEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_PreRemove() {
		return (EReference)entityEClass.getEStructuralFeatures().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_PostRemove() {
		return (EReference)entityEClass.getEStructuralFeatures().get(21);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_PreUpdate() {
		return (EReference)entityEClass.getEStructuralFeatures().get(22);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_PostUpdate() {
		return (EReference)entityEClass.getEStructuralFeatures().get(23);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_PostLoad() {
		return (EReference)entityEClass.getEStructuralFeatures().get(24);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_AttributeOverride() {
		return (EReference)entityEClass.getEStructuralFeatures().get(25);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_AssociationOverride() {
		return (EReference)entityEClass.getEStructuralFeatures().get(26);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_Convert() {
		return (EReference)entityEClass.getEStructuralFeatures().get(27);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_NamedEntityGraph() {
		return (EReference)entityEClass.getEStructuralFeatures().get(28);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntity_Attributes() {
		return (EReference)entityEClass.getEStructuralFeatures().get(29);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntity_Access() {
		return (EAttribute)entityEClass.getEStructuralFeatures().get(30);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntity_Cacheable() {
		return (EAttribute)entityEClass.getEStructuralFeatures().get(31);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntity_Class() {
		return (EAttribute)entityEClass.getEStructuralFeatures().get(32);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntity_MetadataComplete() {
		return (EAttribute)entityEClass.getEStructuralFeatures().get(33);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntity_Name() {
		return (EAttribute)entityEClass.getEStructuralFeatures().get(34);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEntityListener() {
		return entityListenerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntityListener_Description() {
		return (EAttribute)entityListenerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityListener_PrePersist() {
		return (EReference)entityListenerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityListener_PostPersist() {
		return (EReference)entityListenerEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityListener_PreRemove() {
		return (EReference)entityListenerEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityListener_PostRemove() {
		return (EReference)entityListenerEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityListener_PreUpdate() {
		return (EReference)entityListenerEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityListener_PostUpdate() {
		return (EReference)entityListenerEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityListener_PostLoad() {
		return (EReference)entityListenerEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntityListener_Class() {
		return (EAttribute)entityListenerEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEntityListeners() {
		return entityListenersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityListeners_EntityListener() {
		return (EReference)entityListenersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEntityMappingsType() {
		return entityMappingsTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntityMappingsType_Description() {
		return (EAttribute)entityMappingsTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityMappingsType_PersistenceUnitMetadata() {
		return (EReference)entityMappingsTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntityMappingsType_Package() {
		return (EAttribute)entityMappingsTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntityMappingsType_Schema() {
		return (EAttribute)entityMappingsTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntityMappingsType_Catalog() {
		return (EAttribute)entityMappingsTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntityMappingsType_Access() {
		return (EAttribute)entityMappingsTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityMappingsType_SequenceGenerator() {
		return (EReference)entityMappingsTypeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityMappingsType_TableGenerator() {
		return (EReference)entityMappingsTypeEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityMappingsType_NamedQuery() {
		return (EReference)entityMappingsTypeEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityMappingsType_NamedNativeQuery() {
		return (EReference)entityMappingsTypeEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityMappingsType_NamedStoredProcedureQuery() {
		return (EReference)entityMappingsTypeEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityMappingsType_SqlResultSetMapping() {
		return (EReference)entityMappingsTypeEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityMappingsType_MappedSuperclass() {
		return (EReference)entityMappingsTypeEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityMappingsType_Entity() {
		return (EReference)entityMappingsTypeEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityMappingsType_Embeddable() {
		return (EReference)entityMappingsTypeEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityMappingsType_Converter() {
		return (EReference)entityMappingsTypeEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntityMappingsType_Version() {
		return (EAttribute)entityMappingsTypeEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEntityResult() {
		return entityResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntityResult_FieldResult() {
		return (EReference)entityResultEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntityResult_DiscriminatorColumn() {
		return (EAttribute)entityResultEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEntityResult_EntityClass() {
		return (EAttribute)entityResultEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFieldResult() {
		return fieldResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFieldResult_Column() {
		return (EAttribute)fieldResultEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFieldResult_Name() {
		return (EAttribute)fieldResultEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getForeignKey() {
		return foreignKeyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getForeignKey_Description() {
		return (EAttribute)foreignKeyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getForeignKey_ConstraintMode() {
		return (EAttribute)foreignKeyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getForeignKey_ForeignKeyDefinition() {
		return (EAttribute)foreignKeyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getForeignKey_Name() {
		return (EAttribute)foreignKeyEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getGeneratedValue() {
		return generatedValueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getGeneratedValue_Generator() {
		return (EAttribute)generatedValueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getGeneratedValue_Strategy() {
		return (EAttribute)generatedValueEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getId() {
		return idEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getId_Column() {
		return (EReference)idEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getId_GeneratedValue() {
		return (EReference)idEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getId_Temporal() {
		return (EAttribute)idEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getId_TableGenerator() {
		return (EReference)idEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getId_SequenceGenerator() {
		return (EReference)idEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getId_Access() {
		return (EAttribute)idEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getId_Name() {
		return (EAttribute)idEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getIdClass() {
		return idClassEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getIdClass_Class() {
		return (EAttribute)idClassEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getIndex() {
		return indexEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getIndex_Description() {
		return (EAttribute)indexEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getIndex_ColumnList() {
		return (EAttribute)indexEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getIndex_Name() {
		return (EAttribute)indexEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getIndex_Unique() {
		return (EAttribute)indexEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getInheritance() {
		return inheritanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getInheritance_Strategy() {
		return (EAttribute)inheritanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getJoinColumn() {
		return joinColumnEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getJoinColumn_ColumnDefinition() {
		return (EAttribute)joinColumnEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getJoinColumn_Insertable() {
		return (EAttribute)joinColumnEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getJoinColumn_Name() {
		return (EAttribute)joinColumnEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getJoinColumn_Nullable() {
		return (EAttribute)joinColumnEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getJoinColumn_ReferencedColumnName() {
		return (EAttribute)joinColumnEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getJoinColumn_Table() {
		return (EAttribute)joinColumnEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getJoinColumn_Unique() {
		return (EAttribute)joinColumnEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getJoinColumn_Updatable() {
		return (EAttribute)joinColumnEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getJoinTable() {
		return joinTableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getJoinTable_JoinColumn() {
		return (EReference)joinTableEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getJoinTable_ForeignKey() {
		return (EReference)joinTableEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getJoinTable_InverseJoinColumn() {
		return (EReference)joinTableEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getJoinTable_InverseForeignKey() {
		return (EReference)joinTableEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getJoinTable_UniqueConstraint() {
		return (EReference)joinTableEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getJoinTable_Index() {
		return (EReference)joinTableEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getJoinTable_Catalog() {
		return (EAttribute)joinTableEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getJoinTable_Name() {
		return (EAttribute)joinTableEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getJoinTable_Schema() {
		return (EAttribute)joinTableEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLob() {
		return lobEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getManyToMany() {
		return manyToManyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getManyToMany_OrderBy() {
		return (EAttribute)manyToManyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getManyToMany_OrderColumn() {
		return (EReference)manyToManyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getManyToMany_MapKey() {
		return (EReference)manyToManyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getManyToMany_MapKeyClass() {
		return (EReference)manyToManyEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getManyToMany_MapKeyTemporal() {
		return (EAttribute)manyToManyEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getManyToMany_MapKeyEnumerated() {
		return (EAttribute)manyToManyEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getManyToMany_MapKeyAttributeOverride() {
		return (EReference)manyToManyEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getManyToMany_MapKeyConvert() {
		return (EReference)manyToManyEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getManyToMany_MapKeyColumn() {
		return (EReference)manyToManyEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getManyToMany_MapKeyJoinColumn() {
		return (EReference)manyToManyEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getManyToMany_MapKeyForeignKey() {
		return (EReference)manyToManyEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getManyToMany_JoinTable() {
		return (EReference)manyToManyEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getManyToMany_Cascade() {
		return (EReference)manyToManyEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getManyToMany_Access() {
		return (EAttribute)manyToManyEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getManyToMany_Fetch() {
		return (EAttribute)manyToManyEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getManyToMany_MappedBy() {
		return (EAttribute)manyToManyEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getManyToMany_Name() {
		return (EAttribute)manyToManyEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getManyToMany_TargetEntity() {
		return (EAttribute)manyToManyEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getManyToOne() {
		return manyToOneEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getManyToOne_JoinColumn() {
		return (EReference)manyToOneEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getManyToOne_ForeignKey() {
		return (EReference)manyToOneEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getManyToOne_JoinTable() {
		return (EReference)manyToOneEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getManyToOne_Cascade() {
		return (EReference)manyToOneEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getManyToOne_Access() {
		return (EAttribute)manyToOneEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getManyToOne_Fetch() {
		return (EAttribute)manyToOneEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getManyToOne_Id() {
		return (EAttribute)manyToOneEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getManyToOne_MapsId() {
		return (EAttribute)manyToOneEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getManyToOne_Name() {
		return (EAttribute)manyToOneEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getManyToOne_Optional() {
		return (EAttribute)manyToOneEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getManyToOne_TargetEntity() {
		return (EAttribute)manyToOneEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMapKey() {
		return mapKeyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKey_Name() {
		return (EAttribute)mapKeyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMapKeyClass() {
		return mapKeyClassEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyClass_Class() {
		return (EAttribute)mapKeyClassEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMapKeyColumn() {
		return mapKeyColumnEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyColumn_ColumnDefinition() {
		return (EAttribute)mapKeyColumnEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyColumn_Insertable() {
		return (EAttribute)mapKeyColumnEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyColumn_Length() {
		return (EAttribute)mapKeyColumnEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyColumn_Name() {
		return (EAttribute)mapKeyColumnEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyColumn_Nullable() {
		return (EAttribute)mapKeyColumnEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyColumn_Precision() {
		return (EAttribute)mapKeyColumnEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyColumn_Scale() {
		return (EAttribute)mapKeyColumnEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyColumn_Table() {
		return (EAttribute)mapKeyColumnEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyColumn_Unique() {
		return (EAttribute)mapKeyColumnEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyColumn_Updatable() {
		return (EAttribute)mapKeyColumnEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMapKeyJoinColumn() {
		return mapKeyJoinColumnEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyJoinColumn_ColumnDefinition() {
		return (EAttribute)mapKeyJoinColumnEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyJoinColumn_Insertable() {
		return (EAttribute)mapKeyJoinColumnEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyJoinColumn_Name() {
		return (EAttribute)mapKeyJoinColumnEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyJoinColumn_Nullable() {
		return (EAttribute)mapKeyJoinColumnEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyJoinColumn_ReferencedColumnName() {
		return (EAttribute)mapKeyJoinColumnEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyJoinColumn_Table() {
		return (EAttribute)mapKeyJoinColumnEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyJoinColumn_Unique() {
		return (EAttribute)mapKeyJoinColumnEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMapKeyJoinColumn_Updatable() {
		return (EAttribute)mapKeyJoinColumnEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMappedSuperclass() {
		return mappedSuperclassEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMappedSuperclass_Description() {
		return (EAttribute)mappedSuperclassEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappedSuperclass_IdClass() {
		return (EReference)mappedSuperclassEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappedSuperclass_ExcludeDefaultListeners() {
		return (EReference)mappedSuperclassEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappedSuperclass_ExcludeSuperclassListeners() {
		return (EReference)mappedSuperclassEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappedSuperclass_EntityListeners() {
		return (EReference)mappedSuperclassEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappedSuperclass_PrePersist() {
		return (EReference)mappedSuperclassEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappedSuperclass_PostPersist() {
		return (EReference)mappedSuperclassEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappedSuperclass_PreRemove() {
		return (EReference)mappedSuperclassEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappedSuperclass_PostRemove() {
		return (EReference)mappedSuperclassEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappedSuperclass_PreUpdate() {
		return (EReference)mappedSuperclassEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappedSuperclass_PostUpdate() {
		return (EReference)mappedSuperclassEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappedSuperclass_PostLoad() {
		return (EReference)mappedSuperclassEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappedSuperclass_Attributes() {
		return (EReference)mappedSuperclassEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMappedSuperclass_Access() {
		return (EAttribute)mappedSuperclassEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMappedSuperclass_Class() {
		return (EAttribute)mappedSuperclassEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMappedSuperclass_MetadataComplete() {
		return (EAttribute)mappedSuperclassEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getNamedAttributeNode() {
		return namedAttributeNodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedAttributeNode_KeySubgraph() {
		return (EAttribute)namedAttributeNodeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedAttributeNode_Name() {
		return (EAttribute)namedAttributeNodeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedAttributeNode_Subgraph() {
		return (EAttribute)namedAttributeNodeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getNamedEntityGraph() {
		return namedEntityGraphEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getNamedEntityGraph_NamedAttributeNode() {
		return (EReference)namedEntityGraphEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getNamedEntityGraph_Subgraph() {
		return (EReference)namedEntityGraphEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getNamedEntityGraph_SubclassSubgraph() {
		return (EReference)namedEntityGraphEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedEntityGraph_IncludeAllAttributes() {
		return (EAttribute)namedEntityGraphEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedEntityGraph_Name() {
		return (EAttribute)namedEntityGraphEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getNamedNativeQuery() {
		return namedNativeQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedNativeQuery_Description() {
		return (EAttribute)namedNativeQueryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedNativeQuery_Query() {
		return (EAttribute)namedNativeQueryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getNamedNativeQuery_Hint() {
		return (EReference)namedNativeQueryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedNativeQuery_Name() {
		return (EAttribute)namedNativeQueryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedNativeQuery_ResultClass() {
		return (EAttribute)namedNativeQueryEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedNativeQuery_ResultSetMapping() {
		return (EAttribute)namedNativeQueryEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getNamedQuery() {
		return namedQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedQuery_Description() {
		return (EAttribute)namedQueryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedQuery_Query() {
		return (EAttribute)namedQueryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedQuery_LockMode() {
		return (EAttribute)namedQueryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getNamedQuery_Hint() {
		return (EReference)namedQueryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedQuery_Name() {
		return (EAttribute)namedQueryEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getNamedStoredProcedureQuery() {
		return namedStoredProcedureQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedStoredProcedureQuery_Description() {
		return (EAttribute)namedStoredProcedureQueryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getNamedStoredProcedureQuery_Parameter() {
		return (EReference)namedStoredProcedureQueryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedStoredProcedureQuery_ResultClass() {
		return (EAttribute)namedStoredProcedureQueryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedStoredProcedureQuery_ResultSetMapping() {
		return (EAttribute)namedStoredProcedureQueryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getNamedStoredProcedureQuery_Hint() {
		return (EReference)namedStoredProcedureQueryEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedStoredProcedureQuery_Name() {
		return (EAttribute)namedStoredProcedureQueryEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedStoredProcedureQuery_ProcedureName() {
		return (EAttribute)namedStoredProcedureQueryEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getNamedSubgraph() {
		return namedSubgraphEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getNamedSubgraph_NamedAttributeNode() {
		return (EReference)namedSubgraphEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedSubgraph_Class() {
		return (EAttribute)namedSubgraphEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedSubgraph_Name() {
		return (EAttribute)namedSubgraphEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOneToMany() {
		return oneToManyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToMany_OrderBy() {
		return (EAttribute)oneToManyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToMany_OrderColumn() {
		return (EReference)oneToManyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToMany_MapKey() {
		return (EReference)oneToManyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToMany_MapKeyClass() {
		return (EReference)oneToManyEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToMany_MapKeyTemporal() {
		return (EAttribute)oneToManyEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToMany_MapKeyEnumerated() {
		return (EAttribute)oneToManyEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToMany_MapKeyAttributeOverride() {
		return (EReference)oneToManyEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToMany_MapKeyConvert() {
		return (EReference)oneToManyEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToMany_MapKeyColumn() {
		return (EReference)oneToManyEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToMany_MapKeyJoinColumn() {
		return (EReference)oneToManyEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToMany_MapKeyForeignKey() {
		return (EReference)oneToManyEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToMany_JoinTable() {
		return (EReference)oneToManyEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToMany_JoinColumn() {
		return (EReference)oneToManyEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToMany_ForeignKey() {
		return (EReference)oneToManyEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToMany_Cascade() {
		return (EReference)oneToManyEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToMany_Access() {
		return (EAttribute)oneToManyEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToMany_Fetch() {
		return (EAttribute)oneToManyEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToMany_MappedBy() {
		return (EAttribute)oneToManyEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToMany_Name() {
		return (EAttribute)oneToManyEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToMany_OrphanRemoval() {
		return (EAttribute)oneToManyEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToMany_TargetEntity() {
		return (EAttribute)oneToManyEClass.getEStructuralFeatures().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOneToOne() {
		return oneToOneEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToOne_PrimaryKeyJoinColumn() {
		return (EReference)oneToOneEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToOne_PrimaryKeyForeignKey() {
		return (EReference)oneToOneEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToOne_JoinColumn() {
		return (EReference)oneToOneEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToOne_ForeignKey() {
		return (EReference)oneToOneEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToOne_JoinTable() {
		return (EReference)oneToOneEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOneToOne_Cascade() {
		return (EReference)oneToOneEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToOne_Access() {
		return (EAttribute)oneToOneEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToOne_Fetch() {
		return (EAttribute)oneToOneEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToOne_Id() {
		return (EAttribute)oneToOneEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToOne_MappedBy() {
		return (EAttribute)oneToOneEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToOne_MapsId() {
		return (EAttribute)oneToOneEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToOne_Name() {
		return (EAttribute)oneToOneEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToOne_Optional() {
		return (EAttribute)oneToOneEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToOne_OrphanRemoval() {
		return (EAttribute)oneToOneEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOneToOne_TargetEntity() {
		return (EAttribute)oneToOneEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOrderColumn() {
		return orderColumnEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOrderColumn_ColumnDefinition() {
		return (EAttribute)orderColumnEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOrderColumn_Insertable() {
		return (EAttribute)orderColumnEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOrderColumn_Name() {
		return (EAttribute)orderColumnEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOrderColumn_Nullable() {
		return (EAttribute)orderColumnEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOrderColumn_Updatable() {
		return (EAttribute)orderColumnEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPersistenceUnitDefaults() {
		return persistenceUnitDefaultsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPersistenceUnitDefaults_Description() {
		return (EAttribute)persistenceUnitDefaultsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPersistenceUnitDefaults_Schema() {
		return (EAttribute)persistenceUnitDefaultsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPersistenceUnitDefaults_Catalog() {
		return (EAttribute)persistenceUnitDefaultsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPersistenceUnitDefaults_DelimitedIdentifiers() {
		return (EReference)persistenceUnitDefaultsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPersistenceUnitDefaults_Access() {
		return (EAttribute)persistenceUnitDefaultsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPersistenceUnitDefaults_CascadePersist() {
		return (EReference)persistenceUnitDefaultsEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPersistenceUnitDefaults_EntityListeners() {
		return (EReference)persistenceUnitDefaultsEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPersistenceUnitMetadata() {
		return persistenceUnitMetadataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPersistenceUnitMetadata_Description() {
		return (EAttribute)persistenceUnitMetadataEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPersistenceUnitMetadata_XmlMappingMetadataComplete() {
		return (EReference)persistenceUnitMetadataEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPersistenceUnitMetadata_PersistenceUnitDefaults() {
		return (EReference)persistenceUnitMetadataEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPostLoad() {
		return postLoadEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPostLoad_Description() {
		return (EAttribute)postLoadEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPostLoad_MethodName() {
		return (EAttribute)postLoadEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPostPersist() {
		return postPersistEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPostPersist_Description() {
		return (EAttribute)postPersistEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPostPersist_MethodName() {
		return (EAttribute)postPersistEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPostRemove() {
		return postRemoveEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPostRemove_Description() {
		return (EAttribute)postRemoveEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPostRemove_MethodName() {
		return (EAttribute)postRemoveEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPostUpdate() {
		return postUpdateEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPostUpdate_Description() {
		return (EAttribute)postUpdateEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPostUpdate_MethodName() {
		return (EAttribute)postUpdateEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPrePersist() {
		return prePersistEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPrePersist_Description() {
		return (EAttribute)prePersistEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPrePersist_MethodName() {
		return (EAttribute)prePersistEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPreRemove() {
		return preRemoveEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPreRemove_Description() {
		return (EAttribute)preRemoveEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPreRemove_MethodName() {
		return (EAttribute)preRemoveEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPreUpdate() {
		return preUpdateEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPreUpdate_Description() {
		return (EAttribute)preUpdateEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPreUpdate_MethodName() {
		return (EAttribute)preUpdateEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPrimaryKeyJoinColumn() {
		return primaryKeyJoinColumnEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPrimaryKeyJoinColumn_ColumnDefinition() {
		return (EAttribute)primaryKeyJoinColumnEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPrimaryKeyJoinColumn_Name() {
		return (EAttribute)primaryKeyJoinColumnEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPrimaryKeyJoinColumn_ReferencedColumnName() {
		return (EAttribute)primaryKeyJoinColumnEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getQueryHint() {
		return queryHintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getQueryHint_Description() {
		return (EAttribute)queryHintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getQueryHint_Name() {
		return (EAttribute)queryHintEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getQueryHint_Value() {
		return (EAttribute)queryHintEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSecondaryTable() {
		return secondaryTableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSecondaryTable_PrimaryKeyJoinColumn() {
		return (EReference)secondaryTableEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSecondaryTable_PrimaryKeyForeignKey() {
		return (EReference)secondaryTableEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSecondaryTable_UniqueConstraint() {
		return (EReference)secondaryTableEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSecondaryTable_Index() {
		return (EReference)secondaryTableEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSecondaryTable_Catalog() {
		return (EAttribute)secondaryTableEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSecondaryTable_Name() {
		return (EAttribute)secondaryTableEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSecondaryTable_Schema() {
		return (EAttribute)secondaryTableEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSequenceGenerator() {
		return sequenceGeneratorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSequenceGenerator_Description() {
		return (EAttribute)sequenceGeneratorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSequenceGenerator_AllocationSize() {
		return (EAttribute)sequenceGeneratorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSequenceGenerator_Catalog() {
		return (EAttribute)sequenceGeneratorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSequenceGenerator_InitialValue() {
		return (EAttribute)sequenceGeneratorEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSequenceGenerator_Name() {
		return (EAttribute)sequenceGeneratorEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSequenceGenerator_Schema() {
		return (EAttribute)sequenceGeneratorEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSequenceGenerator_SequenceName() {
		return (EAttribute)sequenceGeneratorEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSqlResultSetMapping() {
		return sqlResultSetMappingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSqlResultSetMapping_Description() {
		return (EAttribute)sqlResultSetMappingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSqlResultSetMapping_EntityResult() {
		return (EReference)sqlResultSetMappingEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSqlResultSetMapping_ConstructorResult() {
		return (EReference)sqlResultSetMappingEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSqlResultSetMapping_ColumnResult() {
		return (EReference)sqlResultSetMappingEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSqlResultSetMapping_Name() {
		return (EAttribute)sqlResultSetMappingEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getStoredProcedureParameter() {
		return storedProcedureParameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStoredProcedureParameter_Description() {
		return (EAttribute)storedProcedureParameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStoredProcedureParameter_Class() {
		return (EAttribute)storedProcedureParameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStoredProcedureParameter_Mode() {
		return (EAttribute)storedProcedureParameterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStoredProcedureParameter_Name() {
		return (EAttribute)storedProcedureParameterEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTable() {
		return tableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTable_UniqueConstraint() {
		return (EReference)tableEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTable_Index() {
		return (EReference)tableEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTable_Catalog() {
		return (EAttribute)tableEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTable_Name() {
		return (EAttribute)tableEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTable_Schema() {
		return (EAttribute)tableEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTableGenerator() {
		return tableGeneratorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTableGenerator_Description() {
		return (EAttribute)tableGeneratorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTableGenerator_UniqueConstraint() {
		return (EReference)tableGeneratorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTableGenerator_Index() {
		return (EReference)tableGeneratorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTableGenerator_AllocationSize() {
		return (EAttribute)tableGeneratorEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTableGenerator_Catalog() {
		return (EAttribute)tableGeneratorEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTableGenerator_InitialValue() {
		return (EAttribute)tableGeneratorEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTableGenerator_Name() {
		return (EAttribute)tableGeneratorEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTableGenerator_PkColumnName() {
		return (EAttribute)tableGeneratorEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTableGenerator_PkColumnValue() {
		return (EAttribute)tableGeneratorEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTableGenerator_Schema() {
		return (EAttribute)tableGeneratorEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTableGenerator_Table() {
		return (EAttribute)tableGeneratorEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTableGenerator_ValueColumnName() {
		return (EAttribute)tableGeneratorEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTransient() {
		return transientEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTransient_Name() {
		return (EAttribute)transientEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getUniqueConstraint() {
		return uniqueConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUniqueConstraint_ColumnName() {
		return (EAttribute)uniqueConstraintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUniqueConstraint_Name() {
		return (EAttribute)uniqueConstraintEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVersion() {
		return versionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVersion_Column() {
		return (EReference)versionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVersion_Temporal() {
		return (EAttribute)versionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVersion_Access() {
		return (EAttribute)versionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVersion_Name() {
		return (EAttribute)versionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getAccessType() {
		return accessTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getConstraintMode() {
		return constraintModeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getDiscriminatorType() {
		return discriminatorTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getEnumType() {
		return enumTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getFetchType() {
		return fetchTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getGenerationType() {
		return generationTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getInheritanceType() {
		return inheritanceTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getLockModeType() {
		return lockModeTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getParameterMode() {
		return parameterModeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getTemporalType() {
		return temporalTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getAccessTypeObject() {
		return accessTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getConstraintModeObject() {
		return constraintModeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getDiscriminatorTypeObject() {
		return discriminatorTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getDiscriminatorValue() {
		return discriminatorValueEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getEnumerated() {
		return enumeratedEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getEnumTypeObject() {
		return enumTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getFetchTypeObject() {
		return fetchTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getGenerationTypeObject() {
		return generationTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getInheritanceTypeObject() {
		return inheritanceTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getLockModeTypeObject() {
		return lockModeTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getOrderBy() {
		return orderByEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getParameterModeObject() {
		return parameterModeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getTemporal() {
		return temporalEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getTemporalTypeObject() {
		return temporalTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getVersionType() {
		return versionTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OrmFactory getOrmFactory() {
		return (OrmFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		associationOverrideEClass = createEClass(ASSOCIATION_OVERRIDE);
		createEAttribute(associationOverrideEClass, ASSOCIATION_OVERRIDE__DESCRIPTION);
		createEReference(associationOverrideEClass, ASSOCIATION_OVERRIDE__JOIN_COLUMN);
		createEReference(associationOverrideEClass, ASSOCIATION_OVERRIDE__FOREIGN_KEY);
		createEReference(associationOverrideEClass, ASSOCIATION_OVERRIDE__JOIN_TABLE);
		createEAttribute(associationOverrideEClass, ASSOCIATION_OVERRIDE__NAME);

		attributeOverrideEClass = createEClass(ATTRIBUTE_OVERRIDE);
		createEAttribute(attributeOverrideEClass, ATTRIBUTE_OVERRIDE__DESCRIPTION);
		createEReference(attributeOverrideEClass, ATTRIBUTE_OVERRIDE__COLUMN);
		createEAttribute(attributeOverrideEClass, ATTRIBUTE_OVERRIDE__NAME);

		attributesEClass = createEClass(ATTRIBUTES);
		createEAttribute(attributesEClass, ATTRIBUTES__DESCRIPTION);
		createEReference(attributesEClass, ATTRIBUTES__ID);
		createEReference(attributesEClass, ATTRIBUTES__EMBEDDED_ID);
		createEReference(attributesEClass, ATTRIBUTES__BASIC);
		createEReference(attributesEClass, ATTRIBUTES__VERSION);
		createEReference(attributesEClass, ATTRIBUTES__MANY_TO_ONE);
		createEReference(attributesEClass, ATTRIBUTES__ONE_TO_MANY);
		createEReference(attributesEClass, ATTRIBUTES__ONE_TO_ONE);
		createEReference(attributesEClass, ATTRIBUTES__MANY_TO_MANY);
		createEReference(attributesEClass, ATTRIBUTES__ELEMENT_COLLECTION);
		createEReference(attributesEClass, ATTRIBUTES__EMBEDDED);
		createEReference(attributesEClass, ATTRIBUTES__TRANSIENT);

		basicEClass = createEClass(BASIC);
		createEReference(basicEClass, BASIC__COLUMN);
		createEReference(basicEClass, BASIC__LOB);
		createEAttribute(basicEClass, BASIC__TEMPORAL);
		createEAttribute(basicEClass, BASIC__ENUMERATED);
		createEReference(basicEClass, BASIC__CONVERT);
		createEAttribute(basicEClass, BASIC__ACCESS);
		createEAttribute(basicEClass, BASIC__FETCH);
		createEAttribute(basicEClass, BASIC__NAME);
		createEAttribute(basicEClass, BASIC__OPTIONAL);

		cascadeTypeEClass = createEClass(CASCADE_TYPE);
		createEReference(cascadeTypeEClass, CASCADE_TYPE__CASCADE_ALL);
		createEReference(cascadeTypeEClass, CASCADE_TYPE__CASCADE_PERSIST);
		createEReference(cascadeTypeEClass, CASCADE_TYPE__CASCADE_MERGE);
		createEReference(cascadeTypeEClass, CASCADE_TYPE__CASCADE_REMOVE);
		createEReference(cascadeTypeEClass, CASCADE_TYPE__CASCADE_REFRESH);
		createEReference(cascadeTypeEClass, CASCADE_TYPE__CASCADE_DETACH);

		collectionTableEClass = createEClass(COLLECTION_TABLE);
		createEReference(collectionTableEClass, COLLECTION_TABLE__JOIN_COLUMN);
		createEReference(collectionTableEClass, COLLECTION_TABLE__FOREIGN_KEY);
		createEReference(collectionTableEClass, COLLECTION_TABLE__UNIQUE_CONSTRAINT);
		createEReference(collectionTableEClass, COLLECTION_TABLE__INDEX);
		createEAttribute(collectionTableEClass, COLLECTION_TABLE__CATALOG);
		createEAttribute(collectionTableEClass, COLLECTION_TABLE__NAME);
		createEAttribute(collectionTableEClass, COLLECTION_TABLE__SCHEMA);

		columnEClass = createEClass(COLUMN);
		createEAttribute(columnEClass, COLUMN__COLUMN_DEFINITION);
		createEAttribute(columnEClass, COLUMN__INSERTABLE);
		createEAttribute(columnEClass, COLUMN__LENGTH);
		createEAttribute(columnEClass, COLUMN__NAME);
		createEAttribute(columnEClass, COLUMN__NULLABLE);
		createEAttribute(columnEClass, COLUMN__PRECISION);
		createEAttribute(columnEClass, COLUMN__SCALE);
		createEAttribute(columnEClass, COLUMN__TABLE);
		createEAttribute(columnEClass, COLUMN__UNIQUE);
		createEAttribute(columnEClass, COLUMN__UPDATABLE);

		columnResultEClass = createEClass(COLUMN_RESULT);
		createEAttribute(columnResultEClass, COLUMN_RESULT__CLASS);
		createEAttribute(columnResultEClass, COLUMN_RESULT__NAME);

		constructorResultEClass = createEClass(CONSTRUCTOR_RESULT);
		createEReference(constructorResultEClass, CONSTRUCTOR_RESULT__COLUMN);
		createEAttribute(constructorResultEClass, CONSTRUCTOR_RESULT__TARGET_CLASS);

		convertEClass = createEClass(CONVERT);
		createEAttribute(convertEClass, CONVERT__DESCRIPTION);
		createEAttribute(convertEClass, CONVERT__ATTRIBUTE_NAME);
		createEAttribute(convertEClass, CONVERT__CONVERTER);
		createEAttribute(convertEClass, CONVERT__DISABLE_CONVERSION);

		converterEClass = createEClass(CONVERTER);
		createEAttribute(converterEClass, CONVERTER__DESCRIPTION);
		createEAttribute(converterEClass, CONVERTER__AUTO_APPLY);
		createEAttribute(converterEClass, CONVERTER__CLASS);

		discriminatorColumnEClass = createEClass(DISCRIMINATOR_COLUMN);
		createEAttribute(discriminatorColumnEClass, DISCRIMINATOR_COLUMN__COLUMN_DEFINITION);
		createEAttribute(discriminatorColumnEClass, DISCRIMINATOR_COLUMN__DISCRIMINATOR_TYPE);
		createEAttribute(discriminatorColumnEClass, DISCRIMINATOR_COLUMN__LENGTH);
		createEAttribute(discriminatorColumnEClass, DISCRIMINATOR_COLUMN__NAME);

		documentRootEClass = createEClass(DOCUMENT_ROOT);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__ENTITY_MAPPINGS);

		elementCollectionEClass = createEClass(ELEMENT_COLLECTION);
		createEAttribute(elementCollectionEClass, ELEMENT_COLLECTION__ORDER_BY);
		createEReference(elementCollectionEClass, ELEMENT_COLLECTION__ORDER_COLUMN);
		createEReference(elementCollectionEClass, ELEMENT_COLLECTION__MAP_KEY);
		createEReference(elementCollectionEClass, ELEMENT_COLLECTION__MAP_KEY_CLASS);
		createEAttribute(elementCollectionEClass, ELEMENT_COLLECTION__MAP_KEY_TEMPORAL);
		createEAttribute(elementCollectionEClass, ELEMENT_COLLECTION__MAP_KEY_ENUMERATED);
		createEReference(elementCollectionEClass, ELEMENT_COLLECTION__MAP_KEY_ATTRIBUTE_OVERRIDE);
		createEReference(elementCollectionEClass, ELEMENT_COLLECTION__MAP_KEY_CONVERT);
		createEReference(elementCollectionEClass, ELEMENT_COLLECTION__MAP_KEY_COLUMN);
		createEReference(elementCollectionEClass, ELEMENT_COLLECTION__MAP_KEY_JOIN_COLUMN);
		createEReference(elementCollectionEClass, ELEMENT_COLLECTION__MAP_KEY_FOREIGN_KEY);
		createEReference(elementCollectionEClass, ELEMENT_COLLECTION__COLUMN);
		createEAttribute(elementCollectionEClass, ELEMENT_COLLECTION__TEMPORAL);
		createEAttribute(elementCollectionEClass, ELEMENT_COLLECTION__ENUMERATED);
		createEReference(elementCollectionEClass, ELEMENT_COLLECTION__LOB);
		createEReference(elementCollectionEClass, ELEMENT_COLLECTION__ATTRIBUTE_OVERRIDE);
		createEReference(elementCollectionEClass, ELEMENT_COLLECTION__ASSOCIATION_OVERRIDE);
		createEReference(elementCollectionEClass, ELEMENT_COLLECTION__CONVERT);
		createEReference(elementCollectionEClass, ELEMENT_COLLECTION__COLLECTION_TABLE);
		createEAttribute(elementCollectionEClass, ELEMENT_COLLECTION__ACCESS);
		createEAttribute(elementCollectionEClass, ELEMENT_COLLECTION__FETCH);
		createEAttribute(elementCollectionEClass, ELEMENT_COLLECTION__NAME);
		createEAttribute(elementCollectionEClass, ELEMENT_COLLECTION__TARGET_CLASS);

		embeddableEClass = createEClass(EMBEDDABLE);
		createEAttribute(embeddableEClass, EMBEDDABLE__DESCRIPTION);
		createEReference(embeddableEClass, EMBEDDABLE__ATTRIBUTES);
		createEAttribute(embeddableEClass, EMBEDDABLE__ACCESS);
		createEAttribute(embeddableEClass, EMBEDDABLE__CLASS);
		createEAttribute(embeddableEClass, EMBEDDABLE__METADATA_COMPLETE);

		embeddableAttributesEClass = createEClass(EMBEDDABLE_ATTRIBUTES);
		createEReference(embeddableAttributesEClass, EMBEDDABLE_ATTRIBUTES__BASIC);
		createEReference(embeddableAttributesEClass, EMBEDDABLE_ATTRIBUTES__MANY_TO_ONE);
		createEReference(embeddableAttributesEClass, EMBEDDABLE_ATTRIBUTES__ONE_TO_MANY);
		createEReference(embeddableAttributesEClass, EMBEDDABLE_ATTRIBUTES__ONE_TO_ONE);
		createEReference(embeddableAttributesEClass, EMBEDDABLE_ATTRIBUTES__MANY_TO_MANY);
		createEReference(embeddableAttributesEClass, EMBEDDABLE_ATTRIBUTES__ELEMENT_COLLECTION);
		createEReference(embeddableAttributesEClass, EMBEDDABLE_ATTRIBUTES__EMBEDDED);
		createEReference(embeddableAttributesEClass, EMBEDDABLE_ATTRIBUTES__TRANSIENT);

		embeddedEClass = createEClass(EMBEDDED);
		createEReference(embeddedEClass, EMBEDDED__ATTRIBUTE_OVERRIDE);
		createEReference(embeddedEClass, EMBEDDED__ASSOCIATION_OVERRIDE);
		createEReference(embeddedEClass, EMBEDDED__CONVERT);
		createEAttribute(embeddedEClass, EMBEDDED__ACCESS);
		createEAttribute(embeddedEClass, EMBEDDED__NAME);

		embeddedIdEClass = createEClass(EMBEDDED_ID);
		createEReference(embeddedIdEClass, EMBEDDED_ID__ATTRIBUTE_OVERRIDE);
		createEAttribute(embeddedIdEClass, EMBEDDED_ID__ACCESS);
		createEAttribute(embeddedIdEClass, EMBEDDED_ID__NAME);

		emptyTypeEClass = createEClass(EMPTY_TYPE);

		entityEClass = createEClass(ENTITY);
		createEAttribute(entityEClass, ENTITY__DESCRIPTION);
		createEReference(entityEClass, ENTITY__TABLE);
		createEReference(entityEClass, ENTITY__SECONDARY_TABLE);
		createEReference(entityEClass, ENTITY__PRIMARY_KEY_JOIN_COLUMN);
		createEReference(entityEClass, ENTITY__PRIMARY_KEY_FOREIGN_KEY);
		createEReference(entityEClass, ENTITY__ID_CLASS);
		createEReference(entityEClass, ENTITY__INHERITANCE);
		createEAttribute(entityEClass, ENTITY__DISCRIMINATOR_VALUE);
		createEReference(entityEClass, ENTITY__DISCRIMINATOR_COLUMN);
		createEReference(entityEClass, ENTITY__SEQUENCE_GENERATOR);
		createEReference(entityEClass, ENTITY__TABLE_GENERATOR);
		createEReference(entityEClass, ENTITY__NAMED_QUERY);
		createEReference(entityEClass, ENTITY__NAMED_NATIVE_QUERY);
		createEReference(entityEClass, ENTITY__NAMED_STORED_PROCEDURE_QUERY);
		createEReference(entityEClass, ENTITY__SQL_RESULT_SET_MAPPING);
		createEReference(entityEClass, ENTITY__EXCLUDE_DEFAULT_LISTENERS);
		createEReference(entityEClass, ENTITY__EXCLUDE_SUPERCLASS_LISTENERS);
		createEReference(entityEClass, ENTITY__ENTITY_LISTENERS);
		createEReference(entityEClass, ENTITY__PRE_PERSIST);
		createEReference(entityEClass, ENTITY__POST_PERSIST);
		createEReference(entityEClass, ENTITY__PRE_REMOVE);
		createEReference(entityEClass, ENTITY__POST_REMOVE);
		createEReference(entityEClass, ENTITY__PRE_UPDATE);
		createEReference(entityEClass, ENTITY__POST_UPDATE);
		createEReference(entityEClass, ENTITY__POST_LOAD);
		createEReference(entityEClass, ENTITY__ATTRIBUTE_OVERRIDE);
		createEReference(entityEClass, ENTITY__ASSOCIATION_OVERRIDE);
		createEReference(entityEClass, ENTITY__CONVERT);
		createEReference(entityEClass, ENTITY__NAMED_ENTITY_GRAPH);
		createEReference(entityEClass, ENTITY__ATTRIBUTES);
		createEAttribute(entityEClass, ENTITY__ACCESS);
		createEAttribute(entityEClass, ENTITY__CACHEABLE);
		createEAttribute(entityEClass, ENTITY__CLASS);
		createEAttribute(entityEClass, ENTITY__METADATA_COMPLETE);
		createEAttribute(entityEClass, ENTITY__NAME);

		entityListenerEClass = createEClass(ENTITY_LISTENER);
		createEAttribute(entityListenerEClass, ENTITY_LISTENER__DESCRIPTION);
		createEReference(entityListenerEClass, ENTITY_LISTENER__PRE_PERSIST);
		createEReference(entityListenerEClass, ENTITY_LISTENER__POST_PERSIST);
		createEReference(entityListenerEClass, ENTITY_LISTENER__PRE_REMOVE);
		createEReference(entityListenerEClass, ENTITY_LISTENER__POST_REMOVE);
		createEReference(entityListenerEClass, ENTITY_LISTENER__PRE_UPDATE);
		createEReference(entityListenerEClass, ENTITY_LISTENER__POST_UPDATE);
		createEReference(entityListenerEClass, ENTITY_LISTENER__POST_LOAD);
		createEAttribute(entityListenerEClass, ENTITY_LISTENER__CLASS);

		entityListenersEClass = createEClass(ENTITY_LISTENERS);
		createEReference(entityListenersEClass, ENTITY_LISTENERS__ENTITY_LISTENER);

		entityMappingsTypeEClass = createEClass(ENTITY_MAPPINGS_TYPE);
		createEAttribute(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__DESCRIPTION);
		createEReference(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__PERSISTENCE_UNIT_METADATA);
		createEAttribute(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__PACKAGE);
		createEAttribute(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__SCHEMA);
		createEAttribute(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__CATALOG);
		createEAttribute(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__ACCESS);
		createEReference(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__SEQUENCE_GENERATOR);
		createEReference(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__TABLE_GENERATOR);
		createEReference(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__NAMED_QUERY);
		createEReference(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__NAMED_NATIVE_QUERY);
		createEReference(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__NAMED_STORED_PROCEDURE_QUERY);
		createEReference(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__SQL_RESULT_SET_MAPPING);
		createEReference(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__MAPPED_SUPERCLASS);
		createEReference(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__ENTITY);
		createEReference(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__EMBEDDABLE);
		createEReference(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__CONVERTER);
		createEAttribute(entityMappingsTypeEClass, ENTITY_MAPPINGS_TYPE__VERSION);

		entityResultEClass = createEClass(ENTITY_RESULT);
		createEReference(entityResultEClass, ENTITY_RESULT__FIELD_RESULT);
		createEAttribute(entityResultEClass, ENTITY_RESULT__DISCRIMINATOR_COLUMN);
		createEAttribute(entityResultEClass, ENTITY_RESULT__ENTITY_CLASS);

		fieldResultEClass = createEClass(FIELD_RESULT);
		createEAttribute(fieldResultEClass, FIELD_RESULT__COLUMN);
		createEAttribute(fieldResultEClass, FIELD_RESULT__NAME);

		foreignKeyEClass = createEClass(FOREIGN_KEY);
		createEAttribute(foreignKeyEClass, FOREIGN_KEY__DESCRIPTION);
		createEAttribute(foreignKeyEClass, FOREIGN_KEY__CONSTRAINT_MODE);
		createEAttribute(foreignKeyEClass, FOREIGN_KEY__FOREIGN_KEY_DEFINITION);
		createEAttribute(foreignKeyEClass, FOREIGN_KEY__NAME);

		generatedValueEClass = createEClass(GENERATED_VALUE);
		createEAttribute(generatedValueEClass, GENERATED_VALUE__GENERATOR);
		createEAttribute(generatedValueEClass, GENERATED_VALUE__STRATEGY);

		idEClass = createEClass(ID);
		createEReference(idEClass, ID__COLUMN);
		createEReference(idEClass, ID__GENERATED_VALUE);
		createEAttribute(idEClass, ID__TEMPORAL);
		createEReference(idEClass, ID__TABLE_GENERATOR);
		createEReference(idEClass, ID__SEQUENCE_GENERATOR);
		createEAttribute(idEClass, ID__ACCESS);
		createEAttribute(idEClass, ID__NAME);

		idClassEClass = createEClass(ID_CLASS);
		createEAttribute(idClassEClass, ID_CLASS__CLASS);

		indexEClass = createEClass(INDEX);
		createEAttribute(indexEClass, INDEX__DESCRIPTION);
		createEAttribute(indexEClass, INDEX__COLUMN_LIST);
		createEAttribute(indexEClass, INDEX__NAME);
		createEAttribute(indexEClass, INDEX__UNIQUE);

		inheritanceEClass = createEClass(INHERITANCE);
		createEAttribute(inheritanceEClass, INHERITANCE__STRATEGY);

		joinColumnEClass = createEClass(JOIN_COLUMN);
		createEAttribute(joinColumnEClass, JOIN_COLUMN__COLUMN_DEFINITION);
		createEAttribute(joinColumnEClass, JOIN_COLUMN__INSERTABLE);
		createEAttribute(joinColumnEClass, JOIN_COLUMN__NAME);
		createEAttribute(joinColumnEClass, JOIN_COLUMN__NULLABLE);
		createEAttribute(joinColumnEClass, JOIN_COLUMN__REFERENCED_COLUMN_NAME);
		createEAttribute(joinColumnEClass, JOIN_COLUMN__TABLE);
		createEAttribute(joinColumnEClass, JOIN_COLUMN__UNIQUE);
		createEAttribute(joinColumnEClass, JOIN_COLUMN__UPDATABLE);

		joinTableEClass = createEClass(JOIN_TABLE);
		createEReference(joinTableEClass, JOIN_TABLE__JOIN_COLUMN);
		createEReference(joinTableEClass, JOIN_TABLE__FOREIGN_KEY);
		createEReference(joinTableEClass, JOIN_TABLE__INVERSE_JOIN_COLUMN);
		createEReference(joinTableEClass, JOIN_TABLE__INVERSE_FOREIGN_KEY);
		createEReference(joinTableEClass, JOIN_TABLE__UNIQUE_CONSTRAINT);
		createEReference(joinTableEClass, JOIN_TABLE__INDEX);
		createEAttribute(joinTableEClass, JOIN_TABLE__CATALOG);
		createEAttribute(joinTableEClass, JOIN_TABLE__NAME);
		createEAttribute(joinTableEClass, JOIN_TABLE__SCHEMA);

		lobEClass = createEClass(LOB);

		manyToManyEClass = createEClass(MANY_TO_MANY);
		createEAttribute(manyToManyEClass, MANY_TO_MANY__ORDER_BY);
		createEReference(manyToManyEClass, MANY_TO_MANY__ORDER_COLUMN);
		createEReference(manyToManyEClass, MANY_TO_MANY__MAP_KEY);
		createEReference(manyToManyEClass, MANY_TO_MANY__MAP_KEY_CLASS);
		createEAttribute(manyToManyEClass, MANY_TO_MANY__MAP_KEY_TEMPORAL);
		createEAttribute(manyToManyEClass, MANY_TO_MANY__MAP_KEY_ENUMERATED);
		createEReference(manyToManyEClass, MANY_TO_MANY__MAP_KEY_ATTRIBUTE_OVERRIDE);
		createEReference(manyToManyEClass, MANY_TO_MANY__MAP_KEY_CONVERT);
		createEReference(manyToManyEClass, MANY_TO_MANY__MAP_KEY_COLUMN);
		createEReference(manyToManyEClass, MANY_TO_MANY__MAP_KEY_JOIN_COLUMN);
		createEReference(manyToManyEClass, MANY_TO_MANY__MAP_KEY_FOREIGN_KEY);
		createEReference(manyToManyEClass, MANY_TO_MANY__JOIN_TABLE);
		createEReference(manyToManyEClass, MANY_TO_MANY__CASCADE);
		createEAttribute(manyToManyEClass, MANY_TO_MANY__ACCESS);
		createEAttribute(manyToManyEClass, MANY_TO_MANY__FETCH);
		createEAttribute(manyToManyEClass, MANY_TO_MANY__MAPPED_BY);
		createEAttribute(manyToManyEClass, MANY_TO_MANY__NAME);
		createEAttribute(manyToManyEClass, MANY_TO_MANY__TARGET_ENTITY);

		manyToOneEClass = createEClass(MANY_TO_ONE);
		createEReference(manyToOneEClass, MANY_TO_ONE__JOIN_COLUMN);
		createEReference(manyToOneEClass, MANY_TO_ONE__FOREIGN_KEY);
		createEReference(manyToOneEClass, MANY_TO_ONE__JOIN_TABLE);
		createEReference(manyToOneEClass, MANY_TO_ONE__CASCADE);
		createEAttribute(manyToOneEClass, MANY_TO_ONE__ACCESS);
		createEAttribute(manyToOneEClass, MANY_TO_ONE__FETCH);
		createEAttribute(manyToOneEClass, MANY_TO_ONE__ID);
		createEAttribute(manyToOneEClass, MANY_TO_ONE__MAPS_ID);
		createEAttribute(manyToOneEClass, MANY_TO_ONE__NAME);
		createEAttribute(manyToOneEClass, MANY_TO_ONE__OPTIONAL);
		createEAttribute(manyToOneEClass, MANY_TO_ONE__TARGET_ENTITY);

		mapKeyEClass = createEClass(MAP_KEY);
		createEAttribute(mapKeyEClass, MAP_KEY__NAME);

		mapKeyClassEClass = createEClass(MAP_KEY_CLASS);
		createEAttribute(mapKeyClassEClass, MAP_KEY_CLASS__CLASS);

		mapKeyColumnEClass = createEClass(MAP_KEY_COLUMN);
		createEAttribute(mapKeyColumnEClass, MAP_KEY_COLUMN__COLUMN_DEFINITION);
		createEAttribute(mapKeyColumnEClass, MAP_KEY_COLUMN__INSERTABLE);
		createEAttribute(mapKeyColumnEClass, MAP_KEY_COLUMN__LENGTH);
		createEAttribute(mapKeyColumnEClass, MAP_KEY_COLUMN__NAME);
		createEAttribute(mapKeyColumnEClass, MAP_KEY_COLUMN__NULLABLE);
		createEAttribute(mapKeyColumnEClass, MAP_KEY_COLUMN__PRECISION);
		createEAttribute(mapKeyColumnEClass, MAP_KEY_COLUMN__SCALE);
		createEAttribute(mapKeyColumnEClass, MAP_KEY_COLUMN__TABLE);
		createEAttribute(mapKeyColumnEClass, MAP_KEY_COLUMN__UNIQUE);
		createEAttribute(mapKeyColumnEClass, MAP_KEY_COLUMN__UPDATABLE);

		mapKeyJoinColumnEClass = createEClass(MAP_KEY_JOIN_COLUMN);
		createEAttribute(mapKeyJoinColumnEClass, MAP_KEY_JOIN_COLUMN__COLUMN_DEFINITION);
		createEAttribute(mapKeyJoinColumnEClass, MAP_KEY_JOIN_COLUMN__INSERTABLE);
		createEAttribute(mapKeyJoinColumnEClass, MAP_KEY_JOIN_COLUMN__NAME);
		createEAttribute(mapKeyJoinColumnEClass, MAP_KEY_JOIN_COLUMN__NULLABLE);
		createEAttribute(mapKeyJoinColumnEClass, MAP_KEY_JOIN_COLUMN__REFERENCED_COLUMN_NAME);
		createEAttribute(mapKeyJoinColumnEClass, MAP_KEY_JOIN_COLUMN__TABLE);
		createEAttribute(mapKeyJoinColumnEClass, MAP_KEY_JOIN_COLUMN__UNIQUE);
		createEAttribute(mapKeyJoinColumnEClass, MAP_KEY_JOIN_COLUMN__UPDATABLE);

		mappedSuperclassEClass = createEClass(MAPPED_SUPERCLASS);
		createEAttribute(mappedSuperclassEClass, MAPPED_SUPERCLASS__DESCRIPTION);
		createEReference(mappedSuperclassEClass, MAPPED_SUPERCLASS__ID_CLASS);
		createEReference(mappedSuperclassEClass, MAPPED_SUPERCLASS__EXCLUDE_DEFAULT_LISTENERS);
		createEReference(mappedSuperclassEClass, MAPPED_SUPERCLASS__EXCLUDE_SUPERCLASS_LISTENERS);
		createEReference(mappedSuperclassEClass, MAPPED_SUPERCLASS__ENTITY_LISTENERS);
		createEReference(mappedSuperclassEClass, MAPPED_SUPERCLASS__PRE_PERSIST);
		createEReference(mappedSuperclassEClass, MAPPED_SUPERCLASS__POST_PERSIST);
		createEReference(mappedSuperclassEClass, MAPPED_SUPERCLASS__PRE_REMOVE);
		createEReference(mappedSuperclassEClass, MAPPED_SUPERCLASS__POST_REMOVE);
		createEReference(mappedSuperclassEClass, MAPPED_SUPERCLASS__PRE_UPDATE);
		createEReference(mappedSuperclassEClass, MAPPED_SUPERCLASS__POST_UPDATE);
		createEReference(mappedSuperclassEClass, MAPPED_SUPERCLASS__POST_LOAD);
		createEReference(mappedSuperclassEClass, MAPPED_SUPERCLASS__ATTRIBUTES);
		createEAttribute(mappedSuperclassEClass, MAPPED_SUPERCLASS__ACCESS);
		createEAttribute(mappedSuperclassEClass, MAPPED_SUPERCLASS__CLASS);
		createEAttribute(mappedSuperclassEClass, MAPPED_SUPERCLASS__METADATA_COMPLETE);

		namedAttributeNodeEClass = createEClass(NAMED_ATTRIBUTE_NODE);
		createEAttribute(namedAttributeNodeEClass, NAMED_ATTRIBUTE_NODE__KEY_SUBGRAPH);
		createEAttribute(namedAttributeNodeEClass, NAMED_ATTRIBUTE_NODE__NAME);
		createEAttribute(namedAttributeNodeEClass, NAMED_ATTRIBUTE_NODE__SUBGRAPH);

		namedEntityGraphEClass = createEClass(NAMED_ENTITY_GRAPH);
		createEReference(namedEntityGraphEClass, NAMED_ENTITY_GRAPH__NAMED_ATTRIBUTE_NODE);
		createEReference(namedEntityGraphEClass, NAMED_ENTITY_GRAPH__SUBGRAPH);
		createEReference(namedEntityGraphEClass, NAMED_ENTITY_GRAPH__SUBCLASS_SUBGRAPH);
		createEAttribute(namedEntityGraphEClass, NAMED_ENTITY_GRAPH__INCLUDE_ALL_ATTRIBUTES);
		createEAttribute(namedEntityGraphEClass, NAMED_ENTITY_GRAPH__NAME);

		namedNativeQueryEClass = createEClass(NAMED_NATIVE_QUERY);
		createEAttribute(namedNativeQueryEClass, NAMED_NATIVE_QUERY__DESCRIPTION);
		createEAttribute(namedNativeQueryEClass, NAMED_NATIVE_QUERY__QUERY);
		createEReference(namedNativeQueryEClass, NAMED_NATIVE_QUERY__HINT);
		createEAttribute(namedNativeQueryEClass, NAMED_NATIVE_QUERY__NAME);
		createEAttribute(namedNativeQueryEClass, NAMED_NATIVE_QUERY__RESULT_CLASS);
		createEAttribute(namedNativeQueryEClass, NAMED_NATIVE_QUERY__RESULT_SET_MAPPING);

		namedQueryEClass = createEClass(NAMED_QUERY);
		createEAttribute(namedQueryEClass, NAMED_QUERY__DESCRIPTION);
		createEAttribute(namedQueryEClass, NAMED_QUERY__QUERY);
		createEAttribute(namedQueryEClass, NAMED_QUERY__LOCK_MODE);
		createEReference(namedQueryEClass, NAMED_QUERY__HINT);
		createEAttribute(namedQueryEClass, NAMED_QUERY__NAME);

		namedStoredProcedureQueryEClass = createEClass(NAMED_STORED_PROCEDURE_QUERY);
		createEAttribute(namedStoredProcedureQueryEClass, NAMED_STORED_PROCEDURE_QUERY__DESCRIPTION);
		createEReference(namedStoredProcedureQueryEClass, NAMED_STORED_PROCEDURE_QUERY__PARAMETER);
		createEAttribute(namedStoredProcedureQueryEClass, NAMED_STORED_PROCEDURE_QUERY__RESULT_CLASS);
		createEAttribute(namedStoredProcedureQueryEClass, NAMED_STORED_PROCEDURE_QUERY__RESULT_SET_MAPPING);
		createEReference(namedStoredProcedureQueryEClass, NAMED_STORED_PROCEDURE_QUERY__HINT);
		createEAttribute(namedStoredProcedureQueryEClass, NAMED_STORED_PROCEDURE_QUERY__NAME);
		createEAttribute(namedStoredProcedureQueryEClass, NAMED_STORED_PROCEDURE_QUERY__PROCEDURE_NAME);

		namedSubgraphEClass = createEClass(NAMED_SUBGRAPH);
		createEReference(namedSubgraphEClass, NAMED_SUBGRAPH__NAMED_ATTRIBUTE_NODE);
		createEAttribute(namedSubgraphEClass, NAMED_SUBGRAPH__CLASS);
		createEAttribute(namedSubgraphEClass, NAMED_SUBGRAPH__NAME);

		oneToManyEClass = createEClass(ONE_TO_MANY);
		createEAttribute(oneToManyEClass, ONE_TO_MANY__ORDER_BY);
		createEReference(oneToManyEClass, ONE_TO_MANY__ORDER_COLUMN);
		createEReference(oneToManyEClass, ONE_TO_MANY__MAP_KEY);
		createEReference(oneToManyEClass, ONE_TO_MANY__MAP_KEY_CLASS);
		createEAttribute(oneToManyEClass, ONE_TO_MANY__MAP_KEY_TEMPORAL);
		createEAttribute(oneToManyEClass, ONE_TO_MANY__MAP_KEY_ENUMERATED);
		createEReference(oneToManyEClass, ONE_TO_MANY__MAP_KEY_ATTRIBUTE_OVERRIDE);
		createEReference(oneToManyEClass, ONE_TO_MANY__MAP_KEY_CONVERT);
		createEReference(oneToManyEClass, ONE_TO_MANY__MAP_KEY_COLUMN);
		createEReference(oneToManyEClass, ONE_TO_MANY__MAP_KEY_JOIN_COLUMN);
		createEReference(oneToManyEClass, ONE_TO_MANY__MAP_KEY_FOREIGN_KEY);
		createEReference(oneToManyEClass, ONE_TO_MANY__JOIN_TABLE);
		createEReference(oneToManyEClass, ONE_TO_MANY__JOIN_COLUMN);
		createEReference(oneToManyEClass, ONE_TO_MANY__FOREIGN_KEY);
		createEReference(oneToManyEClass, ONE_TO_MANY__CASCADE);
		createEAttribute(oneToManyEClass, ONE_TO_MANY__ACCESS);
		createEAttribute(oneToManyEClass, ONE_TO_MANY__FETCH);
		createEAttribute(oneToManyEClass, ONE_TO_MANY__MAPPED_BY);
		createEAttribute(oneToManyEClass, ONE_TO_MANY__NAME);
		createEAttribute(oneToManyEClass, ONE_TO_MANY__ORPHAN_REMOVAL);
		createEAttribute(oneToManyEClass, ONE_TO_MANY__TARGET_ENTITY);

		oneToOneEClass = createEClass(ONE_TO_ONE);
		createEReference(oneToOneEClass, ONE_TO_ONE__PRIMARY_KEY_JOIN_COLUMN);
		createEReference(oneToOneEClass, ONE_TO_ONE__PRIMARY_KEY_FOREIGN_KEY);
		createEReference(oneToOneEClass, ONE_TO_ONE__JOIN_COLUMN);
		createEReference(oneToOneEClass, ONE_TO_ONE__FOREIGN_KEY);
		createEReference(oneToOneEClass, ONE_TO_ONE__JOIN_TABLE);
		createEReference(oneToOneEClass, ONE_TO_ONE__CASCADE);
		createEAttribute(oneToOneEClass, ONE_TO_ONE__ACCESS);
		createEAttribute(oneToOneEClass, ONE_TO_ONE__FETCH);
		createEAttribute(oneToOneEClass, ONE_TO_ONE__ID);
		createEAttribute(oneToOneEClass, ONE_TO_ONE__MAPPED_BY);
		createEAttribute(oneToOneEClass, ONE_TO_ONE__MAPS_ID);
		createEAttribute(oneToOneEClass, ONE_TO_ONE__NAME);
		createEAttribute(oneToOneEClass, ONE_TO_ONE__OPTIONAL);
		createEAttribute(oneToOneEClass, ONE_TO_ONE__ORPHAN_REMOVAL);
		createEAttribute(oneToOneEClass, ONE_TO_ONE__TARGET_ENTITY);

		orderColumnEClass = createEClass(ORDER_COLUMN);
		createEAttribute(orderColumnEClass, ORDER_COLUMN__COLUMN_DEFINITION);
		createEAttribute(orderColumnEClass, ORDER_COLUMN__INSERTABLE);
		createEAttribute(orderColumnEClass, ORDER_COLUMN__NAME);
		createEAttribute(orderColumnEClass, ORDER_COLUMN__NULLABLE);
		createEAttribute(orderColumnEClass, ORDER_COLUMN__UPDATABLE);

		persistenceUnitDefaultsEClass = createEClass(PERSISTENCE_UNIT_DEFAULTS);
		createEAttribute(persistenceUnitDefaultsEClass, PERSISTENCE_UNIT_DEFAULTS__DESCRIPTION);
		createEAttribute(persistenceUnitDefaultsEClass, PERSISTENCE_UNIT_DEFAULTS__SCHEMA);
		createEAttribute(persistenceUnitDefaultsEClass, PERSISTENCE_UNIT_DEFAULTS__CATALOG);
		createEReference(persistenceUnitDefaultsEClass, PERSISTENCE_UNIT_DEFAULTS__DELIMITED_IDENTIFIERS);
		createEAttribute(persistenceUnitDefaultsEClass, PERSISTENCE_UNIT_DEFAULTS__ACCESS);
		createEReference(persistenceUnitDefaultsEClass, PERSISTENCE_UNIT_DEFAULTS__CASCADE_PERSIST);
		createEReference(persistenceUnitDefaultsEClass, PERSISTENCE_UNIT_DEFAULTS__ENTITY_LISTENERS);

		persistenceUnitMetadataEClass = createEClass(PERSISTENCE_UNIT_METADATA);
		createEAttribute(persistenceUnitMetadataEClass, PERSISTENCE_UNIT_METADATA__DESCRIPTION);
		createEReference(persistenceUnitMetadataEClass, PERSISTENCE_UNIT_METADATA__XML_MAPPING_METADATA_COMPLETE);
		createEReference(persistenceUnitMetadataEClass, PERSISTENCE_UNIT_METADATA__PERSISTENCE_UNIT_DEFAULTS);

		postLoadEClass = createEClass(POST_LOAD);
		createEAttribute(postLoadEClass, POST_LOAD__DESCRIPTION);
		createEAttribute(postLoadEClass, POST_LOAD__METHOD_NAME);

		postPersistEClass = createEClass(POST_PERSIST);
		createEAttribute(postPersistEClass, POST_PERSIST__DESCRIPTION);
		createEAttribute(postPersistEClass, POST_PERSIST__METHOD_NAME);

		postRemoveEClass = createEClass(POST_REMOVE);
		createEAttribute(postRemoveEClass, POST_REMOVE__DESCRIPTION);
		createEAttribute(postRemoveEClass, POST_REMOVE__METHOD_NAME);

		postUpdateEClass = createEClass(POST_UPDATE);
		createEAttribute(postUpdateEClass, POST_UPDATE__DESCRIPTION);
		createEAttribute(postUpdateEClass, POST_UPDATE__METHOD_NAME);

		prePersistEClass = createEClass(PRE_PERSIST);
		createEAttribute(prePersistEClass, PRE_PERSIST__DESCRIPTION);
		createEAttribute(prePersistEClass, PRE_PERSIST__METHOD_NAME);

		preRemoveEClass = createEClass(PRE_REMOVE);
		createEAttribute(preRemoveEClass, PRE_REMOVE__DESCRIPTION);
		createEAttribute(preRemoveEClass, PRE_REMOVE__METHOD_NAME);

		preUpdateEClass = createEClass(PRE_UPDATE);
		createEAttribute(preUpdateEClass, PRE_UPDATE__DESCRIPTION);
		createEAttribute(preUpdateEClass, PRE_UPDATE__METHOD_NAME);

		primaryKeyJoinColumnEClass = createEClass(PRIMARY_KEY_JOIN_COLUMN);
		createEAttribute(primaryKeyJoinColumnEClass, PRIMARY_KEY_JOIN_COLUMN__COLUMN_DEFINITION);
		createEAttribute(primaryKeyJoinColumnEClass, PRIMARY_KEY_JOIN_COLUMN__NAME);
		createEAttribute(primaryKeyJoinColumnEClass, PRIMARY_KEY_JOIN_COLUMN__REFERENCED_COLUMN_NAME);

		queryHintEClass = createEClass(QUERY_HINT);
		createEAttribute(queryHintEClass, QUERY_HINT__DESCRIPTION);
		createEAttribute(queryHintEClass, QUERY_HINT__NAME);
		createEAttribute(queryHintEClass, QUERY_HINT__VALUE);

		secondaryTableEClass = createEClass(SECONDARY_TABLE);
		createEReference(secondaryTableEClass, SECONDARY_TABLE__PRIMARY_KEY_JOIN_COLUMN);
		createEReference(secondaryTableEClass, SECONDARY_TABLE__PRIMARY_KEY_FOREIGN_KEY);
		createEReference(secondaryTableEClass, SECONDARY_TABLE__UNIQUE_CONSTRAINT);
		createEReference(secondaryTableEClass, SECONDARY_TABLE__INDEX);
		createEAttribute(secondaryTableEClass, SECONDARY_TABLE__CATALOG);
		createEAttribute(secondaryTableEClass, SECONDARY_TABLE__NAME);
		createEAttribute(secondaryTableEClass, SECONDARY_TABLE__SCHEMA);

		sequenceGeneratorEClass = createEClass(SEQUENCE_GENERATOR);
		createEAttribute(sequenceGeneratorEClass, SEQUENCE_GENERATOR__DESCRIPTION);
		createEAttribute(sequenceGeneratorEClass, SEQUENCE_GENERATOR__ALLOCATION_SIZE);
		createEAttribute(sequenceGeneratorEClass, SEQUENCE_GENERATOR__CATALOG);
		createEAttribute(sequenceGeneratorEClass, SEQUENCE_GENERATOR__INITIAL_VALUE);
		createEAttribute(sequenceGeneratorEClass, SEQUENCE_GENERATOR__NAME);
		createEAttribute(sequenceGeneratorEClass, SEQUENCE_GENERATOR__SCHEMA);
		createEAttribute(sequenceGeneratorEClass, SEQUENCE_GENERATOR__SEQUENCE_NAME);

		sqlResultSetMappingEClass = createEClass(SQL_RESULT_SET_MAPPING);
		createEAttribute(sqlResultSetMappingEClass, SQL_RESULT_SET_MAPPING__DESCRIPTION);
		createEReference(sqlResultSetMappingEClass, SQL_RESULT_SET_MAPPING__ENTITY_RESULT);
		createEReference(sqlResultSetMappingEClass, SQL_RESULT_SET_MAPPING__CONSTRUCTOR_RESULT);
		createEReference(sqlResultSetMappingEClass, SQL_RESULT_SET_MAPPING__COLUMN_RESULT);
		createEAttribute(sqlResultSetMappingEClass, SQL_RESULT_SET_MAPPING__NAME);

		storedProcedureParameterEClass = createEClass(STORED_PROCEDURE_PARAMETER);
		createEAttribute(storedProcedureParameterEClass, STORED_PROCEDURE_PARAMETER__DESCRIPTION);
		createEAttribute(storedProcedureParameterEClass, STORED_PROCEDURE_PARAMETER__CLASS);
		createEAttribute(storedProcedureParameterEClass, STORED_PROCEDURE_PARAMETER__MODE);
		createEAttribute(storedProcedureParameterEClass, STORED_PROCEDURE_PARAMETER__NAME);

		tableEClass = createEClass(TABLE);
		createEReference(tableEClass, TABLE__UNIQUE_CONSTRAINT);
		createEReference(tableEClass, TABLE__INDEX);
		createEAttribute(tableEClass, TABLE__CATALOG);
		createEAttribute(tableEClass, TABLE__NAME);
		createEAttribute(tableEClass, TABLE__SCHEMA);

		tableGeneratorEClass = createEClass(TABLE_GENERATOR);
		createEAttribute(tableGeneratorEClass, TABLE_GENERATOR__DESCRIPTION);
		createEReference(tableGeneratorEClass, TABLE_GENERATOR__UNIQUE_CONSTRAINT);
		createEReference(tableGeneratorEClass, TABLE_GENERATOR__INDEX);
		createEAttribute(tableGeneratorEClass, TABLE_GENERATOR__ALLOCATION_SIZE);
		createEAttribute(tableGeneratorEClass, TABLE_GENERATOR__CATALOG);
		createEAttribute(tableGeneratorEClass, TABLE_GENERATOR__INITIAL_VALUE);
		createEAttribute(tableGeneratorEClass, TABLE_GENERATOR__NAME);
		createEAttribute(tableGeneratorEClass, TABLE_GENERATOR__PK_COLUMN_NAME);
		createEAttribute(tableGeneratorEClass, TABLE_GENERATOR__PK_COLUMN_VALUE);
		createEAttribute(tableGeneratorEClass, TABLE_GENERATOR__SCHEMA);
		createEAttribute(tableGeneratorEClass, TABLE_GENERATOR__TABLE);
		createEAttribute(tableGeneratorEClass, TABLE_GENERATOR__VALUE_COLUMN_NAME);

		transientEClass = createEClass(TRANSIENT);
		createEAttribute(transientEClass, TRANSIENT__NAME);

		uniqueConstraintEClass = createEClass(UNIQUE_CONSTRAINT);
		createEAttribute(uniqueConstraintEClass, UNIQUE_CONSTRAINT__COLUMN_NAME);
		createEAttribute(uniqueConstraintEClass, UNIQUE_CONSTRAINT__NAME);

		versionEClass = createEClass(VERSION);
		createEReference(versionEClass, VERSION__COLUMN);
		createEAttribute(versionEClass, VERSION__TEMPORAL);
		createEAttribute(versionEClass, VERSION__ACCESS);
		createEAttribute(versionEClass, VERSION__NAME);

		// Create enums
		accessTypeEEnum = createEEnum(ACCESS_TYPE);
		constraintModeEEnum = createEEnum(CONSTRAINT_MODE);
		discriminatorTypeEEnum = createEEnum(DISCRIMINATOR_TYPE);
		enumTypeEEnum = createEEnum(ENUM_TYPE);
		fetchTypeEEnum = createEEnum(FETCH_TYPE);
		generationTypeEEnum = createEEnum(GENERATION_TYPE);
		inheritanceTypeEEnum = createEEnum(INHERITANCE_TYPE);
		lockModeTypeEEnum = createEEnum(LOCK_MODE_TYPE);
		parameterModeEEnum = createEEnum(PARAMETER_MODE);
		temporalTypeEEnum = createEEnum(TEMPORAL_TYPE);

		// Create data types
		accessTypeObjectEDataType = createEDataType(ACCESS_TYPE_OBJECT);
		constraintModeObjectEDataType = createEDataType(CONSTRAINT_MODE_OBJECT);
		discriminatorTypeObjectEDataType = createEDataType(DISCRIMINATOR_TYPE_OBJECT);
		discriminatorValueEDataType = createEDataType(DISCRIMINATOR_VALUE);
		enumeratedEDataType = createEDataType(ENUMERATED);
		enumTypeObjectEDataType = createEDataType(ENUM_TYPE_OBJECT);
		fetchTypeObjectEDataType = createEDataType(FETCH_TYPE_OBJECT);
		generationTypeObjectEDataType = createEDataType(GENERATION_TYPE_OBJECT);
		inheritanceTypeObjectEDataType = createEDataType(INHERITANCE_TYPE_OBJECT);
		lockModeTypeObjectEDataType = createEDataType(LOCK_MODE_TYPE_OBJECT);
		orderByEDataType = createEDataType(ORDER_BY);
		parameterModeObjectEDataType = createEDataType(PARAMETER_MODE_OBJECT);
		temporalEDataType = createEDataType(TEMPORAL);
		temporalTypeObjectEDataType = createEDataType(TEMPORAL_TYPE_OBJECT);
		versionTypeEDataType = createEDataType(VERSION_TYPE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(associationOverrideEClass, AssociationOverride.class, "AssociationOverride", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAssociationOverride_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, AssociationOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAssociationOverride_JoinColumn(), this.getJoinColumn(), null, "joinColumn", null, 0, -1, AssociationOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAssociationOverride_ForeignKey(), this.getForeignKey(), null, "foreignKey", null, 0, 1, AssociationOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAssociationOverride_JoinTable(), this.getJoinTable(), null, "joinTable", null, 0, 1, AssociationOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationOverride_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, AssociationOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(attributeOverrideEClass, AttributeOverride.class, "AttributeOverride", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAttributeOverride_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, AttributeOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAttributeOverride_Column(), this.getColumn(), null, "column", null, 1, 1, AttributeOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAttributeOverride_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, AttributeOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(attributesEClass, Attributes.class, "Attributes", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAttributes_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, Attributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAttributes_Id(), this.getId(), null, "id", null, 0, -1, Attributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAttributes_EmbeddedId(), this.getEmbeddedId(), null, "embeddedId", null, 0, 1, Attributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAttributes_Basic(), this.getBasic(), null, "basic", null, 0, -1, Attributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAttributes_Version(), this.getVersion(), null, "version", null, 0, -1, Attributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAttributes_ManyToOne(), this.getManyToOne(), null, "manyToOne", null, 0, -1, Attributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAttributes_OneToMany(), this.getOneToMany(), null, "oneToMany", null, 0, -1, Attributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAttributes_OneToOne(), this.getOneToOne(), null, "oneToOne", null, 0, -1, Attributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAttributes_ManyToMany(), this.getManyToMany(), null, "manyToMany", null, 0, -1, Attributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAttributes_ElementCollection(), this.getElementCollection(), null, "elementCollection", null, 0, -1, Attributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAttributes_Embedded(), this.getEmbedded(), null, "embedded", null, 0, -1, Attributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAttributes_Transient(), this.getTransient(), null, "transient", null, 0, -1, Attributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(basicEClass, Basic.class, "Basic", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBasic_Column(), this.getColumn(), null, "column", null, 0, 1, Basic.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBasic_Lob(), this.getLob(), null, "lob", null, 0, 1, Basic.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBasic_Temporal(), this.getTemporal(), "temporal", null, 0, 1, Basic.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBasic_Enumerated(), this.getEnumerated(), "enumerated", null, 0, 1, Basic.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBasic_Convert(), this.getConvert(), null, "convert", null, 0, 1, Basic.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBasic_Access(), this.getAccessType(), "access", null, 0, 1, Basic.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBasic_Fetch(), this.getFetchType(), "fetch", null, 0, 1, Basic.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBasic_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, Basic.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBasic_Optional(), theXMLTypePackage.getBoolean(), "optional", null, 0, 1, Basic.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cascadeTypeEClass, CascadeType.class, "CascadeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCascadeType_CascadeAll(), this.getEmptyType(), null, "cascadeAll", null, 0, 1, CascadeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCascadeType_CascadePersist(), this.getEmptyType(), null, "cascadePersist", null, 0, 1, CascadeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCascadeType_CascadeMerge(), this.getEmptyType(), null, "cascadeMerge", null, 0, 1, CascadeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCascadeType_CascadeRemove(), this.getEmptyType(), null, "cascadeRemove", null, 0, 1, CascadeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCascadeType_CascadeRefresh(), this.getEmptyType(), null, "cascadeRefresh", null, 0, 1, CascadeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCascadeType_CascadeDetach(), this.getEmptyType(), null, "cascadeDetach", null, 0, 1, CascadeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(collectionTableEClass, CollectionTable.class, "CollectionTable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCollectionTable_JoinColumn(), this.getJoinColumn(), null, "joinColumn", null, 0, -1, CollectionTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCollectionTable_ForeignKey(), this.getForeignKey(), null, "foreignKey", null, 0, 1, CollectionTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCollectionTable_UniqueConstraint(), this.getUniqueConstraint(), null, "uniqueConstraint", null, 0, -1, CollectionTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCollectionTable_Index(), this.getIndex(), null, "index", null, 0, -1, CollectionTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCollectionTable_Catalog(), theXMLTypePackage.getString(), "catalog", null, 0, 1, CollectionTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCollectionTable_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, CollectionTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCollectionTable_Schema(), theXMLTypePackage.getString(), "schema", null, 0, 1, CollectionTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(columnEClass, Column.class, "Column", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getColumn_ColumnDefinition(), theXMLTypePackage.getString(), "columnDefinition", null, 0, 1, Column.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getColumn_Insertable(), theXMLTypePackage.getBoolean(), "insertable", null, 0, 1, Column.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getColumn_Length(), theXMLTypePackage.getInt(), "length", null, 0, 1, Column.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getColumn_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, Column.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getColumn_Nullable(), theXMLTypePackage.getBoolean(), "nullable", null, 0, 1, Column.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getColumn_Precision(), theXMLTypePackage.getInt(), "precision", null, 0, 1, Column.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getColumn_Scale(), theXMLTypePackage.getInt(), "scale", null, 0, 1, Column.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getColumn_Table(), theXMLTypePackage.getString(), "table", null, 0, 1, Column.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getColumn_Unique(), theXMLTypePackage.getBoolean(), "unique", null, 0, 1, Column.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getColumn_Updatable(), theXMLTypePackage.getBoolean(), "updatable", null, 0, 1, Column.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(columnResultEClass, ColumnResult.class, "ColumnResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getColumnResult_Class(), theXMLTypePackage.getString(), "class", null, 0, 1, ColumnResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getColumnResult_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, ColumnResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(constructorResultEClass, ConstructorResult.class, "ConstructorResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getConstructorResult_Column(), this.getColumnResult(), null, "column", null, 1, -1, ConstructorResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConstructorResult_TargetClass(), theXMLTypePackage.getString(), "targetClass", null, 1, 1, ConstructorResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(convertEClass, Convert.class, "Convert", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConvert_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, Convert.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConvert_AttributeName(), theXMLTypePackage.getString(), "attributeName", null, 0, 1, Convert.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConvert_Converter(), theXMLTypePackage.getString(), "converter", null, 0, 1, Convert.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConvert_DisableConversion(), theXMLTypePackage.getBoolean(), "disableConversion", null, 0, 1, Convert.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(converterEClass, Converter.class, "Converter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConverter_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, Converter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConverter_AutoApply(), theXMLTypePackage.getBoolean(), "autoApply", null, 0, 1, Converter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConverter_Class(), theXMLTypePackage.getString(), "class", null, 1, 1, Converter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(discriminatorColumnEClass, DiscriminatorColumn.class, "DiscriminatorColumn", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDiscriminatorColumn_ColumnDefinition(), theXMLTypePackage.getString(), "columnDefinition", null, 0, 1, DiscriminatorColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDiscriminatorColumn_DiscriminatorType(), this.getDiscriminatorType(), "discriminatorType", null, 0, 1, DiscriminatorColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDiscriminatorColumn_Length(), theXMLTypePackage.getInt(), "length", null, 0, 1, DiscriminatorColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDiscriminatorColumn_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, DiscriminatorColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_EntityMappings(), this.getEntityMappingsType(), null, "entityMappings", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(elementCollectionEClass, ElementCollection.class, "ElementCollection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getElementCollection_OrderBy(), this.getOrderBy(), "orderBy", null, 0, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElementCollection_OrderColumn(), this.getOrderColumn(), null, "orderColumn", null, 0, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElementCollection_MapKey(), this.getMapKey(), null, "mapKey", null, 0, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElementCollection_MapKeyClass(), this.getMapKeyClass(), null, "mapKeyClass", null, 0, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElementCollection_MapKeyTemporal(), this.getTemporal(), "mapKeyTemporal", null, 0, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElementCollection_MapKeyEnumerated(), this.getEnumerated(), "mapKeyEnumerated", null, 0, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElementCollection_MapKeyAttributeOverride(), this.getAttributeOverride(), null, "mapKeyAttributeOverride", null, 0, -1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElementCollection_MapKeyConvert(), this.getConvert(), null, "mapKeyConvert", null, 0, -1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElementCollection_MapKeyColumn(), this.getMapKeyColumn(), null, "mapKeyColumn", null, 0, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElementCollection_MapKeyJoinColumn(), this.getMapKeyJoinColumn(), null, "mapKeyJoinColumn", null, 0, -1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElementCollection_MapKeyForeignKey(), this.getForeignKey(), null, "mapKeyForeignKey", null, 0, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElementCollection_Column(), this.getColumn(), null, "column", null, 0, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElementCollection_Temporal(), this.getTemporal(), "temporal", null, 0, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElementCollection_Enumerated(), this.getEnumerated(), "enumerated", null, 0, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElementCollection_Lob(), this.getLob(), null, "lob", null, 0, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElementCollection_AttributeOverride(), this.getAttributeOverride(), null, "attributeOverride", null, 0, -1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElementCollection_AssociationOverride(), this.getAssociationOverride(), null, "associationOverride", null, 0, -1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElementCollection_Convert(), this.getConvert(), null, "convert", null, 0, -1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElementCollection_CollectionTable(), this.getCollectionTable(), null, "collectionTable", null, 0, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElementCollection_Access(), this.getAccessType(), "access", null, 0, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElementCollection_Fetch(), this.getFetchType(), "fetch", null, 0, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElementCollection_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElementCollection_TargetClass(), theXMLTypePackage.getString(), "targetClass", null, 0, 1, ElementCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(embeddableEClass, Embeddable.class, "Embeddable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEmbeddable_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, Embeddable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEmbeddable_Attributes(), this.getEmbeddableAttributes(), null, "attributes", null, 0, 1, Embeddable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEmbeddable_Access(), this.getAccessType(), "access", null, 0, 1, Embeddable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEmbeddable_Class(), theXMLTypePackage.getString(), "class", null, 1, 1, Embeddable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEmbeddable_MetadataComplete(), theXMLTypePackage.getBoolean(), "metadataComplete", null, 0, 1, Embeddable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(embeddableAttributesEClass, EmbeddableAttributes.class, "EmbeddableAttributes", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEmbeddableAttributes_Basic(), this.getBasic(), null, "basic", null, 0, -1, EmbeddableAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEmbeddableAttributes_ManyToOne(), this.getManyToOne(), null, "manyToOne", null, 0, -1, EmbeddableAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEmbeddableAttributes_OneToMany(), this.getOneToMany(), null, "oneToMany", null, 0, -1, EmbeddableAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEmbeddableAttributes_OneToOne(), this.getOneToOne(), null, "oneToOne", null, 0, -1, EmbeddableAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEmbeddableAttributes_ManyToMany(), this.getManyToMany(), null, "manyToMany", null, 0, -1, EmbeddableAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEmbeddableAttributes_ElementCollection(), this.getElementCollection(), null, "elementCollection", null, 0, -1, EmbeddableAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEmbeddableAttributes_Embedded(), this.getEmbedded(), null, "embedded", null, 0, -1, EmbeddableAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEmbeddableAttributes_Transient(), this.getTransient(), null, "transient", null, 0, -1, EmbeddableAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(embeddedEClass, Embedded.class, "Embedded", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEmbedded_AttributeOverride(), this.getAttributeOverride(), null, "attributeOverride", null, 0, -1, Embedded.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEmbedded_AssociationOverride(), this.getAssociationOverride(), null, "associationOverride", null, 0, -1, Embedded.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEmbedded_Convert(), this.getConvert(), null, "convert", null, 0, -1, Embedded.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEmbedded_Access(), this.getAccessType(), "access", null, 0, 1, Embedded.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEmbedded_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, Embedded.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(embeddedIdEClass, EmbeddedId.class, "EmbeddedId", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEmbeddedId_AttributeOverride(), this.getAttributeOverride(), null, "attributeOverride", null, 0, -1, EmbeddedId.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEmbeddedId_Access(), this.getAccessType(), "access", null, 0, 1, EmbeddedId.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEmbeddedId_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, EmbeddedId.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(emptyTypeEClass, EmptyType.class, "EmptyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(entityEClass, Entity.class, "Entity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEntity_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_Table(), this.getTable(), null, "table", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_SecondaryTable(), this.getSecondaryTable(), null, "secondaryTable", null, 0, -1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_PrimaryKeyJoinColumn(), this.getPrimaryKeyJoinColumn(), null, "primaryKeyJoinColumn", null, 0, -1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_PrimaryKeyForeignKey(), this.getForeignKey(), null, "primaryKeyForeignKey", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_IdClass(), this.getIdClass(), null, "idClass", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_Inheritance(), this.getInheritance(), null, "inheritance", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntity_DiscriminatorValue(), this.getDiscriminatorValue(), "discriminatorValue", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_DiscriminatorColumn(), this.getDiscriminatorColumn(), null, "discriminatorColumn", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_SequenceGenerator(), this.getSequenceGenerator(), null, "sequenceGenerator", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_TableGenerator(), this.getTableGenerator(), null, "tableGenerator", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_NamedQuery(), this.getNamedQuery(), null, "namedQuery", null, 0, -1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_NamedNativeQuery(), this.getNamedNativeQuery(), null, "namedNativeQuery", null, 0, -1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_NamedStoredProcedureQuery(), this.getNamedStoredProcedureQuery(), null, "namedStoredProcedureQuery", null, 0, -1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_SqlResultSetMapping(), this.getSqlResultSetMapping(), null, "sqlResultSetMapping", null, 0, -1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_ExcludeDefaultListeners(), this.getEmptyType(), null, "excludeDefaultListeners", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_ExcludeSuperclassListeners(), this.getEmptyType(), null, "excludeSuperclassListeners", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_EntityListeners(), this.getEntityListeners(), null, "entityListeners", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_PrePersist(), this.getPrePersist(), null, "prePersist", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_PostPersist(), this.getPostPersist(), null, "postPersist", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_PreRemove(), this.getPreRemove(), null, "preRemove", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_PostRemove(), this.getPostRemove(), null, "postRemove", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_PreUpdate(), this.getPreUpdate(), null, "preUpdate", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_PostUpdate(), this.getPostUpdate(), null, "postUpdate", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_PostLoad(), this.getPostLoad(), null, "postLoad", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_AttributeOverride(), this.getAttributeOverride(), null, "attributeOverride", null, 0, -1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_AssociationOverride(), this.getAssociationOverride(), null, "associationOverride", null, 0, -1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_Convert(), this.getConvert(), null, "convert", null, 0, -1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_NamedEntityGraph(), this.getNamedEntityGraph(), null, "namedEntityGraph", null, 0, -1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntity_Attributes(), this.getAttributes(), null, "attributes", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntity_Access(), this.getAccessType(), "access", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntity_Cacheable(), theXMLTypePackage.getBoolean(), "cacheable", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntity_Class(), theXMLTypePackage.getString(), "class", null, 1, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntity_MetadataComplete(), theXMLTypePackage.getBoolean(), "metadataComplete", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntity_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entityListenerEClass, EntityListener.class, "EntityListener", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEntityListener_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, EntityListener.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityListener_PrePersist(), this.getPrePersist(), null, "prePersist", null, 0, 1, EntityListener.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityListener_PostPersist(), this.getPostPersist(), null, "postPersist", null, 0, 1, EntityListener.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityListener_PreRemove(), this.getPreRemove(), null, "preRemove", null, 0, 1, EntityListener.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityListener_PostRemove(), this.getPostRemove(), null, "postRemove", null, 0, 1, EntityListener.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityListener_PreUpdate(), this.getPreUpdate(), null, "preUpdate", null, 0, 1, EntityListener.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityListener_PostUpdate(), this.getPostUpdate(), null, "postUpdate", null, 0, 1, EntityListener.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityListener_PostLoad(), this.getPostLoad(), null, "postLoad", null, 0, 1, EntityListener.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntityListener_Class(), theXMLTypePackage.getString(), "class", null, 1, 1, EntityListener.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entityListenersEClass, EntityListeners.class, "EntityListeners", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEntityListeners_EntityListener(), this.getEntityListener(), null, "entityListener", null, 0, -1, EntityListeners.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entityMappingsTypeEClass, EntityMappingsType.class, "EntityMappingsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEntityMappingsType_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityMappingsType_PersistenceUnitMetadata(), this.getPersistenceUnitMetadata(), null, "persistenceUnitMetadata", null, 0, 1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntityMappingsType_Package(), theXMLTypePackage.getString(), "package", null, 0, 1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntityMappingsType_Schema(), theXMLTypePackage.getString(), "schema", null, 0, 1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntityMappingsType_Catalog(), theXMLTypePackage.getString(), "catalog", null, 0, 1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntityMappingsType_Access(), this.getAccessType(), "access", null, 0, 1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityMappingsType_SequenceGenerator(), this.getSequenceGenerator(), null, "sequenceGenerator", null, 0, -1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityMappingsType_TableGenerator(), this.getTableGenerator(), null, "tableGenerator", null, 0, -1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityMappingsType_NamedQuery(), this.getNamedQuery(), null, "namedQuery", null, 0, -1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityMappingsType_NamedNativeQuery(), this.getNamedNativeQuery(), null, "namedNativeQuery", null, 0, -1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityMappingsType_NamedStoredProcedureQuery(), this.getNamedStoredProcedureQuery(), null, "namedStoredProcedureQuery", null, 0, -1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityMappingsType_SqlResultSetMapping(), this.getSqlResultSetMapping(), null, "sqlResultSetMapping", null, 0, -1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityMappingsType_MappedSuperclass(), this.getMappedSuperclass(), null, "mappedSuperclass", null, 0, -1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityMappingsType_Entity(), this.getEntity(), null, "entity", null, 0, -1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityMappingsType_Embeddable(), this.getEmbeddable(), null, "embeddable", null, 0, -1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityMappingsType_Converter(), this.getConverter(), null, "converter", null, 0, -1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntityMappingsType_Version(), this.getVersionType(), "version", "3.1", 1, 1, EntityMappingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entityResultEClass, EntityResult.class, "EntityResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEntityResult_FieldResult(), this.getFieldResult(), null, "fieldResult", null, 0, -1, EntityResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntityResult_DiscriminatorColumn(), theXMLTypePackage.getString(), "discriminatorColumn", null, 0, 1, EntityResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntityResult_EntityClass(), theXMLTypePackage.getString(), "entityClass", null, 1, 1, EntityResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fieldResultEClass, FieldResult.class, "FieldResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFieldResult_Column(), theXMLTypePackage.getString(), "column", null, 1, 1, FieldResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFieldResult_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, FieldResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(foreignKeyEClass, ForeignKey.class, "ForeignKey", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getForeignKey_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, ForeignKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getForeignKey_ConstraintMode(), this.getConstraintMode(), "constraintMode", null, 0, 1, ForeignKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getForeignKey_ForeignKeyDefinition(), theXMLTypePackage.getString(), "foreignKeyDefinition", null, 0, 1, ForeignKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getForeignKey_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, ForeignKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(generatedValueEClass, GeneratedValue.class, "GeneratedValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGeneratedValue_Generator(), theXMLTypePackage.getString(), "generator", null, 0, 1, GeneratedValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeneratedValue_Strategy(), this.getGenerationType(), "strategy", null, 0, 1, GeneratedValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(idEClass, Id.class, "Id", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getId_Column(), this.getColumn(), null, "column", null, 0, 1, Id.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getId_GeneratedValue(), this.getGeneratedValue(), null, "generatedValue", null, 0, 1, Id.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getId_Temporal(), this.getTemporal(), "temporal", null, 0, 1, Id.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getId_TableGenerator(), this.getTableGenerator(), null, "tableGenerator", null, 0, 1, Id.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getId_SequenceGenerator(), this.getSequenceGenerator(), null, "sequenceGenerator", null, 0, 1, Id.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getId_Access(), this.getAccessType(), "access", null, 0, 1, Id.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getId_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, Id.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(idClassEClass, IdClass.class, "IdClass", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIdClass_Class(), theXMLTypePackage.getString(), "class", null, 1, 1, IdClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indexEClass, Index.class, "Index", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIndex_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, Index.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIndex_ColumnList(), theXMLTypePackage.getString(), "columnList", null, 1, 1, Index.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIndex_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, Index.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIndex_Unique(), theXMLTypePackage.getBoolean(), "unique", null, 0, 1, Index.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(inheritanceEClass, Inheritance.class, "Inheritance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getInheritance_Strategy(), this.getInheritanceType(), "strategy", null, 0, 1, Inheritance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(joinColumnEClass, JoinColumn.class, "JoinColumn", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getJoinColumn_ColumnDefinition(), theXMLTypePackage.getString(), "columnDefinition", null, 0, 1, JoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJoinColumn_Insertable(), theXMLTypePackage.getBoolean(), "insertable", null, 0, 1, JoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJoinColumn_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, JoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJoinColumn_Nullable(), theXMLTypePackage.getBoolean(), "nullable", null, 0, 1, JoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJoinColumn_ReferencedColumnName(), theXMLTypePackage.getString(), "referencedColumnName", null, 0, 1, JoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJoinColumn_Table(), theXMLTypePackage.getString(), "table", null, 0, 1, JoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJoinColumn_Unique(), theXMLTypePackage.getBoolean(), "unique", null, 0, 1, JoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJoinColumn_Updatable(), theXMLTypePackage.getBoolean(), "updatable", null, 0, 1, JoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(joinTableEClass, JoinTable.class, "JoinTable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getJoinTable_JoinColumn(), this.getJoinColumn(), null, "joinColumn", null, 0, -1, JoinTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJoinTable_ForeignKey(), this.getForeignKey(), null, "foreignKey", null, 0, 1, JoinTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJoinTable_InverseJoinColumn(), this.getJoinColumn(), null, "inverseJoinColumn", null, 0, -1, JoinTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJoinTable_InverseForeignKey(), this.getForeignKey(), null, "inverseForeignKey", null, 0, 1, JoinTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJoinTable_UniqueConstraint(), this.getUniqueConstraint(), null, "uniqueConstraint", null, 0, -1, JoinTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJoinTable_Index(), this.getIndex(), null, "index", null, 0, -1, JoinTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJoinTable_Catalog(), theXMLTypePackage.getString(), "catalog", null, 0, 1, JoinTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJoinTable_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, JoinTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJoinTable_Schema(), theXMLTypePackage.getString(), "schema", null, 0, 1, JoinTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lobEClass, Lob.class, "Lob", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(manyToManyEClass, ManyToMany.class, "ManyToMany", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getManyToMany_OrderBy(), this.getOrderBy(), "orderBy", null, 0, 1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getManyToMany_OrderColumn(), this.getOrderColumn(), null, "orderColumn", null, 0, 1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getManyToMany_MapKey(), this.getMapKey(), null, "mapKey", null, 0, 1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getManyToMany_MapKeyClass(), this.getMapKeyClass(), null, "mapKeyClass", null, 0, 1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getManyToMany_MapKeyTemporal(), this.getTemporal(), "mapKeyTemporal", null, 0, 1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getManyToMany_MapKeyEnumerated(), this.getEnumerated(), "mapKeyEnumerated", null, 0, 1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getManyToMany_MapKeyAttributeOverride(), this.getAttributeOverride(), null, "mapKeyAttributeOverride", null, 0, -1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getManyToMany_MapKeyConvert(), this.getConvert(), null, "mapKeyConvert", null, 0, -1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getManyToMany_MapKeyColumn(), this.getMapKeyColumn(), null, "mapKeyColumn", null, 0, 1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getManyToMany_MapKeyJoinColumn(), this.getMapKeyJoinColumn(), null, "mapKeyJoinColumn", null, 0, -1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getManyToMany_MapKeyForeignKey(), this.getForeignKey(), null, "mapKeyForeignKey", null, 0, 1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getManyToMany_JoinTable(), this.getJoinTable(), null, "joinTable", null, 0, 1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getManyToMany_Cascade(), this.getCascadeType(), null, "cascade", null, 0, 1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getManyToMany_Access(), this.getAccessType(), "access", null, 0, 1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getManyToMany_Fetch(), this.getFetchType(), "fetch", null, 0, 1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getManyToMany_MappedBy(), theXMLTypePackage.getString(), "mappedBy", null, 0, 1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getManyToMany_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getManyToMany_TargetEntity(), theXMLTypePackage.getString(), "targetEntity", null, 0, 1, ManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(manyToOneEClass, ManyToOne.class, "ManyToOne", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getManyToOne_JoinColumn(), this.getJoinColumn(), null, "joinColumn", null, 0, -1, ManyToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getManyToOne_ForeignKey(), this.getForeignKey(), null, "foreignKey", null, 0, 1, ManyToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getManyToOne_JoinTable(), this.getJoinTable(), null, "joinTable", null, 0, 1, ManyToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getManyToOne_Cascade(), this.getCascadeType(), null, "cascade", null, 0, 1, ManyToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getManyToOne_Access(), this.getAccessType(), "access", null, 0, 1, ManyToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getManyToOne_Fetch(), this.getFetchType(), "fetch", null, 0, 1, ManyToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getManyToOne_Id(), theXMLTypePackage.getBoolean(), "id", null, 0, 1, ManyToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getManyToOne_MapsId(), theXMLTypePackage.getString(), "mapsId", null, 0, 1, ManyToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getManyToOne_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, ManyToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getManyToOne_Optional(), theXMLTypePackage.getBoolean(), "optional", null, 0, 1, ManyToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getManyToOne_TargetEntity(), theXMLTypePackage.getString(), "targetEntity", null, 0, 1, ManyToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mapKeyEClass, MapKey.class, "MapKey", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMapKey_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, MapKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mapKeyClassEClass, MapKeyClass.class, "MapKeyClass", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMapKeyClass_Class(), theXMLTypePackage.getString(), "class", null, 1, 1, MapKeyClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mapKeyColumnEClass, MapKeyColumn.class, "MapKeyColumn", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMapKeyColumn_ColumnDefinition(), theXMLTypePackage.getString(), "columnDefinition", null, 0, 1, MapKeyColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMapKeyColumn_Insertable(), theXMLTypePackage.getBoolean(), "insertable", null, 0, 1, MapKeyColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMapKeyColumn_Length(), theXMLTypePackage.getInt(), "length", null, 0, 1, MapKeyColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMapKeyColumn_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, MapKeyColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMapKeyColumn_Nullable(), theXMLTypePackage.getBoolean(), "nullable", null, 0, 1, MapKeyColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMapKeyColumn_Precision(), theXMLTypePackage.getInt(), "precision", null, 0, 1, MapKeyColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMapKeyColumn_Scale(), theXMLTypePackage.getInt(), "scale", null, 0, 1, MapKeyColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMapKeyColumn_Table(), theXMLTypePackage.getString(), "table", null, 0, 1, MapKeyColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMapKeyColumn_Unique(), theXMLTypePackage.getBoolean(), "unique", null, 0, 1, MapKeyColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMapKeyColumn_Updatable(), theXMLTypePackage.getBoolean(), "updatable", null, 0, 1, MapKeyColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mapKeyJoinColumnEClass, MapKeyJoinColumn.class, "MapKeyJoinColumn", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMapKeyJoinColumn_ColumnDefinition(), theXMLTypePackage.getString(), "columnDefinition", null, 0, 1, MapKeyJoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMapKeyJoinColumn_Insertable(), theXMLTypePackage.getBoolean(), "insertable", null, 0, 1, MapKeyJoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMapKeyJoinColumn_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, MapKeyJoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMapKeyJoinColumn_Nullable(), theXMLTypePackage.getBoolean(), "nullable", null, 0, 1, MapKeyJoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMapKeyJoinColumn_ReferencedColumnName(), theXMLTypePackage.getString(), "referencedColumnName", null, 0, 1, MapKeyJoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMapKeyJoinColumn_Table(), theXMLTypePackage.getString(), "table", null, 0, 1, MapKeyJoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMapKeyJoinColumn_Unique(), theXMLTypePackage.getBoolean(), "unique", null, 0, 1, MapKeyJoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMapKeyJoinColumn_Updatable(), theXMLTypePackage.getBoolean(), "updatable", null, 0, 1, MapKeyJoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mappedSuperclassEClass, MappedSuperclass.class, "MappedSuperclass", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMappedSuperclass_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, MappedSuperclass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappedSuperclass_IdClass(), this.getIdClass(), null, "idClass", null, 0, 1, MappedSuperclass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappedSuperclass_ExcludeDefaultListeners(), this.getEmptyType(), null, "excludeDefaultListeners", null, 0, 1, MappedSuperclass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappedSuperclass_ExcludeSuperclassListeners(), this.getEmptyType(), null, "excludeSuperclassListeners", null, 0, 1, MappedSuperclass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappedSuperclass_EntityListeners(), this.getEntityListeners(), null, "entityListeners", null, 0, 1, MappedSuperclass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappedSuperclass_PrePersist(), this.getPrePersist(), null, "prePersist", null, 0, 1, MappedSuperclass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappedSuperclass_PostPersist(), this.getPostPersist(), null, "postPersist", null, 0, 1, MappedSuperclass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappedSuperclass_PreRemove(), this.getPreRemove(), null, "preRemove", null, 0, 1, MappedSuperclass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappedSuperclass_PostRemove(), this.getPostRemove(), null, "postRemove", null, 0, 1, MappedSuperclass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappedSuperclass_PreUpdate(), this.getPreUpdate(), null, "preUpdate", null, 0, 1, MappedSuperclass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappedSuperclass_PostUpdate(), this.getPostUpdate(), null, "postUpdate", null, 0, 1, MappedSuperclass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappedSuperclass_PostLoad(), this.getPostLoad(), null, "postLoad", null, 0, 1, MappedSuperclass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappedSuperclass_Attributes(), this.getAttributes(), null, "attributes", null, 0, 1, MappedSuperclass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMappedSuperclass_Access(), this.getAccessType(), "access", null, 0, 1, MappedSuperclass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMappedSuperclass_Class(), theXMLTypePackage.getString(), "class", null, 1, 1, MappedSuperclass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMappedSuperclass_MetadataComplete(), theXMLTypePackage.getBoolean(), "metadataComplete", null, 0, 1, MappedSuperclass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(namedAttributeNodeEClass, NamedAttributeNode.class, "NamedAttributeNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamedAttributeNode_KeySubgraph(), theXMLTypePackage.getString(), "keySubgraph", null, 0, 1, NamedAttributeNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedAttributeNode_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, NamedAttributeNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedAttributeNode_Subgraph(), theXMLTypePackage.getString(), "subgraph", null, 0, 1, NamedAttributeNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(namedEntityGraphEClass, NamedEntityGraph.class, "NamedEntityGraph", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getNamedEntityGraph_NamedAttributeNode(), this.getNamedAttributeNode(), null, "namedAttributeNode", null, 0, -1, NamedEntityGraph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNamedEntityGraph_Subgraph(), this.getNamedSubgraph(), null, "subgraph", null, 0, -1, NamedEntityGraph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNamedEntityGraph_SubclassSubgraph(), this.getNamedSubgraph(), null, "subclassSubgraph", null, 0, -1, NamedEntityGraph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedEntityGraph_IncludeAllAttributes(), theXMLTypePackage.getBoolean(), "includeAllAttributes", null, 0, 1, NamedEntityGraph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedEntityGraph_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, NamedEntityGraph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(namedNativeQueryEClass, NamedNativeQuery.class, "NamedNativeQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamedNativeQuery_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, NamedNativeQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedNativeQuery_Query(), theXMLTypePackage.getString(), "query", null, 1, 1, NamedNativeQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNamedNativeQuery_Hint(), this.getQueryHint(), null, "hint", null, 0, -1, NamedNativeQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedNativeQuery_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, NamedNativeQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedNativeQuery_ResultClass(), theXMLTypePackage.getString(), "resultClass", null, 0, 1, NamedNativeQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedNativeQuery_ResultSetMapping(), theXMLTypePackage.getString(), "resultSetMapping", null, 0, 1, NamedNativeQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(namedQueryEClass, NamedQuery.class, "NamedQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamedQuery_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, NamedQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedQuery_Query(), theXMLTypePackage.getString(), "query", null, 1, 1, NamedQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedQuery_LockMode(), this.getLockModeType(), "lockMode", null, 0, 1, NamedQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNamedQuery_Hint(), this.getQueryHint(), null, "hint", null, 0, -1, NamedQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedQuery_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, NamedQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(namedStoredProcedureQueryEClass, NamedStoredProcedureQuery.class, "NamedStoredProcedureQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamedStoredProcedureQuery_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, NamedStoredProcedureQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNamedStoredProcedureQuery_Parameter(), this.getStoredProcedureParameter(), null, "parameter", null, 0, -1, NamedStoredProcedureQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedStoredProcedureQuery_ResultClass(), theXMLTypePackage.getString(), "resultClass", null, 0, -1, NamedStoredProcedureQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedStoredProcedureQuery_ResultSetMapping(), theXMLTypePackage.getString(), "resultSetMapping", null, 0, -1, NamedStoredProcedureQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNamedStoredProcedureQuery_Hint(), this.getQueryHint(), null, "hint", null, 0, -1, NamedStoredProcedureQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedStoredProcedureQuery_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, NamedStoredProcedureQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedStoredProcedureQuery_ProcedureName(), theXMLTypePackage.getString(), "procedureName", null, 1, 1, NamedStoredProcedureQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(namedSubgraphEClass, NamedSubgraph.class, "NamedSubgraph", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getNamedSubgraph_NamedAttributeNode(), this.getNamedAttributeNode(), null, "namedAttributeNode", null, 0, -1, NamedSubgraph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedSubgraph_Class(), theXMLTypePackage.getString(), "class", null, 0, 1, NamedSubgraph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedSubgraph_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, NamedSubgraph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(oneToManyEClass, OneToMany.class, "OneToMany", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOneToMany_OrderBy(), this.getOrderBy(), "orderBy", null, 0, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToMany_OrderColumn(), this.getOrderColumn(), null, "orderColumn", null, 0, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToMany_MapKey(), this.getMapKey(), null, "mapKey", null, 0, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToMany_MapKeyClass(), this.getMapKeyClass(), null, "mapKeyClass", null, 0, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToMany_MapKeyTemporal(), this.getTemporal(), "mapKeyTemporal", null, 0, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToMany_MapKeyEnumerated(), this.getEnumerated(), "mapKeyEnumerated", null, 0, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToMany_MapKeyAttributeOverride(), this.getAttributeOverride(), null, "mapKeyAttributeOverride", null, 0, -1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToMany_MapKeyConvert(), this.getConvert(), null, "mapKeyConvert", null, 0, -1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToMany_MapKeyColumn(), this.getMapKeyColumn(), null, "mapKeyColumn", null, 0, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToMany_MapKeyJoinColumn(), this.getMapKeyJoinColumn(), null, "mapKeyJoinColumn", null, 0, -1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToMany_MapKeyForeignKey(), this.getForeignKey(), null, "mapKeyForeignKey", null, 0, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToMany_JoinTable(), this.getJoinTable(), null, "joinTable", null, 0, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToMany_JoinColumn(), this.getJoinColumn(), null, "joinColumn", null, 0, -1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToMany_ForeignKey(), this.getForeignKey(), null, "foreignKey", null, 0, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToMany_Cascade(), this.getCascadeType(), null, "cascade", null, 0, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToMany_Access(), this.getAccessType(), "access", null, 0, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToMany_Fetch(), this.getFetchType(), "fetch", null, 0, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToMany_MappedBy(), theXMLTypePackage.getString(), "mappedBy", null, 0, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToMany_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToMany_OrphanRemoval(), theXMLTypePackage.getBoolean(), "orphanRemoval", null, 0, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToMany_TargetEntity(), theXMLTypePackage.getString(), "targetEntity", null, 0, 1, OneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(oneToOneEClass, OneToOne.class, "OneToOne", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOneToOne_PrimaryKeyJoinColumn(), this.getPrimaryKeyJoinColumn(), null, "primaryKeyJoinColumn", null, 0, -1, OneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToOne_PrimaryKeyForeignKey(), this.getForeignKey(), null, "primaryKeyForeignKey", null, 0, 1, OneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToOne_JoinColumn(), this.getJoinColumn(), null, "joinColumn", null, 0, -1, OneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToOne_ForeignKey(), this.getForeignKey(), null, "foreignKey", null, 0, 1, OneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToOne_JoinTable(), this.getJoinTable(), null, "joinTable", null, 0, 1, OneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOneToOne_Cascade(), this.getCascadeType(), null, "cascade", null, 0, 1, OneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToOne_Access(), this.getAccessType(), "access", null, 0, 1, OneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToOne_Fetch(), this.getFetchType(), "fetch", null, 0, 1, OneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToOne_Id(), theXMLTypePackage.getBoolean(), "id", null, 0, 1, OneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToOne_MappedBy(), theXMLTypePackage.getString(), "mappedBy", null, 0, 1, OneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToOne_MapsId(), theXMLTypePackage.getString(), "mapsId", null, 0, 1, OneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToOne_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, OneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToOne_Optional(), theXMLTypePackage.getBoolean(), "optional", null, 0, 1, OneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToOne_OrphanRemoval(), theXMLTypePackage.getBoolean(), "orphanRemoval", null, 0, 1, OneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOneToOne_TargetEntity(), theXMLTypePackage.getString(), "targetEntity", null, 0, 1, OneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(orderColumnEClass, OrderColumn.class, "OrderColumn", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOrderColumn_ColumnDefinition(), theXMLTypePackage.getString(), "columnDefinition", null, 0, 1, OrderColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOrderColumn_Insertable(), theXMLTypePackage.getBoolean(), "insertable", null, 0, 1, OrderColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOrderColumn_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, OrderColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOrderColumn_Nullable(), theXMLTypePackage.getBoolean(), "nullable", null, 0, 1, OrderColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOrderColumn_Updatable(), theXMLTypePackage.getBoolean(), "updatable", null, 0, 1, OrderColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(persistenceUnitDefaultsEClass, PersistenceUnitDefaults.class, "PersistenceUnitDefaults", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPersistenceUnitDefaults_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, PersistenceUnitDefaults.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPersistenceUnitDefaults_Schema(), theXMLTypePackage.getString(), "schema", null, 0, 1, PersistenceUnitDefaults.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPersistenceUnitDefaults_Catalog(), theXMLTypePackage.getString(), "catalog", null, 0, 1, PersistenceUnitDefaults.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPersistenceUnitDefaults_DelimitedIdentifiers(), this.getEmptyType(), null, "delimitedIdentifiers", null, 0, 1, PersistenceUnitDefaults.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPersistenceUnitDefaults_Access(), this.getAccessType(), "access", null, 0, 1, PersistenceUnitDefaults.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPersistenceUnitDefaults_CascadePersist(), this.getEmptyType(), null, "cascadePersist", null, 0, 1, PersistenceUnitDefaults.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPersistenceUnitDefaults_EntityListeners(), this.getEntityListeners(), null, "entityListeners", null, 0, 1, PersistenceUnitDefaults.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(persistenceUnitMetadataEClass, PersistenceUnitMetadata.class, "PersistenceUnitMetadata", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPersistenceUnitMetadata_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, PersistenceUnitMetadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPersistenceUnitMetadata_XmlMappingMetadataComplete(), this.getEmptyType(), null, "xmlMappingMetadataComplete", null, 0, 1, PersistenceUnitMetadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPersistenceUnitMetadata_PersistenceUnitDefaults(), this.getPersistenceUnitDefaults(), null, "persistenceUnitDefaults", null, 0, 1, PersistenceUnitMetadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(postLoadEClass, PostLoad.class, "PostLoad", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPostLoad_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, PostLoad.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPostLoad_MethodName(), theXMLTypePackage.getString(), "methodName", null, 1, 1, PostLoad.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(postPersistEClass, PostPersist.class, "PostPersist", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPostPersist_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, PostPersist.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPostPersist_MethodName(), theXMLTypePackage.getString(), "methodName", null, 1, 1, PostPersist.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(postRemoveEClass, PostRemove.class, "PostRemove", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPostRemove_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, PostRemove.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPostRemove_MethodName(), theXMLTypePackage.getString(), "methodName", null, 1, 1, PostRemove.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(postUpdateEClass, PostUpdate.class, "PostUpdate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPostUpdate_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, PostUpdate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPostUpdate_MethodName(), theXMLTypePackage.getString(), "methodName", null, 1, 1, PostUpdate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(prePersistEClass, PrePersist.class, "PrePersist", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPrePersist_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, PrePersist.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPrePersist_MethodName(), theXMLTypePackage.getString(), "methodName", null, 1, 1, PrePersist.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(preRemoveEClass, PreRemove.class, "PreRemove", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPreRemove_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, PreRemove.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPreRemove_MethodName(), theXMLTypePackage.getString(), "methodName", null, 1, 1, PreRemove.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(preUpdateEClass, PreUpdate.class, "PreUpdate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPreUpdate_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, PreUpdate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPreUpdate_MethodName(), theXMLTypePackage.getString(), "methodName", null, 1, 1, PreUpdate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(primaryKeyJoinColumnEClass, PrimaryKeyJoinColumn.class, "PrimaryKeyJoinColumn", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPrimaryKeyJoinColumn_ColumnDefinition(), theXMLTypePackage.getString(), "columnDefinition", null, 0, 1, PrimaryKeyJoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPrimaryKeyJoinColumn_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, PrimaryKeyJoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPrimaryKeyJoinColumn_ReferencedColumnName(), theXMLTypePackage.getString(), "referencedColumnName", null, 0, 1, PrimaryKeyJoinColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(queryHintEClass, QueryHint.class, "QueryHint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getQueryHint_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, QueryHint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQueryHint_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, QueryHint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQueryHint_Value(), theXMLTypePackage.getString(), "value", null, 1, 1, QueryHint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(secondaryTableEClass, SecondaryTable.class, "SecondaryTable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSecondaryTable_PrimaryKeyJoinColumn(), this.getPrimaryKeyJoinColumn(), null, "primaryKeyJoinColumn", null, 0, -1, SecondaryTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSecondaryTable_PrimaryKeyForeignKey(), this.getForeignKey(), null, "primaryKeyForeignKey", null, 0, 1, SecondaryTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSecondaryTable_UniqueConstraint(), this.getUniqueConstraint(), null, "uniqueConstraint", null, 0, -1, SecondaryTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSecondaryTable_Index(), this.getIndex(), null, "index", null, 0, -1, SecondaryTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSecondaryTable_Catalog(), theXMLTypePackage.getString(), "catalog", null, 0, 1, SecondaryTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSecondaryTable_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, SecondaryTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSecondaryTable_Schema(), theXMLTypePackage.getString(), "schema", null, 0, 1, SecondaryTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sequenceGeneratorEClass, SequenceGenerator.class, "SequenceGenerator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSequenceGenerator_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, SequenceGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSequenceGenerator_AllocationSize(), theXMLTypePackage.getInt(), "allocationSize", null, 0, 1, SequenceGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSequenceGenerator_Catalog(), theXMLTypePackage.getString(), "catalog", null, 0, 1, SequenceGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSequenceGenerator_InitialValue(), theXMLTypePackage.getInt(), "initialValue", null, 0, 1, SequenceGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSequenceGenerator_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, SequenceGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSequenceGenerator_Schema(), theXMLTypePackage.getString(), "schema", null, 0, 1, SequenceGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSequenceGenerator_SequenceName(), theXMLTypePackage.getString(), "sequenceName", null, 0, 1, SequenceGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sqlResultSetMappingEClass, SqlResultSetMapping.class, "SqlResultSetMapping", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSqlResultSetMapping_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, SqlResultSetMapping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSqlResultSetMapping_EntityResult(), this.getEntityResult(), null, "entityResult", null, 0, -1, SqlResultSetMapping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSqlResultSetMapping_ConstructorResult(), this.getConstructorResult(), null, "constructorResult", null, 0, -1, SqlResultSetMapping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSqlResultSetMapping_ColumnResult(), this.getColumnResult(), null, "columnResult", null, 0, -1, SqlResultSetMapping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSqlResultSetMapping_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, SqlResultSetMapping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(storedProcedureParameterEClass, StoredProcedureParameter.class, "StoredProcedureParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStoredProcedureParameter_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, StoredProcedureParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStoredProcedureParameter_Class(), theXMLTypePackage.getString(), "class", null, 1, 1, StoredProcedureParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStoredProcedureParameter_Mode(), this.getParameterMode(), "mode", null, 0, 1, StoredProcedureParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStoredProcedureParameter_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, StoredProcedureParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(tableEClass, Table.class, "Table", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTable_UniqueConstraint(), this.getUniqueConstraint(), null, "uniqueConstraint", null, 0, -1, Table.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTable_Index(), this.getIndex(), null, "index", null, 0, -1, Table.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTable_Catalog(), theXMLTypePackage.getString(), "catalog", null, 0, 1, Table.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTable_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, Table.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTable_Schema(), theXMLTypePackage.getString(), "schema", null, 0, 1, Table.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(tableGeneratorEClass, TableGenerator.class, "TableGenerator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTableGenerator_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, TableGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTableGenerator_UniqueConstraint(), this.getUniqueConstraint(), null, "uniqueConstraint", null, 0, -1, TableGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTableGenerator_Index(), this.getIndex(), null, "index", null, 0, -1, TableGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTableGenerator_AllocationSize(), theXMLTypePackage.getInt(), "allocationSize", null, 0, 1, TableGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTableGenerator_Catalog(), theXMLTypePackage.getString(), "catalog", null, 0, 1, TableGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTableGenerator_InitialValue(), theXMLTypePackage.getInt(), "initialValue", null, 0, 1, TableGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTableGenerator_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, TableGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTableGenerator_PkColumnName(), theXMLTypePackage.getString(), "pkColumnName", null, 0, 1, TableGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTableGenerator_PkColumnValue(), theXMLTypePackage.getString(), "pkColumnValue", null, 0, 1, TableGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTableGenerator_Schema(), theXMLTypePackage.getString(), "schema", null, 0, 1, TableGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTableGenerator_Table(), theXMLTypePackage.getString(), "table", null, 0, 1, TableGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTableGenerator_ValueColumnName(), theXMLTypePackage.getString(), "valueColumnName", null, 0, 1, TableGenerator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(transientEClass, Transient.class, "Transient", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTransient_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, Transient.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(uniqueConstraintEClass, UniqueConstraint.class, "UniqueConstraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUniqueConstraint_ColumnName(), theXMLTypePackage.getString(), "columnName", null, 1, -1, UniqueConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUniqueConstraint_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, UniqueConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(versionEClass, Version.class, "Version", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVersion_Column(), this.getColumn(), null, "column", null, 0, 1, Version.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVersion_Temporal(), this.getTemporal(), "temporal", null, 0, 1, Version.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVersion_Access(), this.getAccessType(), "access", null, 0, 1, Version.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVersion_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, Version.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(accessTypeEEnum, AccessType.class, "AccessType");
		addEEnumLiteral(accessTypeEEnum, AccessType.PROPERTY);
		addEEnumLiteral(accessTypeEEnum, AccessType.FIELD);

		initEEnum(constraintModeEEnum, ConstraintMode.class, "ConstraintMode");
		addEEnumLiteral(constraintModeEEnum, ConstraintMode.CONSTRAINT);
		addEEnumLiteral(constraintModeEEnum, ConstraintMode.NOCONSTRAINT);
		addEEnumLiteral(constraintModeEEnum, ConstraintMode.PROVIDERDEFAULT);

		initEEnum(discriminatorTypeEEnum, DiscriminatorType.class, "DiscriminatorType");
		addEEnumLiteral(discriminatorTypeEEnum, DiscriminatorType.STRING);
		addEEnumLiteral(discriminatorTypeEEnum, DiscriminatorType.CHAR);
		addEEnumLiteral(discriminatorTypeEEnum, DiscriminatorType.INTEGER);

		initEEnum(enumTypeEEnum, EnumType.class, "EnumType");
		addEEnumLiteral(enumTypeEEnum, EnumType.ORDINAL);
		addEEnumLiteral(enumTypeEEnum, EnumType.STRING);

		initEEnum(fetchTypeEEnum, FetchType.class, "FetchType");
		addEEnumLiteral(fetchTypeEEnum, FetchType.LAZY);
		addEEnumLiteral(fetchTypeEEnum, FetchType.EAGER);

		initEEnum(generationTypeEEnum, GenerationType.class, "GenerationType");
		addEEnumLiteral(generationTypeEEnum, GenerationType.TABLE);
		addEEnumLiteral(generationTypeEEnum, GenerationType.SEQUENCE);
		addEEnumLiteral(generationTypeEEnum, GenerationType.IDENTITY);
		addEEnumLiteral(generationTypeEEnum, GenerationType.UUID);
		addEEnumLiteral(generationTypeEEnum, GenerationType.AUTO);

		initEEnum(inheritanceTypeEEnum, InheritanceType.class, "InheritanceType");
		addEEnumLiteral(inheritanceTypeEEnum, InheritanceType.SINGLETABLE);
		addEEnumLiteral(inheritanceTypeEEnum, InheritanceType.JOINED);
		addEEnumLiteral(inheritanceTypeEEnum, InheritanceType.TABLEPERCLASS);

		initEEnum(lockModeTypeEEnum, LockModeType.class, "LockModeType");
		addEEnumLiteral(lockModeTypeEEnum, LockModeType.READ);
		addEEnumLiteral(lockModeTypeEEnum, LockModeType.WRITE);
		addEEnumLiteral(lockModeTypeEEnum, LockModeType.OPTIMISTIC);
		addEEnumLiteral(lockModeTypeEEnum, LockModeType.OPTIMISTICFORCEINCREMENT);
		addEEnumLiteral(lockModeTypeEEnum, LockModeType.PESSIMISTICREAD);
		addEEnumLiteral(lockModeTypeEEnum, LockModeType.PESSIMISTICWRITE);
		addEEnumLiteral(lockModeTypeEEnum, LockModeType.PESSIMISTICFORCEINCREMENT);
		addEEnumLiteral(lockModeTypeEEnum, LockModeType.NONE);

		initEEnum(parameterModeEEnum, ParameterMode.class, "ParameterMode");
		addEEnumLiteral(parameterModeEEnum, ParameterMode.IN);
		addEEnumLiteral(parameterModeEEnum, ParameterMode.INOUT);
		addEEnumLiteral(parameterModeEEnum, ParameterMode.OUT);
		addEEnumLiteral(parameterModeEEnum, ParameterMode.REFCURSOR);

		initEEnum(temporalTypeEEnum, TemporalType.class, "TemporalType");
		addEEnumLiteral(temporalTypeEEnum, TemporalType.DATE);
		addEEnumLiteral(temporalTypeEEnum, TemporalType.TIME);
		addEEnumLiteral(temporalTypeEEnum, TemporalType.TIMESTAMP);

		// Initialize data types
		initEDataType(accessTypeObjectEDataType, AccessType.class, "AccessTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(constraintModeObjectEDataType, ConstraintMode.class, "ConstraintModeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(discriminatorTypeObjectEDataType, DiscriminatorType.class, "DiscriminatorTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(discriminatorValueEDataType, String.class, "DiscriminatorValue", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(enumeratedEDataType, EnumType.class, "Enumerated", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(enumTypeObjectEDataType, EnumType.class, "EnumTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(fetchTypeObjectEDataType, FetchType.class, "FetchTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(generationTypeObjectEDataType, GenerationType.class, "GenerationTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(inheritanceTypeObjectEDataType, InheritanceType.class, "InheritanceTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(lockModeTypeObjectEDataType, LockModeType.class, "LockModeTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(orderByEDataType, String.class, "OrderBy", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(parameterModeObjectEDataType, ParameterMode.class, "ParameterModeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(temporalEDataType, TemporalType.class, "Temporal", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(temporalTypeObjectEDataType, TemporalType.class, "TemporalTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(versionTypeEDataType, String.class, "VersionType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";
		addAnnotation
		  (accessTypeEEnum,
		   source,
		   new String[] {
			   "name", "access-type"
		   });
		addAnnotation
		  (accessTypeObjectEDataType,
		   source,
		   new String[] {
			   "name", "access-type:Object",
			   "baseType", "access-type"
		   });
		addAnnotation
		  (associationOverrideEClass,
		   source,
		   new String[] {
			   "name", "association-override",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getAssociationOverride_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAssociationOverride_JoinColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "join-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAssociationOverride_ForeignKey(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "foreign-key",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAssociationOverride_JoinTable(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "join-table",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAssociationOverride_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (attributeOverrideEClass,
		   source,
		   new String[] {
			   "name", "attribute-override",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getAttributeOverride_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAttributeOverride_Column(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAttributeOverride_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (attributesEClass,
		   source,
		   new String[] {
			   "name", "attributes",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getAttributes_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAttributes_Id(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "id",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAttributes_EmbeddedId(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "embedded-id",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAttributes_Basic(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "basic",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAttributes_Version(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "version",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAttributes_ManyToOne(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "many-to-one",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAttributes_OneToMany(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "one-to-many",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAttributes_OneToOne(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "one-to-one",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAttributes_ManyToMany(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "many-to-many",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAttributes_ElementCollection(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "element-collection",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAttributes_Embedded(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "embedded",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getAttributes_Transient(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "transient",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (basicEClass,
		   source,
		   new String[] {
			   "name", "basic",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getBasic_Column(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getBasic_Lob(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "lob",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getBasic_Temporal(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "temporal",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getBasic_Enumerated(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "enumerated",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getBasic_Convert(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "convert",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getBasic_Access(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "access"
		   });
		addAnnotation
		  (getBasic_Fetch(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "fetch"
		   });
		addAnnotation
		  (getBasic_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getBasic_Optional(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "optional"
		   });
		addAnnotation
		  (cascadeTypeEClass,
		   source,
		   new String[] {
			   "name", "cascade-type",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getCascadeType_CascadeAll(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "cascade-all",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getCascadeType_CascadePersist(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "cascade-persist",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getCascadeType_CascadeMerge(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "cascade-merge",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getCascadeType_CascadeRemove(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "cascade-remove",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getCascadeType_CascadeRefresh(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "cascade-refresh",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getCascadeType_CascadeDetach(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "cascade-detach",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (collectionTableEClass,
		   source,
		   new String[] {
			   "name", "collection-table",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getCollectionTable_JoinColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "join-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getCollectionTable_ForeignKey(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "foreign-key",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getCollectionTable_UniqueConstraint(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "unique-constraint",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getCollectionTable_Index(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "index",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getCollectionTable_Catalog(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "catalog"
		   });
		addAnnotation
		  (getCollectionTable_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getCollectionTable_Schema(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "schema"
		   });
		addAnnotation
		  (columnEClass,
		   source,
		   new String[] {
			   "name", "column",
			   "kind", "empty"
		   });
		addAnnotation
		  (getColumn_ColumnDefinition(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "column-definition"
		   });
		addAnnotation
		  (getColumn_Insertable(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "insertable"
		   });
		addAnnotation
		  (getColumn_Length(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "length"
		   });
		addAnnotation
		  (getColumn_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getColumn_Nullable(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "nullable"
		   });
		addAnnotation
		  (getColumn_Precision(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "precision"
		   });
		addAnnotation
		  (getColumn_Scale(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "scale"
		   });
		addAnnotation
		  (getColumn_Table(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "table"
		   });
		addAnnotation
		  (getColumn_Unique(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "unique"
		   });
		addAnnotation
		  (getColumn_Updatable(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "updatable"
		   });
		addAnnotation
		  (columnResultEClass,
		   source,
		   new String[] {
			   "name", "column-result",
			   "kind", "empty"
		   });
		addAnnotation
		  (getColumnResult_Class(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "class"
		   });
		addAnnotation
		  (getColumnResult_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (constraintModeEEnum,
		   source,
		   new String[] {
			   "name", "constraint-mode"
		   });
		addAnnotation
		  (constraintModeObjectEDataType,
		   source,
		   new String[] {
			   "name", "constraint-mode:Object",
			   "baseType", "constraint-mode"
		   });
		addAnnotation
		  (constructorResultEClass,
		   source,
		   new String[] {
			   "name", "constructor-result",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getConstructorResult_Column(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getConstructorResult_TargetClass(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "target-class"
		   });
		addAnnotation
		  (convertEClass,
		   source,
		   new String[] {
			   "name", "convert",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getConvert_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getConvert_AttributeName(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "attribute-name"
		   });
		addAnnotation
		  (getConvert_Converter(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "converter"
		   });
		addAnnotation
		  (getConvert_DisableConversion(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "disable-conversion"
		   });
		addAnnotation
		  (converterEClass,
		   source,
		   new String[] {
			   "name", "converter",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getConverter_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getConverter_AutoApply(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "auto-apply"
		   });
		addAnnotation
		  (getConverter_Class(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "class"
		   });
		addAnnotation
		  (discriminatorColumnEClass,
		   source,
		   new String[] {
			   "name", "discriminator-column",
			   "kind", "empty"
		   });
		addAnnotation
		  (getDiscriminatorColumn_ColumnDefinition(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "column-definition"
		   });
		addAnnotation
		  (getDiscriminatorColumn_DiscriminatorType(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "discriminator-type"
		   });
		addAnnotation
		  (getDiscriminatorColumn_Length(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "length"
		   });
		addAnnotation
		  (getDiscriminatorColumn_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (discriminatorTypeEEnum,
		   source,
		   new String[] {
			   "name", "discriminator-type"
		   });
		addAnnotation
		  (discriminatorTypeObjectEDataType,
		   source,
		   new String[] {
			   "name", "discriminator-type:Object",
			   "baseType", "discriminator-type"
		   });
		addAnnotation
		  (discriminatorValueEDataType,
		   source,
		   new String[] {
			   "name", "discriminator-value",
			   "baseType", "http://www.eclipse.org/emf/2003/XMLType#string"
		   });
		addAnnotation
		  (documentRootEClass,
		   source,
		   new String[] {
			   "name", "",
			   "kind", "mixed"
		   });
		addAnnotation
		  (getDocumentRoot_Mixed(),
		   source,
		   new String[] {
			   "kind", "elementWildcard",
			   "name", ":mixed"
		   });
		addAnnotation
		  (getDocumentRoot_XMLNSPrefixMap(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "xmlns:prefix"
		   });
		addAnnotation
		  (getDocumentRoot_XSISchemaLocation(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "xsi:schemaLocation"
		   });
		addAnnotation
		  (getDocumentRoot_EntityMappings(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "entity-mappings",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (elementCollectionEClass,
		   source,
		   new String[] {
			   "name", "element-collection",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getElementCollection_OrderBy(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "order-by",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_OrderColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "order-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_MapKey(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_MapKeyClass(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-class",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_MapKeyTemporal(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-temporal",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_MapKeyEnumerated(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-enumerated",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_MapKeyAttributeOverride(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-attribute-override",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_MapKeyConvert(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-convert",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_MapKeyColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_MapKeyJoinColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-join-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_MapKeyForeignKey(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-foreign-key",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_Column(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_Temporal(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "temporal",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_Enumerated(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "enumerated",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_Lob(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "lob",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_AttributeOverride(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "attribute-override",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_AssociationOverride(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "association-override",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_Convert(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "convert",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_CollectionTable(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "collection-table",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getElementCollection_Access(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "access"
		   });
		addAnnotation
		  (getElementCollection_Fetch(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "fetch"
		   });
		addAnnotation
		  (getElementCollection_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getElementCollection_TargetClass(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "target-class"
		   });
		addAnnotation
		  (embeddableEClass,
		   source,
		   new String[] {
			   "name", "embeddable",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getEmbeddable_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEmbeddable_Attributes(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "attributes",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEmbeddable_Access(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "access"
		   });
		addAnnotation
		  (getEmbeddable_Class(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "class"
		   });
		addAnnotation
		  (getEmbeddable_MetadataComplete(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "metadata-complete"
		   });
		addAnnotation
		  (embeddableAttributesEClass,
		   source,
		   new String[] {
			   "name", "embeddable-attributes",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getEmbeddableAttributes_Basic(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "basic",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEmbeddableAttributes_ManyToOne(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "many-to-one",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEmbeddableAttributes_OneToMany(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "one-to-many",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEmbeddableAttributes_OneToOne(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "one-to-one",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEmbeddableAttributes_ManyToMany(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "many-to-many",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEmbeddableAttributes_ElementCollection(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "element-collection",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEmbeddableAttributes_Embedded(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "embedded",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEmbeddableAttributes_Transient(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "transient",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (embeddedEClass,
		   source,
		   new String[] {
			   "name", "embedded",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getEmbedded_AttributeOverride(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "attribute-override",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEmbedded_AssociationOverride(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "association-override",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEmbedded_Convert(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "convert",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEmbedded_Access(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "access"
		   });
		addAnnotation
		  (getEmbedded_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (embeddedIdEClass,
		   source,
		   new String[] {
			   "name", "embedded-id",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getEmbeddedId_AttributeOverride(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "attribute-override",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEmbeddedId_Access(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "access"
		   });
		addAnnotation
		  (getEmbeddedId_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (emptyTypeEClass,
		   source,
		   new String[] {
			   "name", "emptyType",
			   "kind", "empty"
		   });
		addAnnotation
		  (entityEClass,
		   source,
		   new String[] {
			   "name", "entity",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getEntity_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_Table(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "table",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_SecondaryTable(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "secondary-table",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_PrimaryKeyJoinColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "primary-key-join-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_PrimaryKeyForeignKey(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "primary-key-foreign-key",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_IdClass(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "id-class",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_Inheritance(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "inheritance",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_DiscriminatorValue(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "discriminator-value",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_DiscriminatorColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "discriminator-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_SequenceGenerator(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "sequence-generator",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_TableGenerator(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "table-generator",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_NamedQuery(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "named-query",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_NamedNativeQuery(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "named-native-query",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_NamedStoredProcedureQuery(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "named-stored-procedure-query",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_SqlResultSetMapping(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "sql-result-set-mapping",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_ExcludeDefaultListeners(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "exclude-default-listeners",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_ExcludeSuperclassListeners(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "exclude-superclass-listeners",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_EntityListeners(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "entity-listeners",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_PrePersist(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "pre-persist",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_PostPersist(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "post-persist",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_PreRemove(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "pre-remove",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_PostRemove(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "post-remove",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_PreUpdate(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "pre-update",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_PostUpdate(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "post-update",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_PostLoad(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "post-load",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_AttributeOverride(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "attribute-override",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_AssociationOverride(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "association-override",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_Convert(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "convert",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_NamedEntityGraph(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "named-entity-graph",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_Attributes(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "attributes",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntity_Access(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "access"
		   });
		addAnnotation
		  (getEntity_Cacheable(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "cacheable"
		   });
		addAnnotation
		  (getEntity_Class(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "class"
		   });
		addAnnotation
		  (getEntity_MetadataComplete(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "metadata-complete"
		   });
		addAnnotation
		  (getEntity_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (entityListenerEClass,
		   source,
		   new String[] {
			   "name", "entity-listener",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getEntityListener_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityListener_PrePersist(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "pre-persist",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityListener_PostPersist(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "post-persist",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityListener_PreRemove(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "pre-remove",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityListener_PostRemove(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "post-remove",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityListener_PreUpdate(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "pre-update",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityListener_PostUpdate(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "post-update",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityListener_PostLoad(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "post-load",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityListener_Class(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "class"
		   });
		addAnnotation
		  (entityListenersEClass,
		   source,
		   new String[] {
			   "name", "entity-listeners",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getEntityListeners_EntityListener(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "entity-listener",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (entityMappingsTypeEClass,
		   source,
		   new String[] {
			   "name", "entity-mappings_._type",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getEntityMappingsType_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityMappingsType_PersistenceUnitMetadata(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "persistence-unit-metadata",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityMappingsType_Package(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "package",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityMappingsType_Schema(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "schema",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityMappingsType_Catalog(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "catalog",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityMappingsType_Access(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "access",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityMappingsType_SequenceGenerator(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "sequence-generator",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityMappingsType_TableGenerator(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "table-generator",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityMappingsType_NamedQuery(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "named-query",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityMappingsType_NamedNativeQuery(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "named-native-query",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityMappingsType_NamedStoredProcedureQuery(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "named-stored-procedure-query",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityMappingsType_SqlResultSetMapping(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "sql-result-set-mapping",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityMappingsType_MappedSuperclass(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "mapped-superclass",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityMappingsType_Entity(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "entity",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityMappingsType_Embeddable(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "embeddable",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityMappingsType_Converter(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "converter",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityMappingsType_Version(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "version"
		   });
		addAnnotation
		  (entityResultEClass,
		   source,
		   new String[] {
			   "name", "entity-result",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getEntityResult_FieldResult(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "field-result",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getEntityResult_DiscriminatorColumn(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "discriminator-column"
		   });
		addAnnotation
		  (getEntityResult_EntityClass(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "entity-class"
		   });
		addAnnotation
		  (enumeratedEDataType,
		   source,
		   new String[] {
			   "name", "enumerated",
			   "baseType", "enum-type"
		   });
		addAnnotation
		  (enumTypeEEnum,
		   source,
		   new String[] {
			   "name", "enum-type"
		   });
		addAnnotation
		  (enumTypeObjectEDataType,
		   source,
		   new String[] {
			   "name", "enum-type:Object",
			   "baseType", "enum-type"
		   });
		addAnnotation
		  (fetchTypeEEnum,
		   source,
		   new String[] {
			   "name", "fetch-type"
		   });
		addAnnotation
		  (fetchTypeObjectEDataType,
		   source,
		   new String[] {
			   "name", "fetch-type:Object",
			   "baseType", "fetch-type"
		   });
		addAnnotation
		  (fieldResultEClass,
		   source,
		   new String[] {
			   "name", "field-result",
			   "kind", "empty"
		   });
		addAnnotation
		  (getFieldResult_Column(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "column"
		   });
		addAnnotation
		  (getFieldResult_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (foreignKeyEClass,
		   source,
		   new String[] {
			   "name", "foreign-key",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getForeignKey_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getForeignKey_ConstraintMode(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "constraint-mode"
		   });
		addAnnotation
		  (getForeignKey_ForeignKeyDefinition(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "foreign-key-definition"
		   });
		addAnnotation
		  (getForeignKey_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (generatedValueEClass,
		   source,
		   new String[] {
			   "name", "generated-value",
			   "kind", "empty"
		   });
		addAnnotation
		  (getGeneratedValue_Generator(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "generator"
		   });
		addAnnotation
		  (getGeneratedValue_Strategy(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "strategy"
		   });
		addAnnotation
		  (generationTypeEEnum,
		   source,
		   new String[] {
			   "name", "generation-type"
		   });
		addAnnotation
		  (generationTypeObjectEDataType,
		   source,
		   new String[] {
			   "name", "generation-type:Object",
			   "baseType", "generation-type"
		   });
		addAnnotation
		  (idEClass,
		   source,
		   new String[] {
			   "name", "id",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getId_Column(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getId_GeneratedValue(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "generated-value",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getId_Temporal(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "temporal",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getId_TableGenerator(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "table-generator",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getId_SequenceGenerator(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "sequence-generator",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getId_Access(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "access"
		   });
		addAnnotation
		  (getId_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (idClassEClass,
		   source,
		   new String[] {
			   "name", "id-class",
			   "kind", "empty"
		   });
		addAnnotation
		  (getIdClass_Class(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "class"
		   });
		addAnnotation
		  (indexEClass,
		   source,
		   new String[] {
			   "name", "index",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getIndex_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getIndex_ColumnList(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "column-list"
		   });
		addAnnotation
		  (getIndex_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getIndex_Unique(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "unique"
		   });
		addAnnotation
		  (inheritanceEClass,
		   source,
		   new String[] {
			   "name", "inheritance",
			   "kind", "empty"
		   });
		addAnnotation
		  (getInheritance_Strategy(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "strategy"
		   });
		addAnnotation
		  (inheritanceTypeEEnum,
		   source,
		   new String[] {
			   "name", "inheritance-type"
		   });
		addAnnotation
		  (inheritanceTypeObjectEDataType,
		   source,
		   new String[] {
			   "name", "inheritance-type:Object",
			   "baseType", "inheritance-type"
		   });
		addAnnotation
		  (joinColumnEClass,
		   source,
		   new String[] {
			   "name", "join-column",
			   "kind", "empty"
		   });
		addAnnotation
		  (getJoinColumn_ColumnDefinition(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "column-definition"
		   });
		addAnnotation
		  (getJoinColumn_Insertable(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "insertable"
		   });
		addAnnotation
		  (getJoinColumn_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getJoinColumn_Nullable(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "nullable"
		   });
		addAnnotation
		  (getJoinColumn_ReferencedColumnName(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "referenced-column-name"
		   });
		addAnnotation
		  (getJoinColumn_Table(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "table"
		   });
		addAnnotation
		  (getJoinColumn_Unique(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "unique"
		   });
		addAnnotation
		  (getJoinColumn_Updatable(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "updatable"
		   });
		addAnnotation
		  (joinTableEClass,
		   source,
		   new String[] {
			   "name", "join-table",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getJoinTable_JoinColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "join-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getJoinTable_ForeignKey(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "foreign-key",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getJoinTable_InverseJoinColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "inverse-join-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getJoinTable_InverseForeignKey(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "inverse-foreign-key",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getJoinTable_UniqueConstraint(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "unique-constraint",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getJoinTable_Index(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "index",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getJoinTable_Catalog(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "catalog"
		   });
		addAnnotation
		  (getJoinTable_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getJoinTable_Schema(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "schema"
		   });
		addAnnotation
		  (lobEClass,
		   source,
		   new String[] {
			   "name", "lob",
			   "kind", "empty"
		   });
		addAnnotation
		  (lockModeTypeEEnum,
		   source,
		   new String[] {
			   "name", "lock-mode-type"
		   });
		addAnnotation
		  (lockModeTypeObjectEDataType,
		   source,
		   new String[] {
			   "name", "lock-mode-type:Object",
			   "baseType", "lock-mode-type"
		   });
		addAnnotation
		  (manyToManyEClass,
		   source,
		   new String[] {
			   "name", "many-to-many",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getManyToMany_OrderBy(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "order-by",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToMany_OrderColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "order-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToMany_MapKey(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToMany_MapKeyClass(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-class",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToMany_MapKeyTemporal(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-temporal",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToMany_MapKeyEnumerated(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-enumerated",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToMany_MapKeyAttributeOverride(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-attribute-override",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToMany_MapKeyConvert(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-convert",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToMany_MapKeyColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToMany_MapKeyJoinColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-join-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToMany_MapKeyForeignKey(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-foreign-key",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToMany_JoinTable(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "join-table",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToMany_Cascade(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "cascade",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToMany_Access(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "access"
		   });
		addAnnotation
		  (getManyToMany_Fetch(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "fetch"
		   });
		addAnnotation
		  (getManyToMany_MappedBy(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "mapped-by"
		   });
		addAnnotation
		  (getManyToMany_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getManyToMany_TargetEntity(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "target-entity"
		   });
		addAnnotation
		  (manyToOneEClass,
		   source,
		   new String[] {
			   "name", "many-to-one",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getManyToOne_JoinColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "join-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToOne_ForeignKey(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "foreign-key",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToOne_JoinTable(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "join-table",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToOne_Cascade(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "cascade",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getManyToOne_Access(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "access"
		   });
		addAnnotation
		  (getManyToOne_Fetch(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "fetch"
		   });
		addAnnotation
		  (getManyToOne_Id(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "id"
		   });
		addAnnotation
		  (getManyToOne_MapsId(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "maps-id"
		   });
		addAnnotation
		  (getManyToOne_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getManyToOne_Optional(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "optional"
		   });
		addAnnotation
		  (getManyToOne_TargetEntity(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "target-entity"
		   });
		addAnnotation
		  (mapKeyEClass,
		   source,
		   new String[] {
			   "name", "map-key",
			   "kind", "empty"
		   });
		addAnnotation
		  (getMapKey_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (mapKeyClassEClass,
		   source,
		   new String[] {
			   "name", "map-key-class",
			   "kind", "empty"
		   });
		addAnnotation
		  (getMapKeyClass_Class(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "class"
		   });
		addAnnotation
		  (mapKeyColumnEClass,
		   source,
		   new String[] {
			   "name", "map-key-column",
			   "kind", "empty"
		   });
		addAnnotation
		  (getMapKeyColumn_ColumnDefinition(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "column-definition"
		   });
		addAnnotation
		  (getMapKeyColumn_Insertable(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "insertable"
		   });
		addAnnotation
		  (getMapKeyColumn_Length(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "length"
		   });
		addAnnotation
		  (getMapKeyColumn_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getMapKeyColumn_Nullable(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "nullable"
		   });
		addAnnotation
		  (getMapKeyColumn_Precision(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "precision"
		   });
		addAnnotation
		  (getMapKeyColumn_Scale(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "scale"
		   });
		addAnnotation
		  (getMapKeyColumn_Table(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "table"
		   });
		addAnnotation
		  (getMapKeyColumn_Unique(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "unique"
		   });
		addAnnotation
		  (getMapKeyColumn_Updatable(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "updatable"
		   });
		addAnnotation
		  (mapKeyJoinColumnEClass,
		   source,
		   new String[] {
			   "name", "map-key-join-column",
			   "kind", "empty"
		   });
		addAnnotation
		  (getMapKeyJoinColumn_ColumnDefinition(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "column-definition"
		   });
		addAnnotation
		  (getMapKeyJoinColumn_Insertable(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "insertable"
		   });
		addAnnotation
		  (getMapKeyJoinColumn_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getMapKeyJoinColumn_Nullable(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "nullable"
		   });
		addAnnotation
		  (getMapKeyJoinColumn_ReferencedColumnName(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "referenced-column-name"
		   });
		addAnnotation
		  (getMapKeyJoinColumn_Table(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "table"
		   });
		addAnnotation
		  (getMapKeyJoinColumn_Unique(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "unique"
		   });
		addAnnotation
		  (getMapKeyJoinColumn_Updatable(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "updatable"
		   });
		addAnnotation
		  (mappedSuperclassEClass,
		   source,
		   new String[] {
			   "name", "mapped-superclass",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getMappedSuperclass_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getMappedSuperclass_IdClass(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "id-class",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getMappedSuperclass_ExcludeDefaultListeners(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "exclude-default-listeners",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getMappedSuperclass_ExcludeSuperclassListeners(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "exclude-superclass-listeners",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getMappedSuperclass_EntityListeners(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "entity-listeners",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getMappedSuperclass_PrePersist(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "pre-persist",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getMappedSuperclass_PostPersist(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "post-persist",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getMappedSuperclass_PreRemove(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "pre-remove",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getMappedSuperclass_PostRemove(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "post-remove",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getMappedSuperclass_PreUpdate(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "pre-update",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getMappedSuperclass_PostUpdate(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "post-update",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getMappedSuperclass_PostLoad(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "post-load",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getMappedSuperclass_Attributes(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "attributes",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getMappedSuperclass_Access(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "access"
		   });
		addAnnotation
		  (getMappedSuperclass_Class(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "class"
		   });
		addAnnotation
		  (getMappedSuperclass_MetadataComplete(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "metadata-complete"
		   });
		addAnnotation
		  (namedAttributeNodeEClass,
		   source,
		   new String[] {
			   "name", "named-attribute-node",
			   "kind", "empty"
		   });
		addAnnotation
		  (getNamedAttributeNode_KeySubgraph(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "key-subgraph"
		   });
		addAnnotation
		  (getNamedAttributeNode_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getNamedAttributeNode_Subgraph(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "subgraph"
		   });
		addAnnotation
		  (namedEntityGraphEClass,
		   source,
		   new String[] {
			   "name", "named-entity-graph",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getNamedEntityGraph_NamedAttributeNode(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "named-attribute-node",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getNamedEntityGraph_Subgraph(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "subgraph",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getNamedEntityGraph_SubclassSubgraph(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "subclass-subgraph",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getNamedEntityGraph_IncludeAllAttributes(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "include-all-attributes"
		   });
		addAnnotation
		  (getNamedEntityGraph_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (namedNativeQueryEClass,
		   source,
		   new String[] {
			   "name", "named-native-query",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getNamedNativeQuery_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getNamedNativeQuery_Query(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "query",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getNamedNativeQuery_Hint(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "hint",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getNamedNativeQuery_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getNamedNativeQuery_ResultClass(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "result-class"
		   });
		addAnnotation
		  (getNamedNativeQuery_ResultSetMapping(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "result-set-mapping"
		   });
		addAnnotation
		  (namedQueryEClass,
		   source,
		   new String[] {
			   "name", "named-query",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getNamedQuery_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getNamedQuery_Query(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "query",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getNamedQuery_LockMode(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "lock-mode",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getNamedQuery_Hint(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "hint",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getNamedQuery_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (namedStoredProcedureQueryEClass,
		   source,
		   new String[] {
			   "name", "named-stored-procedure-query",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getNamedStoredProcedureQuery_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getNamedStoredProcedureQuery_Parameter(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "parameter",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getNamedStoredProcedureQuery_ResultClass(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "result-class",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getNamedStoredProcedureQuery_ResultSetMapping(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "result-set-mapping",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getNamedStoredProcedureQuery_Hint(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "hint",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getNamedStoredProcedureQuery_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getNamedStoredProcedureQuery_ProcedureName(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "procedure-name"
		   });
		addAnnotation
		  (namedSubgraphEClass,
		   source,
		   new String[] {
			   "name", "named-subgraph",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getNamedSubgraph_NamedAttributeNode(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "named-attribute-node",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getNamedSubgraph_Class(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "class"
		   });
		addAnnotation
		  (getNamedSubgraph_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (oneToManyEClass,
		   source,
		   new String[] {
			   "name", "one-to-many",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getOneToMany_OrderBy(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "order-by",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToMany_OrderColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "order-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToMany_MapKey(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToMany_MapKeyClass(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-class",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToMany_MapKeyTemporal(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-temporal",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToMany_MapKeyEnumerated(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-enumerated",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToMany_MapKeyAttributeOverride(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-attribute-override",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToMany_MapKeyConvert(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-convert",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToMany_MapKeyColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToMany_MapKeyJoinColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-join-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToMany_MapKeyForeignKey(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "map-key-foreign-key",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToMany_JoinTable(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "join-table",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToMany_JoinColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "join-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToMany_ForeignKey(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "foreign-key",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToMany_Cascade(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "cascade",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToMany_Access(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "access"
		   });
		addAnnotation
		  (getOneToMany_Fetch(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "fetch"
		   });
		addAnnotation
		  (getOneToMany_MappedBy(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "mapped-by"
		   });
		addAnnotation
		  (getOneToMany_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getOneToMany_OrphanRemoval(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "orphan-removal"
		   });
		addAnnotation
		  (getOneToMany_TargetEntity(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "target-entity"
		   });
		addAnnotation
		  (oneToOneEClass,
		   source,
		   new String[] {
			   "name", "one-to-one",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getOneToOne_PrimaryKeyJoinColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "primary-key-join-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToOne_PrimaryKeyForeignKey(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "primary-key-foreign-key",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToOne_JoinColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "join-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToOne_ForeignKey(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "foreign-key",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToOne_JoinTable(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "join-table",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToOne_Cascade(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "cascade",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOneToOne_Access(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "access"
		   });
		addAnnotation
		  (getOneToOne_Fetch(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "fetch"
		   });
		addAnnotation
		  (getOneToOne_Id(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "id"
		   });
		addAnnotation
		  (getOneToOne_MappedBy(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "mapped-by"
		   });
		addAnnotation
		  (getOneToOne_MapsId(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "maps-id"
		   });
		addAnnotation
		  (getOneToOne_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getOneToOne_Optional(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "optional"
		   });
		addAnnotation
		  (getOneToOne_OrphanRemoval(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "orphan-removal"
		   });
		addAnnotation
		  (getOneToOne_TargetEntity(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "target-entity"
		   });
		addAnnotation
		  (orderByEDataType,
		   source,
		   new String[] {
			   "name", "order-by",
			   "baseType", "http://www.eclipse.org/emf/2003/XMLType#string"
		   });
		addAnnotation
		  (orderColumnEClass,
		   source,
		   new String[] {
			   "name", "order-column",
			   "kind", "empty"
		   });
		addAnnotation
		  (getOrderColumn_ColumnDefinition(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "column-definition"
		   });
		addAnnotation
		  (getOrderColumn_Insertable(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "insertable"
		   });
		addAnnotation
		  (getOrderColumn_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getOrderColumn_Nullable(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "nullable"
		   });
		addAnnotation
		  (getOrderColumn_Updatable(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "updatable"
		   });
		addAnnotation
		  (parameterModeEEnum,
		   source,
		   new String[] {
			   "name", "parameter-mode"
		   });
		addAnnotation
		  (parameterModeObjectEDataType,
		   source,
		   new String[] {
			   "name", "parameter-mode:Object",
			   "baseType", "parameter-mode"
		   });
		addAnnotation
		  (persistenceUnitDefaultsEClass,
		   source,
		   new String[] {
			   "name", "persistence-unit-defaults",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getPersistenceUnitDefaults_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getPersistenceUnitDefaults_Schema(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "schema",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getPersistenceUnitDefaults_Catalog(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "catalog",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getPersistenceUnitDefaults_DelimitedIdentifiers(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "delimited-identifiers",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getPersistenceUnitDefaults_Access(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "access",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getPersistenceUnitDefaults_CascadePersist(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "cascade-persist",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getPersistenceUnitDefaults_EntityListeners(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "entity-listeners",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (persistenceUnitMetadataEClass,
		   source,
		   new String[] {
			   "name", "persistence-unit-metadata",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getPersistenceUnitMetadata_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getPersistenceUnitMetadata_XmlMappingMetadataComplete(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "xml-mapping-metadata-complete",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getPersistenceUnitMetadata_PersistenceUnitDefaults(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "persistence-unit-defaults",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (postLoadEClass,
		   source,
		   new String[] {
			   "name", "post-load",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getPostLoad_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getPostLoad_MethodName(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "method-name"
		   });
		addAnnotation
		  (postPersistEClass,
		   source,
		   new String[] {
			   "name", "post-persist",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getPostPersist_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getPostPersist_MethodName(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "method-name"
		   });
		addAnnotation
		  (postRemoveEClass,
		   source,
		   new String[] {
			   "name", "post-remove",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getPostRemove_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getPostRemove_MethodName(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "method-name"
		   });
		addAnnotation
		  (postUpdateEClass,
		   source,
		   new String[] {
			   "name", "post-update",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getPostUpdate_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getPostUpdate_MethodName(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "method-name"
		   });
		addAnnotation
		  (prePersistEClass,
		   source,
		   new String[] {
			   "name", "pre-persist",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getPrePersist_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getPrePersist_MethodName(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "method-name"
		   });
		addAnnotation
		  (preRemoveEClass,
		   source,
		   new String[] {
			   "name", "pre-remove",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getPreRemove_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getPreRemove_MethodName(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "method-name"
		   });
		addAnnotation
		  (preUpdateEClass,
		   source,
		   new String[] {
			   "name", "pre-update",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getPreUpdate_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getPreUpdate_MethodName(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "method-name"
		   });
		addAnnotation
		  (primaryKeyJoinColumnEClass,
		   source,
		   new String[] {
			   "name", "primary-key-join-column",
			   "kind", "empty"
		   });
		addAnnotation
		  (getPrimaryKeyJoinColumn_ColumnDefinition(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "column-definition"
		   });
		addAnnotation
		  (getPrimaryKeyJoinColumn_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getPrimaryKeyJoinColumn_ReferencedColumnName(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "referenced-column-name"
		   });
		addAnnotation
		  (queryHintEClass,
		   source,
		   new String[] {
			   "name", "query-hint",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getQueryHint_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getQueryHint_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getQueryHint_Value(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "value"
		   });
		addAnnotation
		  (secondaryTableEClass,
		   source,
		   new String[] {
			   "name", "secondary-table",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getSecondaryTable_PrimaryKeyJoinColumn(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "primary-key-join-column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getSecondaryTable_PrimaryKeyForeignKey(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "primary-key-foreign-key",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getSecondaryTable_UniqueConstraint(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "unique-constraint",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getSecondaryTable_Index(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "index",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getSecondaryTable_Catalog(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "catalog"
		   });
		addAnnotation
		  (getSecondaryTable_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getSecondaryTable_Schema(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "schema"
		   });
		addAnnotation
		  (sequenceGeneratorEClass,
		   source,
		   new String[] {
			   "name", "sequence-generator",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getSequenceGenerator_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getSequenceGenerator_AllocationSize(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "allocation-size"
		   });
		addAnnotation
		  (getSequenceGenerator_Catalog(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "catalog"
		   });
		addAnnotation
		  (getSequenceGenerator_InitialValue(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "initial-value"
		   });
		addAnnotation
		  (getSequenceGenerator_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getSequenceGenerator_Schema(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "schema"
		   });
		addAnnotation
		  (getSequenceGenerator_SequenceName(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "sequence-name"
		   });
		addAnnotation
		  (sqlResultSetMappingEClass,
		   source,
		   new String[] {
			   "name", "sql-result-set-mapping",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getSqlResultSetMapping_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getSqlResultSetMapping_EntityResult(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "entity-result",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getSqlResultSetMapping_ConstructorResult(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "constructor-result",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getSqlResultSetMapping_ColumnResult(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "column-result",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getSqlResultSetMapping_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (storedProcedureParameterEClass,
		   source,
		   new String[] {
			   "name", "stored-procedure-parameter",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getStoredProcedureParameter_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getStoredProcedureParameter_Class(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "class"
		   });
		addAnnotation
		  (getStoredProcedureParameter_Mode(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "mode"
		   });
		addAnnotation
		  (getStoredProcedureParameter_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (tableEClass,
		   source,
		   new String[] {
			   "name", "table",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getTable_UniqueConstraint(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "unique-constraint",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getTable_Index(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "index",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getTable_Catalog(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "catalog"
		   });
		addAnnotation
		  (getTable_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getTable_Schema(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "schema"
		   });
		addAnnotation
		  (tableGeneratorEClass,
		   source,
		   new String[] {
			   "name", "table-generator",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getTableGenerator_Description(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "description",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getTableGenerator_UniqueConstraint(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "unique-constraint",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getTableGenerator_Index(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "index",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getTableGenerator_AllocationSize(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "allocation-size"
		   });
		addAnnotation
		  (getTableGenerator_Catalog(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "catalog"
		   });
		addAnnotation
		  (getTableGenerator_InitialValue(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "initial-value"
		   });
		addAnnotation
		  (getTableGenerator_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (getTableGenerator_PkColumnName(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "pk-column-name"
		   });
		addAnnotation
		  (getTableGenerator_PkColumnValue(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "pk-column-value"
		   });
		addAnnotation
		  (getTableGenerator_Schema(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "schema"
		   });
		addAnnotation
		  (getTableGenerator_Table(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "table"
		   });
		addAnnotation
		  (getTableGenerator_ValueColumnName(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "value-column-name"
		   });
		addAnnotation
		  (temporalEDataType,
		   source,
		   new String[] {
			   "name", "temporal",
			   "baseType", "temporal-type"
		   });
		addAnnotation
		  (temporalTypeEEnum,
		   source,
		   new String[] {
			   "name", "temporal-type"
		   });
		addAnnotation
		  (temporalTypeObjectEDataType,
		   source,
		   new String[] {
			   "name", "temporal-type:Object",
			   "baseType", "temporal-type"
		   });
		addAnnotation
		  (transientEClass,
		   source,
		   new String[] {
			   "name", "transient",
			   "kind", "empty"
		   });
		addAnnotation
		  (getTransient_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (uniqueConstraintEClass,
		   source,
		   new String[] {
			   "name", "unique-constraint",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getUniqueConstraint_ColumnName(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "column-name",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getUniqueConstraint_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (versionEClass,
		   source,
		   new String[] {
			   "name", "version",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getVersion_Column(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "column",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getVersion_Temporal(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "temporal",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getVersion_Access(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "access"
		   });
		addAnnotation
		  (getVersion_Name(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "name"
		   });
		addAnnotation
		  (versionTypeEDataType,
		   source,
		   new String[] {
			   "name", "versionType",
			   "baseType", "http://www.eclipse.org/emf/2003/XMLType#token",
			   "pattern", "[0-9]+(\\.[0-9]+)*"
		   });
	}

} //OrmPackageImpl
