package windows.x

import windows.msg._
import windows.system.Commands

class XAdapter(conn: X) {
  import XHelper._

  var shouldExit = false
  var shouldFlush = false

  def act(action: ConnectionAction): Option[String] = action match {
    case Configure(config) => import config._
      conn.grabPointer()
      conn.grabKey(exitKey.toByte, translateMod(mod))
      conn.grabKey(closeKey.toByte, translateMod(mod))
      conn.grabKey(execKey.toByte, translateMod(mod))
      conn.grabButton(moveButton.toByte, translateMod(mod))
      conn.grabButton(resizeButton.toByte, translateMod(mod))
      shouldFlush = true
      None
    case Exit(reason) =>
      shouldExit = true
      None
    case Command(cmd) =>
      Commands.execute(cmd)
      None
    case Close(window) =>
      conn.destroyWindow(window)
      shouldFlush = true
      None
    case WarpPointer(window, x, y) =>
      conn.warpPointer(window, x, y)
      shouldFlush = true
      None
    case MoveWindow(window, x, y) if window != 0 =>
      conn.bringToFront(window)
      conn.moveWindow(window, x, y)
      shouldFlush = true
      None
    case ResizeWindow(window, x, y) if window != 0 =>
      conn.bringToFront(window)
      conn.resizeWindow(window, x, y)
      shouldFlush = true
      None
    case ManageWindow(window) =>
      conn.manageWindow(window)
      conn.bringToFront(window)
      shouldFlush = true
      None
    case _ => None
  }

  def run(handler: Event => Boolean): Option[String] = {
    conn.registerEvents()
    conn.flush

    while(!shouldExit) {
      //TODO: here a partial function throws exception in native?
      import windows.{msg => w}
      val event = conn.waitForEvent() match {
        case KeyPressEvent(e) => Some(w.KeyPressEvent((!e).child, (!e).detail, activeMods((!e).state)))
        case KeyReleaseEvent(e) => Some(w.KeyReleaseEvent((!e).child, (!e).detail, activeMods((!e).state)))
        case ButtonPressEvent(e) => Some(w.ButtonPressEvent((!e).child, (!e).detail, activeMods((!e).state), Point((!e).event_x, (!e).event_y)))
        case ButtonReleaseEvent(e) => Some(w.ButtonReleaseEvent((!e).child, (!e).detail, activeMods((!e).state), Point((!e).event_x, (!e).event_y)))
        case MotionNotifyEvent(e) => Some(w.MotionNotifyEvent((!e).child, Point((!e).event_x, (!e).event_y)))
        case MapRequestEvent(e) => Some(w.MapRequestEvent((!e).window))
        case MapNotifyEvent(e) => Some(w.MapNotifyEvent((!e).window))
        case UnmapNotifyEvent(e) => Some(w.UnmapNotifyEvent((!e).window))
        // case _ => None
      }

      event.foreach(handler)
      if (shouldFlush) {
        shouldFlush = false
        conn.flush()
      }
    }

    shouldExit = false
    conn.disconnect()
    None
  }
}
