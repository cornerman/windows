package windows

import windows.msg._

object Interpreter {
  val screenWidth = 500 //TODO really?
  val screenHeight = 500 //TODO really?

  def interpret(s: State, reaction: Reaction): (State, Seq[Action]) = reaction match {
    case MappedWindow(window) =>
      val windows = s.windows :+ window
      val width = screenWidth / (windows.size + 1)
      val relayouts = windows.zipWithIndex.flatMap { case (win, i) =>
        Seq(MoveWindow(win, Point(i * width, 0)), ResizeWindow(win, Point(width, screenHeight)))
      }
      (s.copy(windows = windows), relayouts)
    case UnmappedWindow(window) =>
      val leftWindows = s.windows.filterNot(_ == window)
      val todo = if (s.focused == window) Some(Focus(leftWindows.lastOption.getOrElse(0))) else None
      (s.copy(windows = leftWindows), todo.toSeq)
    case ModeUpdate(mode) =>
      (s.copy(mode = mode), Seq.empty)
    case LogAction(log) =>
      println("[LOG] " + log)
      (s, Seq.empty)
    case SetWindowFocus(window, focused) =>
      val next = if (focused) window else 0
      (s.copy(focused = next), Seq.empty)
    case ActionReaction(action) => (s, Seq(action))
    case _ => (s, Seq.empty)
  }
}
