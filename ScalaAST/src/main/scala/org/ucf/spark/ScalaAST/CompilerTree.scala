package org.ucf.spark.ScalaAST
import scala.tools.nsc._
import io._
import scala.io.Source
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox
import com.google.common.io.Files
import java.nio.charset.Charset
import java.io.File

object CompilerTree extends Global(new Settings()) {
  new Run
  def parseToTree(path:String) = {
    val code  = AbstractFile.getFile(path)
    val bfs = new util.BatchSourceFile(code,code.toCharArray)
    val parser = new syntaxAnalyzer.UnitParser(new CompilationUnit(bfs))
    parser.smartParse()
  }
  def parseToString(path:String) = {
      showRaw(this.parseToTree(path))
  }
  def parseWithMirror(path:String) = {
    val source = Files.toString(new File(path),Charset.forName("UTF-8"))
    val toolBox = runtimeMirror(getClass.getClassLoader).mkToolBox()
    toolBox.parse(source)
  }
  
  def parseWithMirrorTypeCheck(path:String) = {
    val source = Files.toString(new File(path),Charset.forName("UTF-8"))
    val toolBox = runtimeMirror(getClass.getClassLoader).mkToolBox()
    toolBox.typecheck(toolBox.parse(source))
  }
}