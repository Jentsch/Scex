package org.scex

sealed trait Annotation[T] {
  /**
   * Creates a binding.
   */
  def > (value:T) = new Binding(this, value)
  
  //XXX: Casting
  def unapply(binding: Binding[_]) = 
    if (binding.annotation == this)
      Some(binding.value.asInstanceOf[T])
    else
      None

  override def toString = this.getClass.getSimpleName.takeWhile(_ != '$')
}

object Annotation {
  class Attribute[T] extends Annotation[T]
  
  abstract class Processor[T] extends Annotation[T] {
    def apply(value: T, node: Element) =
	  process(value, node)
	
    def process(value: T, node: Element): Element
  }
  
  trait Toggle {
    this: Annotation[Boolean]=>
      
    def unary_+ = this > true
    def unary_- = this > false
    
  }
}
