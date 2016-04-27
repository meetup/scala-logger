resolvers += "Nexus" at "https://nexus.blt.meetup.com/content/repositories/releases"

addSbtPlugin("com.meetup" % "sbt-plugins" % "15.0.0")

addSbtPlugin("com.eed3si9n" % "sbt-dirty-money" % "0.1.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.5")

addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.1.0")
