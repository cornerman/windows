package windows.msg

//TODO: structs
//TODO: window type should not be int

sealed trait Action

sealed trait ConnectionAction extends Action
sealed trait InterpreterAction extends Action

case class Command(cmd: String) extends ConnectionAction
case class Exit(reason: String) extends ConnectionAction
case class Close(window: Long) extends ConnectionAction
case class WarpPointer(window: Long, x: Int, y: Int) extends ConnectionAction
case class ResizeWindow(window: Long, x: Int, y: Int) extends ConnectionAction
case class MoveWindow(window: Long, x: Int, y: Int) extends ConnectionAction
case class Configure(config: Config) extends ConnectionAction
case class ManageWindow(window: Long) extends ConnectionAction

case class MappedWindow(window: Long) extends InterpreterAction
case class UnmappedWindow(window: Long) extends InterpreterAction

object ActionDispatch {
  def chain(handlers: ((State, Action) => State)*)(state: State, action: Action): State = {
    val stateHandlers = handlers.map(handler => (s: State) => handler(s, action))
    val chainedHandler = stateHandlers.fold((s: State) => s)((h1, h2) => s => h2(h1(s)))
    chainedHandler(state)
  }

  def onAction(handler: PartialFunction[(State, Action), State])(state: State, action: Action): State = {
    handler.lift(state, action).getOrElse(state)
  }

  def onInterpreter(handler: (State, InterpreterAction) => State) = {
    onAction { case (s, a: InterpreterAction)  => handler(s, a) } _
  }

  def onConnection(handler: ConnectionAction => Option[String]) = {
    onAction { case (s, a: ConnectionAction) => handler(a).map(s.error).getOrElse(s) } _
  }
}
