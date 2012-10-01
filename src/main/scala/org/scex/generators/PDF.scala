package org.scex.generators

import org.scex._
import attributes._
import java.io._
import javax.xml.transform._
import javax.xml.transform.stream._
import sax._
import org.apache.fop.apps._
import scala.xml.UnprefixedAttribute
import scala.xml.MetaData
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder

object PDF {
  sealed abstract class Output(val path: String, val mime: String)
  case class PostScript(override val path: String) extends Output(path, "application/postscript")

  val target = new File("demo.ps")
  val xmlTarget = new File("demo.xml")

  private val fopFactory = FopFactory.newInstance

  def apply(node: Node) {
    val foUserAgent = fopFactory.newFOUserAgent

    val src = new StreamSource(new StringReader(body(node).toString))

    val out = new BufferedOutputStream(new FileOutputStream(target))

    val fop = fopFactory.newFop("application/postscript", foUserAgent, out);

    val factory = TransformerFactory.newInstance();
    val transformer = factory.newTransformer();

    val res = new SAXResult(fop.getDefaultHandler());
    transformer.transform(src, res)
    val foResults = fop.getResults

    println("Target: " + target.getAbsolutePath)
    println("Generated " + foResults.getPageCount() + " pages in total.")
  }

  def printXML(node: Node) {
    val a = new java.io.PrintWriter(xmlTarget)

    try {
      a.write(body(node).toString)
    } finally {
      a.close
    }
  }

  private def body(node: Node) =
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="simpleA4" page-height="297mm" page-width="210mm" margin-top="2cm" margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
          <fo:region-body/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simpleA4">
        <fo:flow flow-name="xsl-region-body">
          { content(node) }
        </fo:flow>
      </fo:page-sequence>
    </fo:root>

  private def content(node: Node): scala.xml.Node = node match {
    case Text(text) =>
      scala.xml.Text(text)
    case Element(ch, attributes) =>
      val display = attributes.get(Display).map(_.name).getOrElse("block")

      scala.xml.Elem("fo", display, metaData(attributes), scala.xml.TopScope, false, ch.map(content(_)): _*)
  }

  private def metaData(at: Bindings) = {
    def attribute(name: String, value: String, prev: MetaData) =
      new UnprefixedAttribute(name, value, prev)

    val preresult = at.foldLeft(scala.xml.Null: MetaData) {
      case (n, FontFamily(family)) =>
        attribute("font-family", family, n)
      case (n, TextAlign(align)) =>
        attribute("text-align", align, n)
      case (n, Binding(ann, v)) if (ptAttributes.isDefinedAt(ann)) =>
        attribute(ptAttributes(ann), v + "pt", n)
      case (n, FontWeight(weight)) =>
        attribute("font-weight", weight.toString, n)
      case (n, WhiteSpaceCollapse(on)) =>
        attribute("white-space-collapse", if (on) "true" else "false", n)
      case (n, BreakBefore(break)) =>
        attribute("break-before", break.name, n)
      case (n, BreakAfter(break)) =>
        attribute("break-after", break.name, n)
      case (n, TextTransform(transform)) =>
        attribute("text-transform", transform.name, n)
      case (n, TextColor(color)) =>
        import color._

        attribute("color", s"rgb($r, $g, $b)", n)
      // Ignore unknown attributes
      case (n, _) => n
    }

    decorations(at) match {
      case None => preresult
      case Some(dec) => attribute("text-decoration", dec, preresult)
    }
  }

  private def decorations(at: Bindings) = {
    val map = Map(
      TextUnderline -> "underline",
      TextOverline -> "overline",
      TextLineThrought -> "line-through")

    val may = map.map {
      case (key, value) =>
        at.get(key) map {
          case true => map(key)
          case false => "no-" + map(key)
        }
    }.toSeq.flatten

    if (may.isEmpty)
      None
    else
      Some(may.mkString(" "))
  }

  private val ptAttributes = Map[org.scex.Annotation[_], String](
    FontSize -> "font-size",
    LineHeight -> "line-height",
    SpaceAfter -> "space-after",
    SpaceBefore -> "space-before")
}
// vim: set ts=4 sw=4 et:
