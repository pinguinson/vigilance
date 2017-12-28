[ ![Download](https://api.bintray.com/packages/pinguinson/sbt-plugins/sbt-vigilance/images/download.svg) ](https://bintray.com/pinguinson/sbt-plugins/sbt-vigilance/_latestVersion)

# vigilance
Scala linter (fork of https://github.com/sksamuel/scapegoat)

## Usage

Add sbt plugin:
```scala
addSbtPlugin("com.github.pinguinson" % "sbt-vigilance" % "0.0.7")
```

Run inspections in sbt:
```scala
vigilance                 // runs for a whole project
vigilanceDiff branch_name // runs only for files in the diff against branch_name
```

## TODO

- log/print future instead of callback
- get rid of comparisons via toString
- re-add tests
- move from scala-xml to scalatags
- aliases for (local) publishing in sbt
- .drop.take -> .slice
- add sorting/filtering to html reports
- use sbt-buildinfo
- add tests for catch operations
- fix variable shadowing inspection
- fix MapGetAndGetOrElse tests (terribly inconsistent)