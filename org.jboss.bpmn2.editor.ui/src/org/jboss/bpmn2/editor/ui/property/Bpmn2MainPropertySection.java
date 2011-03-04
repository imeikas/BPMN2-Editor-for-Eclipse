package org.jboss.bpmn2.editor.ui.property;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
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
			composite.setEObject((BPMN2Editor) getDiagramEditor(), be);
		}
	}
}
