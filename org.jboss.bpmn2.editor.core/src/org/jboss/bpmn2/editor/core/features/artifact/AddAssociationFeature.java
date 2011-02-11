package org.jboss.bpmn2.editor.core.features.artifact;

import org.eclipse.bpmn2.Association;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class AddAssociationFeature extends AbstractAddShapeFeature {

	public AddAssociationFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    public boolean canAdd(IAddContext context) {
		return context instanceof IAddConnectionContext && context.getNewObject() instanceof Association;
	}

	@Override
    public PictogramElement add(IAddContext context) {
		IAddConnectionContext addConContext = (IAddConnectionContext) context;

		Association association = (Association) context.getNewObject();
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		
		Connection connection = peCreateService.createFreeFormConnection(getDiagram());
		connection.setStart(addConContext.getSourceAnchor());
		connection.setEnd(addConContext.getTargetAnchor());
		
		IGaService gaService = Graphiti.getGaService();
		Polyline line = gaService.createPolyline(connection);
		line.setLineWidth(2);
		line.setLineStyle(LineStyle.DOT);
		line.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		
		if (association.eResource() == null) {
			getDiagram().eResource().getContents().add(association);
		}
		
		link(connection, association);
	    return connection;
    }

}
