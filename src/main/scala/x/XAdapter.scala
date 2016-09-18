package windows.x

import windows.msg._

object XAdapter {
  def act(conn: X)(action: ConnectionAction): Unit = action match {
    case Configure(config) => import config._
      println("configure")
      conn.registerEvents()
      conn.grabKey(exitKey.toByte, mod)
      conn.grabKey(closeKey.toByte, mod)
      conn.grabKey(execKey.toByte, mod)
      conn.grabButton(moveButton.toByte, mod)
      conn.grabButton(resizeButton.toByte, mod)
      conn.flush()
    case Exit(reason) =>
      println("exit")
      conn.disconnect()
    case Close(window) =>
      println("close")
      println(window)
      conn.destroyWindow(window)
      conn.flush()
    case MouseMoveStart(window) if window != 0 =>
      println("movestart")
      println(window)
      conn.warpPointerForMove(window)
      conn.grabPointer(window)
      conn.flush()
    case MouseResizeStart(window) if window != 0 =>
      println("resizestart")
      println(window)
      conn.warpPointerForResize(window)
      conn.grabPointer(window)
      conn.flush()
    case MouseMoving(window) if window != 0 =>
      println("moving")
      println(window)
      conn.moveToPointer(window)
      conn.flush()
    case MouseResizing(window) if window != 0 =>
      println("resizing")
      println(window)
      conn.resizeToPointer(window)
      conn.flush()
    case MouseMoveEnd(window) if window != 0 =>
      println("moveend")
      println(window)
      conn.ungrabPointer()
      conn.flush()
    case MouseResizeEnd(window) if window != 0 =>
      println("resizeend")
      println(window)
      conn.ungrabPointer()
      conn.flush()
    case ManageWindow(window) =>
      println("manage")
      println(window)
      conn.manageWindow(window)
      conn.flush()
    case _ => println("ignored action")
  }

  def run(conn: X)(handler: Event => Unit) = while(true) {
    //TODO: here a partial function throws exception in native?
    import windows.{msg => w}
    val event = conn.waitForEvent() match {
      case KeyPressEvent(e) => Some(w.KeyPressEvent((!e).child, (!e).detail))
      case KeyReleaseEvent(e) => Some(w.KeyReleaseEvent((!e).child, (!e).detail))
      case ButtonPressEvent(e) => Some(w.ButtonPressEvent((!e).child, (!e).detail))
      case ButtonReleaseEvent(e) => Some(w.ButtonReleaseEvent((!e).child, (!e).detail))
      case MotionNotifyEvent(e) => Some(w.MotionNotifyEvent((!e).child))
      case MapRequestEvent(e) => Some(w.MapRequestEvent((!e).window))
      case MapNotifyEvent(e) => Some(w.MapNotifyEvent((!e).window))
      case UnmapNotifyEvent(e) => Some(w.UnmapNotifyEvent((!e).window))
      case _ => None
    }

    event.foreach(handler)
  }
}
