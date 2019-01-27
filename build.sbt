
name := "mvc"
organization := "com.alab"

version := "1.0" 
      
lazy val `mvc` = (project in file(".")).enablePlugins(PlayJava)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

scalaVersion := "2.12.7"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "com.alab" %% "config" % "0.0.1"