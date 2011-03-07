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

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.ui.Activator;
import org.jboss.bpmn2.editor.ui.editor.BPMN2Editor;
import org.jboss.bpmn2.editor.ui.property.AdvancedPropertiesComposite;

public class AbstractPropertyComposite extends Composite {

	protected final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	protected final ArrayList<Widget> widgets = new ArrayList<Widget>();
	protected BPMN2Editor bpmn2Editor;
	protected ModelHandler modelHandler;

	public AbstractPropertyComposite(Composite parent, int style) {
		super(parent, style);
	}

	protected void relayout() {
		Composite p = this;
		while (!(p instanceof AdvancedPropertiesComposite)) {
			p = p.getParent();
		}
		((AdvancedPropertiesComposite) p).relayout();
	}

	protected void cleanWidgets() {
		for (Widget w : widgets) {
			w.dispose();
		}
		widgets.clear();
	}

	public void setDiagramEditor(BPMN2Editor bpmn2Editor) {
		this.bpmn2Editor = bpmn2Editor;
		try {
			modelHandler = ModelHandlerLocator.getModelHandler(bpmn2Editor.getDiagramTypeProvider().getDiagram()
					.eResource());
		} catch (IOException e) {
			Activator.showErrorWithLogging(e);
		}
	}
}