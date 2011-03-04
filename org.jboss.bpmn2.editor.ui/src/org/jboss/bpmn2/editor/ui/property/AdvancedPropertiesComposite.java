package org.jboss.bpmn2.editor.ui.property;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.CallableElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.bpmn2.editor.core.Bpmn2Preferences;
import org.jboss.bpmn2.editor.ui.property.iospecification.IOSpecificationComposite;

public class AdvancedPropertiesComposite extends AbstractBpmn2PropertiesComposite {

	private IOSpecificationComposite ioSpecificationComposite;
	private Group ioGroup;
	private TabbedPropertySheetPage sheetPage;

	public AdvancedPropertiesComposite(Composite parent, int style) {
		super(parent, style);
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		fillLayout.spacing = 3;
		fillLayout.marginHeight = 3;
		fillLayout.marginWidth = 3;
		setLayout(fillLayout);
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
	}

	@Override
	public void createBindings() {
		if (ioGroup != null) {
			ioGroup.setVisible(false);
		}

		boolean ioEnabled = Bpmn2Preferences.getPreferences(project).isEnabled(
				be.eClass().getName() + ".ioSpecification");
		if ((be instanceof Activity || be instanceof CallableElement) && ioEnabled) {
			if (ioSpecificationComposite == null) {
				ioGroup = new Group(this, SWT.NONE);
				ioGroup.setText("IO Specification");
				ioGroup.setLayout(new FillLayout());

				toolkit.adapt(ioGroup);
				toolkit.paintBordersFor(ioGroup);

				ioSpecificationComposite = new IOSpecificationComposite(ioGroup, SWT.NONE);
			}
			ioGroup.setVisible(true);
			ioSpecificationComposite.setEObject(bpmn2Editor, be);
		}
	}

	public void setSheetPage(TabbedPropertySheetPage sheetPage) {
		this.sheetPage = sheetPage;
	}

	public void relayout() {
		parent.getParent().layout(true, true);
		sheetPage.resizeScrolledComposite();
	}
}
