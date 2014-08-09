package org

import language.implicitConversions

/**
 * Scex is a tool to build documents in Scala.
 */
package object scex {
  implicit def strToText(str: String) = Text(str)

  implicit def strsToTexts(strs: Seq[String]) = strs.map(Text(_))

  private val _stringContext = new util.DynamicVariable[Option[StringContext]](None)
  def stringContext: Option[StringContext] = _stringContext.value
  def stringContext_=(cs: StringContext): Unit = {
    _stringContext.value = Some(cs)
  }
}

