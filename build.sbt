ThisBuild / scalaVersion := "3.3.1"
ThisBuild / version := "0.1.0"
ThisBuild / organization := "com.example"

lazy val root = (project in file("."))
  .settings(
    name := "snake-game",
    libraryDependencies ++= {
      // Determine OS version for JavaFX dependencies
      val osName = System.getProperty("os.name") match {
        case n if n.startsWith("Linux") => "linux"
        case n if n.startsWith("Mac") => "mac"
        case n if n.startsWith("Windows") => "win"
        case _ => throw new Exception("Unknown platform!")
      }
      Seq(
        "org.openjfx" % "javafx-base" % "22.0.1" classifier osName,
        "org.openjfx" % "javafx-controls" % "22.0.1" classifier osName,
        "org.openjfx" % "javafx-graphics" % "22.0.2" classifier osName
      )
    }
  )
