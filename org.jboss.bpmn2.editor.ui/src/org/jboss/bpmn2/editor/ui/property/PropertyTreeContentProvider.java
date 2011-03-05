package org.jboss.bpmn2.editor.ui.property;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

final class PropertyTreeContentProvider implements ITreeContentProvider {
	/**
	 * 
	 */
	private final ImprovedAdvancedPropertiesComposite improvedAdvancedPropertiesComposite;

	/**
	 * @param improvedAdvancedPropertiesComposite
	 */
	PropertyTreeContentProvider(ImprovedAdvancedPropertiesComposite improvedAdvancedPropertiesComposite) {
		this.improvedAdvancedPropertiesComposite = improvedAdvancedPropertiesComposite;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof EObject) {
			return !((EObject) element).eContents().isEmpty();
		}
		return false;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof EObject) {
			return ((EObject) inputElement).eContents().toArray();

		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof EObject) {
			return ((EObject) parentElement).eContents().toArray();
		}
		return null;
	}
}