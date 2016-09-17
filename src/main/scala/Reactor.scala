package windows

import windows.msg._

object Reactor {
  def react(config: Config, mode: Mode, event: Event): Option[Action] = event match {
    case e@KeyPressEvent(window, key) => Some(key) collect {
      case config.closeKey => Close(window)
      case config.execKey => Command(config.execCmd)
      case config.exitKey => Exit("pressed exit key")
    }
    case KeyReleaseEvent(window, key) => None
    case e@ButtonPressEvent(window, button) => Some(button) collect {
      case config.moveButton => MouseMoveStart(window)
      case config.resizeButton => MouseResizeStart(window)
    }
    case ButtonReleaseEvent(window, button) => Some(button) collect {
      case config.moveButton => MouseMoveEnd(window)
      case config.resizeButton => MouseResizeEnd(window)
    }
    case MotionNotifyEvent(_) => mode.button collect {
      case ButtonPressEvent(window, config.moveButton) => MouseMoving(window)
      case ButtonPressEvent(window, config.resizeButton) => MouseResizing(window)
    }
    case MapRequestEvent(_) =>
      println("maprequest")
      None
    case UnmapNotifyEvent(_) =>
      println("unmapnotify")
      None
    case _ => None
  }
}
