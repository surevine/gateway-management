name := """gateway-management"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)

libraryDependencies += "commons-io" % "commons-io" % "2.4"

javaOptions in Test += "-Dconfig.file=conf/application.test.conf"