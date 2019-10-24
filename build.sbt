enablePlugins(CommonSettingsPlugin)
enablePlugins(CoverallsWrapper)

crossScalaVersions := Seq("2.11.12", "2.12.10")

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.28",
  "org.slf4j" % "slf4j-log4j12" % "1.7.28",
  "net.logstash.log4j" % "jsonevent-layout" % "1.7",

  // Override scalatest and scalacheck versions in CommonSettingsPlugin to versions that are compatible with scala 2.12
  "org.scalatest" %% "scalatest" % "3.0.8",
  "org.scalacheck" %% "scalacheck" % "1.14.2" % "test,component,it"
)

name := "scala-logger"

coverageOutputXML := false

resolvers += "softprops-maven" at "http://dl.bintray.com/content/softprops/maven"

bintrayOrganization in ThisBuild := Some("meetup")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
