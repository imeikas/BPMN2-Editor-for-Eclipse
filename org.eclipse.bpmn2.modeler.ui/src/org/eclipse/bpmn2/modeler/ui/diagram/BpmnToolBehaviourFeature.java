/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.diagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.features.activity.ActivitySelectionBehavior;
import org.eclipse.bpmn2.modeler.core.features.activity.task.extension.ICustomTaskEditor;
import org.eclipse.bpmn2.modeler.core.features.event.EventSelectionBehavior;
import org.eclipse.bpmn2.modeler.ui.FeatureMap;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.TaskFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.choreography.ChoreographySelectionBehavior;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.FeatureCheckerAdapter;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeature;
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

		IFeatureProvider featureProvider = getFeatureProvider();

		createConnectors(pref, ret, featureProvider);

		createEventsCompartments(pref, ret, featureProvider);
		createTasksCompartments(pref, ret, featureProvider);
		createGatewaysCompartments(pref, ret, featureProvider);
		createEventDefinitionsCompartments(pref, ret, featureProvider);
		createDataCompartments(pref, ret, featureProvider);
		createOtherCompartments(pref, ret, featureProvider);

		createCustomTasks(ret, featureProvider);

		return ret.toArray(new IPaletteCompartmentEntry[ret.size()]);
	}

	private void createEventsCompartments(Bpmn2Preferences pref, List<IPaletteCompartmentEntry> ret,
			IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry("Events", null);
		ret.add(compartmentEntry);

		createEntries(pref, FeatureMap.EVENTS, compartmentEntry, featureProvider);
	}

	private void createOtherCompartments(Bpmn2Preferences pref, List<IPaletteCompartmentEntry> ret,
			IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry("Other", null);
		compartmentEntry.setInitiallyOpen(false);
		ret.add(compartmentEntry);

		createEntries(pref, FeatureMap.OTHER, compartmentEntry, featureProvider);

	}

	private void createDataCompartments(Bpmn2Preferences pref, List<IPaletteCompartmentEntry> ret,
			IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry("Data Items", null);
		compartmentEntry.setInitiallyOpen(false);
		ret.add(compartmentEntry);

		createEntries(pref, FeatureMap.DATA, compartmentEntry, featureProvider);

	}

	private void createEventDefinitionsCompartments(Bpmn2Preferences pref, List<IPaletteCompartmentEntry> ret,
			IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry("Event Definitions", null);
		compartmentEntry.setInitiallyOpen(false);
		ret.add(compartmentEntry);

		createEntries(pref, FeatureMap.EVENT_DEFINITIONS, compartmentEntry, featureProvider);

	}

	private void createGatewaysCompartments(Bpmn2Preferences pref, List<IPaletteCompartmentEntry> ret,
			IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry("Gateways", null);
		ret.add(compartmentEntry);

		createEntries(pref, FeatureMap.GATEWAYS, compartmentEntry, featureProvider);

	}

	private void createTasksCompartments(Bpmn2Preferences pref, List<IPaletteCompartmentEntry> ret,
			IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry("Tasks", null);
		ret.add(compartmentEntry);

		createEntries(pref, FeatureMap.TASKS, compartmentEntry, featureProvider);

	}

	private void createConnectors(Bpmn2Preferences pref, List<IPaletteCompartmentEntry> ret,
			IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry;
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
	}

	private void createEntries(Bpmn2Preferences pref, List<Class<? extends IFeature>> neededEntries,
			PaletteCompartmentEntry compartmentEntry, IFeatureProvider featureProvider) {
		List<ICreateFeature> tools = Arrays.asList(featureProvider.getCreateFeatures());

		for (ICreateFeature cf : tools) {
			EClass feature = FeatureMap.getElement(cf);
			if (pref.isEnabled(feature) && neededEntries.contains(cf.getClass())) {
				ObjectCreationToolEntry objectCreationToolEntry = new ObjectCreationToolEntry(cf.getCreateName(),
						cf.getCreateDescription(), cf.getCreateImageId(), cf.getCreateLargeImageId(), cf);
				compartmentEntry.addToolEntry(objectCreationToolEntry);
			}
		}
	}

	private void createCustomTasks(List<IPaletteCompartmentEntry> ret, IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry;
		compartmentEntry = new PaletteCompartmentEntry("Custom Task", null);
		compartmentEntry.setInitiallyOpen(false);
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
		} else if (EventSelectionBehavior.canApplyTo(pe)) {
			return EventSelectionBehavior.getClickArea(pe);
		} else if (ChoreographySelectionBehavior.canApplyTo(pe)) {
			return ChoreographySelectionBehavior.getClickArea(pe);
		}
		return super.getClickArea(pe);
	}

	@Override
	public GraphicsAlgorithm getSelectionBorder(PictogramElement pe) {
		if (ActivitySelectionBehavior.canApplyTo(pe)) {
			return ActivitySelectionBehavior.getSelectionBorder(pe);
		} else if (EventSelectionBehavior.canApplyTo(pe)) {
			return EventSelectionBehavior.getSelectionBorder(pe);
		} else if (ChoreographySelectionBehavior.canApplyTo(pe)) {
			return ChoreographySelectionBehavior.getSelectionBorder(pe);
		}
		return super.getSelectionBorder(pe);
	}
}