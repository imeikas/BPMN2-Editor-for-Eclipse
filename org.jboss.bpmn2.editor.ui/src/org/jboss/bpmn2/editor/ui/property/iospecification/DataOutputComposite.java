package org.jboss.bpmn2.editor.ui.property.iospecification;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.swt.widgets.Composite;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class DataOutputComposite extends NamedElementComposite {

	public DataOutputComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public BaseElement getNewElement() {
		return ModelHandler.FACTORY.createDataOutput();
	}

	@Override
	public String getNewButtonText() {
		return "Add Data Output";
	}

	@Override
	public String getLabelText() {
		return "Data Out";
	}

}
