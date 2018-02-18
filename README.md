[ ![Download](https://img.shields.io/github/tag/pinguinson/vigilance.svg?label=release&colorB=007ec6) ](https://bintray.com/pinguinson/sbt-plugins/sbt-vigilance/_latestVersion)

# vigilance
Scala linter (fork of https://github.com/sksamuel/scapegoat)

## Usage

Add sbt plugin:
```scala
addSbtPlugin("com.github.pinguinson" % "sbt-vigilance" % "0.0.10")
```

Run inspections in sbt:
```scala
vigilance                 // runs for a whole project
vigilanceDiff branch_name // runs only for files in the diff against branch_name
```

## TODO

- [ ] log/print future instead of callback
- [ ] get rid of comparisons via toString
- [x] re-add tests
- [ ] move from scala-xml to scalatags
- [x] aliases for (local) publishing in sbt
- [ ] .drop.take -> .slice
- [ ] add sorting/filtering to html reports
- [x] use sbt-buildinfo
- [ ] add tests for catch operations
- [ ] fix variable shadowing inspection
- [x] fix MapGetAndGetOrElse tests (terribly inconsistent)


### Expected release steps for sbt-release

- [x] checkSnapshotDependencies
- [x] inquireVersions
- [x] runClean
- [x] runTest (for vigilanceCore only! doesn't work right now)
- [x] setReleaseVersion (write to `version.sbt` in root project)
- [ ] maybe update current version in `README.md` as well?
- [x] commitReleaseVersion
- [x] tagRelease
- [ ] publishArtifacts (for both `vigilanceCore` and `vigilanceSbt`)
- [x] setNextVersion (write to `version.sbt` in root project)
- [x] commitNextVersion
- [x] pushChanges