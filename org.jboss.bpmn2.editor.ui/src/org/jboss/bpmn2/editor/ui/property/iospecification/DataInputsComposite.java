package org.jboss.bpmn2.editor.ui.property.iospecification;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.swt.widgets.Composite;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class DataInputsComposite extends NamedElementComposite {

	public DataInputsComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public BaseElement getNewElement() {
		return ModelHandler.FACTORY.createDataInput();
	}

	@Override
	public String getNewButtonText() {
		return "Add Data Input";
	}

	@Override
	public String getLabelText() {
		return "Data Input";
	}

}
