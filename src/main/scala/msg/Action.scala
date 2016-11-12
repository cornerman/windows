package windows.msg

//TODO: structs
//TODO: window type should not be int

case class EventResult(handled: Boolean, actions: Seq[ConnectionAction])

sealed trait Action

sealed trait ConnectionAction extends Action

case class Command(cmd: String) extends ConnectionAction
case class Exit(reason: String) extends ConnectionAction
case class Close(window: Long) extends ConnectionAction
case class WarpPointer(window: Long, point: Point) extends ConnectionAction
case class ResizeWindow(window: Long, point: Point) extends ConnectionAction
case class MoveWindow(window: Long, point: Point) extends ConnectionAction
case class ManageWindow(window: Long) extends ConnectionAction
case class Configure(config: Config) extends ConnectionAction

case class MappedWindow(window: Long) extends Action
case class UnmappedWindow(window: Long) extends Action
case class ModeUpdate(mode: Option[Mode]) extends Action
case class LogAction(msg: Log.Message) extends ConnectionAction
