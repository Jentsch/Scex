package org.scex

import scala.language.implicitConversions
import scala.collection.mutable.ListBuffer

/**
 * Helper to create new nodes. Use one of its subclasses to create your own document.
 */
@annotation.implicitNotFound(msg = "Use this method inside a Builder.")
trait Builder extends Element {
  def modifiers = Modifiers.empty

  /** So far collected children in ''reverse'' order, it's a stack */
  private var buffer: List[Node] = Nil
  lazy val children = collectedChildren
  /** All so far collected children, should be called only once to init children. */
  protected def collectedChildren = buffer.reverse

  /** Used by children to register them self. */
  protected implicit val self: Builder = this

  /** Adds a node to this Builder */
  private[scex] def register(n: Node): Unit =
    buffer = n :: buffer

  /**
   * Used to allow Modifiers as String Interpolator. Don't do this at home kids.
   */
  implicit protected def toStringContext(sc: StringContext): this.type = {
    stringContext = sc
    this
  }
}

