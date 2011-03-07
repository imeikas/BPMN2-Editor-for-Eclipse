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
package org.jboss.bpmn2.editor.core.features.activity.task;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.jboss.bpmn2.editor.core.features.DirectEditFlowElementFeature;

public class DirectEditTaskFeature extends DirectEditFlowElementFeature {

	public DirectEditTaskFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String checkValueValid(String value, IDirectEditingContext context) {
		if (value.length() < 1) {
			return "Please enter any text as Task name.";
		} else if (value.contains("\n")) {
			return "Line breakes are not allowed in Task names.";
		}
		return null;
	}
}