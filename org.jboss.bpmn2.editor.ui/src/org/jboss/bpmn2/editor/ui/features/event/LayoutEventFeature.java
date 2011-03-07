package org.jboss.bpmn2.editor.ui.features.event;

import static org.jboss.bpmn2.editor.core.features.event.AddEventFeature.EVENT_CIRCLE;
import static org.jboss.bpmn2.editor.core.features.event.AddEventFeature.EVENT_ELEMENT;
import static org.jboss.bpmn2.editor.core.features.event.AddEventFeature.EVENT_TEXT;
import static org.jboss.bpmn2.editor.core.utils.FeatureSupport.getShape;

import org.eclipse.bpmn2.Event;
import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.utils.GraphicsUtil;

public class LayoutEventFeature extends AbstractLayoutFeature {

	private static IGaService gaService = Graphiti.getGaService();

	public LayoutEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		PictogramElement pictoElem = context.getPictogramElement();
		if (!(pictoElem instanceof ContainerShape)) {
			return false;
		}
		return BusinessObjectUtil.containsElementOfType(pictoElem, Event.class);
	}

	@Override
	public boolean layout(ILayoutContext context) {
		ContainerShape container = (ContainerShape) context.getPictogramElement();

		Shape textShape = getShape(container, EVENT_ELEMENT, EVENT_TEXT);
		Text textGa = (Text) textShape.getGraphicsAlgorithm();
		IDimension size = GraphitiUi.getUiLayoutService().calculateTextSize(textGa.getValue(), textGa.getFont());
		
		GraphicsAlgorithm parentGa = container.getGraphicsAlgorithm();
		gaService.setSize(parentGa, size.getWidth(), parentGa.getHeight());

		gaService.setSize(textGa, size.getWidth(), size.getHeight());
		
		int s = GraphicsUtil.EVENT_SIZE;
		Shape circleShape = getShape(container, EVENT_ELEMENT, EVENT_CIRCLE);
		GraphicsAlgorithm circle = circleShape.getGraphicsAlgorithm();
		gaService.setLocationAndSize(circle, (size.getWidth() / 2) - (s / 2), 0, s, s);

		return true;
	}
}