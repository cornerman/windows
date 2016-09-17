package windows

import windows.msg._

object Tiling {
  def interpret(s: State, action: Action) = action match {
    // case AddWindow(window) => s.copy(windows = s.windows :+ window)
    // case RemoveWindow(window) => s.copy(windows = s.windows - window)
    case _ => s
  }
}
