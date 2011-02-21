package org.jboss.bpmn2.editor.ui.property.iospecification;

import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public abstract class NamedElementComposite extends AbstractPropertyComposite {
	protected final Button btnAddNew;
	private SelectionListener listener;

	public NamedElementComposite(Composite parent, int style) {
		super(parent, style);

		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);

		Label lblDataInputs = new Label(this, SWT.NONE);
		toolkit.adapt(lblDataInputs, true, true);
		lblDataInputs.setText(getLabelText());

		btnAddNew = new Button(this, SWT.NONE);
		btnAddNew.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));
		toolkit.adapt(btnAddNew, true, true);
		btnAddNew.setText(getNewButtonText());

		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_label.verticalIndent = 3;
		label.setLayoutData(gd_label);
		toolkit.adapt(label, true, true);

	}

	protected void createNameDetailsComposite(final List<BaseElement> list, final BaseElement value) {
		final NameDetailsComposite c = new NameDetailsComposite(this, SWT.NONE);
		c.setBaseElement(bpmn2Editor, value);
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		toolkit.adapt(c);
		toolkit.paintBordersFor(c);
		widgets.add(c);
		final Button button = new Button(this, SWT.NONE);
		button.setText("Remove");
		button.addSelectionListener(new SelectionListener() {

			@SuppressWarnings("restriction")
			@Override
			public void widgetSelected(SelectionEvent e) {
				TransactionalEditingDomain domain = bpmn2Editor.getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						list.remove(value);
					}
				});
				c.dispose();
				button.dispose();
				relayout();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		toolkit.adapt(button, true, true);
		widgets.add(button);
	}

	public void setContent(final List<BaseElement> list) {
		cleanWidgets();

		if (list == null) {
			return;
		}

		if (listener != null) {
			btnAddNew.removeSelectionListener(listener);
		}
		listener = new SelectionListener() {

			@SuppressWarnings("restriction")
			@Override
			public void widgetSelected(SelectionEvent e) {
				final BaseElement dataOutput = getNewElement();
				TransactionalEditingDomain domain = bpmn2Editor.getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						list.add(dataOutput);
					}
				});
				createNameDetailsComposite(list, dataOutput);
				relayout();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		};
		btnAddNew.addSelectionListener(listener);

		for (BaseElement value : list) {
			createNameDetailsComposite(list, value);
		}
		relayout();
	}

	public abstract BaseElement getNewElement();

	public abstract String getNewButtonText();

	public abstract String getLabelText();

}
