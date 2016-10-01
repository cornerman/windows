package windows

import windows.msg._

object Reactor {
  def react(config: Config, mode: Mode, event: Event): Seq[Action] = event match {
    //TODO pattern match does not work on set/seq
    case KeyPressEvent(window, key, mods) if mods.size == 1 && mods.head == config.mod => Seq(key) collect {
      case config.closeKey => Close(window)
      case config.execKey => Command(config.execCmd)
      case config.exitKey => Exit("pressed exit key")
    }
    case KeyReleaseEvent(window, key, mods) if mods.size == 1 && mods.head == config.mod => Seq.empty
    case ButtonPressEvent(window, button, mods, point) if mods.size == 1 && mods.head == config.mod => Seq.empty
    case ButtonReleaseEvent(window, button, mods, point) if mods.size == 1 && mods.head == config.mod => Seq.empty
    case MotionNotifyEvent(_, pos) => mode.buttons collect {
      case ButtonPressEvent(window, config.moveButton, mods, _) if mods.size == 1 && mods.head == config.mod => MoveWindow(window, pos.x, pos.y)
      case ButtonPressEvent(window, config.resizeButton, mods, _) if mods.size == 1 && mods.head == config.mod => ResizeWindow(window, pos.x, pos.y)
    }
    case MapRequestEvent(window) => Seq(ManageWindow(window))
    case MapNotifyEvent(window) => Seq(MappedWindow(window))
    case UnmapNotifyEvent(window) => Seq(UnmappedWindow(window))
    case _ => Seq.empty
  }
}
