name := """sena-web"""

version := "1.1.0-1.1"

lazy val core = (project in file("modules/sena-core/src")).enablePlugins(PlayJava).settings(javacOptions in (Compile,doc) += "-Xdoclit:none")

lazy val root = (project in file(".")).enablePlugins(PlayJava).dependsOn(core).aggregate(core)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.json" % "json" % "20160212"
)

TwirlKeys.templateImports ++= Seq(
  "yokohama.yellow_man.sena.views.helper.AppHelper"
)
