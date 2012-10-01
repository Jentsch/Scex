package org.scex.sample

import org.scex._
import org.scex.attributes._

object PDF extends App with Builder {
  def text = 
    FontFamily > "Times Roman" &
    FontSize > 20 &
    TextAlign > "justify"
  
  def headline = text & 
    FontFamily > "Helvetica" & 
    FontSize > 28 &
    SpaceAfter > 6 &
    + TextUnderline

  def title = headline &
    FontSize > 38 &
    SpaceAfter > 10 &
    SpaceBefore > 12 &
    TextAlign > "center" &
	BreakBefore > page &
	TextColor > Color(0,0,128)

  def section = headline &
    BreakBefore > page

  def em = text & bold
  
  def subtitle = headline &
	TextAlign > "center"
  
  def list = {
    val block = bold
	val line  = text & SpaceAfter > 0 & PreText > "• "
	
	line asMinorOf block
  }

  title | 
  "Scex!"
  
  title |
  "Scex"
  
  subtitle |
  "Sc(ala) (Lat)ex"
  
  text | "a inner DSL for Scala to be like Latex"

  text |
  new Section("Motivation") {
    text |
    "I have some data and I want present them in a report."
    
    text |
    """My hole project is written Scala, no other languages like Java, SQL or XML.
    But now I have to add a new language (or not)."""
    
    text |
    "So why not use something familar, somthing known good..."
  }
  
  
  class Section(text: String) extends Builder {
    val test = PDF.section.|(text)
  }
  
  text & Display > table | new Builder {
    
  }
  
  text & TextColor > red | "Scala!"
  
  section | "What's better?"
  
  text | "Everything is type save"
  
  text | "Refrectoring is possible (renember those CTRL+SHIFT+R feature?)"
  
  section | "What is open to do?"
  
  val todo = List(
	"More attributes", 
	"Implementing good and usefull templates, like DIN-A4, sections and so on...",
	"Implement inline formating, for that see String INterpolation in Scala 2.10",
	"Implement document reflections (current task)",
	"Find a way to add include object like images and charts")
  
  list | todo
  
  section | "Fancy ideas"
  
  list |
    "Build a Web-Server" |
	"Integrate an other libary to create cool diagrams"
  
  section | "What's not so good?"

  text | "A bit more overhead, e.g. trible quots"
  
  text | "Everything is in memory, so it consumes more RAM than a template system that forgets erverything after the content generation"
  
  generators.PDF(this)
  generators.PDF.printXML(this)
}
