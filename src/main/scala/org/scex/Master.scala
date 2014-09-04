package org.scex

/**
 * This is the root element of a document. All processor modifiers are
 * evaluated with the creation of this node.
 */
trait Master extends Builder {
  override lazy val children =
    collectedChildren map {
      case e: Element => evaluate(this, e)
      case o => o
    }

  private def evaluate(root: Element, e: Element): Node = {
    val children = e.children map {
      case e: Element => evaluate(this, e)
      case o => o
    }
    process(root, Element(children, e.modifiers))
  }

  private def process(root: Element, e: Element): Node = {
    e.modifiers.foldRight(Element(e.children): Node) {
      case (Modifier(p: Processor[_], v), node: Element) =>
        p(root: Element, node: Element, v)
      case (a, node) =>
       node.add(a)
    }
  }

}
