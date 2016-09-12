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
  var grabWindow = 0.toUInt
  var currentMode = Mode.Normal

  def handleEvent(event: Ptr[xcb_generic_event_t]) {
    //TODO: broken match on extractors: https://github.com/scala-native/scala-native/issues/112
    // event match {
    //   case KeyPressEvent(e) =>
    //   case KeyReleaseEvent(e) =>
    //   case ButtonPressEvent(e) =>
    //   case ButtonReleaseEvent(e) =>
    //   case MotionNotifyEvent(e) =>
    //   case _ =>
    // }
    KeyPressEvent.unapply(event) match {
      case Some(e) =>
        val keycode = (!e).detail
        if (keycode == Config.closeKey) {
          val win = (!e).child
          if (win != 0.toUInt) {
            conn.destroyWindow(win)
            conn.flush()
          }
        } else if (keycode == Config.execKey) {
          Commands.execute(Config.execCmd)
        } else if (keycode == Config.exitKey) {
          conn.disconnect()
          exit(1)
        }
      case None =>
    }
    ButtonPressEvent.unapply(event) match {
      case Some(e) =>
        val button = (!e).detail
        grabWindow = (!e).child
        currentMode = if (button == Config.moveButton) Mode.Move else if (button == Config.resizeButton) Mode.Resize else Mode.Normal
        if (grabWindow != 0.toUInt && currentMode != Mode.Normal) {
          if (currentMode == Mode.Move) conn.warpPointerForMove(grabWindow)
          else conn.warpPointerForResize(grabWindow)
          conn.grabPointer(grabWindow)
          conn.flush()
        }
      case None =>
    }
    ButtonReleaseEvent.unapply(event) match {
      case Some(e) =>
        grabWindow = 0.toUInt
        currentMode = Mode.Normal
        conn.ungrabPointer()
        conn.flush()
      case None =>
    }
    MotionNotifyEvent.unapply(event) match {
      case Some(e) =>
        if (grabWindow != 0.toUInt && currentMode != Mode.Normal) {
          if (currentMode == Mode.Move) conn.moveToPointer(grabWindow)
          else conn.resizeToPointer(grabWindow)
          conn.flush()
        }
      case None =>
    }
  }

  def grabKeys() {
    conn.grabKey(Config.exitKey, Config.mod)
    conn.grabKey(Config.closeKey, Config.mod)
    conn.grabKey(Config.execKey, Config.mod)
    conn.grabButton(Config.moveButton, Config.mod)
    conn.grabButton(Config.resizeButton, Config.mod)
    conn.flush()
  }

  def run() {
    while (true) {
      val ev = conn.waitForEvent()
      handleEvent(ev)
    }
  }
}
