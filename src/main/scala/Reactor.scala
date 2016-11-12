package windows

import windows.msg._

object Reactor {
  //TODO pattern match does not work on set/seq
  private def isOnlyMod(mod: Modifier)(mods: Set[Modifier]) = mods.size == 1 && mods.head == mod

  def react(state: State, event: Event): Option[Action] = {
    import state._
    val isMod = isOnlyMod(config.mod) _

    event match {
      case KeyPressEvent(window, key, mods) if isMod(mods) => Some(key) collect {
        case config.closeKey => Close(window)
        case config.execKey => Command(config.execCmd)
        case config.exitKey => Exit("pressed exit key")
      }
      case KeyReleaseEvent(window, key, mods) => None
      case ButtonPressEvent(window, button, mods, point) if isMod(mods) && mode.isEmpty => Some(button) collect {
        case config.resizeButton => ModeUpdate(Some(Mode.Resize))
        case config.moveButton => ModeUpdate(Some(Mode.Move))
      }
      case ButtonReleaseEvent(window, button, mods, point) => mode.map((button, _)) collect {
        case (config.resizeButton, Mode.Resize) => ModeUpdate(None)
        case (config.moveButton, Mode.Move) => ModeUpdate(None)
      }
      case MotionNotifyEvent(window, pos) => mode collect {
        case Mode.Resize => ResizeWindow(window, pos)
        case Mode.Move => MoveWindow(window, pos)
      }
      case MapRequestEvent(window) => Some(ManageWindow(window))
      case MapNotifyEvent(window) => Some(MappedWindow(window))
      case UnmapNotifyEvent(window) => Some(UnmappedWindow(window))
      case LogEvent(msg) => Some(LogAction(msg))
      case ConfigureRequest => Some(Configure(state.config))
      case _ => None
    }
  }
}
