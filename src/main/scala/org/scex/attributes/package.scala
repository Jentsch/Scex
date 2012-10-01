package org.scex

package object attributes
	extends Breaks
	with Color.PreDefs
	with Display 
	with Font
	with Text
	with Spaces {
  
  type Attribute[T] = org.scex.Attribute[T]
  class Toggle extends Attribute[Boolean] with org.scex.Annotation.Toggle

  val PreText = new Annotation.Processor[String] {
    def process(value: String, elem: Element) = {
	  helper(value, elem) match {
	    case result: Element => result
		case _ => throw new RuntimeException
	  }
	}
	
    private def helper(value: String, elem: Node): Node = elem match {
	  case Element(first :: others, attr) => 
		Element(helper(value, first) :: others, attr)
	  case Element(none, attr) =>
	    Element(Text(value) +: none, attr)
	  case Text(text) => 
		Text(value+text)
	  }
  }
}