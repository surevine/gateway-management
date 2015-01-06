name := """gateway-management"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "postgresql" % "postgresql" % "9.1-901-1.jdbc4"

libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "3.6.0.201412230720-r"

javaOptions in Test += "-Dconfig.file=conf/application.test.conf"

javaOptions += "-Dconfig.file=conf/application.db.conf"