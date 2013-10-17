package org.scex

/**
 * Annotations for `Nodes`. Has two direct subclasses: Attribute and Processor.
 */
sealed trait Annotation[T] {
  /**
   * Creates a modifier.
   */
  def > (value:T) = new Modifier(this, value)
  
  //XXX: Casting
  def unapply(modifier: Modifier[_]) = 
    if (modifier.annotation == this)
      Some(modifier.value.asInstanceOf[T])
    else
      None
      
  val name: String

  override def toString = name
}

object Annotation {
  /**
   * Attributes are passiv data for a generator, e.g. FontFamily.
   */
  class Attribute[T](val name: String) extends Annotation[T]
  
  /**
   * A processor can modifiy annotated nodes.
   */
  abstract class Processor[T](val name: String) extends Annotation[T] {
    def apply(value: T, node: Element) =
      process(value, node)
	
    def process(value: T, node: Element): Element
  }
  
  /**
   * Inject usefull methods for Annotations of Booleans. Set 'Known Subclasses' below for
   * examples.
   */
  trait Toggle {
    this: Annotation[Boolean]=>

    /**
     * Allows to write `+ Enable` instand of `Enable > true` where `Enable` is an 
     * Annotation.
     */
    def unary_+ = this > true
    /**
     * Allows to write `- Enable` instand of `Enable > false` where `Enable` is an 
     * Annotation.
     */
    def unary_- = this > false
  }
}
