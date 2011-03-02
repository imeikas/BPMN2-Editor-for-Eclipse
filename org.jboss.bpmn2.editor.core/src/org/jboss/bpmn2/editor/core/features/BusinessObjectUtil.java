package org.jboss.bpmn2.editor.core.features;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class BusinessObjectUtil {
	public static boolean containsElementOfType(PictogramElement elem, Class clazz) {
		EList<EObject> businessObjs = elem.getLink().getBusinessObjects();
		for (EObject eObject : businessObjs) {
			if (clazz.isInstance(eObject)) {
				return true;
			}
		}
		return false;
	}

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
