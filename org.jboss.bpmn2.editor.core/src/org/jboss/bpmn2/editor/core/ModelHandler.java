package org.jboss.bpmn2.editor.core;

import java.io.IOException;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Task;
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

	@Deprecated
	public Task createTask() {
		return addFlowElement(FACTORY.createTask());
	}

	@Deprecated
	public SequenceFlow createSequenceFlow() {
		return addFlowElement(FACTORY.createSequenceFlow());
	}
	public SequenceFlow createSequenceFlow(FlowNode source, FlowNode target) {
		SequenceFlow flow = addFlowElement(FACTORY.createSequenceFlow());
		flow.setSourceRef(source);
		flow.setTargetRef(target);
		return flow;
	}
	
	@Deprecated
	public ExclusiveGateway createExclusiveGateway() {
		return addFlowElement(FACTORY.createExclusiveGateway());
	}

	private Process getOrCreateFirstProcess() {
		Process process = getFirstProcess();
		if (process == null) {
			process = FACTORY.createProcess();
			getDefinitions().getRootElements().add(process);
		}
		return process;
	}

	public Process getFirstProcess() {
		for (RootElement element : getDefinitions().getRootElements()) {
			if (element instanceof Process) {
				return (Process) element;
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
