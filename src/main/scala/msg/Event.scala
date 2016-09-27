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

object Modifier {
  // TODOset with multiple entries throws exception?
  def values = Array(Shift, Lock, Ctrl, Mod1, Mod2, Mod3, Mod4, Mod5)
}
import Modifier._

//TODO: structs
//TODO button/key types should not be int

sealed trait Event

case class ButtonPressEvent(window: Long, button: Int, modifiers: Set[Modifier], x: Int, y: Int) extends Event
case class ButtonReleaseEvent(window: Long, button: Int, modifier: Set[Modifier], x: Int, y: Int) extends Event
case class KeyPressEvent(window: Long, key: Int, modifier: Set[Modifier]) extends Event
case class KeyReleaseEvent(window: Long, key: Int, modifier: Set[Modifier]) extends Event
case class MotionNotifyEvent(window: Long, x: Int, y: Int) extends Event
case class MapRequestEvent(window: Long) extends Event
case class MapNotifyEvent(window: Long) extends Event
case class UnmapNotifyEvent(window: Long) extends Event
