package org.jboss.bpmn2.editor.core.features.artifact;

import org.eclipse.bpmn2.Artifact;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Group;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class GroupFeatureContainer implements FeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof Group;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateGroupFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AbstractAddShapeFeature(fp) {

			@Override
			public boolean canAdd(IAddContext context) {
				return true;
			}

			@Override
			public PictogramElement add(IAddContext context) {
				IGaService gaService = Graphiti.getGaService();
				IPeService peService = Graphiti.getPeService();
				Group group = (Group) context.getNewObject();

				ContainerShape container = peService.createContainerShape(context.getTargetContainer(), true);
				RoundedRectangle rect = gaService.createRoundedRectangle(container, 5, 5);
				rect.setFilled(false);
				rect.setLineWidth(2);
				rect.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				rect.setLineStyle(LineStyle.DASHDOT);
				gaService.setLocationAndSize(rect, context.getX(), context.getY(), 400, 400);

				if (group.eResource() == null) {
					getDiagram().eResource().getContents().add(group);
				}
				
				link(container, group);
				return container;
			}
		};
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return null;
	}

	public static class CreateGroupFeature extends AbstractCreateArtifactFeature {

		public CreateGroupFeature(IFeatureProvider fp) {
			super(fp, "Group", "Visually groups elements");
		}

		@Override
		public boolean canCreate(ICreateContext context) {
			return true;
		}

		@Override
		Artifact createArtifact(ICreateContext context) {
			return ModelHandler.FACTORY.createGroup();
		}

		@Override
        String getStencilImageId() {
			return ImageProvider.IMG_16_GROUP;
        }
	}
}