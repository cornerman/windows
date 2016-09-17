package windows.msg

import windows.x.xcb._

trait Config {
  val mod = XCB_MOD_MASK_1 //alt
  val moveButton = 1 //left
  val resizeButton = 3 //right
  val exitKey = 26 //e
  val closeKey = 24 //q
  val execKey = 40 //d
  val execCmd = "urxvt"
}

object MyConfig extends Config
