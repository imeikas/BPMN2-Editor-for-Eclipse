package org.jboss.bpmn2.editor.core.features.flow;

import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;

public class AddSequenceFlowFeature extends AbstractBpmnAddFeature {
	private static final int WIDTH = 3;
	private static final int LENGTH = 8;
	private static final IColorConstant CLASS_FOREGROUND = new ColorConstant(146, 146, 208);

	public AddSequenceFlowFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context instanceof IAddConnectionContext && context.getNewObject() instanceof SequenceFlow;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		IAddConnectionContext addConContext = (IAddConnectionContext) context;

		SequenceFlow flow = (SequenceFlow) context.getNewObject();
		IPeCreateService peCreateService = Graphiti.getPeCreateService();

		// CONNECTION WITH POLYLINE
		Connection connection = peCreateService.createFreeFormConnection(getDiagram());
		connection.setStart(addConContext.getSourceAnchor());
		connection.setEnd(addConContext.getTargetAnchor());

		IGaService gaService = Graphiti.getGaService();
		Polyline polyline = gaService.createPolyline(connection);
		polyline.setLineWidth(1);
		polyline.setForeground(manageColor(CLASS_FOREGROUND));

		if (flow.eResource() == null) {
			getDiagram().eResource().getContents().add(flow);
		}
		// create link and wire it
		createDIEdge(connection, flow);

		ConnectionDecorator decorator = peCreateService.createConnectionDecorator(connection, false, 1.0, true);
		createArrow(decorator);
		return connection;
	}

	private Polyline createArrow(GraphicsAlgorithmContainer gaContainer) {
		IGaService gaService = Graphiti.getGaService();
		Polyline polyline = gaService.createPolygon(gaContainer, new int[] { -LENGTH, WIDTH, 0, 0, -LENGTH, -WIDTH,
				-LENGTH, WIDTH });

		polyline.setForeground(manageColor(CLASS_FOREGROUND));
		polyline.setBackground(manageColor(CLASS_FOREGROUND));
		polyline.setFilled(true);
		polyline.setLineWidth(2);

		return polyline;
	}
}
