package org.jboss.bpmn2.editor.ui.property.iospecification;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.InputSet;
import org.eclipse.bpmn2.impl.InputSetImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.edit.ui.celleditor.FeatureEditorDialog;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.ui.editor.BPMN2Editor;
import org.jboss.bpmn2.editor.ui.property.AbstractBpmn2PropertiesComposite;
import org.jboss.bpmn2.editor.ui.property.AdvancedPropertiesComposite;

public class InputSetsComposite extends Composite {
	protected final FormToolkit toolkit = new FormToolkit(Display.getCurrent());

	private SelectionListener inSetListener;
	private EObjectContainmentEList<InputSet> list;
	private final ArrayList<Widget> widgets = new ArrayList<Widget>();

	private final Button btnAddInputSet;
	private BPMN2Editor bpmn2Editor;

	private SelectionListener listener;

	private class InputSetsDetailsComposite extends AbstractBpmn2PropertiesComposite {

		private final Label lblInputSets;
		private final Text text;
		private final Button inputsetsButton;

		public InputSetsDetailsComposite(Composite parent, int style) {
			super(parent, style);
			setLayout(new GridLayout(3, false));
			toolkit.adapt(this);
			lblInputSets = new Label(this, SWT.NONE);
			lblInputSets.setText("Input Sets");
			toolkit.adapt(lblInputSets, false, false);

			text = new Text(this, SWT.NONE);
			text.setEditable(false);
			text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			toolkit.adapt(text, false, false);

			inputsetsButton = new Button(this, SWT.NONE);
			inputsetsButton.setText("...");
			toolkit.adapt(inputsetsButton, true, true);

		}

		private final AdapterFactoryLabelProvider LABEL_PROVIDER = new AdapterFactoryLabelProvider(ADAPTER_FACTORY);

		@Override
		public void createBindings() {
			for (EReference a : be.eClass().getEAllReferences()) {
				if ("dataInputRefs".equals(a.getName())) {
					final EReference x = a;
					final List<DataInput> refs = ((InputSetImpl) be).getDataInputRefs();
					updateTextField(refs);
					if (inSetListener != null) {
						inputsetsButton.removeSelectionListener(inSetListener);
					}
					inSetListener = new SelectionListener() {

						@Override
						public void widgetSelected(SelectionEvent e) {

							FeatureEditorDialog featureEditorDialog = new FeatureEditorDialog(getShell(),
									LABEL_PROVIDER, be, x, "x", refs);
							featureEditorDialog.open();
							final EList<DataInput> result = (EList<DataInput>) featureEditorDialog.getResult();
							TransactionalEditingDomain domain = bpmn2Editor.getEditingDomain();
							domain.getCommandStack().execute(new RecordingCommand(domain) {
								@Override
								protected void doExecute() {

									refs.retainAll(result);
									if (result == null) {
										return;
									}
									for (DataInput di : result) {
										if (!refs.contains(di)) {
											refs.add(di);
										}
									}
								}
							});
							updateTextField(refs);
						}

						@Override
						public void widgetDefaultSelected(SelectionEvent e) {

						}
					};
					inputsetsButton.addSelectionListener(inSetListener);

				}
			}
		}

		private void updateTextField(final List<DataInput> refs) {
			String listText = "";
			for (int i = 0; i < refs.size() - 1; i++) {
				listText += LABEL_PROVIDER.getText(refs.get(i)) + ", ";
			}
			if (refs.size() > 0) {
				listText += LABEL_PROVIDER.getText(refs.get(refs.size() - 1));
			}

			text.setText(listText);
		}
	}

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public InputSetsComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);

		Label label = new Label(this, SWT.NONE);
		toolkit.adapt(label, true, true);
		label.setText("Input Sets");

		btnAddInputSet = new Button(this, SWT.NONE);
		btnAddInputSet.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));
		toolkit.adapt(btnAddInputSet, true, true);
		btnAddInputSet.setText("Add Input Set");

		Label label_1 = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_label_1.verticalIndent = 3;
		label_1.setLayoutData(gd_label_1);
		toolkit.adapt(label_1, true, true);

	}

	public void setSets(final EObjectContainmentEList<InputSet> list) {
		this.list = list;
		cleanWidgets();

		if (list == null) {
			return;
		}

		if (listener != null) {
			btnAddInputSet.removeSelectionListener(listener);
		}
		listener = new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				final InputSet is = ModelHandler.FACTORY.createInputSet();
				TransactionalEditingDomain domain = bpmn2Editor.getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						list.add(is);
					}
				});
				createInputSetComposite(is);
				relayout();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		};
		btnAddInputSet.addSelectionListener(listener);

		for (InputSet value : list) {
			createInputSetComposite(value);
		}

		relayout();

	}

	private void createInputSetComposite(final InputSet value) {
		final InputSetsDetailsComposite c = new InputSetsDetailsComposite(this, SWT.NONE);
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

	private void relayout() {
		Composite p = this;
		while (!(p instanceof AdvancedPropertiesComposite)) {
			p = p.getParent();
		}
		((AdvancedPropertiesComposite) p).relayout();
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
