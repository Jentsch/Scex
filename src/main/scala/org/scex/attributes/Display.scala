package org.scex.attributes

trait Display {
  sealed class DisplayKind private[Display](val name: String) {
    override def toString = name
  }
  
  /**
   * Every element the is a block receive its own line.
   */
  val block = new DisplayKind("block")
  val inline = new DisplayKind("inline")
  
  /**
   * Contains all display kinds to describe table.
   * 
   * @note table is it self the starting point for a table definition.
   */
  object table extends DisplayKind("table") {
    /**
     * A single table cell.
     */
    val cell = new DisplayKind("table-cell")
    /**
     * A row of a table, should contains multiple [[cell]]s.
     */
    val row  = new DisplayKind("table-row")
    /**
     * If a tables breaks the header will be repeated. Together with [[body]] and 
     * [[footer]] the only direct children of a table. Good for headlines.
     */
    val header = new DisplayKind("table-header")
    val body   = new DisplayKind("table-body")
    val footer = new DisplayKind("table-footer")
  }
  
  val Display = new Attribute[DisplayKind]("Display")
}
