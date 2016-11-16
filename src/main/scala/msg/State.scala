package windows.msg

sealed trait Mode
object Mode {
  case object Resize extends Mode
  case object Move extends Mode
}

case class State(config: Config, mode: Option[Mode], windows: Seq[Long], focused: Long) {
  // require(focused.forall(windows.contains)) //TODO focused should be option 0 => always root window
}
object State {
  def initial(config: Config) = State(config, None, Seq.empty, 0)
}
