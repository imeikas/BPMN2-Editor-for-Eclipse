package org.jboss.bpmn2.editor.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeature;
import org.jboss.bpmn2.editor.core.diagram.BPMNFeatureProvider;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class Bpmn2Preferences {

	private final Preferences prefs;

	private Bpmn2Preferences(Preferences prefs) {
		this.prefs = prefs;
	}

	public static Bpmn2Preferences getPreferences(IProject project) {
		IEclipsePreferences rootNode = Platform.getPreferencesService().getRootNode();
		Preferences prefs = rootNode.node(ProjectScope.SCOPE).node(project.getName())
		        .node("org.jboss.bpmn2.editor.tools");
		return new Bpmn2Preferences(prefs);
	}

	public boolean isEnabled(IFeature feature) {
		return prefs.getBoolean(feature.getClass().getCanonicalName(), true);
	}

	public void setEnabled(IFeature feature, boolean enabled) throws BackingStoreException {
		prefs.putBoolean(feature.getClass().getCanonicalName(), enabled);
		prefs.flush();
	}

	public List<ToolEnablement> getListOfTools() {
		// works as long as feature provider returns statically compiled lists
		ICreateFeature[] createFeatures = new BPMNFeatureProvider(null).getCreateFeatures();
		return getEnablementList(createFeatures);
	}

	private List<ToolEnablement> getEnablementList(IFeature[] features) {
	    ArrayList<ToolEnablement> ret = new ArrayList<ToolEnablement>();
		for (IFeature feature : features) {
	        ToolEnablement e = new ToolEnablement();
	        e.feature = feature;
	        e.setEnabled(isEnabled(feature));
			ret.add(e);
        }
		return ret;
    }

	public List<ToolEnablement> getListOfConnectors() {
		// works as long as feature provider returns statically compiled lists
		ICreateConnectionFeature[] features = new BPMNFeatureProvider(null).getCreateConnectionFeatures();
		return getEnablementList(features);
	}

}
