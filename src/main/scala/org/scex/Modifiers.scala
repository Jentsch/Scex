package org.scex
import scala.collection.generic.TraversableFactory

/**
 * A set of Modifiers. Is like a Map[Annotation[T], T], but that can't expressed
 * this way.
 */
trait Modifiers extends Iterable[Modifier[_]] with Inlineable {

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
  def | (node: Node)(implicit parent: Builder): Element = {
    val result = node add this
    parent register result
    result
  }

  def asMinorOf (that: Modifiers) =
    BatchModifiers(that, this)

  /**
   * Returns all annotations of this modifier.
   */
  def annotations = modifiers.map{_.annotation}.toSet

  override def toString = modifiers.mkString("Modifiers(", ", ",")")

  /**
   * Used for the string inpolation feature.
   */
  //TODO: refere to the documentation about the string interpolation
  def apply(params: Inlineable*)(implicit b: Builder): Element = {
    val sc = stringContext getOrElse sys.error("No StringContext given")
    val head: Text = sc.parts.head

    val tail: Seq[Node] = params zip sc.parts.tail flatMap {
      // finde modifers with append a append group
      case (m: Modifiers, text)
          if text.dropWhile(_.isWhitespace).headOption == Some('{') &&
             text.contains('}') =>
        val formated =
          text.dropWhile(_.isWhitespace).tail.takeWhile(_ != '}')
        val unaffected =
          text.dropWhile(_ != '}').tail
        if (unaffected.isEmpty)
          Seq(Text(formated) add m)
        else
          Seq(Text(formated) add m, Text(unaffected))
      case (m: Modifiers, text) =>
        val (formated, unaffected) =
          text.dropWhile(_.isWhitespace).span(! _.isWhitespace)
        if (unaffected.isEmpty)
          Seq(Text(formated) add m)
        else
          Seq(Text(formated) add m, Text(unaffected))
      case (n: Node, text) =>
        Seq(n, Text(text))
      case (any, text) =>
        Seq(Text(any.toString), Text(text))
    }

    val list = (head +: tail).foldRight[List[Node]](Nil){
      case (Text(""), ls) => ls
      case (Text(t1), Text(t2) :: ls) => Text(t1 + t2) :: ls
      case (h, ls) => h :: ls
    }

    this | Element(list)
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
