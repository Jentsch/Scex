package org.scex

/**
 * Annotations for `Nodes`. Has two direct subclasses: Attribute and Processor.
 *
 * @param name Name of this annotation
 *
 * @define name annotation
 * @define Name Annotation
 */
sealed abstract class Annotation[T](val name: String) {
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

  override def toString = name
}

/**
 * Attributes are passiv data for a generator, e.g. FontFamily.
 */
class Attribute[T](name: String) extends Annotation[T](name)

/**
 * A processor can modifiy annotated nodes.
 */
abstract class Processor[T](name: String) extends Annotation[T](name) {
  private[scex] def apply(value: T, node: Element) =
    process(value, node)

  /**
   * Process the assigned element.
   */
  protected[scex] def process(value: T, element: Element): Element
}

