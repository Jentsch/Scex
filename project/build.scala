import sbt._
import Keys._

object ScexBuild extends Build {
  lazy val main = Project(id = "main",
                          base = file("."))

  lazy val example = Project(id = "example",
                         base = file("example")) dependsOn main
}
