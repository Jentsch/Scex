package org.scex.attributes

trait Font {
  object FontWeight extends Attribute[Int]("FontWeight")
  val bold = FontWeight > 700
  
  val FontFamily = new Attribute[String]("FontFamily")
  val FontSize   = new Attribute[Int]("FontSize")
}