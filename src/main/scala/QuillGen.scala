import scopt.OptionParser
import quill_gen.BuildInfo

case class ProgramOptions(
  outputDir: String = "target/quillGen/", //"src/main/scala/",
  packageName: String = "model.dao",
  classNames: List[String] = Nil
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

    arg[String]("Class1, Class2, etc")
      .action((value, programOptions) => programOptions.copy(classNames = value.trim.split(" ").toList))
      .text("Names of domain objects to generate DAOs for")

    help("help").text("prints this usage text")

    note(s"""
            |Generated quill-cache DAOs.
            |
            |Example usage:
            |  quillGen Blah                            // Write generated DAO for Blah to ${ defaultOptions.outputDir }/${ defaultOptions.packageDir }Blahs.scala
            |  quillGen Blah Foo Bar                    // Write generated DAOs for Blah, Foo and Bar to ${ defaultOptions.outputDir }/${ defaultOptions.packageDir }{Blah,Foo,Bar}.scala
            |  quillGen -d src/main/scala/ Blah         // Write generated DAO for Blah.scala to src/main/scala/${ defaultOptions.packageDir }
            |  quillGen -d src/main/scala/ -p "" Blah   // Write generated DAO for Blah to src/main/scala/Blah.scala
            |  quillGen -p "" Blah                      // Write generated DAO for Blah to ${ defaultOptions.outputDir }/Blah/scala
            |""".stripMargin)
  }

  implicit val options: ProgramOptions = parser.parse(args, ProgramOptions()).getOrElse {
    parser.showUsage()
    sys.exit(-1)
  }

  new Generator().run()
}
