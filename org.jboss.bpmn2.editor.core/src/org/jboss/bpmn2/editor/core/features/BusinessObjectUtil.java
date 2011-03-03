package org.jboss.bpmn2.editor.core.features;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class BusinessObjectUtil {
	
	@SuppressWarnings("rawtypes")
	public static boolean containsElementOfType(PictogramElement elem, Class clazz) {
		if(elem.getLink() == null) {
			return false;
		}
		EList<EObject> businessObjs = elem.getLink().getBusinessObjects();
		for (EObject eObject : businessObjs) {
			if (clazz.isInstance(eObject)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
    public static EObject getFirstElementOfType(PictogramElement elem, Class clazz) {
		if (elem.getLink() == null) {
			return null;
		}
		EList<EObject> businessObjs = elem.getLink().getBusinessObjects();
		for (EObject eObject : businessObjs) {
			if (clazz.isInstance(eObject)) {
				return eObject;
			}
		}
		return null;
	}
}
