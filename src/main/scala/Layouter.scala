package windows

import windows.msg._

object Layouter {
  def layout(state: State, action: Action): Seq[Action] = action match {
    case a@ManageWindow(window) => Seq(a) //TODO fit into layout
    case a => Seq(a)
  }
}
