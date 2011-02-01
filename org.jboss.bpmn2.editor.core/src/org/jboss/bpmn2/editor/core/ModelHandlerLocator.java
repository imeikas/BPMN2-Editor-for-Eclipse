package org.jboss.bpmn2.editor.core;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

public class ModelHandlerLocator {
	
	private static HashMap<URI, ModelHandler> map = new HashMap<URI, ModelHandler>();

	public static ModelHandler getModelHandler(Resource eResource) throws IOException {
		URI uri = eResource.getURI();

		uri = uri.trimFragment();
		uri = uri.trimFileExtension();
		uri = uri.appendFileExtension("bpmn2");

		return getModelHandler(uri);
	}

	public static ModelHandler getModelHandler(URI path) throws IOException {
		return map.get(path);
	}

	public static void releaseModel(URI path) {
		map.remove(path);
	}

	
	public static ModelHandler createModelHandler(URI path, final Bpmn2ResourceImpl resource) {
		if (map.containsKey(path)) {
			return map.get(path);
		}
		return createNewModelHandler(path, resource);
	}

	private static ModelHandler createNewModelHandler(URI path, final Bpmn2ResourceImpl resource) {
		ModelHandler handler = new ModelHandler();
		map.put(path, handler);
		handler.resource = resource;

		URI uri = resource.getURI();
		if (ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(uri.toPlatformString(true))).exists() && !resource.isLoaded()) {
			handler.loadResource();
		}

		handler.createDefinitionsIfMissing();
		return handler;
	}

}
