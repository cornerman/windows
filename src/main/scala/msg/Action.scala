package windows.msg

//TODO: structs
//TODO: window type should not be int

sealed trait Action

sealed trait ConnectionAction extends Action
sealed trait SystemAction extends Action
sealed trait CombinedAction extends ConnectionAction with SystemAction

case class Command(cmd: String) extends SystemAction
case class Exit(reason: String) extends CombinedAction
case class Close(window: Int) extends ConnectionAction
// case class AddWindow(window: Int) extends ConnectionAction
// case class RemoveWindow(window: Int) extends ConnectionAction
case class MouseResizeStart(window: Int) extends ConnectionAction
case class MouseMoveStart(window: Int) extends ConnectionAction
case class MouseResizing(window: Int) extends ConnectionAction
case class MouseMoving(window: Int) extends ConnectionAction
case class MouseResizeEnd(window: Int) extends ConnectionAction
case class MouseMoveEnd(window: Int) extends ConnectionAction
case class Configure(config: Config) extends ConnectionAction

object ActionDispatch {
  def apply(system: SystemAction => Unit, connection: ConnectionAction => Unit)(action: Action) = action match {
    case a: CombinedAction => connection(a); system(a)
    case a: ConnectionAction => connection(a)
    case a: SystemAction => system(a)
  }
}
