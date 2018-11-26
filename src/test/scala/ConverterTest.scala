import java.nio.file.{Files, Path, Paths}
import java.util.stream.Collectors

import org.scalatest.FlatSpec

class ConverterTest extends FlatSpec{
  private def getFromFile(path: Path) = {
    Files.newBufferedReader(path).lines().collect(Collectors.joining("\n"))
  }

  private def convertToMathML(testNumber: Int) = {
    val pathToTex = Paths.get(s"src/test/resources/tex/$testNumber.tex")
    Converter.fromFile(pathToTex)
  }

  private def testEqual(testNumber: Int) = {
    val mathML = convertToMathML(testNumber)
    val pathToMathML = Paths.get(s"src/test/resources/mathML/$testNumber.html")
    assert(mathML == getFromFile(pathToMathML))
  }

  "Tex to MathML converter" should "convert trivial expression" in {
    testEqual(1)
  }

  it should "convert * and + expressions" in {
    testEqual(2)
  }
}
