package org.scex

import scala.language.implicitConversions

/**
 * Helper to create new nodes. Use one of its subclasses to create your own document.
 */
@annotation.implicitNotFound(msg = "Use this method inside a Builder.")
trait Builder extends Element {
  protected implicit val self: Builder = this

  private var buffer = List.empty[Node]
  lazy val children = buffer.map {
    case child: Element =>
      val base = child.children match {
        case Seq(n: Text) => n
        case children => Element(children)
      }
      child.modifiers.foldLeft(base) {
        case (elem: Element, bind @ Modifier(_: Attribute[_], _)) =>
          Element(elem.children, elem.modifiers & bind)
        case (elem: Element, Modifier(proc: Processor[_], value)) =>
          //FIXME: This 'self' isn't correct
          proc(self, elem, value)
      }
    case node => node
  }

  val modifiers = Modifiers.empty

  private[scex] def register(n: Node) {
    buffer = buffer :+ n
  }

  /**
   * Used to allow Modifiers as String Interpolator. Don't do this at home kids.
   */
  implicit protected def toStringContext(sc: StringContext): this.type = {
    stringContext = sc
    this
  }
}

