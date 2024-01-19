package org.gecko.emf.repository.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * A mapping building traverser of a collection of {@link EObject#eAllContents content trees};
 * the map is from {@link EObject} to <code>EObject</code>, i.e., from original to copy;
 * use {@link EcoreUtil#copy EcoreUtil.copy} or {@link EcoreUtil#copyAll EcoreUtil.copyAll} to do routine copies.
 * Since this implementation extends a Map implementation, it acts as the result of the over all copy.
 * The client can call {@link #copy copy} and {@link #copyAll copyAll} repeatedly.
 * When all the objects have been copied, 
 * the client should call {@link #copyReferences copyReferences} 
 * to copy the {@link #copyReference appropriate} {@link EObject#eCrossReferences cross references}.
 *<pre>
 *  Copier copier = new Copier();
 *  EObject result = copier.copy(eObject);
 *  Collection results = copier.copyAll(eObjects);
 *  copier.copyReferences();
 *</pre>
 * The copier delegates to {@link #copyContainment copyContainment}, {@link #copyAttribute copyAttribute} during the copy phase
 * and to {@link #copyReference copyReference}, during the cross reference phase.
 * This allows tailored handling through derivation.
 * @author Mark Hoffmann
 * @since 14.03.2012
 */
public class Copier extends EcoreUtil.Copier {

	private static final long serialVersionUID = 1L;
	private final EObject target;
	private final List<EStructuralFeature> omitCopyFeatures;
	
	/**
	 * Constructor with target of the copy
	 * @param target the target to copy the content into
	 */
	public Copier(EObject target) {
		this(target, new ArrayList<EStructuralFeature>(0));
	}

	/**
	 * Constructor with target and a list of features that won't be overwritten
	 * @param target the target to copy the content into
	 * @param omitCopyFeatures the features that will not be overwritten in the target
	 */
	public Copier(EObject target, List<EStructuralFeature> omitCopyFeatures) {
		this.target = target;
		this.omitCopyFeatures = omitCopyFeatures;
	}

	@Override
	protected EObject createCopy(EObject eObject) {
		if (eObject.eClass().equals(target.eClass())) {
			return target;
		}
		return super.createCopy(eObject);
	}

	/**
	 * Returns a copy of the given eObject.
	 * @param eObject the object to copy.
	 * @return the copy.
	 */
	public EObject copy(EObject eObject) {
		if (eObject == null) {
			return null;
		} else {
			EObject copyEObject = createCopy(eObject);
			put(eObject, copyEObject);
			EClass eClass = eObject.eClass();
			for (int i = 0, size = eClass.getFeatureCount(); i < size; ++i) 	{
				EStructuralFeature eStructuralFeature = eClass.getEStructuralFeature(i);
				if (omitCopyFeatures.contains(eStructuralFeature)) {
					continue;
				}
				clearManyFeatures(eStructuralFeature);
				if (eStructuralFeature.isChangeable() && !eStructuralFeature.isDerived()) {
					if (eStructuralFeature instanceof EAttribute) {
						copyAttribute((EAttribute)eStructuralFeature, eObject, copyEObject);
					} else {
						EReference eReference = (EReference)eStructuralFeature;
						if (eReference.isContainment()) {
							copyContainment(eReference, eObject, copyEObject);
						}
					}
				}
			}

			copyProxyURI(eObject, copyEObject);
			return copyEObject;
		}
	}
	
	/**
	 * Clears the target content if it is a many feature
	 * @param feature the {@link EStructuralFeature} to check
	 */
	@SuppressWarnings("unchecked")
	private void clearManyFeatures(final EStructuralFeature feature) {
		if (feature.isMany()) {
			((List<EObject>)target.eGet(feature)).clear();
		}
	}
}