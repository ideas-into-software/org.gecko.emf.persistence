/**
 * Copyright (c) 2012 - 2022
 */
package org.gecko.emf.persistence.jpa.orm.model.orm.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.gecko.emf.persistence.jpa.orm.model.orm.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrmPackage
 * @generated
 */
public class OrmAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static OrmPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OrmAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = OrmPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OrmSwitch<Adapter> modelSwitch =
		new OrmSwitch<Adapter>() {
			@Override
			public Adapter caseAssociationOverride(AssociationOverride object) {
				return createAssociationOverrideAdapter();
			}
			@Override
			public Adapter caseAttributeOverride(AttributeOverride object) {
				return createAttributeOverrideAdapter();
			}
			@Override
			public Adapter caseAttributes(Attributes object) {
				return createAttributesAdapter();
			}
			@Override
			public Adapter caseBasic(Basic object) {
				return createBasicAdapter();
			}
			@Override
			public Adapter caseCascadeType(CascadeType object) {
				return createCascadeTypeAdapter();
			}
			@Override
			public Adapter caseCollectionTable(CollectionTable object) {
				return createCollectionTableAdapter();
			}
			@Override
			public Adapter caseColumn(Column object) {
				return createColumnAdapter();
			}
			@Override
			public Adapter caseColumnResult(ColumnResult object) {
				return createColumnResultAdapter();
			}
			@Override
			public Adapter caseConstructorResult(ConstructorResult object) {
				return createConstructorResultAdapter();
			}
			@Override
			public Adapter caseConvert(Convert object) {
				return createConvertAdapter();
			}
			@Override
			public Adapter caseConverter(Converter object) {
				return createConverterAdapter();
			}
			@Override
			public Adapter caseDiscriminatorColumn(DiscriminatorColumn object) {
				return createDiscriminatorColumnAdapter();
			}
			@Override
			public Adapter caseDocumentRoot(DocumentRoot object) {
				return createDocumentRootAdapter();
			}
			@Override
			public Adapter caseElementCollection(ElementCollection object) {
				return createElementCollectionAdapter();
			}
			@Override
			public Adapter caseEmbeddable(Embeddable object) {
				return createEmbeddableAdapter();
			}
			@Override
			public Adapter caseEmbeddableAttributes(EmbeddableAttributes object) {
				return createEmbeddableAttributesAdapter();
			}
			@Override
			public Adapter caseEmbedded(Embedded object) {
				return createEmbeddedAdapter();
			}
			@Override
			public Adapter caseEmbeddedId(EmbeddedId object) {
				return createEmbeddedIdAdapter();
			}
			@Override
			public Adapter caseEmptyType(EmptyType object) {
				return createEmptyTypeAdapter();
			}
			@Override
			public Adapter caseEntity(Entity object) {
				return createEntityAdapter();
			}
			@Override
			public Adapter caseEntityListener(EntityListener object) {
				return createEntityListenerAdapter();
			}
			@Override
			public Adapter caseEntityListeners(EntityListeners object) {
				return createEntityListenersAdapter();
			}
			@Override
			public Adapter caseEntityMappingsType(EntityMappingsType object) {
				return createEntityMappingsTypeAdapter();
			}
			@Override
			public Adapter caseEntityResult(EntityResult object) {
				return createEntityResultAdapter();
			}
			@Override
			public Adapter caseFieldResult(FieldResult object) {
				return createFieldResultAdapter();
			}
			@Override
			public Adapter caseForeignKey(ForeignKey object) {
				return createForeignKeyAdapter();
			}
			@Override
			public Adapter caseGeneratedValue(GeneratedValue object) {
				return createGeneratedValueAdapter();
			}
			@Override
			public Adapter caseId(Id object) {
				return createIdAdapter();
			}
			@Override
			public Adapter caseIdClass(IdClass object) {
				return createIdClassAdapter();
			}
			@Override
			public Adapter caseIndex(Index object) {
				return createIndexAdapter();
			}
			@Override
			public Adapter caseInheritance(Inheritance object) {
				return createInheritanceAdapter();
			}
			@Override
			public Adapter caseJoinColumn(JoinColumn object) {
				return createJoinColumnAdapter();
			}
			@Override
			public Adapter caseJoinTable(JoinTable object) {
				return createJoinTableAdapter();
			}
			@Override
			public Adapter caseLob(Lob object) {
				return createLobAdapter();
			}
			@Override
			public Adapter caseManyToMany(ManyToMany object) {
				return createManyToManyAdapter();
			}
			@Override
			public Adapter caseManyToOne(ManyToOne object) {
				return createManyToOneAdapter();
			}
			@Override
			public Adapter caseMapKey(MapKey object) {
				return createMapKeyAdapter();
			}
			@Override
			public Adapter caseMapKeyClass(MapKeyClass object) {
				return createMapKeyClassAdapter();
			}
			@Override
			public Adapter caseMapKeyColumn(MapKeyColumn object) {
				return createMapKeyColumnAdapter();
			}
			@Override
			public Adapter caseMapKeyJoinColumn(MapKeyJoinColumn object) {
				return createMapKeyJoinColumnAdapter();
			}
			@Override
			public Adapter caseMappedSuperclass(MappedSuperclass object) {
				return createMappedSuperclassAdapter();
			}
			@Override
			public Adapter caseNamedAttributeNode(NamedAttributeNode object) {
				return createNamedAttributeNodeAdapter();
			}
			@Override
			public Adapter caseNamedEntityGraph(NamedEntityGraph object) {
				return createNamedEntityGraphAdapter();
			}
			@Override
			public Adapter caseNamedNativeQuery(NamedNativeQuery object) {
				return createNamedNativeQueryAdapter();
			}
			@Override
			public Adapter caseNamedQuery(NamedQuery object) {
				return createNamedQueryAdapter();
			}
			@Override
			public Adapter caseNamedStoredProcedureQuery(NamedStoredProcedureQuery object) {
				return createNamedStoredProcedureQueryAdapter();
			}
			@Override
			public Adapter caseNamedSubgraph(NamedSubgraph object) {
				return createNamedSubgraphAdapter();
			}
			@Override
			public Adapter caseOneToMany(OneToMany object) {
				return createOneToManyAdapter();
			}
			@Override
			public Adapter caseOneToOne(OneToOne object) {
				return createOneToOneAdapter();
			}
			@Override
			public Adapter caseOrderColumn(OrderColumn object) {
				return createOrderColumnAdapter();
			}
			@Override
			public Adapter casePersistenceUnitDefaults(PersistenceUnitDefaults object) {
				return createPersistenceUnitDefaultsAdapter();
			}
			@Override
			public Adapter casePersistenceUnitMetadata(PersistenceUnitMetadata object) {
				return createPersistenceUnitMetadataAdapter();
			}
			@Override
			public Adapter casePostLoad(PostLoad object) {
				return createPostLoadAdapter();
			}
			@Override
			public Adapter casePostPersist(PostPersist object) {
				return createPostPersistAdapter();
			}
			@Override
			public Adapter casePostRemove(PostRemove object) {
				return createPostRemoveAdapter();
			}
			@Override
			public Adapter casePostUpdate(PostUpdate object) {
				return createPostUpdateAdapter();
			}
			@Override
			public Adapter casePrePersist(PrePersist object) {
				return createPrePersistAdapter();
			}
			@Override
			public Adapter casePreRemove(PreRemove object) {
				return createPreRemoveAdapter();
			}
			@Override
			public Adapter casePreUpdate(PreUpdate object) {
				return createPreUpdateAdapter();
			}
			@Override
			public Adapter casePrimaryKeyJoinColumn(PrimaryKeyJoinColumn object) {
				return createPrimaryKeyJoinColumnAdapter();
			}
			@Override
			public Adapter caseQueryHint(QueryHint object) {
				return createQueryHintAdapter();
			}
			@Override
			public Adapter caseSecondaryTable(SecondaryTable object) {
				return createSecondaryTableAdapter();
			}
			@Override
			public Adapter caseSequenceGenerator(SequenceGenerator object) {
				return createSequenceGeneratorAdapter();
			}
			@Override
			public Adapter caseSqlResultSetMapping(SqlResultSetMapping object) {
				return createSqlResultSetMappingAdapter();
			}
			@Override
			public Adapter caseStoredProcedureParameter(StoredProcedureParameter object) {
				return createStoredProcedureParameterAdapter();
			}
			@Override
			public Adapter caseTable(Table object) {
				return createTableAdapter();
			}
			@Override
			public Adapter caseTableGenerator(TableGenerator object) {
				return createTableGeneratorAdapter();
			}
			@Override
			public Adapter caseTransient(Transient object) {
				return createTransientAdapter();
			}
			@Override
			public Adapter caseUniqueConstraint(UniqueConstraint object) {
				return createUniqueConstraintAdapter();
			}
			@Override
			public Adapter caseVersion(Version object) {
				return createVersionAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.AssociationOverride <em>Association Override</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.AssociationOverride
	 * @generated
	 */
	public Adapter createAssociationOverrideAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.AttributeOverride <em>Attribute Override</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.AttributeOverride
	 * @generated
	 */
	public Adapter createAttributeOverrideAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Attributes <em>Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.Attributes
	 * @generated
	 */
	public Adapter createAttributesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Basic <em>Basic</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.Basic
	 * @generated
	 */
	public Adapter createBasicAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.CascadeType <em>Cascade Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.CascadeType
	 * @generated
	 */
	public Adapter createCascadeTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.CollectionTable <em>Collection Table</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.CollectionTable
	 * @generated
	 */
	public Adapter createCollectionTableAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Column <em>Column</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.Column
	 * @generated
	 */
	public Adapter createColumnAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ColumnResult <em>Column Result</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.ColumnResult
	 * @generated
	 */
	public Adapter createColumnResultAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ConstructorResult <em>Constructor Result</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.ConstructorResult
	 * @generated
	 */
	public Adapter createConstructorResultAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Convert <em>Convert</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.Convert
	 * @generated
	 */
	public Adapter createConvertAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Converter <em>Converter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.Converter
	 * @generated
	 */
	public Adapter createConverterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.DiscriminatorColumn <em>Discriminator Column</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.DiscriminatorColumn
	 * @generated
	 */
	public Adapter createDiscriminatorColumnAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.DocumentRoot
	 * @generated
	 */
	public Adapter createDocumentRootAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection <em>Element Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.ElementCollection
	 * @generated
	 */
	public Adapter createElementCollectionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Embeddable <em>Embeddable</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.Embeddable
	 * @generated
	 */
	public Adapter createEmbeddableAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.EmbeddableAttributes <em>Embeddable Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.EmbeddableAttributes
	 * @generated
	 */
	public Adapter createEmbeddableAttributesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Embedded <em>Embedded</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.Embedded
	 * @generated
	 */
	public Adapter createEmbeddedAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.EmbeddedId <em>Embedded Id</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.EmbeddedId
	 * @generated
	 */
	public Adapter createEmbeddedIdAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.EmptyType <em>Empty Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.EmptyType
	 * @generated
	 */
	public Adapter createEmptyTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Entity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.Entity
	 * @generated
	 */
	public Adapter createEntityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener <em>Entity Listener</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.EntityListener
	 * @generated
	 */
	public Adapter createEntityListenerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityListeners <em>Entity Listeners</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.EntityListeners
	 * @generated
	 */
	public Adapter createEntityListenersAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityMappingsType <em>Entity Mappings Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.EntityMappingsType
	 * @generated
	 */
	public Adapter createEntityMappingsTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.EntityResult <em>Entity Result</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.EntityResult
	 * @generated
	 */
	public Adapter createEntityResultAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.FieldResult <em>Field Result</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.FieldResult
	 * @generated
	 */
	public Adapter createFieldResultAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ForeignKey <em>Foreign Key</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.ForeignKey
	 * @generated
	 */
	public Adapter createForeignKeyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.GeneratedValue <em>Generated Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.GeneratedValue
	 * @generated
	 */
	public Adapter createGeneratedValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Id <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.Id
	 * @generated
	 */
	public Adapter createIdAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.IdClass <em>Id Class</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.IdClass
	 * @generated
	 */
	public Adapter createIdClassAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Index <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.Index
	 * @generated
	 */
	public Adapter createIndexAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Inheritance <em>Inheritance</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.Inheritance
	 * @generated
	 */
	public Adapter createInheritanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.JoinColumn <em>Join Column</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.JoinColumn
	 * @generated
	 */
	public Adapter createJoinColumnAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.JoinTable <em>Join Table</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.JoinTable
	 * @generated
	 */
	public Adapter createJoinTableAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Lob <em>Lob</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.Lob
	 * @generated
	 */
	public Adapter createLobAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ManyToMany <em>Many To Many</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.ManyToMany
	 * @generated
	 */
	public Adapter createManyToManyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.ManyToOne <em>Many To One</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.ManyToOne
	 * @generated
	 */
	public Adapter createManyToOneAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.MapKey <em>Map Key</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.MapKey
	 * @generated
	 */
	public Adapter createMapKeyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.MapKeyClass <em>Map Key Class</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.MapKeyClass
	 * @generated
	 */
	public Adapter createMapKeyClassAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.MapKeyColumn <em>Map Key Column</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.MapKeyColumn
	 * @generated
	 */
	public Adapter createMapKeyColumnAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.MapKeyJoinColumn <em>Map Key Join Column</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.MapKeyJoinColumn
	 * @generated
	 */
	public Adapter createMapKeyJoinColumnAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.MappedSuperclass <em>Mapped Superclass</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.MappedSuperclass
	 * @generated
	 */
	public Adapter createMappedSuperclassAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedAttributeNode <em>Named Attribute Node</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.NamedAttributeNode
	 * @generated
	 */
	public Adapter createNamedAttributeNodeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedEntityGraph <em>Named Entity Graph</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.NamedEntityGraph
	 * @generated
	 */
	public Adapter createNamedEntityGraphAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedNativeQuery <em>Named Native Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.NamedNativeQuery
	 * @generated
	 */
	public Adapter createNamedNativeQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedQuery <em>Named Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.NamedQuery
	 * @generated
	 */
	public Adapter createNamedQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedStoredProcedureQuery <em>Named Stored Procedure Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.NamedStoredProcedureQuery
	 * @generated
	 */
	public Adapter createNamedStoredProcedureQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.NamedSubgraph <em>Named Subgraph</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.NamedSubgraph
	 * @generated
	 */
	public Adapter createNamedSubgraphAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.OneToMany <em>One To Many</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OneToMany
	 * @generated
	 */
	public Adapter createOneToManyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.OneToOne <em>One To One</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OneToOne
	 * @generated
	 */
	public Adapter createOneToOneAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.OrderColumn <em>Order Column</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.OrderColumn
	 * @generated
	 */
	public Adapter createOrderColumnAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.PersistenceUnitDefaults <em>Persistence Unit Defaults</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.PersistenceUnitDefaults
	 * @generated
	 */
	public Adapter createPersistenceUnitDefaultsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.PersistenceUnitMetadata <em>Persistence Unit Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.PersistenceUnitMetadata
	 * @generated
	 */
	public Adapter createPersistenceUnitMetadataAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.PostLoad <em>Post Load</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.PostLoad
	 * @generated
	 */
	public Adapter createPostLoadAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.PostPersist <em>Post Persist</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.PostPersist
	 * @generated
	 */
	public Adapter createPostPersistAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.PostRemove <em>Post Remove</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.PostRemove
	 * @generated
	 */
	public Adapter createPostRemoveAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.PostUpdate <em>Post Update</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.PostUpdate
	 * @generated
	 */
	public Adapter createPostUpdateAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.PrePersist <em>Pre Persist</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.PrePersist
	 * @generated
	 */
	public Adapter createPrePersistAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.PreRemove <em>Pre Remove</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.PreRemove
	 * @generated
	 */
	public Adapter createPreRemoveAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.PreUpdate <em>Pre Update</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.PreUpdate
	 * @generated
	 */
	public Adapter createPreUpdateAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.PrimaryKeyJoinColumn <em>Primary Key Join Column</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.PrimaryKeyJoinColumn
	 * @generated
	 */
	public Adapter createPrimaryKeyJoinColumnAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.QueryHint <em>Query Hint</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.QueryHint
	 * @generated
	 */
	public Adapter createQueryHintAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.SecondaryTable <em>Secondary Table</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.SecondaryTable
	 * @generated
	 */
	public Adapter createSecondaryTableAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.SequenceGenerator <em>Sequence Generator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.SequenceGenerator
	 * @generated
	 */
	public Adapter createSequenceGeneratorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.SqlResultSetMapping <em>Sql Result Set Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.SqlResultSetMapping
	 * @generated
	 */
	public Adapter createSqlResultSetMappingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.StoredProcedureParameter <em>Stored Procedure Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.StoredProcedureParameter
	 * @generated
	 */
	public Adapter createStoredProcedureParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Table <em>Table</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.Table
	 * @generated
	 */
	public Adapter createTableAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator <em>Table Generator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.TableGenerator
	 * @generated
	 */
	public Adapter createTableGeneratorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Transient <em>Transient</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.Transient
	 * @generated
	 */
	public Adapter createTransientAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.UniqueConstraint <em>Unique Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.UniqueConstraint
	 * @generated
	 */
	public Adapter createUniqueConstraintAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gecko.emf.persistence.jpa.orm.model.orm.Version <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gecko.emf.persistence.jpa.orm.model.orm.Version
	 * @generated
	 */
	public Adapter createVersionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //OrmAdapterFactory
