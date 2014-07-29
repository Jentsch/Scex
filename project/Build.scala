import sbt._
import Keys._

object ScexBuild extends Build {
  val web = TaskKey[File]("web", "Creates api doc and tests'")

  val webTask =
    web := {
      val log = streams.value.log

      val target = new File("target/web/")

      (test in Test).value
      val tests: File = new File("target/specs2-reports")

      val docs = (doc in Compile).value

      IO.createDirectory(target)
      IO.copyDirectory(docs, target / "api")
      IO.copyDirectory(tests, target / "tests")

      log.success("Generated web page placed at " + target)

      target
    }

  lazy val root = project.aggregate(main, example)

  lazy val main =
    Project(id = "main", base = file(".")) settings (
      webTask,
      scalaVersion := "2.11.2"
    )

  lazy val example =
    Project(id = "example", base = file("example")) dependsOn main settings (
      description := "Some simple examples to learn Scex",
      scalaVersion := "2.11.2"
    )
}
