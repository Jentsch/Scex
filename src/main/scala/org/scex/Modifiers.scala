package org.scex
import scala.collection.generic.TraversableFactory

/**
 * A set of Modifiers. Is like a Map[Annotation[T], T], but that can't expressed
 * this way.
 */
trait Modifiers extends Iterable[Modifier[_]] {

  protected val modifiers: Seq[Modifier[_]]

  def iterator = modifiers.iterator

  // XXX: Is in O(n), should be O(log n)
  def get[T](annotation: Annotation[T]) =
    modifiers.
    collectFirst{case annotation(t) => t}

  def isDefinedAt(annotation: Annotation[_]) =
    get(annotation) != None

  def apply[T](annotation: Annotation[T]) =
    get(annotation).get
	
  def & (that: Modifiers) =
    this ++ that
	
  override def filter(condition: Modifier[_] => Boolean) =
    Modifiers(modifiers filter condition)
 
  override def filterNot(condition: Modifier[_] => Boolean) =
    Modifiers(modifiers filterNot condition)

  def ++ (that: Modifiers) =
    Modifiers(modifiers.filterNot{bind => that.annotations.contains(bind.annotation)} ++ that.modifiers)

  /**
   * Binds attributes to a node a append the node to a builder.
   */
  def | (node: Node)(implicit parent: Builder) =
    parent.register(node add this)

  def asMinorOf (that: Modifiers) =
    BatchModifiers(that, this)

  /**
   * Returns all annotations of this modifier.
   */
  def annotations = modifiers.map{_.annotation}.toSet

  override def toString = modifiers.mkString("Modifiers(", ", ",")")

  def apply(params: Any*)(implicit b: Builder) = {
    val sc = stringContext getOrElse sys.error("No StringContext given")
    this | sc.s(params : _*)
  }
}

object Modifiers {
  def apply(binds: Seq[Modifier[_]]): Modifiers = binds match {
    case Seq(single) => single
	case _ => new Modifiers {
      val modifiers = binds
    }
  }

  val empty = Modifiers(List.empty)
}
