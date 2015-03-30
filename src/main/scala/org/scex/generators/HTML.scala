package org.scex.generators

import org.scex._
import org.scex.attributes._
import xml.{ Text => XText }
import xml.{ Node => XNode }
import xml.{ Elem => XElem }
import xml.{ TopScope, Null }
import java.net.URL

object HTML {
  def apply(node: Node): XNode =
    <html>
      <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
      </head>
      <body>
        { content(node) }
      </body>
    </html>

  private def content(node: Node): XNode = node match {
    case Text(text) =>
      XText(text)
    case Element(children, modifiers) =>
      chooseFirst(modifiers)(
        <span style={ style(modifiers) }>{ children map content }</span>)(
          Choice(Display) {
            case `block` =>
              <div style={ style(modifiers) }>{ children map content }</div>
          },
          Choice(Link) {
            case url =>
              <a style={ style(modifiers) } href={ url.toString }>{ children map content }</a>
          })
  }

  private case class Choice[A, R](annotation: Annotation[A])(val f: PartialFunction[A, R])

  private def chooseFirst[R](modifiers: Modifiers)(default: => R)(choices: Choice[_, R]*): R = choices match {
    case empty if empty.isEmpty => default
    case (choice: Choice[_, _]) +: rest =>
      modifiers.get(choice.annotation).collect(choice.f).
        getOrElse(chooseFirst(modifiers)(default)(rest: _*))
  }

  private def style(atts: Modifiers) =
    atts.collect {
      case Modifier(TextUnderline, true) =>
        "text-decoration: underline"
      case Modifier(name, value) if name != Link =>
        toCSS(name) + ": " + value.toString
    }.mkString("; ")

  private def toCSS(anno: Annotation[_]) = {
    val name = anno.name
    nameTranslation.getOrElse(name, genericToCSS(name))
  }

  private def genericToCSS(name: String) =
    name.foldLeft("") {
      case (r, upper) if (upper.isUpper) =>
        r + '-' + upper.toLower
      case (r, lower) =>
        r + lower
    }.tail

  private val nameTranslation = Map(
    "TextColor" -> "color",
    "SpaceAfter" -> "margin-bottom",
    "SpaceBefore" -> "margin-top")
}

