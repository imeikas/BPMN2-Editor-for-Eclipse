package org.jboss.bpmn2.editor.ui.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.dialogs.PropertyPage;
import org.jboss.bpmn2.editor.core.Bpmn2Preferences;
import org.jboss.bpmn2.editor.core.ToolEnablement;
import org.jboss.bpmn2.editor.ui.Activator;
import org.osgi.service.prefs.BackingStoreException;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

public class ToolEnablementPropertyPage extends PropertyPage {
	private DataBindingContext m_bindingContext;

	private Bpmn2Preferences preferences;
	private List<ToolEnablement> tools;
	private Object[] toolsEnabled;
	private List<ToolEnablement> connectors;
	private Object[] connectorsEnabled;
	private CheckboxTableViewer toolsTableViewer;
	private CheckboxTableViewer connectorsTableViewer;

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
		container.setLayout(new GridLayout(1, false));

		Label label = new Label(container, SWT.NONE);
		label.setText("Tools");

		toolsTableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
		Table toolTable = toolsTableViewer.getTable();
		toolTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Label label_1 = new Label(container, SWT.NONE);
		label_1.setText("Connectors");

		connectorsTableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
		Table connectorTable = connectorsTableViewer.getTable();
		connectorTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		m_bindingContext = initDataBindings();

		restoreDefaults();

		return container;
	}

	private void restoreDefaults() {
	    toolsTableViewer.setCheckedElements(toolsEnabled);
		connectorsTableViewer.setCheckedElements(connectorsEnabled);
    }

	@Override
	protected void performDefaults() {
	    super.performDefaults();
		restoreDefaults();
	}
	
	
	private void initData() {
		preferences = Bpmn2Preferences.getPreferences((IProject) getElement());

		tools = preferences.getListOfTools();
		ArrayList<ToolEnablement> tEnabled = new ArrayList<ToolEnablement>();
		for (ToolEnablement tool : tools) {
			if (tool.getEnabled()) {
				tEnabled.add(tool);
			}
		}

		toolsEnabled = tEnabled.toArray();
		connectors = preferences.getListOfConnectors();
		ArrayList<ToolEnablement> cEnabled = new ArrayList<ToolEnablement>();
		for (ToolEnablement tool : connectors) {
			if (tool.getEnabled()) {
				cEnabled.add(tool);
			}
		}
		connectorsEnabled = cEnabled.toArray();
	}

	
	
	@Override
	public boolean performOk() {
		setErrorMessage(null);
		List<Object> enabledTools = Arrays.asList(toolsTableViewer.getCheckedElements());
		List<Object> enabledConnectors = Arrays.asList(connectorsTableViewer.getCheckedElements());

		try {
			updateToolEnablement(tools, enabledTools);
			updateToolEnablement(connectors, enabledConnectors);
		} catch (BackingStoreException e) {
			Activator.logError(e);
			setErrorMessage("There was a problem saving your settings, more details in log!");
			return false;
		}

		return true;
	}

	private void updateToolEnablement(List<ToolEnablement> saveables, List<Object> enabled)
	        throws BackingStoreException {
		for (ToolEnablement t : saveables) {
			preferences.setEnabled(t.getFeature(), enabled.contains(t));
		}
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		toolsTableViewer.setContentProvider(listContentProvider);
		//
		IObservableMap observeMap = BeansObservables.observeMap(listContentProvider.getKnownElements(),
		        ToolEnablement.class, "name");
		toolsTableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		//
		WritableList writableList = new WritableList(tools, ToolEnablement.class);
		toolsTableViewer.setInput(writableList);
		//
		ObservableListContentProvider listContentProvider_1 = new ObservableListContentProvider();
		connectorsTableViewer.setContentProvider(listContentProvider_1);
		//
		IObservableMap observeMap_1 = BeansObservables.observeMap(listContentProvider_1.getKnownElements(),
		        ToolEnablement.class, "name");
		connectorsTableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMap_1));
		//
		WritableList writableList_1 = new WritableList(connectors, ToolEnablement.class);
		connectorsTableViewer.setInput(writableList_1);
		//
		return bindingContext;
	}
}
