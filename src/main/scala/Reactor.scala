package windows

import windows.msg._

object Reactor {
  //TODO pattern match does not work on set/seq
  private def isOnlyMod(mod: Modifier)(mods: Set[Modifier]) = mods.size == 1 && mods.head == mod

  def react(state: State, event: Event): Option[Reaction] = {
    import state._
    val isMod = isOnlyMod(config.mod) _

    event match {
      case KeyPressEvent(window, key, mods) if isMod(mods) => Some(key) collect {
        case config.closeKey => ActionReaction(Close(window))
        case config.execKey => ActionReaction(Command(config.execCmd))
        case config.exitKey => ActionReaction(Exit("pressed exit key"))
      }
      case KeyReleaseEvent(window, key, mods) => None
      case ButtonPressEvent(window, button, mods, point)  => Some(button) collect {
        case config.resizeButton if isMod(mods) && mode.isEmpty => ModeUpdate(Some(Mode.Resize))
        case config.moveButton if isMod(mods) && mode.isEmpty => ModeUpdate(Some(Mode.Move))
      }
      case ButtonReleaseEvent(window, button, mods, point) => mode.map((button, _)) collect {
        case (config.resizeButton, Mode.Resize) => ModeUpdate(None)
        case (config.moveButton, Mode.Move) => ModeUpdate(None)
      }
      case MotionNotifyEvent(window, pos) => mode collect {
        //TODO sometiomes focused gets lost after these operations, assure focus
        case Mode.Resize => ActionReaction(ResizeWindow(focused, pos))
        case Mode.Move => ActionReaction(MoveWindow(focused, pos))
      }
      case MapRequestEvent(window) => Some(ActionReaction(ManageWindow(window)))
      case MapNotifyEvent(window) => Some(MappedWindow(window))
      case UnmapNotifyEvent(window) => Some(UnmappedWindow(window))
      case FocusEvent(window, focused) => Some(SetWindowFocus(window, focused))
      case LogEvent(msg) => Some(LogAction(msg))
      case ConfigureRequest => Some(ActionReaction(Configure(state.config)))
      case _ => None
    }
  }
}
