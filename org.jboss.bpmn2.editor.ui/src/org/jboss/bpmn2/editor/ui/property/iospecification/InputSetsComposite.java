package org.jboss.bpmn2.editor.ui.property.iospecification;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.InputSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
		InputSet inputSet = ModelHandler.FACTORY.createInputSet();
		inputSet.setId(EcoreUtil.generateUUID());
		return inputSet;
	}
}
