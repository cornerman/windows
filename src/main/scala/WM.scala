package windows

import scala.scalanative.native._
import native._, stdlib._
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
      case None =>
    }
    ButtonPressEvent.unapply(event) match {
      case Some(e) =>
        val button = (!e).detail
        grabWindow = (!e).child
        currentMode = if (button == Config.moveButton) Mode.Move else if (button == Config.resizeButton) Mode.Resize else Mode.Normal
        if (grabWindow != 0 && currentMode != Mode.Normal) {
          if (currentMode == Mode.Move) conn.warpPointerForMove(grabWindow)
          else conn.warpPointerForResize(grabWindow)
          conn.grabPointer(grabWindow)
          conn.flush()
        }
      case None =>
    }
    ButtonReleaseEvent.unapply(event) match {
      case Some(e) =>
        grabWindow = 0
        currentMode = Mode.Normal
        conn.ungrabPointer()
        conn.flush()
      case None =>
    }
    MotionNotifyEvent.unapply(event) match {
      case Some(e) =>
        if (grabWindow != 0 && currentMode != Mode.Normal) {
          if (currentMode == Mode.Move) conn.moveToPointer(grabWindow)
          else conn.resizeToPointer(grabWindow)
          conn.flush()
        }
      case None =>
    }
  }

  def registerKeys() {
    conn.registerKey(Config.exitKey.toByte, Config.mod)
    conn.registerKey(Config.closeKey.toByte, Config.mod)
    conn.registerKey(Config.execKey.toByte, Config.mod)
    conn.registerButton(Config.moveButton.toByte, Config.mod)
    conn.registerButton(Config.resizeButton.toByte, Config.mod)
    conn.flush()
  }

  def tick() {
    val ev = conn.waitForEvent()
    handleEvent(ev)
  }
}
