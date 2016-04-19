
enablePlugins(CommonSettingsPlugin)
enablePlugins(NexusPlugin)

libraryDependencies ++= Seq(
  "log4j" % "log4j" % "1.2.17"
)

name := "scala-logger"

coverageEnabled := true
coverageOutputXML := false
