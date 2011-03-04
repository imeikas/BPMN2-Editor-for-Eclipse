package org.jboss.bpmn2.editor.ui.property;

import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
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
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
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
import org.jboss.bpmn2.editor.ui.editor.BPMN2Editor;

public class ImprovedAdvancedPropertiesComposite extends Composite {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private BaseElement be;
	private TreeViewer treeViewer;
	private TabbedPropertySheetPage aTabbedPropertySheetPage;
	private BPMN2Editor diagramEditor;
	private MainPropertiesComposite mainPropertiesComposite;

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
			}
		});

		treeViewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof BaseElement) {
					return !((BaseElement) element).eContents().isEmpty();
				}
				return false;
			}

			@Override
			public Object getParent(Object element) {
				return null;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return be.eContents().toArray();
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof BaseElement) {
					return ((BaseElement) parentElement).eContents().toArray();
				}
				return null;
			}

		});
		treeViewer.setLabelProvider(new AdapterFactoryLabelProvider(AbstractBpmn2PropertiesComposite.ADAPTER_FACTORY));

		Section sctnEditors = toolkit.createSection(sashForm, ExpandableComposite.TITLE_BAR);
		toolkit.paintBordersFor(sctnEditors);
		sctnEditors.setText("Attributes");

		mainPropertiesComposite = new MainPropertiesComposite(sctnEditors, SWT.NONE);
		toolkit.adapt(mainPropertiesComposite);
		toolkit.paintBordersFor(mainPropertiesComposite);
		sctnEditors.setClient(mainPropertiesComposite);
		sashForm.setWeights(new int[] { 1, 1 });

	}

	public void setBaseElement(BPMN2Editor diagramEditor, BaseElement be) {
		this.diagramEditor = diagramEditor;
		this.be = be;
		treeViewer.setInput(be);
	}

	public void setSheetPage(TabbedPropertySheetPage aTabbedPropertySheetPage) {
		this.aTabbedPropertySheetPage = aTabbedPropertySheetPage;
		MenuManager manager = new MenuManager("#PropertiesMenu");
		manager.setRemoveAllWhenShown(true);
		manager.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				ImprovedAdvancedPropertiesComposite.this.buildMenu(manager);
			}
		});
		Tree tree = treeViewer.getTree();
		Menu menu = manager.createContextMenu(tree);
		tree.setMenu(menu);
		aTabbedPropertySheetPage.getSite().registerContextMenu("#PropertiesMenu", manager, treeViewer);
	}

	protected void buildMenu(IMenuManager manager) {
		EObject selectedElem = getSelectedBaseElement();
		createElementProperties(manager, selectedElem);

		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		if (selectedElem != null) {
			manager.add(createRemoveAction(selectedElem));
		}

		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		createRootProperties(manager);
	}

	private void createRootProperties(IMenuManager manager) {
		MenuManager menuManager = new MenuManager("Add Root Property");
		manager.add(menuManager);
		EList<EReference> eAllContainments = be.eClass().getEAllContainments();
		for (EReference eReference : eAllContainments) {
			Object value = be.eGet(eReference);
			Action item = createMenuItemFor("", be, eReference);
			item.setEnabled(value == null || value instanceof EList);
			menuManager.add(item);
		}
	}

	private void createElementProperties(IMenuManager manager, EObject baseElement) {
		EList<EReference> eAllContainments;
		if (baseElement != null) {
			eAllContainments = baseElement.eClass().getEAllContainments();
			for (EReference eReference : eAllContainments) {
				Object value = baseElement.eGet(eReference);
				Action item = createMenuItemFor("Add ", baseElement, eReference);
				item.setEnabled(value == null || value instanceof EList);
				manager.add(item);
			}
		}
	}

	private Action createMenuItemFor(String prefix, final EObject baseElement, final EReference eReference) {
		return new Action(prefix + eReference.getName()) {
			@Override
			public void run() {
				TransactionalEditingDomain domain = diagramEditor.getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						createNewProperty(baseElement, eReference);
					}

					private void createNewProperty(final EObject baseElement, final EReference eReference) {
						EObject created = Bpmn2Factory.eINSTANCE.create((EClass) eReference.getEType());
						Object eGet = baseElement.eGet(eReference);
						if (eGet instanceof EList) {
							((EList) eGet).add(created);
						} else {
							baseElement.eSet(eReference, created);
						}
						treeViewer.refresh(true);
					}
				});
			}
		};
	}

	private EObject getSelectedBaseElement() {
		System.out.println("startedSelection");
		ISelection selection = treeViewer.getSelection();
		System.out.println(selection);
		EObject baseElement = null;
		if (selection instanceof IStructuredSelection) {
			System.out.println("structured selection");
			Object firstElement = ((IStructuredSelection) selection).getFirstElement();
			if (firstElement instanceof EObject) {
				System.out.println("baseElement");
				baseElement = (EObject) firstElement;
			}
		}
		return baseElement;
	}

	private Action createRemoveAction(final EObject baseElement) {
		return new Action("Remove") {
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
						EList<EObject> eCrossReferences = baseElement.eCrossReferences();
						treeViewer.refresh(true);
					}
				});

			}
		};
	}
}
