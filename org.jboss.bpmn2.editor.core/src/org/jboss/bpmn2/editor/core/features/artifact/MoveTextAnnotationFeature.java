package org.jboss.bpmn2.editor.core.features.artifact;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class MoveTextAnnotationFeature extends DefaultMoveShapeFeature {

	private final FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return BusinessObjectUtil.getFirstElementOfType(element, BaseElement.class);
		}
	};

	public MoveTextAnnotationFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = support.isTargetLane(context);
		return intoDiagram || intoLane;
	}
}
