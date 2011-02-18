package org.jboss.bpmn2.editor.ui.property;

import java.util.ArrayList;

import org.eclipse.bpmn2.DataInput;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.ui.editor.BPMN2Editor;

public class DataInputsComposite extends Composite {
	protected final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private EObjectContainmentEList<DataInput> list;

	private final ArrayList<Widget> widgets = new ArrayList<Widget>();
	private BPMN2Editor bpmn2Editor;
	private final Button btnAddNewInput;
	private SelectionListener listener;

	public static class DataInputDetailsComposite extends AbstractBpmn2PropertiesComposite {

		public DataInputDetailsComposite(DataInputsComposite dataInputsComposite, int none) {
			super(dataInputsComposite, none);
		}

		@Override
		public void createBindings() {
			DataInput di = (DataInput) be;
			EList<EAttribute> eAllAttributes = di.eClass().getEAllAttributes();
			for (EAttribute a : eAllAttributes) {
				if ("name".equals(a.getName())) {
					bind(a, createTextInput("Input Name"));
				}
			}
		}

	}

	public DataInputsComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);

		Label lblDataInputs = new Label(this, SWT.NONE);
		toolkit.adapt(lblDataInputs, true, true);
		lblDataInputs.setText("Data Inputs");

		btnAddNewInput = new Button(this, SWT.NONE);
		btnAddNewInput.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));
		toolkit.adapt(btnAddNewInput, true, true);
		btnAddNewInput.setText("Add New Input");

		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_label.verticalIndent = 3;
		label.setLayoutData(gd_label);
		toolkit.adapt(label, true, true);

	}

	public void setInputs(final EObjectContainmentEList<DataInput> list) {
		this.list = list;
		cleanWidgets();

		if (list == null) {
			return;
		}

		if (listener != null) {
			btnAddNewInput.removeSelectionListener(listener);
		}
		listener = new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				final DataInput di = ModelHandler.FACTORY.createDataInput();
				TransactionalEditingDomain domain = bpmn2Editor.getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						list.add(di);
					}
				});
				createDataInputComposite(di);
				relayout();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		};
		btnAddNewInput.addSelectionListener(listener);

		for (DataInput value : list) {
			createDataInputComposite(value);
		}
		relayout();
	}

	private void relayout() {
		Composite p = this;
		while (!(p instanceof AdvancedPropertiesComposite)) {
			p = p.getParent();
		}
		p.getParent().getParent().layout(true, true);
	}

	private void createDataInputComposite(final DataInput value) {
		final DataInputDetailsComposite c = new DataInputDetailsComposite(this, SWT.NONE);
		c.setBaseElement(bpmn2Editor, value);
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		toolkit.adapt(c);
		toolkit.paintBordersFor(c);
		widgets.add(c);
		final Button button = new Button(this, SWT.NONE);
		button.setText("Remove");
		button.addSelectionListener(new SelectionListener() {

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

	private void cleanWidgets() {
		for (Widget w : widgets) {
			w.dispose();
		}
		widgets.clear();
	}

	public void setDiagramEditor(BPMN2Editor bpmn2Editor) {
		this.bpmn2Editor = bpmn2Editor;

	}

}
