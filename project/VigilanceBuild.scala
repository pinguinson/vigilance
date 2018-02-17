import bintray.BintrayPlugin
import bintray.BintrayKeys._
import com.typesafe.sbt.SbtPgp.autoImportImpl.PgpKeys
import sbt.Keys._
import sbt.Opts.resolver._
import sbt._
import sbtbuildinfo.BuildInfoPlugin.autoImport._
import sbtbuildinfo.{BuildInfoKey, BuildInfoPlugin}
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._
import sbtrelease.ReleasePlugin.autoImport._

object VigilanceBuild {

  lazy val githubUrl = url("https://github.com/pinguinson/vigilance")

  lazy val commonSettings = Seq(
    organization  := "com.github.pinguinson",
    scalaVersion  := "2.12.4",
    crossScalaVersions := Seq("2.11.11", scalaVersion.value),
    scalacOptions := Seq(
      "-unchecked",
      "-deprecation",
      "-feature",
      "-encoding", "utf8"
    ),
    javacOptions ++= Seq(
      "-source", "1.8",
      "-target", "1.8"
    )
  ) ++ publishSettings

  lazy val publishSettings = Seq(
    licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    homepage := Some(githubUrl),
    scmInfo  := Some(ScmInfo(
      browseUrl  = githubUrl,
      connection = "git@github.com:pinguinson/vigilance.git"
    )),
    developers += Developer(
      id    = "pinguinson",
      name  = "Nikita Gusak",
      email = "nikita.penguin@gmail.com",
      url   = url("https://github.com/pinguinson")
    ),
    parallelExecution in Test     := false,
    publishArtifact in Test       := false,
    publishMavenStyle             := true,
    publishTo                     := Some(if (isSnapshot.value) sonatypeSnapshots else sonatypeStaging),
    releaseCrossBuild             := true,
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    pomIncludeRepository          := Function.const(false)
  )

  lazy val vigilanceCore = (project in file("vigilance-core"))
    .settings(commonSettings)
    .disablePlugins(BintrayPlugin)
    .settings(
      name := "scalac-vigilance-plugin",
      scalacOptions ++= Seq(
        "-Xmax-classfile-name", "254",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen"
      ),
      libraryDependencies ++= Seq(
        "org.scala-lang.modules" %% "scala-xml"      % "1.0.6", //move to scalatags and remove this
        "com.lihaoyi"            %% "scalatags"      % "0.6.7",
        "org.scala-lang"         %  "scala-reflect"  % scalaVersion.value,
        "org.scala-lang"         %  "scala-compiler" % scalaVersion.value % Provided,
        "org.scala-lang"         %  "scala-compiler" % scalaVersion.value % Test,
        "commons-io"             %  "commons-io"     % "2.5"              % Test,
        "org.scalatest"          %% "scalatest"      % "3.0.4"            % Test,
        "org.mockito"            %  "mockito-all"    % "1.10.19"          % Test,
        "joda-time"              %  "joda-time"      % "2.9.9"            % Test,
        "org.joda"               %  "joda-convert"   % "1.9.2"            % Test,
        "org.slf4j"              %  "slf4j-api"      % "1.7.25"           % Test,
        "com.typesafe.akka"      %% "akka-actor"     % "2.5.9"            % Test
      ),
      fullClasspath in (Compile, console) ++= (fullClasspath in Test).value, // because that's where "PluginRunner" is
      releaseUseGlobalVersion := true
    )

  lazy val vigilanceSbt = (project in file("vigilance-sbt"))
    .enablePlugins(BuildInfoPlugin)
    .settings(
      buildInfoKeys := Seq[BuildInfoKey](
        version      in vigilanceCore,
        organization in vigilanceCore,
        name         in vigilanceCore
      ).map(k => BuildInfoKey.map(k) { case (key, value) => "vigilance" + key.capitalize -> value }),
      buildInfoPackage    := "com.github.pinguinson.vigilance.buildinfo",
      publishMavenStyle   := false,
      bintrayRepository   := "sbt-plugins",
      bintrayOrganization := None,
      organization        := "com.github.pinguinson",
      name                := "sbt-vigilance",
      sbtPlugin           := true,
      crossSbtVersions    := Seq("0.13.16", "1.0.3")
    )
}
