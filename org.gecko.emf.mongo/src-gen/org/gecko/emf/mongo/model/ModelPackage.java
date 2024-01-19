/**
 */
package org.gecko.emf.mongo.model;

import java.util.Iterator;

import org.bson.Document;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.gecko.emf.mongo.model.ModelFactory
 * @model kind="package"
 * @generated
 */
public class ModelPackage extends EPackageImpl {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNS_URI = "org.gecko.emf.mongo.model";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNS_PREFIX = "model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final ModelPackage eINSTANCE = org.gecko.emf.mongo.model.ModelPackage.init();

	/**
	 * The meta object id for the '{@link java.lang.Iterable <em>Iterable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Iterable
	 * @see org.gecko.emf.mongo.model.ModelPackage#getIterable()
	 * @generated
	 */
	public static final int ITERABLE = 0;

	/**
	 * The number of structural features of the '<em>Iterable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ITERABLE_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Iterable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ITERABLE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.gecko.emf.mongo.model.EMongoCursor <em>EMongo Cursor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.gecko.emf.mongo.model.EMongoCursor
	 * @see org.gecko.emf.mongo.model.ModelPackage#getEMongoCursor()
	 * @generated
	 */
	public static final int EMONGO_CURSOR = 1;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EMONGO_CURSOR__CURSOR = 0;

	/**
	 * The feature id for the '<em><b>Collection</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EMONGO_CURSOR__COLLECTION = 1;

	/**
	 * The number of structural features of the '<em>EMongo Cursor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EMONGO_CURSOR_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EMongo Cursor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EMONGO_CURSOR_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.gecko.emf.mongo.model.EMongoQuery <em>EMongo Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.gecko.emf.mongo.model.EMongoQuery
	 * @see org.gecko.emf.mongo.model.ModelPackage#getEMongoQuery()
	 * @generated
	 */
	public static final int EMONGO_QUERY = 2;

	/**
	 * The feature id for the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EMONGO_QUERY__FILTER = 0;

	/**
	 * The feature id for the '<em><b>Projection</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EMONGO_QUERY__PROJECTION = 1;

	/**
	 * The feature id for the '<em><b>Projection Only</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EMONGO_QUERY__PROJECTION_ONLY = 2;

	/**
	 * The feature id for the '<em><b>Sort</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EMONGO_QUERY__SORT = 3;

	/**
	 * The feature id for the '<em><b>Skip</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EMONGO_QUERY__SKIP = 4;

	/**
	 * The feature id for the '<em><b>Limit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EMONGO_QUERY__LIMIT = 5;

	/**
	 * The feature id for the '<em><b>Batch Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EMONGO_QUERY__BATCH_SIZE = 6;

	/**
	 * The number of structural features of the '<em>EMongo Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EMONGO_QUERY_FEATURE_COUNT = 7;

	/**
	 * The number of operations of the '<em>EMongo Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EMONGO_QUERY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '<em>EDocument</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.bson.Document
	 * @see org.gecko.emf.mongo.model.ModelPackage#getEDocument()
	 * @generated
	 */
	public static final int EDOCUMENT = 3;

	/**
	 * The meta object id for the '<em>ECursor</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mongodb.client.MongoCursor
	 * @see org.gecko.emf.mongo.model.ModelPackage#getECursor()
	 * @generated
	 */
	public static final int ECURSOR = 4;

	/**
	 * The meta object id for the '<em>EMongo Collection</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mongodb.client.MongoCollection
	 * @see org.gecko.emf.mongo.model.ModelPackage#getEMongoCollection()
	 * @generated
	 */
	public static final int EMONGO_COLLECTION = 5;

	/**
	 * The meta object id for the '<em>EIterator</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.Iterator
	 * @see org.gecko.emf.mongo.model.ModelPackage#getEIterator()
	 * @generated
	 */
	public static final int EITERATOR = 6;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iterableEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eMongoCursorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eMongoQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType eDocumentEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType eCursorEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType eMongoCollectionEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType eIteratorEDataType = null;

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
	 * @see org.gecko.emf.mongo.model.ModelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ModelPackage() {
		super(eNS_URI, ModelFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link ModelPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ModelPackage init() {
		if (isInited) return (ModelPackage)EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredModelPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		ModelPackage theModelPackage = registeredModelPackage instanceof ModelPackage ? (ModelPackage)registeredModelPackage : new ModelPackage();

		isInited = true;

		// Initialize simple dependencies
		EcorePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theModelPackage.createPackageContents();

		// Initialize created meta-data
		theModelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theModelPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, theModelPackage);
		return theModelPackage;
	}


	/**
	 * Returns the meta object for class '{@link java.lang.Iterable <em>Iterable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Iterable</em>'.
	 * @see java.lang.Iterable
	 * @model instanceClass="java.lang.Iterable&lt;org.eclipse.emf.ecore.EObject&gt;"
	 * @generated
	 */
	public EClass getIterable() {
		return iterableEClass;
	}

	/**
	 * Returns the meta object for class '{@link org.gecko.emf.mongo.model.EMongoCursor <em>EMongo Cursor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EMongo Cursor</em>'.
	 * @see org.gecko.emf.mongo.model.EMongoCursor
	 * @generated
	 */
	public EClass getEMongoCursor() {
		return eMongoCursorEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link org.gecko.emf.mongo.model.EMongoCursor#getCursor <em>Cursor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cursor</em>'.
	 * @see org.gecko.emf.mongo.model.EMongoCursor#getCursor()
	 * @see #getEMongoCursor()
	 * @generated
	 */
	public EAttribute getEMongoCursor_Cursor() {
		return (EAttribute)eMongoCursorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute '{@link org.gecko.emf.mongo.model.EMongoCursor#getCollection <em>Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Collection</em>'.
	 * @see org.gecko.emf.mongo.model.EMongoCursor#getCollection()
	 * @see #getEMongoCursor()
	 * @generated
	 */
	public EAttribute getEMongoCursor_Collection() {
		return (EAttribute)eMongoCursorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for class '{@link org.gecko.emf.mongo.model.EMongoQuery <em>EMongo Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EMongo Query</em>'.
	 * @see org.gecko.emf.mongo.model.EMongoQuery
	 * @generated
	 */
	public EClass getEMongoQuery() {
		return eMongoQueryEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link org.gecko.emf.mongo.model.EMongoQuery#getFilter <em>Filter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Filter</em>'.
	 * @see org.gecko.emf.mongo.model.EMongoQuery#getFilter()
	 * @see #getEMongoQuery()
	 * @generated
	 */
	public EAttribute getEMongoQuery_Filter() {
		return (EAttribute)eMongoQueryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute '{@link org.gecko.emf.mongo.model.EMongoQuery#getProjection <em>Projection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Projection</em>'.
	 * @see org.gecko.emf.mongo.model.EMongoQuery#getProjection()
	 * @see #getEMongoQuery()
	 * @generated
	 */
	public EAttribute getEMongoQuery_Projection() {
		return (EAttribute)eMongoQueryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for the attribute '{@link org.gecko.emf.mongo.model.EMongoQuery#getProjectionOnly <em>Projection Only</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Projection Only</em>'.
	 * @see org.gecko.emf.mongo.model.EMongoQuery#getProjectionOnly()
	 * @see #getEMongoQuery()
	 * @generated
	 */
	public EAttribute getEMongoQuery_ProjectionOnly() {
		return (EAttribute)eMongoQueryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * Returns the meta object for the attribute '{@link org.gecko.emf.mongo.model.EMongoQuery#getSort <em>Sort</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sort</em>'.
	 * @see org.gecko.emf.mongo.model.EMongoQuery#getSort()
	 * @see #getEMongoQuery()
	 * @generated
	 */
	public EAttribute getEMongoQuery_Sort() {
		return (EAttribute)eMongoQueryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * Returns the meta object for the attribute '{@link org.gecko.emf.mongo.model.EMongoQuery#getSkip <em>Skip</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Skip</em>'.
	 * @see org.gecko.emf.mongo.model.EMongoQuery#getSkip()
	 * @see #getEMongoQuery()
	 * @generated
	 */
	public EAttribute getEMongoQuery_Skip() {
		return (EAttribute)eMongoQueryEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * Returns the meta object for the attribute '{@link org.gecko.emf.mongo.model.EMongoQuery#getLimit <em>Limit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Limit</em>'.
	 * @see org.gecko.emf.mongo.model.EMongoQuery#getLimit()
	 * @see #getEMongoQuery()
	 * @generated
	 */
	public EAttribute getEMongoQuery_Limit() {
		return (EAttribute)eMongoQueryEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * Returns the meta object for the attribute '{@link org.gecko.emf.mongo.model.EMongoQuery#getBatchSize <em>Batch Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Batch Size</em>'.
	 * @see org.gecko.emf.mongo.model.EMongoQuery#getBatchSize()
	 * @see #getEMongoQuery()
	 * @generated
	 */
	public EAttribute getEMongoQuery_BatchSize() {
		return (EAttribute)eMongoQueryEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * Returns the meta object for data type '{@link org.bson.Document <em>EDocument</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>EDocument</em>'.
	 * @see org.bson.Document
	 * @model instanceClass="org.bson.Document"
	 * @generated
	 */
	public EDataType getEDocument() {
		return eDocumentEDataType;
	}

	/**
	 * Returns the meta object for data type '{@link com.mongodb.client.MongoCursor <em>ECursor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>ECursor</em>'.
	 * @see com.mongodb.client.MongoCursor
	 * @model instanceClass="com.mongodb.client.MongoCursor&lt;org.eclipse.emf.ecore.EObject&gt;"
	 * @generated
	 */
	public EDataType getECursor() {
		return eCursorEDataType;
	}

	/**
	 * Returns the meta object for data type '{@link com.mongodb.client.MongoCollection <em>EMongo Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>EMongo Collection</em>'.
	 * @see com.mongodb.client.MongoCollection
	 * @model instanceClass="com.mongodb.client.MongoCollection&lt;org.eclipse.emf.ecore.EObject&gt;"
	 * @generated
	 */
	public EDataType getEMongoCollection() {
		return eMongoCollectionEDataType;
	}

	/**
	 * Returns the meta object for data type '{@link java.util.Iterator <em>EIterator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>EIterator</em>'.
	 * @see java.util.Iterator
	 * @model instanceClass="java.util.Iterator&lt;org.eclipse.emf.ecore.EObject&gt;"
	 * @generated
	 */
	public EDataType getEIterator() {
		return eIteratorEDataType;
	}

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	public ModelFactory getModelFactory() {
		return (ModelFactory)getEFactoryInstance();
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
		iterableEClass = createEClass(ITERABLE);

		eMongoCursorEClass = createEClass(EMONGO_CURSOR);
		createEAttribute(eMongoCursorEClass, EMONGO_CURSOR__CURSOR);
		createEAttribute(eMongoCursorEClass, EMONGO_CURSOR__COLLECTION);

		eMongoQueryEClass = createEClass(EMONGO_QUERY);
		createEAttribute(eMongoQueryEClass, EMONGO_QUERY__FILTER);
		createEAttribute(eMongoQueryEClass, EMONGO_QUERY__PROJECTION);
		createEAttribute(eMongoQueryEClass, EMONGO_QUERY__PROJECTION_ONLY);
		createEAttribute(eMongoQueryEClass, EMONGO_QUERY__SORT);
		createEAttribute(eMongoQueryEClass, EMONGO_QUERY__SKIP);
		createEAttribute(eMongoQueryEClass, EMONGO_QUERY__LIMIT);
		createEAttribute(eMongoQueryEClass, EMONGO_QUERY__BATCH_SIZE);

		// Create data types
		eDocumentEDataType = createEDataType(EDOCUMENT);
		eCursorEDataType = createEDataType(ECURSOR);
		eMongoCollectionEDataType = createEDataType(EMONGO_COLLECTION);
		eIteratorEDataType = createEDataType(EITERATOR);
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
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(iterableEClass, Iterable.class, "Iterable", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS, "java.lang.Iterable<org.eclipse.emf.ecore.EObject>");

		initEClass(eMongoCursorEClass, EMongoCursor.class, "EMongoCursor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEMongoCursor_Cursor(), this.getECursor(), "cursor", null, 0, 1, EMongoCursor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEMongoCursor_Collection(), this.getEMongoCollection(), "collection", null, 0, 1, EMongoCursor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eMongoQueryEClass, EMongoQuery.class, "EMongoQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEMongoQuery_Filter(), this.getEDocument(), "filter", null, 0, 1, EMongoQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEMongoQuery_Projection(), this.getEDocument(), "projection", null, 0, 1, EMongoQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEMongoQuery_ProjectionOnly(), this.getEDocument(), "projectionOnly", null, 0, 1, EMongoQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEMongoQuery_Sort(), this.getEDocument(), "sort", null, 0, 1, EMongoQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEMongoQuery_Skip(), theEcorePackage.getEIntegerObject(), "skip", null, 0, 1, EMongoQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEMongoQuery_Limit(), theEcorePackage.getEIntegerObject(), "limit", null, 0, 1, EMongoQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEMongoQuery_BatchSize(), theEcorePackage.getEIntegerObject(), "batchSize", "1000", 0, 1, EMongoQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(eDocumentEDataType, Document.class, "EDocument", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(eCursorEDataType, MongoCursor.class, "ECursor", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS, "com.mongodb.client.MongoCursor<org.eclipse.emf.ecore.EObject>");
		initEDataType(eMongoCollectionEDataType, MongoCollection.class, "EMongoCollection", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS, "com.mongodb.client.MongoCollection<org.eclipse.emf.ecore.EObject>");
		initEDataType(eIteratorEDataType, Iterator.class, "EIterator", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS, "java.util.Iterator<org.eclipse.emf.ecore.EObject>");

		// Create resource
		createResource(eNS_URI);
	}

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public interface Literals {
		/**
		 * The meta object literal for the '{@link java.lang.Iterable <em>Iterable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Iterable
		 * @see org.gecko.emf.mongo.model.ModelPackage#getIterable()
		 * @generated
		 */
		public static final EClass ITERABLE = eINSTANCE.getIterable();

		/**
		 * The meta object literal for the '{@link org.gecko.emf.mongo.model.EMongoCursor <em>EMongo Cursor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.gecko.emf.mongo.model.EMongoCursor
		 * @see org.gecko.emf.mongo.model.ModelPackage#getEMongoCursor()
		 * @generated
		 */
		public static final EClass EMONGO_CURSOR = eINSTANCE.getEMongoCursor();

		/**
		 * The meta object literal for the '<em><b>Cursor</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute EMONGO_CURSOR__CURSOR = eINSTANCE.getEMongoCursor_Cursor();

		/**
		 * The meta object literal for the '<em><b>Collection</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute EMONGO_CURSOR__COLLECTION = eINSTANCE.getEMongoCursor_Collection();

		/**
		 * The meta object literal for the '{@link org.gecko.emf.mongo.model.EMongoQuery <em>EMongo Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.gecko.emf.mongo.model.EMongoQuery
		 * @see org.gecko.emf.mongo.model.ModelPackage#getEMongoQuery()
		 * @generated
		 */
		public static final EClass EMONGO_QUERY = eINSTANCE.getEMongoQuery();

		/**
		 * The meta object literal for the '<em><b>Filter</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute EMONGO_QUERY__FILTER = eINSTANCE.getEMongoQuery_Filter();

		/**
		 * The meta object literal for the '<em><b>Projection</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute EMONGO_QUERY__PROJECTION = eINSTANCE.getEMongoQuery_Projection();

		/**
		 * The meta object literal for the '<em><b>Projection Only</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute EMONGO_QUERY__PROJECTION_ONLY = eINSTANCE.getEMongoQuery_ProjectionOnly();

		/**
		 * The meta object literal for the '<em><b>Sort</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute EMONGO_QUERY__SORT = eINSTANCE.getEMongoQuery_Sort();

		/**
		 * The meta object literal for the '<em><b>Skip</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute EMONGO_QUERY__SKIP = eINSTANCE.getEMongoQuery_Skip();

		/**
		 * The meta object literal for the '<em><b>Limit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute EMONGO_QUERY__LIMIT = eINSTANCE.getEMongoQuery_Limit();

		/**
		 * The meta object literal for the '<em><b>Batch Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute EMONGO_QUERY__BATCH_SIZE = eINSTANCE.getEMongoQuery_BatchSize();

		/**
		 * The meta object literal for the '<em>EDocument</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.bson.Document
		 * @see org.gecko.emf.mongo.model.ModelPackage#getEDocument()
		 * @generated
		 */
		public static final EDataType EDOCUMENT = eINSTANCE.getEDocument();

		/**
		 * The meta object literal for the '<em>ECursor</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mongodb.client.MongoCursor
		 * @see org.gecko.emf.mongo.model.ModelPackage#getECursor()
		 * @generated
		 */
		public static final EDataType ECURSOR = eINSTANCE.getECursor();

		/**
		 * The meta object literal for the '<em>EMongo Collection</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mongodb.client.MongoCollection
		 * @see org.gecko.emf.mongo.model.ModelPackage#getEMongoCollection()
		 * @generated
		 */
		public static final EDataType EMONGO_COLLECTION = eINSTANCE.getEMongoCollection();

		/**
		 * The meta object literal for the '<em>EIterator</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.Iterator
		 * @see org.gecko.emf.mongo.model.ModelPackage#getEIterator()
		 * @generated
		 */
		public static final EDataType EITERATOR = eINSTANCE.getEIterator();

	}

} //ModelPackage
