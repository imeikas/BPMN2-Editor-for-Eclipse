package org.jboss.bpmn2.editor.ui.property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.provider.BpmnDiItemProviderAdapterFactory;
import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.dd.dc.provider.DcItemProviderAdapterFactory;
import org.eclipse.dd.di.provider.DiItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor.PropertyValueWrapper;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.bpmn2.editor.core.Bpmn2Preferences;
import org.jboss.bpmn2.editor.ui.editor.BPMN2Editor;

public class AdvancedPropertiesComposite extends Composite {
	private final class IStructuredContentProviderImplementation implements IStructuredContentProvider {
		IPropertyDescriptor[] propertyDescriptors;

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			propertySource = adapterFactoryContentProvider.getPropertySource(be);
			List<IPropertyDescriptor> desc = Arrays.asList(propertySource.getPropertyDescriptors());
			List<IPropertyDescriptor> ret = new ArrayList<IPropertyDescriptor>();

			String projectName = bpmn2Editor.getDiagramTypeProvider().getDiagram().eResource().getURI().segment(1);
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
			Bpmn2Preferences preferences = Bpmn2Preferences.getPreferences(project);

			System.out.println();

			for (IPropertyDescriptor descriptor : desc) {
				String attribute = be.eClass().getName() + "." + descriptor.getId();
				// System.out.println(attribute);
				// if (preferences.isEnabled(attribute, false)) {
				ret.add(descriptor);
				// }
			}

			propertyDescriptors = ret.toArray(new IPropertyDescriptor[ret.size()]);

		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return propertyDescriptors;
		}
	}

	private final class EditingSupportExtension extends EditingSupport {
		private EditingSupportExtension(ColumnViewer viewer) {
			super(viewer);
		}

		@Override
		protected void setValue(Object element, Object value) {
		}

		@Override
		protected Object getValue(Object element) {
			PropertyValueWrapper propertyValue = (PropertyValueWrapper) propertySource
					.getPropertyValue(((PropertyDescriptor) element).getId());
			if (propertyValue == null) {
				return null;
			}
			return propertyValue.getEditableValue(null);
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return ((PropertyDescriptor) element).createPropertyEditor(table);
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}
	}

	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int index) {
			if (index == 0) {
				return ((IPropertyDescriptor) element).getDisplayName();
			} else {
				PropertyValueWrapper propertyValue = (PropertyValueWrapper) propertySource
						.getPropertyValue(((PropertyDescriptor) element).getId());
				if (propertyValue == null) {
					return null;
				}
				return propertyValue.getText(null);
			}
		}
	}

	private static class ContentProvider implements IStructuredContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			return new Object[0];
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	protected BaseElement be;
	protected BPMN2Editor bpmn2Editor;

	private final Table table;
	private final TableViewer tableViewer;
	private final ComposedAdapterFactory adapterFactory;
	private final AdapterFactoryContentProvider adapterFactoryContentProvider;
	private IPropertySource propertySource;
	private final Composite parent;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public AdvancedPropertiesComposite(Composite parent, int style) {
		super(parent, style);
		this.parent = parent;

		parent.setLayout(new FillLayout());
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new Bpmn2ItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new BpmnDiItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new DiItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new DcItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		adapterFactoryContentProvider = new AdapterFactoryContentProvider(adapterFactory);
		setLayout(new FillLayout(SWT.HORIZONTAL));

		tableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn propertyColumnViewer = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn propertyColumn = propertyColumnViewer.getColumn();
		propertyColumn.setWidth(264);
		propertyColumn.setText("Property");

		TableViewerColumn valueColumnViewer = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn ValueColumn = valueColumnViewer.getColumn();
		ValueColumn.setWidth(100);
		ValueColumn.setText("Value");
		//
		// tableViewer.setLabelProvider(adapterFactoryLabelProvider);
		// tableViewer.setContentProvider(adapterFactoryContentProvider);
		tableViewer.setContentProvider(new IStructuredContentProviderImplementation());
		tableViewer.setLabelProvider(new TableLabelProvider());
		valueColumnViewer.setEditingSupport(new EditingSupportExtension(tableViewer));
	}

	public void setDiagramEditor(BPMN2Editor bpmn2Editor) {
		this.bpmn2Editor = bpmn2Editor;
	}

	public void setBaseElement(BaseElement be) {
		this.be = be;
		tableViewer.setInput(be);
		// layout(true, true);
		// parent.setSize(parent.computeSize(parent.getSize().x, SWT.DEFAULT, true));
	}
}
