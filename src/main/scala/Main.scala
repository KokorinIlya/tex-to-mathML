import parser.TexLexer
import parser.TexParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream

import java.nio.file.Files
import java.nio.file.Paths


object Main {
  def main(args: Array[String]): Unit = {
    val pathToInputFile = Paths.get("input.txt")
    val reader = new ANTLRInputStream(Files.newInputStream(pathToInputFile))
    val lexer = new TexLexer(reader)
    val tokens = new CommonTokenStream(lexer)
    val parser = new TexParser(tokens)
    val expression = parser.texstring().expr
    System.out.println(expression.toMathML)
  }
}
