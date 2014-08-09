package org.scex.attributes

/**
 * Represents a color. All constructors are inside the
 * [[org.scex.attributes attributes]] package.
 */
final class Color private[Color] (val intRep: Int) extends AnyVal {
  def red = intRep./(256 * 256).&(255)
  def green = intRep./(256).&(255)
  def blue = intRep & 255

  /** Shortcut for red */
  @inline
  def r = red
  /** Shortcut for green */
  @inline
  def g = green
  /** Shortcut for blue */
  @inline
  def b = blue

  def toTupple = (r, g, b)
  def toList = List(r, g, b)

  def toHex =
    f"$intRep%08x" takeRight 6

  override def toString = "#" + toHex
}

private[attributes] object Color {
  private def apply(r: Int, g: Int, b: Int) = {
    def check(v: Int, name: String) {
      assert((v & 255) == v, s"$name should be in 0..255 but is $v")
    }
    check(r, "r")
    check(g, "g")
    check(b, "b")

    new Color(r * 256 * 256 | g * 256 | b)
  }

  /**
   * Definitions for the [[org.scex.attributes attributes]] package.
   */
  private[attributes] trait PreDefs {
    /**
     * Sample: <span style="background: #0FF; color: #000;">Aqua</span>
     */
    val aqua = Color(0, 255, 255)
    /**
     * Sample: <span style="background: #000; color: #FFF;">Black</span>
     */
    val black = Color(0, 0, 0)
    /**
     * Sample: <span style="background: #00F; color: #FFF;">Blue</span>
     */
    val blue = Color(0, 0, 255)

    /**
     * Sample: <span style="background: #F0F; color: #FFF;">Fuchsia</span>
     */
    val fuchsia = Color(255, 0, 255)

    /**
     * Sample: <span style="background: rgb(128, 128, 128); color: #FFF;">Gray</span>
     */
    val gray = Color(128, 128, 128)

    /**
     * Sample: <span style="background: rgb(0, 128, 0); color: #FFF;">Green</span>
     */
    val green = Color(0, 128, 0)

    /**
     * Sample: <span style="background: #0F0; color: #FFF;">Lime</span>
     */
    val lime = Color(0, 255, 0)

    /**
     * Sample: <span style="background: rgb(128, 0, 0); color: #FFF;">Maroon</span>
     */
    val maroon = Color(128, 0, 0)

    /**
     * Sample: <span style="background: #F00; color: #FFF;">Red</span>
     */
    val red = Color(255, 0, 0)

    /**
     * Sample: <span style="background: #FFF; color: #000;">White</span>
     */
    val white = Color(255, 255, 255)

    /**
     * Sample: <span style="background: #FF0; color: #000;">Yellow</span>
     */
    val yellow = Color(255, 255, 0)

    /**
     * Color in RGB-Channels. All parameters should be in 0..255.
     */
    def rgb(r: Int, g: Int, b: Int) = Color(r, g, b)

    /**
     * Returns the color with the given fraction of channels. All values should be
     * in 0..1.
     *
     * @usecase def rgb(r: Double, g: Double, b: Double): Color
     */
    def rgb[F](r: F, g: F, b: F)(implicit f: Fractional[F]) = {
      val max = f.fromInt(255)
      def toInt(v: F) =
        f.toInt(f.times(v, max)).
          ensuring(v => (v & 255) == v)

      Color(toInt(r), toInt(g), toInt(b))
    }
  }
}
