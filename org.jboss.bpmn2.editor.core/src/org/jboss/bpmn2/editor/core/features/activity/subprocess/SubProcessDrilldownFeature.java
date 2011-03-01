package org.jboss.bpmn2.editor.core.features.activity.subprocess;

import java.util.Collection;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.AbstractDrillDownFeature;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

// NOT USED YET
public class SubProcessDrilldownFeature extends AbstractDrillDownFeature {

	public SubProcessDrilldownFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String getName() {
		return "Open sub-process";
	}

	@Override
	public String getDescription() {
		return "Shows sub-process contents";
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		PictogramElement[] elements = context.getPictogramElements();
		if (elements != null && elements.length == 1) {
			Object o = BusinessObjectUtil.getFirstElementOfType(elements[0], BaseElement.class);
			if (o instanceof SubProcess) {
				return super.canExecute(context);
			}
		}
		return false;
	}

	@Override
	protected Collection<Diagram> getDiagrams() {
		return null;
	}
}