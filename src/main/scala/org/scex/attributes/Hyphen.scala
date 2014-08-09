package org.scex.attributes

import org.scex._

object Hyphen extends Processor[Lang]("hypen") {
  def process(lang: Lang, elem: Element): Element =
    Element(elem.children map (process(lang, _)), elem.modifiers)

  def process(lang: Lang, node: Node): Node = node match {
    case elem: Element =>
      process(lang, elem)
    case Text(text) =>
      Text(withHypen(text, lang))
    case node =>
      node
  }

  // TODO: Create a version with more performance
  private[attributes] def withHypen(text: String, lang: Lang): String = {
    val (nonWord, rest) = text span (!_.isLetter)
    val (word, rest2) = rest span (_.isLetter)

    nonWord + lang.hypenOf(word) + (if (rest2.isEmpty) "" else withHypen(rest2, lang))
  }
}
