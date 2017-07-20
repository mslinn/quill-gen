import java.io.File
import java.nio.file.Files

object Generator {
  def write(text: String, file: File): Unit = {
    val bw = new java.io.BufferedWriter(new java.io.FileWriter(file))
    bw.write(text)
    bw.close()
  }

  def outputDirectory(implicit options: ProgramOptions): File =
    new File(options.outputDir, options.packageName.replace(".", "/"))

  def outputPathFQ(className: String)(implicit options: ProgramOptions): File =
    new File(outputDirectory, s"$className.scala")
}

class Generator(implicit options: ProgramOptions) {
  import Generator._

  protected lazy val template: String = io.Source.fromResource("Template.scala").mkString

  def run(): Unit =
    options.classNames foreach { className =>
      val result = template.replace("Template", className)
      Files.createDirectories(outputDirectory.toPath)
      Generator.write(result, outputPathFQ(className))
    }
}
