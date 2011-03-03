package org.jboss.bpmn2.editor.core.features.activity.subprocess;

import java.util.Iterator;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.AdHocSubProcess;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

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
		return new AddExpandedSubProcessFeature(fp) {
			@Override
			protected void hook(Activity activity, ContainerShape container, IAddContext context, int width, int height) {
			    super.hook(activity, container, context, width, height);
			    IPeService peService = Graphiti.getPeService();
			    Iterator<Shape> iterator = peService.getAllContainedShapes(container).iterator();
			    while (iterator.hasNext()) {
	                Shape shape = (Shape) iterator.next();
	                String property = peService.getPropertyValue(shape, ShapeUtil.ACTIVITY_MARKER_CONTAINER);
	                if(property != null && new Boolean(property)) {
	                	Polyline tilde = ShapeUtil.createActivityMarkerAdHoc((ContainerShape) shape);
	                	tilde.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
	                }
                }
			}
		};
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
			return ImageProvider.IMG_16_AD_HOC_SUB_PROCESS;
		}
	}
}