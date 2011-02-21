package org.jboss.bpmn2.editor.core;

import java.io.IOException;

import org.eclipse.bpmn2.Artifact;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.query.conditions.eobjects.EObjectCondition;
import org.eclipse.emf.query.statements.FROM;
import org.eclipse.emf.query.statements.SELECT;
import org.eclipse.emf.query.statements.WHERE;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.mm.pictograms.Diagram;

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
				Collaboration collaboration = FACTORY.createCollaboration();
				Participant participant = FACTORY.createParticipant();
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

	public <T extends FlowElement> T addFlowElement(Participant participant, T elem) {
		Process process = getOrCreateProcess(participant);
		process.getFlowElements().add(elem);
		return elem;
	}

	public <T extends FlowElement> T addFlowElement(T elem) {
		return addFlowElement(getInternalParticipant(), elem);
	}

	public void moveFlowNode(FlowNode node, Participant source, Participant target) {
		if (!source.equals(target)) {
			Process sourceProcess = getOrCreateProcess(source);
			Process targetProcess = getOrCreateProcess(target);
			moveFlowNode(node, sourceProcess, targetProcess);
		}
	}

	private void moveFlowNode(FlowNode node, Process sourceProcess, Process targetProcess) {
		sourceProcess.getFlowElements().remove(node);
		targetProcess.getFlowElements().add(node);
		for (SequenceFlow flow : node.getOutgoing()) {
			targetProcess.getFlowElements().add(flow);
			sourceProcess.getFlowElements().remove(flow);
		}
	}

	public Participant addParticipant() {
		Collaboration collaboration = getCollaboration();
		Participant participant = FACTORY.createParticipant();
		collaboration.getParticipants().add(participant);
		return participant;
	}

	public <A extends Artifact> A addArtifact(Participant participant, A artifact) {
		Process process = getOrCreateProcess(participant);
		process.getArtifacts().add(artifact);
		return artifact;
	}

	public Lane addLane(Participant participant) {
		Lane lane = FACTORY.createLane();
		Process process = getOrCreateProcess(participant);
		if (process.getLaneSets().isEmpty()) {
			process.getLaneSets().add(FACTORY.createLaneSet());
		}
		process.getLaneSets().get(0).getLanes().add(lane);
		return lane;
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

	private Process getOrCreateProcess(Participant participant) {
		if (participant.getProcessRef() == null) {
			Process process = FACTORY.createProcess();
			process.setName("Process for " + participant.getName());
			getDefinitions().getRootElements().add(process);
			participant.setProcessRef(process);
		}
		return participant.getProcessRef();
	}

	public Lane addLaneTo(Lane targetLane) {
		Lane lane = FACTORY.createLane();

		if (targetLane.getChildLaneSet() == null) {
			targetLane.setChildLaneSet(ModelHandler.FACTORY.createLaneSet());
		}

		LaneSet targetLaneSet = targetLane.getChildLaneSet();
		targetLaneSet.getLanes().add(lane);

		lane.getFlowNodeRefs().addAll(targetLane.getFlowNodeRefs());
		targetLane.getFlowNodeRefs().clear();

		return lane;
	}

	public void laneToTop(Lane lane) {
		LaneSet laneSet = FACTORY.createLaneSet();
		laneSet.getLanes().add(lane);
		Process process = getOrCreateProcess(getInternalParticipant());
		process.getLaneSets().add(laneSet);
	}

	public SequenceFlow createSequenceFlow(FlowNode source, FlowNode target) {
		SequenceFlow flow = addFlowElement(FACTORY.createSequenceFlow());
		flow.setSourceRef(source);
		flow.setTargetRef(target);
		return flow;
	}

	public MessageFlow createMessageFlow(InteractionNode source, InteractionNode target) {
		MessageFlow messageFlow = FACTORY.createMessageFlow();
		messageFlow.setSourceRef(source);
		messageFlow.setTargetRef(target);
		getCollaboration().getMessageFlows().add(messageFlow);
		return messageFlow;
	}

	public Association createAssociation(TextAnnotation annotation, BaseElement element) {
		Association association = addArtifact(getParticipant(element), FACTORY.createAssociation());
		association.setSourceRef(element);
		association.setTargetRef(annotation);
		return association;
	}

	public Collaboration getCollaboration() {
		for (RootElement element : getDefinitions().getRootElements()) {
			if (element instanceof Collaboration) {
				return (Collaboration) element;
			}
		}
		return null;
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
		try {
			resource.save(null);
		} catch (IOException e) {
			Activator.logError(e);
		}
	}

	void loadResource() {
		try {
			resource.load(null);
		} catch (IOException e) {
			Activator.logError(e);
		}
	}

	public Participant getInternalParticipant() {
		return getCollaboration().getParticipants().get(0);
	}

	public Participant getParticipant(Object o) {
		if (o instanceof Diagram) {
			return getInternalParticipant();
		}

		if (o instanceof Participant) {
			return (Participant) o;
		}

		for (Participant p : getCollaboration().getParticipants()) {

			if (p.getProcessRef() == null) {
				continue;
			}

			Process process = p.getProcessRef();

			if (o instanceof Lane && isSubLane(process, (Lane) o)) {
				return p;
			} else if (process.getFlowElements().contains(o)) {
				return p;
			}

		}
		return null;
	}

	private boolean isSubLane(Process p, Lane lane) {
		if (p.getLaneSets().isEmpty()) {
			return false;
		}

		boolean found = false;

		for (LaneSet s : p.getLaneSets()) {
			if (isSubLane(s, lane)) {
				found = true;
				break;
			}
		}

		return found;
	}

	private boolean isSubLane(LaneSet set, Lane lane) {
		if (set == null) {
			return false;
		}

		if (set.getLanes().contains(lane)) {
			return true;
		}

		boolean found = false;

		for (Lane l : set.getLanes()) {
			if (isSubLane(l.getChildLaneSet(), lane)) {
				found = true;
				break;
			}
		}

		return found;
	}

	public Object[] getAll(final Class class1) {
		SELECT select = new SELECT(new FROM(resource.getContents()), new WHERE(new EObjectCondition() {

			@Override
			public boolean isSatisfied(EObject eObject) {
				return class1.isInstance(eObject);
			}
		}));
		return select.execute().toArray();
	}
}
