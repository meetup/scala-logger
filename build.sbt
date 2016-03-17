
enablePlugins(CommonSettingsPlugin)
enablePlugins(ScalariformPlugin)
enablePlugins(NexusPlugin)

libraryDependencies ++= Seq(
  "log4j" % "log4j" % "1.2.17",
  "org.json4s" %% "json4s-native" % "3.3.0" % "test"
)

scalaVersion := "2.11.7"

name := "meetup-logger"

coverageEnabled := true
