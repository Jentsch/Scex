import sbt._
import Keys._

object ScexBuild extends Build {
  val page = TaskKey[Unit]("page", "Prints 'Hello World!'")

  val pageTask = {
    page := {
      (test in Test).value
      (doc in Compile).value

      val docs: File = new File("target/scala-2.11.0-RC1/api")
      val tests: File = new File("target/specs2-reports")

      IO.copyDirectory(new File("web"), new File("target/web"))
      IO.copyDirectory(docs, new File("target/web/api"))
      IO.copyDirectory(tests, new File("target/web/tests"))
    }
  }

  lazy val main = Project(id = "main",
                          base = file(".")) settings pageTask

  lazy val example = Project(id = "example",
                         base = file("example")) dependsOn main
}
