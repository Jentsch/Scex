name := "scex"

version := "0.2-SNAPSHOT"

organization := "de.fu"

description := "A Scala innerDSL to describe documents"

scalacOptions <<= baseDirectory map {
  bd => Seq ("-sourcepath", bd.getAbsolutePath)
}

scalacOptions ++= Seq("-deprecation", "-feature", "-Xlint")

// Tests
resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies += "org.specs2" %% "specs2" % "2.4.17" % "test"

libraryDependencies += "org.pegdown" % "pegdown" % "1.5.0" % "test"

scalacOptions in Test += "-Yrangepos"

// API
scalacOptions in (Compile, doc) ++= Opts.doc.sourceUrl("https://github.com/Jentsch/Scex/blob/master€{FILE_PATH}.scala")

scalacOptions in (Compile, doc) ++= Opts.doc.title("Scex")
