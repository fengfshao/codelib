package com.berry.scalaProgramming

import java.io.FileReader
import scala.util.parsing.combinator._

/**
 * scala编程（第三版）31章本地调试
 *
 * @author leonardo
 * @since 2024/11/17
 *
 */
class Arith extends JavaTokenParsers {
  def expr: Parser[Any] = term ~ rep("+" ~ term | "-" ~ term)
  def term: Parser[Any] = factor ~ rep("*" ~ factor | "/" ~ factor)
  def factor: Parser[Any] = floatingPointNumber | "(" ~ expr ~ ")"
}
class JSON extends JavaTokenParsers {
  def value: Parser[Any] =
    (obj
      | arr
      | stringLiteral
      | floatingPointNumber ^^ (_.toDouble)
      | "null" ^^ null
      | "true" ^^ (_ => true)
      | "false" ^^ (_ => false))

  def obj: Parser[Map[String, Any]] = "{" ~ repsep(member, ",") ~ "}" ^^ {
    case "{" ~ ms ~ "}" => Map() ++ ms
  }
  def arr: Parser[List[Any]] = "[" ~> repsep(value, ",") <~ "]"
  def member: Parser[(String, Any)] = stringLiteral ~ ":" ~ value ^^ {
    case name ~ ":" ~ value => (name, value)
  }
}
object ParserExpr extends Arith {
  def main(args: Array[String]): Unit = {
    val p = parseAll(expr, "2*(3+7 )")
    val p2 = parseAll(expr, "2*3+7 )")
    println(p)
    println(p2)
  }
}
object ParserJson extends JSON {
  def main(args: Array[String]): Unit = {
    println(parseAll(value, new FileReader("src/main/resources/stu.json")))
  }
}
object Ch31 {
}
