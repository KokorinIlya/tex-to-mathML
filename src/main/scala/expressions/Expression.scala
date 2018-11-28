package expressions

import java.nio.file.{Files, Paths}
import java.util.stream.Collectors

import utils.IOUtils

sealed trait Expression {
  def toMathML: String
}

case class TexExpression(inner: Expression) extends Expression {
  override def toMathML: String = {
    TexExpression.template.replace("MATHML_CODE_HERE", inner.toMathML)
  }
}

object TexExpression {
  private def getTemplate() = {
    val path = Paths.get("src/main/resources/MathMLTemplate.html")
    IOUtils.using(Files.newBufferedReader(path)) { reader =>
      reader.lines().collect(Collectors.joining("\n"))
    }
  }

  private val template = getTemplate()
}

case class Sup(basic: Expression, upper: Expression) extends Expression {
  override def toMathML: String = {
    s"<msup> ${basic.toMathML} ${upper.toMathML} </msup>"
  }
}

case class Sub(basic: Expression, under: Expression) extends Expression {
  override def toMathML: String = {
    s"<msub> ${basic.toMathML} ${under.toMathML} </msub>"
  }
}

case class SubSup(basic: Expression, under: Expression, upper: Expression) extends Expression {
  override def toMathML: String = {
    s"<msubsup> ${basic.toMathML} ${under.toMathML} ${upper.toMathML} </msubsup>"
  }
}

sealed trait BinaryOperation extends Expression {
  val left: Expression
  val right: Expression
}

sealed trait SymbolicBinaryOperaration extends BinaryOperation {
  protected val symbol: Char

  override def toMathML: String = {
    s"<mrow> ${left.toMathML} <mo>$symbol</mo> ${right.toMathML} </mrow>"
  }
}

case class Plus(override val left: Expression, override val right: Expression) extends SymbolicBinaryOperaration {
  override protected val symbol: Char = '+'
}

case class Minus(override val left: Expression, override val right: Expression) extends SymbolicBinaryOperaration {
  override protected val symbol: Char = '-'
}

case class Mul(override val left: Expression, override val right: Expression) extends SymbolicBinaryOperaration {
  override protected val symbol: Char = '*'
}

case class Div(override val left: Expression, override val right: Expression) extends BinaryOperation {
  override def toMathML: String = {
    s"<mfrac> ${left.toMathML} ${right.toMathML} </mfrac>"
  }
}

case class Eq(override val left: Expression, override val right: Expression) extends BinaryOperation {
  override def toMathML: String = {
    s"<mrow> ${left.toMathML} <mo>=</mo> ${right.toMathML} </mrow>"
  }
}

sealed trait UnaryOperation extends Expression {
  val operand: Expression
}

case class UnaryMinus(override val operand: Expression) extends UnaryOperation {
  override def toMathML: String = {
    s"<mo>-</mo> ${operand.toMathML}"
  }
}

case class Parenthesis(inner: Expression) extends Expression {
  override def toMathML: String = {
    s"<mrow><mo>(</mo> ${inner.toMathML} <mo>)</mo> </mrow>"
  }
}

case class LiteralHolder(literal: String) extends Expression {
  override def toMathML: String = {
    s"<mn> $literal </mn>"
  }
}
