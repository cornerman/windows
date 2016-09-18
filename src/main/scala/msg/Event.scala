package windows.msg

//TODO: structs
//TODO button/key types should not be int

sealed trait Event

case class ButtonPressEvent(window: Int, button: Int) extends Event
case class ButtonReleaseEvent(window: Int, button: Int) extends Event
case class KeyPressEvent(window: Int, key: Int) extends Event
case class KeyReleaseEvent(window: Int, key: Int) extends Event
case class MotionNotifyEvent(window: Int) extends Event
case class MapRequestEvent(window: Int) extends Event
case class MapNotifyEvent(window: Int) extends Event
case class UnmapNotifyEvent(window: Int) extends Event
