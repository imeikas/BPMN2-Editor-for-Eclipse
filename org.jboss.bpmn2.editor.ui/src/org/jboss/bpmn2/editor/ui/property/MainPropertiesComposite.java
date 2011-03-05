package org.jboss.bpmn2.editor.ui.property;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.ui.celleditor.FeatureEditorDialog;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.jboss.bpmn2.editor.core.Bpmn2Preferences;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.ui.Activator;

public class MainPropertiesComposite extends AbstractBpmn2PropertiesComposite {
	private final AdapterFactoryLabelProvider LABEL_PROVIDER = new AdapterFactoryLabelProvider(ADAPTER_FACTORY);
	private ModelHandler modelHandler;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public MainPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}

	@SuppressWarnings("restriction")
	@Override
	public void createBindings() {
		try {
			modelHandler = ModelHandlerLocator.getModelHandler(bpmn2Editor.getDiagramTypeProvider().getDiagram()
					.eResource());
		} catch (IOException e1) {
			Activator.showErrorWithLogging(e1);
			return;
		}

		EList<EAttribute> eAllAttributes = be.eClass().getEAllAttributes();
		Bpmn2Preferences preferences = Bpmn2Preferences.getPreferences(project);

		for (EAttribute a : eAllAttributes) {
			if (preferences.isEnabled(be.eClass(), a)) {
				if (String.class.equals(a.getEType().getInstanceClass())) {
					Text t = createTextInput(a.getName());
					if ("id".equals(a.getName())) {
						t.setEditable(false);
					}
					Binding bind = bind(a, t);
					bindings.add(bind);
				} else if (boolean.class.equals(a.getEType().getInstanceClass())) {
					Button t = createBooleanInput(a.getName());
					Binding bind = bindBoolean(a, t);
					bindings.add(bind);
				} else if (int.class.equals(a.getEType().getInstanceClass())) {
					Text t = createIntInput(a.getName());
					Binding bind = bindInt(a, t);
					bindings.add(bind);
				}
			}
		}

		EList<EReference> eAllContainments = be.eClass().getEAllContainments();
		for (EReference e : be.eClass().getEAllReferences()) {
			if (preferences.isEnabled(be.eClass(), e) && !eAllContainments.contains(e)) {
				bindReference(e);
			}
		}
	}

	public void bindReference(final EReference reference) {
		Object eGet = be.eGet(reference);
		if (!(eGet instanceof List)) {
			// FIXME: Create single value editor
			return;
		}
		createLabel(reference.getName());

		final Text text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolkit.adapt(text, true, true);
		widgets.add(text);

		Button editButton = new Button(this, SWT.BORDER);
		editButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		editButton.setText("Edit ...");
		toolkit.adapt(editButton, true, true);
		widgets.add(editButton);

		final List<EObject> refs = (List<EObject>) eGet;
		updateTextField(refs, text);

		SelectionAdapter editListener = new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				List<EObject> l = null;

				if (modelHandler != null) {
					l = (List<EObject>) modelHandler.getAll(reference.getEType().getInstanceClass());
				}

				FeatureEditorDialog featureEditorDialog = new FeatureEditorDialog(getShell(), LABEL_PROVIDER, be,
						reference, "Select elements", l);
				featureEditorDialog.open();

				final EList<EObject> result = (EList<EObject>) featureEditorDialog.getResult();

				updateEObject(refs, result);
				updateTextField(refs, text);
			}

			public void updateEObject(final List<EObject> refs, final EList<EObject> result) {
				TransactionalEditingDomain domain = bpmn2Editor.getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {

						refs.retainAll(result);
						if (result == null) {
							return;
						}
						for (EObject di : result) {
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

	private void updateTextField(final List<EObject> refs, Text text) {
		String listText = "";
		if (refs != null) {
			for (int i = 0; i < refs.size() - 1; i++) {
				listText += LABEL_PROVIDER.getText(refs.get(i)) + ", ";
			}
			if (refs.size() > 0) {
				listText += LABEL_PROVIDER.getText(refs.get(refs.size() - 1));
			}
		}

		text.setText(listText);
	}
}
