import sbt.Keys._
import sbt._
import sbtrelease.ReleasePlugin.autoImport.ReleaseKeys.skipTests
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

def runTests(project: Project) = ReleaseStep { st: State =>
  if (!st.get(skipTests).getOrElse(false)) {
    val extracted = Project.extract(st)
    extracted.runAggregated(vigilanceCore / Test / test, st)
  } else st
}

def runInProject(project: Project, command: String): ReleaseStep = {
  releaseStepCommandAndRemaining(s";project ${project.id}; $command; project ${vigilance.id}")
}

lazy val vigilance = (project in file("."))
  .settings(
    releaseCrossBuild := true,
    releaseUseGlobalVersion := true,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTests(vigilanceCore),
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      runInProject(vigilanceCore, "+publishSigned"),
      runInProject(vigilanceSbt,  "^publish"),
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )

lazy val vigilanceCore = VigilanceBuild.vigilanceCore
lazy val vigilanceSbt  = VigilanceBuild.vigilanceSbt

// for some reason '^vigilanceSbt/publishLocal' will only publish for sbt 1.X
addCommandAlias("pubLocal", ";+vigilanceCore/publishLocal; project vigilanceSbt; ^publishLocal ;project vigilance")