package org.jboss.bpmn2.editor.core.features.data;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.utils.ShapeUtil;
import org.jboss.bpmn2.editor.utils.StyleUtil;

public class DataOutputFeatureContainer extends AbstractDataFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof DataOutput;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateDataOutputFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddDataFeature<DataOutput>(fp) {
			@Override
			protected boolean isSupportCollectionMarkers() {
				return false;
			}

			@Override
			protected void decorate(Polygon p) {
				Polygon arrow = ShapeUtil.createDataArrow(p);
				arrow.setFilled(true);
				arrow.setBackground(manageColor(StyleUtil.CLASS_FOREGROUND));
				arrow.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			}
		};
	}

	public static class CreateDataOutputFeature extends AbstractCreateDataInputOutputFeature {

		public CreateDataOutputFeature(IFeatureProvider fp) {
			super(fp, "Data Output", "Declaration that a particular kind of data can be produced as output");
		}

		@SuppressWarnings("unchecked")
		@Override
		DataOutput add(Object target, ModelHandler handler) {
			DataOutput dataOutput = ModelHandler.FACTORY.createDataOutput();
			dataOutput.setId(EcoreUtil.generateUUID());
			dataOutput.setName("Data Output");
			handler.addDataOutput(target, dataOutput);
			return dataOutput;
		}

		@Override
		String getStencilImageId() {
			return ImageProvider.IMG_16_DATA_OUTPUT;
		}

	}
}