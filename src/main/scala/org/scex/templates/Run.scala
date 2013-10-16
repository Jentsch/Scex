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
    
    scala.xml.XML.save("/home/mi/djentsch/Dropbox/test.html", generators HTML this, "UTF-8")

    println(generators HTML this)
  }

}
