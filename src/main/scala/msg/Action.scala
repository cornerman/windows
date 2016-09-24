package windows.msg

//TODO: structs
//TODO: window type should not be int

sealed trait Action

sealed trait ConnectionAction extends Action
sealed trait InterpreterAction extends Action

case class Command(cmd: String) extends ConnectionAction
case class Exit(reason: String) extends ConnectionAction
case class Close(window: Long) extends ConnectionAction
case class MouseResizeStart(window: Long) extends ConnectionAction
case class MouseMoveStart(window: Long) extends ConnectionAction
case class MouseResizing(window: Long) extends ConnectionAction
case class MouseMoving(window: Long) extends ConnectionAction
case class MouseResizeEnd(window: Long) extends ConnectionAction
case class MouseMoveEnd(window: Long) extends ConnectionAction
case class Configure(config: Config) extends ConnectionAction
case class ManageWindow(window: Long) extends ConnectionAction

case class MappedWindow(window: Long) extends InterpreterAction
case class UnmappedWindow(window: Long) extends InterpreterAction

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

  def onConnection(handler: ConnectionAction => Option[String]) = {
    onAction { case a: ConnectionAction => handler(a) } _
  }
}
