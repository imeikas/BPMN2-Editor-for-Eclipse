package org.jboss.bpmn2.editor.core;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class ModelHandler {
	private final URI path;
	private final Bpmn2Factory factory = Bpmn2Factory.eINSTANCE;

	private Bpmn2ResourceImpl resource;
	private Definitions definitions;

	public ModelHandler(URI path) {
		this.path = path;
	}

	public Bpmn2Resource createNewResource() {
		Bpmn2ResourceFactoryImpl rFactory = new Bpmn2ResourceFactoryImpl();

		resource = (Bpmn2ResourceImpl) rFactory.createResource(path);
		getResource().basicSetResourceSet(new ResourceSetImpl(), null);

		DocumentRoot root = factory.createDocumentRoot();
		definitions = factory.createDefinitions();
		root.setDefinitions(definitions);
		resource.getContents().add(root);

		return getResource();
	}

	public Task createTask() {
		if (definitions == null)
			return null;

		EList<EObject> defCont = definitions.eContents();
		Process process;
		if (defCont.size() == 0) {
			process = factory.createProcess();
			definitions.getRootElements().add(process);
		} else {
			//FIXME: need to fix asap
			throw new RuntimeException("Not yet Implemented!");
		}
		Task task = factory.createTask();
		process.getFlowElements().add(task);
		return task;
	}

	public Bpmn2ResourceImpl getResource() {
		return resource;
	}
}
