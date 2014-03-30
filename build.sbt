name := "scex"

version := "0.2-SNAPSHOT"

organization := "de.fu"

scalaVersion := "2.11.0-RC1"

scalacOptions <<= baseDirectory map {
  bd => Seq ("-sourcepath", bd.getAbsolutePath)
}

scalacOptions ++= Seq("-deprecation", "-feature", "-Xlint")

libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.0"

// Tests
libraryDependencies += "org.specs2" %% "specs2" % "2.3.10" % "test"

libraryDependencies += "org.pegdown" % "pegdown" % "1.2.1" % "test"

testOptions in Test += Tests.Argument("html")

// Docs

// API
scalacOptions in (Compile, doc) ++= Opts.doc.sourceUrl("https://github.com/Jentsch/Scex/blob/master€{FILE_PATH}.scala")

scalacOptions in (Compile, doc) ++= Opts.doc.title("Scex")
