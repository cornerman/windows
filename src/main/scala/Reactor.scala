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
    case e@ButtonPressEvent(window, button, mods, x, y) if mods.size == 1 && mods.head == config.mod => Some(button) collect {
      case config.moveButton => WarpPointer(window, x, y)
      case config.resizeButton => WarpPointer(window, x, y)
    }
    case ButtonReleaseEvent(window, button, mods, x, y) if mods.size == 1 && mods.head == config.mod => None
    case MotionNotifyEvent(_, x, y) => mode.button collect {
      case ButtonPressEvent(window, config.moveButton, mods, _, _) if mods.size == 1 && mods.head == config.mod => MoveWindow(window, x, y)
      case ButtonPressEvent(window, config.resizeButton, mods, _, _) if mods.size == 1 && mods.head == config.mod => ResizeWindow(window, x, y)
    }
    case MapRequestEvent(window) => Some(ManageWindow(window))
    case MapNotifyEvent(window) => Some(MappedWindow(window))
    case UnmapNotifyEvent(window) => Some(UnmappedWindow(window))
    case _ => None
  }
}
