import java.io.File
import java.nio.file.Files

object Generator {
  def write(text: String, file: File): Unit = {
    val bw = new java.io.BufferedWriter(new java.io.FileWriter(file))
    bw.write(text)
    bw.close()
  }

  def outputDirectory(implicit options: ProgramOptions): File =
    new File(options.outputDir, options.packageDir)

  def outputPathFQ(className: String)(implicit options: ProgramOptions): File =
    new File(outputDirectory, s"$className.scala")
}

class Generator(implicit options: ProgramOptions) {
  import Generator._

  protected lazy val template: String = io.Source.fromResource("Template.scala").mkString

  protected def extendString(className: String): String = options.cacheType match {
    case CacheType.NONE =>
      s"UnCachedPersistence[Long, Option[Long], $className]"

    case CacheType.SOFT =>
      s"""CachedPersistence[Long, Option[Long], $className]
         |    with SoftCacheLike[Long, Option[Long], $className]""".stripMargin

    case CacheType.STRONG =>
      s"""CachedPersistence[Long, Option[Long], $className]
         |    with StrongCacheLike[Long, Option[Long], $className]""".stripMargin
  }

  def run(): Unit = {
    options.classNames foreach { className =>
      val result = template
                     .replace("$className", className)
                     .replace("$package", options.packageName)
                     .replace("$extend", extendString(className))
      Files.createDirectories(outputDirectory.toPath)
      Generator.write(result, outputPathFQ(className))
    }
  }
}
