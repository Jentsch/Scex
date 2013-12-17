package org.scex

import org.scex._

package object attributes
	extends Breaks
	with Color.PreDefs
	with Display
	with Font
	with Text
	with Spaces {

  private[attributes] type Attribute[T] = org.scex.Attribute[T]

  class Toggle(name: String)
    extends Attribute[Boolean](name)
    with org.scex.Toggle

  object PreText extends Processor[String]("PreText") {
    private def helper(value: String, elem: Node): Node = elem match {
      case Element(first :: others, attr) =>
      Element(helper(value, first) :: others, attr)
      case Element(none, attr) =>
        Element(Text(value) +: none, attr)
      case Text(text) =>
        Text(value+text)
    }

    def process(value: String, elem: Element) =
      helper(value, elem) match {
        case result: Element =>
          result
        case _ =>
          throw new RuntimeException
      }

	}
}
