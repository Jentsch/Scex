package org.scex.attributes

/**
 * Text from [http://www.w3.org/TR/xsl/#text-transform]
 */
trait Text {
  val TextAlign = new Attribute[String]
  
  val TextColor = new Attribute[Color]
  
  sealed class TextTransform(val name: String)
  /** Puts the first character of each word in uppercase. */
  val capitalize = new TextTransform("capitalize")
  /** Puts all characters of each word in uppercase. */
  val uppercase  = new TextTransform("uppercase")
  /** Puts all characters of each word in lowercase */
  val lowercase  = new TextTransform("lowercase")
  
  val TextTransform = new Attribute[TextTransform]
  
  val TextUnderline = new Toggle
  val TextOverline = new Toggle
  val TextLineThrought = new Toggle
  val TextBlink = new Toggle
}