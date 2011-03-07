package org.jboss.bpmn2.editor.ui.features.artifact;

import org.eclipse.bpmn2.Artifact;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Group;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;
import org.jboss.bpmn2.editor.core.features.DefaultBPMNResizeFeature;
import org.jboss.bpmn2.editor.core.features.DefaultBpmnMoveFeature;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;
import org.jboss.bpmn2.editor.core.features.artifact.AbstractCreateArtifactFeature;
import org.jboss.bpmn2.editor.ui.ImageProvider;
import org.jboss.bpmn2.editor.utils.AnchorUtil;
import org.jboss.bpmn2.editor.utils.StyleUtil;

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
		return new AbstractBpmnAddFeature(fp) {

			@Override
			public boolean canAdd(IAddContext context) {
				return true;
			}

			@Override
			public PictogramElement add(IAddContext context) {
				IGaService gaService = Graphiti.getGaService();
				IPeService peService = Graphiti.getPeService();
				Group group = (Group) context.getNewObject();

				int width = context.getWidth() > 0 ? context.getWidth() : 400;
				int height = context.getHeight() > 0 ? context.getHeight() : 400;

				ContainerShape container = peService.createContainerShape(context.getTargetContainer(), true);
				RoundedRectangle rect = gaService.createRoundedRectangle(container, 5, 5);
				rect.setFilled(false);
				rect.setLineWidth(2);
				rect.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				rect.setLineStyle(LineStyle.DASHDOT);
				gaService.setLocationAndSize(rect, context.getX(), context.getY(), width, height);

				peService.createChopboxAnchor(container);
				AnchorUtil.addFixedPointAnchors(container, rect);

				link(container, group);
				createDIShape(container, group);
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
		return new DefaultBpmnMoveFeature(fp);
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return new DefaultBPMNResizeFeature(fp);
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
		public Artifact createArtifact(ICreateContext context) {
			Group group = ModelHandler.FACTORY.createGroup();
			group.setId(EcoreUtil.generateUUID());
			return group;
		}

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_GROUP;
		}
	}

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider context) {
		// TODO Auto-generated method stub
		return null;
	}
}