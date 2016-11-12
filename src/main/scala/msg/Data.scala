package windows.msg

case class Point(x: Int, y: Int)
case class Rectangle(height: Int, width: Int)

object Log {
  sealed trait Message { val msg: String }
  case class Info(msg: String) extends Message
  case class Warn(msg: String) extends Message
  case class Error(msg: String) extends Message
}

sealed trait Modifier
case object Shift extends Modifier
case object Lock extends Modifier
case object Ctrl extends Modifier
case object Mod1 extends Modifier
case object Mod2 extends Modifier
case object Mod3 extends Modifier
case object Mod4 extends Modifier
case object Mod5 extends Modifier
