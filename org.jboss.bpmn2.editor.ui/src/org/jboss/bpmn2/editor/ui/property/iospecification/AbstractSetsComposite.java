package org.jboss.bpmn2.editor.ui.property.iospecification;

import java.util.Arrays;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.ui.celleditor.FeatureEditorDialog;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.bpmn2.editor.ui.property.AbstractBpmn2PropertiesComposite;

public abstract class AbstractSetsComposite extends AbstractPropertyComposite {
	private SelectionListener editListener;
	private List<BaseElement> list;
	private final Button btnAddInputSet;
	private SelectionListener listener;

	private class SetsDetailsComposite extends AbstractBpmn2PropertiesComposite {

		private final Label lblInputSets;
		private final Text text;
		private final Button editButton;

		public SetsDetailsComposite(Composite parent, int style) {
			super(parent, style);
			GridLayout gridLayout = new GridLayout(3, false);
			gridLayout.marginHeight = 2;
			gridLayout.marginWidth = 3;
			gridLayout.verticalSpacing = 5;
			gridLayout.horizontalSpacing = 5;
			setLayout(gridLayout);
			toolkit.adapt(this);
			lblInputSets = new Label(this, SWT.NONE);
			lblInputSets.setText(getLabelText());
			toolkit.adapt(lblInputSets, false, false);

			text = new Text(this, SWT.NONE);
			text.setEditable(false);
			text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			toolkit.adapt(text, false, false);

			editButton = new Button(this, SWT.NONE);
			editButton.setText("Edit ...");
			toolkit.adapt(editButton, true, true);

		}

		private final AdapterFactoryLabelProvider LABEL_PROVIDER = new AdapterFactoryLabelProvider(ADAPTER_FACTORY);

		@Override
		public void createBindings() {
			for (EReference r : be.eClass().getEAllReferences()) {
				if (getReferenceName().equals(r.getName())) {
					bindReference(r);
				}
			}
		}

		public void bindReference(final EReference reference) {
			final List<BaseElement> refs = (List<BaseElement>) be.eGet(reference);
			updateTextField(refs);

			if (editListener != null) {
				editButton.removeSelectionListener(editListener);
			}

			editListener = new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					List<Object> l = null;

					if (modelHandler != null) {
						l = Arrays.asList(modelHandler.getAll(getElementClass()));
					}

					FeatureEditorDialog featureEditorDialog = new FeatureEditorDialog(getShell(), LABEL_PROVIDER, be,
							reference, "Select elements", l);
					featureEditorDialog.open();

					final EList<BaseElement> result = (EList<BaseElement>) featureEditorDialog.getResult();

					updateBaseElement(refs, result);
					updateTextField(refs);
				}

				public void updateBaseElement(final List<BaseElement> refs, final EList<BaseElement> result) {
					TransactionalEditingDomain domain = bpmn2Editor.getEditingDomain();
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {

							refs.retainAll(result);
							if (result == null) {
								return;
							}
							for (BaseElement di : result) {
								if (!refs.contains(di)) {
									refs.add(di);
								}
							}
						}
					});
				}
			};
			editButton.addSelectionListener(editListener);
		}

		private void updateTextField(final List<BaseElement> refs) {
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
	public AbstractSetsComposite(Composite parent, int style) {
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
		label.setText(getMainLabelText());

		btnAddInputSet = new Button(this, SWT.NONE);
		btnAddInputSet.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));
		toolkit.adapt(btnAddInputSet, true, true);
		btnAddInputSet.setText(getNewButtonText());

		Label label_1 = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_label_1.verticalIndent = 3;
		label_1.setLayoutData(gd_label_1);
		toolkit.adapt(label_1, true, true);
	}

	public void setSets(final List<BaseElement> list) {
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
				final BaseElement newElem = getNewElement();
				TransactionalEditingDomain domain = bpmn2Editor.getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						list.add(newElem);
					}
				});
				createInputSetComposite(newElem);
				relayout();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		};
		btnAddInputSet.addSelectionListener(listener);

		for (BaseElement value : list) {
			createInputSetComposite(value);
		}

		relayout();

	}

	private void createInputSetComposite(final BaseElement value) {
		final SetsDetailsComposite c = new SetsDetailsComposite(this, SWT.NONE);
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

	public abstract BaseElement getNewElement();

	public abstract String getNewButtonText();

	public abstract String getMainLabelText();

	public abstract String getLabelText();

	public abstract String getReferenceName();

	public abstract Class<? extends BaseElement> getElementClass();

}
