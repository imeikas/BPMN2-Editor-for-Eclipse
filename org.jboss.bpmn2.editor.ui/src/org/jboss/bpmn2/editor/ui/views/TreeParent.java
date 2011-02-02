package org.jboss.bpmn2.editor.ui.views;

import java.util.ArrayList;

import org.eclipse.bpmn2.BaseElement;

class TreeParent extends TreeObject {
	private ArrayList<TreeObject> children;

	public TreeParent(String name) {
		super(name);
		children = new ArrayList<TreeObject>();
	}

	public TreeParent(BaseElement elem) {
		super(elem);
		children = new ArrayList<TreeObject>();
	}
	
	public void addChild(TreeObject child) {
		children.add(child);
		child.setParent(this);
	}

	public void removeChild(TreeObject child) {
		children.remove(child);
		child.setParent(null);
	}

	public TreeObject[] getChildren() {
		return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public void removeChildren() {
		children.clear();
    }
}
