package org.scex

sealed trait Node {
  /**
   * Creates a new Element by adding attributes to this Node.
   */
  def add(modifiers: Modifiers): Element

  /**
   * Combines two Nodes into one.
   */
  def +(that: Node): Element
}

object Node {
}

/**
 * Text elements contains only one string and have no attributes.
 */
final case class Text(val text: String) extends Node {
  def add(modifiers: Modifiers) =
    Element(List(this), modifiers)
  def +(that: Node) =
    Element(List(this, that), Modifiers.empty)
}

trait Element extends Node {
  def attributes: Modifiers
  def children: Seq[Node]

  def add(modifiers: Modifiers) = Element(children, attributes & modifiers)

  def +(that: Node) = that match {
    case text: Text if (this.attributes.isEmpty) => Element(children :+ text, attributes)
    case that: Element if (this.attributes.isEmpty && that.attributes.isEmpty) =>
      Element(this.children ++ that.children, Modifiers.empty)
      case that => Element(List(this, that), Modifiers.empty)
  }

  override def toString =
    attributes.mkString("Element(",", ", "") + children.mkString(" :",", ",")")
}

object Element {
  def apply(child: Seq[Node], attribute: Modifiers = Modifiers.empty): Element = new Element {
    val children = child
    val attributes = attribute
  }

  def unapply(element: Element) = Some((element.children, element.attributes))
}
