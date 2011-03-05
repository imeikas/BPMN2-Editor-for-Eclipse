package org.jboss.bpmn2.editor.ui.property;

public class PropertyUtil {

	public static String deCamelCase(String string) {
		return string.replaceAll("([A-Z][a-z])", " $0").substring(1);
	}
}
