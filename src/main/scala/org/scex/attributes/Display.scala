package org.scex.attributes

trait Display {
  sealed class DisplayKind(val name: String)
  
  val block = new DisplayKind("block")
  val inline = new DisplayKind("inline")
  object table extends DisplayKind("table") {
    val cell = new DisplayKind("table-cell")
    val row  = new DisplayKind("table-row")
    val header = new DisplayKind("table-header")
    val body   = new DisplayKind("table-body")
    val footer = new DisplayKind("table-footer")
  }
  
  val Display = new Attribute[DisplayKind] 
}