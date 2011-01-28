package org.jboss.bpmn2.editor.core.diagram;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.impl.ConnectionCreationToolEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.jboss.bpmn2.editor.core.Bpmn2Preferences;

public class BpmnToolBehaviourFeature extends DefaultToolBehaviorProvider {

	public BpmnToolBehaviourFeature(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
	}

	@Override
	public IPaletteCompartmentEntry[] getPalette() {

		String projectName = getDiagramTypeProvider().getDiagram().eResource().getURI().segment(1);
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		Bpmn2Preferences pref = Bpmn2Preferences.getPreferences(project);

		List<IPaletteCompartmentEntry> ret = new ArrayList<IPaletteCompartmentEntry>();

		// add compartments from super class

		// add new compartment at the end of the existing compartments
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry("Flow Objects", null);
		ret.add(compartmentEntry);

		// add all create-features to the new stack-entry
		IFeatureProvider featureProvider = getFeatureProvider();
		ICreateFeature[] createFeatures = featureProvider.getCreateFeatures();
		for (ICreateFeature cf : createFeatures) {
			if (pref.isEnabled(cf)) {
				ObjectCreationToolEntry objectCreationToolEntry = new ObjectCreationToolEntry(cf.getCreateName(),
				        cf.getCreateDescription(), cf.getCreateImageId(), cf.getCreateLargeImageId(), cf);
				compartmentEntry.addToolEntry(objectCreationToolEntry);
			}
		}

		compartmentEntry = new PaletteCompartmentEntry("Connectors", null);
		ret.add(compartmentEntry);
		// add all create-connection-features to the new stack-entry
		ICreateConnectionFeature[] createConnectionFeatures = featureProvider.getCreateConnectionFeatures();
		for (ICreateConnectionFeature cf : createConnectionFeatures) {
			if (pref.isEnabled(cf)) {
				ConnectionCreationToolEntry connectionCreationToolEntry = new ConnectionCreationToolEntry(
				        cf.getCreateName(), cf.getCreateDescription(), cf.getCreateImageId(),
				        cf.getCreateLargeImageId());
				connectionCreationToolEntry.addCreateConnectionFeature(cf);
				compartmentEntry.addToolEntry(connectionCreationToolEntry);
			}
		}
		return ret.toArray(new IPaletteCompartmentEntry[ret.size()]);
	}
}
