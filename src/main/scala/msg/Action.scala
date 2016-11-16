package windows.msg

//TODO: structs
//TODO: window type should not be int

case class EventResult(handled: Boolean, actions: Seq[Action])

sealed trait Action

case class Command(cmd: String) extends Action
case class Exit(reason: String) extends Action
case class Close(window: Long) extends Action
case class Focus(window: Long) extends Action
case class WarpPointer(window: Long, point: Point) extends Action
case class ResizeWindow(window: Long, point: Point) extends Action
case class MoveWindow(window: Long, point: Point) extends Action
case class ManageWindow(window: Long) extends Action
case class Configure(config: Config) extends Action

sealed trait Reaction

case class MappedWindow(window: Long) extends Reaction
case class UnmappedWindow(window: Long) extends Reaction
case class SetWindowFocus(window: Long, focused: Boolean) extends Reaction
case class ModeUpdate(mode: Option[Mode]) extends Reaction
case class LogAction(msg: Log.Message) extends Reaction
case class ActionReaction(action: Action) extends Reaction
