package org.scex

class RowBindings(val major: Bindings, val minor: Bindings) {
  import RowBindings._

  def | (node: Node)(implicit parent: Builder) : RowElement = {
    val element = new RowElement(major, minor)
    parent.register(element)
	
	element | node
  }
  
  def | (nodes: Seq[Node])(implicit parent: Builder) {
    var element = new RowElement(major, minor)
	parent.register(element)
	
	// XXX: Side effects
	nodes foreach { element | _ }
  }
}

object RowBindings {
  def apply(major: Bindings, minor: Bindings) = 
    new RowBindings(major, minor)

  class RowElement(
    override val attributes: Bindings,
    val minor: Bindings)
    extends Element with Builder {
	
	def |(node: Node) = {
	  register(node add minor)
	  this
	}
	
	def |(nodes: Iterable[Node]): RowElement =
	  nodes.foldLeft(this)(_ | _)
	
	def |(texts: Seq[String]): RowElement =
	  this | texts.map(Text(_))
  }
}