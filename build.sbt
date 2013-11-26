name := "Scex"

version := "0.2-SNAPSHOT"

organization := "de.fu"

scalaVersion := "2.10.3"

scalacOptions <<= baseDirectory map {
  bd => Seq ("-sourcepath", bd.getAbsolutePath)
}

scalacOptions ++= Seq("-deprecation", "-feature")

scalacOptions in doc ++= Opts.doc.sourceUrl("https://github.com/Jentsch/Scex/blob/master€{FILE_PATH}.scala")

scalacOptions in doc ++= Opts.doc.title("Scex")

