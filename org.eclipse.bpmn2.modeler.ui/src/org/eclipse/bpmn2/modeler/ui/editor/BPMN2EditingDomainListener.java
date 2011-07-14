package org.eclipse.bpmn2.modeler.ui.editor;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.transaction.ExceptionHandler;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.emf.transaction.TransactionalEditingDomainEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomainListenerImpl;

public class BPMN2EditingDomainListener extends TransactionalEditingDomainListenerImpl implements ExceptionHandler {
	
	protected BPMN2Editor bpmn2Editor;
	protected BasicDiagnostic diagnostics;

	public BPMN2EditingDomainListener(BPMN2Editor bpmn2Editor) {
		super();
		this.bpmn2Editor = bpmn2Editor;
		TransactionalCommandStack stack = (TransactionalCommandStack) bpmn2Editor.getEditingDomain().getCommandStack();
		stack.setExceptionHandler(this);
	}

	@Override
	public void transactionStarting(TransactionalEditingDomainEvent event) {
		diagnostics = null;
	}

	@Override
	public void handleException(Exception e) {
		String source = null;
		int code = 0;
		String message = e.getMessage();
		Object[] data = null;
		StackTraceElement trace[] = e.getStackTrace();
		if (trace!=null && trace.length>0) {
			source = trace[0].getMethodName();
		}
		if (diagnostics==null) {
			diagnostics = new BasicDiagnostic(source,code,message,data);
		}
		else
			diagnostics.add(new BasicDiagnostic(source,code,message,data));
	}

	public BasicDiagnostic getDiagnostics() {
		return diagnostics;
	}
}
