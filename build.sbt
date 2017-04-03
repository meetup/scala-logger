enablePlugins(CommonSettingsPlugin)
enablePlugins(CoverallsWrapper)

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "org.slf4j" % "slf4j-log4j12" % "1.7.21",
  "net.logstash.log4j" % "jsonevent-layout" % "1.7"
)

name := "scala-logger"

coverageOutputXML := false

resolvers += "softprops-maven" at "http://dl.bintray.com/content/softprops/maven"

bintrayOrganization in ThisBuild := Some("meetup")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
