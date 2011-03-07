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
package org.jboss.bpmn2.editor.ui.property;

import java.io.IOException;

import org.eclipse.bpmn2.di.impl.BPMNDiagramImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.ui.Activator;
import org.jboss.bpmn2.editor.ui.editor.BPMN2Editor;

public class Bpmn2MainPropertySection extends GFPropertySection implements ITabbedPropertyConstants {

	private MainPropertiesComposite composite;

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		parent.setLayout(new FillLayout());
		composite = new MainPropertiesComposite(parent, SWT.None);
	}

	@Override
	public void refresh() {
		PictogramElement pe = getSelectedPictogramElement();
		if (pe != null) {
			EObject be = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
			if (be instanceof BPMNDiagramImpl) {
				try {
					Resource eResource = be.eResource();
					if (eResource != null) {
						composite.setEObject((BPMN2Editor) getDiagramEditor(),
						        ModelHandlerLocator.getModelHandler(eResource).getDefinitions());
					} else {
						composite.setEObject((BPMN2Editor) getDiagramEditor(), null);
					}
				} catch (IOException e) {
					Activator.showErrorWithLogging(e);
				}
			} else {
				composite.setEObject((BPMN2Editor) getDiagramEditor(), be);
			}
		}
	}
}
