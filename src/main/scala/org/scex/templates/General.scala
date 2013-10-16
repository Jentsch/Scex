package org.scex.templates

import org.scex._
import attributes._

trait General extends Builder {

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
  	
  def code = text &
    FontFamily > "Consolas" &
    WrapLines > codeLine
    
  def codeLine = text &
    HighLightKeyWords > keyWord
  
  def keyWord = bold
}
