package eu.joaocosta.minart.backend

import eu.joaocosta.minart.core._

class JsRenderLoopSpec extends RenderLoopTests {
  lazy val renderLoop: RenderLoop = JsRenderLoop
  lazy val renderLoopName: String = "JsRenderLoop"
  override lazy val testLoop: Boolean = false
}
