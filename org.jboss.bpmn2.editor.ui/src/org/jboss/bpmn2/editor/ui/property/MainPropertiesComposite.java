package org.jboss.bpmn2.editor.ui.property;

import org.eclipse.core.databinding.Binding;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class MainPropertiesComposite extends AbstractBpmn2PropertiesComposite {

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public MainPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void createBindings() {
		EList<EAttribute> eAllAttributes = be.eClass().getEAllAttributes();

		for (EAttribute a : eAllAttributes) {
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
}
