package windows.msg

//TODO: structs
//TODO button/key types should not be int

sealed trait Event
case class ErrorEvent(msg: String) extends Event
case class ButtonPressEvent(window: Long, button: Int, modifiers: Set[Modifier], point: Point) extends Event
case class ButtonReleaseEvent(window: Long, button: Int, modifier: Set[Modifier], point: Point) extends Event
case class KeyPressEvent(window: Long, key: Int, modifier: Set[Modifier]) extends Event
case class KeyReleaseEvent(window: Long, key: Int, modifier: Set[Modifier]) extends Event
case class MotionNotifyEvent(window: Long, point: Point) extends Event
case class MapRequestEvent(window: Long) extends Event
case class MapNotifyEvent(window: Long) extends Event
case class UnmapNotifyEvent(window: Long) extends Event
case class LogEvent(msg: Log.Message) extends Event
case object ConfigureRequest extends Event
