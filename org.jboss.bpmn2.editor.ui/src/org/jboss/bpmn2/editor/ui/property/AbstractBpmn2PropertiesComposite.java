package org.jboss.bpmn2.editor.ui.property;

import java.util.ArrayList;

import org.eclipse.bpmn2.di.provider.BpmnDiItemProviderAdapterFactory;
import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.dd.dc.provider.DcItemProviderAdapterFactory;
import org.eclipse.dd.di.provider.DiItemProviderAdapterFactory;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.bpmn2.editor.ui.editor.BPMN2Editor;

public abstract class AbstractBpmn2PropertiesComposite extends Composite {

	public final static ComposedAdapterFactory ADAPTER_FACTORY;
	protected EObject be;
	protected BPMN2Editor bpmn2Editor;
	protected final DataBindingContext bindingContext;
	protected final ArrayList<Widget> widgets = new ArrayList<Widget>();
	protected final ArrayList<Binding> bindings = new ArrayList<Binding>();
	protected final Composite parent;
	protected final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	protected IProject project;
	private Text text_1;

	static {
		ADAPTER_FACTORY = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		ADAPTER_FACTORY.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		ADAPTER_FACTORY.addAdapterFactory(new Bpmn2ItemProviderAdapterFactory());
		ADAPTER_FACTORY.addAdapterFactory(new BpmnDiItemProviderAdapterFactory());
		ADAPTER_FACTORY.addAdapterFactory(new DiItemProviderAdapterFactory());
		ADAPTER_FACTORY.addAdapterFactory(new DcItemProviderAdapterFactory());
		ADAPTER_FACTORY.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
	}

	/**
	 * NB! Must call setEObject for updating contents and rebuild the UI.
	 * 
	 * @param parent
	 * @param style
	 */
	public AbstractBpmn2PropertiesComposite(Composite parent, int style) {
		super(parent, style);
		this.parent = parent;
		bindingContext = new DataBindingContext();
		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(3, false));
	}

	public final void setEObject(BPMN2Editor bpmn2Editor, final EObject be) {
		String projectName = bpmn2Editor.getDiagramTypeProvider().getDiagram().eResource().getURI().segment(1);
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		setDiagramEditor(bpmn2Editor);
		setEObject(be);
	}

	private final void setDiagramEditor(BPMN2Editor bpmn2Editor) {
		this.bpmn2Editor = bpmn2Editor;
	}

	private final void setEObject(final EObject be) {
		this.be = be;
		cleanBindings();
		if (be != null) {
			createBindings();
		}
		layout(true, true);
		parent.setSize(parent.computeSize(parent.getSize().x, SWT.DEFAULT, true));
	}

	/**
	 * This method is called when setEObject is called and this should recreate all bindings and widgets for the
	 * component.
	 */
	public abstract void createBindings();

	protected Text createTextInput(String name, boolean multiLine) {
		createLabel(name);

		int flag = SWT.BORDER;
		if (multiLine) {
			flag |= SWT.BORDER | SWT.WRAP | SWT.MULTI;
		}
		Text text = new Text(this, flag);
		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		if (multiLine) {
			data.heightHint = 50;
		}
		text.setLayoutData(data);
		toolkit.adapt(text, true, true);
		widgets.add(text);

		return text;
	}

	protected Text createIntInput(String name) {
		createLabel(name);

		Text text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		toolkit.adapt(text, true, true);
		widgets.add(text);
		return text;
	}

	protected Button createBooleanInput(String name) {
		createLabel(name);

		Button button = new Button(this, SWT.CHECK);
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		toolkit.adapt(button, true, true);
		widgets.add(button);
		return button;
	}

	protected void createLabel(String name) {
		Label label = new Label(this, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		toolkit.adapt(label, true, true);
		label.setText(name);
		widgets.add(label);
	}

	protected Binding bind(final EStructuralFeature a, final Text text) {
		IObservableValue textObserveTextObserveWidget = SWTObservables.observeText(text, SWT.Modify);
		IObservableValue holderIdObserveValue = EMFObservables.observeValue(be, a);
		textObserveTextObserveWidget.addValueChangeListener(new IValueChangeListener() {
			@SuppressWarnings("restriction")
			@Override
			public void handleValueChange(ValueChangeEvent event) {

				if (!text.getText().equals(be.eGet(a))) {
					bpmn2Editor.getEditingDomain().getCommandStack()
							.execute(new RecordingCommand(bpmn2Editor.getEditingDomain()) {
								@Override
								protected void doExecute() {
									be.eSet(a, text.getText());
								}
							});
				}
			}
		});

		Binding bindValue = bindingContext.bindValue(textObserveTextObserveWidget, holderIdObserveValue,
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), null);
		return bindValue;
	}

	protected Binding bindBoolean(final EStructuralFeature a, final Button button) {
		IObservableValue textObserveTextObserveWidget = SWTObservables.observeSelection(button);
		IObservableValue holderIdObserveValue = EMFObservables.observeValue(be, a);
		textObserveTextObserveWidget.addValueChangeListener(new IValueChangeListener() {
			@SuppressWarnings("restriction")
			@Override
			public void handleValueChange(ValueChangeEvent event) {

				if (!be.eGet(a).equals(button.getSelection())) {
					bpmn2Editor.getEditingDomain().getCommandStack()
							.execute(new RecordingCommand(bpmn2Editor.getEditingDomain()) {
								@Override
								protected void doExecute() {
									be.eSet(a, button.getSelection());
								}
							});
				}
			}
		});

		Binding bindValue = bindingContext.bindValue(textObserveTextObserveWidget, holderIdObserveValue,
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), null);
		return bindValue;
	}

	protected Binding bindInt(final EStructuralFeature a, final Text t) {
		IObservableValue textObserveTextObserveWidget = SWTObservables.observeText(t, SWT.Modify);
		IObservableValue holderIdObserveValue = EMFObservables.observeValue(be, a);

		textObserveTextObserveWidget.addValueChangeListener(new IValueChangeListener() {
			@Override
			public void handleValueChange(ValueChangeEvent event) {

				try {
					final int i = Integer.parseInt(t.getText());
					if (!be.eGet(a).equals(i)) {
						setFeatureValue(i);
					}
				} catch (NumberFormatException e) {
					resetValue(event);
				}
			}

			@SuppressWarnings("restriction")
			private void setFeatureValue(final int i) {
				RecordingCommand command = new RecordingCommand(bpmn2Editor.getEditingDomain()) {
					@Override
					protected void doExecute() {
						be.eSet(a, i);
					}
				};
				bpmn2Editor.getEditingDomain().getCommandStack().execute(command);
			}

			private void resetValue(ValueChangeEvent event) {
				int caretPosition = t.getCaretPosition();
				t.setText((String) event.diff.getOldValue());
				int position = caretPosition - 1;
				if (position >= 0) {
					t.setSelection(position);
				} else {
					t.setSelection(0);
				}
			}
		});

		Binding bindValue = bindingContext.bindValue(textObserveTextObserveWidget, holderIdObserveValue,
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER),
				new UpdateValueStrategy().setConverter(new Converter(int.class, String.class) {

					@Override
					public Object convert(Object fromObject) {
						return fromObject.toString();
					}
				}));
		return bindValue;
	}

	protected void cleanBindings() {
		for (Binding b : bindings) {
			b.dispose();
		}
		bindings.clear();

		for (Widget w : widgets) {
			w.dispose();
		}
		widgets.clear();
	}

}