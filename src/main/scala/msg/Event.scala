package windows.msg

//TODO enum
object Modifier {
  type Modifier = Int
  val Shift = 1
  val Lock = 2
  val Ctrl = 3
  val Mod1 = 4
  val Mod2 = 5
  val Mod3 = 6
  val Mod4 = 7
  val Mod5 = 8

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
