name := """freitech-app"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"


libraryDependencies ++= Seq(
  cache
  ,filters
  ,ws
  ,"com.typesafe" % "config" % "1.3.1"
  ,"org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test
  ,"org.scalatest" % "scalatest_2.11" % "3.0.4" % "test"
  ,"org.mockito" % "mockito-core" % "2.11.0" % "test"
  ,"mysql" % "mysql-connector-java" % "5.1.42"
  ,"com.adrianhurt" %% "play-bootstrap" % "1.2-P25-B4"
  ,"org.scalikejdbc" %% "scalikejdbc" % "2.5.2"
  ,"org.scalikejdbc" %% "scalikejdbc-config" % "2.5.2"
  ,"org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.5.1"
  ,"org.flywaydb" %% "flyway-play" % "3.2.0"
  ,"com.opencsv" % "opencsv" % "4.0"
  ,"org.apache.pdfbox" % "pdfbox" % "2.0.7"
)
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource
sources in (Compile, doc) ~= (_ filter (_.getName endsWith ".java"))

