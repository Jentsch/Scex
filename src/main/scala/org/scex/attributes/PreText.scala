package org.scex.attributes

import org.scex._

/**
 * Preappends a string to node.
 */
object PreText extends Processor[String]("PreText") {
  def process(value: String, elem: Element) =
    helper(value, elem) match {
      case result: Element =>
        result
      case _ =>
        // Enforce by type system
        sys.error("This should happend")
    }

  private def helper(value: String, node: Node): Node = node match {
    case Element(first :: others, attr) =>
      Element(helper(value, first) :: others, attr)
    case Element(none, attr) if none.isEmpty =>
      Element(Text(value) +: none, attr)
    case Text(text) =>
      Text(value+text)
    case node =>
      node
  }
}
