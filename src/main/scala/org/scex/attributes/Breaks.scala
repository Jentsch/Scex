package org.scex.attributes

trait Breaks {
  sealed class Break(val name: String) {
    override def toString = name
  }

  /**
   * Neither deny a page brake or force it.
   */
  val auto = new Break("auto")
  object page extends Break("page") {
    val odd = new Break("odd-page")
    val even = new Break("even-page")
  }
  val column = new Break("column")

  val BreakBefore = new Attribute[Break]("BreakBefore")
  val BreakAfter = new Attribute[Break]("BreakAfter")
}
