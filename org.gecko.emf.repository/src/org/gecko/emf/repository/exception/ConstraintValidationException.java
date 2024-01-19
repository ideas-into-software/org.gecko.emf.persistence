/**
 * Copyright (c) 2012 - 2019 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.repository.exception;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;

/**
 * This Exception represents a validation error on an {@link EObject}. 
 * This can either be a Model constraint or an invalid non-containment 
 * reference somewhere in an Object Tree  
 * @author Juergen Albert
 * @since 2 Jul 2019
 */
public class ConstraintValidationException extends IllegalStateException {

	
	/** serialVersionUID */
	private static final long serialVersionUID = 3033546214726604596L;
	private static final String VALIDATION_MESSAGE = "%sSource: [%s] Message [%s]";
	private Diagnostic diag;
	
	/**
	 * Creates a new instance.
	 * @param string
	 * @param diag
	 */
	public ConstraintValidationException(String message, Diagnostic diag) {
		super(createDiagnosticMessage(message, diag));
		this.diag = diag;
	}
	
	/**
	 * Returns the diagnostic of the root cause.
	 * @return the {@link Diagnostic}
	 */
	public Diagnostic getDiagnostic() {
		return diag;
	}
	
	private static String createDiagnosticMessage(String initialMessage, Diagnostic diag) {
		StringBuilder message = new StringBuilder(initialMessage + "for " + diag.getMessage());
		diag.getChildren().stream()
		.filter(diagnostic -> diagnostic.getSeverity() != Diagnostic.OK)
		.forEach(diagnostic -> {
			createValidationMessage("", diagnostic, message);
		});
		return message.toString();
	}
	
	private static void createValidationMessage(String intent, Diagnostic diagnostic, StringBuilder message) {
		message.append(String.format(VALIDATION_MESSAGE, intent, diagnostic.getSource(), diagnostic.getMessage()));
		message.append("\r\n");
		diagnostic.getChildren().forEach(d -> createValidationMessage("  " + intent , d, message));
	}

}
