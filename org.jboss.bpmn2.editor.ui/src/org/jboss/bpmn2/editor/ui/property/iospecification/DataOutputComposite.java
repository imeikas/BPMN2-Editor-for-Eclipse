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
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.widgets.Composite;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class DataOutputComposite extends NamedElementComposite {

	public DataOutputComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public BaseElement getNewElement() {
		DataOutput dataOutput = ModelHandler.FACTORY.createDataOutput();
		dataOutput.setId(EcoreUtil.generateUUID());
		return dataOutput;
	}

	@Override
	public String getNewButtonText() {
		return "Add Data Output";
	}

	@Override
	public String getLabelText() {
		return "Data Out";
	}

}
