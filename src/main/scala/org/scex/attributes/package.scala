package org.scex

import org.scex._

package object attributes
	extends Breaks
	with Color.PreDefs
	with Display
	with Font
	with Spaces {

  private[attributes] type Attribute[T] = org.scex.Attribute[T]

  object PreText extends Processor[String]("PreText") {
    private def helper(value: String, node: Node): Node = node match {
      case Element(first :: others, attr) =>
        Element(helper(value, first) :: others, attr)
      case Element(none, attr) =>
        Element(Text(value) +: none, attr)
      case Text(text) =>
        Text(value+text)
      case node =>
        node
    }

    def process(value: String, elem: Element) =
      helper(value, elem) match {
        case result: Element =>
          result
        case _ =>
          ???
      }

	}

  val TextAlign = new Attribute[String]("TextAlign")

  val TextColor = new Attribute[Color]("TextColor")

  final class TextTransform private[attributes](val name: String)
  /**
   *  Puts the first character of each word in uppercase.
   */
  val capitalize = new TextTransform("capitalize")
  /** Puts all characters of each word in uppercase. */
  val uppercase  = new TextTransform("uppercase")
  /** Puts all characters of each word in lowercase */
  val lowercase  = new TextTransform("lowercase")

  val TextTransform = new Attribute[TextTransform]("TextTransform")

  val TextUnderline = new Toggle("TextUnderline")
  val TextOverline = new Toggle("TextOverline")
  val TextLineThrought = new Toggle("TextLineThrought")
  /** May not supported by all generators. */
  val TextBlink = new Toggle("TextBlink")

  implicit class RichDouble(val int: Double) {
    def mm = Distance.millimeter(int)
    def cm = Distance.centimeter(int)
    def pt = Distance.point(int)
  }
}

