package org.jboss.bpmn2.editor.core.features;

import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil;

public class DefaultBpmnMoveFeature extends DefaultMoveShapeFeature {

	public DefaultBpmnMoveFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		Object[] node = getAllBusinessObjectsForPictogramElement(context.getShape());
		for (Object object : node) {
			if (object instanceof BPMNShape) {
				BPMNShape shape = (BPMNShape) object;
				Bounds bounds = shape.getBounds();
				bounds.setX(context.getX());
				bounds.setY(context.getY());
				AnchorUtil.reConnect(shape, getDiagram());
			}
		}
	}
}