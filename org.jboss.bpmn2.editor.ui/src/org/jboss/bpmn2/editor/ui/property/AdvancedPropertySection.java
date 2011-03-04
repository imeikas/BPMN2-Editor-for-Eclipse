package org.jboss.bpmn2.editor.ui.property;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.bpmn2.editor.ui.editor.BPMN2Editor;

public class AdvancedPropertySection extends GFPropertySection implements ITabbedPropertyConstants {

	private ImprovedAdvancedPropertiesComposite composite;

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		parent.setLayout(new FillLayout());

		composite = new ImprovedAdvancedPropertiesComposite(parent, SWT.BORDER);
		composite.setSheetPage(aTabbedPropertySheetPage);
	}

	@Override
	public void refresh() {
		PictogramElement pe = getSelectedPictogramElement();
		if (pe != null) {
			BaseElement be = (BaseElement) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
			composite.setBaseElement((BPMN2Editor) getDiagramEditor(), be);
		}
	}

	@Override
	public boolean shouldUseExtraSpace() {
		return true;
	}
}
