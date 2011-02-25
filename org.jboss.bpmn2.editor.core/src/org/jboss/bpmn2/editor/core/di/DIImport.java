package org.jboss.bpmn2.editor.core.di;

import java.util.HashMap;
import java.util.List;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dd.dc.Point;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.AreaContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.impl.FreeFormConnectionImpl;
import org.eclipse.graphiti.services.Graphiti;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class DIImport {

	private Diagram diagram;
	private TransactionalEditingDomain domain;
	private ModelHandler modelHandler;
	private IFeatureProvider featureProvider;
	private HashMap<BaseElement, PictogramElement> elements;

	public void generateFromDI() {
		final Object[] diagrams = modelHandler.getAll(BPMNDiagram.class);
		elements = new HashMap<BaseElement, PictogramElement>();
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			@Override
			protected void doExecute() {

				for (Object object : diagrams) {
					List<DiagramElement> ownedElement = ((BPMNDiagram) object).getPlane().getPlaneElement();

					importShapes(ownedElement);
					importConnections(ownedElement);

				}
			}
		});
	}

	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
	}

	public void setDomain(TransactionalEditingDomain editingDomain) {
		this.domain = editingDomain;

	}

	public void setModelHandler(ModelHandler modelHandler) {
		this.modelHandler = modelHandler;
	}

	public void setFeatureProvider(IFeatureProvider featureProvider) {
		this.featureProvider = featureProvider;
	}

	private void importShapes(List<DiagramElement> ownedElement) {
		for (DiagramElement diagramElement : ownedElement) {
			if (diagramElement instanceof BPMNShape) {
				createShape((BPMNShape) diagramElement);
			}
		}
	}

	private void importConnections(List<DiagramElement> ownedElement) {
		for (DiagramElement diagramElement : ownedElement) {
			if (diagramElement instanceof BPMNEdge) {
				importEdge((BPMNEdge) diagramElement);
			}
		}
	}

	private void createShape(BPMNShape shape) {
		BaseElement bpmnElement = shape.getBpmnElement();

		IAddFeature addFeature = featureProvider.getAddFeature(new AddContext(new AreaContext(), bpmnElement));

		if (addFeature == null) {
			Activator.logStatus(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Element not supported: "
					+ bpmnElement.eClass().getName()));
			return;
		}

		AddContext context = new AddContext();
		context.setNewObject(bpmnElement);
		context.setSize((int) shape.getBounds().getWidth(), (int) shape.getBounds().getHeight());

		if (bpmnElement instanceof Lane) {
			handleLane(bpmnElement, context, shape);
		} else if (bpmnElement instanceof FlowNode) {
			handleFlowNode((FlowNode) bpmnElement, context, shape);
		} else {
			context.setTargetContainer(diagram);
			context.setLocation((int) shape.getBounds().getX(), (int) shape.getBounds().getY());
		}

		if (addFeature.canAdd(context)) {
			PictogramElement newContainer = addFeature.add(context);
			elements.put(bpmnElement, newContainer);
			handleEvents(bpmnElement, newContainer);
		}
	}

	private void handleEvents(BaseElement bpmnElement, PictogramElement newContainer) {
		if (bpmnElement instanceof Event) {
			EList<EObject> contents = bpmnElement.eContents();
			for (EObject obj : contents) {

				AddContext context = new AddContext();
				context.setTargetContainer((ContainerShape) newContainer);
				context.setNewObject(obj);

				IAddFeature aFeat = featureProvider.getAddFeature(context);
				if (aFeat != null && aFeat.canAdd(context)) {
					aFeat.add(context);
				}
			}
		}
	}

	private void handleLane(BaseElement bpmnElement, AddContext context, BPMNShape shape) {
		BaseElement parent = (BaseElement) ((Lane) bpmnElement).eContainer().eContainer();
		ContainerShape cont = diagram;
		for (BaseElement be : elements.keySet()) {
			if (be instanceof Participant) {
				Process processRef = ((Participant) be).getProcessRef();
				if (processRef != null && parent.getId().equals(processRef.getId())) {
					cont = (ContainerShape) elements.get(be);
				}
			}
		}
		context.setTargetContainer(cont);
		context.setLocation((int) shape.getBounds().getX(), (int) shape.getBounds().getY());

	}

	private void handleFlowNode(FlowNode node, AddContext context, BPMNShape shape) {
		ContainerShape cont = diagram;
		int x = (int) shape.getBounds().getX();
		int y = (int) shape.getBounds().getY();

		List<Lane> lanes = node.getLanes();
		if (node.eContainer() instanceof SubProcess) {
			ContainerShape containerShape = (ContainerShape) elements.get(node.eContainer());
			if (containerShape != null) {
				cont = containerShape;
				ILocation loc = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(cont);
				x -= loc.getX();
				y -= loc.getX();
			}
		} else if (!lanes.isEmpty()) {
			for (Lane lane : lanes) {
				cont = (ContainerShape) elements.get(lane);
				ILocation loc = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(cont);
				x -= loc.getX();
				y -= loc.getY();
			}
		}
		context.setTargetContainer(cont);
		context.setLocation(x, y);
	}

	private void importEdge(BPMNEdge bpmnEdge) {
		BaseElement source = null;
		BaseElement target = null;

		if (bpmnEdge.getBpmnElement() instanceof MessageFlow) {
			source = (BaseElement) ((MessageFlow) bpmnEdge.getBpmnElement()).getSourceRef();
			target = (BaseElement) ((MessageFlow) bpmnEdge.getBpmnElement()).getTargetRef();
		} else if (bpmnEdge.getBpmnElement() instanceof SequenceFlow) {
			source = ((SequenceFlow) bpmnEdge.getBpmnElement()).getSourceRef();
			target = ((SequenceFlow) bpmnEdge.getBpmnElement()).getTargetRef();
		} else if (bpmnEdge.getBpmnElement() instanceof Association) {
			source = ((Association) bpmnEdge.getBpmnElement()).getSourceRef();
			target = ((Association) bpmnEdge.getBpmnElement()).getTargetRef();
		}

		PictogramElement se = elements.get(source);
		PictogramElement te = elements.get(target);

		if (se != null && te != null) {
			Anchor sa = Graphiti.getPeService().getChopboxAnchor((AnchorContainer) se);
			Anchor ta = Graphiti.getPeService().getChopboxAnchor((AnchorContainer) te);

			AddConnectionContext context = new AddConnectionContext(sa, ta);
			context.setNewObject(bpmnEdge.getBpmnElement());

			IAddFeature addFeature = featureProvider.getAddFeature(context);
			if (addFeature.canAdd(context)) {
				Connection connection = (Connection) addFeature.add(context);
				if (connection instanceof FreeFormConnectionImpl) {
					FreeFormConnectionImpl freeForm = (FreeFormConnectionImpl) connection;
					List<Point> waypoint = bpmnEdge.getWaypoint();

					// FIXME: if we add last waypoint arrows will draw themselves incorrectly
					int size = waypoint.size() - 1;
					for (int i = 0; i < size; i++) {
						Point point = waypoint.get(i);
						org.eclipse.graphiti.mm.algorithms.styles.Point p = Graphiti.getGaService().createPoint(
								(int) point.getX(), (int) point.getY());
						freeForm.getBendpoints().add(p);
					}
				}
			}
		} else {
			Activator.logStatus(new Status(IStatus.WARNING, Activator.PLUGIN_ID,
					"Couldn't find target element, probably not supported! Source: " + source + " Target: " + target));
		}
	}
}
