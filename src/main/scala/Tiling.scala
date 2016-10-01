package windows

import windows.msg._

object Tiling {
  def interpret(s: State, action: InterpreterAction) = action match {
    case MappedWindow(window) =>
      s.copy(windows = s.windows :+ window)
    case UnmappedWindow(window) =>
      s.copy(windows = s.windows.filterNot(_ == window))
  }
}
