import scopt.OptionParser
import quill_gen.BuildInfo

case class ProgramOptions(
  outputDir: String = "target/quillGen/", //"src/main/scala/",
  packageName: String = "model.dao",
  classNames: List[String] = Nil,
  cacheType: CacheType = CacheType.STRONG
) {
  val packageDir: String = {
    val x = packageName.replace(".", "/")
    if (x.endsWith("/")) x else s"$x/"
  }
}

object QuillGen extends App {
  val defaultOptions: ProgramOptions = ProgramOptions()
  val parser = new OptionParser[ProgramOptions]("quillGen") {
    head("QuillGen", BuildInfo.version)

    opt[String]('p', "package")
      .action((value, programOptions) => programOptions.copy(packageName = value))
      .text(s"Package for generated quill-cache DAOs (defaults to ${ defaultOptions.packageName })")

    opt[String]('d', "outputDir")
      .action((value, programOptions) => programOptions.copy(outputDir = value))
      .text(s"Directory to write generated quill-cache DAOs to (defaults to ${ defaultOptions.outputDir })")

    opt[String]('c', "cache")
      .validate( string =>
        try {
          CacheType.valueOf(string.toUpperCase)
          Right(Unit)
        } catch {
          case _: Throwable =>
            Left(s"Cache values are case insensitive. Valid cache values are ${ CacheType.NONE }, ${ CacheType.SOFT } and ${ CacheType.STRONG }")
        }
      )
      .action((value, programOptions) => programOptions.copy(cacheType = CacheType.valueOf(value.toUpperCase)))
      .text(s"Type of cache for the DAOs being created (case insensitive, can be ${ CacheType.NONE }, ${ CacheType.SOFT } or ${ CacheType.STRONG }, defaults to ${ CacheType.STRONG })")

    arg[String]("Class1, Class2, etc")
      .action((value, programOptions) => programOptions.copy(classNames = value.trim.split(" ").toList))
      .text("Names of domain objects to generate DAOs for")

    help("help").text("prints this usage text")

    note(s"""
            |Generate quill-cache DAOs.
            |
            |Example usages using the included bin/run script:
            |  - Write generated DAO for Blah to ${ defaultOptions.outputDir }${ defaultOptions.packageDir }Blahs.scala
            |    $$ bin/run Blah
            |
            |  - Write generated DAOs for Blah, Foo and Bar to ${ defaultOptions.outputDir }${ defaultOptions.packageDir }{Blahs,Foos,Bars}.scala
            |    $$ bin/run Blah Foo Bar
            |
            |  - Write generated DAO for Blah.scala to src/main/scala/${ defaultOptions.packageDir }
            |    $$ bin/run -d src/main/scala/ Blah
            |
            |  - Write generated DAO for Blah to src/main/scala/Blahs.scala
            |    $$ bin/run -d src/main/scala/ -p "" Blah
            |
            |  - Write generated DAO for Blah to ${ defaultOptions.outputDir }Blahs.scala
            |    $$ bin/run -p "" Blah
            |""".stripMargin)
  }

  implicit val options: ProgramOptions = parser.parse(args, ProgramOptions()).getOrElse {
    parser.showUsage()
    sys.exit(-1)
  }

  new Generator().run()
}
