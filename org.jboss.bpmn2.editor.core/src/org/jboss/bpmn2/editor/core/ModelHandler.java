package org.jboss.bpmn2.editor.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;

public class ModelHandler {
	private final URI path;
	private static final Bpmn2Factory factory = Bpmn2Factory.eINSTANCE;

	private Bpmn2ResourceImpl resource;
	private Definitions definitions;

	private static HashMap<URI, ModelHandler> map = new HashMap<URI, ModelHandler>();

	private ModelHandler(URI path) {
		this.path = path;
	}

	public static ModelHandler createModelHandler(URI path, final Bpmn2ResourceImpl resource) {
		if (map.containsKey(path))
			return map.get(path);

		ModelHandler handler = new ModelHandler(path);
		map.put(path, handler);
		handler.resource = resource;
		EList<EObject> contents = resource.getContents();
		
		if (contents.isEmpty() || !(contents.get(0) instanceof RootElement)) {
			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(resource);
		
			if (domain != null) {
				domain.getCommandStack().execute(new RecordingCommand(domain) {
			
					protected void doExecute() {
					
						DocumentRoot docRoot = factory.createDocumentRoot();
						Definitions def = factory.createDefinitions();
						
						docRoot.setDefinitions(def);
						resource.getContents().add(docRoot);
					}
				});
			}
		}
		handler.definitions = (Definitions) contents.get(0).eContents().get(0);
		return handler;
	}

	public static ModelHandler getModelHandler(URI path) throws IOException {
		return map.get(path);
	}

	public static void releaseModel(URI path) {
		map.remove(path);
	}

	private void loadResource() {
		Bpmn2ResourceFactoryImpl rFactory = new Bpmn2ResourceFactoryImpl();

		resource = (Bpmn2ResourceImpl) rFactory.createResource(path);
		try {
			resource.load(null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Task createTask() {
		Process process = getOrCreateFirstProcess();
		Task task = factory.createTask();
		process.getFlowElements().add(task);
		return task;
	}

	public SequenceFlow createSequenceFlow(){
		Process process = getOrCreateFirstProcess();
		SequenceFlow flow = factory.createSequenceFlow();
		process.getFlowElements().add(flow);
		return flow;
	}

	public ExclusiveGateway createGateway() {
		Process process = getOrCreateFirstProcess();
		ExclusiveGateway flow = factory.createExclusiveGateway();
		process.getFlowElements().add(flow);
	    return flow;
    }
	
	private Process getOrCreateFirstProcess() {
	    Process process = getFirstProcess();
		if (process == null) {
			process = factory.createProcess();
			definitions.getRootElements().add(process);
		}
	    return process;
    }

	
	public Process getFirstProcess() {
		List<RootElement> rootElements = definitions.getRootElements();
		for (RootElement element : rootElements) {
			if (element instanceof Process)
				return (Process) element;
		}
		return null;
	}

	public Bpmn2ResourceImpl getResource() {
		return resource;
	}

	public Definitions getDefinitions() {
		return definitions;
	}

	public void save()  {
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(resource);
		if (domain != null) {
			domain.getCommandStack().execute(new RecordingCommand(domain) {
				protected void doExecute() {
					saveWithoudTransaction();
				}
			});
		} else {
			saveWithoudTransaction();
		}
	}

	private void saveWithoudTransaction() {
	    try {
	        resource.save(null);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    }

	public static ModelHandler getModelHandler(Resource eResource) throws IOException {
		URI uri = eResource.getURI();
		uri = uri.trimFragment();
		uri = uri.trimFileExtension();
		uri = uri.appendFileExtension("bpmn2"); //FIXME: move into some Util
		return getModelHandler(uri);
    }


}
