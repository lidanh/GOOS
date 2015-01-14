name := "GOOS"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "2.4.15" % "test",
  "org.scala-lang" % "scala-swing" % "2.11+",
  "com.googlecode.windowlicker" % "windowlicker-core" % "r268",
  "com.googlecode.windowlicker" % "windowlicker-swing" % "r268",
  "org.igniterealtime.smack" % "smack" % "3.2.1",
  "org.igniterealtime.smack" % "smackx" % "3.2.1"
)

scalacOptions in Test ++= Seq("-Yrangepos")

// Read here for optional jars and dependencies:
// http://etorreborre.github.io/specs2/guide/org.specs2.guide.Runners.html#Dependencies

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)