package org.jboss.bpmn2.editor.ui.property;

import java.util.Collection;
import java.util.List;

import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.bpmn2.editor.core.Bpmn2Preferences;
import org.jboss.bpmn2.editor.ui.editor.BPMN2Editor;

@SuppressWarnings("unchecked")
public class ImprovedAdvancedPropertiesComposite extends Composite {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private EObject be;
	private final TreeViewer treeViewer;
	private TabbedPropertySheetPage aTabbedPropertySheetPage;
	private BPMN2Editor diagramEditor;
	private final MainPropertiesComposite mainPropertiesComposite;
	private Bpmn2Preferences prefs;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ImprovedAdvancedPropertiesComposite(Composite parent, int style) {
		super(parent, style);
		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		FillLayout fillLayout = new FillLayout(SWT.HORIZONTAL);
		fillLayout.spacing = 2;
		fillLayout.marginHeight = 3;
		fillLayout.marginWidth = 3;
		setLayout(fillLayout);

		SashForm sashForm = new SashForm(this, SWT.NONE);
		sashForm.setSashWidth(5);
		toolkit.adapt(sashForm);
		toolkit.paintBordersFor(sashForm);

		Section sctnProperties = toolkit.createSection(sashForm, ExpandableComposite.TITLE_BAR);
		toolkit.paintBordersFor(sctnProperties);
		sctnProperties.setText("Properties");

		Composite composite = toolkit.createComposite(sctnProperties, SWT.NONE);
		toolkit.paintBordersFor(composite);
		sctnProperties.setClient(composite);
		composite.setLayout(new GridLayout(1, false));

		treeViewer = new TreeViewer(composite, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		toolkit.paintBordersFor(tree);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				mainPropertiesComposite.setEObject(diagramEditor, getSelectedBaseElement());
				aTabbedPropertySheetPage.resizeScrolledComposite();
			}
		});

		treeViewer.setContentProvider(new PropertyTreeContentProvider(this));
		treeViewer.setLabelProvider(new AdapterFactoryLabelProvider(AbstractBpmn2PropertiesComposite.ADAPTER_FACTORY));

		Section sctnEditors = toolkit.createSection(sashForm, ExpandableComposite.TITLE_BAR);
		toolkit.paintBordersFor(sctnEditors);
		sctnEditors.setText("Attributes");

		mainPropertiesComposite = new MainPropertiesComposite(sctnEditors, SWT.NONE);
		sctnEditors.setClient(mainPropertiesComposite);
		toolkit.adapt(mainPropertiesComposite);
		toolkit.paintBordersFor(mainPropertiesComposite);
		sashForm.setWeights(new int[] { 1, 1 });

	}

	public void setEObject(BPMN2Editor diagramEditor, EObject be) {
		this.diagramEditor = diagramEditor;
		this.be = be;
		treeViewer.setInput(be);
		prefs = Bpmn2Preferences.getPreferences(diagramEditor.getModelFile().getProject());
	}

	public void setSheetPage(TabbedPropertySheetPage aTabbedPropertySheetPage) {
		this.aTabbedPropertySheetPage = aTabbedPropertySheetPage;

		MenuManager manager = new MenuManager("#PropertiesMenu");
		manager.setRemoveAllWhenShown(true);
		manager.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				ImprovedAdvancedPropertiesComposite.this.buildMenu((MenuManager) manager);
			}
		});

		Tree tree = treeViewer.getTree();
		Menu menu = manager.createContextMenu(tree);
		tree.setMenu(menu);
		aTabbedPropertySheetPage.getSite().registerContextMenu("#PropertiesMenu", manager, treeViewer);
	}

	protected void buildMenu(MenuManager manager) {

		EObject selectedElem = getSelectedBaseElement();
		createElementProperties(manager, selectedElem);

		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		if (selectedElem != null) {
			manager.add(createRemoveAction(selectedElem));
		}

		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		createRootProperties(manager);
	}

	private void createRootProperties(MenuManager menuManager) {
		MenuManager manager = new MenuManager("Add Root Property");
		menuManager.add(manager);
		createMenuItems(manager, "", be);
	}

	private void createElementProperties(MenuManager manager, EObject baseElement) {
		if (baseElement != null) {
			createMenuItems(manager, "Add ", baseElement);
		}
	}

	private void createMenuItems(MenuManager manager, String prefix, EObject baseElement) {
		ItemProviderAdapter itemProviderAdapter = (ItemProviderAdapter) new Bpmn2ItemProviderAdapterFactory().adapt(
				baseElement, ItemProviderAdapter.class);
		Collection<CommandParameter> desc = (Collection<CommandParameter>) itemProviderAdapter.getNewChildDescriptors(
				baseElement, diagramEditor.getEditingDomain(), null);

		EList<EReference> eAllContainments = baseElement.eClass().getEAllContainments();

		for (CommandParameter command : desc) {
			EStructuralFeature feature = (EStructuralFeature) command.feature;

			if (eAllContainments.contains(feature) // && !"flowElements".equals(feature.getName())
					&& prefs.isEnabled(baseElement.eClass(), feature)) {
				Object value = baseElement.eGet(feature);

				String name = PropertyUtil.deCamelCase(((EObject) command.value).eClass().getName());
				Action item = createMenuItemFor(prefix + name, baseElement, (EReference) feature, command.value);

				item.setEnabled(value == null || value instanceof EList);
				manager.add(item);
			}
		}
	}

	private Action createMenuItemFor(String prefix, final EObject baseElement, final EReference eReference,
			final Object value) {
		return new Action(prefix) {
			@Override
			public void run() {
				TransactionalEditingDomain domain = diagramEditor.getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						createNewProperty(baseElement, eReference);
					}

					private void createNewProperty(final EObject baseElement, final EReference eReference) {
						Object eGet = baseElement.eGet(eReference);
						if (eGet instanceof EList) {
							((EList) eGet).add(value);
						} else {
							baseElement.eSet(eReference, value);
						}
						treeViewer.refresh(true);
					}
				});
			}
		};
	}

	private EObject getSelectedBaseElement() {
		ISelection selection = treeViewer.getSelection();
		EObject baseElement = null;

		if (selection instanceof IStructuredSelection) {
			Object firstElement = ((IStructuredSelection) selection).getFirstElement();

			if (firstElement instanceof EObject) {
				baseElement = (EObject) firstElement;
			}
		}
		return baseElement;
	}

	private Action createRemoveAction(final EObject baseElement) {
		return new Action("Remove") {
			@SuppressWarnings("restriction")
			@Override
			public void run() {

				if (baseElement == null) {
					treeViewer.refresh(true);
					return;
				}

				final EObject container = baseElement.eContainer();
				final Object eGet = container.eGet(baseElement.eContainingFeature());
				TransactionalEditingDomain domain = diagramEditor.getEditingDomain();

				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@SuppressWarnings("rawtypes")
					@Override
					protected void doExecute() {
						List<PictogramElement> pictogramElements = GraphitiUi.getLinkService().getPictogramElements(
								diagramEditor.getDiagramTypeProvider().getDiagram(), baseElement);
						if (eGet instanceof EList) {
							((EList) eGet).remove(baseElement);
						} else {
							container.eUnset(baseElement.eContainingFeature());
						}

						for (PictogramElement pictogramElement : pictogramElements) {
							// TODO: check if we need to update the diagram manually or will the autoupdate do
							// it
							// UpdateContext context = new UpdateContext(pictogramElement);
							// IUpdateFeature updateFeature = diagramEditor.getDiagramTypeProvider()
							// .getFeatureProvider().getUpdateFeature(context);
							// context.putProperty("deleted", baseElement);
							// if (updateFeature != null) {
							// updateFeature.update(context);
							// }
							pictogramElement.getLink().getBusinessObjects().clear();
						}
						treeViewer.refresh(true);
					}
				});

			}
		};
	}
}
