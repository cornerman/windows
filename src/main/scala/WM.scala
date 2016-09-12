package windows

import scala.scalanative.native._, stdlib._
import xcb._, XCB._

//TODO: enum
// object Mode extends Enumeration {
//   type Mode = Value
//   val Normal, Move, Resize = Value
// }
object Mode {
  val Normal = 0
  val Move = 1
  val Resize = 2
}

class WM(conn: X) {
  var grabWindow = 0
  var currentMode = Mode.Normal

  def handleEvent(event: Event) {
    event match {
      case KeyPressEvent(e) =>
        val keycode = (!e).detail
        if (keycode == Config.closeKey) {
          val win = (!e).child
          if (win != 0) {
            conn.destroyWindow(win)
            conn.flush()
          }
        } else if (keycode == Config.execKey) {
          Commands.execute(Config.execCmd)
        } else if (keycode == Config.exitKey) {
          conn.disconnect()
          exit(1)
        }
      case KeyReleaseEvent(e) =>
      case ButtonPressEvent(e) =>
        val button = (!e).detail
        grabWindow = (!e).child
        currentMode = if (button == Config.moveButton) Mode.Move else if (button == Config.resizeButton) Mode.Resize else Mode.Normal
        if (grabWindow != 0 && currentMode != Mode.Normal) {
          if (currentMode == Mode.Move) conn.warpPointerForMove(grabWindow)
          else conn.warpPointerForResize(grabWindow)
          conn.grabPointer(grabWindow)
          conn.flush()
        }
      case ButtonReleaseEvent(e) =>
        grabWindow = 0
        currentMode = Mode.Normal
        conn.ungrabPointer()
        conn.flush()
      case MotionNotifyEvent(e) =>
        if (grabWindow != 0 && currentMode != Mode.Normal) {
          if (currentMode == Mode.Move) conn.moveToPointer(grabWindow)
          else conn.resizeToPointer(grabWindow)
          conn.flush()
        }
      case _ =>
    }
  }

  def grabKeys() {
    conn.grabKey(Config.exitKey.toByte, Config.mod)
    conn.grabKey(Config.closeKey.toByte, Config.mod)
    conn.grabKey(Config.execKey.toByte, Config.mod)
    conn.grabButton(Config.moveButton.toByte, Config.mod)
    conn.grabButton(Config.resizeButton.toByte, Config.mod)
    conn.flush()
  }

  def tick() {
    val ev = conn.waitForEvent()
    handleEvent(ev)
  }
}
