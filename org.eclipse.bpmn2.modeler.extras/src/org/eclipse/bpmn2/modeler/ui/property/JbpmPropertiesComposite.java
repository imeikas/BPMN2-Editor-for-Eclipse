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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.core.databinding.Binding;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

public class JbpmPropertiesComposite extends AbstractBpmn2PropertiesComposite {

	private ArrayList<EStructuralFeature> attributes;
	private Button customEditorButton;
	private GridData buttonGridData;

	public JbpmPropertiesComposite(Composite parent, int none) {
		super(parent, none);
		customEditorButton = new Button(this, SWT.None);
		customEditorButton.setText("Open Custom Editor");
		buttonGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		customEditorButton.setLayoutData(buttonGridData);
		toolkit.adapt(customEditorButton, true, true);
		customEditorButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox box = new MessageBox(getShell());
				box.setText("Custom Editor");

				updateDialogContents(box);
				box.open();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	@Override
	public void createBindings() {
		boolean showCustomButton = be.eClass().getInstanceClass()
				.equals(Task.class);
		customEditorButton.setVisible(showCustomButton);
		buttonGridData.exclude = !showCustomButton;

		EList<EAttribute> eAllAttributes = be.eClass().getEAllAttributes();

		for (EAttribute attrib : eAllAttributes) {
			if ("anyAttribute".equals(attrib.getName())) {
				attributes = Bpmn2Preferences.getAttributes(be.eClass());

				replaceExistingAnyAttributes(attrib);

				Collections.sort(attributes,
						new Comparator<EStructuralFeature>() {

							@Override
							public int compare(EStructuralFeature o1,
									EStructuralFeature o2) {
								return o1.getName().compareTo(o2.getName());

							}
						});

				for (EStructuralFeature a : attributes) {
					if (Object.class.equals(a.getEType().getInstanceClass())) {
						Text t = createTextInput(a.getName(), false);
						bind(a, t);
					}
				}
			}
		}
		parent.setSize(parent.computeSize(parent.getSize().x, SWT.DEFAULT, true));
	}

	/**
	 * EMF creates new StructuralFeatures for each unspecified anyAttribute
	 * element. For bindings to work, we must replace these features with EMF
	 * generated instance, or there would be two or more attributes with the
	 * same name, but different values.
	 */
	private void replaceExistingAnyAttributes(EAttribute attrib) {
		HashMap<EStructuralFeature, EStructuralFeature> replace = new HashMap<EStructuralFeature, EStructuralFeature>();
		for (EStructuralFeature a : attributes) {
			List<Entry> basicList = ((BasicFeatureMap) be.eGet(attrib))
					.basicList();
			for (Entry entry : basicList) {
				if (entry.getEStructuralFeature().getName().equals(a.getName())) {
					replace.put(a, entry.getEStructuralFeature());
				}
			}
		}
		for (EStructuralFeature a : replace.keySet()) {
			attributes.remove(a);
			attributes.add(replace.get(a));
		}
	}

	private void updateDialogContents(MessageBox box) {
		for (EStructuralFeature eStructuralFeature : attributes) {
			if (eStructuralFeature.getName().equals("taskName"))
				box.setMessage("Here should be a custom editor for "
						+ be.eGet(eStructuralFeature) + " !");
		}
	}
}
