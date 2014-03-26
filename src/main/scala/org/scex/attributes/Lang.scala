package org.scex.attributes

sealed trait Lang {
  def hypenOf(word: String): String = hyphen(word)

  private val hyphen: Map[String, String] =
    io.Source.fromInputStream(getClass.getResourceAsStream(toString + ".dic")).
    getLines.
    map(_.replace("-", "\u00AD")).
    toTraversable.
    groupBy(_.replace("\u00AD", "")).
    mapValues(_.head).
    withDefault(identity)
}

case object de extends Lang
case object en extends Lang

