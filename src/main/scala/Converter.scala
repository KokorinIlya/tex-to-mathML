import java.nio.file.{Files, Path}

import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream}
import parser.{TexLexer, TexParser}
import utils.IOUtils

object Converter {
  private def fromReader(reader: ANTLRInputStream): String = {
    val lexer = new TexLexer(reader)
    val tokens = new CommonTokenStream(lexer)
    val parser = new TexParser(tokens)
    parser.texstring().expr.toMathML
  }

  def fromFile(path: Path): String = {
    IOUtils.using(Files.newInputStream(path)) { inputStream =>
      fromReader(new ANTLRInputStream(inputStream))
    }
  }

  def fromString(tex: String): String = {
    fromReader(new ANTLRInputStream(tex))
  }
}
