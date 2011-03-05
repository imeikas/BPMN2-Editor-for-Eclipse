package org.jboss.bpmn2.editor.ui.property;

import java.io.IOException;

import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.emf.ecore.EObject;
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

public class JbpmPropertySection extends GFPropertySection implements ITabbedPropertyConstants {
	public JbpmPropertySection() {
	}

	private JbpmPropertiesComposite composite;
	private TabbedPropertySheetPage aTabbedPropertySheetPage;

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		this.aTabbedPropertySheetPage = aTabbedPropertySheetPage;
		super.createControls(parent, aTabbedPropertySheetPage);
		parent.setLayout(new FillLayout());
		composite = new JbpmPropertiesComposite(parent, SWT.None);
	}

	@Override
	public void refresh() {
		PictogramElement pe = getSelectedPictogramElement();
		if (pe != null) {
			EObject be = (EObject) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
			if (be instanceof Participant){
				composite.setEObject((BPMN2Editor) getDiagramEditor(), ((Participant) be).getProcessRef());	
			}else if (be instanceof BPMNDiagram){
				try {
					composite.setEObject((BPMN2Editor) getDiagramEditor(), ModelHandlerLocator.getModelHandler(be.eResource()).getInternalParticipant().getProcessRef());
				} catch (IOException e) {
					Activator.showErrorWithLogging(e);
				}
			}else 
			composite.setEObject((BPMN2Editor) getDiagramEditor(), be);
			aTabbedPropertySheetPage.resizeScrolledComposite();
		}
		

	}
}
