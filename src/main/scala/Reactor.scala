package windows

import windows.msg._

object Reactor {
  //TODO pattern match does not work on set/seq
  private def isOnlyMod(mod: Modifier)(mods: Set[Modifier]) = mods.size == 1 && mods.head == mod

  def react(s: State, event: Event): Option[Reaction] = {
    val isMod = isOnlyMod(s.config.mod) _

    event match {
      case KeyPressEvent(window, key, mods) if isMod(mods) => Some(key) collect {
        case s.config.closeKey => ActionReaction(Close(window))
        case s.config.execKey => ActionReaction(Command(s.config.execCmd))
        case s.config.exitKey => ActionReaction(Exit("pressed exit key"))
      }
      case KeyReleaseEvent(window, key, mods) => None
      case ButtonPressEvent(window, button, mods, point)  => Some(button) collect {
        case s.config.resizeButton if isMod(mods) && s.mode.isEmpty => ModeUpdate(Some(Mode.Resize))
        case s.config.moveButton if isMod(mods) && s.mode.isEmpty => ModeUpdate(Some(Mode.Move))
      }
      case ButtonReleaseEvent(window, button, mods, point) => s.mode.map((button, _)) collect {
        case (s.config.resizeButton, Mode.Resize) => ModeUpdate(None)
        case (s.config.moveButton, Mode.Move) => ModeUpdate(None)
      }
      case MotionNotifyEvent(window, pos) => s.mode collect {
        //TODO sometiomes focused gets lost after these operations, assure focus
        // case Mode.Resize => ActionReaction(ResizeWindow(s.focused, pos))
        // case Mode.Move => ActionReaction(MoveWindow(s.focused, pos))
        case Mode.Resize => ActionReaction(ResizeWindow(window, pos))
        case Mode.Move => ActionReaction(MoveWindow(window, pos))
      }
      case MapRequestEvent(window) => Some(ActionReaction(ManageWindow(window)))
      case MapNotifyEvent(window) => Some(MappedWindow(window))
      case UnmapNotifyEvent(window) => Some(UnmappedWindow(window))
      case FocusEvent(window, focused) => Some(SetWindowFocus(window, focused))
      case LogEvent(msg) => Some(LogAction(msg))
      case ConfigureRequest => Some(ActionReaction(Configure(s.config)))
      case _ => None
    }
  }
}
