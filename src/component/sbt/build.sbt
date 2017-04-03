enablePlugins(CommonSettingsPlugin)

val projectDir = file("./../../..")
val projectVersion = s"make -s -C $projectDir version".!!.trim

libraryDependencies += "com.meetup" %% "scala-logger" % projectVersion

version := "0.0.1"

assemblyJarName in assembly := "component-test.jar"
