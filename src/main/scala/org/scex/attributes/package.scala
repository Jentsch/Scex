package org.scex

import org.scex._
import java.net.URL

package object attributes
  extends Breaks
  with Color.PreDefs
  with Display {

  /**
   * Imports the Attribute type into this package.
   */
  private[attributes]type Attribute[T] = org.scex.Attribute[T]

  val PreText = Processor("PreText") { (elem: Element, value: String) =>
    def helper(value: String, node: Node): Node = node match {
      case Element(first :: others, attr) =>
        Element(helper(value, first) :: others, attr)
      case Element(none, attr) =>
        Element(Text(value) +: none, attr)
      case Text(text) =>
        Text(value + text)
      case node =>
        node
    }

    helper(value, elem)
  }

  val Hyphen = Processor("hypen") { (node: Node, lang: Lang) =>
    def process(lang: Lang, node: Node): Node = node match {
      case elem: Element =>
        Element(elem.children map (process(lang, _)), elem.modifiers)
      case Text(text) =>
        Text(lang addHypenTo text)
      case node =>
        node
    }

    process(lang, node)
  }

  object Link extends Attribute[URL]("Link") {
    def >(str: String): Modifier[URL] =
      this > new URL(str)
  }

  val TextAlign = new Attribute[String]("TextAlign")

  val TextColor = new Attribute[Color]("TextColor")

  /**
   *  Puts the first character of each word in uppercase.
   */
  val capitalize = new TextTransform("capitalize")
  /** Puts all characters of each word in uppercase. */
  val uppercase = new TextTransform("uppercase")
  /** Puts all characters of each word in lowercase */
  val lowercase = new TextTransform("lowercase")

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

  val SpaceBefore = new Attribute[Distance]("SpaceBefore")
  val SpaceAfter = new Attribute[Distance]("SpaceAfter")

  /**
   * Weight of the font as specified in TrueType between 100 and 900.
   */
  val FontWeight = new Attribute[Int]("FontWeight")
  val bold = FontWeight > 700

  val FontFamily = new Attribute[String]("FontFamily")
  val FontSize = new Attribute[Distance]("FontSize")

  val LineHeight = new Attribute[Int]("LineHeight")
}

package attributes {
  final class TextTransform private[attributes] (val name: String)
}

