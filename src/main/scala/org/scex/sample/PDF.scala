package org.scex.sample

import org.scex._
import org.scex.attributes._

object PDF extends templates.Presentation with templates.Run {
  title |
  "Scex"
  
  subtitle |
  "Sc(ala) (Lat)ex"
  
  text | 
  "a inner DSL for Scala to be like Latex"

  section |
  "Motivation"
  
  text |
  "I have some data and I want present them in a report."
  
  text |  """
  My hole project is written Scala, no other languages like Java, SQL or XML.
  But now I have to add a new language (or not).
  """
    
  text |
  "So why not use something familar, somthing known good..."
    
  text & TextColor > red | "Scala!"
  
  chapter |
  "Examples"
  
  section |
  "Text"
  
  text |
  "How to write text:"
  
  code | """
    text |
    "Here is your Text"
  """
  
  section |
  "Sections"
  
  code | """
    chapter | 
    "Chapter One"
  
    text |
    "Your first chapter"
  """
  
  section |
  "Definitions"
  
  code | """
     def warning = text & TextColor > red
     override def text = super.text & FontFamily > "MyFont"
  """
  
  text |
  "You could use rgb(255, 0, 0) as well."
  
  chapter |
  "Conclusion"
  
  section | "What's cool?"
  
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
}
