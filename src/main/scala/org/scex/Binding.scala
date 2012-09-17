package org.scex

/**
 * Binds a Annotation with an proper value.
 */
class Binding[T](val annotation: Annotation[T], val value: T) 
	extends Tuple2(annotation, value)
	with Bindings {
  
  protected val bindings = List(this)
  
  override def toString = annotation.toString + "> " + value.toString
}

object Binding {
  def unapply[T](bin: Binding[T]) = Some((bin.annotation, bin.value))
}
