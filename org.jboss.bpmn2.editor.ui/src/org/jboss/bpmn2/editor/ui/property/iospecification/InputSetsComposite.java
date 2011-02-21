package org.jboss.bpmn2.editor.ui.property.iospecification;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.swt.widgets.Composite;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class InputSetsComposite extends AbstractSetsComposite {
	public InputSetsComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public String getNewButtonText() {
		return "Add Input Set";
	}

	@Override
	public String getMainLabelText() {
		return "Input Sets";
	}

	@Override
	public String getLabelText() {
		return "Data Inputs";
	}

	@Override
	public String getReferenceName() {
		return "dataInputRefs";
	}

	@Override
	public Class<? extends BaseElement> getElementClass() {
		return DataInput.class;
	}

	@Override
	public BaseElement getNewElement() {
		return ModelHandler.FACTORY.createInputSet();
	}
}
