package org.jboss.bpmn2.editor.core.features;

import java.util.Map;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.di.DIUtils;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil.AnchorLocation;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil.BoundaryAnchor;
import org.jboss.bpmn2.editor.core.utils.GraphicsUtil;

public class DefaultBPMNResizeFeature extends DefaultResizeShapeFeature {

	public DefaultBPMNResizeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public void resizeShape(IResizeShapeContext context) {
		super.resizeShape(context);
		DIUtils.updateDIShape(getDiagram(), context.getPictogramElement(), BaseElement.class, 0);
		if (context.getPictogramElement() instanceof Shape) {
			Shape shape = (Shape) context.getPictogramElement();
			IGaService gaService = Graphiti.getGaService();

			int w = context.getWidth();
			int h = context.getHeight();

			if (BusinessObjectUtil.containsElementOfType(context.getPictogramElement(), Activity.class)) {
				h -= GraphicsUtil.ACTIVITY_BOTTOM_PADDING;
			}

			Map<AnchorLocation, BoundaryAnchor> anchors = AnchorUtil.getBoundaryAnchors(shape);

			FixPointAnchor anchor = anchors.get(AnchorLocation.TOP).anchor;
			anchor.setLocation(gaService.createPoint(w / 2, 0));

			anchor = anchors.get(AnchorLocation.RIGHT).anchor;
			anchor.setLocation(gaService.createPoint(w, h / 2));

			anchor = anchors.get(AnchorLocation.BOTTOM).anchor;
			anchor.setLocation(gaService.createPoint(w / 2, h));

			anchor = anchors.get(AnchorLocation.LEFT).anchor;
			anchor.setLocation(gaService.createPoint(0, h / 2));

			Object[] node = getAllBusinessObjectsForPictogramElement(context.getShape());
			for (Object object : node) {
				if (object instanceof BPMNShape) {
					BPMNShape s = (BPMNShape) object;
					AnchorUtil.reConnect(s, getDiagram());
				}
			}
		}
	}
}