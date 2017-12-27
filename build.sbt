cancelable := true

developers := List(
  Developer("mslinn",
            "Mike Slinn",
            "mslinn@micronauticsresearch.com",
            url("https://github.com/mslinn")
  )
)

// define the statements initially evaluated when entering 'console', 'console-quick', but not 'console-project'
initialCommands in console := """
                                |""".stripMargin

javacOptions ++= Seq(
  "-Xlint:deprecation",
  "-Xlint:unchecked",
  "-source", "1.8",
  "-target", "1.8",
  "-g:vars"
)

libraryDependencies ++= Seq(
  "com.github.scopt"  %% "scopt"       % "3.7.0" withSources(),
  "org.scalatest"     %% "scalatest"   % "3.0.4" % "test" withSources(),
  "junit"             %  "junit"       % "4.12"  % "test"
)

licenses +=  ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

logLevel := Level.Warn

// Only show warnings and errors on the screen for compilations.
// This applies to both test:compile and compile and is Info by default
logLevel in compile := Level.Warn

// Level.INFO is needed to see detailed output when running tests
logLevel in test := Level.Info

name := "quill-gen"

organization := "com.micronautics"

resolvers ++= Seq(
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-target:jvm-1.8",
  "-unchecked",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Xlint"
)

scalacOptions in (Compile, doc) ++= baseDirectory.map {
  (bd: File) => Seq[String](
     "-sourcepath", bd.getAbsolutePath,
     "-doc-source-url", "https://github.com/mslinn/{name.value}/tree/master€{FILE_PATH}.scala"
  )
}.value

scalaVersion := "2.12.4"

scmInfo := Some(
  ScmInfo(
    url(s"https://github.com/mslinn/$name"),
    s"git@github.com:mslinn/$name.git"
  )
)

sublimeTransitive := true

version := "0.2.0"

lazy val root = (project in file(".")).
  enablePlugins(BuildInfoPlugin).
  settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "quill_gen"
  )

assemblyMergeStrategy in assembly := {
  case ".gitkeep" => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
