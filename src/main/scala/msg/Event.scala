package windows.msg

//TODO: structs
//TODO button/key types should not be int

sealed trait Event

case class ButtonPressEvent(window: Long, button: Int) extends Event
case class ButtonReleaseEvent(window: Long, button: Int) extends Event
case class KeyPressEvent(window: Long, key: Int) extends Event
case class KeyReleaseEvent(window: Long, key: Int) extends Event
case class MotionNotifyEvent(window: Long) extends Event
case class MapRequestEvent(window: Long) extends Event
case class MapNotifyEvent(window: Long) extends Event
case class UnmapNotifyEvent(window: Long) extends Event
