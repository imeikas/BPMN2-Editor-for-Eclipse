package org.jboss.bpmn2.editor.ui.features.artifact;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.ui.features.DefaultDeleteFeature;
import org.jboss.bpmn2.editor.core.features.DefaultBPMNResizeFeature;
import org.jboss.bpmn2.editor.core.features.FeatureResolver;
import org.jboss.bpmn2.editor.core.features.artifact.AddTextAnnotationFeature;
import org.jboss.bpmn2.editor.core.features.artifact.DirectEditTextAnnotationFeature;
import org.jboss.bpmn2.editor.core.features.artifact.LayoutTextAnnotationFeature;
import org.jboss.bpmn2.editor.core.features.artifact.MoveTextAnnotationFeature;
import org.jboss.bpmn2.editor.core.features.artifact.UpdateTextAnnotationFeature;

public class ArtifactFeatureResolver implements FeatureResolver {

	@Override
	public List<ICreateConnectionFeature> getCreateConnectionFeatures(IFeatureProvider fp) {
		return new ArrayList<ICreateConnectionFeature>();
	}

	@Override
	public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {
		List<ICreateFeature> list = new ArrayList<ICreateFeature>();
		list.add(new CreateTextAnnotationFeature(fp));
		return list;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof TextAnnotation) {
			return new AddTextAnnotationFeature(fp);
		}
		return null;
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof TextAnnotation) {
			return new DirectEditTextAnnotationFeature(fp);
		}
		return null;
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof TextAnnotation) {
			return new LayoutTextAnnotationFeature(fp);
		}
		return null;
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof TextAnnotation) {
			return new UpdateTextAnnotationFeature(fp);
		}
		return null;
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof TextAnnotation) {
			return new MoveTextAnnotationFeature(fp);
		}
		return null;
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof TextAnnotation) {
			return new DefaultBPMNResizeFeature(fp);
		}
		return null;
	}

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof TextAnnotation) {
			return new DefaultDeleteFeature(fp);
		}
		return null;
	}

}
