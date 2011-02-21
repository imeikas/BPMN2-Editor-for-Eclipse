package org.jboss.bpmn2.editor.ui.property.iospecification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.widgets.Composite;
import org.jboss.bpmn2.editor.ui.property.AbstractBpmn2PropertiesComposite;

public class NameDetailsComposite extends AbstractBpmn2PropertiesComposite {

	public NameDetailsComposite(Composite composite, int none) {
		super(composite, none);
	}

	@Override
	public void createBindings() {
		EList<EAttribute> eAllAttributes = be.eClass().getEAllAttributes();
		for (EAttribute a : eAllAttributes) {
			if ("name".equals(a.getName())) {
				bind(a, createTextInput("Name"));
			}
		}
	}

}