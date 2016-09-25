package windows.x

import windows.msg._
import windows.system.Commands

object XAdapter {
  import XHelper._

  def act(conn: X)(action: ConnectionAction): Option[String] = action match {
    case Configure(config) => import config._
      println("configure")
      conn.registerEvents()
      conn.grabKey(exitKey.toByte, translateMod(mod))
      conn.grabKey(closeKey.toByte, translateMod(mod))
      conn.grabKey(execKey.toByte, translateMod(mod))
      conn.grabButton(moveButton.toByte, translateMod(mod))
      conn.grabButton(resizeButton.toByte, translateMod(mod))
      conn.flush()
      None
    case Exit(reason) =>
      println("exit")
      println(reason)
      conn.disconnect()
      System.exit(0)
      None
    case Command(cmd) =>
      println("command")
      println(cmd)
      Commands.execute(cmd)
      None
    case Close(window) =>
      println("close")
      println(window)
      conn.destroyWindow(window)
      conn.flush()
      None
    case MouseMoveStart(window) if window != 0 =>
      println("movestart")
      println(window)
      conn.warpPointerForMove(window)
      conn.grabPointer(window)
      conn.flush()
      None
    case MouseResizeStart(window) if window != 0 =>
      println("resizestart")
      println(window)
      conn.warpPointerForResize(window)
      conn.grabPointer(window)
      conn.flush()
      None
    case MouseMoving(window) if window != 0 =>
      println("moving")
      println(window)
      conn.moveToPointer(window)
      conn.flush()
      None
    case MouseResizing(window) if window != 0 =>
      println("resizing")
      println(window)
      conn.resizeToPointer(window)
      conn.flush()
      None
    case MouseMoveEnd(window) if window != 0 =>
      println("moveend")
      println(window)
      conn.ungrabPointer()
      conn.flush()
      None
    case MouseResizeEnd(window) if window != 0 =>
      println("resizeend")
      println(window)
      conn.ungrabPointer()
      conn.flush()
      None
    case ManageWindow(window) =>
      println("manage")
      println(window)
      conn.manageWindow(window)
      conn.flush()
      None
    case _ =>
      println("ignored action")
      None
  }

  def run(conn: X)(handler: Event => Unit): Option[String] = {
    while(true) {
      //TODO: here a partial function throws exception in native?
      import windows.{msg => w}
      val event = conn.waitForEvent() match {
        case KeyPressEvent(e) => Some(w.KeyPressEvent((!e).child, (!e).detail, activeMods((!e).state)))
        case KeyReleaseEvent(e) => Some(w.KeyReleaseEvent((!e).child, (!e).detail, activeMods((!e).state)))
        case ButtonPressEvent(e) => Some(w.ButtonPressEvent((!e).child, (!e).detail, activeMods((!e).state)))
        case ButtonReleaseEvent(e) => Some(w.ButtonReleaseEvent((!e).child, (!e).detail, activeMods((!e).state)))
        case MotionNotifyEvent(e) => Some(w.MotionNotifyEvent((!e).child))
        case MapRequestEvent(e) => Some(w.MapRequestEvent((!e).window))
        case MapNotifyEvent(e) => Some(w.MapNotifyEvent((!e).window))
        case UnmapNotifyEvent(e) => Some(w.UnmapNotifyEvent((!e).window))
        case _ => None
      }

      event.foreach(handler)
    }
    None
  }
}
