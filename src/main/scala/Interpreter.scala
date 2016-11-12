package windows

import windows.msg._

object Interpreter {
  def interpret(s: State, action: Action): State = action match {
    case MappedWindow(window) => s.copy(windows = s.windows :+ window)
    case UnmappedWindow(window) => s.copy(windows = s.windows.filterNot(_ == window))
    case ModeUpdate(mode) => s.copy(mode = mode)
    case LogAction(log) => println("LOG" + log); s
    case _ => s
  }
}
