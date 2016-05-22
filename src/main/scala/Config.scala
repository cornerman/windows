package windows

import xcb._
import scala.scalanative.native._

object Config {
  val mod = XCB_MOD_MASK_1 //alt
  val moveButton = 1 //left
  val resizeButton = 3 //right
  val exitKey = 26 //e
  val closeKey = 24 //q
  val execKey = 40 //d
  val execCmd = c"urxvt"
}