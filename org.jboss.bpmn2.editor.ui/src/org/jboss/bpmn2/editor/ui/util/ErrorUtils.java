package org.jboss.bpmn2.editor.ui.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.jboss.bpmn2.editor.ui.Activator;

public class ErrorUtils {
	public static void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR,
				Activator.PLUGIN_ID, IStatus.OK, message, null);
		Platform.getLog(Activator.getDefault().getBundle()).log(status);
		throw new CoreException(status);
	}

}
