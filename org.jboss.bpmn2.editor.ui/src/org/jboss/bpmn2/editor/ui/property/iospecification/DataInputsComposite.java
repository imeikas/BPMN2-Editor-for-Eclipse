package org.jboss.bpmn2.editor.ui.property.iospecification;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.widgets.Composite;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class DataInputsComposite extends NamedElementComposite {

	public DataInputsComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public BaseElement getNewElement() {
		DataInput dataInput = ModelHandler.FACTORY.createDataInput();
		dataInput.setId(EcoreUtil.generateUUID());
		return dataInput;
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
