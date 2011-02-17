package org.jboss.bpmn2.editor.ui.property;

import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EReference;
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
import org.jboss.bpmn2.editor.core.ModelHandler;

public class IOSpecificationComposite extends AbstractBpmn2PropertiesComposite {

	private final DataInputsComposite dataInputs;
	private final Button btnAddIoSpecification;
	private InputOutputSpecification spec;
	private SelectionListener ioSpecListener;

	public IOSpecificationComposite(Composite parent, int style) {
		super(parent, style);
		toolkit.adapt(this);
		setLayout(new GridLayout(1, false));

		btnAddIoSpecification = new Button(this, SWT.NONE);
		btnAddIoSpecification.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		btnAddIoSpecification.setText("Add IO Specification");
		dataInputs = new DataInputsComposite(this, SWT.None);
		dataInputs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	@Override
	public void createBindings() {
		dataInputs.setDiagramEditor(bpmn2Editor);
		EList<EReference> mainContainments = be.eClass().getEAllContainments();
		for (final EReference mRef : mainContainments) {
			if ("ioSpecification".equals(mRef.getName())) {
				spec = (InputOutputSpecification) be.eGet(mRef);

				if (spec == null) {
					showAddIOSpecificationButton(mRef);
				} else {
					createIOSpecBindings();
				}
			}
		}
	}

	private void showAddIOSpecificationButton(final EReference mRef) {
		dataInputs.setInputs(null);
		dataInputs.setVisible(false);
		btnAddIoSpecification.setVisible(true);
		relayout();
		if (ioSpecListener != null) {
			btnAddIoSpecification.removeSelectionListener(ioSpecListener);
		}

		ioSpecListener = new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				spec = ModelHandler.FACTORY.createInputOutputSpecification();
				TransactionalEditingDomain domain = bpmn2Editor.getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						be.eSet(mRef, spec);
					}
				});
				createIOSpecBindings();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		};
		btnAddIoSpecification.addSelectionListener(ioSpecListener);
	}

	private void createIOSpecBindings() {
		btnAddIoSpecification.setVisible(false);
		dataInputs.setVisible(true);

		EList<EReference> specContainments = spec.eClass().getEAllContainments();
		for (EReference sRef : specContainments) {
			if ("dataInputs".equals(sRef.getName())) {
				dataInputs.setDiagramEditor(bpmn2Editor);
				dataInputs.setInputs((EObjectContainmentEList<DataInput>) spec.eGet(sRef));
				toolkit.adapt(dataInputs);
			}
		}
		relayout();
	}

	private void relayout() {
		GridData gd = (GridData) dataInputs.getLayoutData();
		gd.exclude = !dataInputs.isVisible();

		gd = (GridData) btnAddIoSpecification.getLayoutData();
		gd.exclude = !btnAddIoSpecification.isVisible();

		Composite p = parent;
		while (!(p instanceof AdvancedPropertiesComposite)) {
			p = p.getParent();
		}
		p.getParent().getParent().layout(true, true);
	}
}
