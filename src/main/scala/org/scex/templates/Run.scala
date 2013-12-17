package org.scex.templates

import org.scex._

/**
 * A document annotated with this trait will be runnable.
 */
trait Run extends General {
  
  private var bodies = List.empty[() => Unit] 
  
  def delayedInit(body: => Unit) = {
    bodies = {() => body } :: bodies
  }
  
  def main(params: Array[String]) {
    bodies foreach {
      _ ()
    }
    
    scala.xml.XML.save(filename, generators HTML this, "UTF-8")

    println("Documente saved under " + filename)
  }

  private def filename = name + ".html"

  private def name = this.getClass.getSimpleName.takeWhile(_ != '$')
}
