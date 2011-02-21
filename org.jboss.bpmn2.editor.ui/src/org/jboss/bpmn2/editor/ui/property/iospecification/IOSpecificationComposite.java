package org.jboss.bpmn2.editor.ui.property.iospecification;

import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.bpmn2.InputSet;
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
import org.jboss.bpmn2.editor.ui.property.AbstractBpmn2PropertiesComposite;
import org.jboss.bpmn2.editor.ui.property.AdvancedPropertiesComposite;

import com.swtdesigner.SWTResourceManager;

public class IOSpecificationComposite extends AbstractBpmn2PropertiesComposite {

	private final DataInputsComposite dataInputs;
	private final DataOutputComposite dataOutputs;
	private final Button btnAddIoSpecification;
	private InputOutputSpecification spec;
	private SelectionListener ioSpecListener;

	private final InputSetsComposite inputSets;

	public IOSpecificationComposite(Composite parent, int style) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
		GridLayout gridLayout = (GridLayout) getLayout();
		gridLayout.numColumns = 3;
		toolkit.adapt(this);
		setLayout(new GridLayout(3, false));

		btnAddIoSpecification = new Button(this, SWT.NONE);
		btnAddIoSpecification.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
		btnAddIoSpecification.setText("Add IO Specification");
		dataInputs = new DataInputsComposite(this, SWT.None);
		dataInputs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		dataOutputs = new DataOutputComposite(this, SWT.None);
		dataOutputs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		inputSets = new InputSetsComposite(this, SWT.None);
		inputSets.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
	}

	@Override
	public void createBindings() {
		dataInputs.setDiagramEditor(bpmn2Editor);
		dataOutputs.setDiagramEditor(bpmn2Editor);
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
		dataOutputs.setOutputs(null);
		dataInputs.setVisible(false);
		dataOutputs.setVisible(false);

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

	@SuppressWarnings("unchecked")
	private void createIOSpecBindings() {
		btnAddIoSpecification.setVisible(false);
		dataInputs.setVisible(true);
		dataOutputs.setVisible(true);

		EList<EReference> specContainments = spec.eClass().getEAllContainments();
		for (EReference sRef : specContainments) {
			if ("dataInputs".equals(sRef.getName())) {
				dataInputs.setDiagramEditor(bpmn2Editor);
				dataInputs.setInputs((EObjectContainmentEList<DataInput>) spec.eGet(sRef));
				toolkit.adapt(dataInputs);
			} else if ("dataOutputs".equals(sRef.getName())) {
				dataOutputs.setDiagramEditor(bpmn2Editor);
				dataOutputs.setOutputs((EObjectContainmentEList<DataOutput>) spec.eGet(sRef));
				toolkit.adapt(dataOutputs);
			} else if ("inputSets".equals(sRef.getName())) {
				EObjectContainmentEList<InputSet> sets = (EObjectContainmentEList<InputSet>) spec.eGet(sRef);
				inputSets.setDiagramEditor(bpmn2Editor);
				inputSets.setSets(sets);
				// if (sets.size() > 0) {
				// final InputSetImpl set = (InputSetImpl) sets.get(0);
				// inputSets.setInputSet(set);
				// }

			} else if ("outputSets".equals(sRef.getName())) {

			}
		}
		relayout();
	}

	private void relayout() {
		GridData gd = (GridData) dataInputs.getLayoutData();
		gd.exclude = !dataInputs.isVisible();

		gd = (GridData) dataOutputs.getLayoutData();
		gd.exclude = !dataOutputs.isVisible();

		gd = (GridData) btnAddIoSpecification.getLayoutData();
		gd.exclude = !btnAddIoSpecification.isVisible();

		Composite p = parent;
		while (!(p instanceof AdvancedPropertiesComposite)) {
			p = p.getParent();
		}
		((AdvancedPropertiesComposite) p).relayout();
	}

}
