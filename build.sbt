name := "scala-scripts"

lazy val commonSettings = Seq(
  version := "0.1",
  organization := "com.teamgehem",
  scalaVersion := "2.13.5",
  libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % "3.2.7",
    "org.scalatest" %% "scalatest" % "3.2.7" % "test",
    "com.github.pathikrit" %% "better-files" % "3.9.1"
  ),
  assembly / test := {}
)

lazy val wii = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    assembly / mainClass := Some("com.teamgehem.scala_scripts.wii.WiiFileNameChanger"),
    assembly / assemblyOutputPath := baseDirectory.value / "dist" / "wiiFileNameChanger.jar"
  )
