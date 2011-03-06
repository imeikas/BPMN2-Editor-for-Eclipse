package org.jboss.bpmn2.editor.core.features.flow;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.di.DIImport;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;
import org.jboss.bpmn2.editor.utils.AnchorUtil;
import org.jboss.bpmn2.editor.utils.StyleUtil;
import org.jboss.bpmn2.editor.utils.Tuple;

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
		
		Object importProp = context.getProperty(DIImport.IMPORT_PROPERTY);
		if(importProp != null && (Boolean) importProp) {
			connection.setStart(addConContext.getSourceAnchor());
			connection.setEnd(addConContext.getTargetAnchor());
		} else {
			ContainerShape sourceContainer = (ContainerShape) addConContext.getSourceAnchor().eContainer();
			ContainerShape targetContainer = (ContainerShape) addConContext.getTargetAnchor().eContainer();
			Tuple<FixPointAnchor, FixPointAnchor> anchors = AnchorUtil.getSourceAndTargetBoundaryAnchors(sourceContainer,
					targetContainer);
			
			connection.setStart(anchors.getFirst());
			connection.setEnd(anchors.getSecond());
		}

		IGaService gaService = Graphiti.getGaService();
		Polyline connectionLine = gaService.createPolyline(connection);
		connectionLine.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));

		decorateConnectionLine(connectionLine);

		createDIEdge(connection, element);
		link(connection, element);
		createConnectionDecorators(connection);
		hook(addConContext, connection, element);

		return connection;
	}

	protected abstract Class<? extends BaseElement> getBoClass();

	protected void hook(IAddContext context, Connection connection, BaseElement element) {
	}

	protected void decorateConnectionLine(Polyline connectionLine) {
	}

	protected void createConnectionDecorators(Connection connection) {
	}
}