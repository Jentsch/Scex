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

object PDF {
  sealed abstract class Output(val path:String, val mime:String)
  case class PostScript(override val path: String) extends Output(path, "application/postscript")

  val target = new File("demo.ps")

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

  private def body(node: Node) =
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="simpleA4" page-height="700px" page-width="1000px" margin-top="2cm" margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
          <fo:region-body/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simpleA4">
        <fo:flow flow-name="xsl-region-body">
          {content(node)}
 	</fo:flow>
       </fo:page-sequence>
     </fo:root>

  private def content(node: Node): scala.xml.Node = node match {
    case Text(text) =>
      scala.xml.Text(text)
    case Element(ch, at) => 
      scala.xml.Elem("fo", "block", metaData(at), scala.xml.TopScope, ch.map(content(_)) : _*)
  }

  private def metaData(at: Bindings) = {
    def attribute(name: String, value: String, prev: MetaData) =
      new UnprefixedAttribute(name, value, prev)

    at.foldLeft(scala.xml.Null : MetaData) {
      case (n, FontFamily(family)) => 
        attribute("font-family", family, n)
      case (n, TextAlign(align)) =>
        attribute("text-align", align, n)
      case (n, Binding(ann, v)) if (ptAttributes.isDefinedAt(ann))=>
        attribute(ptAttributes(ann), v + "pt", n)
      case (n, FontWeight(weight)) =>
        attribute("font-weight", weight.toString, n)
      case (n, WhiteSpaceCollapse(on)) =>
        attribute("white-space-collapse", if(on) "true" else "false", n)
      case (n, BreakBefore(Break(typ))) =>
        attribute("break-before", typ, n)
      case (n, BreakAfter(Break(typ))) =>
        attribute("break-after", typ, n)
      
	  // Ignore unknow attributes
      case (n, _) => n
    }
  }

  private val ptAttributes = Map[org.scex.Annotation[_], String] (
    FontSize -> "font-size",
    LineHeight -> "line-height",
    SpaceAfter -> "space-after",
    SpaceBefore -> "space-before"
    )
}
// vim: set ts=4 sw=4 et:
