package org.jboss.bpmn2.editor.core.diagram;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.FeatureCheckerAdapter;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureChecker;
import org.eclipse.graphiti.features.IFeatureCheckerHolder;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.impl.ConnectionCreationToolEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.Bpmn2Preferences;
import org.jboss.bpmn2.editor.core.FeatureMap;
import org.jboss.bpmn2.editor.core.features.activity.ActivitySelectionBehavior;
import org.jboss.bpmn2.editor.core.features.activity.task.TaskFeatureContainer;
import org.jboss.bpmn2.editor.core.features.activity.task.extension.ICustomTaskEditor;

public class BpmnToolBehaviourFeature extends DefaultToolBehaviorProvider implements IFeatureCheckerHolder {

	public BpmnToolBehaviourFeature(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
	}

	@Override
	public IPaletteCompartmentEntry[] getPalette() {

		EList<Resource> resources = getDiagramTypeProvider().getDiagram().eResource().getResourceSet().getResources();
		IProject project = null;
		for (Resource resource : resources) {
			if (resource.getURI().segmentCount() > 1) {
				String projectName = resource.getURI().segment(1);
				project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
				if (project != null) {
					break;
				}
			}
		}

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
			if (pref.isEnabled(FeatureMap.getElement(cf))) {
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
			if (pref.isEnabled(FeatureMap.getElement(cf))) {
				ConnectionCreationToolEntry connectionCreationToolEntry = new ConnectionCreationToolEntry(
						cf.getCreateName(), cf.getCreateDescription(), cf.getCreateImageId(),
						cf.getCreateLargeImageId());
				connectionCreationToolEntry.addCreateConnectionFeature(cf);
				compartmentEntry.addToolEntry(connectionCreationToolEntry);
			}
		}

		createCustomTasks(ret, featureProvider);

		return ret.toArray(new IPaletteCompartmentEntry[ret.size()]);
	}

	private void createCustomTasks(List<IPaletteCompartmentEntry> ret, IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry;
		compartmentEntry = new PaletteCompartmentEntry("Custom Task", null);
		ret.add(compartmentEntry);

		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
				ICustomTaskEditor.TASK_EDITOR_ID);

		try {
			for (IConfigurationElement e : config) {
				String name = e.getAttribute("name");

				final Object o = e.createExecutableExtension("createFeature");
				if (o instanceof TaskFeatureContainer) {

					ICreateFeature cf = ((TaskFeatureContainer) o).getCreateFeature(featureProvider);
					ObjectCreationToolEntry objectCreationToolEntry = new ObjectCreationToolEntry(name,
							cf.getCreateDescription(), cf.getCreateImageId(), cf.getCreateLargeImageId(), cf);
					compartmentEntry.addToolEntry(objectCreationToolEntry);

				}
			}
		} catch (CoreException ex) {
			Activator.logError(ex);
		}
	}

	@Override
	public IFeatureChecker getFeatureChecker() {
		return new FeatureCheckerAdapter(false) {
			@Override
			public boolean allowAdd(IContext context) {
				return super.allowAdd(context);
			}

			@Override
			public boolean allowCreate() {
				return super.allowCreate();
			}
		};
	}

	@Override
	public GraphicsAlgorithm[] getClickArea(PictogramElement pe) {
		if (ActivitySelectionBehavior.canApplyTo(pe)) {
			return ActivitySelectionBehavior.getClickArea(pe);
		}
		return super.getClickArea(pe);
	}

	@Override
	public GraphicsAlgorithm getSelectionBorder(PictogramElement pe) {
		if (ActivitySelectionBehavior.canApplyTo(pe)) {
			return ActivitySelectionBehavior.getSelectionBorder(pe);
		}
		return super.getSelectionBorder(pe);
	}
}