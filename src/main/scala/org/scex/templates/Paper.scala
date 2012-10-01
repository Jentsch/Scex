package org.scex.templates

import org.scex._
import attributes._

trait Paper extends Builder with DelayedInit {
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
  
  def text = 
    FontFamily > "Times Roman" &
    FontSize > 12 &
    TextAlign > "justify"
  
  def headline = text & 
    FontFamily > "Verdana" & 
    FontSize > 28 &
    SpaceBefore > 10 &
    SpaceAfter > 6 &
    bold &
    TextAlign > "left"

  def title = headline &
    FontSize > 38 &
    SpaceAfter > 10 &
    SpaceBefore > 12 &
    TextAlign > "center" &
	BreakBefore > page
  
  def subtitle = headline &
	TextAlign > "center"

  def author = 
    headline &
    PreText > "Autor: " &
	TextAlign > "center"
  
  def chapter = headline &
  	BreakBefore > page &
  	FontSize > 32

  def section = headline

  def em = text & bold
  
  def list = {
    val block = text
	val line  = 
	  text & 
	  SpaceAfter > 0 & 
	  PreText > "• "
	
	line asMinorOf block
  }
}