package org.scex.generators

import org.scex._
import org.scex.attributes._
import xml.{ Text => XText }
import xml.{ Node => XNode }
import xml.{ Elem => XElem }
import xml.{ TopScope, Null, NamespaceBinding, UnprefixedAttribute, MetaData }
import java.net.URL

object HTML {
  val nameSpace = new NamespaceBinding("ii", "ii", null);

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
      val childrenContent = children map content
      val attrs = Map("style" -> style(modifiers)).filterNot(_._2.isEmpty)

      chooseFirst(modifiers)(
        elem("span", attrs, childrenContent))(
          Choice(Display) {
            case `block` =>
              elem("div", attrs, childrenContent)
          },
          Choice(Link) {
            case url =>
            elem("a", attrs + ("href" -> url.toString), childrenContent)
          })
  }

  private def elem(name: String, attributes: Map[String, String], children: Seq[XNode]) = {
    val meta: MetaData = attributes.foldLeft(Null: MetaData) {
      case (coll, (key, value)) => new UnprefixedAttribute(key, value, coll)
    }

    XElem(null, name, meta, nameSpace, true, children: _*)
  }

  private case class Choice[A, R](annotation: Annotation[A])(val f: PartialFunction[A, R])

  private def chooseFirst[R](modifiers: Modifiers)(default: => R)(choices: Choice[_, R]*): R = choices match {
    case empty if empty.isEmpty => default
    case (choice: Choice[_, _]) +: rest =>
      modifiers.get(choice.annotation).collect(choice.f).
        getOrElse(chooseFirst(modifiers)(default)(rest: _*))
  }

  private def style(atts: Modifiers): String =
    atts.collect {
      case Modifier(TextUnderline, true) =>
        "text-decoration: underline"
      case Modifier(BreakBefore, page) =>
        "page-break-before:always"
      case Modifier(BreakAfter, page) =>
        "page-break-after:always"
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

