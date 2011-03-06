package org.jboss.bpmn2.editor.utils;

public class Tuple<A extends Object, B extends Object> {

	private A first;
	private B second;

	public Tuple(A first, B second) {
		this.first = first;
		this.second = second;
	}

	public A getFirst() {
		return first;
	};

	public B getSecond() {
		return second;
	}
}
