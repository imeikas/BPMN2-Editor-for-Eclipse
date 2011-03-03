package org.jboss.bpmn2.editor.ui.property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

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
import org.jboss.bpmn2.editor.core.Bpmn2Preferences;

public class JbpmPropertiesComposite extends AbstractBpmn2PropertiesComposite {

	private ArrayList<EStructuralFeature> attributes;

	public JbpmPropertiesComposite(Composite parent, int none) {
		super(parent, none);
		Button b = new Button(this, SWT.None);
		b.setText("Open Custom Editor");
		b.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		toolkit.adapt(b, true, true);
		b.addSelectionListener(new SelectionListener() {

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
						Text t = createTextInput(a.getName());
						Binding bind = bind(a, t);
						bindings.add(bind);
					}
				}
			}
		}
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
