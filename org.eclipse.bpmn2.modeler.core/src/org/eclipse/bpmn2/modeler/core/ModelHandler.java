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
package org.eclipse.bpmn2.modeler.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Artifact;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.ConversationLink;
import org.eclipse.bpmn2.ConversationNode;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class ModelHandler {
	public static final Bpmn2Factory FACTORY = Bpmn2Factory.eINSTANCE;

	Bpmn2ResourceImpl resource;

	ModelHandler() {
	}

	void createDefinitionsIfMissing() {
		EList<EObject> contents = resource.getContents();

		if (contents.isEmpty() || !(contents.get(0) instanceof DocumentRoot)) {
			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(resource);

			if (domain != null) {
				final DocumentRoot docRoot = FACTORY.createDocumentRoot();
				final Definitions definitions = FACTORY.createDefinitions();
//				definitions.setId(EcoreUtil.generateUUID());
				ModelUtil.setID(definitions,resource);
				Collaboration collaboration = FACTORY.createCollaboration();
//				collaboration.setId(EcoreUtil.generateUUID());
				ModelUtil.setID(collaboration,resource);
				Participant participant = FACTORY.createParticipant();
//				participant.setId(EcoreUtil.generateUUID());
				ModelUtil.setID(participant,resource);
				participant.setName("Internal");
				collaboration.getParticipants().add(participant);
				definitions.getRootElements().add(collaboration);

				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						docRoot.setDefinitions(definitions);
						resource.getContents().add(docRoot);
					}
				});
				return;
			}
		}
	}

	public static ModelHandler getInstance(Diagram diagram) throws IOException {
		return ModelHandlerLocator.getModelHandler(diagram.eResource());
	}

	/**
	 * @param <T>
	 * @param target
	 *            object that this element is being added to
	 * @param elem
	 *            flow element to be added
	 * @return
	 */
	public <T extends FlowElement> T addFlowElement(Object target, T elem) {
		FlowElementsContainer container = getFlowElementContainer(target);
		container.getFlowElements().add(elem);
		return elem;
	}

	/**
	 * @param <A>
	 * @param target
	 *            object that this artifact is being added to
	 * @param artifact
	 *            artifact to be added
	 * @return
	 */
	public <T extends Artifact> T addArtifact(Object target, T artifact) {
		Process process = getOrCreateProcess(getParticipant(target));
		process.getArtifacts().add(artifact);
		return artifact;
	}

	public <T extends RootElement> T addRootElement(T element) {
		getDefinitions().getRootElements().add(element);
		return element;
	}

	public DataOutput addDataOutput(Object target, DataOutput dataOutput) {
		getOrCreateIOSpecification(target).getDataOutputs().add(dataOutput);
		return dataOutput;
	}

	public DataInput addDataInput(Object target, DataInput dataInput) {
		getOrCreateIOSpecification(target).getDataInputs().add(dataInput);
		return dataInput;
	}

	public ConversationNode addConversationNode(ConversationNode conversationNode) {
		getOrCreateCollaboration().getConversations().add(conversationNode);
		return conversationNode;
	}

	private InputOutputSpecification getOrCreateIOSpecification(Object target) {
		Process process = getOrCreateProcess(getParticipant(target));
		if (process.getIoSpecification() == null) {
			InputOutputSpecification ioSpec = FACTORY.createInputOutputSpecification();
//			ioSpec.setId(EcoreUtil.generateUUID());
			ModelUtil.setID(ioSpec,resource);
			process.setIoSpecification(ioSpec);
		}
		return process.getIoSpecification();
	}

	public void moveFlowNode(FlowNode node, Object source, Object target) {
		FlowElementsContainer sourceContainer = getFlowElementContainer(source);
		FlowElementsContainer targetContainer = getFlowElementContainer(target);
		sourceContainer.getFlowElements().remove(node);
		targetContainer.getFlowElements().add(node);
		for (SequenceFlow flow : node.getOutgoing()) {
			sourceContainer.getFlowElements().remove(flow);
			targetContainer.getFlowElements().add(flow);
		}
	}

	public Participant addParticipant() {
		Collaboration collaboration = getOrCreateCollaboration();
		Participant participant = FACTORY.createParticipant();
//		participant.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(participant,resource);
		collaboration.getParticipants().add(participant);
		return participant;
	}

	@Deprecated
	public void moveLane(Lane movedLane, Participant targetParticipant) {
		Participant sourceParticipant = getParticipant(movedLane);
		moveLane(movedLane, sourceParticipant, targetParticipant);
	}

	public void moveLane(Lane movedLane, Participant sourceParticipant, Participant targetParticipant) {
		Process sourceProcess = getOrCreateProcess(sourceParticipant);
		Process targetProcess = getOrCreateProcess(targetParticipant);
		for (FlowNode node : movedLane.getFlowNodeRefs()) {
			moveFlowNode(node, sourceProcess, targetProcess);
		}
		if (movedLane.getChildLaneSet() != null && !movedLane.getChildLaneSet().getLanes().isEmpty()) {
			for (Lane lane : movedLane.getChildLaneSet().getLanes()) {
				moveLane(lane, sourceParticipant, targetParticipant);
			}
		}
	}

	public Process getOrCreateProcess(Participant participant) {
		if (participant.getProcessRef() == null) {
			Process process = FACTORY.createProcess();
//			process.setId(EcoreUtil.generateUUID());
			ModelUtil.setID(process,resource);
			process.setName("Process for " + participant.getName());
			getDefinitions().getRootElements().add(process);
			participant.setProcessRef(process);
		}
		return participant.getProcessRef();
	}

	public Lane createLane(Lane targetLane) {
		Lane lane = FACTORY.createLane();
//		lane.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(lane,resource);

		if (targetLane.getChildLaneSet() == null) {
			targetLane.setChildLaneSet(ModelHandler.FACTORY.createLaneSet());
		}

		LaneSet targetLaneSet = targetLane.getChildLaneSet();
		targetLaneSet.getLanes().add(lane);

		lane.getFlowNodeRefs().addAll(targetLane.getFlowNodeRefs());
		targetLane.getFlowNodeRefs().clear();

		return lane;
	}

	public Lane createLane(Object target) {
		Lane lane = FACTORY.createLane();
//		lane.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(lane,resource);
		FlowElementsContainer container = getFlowElementContainer(target);
		if (container.getLaneSets().isEmpty()) {
			LaneSet laneSet = FACTORY.createLaneSet();
//			laneSet.setId(EcoreUtil.generateUUID());
			container.getLaneSets().add(laneSet);
		}
		container.getLaneSets().get(0).getLanes().add(lane);
		ModelUtil.setID(lane);
		return lane;
	}

	public void laneToTop(Lane lane) {
		LaneSet laneSet = FACTORY.createLaneSet();
//		laneSet.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(laneSet,resource);
		laneSet.getLanes().add(lane);
		Process process = getOrCreateProcess(getInternalParticipant());
		process.getLaneSets().add(laneSet);
	}

	public SequenceFlow createSequenceFlow(FlowNode source, FlowNode target) {
		SequenceFlow sequenceFlow = FACTORY.createSequenceFlow();
//		sequenceFlow.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(sequenceFlow,resource);

		addFlowElement(source, sequenceFlow);
		sequenceFlow.setSourceRef(source);
		sequenceFlow.setTargetRef(target);
		return sequenceFlow;
	}

	public MessageFlow createMessageFlow(InteractionNode source, InteractionNode target) {
		MessageFlow messageFlow = FACTORY.createMessageFlow();
//		messageFlow.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(messageFlow,resource);
		messageFlow.setSourceRef(source);
		messageFlow.setTargetRef(target);
		getOrCreateCollaboration().getMessageFlows().add(messageFlow);
		return messageFlow;
	}

	public ConversationLink createConversationLink(InteractionNode source, InteractionNode target) {
		ConversationLink link = FACTORY.createConversationLink();
		link.setSourceRef(source);
		link.setTargetRef(target);
		getOrCreateCollaboration().getConversationLinks().add(link);
		return link;
	}

	public Association createAssociation(BaseElement source, BaseElement target) {
		BaseElement e = null;
		if (getParticipant(source) != null) {
			e = source;
		} else if (getParticipant(target) != null) {
			e = target;
		} else {
			e = getInternalParticipant();
		}
		Association association = FACTORY.createAssociation();
		addArtifact(e, association);
//		association.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(association,resource);
		association.setSourceRef(source);
		association.setTargetRef(target);
		return association;
	}

	private Collaboration getOrCreateCollaboration() {
		final List<RootElement> rootElements = getDefinitions().getRootElements();

		for (RootElement element : rootElements) {
			if (element instanceof Collaboration) {
				return (Collaboration) element;
			}
		}
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(resource);
		final Collaboration collaboration = FACTORY.createCollaboration();
//		collaboration.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(collaboration,resource);
		if (domain != null) {
			domain.getCommandStack().execute(new RecordingCommand(domain) {

				@Override
				protected void doExecute() {
					addCollaborationToRootElements(rootElements, collaboration);
				}
			});
		}
		return collaboration;
	}

	private void addCollaborationToRootElements(final List<RootElement> rootElements, final Collaboration collaboration) {
		Participant participant = FACTORY.createParticipant();
//		participant.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(participant,resource);
		participant.setName("Internal");
		for (RootElement element : rootElements) {
			if (element instanceof Process) {
				participant.setProcessRef((Process) element);
				break;
			}
		}
		collaboration.getParticipants().add(participant);
		rootElements.add(collaboration);
	}

	public Bpmn2ResourceImpl getResource() {
		return resource;
	}

	public Definitions getDefinitions() {
		return (Definitions) resource.getContents().get(0).eContents().get(0);
	}

	public void save() {
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(resource);
		if (domain != null) {
			domain.getCommandStack().execute(new RecordingCommand(domain) {
				@Override
				protected void doExecute() {
					saveResource();
				}
			});
		} else {
			saveResource();
		}
	}

	private void saveResource() {
		fixZOrder();
		try {
			resource.save(null);
		} catch (IOException e) {
			Activator.logError(e);
		}
	}

	private void fixZOrder() {
		final List<BPMNDiagram> diagrams = getAll(BPMNDiagram.class);
		for (BPMNDiagram bpmnDiagram : diagrams) {
			fixZOrder(bpmnDiagram);
		}

	}

	private void fixZOrder(BPMNDiagram bpmnDiagram) {
		EList<DiagramElement> elements = (EList<DiagramElement>) bpmnDiagram.getPlane().getPlaneElement();
		ECollections.sort(elements, new DIZorderComparator());
	}

	void loadResource() {
		try {
			resource.load(null);
		} catch (IOException e) {
			Activator.logError(e);
		}
	}

	public Participant getInternalParticipant() {
		return getOrCreateCollaboration().getParticipants().get(0);
	}

	public FlowElementsContainer getFlowElementContainer(Object o) {
		if (o == null || o instanceof BPMNDiagram) {
			return getOrCreateProcess(getInternalParticipant());
		}
		if (o instanceof Participant) {
			return getOrCreateProcess((Participant) o);
		}
		return findElementOfType(FlowElementsContainer.class, o);
	}

	public Participant getParticipant(final Object o) {
		if (o == null || o instanceof Diagram) {
			return getInternalParticipant();
		}

		Object object = o;
		if (o instanceof Shape) {
			object = BusinessObjectUtil.getFirstElementOfType((PictogramElement) o, BaseElement.class);
		}

		if (object instanceof Participant) {
			return (Participant) object;
		}

		Process process = findElementOfType(Process.class, object);

		for (Participant p : getOrCreateCollaboration().getParticipants()) {
			if (p.getProcessRef() != null && p.getProcessRef().equals(process)) {
				return p;
			}
		}

		return getInternalParticipant();
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseElement> T findElementOfType(Class<T> clazz, Object from) {
		if (!(from instanceof BaseElement)) {
			return null;
		}

		if (clazz.isAssignableFrom(from.getClass())) {
			return (T) from;
		}

		return findElementOfType(clazz, ((BaseElement) from).eContainer());
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getAll(final Class<T> class1) {
		ArrayList<T> l = new ArrayList<T>();
		TreeIterator<EObject> contents = resource.getAllContents();
		for (; contents.hasNext();) {
			Object t = contents.next();
			if (class1.isInstance(t)) {
				l.add((T) t);
			}
		}
		return l;
	}

	public DiagramElement findDIElement(Diagram diagram, BaseElement element) {
		List<BPMNDiagram> diagrams = getAll(BPMNDiagram.class);

		for (BPMNDiagram d : diagrams) {
			List<DiagramElement> planeElement = d.getPlane().getPlaneElement();

			for (DiagramElement elem : planeElement) {
				if (elem instanceof BPMNShape && element.getId() != null
						&& element.getId().equals(((BPMNShape) elem).getBpmnElement().getId())) {
					return (elem);
				} else if (elem instanceof BPMNEdge && element.getId() != null
						&& element.getId().equals(((BPMNEdge) elem).getBpmnElement().getId())) {
					return (elem);
				}
			}
		}

		return null;
	}

}
