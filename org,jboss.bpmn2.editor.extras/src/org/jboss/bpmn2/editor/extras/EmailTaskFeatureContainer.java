package org.jboss.bpmn2.editor.extras;

import java.util.ArrayList;

import org.eclipse.bpmn2.Task;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.Bpmn2Preferences;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.activity.task.AbstractCreateTaskFeature;
import org.jboss.bpmn2.editor.core.features.activity.task.TaskFeatureContainer;

public class EmailTaskFeatureContainer extends TaskFeatureContainer {

		public ICreateFeature getCreateFeature(IFeatureProvider fp) {
			return new CreateTaskFeature(fp);
		}

		public static class CreateTaskFeature extends AbstractCreateTaskFeature {

			public CreateTaskFeature(IFeatureProvider fp) {
				super(fp, "Email Task", "Create Email Task");
			}

			protected Task createFlowElement(ICreateContext context) {
				Task task = ModelHandler.FACTORY.createTask();
				task.setName("Email Task");
				ArrayList<EStructuralFeature> attributes = Bpmn2Preferences
						.getAttributes(task.eClass());
				for (EStructuralFeature eStructuralFeature : attributes) {
					if (eStructuralFeature.getName().equals("taskName"))
						task.eSet(eStructuralFeature, "email");
				}
				return task;
			}

			@Override
			protected String getStencilImageId() {
				return ImageProvider.IMG_16_ACTION;
			}
	}
}

