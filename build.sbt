enablePlugins(CommonSettingsPlugin)
enablePlugins(NexusPlugin)

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "org.slf4j" % "slf4j-log4j12" % "1.7.21"
)

name := "scala-logger"

coverageOutputXML := false
