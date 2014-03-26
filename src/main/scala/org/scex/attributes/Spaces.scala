package org.scex.attributes

trait Spaces {
  val SpaceBefore = new Attribute[Distance]("SpaceBefore")
  val SpaceAfter = new Attribute[Distance]("SpaceAfter")
}
