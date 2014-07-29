package org.scex.sample

import org.scex._
import org.scex.attributes._

object Welcome extends templates.Run {
  title"Scex"

  subtitle"An innerDSL to describe Documents"

  p"Follow on ${Link > "http://github.com"} GitHub"
}
