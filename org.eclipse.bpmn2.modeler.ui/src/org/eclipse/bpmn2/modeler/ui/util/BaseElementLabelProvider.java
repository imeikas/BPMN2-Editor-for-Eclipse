package org.eclipse.bpmn2.modeler.ui.util;

import java.util.Arrays;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Expression;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

/**
 * Label Provider for ComboBox elements in the BPMN property section
 * 
 * @author adrobisch
 * 
 */
public class BaseElementLabelProvider extends AdapterFactoryLabelProvider {
	
	List<String> featureLabelClasses = Arrays.asList(new String[] {FormalExpression.class.getSimpleName(), Expression.class.getSimpleName()}); 
	
	boolean addId = false;
	
	private String formatString = "%s %s";
	
	public BaseElementLabelProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}
	
	public BaseElementLabelProvider(AdapterFactory adapterFactory, boolean addId) {
		super(adapterFactory);
		this.addId = addId;
		if (addId){
			formatString+=" (id=%s)";
		}
	}
	
	@Override
	public String getText(Object element) {
		if (element == null) {
			return "";
		} else if (element instanceof BaseElement) {
			
			BaseElement baseElement = (BaseElement) element;
			String featureName = baseElement.eContainingFeature().getName();
			
			if (featureLabelClasses.contains(baseElement.eClass().getName())){
				return String.format(formatString, baseElement.eClass().getName(), featureName,
						baseElement.getId());
			}else{
				return String.format(formatString, baseElement.eClass().getName(), "", baseElement.getId());
			}
			
		}
		return element.toString();
	}

}
