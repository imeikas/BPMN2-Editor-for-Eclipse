package org.jboss.bpmn2.editor.core.features.activity.subprocess;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.MultiUpdateFeature;

public class SubProcessFeatureContainer extends AbstractSubProcessFeatureContainer {
	
	public static final String TRIGGERED_BY_EVENT = "triggered-by-event-key";
	
	@Override
    public boolean canApplyTo(BaseElement element) {
	    return element instanceof SubProcess;
    }

	@Override
    public ICreateFeature getCreateFeature(IFeatureProvider fp) {
	    return new CreateSubProcessFeature(fp);
    }

	@Override
    public IAddFeature getAddFeature(IFeatureProvider fp) {
	    return new AddExpandedSubProcessFeature(fp);
	}
	
	@Override
	public MultiUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature multiUpdate = super.getUpdateFeature(fp);
		UpdateSubProcessFeature updateSubProcessFeature = new UpdateSubProcessFeature(fp);
		multiUpdate.addUpdateFeature(updateSubProcessFeature);
		return multiUpdate;
	}
	
	public static class CreateSubProcessFeature extends AbstractCreateSubProcess {

		public CreateSubProcessFeature(IFeatureProvider fp) {
	        super(fp, "Sub-Process", "Inner activity");
        }

		@Override
		protected SubProcess createFlowElement(ICreateContext context) {
			SubProcess subProcess = ModelHandler.FACTORY.createSubProcess();
			subProcess.setName("SubProcess");
			subProcess.setTriggeredByEvent(false);
			return subProcess;
		}
		
		@Override
        protected String getStencilImageId() {
	        return ImageProvider.IMG_16_SUB_PROCESS;
        }
	}
}