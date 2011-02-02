package org.jboss.bpmn2.editor.core.features;

import org.eclipse.graphiti.mm.StyleContainer;
import org.eclipse.graphiti.mm.algorithms.styles.Style;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

public class StyleUtil {
	
	private static final String CLASS_ID = "E-CLASS";
	private static final String TEXT_ID = "E-CLASS-TEXT";
	
	public static final IColorConstant CLASS_TEXT_FOREGROUND = IColorConstant.BLACK;
	public static final IColorConstant CLASS_FOREGROUND = new ColorConstant(146, 146, 208);
	public static final IColorConstant CLASS_BACKGROUND = new ColorConstant(220, 220, 255);

	public static Style getStyleForClass(Diagram diagram) {
		Style s = findStyle(diagram, CLASS_ID);
		
		if(s == null) {
			IGaService gaService = Graphiti.getGaService();
			s = gaService.createStyle(diagram, CLASS_ID);
			s.setForeground(gaService.manageColor(diagram, CLASS_FOREGROUND));
			s.setBackground(gaService.manageColor(diagram, CLASS_BACKGROUND));
			s.setLineWidth(1);
		}
		
		return s;
	}

	public static Style getStyleForText(Diagram diagram) {
		Style parentStyle = getStyleForClass(diagram);
		Style s = findStyle(parentStyle, TEXT_ID);
		
		if(s == null) {
			IGaService gaService = Graphiti.getGaService();
			s = gaService.createStyle(diagram, TEXT_ID);
			s.setForeground(gaService.manageColor(diagram, CLASS_TEXT_FOREGROUND));
		}
		
		return s;
	}

	private static Style findStyle(StyleContainer container, String id) {
		if (container.getStyles() != null) {
			for (Style s : container.getStyles()) {
				if (s.getId().equals(id)) {
					return s;
				}
			}
		}
		return null;
	}
}
