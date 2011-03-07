package org.jboss.bpmn2.editor.ui.features.event;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.event.AbstractCreateEventFeature;
import org.jboss.bpmn2.editor.core.features.event.AbstractEventFeatureContainer;
import org.jboss.bpmn2.editor.core.features.event.AddEventFeature;
import org.jboss.bpmn2.editor.ui.ImageProvider;

public class EndEventFeatureContainer extends AbstractEventFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof EndEvent;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateEndEventFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddEventFeature(fp) {
			@Override
			protected void decorateEllipse(Ellipse e) {
				e.setLineWidth(3);
			}
		};
	}

	public static class CreateEndEventFeature extends AbstractCreateEventFeature {

		public CreateEndEventFeature(IFeatureProvider fp) {
			super(fp, "End Event", "Indicates the end of a process or choreography");
		}

		@Override
		protected Event createFlowElement(ICreateContext context) {
			EndEvent end = ModelHandler.FACTORY.createEndEvent();
			end.setName("End");
			return end;
		}

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_END_EVENT;
		}
	}
}