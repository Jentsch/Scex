package org.scex.attributes

import org.specs2._
import Double._

class DistanceTest extends Specification { 
 
  // Distances based on floating point operations, exact matches are not likely
  private implicit class nearlyEqual(length: Distance) {
    def === (test: Distance) =
      length must beBetween(test * 0.999, test * 1.001)
  }

def is = s2"""

This is a specification to check the Distance type
  Unit relations:
    ${1000.mm === 100.cm}
    ${1.pt === 0.376065.mm}
  Math operations:
    ${1.mm * 2 === 2.mm}
    ${1.mm + 2.mm === 3.mm}
    ${5.mm - 2.mm === 3.mm}
    ${Set(1.mm, 2.mm).min === 1.mm}
    ${4.mm > 2.mm}
  General behaviour:
    ${NaN.mm must throwA [IllegalArgumentException]}
    ${NaN.pt must throwA [IllegalArgumentException]}
    ${PositiveInfinity.mm must throwA [IllegalArgumentException]}
    ${10.mm * NaN must throwA [IllegalArgumentException]}
"""


}
