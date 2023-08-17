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
package org.gecko.emf.persistence.helper;

import static org.gecko.emf.persistence.PersistenceConstants.EXTENDED_METADATA_NAMESPACE;
import static org.gecko.emf.persistence.PersistenceConstants.EXTENDED_METADATA_NAMESPACE_KEY;
import static org.gecko.emf.persistence.PersistenceConstants.EXTENDED_METADATA_NAME_KEY;
import static org.gecko.emf.persistence.PersistenceConstants.URI_HINT;
import static org.gecko.emf.persistence.PersistenceConstants.URI_HINT_NAME_KEY;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.gecko.emf.persistence.ConstraintValidationException;
import org.gecko.emf.persistence.Options;

/**
 * Some {@link EcoreUtil} extensions
 * @author Mark Hoffmann
 * @since 14.03.2012
 */
public class PersistenceHelper {

	private static Diagnostic OK_INSTANCE = new BasicDiagnostic(Diagnostic.OK, "org.eclipse.emf.common", 0, "OK", null);
	private static Diagnostic ERROR_INSTANCE =  new BasicDiagnostic("", 1, "ERROR", null);
	
	/**
	 * Goes through the whole containment Reference Tree of the given Object 
	 * and checks if set non-containment References are proxies or attached Objects 
	 * 	 
	 * @param eObject the {@link EObject} to check
	 * @throws ConstraintValidationException if any Object has any unattached references
	 */
	public static void checkForAttachedNonContainmentReferences(EObject eObject) throws ConstraintValidationException {

		Diagnostic diag = checkForAttachedNonContainmentRefs(eObject);
		if(diag.getSeverity() != Diagnostic.OK) {
			throw new ConstraintValidationException("EObject " + eObject.toString() + " has invalid Non-Containment References", diag);
		}
	}

	/**
	 * Goes through the whole containment Reference Tree of the all given Objects 
	 * and checks if set non-containment References are proxies or attached Objects 
	 * 	 
	 * @param eObjects the {@link Collection} of {@link EObject} to check
	 * @throws ConstraintValidationException if any Object has any unattached references
	 */
	public static void checkForAttachedNonContainmentReferences(Collection<EObject> eObjects) throws ConstraintValidationException{

		BasicDiagnostic chain = new BasicDiagnostic();

		eObjects.stream()
		.map(PersistenceHelper::checkForAttachedNonContainmentRefs)
		.forEach(chain::add);

		if(chain.getSeverity() != Diagnostic.OK) {
			throw new ConstraintValidationException("EObject(s) have invalid non Containment Referneces", chain);
		}
	}

	private static Diagnostic checkForAttachedNonContainmentRefs(EObject eObject) {
		return checkForAttachedNonContainmentRefs(eObject, null);

	}
	private static Diagnostic checkForAttachedNonContainmentRefs(EObject eObject, EReference reference) {

		if(reference != null && !reference.isContainment()) {
			if(eObject.eIsProxy() || (eObject.eResource() != null && eObject.eResource().getResourceSet() != null)){
				return OK_INSTANCE;
			} else {
				return new BasicDiagnostic(Diagnostic.ERROR, eObject.toString() + " for reference " + reference.getName(), 42, "The Object is no Proxy and is not attached to any Resource/ResourceSet", null);
			}
		}

		EClass eClass = eObject.eClass();
		EList<EReference> references = eClass.getEAllReferences();

		List<Diagnostic> diagnostics = references.stream()
				.filter(eObject::eIsSet)
				.filter(r->!r.isTransient())
				.map(r -> {
					if(r.isMany()) {
						@SuppressWarnings("unchecked")
						List<EObject> eos = (List<EObject>) eObject.eGet(r, false);
						BasicDiagnostic chain = new BasicDiagnostic();
						if(r.isContainment()) {
							eos.stream().map(eo -> checkForAttachedNonContainmentRefs(eo, r)).forEach(d->chain.add(d));
							//					eos.stream().map(eo -> checkForAttachedNonContainmentRefs(eo, r)).forEach(chain::add);//Leads to compile error in gradle
						} else {
							BasicEList<EObject> list = (BasicEList<EObject>) eos;
							for(int i = 0 ; i < list.size(); i++) {
								EObject eo = list.basicGet(i);
								chain.add(checkForAttachedNonContainmentRefs(eo, r));
							}
						}
						return chain;
					} else {
						EObject eo = (EObject) eObject.eGet(r, false);
						if(r.isContainment()) {
							return checkForAttachedNonContainmentRefs(eo, r);
						}
						else if(eo.eIsProxy() || (eo.eResource() != null && eo.eResource().getResourceSet() != null)){
							return OK_INSTANCE;
						} else {
							return new BasicDiagnostic(Diagnostic.ERROR, "Reference " + r.getName(), 42, "The Object is no Proxy and is not attached to any Resource/ResourceSet", null);
						}
					}
				})
				.collect(Collectors.toList());

		return new BasicDiagnostic(eObject.toString(), 42, diagnostics, "Validation Result of EObject", null);
	}

	public static class ReferenceHolder {
		public EReference reference;
		public EObject eObject;
	}

	/**
	 * Returns a self-contained copy of the source that will be copied into
	 * target.
	 * @param <T> the type of the {@link EObject}
	 * @param source the object to copy.
	 * @param target the object to copy the source into.
	 * @return the copy.
	 * @see Copier
	 */
	public static EObject copyInto(EObject source, EObject target) {
		Copier copier = new Copier(target);
		EObject result = copier.copy(source);
		copier.copyReferences();
		return result;
	}

	/**
	 * Returns a self-contained copy of the source that will be copied into
	 * target. The given {@link EStructuralFeature} will not be overwritten
	 * in the target.
	 * @param <T> the type of the {@link EObject}
	 * @param source the object to copy.
	 * @param target the object to copy the source into.
	 * @param featuresToOmit the features that will not be overwritten in the target
	 * @return the copy.
	 * @see Copier
	 */
	public static <T extends EObject> T copyInto(T source, T target, List<EStructuralFeature> featuresToOmit) {
		Copier copier = new Copier(target, featuresToOmit);
		EObject result = copier.copy(source);
		copier.copyReferences();

		@SuppressWarnings("unchecked")T t = (T)result;
		return t;
	}

	/**
	 * Copies the given {@link EObject} and includes all the features named in the includedFeaturePaths {@link List}
	 * @param eObject the {@link EObject} to copy
	 * @param includedFeaturePaths the {@link List} of feature paths to include.
	 * @param <T> the object type
	 * @return the copied object
	 */
	public static <T extends EObject> T copySelectiv(T eObject, List<String> includedFeaturePaths){
		if (includedFeaturePaths == null) {
			throw new IllegalArgumentException("No featurepaths to include");
		}
		return copySelectiv(eObject, includedFeaturePaths, "");
	}

	/**
	 * Copies the given {@link EObject} and includes all the features named in the includedFeaturePaths {@link List}.
	 * The prefix defines the level in the feature path hierarchy and the given {@link EObject} must be of this hierarchy level.
	 *
	 * e.g. a Customer have to be copied and a few of it's references. For the Customer itself the prefix is an empty {@link String}.
	 * If the ContactPerson have to be copied as well, this method is called recursive and the prefex will be "contactPerson.". Thus every feature with this prefix will be called.
	 *
	 * @param eObject the {@link EObject} to copy
	 * @param includedFeaturePaths the {@link List} of feature paths to include.
	 * @param prefix the feature path prefix
	 * @return the copy of the given Object
	 */
	private static <T extends EObject> T copySelectiv(T eObject, List<String> includedFeaturePaths, String prefix){
		return copySelectiv(eObject, includedFeaturePaths, prefix, false);
	}

	/**
	 * Copies the given {@link EObject} and includes all the features named in the includedFeaturePaths {@link List}.
	 * The prefix defines the level in the feature path hierarchy and the given {@link EObject} must be of this hierarchy level.
	 *
	 * e.g. a Customer have to be copied and a few of it's references. For the Customer itself the prefix is an empty {@link String}.
	 * If the ContactPerson have to be copied as well, this method is called recursive and the prefex will be "contactPerson.". Thus every feature with this prefix will be called.
	 *
	 * @param eObject the {@link EObject} to copy
	 * @param includedFeaturePaths the {@link List} of feature paths to include.
	 * @param prefix the feature path prefix
	 * @param isWildCard was there previously a wildcard in the path
	 * @return the copy of the given Object
	 */
	@SuppressWarnings("unchecked")
	private static <T extends EObject> T copySelectiv(T eObject, List<String> includedFeaturePaths, String prefix, boolean isWildCard){
		if(eObject == null)
			return null;
		EClass eClass = eObject.eClass();
		T copy = (T) EcoreUtil.create(eClass);
		boolean wildcard = includedFeaturePaths.contains(prefix + "*") || isWildCard;
		for (EAttribute attribute : eClass.getEAllAttributes())
		{
			if(includedFeaturePaths.contains(prefix + attribute.getName()) || wildcard)
				copy.eSet(attribute, eObject.eGet(attribute, true));
		}
		for (EReference reference : eClass.getEAllReferences()){
			if(shouldCopyReference(prefix, reference, includedFeaturePaths) || wildcard){
				if(reference.isMany()){
					EList<EObject> fromList = (EList<EObject>) eObject.eGet(reference, true);
					EList<EObject> toList = (EList<EObject>) copy.eGet(reference, true);
					for(EObject listObject : fromList){
						toList.add(copySelectiv(listObject, includedFeaturePaths, prefix + reference.getName() + ".", wildcard));
					}
				} else {
					copy.eSet(reference, copySelectiv((EObject) eObject.eGet(reference, true), includedFeaturePaths, prefix + reference.getName() + ".", wildcard));
				}
			}
		}
		return copy;
	}

	/**
	 * Determines if the given reference should be copied
	 * @param prefix the feature prefix
	 * @param reference the reference that might be copied
	 * @param includedFeaturePaths the list of all included features
	 * @return true if the {@link EReference} can be copied
	 */
	private static boolean shouldCopyReference(String prefix,
			EReference reference, List<String> includedFeaturePaths) {
		for(String featurePath : includedFeaturePaths){
			if(featurePath.startsWith(prefix + reference.getName() + "."))
				return true;
		}
		return false;
	}

	/**
	 * Sets ids for the specified {@link EObject} and all its contained {@link EObject}s.
	 * @param rootObject the {@link EObject}
	 * @return the ID assigned to the given {@link EObject} or null if the {@link EObject} has no id feature
	 */
	public static Object setIds(EObject rootObject, Supplier<Object> rootIdsupplier, Supplier<Object> containedIdSupplier) {
		String id = null;
		TreeIterator<EObject> eAllContents = rootObject.eAllContents();
		while (eAllContents.hasNext()) {
			EObject eo = eAllContents.next();
			if (eo.eClass().getEIDAttribute() != null 
					&& EcoreUtil.getID(eo) == null
					&& (
							eo.eClass().getEIDAttribute().getEType().equals(EcorePackage.Literals.ESTRING) 
							|| eo.eClass().getEIDAttribute().getEType().equals(EcorePackage.Literals.EJAVA_OBJECT
									)
							)
					) {
				if(rootObject.eClass().getEIDAttribute() == null) {
					throw new ConstraintValidationException(String.format("Root Object %s from EPackage %s has no ID attribute set. Cannot saved contained object %s.", rootObject.eClass().getName(), rootObject.eClass().getEPackage().getNsURI(), eo.eClass().getName()), ERROR_INSTANCE);
				}
				EcoreUtil.setID(eo, EcoreUtil.convertToString((EDataType) rootObject.eClass().getEIDAttribute().getEType(), containedIdSupplier.get()));
			}
		}
		if (rootObject.eClass().getEIDAttribute() != null 
				&& EcoreUtil.getID(rootObject) == null 
				&& (
						rootObject.eClass().getEIDAttribute().getEType().equals(EcorePackage.Literals.ESTRING) 
						|| rootObject.eClass().getEIDAttribute().getEType().equals(EcorePackage.Literals.EJAVA_OBJECT
								)
						)
				) {
			id = EcoreUtil.convertToString((EDataType) rootObject.eClass().getEIDAttribute().getEType(), rootIdsupplier.get());
			EcoreUtil.setID(rootObject, id);
		}
		return id;
	}
	/**
	 * Sets ids for the specified {@link EObject} and all its contained {@link EObject}s.
	 * @param eObject the {@link EObject}
	 * @return the ID assigned to the given {@link EObject} or null if the {@link EObject} has no id feature
	 */
	public static Object setIds(EObject eObject) {
		return setIds(eObject, () -> UUID.randomUUID().toString(), () -> UUID.randomUUID().toString()); 
	}

	/**
	 * Retrieves the name of the {@link EClass} that should be used as eclass segment in the {@link URI}.
	 *
	 * By default this is the {@link EClass#getName()}. If anywhere in the {@link EClass} or on off its super classes the annotation <code>UriHint</code> is found, a different name will be chosen.
	 * At first a possibly named reference will be used. This can reference to any {@link EClass}, even if it is not a super type of the given {@link EClass}. If found it will call {@link PersistenceHelper#getUriHintNameForEClass(EClass)} with the found reference.
	 * So the name returned String will be at least name of the referenced {@link EClass}.
	 * If such a reference is not present a DetailEntry with the key <code>name</code> will be taken under consideration.
	 *
	 * The first annotation found on the class hierarchy will define the hint.
	 * @param eClass
	 * @return
	 */
	public static String getUriNameForEClass(EClass eClass){
		return getEClassUriHint(eClass);
//		String name = getCollectionNameFromEClassAnnotation(eClass);
//		if(name == null){
//			name = getNameForElement(eClass);
//		}
//		return name;
	}

//	/**
//	 * Grabs the hint from the given class if an annotation is present at the given {@link EClass} or one of its super types
//	 * @param eClass the {@link EClass} to look in
//	 * @return the hint or <code>null</code> if no annotation is present.
//	 */
//	private static String getCollectionNameFromEClassAnnotation(EClass eClass){
//		String name = null;
//		String hint = EcoreUtil.getAnnotation(eClass, PersistenceConstants.URI_HINT, "name");
//
//		EAnnotation annotation = eClass.getEAnnotation("UriHint");
//		if(annotation != null){
//			annotation.getReferences().stream().findFirst().map(EClass.class::cast).map(null)
//			if(annotation.getReferences().size() > 0){
//				EClass refEClass = (EClass) annotation.getReferences().get(0);
//				name = getUriHintNameForEClass(refEClass);
//			} else {
//				if(annotation.getDetails().size() > 0){
//					name = annotation.getDetails().get("name");
//				}
//			}
//		}
//		if(name == null){
//			List<EClass> superTypes = eClass.getESuperTypes();
//			for(EClass superEClass : superTypes){
//				name = getCollectionNameFromEClassAnnotation(superEClass);
//				if(name != null){
//					if(superTypes.size() > 1){
//						for(int i = superTypes.indexOf(superEClass) + 1 ; i > superTypes.size(); i++){
//							String conflict = getCollectionNameFromEClassAnnotation(superTypes.get(i));
//							if(conflict != null){
//								throw new RuntimeException(String.format("At least two colliding Collection names are defined for the two super types ( %s, %s ) of %s with the names %s and %s",  superEClass.getName(), superTypes.get(i).getName(), eClass.getName(), name, conflict));
//							}
//						}
//					}
//					break;
//				}
//			}
//		}
//		return name;
//	}
	
	/**
	 * Returns the name of the {@link ENamedElement} or the {@link ExtendedMetaData} name key if the 
	 * name space equals "emf.persistence"
	 * @param element the {@link ENamedElement}
	 * @return the name or {@link ExtendedMetaData} value for the key 'name'
	 */
	public static String getElementNameLower(ENamedElement element) {
		assert(element != null);
		String name = getElementEMDName(element, EXTENDED_METADATA_NAMESPACE);
		return name == null ? element.getName().toLowerCase(): name;
	}

	/**
	 * Returns the name of the {@link ExtendedMetaData} name key if the 
	 * name space equals "emf.persistence" of an {@link ENamedElement}
	 * @param element the {@link ENamedElement}
	 * @return the name or {@link ExtendedMetaData} value for the key 'name'
	 */
	public static String getElementEMDName(ENamedElement element) {
		return getElementEMDName(element, EXTENDED_METADATA_NAMESPACE);
	}

	/**
	 * Returns the name - details entry for {@link ExtendedMetaData} with the give namespaceValue of a {@link ENamedElement}.
	 * @param element the {@link ENamedElement}
	 * @param namespaceValue the namespace value
	 * @return the name value or <code>null</code>
	 */
	public static String getElementEMDName(ENamedElement element, String namespaceValue) {
		assert(element != null);
		assert(namespaceValue != null);
		String annotation = EcoreUtil.getAnnotation(element, ExtendedMetaData.ANNOTATION_URI, EXTENDED_METADATA_NAMESPACE_KEY);
		String name = EcoreUtil.getAnnotation(element, ExtendedMetaData.ANNOTATION_URI, EXTENDED_METADATA_NAME_KEY);
		return (namespaceValue.equals(annotation) && name != null) ? name : null;
	}

	/**
	 * Returns all name - details entries for {@link ExtendedMetaData} with the give namespace.
	 * It parses the whole EClass super type hierarchy. The first element in the list is the base EClass
	 * @param eclass the EClass to check for {@link ExtendedMetaData}
	 * @return names list or empty list
	 */
	public static List<String> getAllEClassEMDName(EClass eclass) {
		return getAllEClassEMDName(eclass, EXTENDED_METADATA_NAMESPACE);
	}

	/**
	 * Returns all name - details entries for {@link ExtendedMetaData} with the give namespace.
	 * It parses the whole EClass super type hierarchy. The first element in the list is the base EClass
	 * @param eclass the EClass to check for {@link ExtendedMetaData}
	 * @param namespace the namespace
	 * @return names list or empty list
	 */
	public static List<String> getAllEClassEMDName(EClass eclass, String namespace) {
		assert(eclass != null);
		assert(namespace != null);
		String name = getElementEMDName(eclass, namespace);
		List<String> names = new LinkedList<String>();
		if (name != null) {
			names.add(name);
		}
		List<String> superNames = eclass.getESuperTypes().
				stream().
				map(sec->getElementEMDName(sec, namespace)).
				filter(Objects::nonNull).
				collect(Collectors.toList());
		if (!superNames.isEmpty()) {
			names.addAll(superNames);
		}
		return names;
	}

	/**
	 * Returns the annotation value for the EClass super type hierarchy 
	 * @param eclass the EClass. Must not be <code>null</code>
	 * @param source the annotation source. Must not be <code>null</code>
	 * @param detailsKey the annotation details key. Must not be <code>null</code>
	 * @return
	 */
	public static final List<String> getAllEClassAnnotationValue(EClass eclass, String source, String detailsKey) {
		assert(eclass != null);
		assert(source != null);
		assert(detailsKey != null);
		String value = EcoreUtil.getAnnotation(eclass, source, detailsKey);
		List<String> values = new LinkedList<String>(); 
		if (value != null) {
			values.add(value);
		}
		List<String> superValues = eclass.
				getESuperTypes().
				stream().
				map(sec->EcoreUtil.getAnnotation(sec, source, detailsKey)).
				collect(Collectors.toList());
		if (!superValues.isEmpty()) {
			values.addAll(superValues);
		}
		return values;
	}

	/**
	 * Returns the uri-hints annotation values for the {@link EClass}
	 * @param eclass the {@link EClass}
	 * @return the uri-hints
	 */
	public static List<String> getAllEClassUriHint(EClass eclass) {
		return getAllEClassAnnotationValue(eclass, URI_HINT, URI_HINT_NAME_KEY);
	}
	
	/**
	 * Retrieves the name of the {@link EClass} that should be used as eclass segment in the {@link URI}.
	 *
	 * By default this is the {@link EClass#getName()}. If anywhere in the {@link EClass} or on off its super classes the annotation <code>UriHint</code> is found, a different name will be chosen.
	 * At first a possibly named reference will be used. This can reference to any {@link EClass}, even if it is not a super type of the given {@link EClass}. If found it will call {@link PersistenceHelper#getUriHintNameForEClass(EClass)} with the found reference.
	 * So the name returned String will be at least name of the referenced {@link EClass}.
	 * If such a reference is not present a DetailEntry with the key <code>name</code> will be taken under consideration.
	 *
	 * The first annotation found on the class hierarchy will define the hint.
	 * @param eClass
	 * @return
	 */
	public static String getEClassUriHint(EClass eClass){
		List<String> names = getAllEClassUriHint(eClass);
		switch (names.size()) {
		case 0:	
			return eClass.getName();
		case 1:	
			return names.get(0);
		default:
			throw new RuntimeException(String.format("At least two colliding uri-hints are defined for the EClass '%s'and the super type hierarchy: '%s'", eClass.getName(), String.join(",", names)));
		}
	}

	public static final URI createPersistenceUri(String baseUri, EClass eclass) {
		URI uri = URI.createURI(baseUri);
		String name = getAllEClassEMDName(eclass).stream().findFirst().orElse(eclass.getName());
		if (name != null) {
			uri = uri.appendSegment(name);
		}
		return uri;
	}
	
	public static final Map<String, Object> createLoadSaveOptions(EClass eclass, Map<String, Object> options) {
		if (eclass == null) {
			return options;
		}
		if (options == null) {
			options = new HashMap<>();
		}
		options.put(Options.OPTION_ECLASS_HINT, eclass);
		options.put(Options.OPTION_ECLASS_URI_HINT, EcoreUtil.getURI(eclass).toString());
		return options;
	}
	
	public static Class<?> getInstanceClassOrDefault(EClass eClass) {
		return eClass.getInstanceClass() != null ? eClass.getInstanceClass() : EObject.class;
	}
	
	public static EMFPersistenceContext createPersistenceContext(String baseUri, EClass eclass, Map<String, Object> options) {
		assert(eclass != null);
		assert(baseUri != null);
		return new EMFPersistenceContext(baseUri, eclass, options);
	}
	
	public static class EMFPersistenceContext {
		private final EClass eclass;
		private final Map<String, Object> options;
		private final URI uri;
		
		private EMFPersistenceContext(String baseUri, EClass eclass, Map<String, Object> options) {
			this.eclass = eclass;
			this.uri = createPersistenceUri(baseUri, eclass);
			this.options = createLoadSaveOptions(eclass, options); 
		}
		
		/**
		 * Returns the eclass.
		 * @return the eclass
		 */
		public EClass getEclass() {
			return eclass;
		}
		
		/**
		 * Returns the options.
		 * @return the options
		 */
		public Map<String, Object> getOptions() {
			return options;
		}
		
		/**
		 * Returns the uri.
		 * @return the uri
		 */
		public URI getUri() {
			return uri;
		}
	}
	
	

}
