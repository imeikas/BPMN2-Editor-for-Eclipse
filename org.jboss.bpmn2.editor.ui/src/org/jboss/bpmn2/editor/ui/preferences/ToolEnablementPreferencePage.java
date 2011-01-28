package org.jboss.bpmn2.editor.ui.preferences;

import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.jboss.bpmn2.editor.core.Bpmn2Preferences;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.jboss.bpmn2.editor.core.ToolEnablement;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.viewers.CheckboxCellEditor;

public class ToolEnablementPreferencePage extends PreferencePage {
	private DataBindingContext m_bindingContext;
	
	private Bpmn2Preferences preferences = Bpmn2Preferences.getPreferences(null);
	private Table table_1;
	private TableColumn tblclmnTool;
	private Table table_2;
	private CheckboxTableViewer checkboxTableViewer;
	private TableViewerColumn toolColumn;
	private TableViewerColumn toolEnabledColumn;

	/**
	 * Create the preference page.
	 */
	public ToolEnablementPreferencePage() {
		setTitle("Enabled Tools");
	}

	/**
	 * Create contents of the preference page.
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, false));
		
		Label lblTools = new Label(container, SWT.NONE);
		lblTools.setText("Tools");
		
		checkboxTableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
		table_2 = checkboxTableViewer.getTable();
		table_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		toolColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		tblclmnTool = toolColumn.getColumn();
		tblclmnTool.setWidth(100);
		tblclmnTool.setText("Tool");
		
		toolEnabledColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnToolEnabled = toolEnabledColumn.getColumn();
		tblclmnToolEnabled.setWidth(100);
		tblclmnToolEnabled.setText("Enabled");
		
		Label lblConnectors = new Label(container, SWT.NONE);
		lblConnectors.setText("Connectors");
		
		TableViewer tableViewer_1 = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		table_1 = tableViewer_1.getTable();
		table_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn connectorColumn = new TableViewerColumn(tableViewer_1, SWT.NONE);
		TableColumn tblclmnConnector = connectorColumn.getColumn();
		tblclmnConnector.setWidth(100);
		tblclmnConnector.setText("Connector");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		TableColumn tblclmnConnectorEnabled = tableViewerColumn_2.getColumn();
		tblclmnConnectorEnabled.setWidth(100);
		tblclmnConnectorEnabled.setText("Enabled");
		m_bindingContext = initDataBindings();

		return container;
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		checkboxTableViewer.setContentProvider(listContentProvider);
		//
		IObservableMap[] observeMaps = PojoObservables.observeMaps(listContentProvider.getKnownElements(), ToolEnablement.class, new String[]{"feature.name", "enabled"});
		checkboxTableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		//
		IObservableList preferencesListOfToolsObserveList = PojoObservables.observeList(Realm.getDefault(), preferences, "listOfTools");
		checkboxTableViewer.setInput(preferencesListOfToolsObserveList);
		//
		CellEditor cellEditor = new TextCellEditor(checkboxTableViewer.getTable());
		IValueProperty cellEditorProperty = BeanProperties.value("value");
		IBeanValueProperty valueProperty = BeanProperties.value("feature.name");
		toolColumn.setEditingSupport(ObservableValueEditingSupport.create(checkboxTableViewer, bindingContext, cellEditor, cellEditorProperty, valueProperty));
		//
		CellEditor cellEditor_1 = new CheckboxCellEditor(checkboxTableViewer.getTable());
		IValueProperty cellEditorProperty_1 = BeanProperties.value("value");
		IBeanValueProperty valueProperty_1 = BeanProperties.value("enabled");
		toolEnabledColumn.setEditingSupport(ObservableValueEditingSupport.create(checkboxTableViewer, bindingContext, cellEditor_1, cellEditorProperty_1, valueProperty_1));
		//
		return bindingContext;
	}
}
