package org.jboss.bpmn2.editor.ui.property;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.CallableElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jboss.bpmn2.editor.core.Bpmn2Preferences;

public class AdvancedPropertiesComposite extends AbstractBpmn2PropertiesComposite {

	private IOSpecificationComposite ioSpecificationComposite;
	private Group ioGroup;

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

		if ((be instanceof Activity || be instanceof CallableElement)
				&& Bpmn2Preferences.getPreferences(project).isEnabled(be.eClass().getName() + ".ioSpecification")) {
			if (ioSpecificationComposite == null) {
				ioGroup = new Group(this, SWT.NONE);
				ioGroup.setText("IO Specification");
				ioGroup.setLayout(new FillLayout());
				toolkit.adapt(ioGroup);
				toolkit.paintBordersFor(ioGroup);
				ioSpecificationComposite = new IOSpecificationComposite(ioGroup, SWT.NONE);
			}
			if (!ioGroup.isVisible()) {
				ioGroup.setVisible(true);
			}
			ioSpecificationComposite.setBaseElement(bpmn2Editor, be);
		}
	}
}
