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
package org.eclipse.bpmn2.modeler.ui.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.ToolEnablement;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PropertyPage;
import org.osgi.service.prefs.BackingStoreException;

public class ToolEnablementPropertyPage extends PropertyPage {

	private DataBindingContext m_bindingContext;

	private Bpmn2Preferences preferences;
	private final List<ToolEnablement> tools = new ArrayList<ToolEnablement>();
	private Object[] toolsEnabled;
	private CheckboxTreeViewer checkboxTreeViewer;

	private WritableList writableList;

	/**
	 * Create the property page.
	 */
	public ToolEnablementPropertyPage() {
		setTitle("BPMN2");
	}

	/**
	 * Create contents of the property page.
	 * 
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		initData();

		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(3, false));

		Label lblEnabledToolsAnd = new Label(container, SWT.NONE);
		lblEnabledToolsAnd.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblEnabledToolsAnd.setText("Enabled tools and attributes");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		checkboxTreeViewer = new CheckboxTreeViewer(scrolledComposite, SWT.BORDER);
		Tree tree = checkboxTreeViewer.getTree();
		scrolledComposite.setContent(tree);
		scrolledComposite.setMinSize(tree.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		new Label(container, SWT.NONE);

		Button btnImportProfile = new Button(container, SWT.NONE);
		btnImportProfile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.NULL);
				String path = dialog.open();
				if (path != null) {
					try {
						tools.clear();
						preferences.importPreferences(path);
						reloadPreferences();
						checkboxTreeViewer.refresh();
						restoreDefaults();
					} catch (Exception e1) {
						Activator.showErrorWithLogging(e1);
					}
				}
			}
		});
		btnImportProfile.setText("Import Profile ...");

		Button btnExportProfile = new Button(container, SWT.NONE);
		btnExportProfile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.SAVE);
				String path = dialog.open();
				if (path != null) {
					try {
						preferences.export(path);
					} catch (Exception e1) {
						Activator.showErrorWithLogging(e1);
					}
				}
			}
		});
		btnExportProfile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnExportProfile.setText("Export Profile ...");

		checkboxTreeViewer.setComparer(new IElementComparer() {

			@Override
			public boolean equals(Object a, Object b) {
				return a == b;
			}

			@Override
			public int hashCode(Object element) {
				return System.identityHashCode(element);
			}
		});
		checkboxTreeViewer.setUseHashlookup(true);
		m_bindingContext = initDataBindings();

		restoreDefaults();

		return container;
	}

	private void restoreDefaults() {
		checkboxTreeViewer.setCheckedElements(toolsEnabled);
	}

	@Override
	protected void performDefaults() {
		super.performDefaults();
		restoreDefaults();
	}

	private void initData() {
		preferences = Bpmn2Preferences.getPreferences((IProject) getElement().getAdapter(IProject.class));

		reloadPreferences();
	}

	private void reloadPreferences() {
		tools.clear();
		tools.addAll(preferences.getAllElements());
		ArrayList<ToolEnablement> tEnabled = new ArrayList<ToolEnablement>();
		for (ToolEnablement tool : tools) {
			if (tool.getEnabled()) {
				tEnabled.add(tool);
			}
			ArrayList<ToolEnablement> children = tool.getChildren();
			for (ToolEnablement t : children) {
				if (t.getEnabled()) {
					tEnabled.add(t);
				}
			}
		}
		toolsEnabled = tEnabled.toArray();
	}

	@Override
	public boolean performOk() {
		setErrorMessage(null);
		try {
			updateToolEnablement(tools, Arrays.asList(checkboxTreeViewer.getCheckedElements()));
		} catch (BackingStoreException e) {
			Activator.showErrorWithLogging(e);
		}
		return true;
	}

	private void updateToolEnablement(List<ToolEnablement> saveables, List<Object> enabled)
			throws BackingStoreException {
		for (ToolEnablement t : saveables) {
			preferences.setEnabled(t, enabled.contains(t));
			for (ToolEnablement c : t.getChildren()) {
				preferences.setEnabled(c, enabled.contains(c));
			}
		}
		preferences.flush();
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		checkboxTreeViewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof ToolEnablement) {
					return !((ToolEnablement) element).getChildren().isEmpty();
				}
				return false;
			}

			@Override
			public Object getParent(Object element) {
				if (element instanceof ToolEnablement) {
					return ((ToolEnablement) element).getParent();
				}
				return null;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof WritableList) {
					return ((WritableList) inputElement).toArray();
				}
				return null;
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof ToolEnablement) {
					return ((ToolEnablement) parentElement).getChildren().toArray();
				}
				return null;
			}
		});

		checkboxTreeViewer.setLabelProvider(new ILabelProvider() {
			@Override
			public void removeListener(ILabelProviderListener listener) {
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void dispose() {

			}

			@Override
			public void addListener(ILabelProviderListener listener) {
			}

			@Override
			public Image getImage(Object element) {
				return null;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof ToolEnablement) {
					return ((ToolEnablement) element).getName();
				}
				return null;
			}
		});
		writableList = new WritableList(tools, ToolEnablement.class);
		checkboxTreeViewer.setInput(writableList);
		//
		return bindingContext;
	}

}
