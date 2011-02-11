package org.jboss.bpmn2.editor.core.features.artifact;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.jboss.bpmn2.editor.core.features.FeatureResolver;

public class ArtifactFeatureResolver implements FeatureResolver {

	@Override
	public List<ICreateConnectionFeature> getCreateConnectionFeatures(IFeatureProvider fp) {
		List<ICreateConnectionFeature> list = new ArrayList<ICreateConnectionFeature>();
		list.add(new CreateAssociationFeature(fp));
		return list;
	}

	@Override
	public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {
		List<ICreateFeature> list = new ArrayList<ICreateFeature>();
		list.add(new CreateTextAnnotationFeature(fp));
		return list;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof TextAnnotation)
			return new AddTextAnnotationFeature(fp);
		if (e instanceof Association)
			return new AddAssociationFeature(fp);
		return null;
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof TextAnnotation)
			return new DirectEditTextAnnotationFeature(fp);
		return null;
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof TextAnnotation)
			return new LayoutTextAnnotationFeature(fp);
		return null;
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof TextAnnotation)
			return new UpdateTextAnnotationFeature(fp);
		return null;
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof TextAnnotation)
			return new MoveTextAnnotationFeature(fp);
		return null;
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp, BaseElement e) {
		return null; // NOT YET SUPPORTED
	}

}
