package org.scex.generators

import org.scex._
import org.scex.attributes._

import xml.{Text => XText}
import xml.{Node => XNode}
import xml.{Elem => XElem}
import xml.{TopScope, Null}

object HTML {
  def apply(node: Node): XNode =
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  </head>
  <body>
    { content(node) }
  </body>
</html>

  private def content(node: Node): XNode = node match {
    case Text(text) =>
      XText(text)

    case Element(ch, atts) if atts.get(Display).map(_ == block).getOrElse(false) =>
      <div style={style(atts)}>{ch map content}</div>

    case Element(ch, atts) if atts.get(Link).isDefined =>
      <a style={style(atts)} href={atts(Link).toString}>{ch map content}</a>

    case Element(ch, atts) =>
      <span style={style(atts)}>{ch map content}</span>

    case unknow =>
      ???
  }

  private def style(atts: Modifiers) =
    atts.collect {
        case Modifier(name, value) if name != Link =>
          toCSS(name) + ": " +value.toString
    }.mkString("; ")

  private def toCSS(anno: Annotation[_]) = {
    val name = anno.name
    exception.getOrElse(name, genericToCSS(name))
  }

  private def genericToCSS(name: String) =
    name.foldLeft(""){
      case (r, upper) if (upper.isUpper) =>
        r + '-' + upper.toLower
      case (r, lower) =>
        r + lower
    }.tail

  private val exception = Map(
    "TextColor" -> "color",
    "SpaceAfter" -> "margin-bottom",
    "SpaceBefore" -> "margin-top")
}

