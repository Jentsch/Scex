package org.scex.attributes

class Color(val intRep: Int) extends AnyVal {
  def red = intRep./(256 * 256).&(255)
  def green = intRep./(256).&(255)
  def blue  = intRep & 255
  
  def r = red
  def g = green
  def b = blue
  
  def toTupple = (r, g, b)
  def toList = List(r, g, b)
  
  def toHex = {
    val s = intRep.toHexString.toUpperCase
    val missing = 6 - s.length
    "0" * missing + s
  }
}

object Color {
  def apply(r: Int, g: Int, b: Int) = {
    assert((r & 255) == r, r.toString)
    assert((g & 255) == g, g.toString)
    assert((b & 255) == b, b.toString)
    
    val res = new Color(r * 256 * 256 | g * 256 | b)
    
    res
  }
  
  trait PreDefs {
    val black = Color(0, 0,0)
    val blue  = Color(0, 0, 255)
    val green = Color(0, 128, 0)
    val lime = Color(0, 255, 0)
    val red = Color(255, 0, 0)
    val white = Color(255, 255, 255)
    val yellow = Color(255, 255, 0)
  }
}
