package windows.x

import windows.msg
import windows.msg._
import windows.system.Commands

object XAdapter {
  import XHelper._

  private var shouldExit = false

  def act(conn: X)(action: Action): Unit = action match {
    case Configure(config) => import config._
      conn.grabKey(exitKey.toByte, translateMod(mod))
      conn.grabKey(closeKey.toByte, translateMod(mod))
      conn.grabKey(execKey.toByte, translateMod(mod))
      conn.grabButton(moveButton.toByte, translateMod(mod))
      conn.grabButton(resizeButton.toByte, translateMod(mod))
    case Exit(reason) =>
      shouldExit = true
    case Command(cmd) =>
      Commands.execute(cmd)
    case Focus(window) =>
      conn.bringToFront(window)
      conn.focusWindow(window)
    case Close(window) =>
      conn.destroyWindow(window)
    case WarpPointer(window, point) =>
      conn.warpPointer(window, point)
    case MoveWindow(window, point) if window != 0 =>
      conn.bringToFront(window)
      conn.moveWindow(window, point)
    case ResizeWindow(window, point) if window != 0 =>
      conn.bringToFront(window)
      conn.resizeWindow(window, point)
    case ManageWindow(window) =>
      conn.manageWindow(window)
      conn.bringToFront(window)
    case _ =>
  }

  //TODO: here a partial function throws exception in native?
  def translate(event: Event.EventPtr): Option[Event] = Some(event) collect {
    case KeyPressEvent(e) => msg.KeyPressEvent((!e).child, (!e).detail, activeMods((!e).state))
    case KeyReleaseEvent(e) => msg.KeyReleaseEvent((!e).child, (!e).detail, activeMods((!e).state))
    case ButtonPressEvent(e) => msg.ButtonPressEvent((!e).child, (!e).detail, activeMods((!e).state), Point((!e).event_x, (!e).event_y))
    case ButtonReleaseEvent(e) => msg.ButtonReleaseEvent((!e).child, (!e).detail, activeMods((!e).state), Point((!e).event_x, (!e).event_y))
    case MotionNotifyEvent(e) => msg.MotionNotifyEvent((!e).child, Point((!e).event_x, (!e).event_y))
    case MapRequestEvent(e) => msg.MapRequestEvent((!e).window)
    case MapNotifyEvent(e) => msg.MapNotifyEvent((!e).window)
    case UnmapNotifyEvent(e) => msg.UnmapNotifyEvent((!e).window)
    case ErrorEvent(e) => msg.LogEvent(Log.Error(s"X11 error: ${(!e).error_code}"))
  }

  def run(handler: Event => EventResult): Option[String] = X.connect map { conn =>
    conn.registerEvents()

    //TODO: how to pass unhandled key events to x windows?
    conn.grabPointer()
    conn.grabKeyboard()
    // handler(ConfigureRequest).actions.foreach(act(conn))

    conn.flush

    while(!shouldExit) {
      val event = conn.waitForEvent()
      translate(event) foreach { ev =>
        val res = handler(ev)
        res.actions.foreach(act(conn))
        conn.flush()
      }
    }

    conn.disconnect()
    None
  } getOrElse Some("Error connecting to x")
}
