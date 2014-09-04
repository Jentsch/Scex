package org.scex

import org.scex.attributes.{red, TextColor}
import org.specs2._

class ReferenceTest extends Specification {
  object TestCase extends templates.General with Master {
    val a = p"b is: ${refText(b)}"
    val b = p"Bla"
    val c = p"b is: ${refText(b)}"
  }

  object text{
    def of (n: Node) = n match {
      case e: Element => e.children.map(_.toText).mkString("")
      case n: Node => n.toText
    }
  }

  import TestCase._

  def is = s2"""
${"Inline format".title}
  ${text of children(0) must be equalTo "b is: Bla"}
  ${text of children(1) must be equalTo "Bla"}
  ${text of children(2) must be equalTo "b is: Bla"}
  """
}

