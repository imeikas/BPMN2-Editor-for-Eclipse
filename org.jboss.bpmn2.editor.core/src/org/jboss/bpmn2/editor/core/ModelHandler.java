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
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;

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

				domain.getCommandStack().execute(new RecordingCommand(domain) {
					protected void doExecute() {
						docRoot.setDefinitions(definitions);
						resource.getContents().add(docRoot);
					}
				});
				return;
			}
		}
	}

	public <T extends FlowElement> T addFlowElement(T elem) {
		Process process = getOrCreateFirstProcess();
		process.getFlowElements().add(elem);
		return elem;
	}
	
	public Participant addCollaborator() {
		Collaboration collaboration = getOrCreateFirstCollaboration();
		Participant participant = FACTORY.createParticipant();
		collaboration.getParticipants().add(participant);
		participant.setProcessRef(getOrCreateFirstProcess());
		return participant;
	}
	
	public <A extends Artifact> A addArtifact(A artifact) {
		Process process = getOrCreateFirstProcess();
		process.getArtifacts().add(artifact);
		return artifact;
	}
	
	public Lane addLane() {
		LaneSet laneSet = FACTORY.createLaneSet();
		Lane lane = FACTORY.createLane();
		laneSet.getLanes().add(lane);
		Process process = getOrCreateFirstProcess();
		process.getLaneSets().add(laneSet);
		return lane;
	}
	
	public Lane addLaneTo(Lane targetLane) {
		Lane lane = FACTORY.createLane();
		
		if(targetLane.getChildLaneSet() == null) {
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
		Process process = getOrCreateFirstProcess();
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
		return messageFlow;
	}
	
	public Association createAssociation(TextAnnotation annotation, BaseElement element) {
		Association association = addArtifact(FACTORY.createAssociation());
		association.setSourceRef(element);
		association.setTargetRef(annotation);
		return association;
	}
	
	private Process getOrCreateFirstProcess() {
		Process process = getFirstProcess();
		if (process == null) {
			process = FACTORY.createProcess();
			getDefinitions().getRootElements().add(process);
		}
		return process;
	}
	
	private Collaboration getOrCreateFirstCollaboration() {
		Collaboration collaboration = getFirstCollaboration();
		if(collaboration == null) {
			collaboration = FACTORY.createCollaboration();
			getDefinitions().getRootElements().add(collaboration);
		}
		return collaboration;
	}

	public Process getFirstProcess() {
		for (RootElement element : getDefinitions().getRootElements()) {
			if (element instanceof Process) {
				return (Process) element;
			}
		}
		return null;
	}
	
	public Collaboration getFirstCollaboration() {
		for (RootElement element : getDefinitions().getRootElements()) {
			if(element instanceof Collaboration) {
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
}
