package eu.joaocosta.minart

/**
 * A simpler canvas without `init` and `destroy` that's ready to be used
 * inside a render loop.
 */
trait RenderLoopCanvas {
  /** The Canvas width */
  def width: Int
  /** The Canvas height */
  def height: Int
  /** The Canvas integer scaling factor */
  def scale: Int
  /** The color to be used when the canvas is cleared */
  def clearColor: Color

  /** The Canvas width with the integer scaling applied */
  lazy val scaledWidth = width * scale
  /** The Canvas height with the integer scaling applied */
  lazy val scaledHeight = height * scale

  /**
   * Puts a pixel in the back buffer with a certain color.
   *
   * @param x pixel x position
   * @param y pixel y position
   * @param color `Color` to apply to the pixel
   */
  def putPixel(x: Int, y: Int, color: Color): Unit

  /**
   * Gets the color from the backbuffer.
   * This operation can be perfomance intensive, so it might be worthwile
   * to implement this operation on the application code.
   *
   * @param x pixel x position
   * @param y pixel y position
   * @return pixel color
   */
  def getBackbufferPixel(x: Int, y: Int): Color

  /**
   * Clears the backbuffer
   */
  def clear(): Unit

  /**
   * Flips buffers and redraws the screen
   */
  def redraw(): Unit
}