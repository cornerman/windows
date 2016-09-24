package windows.msg

case class Mode(key: Option[KeyPressEvent], button: Option[ButtonPressEvent]) {
  def update(event: Event) = event match {
    case e: KeyPressEvent => copy(key = Some(e))
    case e: KeyReleaseEvent => copy(key = None)
    case e: ButtonPressEvent => copy(button = Some(e))
    case e: ButtonReleaseEvent => copy(button = None)
    case _ => this
  }
}

object Mode {
  def initial = Mode(None, None)
}

case class State(config: Config, windows: Seq[Long], errors: Seq[String]) {
  def error(msg: String) = copy(errors = errors :+ msg)
}
object State {
  def initial(config: Config) = State(config, Seq.empty, Seq.empty)
}
