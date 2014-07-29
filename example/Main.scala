package org.scex.sample

import org.scex._
import org.scex.attributes._
import org.scex.templates._

object Main extends App {
  private val examples = Map(
    "HTML" -> HTML,
    "PDF" -> PDF,
    "Welcome" -> Welcome)

  args.headOption getOrElse("HTML") match {
    case "all" =>
      examples.values foreach (_.main(args drop 1))
    case name =>
      examples get name foreach (_.main(args drop 1))
  }
}
