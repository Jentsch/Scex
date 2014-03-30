package org.scex

import org.specs2._

class ModifiersTest extends Specification {

  val A = new Attribute[Int]("A")
  val B = new Attribute[Int]("B")

def is = s2"""
${"Modifiers".title}
  ${Modifiers.empty should be empty}
  ${(A > 1) get A should beSome(1)}
  ${(A > 1) get B should beNone}
  ${(A > 1 & B > 2) get A should beSome(1)}
  ${(A > 1 & B > 2) get B should beSome(2)}

### Last add modifier should stay
  ${(A > 1 & A > 2) get A should beSome(2)}
  ${(A > 1 & B > 2 & A > 3) get A should beSome(3)}

### Error handling
  ${(A > 1)(B) should throwA [RuntimeException]}
  ${(A > 1 & null) should throwA [NullPointerException]}
"""
}
