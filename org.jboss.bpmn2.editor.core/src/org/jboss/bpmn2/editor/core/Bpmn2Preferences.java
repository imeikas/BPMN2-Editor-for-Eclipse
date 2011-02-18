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
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class Bpmn2Preferences {

	private static final String DROOLS_NAMESPACE = "http://www.jboss.org/drools";

	private final Preferences prefs;

	private static HashSet<EClass> elementSet = new HashSet<EClass>();

	private static final EStructuralFeature taskName;
	private final static EStructuralFeature waitFor;
	private static final EStructuralFeature independent;
	private static final EStructuralFeature ruleFlowGroup;
	private static final EStructuralFeature packageName;

	static {
		Bpmn2Package i = Bpmn2Package.eINSTANCE;
		elementSet.addAll(getSubClasses(i.getFlowElement()));
		elementSet.addAll(getSubClasses(i.getItemAwareElement()));
		elementSet.addAll(getSubClasses(i.getDataAssociation()));
		elementSet.addAll(getSubClasses(i.getRootElement()));
		//elementSet.addAll(getSubClasses(i.getEventDefinition()));
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

		ExtendedMetaData emd = new BasicExtendedMetaData();
		taskName = emd.demandFeature(DROOLS_NAMESPACE, "taskName", false);
		waitFor = emd.demandFeature(DROOLS_NAMESPACE, "waitForCompletion", false);
		independent = emd.demandFeature(DROOLS_NAMESPACE, "independent", false);
		ruleFlowGroup = emd.demandFeature(DROOLS_NAMESPACE, "ruleFlowGroup", false);
		packageName = emd.demandFeature(DROOLS_NAMESPACE, "packageName", false);
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
		HashSet<EAttribute> attribs = new HashSet<EAttribute>();
		ArrayList<ToolEnablement> ret = new ArrayList<ToolEnablement>();

		for (EClass e : elementSet) {
			ToolEnablement tool = new ToolEnablement();
			tool.setTool(e);
			tool.setEnabled(isEnabled(e));
			ret.add(tool);
			ArrayList<ToolEnablement> children = new ArrayList<ToolEnablement>();

			for (EAttribute a : e.getEAllAttributes()) {
				attribs.add(a);
				if (!("id".equals(a.getName()) || "anyAttribute".equals(a.getName()))) {
					ToolEnablement toolEnablement = new ToolEnablement(a, tool);
					toolEnablement.setEnabled(isEnabled(e));
					children.add(toolEnablement);
				}
			}

			ArrayList<EStructuralFeature> customAttributes = getAttributes(e);
			for (EStructuralFeature a : customAttributes) {
				attribs.add((EAttribute) a);
				if (!("id".equals(a.getName()) || "anyAttribute".equals(a.getName()))) {
					ToolEnablement toolEnablement = new ToolEnablement(a, tool);
					toolEnablement.setEnabled(isEnabled(e));
					children.add(toolEnablement);
				}
			}

			for (EReference a : e.getEAllContainments()) {
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

	public boolean isEnabled(String name) {
		return prefs.getBoolean(name, true);
	}

	public boolean isEnabled(String name, boolean b) {
		return prefs.getBoolean(name, b);
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

	public static ArrayList<EStructuralFeature> getAttributes(EClass eClass) {
		ArrayList<EStructuralFeature> ret = new ArrayList<EStructuralFeature>();

		if (Bpmn2Package.eINSTANCE.getTask().equals(eClass)) {
			ret.add(taskName);
		} else if (Bpmn2Package.eINSTANCE.getCallActivity().equals(eClass)) {
			ret.add(waitFor);
			ret.add(independent);
		} else if (Bpmn2Package.eINSTANCE.getBusinessRuleTask().equals(eClass)) {
			ret.add(ruleFlowGroup);
		} else if (Bpmn2Package.eINSTANCE.getProcess().equals(eClass)) {
			ret.add(packageName);
		}

		return ret;
	}

}
