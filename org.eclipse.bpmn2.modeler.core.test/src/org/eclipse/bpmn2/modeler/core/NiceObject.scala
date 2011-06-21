package org.eclipse.bpmn2.modeler.core

class NiceObject[T <: AnyRef](x: T) {
  def niceClass: Class[_ <: T] = x.getClass.asInstanceOf[Class[T]]
}

object NiceObject{
	  implicit def toNiceObject[T <: AnyRef](x: T) = new NiceObject(x)
}
