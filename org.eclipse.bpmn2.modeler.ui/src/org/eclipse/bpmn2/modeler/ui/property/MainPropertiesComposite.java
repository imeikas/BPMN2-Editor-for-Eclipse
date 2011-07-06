/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.property;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.bpmn2.GatewayDirection;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiPackage;
import org.eclipse.bpmn2.modeler.core.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.ui.celleditor.FeatureEditorDialog;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class MainPropertiesComposite extends AbstractBpmn2PropertiesComposite {
	private final AdapterFactoryLabelProvider LABEL_PROVIDER = new AdapterFactoryLabelProvider(ADAPTER_FACTORY);
	private ModelHandler modelHandler;
	private BPMNShape shape;

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

		ItemProviderAdapter itemProviderAdapter = (ItemProviderAdapter) new Bpmn2ItemProviderAdapterFactory().adapt(be,
				ItemProviderAdapter.class);

		EList<EAttribute> eAllAttributes = be.eClass().getEAllAttributes();
		Bpmn2Preferences preferences = Bpmn2Preferences.getPreferences(project);

		for (EAttribute a : eAllAttributes) {

			if (preferences.isEnabled(be.eClass(), a)) {
				IItemPropertyDescriptor propertyDescriptor = itemProviderAdapter.getPropertyDescriptor(be, a);

				if (String.class.equals(a.getEType().getInstanceClass())) {
					bind(a, createTextInput(propertyDescriptor.getDisplayName(be), propertyDescriptor.isMultiLine(be)));
				} else if (boolean.class.equals(a.getEType().getInstanceClass())) {
					bindBoolean(a, createBooleanInput(propertyDescriptor.getDisplayName(be)));
				} else if (int.class.equals(a.getEType().getInstanceClass())) {
					bindInt(a, createIntInput(propertyDescriptor.getDisplayName(be)));
				} else if (propertyDescriptor != null) {
					propertyDescriptor.getChoiceOfValues(be);
					createLabel(propertyDescriptor.getDisplayName(be));
					createSingleItemEditor(a, be.eGet(a), propertyDescriptor.getChoiceOfValues(be));
				}
			}
		}

		EList<EReference> eAllContainments = be.eClass().getEAllContainments();
		for (EReference e : be.eClass().getEAllReferences()) {
			if (preferences.isEnabled(be.eClass(), e) && !eAllContainments.contains(e)) {
				IItemPropertyDescriptor propertyDescriptor = itemProviderAdapter.getPropertyDescriptor(be, e);
				bindReference(e, propertyDescriptor.getDisplayName(e));
			}
		}

		if (be instanceof Participant) {
			Diagram diagram = bpmn2Editor.getDiagramTypeProvider().getDiagram();
			if (shape != null && shape.getParticipantBandKind() != null) {
				bindBoolean(shape.eClass().getEStructuralFeature(BpmnDiPackage.BPMN_SHAPE__IS_MESSAGE_VISIBLE),
						createBooleanInput("Is Message Visible"), shape);
			}

		}
	}

	public void bindReference(final EReference reference, final String name) {
		Object eGet = be.eGet(reference);

		createLabel(name);
		if (eGet instanceof List) {
			createListEditor(reference, eGet);
		} else {
			createSingleItemEditor(reference, eGet, null);
		}
	}

	private void createListEditor(final EReference reference, Object eGet) {

		final Text text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolkit.adapt(text, true, true);
		widgets.add(text);

		Button editButton = new Button(this, SWT.NONE);
		editButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		editButton.setText("Edit ...");
		toolkit.adapt(editButton, true, true);
		widgets.add(editButton);

		final List<EObject> refs = (List<EObject>) eGet;
		updateTextField(refs, text);

		SelectionAdapter editListener = new SelectionAdapter() {

			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<EObject> l = null;

				if (modelHandler != null) {
					l = (List<EObject>) modelHandler.getAll(reference.getEType().getInstanceClass());
				}

				FeatureEditorDialog featureEditorDialog = new FeatureEditorDialog(getShell(), LABEL_PROVIDER, be,
						reference, "Select elements", l);

				if (featureEditorDialog.open() == Window.OK) {

					updateEObject(refs, (EList<EObject>) featureEditorDialog.getResult());
					updateTextField(refs, text);
				}
			}

			public void updateEObject(final List<EObject> refs, final EList<EObject> result) {
				TransactionalEditingDomain domain = bpmn2Editor.getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {

						if (result == null) {
							refs.clear();
							return;
						}
						refs.retainAll(result);
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

	private void createSingleItemEditor(final EStructuralFeature reference, Object eGet, Collection values) {
		final ComboViewer combo = new ComboViewer(this, SWT.BORDER);
		Combo c = combo.getCombo();
		combo.setLabelProvider(LABEL_PROVIDER);
		c.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		toolkit.adapt(c, true, true);
		widgets.add(c);

		List<Object> l = null;

		if (values != null) {
			l = Arrays.asList(values.toArray());
		} else if (modelHandler != null) {
			l = (List<Object>) modelHandler.getAll(reference.getEType().getInstanceClass());
		}

		combo.add("");
		combo.add(l.toArray());
		if (eGet != null) {
			combo.setSelection(new StructuredSelection(eGet));
		}

		combo.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = combo.getSelection();
				if (selection instanceof StructuredSelection) {
					Object firstElement = ((StructuredSelection) selection).getFirstElement();
					if (firstElement instanceof EObject) {
						updateEObject(firstElement);
					} else if (firstElement instanceof GatewayDirection) {
						updateGatewayDirection(firstElement);
					} else {
						updateEObject(null);
					}
				}
			}

			public void updateEObject(final Object result) {
				TransactionalEditingDomain domain = bpmn2Editor.getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						be.eSet(reference, result);
					}
				});
			}
			
			public void updateGatewayDirection(final Object result) {
				TransactionalEditingDomain domain = bpmn2Editor.getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						GatewayDirection direction = (GatewayDirection) result;
						be.eSet(reference, direction);
					}
				});
			}
			
		});
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

	public void setShape(BPMNShape shape) {
		this.shape = shape;
	}
}
