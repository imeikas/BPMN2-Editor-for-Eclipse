package org.jboss.bpmn2.editor.core.features.flow;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public abstract class AbstractAddFlowFeature extends AbstractBpmnAddFeature {

	public AbstractAddFlowFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context instanceof IAddConnectionContext
		        && getBoClass().isAssignableFrom(context.getNewObject().getClass());
	}

	@Override
	public PictogramElement add(IAddContext context) {
		IPeService peService = Graphiti.getPeService();
		
		BaseElement element = (BaseElement) context.getNewObject();
		IAddConnectionContext addConContext = (IAddConnectionContext) context;
		
		Connection connection = peService.createFreeFormConnection(getDiagram());
		connection.setStart(addConContext.getSourceAnchor());
		connection.setEnd(addConContext.getTargetAnchor());
		
		IGaService gaService = Graphiti.getGaService();
		Polyline connectionLine = gaService.createPolyline(connection);
		connectionLine.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		decorateConnectionLine(connectionLine);
		
		if (element.eResource() == null) {
			getDiagram().eResource().getContents().add(element);
		}
		
		link(connection, element);
		
		createConnectionDecorators(connection);
		return connection;
	}

	abstract Class<? extends BaseElement> getBoClass();
	
	void decorateConnectionLine(Polyline connectionLine) {
	}

	void createConnectionDecorators(Connection connection) {
	}
}