package org.jboss.bpmn2.editor.core.features.activity.subprocess;

import org.eclipse.bpmn2.AdHocSubProcess;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class AdHocSubProcessFeatureContainer extends AbstractSubProcessFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof AdHocSubProcess;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateAdHocSubProcessFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return null;
	}

	public static class CreateAdHocSubProcessFeature extends AbstractCreateSubProcess {

		public CreateAdHocSubProcessFeature(IFeatureProvider fp) {
			super(fp, "Ad-Hoc SubProcess",
			        "A specialized type of Sub-Process that is a group of Activities that have no REQUIRED sequence relationships");
		}

		@Override
		protected SubProcess createFlowElement(ICreateContext context) {
			AdHocSubProcess adHocSubProcess = ModelHandler.FACTORY.createAdHocSubProcess();
			adHocSubProcess.setName("Ad-Hoc SubProcess");
			return adHocSubProcess;
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_TRANSACTION;
		}
	}
}