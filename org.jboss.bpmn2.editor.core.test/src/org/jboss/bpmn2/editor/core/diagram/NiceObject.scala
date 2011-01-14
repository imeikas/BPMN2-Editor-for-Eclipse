package org.jboss.bpmn2.editor.core.diagram

class NiceObject[T <: AnyRef](x: T) {
  def niceClass: Class[_ <: T] = x.getClass.asInstanceOf[Class[T]]
}

object NiceObject{
	  implicit def toNiceObject[T <: AnyRef](x: T) = new NiceObject(x)
}
