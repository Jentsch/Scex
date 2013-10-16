package org.scex.sample

import org.scex._
import org.scex.attributes._

object HTML extends templates.Presentation with templates.Run {
  title"Scex"
  
  subtitle"Sc(ala) (Lat)ex"
  
  p"a inner DSL for Scala to be like Latex"

  section"Motivation"
  
  p"I have some data and I want present them in a report."
  
  p"""
  My hole project is written Scala, no other languages like Java, SQL or XML.
  But now I have to add a new language (or not).
  """
    
  p"So why not use something familar, somthing known good..."
    
  p & TextColor > red | "Scala!"
  
  chapter"Examples"
  
  section"Text"
  
  p"How to write text:"
  
  code"""
    p"Here is your Text"
  """

  p"p stands for paragraph, like <p> in HTML"
  
  section"Sections"
  
  code"""
    chapter"Chapter One"
  
    p"Your first chapter"
  """
  
  section"Definitions"
  
  code"""
     def warning = p & TextColor > red
     override def p = super.p & FontFamily > "MyFont"
  """
  
  p"You could use rgb(255, 0, 0) as well."
  
  chapter"Conclusion"
  
  section"What's cool?"
  
  p"Everything is type save"
  
  p"Refrectoring is possible (renember those CTRL+SHIFT+R feature?)"
  
  section"What is open to do?"
  
  val todo = List(
	"More attributes", 
	"Implementing good and usefull templates, like DIN-A4, sections and so on...",
	"Implement inline formating, for that see String INterpolation in Scala 2.10",
	"Implement document reflections (current task)",
	"Find a way to add include object like images and charts")
  
  list | todo
  
  section"Fancy ideas"
  
  list |
    "Build a Web-Server" |
    "Integrate an other libary to create cool diagrams"
  
  section"What's not so good?"

  p"A bit more overhead, e.g. trible quots"
  
  p"Everything is in memory, so it consumes more RAM than a template system that forgets erverything after the content generation"
}
