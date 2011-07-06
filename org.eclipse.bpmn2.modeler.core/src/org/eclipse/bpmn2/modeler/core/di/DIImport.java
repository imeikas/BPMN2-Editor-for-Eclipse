/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.di;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.ConversationLink;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.SubChoreography;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dd.dc.Point;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.AreaContext;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.mm.pictograms.impl.FreeFormConnectionImpl;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

@SuppressWarnings("restriction")
public class DIImport {

	public static final String IMPORT_PROPERTY = DIImport.class.getSimpleName().concat(".import");

	private Diagram diagram;
	private TransactionalEditingDomain domain;
	private ModelHandler modelHandler;
	private IFeatureProvider featureProvider;
	private HashMap<BaseElement, PictogramElement> elements;
	private static HashMap<ResourceSet, Hashtable<String, EObject>> ids = new  HashMap<ResourceSet, Hashtable<String, EObject>>();
	private static HashMap<String, Integer> defaultIds = new HashMap<String, Integer>();

	private final IPeService peService = Graphiti.getPeService();
	private final IGaService gaService = Graphiti.getGaService();

	/**
	 * Look for model diagram interchange information and generate all shapes for the diagrams.
	 * 
	 * NB! Currently only first found diagram is generated.
	 */
	public void generateFromDI() {
		final List<BPMNDiagram> diagrams = modelHandler.getAll(BPMNDiagram.class);
		elements = new HashMap<BaseElement, PictogramElement>();
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			@Override
			protected void doExecute() {

				if (diagrams.size() == 0) {
					BPMNPlane plane = BpmnDiFactory.eINSTANCE.createBPMNPlane();
					plane.setBpmnElement(modelHandler.getOrCreateProcess(modelHandler.getInternalParticipant()));

					BPMNDiagram d = BpmnDiFactory.eINSTANCE.createBPMNDiagram();
					d.setPlane(plane);

					modelHandler.getDefinitions().getDiagrams().add(d);
					featureProvider.link(diagram, d);
				}

				for (BPMNDiagram d : diagrams) {
					TreeIterator<EObject> iter = d.eAllContents();
					while (iter.hasNext()) {
						EObject obj = iter.next();
						EStructuralFeature feature = ((EObject)obj).eClass().getEStructuralFeature("id");
						if (feature!=null) {
							Object value = obj.eGet(feature);
							if (value!=null) {
								addID(obj,(String)value);
							}
						}
					}
				}
				
				for (BPMNDiagram d : diagrams) {
					featureProvider.link(diagram, d);
					BPMNPlane plane = d.getPlane();
					if (plane.getBpmnElement() == null) {
						plane.setBpmnElement(modelHandler.getOrCreateProcess(modelHandler.getInternalParticipant()));
					}
					List<DiagramElement> ownedElement = plane.getPlaneElement();

					// FIXME: here we should create a new diagram and an editor page
					importShapes(ownedElement);
					importConnections(ownedElement);

					relayoutLanes(ownedElement);
					// FIXME: we don't really want to leave, but we also don't want all diagrams mixed together
					return;
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
				createEdge((BPMNEdge) diagramElement);
			}
		}
	}

	private void relayoutLanes(List<DiagramElement> ownedElement) {
		for (DiagramElement diagramElement : ownedElement) {
			if (diagramElement instanceof BPMNShape && ((BPMNShape) diagramElement).getBpmnElement() instanceof Lane) {
				BaseElement lane = ((BPMNShape) diagramElement).getBpmnElement();
				ContainerShape shape = (ContainerShape) BusinessObjectUtil.getFirstBaseElementFromDiagram(diagram, lane);
				FeatureSupport.redraw(shape);
			}
		}
	}

	/**
	 * Find a Graphiti feature for given shape and generate necessary diagram elements.
	 * 
	 * @param shape
	 */
	private void createShape(BPMNShape shape) {
		BaseElement bpmnElement = shape.getBpmnElement();
		if (shape.getChoreographyActivityShape() != null) {
			// FIXME: we currently generate participant bands automatically
			return;
		}
		IAddFeature addFeature = featureProvider.getAddFeature(new AddContext(new AreaContext(), bpmnElement));

		if (addFeature == null) {
			Activator.logStatus(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Element not supported: "
					+ bpmnElement.eClass().getName()));
			return;
		}

		AddContext context = new AddContext();
		context.putProperty(IMPORT_PROPERTY, true);
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
			featureProvider.link(newContainer, new Object[] { bpmnElement, shape });
			if (bpmnElement instanceof Participant) {
				elements.put(((Participant) bpmnElement).getProcessRef(), newContainer);
			}
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

		// find the process this lane belongs to
		for (BaseElement be : elements.keySet()) {
			if (be instanceof Participant) {
				Process processRef = ((Participant) be).getProcessRef();
				if (processRef != null && parent.getId().equals(processRef.getId())) {
					cont = (ContainerShape) elements.get(be);
					break;
				}
			} else if (be instanceof Process) {
				if (be.getId().equals(parent.getId())) {
					cont = (ContainerShape) elements.get(be);
					break;
				}
			} else if (be instanceof Lane) {
				if (be.getId().equals(parent.getId())) {
					cont = (ContainerShape) elements.get(be);
					break;
				}
			}
		}

		context.setTargetContainer(cont);
		context.setLocation((int) shape.getBounds().getX(), (int) shape.getBounds().getY());

	}

	private void handleFlowNode(FlowNode node, AddContext context, BPMNShape shape) {
		ContainerShape target = diagram;
		int x = (int) shape.getBounds().getX();
		int y = (int) shape.getBounds().getY();

		// find a correct container element
		List<Lane> lanes = node.getLanes();
		if ((node.eContainer() instanceof SubProcess || (node.eContainer() instanceof Process || node.eContainer() instanceof SubChoreography)
				&& lanes.isEmpty())) {
			ContainerShape containerShape = (ContainerShape) elements.get(node.eContainer());
			if (containerShape != null) {
				target = containerShape;
				ILocation loc = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(target);
				x -= loc.getX();
				y -= loc.getY();
			}
		} else if (!lanes.isEmpty()) {
			for (Lane lane : lanes) {
				target = (ContainerShape) elements.get(lane);
				ILocation loc = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(target);
				x -= loc.getX();
				y -= loc.getY();
			}
		}
		context.setTargetContainer(target);
		context.setLocation(x, y);
	}

	/**
	 * Find a Graphiti feature for given edge and generate necessary connections and bendpoints.
	 * 
	 * @param shape
	 */
	private void createEdge(BPMNEdge bpmnEdge) {
		BaseElement bpmnElement = bpmnEdge.getBpmnElement();
		EObject source = null;
		EObject target = null;
		PictogramElement se = null;
		PictogramElement te = null;

		// for some reason connectors don't have a common interface
		if (bpmnElement instanceof MessageFlow) {
			source = ((MessageFlow) bpmnElement).getSourceRef();
			target = ((MessageFlow) bpmnElement).getTargetRef();
			se = elements.get(source);
			te = elements.get(target);
		} else if (bpmnElement instanceof SequenceFlow) {
			source = ((SequenceFlow) bpmnElement).getSourceRef();
			target = ((SequenceFlow) bpmnElement).getTargetRef();
			se = elements.get(source);
			te = elements.get(target);
		} else if (bpmnElement instanceof Association) {
			source = ((Association) bpmnElement).getSourceRef();
			target = ((Association) bpmnElement).getTargetRef();
			se = elements.get(source);
			te = elements.get(target);
		} else if (bpmnElement instanceof ConversationLink) {
			source = ((ConversationLink) bpmnElement).getSourceRef();
			target = ((ConversationLink) bpmnElement).getTargetRef();
			se = elements.get(source);
			te = elements.get(target);
		} else if (bpmnElement instanceof DataAssociation) {
			// Data Association allows connections for multiple starting points, we don't support it yet
			List<ItemAwareElement> sourceRef = ((DataAssociation) bpmnElement).getSourceRef();
			ItemAwareElement targetRef = ((DataAssociation) bpmnElement).getTargetRef();
			if (sourceRef != null) {
				source = sourceRef.get(0);
			}
			target = targetRef;
			do {
				se = elements.get(source);
				source = source.eContainer();
			} while (se == null && source.eContainer() != null);
			do {
				te = elements.get(target);
				target = target.eContainer();
			} while (te == null && target.eContainer() != null);
		}

		if (source != null && target != null) {
			addSourceAndTargetToEdge(bpmnEdge, source, target);
		}

		if (se != null && te != null) {

			createConnectionAndSetBendpoints(bpmnEdge, se, te);
		} else {
			Activator.logStatus(new Status(IStatus.WARNING, Activator.PLUGIN_ID,
					"Couldn't find target element, probably not supported! Source: " + source + " Target: " + target
							+ " Element: " + bpmnElement));
		}
	}

	private void addSourceAndTargetToEdge(BPMNEdge bpmnEdge, EObject source, EObject target) {
		// We get most of the information from the BpmnEdge, not from the referencing business object. Because of this
		// we must ensure, that the edge contains necessary information.
		if (bpmnEdge.getSourceElement() == null) {
			bpmnEdge.setSourceElement(modelHandler.findDIElement(diagram, (BaseElement) source));
		}
		if (bpmnEdge.getTargetElement() == null) {
			bpmnEdge.setTargetElement(modelHandler.findDIElement(diagram, (BaseElement) target));
		}
	}

	private Connection createConnectionAndSetBendpoints(BPMNEdge bpmnEdge, PictogramElement sourceElement,
			PictogramElement targetElement) {
		FixPointAnchor sourceAnchor = createAnchor(sourceElement);
		FixPointAnchor targetAnchor = createAnchor(targetElement);

		AddConnectionContext context = new AddConnectionContext(sourceAnchor, targetAnchor);
		context.setNewObject(bpmnEdge.getBpmnElement());

		IAddFeature addFeature = featureProvider.getAddFeature(context);
		if (addFeature != null && addFeature.canAdd(context)) {
			context.putProperty(IMPORT_PROPERTY, true);
			Connection connection = (Connection) addFeature.add(context);

			if (connection instanceof FreeFormConnectionImpl) {
				FreeFormConnectionImpl freeForm = (FreeFormConnectionImpl) connection;

				List<Point> waypoint = bpmnEdge.getWaypoint();
				int size = waypoint.size() - 1;

				setAnchorLocation(sourceElement, sourceAnchor, waypoint.get(0));
				setAnchorLocation(targetElement, targetAnchor, waypoint.get(size));

				for (int i = 1; i < size; i++) {
					DIUtils.addBendPoint(freeForm, waypoint.get(i));
				}
			}
			featureProvider.link(connection, new Object[] { bpmnEdge.getBpmnElement(), bpmnEdge });
			return connection;
		} else {
			Activator.logStatus(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Unsupported feature "
					+ ((EObject) context.getNewObject()).eClass().getName()));
		}
		return null;
	}

	private FixPointAnchor createAnchor(PictogramElement elem) {
		FixPointAnchor sa = peService.createFixPointAnchor((AnchorContainer) elem);
		sa.setReferencedGraphicsAlgorithm(elem.getGraphicsAlgorithm());
		Rectangle rect = gaService.createInvisibleRectangle(sa);
		gaService.setSize(rect, 1, 1);
		return sa;
	}

	private void setAnchorLocation(PictogramElement elem, FixPointAnchor anchor, Point point) {
		org.eclipse.graphiti.mm.algorithms.styles.Point p = gaService.createPoint((int) point.getX(),
				(int) point.getY());

		ILocation loc = Graphiti.getPeLayoutService().getLocationRelativeToDiagram((Shape) elem);

		int x = p.getX() - loc.getX();
		int y = p.getY() - loc.getY();

		p.setX(x);
		p.setY(y);

		anchor.setLocation(p);
	}
	

	private static String getObjectName(EObject obj) {
		String name;
		EStructuralFeature feature = ((EObject)obj).eClass().getEStructuralFeature("bpmnElement");
		if (feature!=null) {
			EObject bpmnElement = (EObject) obj.eGet(feature);
			name = obj.eClass().getName() + "_" + bpmnElement.eClass().getName();
		}
		else {
			name = obj.eClass().getName();
		}
		return name;
	}
	
	public static String generateDefaultID(EObject obj) {
		String name = getObjectName(obj);
		Integer value = defaultIds.get(name);
		if (value==null)
			value = Integer.valueOf(1);
		value = Integer.valueOf( value.intValue() + 1 );
		defaultIds.put(name, Integer.valueOf(value));
		
		return "_" + name + "_" + value;
	}
	
	public static String generateID(EObject obj) {
		return generateID(obj,obj.eResource());
	}
	
	public static String generateID(EObject obj, Resource res) {
		ResourceSet resSet = (res==null || res.getResourceSet()==null) ? null : res.getResourceSet();
		if (resSet!=null) {
			Hashtable<String, EObject> tab = ids.get(resSet);
			if (tab==null) {
				tab = new Hashtable<String, EObject>();
				ids.put(resSet, tab);
			}
			
			String name = getObjectName(obj);
			for (int i=1;; ++i) {
				String id = name + "_" + i;
				if (tab.get(id)==null) {
					tab.put(id, obj);
					return id;
				}
			}
		}
		return generateDefaultID(obj);
	}

	private static void addID(EObject obj, String id) {
		Resource res = obj.eResource();
		ResourceSet resSet = res.getResourceSet()==null ? null : res.getResourceSet();
		if (res==null || resSet==null || id.startsWith("_")) {
			String name = getObjectName(obj);
			int newValue = 0;
			try {
				newValue = Integer.parseInt(id.substring(1));
			} catch (Exception e) {
			}
			Integer oldValue = defaultIds.get(name);
			if (oldValue==null || newValue > oldValue.intValue())
				defaultIds.put(name, Integer.valueOf(newValue));
		}
		else {	
			Hashtable<String, EObject> tab = ids.get(resSet);
			if (tab==null) {
				tab = new Hashtable<String, EObject>();
				ids.put(resSet, tab);
			}
			tab.put(id, obj);
		}
	}
	
	public static void setID(EObject obj) {
		setID(obj,obj.eResource());
	}
	
	public static void setID(EObject obj, Resource res) {
		EStructuralFeature feature = ((EObject)obj).eClass().getEStructuralFeature("id");
		if (feature!=null) {
			obj.eSet(feature, generateID(obj,res));
		}
	}
	
	public void clearIDs(Resource res) {
		
	}
}
