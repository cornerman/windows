package windows.msg

//TODO: structs
//TODO: window type should not be int

sealed trait Action

sealed trait ConnectionAction extends Action
sealed trait SystemAction extends Action
sealed trait InterpreterAction extends Action

case class Command(cmd: String) extends SystemAction
case class Exit(reason: String) extends SystemAction with ConnectionAction
case class Close(window: Int) extends ConnectionAction
case class MouseResizeStart(window: Int) extends ConnectionAction
case class MouseMoveStart(window: Int) extends ConnectionAction
case class MouseResizing(window: Int) extends ConnectionAction
case class MouseMoving(window: Int) extends ConnectionAction
case class MouseResizeEnd(window: Int) extends ConnectionAction
case class MouseMoveEnd(window: Int) extends ConnectionAction
case class Configure(config: Config) extends ConnectionAction
case class ManageWindow(window: Int) extends ConnectionAction

case class MappedWindow(window: Int) extends InterpreterAction
case class UnmappedWindow(window: Int) extends InterpreterAction

object ActionDispatch {
  def chain(handlers: ((State, Action) => State)*)(initialState: State, action: Action): State = {
    var state = initialState
    for (handler <- handlers)
      state = handler(state, action)

    state
  }

  def onAction(handler: PartialFunction[Action, Option[String]])(s: State, action: Action): State = {
    handler.lift(action).flatten.map(s.error).getOrElse(s)
  }

  def onStateAction(handler: PartialFunction[(State, Action), State])(s: State, action: Action): State = {
    handler.lift(s, action).getOrElse(s)
  }

  def onInterpreter(handler: (State, InterpreterAction) => State) = {
    onStateAction { case (s, a: InterpreterAction)  => handler(s, a) } _
  }

  def onSystem(handler: SystemAction => Option[String]) = {
    onAction { case a: SystemAction => handler(a) } _
  }

  def onConnection(handler: ConnectionAction => Option[String]) = {
    onAction { case a: ConnectionAction => handler(a) } _
  }
}
