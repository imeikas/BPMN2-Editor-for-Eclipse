package org.eclipse.bpmn2.modeler.core.validation;

import org.eclipse.emf.validation.model.IClientSelector;

public class ValidationDelegateClientSelector implements IClientSelector {
	
	public boolean selects(Object object) {
		return true;
	}
}
