lazy val root = (project in file(".")).
  settings(
    name := "Tutorial6_FuturesPromises",
    version := "1.0",
    scalaVersion := "2.11.7"
  )

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.0",
  "com.typesafe.play" %% "play-ws" % "2.5.0",
  "com.typesafe.play" %% "play-json" % "2.5.0",
  "org.scalactic" %% "scalactic" % "2.2.6",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)
