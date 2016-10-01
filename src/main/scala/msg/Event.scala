package windows.msg

sealed trait Modifier
case object Shift extends Modifier
case object Lock extends Modifier
case object Ctrl extends Modifier
case object Mod1 extends Modifier
case object Mod2 extends Modifier
case object Mod3 extends Modifier
case object Mod4 extends Modifier
case object Mod5 extends Modifier

//TODO: structs
//TODO button/key types should not be int

case class Point(x: Int, y: Int)

sealed trait Event
case class ButtonPressEvent(window: Long, button: Int, modifiers: Set[Modifier], point: Point) extends Event
case class ButtonReleaseEvent(window: Long, button: Int, modifier: Set[Modifier], point: Point) extends Event
case class KeyPressEvent(window: Long, key: Int, modifier: Set[Modifier]) extends Event
case class KeyReleaseEvent(window: Long, key: Int, modifier: Set[Modifier]) extends Event
case class MotionNotifyEvent(window: Long, point: Point) extends Event
case class MapRequestEvent(window: Long) extends Event
case class MapNotifyEvent(window: Long) extends Event
case class UnmapNotifyEvent(window: Long) extends Event
