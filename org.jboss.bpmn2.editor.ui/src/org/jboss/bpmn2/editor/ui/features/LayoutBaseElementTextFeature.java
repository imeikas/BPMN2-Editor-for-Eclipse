package org.jboss.bpmn2.editor.ui.features;

import static org.jboss.bpmn2.editor.core.utils.FeatureSupport.getShape;

import org.eclipse.bpmn2.BaseElement;
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
import org.jboss.bpmn2.editor.core.features.UpdateBaseElementNameFeature;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public abstract class LayoutBaseElementTextFeature extends AbstractLayoutFeature {

	private static IGaService gaService = Graphiti.getGaService();

	public LayoutBaseElementTextFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		PictogramElement pictoElem = context.getPictogramElement();
		if (!(pictoElem instanceof ContainerShape)) {
			return false;
		}
		return BusinessObjectUtil.containsElementOfType(pictoElem, BaseElement.class);
	}

	@Override
	public boolean layout(ILayoutContext context) {
		ContainerShape container = (ContainerShape) context.getPictogramElement();

		Shape textShape = getShape(container, UpdateBaseElementNameFeature.TEXT_ELEMENT, Boolean.toString(true));
		Text textGa = (Text) textShape.getGraphicsAlgorithm();
		IDimension size = GraphitiUi.getUiLayoutService().calculateTextSize(textGa.getValue(), textGa.getFont());
		
		GraphicsAlgorithm parentGa = container.getGraphicsAlgorithm();
		
		if(size.getWidth() > getMinimumWidth()) {
			gaService.setSize(parentGa, size.getWidth(), parentGa.getHeight());
		}
		
		gaService.setSize(textGa, size.getWidth(), size.getHeight());
		
		return true;
	}
	
	public abstract int getMinimumWidth();
}