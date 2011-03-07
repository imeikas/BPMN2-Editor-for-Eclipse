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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.widgets.Composite;
import org.jboss.bpmn2.editor.ui.property.AbstractBpmn2PropertiesComposite;

public class NameDetailsComposite extends AbstractBpmn2PropertiesComposite {

	public NameDetailsComposite(Composite composite, int none) {
		super(composite, none);
	}

	@Override
	public void createBindings() {
		EList<EAttribute> eAllAttributes = be.eClass().getEAllAttributes();
		for (EAttribute a : eAllAttributes) {
			if ("name".equals(a.getName())) {
				bind(a, createTextInput("Name", false));
			}
		}
	}

}