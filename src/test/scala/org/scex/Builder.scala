package org.scex

import org.specs2._

class BuilderTest extends Specification {

  def TestCase = new Builder {
    val p = attributes.FontWeight > 700

    p"Paragraph 1"

    p"Paragraph 2"
  }

  def children = TestCase.children

  object textparts {
    def of(e: Element) = e.children.map(_.toText)
  }

  def is = s2"""
${"Builder".title}
  ${children must have length 2}
  ${textparts of TestCase must be equalTo Seq("Paragraph 1", "Paragraph 2")}
  """
}