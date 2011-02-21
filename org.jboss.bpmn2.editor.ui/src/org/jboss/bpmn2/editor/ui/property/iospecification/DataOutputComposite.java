package org.jboss.bpmn2.editor.ui.property.iospecification;

import java.util.ArrayList;

import org.eclipse.bpmn2.DataOutput;
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
import org.jboss.bpmn2.editor.ui.property.AbstractBpmn2PropertiesComposite;
import org.jboss.bpmn2.editor.ui.property.AdvancedPropertiesComposite;

public class DataOutputComposite extends Composite {
	protected final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private EObjectContainmentEList<DataOutput> list;

	private final ArrayList<Widget> widgets = new ArrayList<Widget>();
	private BPMN2Editor bpmn2Editor;
	private final Button btnAddNewOutput;
	private SelectionListener listener;

	public static class DataOutputDetailsComposite extends AbstractBpmn2PropertiesComposite {

		public DataOutputDetailsComposite(DataOutputComposite dataOutputsComposite, int none) {
			super(dataOutputsComposite, none);
		}

		@Override
		public void createBindings() {
			DataOutput di = (DataOutput) be;
			EList<EAttribute> eAllAttributes = di.eClass().getEAllAttributes();
			for (EAttribute a : eAllAttributes) {
				if ("name".equals(a.getName())) {
					bind(a, createTextInput("Output Name"));
				}
			}
		}

	}

	public DataOutputComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);

		Label lblDataOutputs = new Label(this, SWT.NONE);
		toolkit.adapt(lblDataOutputs, true, true);
		lblDataOutputs.setText("Data Outputs");

		btnAddNewOutput = new Button(this, SWT.NONE);
		btnAddNewOutput.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));
		toolkit.adapt(btnAddNewOutput, true, true);
		btnAddNewOutput.setText("Add New Output");

		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_label.verticalIndent = 3;
		label.setLayoutData(gd_label);
		toolkit.adapt(label, true, true);

	}

	public void setOutputs(final EObjectContainmentEList<DataOutput> list) {
		this.list = list;
		cleanWidgets();

		if (list == null) {
			return;
		}

		if (listener != null) {
			btnAddNewOutput.removeSelectionListener(listener);
		}
		listener = new SelectionListener() {

			@SuppressWarnings("restriction")
			@Override
			public void widgetSelected(SelectionEvent e) {
				final DataOutput di = ModelHandler.FACTORY.createDataOutput();
				TransactionalEditingDomain domain = bpmn2Editor.getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						list.add(di);
					}
				});
				createDataOutputComposite(di);
				relayout();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		};
		btnAddNewOutput.addSelectionListener(listener);

		for (DataOutput value : list) {
			createDataOutputComposite(value);
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

	private void createDataOutputComposite(final DataOutput value) {
		final DataOutputDetailsComposite c = new DataOutputDetailsComposite(this, SWT.NONE);
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
