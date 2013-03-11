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
    
    generators.PDF(this)
    generators.PDF.printXML(this)
  }

}