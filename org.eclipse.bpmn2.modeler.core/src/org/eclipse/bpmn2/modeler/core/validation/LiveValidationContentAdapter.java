package org.eclipse.bpmn2.modeler.core.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.ILiveValidator;
import org.eclipse.emf.validation.service.ModelValidationService;

public class LiveValidationContentAdapter extends EContentAdapter {
	private ILiveValidator validator = null;

	public LiveValidationContentAdapter() {
	}

	public void notifyChanged(final Notification notification) {
		super.notifyChanged(notification);
		
		if (validator == null) {
			validator = (ILiveValidator)ModelValidationService.getInstance().newValidator(EvaluationMode.LIVE);
		}
		
		IStatus status = validator.validate(notification);
		
		if (!status.isOK()) {
			if (status.isMultiStatus()) {
				status = status.getChildren()[0];
			}
		}
	}

}
