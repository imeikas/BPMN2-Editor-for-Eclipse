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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.jboss.bpmn2.editor.core.Bpmn2Preferences;

public class JbpmPropertiesComposite extends AbstractBpmn2PropertiesComposite {

	public JbpmPropertiesComposite(Composite parent, int none) {
		super(parent, none);
	}

	@Override
	public void createBindings() {
		EList<EAttribute> eAllAttributes = be.eClass().getEAllAttributes();

		for (EAttribute attrib : eAllAttributes) {
			if ("anyAttribute".equals(attrib.getName())) {
				ArrayList<EStructuralFeature> attributes = Bpmn2Preferences.getAttributes(be.eClass());

				HashMap<EStructuralFeature, EStructuralFeature> replace = new HashMap<EStructuralFeature, EStructuralFeature>();
				for (EStructuralFeature a : attributes) {
					List<Entry> basicList = ((BasicFeatureMap) be.eGet(attrib)).basicList();
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

				Collections.sort(attributes, new Comparator<EStructuralFeature>() {

					@Override
					public int compare(EStructuralFeature o1, EStructuralFeature o2) {
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
}
