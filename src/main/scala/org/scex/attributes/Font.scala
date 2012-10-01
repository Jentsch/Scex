package org.scex.attributes

trait Font {
  object FontWeight extends Attribute[Int]
  val bold = FontWeight > 700
  
  val FontFamily = new Attribute[String]
  val FontSize   = new Attribute[Int]
}