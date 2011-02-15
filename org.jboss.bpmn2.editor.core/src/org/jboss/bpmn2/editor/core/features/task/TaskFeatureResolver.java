package org.jboss.bpmn2.editor.core.features.task;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BusinessRuleTask;
import org.eclipse.bpmn2.ManualTask;
import org.eclipse.bpmn2.ReceiveTask;
import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.bpmn2.SendTask;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.UserTask;
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
import org.jboss.bpmn2.editor.core.features.MoveFlowNodeFeature;

public class TaskFeatureResolver implements FeatureResolver {

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof UserTask)
			return new AddUserTaskFeature(fp);
		if (e instanceof ManualTask)
			return new AddManualTaskFeature(fp);
		if (e instanceof ScriptTask)
			return new AddScriptTaskFeature(fp);
		if (e instanceof BusinessRuleTask)
			return new AddBusinessRuleTaskFeature(fp);
		if (e instanceof ServiceTask)
			return new AddServiceTaskFeature(fp);
		if (e instanceof SendTask)
			return new AddSendTaskFeature(fp);
		if (e instanceof ReceiveTask)
			return new AddReceiveTaskFeature(fp);
		if (e instanceof Task)
			return new AddTaskFeature(fp);
		return null;
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Task) {
			return new DirectEditTaskFeature(fp);
		}
		return null;
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Task) {
			return new LayoutTaskFeature(fp);
		}
		return null;
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Task) {
			return new UpdateTaskFeature(fp);
		}
		return null;
	}

	@Override
	public List<ICreateConnectionFeature> getCreateConnectionFeatures(IFeatureProvider fp) {
		return new ArrayList<ICreateConnectionFeature>();
	}

	@Override
	public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {
		List<ICreateFeature> list = new ArrayList<ICreateFeature>();
		list.add(new CreateTaskFeature(fp));
		list.add(new CreateServiceTaskFeature(fp));
		list.add(new CreateSendTaskFeature(fp));
		list.add(new CreateReceiveTaskFeature(fp));
		list.add(new CreateUserTaskFeature(fp));
		list.add(new CreateManualTaskFeature(fp));
		list.add(new CreateScriptTaskFeature(fp));
		list.add(new CreateBusinessRuleTaskFeature(fp));
		return list;
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Task) {
			return new MoveFlowNodeFeature(fp);
		}
		return null;
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp, BaseElement e) {
		return null; // NOT YET SUPPORTED
	}
}