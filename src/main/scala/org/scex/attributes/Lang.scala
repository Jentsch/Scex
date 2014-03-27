package org.scex.attributes

sealed trait Lang {
  import Lang._

  def hypenOf(word: String): String = hyphen(word)

  private val hyphen: Map[String, String] =
    io.Source.fromInputStream(getClass.getResourceAsStream(toString + ".dic")).
    getLines.
    map(_.replace("-", "\u00AD")).
    flatMap{ word =>
      if (word contains '(')
        Seq(word takeWhile (_ != '('), word filterNot Bracket)
      else
        Seq(word)
    }.
    toTraversable.
    groupBy(_.replace("\u00AD", "")).
    mapValues(_.head).
    withDefault(identity)
}

private object Lang {
  val Bracket = Set('(', ')')
}

case object de extends Lang
case object en extends Lang

