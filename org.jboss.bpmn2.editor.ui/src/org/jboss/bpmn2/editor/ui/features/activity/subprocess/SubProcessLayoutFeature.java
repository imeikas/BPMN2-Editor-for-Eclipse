package org.jboss.bpmn2.editor.ui.features.activity.subprocess;

import org.eclipse.bpmn2.Activity;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.jboss.bpmn2.editor.core.features.activity.ActivityLayoutFeature;

public class SubProcessLayoutFeature extends ActivityLayoutFeature {

	public SubProcessLayoutFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected boolean layoutHook(Shape shape, GraphicsAlgorithm ga, Object bo, int newWidth, int newHeight) {
		if (bo != null && bo instanceof Activity && ga instanceof Text) {
			Graphiti.getGaService().setLocationAndSize(ga, 5, 5, newWidth - 10, 15);
			return true;
		}
		return false;
	}
}
