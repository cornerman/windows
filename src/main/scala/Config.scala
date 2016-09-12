package windows

import xcb._
import scala.scalanative.native._

object Config {
  val mod = XCB_MOD_MASK_1 //alt
  val moveButton = 1.toUByte //left
  val resizeButton = 3.toUByte //right
  val exitKey = 26.toUByte //e
  val closeKey = 24.toUByte //q
  val execKey = 40.toUByte //d
  val execCmd = "urxvt"
}
