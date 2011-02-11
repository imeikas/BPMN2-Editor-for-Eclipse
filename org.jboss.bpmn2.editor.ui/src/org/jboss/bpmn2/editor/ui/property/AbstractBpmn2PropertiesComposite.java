package org.jboss.bpmn2.editor.ui.property;

import java.util.ArrayList;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
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

	protected BaseElement be;
	protected BPMN2Editor bpmn2Editor;
	protected final DataBindingContext bindingContext;
	private final ArrayList<Widget> widgets = new ArrayList<Widget>();
	protected final ArrayList<Binding> bindings = new ArrayList<Binding>();
	protected final Composite parent;
	private PictogramElement pe;
	protected final FormToolkit toolkit = new FormToolkit(Display.getCurrent());

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
		setLayout(new GridLayout(2, false));

	}

	public final void setBaseElement(final BaseElement be) {
		this.be = be;
		cleanBindings();
		createBindings();
	}

	public abstract void createBindings();

	protected Text createIntInput(String name) {
		createLabel(name);
		Text text = new Text(this, SWT.NONE);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolkit.adapt(text, true, true);
		widgets.add(text);

		return text;
	}

	protected Button createBooleanInput(String name) {
		createLabel(name);

		Button button = new Button(this, SWT.CHECK);
		toolkit.adapt(button, true, true);
		widgets.add(button);

		return button;
	}

	protected Text createTextInput(String name) {
		createLabel(name);

		Text text = new Text(this, SWT.NONE);
		// FIXME:add verify listener
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolkit.adapt(text, true, true);
		widgets.add(text);

		return text;
	}

	private void createLabel(String name) {
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
			@SuppressWarnings("restriction")
			@Override
			public void handleValueChange(ValueChangeEvent event) {

				final int i = Integer.parseInt(t.getText());
				if (!be.eGet(a).equals(i)) {
					bpmn2Editor.getEditingDomain().getCommandStack()
							.execute(new RecordingCommand(bpmn2Editor.getEditingDomain()) {
								@Override
								protected void doExecute() {
									be.eSet(a, i);
								}
							});
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

	public void setPictogramElement(PictogramElement pe) {
		this.pe = pe;
	}

	private void cleanBindings() {
		for (Binding b : bindings) {
			b.dispose();
		}
		bindings.clear();

		for (Widget w : widgets) {
			w.dispose();
		}
		widgets.clear();
	}

	public void setDiagramEditor(BPMN2Editor bpmn2Editor) {
		this.bpmn2Editor = bpmn2Editor;

	}

}