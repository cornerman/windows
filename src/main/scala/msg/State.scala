package windows.msg

case class Mode(keys: Seq[KeyPressEvent], buttons: Seq[ButtonPressEvent]) {
  def update(event: Event) = event match {
    case e: KeyPressEvent => copy(keys = keys :+ e)
    case KeyReleaseEvent(_, key, _) => copy(keys = keys.filterNot(_.key == key))
    case e: ButtonPressEvent => copy(buttons = buttons :+ e)
    case ButtonReleaseEvent(_, button, _, _) => copy(buttons = buttons.filterNot(_.button == button))
    case _ => this
  }
}

object Mode {
  def initial = Mode(Seq.empty, Seq.empty)
}

case class State(config: Config, windows: Seq[Long], errors: Seq[String]) {
  def error(msg: String) = copy(errors = errors :+ msg)
}
object State {
  def initial(config: Config) = State(config, Seq.empty, Seq.empty)
}
