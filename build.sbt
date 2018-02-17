lazy val vigilance = project in file(".")

lazy val vigilanceCore = VigilanceBuild.vigilanceCore
lazy val vigilanceSbt  = VigilanceBuild.vigilanceSbt

// for some reason '^vigilanceSbt/publishLocal' will only publish for sbt 1.X
addCommandAlias("pubLocal", ";+vigilanceCore/publishLocal; project vigilanceSbt; ^publishLocal ;project vigilance")
addCommandAlias("releaseArtifacts", ";+vigilanceCore/release ;^vigilanceSbt/publish")