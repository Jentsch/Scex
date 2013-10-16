package org.scex

import org.scex._

package object attributes
	extends Breaks
	with Color.PreDefs
	with Display 
	with Font
	with Text
	with Spaces {
  
  type Attribute[T] = org.scex.Attribute[T]

  class Toggle(name: String) 
    extends Attribute[Boolean](name)
    with Annotation.Toggle

  val PreText = new Annotation.Processor[String]("PreText") {
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
  
  val WrapLines = new Annotation.Processor[Modifiers]("WrapLines")  {
    def process(value: Modifiers, elem: Element) = {
      val Element(Seq(Text(text)), binds) = elem
      println(elem)
      
      new Builder {
      	text.lines.map(s => value | s).to[Seq]
      }
    }
  }
  
  val HighLightKeyWords = new Annotation.Processor[Modifiers]("WrapLines") {
    def process(value: Modifiers, elem: Element) = {
      val Element(Seq(Text(text)), binds) = elem
      
      new Builder {
        
      }
    }
  }
}
