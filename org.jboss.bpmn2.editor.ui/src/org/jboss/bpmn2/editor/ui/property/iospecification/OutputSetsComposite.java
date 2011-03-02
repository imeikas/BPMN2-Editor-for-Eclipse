package org.jboss.bpmn2.editor.ui.property.iospecification;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.bpmn2.OutputSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.widgets.Composite;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class OutputSetsComposite extends AbstractSetsComposite {

	public OutputSetsComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public String getNewButtonText() {
		return "Add Output Set";
	}

	@Override
	public String getMainLabelText() {
		return "Output Sets";
	}

	@Override
	public String getLabelText() {
		return "Data Outs";
	}

	@Override
	public String getReferenceName() {
		return "dataOutputRefs";
	}

	@Override
	public Class<? extends BaseElement> getElementClass() {
		return DataOutput.class;
	}

	@Override
	public BaseElement getNewElement() {
		OutputSet outputSet = ModelHandler.FACTORY.createOutputSet();
		outputSet.setId(EcoreUtil.generateUUID());
		return outputSet;
	}
}
