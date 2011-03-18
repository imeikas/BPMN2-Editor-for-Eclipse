/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.bpmn2.editor.ui.features.participant;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Participant;
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
import org.jboss.bpmn2.editor.core.features.FeatureResolver;
import org.jboss.bpmn2.editor.core.features.MultiUpdateFeature;
import org.jboss.bpmn2.editor.core.features.participant.AddParticipantFeature;
import org.jboss.bpmn2.editor.core.features.participant.DirectEditParticipantFeature;
import org.jboss.bpmn2.editor.core.features.participant.LayoutParticipantFeature;
import org.jboss.bpmn2.editor.core.features.participant.MoveParticipantFeature;
import org.jboss.bpmn2.editor.core.features.participant.ParticipantMultiplicityUpdateFeature;
import org.jboss.bpmn2.editor.core.features.participant.ResizeParticipantFeature;
import org.jboss.bpmn2.editor.core.features.participant.UpdateParticipantFeature;
import org.jboss.bpmn2.editor.ui.features.AbstractDefaultDeleteFeature;

public class ParticipantFeatureResolver implements FeatureResolver {

	@Override
	public List<ICreateConnectionFeature> getCreateConnectionFeatures(IFeatureProvider fp) {
		return new ArrayList<ICreateConnectionFeature>();
	}

	@Override
	public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {
		List<ICreateFeature> list = new ArrayList<ICreateFeature>();
		list.add(new CreateParticipantFeature(fp));
		return list;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Participant) {
			return new AddParticipantFeature(fp);
		}
		return null;
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Participant) {
			return new DirectEditParticipantFeature(fp);
		}
		return null;
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Participant) {
			return new LayoutParticipantFeature(fp);
		}
		return null;
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Participant) {
			MultiUpdateFeature multiUpdate = new MultiUpdateFeature(fp);
			multiUpdate.addUpdateFeature(new UpdateParticipantFeature(fp));
			multiUpdate.addUpdateFeature(new ParticipantMultiplicityUpdateFeature(fp));
			return multiUpdate;
		}
		return null;
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Participant) {
			return new MoveParticipantFeature(fp);
		}
		return null;
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Participant) {
			return new ResizeParticipantFeature(fp);
		}
		return null;
	}

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Participant) {
			return new AbstractDefaultDeleteFeature(fp);
		}
		return null;
	}
}