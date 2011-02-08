package org.jboss.bpmn2.editor.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EReference;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class Bpmn2Preferences {

	private final Preferences prefs;

	private static HashSet<EClass> elementSet = new HashSet<EClass>();

	static {
		Bpmn2Package i = Bpmn2Package.eINSTANCE;
		elementSet.addAll(getSubClasses(i.getFlowElement()));
		elementSet.addAll(getSubClasses(i.getItemAwareElement()));
		elementSet.addAll(getSubClasses(i.getDataAssociation()));
		elementSet.addAll(getSubClasses(i.getRootElement()));
		elementSet.addAll(getSubClasses(i.getEventDefinition()));
		elementSet.addAll(getSubClasses(i.getLoopCharacteristics()));
		elementSet.addAll(getSubClasses(i.getExpression()));
		elementSet.add(i.getOperation());
		elementSet.add(i.getLane());
		elementSet.add(i.getEscalation());
		elementSet.add(i.getPotentialOwner());
		elementSet.add(i.getResourceAssignmentExpression());
		elementSet.add(i.getInputSet());
		elementSet.add(i.getOutputSet());
		elementSet.add(i.getAssignment());
		elementSet.add(i.getAssociation());
		elementSet.add(i.getTextAnnotation());
	}

	private Bpmn2Preferences(Preferences prefs) {
		this.prefs = prefs;
	}

	public static Bpmn2Preferences getPreferences(IProject project) {
		IEclipsePreferences rootNode = Platform.getPreferencesService().getRootNode();
		Preferences prefs = rootNode.node(ProjectScope.SCOPE).node(project.getName())
				.node("org.jboss.bpmn2.editor.tools");
		return new Bpmn2Preferences(prefs);
	}

	public List<ToolEnablement> getAllElements() {
		ArrayList<ToolEnablement> ret = new ArrayList<ToolEnablement>();

		for (EClass e : elementSet) {
			ToolEnablement tool = new ToolEnablement();
			tool.setTool(e);
			tool.setEnabled(isEnabled(e));
			ret.add(tool);
			ArrayList<ToolEnablement> children = new ArrayList<ToolEnablement>();

			for (org.eclipse.emf.ecore.EAttribute a : e.getEAllAttributes()) {
				if (!("id".equals(a.getName()) || "anyAttribute".equals(a.getName()))) {
					ToolEnablement toolEnablement = new ToolEnablement(a, tool);
					toolEnablement.setEnabled(isEnabled(e));
					children.add(toolEnablement);
				}
			}

			for (org.eclipse.emf.ecore.EReference a : e.getEAllContainments()) {
				ToolEnablement toolEnablement = new ToolEnablement(a, tool);
				toolEnablement.setEnabled(isEnabled(e));
				children.add(toolEnablement);
			}
			tool.setChildren(children);
		}

		Collections.sort(ret, new Comparator<ToolEnablement>() {

			@Override
			public int compare(ToolEnablement o1, ToolEnablement o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}

		});
		return ret;
	}

	public boolean isEnabled(EClass element) {
		return prefs.getBoolean(element.getName(), true);
	}

	public boolean isEnabled(EAttribute element) {
		return prefs.getBoolean(element.getEContainingClass().getName() + "." + element.getName(), true);
	}

	public boolean isEnabled(EReference element) {
		return prefs.getBoolean(element.getEContainingClass().getName() + "." + element.getName(), true);
	}

	public void setEnabled(ToolEnablement tool, boolean enabled) {
		prefs.putBoolean(tool.getPreferenceName(), enabled);
	}

	public boolean isEnabled(ToolEnablement tool) {
		return prefs.getBoolean(tool.getPreferenceName(), true);
	}

	public void flush() throws BackingStoreException {
		prefs.flush();
	}

	public static List<EClass> getSubClasses(EClass parentClass) {

		List<EClass> classList = new ArrayList<EClass>();
		EList<EClassifier> classifiers = Bpmn2Package.eINSTANCE.getEClassifiers();

		for (EClassifier classifier : classifiers) {
			if (classifier instanceof EClass) {
				EClass clazz = (EClass) classifier;

				clazz.getEAllSuperTypes().contains(parentClass);
				if (parentClass.isSuperTypeOf(clazz) && !clazz.isAbstract()) {
					classList.add(clazz);
				}
			}
		}
		return classList;
	}

	public void importPreferences(String path) throws FileNotFoundException, IOException, BackingStoreException {
		Properties p = new Properties();
		p.load(new FileInputStream(path));

		for (Object k : p.keySet()) {
			Object object = p.get(k);
			if (k instanceof String && object instanceof String) {
				prefs.putBoolean((String) k, Boolean.parseBoolean((String) object));
			}
		}
		prefs.flush();
	}

	public void export(String path) throws BackingStoreException, FileNotFoundException, IOException {
		FileWriter fw = new FileWriter(path);

		List<String> keys = Arrays.asList(prefs.keys());
		Collections.sort(keys);
		for (String k : keys) {
			fw.write(k + "=" + prefs.getBoolean(k, true) + "\r\n");
		}
		fw.flush();
		fw.close();
	}
}
