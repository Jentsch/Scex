package org.scex

package object attributes {
  type Attribute[T] = org.scex.Attribute[T]

  val bold = FontWeight > 700
  
  final case class Break private[attributes](val name: String)
  val auto = Break("auto")
  val column = Break("column")
  val page = Break("page")
  val evenPage = Break("evenPage")
  val oddPage = Break("oddPage")
  
  object BreakAfter extends Attribute[Break]
  object BreakBefore extends Attribute[Break]
  
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
	  case Text(text) => 
		Text(value+text)
	  }
  }
}