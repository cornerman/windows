package windows

import windows.msg._

object Reactor {
  def react(config: Config, mode: Mode, event: Event): Option[Action] = event match {
    //TODO pattern match does not work on set/seq
    case e@KeyPressEvent(window, key, mods) if mods.size == 1 && mods.head == config.mod => Some(key) collect {
      case config.closeKey => Close(window)
      case config.execKey => Command(config.execCmd)
      case config.exitKey => Exit("pressed exit key")
    }
    case KeyReleaseEvent(window, key, mods) if mods.size == 1 && mods.head == config.mod => None
    case e@ButtonPressEvent(window, button, mods) if mods.size == 1 && mods.head == config.mod => Some(button) collect {
      case config.moveButton => MouseMoveStart(window)
      case config.resizeButton => MouseResizeStart(window)
    }
    case ButtonReleaseEvent(window, button, mods) if mods.size == 1 && mods.head == config.mod => Some(button) collect {
      case config.moveButton => MouseMoveEnd(window)
      case config.resizeButton => MouseResizeEnd(window)
    }
    case MotionNotifyEvent(_) => mode.button collect {
      case ButtonPressEvent(window, config.moveButton, mods) if mods == Set(config.mod) => MouseMoving(window)
      case ButtonPressEvent(window, config.resizeButton, mods) if mods == Set(config.mod) => MouseResizing(window)
    }
    case MapRequestEvent(window) => Some(ManageWindow(window))
    case MapNotifyEvent(window) => Some(MappedWindow(window))
    case UnmapNotifyEvent(window) => Some(UnmappedWindow(window))
    case _ => println("no reaction"); None
  }
}
