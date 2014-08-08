package org.scex

sealed trait Node extends Inlineable {
  /**
   * Creates a new Element by adding attributes to this Node. If this node isn't an element, it will be
   * wrapped by a element node with the modifiers.
   */
  final def add(modifiers: Modifiers): Element = this match {
    // An optimzation
    case Element(children, modifiersOld) => Element(children, modifiersOld & modifiers)
    case node => Element(node :: Nil, modifiers)
  }

  /**
   * Combines two Nodes into one.
   */
  def +(that: Node): Element =
    Element(this :: that :: Nil)

  def toText: String
}

/**
 * Text elements contains only one string and have no attributes.
 */
final case class Text(val text: String) extends Node {
  def toText: String = text
}

// TODO: need concept for external sources
final class Graphic private() extends Node {
  def toText: String = ""
}

trait Element extends Node {
  def modifiers: Modifiers
  def children: Seq[Node]

  override def toString: String =
    modifiers.mkString("Element(",", ", "") + children.mkString(" :",", ",")")

  def toText: String = children.map(_.toText).mkString(" ")
}

object Element {
  def apply(children: Seq[Node], modifiers: Modifiers = Modifiers.empty): Element = {
    val c = children
    val m = modifiers
    new Element {
      val children = c
      val modifiers = m
    }
  }

  def unapply(element: Element) = Some((element.children, element.modifiers))
}
