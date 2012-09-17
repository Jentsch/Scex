package org.scex
import scala.collection.generic.TraversableFactory

/**
 * A set of bindings. Is like a Map[Annotation[T], T].
 */
trait Bindings extends Iterable[Binding[_]] {
  
  protected val bindings: Seq[Binding[_]]
  
  def iterator = bindings.iterator
  
  // XXX: Is in O(n), should be O(log n)
  def get[T](annotation: Annotation[T]) =
    bindings.
    collectFirst{case annotation(t) => t}
  
  def isDefinedAt(annotation: Annotation[_]) =
    get(annotation) != None
    
  def apply[T](annotation: Annotation[T]) =
    get(annotation).get
	
  def & (that: Bindings) =
    this ++ that
	
  override def filter(condition: Binding[_] => Boolean) =
    Bindings(bindings filter condition)
  
  override def filterNot(condition: Binding[_] => Boolean) = 
	Bindings(bindings filterNot condition)
  
  def ++ (that: Bindings) = 
    Bindings(bindings.filterNot{bind => that.annotations.contains(bind.annotation)} ++ that.bindings)
  
  /**
   * Binds attributes to a node a append the node to a builder.
   */
  def | (node: Node)(implicit parent: Builder) =
    parent.register(node add this)

  def asMinorOf (that: Bindings) =
    RowBindings(that, this)
    
  /**
   * Returns all annotations of this binding.
   */
  def annotations = bindings map {_.annotation} toSet

  override def toString = bindings.mkString("Bindings(", ", ",")")
}

object Bindings {
  def apply(binds: Seq[Binding[_]]): Bindings = binds match {
    case Seq(single) => single
	case _ => new Bindings {
      val bindings = binds
    }
  }
  
  val empty = Bindings(List.empty)
}
