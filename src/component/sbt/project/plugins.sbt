resolvers +=  Resolver.url(
  "meetup-sbt-plugins",
  new java.net.URL("https://dl.bintray.com/meetup/sbt-plugins/")
)(Resolver.ivyStylePatterns)

addSbtPlugin("com.meetup" % "sbt-plugins" % "0.2.17")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.4")
