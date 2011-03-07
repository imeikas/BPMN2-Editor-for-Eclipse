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
package org.jboss.bpmn2.editor.ui.property.iospecification;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.InputSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.widgets.Composite;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class InputSetsComposite extends AbstractSetsComposite {
	public InputSetsComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public String getNewButtonText() {
		return "Add Input Set";
	}

	@Override
	public String getMainLabelText() {
		return "Input Sets";
	}

	@Override
	public String getLabelText() {
		return "Data Inputs";
	}

	@Override
	public String getReferenceName() {
		return "dataInputRefs";
	}

	@Override
	public Class<? extends BaseElement> getElementClass() {
		return DataInput.class;
	}

	@Override
	public BaseElement getNewElement() {
		InputSet inputSet = ModelHandler.FACTORY.createInputSet();
		inputSet.setId(EcoreUtil.generateUUID());
		return inputSet;
	}
}
