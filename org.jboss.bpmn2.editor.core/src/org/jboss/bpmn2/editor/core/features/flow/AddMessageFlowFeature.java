package org.jboss.bpmn2.editor.core.features.flow;

import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

public class AddMessageFlowFeature extends AbstractAddShapeFeature {
	
	private static final int WIDTH = 5;
	private static final int LENGTH = 15;
	private static final IColorConstant CLASS_FOREGROUND = new ColorConstant(146, 146, 208);
	
	public AddMessageFlowFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context instanceof IAddConnectionContext && context.getNewObject() instanceof MessageFlow;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		IAddConnectionContext addConContext = (IAddConnectionContext) context;

		MessageFlow flow = (MessageFlow) context.getNewObject();
		IPeCreateService peCreateService = Graphiti.getPeCreateService();

		Connection connection = peCreateService.createFreeFormConnection(getDiagram());
		connection.setStart(addConContext.getSourceAnchor());
		connection.setEnd(addConContext.getTargetAnchor());

		IGaService gaService = Graphiti.getGaService();
		Polyline polyline = gaService.createPolyline(connection);
		polyline.setLineWidth(1);
		polyline.setLineStyle(LineStyle.DASH);
		polyline.setForeground(manageColor(CLASS_FOREGROUND));

		if (flow.eResource() == null) {
			getDiagram().eResource().getContents().add(flow);
		}

		link(connection, flow);

		ConnectionDecorator endDecorator = peCreateService.createConnectionDecorator(connection, false, 1.0, true);
		createArrow(endDecorator);
		
		ConnectionDecorator startDecorator = peCreateService.createConnectionDecorator(connection, false, 0, true);
		createEllipse(startDecorator);
		
		return connection;
	}
	
	private Polyline createArrow(GraphicsAlgorithmContainer gaContainer) {
		IGaService gaService = Graphiti.getGaService();
		Polyline polyline = gaService.createPolygon(gaContainer, new int[] { -LENGTH, WIDTH, 0, 0, -LENGTH, -WIDTH, -LENGTH, WIDTH });
		polyline.setForeground(manageColor(CLASS_FOREGROUND));
		polyline.setBackground(manageColor(IColorConstant.WHITE));
		polyline.setFilled(true);
		polyline.setLineWidth(1);

		return polyline;
	}
	
	private Ellipse createEllipse(GraphicsAlgorithmContainer gaContainer) {
		IGaService gaService = Graphiti.getGaService();
		Ellipse ellipse = gaService.createEllipse(gaContainer);
		ellipse.setForeground(manageColor(CLASS_FOREGROUND));
		ellipse.setBackground(manageColor(IColorConstant.WHITE));
		ellipse.setFilled(true);
		ellipse.setLineWidth(1);
		gaService.setSize(ellipse, 10, 10);
		return ellipse;
	}
}
