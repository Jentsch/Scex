package org.scex

sealed trait Node {
  /**
   * Creates a new node by adding attributes
   */
  def add(bindings: Bindings): Node
  
  /**
   * Combines to nodes.
   */
  def + (that: Node): Element
}

object Node {

  /**
   * Helper to create new nodes.
   */
  trait Builder extends Element {
    protected implicit val self = this

    private var buffer = List.empty[Node]
    lazy val children = buffer.map { 
      case child: Element =>
        child.attributes.foldLeft(Element(child.children, Bindings.empty): Element) {
          case (elem:Element, bind @ Binding(_: Attribute[_], _)) => Element(elem.children, elem.attributes & bind)
          case (elem:Element, Binding(proc: Processor[_], value)) => proc(value, elem)
        }
      case text: Text => text
    }
	
    val attributes = Bindings.empty
		
    private[scex] def register(n: Node) {
      buffer = buffer :+ n
    }

    import scala.language.implicitConversions
    /**
     * Used to allow Bindings as String Interpolator
     */
    implicit protected def toStringContext(sc: StringContext): this.type = {
      stringContext = sc
      this
    }
  }
  
  /**
   * Text elements contains only one string and have no attributes.
   */
  final case class Text(val text: String) extends Node {
    def add(bindings: Bindings) = Element(List(this), bindings)
	def +  (that: Node) =
	  Element(List(this, that), Bindings.empty)
  }
  
  trait Element extends Node {
    def attributes: Bindings
    def children: Seq[Node]

    def add(bindings: Bindings) = Element(children, attributes & bindings)
	
	def +  (that: Node) = that match {
	  case text: Text if (this.attributes.isEmpty) => Element(children :+ text, attributes)
	  case that: Element if (this.attributes.isEmpty && that.attributes.isEmpty) =>
	    Element(this.children ++ that.children, Bindings.empty)
      case that => Element(List(this, that), Bindings.empty)
	}

    override def toString =
      attributes.mkString("Element(",", ", "") + children.mkString(" :",", ",")")
    
  }
  object Element {
    def apply(child: Seq[Node], attribute: Bindings = Bindings.empty): Element = new Element {
      val children = child
      val attributes = attribute
    }

    def unapply(element: Element) = Some((element.children, element.attributes))
  }
}
